package net.fightingpainter.mc.coined.gui.purse;

import java.time.Duration;

import javax.annotation.Nonnull;

import net.fightingpainter.mc.coined.Coined;
import net.fightingpainter.mc.coined.gui.CustomButton;
import net.fightingpainter.mc.coined.gui.CustomWidget;
import net.fightingpainter.mc.coined.gui.currency.BalanceManager;
import net.fightingpainter.mc.coined.gui.currency.CoinType;
import net.fightingpainter.mc.coined.gui.currency.CurrencyTxt;
import net.fightingpainter.mc.coined.util.Money;
import net.fightingpainter.mc.coined.util.RenderHelper;
import net.fightingpainter.mc.coined.util.Txt;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CoinAmount extends CustomWidget {

    //class for displaying coin amounts in the Purse Window.
    //meaning we have multiple buttons and stuff

    //to test: I will hardcode copper for now

    private final CoinType coinType;
    private final CoinDisplay coinDisplay;
    private AddButton addButton;
    private SubtractButton subtractButton;
    
    public CoinAmount(CoinType coinType, int offsetX, int offsetY) {
        super(0, 0, offsetX, offsetY);
        this.coinType = coinType;

        this.coinDisplay = (CoinDisplay) addChild(new CoinDisplay(coinType, 0, 0));
        this.addButton = (AddButton) addChild(new AddButton(60, 0, button -> onAddPress()));
        this.subtractButton = (SubtractButton) addChild(new SubtractButton(70, 0, button -> onSubtractPress()));
    }

    @Override
    public void render(@Nonnull GuiGraphics graphics, int x, int y) {
        this.coinDisplay.renderCW(graphics, x, y);
        
        Money playerMoney = BalanceManager.getPlayerMoney(BalanceManager.getCurrentPlayer());
        int coinAmount = this.coinType.getAmount(playerMoney);
        RenderHelper.renderText(graphics, String.valueOf(coinAmount), RenderHelper.getFont(), coinType.getNameColor(), true, x + 18, y + 4);
        
        this.addButton.renderCW(graphics, x, y);
        this.subtractButton.renderCW(graphics, x, y);
    }
    
    private void onAddPress() {
        Coined.LOGGER.info("Add button pressed for " + this.coinType.name());
    }
    
    private void onSubtractPress() {
        Coined.LOGGER.info("Subtract button pressed for " + this.coinType.name());
    }


    private class CoinDisplay extends CustomWidget {
        
        private final CoinType coinType;

        public CoinDisplay(CoinType coinType, int offsetX, int offsetY) {
            super(16, 16, offsetX, offsetY);
            this.coinType = coinType;
        }

        @Override
        public void render(@Nonnull GuiGraphics graphics, int x, int y) {
            graphics.renderFakeItem(new ItemStack(this.coinType.getItem()), x, y);
        }

        @Override
        protected void renderTooltip() {
            long singleValue = this.coinType.getValue(); //get single coin value
            setTooltip(Tooltip.create(CurrencyTxt.valueLabel(singleValue, this.coinType.getNameColor())));
        }
    }

    private class AddButton extends CustomButton {
        public AddButton(int offsetX, int offsetY, OnPress onPress) {
            super(ResourceLocation.fromNamespaceAndPath(Coined.MOD_ID, "textures/gui/purse/add_button.png"), 10, 10, offsetX, offsetY, onPress);
        }

        @Override
        public void renderTooltip() {
            setTooltipDelay(Duration.ofSeconds(2));
            if (Screen.hasShiftDown()) { setTooltip(Tooltip.create(Txt.colored("purse.button.add_ten", Txt.TOOLTIP)));}
            else { setTooltip(Tooltip.create(Txt.colored("purse.button.add_one", Txt.TOOLTIP)));}
        }
    }

    private class SubtractButton extends CustomButton {
        public SubtractButton(int offsetX, int offsetY, OnPress onPress) {
            super(ResourceLocation.fromNamespaceAndPath(Coined.MOD_ID, "textures/gui/purse/subtract_button.png"), 10, 10, offsetX, offsetY, onPress);
        }

        @Override
        public void renderTooltip() {
            setTooltipDelay(Duration.ofSeconds(2));
            if (Screen.hasShiftDown()) { setTooltip(Tooltip.create(Txt.colored("purse.button.subtract_ten", Txt.TOOLTIP)));}
            else { setTooltip(Tooltip.create(Txt.colored("purse.button.subtract_one", Txt.TOOLTIP)));}
        }
    }
}
