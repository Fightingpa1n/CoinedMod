package net.fightingpainter.mc.coined.gui;

import net.fightingpainter.mc.coined.Coined;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.ScreenEvent;

public class PurseWindow {

    private static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(Coined.MOD_ID, "textures/gui/purse/bg.png");
    private final int textureWidth = 80;
    private final int textureHeight = 96;

    // Window position Info
    private int windowPosX = 0;
    private int windowPosY = 0;
    private int windowSizeX = 80;
    private int windowSizeY = 96;

    public void render(ScreenEvent.Render.Post event) { //render the GUI (each frame I think)
        GuiGraphics guiGraphics = event.getGuiGraphics();

        // Save the current matrix and push to render above everything else
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 300); // Move to a higher Z-level (300 is above most elements)

        // Render the background texture
        guiGraphics.blit(BACKGROUND_TEXTURE, windowPosX, windowPosY, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);

        // Render some text inside the embedded GUI
        guiGraphics.drawString(Minecraft.getInstance().font, Component.literal("Purse GUI"), windowPosX + 5, windowPosY + 5, 0xFFFFFF);

        // Restore the previous matrix
        guiGraphics.pose().popPose();
    }

    public boolean isDragArea(double mouseX, double mouseY) { //since I can't just return the dragArea as a whole this is a function instead that will check that directly (Defining the drag area in here)
        int dragAreaStartX = windowPosX; //top left corner of the window
        int dragAreaStartY = windowPosY; //top left corner of the window
        int dragAreaEndX = windowPosX + windowSizeX; //the entire width of the window
        int dragAreaEndY = windowPosY + 20; //Only the top 20 pixels are draggable

        boolean mouseInDragX = mouseX >= dragAreaStartX && mouseX <= dragAreaEndX; //check if the mouse is within the X bounds of the drag area
        boolean mouseInDragY = mouseY >= dragAreaStartY && mouseY <= dragAreaEndY; //check if the mouse is within the Y bounds of the drag area
        return mouseInDragX && mouseInDragY; //return if the mouse is within the bounds of the drag area
    }


    /** returns the X postion of the Purse Window */
    public int getPosX() {return windowPosX;}
    /** returns the Y postion of the Purse Window */
    public int getPosY() {return windowPosY;}

    /** returns the X size of the Purse Window */
    public int getSizeX() {return windowSizeX;}
    /** returns the Y size of the Purse Window */
    public int getSizeY() {return windowSizeY;}

    /** 
     * sets the X and Y postion of the Purse Window
     * @param x the X postion
     * @param y the Y postion
    */
    public void setPos(int x, int y) {
        windowPosX = x;
        windowPosY = y;
    }
}
