package net.fightingpainter.mc.coined.gui.purse;

import javax.annotation.Nonnull;

import net.fightingpainter.mc.coined.Coined;
import net.fightingpainter.mc.coined.gui.currency.BalanceManager;
import net.fightingpainter.mc.coined.gui.currency.CoinType;
import net.fightingpainter.mc.coined.gui.currency.CurrencyTxt;
import net.fightingpainter.mc.coined.util.RenderHelper;
import net.fightingpainter.mc.coined.util.Txt;
import net.fightingpainter.mc.coined.util.widgets.CustomButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.ScreenEvent;

public class PurseWindow extends AbstractWidget {
    private static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(Coined.MOD_ID, "textures/gui/purse/bg.png");
    private static final int WINDOW_WIDTH = 90;
    private static final int WINDOW_HEIGHT = 131;
    private static final int DRAG_AREA_HEIGHT = 15; //height of the draggable area at the top of the purse window

    private static final int TITLE_OFFSET_X = 5;
    private static final int TITLE_OFFSET_Y = 5;
    private static final int TITLE_WIDTH = 67; //max width of the title text

    private static final int TOTAL_OFFSET_X = 5;
    private static final int TOTAL_OFFSET_Y = 18;
    private static final int TOTAL_WIDTH = 80; //max width of the total text
    
    private static final int DEPOSIT_BUTTON_OFFSET_X = 73;
    private static final int DEPOSIT_BUTTON_OFFSET_Y = 5;
    private Button depositButton = new DepositButton(button -> onDepositAll());

    private static final int WITHDRAW_OFFSET_X = 5;
    private static final int WITHDRAW_OFFSET_Y = 114;
    private final Withdraw withdraw = new Withdraw(button -> onWithdraw());
    
    private static final int COPPER_AMOUNT_OFFSET_X = 5;
    private static final int COPPER_AMOUNT_OFFSET_Y = 31;
    private final CoinAmount copperAmount = new CoinAmount(CoinType.COPPER, this::onWithdrawCoinChange);
    
    private static final int SILVER_AMOUNT_OFFSET_X = 5;
    private static final int SILVER_AMOUNT_OFFSET_Y = 51;
    private final CoinAmount silverAmount = new CoinAmount(CoinType.SILVER, this::onWithdrawCoinChange);
    
    private static final int GOLD_AMOUNT_OFFSET_X = 5;
    private static final int GOLD_AMOUNT_OFFSET_Y = 71;
    private final CoinAmount goldAmount = new CoinAmount(CoinType.GOLD, this::onWithdrawCoinChange);
    
    private static final int PLATINUM_AMOUNT_OFFSET_X = 5;
    private static final int PLATINUM_AMOUNT_OFFSET_Y = 91;
    private final CoinAmount platinumAmount = new CoinAmount(CoinType.PLATINUM, this::onWithdrawCoinChange);
    private long withdrawAmount = 0L;
    
    public PurseWindow() {
        super(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, CommonComponents.EMPTY);
        this.active = false;
        this.togglePurse(false); //start closed
    }

    public void init(ScreenEvent.Init event) {
        event.addListener(this); //register purse window listener

        event.addListener(this.depositButton);

        this.withdraw.init(event);

        this.copperAmount.init(event);
        this.silverAmount.init(event);
        this.goldAmount.init(event);
        this.platinumAmount.init(event);

        setPosition(this.getX(), this.getY()); //set initial position of the Purse Window
    }

