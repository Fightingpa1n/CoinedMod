package net.fightingpainter.mc.coined.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.fightingpainter.mc.coined.Coined;
import net.fightingpainter.mc.coined.blocks.ModBlocks;
import net.fightingpainter.mc.coined.gui.currency.CoinType;
import net.neoforged.bus.api.IEventBus;

/**
 * Contains all items
 * and I would reccomoned to keep it that way to keep things organized
*/
public class ModItems {
    public static final DeferredRegister.Items ITEM_REGISTER = DeferredRegister.createItems(Coined.MOD_ID); //Item Registry

    //============================== Items ==============================\\
    public static final DeferredItem<Item> COPPER_COIN = ITEM_REGISTER.register("copper_coin", () -> new CoinItem(CoinType.COPPER));
    public static final DeferredItem<Item> SILVER_COIN = ITEM_REGISTER.register("silver_coin", () -> new CoinItem(CoinType.SILVER));
    public static final DeferredItem<Item> GOLD_COIN = ITEM_REGISTER.register("gold_coin", () -> new CoinItem(CoinType.GOLD));
    public static final DeferredItem<Item> PLATINUM_COIN = ITEM_REGISTER.register("platinum_coin", () -> new CoinItem(CoinType.PLATINUM));

    public static final DeferredItem<Item> MONEY_BAG = ITEM_REGISTER.register("money_bag", () -> new MoneyBagItem());

    
    //============================== Block Items ==============================\\
    public static final DeferredItem<BlockItem> SHOP_BLOCK_WHITE_ITEM = registerBlockItem(ModBlocks.SHOP_BLOCK_WHITE);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_LIGHT_GRAY_ITEM = registerBlockItem(ModBlocks.SHOP_BLOCK_LIGHT_GRAY);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_GRAY_ITEM = registerBlockItem(ModBlocks.SHOP_BLOCK_GRAY);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_BLACK_ITEM = registerBlockItem(ModBlocks.SHOP_BLOCK_BLACK);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_BROWN_ITEM = registerBlockItem(ModBlocks.SHOP_BLOCK_BROWN);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_RED_ITEM = registerBlockItem(ModBlocks.SHOP_BLOCK_RED);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_ORANGE_ITEM = registerBlockItem(ModBlocks.SHOP_BLOCK_ORANGE);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_YELLOW_ITEM = registerBlockItem(ModBlocks.SHOP_BLOCK_YELLOW);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_LIME_ITEM = registerBlockItem(ModBlocks.SHOP_BLOCK_LIME);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_GREEN_ITEM = registerBlockItem(ModBlocks.SHOP_BLOCK_GREEN);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_CYAN_ITEM = registerBlockItem(ModBlocks.SHOP_BLOCK_CYAN);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_LIGHT_BLUE_ITEM = registerBlockItem(ModBlocks.SHOP_BLOCK_LIGHT_BLUE);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_BLUE_ITEM = registerBlockItem(ModBlocks.SHOP_BLOCK_BLUE);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_PURPLE_ITEM = registerBlockItem(ModBlocks.SHOP_BLOCK_PURPLE);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_MAGENTA_ITEM = registerBlockItem(ModBlocks.SHOP_BLOCK_MAGENTA);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_PINK_ITEM = registerBlockItem(ModBlocks.SHOP_BLOCK_PINK);
    
    //==================================================================================\\

    private static DeferredItem<BlockItem> registerBlockItem(DeferredBlock<Block> block) {
        return ITEM_REGISTER.registerSimpleBlockItem(block.getId().getPath(), block);
    }
    
    /**
     * Register Items
     * @param modEventBus The mod event bus to register the items to
    */
    public static void register(IEventBus modEventBus) {
        Coined.LOGGER.info("Registering items...");
        ITEM_REGISTER.register(modEventBus);

    }
}
