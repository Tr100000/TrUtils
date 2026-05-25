package io.github.tr100000.trutils.api.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.core.HolderLookup;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public abstract class ExtendedLanguageProvider extends FabricLanguageProvider {
    protected ExtendedLanguageProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(packOutput, registryLookup);
    }

    protected ExtendedLanguageProvider(FabricPackOutput packOutput, String languageCode, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(packOutput, languageCode, registryLookup);
    }

    protected void addExistingTranslations(String langCode, TranslationBuilder translationBuilder) {
        ModContainer mod = packOutput.getModContainer();
        String pathStr = String.format("assets/%s/lang/%s.existing.json", mod.getMetadata().getId(), langCode);
        addExistingTranslations(mod.findPath(pathStr).orElseThrow(), translationBuilder);
    }

    protected void addExistingTranslations(Path path, TranslationBuilder translationBuilder) {
        try {
            translationBuilder.add(path);
        }
        catch (IOException e) {
            throw new IllegalArgumentException("Failed to load existing language data!", e);
        }
    }
}
