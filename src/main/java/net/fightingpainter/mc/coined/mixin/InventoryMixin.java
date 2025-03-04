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
            AbstractContainerMenu container = (AbstractContainerMenu)(Object)this; //get container
            Slot slot = container.getSlot(slotId); //get slot
            ItemStack clickedStack = slot.getItem(); //get itemstack
            ItemStack carriedStack = container.getCarried(); //get carried itemstack
            
            if (!clickedStack.isEmpty() && clickedStack.getItem() instanceof CurrencyItem clickedCurrencyItem) { //if clicked a currency item
                if (clickType == ClickType.PICKUP) { //if pick up (right click and left click)
                    MouseButton clickedMouseButton = button == 0 ? MouseButton.LEFT_CLICK : MouseButton.RIGHT_CLICK; //get the clicked mouse button

                    //if click a money bag item
                    if (clickedStack.getItem() instanceof MoneyBagItem) {
                        
                        //if click with coin
                        if (!carriedStack.isEmpty() && carriedStack.getItem() instanceof CoinItem) {
                            //if left click (add whole stack to bag)
                            if (clickedMouseButton == MouseButton.LEFT_CLICK) {
                                MoneyBagItem.addBagToBag(clickedStack, carriedStack); //add the whole stack to the bag
                                container.setCarried(ItemStack.EMPTY); //set carried to empty
                                container.broadcastChanges(); //broadcast changes
                                ci.cancel(); //cancel the click
                            }

                            //if right click (add single coin to bag)
                            else if (clickedMouseButton == MouseButton.RIGHT_CLICK) {
                                ItemStack singularCoin = carriedStack.copy(); //copy the carried stack
                                singularCoin.setCount(1); //set the count to 1
                                MoneyBagItem.addBagToBag(clickedStack, singularCoin); //add the singular coin to the bag
                                carriedStack.shrink(1); //shrink the carried stack
                                container.broadcastChanges(); //broadcast changes
                                ci.cancel(); //cancel the click
                            }
                            
                        }
                        
                        //if left/right click with money bag (add bag to bag)
                        else if (!carriedStack.isEmpty() && carriedStack.getItem() instanceof MoneyBagItem) {
                            MoneyBagItem.addBagToBag(clickedStack, carriedStack); //add the whole stack to the bag
                            container.setCarried(ItemStack.EMPTY); //set carried to empty
                            container.broadcastChanges(); //broadcast changes
                            ci.cancel(); //cancel the click
                        }

                        //if right click with nothing. (take out a coin)
                        else if (carriedStack.isEmpty() && clickedMouseButton == MouseButton.RIGHT_CLICK) {
                            //pass
                        }
                    }

                    
                    //if click a coin item
                    else if (clickedStack.getItem() instanceof CoinItem) {

                        //if click with money bag
                        if (!carriedStack.isEmpty() && carriedStack.getItem() instanceof MoneyBagItem) {
                            //if left click (add coins to bag and place bag in slot)
                            if (clickedMouseButton == MouseButton.LEFT_CLICK) {
                                MoneyBagItem.addCoinToBag(carriedStack, clickedStack); //add the whole stack to the bag
                                slot.set(carriedStack); //set the slot to the bag
                                container.setCarried(ItemStack.EMPTY); //set carried to empty
                                container.broadcastChanges(); //broadcast changes
                                ci.cancel(); //cancel the click
                            }

                            //if right click (add coins to bag but keep bag carried)
                            else if (clickedMouseButton == MouseButton.RIGHT_CLICK) {
                                MoneyBagItem.addCoinToBag(carriedStack, clickedStack); //add the whole stack to the bag
                                slot.set(ItemStack.EMPTY); //set the slot to empty
                                container.setCarried(clickedStack); //set carried to the bag
                                container.broadcastChanges(); //broadcast changes
                                ci.cancel(); //cancel the click
                            }
                        }

                        //if click with coin
                        else if (!carriedStack.isEmpty() && carriedStack.getItem() instanceof CoinItem) {
                            
                            //if left click
                            if (clickedMouseButton == MouseButton.LEFT_CLICK) {

                                //if same coin type and combined stack exeeds max stack size or two different coin types (create bag item and put in slot)
                                if (clickedStack.getItem() == carriedStack.getItem() && clickedStack.getCount() + carriedStack.getCount() > clickedStack.getMaxStackSize() || clickedStack.getItem() != carriedStack.getItem()) {
                                    

                                }

                            }

                        



                        }
                        
                    }
                }
            }

        }
    }


    private enum MouseButton {
        LEFT_CLICK,
        RIGHT_CLICK
    }

}
