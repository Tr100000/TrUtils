package io.github.tr100000.trutils;

import io.github.tr100000.trutils.api.gui.component.AbstractComponentScreenHandler;
import io.github.tr100000.trutils.api.gui.component.ClientComponentScreenHandler;
import net.minecraft.world.entity.player.Inventory;

public class ClientDelegateClientImpl extends ClientDelegate {
    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public AbstractComponentScreenHandler<?> createComponentScreenHandler(int syncId, Inventory playerInventory, AbstractComponentScreenHandler.Payload payload) {
        return ClientComponentScreenHandler.create(syncId, playerInventory, payload);
    }
}
