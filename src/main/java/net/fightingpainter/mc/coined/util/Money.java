package net.fightingpainter.mc.coined.util;

import net.fightingpainter.mc.coined.currency.CoinType;
import net.fightingpainter.mc.coined.util.types.Dict;
import net.minecraft.nbt.CompoundTag;

/**
 * The Money Class is a helper class to store Currency amounts and helps with conversion and other stuff
 * Think of it as a Smart Money Bag that you can put money into and you can ask it stuff
 * Note: it stores stuff as individual coin amounts rather than the total value
*/
public class Money {
    private int copper_coin_amount = 0; //amount of stored copper coins
    private int silver_coin_amount = 0; //amount of stored silver coins
    private int gold_coin_amount = 0; //amount of stored gold coins
    private int platinum_coin_amount = 0; //amount of stored platinum coins

    //============================== Constructors ==============================\\
    /** An Empty Constructor that will create a Money Object with no money stored */
    public Money() {}

    /**
     * A constructor that takes each individual amount of each coin type
     * @param copper_coin_amount the amount of copper coins that should be stored
     * @param silver_coin_amount the amount of silver coins that should be stored
     * @param gold_coin_amount the amount of gold coins that should be stored
     * @param platinum_coin_amount the amount of platinum coins that should be stored
    */
    public Money(int copper_coin_amount, int silver_coin_amount, int gold_coin_amount, int platinum_coin_amount) {
        this.copper_coin_amount = copper_coin_amount;
        this.silver_coin_amount = silver_coin_amount;
        this.gold_coin_amount = gold_coin_amount;
        this.platinum_coin_amount = platinum_coin_amount;
    }
    
    /**
     * A constructor that takes a long type value of like the total amount of money that should be stored
     * @param money the total amount of money that should be stored
    */
    public Money(long money) {
        setTotalValue(money);
    }

    /**
     * A constructor that takes a CompoundTag (NBT) and creates a Money Object from it
     * @param nbt the CompoundTag that should be used to create the Money Object
     * @see #toNBT()
     * @see #fromNBT(CompoundTag)
    */
    public Money(CompoundTag nbt) {
        fromNBT(nbt);
    }
    
    
    //============================== Methods ==============================\\
    
    /**
     * Sets the total value of the Money Object to the given value by converting it to individual coin amounts from highest to lowest
     * (eg. if you give a Value of 1'234'567 it will convert it to 1 Platinum, 23 Gold, 45 Silver, 67 Copper)
     * @param value the total value that should be stored
     */
    public void setTotalValue(long value) {
        platinum_coin_amount = (int) (value / CoinType.PLATINUM.getValue()); //get the max amount of platinum coins possible from the value
        value %= CoinType.PLATINUM.getValue(); //get the remainder of the value after removing the platinum coins
        
        gold_coin_amount = (int) (value / CoinType.GOLD.getValue()); //get the max amount of gold coins possible from the remainder
        value %= CoinType.GOLD.getValue(); //get the remainder of the remainder after removing the gold coins
        
        silver_coin_amount = (int) (value / CoinType.SILVER.getValue()); //get the max amount of silver coins possible from the remainder
        value %= CoinType.SILVER.getValue(); //get the remainder of the remainder after removing the silver coins
        
        copper_coin_amount = (int) value; //the remainder is the amount of copper coins (since copper coins have a value of 1 we don't need to do any more math)
    }

    /**
     * Takes the Total value of Stored Coins and Sorts them Anew
     * (this is useful if you have like 1'234'567 copper coins stored in here, then this will turn it into 1 Platinum, 23 Gold, 45 Silver, 67 Copper)
     * @return itself (so you can chain stuff)
    */
    public Money flatten() {
        setTotalValue(getTotalValue());
        return this;
    }
    
    //=============== Add ===============\\ (adds coins to the current coin amounts)
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

