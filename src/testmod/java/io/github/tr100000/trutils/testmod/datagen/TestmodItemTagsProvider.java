package io.github.tr100000.trutils.testmod.datagen;

import io.github.tr100000.trutils.testmod.registry.TestmodItemIds;
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
        builder(ItemTags.PICKAXES)
                .add(TestmodItemIds.WIDE_PICKAXE);
        builder(ItemTags.AXES)
                .add(TestmodItemIds.WIDE_AXE);
        builder(ItemTags.SHOVELS)
                .add(TestmodItemIds.WIDE_SHOVEL);
        builder(ItemTags.HOES)
                .add(TestmodItemIds.WIDE_HOE);
    }
}
