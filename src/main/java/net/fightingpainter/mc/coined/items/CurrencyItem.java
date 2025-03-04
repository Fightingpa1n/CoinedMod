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
}
