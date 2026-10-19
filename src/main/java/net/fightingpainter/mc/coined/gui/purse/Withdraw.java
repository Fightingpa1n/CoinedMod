package net.fightingpainter.mc.coined.gui.purse;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import net.fightingpainter.mc.coined.Coined;
import net.fightingpainter.mc.coined.gui.currency.CoinType;
import net.fightingpainter.mc.coined.util.Money;
import net.fightingpainter.mc.coined.util.RenderHelper;
import net.fightingpainter.mc.coined.util.Txt;
import net.fightingpainter.mc.coined.util.widgets.CustomButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.ScreenEvent;

public class Withdraw extends AbstractWidget {

    private static final int WITHDRAW_BUTTON_OFFSET_X = 68;
    private static final int WITHDRAW_BUTTON_OFFSET_Y = 0;
    private final Button withdrawButton;
    
    private static final int WITHDRAW_FIELD_OFFSET_X = 0;
    private static final int WITHDRAW_FIELD_OFFSET_Y = 0;
    private final WithdrawField withdrawField = new WithdrawField();
    
    public Withdraw(Consumer<Void> onWithdraw) {
        super(0, 0, 0, 0, Txt.empty());
        this.active = false;

        this.withdrawButton = new WithdrawButton(this, button -> onWithdraw.accept(null));
        this.toggleVisibility(false); //start hidden
    }

    public void init(ScreenEvent.Init event) {
        event.addListener(this); //register this CoinAmount as a listener to the screen events
        event.addListener(this.withdrawButton);
        event.addListener(this.withdrawField);
    }

    public void toggleVisibility(boolean visible) {
        this.visible = visible;
        this.withdrawButton.visible = visible;
        this.withdrawButton.active = visible;
        this.withdrawField.visible = visible;
        this.withdrawField.active = visible;
        this.withdrawField.setEditable(visible);
    }

    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y); //set the position of the widget
        
        withdrawButton.setPosition(this.getX() + WITHDRAW_BUTTON_OFFSET_X, this.getY() + WITHDRAW_BUTTON_OFFSET_Y);
        withdrawField.setPosition(this.getX() + WITHDRAW_FIELD_OFFSET_X, this.getY() + WITHDRAW_FIELD_OFFSET_Y);
    }

    public long getValue() {
        String valueStr = this.withdrawField.getValue();
        if (valueStr.isEmpty()) {
            return 0L;
        }
        try {
            return Long.parseLong(valueStr);
        } catch (NumberFormatException e) {
            return 0L; //default to 0 on parse error
        }
    }

    public void clearField() {
        this.withdrawField.setValue("");
    }

    public void setFieldValue(long amount) {
        this.withdrawField.setValue(Long.toString(amount));
    }

    public void editFieldValue(long amount) {
        long currentValue = this.getValue();
        this.withdrawField.setValue(Long.toString(currentValue + amount));
    }

    private static class WithdrawField extends EditBox {

        public WithdrawField() {
            super(RenderHelper.getFont(), 0, 0, 67, 12, Txt.empty());

            this.setHint(Txt.colored("Amount", Txt.TOOLTIP2));

            //Limit & numeric-only filter
            this.setMaxLength(18);
            this.setFilter(s -> s.matches("\\d*"));
            
            //Initial value (optional)
            this.setValue("");
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if (keyCode == 257) { //check for Enter key (257 is the keycode for Enter)
                this.setFocused(false); //remove focus
                return true; //consume the event
            }
            
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
        
        @Override
        public void renderWidget(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            graphics.pose().pushPose();
            graphics.pose().translate(0, 0, 301); //Move to a higher Z-level (300 is above most elements)
            super.renderWidget(graphics, mouseX, mouseY, partialTick); //call the parent render method
            graphics.pose().popPose();
        }
    }

    private static class WithdrawButton extends CustomButton {
        private static final ResourceLocation BUTTON_TEXTURE = ResourceLocation.fromNamespaceAndPath(Coined.MOD_ID, "textures/gui/purse/withdraw_button.png");
        private static final int BUTTON_WIDTH = 12;
        private static final int BUTTON_HEIGHT = 12;
        private final Withdraw parent;
        
        public WithdrawButton(Withdraw parent, OnPress onPress) {
            super(BUTTON_TEXTURE, BUTTON_WIDTH, BUTTON_HEIGHT, onPress);
            this.parent = parent;
        }

        @Override
        protected void renderTooltip() {
            long withdrawValue = parent.getValue();
            Money withdrawMoney = Money.fromValue(withdrawValue);
            
            if (withdrawValue > 0) {
                // Create tooltip with coin breakdown
                Component tooltip = Txt.trans("purse.button.withdraw");
                
                // Add coin amounts if they exist
                List<Component> coinLines = new ArrayList<>();
                if (withdrawMoney.hasPlatinum()) {
                    coinLines.add(CoinType.PLATINUM.createLabel(withdrawMoney.getPlatinumAmount()));
                }
                if (withdrawMoney.hasGold()) {
                    coinLines.add(CoinType.GOLD.createLabel(withdrawMoney.getGoldAmount()));
                }
                if (withdrawMoney.hasSilver()) {
                    coinLines.add(CoinType.SILVER.createLabel(withdrawMoney.getSilverAmount()));
                }
                if (withdrawMoney.hasCopper()) {
                    coinLines.add(CoinType.COPPER.createLabel(withdrawMoney.getCopperAmount()));
                }
                
                if (!coinLines.isEmpty()) {
                    // Combine all components into a multiline tooltip
                    Component finalTooltip = tooltip;
                    for (Component coinLine : coinLines) {
                        finalTooltip = Txt.concat(finalTooltip, "\n", coinLine);
                    }
                    setTooltip(Tooltip.create(finalTooltip));
                } else {
                    setTooltip(Tooltip.create(tooltip));
                }
            } else {
                setTooltip(Tooltip.create(Txt.trans("purse.button.withdraw")));
            }
        }
        
        @Override
        public void renderWidget(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            graphics.pose().pushPose();
            graphics.pose().translate(0, 0, 301); // Move to a higher Z-level (300 is above most elements)
            super.renderWidget(graphics, mouseX, mouseY, partialTick); //call the parent render method
            graphics.pose().popPose();
        }
    }

    @Override
    protected void renderWidget(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {} //no rendering needed

    @Override
    protected void updateWidgetNarration(@Nonnull NarrationElementOutput narrationElementOutput) {} //No narration for now
}
