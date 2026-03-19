package io.github.tr100000.trutils.api.datagen;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class BlankDataProvider implements DataProvider {
    protected final FabricPackOutput output;
    protected final PackOutput.PathProvider pathResolver;

    protected BlankDataProvider(FabricPackOutput output, PackOutput.Target type, String directoryName) {
        this.output = output;
        this.pathResolver = output.createPathProvider(type, directoryName);
    }

    protected BlankDataProvider(FabricPackOutput output, ResourceKey<? extends Registry<?>> registryKey) {
        this.output = output;
        this.pathResolver = output.createRegistryElementsPathProvider(registryKey);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput writer) {
        Set<String> ids = new ObjectOpenHashSet<>();
        List<CompletableFuture<?>> list = new ObjectArrayList<>();
        final JsonObject empty = new JsonObject();
        generate(id -> {
            if (!ids.add(id)) {
                throw new IllegalStateException(String.format("Duplicate id: %s", id));
            }
            else {
                list.add(DataProvider.saveStable(writer, empty, pathResolver.json(id(id))));
            }
        });
        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    protected abstract void generate(Consumer<String> consumer);

    protected Identifier id(String path) {
        return (path.contains(":")) ? Identifier.parse(path) : Identifier.fromNamespaceAndPath(output.getModId(), path);
    }

    @Override
    public String getName() {
        return "Empty Files";
    }
}
