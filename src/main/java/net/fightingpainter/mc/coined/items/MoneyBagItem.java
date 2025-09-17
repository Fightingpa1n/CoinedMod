package net.fightingpainter.mc.coined.items;

import java.util.List;
import javax.annotation.Nonnull;

import net.minecraft.client.gui.screens.Screen;
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
import net.fightingpainter.mc.coined.currency.CurrencyTxt;
import net.fightingpainter.mc.coined.data.ModDataTypes;
import net.fightingpainter.mc.coined.util.Money;
import net.fightingpainter.mc.coined.util.Txt;

public class MoneyBagItem extends CurrencyItem {
    private static final DataComponentType<Money> COMPONENT = ModDataTypes.MONEY.get(); //the money data component

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
        Money money = getMoney(stack); //get the money data from the itemstack
        if (money == null || money.isEmpty()) { //if the money is null or empty, return
            tooltipComponents.add(Txt.colored("Empty", Txt.TOOLTIP2));
            return;
        }
        tooltipComponents.add(CurrencyTxt.totalLabel(money.getTotalValue())); //add the total value of the money bag to the tooltip
        if (Screen.hasShiftDown()) {
            List<CoinType> coinTypes = CoinType.getCoinTypesInMoney(money); //get the coin types in the money bag, reversed for better display
            for (CoinType coinType : coinTypes) { //for each coin type in the money bag
                if (coinType.inMoney(money)) {
                    tooltipComponents.add(coinType.createLabel(coinType.getAmount(money))); //add the coin type label to the tooltip
                }
            }
        } else {tooltipComponents.add(Txt.colored(Txt.trans("tooltip.shift_info"), Txt.TOOLTIP2));}
    }


    //============================== Money Stuff ==============================\\
    @Override
    public long getValue(ItemStack stack) {
        return getMoney(stack).getTotalValue();
    }

    @Override
    public Money getMoney(ItemStack stack) {
        return getMoneyData(stack);
    }

    //======================================== Static Methods ========================================\\
    /**
     * Create a new money bag itemstack from a Money object
     * @param money the money to create the money bag from
     * @return the newly created ItemStack
    */
    public static ItemStack createMoneyBag(Money money) {
        ItemStack stack = new ItemStack(ModItems.MONEY_BAG.get());
        setMoneyData(stack, money); //set the money data to the itemstack
        return stack; //return the itemstack
    }

    //======================================== Item Interactions ========================================\\
    

    //=============== Add ===============\\
    /**
     * Add a coin to the money bag
     * @param bagItem the money bag itemstack that the coins should be added to
     * @param coinItem the coin itemstack that should be added to the money bag
     * @return the rest of the coins that couldn't be added to the money bag (todo)
    */
    public static ItemStack addCoinToBag(ItemStack bagItem, ItemStack coinItem) {
        CoinType coinType = CoinType.getCoinType(coinItem); //get the coin type
        Money money = getMoneyData(bagItem); //get the money data
        coinItem.setCount(restAddCoin(money, coinType, coinItem.getCount())); //add the coins to the money bag and get the rest
        if (coinItem.getCount() <= 0) {coinItem = ItemStack.EMPTY;} //if the coin item is empty, set it to empty 
        setMoneyData(bagItem, money); //set the money data to the money bag
        return coinItem; //return the coin item (which is empty if all coins were added)
    }

    /**
     * Add a money bag to the money bag
     * @param bagItem the money bag itemstack that the money bag should be added to
     * @param moneybagItem the money bag itemstack that should be added to the money bag
     * @return the rest of the money bag that couldn't be added to the money bag (todo)
    */
    public static ItemStack addBagToBag(ItemStack bagItem, ItemStack moneybagItem) {
        Money money = getMoneyData(bagItem); //get the money data from the bag item
        Money otherMoney = getMoneyData(moneybagItem); //get the money data from the other money bag
        int rest = 0; //initialize restw
        for (CoinType coinType : CoinType.values()) { //for each coin type
            rest += restAddCoin(money, coinType, coinType.getAmount(otherMoney)); //add the coins to the money bag and get the rest
        } setMoneyData(bagItem, money); //set the money data to the money bag
        if (rest > 0) {moneybagItem = createMoneyBag(restCombineMoney(money, otherMoney));} //if there is a rest, create a new money bag with the rest
        else {moneybagItem = ItemStack.EMPTY;} //if there is no rest, set the money bag to empty
        return moneybagItem; //return the money bag itemstack
    }
    
    //======================================== Helpers ========================================\\
    /**
     * Combines two Money objects and returns the rest of the coins that couldn't be added to the first Money object.
     * @param money the first Money object to combine
     * @param otherMoney the second Money object to combine
     * @return a new Money object containing the rest of the coins that couldn't be added
    */
    private static Money restCombineMoney(Money money, Money otherMoney) {
        Money rest = new Money(); //create a new Money object for the rest
        for (CoinType coinType : CoinType.values()) { //for each coin type
            coinType.setAmountInMoney(rest, restAddCoin(money, coinType, coinType.getAmount(otherMoney))); //add the coins to the rest
        } return rest; //return the rest
    }


    /**
     * Adds a specified amount of coins to a Money object up to the maximum bag amount, and returns the rest coin amount that couldn't be added.
     * @param money the Money object to add the coins to
     * @param coinType the CoinType of the coins to add
     * @param amount the amount of coins to add
     * @return the amount of coins that couldn't be added to the Money object (rest)
    */
    private static int restAddCoin(Money money, CoinType coinType, int amount) {
        int total = coinType.getAmount(money) + amount; //total coin count
        int rest = total - coinType.getMaxBagAmount(); //calculate rest
        if (rest > 0) {coinType.setAmountInMoney(money, coinType.getMaxBagAmount());}
        else {
            coinType.setAmountInMoney(money, total); //set amount to total
            rest = 0; //no rest
        } return rest; //return the rest
    }
}