package net.fightingpainter.mc.coined.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import net.fightingpainter.mc.coined.currency.CoinType;
import net.fightingpainter.mc.coined.items.CoinItem;
import net.fightingpainter.mc.coined.items.CurrencyItem;
import net.fightingpainter.mc.coined.items.MoneyBagItem;
import net.fightingpainter.mc.coined.util.Money;
import net.fightingpainter.mc.coined.util.MouseButton;


@Mixin(AbstractContainerMenu.class)
public class InventoryMixin {

    @Inject(method = "clicked", at = @At("HEAD"), cancellable = true)
    private void onSlotClicked(int slotId, int button, ClickType clickType, Player player, CallbackInfo ci) {
        if (!player.level().isClientSide()) {
            AbstractContainerMenu container = (AbstractContainerMenu)(Object)this; //get container
            Slot slot = container.getSlot(slotId); //get slot
            ItemStack clickedStack = slot.getItem(); //get itemstack
            ItemStack carriedStack = container.getCarried(); //get carried itemstack
            
            if (!clickedStack.isEmpty() && clickedStack.getItem() instanceof CurrencyItem clickedCurrencyItem) { //if clicked a currency item
                if (clickType == ClickType.PICKUP) { //if pick up (right click and left click)
                    MouseButton clickedMouseButton = button == 0 ? MouseButton.LEFT_CLICK : MouseButton.RIGHT_CLICK; //get the clicked mouse button
                    
                    if (!carriedStack.isEmpty() && carriedStack.getItem() instanceof MoneyBagItem carriedMoneyBagItem) { //if clicked with a money bag
                        Money carriedMoney = carriedMoneyBagItem.getValue(carriedStack).copy(); //get the money value of the carried money bag item
                        boolean cancel = clickedCurrencyItem.clickedWithBag(slotId, slot, clickedStack, carriedStack, carriedMoney, clickedMouseButton, player, container); //call clickedWithBag method
                        if (cancel) { //if method did something and it should stop the rest of the click handler
                            container.broadcastChanges(); //broadcast changes if the click handler should stop
                            ci.cancel(); //cancel the click if the method returned true
                        }
                    }
                    else if (!carriedStack.isEmpty() && carriedStack.getItem() instanceof CoinItem carriedCoinItem) { //if clicked with a coin
                        CoinType carriedCoinType = carriedCoinItem.getCoinType(); //get the coin type of the carried coin
                        boolean cancel = clickedCurrencyItem.clickedWithCoin(slotId, slot, clickedStack, carriedStack, carriedCoinType, clickedMouseButton, player, container); //call clickedWithCoin method
                        if (cancel) { //if method did something and it should stop the rest of the click handler
                            container.broadcastChanges(); //broadcast changes if the click handler should stop
                            ci.cancel(); //cancel the click if the method returned true
                        }
                    }
                    else if (carriedStack.isEmpty()) { //if clicked with nothing
                        boolean cancel = clickedCurrencyItem.clickedWithEmpty(slotId, slot, clickedStack, carriedStack, clickedMouseButton, player, container); //call clickedWithEmpty method
                        if (cancel) { //if method did something and it should stop the rest of the click handler
                            container.broadcastChanges(); //broadcast changes if the click handler should stop
                            ci.cancel(); //cancel the click if the method returned true
                        }
                    }

                }
            }

        }
    }
}
