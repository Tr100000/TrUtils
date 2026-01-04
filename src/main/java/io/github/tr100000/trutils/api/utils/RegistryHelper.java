package io.github.tr100000.trutils.api.utils;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * A helper class for registering items
 * @param <T> the type of thing to be registered
 */
public class RegistryHelper<T> implements Map<T, Identifier> {
    protected final BiConsumer<Identifier, T> registerAction;
    protected final String modid;
    protected final Map<T, Identifier> registeredObjects = new LinkedHashMap<>();
    protected boolean frozen;

    public RegistryHelper(BiConsumer<Identifier, T> registerAction, String modid) {
        Objects.requireNonNull(registerAction, "registerAction must not be null");
        Objects.requireNonNull(modid, "modid must not be null");
        this.registerAction = registerAction;
        this.modid = modid;
    }

    public RegistryHelper(Registry<? super T> registry, String modid) {
        this((id, object) -> Registry.register(registry, id, object), modid);
    }

    public <R extends T> R add(R object, Identifier id) {
        requireNotFrozen();
        registeredObjects.put(object, id);
        registerAction.accept(id, object);
        return object;
    }

    public <R extends T> R add(R object, String name) {
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
    public Identifier get(Object key) {
        return registeredObjects.get(key);
    }

    @Override
    public Identifier put(T key, Identifier value) {
        add(key, value);
        return value;
    }

    @Override
    public Identifier remove(Object key) {
        requireNotFrozen();
        return registeredObjects.remove(key);
    }

    @Override
    public void putAll(Map<? extends T, ? extends Identifier> m) {
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
    public Collection<Identifier> values() {
        return registeredObjects.values();
    }

    @Override
    public Set<Entry<T, Identifier>> entrySet() {
        return registeredObjects.entrySet();
    }
}
