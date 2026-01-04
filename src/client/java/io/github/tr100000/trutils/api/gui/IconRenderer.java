package io.github.tr100000.trutils.api.gui;

import net.minecraft.client.gui.GuiGraphics;

public interface IconRenderer<T extends Icon> {
    default void draw(T icon, GuiGraphics draw, int x, int y) {
        draw(icon, draw, x, y, 0);
    }

    default void draw(T icon, GuiGraphics draw, int x, int y, float delta) {
        draw(icon, draw, x, y);
    }

    default void drawWithSize(T icon, GuiGraphics draw, int x, int y, int size) {
        drawWithSize(icon, draw, x, y, size, 0);
    }

    default void drawWithSize(T icon, GuiGraphics draw, int x, int y, int size, float delta) {
        drawWithSize(icon, draw, x, y, size);
    }
}
