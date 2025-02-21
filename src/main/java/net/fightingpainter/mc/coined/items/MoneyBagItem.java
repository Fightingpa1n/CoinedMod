package net.fightingpainter.mc.coined.items;

import java.util.List;

import net.fightingpainter.mc.coined.currency.Money;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

public class MoneyBagItem extends CurrencyItem {

    public MoneyBagItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public long getValue(ItemStack stack) {
        return 0;
    }

    public Money getMoney(ItemStack stack) {
        //get NBT data from stack
        return new Money(0); //Todo: figure out how to do NBT


    }

    @Override
    public Component getName(ItemStack stack) {
        return super.getName(stack);
        
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
    }
    
}
