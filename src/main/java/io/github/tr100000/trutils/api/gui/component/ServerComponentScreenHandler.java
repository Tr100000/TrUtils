package io.github.tr100000.trutils.api.gui.component;

import io.github.tr100000.trutils.TrUtilsScreenHandlerTypes;
import io.github.tr100000.trutils.networking.GuiComponentSyncS2CPacket;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.networking.v1.FriendlyByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class ServerComponentScreenHandler<T extends GuiComponentServer<?>, W extends ComponentWrapper<T>> extends AbstractComponentScreenHandler<T> {
    protected final W wrapper;
    protected final List<Object> componentDataCache = new ObjectArrayList<>();

    public ServerComponentScreenHandler(MenuType<?> type, int syncId, Inventory playerInventory, W wrapper) {
        super(type, syncId, playerInventory, wrapper.getGuiComponents(), wrapper.getDataComponentInventories());
        this.wrapper = wrapper;
        for (GuiComponentServer<?> component : wrapper.getGuiComponents()) {
            componentDataCache.add(component.copyData());
        }
    }

    public ServerComponentScreenHandler(int syncId, Inventory playerInventory, W wrapper) {
        this(TrUtilsScreenHandlerTypes.COMPONENT, syncId, playerInventory, wrapper);
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void broadcastChanges() {
        super.broadcastChanges();
        for (int i = 0; i < wrapper.getGuiComponents().size(); i++) {
            GuiComponentServer component = wrapper.getGuiComponents().get(i);
            if (component.needsSync(componentDataCache.get(i))) {
                RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(FriendlyByteBufs.create(), wrapper.getLevel().registryAccess());
                component.writePacket(buf);
                ServerPlayNetworking.send((ServerPlayer) playerInventory.player, new GuiComponentSyncS2CPacket(containerId, i, buf.array()));
                componentDataCache.set(i, component.copyData());
                buf.release();
            }
        }
    }

    @Override
    public boolean stillValid(Player player) {
        if (wrapper instanceof BlockEntity blockEntity) {
            return Container.stillValidBlockEntity(blockEntity, player);
        } else {
            return true;
        }
    }

    @Override
    public Level getLevel() {
        return wrapper.getLevel();
    }
}
