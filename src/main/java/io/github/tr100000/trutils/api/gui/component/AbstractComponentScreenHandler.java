package io.github.tr100000.trutils.api.gui.component;

import io.github.tr100000.trutils.api.gui.ScreenHandlerWithInventory;
import io.github.tr100000.trutils.api.inventory.EmptyInventory;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.Level;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class AbstractComponentScreenHandler<T extends GuiComponent> extends ScreenHandlerWithInventory {
    public final Inventory playerInventory;
    public final List<? extends T> components;
    public final Map<Identifier, Container> componentInventories;

    protected AbstractComponentScreenHandler(MenuType<?> type, int syncId, Inventory playerInventory, List<T> components, Map<Identifier, Container> componentInventories) {
        super(type, syncId);
        this.playerInventory = playerInventory;
        this.components = components;
        this.componentInventories = componentInventories;

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        for (GuiComponent component : components) {
            component.initScreenHandler(this);
        }

        if (componentInventories.size() > 1) {
            Iterator<Container> iterator = componentInventories.values().iterator();
            inventory = new CompoundContainer(iterator.next(), iterator.next());
            while (iterator.hasNext()) {
                inventory = new CompoundContainer(inventory, iterator.next());
            }
        }
        else if (componentInventories.size() == 1) {
            inventory = componentInventories.values().iterator().next();
        }
        else {
            inventory = EmptyInventory.INSTANCE;
        }
    }

    public abstract Level getLevel();

    @Override
    public Slot addSlot(Slot slot) {
        return super.addSlot(slot);
    }

    public record Payload(Map<Identifier, Integer> componentInventorySizes, byte[] componentData) {
        public static final StreamCodec<RegistryFriendlyByteBuf, Payload> CODEC = StreamCodec.composite(
                ByteBufCodecs.map(Object2IntOpenHashMap::new, Identifier.STREAM_CODEC, ByteBufCodecs.VAR_INT), Payload::componentInventorySizes,
                ByteBufCodecs.BYTE_ARRAY, Payload::componentData,
                Payload::new
        );
    }
}
