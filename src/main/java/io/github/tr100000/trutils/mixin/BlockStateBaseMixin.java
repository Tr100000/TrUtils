package io.github.tr100000.trutils.mixin;

import io.github.tr100000.trutils.api.item.WideAreaTool;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {
    @Inject(method = "getDestroyProgress", at = @At("HEAD"), cancellable = true)
    private void calcBlockBreakingDelta(Player player, BlockGetter world, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof WideAreaTool tool && !player.isShiftKeyDown()) {
            cir.setReturnValue(tool.findBlocksToBreak(world, player, tool.getBreakRadius(stack), tool.getDepth(stack)).stream()
                    .<Float>mapMulti((pos2, consumer) -> {
                        BlockState state = world.getBlockState(pos2);
                        if (tool.isBlockValidForBreaking(world, pos, stack)) {
                            consumer.accept(state.getBlock().getDestroyProgress(state, player, world, pos2) * tool.miningSpeedMultiplier(stack));
                        }
                    })
                    .min(Float::compare).orElse(0.0F));
        }
    }
}
