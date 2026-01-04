package io.github.tr100000.trutils.api.gui.component;

import io.github.tr100000.trutils.TrUtilsScreenHandlerTypes;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ClientComponentScreenHandler<T extends GuiComponentClient<?>> extends AbstractComponentScreenHandler<T> {
    @SuppressWarnings("unchecked")
    public static <T extends GuiComponentClient<?>> ClientComponentScreenHandler<T> create(MenuType<?> type, int syncId, Inventory playerInventory, Payload payload) {
        List<T> components = new ObjectArrayList<>();
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.copiedBuffer(payload.componentData()));
        int componentCount = buf.readVarInt();
        for (int i = 0; i < componentCount; i++) {
            Identifier id = buf.readIdentifier();
            components.add((T)GuiComponentRegistry.get(id).createFromInitPacket(playerInventory.player.level(), buf));
        }

        Map<Identifier, Container> componentInventories = new Object2ObjectOpenHashMap<>(payload.componentInventorySizes().size());
        payload.componentInventorySizes().forEach((id, size) -> componentInventories.put(id, new SimpleContainer(size)));

        return new ClientComponentScreenHandler<>(type, syncId, playerInventory, components, componentInventories);
    }

    public static <T extends GuiComponentClient<?>> ClientComponentScreenHandler<T> create(int syncId, Inventory playerInventory, Payload payload) {
        return create(TrUtilsScreenHandlerTypes.COMPONENT, syncId, playerInventory, payload);
    }

    protected ClientComponentScreenHandler(MenuType<?> type, int syncId, Inventory playerInventory, List<T> guiComponents, Map<Identifier, Container> componentInventories) {
        super(type, syncId, playerInventory, guiComponents, componentInventories);
    }

    @Override
    public Level getLevel() {
        return Minecraft.getInstance().level;
    }

    @SuppressWarnings("unchecked")
    public <C extends T> Optional<C> tryGetGuiComponent(Class<C> componentClass) {
        for (T c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                return Optional.of((C)c);
            }
        }
        return Optional.empty();
    }
}
