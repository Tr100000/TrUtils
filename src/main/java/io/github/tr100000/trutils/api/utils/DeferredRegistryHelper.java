package io.github.tr100000.trutils.api.utils;

import io.github.tr100000.trutils.api.registry.RegistryHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * A subclass of {@link RegistryHelper} that doesn't register things for a while
 * @param <T> the type of thing to be registered
 */
public class DeferredRegistryHelper<T> extends RegistryHelper<T> {
    public DeferredRegistryHelper(BiConsumer<ResourceKey<T>, T> registerAction, Function<Identifier, ResourceKey<T>> toResourceKey, String modid) {
        super(registerAction, toResourceKey, modid);
    }

    public DeferredRegistryHelper(Registry<T> registry, String modid) {
        super(registry, modid);
    }

    @Override
    public <R extends T> R add(R object, ResourceKey<T> key) {
        requireNotFrozen();
        registeredObjects.put(object, key);
        return object;
    }

    @Override
    public void register() {
        registeredObjects.forEach((object, id) -> registerAction.accept(id, object));
        super.register();
    }
}
