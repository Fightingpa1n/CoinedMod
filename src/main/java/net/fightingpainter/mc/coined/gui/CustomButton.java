package net.fightingpainter.mc.coined.gui;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.checkerframework.checker.units.qual.C;
import org.checkerframework.checker.units.qual.s;

import net.fightingpainter.mc.coined.util.RenderHelper;
import net.fightingpainter.mc.coined.util.Txt;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.ScreenEvent;

public abstract class CustomButton extends CustomWidget {

    //======================================== Variables ========================================\\
    protected final ResourceLocation texture; //texture of the button
    protected final CustomButton.OnPress onPress; //action to perform when the button is pressed

    //======================================== Constructors ========================================\\
    /**
     * Constructor for a custom button with a specific texture.
     * @param texture the texture of the button (the texture should be (width) x (height*2) and the top half is the normal state and the bottom half is the hovered state)
     * @param width the width of the button
     * @param height the height of the button
     * @param offsetX the x offset of the button relative to where it is rendered (i.e. the parent widget's position)
     * @param offsetY the y offset of the button relative to where it is rendered (i.e. the parent widget's position)
     * @param onPress The action to perform when the button is pressed.
    */
    public CustomButton(ResourceLocation texture, int width, int height, int offsetX, int offsetY, OnPress onPress) {
        super(width, height, offsetX, offsetY);
        this.texture = texture;
        this.onPress = onPress;
    }

    /**
     * Constructor for a custom button with a specific texture.
     * @param texture the texture of the button (the texture should be (width) x (height*2) and the top half is the normal state and the bottom half is the hovered state)
     * @param width the width of the button
     * @param height the height of the button
     * @param onPress The action to perform when the button is pressed.
    */
    public CustomButton(ResourceLocation texture, int width, int height, OnPress onPress) {
        this(texture, width, height, 0, 0, onPress);
    }

    //======================================== Public Methods ========================================\\
    //==================== Render ====================\\
    @Override
    public void render(@Nonnull GuiGraphics graphics, int posX, int posY) {
        int v = isHoveredOrFocused() ? this.height : 0;
        RenderHelper.renderTexture(graphics, texture, width, height*2, 0, v, width, height, posX, posY);
    }

    //======================================== Click Handling ========================================\\
    protected void onPress() {
        if (onPress != null) {onPress.onPress(this);}
    }
    
    @Override
    public void onClick(double mouseX, double mouseY) {
        this.onPress();
    }

    @OnlyIn(Dist.CLIENT)
    public interface OnPress {
        void onPress(CustomButton button);
    }
}
