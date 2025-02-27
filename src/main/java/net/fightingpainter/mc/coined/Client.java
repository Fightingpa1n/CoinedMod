package net.fightingpainter.mc.coined;

import net.fightingpainter.mc.coined.blocks.ModBlocks;
import net.fightingpainter.mc.coined.blocks.shop.ShopBlockEntityRenderer;
import net.fightingpainter.mc.coined.gui.InventoryAdderThingy;
import net.fightingpainter.mc.coined.gui.menus.ModMenus;
import net.fightingpainter.mc.coined.gui.screens.ShopScreen;
import net.fightingpainter.mc.coined.items.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;

// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid=Coined.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value=Dist.CLIENT)
public class Client {
    
    @SubscribeEvent //Main Client Setup
    public static void onClientSetup(FMLClientSetupEvent event) {

        BlockEntityRenderers.register(ModBlocks.SHOP_BLOCK_ENTITY.get(), ShopBlockEntityRenderer::new);

        event.enqueueWork(() -> {

            //register the InventoryGuiButton
            InventoryAdderThingy inventoryGuiButton = new InventoryAdderThingy();
            NeoForge.EVENT_BUS.register(inventoryGuiButton);

            
            // Item properties
            float maxStack = 64.0f;

            //coins property for coins so they change texture on ammount
            ItemProperties.register(ModItems.COPPER_COIN.get(), ResourceLocation.fromNamespaceAndPath(Coined.MOD_ID, "coins"), (stack, world, entity, seed) -> stack.getCount() / maxStack); //Copper Coin
            ItemProperties.register(ModItems.SILVER_COIN.get(), ResourceLocation.fromNamespaceAndPath(Coined.MOD_ID, "coins"), (stack, world, entity, seed) -> stack.getCount() / maxStack); //Silver Coin
            ItemProperties.register(ModItems.GOLD_COIN.get(), ResourceLocation.fromNamespaceAndPath(Coined.MOD_ID, "coins"), (stack, world, entity, seed) -> stack.getCount() / maxStack); //Gold Coin
            ItemProperties.register(ModItems.PLATINUM_COIN.get(), ResourceLocation.fromNamespaceAndPath(Coined.MOD_ID, "coins"), (stack, world, entity, seed) -> stack.getCount() / maxStack); //Platinum Coin

            Coined.LOGGER.info("Item properties registered successfully.");
        });

        Coined.LOGGER.info("WHAAAT!? I'm inside ClientSetup! I'm not sure what you two are talking about! :|");
        Coined.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

    //============================== Events ==============================\\

    @SubscribeEvent
    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.SHOP_MENU.get(), ShopScreen::new);
    }

    //============================== private stuff ==============================\\

}
