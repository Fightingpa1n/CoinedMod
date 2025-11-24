package net.fightingpainter.mc.coined.util.widgets;

import javax.annotation.Nonnull;

import net.fightingpainter.mc.coined.util.RenderHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;

public class CustomButton extends Button {

    protected ResourceLocation texture;

    protected CustomButton(ResourceLocation texture, int width, int height, OnPress onPress) {
        super(0, 0, width, height, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
        this.texture = texture;
    }

    @Override
    public void renderWidget(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (!this.visible) {return;}

        int v = this.isHoveredOrFocused() ? this.height : 0;
        RenderHelper.renderTexture(graphics, texture, this.width, (this.height*2), 0, v, this.width, this.height, this.getX(), this.getY());
        if (this.isHoveredOrFocused()) {this.renderTooltip();}
    }

    protected void renderTooltip() {
        //Override to add tooltip functionality
    }

    @Override
    public boolean isFocused() {return false;} //disable focus
}
