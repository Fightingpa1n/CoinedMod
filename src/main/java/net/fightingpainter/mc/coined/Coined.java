package net.fightingpainter.mc.coined;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import net.fightingpainter.mc.coined.blocks.ModBlocks;
import net.fightingpainter.mc.coined.data.ModDataTypes;
import net.fightingpainter.mc.coined.gui.menus.ModMenus;
import net.fightingpainter.mc.coined.items.CreativeMenuAdderThingy;
import net.fightingpainter.mc.coined.items.ModItems;
import net.fightingpainter.mc.coined.network.NetworkHandler;


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
        ModDataTypes.register(modEventBus);

        modEventBus.addListener(NetworkHandler::onRegister); //register network handler

        modEventBus.addListener(CreativeMenuAdderThingy::addCreative); //add items to creative menu

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }


    private void commonSetup(final FMLCommonSetupEvent event) { //common setup
        // Some common setup code
        LOGGER.info("Heylo! I'm insde CommonSetup! kinda cramped in here, but I'm just Happy to be here! :D");
    }
}
