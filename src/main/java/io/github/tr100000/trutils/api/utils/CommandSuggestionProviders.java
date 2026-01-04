package io.github.tr100000.trutils.api.utils;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class CommandSuggestionProviders {
    private CommandSuggestionProviders() {}

    private static final Map<Registry<?>, SuggestionProvider<? extends SharedSuggestionProvider>> registrySuggestionProviders = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends SharedSuggestionProvider, R> SuggestionProvider<T> getOrCreateForRegistry(Registry<R> registry) {
        return (SuggestionProvider<T>)registrySuggestionProviders.computeIfAbsent(registry, RegistrySuggestionProvider::new);
    }

    public record RegistrySuggestionProvider<S extends SharedSuggestionProvider>(Registry<?> registry) implements SuggestionProvider<S> {
        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            registry.keySet().forEach(id -> {
                builder.suggest(id.toString(), Component.translatable(id.toLanguageKey(registry.key().identifier().getPath())));
            });
            return builder.buildFuture();
        }
    }
}
