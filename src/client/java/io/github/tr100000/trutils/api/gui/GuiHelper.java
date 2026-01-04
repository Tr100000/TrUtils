package io.github.tr100000.trutils.api.gui;

import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.MenuTooltipPositioner;
import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.joml.Matrix3x2f;

import java.util.List;

public final class GuiHelper {
    private static final Minecraft client = Minecraft.getInstance();

    private GuiHelper() {}

    public static boolean isMouseTouching(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    public static boolean isMouseTouching(int x, int y, int width, int height, double mouseX, double mouseY) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    public static boolean isMouseTouching(LayoutElement widget, int mouseX, int mouseY) {
        return isMouseTouching(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight(), mouseX, mouseY);
    }

    public static boolean isMouseTouching(int x, int y, int width, int height) {
        return isMouseTouching(x, y, width, height, getMouseX(), getMouseY());
    }

    public static int getMouseX() {
        return Mth.floor(client.mouseHandler.xpos() * client.getWindow().getGuiScaledWidth() / client.getWindow().getScreenWidth());
    }

    public static int getMouseY() {
        return Mth.floor(client.mouseHandler.ypos() * client.getWindow().getGuiScaledHeight() / client.getWindow().getScreenHeight());
    }

    public static void drawSlotHighlight(GuiGraphics draw, int x, int y, int width, int height) {
        draw.fill(x, y, x + width, y + height, -2130706433);
    }

    public static void drawSlotHighlight(GuiGraphics draw, LayoutElement widget) {
        drawSlotHighlight(draw, widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight());
    }

    public static void drawItemStack(GuiGraphics draw, Font textRenderer, ItemStack stack, int x, int y) {
        draw.renderFakeItem(stack, x, y);
        draw.renderItemDecorations(textRenderer, stack, x, y);
    }

    public static void drawItemWithoutEntityAndTooltip(GuiGraphics draw, ItemStack stack, int x, int y, int mouseX, int mouseY) {
        drawItemWithoutEntityAndTooltip(draw, stack, x, y, mouseX, mouseY, getItemTooltip(stack));
    }

    public static void drawItemWithoutEntityAndTooltip(GuiGraphics draw, ItemStack stack, int x, int y, int mouseX, int mouseY, List<Component> text) {
        draw.renderFakeItem(stack, x, y);
        if (!text.isEmpty() && GuiHelper.isMouseTouching(x, y, 16, 16, mouseX, mouseY)) {
            draw.setComponentTooltipForNextFrame(Minecraft.getInstance().font, text, mouseX, mouseY);
        }
    }

    public static List<Component> getItemTooltip(ItemStack stack) {
        return stack.getTooltipLines(Item.TooltipContext.of(client.level), client.player, client.options.advancedItemTooltips ? TooltipFlag.ADVANCED : TooltipFlag.NORMAL);
    }

    // A version of the vanilla implementation that changes the size
    public static void drawScaledItemWithoutEntity(GuiGraphics draw, ItemStack stack, int x, int y, int size) {
        if (!stack.isEmpty()) {
            TrackingItemStackRenderState keyedItemRenderState = new TrackingItemStackRenderState();
            client.getItemModelResolver().updateForTopItem(keyedItemRenderState, stack, ItemDisplayContext.GUI, null, null, 0);

            try {
                draw.guiRenderState.submitItem(
                        new ScaledItemGuiElementRenderState(
                                stack.getItem().getName().toString(), new Matrix3x2f(draw.pose()), keyedItemRenderState, x, y, size, draw.scissorStack.peek()
                        )
                );
            } catch (Exception e) {
                CrashReport crashReport = CrashReport.forThrowable(e, "Rendering scaled item (TrUtils)");
                CrashReportCategory crashReportSection = crashReport.addCategory("Item being rendered");
                crashReportSection.setDetail("Item Type", () -> String.valueOf(stack.getItem()));
                crashReportSection.setDetail("Item Components", () -> String.valueOf(stack.getComponents()));
                crashReportSection.setDetail("Item Foil", () -> String.valueOf(stack.hasFoil()));
                throw new ReportedException(crashReport);
            }
        }
    }

    public static void drawTooltip(GuiGraphics draw, Font textRenderer, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner tooltipPositioner) {
        draw.setTooltipForNextFrameInternal(textRenderer, components, x, y, tooltipPositioner, null, false);
    }

    public static boolean isKeyboard() {
        return client.getLastInputType().isKeyboard();
    }

    public static ClientTooltipPositioner widgetPositionerFor(AbstractWidget widget) {
        return !widget.isHovered() && widget.isFocused() && isKeyboard() ? new MenuTooltipPositioner(widget.getRectangle()) : DefaultTooltipPositioner.INSTANCE;
    }
}
