package net.fightingpainter.mc.coined.util;

import java.util.Objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fightingpainter.mc.coined.currency.CoinType;
import net.fightingpainter.mc.coined.util.types.Dict;

/**
 * The Money Class is a helper class to store Currency amounts and helps with conversion and other stuff
 * Think of it as a Smart Money Bag that you can put money into and you can ask it stuff
 * Note: it stores stuff as individual coin amounts rather than the total value
*/
public class Money {
    protected int copper_coin_amount = 0; //amount of stored copper coins
    protected int silver_coin_amount = 0; //amount of stored silver coins
    protected int gold_coin_amount = 0; //amount of stored gold coins
    protected int platinum_coin_amount = 0; //amount of stored platinum coins

    //============================== Constructors ==============================\\
    /** 
     * An Empty Constructor that will create a Money Object with no money stored 
    */
    public Money() {}
    
    /**
     * A constructor that takes each individual amount of each coin type (will be set to 0 if negative)
     * @param copper_coin_amount the amount of copper coins that should be stored
     * @param silver_coin_amount the amount of silver coins that should be stored
     * @param gold_coin_amount the amount of gold coins that should be stored
     * @param platinum_coin_amount the amount of platinum coins that should be stored
     */
    public Money(int copper_coin_amount, int silver_coin_amount, int gold_coin_amount, int platinum_coin_amount) {
        this.copper_coin_amount = copper_coin_amount > 0 ? copper_coin_amount : 0;
        this.silver_coin_amount = silver_coin_amount > 0 ? silver_coin_amount : 0;
        this.gold_coin_amount = gold_coin_amount > 0 ? gold_coin_amount : 0;
        this.platinum_coin_amount = platinum_coin_amount > 0 ? platinum_coin_amount : 0;
    }
    
    /**
     * Constructs a Money Object from a Total Value by sorting it into individual coin amounts from highest to lowest
     * @param money the total amount of money that should be stored
     */
    public Money(long money) {
        Money sortedMoney = sort(money); //sort the money from biggest to smallest
        copper_coin_amount = sortedMoney.getCopperAmount();
        silver_coin_amount = sortedMoney.getSilverAmount();
        gold_coin_amount = sortedMoney.getGoldAmount();
        platinum_coin_amount = sortedMoney.getPlatinumAmount();
    }

    //============================== Operations ==============================\\
    //=============== Get ===============\\
    /**
     * Returns the amount of Copper Coins Stored
     * @return copper coin amount
    */
    public int getCopperAmount() {
        return copper_coin_amount;
    }

    /**
     * Returns the amount of Silver Coins Stored
     * @return silver coin amount
    */
    public int getSilverAmount() {
        return silver_coin_amount;
    }

    /**
     * Returns the amount of Gold Coins Stored
     * @return gold coin amount
    */
    public int getGoldAmount() {
        return gold_coin_amount;
    }

    /**
     * Returns the amount of Platinum Coins Stored
     * @return platinum coin amount
    */
    public int getPlatinumAmount() {
        return platinum_coin_amount;
    }
    
    //=============== Get (Value) ===============\\
    /**
     * Returns the Total Value of all the Copper Coins Stored
     * @return the total value of the copper coins
     * @see CoinType#getValue(int)
    */
    public long getCopperValue() {
        return CoinType.COPPER.getValue(copper_coin_amount);
    }

    /** 
     * Returns the Total Value of all the Silver Coins Stored
     * @return the total value of the silver coins
     * @see CoinType#getValue(int)
    */
    public long getSilverValue() {
        return CoinType.SILVER.getValue(silver_coin_amount);
    }

    /** 
     * Returns the Total Value of all the Gold Coins Stored
     * @return the total value of the gold coins
     * @see CoinType#getValue(int)
    */
    public long getGoldValue() {
        return CoinType.GOLD.getValue(gold_coin_amount);
    }

    /** 
     * Returns the Total Value of all the Platinum Coins Stored
     * @return the total value of the platinum coins
     * @see CoinType#getValue(int)
    */
    public long getPlatinumValue() {
        return CoinType.PLATINUM.getValue(platinum_coin_amount);
    }

    /**
     * Returns the Total Value of all the Coins Stored inside the Money Object (basically how much money is this bag)
     * @return total money in this money object
    */
    public long getTotalValue() {return getCopperValue() + getSilverValue() + getGoldValue() + getPlatinumValue();}

