package io.github.tr100000.trutils.api.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class BlockRegistryHelper extends RegistryHelper<Block> {
    public BlockRegistryHelper(String modid) {
        super(BuiltInRegistries.BLOCK, modid);
    }

    public Block register(BlockItemId id, BlockBehaviour.Properties properties) {
        return register(id.block(), properties);
    }

    public Block register(BlockItemId id, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties) {
        return register(id.block(), factory, properties);
    }

    public Block register(ResourceKey<Block> id, BlockBehaviour.Properties properties) {
        return register(id, Block::new, properties);
    }

    public Block register(ResourceKey<Block> id, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties) {
        Block block = factory.apply(properties.setId(id));
        return add(block, id);
    }
}
