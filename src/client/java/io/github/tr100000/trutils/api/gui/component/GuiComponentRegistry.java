package io.github.tr100000.trutils.api.gui.component;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

import java.util.Map;

@Environment(EnvType.CLIENT)
public final class GuiComponentRegistry {
    private GuiComponentRegistry() {}

    private static final Map<Identifier, GuiComponentClient.Factory<?>> REGISTRY = new Object2ObjectOpenHashMap<>();

    public static <T extends GuiComponentClient<?>> GuiComponentClient.Factory<T> register(Identifier id, GuiComponentClient.Factory<T> factory) {
        REGISTRY.put(id, factory);
        return factory;
    }

    public static GuiComponentClient.Factory<?> get(Identifier id) {
        return REGISTRY.get(id);
    }
}
