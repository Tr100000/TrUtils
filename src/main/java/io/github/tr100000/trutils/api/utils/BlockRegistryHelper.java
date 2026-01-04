package io.github.tr100000.trutils.api.utils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

/**
 * A subclass of {@link RegistryHelper} for blocks
 */
public class BlockRegistryHelper extends RegistryHelper<Block> {
    public BlockRegistryHelper(String modid) {
        super(BuiltInRegistries.BLOCK, modid);
    }

    public <T extends Block> T addBlock(Function<BlockBehaviour.Properties, T> blockFactory, BlockBehaviour.Properties settings, Identifier id) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, id);
        return super.add(blockFactory.apply(settings.setId(key)), id);
    }

    public <T extends Block> T addBlock(Function<BlockBehaviour.Properties, T> blockFactory, BlockBehaviour.Properties settings, String id) {
        return addBlock(blockFactory, settings, Identifier.fromNamespaceAndPath(modid, id));
    }

    public Block addBlock(BlockBehaviour.Properties settings, Identifier id) {
        return addBlock(Block::new, settings, id);
    }

    public Block addBlock(BlockBehaviour.Properties settings, String id) {
        return addBlock(Block::new, settings, id);
    }
}