    //=============== Set ===============\\
    /** Sets the amount of Copper Coins Stored (will be set to 0 if negative)
     * @param amount the amount of copper coins that should be stored
    */
    public void setCopperAmount(int amount) {
        copper_coin_amount = amount > 0 ? amount : 0;
    }

    /** Sets the amount of Silver Coins Stored (will be set to 0 if negative)
     * @param amount the amount of silver coins that should be stored
    */
    public void setSilverAmount(int amount) {
        silver_coin_amount = amount > 0 ? amount : 0;
    }

    /** Sets the amount of Gold Coins Stored (will be set to 0 if negative)
     * @param amount the amount of gold coins that should be stored
    */
    public void setGoldAmount(int amount) {
        gold_coin_amount = amount > 0 ? amount : 0;
    }

    /** Sets the amount of Platinum Coins Stored (will be set to 0 if negative)
     * @param amount the amount of platinum coins that should be stored
    */
    public void setPlatinumAmount(int amount) {
        platinum_coin_amount = amount > 0 ? amount : 0;
    }
    
    //=============== Has ===============\\
    /**
     * Checks if the Money Object has any Copper Coins (amount > 0)
     * @return true if there are any copper coins stored otherwise false
    */
    public boolean hasCopper() {
        return copper_coin_amount > 0;
    }

    /**
     * Checks if the Money Object has any Silver Coins (amount > 0)
     * @return true if there are any silver coins stored otherwise false
    */
    public boolean hasSilver() {
        return silver_coin_amount > 0;
    }

    /**
     * Checks if the Money Object has any Gold Coins (amount > 0)
     * @return true if there are any gold coins stored otherwise false
    */
    public boolean hasGold() {
        return gold_coin_amount > 0;
    }

    /**
     * Checks if the Money Object has any Platinum Coins (amount > 0)
     * @return true if there are any platinum coins stored otherwise false
    */
    public boolean hasPlatinum() {
        return platinum_coin_amount > 0;
    }

    /**
     * Checks if the Money Object has any coins inside
     * @return true if there are any coins stored otherwise false
     * @see #hasMoney()
    */
    public boolean hasCoins() {
        return copper_coin_amount > 0 || silver_coin_amount > 0 || gold_coin_amount > 0 || platinum_coin_amount > 0;
    }

    /**
     * Checks if the Money Object has any Money inside
     * @return true if there is any money stored otherwise false
     * @see #hasCoins()
    */
    public boolean hasMoney() {
        return getTotalValue() > 0;
    }

    /**
     * Checks if the Money Object has no Money inside
     * @return true if there is no money stored otherwise false
    */
    public boolean isEmpty() {
        return getTotalValue() > 0;
    }
    
    //============================== Methods ==============================\\
    //=============== Set ===============\\
    /**
     * Sets the total value of the Money Object to the given value by converting it to individual coin amounts from highest to lowest
     * (eg. if you give a Value of 1'234'567 it will convert it to 1 Platinum, 23 Gold, 45 Silver, 67 Copper)
     * @param value the total value that should be stored
    */
    public void setTotalValue(long value) {
        Money sortedMoney = sort(value); //sort the money from biggest to smallest
        copper_coin_amount = sortedMoney.getCopperAmount();
        silver_coin_amount = sortedMoney.getSilverAmount();
        gold_coin_amount = sortedMoney.getGoldAmount();
        platinum_coin_amount = sortedMoney.getPlatinumAmount();
    }
     
    //=============== Add ===============\\
    /** 
     * Adds the given Money Objects to the current Money Objects
     * (it adds the given Money Objects Coin amounts to the current Money Objects Coin amounts)
     * @param money the Money Object that should be added
     * @return itself (so you can chain stuff)
    */
    public Money add(Money money) { 
        copper_coin_amount += money.getCopperAmount();
        silver_coin_amount += money.getSilverAmount();
        gold_coin_amount += money.getGoldAmount();
        platinum_coin_amount += money.getPlatinumAmount();
        return this;
    }
    
    /**
     * Adds the Given Coin Amounts to the current Money Objects Coin amounts
     * @param copper the amount of copper coins that should be added
     * @param silver the amount of silver coins that should be added
     * @param gold the amount of gold coins that should be added
     * @param platinum the amount of platinum coins that should be added
     * @return itself (so you can chain stuff)
    */
    public Money add(int copper, int silver, int gold, int platinum) {
        copper_coin_amount += copper;
        silver_coin_amount += silver;
        gold_coin_amount += gold;
        platinum_coin_amount += platinum;
        return this;
    }
    
