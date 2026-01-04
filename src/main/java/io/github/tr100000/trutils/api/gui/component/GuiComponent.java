package io.github.tr100000.trutils.api.gui.component;

public interface GuiComponent {
    default void initScreenHandler(AbstractComponentScreenHandler<?> handler) {}
}
