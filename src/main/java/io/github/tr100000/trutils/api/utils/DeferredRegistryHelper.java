package io.github.tr100000.trutils.api.utils;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

import java.util.function.BiConsumer;

/**
 * A subclass of {@link RegistryHelper} that doesn't register things for a while
 * @param <T> the type of thing to be registered
 */
public class DeferredRegistryHelper<T> extends RegistryHelper<T> {
    public DeferredRegistryHelper(BiConsumer<Identifier, T> registerAction, String modid) {
        super(registerAction, modid);
    }

    public DeferredRegistryHelper(Registry<? super T> registry, String modid) {
        super(registry, modid);
    }

    @Override
    public <R extends T> R add(R object, Identifier id) {
        requireNotFrozen();
        registeredObjects.put(object, id);
        return object;
    }

    @Override
    public void register() {
        registeredObjects.forEach((object, id) -> registerAction.accept(id, object));
        super.register();
    }
}
