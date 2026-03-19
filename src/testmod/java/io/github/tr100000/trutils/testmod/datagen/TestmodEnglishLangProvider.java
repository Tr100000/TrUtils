package io.github.tr100000.trutils.testmod.datagen;

import io.github.tr100000.trutils.testmod.registry.TestmodItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class TestmodEnglishLangProvider extends FabricLanguageProvider {
    public TestmodEnglishLangProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(TestmodItems.WIDE_PICKAXE, "Wide Area Pickaxe");
        translationBuilder.add(TestmodItems.WIDE_AXE, "Wide Area Axe");
        translationBuilder.add(TestmodItems.WIDE_SHOVEL, "Wide Area Shovel");
        translationBuilder.add(TestmodItems.WIDE_HOE, "Wide Area Hoe");
    }
}
