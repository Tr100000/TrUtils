package io.github.tr100000.trutils.api.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public interface GuiComponentClient<T extends ComponentScreen<?, ?>> extends GuiComponent {
    Minecraft client = Minecraft.getInstance();

    void readPacket(Level world, FriendlyByteBuf buf);

    default void initScreen(T screen, int x, int y) {}
    default void extractScreenEarly(T screen, final GuiGraphicsExtractor graphics, int x, int y, float delta, int mouseX, int mouseY) {}
    default void extractScreen(T screen, final GuiGraphicsExtractor graphics, int x, int y, float delta, int mouseX, int mouseY) {}

    @FunctionalInterface
    interface Factory<T extends GuiComponentClient<?>> {
        T createFromInitPacket(Level world, FriendlyByteBuf buf);
    }

    interface NoData<T extends ComponentScreen<?, ?>> extends GuiComponentClient<T> {
        @Override
        default void readPacket(Level world, FriendlyByteBuf buf) {}
    }
}
