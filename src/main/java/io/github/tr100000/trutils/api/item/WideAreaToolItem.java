package io.github.tr100000.trutils.api.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class WideAreaToolItem extends Item implements WideAreaTool {
    public final int breakRadius;
    public final int depth;

    public WideAreaToolItem(Item.Properties settings, int breakRadius) {
        this(settings, breakRadius, 1);
    }

    public WideAreaToolItem(Item.Properties settings, int breakRadius, int depth) {
        super(settings);
        this.breakRadius = breakRadius;
        this.depth = depth;
    }

    @Override
    public int getBreakRadius(ItemStack stack) {
        return breakRadius;
    }

    @Override
    public int getDepth(ItemStack stack) {
        return depth;
    }
}
