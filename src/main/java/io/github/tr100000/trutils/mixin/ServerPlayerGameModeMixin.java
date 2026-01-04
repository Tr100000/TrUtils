package io.github.tr100000.trutils.mixin;

import io.github.tr100000.trutils.api.item.TrUtilsServerPlayerGameModeExtensions;
import io.github.tr100000.trutils.api.item.WideAreaTool;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public abstract class ServerPlayerGameModeMixin implements TrUtilsServerPlayerGameModeExtensions {
    @Shadow @Final protected ServerPlayer player;
    @Shadow protected ServerLevel level;
    @Unique private boolean trutils_isMining;

    @Override
    public void trutils_setIsMining(boolean isMining) {
        this.trutils_isMining = isMining;
    }

    @Inject(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;playerWillDestroy(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/level/block/state/BlockState;"), cancellable = true)
    private void tryBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        ItemStack heldStack = player.getMainHandItem();
        if (heldStack.getItem() instanceof WideAreaTool tool) {
            boolean didBreak = trutils_isMining || tool.tryBreakBlocks(level, pos, player);
            if (didBreak) {
                cir.setReturnValue(true);
            }
        }
    }
}
