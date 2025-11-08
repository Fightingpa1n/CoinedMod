package net.fightingpainter.mc.coined.gui.currency;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.fightingpainter.mc.coined.data.ModDataTypes;
import net.fightingpainter.mc.coined.data.SyncHelper;
import net.fightingpainter.mc.coined.util.Money;

/** 
 * BalanceManager is a Helper class to manage player balances */
public final class BalanceManager {
    private BalanceManager() {} //private constructor to prevent instantiation

    //======================================== Balance ========================================\\
    //==================== Get ====================\\
    /**
     * Gets the balance of a player from the player's data component
     * @param player The player entity to get the balance of
     * @return The balance of the player as a long value
     */
    public static long getPlayerBalance(Player player) {
        return player.getData(ModDataTypes.PLAYER_BALANCE.get());
    }
    
    /**
     * Gets the balance of a player as a Money object
     * @param player The player entity to get the balance of
     * @return The balance of the player as a Money object
     */
    public static Money getPlayerMoney(Player player) {
        return Money.fromValue(getPlayerBalance(player));
    }
    
    //==================== Set ====================\\
    /**
     * Sets the balance of a player in the player's data component
     * @param player The player entity to set the balance of
     * @param newBalance The new balance to set for the player
     */
    public static void setPlayerBalance(Player player, long newBalance) {
        player.setData(ModDataTypes.PLAYER_BALANCE.get(), newBalance);
        SyncHelper.broadcast(player, newBalance);
    }
    
    /**
     * Sets the balance of a player in the player's data component using a Money object
     * @param player The player entity to set the balance of
     * @param money The Money object to set as the player's balance
     */
    public static void setPlayerBalance(Player player, Money money) {
        setPlayerBalance(player, money.getTotalValue());
    }
    
    //==================== Add ====================\\
    /**
     * Adds an amount to the player's balance
     * @param player The player entity to add the amount to
     * @param amount The amount to add to the player's balance
     */
    public static void addPlayerBalance(Player player, long amount) {
        long newBalance = getPlayerBalance(player) + amount;
        setPlayerBalance(player, newBalance);
    }
    
    /**
     * Adds an amount to the player's balance using a Money object
     * @param player The player entity to add the amount to
     * @param money The Money object to add to the player's balance
     */
    public static void addPlayerBalance(Player player, Money money) {
        addPlayerBalance(player, money.getTotalValue());
    }
    
    //==================== Subtract ====================\\
    /**
     * Subtracts an amount from the player's balance
     * @param player The player entity to subtract the amount from
     * @param amount The amount to subtract from the player's balance
     */
    public static void subtractPlayerBalance(Player player, long amount) {
        long newBalance = Math.max(0, getPlayerBalance(player) - amount); //prevent negative balance
        setPlayerBalance(player, newBalance);
    }
    
    /**
     * Subtracts an amount from the player's balance using a Money object
     * @param player The player entity to subtract the amount from
     * @param money The Money object to subtract from the player's balance
     */
    public static void subtractPlayerBalance(Player player, Money money) {
        subtractPlayerBalance(player, money.getTotalValue());
    }
    
    //==================== has Enough ====================\\
    /**
     * Checks if the player has enough balance to cover the specified amount
     * @param player The player entity to check the balance of
     * @param amount The amount to check against the player's balance
     * @return True if the player has enough balance, false otherwise
     */
    public static boolean hasEnough(Player player, long amount) {
        return getPlayerBalance(player) >= amount;
    }
    
    /**
     * Checks if the player has enough balance to cover the specified Money object
     * @param player The player entity to check the balance of
     * @param money The Money object to check against the player's balance
     * @return True if the player has enough balance, false otherwise
     */
    public static boolean hasEnough(Player player, Money money) {
        return hasEnough(player, money.getTotalValue());
    }
    
    //==================== Try Subtract ====================\\
    /**
     * Attempts to subtract an amount from the player's balance
     * @param player The player entity to attempt to subtract the amount from
     * @param amount The amount to attempt to subtract from the player's balance
     * @return True if the subtraction was successful, false if the player did not have enough balance
     */
    public static boolean trySubstract(Player player, long amount) {
        if (hasEnough(player, amount)) {
            subtractPlayerBalance(player, amount);
            return true;
        } return false;
    }
    
    /**
     * Attempts to subtract a Money object from the player's balance
     * @param player The player entity to attempt to subtract the Money object from
     * @param money The Money object to attempt to subtract from the player's balance
     * @return True if the subtraction was successful, false if the player did not have enough balance
     */
    public static boolean trySubstract(Player player, Money money) {
        return trySubstract(player, money.getTotalValue());
    }

    //======================================= Client Only ========================================\\
    /**
     * Gets the current player from the Minecraft instance
     * @return The current player entity
    */
    @OnlyIn(Dist.CLIENT)
    public static Player getCurrentPlayer() {
        return Minecraft.getInstance().player;
    }
}
