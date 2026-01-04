package io.github.tr100000.trutils.api.datagen;

import net.minecraft.advancements.Advancement.Builder;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import org.jspecify.annotations.Nullable;

import java.util.function.BiConsumer;

public class RecipeExporterWrapper implements RecipeOutput {
    private final RecipeOutput wrapped;
    private final BiConsumer<Identifier, Recipe<?>> recipeConsumer;

    public RecipeExporterWrapper(RecipeOutput wrapped, BiConsumer<Identifier, Recipe<?>> recipeConsumer) {
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
    public void includeRootAdvancement() {
        wrapped.includeRootAdvancement();
    }
}
