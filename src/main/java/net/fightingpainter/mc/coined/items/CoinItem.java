package net.fightingpainter.mc.coined.items;

import java.util.List;
import javax.annotation.Nonnull;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import net.fightingpainter.mc.coined.currency.CoinType;
import net.fightingpainter.mc.coined.currency.CurrencyTxt;
import net.fightingpainter.mc.coined.util.Money;
import net.fightingpainter.mc.coined.util.MouseButton;
import net.fightingpainter.mc.coined.util.Txt;


/**
 * The Coin Item is a Currency Item that represents coins
 * therefore it has a CoinType associated with it which is then used for the value getting and stuff like that 
*/
public class CoinItem extends CurrencyItem {
    private final CoinType coinType;


    //============================== Item Behavior ==============================\\    
    public CoinItem(CoinType coinType) { //constructor
        super(new Properties().stacksTo(64));
        this.coinType = coinType;
    }
    
    //=============== Display Stuff ===============\\
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


    //============================== Money Stuff ==============================\\
    /** returns the CoinType associated with this CoinItem */
    public CoinType getCoinType() {return coinType;}

    @Override
    public Money getValue(ItemStack stack) { //override the getValue method to return the value of the stack
        return new Money(coinType.getValue(stack.getCount()));
    }


    //============================== Click Events ==============================\\
    @Override
    public boolean clickedWithBag(int clickedSlotId, Slot clickedSlot, ItemStack clickedStack, ItemStack carriedStack, Money carriedMoney, MouseButton clickedMouseButton, Player player, AbstractContainerMenu container) {
        if (clickedMouseButton == MouseButton.LEFT_CLICK) { //on left click
            if (carriedMoney.getAmount(coinType) >=  coinType.getMaxBagSize()) {return false;} //if the carried money is to big to fit into a bag return false to cancel the click
            int rest = Math.max(0, clickedStack.getCount()+carriedMoney.getAmount(coinType) - coinType.getMaxBagSize()); //get the remaining count of coins (the max is for safety)
            if (rest > getMaxStackSize(carriedStack)) {return false;} //if the remaining count is greater than the max stack size of the carried stack return false to cancel the click (to prevent the player from trying to put more coins into the slot than possible)
            carriedMoney.setAmount(coinType, Math.min(coinType.getMaxBagSize(), clickedStack.getCount()+carriedMoney.getAmount(coinType))); //set the amount of the carried money to the max bag size
            clickedSlot.set(MoneyBagItem.createMoneyBag(carriedMoney)); //set the clicked slot to a money bag with the carried money
            container.setCarried(new ItemStack(coinType.getItem().get(), rest)); //set the carried stack to the remaining count
        }
            
        else if (clickedMouseButton == MouseButton.RIGHT_CLICK) { //on right click
            int rest = Math.max(0, clickedStack.getCount()+carriedMoney.getAmount(coinType) - coinType.getMaxBagSize()); //get the remaining count of coins (the max is for safety)
            carriedMoney.setAmount(coinType, Math.min(coinType.getMaxBagSize(), clickedStack.getCount()+carriedMoney.getAmount(coinType))); //set the amount of the carried money to the max bag size
            clickedStack.setCount(Math.min(getMaxStackSize(clickedStack), rest)); //set the count of the clicked stack to the remaining count (the min is for safety)
            container.setCarried(MoneyBagItem.createMoneyBag(carriedMoney)); //set the carried stack to a money bag with the carried money
        }
        return false; //return false if no special actions were needed.
    }

