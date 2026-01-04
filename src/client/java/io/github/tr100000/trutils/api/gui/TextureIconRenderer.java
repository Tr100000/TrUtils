package io.github.tr100000.trutils.api.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;

public final class TextureIconRenderer implements IconRenderer<TextureIcon> {
    public static final TextureIconRenderer INSTANCE = new TextureIconRenderer();

    private TextureIconRenderer() {}

    @Override
    public void draw(TextureIcon icon, GuiGraphics draw, int x, int y) {
        drawWithSize(icon, draw, x, y, 16);
    }

    @Override
    public void drawWithSize(TextureIcon icon, GuiGraphics draw, int x, int y, int size) {
        float scale = (float)size / icon.textureSize();
        draw.pose().pushMatrix();
        draw.pose().translate(x, y);
        draw.pose().scale(scale, scale);
        draw.blit(RenderPipelines.GUI_TEXTURED, icon.texture(), 0, 0, 0, 0, icon.textureSize(), icon.textureSize(), icon.textureSize(), icon.textureSize());
        draw.pose().popMatrix();
    }
}
