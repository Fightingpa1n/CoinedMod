package net.fightingpainter.mc.coined.gui.purse;

import java.time.Duration;
import javax.annotation.Nonnull;

import net.fightingpainter.mc.coined.Coined;
import net.fightingpainter.mc.coined.gui.CustomButton;
import net.fightingpainter.mc.coined.util.RenderHelper;
import net.fightingpainter.mc.coined.util.Txt;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

/** Button for the purse GUI */
@OnlyIn(Dist.CLIENT)
public class PurseButton extends CustomButton {
    private boolean useOpenSprites = false; //track if the button should use the open sprites or not

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Coined.MOD_ID, "textures/gui/purse/button.png"); //button texture (for testing)

    private static final int BUTTON_WIDTH = 11; //width of the button
    private static final int BUTTON_HEIGHT = 14; //height of the button

    public PurseButton(int offsetX, int offsetY, OnPress onPress) { //constructor
        super(TEXTURE, BUTTON_WIDTH, BUTTON_HEIGHT, offsetX, offsetY, onPress);
    }

    @Override
    public void renderWidget(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderCW(graphics, this.getX(), this.getY());
    }

    @Override
    public void render(@Nonnull GuiGraphics graphics, int posX, int posY) {
        int u = useOpenSprites ? BUTTON_WIDTH : 0;
        int v = isHoveredOrFocused() ? BUTTON_HEIGHT : 0;
        RenderHelper.renderTexture(graphics, TEXTURE, BUTTON_WIDTH*2, BUTTON_HEIGHT*2, u, v, BUTTON_WIDTH, BUTTON_HEIGHT, posX, posY);
    }

    @Override
    protected void renderTooltip() {
        setTooltipDelay(Duration.ofSeconds(2)); //tooltip delay
        if (!useOpenSprites) {setTooltip(Tooltip.create(Txt.colored(Txt.trans("purse.button.open"), Txt.TOOLTIP)));} //set the tooltip based on if the button should use the open sprites
        else {
            if (Screen.hasShiftDown()) { setTooltip(Tooltip.create(Txt.colored(Txt.trans("purse.button.reset"), Txt.TOOLTIP)));} //set the tooltip based on if the button should use the open sprites
            else {setTooltip(Tooltip.create(Txt.colored(Txt.trans("purse.button.close"), Txt.TOOLTIP)));} //set the tooltip based on if the button should use the open sprites
        }
    }

    /** set whether the button should use the open sprites or not */
    public void toggleOpenSprites(boolean useOpenSprites) {this.useOpenSprites = useOpenSprites;} //toggle the button sprites
    
    /** @return if the button uses the open sprites */
    public boolean usesOpenSprites() {return useOpenSprites;} //return if the button uses the open sprites
    
}
