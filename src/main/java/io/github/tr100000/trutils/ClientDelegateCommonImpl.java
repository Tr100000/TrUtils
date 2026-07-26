package io.github.tr100000.trutils;

import io.github.tr100000.trutils.api.datagen.RuntimeDatagen;
import io.github.tr100000.trutils.api.gui.component.AbstractComponentScreenHandler;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public final class ClientDelegateCommonImpl extends ClientDelegate {
    @Override
    public boolean isClient() {
        return false;
    }

    @Override
    public AbstractComponentScreenHandler<?> createComponentScreenHandler(int syncId, Inventory playerInventory, AbstractComponentScreenHandler.Payload payload) {
        throw new UnsupportedOperationException("You can't do that!");
    }

    @Override
    public List<String> runtimeDatagenEntrypoints() {
        return List.of(RuntimeDatagen.ENTRYPOINT_KEY_MAIN);
    }
}
