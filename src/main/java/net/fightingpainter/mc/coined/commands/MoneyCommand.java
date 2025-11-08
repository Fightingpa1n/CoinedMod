package net.fightingpainter.mc.coined.commands;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.fightingpainter.mc.coined.gui.currency.BalanceManager;
import net.fightingpainter.mc.coined.util.Money;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.commands.arguments.EntityArgument;

public class MoneyCommand { //TODO: clean up, also make it batter to use ingame. also they currently don't work as the commands are running on both client and server, but should be server only

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("balance")
        
            .then(Commands.literal("get")
                .executes((context) -> getBalanceSelf(context))

                .then(Commands.argument("player", EntityArgument.player())
                    .requires(source -> source.hasPermission(2)) // Requires admin permissions
                    .executes((context) -> getBalance(context, EntityArgument.getPlayer(context, "player")))
                )
            )

            .then(Commands.literal("set")
                .requires(source -> source.hasPermission(2)) // Requires admin permissions
                .then(Commands.argument("amount", LongArgumentType.longArg(0))
                    .executes((context) -> setBalanceSelf(context, LongArgumentType.getLong(context, "amount")))
                )
                
                .then(Commands.argument("player", EntityArgument.player())
                    .then(Commands.argument("amount", LongArgumentType.longArg(0))
                        .executes((context) -> setBalance(context, EntityArgument.getPlayer(context, "player"), LongArgumentType.getLong(context, "amount")))
                    )
                )
            )

            .then(Commands.literal("add")
                .requires(source -> source.hasPermission(2)) // Requires admin permissions

                .then(Commands.argument("amount", LongArgumentType.longArg(0))
                    .executes((context) -> addBalanceSelf(context, LongArgumentType.getLong(context, "amount")))
                )

                .then(Commands.argument("player", EntityArgument.player())
                    .then(Commands.argument("amount", LongArgumentType.longArg(0))
                        .executes((context) -> addBalance(context, EntityArgument.getPlayer(context, "player"), LongArgumentType.getLong(context, "amount")))
                    )
                )
            )

            .then(Commands.literal("subtract")
                .requires(source -> source.hasPermission(2)) // Requires admin permissions

                .then(Commands.argument("amount", LongArgumentType.longArg(0))
                    .executes((context) -> subtractBalanceSelf(context, LongArgumentType.getLong(context, "amount")))
                )

                .then(Commands.argument("player", EntityArgument.player())
                    .then(Commands.argument("amount", LongArgumentType.longArg(0))
                        .executes((context) -> subtractBalance(context, EntityArgument.getPlayer(context, "player"), LongArgumentType.getLong(context, "amount")))
                    )
                )
            )
        );
    }

    //=============== Get ===============\\
    private static int getBalanceSelf(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            return getBalance(context, player);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static int getBalance(CommandContext<CommandSourceStack> context, ServerPlayer targetPlayer) {
        try {
            Money balance = BalanceManager.getPlayerMoney(targetPlayer);
            context.getSource().sendSuccess(() -> Component.literal("Your balance is: " + balance.toString()), false);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //=============== Set ===============\\
    private static int setBalanceSelf(CommandContext<CommandSourceStack> context, long amount) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            return setBalance(context, player, amount);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static int setBalance(CommandContext<CommandSourceStack> context, ServerPlayer targetPlayer, long amount) {
        try {
            BalanceManager.setPlayerBalance(targetPlayer, amount);
            context.getSource().sendSuccess(() -> Component.literal(targetPlayer.getName().getString() + "'s balance has been set to: " + amount), true);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    //=============== Add ===============\\
    private static int addBalanceSelf(CommandContext<CommandSourceStack> context, long amount) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            return addBalance(context, player, amount);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static int addBalance(CommandContext<CommandSourceStack> context, ServerPlayer targetPlayer, long amount) {
        try {
            BalanceManager.addPlayerBalance(targetPlayer, amount);
            context.getSource().sendSuccess(() -> Component.literal("Added " + amount + " to " + targetPlayer.getName().getString() + "'s balance."), false);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //=============== Subtract ===============\\
    private static int subtractBalanceSelf(CommandContext<CommandSourceStack> context, long amount) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            return subtractBalance(context, player, amount);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    private static int subtractBalance(CommandContext<CommandSourceStack> context, ServerPlayer targetPlayer, long amount) {
        try {
            BalanceManager.subtractPlayerBalance(targetPlayer, amount);
            context.getSource().sendSuccess(() -> Component.literal("Subtracted " + amount + " from " + targetPlayer.getName().getString() + "'s balance."), false);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}


/*
    /balance get (get's the current balance of the executing player) [default]
    /balance get <player:Player> (get's the balance of the specified player) [admin]

    /balance set <amount:long> (sets the total balance of the executing player to the specified amount) [admin]
    /balance set <player:Player> <amount:long> (sets the total balance of the specified player to the specified amount) [admin]

    /balance add <amount:long> (adds the specified amount to the total balance of the executing player) [admin]
    /balance add <player:Player> <amount:long> (adds the specified amount to the total balance of the specified player) [admin]

    /balance subtract <amount:long> (subtracts the specified amount from the total balance of the executing player) [admin]
    /balance subtract <player:Player> <amount:long> (subtracts the specified amount from the total balance of the specified player) [admin]
*/