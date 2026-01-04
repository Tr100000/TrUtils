package io.github.tr100000.trutils.api.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

@FunctionalInterface
public interface ToolType {
    Item createItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties settings);
}
