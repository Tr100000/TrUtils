package io.github.tr100000.trutils.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.github.tr100000.trutils.TrUtils;
import io.github.tr100000.trutils.api.datagen.RuntimeDatagen;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Iterator;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public final class TrUtilsCommand {
    private TrUtilsCommand() {}

    private static final SuggestionProvider<CommandSourceStack> REGISTRIES = (_, builder) ->
            SharedSuggestionProvider.suggestResource(BuiltInRegistries.REGISTRY.keySet(), builder);

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal(TrUtils.MODID)
                .then(literal("debug")
                        .then(literal("showComponents")
                                .requires(CommandSourceStack::isPlayer)
                                .executes(TrUtilsCommand::showCurrentHandItemComponents)
                        )
                        .then(literal("runtimeDatagen")
                                .requires(CommandSourceStack::isPlayer)
                                .requires(TrUtilsCommand::isDevelopmentEnvironment)
                                .executes(TrUtilsCommand::runtimeDatagen)
                        )
                        .then(literal("printRegistryEntries")
                                .then(argument("registry", IdentifierArgument.id())
                                        .requires(CommandSourceStack::isPlayer)
                                        .suggests(REGISTRIES)
                                        .executes(TrUtilsCommand::printRegistryEntries)
                                )
                        )
                )
        );
    }

    private static int showCurrentHandItemComponents(CommandContext<CommandSourceStack> context) {
        Player player = context.getSource().getPlayer();
        assert player != null;
        ItemStack stack = player.getMainHandItem();
        context.getSource().sendSuccess(() -> Component.literal(stack.getComponents().toString()), false);
        return 1;
    }

    private static int runtimeDatagen(CommandContext<CommandSourceStack> context) {
        RuntimeDatagen.runAll();
        return 1;
    }

    private static int printRegistryEntries(CommandContext<CommandSourceStack> context) {
        Registry<?> registry = BuiltInRegistries.REGISTRY.getValue(IdentifierArgument.getId(context, "registry"));
        if (registry == null) {
            context.getSource().sendSystemMessage(Component.literal("Registry not found"));
            return 0;
        }
        Iterator<Identifier> ids = registry.keySet().stream().sorted().iterator();
        StringBuilder builder = new StringBuilder();
        while (ids.hasNext()) {
            builder.append('\"');
            builder.append(ids.next());
            builder.append('\"');
            if (ids.hasNext()) builder.append(',');
        }
        context.getSource().sendSystemMessage(Component.literal(builder.toString()));
        return registry.keySet().size();
    }

    private static boolean isDevelopmentEnvironment(CommandSourceStack source) {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
