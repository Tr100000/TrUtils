package io.github.tr100000.trutils.api.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class FilteredSlot extends Slot {
    private final Predicate<ItemStack> predicate;

    public FilteredSlot(Container inventory, Predicate<ItemStack> predicate, int index, int x, int y) {
        super(inventory, index, x, y);
        this.predicate = predicate;
    }

    public FilteredSlot(Container inventory, Item item, int index, int x, int y) {
        this(inventory, stack -> stack.is(item), index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return predicate.test(stack);
    }
}
