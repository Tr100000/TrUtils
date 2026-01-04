package io.github.tr100000.trutils.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.tr100000.trutils.api.datagen.RuntimeDatagen;
import net.fabricmc.fabric.impl.resource.ServerLanguageUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.locale.Language;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;

@Mixin(value = ServerLanguageUtil.class, remap = false)
public abstract class ServerLanguageUtilMixin {
    @Inject(method = "getModLanguageFiles", at = @At(value = "INVOKE", target = "Ljava/util/Collections;unmodifiableCollection(Ljava/util/Collection;)Ljava/util/Collection;"))
    private static void injectGenerated(CallbackInfoReturnable<Collection<Path>> cir, @Local(name = "paths") Set<Path> paths) {
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            RuntimeDatagen.getEntrypoint(mod).ifPresent(entrypoint -> {
                Path path = RuntimeDatagen.getPath(mod)
                        .resolve("assets")
                        .resolve(mod.getMetadata().getId())
                        .resolve("lang")
                        .resolve(Language.DEFAULT + ".json");
                if (Files.isRegularFile(path)) {
                    paths.add(path);
                }
            });
        }
    }
}
