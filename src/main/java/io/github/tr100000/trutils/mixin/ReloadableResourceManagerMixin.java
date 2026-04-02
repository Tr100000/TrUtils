package io.github.tr100000.trutils.mixin;

import io.github.tr100000.trutils.TrUtils;
import io.github.tr100000.trutils.api.datagen.RuntimeDatagen;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(ReloadableResourceManager.class)
public abstract class ReloadableResourceManagerMixin {
    @Shadow @Final private PackType type;

    @ModifyVariable(method = "createReload", at = @At("HEAD"), argsOnly = true)
    private List<PackResources> injectPacks(List<PackResources> resourcePacks) {
        TrUtils.LOGGER.debug("Reloadable inject {}", type.name());
        return RuntimeDatagen.injectAllPacks(new ObjectArrayList<>(resourcePacks), type);
    }
}
