package io.github.tr100000.trutils.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands.CommandSelection;

public final class ModCommands {
    private ModCommands() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, CommandSelection environment) {
        TrUtilsCommand.register(dispatcher);
        ForcedEnchantCommand.register(dispatcher, registryAccess);
    }
}
