package io.github.tr100000.trutils.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.tr100000.trutils.api.item.WideAreaTool;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Unique
    private static final Minecraft minecraft = Minecraft.getInstance();

    @Inject(at = @At("HEAD"), method = "submitHitOutline", cancellable = true)
    private void submitHitOutline(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, RenderType renderType, BlockOutlineRenderState state, int color, float width, boolean afterTerrain, CallbackInfo ci) {
        if (minecraft.player == null || minecraft.level == null) {
            return;
        }

        ItemStack stack = minecraft.player.getMainHandItem();
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

                for (VoxelShape shape : outlineShapes) {
                    submitNodeCollector.submitShapeOutline(poseStack, shape, renderType, color, width, afterTerrain);
                }

                ci.cancel();
            }
        }
    }
}
