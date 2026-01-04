package io.github.tr100000.trutils.mixin;

import io.github.tr100000.trutils.TrUtils;
import io.github.tr100000.trutils.api.datagen.RuntimeDatagen;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(MultiPackResourceManager.class)
public abstract class MultiPackResourceManagerMixin {
    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
    private static List<PackResources> modifyPacks(List<PackResources> packs, PackType type) {
        TrUtils.LOGGER.debug("Lifecycled inject {}", type.name());
        return RuntimeDatagen.injectAllPacks(new ObjectArrayList<>(packs), type);
    }
}
