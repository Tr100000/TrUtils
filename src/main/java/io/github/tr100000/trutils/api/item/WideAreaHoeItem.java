package io.github.tr100000.trutils.api.item;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class WideAreaHoeItem extends HoeItem implements WideAreaTool {
    private final int breakRadius;
    private final int depth;

    public WideAreaHoeItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties settings, int breakRadius) {
        this(material, attackDamage, attackSpeed, settings, breakRadius, 1);
    }

    public WideAreaHoeItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties settings, int breakRadius, int depth) {
        super(material, attackDamage, attackSpeed, settings);
        this.breakRadius = breakRadius;
        this.depth = depth;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return useOnAllBlocks(context, super::useOn);
    }

    @Override
    public List<BlockPos> findBlocksToBreak(BlockGetter world, Player player, int radius, int depth) {
        List<BlockPos> blocksToBreak = new ObjectArrayList<>();

        Vec3 cameraPos = player.getEyePosition(1);
        Vec3 rotation = player.getViewVector(1);
        double reachDistance = player.blockInteractionRange();
        Vec3 combined = cameraPos.add(rotation.x * reachDistance, rotation.y * reachDistance, rotation.z * reachDistance);

        BlockHitResult blockHitResult = world.clip(new ClipContext(cameraPos, combined, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));

        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos origin = blockHitResult.getBlockPos();
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    blocksToBreak.add(origin.offset(x, 0, z));
                }
            }
        }

        return blocksToBreak;
    }

    @Override
    public int getBreakRadius(ItemStack stack) {
        return breakRadius;
    }

    @Override
    public int getDepth(ItemStack stack) {
        return depth;
    }
}
