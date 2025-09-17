package net.fightingpainter.mc.coined.items;

import java.util.List;
import javax.annotation.Nonnull;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.fightingpainter.mc.coined.Coined;
import net.fightingpainter.mc.coined.currency.CoinType;
import net.fightingpainter.mc.coined.currency.CurrencyTxt;
import net.fightingpainter.mc.coined.util.Money;
import net.fightingpainter.mc.coined.util.Txt;


/**
 * The Coin Item is a Currency Item that represents coins
 * therefore it has a CoinType associated with it which is then used for the value getting and stuff like that 
*/
public class CoinItem extends CurrencyItem {
    private final CoinType coinType;

    //======================================== Item Behavior ========================================\\
    /**
     * Constructor for the CoinItem
     * @param coinType The CoinType associated with this CoinItem
    */
    public CoinItem(CoinType coinType) { //constructor
        super(new Properties().stacksTo(coinType.getMaxStackSize()));
        this.coinType = coinType;
    }

    
    //==================== Display Stuff ====================\\
    @Override
    public Component getName(@Nonnull ItemStack stack) { //Override the getName method to do dynamic Naming
        String key = stack.getItem().getDescriptionId(); //okay holy... I know mojang naming conventions are bad but come on... I mean call it TranslationKey or something? why description? and why the id? a!
        if (stack.getCount() > 1) {key += ".multiple";} //if multible coins are used use the multiple translation key
        return Txt.colored(Component.translatable(key), coinType.getNameColor());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag tooltipFlag) {
        tooltipComponents.add(CurrencyTxt.valueLabel(coinType.getValue(), coinType.getNameColor())); //show the value of a single coin
        if (stack.getCount() > 1) { //if more than 1 coin show the value of all coins
            tooltipComponents.add(CurrencyTxt.totalLabel(coinType.getValue(stack.getCount())));
        }
    }

    //======================================== Money Stuff ========================================\\
    /** returns the CoinType associated with this CoinItem */
    public CoinType getCoinType() {return coinType;}
    
    @Override
    public long getValue(ItemStack stack) { //override the getValue method to return the value of the stack
        return coinType.getValue(stack.getCount()); //return the value of the stack
    }

    @Override
    public Money getMoney(ItemStack stack) { //override the getMoney method to return a
        Money money = new Money(); //create a new Money object
        coinType.setAmountInMoney(money, stack.getCount()); //set the amount of this CoinType in the Money object
        return money; //return the Money object
    }
}
