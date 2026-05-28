package io.github.tr100000.trutils.testmod.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import static io.github.tr100000.trutils.testmod.registry.TestmodItems.REGISTRY;

public final class TestmodItemIds {
    private TestmodItemIds() {}

    public static final ResourceKey<Item> WIDE_PICKAXE = REGISTRY.createId("wide_pickaxe");
    public static final ResourceKey<Item> WIDE_AXE = REGISTRY.createId("wide_axe");
    public static final ResourceKey<Item> WIDE_SHOVEL = REGISTRY.createId("wide_shovel");
    public static final ResourceKey<Item> WIDE_HOE = REGISTRY.createId("wide_hoe");
}
