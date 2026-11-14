package net.fightingpainter.mc.coined.util;

import java.util.ArrayList;
import java.util.List;
import net.fightingpainter.mc.coined.gui.currency.BalanceManager;
import net.fightingpainter.mc.coined.gui.currency.CoinType;
import net.fightingpainter.mc.coined.items.CurrencyItem;
import net.fightingpainter.mc.coined.items.MoneyBagItem;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;

/** All inventory/balance transfers are performed on the server thread. */
public final class CurrencyTransactions {
    private CurrencyTransactions() {}
    public static long value(ItemStack stack) {
        return stack.getItem() instanceof CurrencyItem currency ? currency.getValue(stack) : 0;
    }
    public static boolean deposit(ServerPlayer player, ItemStack stack) {
        long value = value(stack);
        if (value <= 0 || value > Long.MAX_VALUE - BalanceManager.getPlayerBalance(player)) return false;
        BalanceManager.addPlayerBalance(player, value);
        stack.setCount(0);
        feedback(player, "message.coined.deposited", value);
        return true;
    }
    public static void depositAll(ServerPlayer player) {
        long total = 0;
        var inventory = player.getInventory();
        // Preflight the whole transfer so overflow never consumes currency.
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            long value = value(inventory.getItem(i));
            if (value > Long.MAX_VALUE - total) { error(player, "message.coined.balance_full"); return; }
            total += value;
        }
        if (total > Long.MAX_VALUE - BalanceManager.getPlayerBalance(player)) {
            error(player, "message.coined.balance_full"); return;
        }
        if (total == 0) { error(player, "message.coined.no_currency"); return; }
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (inventory.getItem(i).getItem() instanceof CurrencyItem) inventory.setItem(i, ItemStack.EMPTY);
        }
        BalanceManager.addPlayerBalance(player, total);
        inventory.setChanged();
        player.containerMenu.broadcastChanges();
        feedback(player, "message.coined.deposited", total);
    }
    public static void withdraw(ServerPlayer player, long amount, boolean coins) {
        if (amount <= 0 || !BalanceManager.hasEnough(player, amount)) {
            error(player, "message.coined.invalid_withdrawal"); return;
        }
        // Bound entity creation per request, including requests from modified clients.
        if (stackCount(amount, coins) > 4096) { error(player, "message.coined.withdrawal_too_large"); return; }
        List<ItemStack> stacks = createStacks(amount, coins);
        BalanceManager.subtractPlayerBalance(player, amount);
        for (ItemStack stack : stacks) {
            player.getInventory().add(stack);
            if (!stack.isEmpty()) player.drop(stack, false);
        }
        player.getInventory().setChanged();
        player.containerMenu.broadcastChanges();
        feedback(player, "message.coined.withdrew", amount);
    }
    public static long stackCount(long amount, boolean coins) {
        long count = 0;
        CoinType[] types = CoinType.values();
        for (int i = types.length - 1; i >= 0; i--) {
            long quantity = amount / types[i].getValue();
            amount %= types[i].getValue();
            int capacity = coins ? types[i].getItem().getDefaultMaxStackSize() : types[i].getMaxBagAmount();
            long stacks = quantity / capacity + (quantity % capacity == 0 ? 0 : 1);
            count = coins ? count + stacks : Math.max(count, stacks);
        }
        return count;
    }
    public static List<ItemStack> createDropStacks(long amount) {
        if (stackCount(amount, false) <= 4096) return createStacks(amount, false);
        List<ItemStack> result = new ArrayList<>();
        while (amount > 0) {
            long part = Math.min(amount, (long)Integer.MAX_VALUE * 1000000L);
            result.add(MoneyBagItem.createMoneyBag(Money.fromValue(part)));
            amount -= part;
        }
        return result;
    }
    public static List<ItemStack> createStacks(long amount, boolean coins) {
        List<ItemStack> result = new ArrayList<>();
        long[] quantities = new long[CoinType.values().length];
        for (int i = quantities.length - 1; i >= 0; i--) {
            CoinType type = CoinType.values()[i];
            quantities[i] = amount / type.getValue();
            amount %= type.getValue();
        }
        if (coins) {
            for (int i = quantities.length - 1; i >= 0; i--) {
                CoinType type = CoinType.values()[i];
                while (quantities[i] > 0) {
                    int count = (int)Math.min(quantities[i], type.getItem().getDefaultMaxStackSize());
                    result.add(new ItemStack(type.getItem(), count));
                    quantities[i] -= count;
                }
            }
        } else {
            boolean remaining;
            do {
                Money money = new Money();
                remaining = false;
                for (int i = 0; i < quantities.length; i++) {
                    CoinType type = CoinType.values()[i];
                    int count = (int)Math.min(quantities[i], type.getMaxBagAmount());
                    type.setAmountInMoney(money, count);
                    quantities[i] -= count;
                    remaining |= quantities[i] > 0;
                }
                if (!money.isEmpty()) result.add(MoneyBagItem.createMoneyBag(money));
            } while (remaining);
        }
        return result;
    }
    public static void feedback(ServerPlayer player, String key, long amount) {
        player.displayClientMessage(Component.translatable(key, amount), true);
        player.level().playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.5F, 1.2F);
    }
    private static void error(ServerPlayer player, String key) {
        player.displayClientMessage(Component.translatable(key), true);
        player.level().playSound(null, player.blockPosition(), SoundEvents.DISPENSER_FAIL, SoundSource.PLAYERS, 0.4F, 1F);
    }
}
