package io.github.tr100000.trutils.testmod.datagen;

import io.github.tr100000.trutils.api.datagen.ExtendedLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class TestmodEnglishLangProvider extends ExtendedLanguageProvider {
    public TestmodEnglishLangProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
        addExistingTranslations("en_us", translationBuilder);
    }
}
