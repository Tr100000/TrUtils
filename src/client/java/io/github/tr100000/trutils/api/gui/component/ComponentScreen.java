package io.github.tr100000.trutils.api.gui.component;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;

public class ComponentScreen<T extends GuiComponentClient<?>, H extends ClientComponentScreenHandler<T>> extends AbstractContainerScreen<H> {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static AbstractContainerScreen<AbstractComponentScreenHandler<?>> create(AbstractComponentScreenHandler<?> handler, Inventory playerInventory, Component title) {
        return (AbstractContainerScreen)new ComponentScreen<>((ClientComponentScreenHandler)handler, playerInventory, title);
    }

    public ComponentScreen(H handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;
        menu.components.forEach(component -> castComponent(component).initScreen(this, leftPos, topPos));
    }

    @SuppressWarnings("unchecked")
    private GuiComponentClient<ComponentScreen<T, H>> castComponent(GuiComponentClient<?> component) {
        return (GuiComponentClient<@NonNull ComponentScreen<T, H>>)component;
    }

    @Override
    public void render(GuiGraphics draw, int mouseX, int mouseY, float delta) {
        renderBg(draw, delta, mouseX, mouseY);
        super.render(draw, mouseX, mouseY, delta);
        drawTooltips(draw, mouseX, mouseY, delta);
        renderTooltip(draw, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics draw, float delta, int mouseX, int mouseY) {
        menu.components.forEach(component -> castComponent(component).drawScreen(this, draw, leftPos, topPos, delta, mouseX, mouseY));
    }

    protected void drawTooltips(GuiGraphics draw, int mouseX, int mouseY, float delta) {
        menu.components.forEach(component -> castComponent(component).postDrawScreen(this, draw, leftPos, topPos, delta, mouseX, mouseY));
    }

    @Override
    public <C extends GuiEventListener & Renderable & NarratableEntry> C addRenderableWidget(C drawableElement) {
        return super.addRenderableWidget(drawableElement);
    }

    @Override
    public <C extends Renderable> C addRenderableOnly(C drawable) {
        return super.addRenderableOnly(drawable);
    }

    @Override
    public <C extends GuiEventListener & NarratableEntry> C addWidget(C child) {
        return super.addWidget(child);
    }

    @Override
    public boolean isHovering(int x, int y, int width, int height, double pointX, double pointY) {
        return super.isHovering(x, y, width, height, pointX, pointY);
    }

    public int getX() {
        return leftPos;
    }

    public int getY() {
        return topPos;
    }

    public int getBackgroundWidth() {
        return imageWidth;
    }

    public int getBackgroundHeight() {
        return imageHeight;
    }
}
