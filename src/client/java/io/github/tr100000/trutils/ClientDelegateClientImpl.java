package io.github.tr100000.trutils;

import io.github.tr100000.trutils.api.datagen.RuntimeDatagen;
import io.github.tr100000.trutils.api.gui.component.AbstractComponentScreenHandler;
import io.github.tr100000.trutils.api.gui.component.ClientComponentScreenHandler;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class ClientDelegateClientImpl extends ClientDelegate {
    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public AbstractComponentScreenHandler<?> createComponentScreenHandler(int syncId, Inventory playerInventory, AbstractComponentScreenHandler.Payload payload) {
        return ClientComponentScreenHandler.create(syncId, playerInventory, payload);
    }

    @Override
    public List<String> runtimeDatagenEntrypoints() {
        return List.of(RuntimeDatagen.ENTRYPOINT_KEY_MAIN, RuntimeDatagen.ENTRYPOINT_KEY_CLIENT);
    }
}