    @Override
    public boolean clickedWithCoin(int clickedSlotId, Slot clickedSlot, ItemStack clickedStack, ItemStack carriedStack, CoinType carriedCoinType, MouseButton clickedMouseButton, Player player, AbstractContainerMenu container) {

        if (clickedMouseButton == MouseButton.LEFT_CLICK) { //on left click (whole stack)

            if (coinType == carriedCoinType) { //if coins are of same type
                int combinedCount = clickedStack.getCount() + carriedStack.getCount(); //get the combined count of coins

                if (combinedCount <= coinType.getMaxBagSize()) { //if the combined count is less than or equal to the max bag size (if all the coins fit into a single bag)
                    clickedSlot.set(MoneyBagItem.createMoneyBag(new Money(coinType.getValue(combinedCount)))); //set the clicked slot to a money bag with the combined value
                    carriedStack.setCount(0); //set the count of the carried stack to 0
                    return true; //return true to cancel the click
                }
                else { //if the combined count is greater than the max bag size (if a single bag can't fit all coins)
                    int remainingCount = Math.max(0, combinedCount - coinType.getMaxBagSize()); //get the remaining count of coins (the max is for safety)
                    if (remainingCount > getMaxStackSize(carriedStack)) {return false;} //if the remaining count is greater than the max stack size of the carried stack return false to cancel the click (to prevent the player from trying to put more coins into the slot than possible)
                    clickedSlot.set(MoneyBagItem.createMoneyBag(new Money(coinType.getValue(coinType.getMaxBagSize())))); //set the clicked slot to a money bag with the max bag size value
                    carriedStack.setCount(remainingCount); //set the count of the carried stack to the remaining count
                    return true; //return true to cancel the click
                }
            }

            else { //if coins are not of same type
                if (clickedStack.getCount() > coinType.getMaxBagSize()) {return false;} //if the clicked stack is to big to fit into a bag return false to cancel the click
                Money combinedValue = new Money(); //create a new money object to store the combined value
                combinedValue.add(new Money(coinType.getValue(clickedStack.getCount()))); //add the value of the clicked stack to the combined value (creating a money object first before adding will make it saver as it's more likely to not combine the raw value)
                int rest = Math.max(0, carriedStack.getCount() - carriedCoinType.getMaxBagSize()); //get the remaining count of coins (the max is for safety)
                combinedValue.add(new Money(carriedCoinType.getValue(carriedStack.getCount()-rest))); //add the value of the carried coins that fit into a bag to the combined value
                clickedSlot.set(MoneyBagItem.createMoneyBag(combinedValue)); //set the clicked slot to a money bag with the combined value
                carriedStack.setCount(rest); //set the count of the carried stack to the remaining count
                return true; //return true to cancel the click
            }
            
        }

        else if (clickedMouseButton == MouseButton.RIGHT_CLICK) { //on right click

            if (coinType == carriedCoinType) { //if coins are of same type
                if (clickedStack.getCount()+1 > getMaxStackSize(clickedStack)) {//if the clicked stack can't fit another coin (if the stack is full)
                    if (clickedStack.getCount()+1 > coinType.getMaxBagSize()) {return false;} //if both the stack and the bag couldn't handle the coins, return false to cancel the click
                    clickedSlot.set(MoneyBagItem.createMoneyBag(new Money(coinType.getValue(clickedStack.getCount()+1)))); //make the clicked slot a money bag instead with the value of the new stack count
                    carriedStack.shrink(1); //remove one coin from the carried stack
                    return true; //return true to cancel the click
                }
            }

            else { //if coins are not of same type
                if (clickedStack.getCount() > coinType.getMaxBagSize()) {return false;} //if the clicked stack is to big to fit into a bag return false to cancel the click
                if (1 > carriedCoinType.getMaxBagSize()) {return false;} //if a single coin of the carrued coin is to big to fit into a bag return false to cancel the click
                Money combinedValue = new Money(); //create a new money object to store the combined value
                combinedValue.add(new Money(coinType.getValue(clickedStack.getCount()))); //add the value of the clicked stack to the combined value (creating a money object first before adding will make it saver as it's more likely to not combine the raw value)
                combinedValue.add(new Money(carriedCoinType.getValue())); //add the value of a single coin to the combined value
                clickedSlot.set(MoneyBagItem.createMoneyBag(combinedValue)); //set the clicked slot to a money bag with the combined value
                carriedStack.shrink(1); //remove one coin from the carried stack
                return true; //return true to cancel the click
            }

        }
        return false; //return false if no special actions were needed.
    }

    @Override
    public boolean clickedWithEmpty(int clickedSlotId, Slot clickedSlot, ItemStack clickedStack, ItemStack carriedStack, MouseButton clickedMouseButton, Player player, AbstractContainerMenu container) {
        return false; //clicking coins with an empty cursor does nothing special
    }

}
