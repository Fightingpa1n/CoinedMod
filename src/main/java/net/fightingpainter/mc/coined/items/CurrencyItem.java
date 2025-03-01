package net.fightingpainter.mc.coined.items;

import javax.annotation.Nonnull;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.level.Level;

import net.fightingpainter.mc.coined.Coined;
import net.fightingpainter.mc.coined.currency.BalanceManager;
import net.fightingpainter.mc.coined.currency.CoinType;
import net.fightingpainter.mc.coined.util.Money;
import net.fightingpainter.mc.coined.util.MouseButton;
import net.fightingpainter.mc.coined.util.Txt;

public class CurrencyItem extends Item {

    public CurrencyItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, @Nonnull Player player, @Nonnull InteractionHand hand) {
        if (!world.isClientSide) { //Ensure server-side execution
            Coined.LOGGER.info("CurrencyItem used");
            ItemStack stack = player.getItemInHand(hand);
            Money value = getValue(stack); //Get the value of the currency item
            Coined.LOGGER.info("Value: " + value.toString());
            BalanceManager.addBalance(player, value); //Add the value to the player's balance
            player.displayClientMessage(Txt.colored(Txt.concat(value.getTotalValue(), " coins"), Txt.GREEN), true);

            if (!player.isCreative()) {
                player.setItemInHand(hand, ItemStack.EMPTY); //Remove the item
            }

            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    /**
     * Get the value of the currency item
     * @param stack the itemstack to get the value from
     * @return the value of the currency item
    */
    public Money getValue(ItemStack stack) {
        return new Money(); //override this method in subclasses
    }
    

    //============================== Click Events ==============================\\
    /**
     * Called when the player clicks on a slot with an Item of this class in it with a MoneyBag in the cursor
     * uses the return of a Boolean to detirmine if something happened and the click should be cancelled
     * @param clickedSlotId the id of the slot that was clicked
     * @param clickedSlot the slot that was clicked where thee item pf this class is in
     * @param clickedStack the item the player clicked on
     * @param carriedStack the item the player is carrying with the cursor
     * @param carriedMoney a copy of the Money object attached to the MoneyBagItem the player is carrying with the cursor
     * @param clickedMouseButton the mouse button that was clicked with
     * @param player the player that clicked
     * @param container the container the click happened in
     * @return true if the click should be cancelled otherwise false if nothing happened
    */
    public boolean clickedWithBag(int clickedSlotId, Slot clickedSlot, ItemStack clickedStack, ItemStack carriedStack, Money carriedMoney, MouseButton clickedMouseButton, Player player, AbstractContainerMenu container) {
        return false; //override this method in subclasses
    }

    /**
     * Called when the player clicks on a slot with an Item of this class in it with an coin Item in the cursor
     * uses the return of a Boolean to detirmine if something happened and the click should be cancelled
     * @param clickedSlotId the id of the slot that was clicked
     * @param clickedSlot the slot that was clicked where thee item pf this class is in
     * @param clickedStack the item the player clicked on
     * @param carriedStack the item the player is carrying with the cursor 
     * @param carriedCoinType the coin type of the item the player is carrying with the cursor
     * @param clickedMouseButton the mouse button that was clicked with
     * @param player the player that clicked
     * @param container the container the click happened in
     * @return true if the click should be cancelled otherwise false if nothing happened
    */
    public boolean clickedWithCoin(int clickedSlotId, Slot clickedSlot, ItemStack clickedStack, ItemStack carriedStack, CoinType carriedCoinType, MouseButton clickedMouseButton, Player player, AbstractContainerMenu container) {
        return false; //override this method in subclasses
    }

    /**
     * Called when the player clicks on a slot with an Item of this class in it with an empty cursor
     * uses the return of a Boolean to detirmine if something happened and the click should be cancelled
     * @param clickedSlotId the id of the slot that was clicked
     * @param clickedSlot the slot that was clicked where thee item pf this class is in
     * @param clickedStack the item the player clicked on
     * @param carriedStack the item the player is carrying with the cursor
     * @param clickedMouseButton the mouse button that was clicked with
     * @param player the player that clicked
     * @param container the container the click happened in
     * @return true if the click should be cancelled otherwise false if nothing happened
    */
    public boolean clickedWithEmpty(int clickedSlotId, Slot clickedSlot, ItemStack clickedStack, ItemStack carriedStack, MouseButton clickedMouseButton, Player player, AbstractContainerMenu container) {
        return false; //override this method in subclasses
    }
}
