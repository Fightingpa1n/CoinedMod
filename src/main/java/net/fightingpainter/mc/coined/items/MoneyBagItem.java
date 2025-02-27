package net.fightingpainter.mc.coined.items;

import java.util.List;
import javax.annotation.Nonnull;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;
import net.fightingpainter.mc.coined.nbt.ModDataComponentTypes;
import net.fightingpainter.mc.coined.util.Money;
import net.fightingpainter.mc.coined.util.Txt;

public class MoneyBagItem extends CurrencyItem {
    private static final DataComponentType<Money> COMPONENT = ModDataComponentTypes.MONEY.get(); //the money data component


    public MoneyBagItem() {
        super(new Properties().stacksTo(1));
    }
    
    @Override
    public Money getValue(ItemStack stack) {
        return getMoneyData(stack);
    }

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


    //max values TODO: make this configurable
    public static final int MAX_COPPER_COINS = 255;
    public static final int MAX_SILVER_COINS = 255;
    public static final int MAX_GOLD_COINS = 255;
    public static final int MAX_PLATINUM_COINS = 255;


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

    /**
     * Add money to a money bag (doesn't consider max values)
     * @param stack the money bag to add the money to
     * @param money the money to add
    */
    public static void addMoney(ItemStack stack, Money money) {
        Money current = getMoneyData(stack);
        current.add(money);
        stack.set(COMPONENT, current);
    }

    /**
     * Adds the Money from Money Object to the MoneyBag ItemStack and returns the rest of the Money that could not be added
     * @param stack the money bag to add the money to
     * @param money the money to add
     * @return the rest of the money that could not be added
    */
    public static Money restAdd(ItemStack stack, Money money) {
        Money current = getMoneyData(stack);

        Money rest = new Money(
            Math.max(0, (current.getCopperAmount()+money.getCopperAmount()) - MAX_COPPER_COINS),
            Math.max(0, (current.getSilverAmount()+money.getSilverAmount()) - MAX_SILVER_COINS),
            Math.max(0, (current.getGoldAmount()+money.getGoldAmount()) - MAX_GOLD_COINS),
            Math.max(0, (current.getPlatinumAmount()+money.getPlatinumAmount()) - MAX_PLATINUM_COINS)
        );

        current = new Money(
            Math.min(MAX_COPPER_COINS, current.getCopperAmount() + money.getCopperAmount()),
            Math.min(MAX_SILVER_COINS, current.getSilverAmount() + money.getSilverAmount()),
            Math.min(MAX_GOLD_COINS, current.getGoldAmount() + money.getGoldAmount()),
            Math.min(MAX_PLATINUM_COINS, current.getPlatinumAmount() + money.getPlatinumAmount())
        );

        stack.set(COMPONENT, current);

        return rest;
    }
}
