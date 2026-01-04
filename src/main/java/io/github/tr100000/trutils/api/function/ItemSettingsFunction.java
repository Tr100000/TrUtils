package io.github.tr100000.trutils.api.function;

import net.minecraft.world.item.Item;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface ItemSettingsFunction extends UnaryOperator<Item.Properties> {
    Item.Properties apply(Item.Properties itemSettings);
}
