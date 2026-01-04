package io.github.tr100000.trutils.api.item;

import net.minecraft.world.item.Item;

@FunctionalInterface
public interface WideAreaToolFactory<T extends Item & WideAreaTool> {
    T apply(Item.Properties settings, int breakRadius, int depth);
}