    /**
     * Adds the Coin Amounts Stored in the Given CompoundTag to the current Money Objects Coin amounts
     * @param nbt the CompoundTag that should be used to add the Coin amounts
     * @return itself (so you can chain stuff)
    */
    public Money add(CompoundTag nbt) {
        Money money = new Money(nbt);
        copper_coin_amount += money.getCopperAmount();
        silver_coin_amount += money.getSilverAmount();
        gold_coin_amount += money.getGoldAmount();
        platinum_coin_amount += money.getPlatinumAmount();
        return this;
    }


    //=============== Combine ===============\\ (bassically add but it will only consider total values)
    /**
     * Combines the Given Money Objects value with the current Money Objects total value and sets it as the new value
     * (coin amounts will be lost as it will only consider total values)
     * @param money the Money Object that should be combined into this one
     * @return itself (so you can chain stuff)
    */
    public Money combine(Money money) {
        long total = money.getTotalValue() + getTotalValue();
        setTotalValue(total);
        return this;
    }

    /**
     * Combines the Given Coin Amounts's Total Value with the current Money Objects total value and sets it as the new value
     * (coin amounts will be lost as it will only consider total values)
     * @param copper the amount of copper coins that should be "added"
     * @param silver the amount of silver coins that should be "added"
     * @param gold the amount of gold coins that should be "added"
     * @param platinum the amount of platinum coins that should be "added"
     * @return itself (so you can chain stuff)
    */
    public Money combine(int copper, int silver, int gold, int platinum) {
        Money money = new Money(copper, silver, gold, platinum);
        long total = money.getTotalValue() + getTotalValue();
        setTotalValue(total);
        return this;
    }

    /**
     * Combines the Given Value with the current Money Objects total value and sets it as the new value
     * (coin amounts will be lost as it will only consider total values)
     * @param value the value that will be "added"
     * @return itself (so you can chain stuff)
    */
    public Money combine(long value) {
        long total = value + getTotalValue();
        setTotalValue(total);
        return this;
    }

    /**
     * Combines the Coin Amounts Stored in the Given CompoundTag with the current Money Objects total value and sets it as the new value
     * (coin amounts will be lost as it will only consider total values)
     * @param nbt the CompoundTag that should be used to "add"
     * @return itself (so you can chain stuff)
    */
    public Money combine(CompoundTag nbt) {
        Money money = new Money(nbt);
        long total = money.getTotalValue() + getTotalValue();
        setTotalValue(total);
        return this;
    }
    
    //============================== Checks ==============================\\

    //=============== Get ===============\\
    /** Returns the amount of Copper Coins Stored */
    public int getCopperAmount() {return copper_coin_amount;}
    /** Returns the amount of Silver Coins Stored */
    public int getSilverAmount() {return silver_coin_amount;}
    /** Returns the amount of Gold Coins Stored */
    public int getGoldAmount() {return gold_coin_amount;}
    /** Returns the amount of Platinum Coins Stored */
    public int getPlatinumAmount() {return platinum_coin_amount;}
    /** Returns the Total amount of all the Coins Stored inside the Money Object (to be honest Idk what this would be used for I just added it for consistency) */
    public int getTotalAmount() {return copper_coin_amount + silver_coin_amount + gold_coin_amount + platinum_coin_amount;}
    
    //=============== Get (Value) ===============\\
    /** Returns the Value of the Copper Coins Stored */
    public long getCopperValue() {return CoinType.COPPER.getValue(copper_coin_amount);}
    /** Returns the Value of the Silver Coins Stored */
    public long getSilverValue() {return CoinType.SILVER.getValue(silver_coin_amount);}
    /** Returns the Value of the Gold Coins Stored */
    public long getGoldValue() {return CoinType.GOLD.getValue(gold_coin_amount);}
    /** Returns the Value of the Platinum Coins Stored */
    public long getPlatinumValue() {return CoinType.PLATINUM.getValue(platinum_coin_amount);}
    /** Returns the Total Value of all the Coins Stored inside the Money Object (basically how much money is this bag) */
    public long getTotalValue() {return getCopperValue() + getSilverValue() + getGoldValue() + getPlatinumValue();}
    
