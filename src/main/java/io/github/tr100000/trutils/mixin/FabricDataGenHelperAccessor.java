package io.github.tr100000.trutils.mixin;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.minecraft.core.HolderLookup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
@Mixin(value = FabricDataGenHelper.class, remap = false)
public interface FabricDataGenHelperAccessor {
    @Invoker("createHolderLookupProvider")
    static HolderLookup.Provider invokeCreateHolderLookupProvider(List<DataGeneratorEntrypoint> dataGeneratorEntrypoints) {
        throw new AssertionError();
    }
}
