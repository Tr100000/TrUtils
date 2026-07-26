package io.github.tr100000.trutils;

import io.github.tr100000.trutils.api.gui.component.AbstractComponentScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public abstract class ClientDelegate {
    public static final ClientDelegate INSTANCE = create();

    private static ClientDelegate create() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            try {
                Class<?> implClass = Class.forName("io.github.tr100000.trutils.ClientDelegateClientImpl");
                return (ClientDelegate)implClass.getConstructor().newInstance();
            }
            catch (Exception e) {
                throw new IllegalStateException("Failed to create a ClientDelegate!", e);
            }
        }
        else {
            return new ClientDelegateCommonImpl();
        }
    }

    public abstract boolean isClient();

    public abstract AbstractComponentScreenHandler<?> createComponentScreenHandler(int syncId, Inventory playerInventory, AbstractComponentScreenHandler.Payload payload);

    public abstract List<String> runtimeDatagenEntrypoints();
}