    //=============== Has ===============\\
    /** checks if the Money Object has any Copper Coins */
    public boolean hasCopper() {return copper_coin_amount > 0;}
    /** checks if the Money Object has any Silver Coins */
    public boolean hasSilver() {return silver_coin_amount > 0;}
    /** checks if the Money Object has any Gold Coins */
    public boolean hasGold() {return gold_coin_amount > 0;}
    /** checks if the Money Object has any Platinum Coins */
    public boolean hasPlatinum() {return platinum_coin_amount > 0;}
    /** checks if the Money Object has any Coins at all */
    public boolean hasTotal() {return getTotalAmount() > 0;}
    
    //=============== Enough ===============\\
    /** checks if the Money Object has equal or more Copper Coins than the given amount (basically can you afford something)*/
    public boolean enoughCopper(int amount) {return copper_coin_amount >= amount;}
    /** checks if the Money Object has equal or more Silver Coins than the given amount (basically can you afford something)*/
    public boolean enoughSilver(int amount) {return silver_coin_amount >= amount;}
    /** checks if the Money Object has equal or more Gold Coins than the given amount (basically can you afford something)*/
    public boolean enoughGold(int amount) {return gold_coin_amount >= amount;}
    /** checks if the Money Object has equal or more Platinum Coins than the given amount (basically can you afford something)*/
    public boolean enoughPlatinum(int amount) {return platinum_coin_amount >= amount;}
    /** checks if the Money Object has equal or more Value (total Value of Coins Stored) than the given amount*/
    public boolean enoughTotal(long amount) {return getTotalValue() >= amount;}

    
    //============================== Operations ==============================\\

    //=============== Set (Coin Amount) ===============\\
    /** sets the copper coin amount to the given new amount */
    public void setCopper(int amount) {copper_coin_amount = amount;}
    /** sets the silver coin amount to the given new amount */
    public void setSilver(int amount) {silver_coin_amount = amount;}
    /** sets the gold coin amount to the given new amount */
    public void setGold(int amount) {gold_coin_amount = amount;}
    /** sets the platinum coin amount to the given new amount */
    public void setPlatinum(int amount) {platinum_coin_amount = amount;}

    //=============== Add (Coin Amout) ===============\\
    /** adds the given amount of copper coins to the current amount */
    public void addCopper(int amount) {copper_coin_amount += amount;}
    /** adds the given amount of silver coins to the current amount */
    public void addSilver(int amount) {silver_coin_amount += amount;}
    /** adds the given amount of gold coins to the current amount */
    public void addGold(int amount) {gold_coin_amount += amount;}
    /** adds the given amount of platinum coins to the current amount */
    public void addPlatinum(int amount) {platinum_coin_amount += amount;}
    
    //=============== Remove ===============\\ //TODO: Add checks/clamp or any thing to prevent negative values and stuff
    /** removes the given amount of copper coins from the current amount */
    public void removeCopper(int amount) {copper_coin_amount -= amount;}
    /** removes the given amount of silver coins from the current amount */
    public void removeSilver(int amount) {silver_coin_amount -= amount;}
    /** removes the given amount of gold coins from the current amount */
    public void removeGold(int amount) {gold_coin_amount -= amount;}
    /** removes the given amount of platinum coins from the current amount */
    public void removePlatinum(int amount) {platinum_coin_amount -= amount;}

    // public int getType() { //TODO: this is was originally for checking what type of MoneyBag it is and was used by the MoneyBag Item to determine the ItemProperty which then was used to switch between textures, but the types here are defined with numbers and that's like the only place I use these numbers so I would like to think if a better way
    //     if (hasCopper() && !hasSilver() && !hasGold() && !hasPlatinum()) return 1;
    //     else if (!hasCopper() && hasSilver() && !hasGold() && !hasPlatinum()) return 2;
    //     else if (!hasCopper() && !hasSilver() && hasGold() && !hasPlatinum()) return 3;
    //     else if (!hasCopper() && !hasSilver() && !hasGold() && hasPlatinum()) return 4;
    //     else return 0;
    // }
    
