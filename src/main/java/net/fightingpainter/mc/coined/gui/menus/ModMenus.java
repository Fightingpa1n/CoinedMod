package net.fightingpainter.mc.coined.gui.menus;

import net.fightingpainter.mc.coined.Coined;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, Coined.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<ShopMenu>> SHOP_MENU = MENU_TYPES.register("shop_menu", () -> new MenuType<ShopMenu>(ShopMenu::new, FeatureFlagSet.of()));


    public static void register(IEventBus modEventBus) {
        Coined.LOGGER.info("Registering Screens...");
        MENU_TYPES.register(modEventBus);
    }
}
