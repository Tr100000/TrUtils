package io.github.tr100000.trutils.mixin;

import io.github.tr100000.trutils.api.datagen.RuntimeDatagen;
import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltInRegistries.class)
public abstract class BuiltInRegistriesMixin {
    @Inject(method = "bootStrap", at = @At("TAIL"))
    private static void afterBootStrap(CallbackInfo ci) {
        RuntimeDatagen.runAll();
    }
}
