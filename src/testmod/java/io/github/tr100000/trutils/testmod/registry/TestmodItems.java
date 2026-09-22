package io.github.tr100000.trutils.testmod.registry;

import io.github.tr100000.trutils.api.item.WideAreaToolItem;
import io.github.tr100000.trutils.api.registry.ItemRegistryHelper;
import io.github.tr100000.trutils.testmod.TrUtilsTestmod;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public final class TestmodItems {
    private TestmodItems() {}

    public static final ItemRegistryHelper REGISTRY = new ItemRegistryHelper(TrUtilsTestmod.MODID);

    public static final Item WIDE_PICKAXE = REGISTRY.addItem(TestmodItemIds.WIDE_PICKAXE, settings -> new WideAreaToolItem(settings.pickaxe(ToolMaterial.NETHERITE, 5, 5), 1, 1));
    public static final Item WIDE_AXE = REGISTRY.addItem(TestmodItemIds.WIDE_AXE, settings -> new WideAreaToolItem(settings.axe(ToolMaterial.NETHERITE, 5, 5), 1, 1));
    public static final Item WIDE_SHOVEL = REGISTRY.addItem(TestmodItemIds.WIDE_SHOVEL, settings -> new WideAreaToolItem(settings.shovel(ToolMaterial.NETHERITE, 5, 5), 1, 1));
    public static final Item WIDE_HOE = REGISTRY.addItem(TestmodItemIds.WIDE_HOE, settings -> new WideAreaToolItem(settings.hoe(ToolMaterial.NETHERITE, 5, 5), 1, 1));
}
