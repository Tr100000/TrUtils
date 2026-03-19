package io.github.tr100000.trutils;

import io.github.tr100000.trutils.api.gui.component.AbstractComponentScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Inventory;

public final class TrUtilsScreenHandlerTypes {
    private TrUtilsScreenHandlerTypes() {}

    public static final ExtendedMenuType<AbstractComponentScreenHandler<?>, AbstractComponentScreenHandler.Payload> COMPONENT = new ExtendedMenuType<>(TrUtilsScreenHandlerTypes::createScreenHandler, AbstractComponentScreenHandler.Payload.CODEC);

    public static void register() {
        Registry.register(BuiltInRegistries.MENU, TrUtils.id("component"), COMPONENT);
    }

    private static AbstractComponentScreenHandler<?> createScreenHandler(int syncId, Inventory playerInventory, AbstractComponentScreenHandler.Payload payload) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            return ClientDelegate.INSTANCE.createComponentScreenHandler(syncId, playerInventory, payload);
        }
        else {
            throw new UnsupportedOperationException("You can't do that");
        }
    }
}
