package net.fightingpainter.mc.coined.gui.purse;

import java.time.Duration;
import java.util.function.BiConsumer;

import javax.annotation.Nonnull;

import net.fightingpainter.mc.coined.Coined;
import net.fightingpainter.mc.coined.gui.currency.BalanceManager;
import net.fightingpainter.mc.coined.gui.currency.CoinType;
import net.fightingpainter.mc.coined.gui.currency.CurrencyTxt;
import net.fightingpainter.mc.coined.util.widgets.CustomButton;
import net.fightingpainter.mc.coined.util.Money;
import net.fightingpainter.mc.coined.util.RenderHelper;
import net.fightingpainter.mc.coined.util.Txt;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.ScreenEvent;

public class CoinAmount extends AbstractWidget {

    //class for displaying coin amounts in the Purse Window.
    //meaning we have multiple buttons and stuff

    private final CoinType coinType;

    private static final int WIDTH = 80;
    private static final int HEIGHT = 18;
    
    private static final int COIN_DISPLAY_OFFSET_X = 0;
    private static final int COIN_DISPLAY_OFFSET_Y = 1;
    private final CoinDisplay coinDisplay;

    private static final int COIN_AMOUNT_TEXT_OFFSET_X = 17;
    private static final int COIN_AMOUNT_TEXT_OFFSET_Y = 5;
    
    private static final int ADD_BUTTON_OFFSET_X = 71;
    private static final int ADD_BUTTON_OFFSET_Y = 0;
    private final AddButton addButton;

    private static final int SUBTRACT_BUTTON_OFFSET_X = 71;
    private static final int SUBTRACT_BUTTON_OFFSET_Y = 9;
    private final SubtractButton subtractButton;

    
    public CoinAmount(CoinType coinType, BiConsumer<CoinType, Boolean> onWithdrawCoinChange) {
        super(0, 0, WIDTH, HEIGHT, Txt.empty());
        this.active = false;
        this.coinType = coinType;

        this.coinDisplay = new CoinDisplay(coinType);
        this.addButton = new AddButton(button -> {onWithdrawCoinChange.accept(this.coinType, true);});
        this.subtractButton = new SubtractButton(button -> {onWithdrawCoinChange.accept(this.coinType, false);});
    }

    public void init(ScreenEvent.Init event) {
        event.addListener(this); //register this CoinAmount as a listener to the screen events
        event.addListener(this.coinDisplay);
        event.addListener(this.addButton);
        event.addListener(this.subtractButton);
    }

    public void toggleVisibility(boolean visible) {
        this.visible = visible;
        this.coinDisplay.visible = visible;
        this.coinDisplay.active = visible;
        this.addButton.visible = visible;
        this.addButton.active = visible;
        this.subtractButton.visible = visible;
        this.subtractButton.active = visible;
    }

    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);
        this.coinDisplay.setPosition(x + COIN_DISPLAY_OFFSET_X,  y + COIN_DISPLAY_OFFSET_Y);
        this.addButton.setPosition(x + ADD_BUTTON_OFFSET_X, y + ADD_BUTTON_OFFSET_Y);
        this.subtractButton.setPosition(x + SUBTRACT_BUTTON_OFFSET_X, y + SUBTRACT_BUTTON_OFFSET_Y);
    }

    @Override
    protected void renderWidget(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, 301); //Move to a higher Z-level (300 is above most elements)

        Money playerMoney = BalanceManager.getPlayerMoney(BalanceManager.getCurrentPlayer());
        int coinAmount = this.coinType.getAmount(playerMoney);
        RenderHelper.renderText(graphics, String.valueOf(coinAmount), RenderHelper.getFont(), coinType.getNameColor(), true, this.getX() + COIN_AMOUNT_TEXT_OFFSET_X, this.getY() + COIN_AMOUNT_TEXT_OFFSET_Y);

        graphics.pose().popPose();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {} //No narration for now
    
    private class CoinDisplay extends AbstractWidget {
        
        private final CoinType coinType;
        
        public CoinDisplay(CoinType coinType) {
            super(0, 0, 16, 16, Txt.empty());
            this.coinType = coinType;
        }

        @Override
        protected void renderWidget(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            graphics.pose().pushPose();
            graphics.pose().translate(0, 0, 301); //Move to a higher Z-level (300 is above most elements)
            
            graphics.renderFakeItem(new ItemStack(this.coinType.getItem()), this.getX(), this.getY());

            if (this.isHoveredOrFocused()) {
                long singleValue = this.coinType.getValue(); //get single coin value
                setTooltip(Tooltip.create(CurrencyTxt.valueLabel(singleValue, this.coinType.getNameColor())));
            }

            graphics.pose().popPose();
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {} //No narration for now
    }

    private class AddButton extends CustomButton {
        private static final ResourceLocation BUTTON_TEXTURE = ResourceLocation.fromNamespaceAndPath(Coined.MOD_ID, "textures/gui/purse/add_button.png");
        private static final int BUTTON_WIDTH = 9;
        private static final int BUTTON_HEIGHT = 9;

        public AddButton(OnPress onPress) {super(BUTTON_TEXTURE, BUTTON_WIDTH, BUTTON_HEIGHT, onPress);}
        
        @Override
        public void renderWidget(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            graphics.pose().pushPose();
            graphics.pose().translate(0, 0, 301); // Move to a higher Z-level (300 is above most elements)
            super.renderWidget(graphics, mouseX, mouseY, partialTick); //call the parent render method
            graphics.pose().popPose();
        }

        @Override
        protected void renderTooltip() {
            setTooltipDelay(Duration.ofSeconds(1));
            if (Screen.hasShiftDown()) { setTooltip(Tooltip.create(Txt.colored(Txt.trans("purse.button.add_ten"), Txt.TOOLTIP)));}
            else { setTooltip(Tooltip.create(Txt.colored(Txt.trans("purse.button.add_one"), Txt.TOOLTIP)));}
        }
    }

    private class SubtractButton extends CustomButton {
        private static final ResourceLocation BUTTON_TEXTURE = ResourceLocation.fromNamespaceAndPath(Coined.MOD_ID, "textures/gui/purse/subtract_button.png");
        private static final int BUTTON_WIDTH = 9;
        private static final int BUTTON_HEIGHT = 9;

        public SubtractButton(OnPress onPress) {super(BUTTON_TEXTURE, BUTTON_WIDTH, BUTTON_HEIGHT, onPress);}

        @Override
        public void renderWidget(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            graphics.pose().pushPose();
            graphics.pose().translate(0, 0, 301); // Move to a higher Z-level (300 is above most elements)
            super.renderWidget(graphics, mouseX, mouseY, partialTick); //call the parent render method
            graphics.pose().popPose();
        }

        @Override
        public void renderTooltip() {
            setTooltipDelay(Duration.ofSeconds(1));
            if (Screen.hasShiftDown()) { setTooltip(Tooltip.create(Txt.colored(Txt.trans("purse.button.subtract_ten"), Txt.TOOLTIP)));}
            else { setTooltip(Tooltip.create(Txt.colored(Txt.trans("purse.button.subtract_one"), Txt.TOOLTIP)));}
        }
    }
}
