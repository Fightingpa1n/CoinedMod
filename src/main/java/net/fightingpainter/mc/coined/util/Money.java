package net.fightingpainter.mc.coined.util;

import java.util.Objects;

import net.fightingpainter.mc.coined.gui.currency.CoinType;

/** the Money Class is a helper class that stores money */
public class Money {
    //======================================== Values ========================================\\
    protected int copperAmount = 0;
    protected int silverAmount = 0;
    protected int goldAmount = 0;
    protected int platinumAmount = 0;
    

    //======================================== Constructor ========================================\\    
    /** Will create an Empty Money object with no coins */
    public Money() {}
    
    /**
     * Inits A Money object with the given amounts of each coin type
     * @param copperAmount copper coin(s) amount
     * @param silverAmount silver coin(s) amount
     * @param goldAmount gold coin(s) amount
     * @param platinumAmount platinum coin(s) amount
    */
    public Money(int copperAmount, int silverAmount, int goldAmount, int platinumAmount) {
        this.copperAmount = Math.max(0, copperAmount); //ensure the amount is not negative
        this.silverAmount = Math.max(0, silverAmount); //ensure the amount is not negative
        this.goldAmount = Math.max(0, goldAmount); //ensure the amount is not negative
        this.platinumAmount = Math.max(0, platinumAmount); //ensure the amount is not negative
    }


    //======================================== Coin Amounts ========================================\\    
    //==================== Getters ====================\\
    /** Gets the amount of copper coins in this Money object */
    public int getCopperAmount() {return this.copperAmount;}
    
    /** Gets the amount of silver coins in this Money object */
    public int getSilverAmount() {return this.silverAmount;}
    
    /** Gets the amount of gold coins in this Money object */
    public int getGoldAmount() {return this.goldAmount;}
    
    /** Gets the amount of platinum coins in this Money object */
    public int getPlatinumAmount() {return this.platinumAmount;}
    
    //==================== Checkers ====================\\
    /** Checks if this Money object has any copper coins */
    public boolean hasCopper() {return this.copperAmount > 0;}

    /** Checks if this Money object has any silver coins */
    public boolean hasSilver() {return this.silverAmount > 0;}

    /** Checks if this Money object has any gold coins */
    public boolean hasGold() {return this.goldAmount > 0;}

    /** Checks if this Money object has any platinum coins */
    public boolean hasPlatinum() {return this.platinumAmount > 0;}

    //==================== Values ====================\\
    /** Gets the total value of the copper coins in this Money object */
    public long getCopperValue() {return CoinType.COPPER.getValue(this.copperAmount);}
    
    /** Gets the total value of the silver coins in this Money object */
    public long getSilverValue() {return CoinType.SILVER.getValue(this.silverAmount);}

    /** Gets the total value of the gold coins in this Money object */
    public long getGoldValue() {return CoinType.GOLD.getValue(this.goldAmount);}

    /** Gets the total value of the platinum coins in this Money object */
    public long getPlatinumValue() {return CoinType.PLATINUM.getValue(this.platinumAmount);}

    //==================== Setters ====================\\
    /** Sets the amount of copper coins in this Money object (clamps to 0) */
    public void setCopperAmount(int copperAmount) {this.copperAmount = Math.max(0, copperAmount);}
    
    /** Sets the amount of silver coins in this Money object (clamps to 0) */
    public void setSilverAmount(int silverAmount) {this.silverAmount = Math.max(0, silverAmount);}

    /** Sets the amount of gold coins in this Money object (clamps to 0) */
    public void setGoldAmount(int goldAmount) {this.goldAmount = Math.max(0, goldAmount);}

    /** Sets the amount of platinum coins in this Money object (clamps to 0) */
    public void setPlatinumAmount(int platinumAmount) {this.platinumAmount = Math.max(0, platinumAmount);}

    //==================== Add ====================\\
    /** Adds the given amount of copper coins to this Money object (clamps to 0) */
    public void addCopper(int amount) {this.copperAmount = Math.max(0, this.copperAmount + amount);}

    /** Adds the given amount of silver coins to this Money object (clamps to 0) */
    public void addSilver(int amount) {this.silverAmount = Math.max(0, this.silverAmount + amount);}

    /** Adds the given amount of gold coins to this Money object (clamps to 0) */
    public void addGold(int amount) {this.goldAmount = Math.max(0, this.goldAmount + amount);}

    /** Adds the given amount of platinum coins to this Money object (clamps to 0) */
    public void addPlatinum(int amount) {this.platinumAmount = Math.max(0, this.platinumAmount + amount);}

    //==================== Subtract ====================\\
    /** Subtracts the given amount of copper coins from this Money object (clamps to 0) */
    public void subtractCopper(int amount) {this.copperAmount = Math.max(0, this.copperAmount - amount);}

    /** Subtracts the given amount of silver coins from this Money object (clamps to 0) */
    public void subtractSilver(int amount) {this.silverAmount = Math.max(0, this.silverAmount - amount);}

    /** Subtracts the given amount of gold coins from this Money object (clamps to 0) */
    public void subtractGold(int amount) {this.goldAmount = Math.max(0, this.goldAmount - amount);}

    /** Subtracts the given amount of platinum coins from this Money object (clamps to 0) */
    public void subtractPlatinum(int amount) {this.platinumAmount = Math.max(0, this.platinumAmount - amount);}
    

    //======================================== Total Value ========================================\\
    //==================== Getters ====================\\
    /** Gets the total amount of coins in this Money object */
    public int getTotalAmount() {
        return (
            this.copperAmount +
            this.silverAmount +
            this.goldAmount +
            this.platinumAmount
        );
    }

