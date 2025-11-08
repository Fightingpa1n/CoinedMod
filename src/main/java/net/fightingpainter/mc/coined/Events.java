package net.fightingpainter.mc.coined;

import net.fightingpainter.mc.coined.commands.MoneyCommand;
import net.fightingpainter.mc.coined.data.SyncHelper;
import net.fightingpainter.mc.coined.gui.currency.BalanceManager;
import net.fightingpainter.mc.coined.items.CoinItem;
import net.fightingpainter.mc.coined.items.MoneyBagItem;
import net.fightingpainter.mc.coined.util.Money;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ContainerScreenEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@EventBusSubscriber(modid = Coined.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class Events {

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) { //server starting
        // Do something when the server starts
        Coined.LOGGER.info("Heey! that's cool! I'm inside onServerStarting! it's more spacious here so you can come and join me! :D");
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) { //TODO: move to commands package
        MoneyCommand.register(event.getDispatcher());
    }


    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer sp) SyncHelper.sync(sp, sp);
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof Player target && event.getEntity() instanceof ServerPlayer watcher) SyncHelper.sync(watcher, target);
    }



}
