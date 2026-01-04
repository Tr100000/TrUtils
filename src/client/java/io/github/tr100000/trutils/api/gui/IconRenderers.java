package io.github.tr100000.trutils.api.gui;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.client.gui.GuiGraphics;

public final class IconRenderers {
    private IconRenderers() {}

    private static final BiMap<Icon.IconType<?>, IconRenderer<?>> REGISTRY = HashBiMap.create();

    static {
        register(BlankIcon.TYPE, BlankIconRenderer.INSTANCE);
        register(ItemIcon.TYPE, ItemIconRenderer.INSTANCE);
        register(TextureIcon.TYPE, TextureIconRenderer.INSTANCE);
    }

    public static <T extends Icon> void register(Icon.IconType<T> type, IconRenderer<T> renderer) {
        REGISTRY.put(type, renderer);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Icon> IconRenderer<T> fromIcon(T icon) {
        return (IconRenderer<T>)REGISTRY.get(icon.getType());
    }

    public static <T extends Icon> void draw(T icon, GuiGraphics draw, int x, int y) {
        fromIcon(icon).draw(icon, draw, x, y);
    }

    public static <T extends Icon> void draw(T icon, GuiGraphics draw, int x, int y, float delta) {
        fromIcon(icon).draw(icon, draw, x, y, delta);
    }

    public static <T extends Icon> void drawWithSize(T icon, GuiGraphics draw, int x, int y, int size) {
        fromIcon(icon).drawWithSize(icon, draw, x, y, size);
    }

    public static <T extends Icon> void drawWithSize(T icon, GuiGraphics draw, int x, int y, int size, float delta) {
        fromIcon(icon).drawWithSize(icon, draw, x, y, size, delta);
    }
}
