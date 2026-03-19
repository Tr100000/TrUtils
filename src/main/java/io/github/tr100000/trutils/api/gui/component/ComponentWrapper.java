package io.github.tr100000.trutils.api.gui.component;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.fabric.api.networking.v1.FriendlyByteBufs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface ComponentWrapper<T extends GuiComponentServer<?>> {
    default List<T> getGuiComponents() {
        return Collections.emptyList();
    }

    default Map<Identifier, Container> getDataComponentInventories() {
        return Collections.emptyMap();
    }

    Level getLevel();

    default AbstractComponentScreenHandler.Payload getScreenOpeningPayload() {
        Map<Identifier, Integer> componentInventorySizes = new Object2IntOpenHashMap<>();
        getDataComponentInventories().forEach((id, inventory) -> componentInventorySizes.put(id, inventory.getContainerSize()));

        return new AbstractComponentScreenHandler.Payload(componentInventorySizes, getScreenOpeningBuf().array());
    }

    default RegistryFriendlyByteBuf getScreenOpeningBuf() {
        RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(FriendlyByteBufs.create(), getLevel().registryAccess());
        buf.writeVarInt(getGuiComponents().size());
        getGuiComponents().forEach(component -> {
            buf.writeIdentifier(component.getId());
            component.writeInitPacket(buf);
        });
        return buf;
    }

    static ComponentWrapper<GuiComponentServer<?>> of(Level world, List<GuiComponentServer<?>> components) {
        return new Impl(world, components);
    }

    class Impl implements ComponentWrapper<GuiComponentServer<?>> {
        private final Level world;
        private final List<GuiComponentServer<?>> components;

        private Impl(Level world, List<GuiComponentServer<?>> components) {
            this.world = world;
            this.components = components;
        }

        @Override
        public Level getLevel() {
            return world;
        }

        @Override
        public List<GuiComponentServer<?>> getGuiComponents() {
            return components;
        }
    }
}
