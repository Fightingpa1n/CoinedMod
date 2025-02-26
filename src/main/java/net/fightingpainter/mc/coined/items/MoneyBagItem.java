package net.fightingpainter.mc.coined.items;

import java.util.List;
import javax.annotation.Nonnull;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;
import net.fightingpainter.mc.coined.nbt.ModDataComponentTypes;
import net.fightingpainter.mc.coined.util.Money;
import net.fightingpainter.mc.coined.util.Txt;

public class MoneyBagItem extends CurrencyItem {

    public MoneyBagItem() {
        super(new Properties().stacksTo(1));
    }
    
    @Override
    public Money getValue(ItemStack stack) {

        Money data = stack.get(ModDataComponentTypes.MONEY.get());
        if (data != null) {return data;} //return money if 

        stack.set(ModDataComponentTypes.MONEY.get(), new Money());
        return new Money();
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
     * Create a money bag itemstack 
     * @param money the money to create the money bag from
     * @return the money bag itemstack
    */
    public static ItemStack createMoneyBag(Money money) {
        ItemStack stack = new ItemStack(ModItems.MONEY_BAG.get());
        stack.set(ModDataComponentTypes.MONEY.get(), money);
        return stack;
    }
}
