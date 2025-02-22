package net.fightingpainter.mc.coined.items;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.fightingpainter.mc.coined.currency.BalanceManager;
import net.fightingpainter.mc.coined.util.Txt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.level.Level;

public class CurrencyItem extends Item {

    public CurrencyItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) { // Ensure server-side execution
            ItemStack stack = player.getItemInHand(hand);
            long value = getValue(stack);
            
            //log value in chat (send a message to the player)
            player.sendSystemMessage(Txt.text("Balance: "+BalanceManager.getBalance(player).toString()));

            // Add to player balance (implement your custom logic for balance handling)
            // For example, Currency.addPlayerBalance(player, value);
            
            //player.setItemInHand(hand, ItemStack.EMPTY); // Remove the item
            //return InteractionResultHolder.sidedSuccess(ItemStack.EMPTY, world.isClientSide);
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    /**
     * The getValue Method of a CurrencyItem is used to get the value of the item stack and is really important.
     * so it has to be overriden
     * @param stack The ItemStack to get the value of (itemstack is bassically the Item ingame meaning the item+stuff like count or nbt)
     * @return The value of the item stack
     */
    public long getValue(ItemStack stack) {
        return 0; // Override in subclasses to provide specific values
    }
}
