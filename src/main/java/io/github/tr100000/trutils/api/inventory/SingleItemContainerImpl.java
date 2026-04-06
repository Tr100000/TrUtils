package io.github.tr100000.trutils.api.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.ticks.ContainerSingleItem;

public class SingleItemContainerImpl implements ContainerSingleItem {
    static SingleItemContainerImpl of() {
        return new SingleItemContainerImpl();
    }

    static SingleItemContainerImpl of(ItemStack stack) {
        return new SingleItemContainerImpl(stack);
    }

    private ItemStack stack;

    protected SingleItemContainerImpl() {
        this(ItemStack.EMPTY);
    }

    protected SingleItemContainerImpl(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public ItemStack getTheItem() {
        return stack;
    }

    @Override
    public void setTheItem(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void setChanged() {}

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
