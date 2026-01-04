package io.github.tr100000.trutils.api.item;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.ToolMaterial;

public enum ToolTypes implements ToolType {
    SWORD {
        @Override
        public Item createItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties settings) {
            return new Item(settings.sword(material, attackDamage, attackSpeed));
        }
    },
    AXE {
        @Override
        public Item createItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties settings) {
            return new AxeItem(material, attackDamage, attackSpeed, settings);
        }
    },
    PICKAXE {
        @Override
        public Item createItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties settings) {
            return new Item(settings.pickaxe(material, attackDamage, attackSpeed));
        }
    },
    SHOVEL {
        @Override
        public Item createItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties settings) {
            return new ShovelItem(material, attackDamage, attackSpeed, settings);
        }
    },
    HOE {
        @Override
        public Item createItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties settings) {
            return new HoeItem(material, attackDamage, attackSpeed, settings);
        }
    },
}