    /**
     * Adds the Given Value to the current Money Objects coin amounts
     * (the value will be converted to individual coin amounts and then added to the current Money Objects Coin amounts)
     * @param value the total value of coins that should be added
     * @return itself (so you can chain stuff)
    */
    public Money add(long value) {
        Money money = new Money(value);
        copper_coin_amount += money.getCopperAmount();
        silver_coin_amount += money.getSilverAmount();
        gold_coin_amount += money.getGoldAmount();
        platinum_coin_amount += money.getPlatinumAmount();
        return this;
    }

    //=============== Subtract ===============\\
    /**
     * Subtracts the given Money Objects from the current Money Objects (will be set to 0 if negative)
     * (it subtracts the given Money Objects Coin amounts from the current Money Objects Coin amounts)
     * @param money the Money Object that should be subtracted
     * @return itself (so you can chain stuff)
    */
    public Money subtract(Money money) {
        copper_coin_amount = Math.max(0, copper_coin_amount - money.getCopperAmount());
        silver_coin_amount = Math.max(0, silver_coin_amount - money.getSilverAmount());
        gold_coin_amount = Math.max(0, gold_coin_amount - money.getGoldAmount());
        platinum_coin_amount = Math.max(0, platinum_coin_amount - money.getPlatinumAmount());
        return this;
    }

    /**
     * Subtracts the Given Coin Amounts from the current Money Objects Coin amounts (will be set to 0 if negative)
     * @param copper the amount of copper coins that should be subtracted
     * @param silver the amount of silver coins that should be subtracted
     * @param gold the amount of gold coins that should be subtracted
     * @param platinum the amount of platinum coins that should be subtracted
     * @return itself (so you can chain stuff)
    */
    public Money subtract(int copper, int silver, int gold, int platinum) {
        copper_coin_amount = Math.max(0, copper_coin_amount - copper);
        silver_coin_amount = Math.max(0, silver_coin_amount - silver);
        gold_coin_amount = Math.max(0, gold_coin_amount - gold);
        platinum_coin_amount = Math.max(0, platinum_coin_amount - platinum);
        return this;
    }

    /**
     * Subtracts the Given Value from the current Money Objects coin amounts (will be set to 0 if negative)
     * (the value will be converted to individual coin amounts and then subtracted from the current Money Objects Coin amounts)
     * @param value the total value of coins that should be subtracted
     * @return itself (so you can chain stuff)
    */
    public Money subtract(long value) {
        Money money = new Money(value);
        copper_coin_amount = Math.max(0, copper_coin_amount - money.getCopperAmount());
        silver_coin_amount = Math.max(0, silver_coin_amount - money.getSilverAmount());
        gold_coin_amount = Math.max(0, gold_coin_amount - money.getGoldAmount());
        platinum_coin_amount = Math.max(0, platinum_coin_amount - money.getPlatinumAmount());
        return this;
    }

    //=============== Enough ===============\\
    /**
     * Checks if the Money Object has more or equal to the given Money Object
     * @param money the Money that should be checked against
     * @return if the current Money Object has more or equal to the given Money Object returns true otherwise false
    */
    public boolean enough(Money money) {
        return getTotalValue() >= money.getTotalValue();
    }

    /**
     * Checks if the Money Object has more or equal to the given Coin Amounts
     * @param copper the amount of copper coins that should be checked against
     * @param silver the amount of silver coins that should be checked against
     * @param gold the amount of gold coins that should be checked against
     * @param platinum the amount of platinum coins that should be checked against
     * @return if the current Money Object has more or equal to the given Coin Amounts returns true otherwise false
    */
    public boolean enough(int copper, int silver, int gold, int platinum) {
        return getTotalValue() >= new Money(copper, silver, gold, platinum).getTotalValue();
    }

    /**
     * Checks if the Money Object has more or equal to the given Value
     * @param value the total value of coins that should be checked against
     * @return if the current Money Object has more or equal to the given Value returns true otherwise false
    */
    public boolean enough(long value) {
        return getTotalValue() >= value;
    }
    
