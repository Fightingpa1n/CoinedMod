package net.fightingpainter.mc.coined.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.fightingpainter.mc.coined.Coined;
import net.fightingpainter.mc.coined.currency.CoinType;
import net.fightingpainter.mc.coined.items.CoinItem;
import net.fightingpainter.mc.coined.items.CurrencyItem;
import net.fightingpainter.mc.coined.items.MoneyBagItem;
import net.fightingpainter.mc.coined.util.Money;


@Mixin(AbstractContainerMenu.class)
public class InventoryMixin {

    @Inject(method = "clicked", at = @At("HEAD"), cancellable = true)
    private void onSlotClicked(int slotId, int button, ClickType clickType, Player player, CallbackInfo ci) {
        if (!player.level().isClientSide()) {
            AbstractContainerMenu self = (AbstractContainerMenu)(Object)this;
            if (slotId >= 0 && slotId < self.slots.size()) {
                Slot slot = self.slots.get(slotId);
                ItemStack slotStack = slot.getItem(); //the item that was clicked
                ItemStack carried = self.getCarried(); //the item the cursor is carrying


                if (!carried.isEmpty() && carried.getItem() instanceof CurrencyItem) {

                    if (!slotStack.isEmpty() && slotStack.getItem() instanceof CurrencyItem) {
                        Item carriedItem = carried.getItem();
                        Item slotItem = slotStack.getItem();

                        if (carriedItem instanceof CoinItem carriedCoin) { //if carrying a coin
                            
                            if (slotItem instanceof CoinItem clickedCoin) { //if clicking on coin
                                if (carriedCoin.getCoinType() != clickedCoin.getCoinType()) {


                                }
                            }
                        }


                        if (slotItem instanceof MoneyBagItem clickedMoneyBag) { //if clicking on money bag

                            if (carriedItem instanceof CoinItem carriedCoin) { //if carrying a coin
                                //pn right click (where normaly you take half of a clicked stack or place 1 item from cursor to slot)
                                if (clickType == ClickType.PICKUP) {
                                    Money bagData = MoneyBagItem.getMoneyData(slotStack);

                                    switch (carriedCoin.getCoinType()) {
                                        case CoinType.COPPER:
                                            if (bagData.getCopperAmount() + 1 < MoneyBagItem.MAX_COPPER_COINS) {
                                                bagData.setCopperAmount(bagData.getCopperAmount()+1);
                                                MoneyBagItem.setMoneyData(slotStack, bagData);
                                                carried.shrink(1);
                                            }
                                            break;

                                    }


                                    




                                }


                            }


                        }







                    }
                }
            }
        }
    }
    
}
