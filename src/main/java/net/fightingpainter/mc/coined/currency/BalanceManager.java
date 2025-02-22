package net.fightingpainter.mc.coined.currency;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.fightingpainter.mc.coined.Coined;
import net.fightingpainter.mc.coined.util.Money;
import net.fightingpainter.mc.coined.util.PlayerBalance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;


public class BalanceManager {
    private static Path balanceDataPath; //path to the balance data file

    //============================== Server Data Loader ==============================\\
    /**
     * Loads the balance data for the server (this needs to be called on server start, reload and other such things. has to be called before any other methods can be used)
     * @param server The server to load the data for
    */
    public static void loadBalanceData(MinecraftServer server) {
        Coined.LOGGER.info("Loading Balance Data File...");
        Path dataFolder = server.getServerDirectory().resolve("data"); //get the data folder
        balanceDataPath = dataFolder.resolve("balance.json"); //get the path to the balance data file
    }

    private static PlayerBalance getPeeBalls(Player player) { ///please semd help i am dying inside aAAAAAAAAAAAAaaaaa
        return new PlayerBalance(player.getUUID().toString(), balanceDataPath);
    }

    //============================== Public Handeling ==============================\\
    /**
     * Gets the balance of a player
     * @param player The player to get the balance of
     * @return The balance of the player
    */
    public static Money getBalance(Player player) {
        return getPeeBalls(player).getBalance();
    }

    /**
     * Sets the balance of a player (this will overwrite the current balance of the player) Note: the balance will automatically be set to 0 if it is negative
     * @param player The player to set the balance of
     * @param balance The new balance of the player
    */
    public static void setBalance(Player player, Money newBalance) {
        getPeeBalls(player).setBalance(newBalance);
    }
}
