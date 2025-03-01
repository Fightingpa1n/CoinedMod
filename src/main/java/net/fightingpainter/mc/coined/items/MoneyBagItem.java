package net.fightingpainter.mc.coined.items;

import java.util.List;
import javax.annotation.Nonnull;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;
import net.fightingpainter.mc.coined.currency.CoinType;
import net.fightingpainter.mc.coined.nbt.ModDataComponentTypes;
import net.fightingpainter.mc.coined.util.Money;
import net.fightingpainter.mc.coined.util.MouseButton;
import net.fightingpainter.mc.coined.util.Txt;

public class MoneyBagItem extends CurrencyItem {
    private static final DataComponentType<Money> COMPONENT = ModDataComponentTypes.MONEY.get(); //the money data component
    
    //max values TODO: make this configurable (Also these should not be less than the max stack size of the coins)
    public static final int MAX_COPPER_COINS = 255;
    public static final int MAX_SILVER_COINS = 255;
    public static final int MAX_GOLD_COINS = 255;
    public static final int MAX_PLATINUM_COINS = 255;


    //============================== Item Behavior ==============================\\
    public MoneyBagItem() {
        super(new Properties().stacksTo(1));
    }
    

    //=============== Display Stuff ===============\\
    @Override
    public Component getName(@Nonnull ItemStack stack) {
        return super.getName(stack);
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag tooltipFlag) {
        Money money = getValue(stack);
        tooltipComponents.add(Txt.text(money.toString(true)));
    }


    //============================== Money Stuff ==============================\\
    @Override
    public Money getValue(ItemStack stack) {
        return getMoneyData(stack);
    }

    //=============== Money Data ===============\\
    /**
     * Get the Money Data from the ItemStack or create a new Money Object if it doesn't exist yet
     * @param stack the ItemStack to get the Money Data from
     * @return the Money Data from the ItemStack
    */
    public static Money getMoneyData(ItemStack stack) {
        Money data = stack.get(ModDataComponentTypes.MONEY.get());
        if (data != null) {return data;} //return money if

        stack.set(ModDataComponentTypes.MONEY.get(), new Money());
        return new Money();
    }
    
    /**
     * Set the Money Data to the ItemStack
     * @param stack the ItemStack to set the Money Data to
     * @param money the Money Data to set 
    */
    public static void setMoneyData(ItemStack stack, Money money) {
        stack.set(ModDataComponentTypes.MONEY.get(), money);
    }
    
    /**
     * Create a money bag itemstack 
     * @param money the money to create the money bag from
     * @return the money bag itemstack
    */
    public static ItemStack createMoneyBag(Money money) {
        ItemStack stack = new ItemStack(ModItems.MONEY_BAG.get());
        stack.set(COMPONENT, money);
        return stack;
    }


    //============================== Click Events ==============================\\
    @Override
    public boolean clickedWithBag(int clickedSlotId, Slot clickedSlot, ItemStack clickedStack, ItemStack carriedStack, Money carriedMoney, MouseButton clickedMouseButton, Player player, AbstractContainerMenu container) {
        return false; //TODO
    }

    @Override
    public boolean clickedWithCoin(int clickedSlotId, Slot clickedSlot, ItemStack clickedStack, ItemStack carriedStack, CoinType carriedCoinType, MouseButton clickedMouseButton, Player player, AbstractContainerMenu container) {
        return false; //TODO
    }
    
    @Override
    public boolean clickedWithEmpty(int clickedSlotId, Slot clickedSlot, ItemStack clickedStack, ItemStack carriedStack, MouseButton clickedMouseButton, Player player, AbstractContainerMenu container) {
        return false; //TODO
    }
}
