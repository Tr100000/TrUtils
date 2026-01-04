package io.github.tr100000.trutils.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.tr100000.trutils.api.item.WideAreaTool;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.state.BlockOutlineRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Shadow @Final private Minecraft minecraft;

    @Inject(at = @At("HEAD"), method = "renderHitOutline", cancellable = true)
    private void drawBlockOutline(PoseStack matrices, VertexConsumer vertexConsumer, double x, double y, double z, BlockOutlineRenderState state, int color, float lineWidth, CallbackInfo ci) {
        if (minecraft.player == null || minecraft.level == null) {
            return;
        }

        ItemStack stack = this.minecraft.player.getMainHandItem();
        if (stack.getItem() instanceof WideAreaTool tool && minecraft.hitResult instanceof BlockHitResult crosshairTarget) {
            BlockPos crosshairPos = crosshairTarget.getBlockPos();
            BlockState crosshairState = minecraft.level.getBlockState(crosshairPos);

            if (!crosshairState.isAir() && minecraft.level.getWorldBorder().isWithinBounds(crosshairPos) && tool.isBlockValidForBreaking(minecraft.level, crosshairPos, stack)) {
                int radius = tool.getBreakRadius(stack);
                List<BlockPos> positions = tool.findBlocksToBreak(minecraft.level, minecraft.player, radius, tool.getDepth(stack));
                List<VoxelShape> outlineShapes = new ObjectArrayList<>();
                outlineShapes.add(Shapes.empty());

                for (BlockPos position : positions) {
                    if (!minecraft.player.isCreative() && !tool.isBlockValidForBreaking(minecraft.level, position, stack)) {
                        continue;
                    }

                    BlockPos diffPos = position.subtract(crosshairPos);
                    BlockState offsetShape = minecraft.level.getBlockState(position);

                    if (!offsetShape.isAir()) {
                        outlineShapes.set(0, Shapes.or(outlineShapes.getFirst(), offsetShape.getShape(minecraft.level, position).move(diffPos.getX(), diffPos.getY(), diffPos.getZ())));
                    }
                }

                outlineShapes.forEach(shape -> ShapeRenderer.renderShape(
                        matrices,
                        vertexConsumer,
                        shape,
                        state.pos().getX() - x,
                        state.pos().getY() - y,
                        state.pos().getZ() - z,
                        color,
                        lineWidth
                ));

                ci.cancel();
            }
        }
    }
}
