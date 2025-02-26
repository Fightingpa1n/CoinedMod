package net.fightingpainter.mc.coined.items;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

public class CreativeMenuAdderThingy {

    
    public static void addCreative(BuildCreativeModeTabContentsEvent event) {

        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) { //coins
            event.accept(ModItems.COPPER_COIN.get());
            event.accept(ModItems.SILVER_COIN.get());
            event.accept(ModItems.GOLD_COIN.get());
            event.accept(ModItems.PLATINUM_COIN.get());
        }

        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) { //shop blocks
            event.accept(ModItems.SHOP_BLOCK_WHITE_ITEM);
            event.accept(ModItems.SHOP_BLOCK_LIGHT_GRAY_ITEM);
            event.accept(ModItems.SHOP_BLOCK_GRAY_ITEM);
            event.accept(ModItems.SHOP_BLOCK_BLACK_ITEM);
            event.accept(ModItems.SHOP_BLOCK_BROWN_ITEM);
            event.accept(ModItems.SHOP_BLOCK_RED_ITEM);
            event.accept(ModItems.SHOP_BLOCK_ORANGE_ITEM);
            event.accept(ModItems.SHOP_BLOCK_YELLOW_ITEM);
            event.accept(ModItems.SHOP_BLOCK_LIME_ITEM);
            event.accept(ModItems.SHOP_BLOCK_GREEN_ITEM);
            event.accept(ModItems.SHOP_BLOCK_CYAN_ITEM);
            event.accept(ModItems.SHOP_BLOCK_LIGHT_BLUE_ITEM);
            event.accept(ModItems.SHOP_BLOCK_BLUE_ITEM);
            event.accept(ModItems.SHOP_BLOCK_PURPLE_ITEM);
            event.accept(ModItems.SHOP_BLOCK_MAGENTA_ITEM);
            event.accept(ModItems.SHOP_BLOCK_PINK_ITEM);
        }
    }
    
}
