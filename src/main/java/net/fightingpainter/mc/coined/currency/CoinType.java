package net.fightingpainter.mc.coined.currency;

import java.util.ArrayList;
import java.util.List;

import net.fightingpainter.mc.coined.Coined;
import net.fightingpainter.mc.coined.items.CoinItem;
import net.fightingpainter.mc.coined.items.ModItems;
import net.fightingpainter.mc.coined.util.Money;
import net.fightingpainter.mc.coined.util.Txt;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredItem;

/**
 * The Coins Enum
 * it's used to define coin types and their properties
*/
public enum CoinType {
    //======================================== Coin Types ========================================\\
    COPPER( //The copper coin 
        1,
        0xae5b3c,
        99,
        255,
        ModItems.COPPER_COIN,
        "\uE091",
        Coined.MOD_ID+".label.copper"
    ),
    
    SILVER( //The silver coin
        100,
        0x8c8c8c,
        99,
        255,
        ModItems.SILVER_COIN,
        "\uE092",
        Coined.MOD_ID+".label.silver"
    ),
    
    GOLD( //The gold coin
        10000,
        0xffd700,
        99,
        255,
        ModItems.GOLD_COIN,
        "\uE093",
        Coined.MOD_ID+".label.gold"
    ),
    
    PLATINUM( //The platinum coin
        1000000,
        0xd7d3e0,
        99,
        255,
        ModItems.PLATINUM_COIN,
        "\uE094",
        Coined.MOD_ID+".label.platinum"
    );
    
    //--------------------------------------------------------------------------------\\
    private final long value;
    private final int nameColor;
    private final int maxStackSize;
    private final int maxBagAmount;
    private final DeferredItem<Item> item;
    private final String symbol;
    private final String labelTranslationKey;

    /**
     * The Coins Constructor
     * @param value The value of a single coin
     * @param nameColor The color of the coin's name (the color used for all the text and stuff)
     * @param item The item that represents the coin
     * @param maxBagSize The maximum amount of this coin that can be stored in a single MoneyBag
    */
    CoinType(long value, int nameColor, int maxStackSize, int maxBagAmount, DeferredItem<Item> item, String symbol, String labelTranslationKey) {
        this.value = value;
        this.nameColor = nameColor;
        this.maxStackSize = maxStackSize;
        this.maxBagAmount = maxBagAmount;
        this.item = item;
        this.symbol = symbol;
        this.labelTranslationKey = labelTranslationKey;
    }

    //======================================== Getters ========================================\\
    /** returns the value of a single coin of this type */
    public long getValue() {return value;}

    /** returns the value of a specified amount of coins of this type */
    public long getValue(int amount) {return value * amount;}

    /** returns the color of the coin's name */
    public int getNameColor() {return nameColor;}

    /** returns the max stack size of this CoinType */
    public int getMaxStackSize() {return maxStackSize;}

    /** returns the max amount of this CoinType that can be stored in a MoneyBag */
    public int getMaxBagAmount() {return maxBagAmount;}

    /** returns the Item that represents this CoinType */
    public Item getItem() {return item.get();}

    /** returns the Symbol of this CoinType */
    public String getSymbol() {return symbol;}

    /** returns the label translation key of this CoinType */
    public String getLabelTranslationKey() {return labelTranslationKey;}

    //======================================== Money Object Methods ========================================\\
    /**
     * Gets the amount of this CoinType in a Money object
     * @param money the Money object to get the amount from
     * @return the amount of this CoinType in the Money object
     * @throws IllegalArgumentException if money is null
    */
    public int getAmount(Money money) {
        if (money == null) {throw new IllegalArgumentException("Money is null!");}
        return switch (this) {
            case COPPER -> money.getCopperAmount();
            case SILVER -> money.getSilverAmount();
            case GOLD -> money.getGoldAmount();
            case PLATINUM -> money.getPlatinumAmount();
        };
    }

