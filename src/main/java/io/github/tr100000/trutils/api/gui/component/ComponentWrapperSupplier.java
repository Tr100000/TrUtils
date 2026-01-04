package io.github.tr100000.trutils.api.gui.component;

@FunctionalInterface
public interface ComponentWrapperSupplier<T extends GuiComponentServer<?>> {
    ComponentWrapper<T> get();
}
