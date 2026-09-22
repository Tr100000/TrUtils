package io.github.tr100000.trutils.api.datagen;

import com.google.common.base.MoreObjects;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.github.tr100000.trutils.ClientDelegate;
import io.github.tr100000.trutils.TrUtils;
import io.github.tr100000.trutils.api.utils.Utils;
import io.github.tr100000.trutils.mixin.FabricDataGenHelperAccessor;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.KnownPack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.util.Util;
import org.jetbrains.annotations.ApiStatus;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@ApiStatus.Internal
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

    @SuppressWarnings("UnstableApiUsage")
    public static void runAll() {
        finishedMods.clear();
        generatedPacks.clear();

        try {
            Utils.deleteFolder(ROOT_PATH);
        }
        catch (Exception e) {
            throw new RuntimeDatagenException("Failed to remove old datagen folder", e);
        }

        final List<EntrypointContainer<RuntimeDatagenEntrypoint>> containers = new ObjectArrayList<>();
        for (String key : ClientDelegate.INSTANCE.runtimeDatagenEntrypoints()) {
            containers.addAll(FabricLoader.getInstance().getEntrypointContainers(key, RuntimeDatagenEntrypoint.class));
        }

        final List<DataGeneratorEntrypoint> fabricEntrypoints = containers.stream()
                .map(EntrypointContainer::getEntrypoint)
                .map(DataGeneratorEntrypoint.class::cast)
                .toList();
        CompletableFuture<HolderLookup.Provider> worldRegistriesFuture = CompletableFuture.supplyAsync(() -> FabricDataGenHelperAccessor.invokeCreateWorldLookupProvider(fabricEntrypoints), Util.backgroundExecutor());
        CompletableFuture<HolderLookup.Provider> registriesFuture = worldRegistriesFuture.thenApplyAsync(provider -> FabricDataGenHelperAccessor.invokeCreateReloadableLookupProvider(fabricEntrypoints, provider), Util.backgroundExecutor());

        Object2IntOpenHashMap<String> jsonKeySortOrders = (Object2IntOpenHashMap<String>) DataProvider.FIXED_ORDER_FIELDS;
        Object2IntOpenHashMap<String> defaultJsonKeySortOrders = new Object2IntOpenHashMap<>(jsonKeySortOrders);

        for (EntrypointContainer<RuntimeDatagenEntrypoint> entrypointContainer : containers) {
            RuntimeDatagenEntrypoint entrypoint = entrypointContainer.getEntrypoint();

            String modid = MoreObjects.firstNonNull(entrypoint.getEffectiveModId(), entrypointContainer.getProvider().getMetadata().getId());
            Path outputPath = ROOT_PATH.resolve(modid);

            String entrypointName = getEntrypointName(entrypoint, modid);

            TrUtils.LOGGER.info("Starting runtime datagen for {}", entrypointName);

            try {
                ModContainer mod = FabricLoader.getInstance().getModContainer(modid).orElseThrow(() -> new RuntimeException("Failed to find mod container for mod id %s".formatted(modid)));

                HashSet<String> keys = new HashSet<>();
                entrypoint.addJsonKeySortOrders((key, value) -> {
                    Objects.requireNonNull(key, "Tried to register a priority for a null key");
                    jsonKeySortOrders.put(key, value);
                    keys.add(key);
                });

                FabricDataGenerator generator = new FabricDataGenerator(outputPath, mod, entrypoint.strictValidation(), worldRegistriesFuture, registriesFuture);
                entrypoint.onInitializeDataGenerator(generator);
                generator.run();

                finishedMods.put(mod, entrypoint);
                generatedPacks.put(mod, GeneratedPackResourcesPair.create(mod, entrypoint));

                jsonKeySortOrders.keySet().removeAll(keys);
                jsonKeySortOrders.putAll(defaultJsonKeySortOrders);
            }
            catch (Exception e) {
                throw new RuntimeDatagenException(String.format("Failed to run data generator from mod %s", modid), e);
            }

            TrUtils.LOGGER.info("Finished runtime datagen for {}", entrypointName);
        }
    }

    private static String getEntrypointName(RuntimeDatagenEntrypoint entrypoint, String modid) {
        Identifier id = entrypoint.getId();
        if (id != null) {
            return id.toString();
        } else {
            return modid;
        }
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

    public static Path getPath(ModContainer mod) {
        return ROOT_PATH.resolve(mod.getMetadata().getId());
    }
}