    //=============== Other ===============\\
    /**
     * Takes the Total value of Stored Coins and Sorts them Anew
     * (this is useful if you have like 1'234'567 copper coins stored in here, then this will turn it into 1 Platinum, 23 Gold, 45 Silver, 67 Copper)
     * @return itself (so you can chain stuff)
    */
    public Money flatten() {
        setTotalValue(getTotalValue());
        return this;
    }
    
    /**
     * Copy the Money Object
     * @return a copy of the Money Object
    */
    public Money copy() {return new Money(copper_coin_amount, silver_coin_amount, gold_coin_amount, platinum_coin_amount);}
    
    /**
     * Returns the Money Object as a readable String (ment for debugging)
     * @param format if true it will format the string to be more readable by using new lines and tabs
     * @return A String Ready to be printed or logged elsewhere
    */
    public String toString(boolean format) {
        if (format) {return String.format("Total: %d (\n\tCopper: %d,\n\tSilver: %d,\n\tGold: %d,\n\tPlatinum: %d\n)", getTotalValue(), copper_coin_amount, silver_coin_amount, gold_coin_amount, platinum_coin_amount);}
        else {return String.format("total: %d (copper: %d, silver: %d, gold: %d, platinum: %d)", getTotalValue(), copper_coin_amount, silver_coin_amount, gold_coin_amount, platinum_coin_amount);}
    }

    /**
     * Returns the Money Object as a readable String (ment for debugging)
     * @return A String Ready to be printed or logged elsewhere
    */
    public String toString() {
        return toString(false);
    }

    
    //============================== Conversions ==============================\\
    //=============== Dict ===============\\
    /**
     * Converts the Money Object's Coin amounts to a Dictionary
     * @return the Dict that represents the Money Object
    */
    public Dict toDict() {
        Dict dict = new Dict();
        dict.set("copper", copper_coin_amount);
        dict.set("silver", silver_coin_amount);
        dict.set("gold", gold_coin_amount);
        dict.set("platinum", platinum_coin_amount);
        return dict;
    }

    /**
     * Creates a Money Object from a Dictionary
     * @param dict the Dict that should be used to create the Money Object
     * @return the created Money Object
    */
    public static Money fromDict(Dict dict) {
        return new Money(
            (int) dict.get("copper", 0),
            (int) dict.get("silver", 0),
            (int) dict.get("gold", 0),
            (int) dict.get("platinum", 0)
        );
    }

    //=============== Nbt ===============\\
    public static final Codec<Money> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("Copper").forGetter(Money::getCopperAmount),
            Codec.INT.fieldOf("Silver").forGetter(Money::getSilverAmount),
            Codec.INT.fieldOf("Gold").forGetter(Money::getGoldAmount),
            Codec.INT.fieldOf("Platinum").forGetter(Money::getPlatinumAmount)
        ).apply(instance, Money::new));

    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Compare memory references
        if (o == null || getClass() != o.getClass()) return false; // Check for null or different class
        Money that = (Money) o;
        return (
            copper_coin_amount == that.copper_coin_amount &&
            silver_coin_amount == that.silver_coin_amount &&
            gold_coin_amount == that.gold_coin_amount &&
            platinum_coin_amount == that.platinum_coin_amount
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            copper_coin_amount,
            silver_coin_amount,
            gold_coin_amount,
            platinum_coin_amount
        );
    }
    

    //============================== MISC ==============================\\
    /**
     * Sorts the Money Object's Coin amounts from highest to lowest and Money Object
     * (eg. if you have 1'234'567 copper coins stored in here, then this will turn it into 1 Platinum, 23 Gold, 45 Silver, 67 Copper)
     * @param money 
     * @return
    */
    private static Money sort(Long money) {
        int platinum_coins = (int) (money / CoinType.PLATINUM.getValue()); //get max amount of platinum coins possible
        money %= CoinType.PLATINUM.getValue(); //get the remainder of the value after removing the platinum coins

        int gold_coins = (int) (money / CoinType.GOLD.getValue()); //get max amount of gold coins possible
        money %= CoinType.GOLD.getValue(); //get the remainder of the value after removing the gold coins

        int silver_coins = (int) (money / CoinType.SILVER.getValue()); //get max amount of silver coins possible
        money %= CoinType.SILVER.getValue(); //get the remainder of the value after removing the silver coins

        int copper_coins = money.intValue(); //the remainder is the amount of copper coins (since copper coins have a value of 1 we don't need to do any more math)

        return new Money(copper_coins, silver_coins, gold_coins, platinum_coins); //create new Money Object with the sorted values
    }

    
}
