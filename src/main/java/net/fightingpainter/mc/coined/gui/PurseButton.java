package net.fightingpainter.mc.coined.gui;

import java.time.Duration;

import net.fightingpainter.mc.coined.Coined;
import net.fightingpainter.mc.coined.util.Txt;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

/** Button for the purse GUI */
@OnlyIn(Dist.CLIENT)
public class PurseButton extends Button {
    private boolean useOpenSprites = false; //track if the button should use the open sprites or not

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Coined.MOD_ID, "textures/gui/purse/button.png"); //button texture (for testing)
    private static final int TEXTURE_WIDTH = 22; //width of the button texture
    private static final int TEXTURE_HEIGHT = 28; //height of the button texture
    private static final int OPEN_OFFSET = 11; //offset for the open button texture
    private static final int HOVER_OFFSET = 14; //offset for the hover button texture

    private static final int BUTTON_WIDTH = 11; //width of the button
    private static final int BUTTON_HEIGHT = 14; //height of the button

    public PurseButton(int x, int y, Button.OnPress onPress) { //constructor
        super(x, y, BUTTON_WIDTH, BUTTON_HEIGHT, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
    }

    @Override
    public boolean isFocused() {return false;} //disable focus for the button

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) { //render the button
        int u = useOpenSprites ? OPEN_OFFSET : 0; //set the offset based on if the button should use the open sprites
        int v = isHoveredOrFocused() ? HOVER_OFFSET : 0; //set the offset based on if the button is hovered or focused
        guiGraphics.blit(TEXTURE, getX(), getY(), u, v, width, height, TEXTURE_WIDTH, TEXTURE_HEIGHT); //render the button

        if (isHoveredOrFocused()) { //tooltip

            setTooltipDelay(Duration.ofSeconds(2));

            if (!useOpenSprites) {setTooltip(Tooltip.create(Txt.colored(Txt.trans("button.purse.open"), Txt.TOOLTIP)));} //set the tooltip based on if the button should use the open sprites
            else {
                if (Screen.hasShiftDown()) { setTooltip(Tooltip.create(Txt.colored(Txt.trans("button.purse.reset"), Txt.TOOLTIP)));} //set the tooltip based on if the button should use the open sprites
                else {setTooltip(Tooltip.create(Txt.colored(Txt.trans("button.purse.close"), Txt.TOOLTIP)));} //set the tooltip based on if the button should use the open sprites
            }
        }
    }

    /** set whether the button should use the open sprites or not */
    public void toggleOpenSprites(boolean useOpenSprites) {this.useOpenSprites = useOpenSprites;} //toggle the button sprites
    /** @return if the button uses the open sprites */
    public boolean usesOpenSprites() {return useOpenSprites;} //return if the button uses the open sprites
    
    
}
