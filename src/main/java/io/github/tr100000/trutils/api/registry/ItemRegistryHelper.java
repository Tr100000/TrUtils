package io.github.tr100000.trutils.api.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class ItemRegistryHelper extends RegistryHelper<Item> {
    public ItemRegistryHelper(String modid) {
        super(BuiltInRegistries.ITEM, modid);
    }

    public ResourceKey<Item> createId(String name) {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(modid, name));
    }

    public Item addBlock(BlockItemId id, Block block) {
        return addBlock(id, block, BlockItem::new, new Item.Properties());
    }

    public Item addBlock(BlockItemId id, Block block, Item.Properties properties) {
        return addBlock(id, block, BlockItem::new, properties);
    }

    public Item addBlock(BlockItemId id, Block block, UnaryOperator<Item.Properties> propertiesFunction) {
        return addBlock(id, block, (b, p) -> new BlockItem(b, propertiesFunction.apply(p)));
    }

    public Item addBlock(BlockItemId id, Block block, Block... alternatives) {
        Item item = addBlock(id, block);

        for (Block alternative : alternatives) {
            Item.BY_BLOCK.put(alternative, item);
        }

        return item;
    }

    public Item addBlock(BlockItemId id, Block block, BiFunction<Block, Item.Properties, Item> itemFactory) {
        return addBlock(id, block, itemFactory, new Item.Properties());
    }

    public Item addBlock(
            BlockItemId id, Block block, BiFunction<Block, Item.Properties, Item> itemFactory, Item.Properties properties
    ) {
        return addItem(id.item(), p -> (Item)itemFactory.apply(block, p), properties.useBlockDescriptionPrefix().requiredFeatures(block.requiredFeatures()));
    }

    public Item addItem(ResourceKey<Item> id) {
        return addItem(id, Item::new, new Item.Properties());
    }

    public Item addItem(ResourceKey<Item> id, Item.Properties properties) {
        return addItem(id, Item::new, properties);
    }

    public Item addItem(BlockItemId id, Function<Item.Properties, Item> itemFactory) {
        return addItem(id.item(), itemFactory, new Item.Properties());
    }

    public Item addItem(ResourceKey<Item> id, Function<Item.Properties, Item> itemFactory) {
        return addItem(id, itemFactory, new Item.Properties());
    }

    public Item addItem(BlockItemId id, Function<Item.Properties, Item> itemFactory, Item.Properties properties) {
        return addItem(id.item(), itemFactory, properties);
    }

    public Item addItem(ResourceKey<Item> id, Function<Item.Properties, Item> itemFactory, Item.Properties properties) {
        properties = properties.setId(id);
        Item item = itemFactory.apply(properties);
        if (item instanceof BlockItem blockItem) {
            blockItem.registerBlocks(Item.BY_BLOCK, item);
        }

        return add(item, id);
    }
}