    public void togglePurse(boolean open) {
        this.visible = open;

        this.depositButton.visible = open;
        this.depositButton.active = open;

        this.withdraw.toggleVisibility(open);

        this.copperAmount.toggleVisibility(open);
        this.silverAmount.toggleVisibility(open);
        this.goldAmount.toggleVisibility(open);
        this.platinumAmount.toggleVisibility(open);
    }

    
    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y); //set the position of the widget
        
        depositButton.setPosition(this.getX() + DEPOSIT_BUTTON_OFFSET_X, this.getY() + DEPOSIT_BUTTON_OFFSET_Y);

        withdraw.setPosition(this.getX() + WITHDRAW_OFFSET_X, this.getY() + WITHDRAW_OFFSET_Y);
        
        this.copperAmount.setPosition(this.getX() + COPPER_AMOUNT_OFFSET_X, this.getY() + COPPER_AMOUNT_OFFSET_Y);
        this.silverAmount.setPosition(this.getX() + SILVER_AMOUNT_OFFSET_X, this.getY() + SILVER_AMOUNT_OFFSET_Y);
        this.goldAmount.setPosition(this.getX() + GOLD_AMOUNT_OFFSET_X, this.getY() + GOLD_AMOUNT_OFFSET_Y);
        this.platinumAmount.setPosition(this.getX() + PLATINUM_AMOUNT_OFFSET_X, this.getY() + PLATINUM_AMOUNT_OFFSET_Y);
    }
    
    
    @Override
    protected void renderWidget(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (!this.visible) return; //do not render if not visible
        
        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, 300); // Move to a higher Z-level (300 is above most elements)
        
        RenderHelper.renderTexture(graphics, BACKGROUND_TEXTURE, WINDOW_WIDTH, WINDOW_HEIGHT, this.getX(), this.getY()); //render the background
        
        RenderHelper.renderText(graphics, Txt.trans("purse.title"), Txt.DEFAULT, true, this.getX() + TITLE_OFFSET_X, this.getY() + TITLE_OFFSET_Y); //render the title text

        long total = BalanceManager.getPlayerBalance(BalanceManager.getCurrentPlayer()); //get total balance
        RenderHelper.renderText(graphics, CurrencyTxt.totalLabel(total), CurrencyTxt.TOTAL, true, this.getX() + TOTAL_OFFSET_X, this.getY() + TOTAL_OFFSET_Y); //render the total balance text
        
        graphics.pose().popPose();
    }

    public boolean isDragArea(double mouseX, double mouseY) { //checks if mouse is inside the draggable area of the purse window
        int dragAreaStartX = this.getX(); //top left corner of the window
        int dragAreaStartY = this.getY(); //top left corner of the window
        int dragAreaEndX = this.getX() + WINDOW_WIDTH; //the entire width of the window
        int dragAreaEndY = this.getY() + DRAG_AREA_HEIGHT; //only the top pixels defined by DRAG_AREA_HEIGHT are draggable
        boolean mouseInDragX = mouseX >= dragAreaStartX && mouseX <= dragAreaEndX; //check if the mouse is within the X bounds of the drag area
        boolean mouseInDragY = mouseY >= dragAreaStartY && mouseY <= dragAreaEndY; //check if the mouse is within the Y bounds of the drag area
        
        boolean mouseOverDepositButton = this.depositButton.isMouseOver(mouseX, mouseY); //check if mouse is over deposit button
        
        return (mouseInDragX && mouseInDragY) && !mouseOverDepositButton; //return if if mouse inside drag area (but not over deposit button)
    }
    
    private void onDepositAll() {
        Coined.LOGGER.info("Deposit All clicked"); //TODO: implement deposit all functionality
    }
    
    private void onWithdraw() {
        //TODO: implement withdraw functionality
        long withdrawValue = this.withdraw.getValue();
        Coined.LOGGER.info("Withdraw clicked with amount: " + withdrawValue);
    }
    
    private void onWithdrawCoinChange(CoinType coinType, boolean increment) {
        Coined.LOGGER.info("Withdraw " + (increment ? "add" : "subtract") + " clicked for " + coinType.name());

        //TODO: adjust withdraw value based on CoinType and increment/decrement also check shift

        if (Screen.hasShiftDown()) {

        }

    }

    //======================================== Disabled Methods ========================================\\
    @Override
    protected void updateWidgetNarration(@Nonnull NarrationElementOutput narrationElementOutput) {}

    //======================================== Custom Classes ========================================\\

    private static class DepositButton extends CustomButton {
        private static final ResourceLocation BUTTON_TEXTURE = ResourceLocation.fromNamespaceAndPath(Coined.MOD_ID, "textures/gui/purse/deposit_button.png");
        private static final int BUTTON_WIDTH = 12;
        private static final int BUTTON_HEIGHT = 8;
        
        public DepositButton(OnPress onPress) {super(BUTTON_TEXTURE, BUTTON_WIDTH, BUTTON_HEIGHT, onPress);}
        
        @Override
        protected void renderTooltip() {
            setTooltip(Tooltip.create(Txt.trans("purse.button.deposit")));
        }
        
        @Override
        public void renderWidget(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            graphics.pose().pushPose();
            graphics.pose().translate(0, 0, 301); // Move to a higher Z-level (300 is above most elements)
            super.renderWidget(graphics, mouseX, mouseY, partialTick); //call the parent render method
            graphics.pose().popPose();
        }
    }
}
