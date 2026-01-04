package io.github.tr100000.trutils.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Collection;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public final class ForcedEnchantCommand {
    private static final DynamicCommandExceptionType FAILED_ENTITY_EXCEPTION = new DynamicCommandExceptionType(entityName -> Component.translatable("commands.enchant.failed.entity", entityName));
    private static final DynamicCommandExceptionType FAILED_ITEMLESS_EXCEPTION = new DynamicCommandExceptionType(entityName -> Component.translatable("commands.enchant.failed.itemless", entityName));
    private static final SimpleCommandExceptionType FAILED_EXCEPTION = new SimpleCommandExceptionType(Component.translatable("commands.enchant.failed"));

    private ForcedEnchantCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess) {
        dispatcher.register(literal("forcedenchant").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(argument("targets", EntityArgument.entities())
                        .then(argument("enchantment", ResourceArgument.resource(registryAccess, Registries.ENCHANTMENT))
                                .executes(context -> execute(context, EntityArgument.getEntities(context, "targets"), ResourceArgument.getEnchantment(context, "enchantment"), 1))
                                .then(argument("level", IntegerArgumentType.integer(0))
                                        .executes(context -> execute(context, EntityArgument.getEntities(context, "targets"), ResourceArgument.getEnchantment(context, "enchantment"), IntegerArgumentType.getInteger(context, "level")))
                                )
                        )
                )
        );
    }

    private static int execute(CommandContext<CommandSourceStack> context, Collection<? extends Entity> targets, Reference<Enchantment> enchantment, int level) throws CommandSyntaxException {
        int i = 0;

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity livingEntity) {
                ItemStack stack = livingEntity.getMainHandItem();
                if (!stack.isEmpty()) {
                    stack.enchant(enchantment, level);
                    i++;
                }
                else if (targets.size() == 1) {
                    throw FAILED_ITEMLESS_EXCEPTION.create(livingEntity.getName().getString());
                }
            }
            else if (targets.size() == 1) {
                throw FAILED_ENTITY_EXCEPTION.create(entity.getName().getString());
            }
        }

        if (i == 0) {
            throw FAILED_EXCEPTION.create();
        }
        else {
            if (targets.size() == 1) {
                context.getSource().sendSuccess(
                    () -> Component.translatable("commands.enchant.success.single", Enchantment.getFullname(enchantment, level), targets.iterator().next().getDisplayName()),
                    true
                );
            }
            else {
                context.getSource().sendSuccess(
                    () -> Component.translatable("commands.enchant.success.multiple", Enchantment.getFullname(enchantment, level), targets.size()),
                    true
                );
            }

            return i;
        }
    }
}
