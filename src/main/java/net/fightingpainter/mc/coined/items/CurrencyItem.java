package net.fightingpainter.mc.coined.items;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.level.Level;

import net.fightingpainter.mc.coined.Coined;
import net.fightingpainter.mc.coined.data.ModDataTypes;
import net.fightingpainter.mc.coined.gui.currency.BalanceManager;
import net.fightingpainter.mc.coined.gui.currency.CoinType;
import net.fightingpainter.mc.coined.gui.currency.CurrencyTxt;
import net.fightingpainter.mc.coined.util.Money;
import net.fightingpainter.mc.coined.util.Txt;

public class CurrencyItem extends Item {
        private static final DataComponentType<Money> COMPONENT = ModDataTypes.MONEY.get(); //the money data component

    //======================================== Constructor ========================================\\
    public CurrencyItem(Properties properties) {
        super(properties);
    }

    //======================================== Item Behavior ========================================\\
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, @Nonnull Player player, @Nonnull InteractionHand hand) {
        if (!world.isClientSide) { //Ensure server-side execution
            Coined.LOGGER.info("CurrencyItem used");
            ItemStack stack = player.getItemInHand(hand);
            long value = getValue(stack);
            BalanceManager.addPlayerBalance(player, value);
            player.displayClientMessage(Txt.concat(CurrencyTxt.valueLabel(value, CurrencyTxt.TOTAL), " ", Txt.colored(Txt.trans("added_to_purse"), Txt.GREEN)), true); //Display a message to the player with the value added to their purse
            if (!player.isCreative()) {
                player.setItemInHand(hand, ItemStack.EMPTY); //Remove the item
            }

            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    //==================== Inventory ====================\\
    @Override
    public boolean overrideOtherStackedOnMe(@Nonnull ItemStack stack, @Nonnull ItemStack other, @Nonnull Slot slot, @Nonnull ClickAction action, @Nonnull Player player, @Nonnull SlotAccess access) {
        if (other.getItem() instanceof CurrencyItem otherItem) {
            Money money = getMoney(stack); //get the money from the current item
            if (action.equals(ClickAction.SECONDARY)) {
                if (otherItem instanceof MoneyBagItem) { // on right click with money bag, try to add clicked to other and put rest in slot
                    Money otherMoney = otherItem.getMoney(other); //get the money from the other item
                    Money restMoney = restCombineMoney(money, otherMoney); //combine the money and the other money
                    access.set(createCurrencyItem(money)); //set the current item to the combined money item
                    slot.set(createCurrencyItem(restMoney)); //set the slot to the rest money item
                } else if (otherItem instanceof CoinItem) { //on right click with coin, only move 1
                    CoinType coinType = ((CoinItem) otherItem).getCoinType(); //get the coin type from the other item
                    int totalAmount = coinType.getAmount(money) + 1; //get the total amount of coins in the money object
                    if (totalAmount <= coinType.getMaxBagAmount()) { //if the total amount is less than or equal to the max bag amount
                        coinType.setAmountInMoney(money, totalAmount); //set the amount in the money object
                        slot.set(createCurrencyItem(money)); //set the current item to the combined money item
                        other.shrink(1);
                        if (other.getCount() <= 0) {access.set(ItemStack.EMPTY);} //if the other item is empty, remove it
                        return true; //return true to indicate some action was performed
                    }
                }
            } else {
                Money otherMoney = otherItem.getMoney(other); //get the money from the other item
                Money restMoney = restCombineMoney(money, otherMoney); //combine the money and the other money
                slot.set(createCurrencyItem(money)); //set the current item to the combined money item
                access.set(createCurrencyItem(restMoney)); //set the slot to the rest money item
                return true; //return true to indicate some action was performed
            }
        } else if (other.isEmpty()) {
            if (action.equals(ClickAction.SECONDARY) && stack.getItem() instanceof MoneyBagItem) { //if right click and the current item is a MoneyBagItem
                Money money = getMoney(stack); //get the money from the current item
                List<CoinType> presentCoinTypes = CoinType.getCoinTypesInMoney(money); //get the coin types in the money object
                if (presentCoinTypes.isEmpty()) {return false;} //return false if no coin types are present
                CoinType coinTypeToSubtract = presentCoinTypes.getFirst();
                int amountToSubtract = Math.clamp(coinTypeToSubtract.getAmount(money), 1, coinTypeToSubtract.getMaxStackSize());
                amountToSubtract = Math.max(1, amountToSubtract/2); //halve the amount to subtract, but ensure it's at least 1
                coinTypeToSubtract.setAmountInMoney(money, coinTypeToSubtract.getAmount(money) - amountToSubtract); //subtract the amount from the money object
                slot.set(createCurrencyItem(money)); //set the slot to the money bag item with the updated money
                ItemStack coinItem = new ItemStack(coinTypeToSubtract.getItem(), amountToSubtract); //create a new itemstack with the coin type item and the amount
                access.set(coinItem); //set the slot access to the coin item
                return true;
            }
        } return false; //return false if nothing happened
    }

    @Override
    public boolean overrideStackedOnOther(@Nonnull ItemStack stack, @Nonnull Slot slot, @Nonnull ClickAction action, @Nonnull Player player) {
        //if MoneyBagItem on Empty slot right click,
        if (stack.getItem() instanceof MoneyBagItem stackItem) {
            if (action.equals(ClickAction.SECONDARY) && !slot.hasItem()) {
                Money money = stackItem.getMoney(stack); //get the money from the current item
                List<CoinType> presentCoinTypes = CoinType.getCoinTypesInMoney(money); //get the coin types in the money object
                if (presentCoinTypes.isEmpty()) {return false;} //return false if no coin types are present
                CoinType coinTypeToSubtract = presentCoinTypes.getFirst(); //get the first coin type
                int amountToSubtract = Math.clamp(coinTypeToSubtract.getAmount(money), 1, coinTypeToSubtract.getMaxStackSize());
                coinTypeToSubtract.setAmountInMoney(money, coinTypeToSubtract.getAmount(money) - amountToSubtract); //subtract the amount from the money object
                AbstractContainerMenu menu = player.containerMenu; //get the player's container menu
                menu.setCarried(createCurrencyItem(money)); //set the carried item to the money bag item with the updated money
                ItemStack coinItem = new ItemStack(coinTypeToSubtract.getItem(), amountToSubtract); //create a new itemstack with the coin type item and the amount
                slot.set(coinItem); //set the slot to the coin item
                return true; //return true to indicate some action was performed
            }
        } return false;
    }





    //======================================== Money stuff ========================================\\
    /**
     * Get the value of the currency item
     * @param stack the itemstack to get the value from
     * @return the value of the currency item
    */
    public long getValue(ItemStack stack) {
        return 0l;
    }

    public Money getMoney(ItemStack stack) {
        return new Money();
    }




    //======================================== Inventory Stack Methods ========================================\\
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

    public static ItemStack createCurrencyItem(Money money) {
        if (!money.isEmpty()) {
            List<CoinType> presentCoinTypes = CoinType.getCoinTypesInMoney(money); //get the coin types in the money object
            if (presentCoinTypes.size() == 1) { //if only one coin type is present
                CoinType coinType = presentCoinTypes.get(0); //get the coin type
                int amount = coinType.getAmount(money); //get the amount of the coin type in the money object
                if (amount <= coinType.getMaxStackSize()) {return new ItemStack(coinType.getItem(), amount);} //create a new itemstack with the coin type item and the amount
            }
            ItemStack moneyBag = new ItemStack(ModItems.MONEY_BAG.get()); //create a new money bag itemstack
            setMoneyData(moneyBag, money); //set the money data to the money bag itemstack
            return moneyBag; //return the money bag itemstack
        } else {return ItemStack.EMPTY;} //if money is empty, return an empty itemstack
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


    //======================================== NBT(Components) ========================================\\
    //==================== Get ====================\\
    /**
     * Get the Money Data from the ItemStack
     * @param stack the ItemStack to get the Money Data from
     * @return the Money Data from the ItemStack or null if no data is present
    */
    public static Money getMoneyData(ItemStack stack) {
        return stack.get(COMPONENT); //get the money data from the itemstack
    }

    /**
     * Get the Money Data from the ItemStack or creates it, if it doesn't exist
     * @param stack the ItemStack to get the Money Data from
     * @return the Money Data from the ItemStack
    */
    public static Money getOrCreateMoneyData(ItemStack stack) {
        Money data = getMoneyData(stack); //get the money data from the itemstack
        if (data == null) {
            data = new Money(); //create a new Money object if no data is present
            setMoneyData(stack, data); //set the Money Data to the ItemStack
        } return data; //return null if no money data is present
    }
    
    //==================== Set ====================\\
    /**
     * Set the Money Data to the ItemStack
     * @param stack the ItemStack to set the Money Data to
     * @param money the Money Data to set 
    */
    public static void setMoneyData(ItemStack stack, Money money) {
        stack.set(COMPONENT, money); //set the money data to the itemstack
    }




}
