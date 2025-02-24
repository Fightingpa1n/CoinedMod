package net.fightingpainter.mc.coined.currency;

import net.fightingpainter.mc.coined.items.ModItems;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

/**
 * The Coins Enum
 * it's used to define coin types and their properties
*/
public enum CoinType {
    
    //============================== Coin Types ==============================\\
    COPPER( //The copper coin 
        1,
        0xae5b3c,
        ModItems.COPPER_COIN
    ),
    
    SILVER( //The silver coin
        100,
        0x8c8c8c,
        ModItems.SILVER_COIN
    ),
    
    GOLD( //The gold coin
        10000,
        0xffd700,
        ModItems.GOLD_COIN
    ),
    
    PLATINUM( //The platinum coin
        1000000,
        0xd7d3e0,
        ModItems.PLATINUM_COIN
    );
    

    //============================== Enum Definition (Ignore everything past this line, as it's just for setting stuff) ==============================\\
    private final long value;
    private final int nameColor;
    private final DeferredItem<Item> item;


    /**
     * The Coins Constructor
     * @param value The value of a single coin
     * @param nameColor The color of the coin's name (the color used for all the text and stuff)
     * @param item The item that represents the coin
     *
    */
    CoinType(long value, int nameColor, DeferredItem<Item> item) {
        this.value = value;
        this.nameColor = nameColor;
        this.item = item;
    }
    
    //============================== Operations ==============================\\
    /**
     * Use to get the Value of a single coin of this Type
     * @return the value of a single coin
    */
    public long getValue() {return value;}

    /** 
     * Use to get the Value of a specified amount of coins of this type
     * @param amount the amount of coins
     * @return the value of a specified amount of coins
    */
    public long getValue(int amount) {return value * amount;}

    /**
     * Use to get the Name Color of this coin type
     * @return the color of the coin's name
    */
    public int getNameColor() {return nameColor;}

    /**
     * Use to get the Item that represents this coin type
     * @return the item that represents the coin
    */
    public DeferredItem<Item> getItem() {return item;}
}
