package io.github.tr100000.trutils.api.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

@FunctionalInterface
public interface WideAreaSpecificToolFactory<T extends Item & WideAreaTool> {
    T apply(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties settings, int breakRadius, int depth);
}
