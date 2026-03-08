package io.github.tr100000.trutils.api.recipe;

import com.mojang.serialization.MapCodec;
import io.github.tr100000.trutils.TrUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;

/**
 * A blank recipe, for when {@code null} isn't an option
 */
public final class BlankRecipe implements Recipe<RecipeInput> {
    public static final BlankRecipe INSTANCE = new BlankRecipe();

    private BlankRecipe() {}

    @Override
    public boolean matches(RecipeInput input, Level world) {
        return false;
    }

    @Override
    public ItemStack assemble(RecipeInput input, HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public RecipeSerializer<? extends Recipe<RecipeInput>> getSerializer() {
        return Type.INSTANCE;
    }

    @Override
    public RecipeType<? extends Recipe<RecipeInput>> getType() {
        return Type.INSTANCE;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CAMPFIRE;
    }

    public static class Type implements RecipeType<BlankRecipe>, RecipeSerializer<BlankRecipe> {
        public static final Type INSTANCE = new Type();
        public static final MapCodec<BlankRecipe> CODEC = MapCodec.unit(BlankRecipe.INSTANCE);
        public static final StreamCodec<RegistryFriendlyByteBuf, BlankRecipe> PACKET_CODEC = StreamCodec.unit(BlankRecipe.INSTANCE);

        @ApiStatus.Internal
        public static void register() {
            Identifier id = TrUtils.id("blank");
            Registry.register(BuiltInRegistries.RECIPE_TYPE, id, Type.INSTANCE);
            Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id, Type.INSTANCE);
        }

        private Type() {}

        @Override
        public MapCodec<BlankRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BlankRecipe> streamCodec() {
            return PACKET_CODEC;
        }
    }
}
