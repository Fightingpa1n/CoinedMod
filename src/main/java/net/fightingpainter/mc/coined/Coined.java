package net.fightingpainter.mc.coined;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.fightingpainter.mc.coined.blocks.ModBlocks;
import net.fightingpainter.mc.coined.blocks.shop.ShopBlockEntityRenderer;
import net.fightingpainter.mc.coined.currency.BalanceManager;
import net.fightingpainter.mc.coined.gui.InventoryAdderThingy;
import net.fightingpainter.mc.coined.gui.menus.ModMenus;
import net.fightingpainter.mc.coined.gui.screens.ShopScreen;
import net.fightingpainter.mc.coined.items.ModItems;


@Mod(Coined.MOD_ID)
public class Coined { //Main Mod Class
    public static final String MOD_ID = "coined"; //mod id
    public static final Logger LOGGER = LogUtils.getLogger(); //logger
    //I'm cool and this is deffenetly not just me trying to hype myself up because I just wanted to continue working on the mod but instead I'be been repairing java stuff for the past 2 hours now and I still can't get the game to run again... a

    // Creates a new Block with the id "coined:example_block", combining the namespace and path
    // public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
    // Creates a new BlockItem with the id "coined:example_block", combining the namespace and path
    // public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);

    // Creates a new food item with the id "coined:example_id", nutrition 1 and saturation 2
    // public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties().food(new FoodProperties.Builder()
    //        .alwaysEdible().nutrition(1).saturationModifier(2f).build()));

    // Creates a creative tab with the id "coined:example_tab" for the example item, that is placed after the combat tab
    // public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
    //        .title(Component.translatable("itemGroup.coined")) //The language key for the title of your CreativeModeTab
    //        .withTabsBefore(CreativeModeTabs.COMBAT)
    //        .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
    //        .displayItems((parameters, output) -> {
    //            output.accept(EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
    //        }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Coined(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        
        modEventBus.addListener(this::commonSetup); //common setup

        //Register stuff
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModMenus.register(modEventBus);
        
        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (Coined) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        //NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }


    private void addCreative(BuildCreativeModeTabContentsEvent event) {

        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) { //building blocks
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


    private void commonSetup(final FMLCommonSetupEvent event) { //common setup
        // Some common setup code
        LOGGER.info("Heylo! I'm insde CommonSetup! kinda cramped in here, but I'm just Happy to be here! :D");

        // Register Stuff
    }

    // Add the example block item to the building blocks tab
    // private void addCreative(BuildCreativeModeTabContentsEvent event) {
    //    if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) event.accept(EXAMPLE_BLOCK_ITEM);
    // }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
}
