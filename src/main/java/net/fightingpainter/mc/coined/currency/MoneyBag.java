package net.fightingpainter.mc.coined.currency;

import net.fightingpainter.mc.coined.util.Money;

/**
 * A MoneyBag is an Extension of the Money Class with the difference that the MoneyBag has a maximum amount of coins it can hold
*/
public class MoneyBag extends Money {

    //max values TODO: make this configurable
    public static final int MAX_COPPER_AMOUNT = 255;
    public static final int MAX_SILVER_AMOUNT = 255;
    public static final int MAX_GOLD_AMOUNT = 255;
    public static final int MAX_PLATINUM_AMOUNT = 255;


    //============================== Operations ==============================\\
    //=============== Set ===============\\
    /**
     * Sets the amount of Copper Coins in the Money Bag while not exceeding the maximum amount
     * @param copperCoins the amount of copper coins to set
    */
    @Override
    public void setCopperAmount(int copperCoins) {
        copperCoins = Math.max(copperCoins, 0); //Ensure positive value
        copper_coin_amount = Math.min(copperCoins, MAX_COPPER_AMOUNT); //Set the amount of copper coins
    }

    /**
     * Sets the amount of Silver Coins in the Money Bag while not exceeding the maximum amount
     * @param silverCoins the amount of silver coins to set
    */
    @Override
    public void setSilverAmount(int silverCoins) {
        silverCoins = Math.max(silverCoins, 0); //Ensure positive value
        silver_coin_amount = Math.min(silverCoins, MAX_SILVER_AMOUNT); //Set the amount of silver coins
    }

    /**
     * Sets the amount of Gold Coins in the Money Bag while not exceeding the maximum amount
     * @param goldCoins the amount of gold coins to set
    */
    @Override
    public void setGoldAmount(int goldCoins) {
        goldCoins = Math.max(goldCoins, 0); //Ensure positive value
        gold_coin_amount = Math.min(goldCoins, MAX_GOLD_AMOUNT); //Set the amount of gold coins
    }
    
    /**
     * Sets the amount of Platinum Coins in the Money Bag while not exceeding the maximum amount
     * @param platinumCoins the amount of platinum coins to set
    */
    @Override
    public void setPlatinumAmount(int platinumCoins) {
        platinumCoins = Math.max(platinumCoins, 0); //Ensure positive value
        platinum_coin_amount = Math.min(platinumCoins, MAX_PLATINUM_AMOUNT); //Set the amount of platinum coins
    }


    //=============== Add ===============\\
    /**
     * Adds Copper Coins to the Money Bag until reaching the maximum amount
     * @param copperCoins the amount of copper coins to add
     * @return the rest of the money that could not be added
    */
    public int addCopper(int copperCoins) {
        copperCoins = Math.max(copperCoins, 0); //Ensure positive value
        int rest = Math.max(0, (copper_coin_amount + copperCoins) - MAX_COPPER_AMOUNT); //Calculate the rest
        copper_coin_amount = Math.min(copper_coin_amount + copperCoins, MAX_COPPER_AMOUNT); //Add the coins to the money bag
        return rest; //Return the rest
    }

    /**
     * Adds Silver Coins to the Money Bag until reaching the maximum amount
     * @param silverCoins the amount of silver coins to add
     * @return the rest of the money that could not be added
    */
    public int addSilver(int silverCoins) {
        silverCoins = Math.max(silverCoins, 0); //Ensure positive value
        int rest = Math.max(0, (silver_coin_amount + silverCoins) - MAX_SILVER_AMOUNT); //Calculate the rest
        silver_coin_amount = Math.min(silver_coin_amount + silverCoins, MAX_SILVER_AMOUNT); //Add the coins to the money bag
        return rest; //Return the rest
    }

    /**
     * Adds Gold Coins to the Money Bag until reaching the maximum amount
     * @param goldCoins the amount of gold coins to add
     * @return the rest of the money that could not be added
    */
    public int addGold(int goldCoins) {
        goldCoins = Math.max(goldCoins, 0); //Ensure positive value
        int rest = Math.max(0, (gold_coin_amount + goldCoins) - MAX_GOLD_AMOUNT); //Calculate the rest
        gold_coin_amount = Math.min(gold_coin_amount + goldCoins, MAX_GOLD_AMOUNT); //Add the coins to the money bag
        return rest; //Return the rest
    }

    /**
     * Adds Platinum Coins to the Money Bag until reaching the maximum amount
     * @param platinumCoins the amount of platinum coins to add
     * @return the rest of the money that could not be added
    */
    public int addPlatinum(int platinumCoins) {
        platinumCoins = Math.max(platinumCoins, 0); //Ensure positive value
        int rest = Math.max(0, (platinum_coin_amount + platinumCoins) - MAX_PLATINUM_AMOUNT); //Calculate the rest
        platinum_coin_amount = Math.min(platinum_coin_amount + platinumCoins, MAX_PLATINUM_AMOUNT); //Add the coins to the money bag
        return rest; //Return the rest
    }

    /**
     * Adds the Money from Money Object to the MoneyBag and returns the rest of the Money that could not be added
     * @param money the money to add
     * @return the rest of the money that could not be added
    */
    public Money restAdd(Money money) {
        Money rest = new Money(
            addCopper(money.getCopperAmount()),
            addSilver(money.getSilverAmount()),
            addGold(money.getGoldAmount()),
            addPlatinum(money.getPlatinumAmount())
        );
        return rest;
    }

    //=============== Subtract ===============\\
    /**
     * Subtracts Copper Coins from the Money Bag
     * @param copperCoins the amount of copper coins to subtract$
     * @return true if the coins could be subtracted, false if there are not enough coins
    */
    public boolean subtractCopper(int copperCoins) {
        copperCoins = Math.max(copperCoins, 0); //Ensure positive value
        if (copperCoins > copper_coin_amount) {return false;} //Check if there are enough coins
        copper_coin_amount = Math.max(copper_coin_amount - copperCoins, 0); //Subtract the coins from the money bag
        return true;
    }

    /**
     * Subtracts Silver Coins from the Money Bag
     * @param silverCoins the amount of silver coins to subtract
     * @return true if the coins could be subtracted, false if there are not enough coins
    */
    public boolean subtractSilver(int silverCoins) {
        silverCoins = Math.max(silverCoins, 0); //Ensure positive value
        if (silverCoins > silver_coin_amount) {return false;} //Check if there are enough coins
        silver_coin_amount = Math.max(silver_coin_amount - silverCoins, 0); //Subtract the coins from the money bag
        return true;
    }

    /**
     * Subtracts Gold Coins from the Money Bag
     * @param goldCoins the amount of gold coins to subtract
     * @return true if the coins could be subtracted, false if there are not enough coins
    */
    public boolean subtractGold(int goldCoins) {
        goldCoins = Math.max(goldCoins, 0); //Ensure positive value
        if (goldCoins > gold_coin_amount) {return false;} //Check if there are enough coins
        gold_coin_amount = Math.max(gold_coin_amount - goldCoins, 0); //Subtract the coins from the money bag
        return true;
    }

    /**
     * Subtracts Platinum Coins from the Money Bag
     * @param platinumCoins the amount of platinum coins to subtract
     * @return true if the coins could be subtracted, false if there are not enough coins
    */
    public boolean subtractPlatinum(int platinumCoins) {
        platinumCoins = Math.max(platinumCoins, 0); //Ensure positive value
        if (platinumCoins > platinum_coin_amount) {return false;} //Check if there are enough coins
        platinum_coin_amount = Math.max(platinum_coin_amount - platinumCoins, 0); //Subtract the coins from the money bag
        return true;
    }
}
