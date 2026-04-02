package io.github.tr100000.trutils.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.function.Function;

public final class BlockUtils {
    private BlockUtils() {}

    public static void dropContentsIfInventoryDestroyed(BlockState state, Level world, BlockPos pos, BlockState newState, Function<BlockEntity, @Nullable Container> inventoryGetter) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            assert blockEntity != null;
            Container inventory = inventoryGetter.apply(blockEntity);
            if (inventory != null) {
                Containers.dropContents(world, pos, inventory);
            }
            world.updateNeighbourForOutputSignal(pos, state.getBlock());
        }
    }
}
