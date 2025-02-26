package net.fightingpainter.mc.coined;

import net.fightingpainter.mc.coined.commands.MoneyCommand;
import net.fightingpainter.mc.coined.currency.BalanceManager;
import net.fightingpainter.mc.coined.items.CoinItem;
import net.fightingpainter.mc.coined.items.MoneyBagItem;
import net.fightingpainter.mc.coined.util.Money;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ContainerScreenEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
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


    //Balance Data Loading
    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
        Level world = (Level) event.getLevel();
        if (!world.isClientSide()) {
            MinecraftServer server = world.getServer();
            if (server != null) {
                BalanceManager.loadBalanceData(server);
            } else {
                Coined.LOGGER.error("Server is null! Can't load balance data!");
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        MoneyCommand.register(event.getDispatcher());
    }
    
}
