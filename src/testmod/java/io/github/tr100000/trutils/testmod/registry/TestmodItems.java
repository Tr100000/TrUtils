package io.github.tr100000.trutils.testmod.registry;

import io.github.tr100000.trutils.api.item.WideAreaAxeItem;
import io.github.tr100000.trutils.api.item.WideAreaHoeItem;
import io.github.tr100000.trutils.api.item.WideAreaShovelItem;
import io.github.tr100000.trutils.api.item.WideAreaToolItem;
import io.github.tr100000.trutils.api.utils.ItemRegistryHelper;
import io.github.tr100000.trutils.testmod.TrUtilsTestmod;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public final class TestmodItems {
    private TestmodItems() {}

    public static final ItemRegistryHelper REGISTRY = new ItemRegistryHelper(TrUtilsTestmod.MODID);

    public static final Item WIDE_PICKAXE = REGISTRY.addItem(settings -> new WideAreaToolItem(settings.pickaxe(ToolMaterial.NETHERITE, 5, 5), 1, 1), new Item.Properties(), "wide_pickaxe");
    public static final Item WIDE_AXE = REGISTRY.addItem(settings -> new WideAreaAxeItem(ToolMaterial.NETHERITE, 5, 5, settings, 1, 1), new Item.Properties(), "wide_axe");
    public static final Item WIDE_SHOVEL = REGISTRY.addItem(settings -> new WideAreaShovelItem(ToolMaterial.NETHERITE, 5, 5, settings, 1, 1), new Item.Properties(), "wide_shovel");
    public static final Item WIDE_HOE = REGISTRY.addItem(settings -> new WideAreaHoeItem(ToolMaterial.NETHERITE, 5, 5, settings, 1, 1), new Item.Properties(), "wide_hoe");
}
