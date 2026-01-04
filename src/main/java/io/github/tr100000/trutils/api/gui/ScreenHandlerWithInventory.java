package io.github.tr100000.trutils.api.gui;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class ScreenHandlerWithInventory extends AbstractContainerMenu {
    public Container inventory;

    protected ScreenHandlerWithInventory(MenuType<?> type, int syncId, Container inventory) {
        this(type, syncId);
        this.inventory = inventory;
    }

    protected ScreenHandlerWithInventory(MenuType<?> type, int syncId) {
        super(type, syncId);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int fromIndex) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = slots.get(fromIndex);
        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            final int inventorySize = inventory.getContainerSize();
            if (fromIndex < inventorySize) {
                if (!moveItemStackTo(originalStack, inventorySize, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!moveItemStackTo(originalStack, 0, inventorySize, false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            }
            else {
                slot.setChanged();
            }
        }

        return newStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return inventory.stillValid(player);
    }

    protected void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    protected void addPlayerInventory(Inventory playerInventory, int x, int y) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, x + l * 18, y + i * 18));
            }
        }
    }

    protected void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    protected void addPlayerHotbar(Inventory playerInventory, int x, int y) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, x + i * 18, y));
        }
    }

    @Override
    public void removed(Player player) {
        inventory.stopOpen(player);
        super.removed(player);
    }
}
