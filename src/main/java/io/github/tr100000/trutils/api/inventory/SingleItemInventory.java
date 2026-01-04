package io.github.tr100000.trutils.api.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.ticks.ContainerSingleItem;

public interface SingleItemInventory extends ContainerSingleItem {
    ItemStack getTheItem();
    void setTheItem(ItemStack stack);

    static SingleItemInventory of() {
        return new Impl();
    }

    static SingleItemInventory of(ItemStack stack) {
        return new Impl(stack);
    }

    @Override
    default void clearContent() {
        setTheItem(ItemStack.EMPTY);
    }

    @Override
    default boolean stillValid(Player player) {
        return true;
    }

    default void setChanged() {}

    class Impl implements SingleItemInventory {
        private ItemStack stack;

        public Impl() {
            this(ItemStack.EMPTY);
        }

        public Impl(ItemStack stack) {
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
    }
}
