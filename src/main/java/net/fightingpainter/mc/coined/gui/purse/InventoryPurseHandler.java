package net.fightingpainter.mc.coined.gui.purse;

import net.fightingpainter.mc.coined.Coined;
import net.fightingpainter.mc.coined.gui.CustomButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.event.ScreenEvent.MouseButtonPressed;
import net.neoforged.neoforge.client.event.ScreenEvent.MouseDragged;
import net.neoforged.neoforge.client.event.ScreenEvent.MouseButtonReleased;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

@OnlyIn(Dist.CLIENT)
public class InventoryPurseHandler {

    /**
     * The Inventory Purse Handler is responsible for integrating the Purse Into the players Inventory.
     * also it handles the PurseButton Logic (opening/closing the purse) as well as the draging of the purse window. 
    */

    private PurseButton button = new PurseButton(0, 0, this::onPurseButtonPress); //PurseButton instance
    private final int purseButtonX = -(4 + button.getWidth()); //x position of the button relativ to the top right corner of the inventory
    private final int purseButtonY = 4; //y position of the button relativ to the top right corner of the inventory

    private final PurseWindow purse = new PurseWindow(); //PurseWindow instance
    private final int initPosX = (-purseButtonX) + 2; //x position of the purse relativ to the purse button
    private final int initPosY = -4; //y position of the purse relativ to the purse button
    private boolean isPurseOpen = false; //track the state of if the purse is visible
    private boolean isInit = true; //is first time opening purse
    
    private boolean isDragging = false; //track if the purse is being dragged
    private int dragOffsetX = 0; //offset of the purse from the mouse
    private int dragOffsetY = 0; //offset of the purse from the mouse

    @SubscribeEvent
    public void onScreenInit(ScreenEvent.Init.Post event) { //screen init get's called on each screen open (meaning reopening the inventory will also call this)
        if (event.getScreen() instanceof InventoryScreen inventoryScreen) {
            int buttonX = (inventoryScreen.getGuiLeft()+inventoryScreen.getXSize()) + purseButtonX; //calculate the x position of the button
            int buttonY = inventoryScreen.getGuiTop() + purseButtonY; //calculate the y position of the button

            Coined.LOGGER.debug("Initializing Inventory Purse Handler: Button Position X: " + buttonX + " Y: " + buttonY);
            button.setPosition(buttonX, buttonY); //set postion of the button
            button.init(event); //init the button
            purse.init(event); //init the purse
        }
    }

    private void onPurseButtonPress(CustomButton _button) { //on button press
        button = (PurseButton) _button; //cast the button to a PurseButton
        if (!isPurseOpen) { //on open
            button.toggleOpenSprites(true); //set the button to use the open sprites
            isPurseOpen = true; //open the purse
            if (isInit) { //if it is the first time opening the purse
                int purseX = button.getX() + initPosX; //purse x position
                int purseY = button.getY() + initPosY; //purse y position
                purse.setPosition(purseX, purseY); //set the position of the purse
                isInit = false; //set isInit to false
            }
        } else { //on close
            if (Screen.hasShiftDown()) { //if shift is pressed don't close and just reset the position
                int purseX = button.getX() + initPosX; //purse x position
                int purseY = button.getY() + initPosY; //purse y position
                purse.setPosition(purseX, purseY); //set the position of the purse
            } else {
                button.toggleOpenSprites(false); //set the button to use the closed sprites
                isPurseOpen = false; //close the purse
                isDragging = false; //stop dragging the purse
            }
        }
        purse.togglePurse(isPurseOpen);
    }

    @SubscribeEvent
    public void onInventoryRender(ScreenEvent.Render.Post event) { //on rendering
        if (!(event.getScreen() instanceof InventoryScreen)) return;

        //reposition the button if the screen is resized
        InventoryScreen inventoryScreen = (InventoryScreen) event.getScreen();

        //get gui right top corner
        int buttonX = (inventoryScreen.getGuiLeft() + inventoryScreen.getXSize()) + purseButtonX; //calculate the x position of the button
        int buttonY = inventoryScreen.getGuiTop() + purseButtonY; //calculate the y position of the button
        button.setPosition(buttonX, buttonY); //set the position of the button
    }

    @SubscribeEvent
    public void onInventoryMouseClick(MouseButtonPressed.Pre event) {
        if (!(event.getScreen() instanceof InventoryScreen)) return; 

        //handle drag start
        if (isPurseOpen) {
            if (purse.isDragArea(event.getMouseX(), event.getMouseY())) { //if you clicked on the drag area
                dragOffsetX = (int) (event.getMouseX() - purse.getX()); //set the offset
                dragOffsetY = (int) (event.getMouseY() - purse.getY()); //set the offset

                isDragging = true;
                event.setCanceled(true); //cancel the event
            }
        }
    }

    @SubscribeEvent
    public void onInventoryMouseDrag(MouseDragged.Pre event) {
        if (!(event.getScreen() instanceof InventoryScreen)) return;

        // Forward mouse drag events to the embedded GUI if it is visible
        if (isPurseOpen) {
            if (isDragging) { //if the purse is being dragged

                // Calculate new position
                int newPosX = (int) (event.getMouseX() - dragOffsetX);
                int newPosY = (int) (event.getMouseY() - dragOffsetY);

                // Ensure the window is not dragged outside the screen
                int screenWidth = event.getScreen().width;
                int screenHeight = event.getScreen().height;

                newPosX = Math.max(0, Math.min(newPosX, screenWidth - purse.getWidth()));
                newPosY = Math.max(0, Math.min(newPosY, screenHeight - purse.getHeight()));

                purse.setPosition(newPosX, newPosY); //set the position of the purse
            }
        }
    }

    @SubscribeEvent
    public void onInventoryMouseRelease(MouseButtonReleased.Post event) {
        if (!(event.getScreen() instanceof InventoryScreen)) return;

        if (isPurseOpen) {
            if (isDragging) {isDragging = false;} //stop dragging the purse
        }
    }

}
