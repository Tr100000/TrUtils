package io.github.tr100000.trutils.mixin;

import io.github.tr100000.trutils.api.item.WideAreaTool;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockTransformer.class)
public abstract class BlockTransformerMixin {
    @Unique
    private boolean isTransformingWithWideTool;

    @Inject(method = "transformBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/context/UseOnContext;getClickedPos()Lnet/minecraft/core/BlockPos;", ordinal = 0), cancellable = true)
    private void transformBlocksWithWideTool(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (!isTransformingWithWideTool && context.getItemInHand().getItem() instanceof WideAreaTool wideAreaTool) {
            isTransformingWithWideTool = true;
            InteractionResult result = wideAreaTool.useOnAllBlocks(context, ((BlockTransformer)(Object)this)::transformBlock);
            isTransformingWithWideTool = false;
            cir.setReturnValue(result);
        }
    }
}
