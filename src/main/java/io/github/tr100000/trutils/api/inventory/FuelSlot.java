package io.github.tr100000.trutils.api.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class FuelSlot extends Slot {
    private final Supplier<Level> world;

    public FuelSlot(Supplier<Level> world, Container inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.world = world;
    }

    public FuelSlot(Level world, Container inventory, int index, int x, int y) {
        this(() -> world, inventory, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return world.get().fuelValues().isFuel(stack) || isBucket(stack);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return isBucket(stack) ? 1 : super.getMaxStackSize(stack);
    }

    public static boolean isBucket(ItemStack stack) {
        return stack.is(Items.BUCKET);
    }
}
