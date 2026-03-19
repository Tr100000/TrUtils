package io.github.tr100000.trutils.api.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.util.FormattedCharSequence;

import java.util.Arrays;
import java.util.List;

@Environment(EnvType.CLIENT)
public abstract class ExtendedTooltipComponent implements ClientTooltipComponent {
    protected void text(GuiGraphicsExtractor graphics, Font textRenderer, FormattedCharSequence text, int x, int y) {
        graphics.text(textRenderer, text, x, y, -1, true);
    }

    protected int longestText(Font textRenderer, FormattedCharSequence... text) {
        return longestText(textRenderer, 0, text);
    }

    protected int longestText(Font textRenderer, int currentLongest, FormattedCharSequence... text) {
        return Math.max(Arrays.stream(text).mapToInt(textRenderer::width).max().orElse(0), currentLongest);
    }

    protected int longestText(Font textRenderer, List<FormattedCharSequence> text) {
        return longestText(textRenderer, 0, text);
    }

    protected int longestText(Font textRenderer, int currentLongest, List<FormattedCharSequence> text) {
        return Math.max(text.stream().mapToInt(textRenderer::width).max().orElse(0), currentLongest);
    }
}
