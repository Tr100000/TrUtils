package io.github.tr100000.trutils.mixin;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.minecraft.core.HolderLookup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
@Mixin(FabricDataGenHelper.class)
public interface FabricDataGenHelperAccessor {
    @Invoker("createWorldLookupProvider")
    static HolderLookup.Provider invokeCreateWorldLookupProvider(List<DataGeneratorEntrypoint> dataGeneratorEntrypoints) {
        throw new AssertionError();
    }

    @Invoker("createReloadableLookupProvider")
    static HolderLookup.Provider invokeCreateReloadableLookupProvider(List<DataGeneratorEntrypoint> dataGeneratorInitializers, HolderLookup.Provider registryLookup) {
        throw new AssertionError();
    }
}
