package io.github.tr100000.trutils.api.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;

public final class ItemIconRenderer implements IconRenderer<ItemIcon> {
    public static final ItemIconRenderer INSTANCE = new ItemIconRenderer();

    private ItemIconRenderer() {}

    @Override
    @Environment(EnvType.CLIENT)
    public void draw(ItemIcon icon, GuiGraphics draw, int x, int y) {
        draw.renderFakeItem(icon.stack(), x, y);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void drawWithSize(ItemIcon icon, GuiGraphics draw, int x, int y, int size) {
        GuiHelper.drawScaledItemWithoutEntity(draw, icon.stack(), x, y, size);
    }
}
