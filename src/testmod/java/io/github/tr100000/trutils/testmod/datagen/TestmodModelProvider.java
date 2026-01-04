package io.github.tr100000.trutils.testmod.datagen;

import io.github.tr100000.trutils.testmod.registry.TestmodItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;

public class TestmodModelProvider extends FabricModelProvider {
    public TestmodModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators models) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators models) {
        models.generateFlatItem(TestmodItems.WIDE_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
        models.generateFlatItem(TestmodItems.WIDE_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
        models.generateFlatItem(TestmodItems.WIDE_SHOVEL, ModelTemplates.FLAT_HANDHELD_ITEM);
        models.generateFlatItem(TestmodItems.WIDE_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);
    }
}
