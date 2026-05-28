package io.github.tr100000.trutils.api.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * A helper class for registering items
 * @param <T> the type of thing to be registered
 */
public class RegistryHelper<T> implements Map<T, ResourceKey<T>> {
    protected final Function<Identifier, ResourceKey<T>> toResourceKey;
    protected final BiConsumer<ResourceKey<T>, T> registerAction;
    protected final String modid;
    protected final Map<T, ResourceKey<T>> registeredObjects = new LinkedHashMap<>();
    protected boolean frozen;

    public RegistryHelper(BiConsumer<ResourceKey<T>, T> registerAction, Function<Identifier, ResourceKey<T>> toResourceKey, String modid) {
        Objects.requireNonNull(registerAction, "registerAction is null");
        Objects.requireNonNull(toResourceKey, "toResourceKey is null");
        Objects.requireNonNull(modid, "modid is null");
        this.toResourceKey = toResourceKey;
        this.registerAction = registerAction;
        this.modid = modid;
    }

    public RegistryHelper(Registry<T> registry, String modid) {
        this((id, object) -> Registry.register(registry, id, object), id -> ResourceKey.create(registry.key(), id), modid);
    }

    public <V extends T> V add(V object, ResourceKey<T> key) {
        Objects.requireNonNull(object, "object is null");
        Objects.requireNonNull(key, "key is null");
        requireNotFrozen();
        registeredObjects.put(object, key);
        registerAction.accept(key, object);
        return object;
    }

    public <V extends T> V add(V object, Identifier id) {
        return add(object, toResourceKey.apply(id));
    }

    public <V extends T> V add(V object, String name) {
        Objects.requireNonNull(name, "name is null");
        return add(object, Identifier.fromNamespaceAndPath(modid, name));
    }

    public void register() {
        requireNotFrozen();
        frozen = true;
    }

    public void requireNotFrozen() {
        if (frozen) {
            throw new IllegalStateException("This RegistryHelper is already frozen!");
        }
    }

    @Override
    public int size() {
        return registeredObjects.size();
    }

    @Override
    public boolean isEmpty() {
        return registeredObjects.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return registeredObjects.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return registeredObjects.containsValue(value);
    }

    @Override
    public ResourceKey<T> get(Object key) {
        return registeredObjects.get(key);
    }

    @Override
    public ResourceKey<T> put(T key, ResourceKey<T> value) {
        add(key, value);
        return value;
    }

    @Override
    public ResourceKey<T> remove(Object key) {
        requireNotFrozen();
        return registeredObjects.remove(key);
    }

    @Override
    public void putAll(Map<? extends T, ? extends ResourceKey<T>> m) {
        m.forEach(this::add);
    }

    @Override
    public void clear() {
        requireNotFrozen();
        registeredObjects.clear();
    }

    @Override
    public Set<T> keySet() {
        return registeredObjects.keySet();
    }

    @Override
    public Collection<ResourceKey<T>> values() {
        return registeredObjects.values();
    }

    @Override
    public Set<Entry<T, ResourceKey<T>>> entrySet() {
        return registeredObjects.entrySet();
    }
}
