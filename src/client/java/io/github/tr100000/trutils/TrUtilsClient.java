package io.github.tr100000.trutils;

import io.github.tr100000.trutils.api.gui.component.AbstractComponentScreenHandler;
import io.github.tr100000.trutils.api.gui.component.ComponentScreen;
import io.github.tr100000.trutils.networking.TrUtilsClientNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

@Environment(EnvType.CLIENT)
public class TrUtilsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.<AbstractComponentScreenHandler<?>, AbstractContainerScreen<AbstractComponentScreenHandler<?>>>register(TrUtilsScreenHandlerTypes.COMPONENT, ComponentScreen::create);
        TrUtilsClientNetworking.registerClientRecievers();
    }
}
