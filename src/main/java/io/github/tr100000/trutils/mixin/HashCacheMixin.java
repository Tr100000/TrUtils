package io.github.tr100000.trutils.mixin;

import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.minecraft.data.HashCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;

@Mixin(value = HashCache.class)
public abstract class HashCacheMixin {
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/nio/file/Files;createDirectories(Ljava/nio/file/Path;[Ljava/nio/file/attribute/FileAttribute;)Ljava/nio/file/Path;"))
    private Path dontCreateDirectories(Path dir, FileAttribute<?>... attrs) throws IOException {
        if (isRuntimeDatagen()) {
            return dir;
        }
        else {
            return Files.createDirectories(dir, attrs);
        }
    }

    @Inject(method = "applyUpdate", at = @At("HEAD"), cancellable = true)
    private void dontUpdate(HashCache.UpdateResult result, CallbackInfo ci) {
        if (isRuntimeDatagen()) {
            ci.cancel();
        }
    }

    @Inject(method = "purgeStaleAndWrite", at = @At("HEAD"), cancellable = true)
    private void dontWrite(CallbackInfo ci) {
        if (isRuntimeDatagen()) {
            ci.cancel();
        }
    }

    @Unique
    @SuppressWarnings("UnstableApiUsage")
    private boolean isRuntimeDatagen() {
        return !FabricDataGenHelper.ENABLED;
    }
}