    /**
     * Checks if this CoinType is present in the given Money object
     * @param money the Money object to check
     * @return true if the CoinType is present, false otherwise
     * @throws IllegalArgumentException if money is null
    */
    public boolean inMoney(Money money) {
        if (money == null) {throw new IllegalArgumentException("Money is null!");}
        return switch (this) {
            case COPPER -> money.hasCopper();
            case SILVER -> money.hasSilver();
            case GOLD -> money.hasGold();
            case PLATINUM -> money.hasPlatinum();
        };
    }

    /**
     * Gets the value of this CoinType in a Money object
     * @param money the Money object to get the value from
     * @return the value of this CoinType in the Money object
     * @throws IllegalArgumentException if money is null
    */
    public long getValueFromMoney(Money money) {
        if (money == null) {throw new IllegalArgumentException("Money is null!");}
        return switch (this) {
            case COPPER -> money.getCopperValue();
            case SILVER -> money.getSilverValue();
            case GOLD -> money.getGoldValue();
            case PLATINUM -> money.getPlatinumValue();
        };
    }

    /**
     * Sets the amount of this CoinType in a Money object
     * @param money the Money object to set the amount in
     * @param amount the amount to set
     * @throws IllegalArgumentException if money is null
    */
    public void setAmountInMoney(Money money, int amount) {
        if (money == null) {throw new IllegalArgumentException("Money is null!");}
        switch (this) {
            case COPPER -> money.setCopperAmount(amount);
            case SILVER -> money.setSilverAmount(amount);
            case GOLD -> money.setGoldAmount(amount);
            case PLATINUM -> money.setPlatinumAmount(amount);
        }
    }

    /**
     * Adds the specified amount of this CoinType to a Money object
     * @param money the Money object to add the amount to
     * @param amount the amount to add
     * @throws IllegalArgumentException if money is null
    */
    public void addToMoney(Money money, int amount) {
        if (money == null) {throw new IllegalArgumentException("Money is null!");}
        switch (this) {
            case COPPER -> money.addCopper(amount);
            case SILVER -> money.addSilver(amount);
            case GOLD -> money.addGold(amount);
            case PLATINUM -> money.addPlatinum(amount);
        }
    }

    /**
     * Subtracts the specified amount of this CoinType from a Money object
     * @param money the Money object to subtract from
     * @param amount the amount to subtract
     * @throws IllegalArgumentException if money is null
    */
    public void subtractFromMoney(Money money, int amount) {
        if (money == null) {throw new IllegalArgumentException("Money is null!");}
        switch (this) {
            case COPPER -> money.subtractCopper(amount);
            case SILVER -> money.subtractSilver(amount);
            case GOLD -> money.subtractGold(amount);
            case PLATINUM -> money.subtractPlatinum(amount);
        }
    }

    //======================================== Display Methods ========================================\\
    public Component createLabel(int amount) {
        return Txt.concat(this.getSymbol(), " ", Txt.colored(amount, this.getNameColor()));
    }

    //======================================== Static Methods ========================================\\
    /**
     * Use to get the CoinType of a specified ItemStack
     * @param coin the ItemStack to get the CoinType from
     * @return the CoinType of the ItemStack
    */
    public static CoinType getCoinType(ItemStack item) {
        if (item.getItem() instanceof CoinItem coinItem) {
            return coinItem.getCoinType();
        } else {return null;}
    }

    /**
     * Gets a list of CoinTypes that are present within a given Money object 
     * @param money the Money object to check for CoinTypes
     * @return a list of CoinTypes that are present in the Money object
    */
    public static List<CoinType> getCoinTypesInMoney(Money money) {
        if (money == null || money.isEmpty()) {return List.of();} //if money is null or empty, return an empty list
        List<CoinType> coinTypes = new ArrayList<>();
        for (CoinType coinType : CoinType.values()) {
            if (coinType.inMoney(money)) {
                coinTypes.add(coinType);
            }
        } return coinTypes;
    }
}
