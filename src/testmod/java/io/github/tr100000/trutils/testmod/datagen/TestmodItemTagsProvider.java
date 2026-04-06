package io.github.tr100000.trutils.testmod.datagen;

import io.github.tr100000.trutils.testmod.registry.TestmodItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;

import java.util.concurrent.CompletableFuture;

public class TestmodItemTagsProvider extends FabricTagsProvider.ItemTagsProvider {
    public TestmodItemTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        valueLookupBuilder(ItemTags.PICKAXES)
                .add(TestmodItems.WIDE_PICKAXE);
        valueLookupBuilder(ItemTags.AXES)
                .add(TestmodItems.WIDE_AXE);
        valueLookupBuilder(ItemTags.SHOVELS)
                .add(TestmodItems.WIDE_SHOVEL);
        valueLookupBuilder(ItemTags.HOES)
                .add(TestmodItems.WIDE_HOE);
    }
}
