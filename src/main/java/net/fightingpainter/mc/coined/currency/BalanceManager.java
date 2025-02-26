package net.fightingpainter.mc.coined.currency;

import java.nio.file.Path;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.LevelResource;

import net.fightingpainter.mc.coined.Coined;
import net.fightingpainter.mc.coined.util.Money;
import net.fightingpainter.mc.coined.util.PlayerBalance;


public class BalanceManager {
    private static Path balanceDataPath; //path to the balance data file
    
    //============================== Server Data Loader ==============================\\
    /**
     * Loads the balance data for the server (this needs to be called on server start, reload and other such things. has to be called before any other methods can be used)
     * @param server The server to load the data for
    */
    public static void loadBalanceData(MinecraftServer server) {
        Coined.LOGGER.info("Loading Balance Data File...");
        Path worldFolder = server.getWorldPath(LevelResource.ROOT);
        Path dataFolder = worldFolder.resolve("data"); //get the data folder
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

    /**
     * Adds an amount to the balance of a player
     * @param player The player to add the amount to
     * @param amount The amount to add
    */
    public static void addBalance(Player player, Money amount) {
        getPeeBalls(player).addBalance(amount);
    }

    /**
     * Subtracts an amount from the balance of a player
     * @param player The player to subtract the amount from
     * @param amount The amount to subtract
    */
    public static void subtractBalance(Player player, Money amount) {
        getPeeBalls(player).subtractBalance(amount);
    }

    /**
     * Checks if a player has enough money
     * @param player The player to check
     * @param amount The amount to check for
     * @return True if the player has enough money, false if not
    */
    public static boolean hasEnough(Player player, Money amount) {
        Money balance = getBalance(player);
        return balance.enough(amount);
    }

    /**
     * Pays an amount of money as long as they have enough
     * @param player The player that has to pay
     * @param amount The amount to pay
     * @return True if the player had enough money and the payment was successful, false if not 
    */
    public static boolean pay(Player player, Money amount) {
        if (hasEnough(player, amount)) {
            subtractBalance(player, amount);
            return true;
        }
        return false;
    }


}
