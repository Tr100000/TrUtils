package io.github.tr100000.trutils.api.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class ItemIconRenderer implements IconRenderer<ItemIcon> {
    public static final ItemIconRenderer INSTANCE = new ItemIconRenderer();

    private ItemIconRenderer() {}

    @Override
    @Environment(EnvType.CLIENT)
    public void draw(ItemIcon icon, GuiGraphicsExtractor graphics, int x, int y) {
        graphics.fakeItem(icon.stack(), x, y);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void drawWithSize(ItemIcon icon, GuiGraphicsExtractor graphics, int x, int y, int size) {
        GuiHelper.fakeItemScaled(graphics, icon.stack(), x, y, size);
    }
}
