package net.fightingpainter.mc.coined;

import net.fightingpainter.mc.coined.data.ModDataTypes;
import net.fightingpainter.mc.coined.data.SyncHelper;
import net.fightingpainter.mc.coined.gui.currency.BalanceManager;
import net.fightingpainter.mc.coined.util.CurrencyTransactions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = Coined.MOD_ID)
public final class CurrencyEvents {
    public static final GameRules.Key<GameRules.IntegerValue> BALANCE_DROP_PERCENTAGE = GameRules.register(
        "BalanceDropPercentage", GameRules.Category.PLAYER, GameRules.IntegerValue.create(0, (server, value) -> {
            int clamped = Math.clamp(value.get(), 0, 100);
            if (value.get() != clamped) value.set(clamped, server);
        }));
    public static void init() {} // Force registration before worlds are created.

    private static TagKey<EntityType<?>> tier(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Coined.MOD_ID, name));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void drops(LivingDropsEvent event) {
        var entity = event.getEntity();
        if (entity.level().isClientSide()) return;
        long amount = 0;
        if (entity instanceof ServerPlayer player) {
            int percentage = Math.clamp(player.level().getGameRules().getInt(BALANCE_DROP_PERCENTAGE), 0, 100);
            long balance = BalanceManager.getPlayerBalance(player);
            amount = (balance / 100) * percentage + (balance % 100) * percentage / 100;
            BalanceManager.subtractPlayerBalance(player, amount);
        } else if (entity instanceof Mob && entity.level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            String[] tiers = {"boss", "miniboss", "uncommon", "common"};
            int[] minimum = {10000, 1000, 100, 1};
            int[] maximum = {50000, 5000, 500, 50};
            for (int i = 0; i < tiers.length; i++) {
                if (entity.getType().is(tier(tiers[i])) || entity.getTags().contains(tiers[i]) || entity.getTags().contains("coined:" + tiers[i])) {
                    amount = minimum[i] + entity.getRandom().nextInt(maximum[i] - minimum[i] + 1);
                    break;
                }
            }
        }
        if (amount <= 0) return;
        // For extreme command-created balances use large bags rather than millions of entities.
        for (var stack : CurrencyTransactions.createDropStacks(amount)) {
            ItemEntity drop = new ItemEntity(entity.level(), entity.getX(), entity.getY() + 0.25, entity.getZ(), stack);
            drop.setDefaultPickUpDelay();
            event.getDrops().add(drop);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void pickup(ItemEntityPickupEvent.Pre event) {
        if (!(event.getPlayer() instanceof ServerPlayer player) || !player.isAlive() || player.isSpectator()
            || !player.getData(ModDataTypes.AUTO_DEPOSIT) || event.canPickup().isFalse()) return;
        ItemEntity item = event.getItemEntity();
        if (!event.canPickup().isTrue() && (item.hasPickUpDelay() || item.getTarget() != null && !item.getTarget().equals(player.getUUID()))) return;
        int count = item.getItem().getCount();
        if (CurrencyTransactions.deposit(player, item.getItem())) {
            player.take(item, count);
            player.onItemPickup(item);
            item.discard();
            event.setCanPickup(TriState.FALSE);
        }
    }
    @SubscribeEvent
    public static void respawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) SyncHelper.sync(player, player);
    }
    @SubscribeEvent
    public static void dimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) SyncHelper.sync(player, player);
    }
}
