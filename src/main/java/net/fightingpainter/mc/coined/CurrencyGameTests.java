package net.fightingpainter.mc.coined;

import net.fightingpainter.mc.coined.items.CurrencyItem;
import net.fightingpainter.mc.coined.items.MoneyBagItem;
import net.fightingpainter.mc.coined.util.CurrencyTransactions;
import net.fightingpainter.mc.coined.util.Money;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(Coined.MOD_ID)
@PrefixGameTestTemplate(false)
public final class CurrencyGameTests {
    @GameTest(template = "empty")
    public static void inventoryPickupAndDeath(GameTestHelper helper) {
        var cookie = net.minecraft.server.network.CommonListenerCookie.createInitial(new com.mojang.authlib.GameProfile(java.util.UUID.randomUUID(), "currency-test"), false);
        var player = new net.minecraft.server.level.ServerPlayer(helper.getLevel().getServer(), helper.getLevel(), cookie.gameProfile(), cookie.clientInformation());
        var connection = new net.minecraft.network.Connection(net.minecraft.network.protocol.PacketFlow.SERVERBOUND);
        player.connection = new net.minecraft.server.network.ServerGamePacketListenerImpl(player.getServer(), connection, player, cookie) {
            @Override
            public void send(net.minecraft.network.protocol.Packet<?> packet, net.minecraft.network.PacketSendListener listener) {
                // There is no negotiated client in a server-only test.
            }
        };
        var inventory = player.getInventory();
        inventory.clearContent();
        inventory.setItem(0, new net.minecraft.world.item.ItemStack(net.fightingpainter.mc.coined.items.ModItems.COPPER_COIN.get(), 50));
        inventory.setItem(40, MoneyBagItem.createMoneyBag(Money.fromValue(12345)));
        inventory.setItem(1, new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.DIAMOND));
        CurrencyTransactions.depositAll(player);
        helper.assertTrue(net.fightingpainter.mc.coined.gui.currency.BalanceManager.getPlayerBalance(player) == 12395, "Deposit missed inventory or offhand currency");
        helper.assertTrue(inventory.getItem(0).isEmpty() && inventory.getItem(40).isEmpty() && inventory.getItem(1).is(net.minecraft.world.item.Items.DIAMOND), "Deposit removed unrelated items or left currency");
        CurrencyTransactions.withdraw(player, 12395, false);
        helper.assertTrue(net.fightingpainter.mc.coined.gui.currency.BalanceManager.getPlayerBalance(player) == 0, "Withdrawal did not debit balance");
        long value = 0;
        for (int i = 0; i < inventory.getContainerSize(); i++) value += CurrencyTransactions.value(inventory.getItem(i));
        helper.assertTrue(value == 12395, "Withdrawal did not give currency");
        for (int i = 0; i < inventory.getContainerSize(); i++) inventory.setItem(i, new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.STONE, 64));
        player.setData(net.fightingpainter.mc.coined.data.ModDataTypes.AUTO_DEPOSIT, true);
        var item = new net.minecraft.world.entity.item.ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), MoneyBagItem.createMoneyBag(Money.fromValue(199)));
        var pickup = new net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent.Pre(player, item);
        CurrencyEvents.pickup(pickup);
        helper.assertTrue(item.isRemoved() && net.fightingpainter.mc.coined.gui.currency.BalanceManager.getPlayerBalance(player) == 199, "Full inventory prevented auto deposit");
        var rules = player.level().getGameRules();
        int old = rules.getInt(CurrencyEvents.BALANCE_DROP_PERCENTAGE);
        try {
            rules.getRule(CurrencyEvents.BALANCE_DROP_PERCENTAGE).set(25, player.getServer());
            var drops = new net.neoforged.neoforge.event.entity.living.LivingDropsEvent(player, player.damageSources().generic(), new java.util.ArrayList<>(), false);
            CurrencyEvents.drops(drops);
            helper.assertTrue(net.fightingpainter.mc.coined.gui.currency.BalanceManager.getPlayerBalance(player) == 150, "Death percentage rounding incorrect");
            helper.assertTrue(drops.getDrops().stream().mapToLong(drop -> CurrencyTransactions.value(drop.getItem())).sum() == 49, "Death drop did not match deducted value");
        } finally {
            rules.getRule(CurrencyEvents.BALANCE_DROP_PERCENTAGE).set(old, player.getServer());
            player.discard();
        }
        helper.succeed();
    }
    @GameTest(template = "empty")
    public static void withdrawalsConserveValue(GameTestHelper helper) {
        long[] values = {1, 99, 100, 10000, 1234567, 255999999, 256000001, 1000000000};
        for (long value : values) for (boolean coins : new boolean[]{false, true}) {
            var stacks = CurrencyTransactions.createStacks(value, coins);
            long actual = stacks.stream().mapToLong(CurrencyTransactions::value).sum();
            helper.assertTrue(actual == value, "Withdrawal changed value: " + value);
            helper.assertTrue(stacks.size() == CurrencyTransactions.stackCount(value, coins), "Incorrect stack estimate");
            for (var stack : stacks) {
                helper.assertTrue(stack.getCount() <= stack.getMaxStackSize(), "Oversized stack");
                if (!coins) for (var type : net.fightingpainter.mc.coined.gui.currency.CoinType.values())
                    helper.assertTrue(type.getAmount(CurrencyItem.getMoneyData(stack)) <= type.getMaxBagAmount(), "Bag over capacity");
            }
        }
        helper.succeed();
    }
    @GameTest(template = "empty")
    public static void bagMergeConservesValue(GameTestHelper helper) {
        var first = MoneyBagItem.createMoneyBag(new Money(250, 250, 250, 250));
        var second = MoneyBagItem.createMoneyBag(new Money(20, 20, 20, 20));
        long before = CurrencyTransactions.value(first) + CurrencyTransactions.value(second);
        var rest = MoneyBagItem.addBagToBag(first, second);
        helper.assertTrue(CurrencyTransactions.value(first) + CurrencyTransactions.value(rest) == before, "Merging duplicated or lost money");
        var copy = first.copy();
        var data = CurrencyItem.getMoneyData(first);
        data.setCopperAmount(0);
        helper.assertTrue(CurrencyTransactions.value(first) == CurrencyTransactions.value(copy), "Reading bag data mutated component");
        helper.succeed();
    }
    @GameTest(template = "empty")
    public static void extremeDropsConserveValue(GameTestHelper helper) {
        var stacks = CurrencyTransactions.createDropStacks(Long.MAX_VALUE);
        long actual = stacks.stream().mapToLong(CurrencyTransactions::value).sum();
        helper.assertTrue(actual == Long.MAX_VALUE && stacks.size() < 5000, "Extreme balance lost value or created excessive drops");
        helper.succeed();
    }
}
