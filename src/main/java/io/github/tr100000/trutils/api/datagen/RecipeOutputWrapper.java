package io.github.tr100000.trutils.api.datagen;

import net.minecraft.advancements.Advancement.Builder;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import org.jspecify.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class RecipeOutputWrapper implements RecipeOutput {
    private final RecipeOutput wrapped;
    private final BiConsumer<Identifier, Recipe<?>> recipeConsumer;

    public RecipeOutputWrapper(RecipeOutput wrapped, BiConsumer<Identifier, Recipe<?>> recipeConsumer) {
        this.wrapped = wrapped;
        this.recipeConsumer = recipeConsumer;
    }

    @Override
    public void accept(ResourceKey<Recipe<?>> key, Recipe<?> recipe, @Nullable AdvancementHolder advancement) {
        wrapped.accept(key, recipe, advancement);
        recipeConsumer.accept(key.identifier(), recipe);
    }

    @Override
    public Builder advancement() {
        return wrapped.advancement();
    }

    @Override
    public <S> HolderGetter<S> lookup(ResourceKey<? extends Registry<? extends S>> key) {
        return wrapped.lookup(key);
    }

    @Override
    @SuppressWarnings("deprecation")
    public <S> Stream<Holder.Reference<S>> listContextElements(ResourceKey<? extends Registry<? extends S>> key) {
        return wrapped.listContextElements(key);
    }
}
