package io.github.tr100000.trutils.api.item;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Function;

public interface WideAreaTool {
    int getBreakRadius(ItemStack stack);

    default int getDepth(ItemStack stack) {
        return 1;
    }

    default float miningSpeedMultiplier(ItemStack stack) {
        return 0.5F;
    }

    default boolean isBlockValidForBreaking(BlockGetter world, BlockPos pos, ItemStack stack) {
        BlockState state = world.getBlockState(pos);

        if (state.getDestroySpeed(world, pos) == -1.0) return false;
        if (stack.isCorrectToolForDrops(state)) return true;
        if (state.requiresCorrectToolForDrops()) return false;

        return stack.getDestroySpeed(state) >= 1.0F;
    }

    default boolean isBlockValidForBreaking(Player player, BlockGetter world, BlockPos pos, ItemStack stack) {
        return player.isCreative() || isBlockValidForBreaking(world, pos, stack);
    }

    default boolean tryBreakBlocks(Level world, BlockPos pos, Player player) {
        ItemStack stack = player.getMainHandItem();
        if (player.isShiftKeyDown()) return false;

        if (isBlockValidForBreaking(player, world, pos, stack)) {
            breakBlocks(world, player, stack);
            return true;
        }

        return false;
    }

    default List<BlockPos> findBlocksToBreak(BlockGetter world, Player player, int breakRadius, int depth) {
        List<BlockPos> blocksToBreak = new ObjectArrayList<>();

        Vec3 cameraPos = player.getEyePosition(1);
        Vec3 rotation = player.getViewVector(1);
        double reachDistance = player.blockInteractionRange();
        Vec3 combined = cameraPos.add(rotation.x * reachDistance, rotation.y * reachDistance, rotation.z * reachDistance);

        BlockHitResult blockHitResult = world.clip(new ClipContext(cameraPos, combined, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));

        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            Direction.Axis axis = blockHitResult.getDirection().getAxis();
            List<Vec3i> positions = new ObjectArrayList<>();

            for (int x = -breakRadius; x <= breakRadius; x++) {
                for (int y = -breakRadius; y <= breakRadius; y++) {
                    for (int z = -breakRadius; z <= breakRadius; z++) {
                        positions.add(new Vec3i(x, y, z));
                    }
                }
            }

            BlockPos origin = blockHitResult.getBlockPos();

            for (Vec3i pos : positions) {
                boolean valid = false;

                if(axis == Direction.Axis.Y) {
                    if (pos.getY() == 0) {
                        blocksToBreak.add(origin.offset(pos));
                        valid = true;
                    }
                }
                else if (axis == Direction.Axis.X) {
                    if (pos.getX() == 0) {
                        blocksToBreak.add(origin.offset(pos));
                        valid = true;
                    }
                }
                else if (axis == Direction.Axis.Z) {
                    if (pos.getZ() == 0) {
                        blocksToBreak.add(origin.offset(pos));
                        valid = true;
                    }
                }

                if (valid) {
                    for (int i = 1; i < depth; i++) {
                        Vec3i vec = blockHitResult.getDirection().getOpposite().getUnitVec3i();
                        blocksToBreak.add(origin.offset(pos).offset(vec.getX() * i, vec.getY() * i, vec.getZ() * i));
                    }
                }
            }
        }

