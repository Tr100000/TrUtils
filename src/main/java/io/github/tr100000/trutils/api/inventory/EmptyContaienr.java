package io.github.tr100000.trutils.api.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class EmptyContaienr implements Container {
    public static final EmptyContaienr INSTANCE = new EmptyContaienr();

    private EmptyContaienr() {}

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, ItemStack stack) { /* Do nothing */ }

    @Override
    public void setChanged() { /* Do nothing */ }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() { /* Do nothing */ }
}
