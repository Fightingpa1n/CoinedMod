package net.fightingpainter.mc.coined.items;

import java.util.List;

import javax.annotation.Nonnull;

import net.fightingpainter.mc.coined.currency.CoinType;
import net.fightingpainter.mc.coined.util.Txt;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * The Coin Item is a Currency Item that represents coins
 * therefore it has a CoinType associated with it which is then used for the value getting and stuff like that 
*/
public class CoinItem extends CurrencyItem {
    private final CoinType coinType;

    public CoinItem(CoinType coinType) { //constructor
        super(new Properties().stacksTo(64));
        this.coinType = coinType;
    }

    @Override
    public long getValue(ItemStack stack) { //override the getValue method to return the value of the stack
        return coinType.getValue(stack.getCount());
    }

    
    @Override
    public Component getName(@Nonnull ItemStack stack) { //Override the getName method to do dynamic Naming
        String key = stack.getItem().getDescriptionId(); //okay holy shit I know mojang naming conventions are bad but this is the first one where it physically pains me so far... I mean TranslationKey or something? why description?
        if (stack.getCount() > 1) {key += ".multiple";} //if multible coins are used use the multiple translation key
        return Txt.colored(Component.translatable(key), coinType.getNameColor());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents,TooltipFlag tooltipFlag) {

    }

    
    // public void appendTooltip(ItemStack stack,  world, List<Component> tooltip, TooltipContext context) {


    //     if (Screen.hasShiftDown()) {
    //         tooltip.add(Txt.colored(Txt.concat("Value: ", coinType.getValue()), coinType.getNameColor()));
    //         if (stack.getCount() > 1) { tooltip.add(Txt.colored(Txt.concat("Total: ", Txt.total(coinType.getValue(stack.getCount()))), Txt.TOOLTIP2));}

    //     } else {tooltip.add(Txt.colored(Txt.trans("txt.shift_info"), Txt.TOOLTIP2));}
    // }
    
    
    /** returns the CoinType associated with this CoinItem */
    public CoinType getCoinType() {return coinType;}
}