        return blocksToBreak;
    }

    default void breakBlocks(Level world, Player player, ItemStack stack) {
        if (!world.isClientSide()) {
            ServerPlayerGameMode interactionManager = ((ServerPlayer)player).gameMode;
            interactionManager.trutils_setIsMining(true);

            WideAreaTool tool = (WideAreaTool)stack.getItem();
            List<BlockPos> blocksToBreak = findBlocksToBreak(world, player, tool.getBreakRadius(stack), tool.getDepth(stack));
            for (BlockPos brokenPos : blocksToBreak) {
                BlockState state = world.getBlockState(brokenPos);
                BlockEntity blockEntity = world.getBlockState(brokenPos).hasBlockEntity() ? world.getBlockEntity(brokenPos) : null;

                if (player.isCreative() || isBlockValidForBreaking(world, brokenPos, stack) && !state.isAir()) {
                    BlockState newState = state.getBlock().playerWillDestroy(world, brokenPos, state, player);
                    if (!interactionManager.destroyBlock(brokenPos)) {
                        continue;
                    }

                    if (!PlayerBlockBreakEvents.BEFORE.invoker().beforeBlockBreak(world, player, brokenPos, state, blockEntity)) {
                        PlayerBlockBreakEvents.CANCELED.invoker().onBlockBreakCanceled(world, player, brokenPos, state, blockEntity);
                        continue;
                    }

                    boolean didRemoveBlock = world.removeBlock(brokenPos, false);
                    if (didRemoveBlock) {
                        state.getBlock().destroy(world, brokenPos, state);
                        PlayerBlockBreakEvents.AFTER.invoker().afterBlockBreak(world, player, brokenPos, state, blockEntity);
                    }

                    if (!player.preventsBlockDrops()) {
                        boolean usingEffectiveTool = player.hasCorrectToolForDrops(state);
                        ItemStack copiedStack = stack.copy();
                        stack.mineBlock(world, state, brokenPos, player);
                        if (didRemoveBlock && usingEffectiveTool) {
                            state.getBlock().playerDestroy(world, player, brokenPos, newState, blockEntity, copiedStack);
                            player.awardStat(Stats.BLOCK_MINED.get(state.getBlock()));
                            player.causeFoodExhaustion(0.005F);
                        }

                        if (stack.getCount() == 0) break;
                    }
                }
            }

            interactionManager.trutils_setIsMining(false);
        }
    }

    default InteractionResult useOnAllBlocks(UseOnContext context, Function<UseOnContext, InteractionResult> useOnBlock) {
        Player player = context.getPlayer();
        assert player != null;
        if (player.isShiftKeyDown()) {
            return useOnBlock.apply(context);
        }
        Level world = context.getLevel();
        ItemStack stack = context.getItemInHand();
        InteractionResult result = InteractionResult.PASS;
        for (BlockPos pos : findBlocksToBreak(world, player, getBreakRadius(stack), getDepth(stack))) {
            BlockHitResult posHit = new BlockHitResult(context.getClickLocation(), context.getClickedFace(), pos, context.isInside(), false);
            UseOnContext posContext = new UseOnContext(world, player, context.getHand(), context.getItemInHand(), posHit);
            if (useOnBlock.apply(posContext) == InteractionResult.SUCCESS) {
                result = InteractionResult.SUCCESS;
            }
        }
        return result;
    }

    static ToolType createPickaxeType(WideAreaSpecificToolFactory<?> factory, int breakRadius, int depth) {
        return (material, attackDamage, attackSpeed, settings) -> factory.apply(material, attackDamage, attackSpeed, settings, breakRadius, depth);
    }

    static ToolType createPickaxeType(WideAreaToolFactory<?> factory, int breakRadius, int depth) {
        return createPickaxeType((material, attackDamage, attackSpeed, settings, breakRadius2, depth2) -> factory.apply(settings.pickaxe(material, attackDamage, attackSpeed), breakRadius2, depth2), breakRadius, depth);
    }

    static ToolType createPickaxeType(int breakRadius, int depth) {
        return createPickaxeType(WideAreaToolItem::new, breakRadius, depth);
    }

    static ToolType createAxeType(WideAreaSpecificToolFactory<?> factory, int breakRadius, int depth) {
        return (material, attackDamage, attackSpeed, settings) -> factory.apply(material, attackDamage, attackSpeed, settings, breakRadius, depth);
    }

    static ToolType createAxeType(int breakRadius, int depth) {
        return createAxeType(WideAreaAxeItem::new, breakRadius, depth);
    }

    static ToolType createShovelType(WideAreaSpecificToolFactory<?> factory, int breakRadius, int depth) {
        return (material, attackDamage, attackSpeed, settings) -> factory.apply(material, attackDamage, attackSpeed, settings, breakRadius, depth);
    }

    static ToolType createShovelType(int breakRadius, int depth) {
        return createShovelType(WideAreaShovelItem::new, breakRadius, depth);
    }

    static ToolType createHoeType(WideAreaSpecificToolFactory<?> factory, int breakRadius, int depth) {
        return (material, attackDamage, attackSpeed, settings) -> factory.apply(material, attackDamage, attackSpeed, settings, breakRadius, depth);
    }

    static ToolType createHoeType(int breakRadius, int depth) {
        return createHoeType(WideAreaHoeItem::new, breakRadius, depth);
    }
}
