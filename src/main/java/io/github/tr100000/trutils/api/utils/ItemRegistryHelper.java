package io.github.tr100000.trutils.api.utils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.BiFunction;
import java.util.function.Function;

public class ItemRegistryHelper extends RegistryHelper<Item> {
    public ItemRegistryHelper(String modid) {
        super(BuiltInRegistries.ITEM, modid);
    }

    public <T extends Item> T addItem(Function<Item.Properties, T> itemFactory, Item.Properties settings, Identifier id) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
        return super.add(itemFactory.apply(settings.setId(key)), id);
    }

    public <T extends Item> T addItem(Function<Item.Properties, T> itemFactory, Item.Properties settings, String id) {
        return addItem(itemFactory, settings, Identifier.fromNamespaceAndPath(modid, id));
    }

    public Item addItem(Item.Properties settings, Identifier id) {
        return addItem(Item::new, settings, id);
    }

    public Item addItem(Item.Properties settings, String id) {
        return addItem(Item::new, settings, id);
    }

    public Item addBlockItem(Block block, Item.Properties settings, Identifier id) {
        return addItem(s -> new BlockItem(block, s), settings.useBlockDescriptionPrefix(), id);
    }

    public Item addBlockItem(Block block, Item.Properties settings, String id) {
        return addItem(s -> new BlockItem(block, s), settings.useBlockDescriptionPrefix(), id);
    }

    public Item addBlockItem(Block block, BiFunction<Block, Item.Properties, BlockItem> blockItemFactory, Item.Properties settings, Identifier id) {
        return addItem(s -> blockItemFactory.apply(block, s), settings.useBlockDescriptionPrefix(), id);
    }

    public Item addBlockItem(Block block, BiFunction<Block, Item.Properties, BlockItem> blockItemFactory, Item.Properties settings, String id) {
        return addItem(s -> blockItemFactory.apply(block, s), settings.useBlockDescriptionPrefix(), id);
    }
}