    //TODO: add Remove methods (probably in tandem with the stuff below)
    /* I just took a look at these and they seem outdated but since they only have specific use cases I will redo them when I need them
    public long removeHighest() {
        if (hasPlatinum()) {
            platinum_coin_amount--;
            return Coins.PLATINUM.getValue();
        }
        else if (hasGold()) {
            gold_coin_amount--;
            return Coins.GOLD.getValue();
        }
        else if (hasSilver()) {
            silver_coin_amount--;
            return Coins.SILVER.getValue();
        }
        else if (hasCopper()) {
            copper_coin_amount--;
            return Coins.COPPER.getValue();
        }
        else return 0;
    }

    public long removeLowest() {
        if (hasCopper()) {
            copper_coin_amount--;
            return Coins.COPPER.getValue();
        }
        else if (hasSilver()) {
            silver_coin_amount--;
            return Coins.SILVER.getValue();
        }
        else if (hasGold()) {
            gold_coin_amount--;
            return Coins.GOLD.getValue();
        }
        else if (hasPlatinum()) {
            platinum_coin_amount--;
            return Coins.PLATINUM.getValue();
        }
        else return 0;
    }


    public Money multiSplit(int amount) { //the multiSplit will split the current money into equal Money objects for the amount given and will put any remainder in itself
        long total = getTotal();
        Money temple = new Money(total % amount);

        copper_coin_amount = temple.getCopper();
        silver_coin_amount = temple.getSilver();
        gold_coin_amount = temple.getGold();
        platinum_coin_amount = temple.getPlatinum();

        return new Money(total / amount);
    }

    public Money singleSplit() { //the singleSplit will split the current money into 2 Money objects (if odd one will be bigger than the other), and will return the bigger one and put the smaller one in itself
        double half = getTotal()/2.0;
        Money temple = new Money((long)Math.floor(half));

        copper_coin_amount = temple.getCopper();
        silver_coin_amount = temple.getSilver();
        gold_coin_amount = temple.getGold();
        platinum_coin_amount = temple.getPlatinum();

        return new Money((long)Math.ceil(half));
    } */
    
    /**
     * Set's the Money Object's Coin amounts to the values stored in the given CompoundTag (NBT)
     * @param nbt the CompoundTag that should be used to set the Coin amounts
     */
    public void fromNBT(CompoundTag nbt) {
        copper_coin_amount = nbt.getInt("copper");
        silver_coin_amount = nbt.getInt("silver");
        gold_coin_amount = nbt.getInt("gold");
        platinum_coin_amount = nbt.getInt("platinum");
    }

    /**
     * Converts the Money Object's Coin amounts to a CompoundTag (NBT)
     * @return the CompoundTag that represents the Money Object
    */
    public CompoundTag toNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("copper", copper_coin_amount);
        nbt.putInt("silver", silver_coin_amount);
        nbt.putInt("gold", gold_coin_amount);
        nbt.putInt("platinum", platinum_coin_amount);
        return nbt;
    }


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

    
    public Money copy() {return new Money(copper_coin_amount, silver_coin_amount, gold_coin_amount, platinum_coin_amount);}


    //============================== DEBUG ==============================\\
    /**
     * Returns the Money Object as a readable String (ment for debugging)
     * @param format if true it will format the string to be more readable by using new lines and tabs
     * @return A String Ready to be printed or logged elsewhere
     */
    public String toPrint(boolean format) {
        if (format) {return String.format("Total: %d (\n\tCopper: %d,\n\tSilver: %d,\n\tGold: %d,\n\tPlatinum: %d\n)", getTotalValue(), copper_coin_amount, silver_coin_amount, gold_coin_amount, platinum_coin_amount);}
        else {return String.format("total: %d (copper: %d, silver: %d, gold: %d, platinum: %d)", getTotalValue(), copper_coin_amount, silver_coin_amount, gold_coin_amount, platinum_coin_amount);}
    }
}