    /** Gets the total value of all coins in this Money object */
    public long getTotalValue() {
        return (
            this.getCopperValue() +
            this.getSilverValue() +
            this.getGoldValue() +
            this.getPlatinumValue()
        );
    }

    //==================== Checkers ====================\\
    /** Checks if this Money object is empty (no coins) */
    public boolean isEmpty() {
        return this.getTotalAmount() <= 0; //if the total amount of coins is 0 or less
    }


    //======================================== Money Operations ========================================\\
    //==================== Add ====================\\
    /**
     * Adds the given Money object to this Money object
     * @param money the Money object to add
    */
    public void add(Money money) {
        if (money != null && !money.isEmpty()) { //if the money object is not null and not empty
            this.copperAmount += money.getCopperAmount(); //add copper amount
            this.silverAmount += money.getSilverAmount(); //add silver amount
            this.goldAmount += money.getGoldAmount(); //add gold amount
            this.platinumAmount += money.getPlatinumAmount(); //add platinum amount
        }
    }

    /**
     * Adds the given amount of coins to this Money object
     * @param copperAmount the amount of copper coins to add
     * @param silverAmount the amount of silver coins to add
     * @param goldAmount the amount of gold coins to add
     * @param platinumAmount the amount of platinum coins to add
    */
    public void add(int copperAmount, int silverAmount, int goldAmount, int platinumAmount) {
        this.copperAmount += copperAmount; //add copper amount
        this.silverAmount += silverAmount; //add silver amount
        this.goldAmount += goldAmount; //add gold amount
        this.platinumAmount += platinumAmount; //add platinum amount
    }

    //==================== Subtract ====================\\
    /**
     * Subtracts the given Money object from this Money object
     * @param money the Money object to subtract
    */
    public void subtract(Money money) {
        if (money != null && !money.isEmpty()) { //if the money object is not null and not empty
            this.copperAmount -= money.getCopperAmount(); //subtract copper amount
            this.silverAmount -= money.getSilverAmount(); //subtract silver amount
            this.goldAmount -= money.getGoldAmount(); //subtract gold amount
            this.platinumAmount -= money.getPlatinumAmount(); //subtract platinum amount
        }
    }

    /**
     * Subtracts the given amount of coins from this Money object
     * @param copperAmount the amount of copper coins to subtract
     * @param silverAmount the amount of silver coins to subtract
     * @param goldAmount the amount of gold coins to subtract
     * @param platinumAmount the amount of platinum coins to subtract
    */
    public void subtract(int copperAmount, int silverAmount, int goldAmount, int platinumAmount) {
        this.copperAmount -= copperAmount; //subtract copper amount
        this.silverAmount -= silverAmount; //subtract silver amount
        this.goldAmount -= goldAmount; //subtract gold amount
        this.platinumAmount -= platinumAmount; //subtract platinum amount
    }

    //==================== Enough ====================\\
    /**
     * Checks if this Money object has enough value to cover the target value
     * @param targetValue the target value to check against
     * @return true if the total value of this Money object is greater than or equal to the target value, false otherwise
    */
    public boolean hasEnough(long targetValue) {
        return this.getTotalValue() >= targetValue; //check if the total value of this Money object is greater than or equal to the target value
    }
    
    //======================================== Base Object Methods ========================================\\
    @Override
    public String toString() { //string representation of the Money object
        return String.format("Money(%d Copper, %d Silver, %d Gold, %d Platinum)", this.copperAmount, this.silverAmount, this.goldAmount, this.platinumAmount);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException { //clone the Money object
        return new Money(this.copperAmount, this.silverAmount, this.goldAmount, this.platinumAmount); //return a new Money object with the same values
    }

    @Override
    public boolean equals(Object obj) { //check if two Money objects are equal
        if (this == obj) return true; //if the same object
        if (obj == null || this.getClass() != obj.getClass()) return false; //if null or not the same class
        if (obj instanceof Money money) { //if the object is a Money object
            return ( //check if all coin amounts are equal
                (this.copperAmount == money.copperAmount) 
                && (this.silverAmount == money.silverAmount)
                && (this.goldAmount == money.goldAmount)
                && (this.platinumAmount == money.platinumAmount)
            );
        } return false;
    }

    @Override
    public int hashCode() { //generate a hash code for the Money object
        return Objects.hash(
            this.copperAmount,
            this.silverAmount,
            this.goldAmount,
            this.platinumAmount
        );
    }


    //======================================== Static Methods ========================================\\
    /**
     * Creates A Money Object from a total value of coins.
     * it will try to calculate the amount of each coin type from highest to lowest value.
     * e.g. if if the total value would be 1'234'567 it would result in: Money(1 Platinum, 23 Gold, 45 Silver, 67 Copper)
     * @param totalValue the total value of coins to convert to a Money object
     * @return a Money object with the calculated amounts of each coin type
    */
    public static Money fromValue(long totalValue) {
        Money money = new Money(); //init empty money object
        if (totalValue > 0) {
            money.setPlatinumAmount((int) (totalValue / CoinType.PLATINUM.getValue())); //get max possible platinum coins
            totalValue %= CoinType.PLATINUM.getValue(); //get the remainder
            money.setGoldAmount((int) (totalValue / CoinType.GOLD.getValue())); //get max possible gold coins
            totalValue %= CoinType.GOLD.getValue(); //get the remainder
            money.setSilverAmount((int) (totalValue / CoinType.SILVER.getValue())); //get max possible silver coins
            totalValue %= CoinType.SILVER.getValue(); //get the remainder
            money.setCopperAmount((int) totalValue); //set copper amount to the remainder (copper is 1:1 so no need to divide)
        } return money; //return the money object
    }
}
