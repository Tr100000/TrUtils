package io.github.tr100000.trutils.api.datagen;

import com.google.common.base.MoreObjects;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.github.tr100000.trutils.ClientDelegate;
import io.github.tr100000.trutils.TrUtils;
import io.github.tr100000.trutils.api.utils.Utils;
import io.github.tr100000.trutils.mixin.FabricDataGenHelperAccessor;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.KnownPack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.util.Util;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class RuntimeDatagen {
    private RuntimeDatagen() {}

    public static final String ENTRYPOINT_KEY_MAIN = "trutils:datagen";
    public static final String ENTRYPOINT_KEY_CLIENT = "trutils:datagen/client";

    public static final Path ROOT_PATH = TrUtils.RUNTIME_DATAGEN_PATH;
    public static final PackSource PACK_SOURCE = PackSource.create(name -> {
        Component text = Component.translatable("pack.source.generated");
        return Component.translatable("pack.nameAndSource", name, text).withStyle(ChatFormatting.GRAY);
    }, false);

    private static final Multimap<ModContainer, RuntimeDatagenEntrypoint> finishedMods = HashMultimap.create();
    private static final Multimap<ModContainer, GeneratedPackResourcesPair> generatedPacks = HashMultimap.create();

    public static void runAll() {
        finishedMods.clear();
        generatedPacks.clear();

        try {
            Utils.deleteFolder(ROOT_PATH);
        }
        catch (Exception e) {
            throw new RuntimeDatagenException("Failed to remove old datagen folder", e);
        }

        List<EntrypointContainer<RuntimeDatagenEntrypoint>> entrypoints = new ObjectArrayList<>();
        for (String key : ClientDelegate.INSTANCE.runtimeDatagenEntrypoints()) {
            entrypoints.addAll(FabricLoader.getInstance().getEntrypointContainers(key, RuntimeDatagenEntrypoint.class));
        }
        entrypoints.forEach(e -> run(e.getEntrypoint(), e.getProvider().getMetadata().getId()));
    }

    private static String getEntrypointName(RuntimeDatagenEntrypoint entrypoint, String modid) {
        Identifier id = entrypoint.getId();
        if (id != null) {
            return id.toString();
        }
        else {
            return modid;
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    public static void run(RuntimeDatagenEntrypoint entrypoint, String modid) {
        modid = MoreObjects.firstNonNull(entrypoint.getEffectiveModId(), modid);
        Path outputPath = ROOT_PATH.resolve(modid);

        String entrypointName = getEntrypointName(entrypoint, modid);

        TrUtils.LOGGER.info("Starting runtime datagen for {}", entrypointName);
        try {
            ModContainer mod = FabricLoader.getInstance().getModContainer(modid).orElseThrow();
            CompletableFuture<HolderLookup.Provider> registriesFuture = CompletableFuture.supplyAsync(() -> FabricDataGenHelperAccessor.invokeCreateHolderLookupProvider(List.of(entrypoint)), Util.backgroundExecutor());
            FabricDataGenerator generator = new FabricDataGenerator(outputPath, mod, entrypoint.strictValidation(), registriesFuture);
            entrypoint.onInitializeDataGenerator(generator);
            generator.run();
            finishedMods.put(mod, entrypoint);
            generatedPacks.put(mod, GeneratedPackResourcesPair.create(mod, entrypoint));
        }
        catch (Exception e) {
            throw new RuntimeDatagenException(String.format("Runtime datagen failed for %s!", modid), e);
        }
        TrUtils.LOGGER.info("Finished runtime datagen for {}", entrypointName);
    }

    public static List<PackResources> injectAllPacks(List<PackResources> packs, PackType type) {
        for (ModContainer mod : finishedMods.keySet()) {
            Collection<GeneratedPackResourcesPair> generatedPacks = RuntimeDatagen.generatedPacks.get(mod);
            for (GeneratedPackResourcesPair pair : generatedPacks) {
                if (pair.hasPack(type)) {
                    injectPack(packs, pair.getPack(type));
                }
            }
        }
        return packs;
    }

    public static void injectPack(List<PackResources> packs, GeneratedPackResources pack) {
        if (packs.contains(pack) || packs.isEmpty()) return;

        TrUtils.LOGGER.info("Injecting pack for {}", pack.getMod().getMetadata().getId());

        for (int i = 0; i < packs.size(); i++) {
            if (packs.get(i).packId().equals("fabric")) {
                packs.add(i + 1, pack);
                return;
            }
        }
        for (int i = 0; i < packs.size(); i++) {
            if (packs.get(i).packId().equals("vanilla")) {
                packs.add(i + 1, pack);
                return;
            }
        }
        packs.add(pack);
    }

    public static void forEachPack(PackType type, Consumer<GeneratedPackResources> packConsumer) {
        for (GeneratedPackResourcesPair pair : generatedPacks.values()) {
            if (pair.hasPack(type)) {
                packConsumer.accept(pair.getPack(type));
            }
        }
    }

    public static GeneratedPackResources createPack(ModContainer mod, PackType type) {
        return new GeneratedPackResources(mod, type);
    }

    public static PackLocationInfo getPackInfo(ModContainer mod, PackType type) {
        String packId = String.format("generated_%s/%s", type.getDirectory(), mod.getMetadata().getId());
        Component displayName = Component.translatable("pack.generated." + type.getDirectory(), mod.getMetadata().getName());
        return new PackLocationInfo(packId, displayName, PACK_SOURCE, Optional.of(new KnownPack(mod.getMetadata().getId(), packId, mod.getMetadata().getVersion().getFriendlyString())));
    }

    public static boolean needsClientPack() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    public static Collection<RuntimeDatagenEntrypoint> getEntrypoints(ModContainer mod) {
        return finishedMods.get(mod);
    }

    @Deprecated(forRemoval = true, since = "0.3.0")
    public static Optional<RuntimeDatagenEntrypoint> getEntrypoint(ModContainer mod) {
        return getEntrypoints(mod).stream().findFirst();
    }

    public static Path getPath(ModContainer mod) {
        return ROOT_PATH.resolve(mod.getMetadata().getId());
    }
}
