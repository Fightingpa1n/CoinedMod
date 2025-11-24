package net.fightingpainter.mc.coined.gui.purse;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import net.fightingpainter.mc.coined.Coined;
import net.fightingpainter.mc.coined.gui.CustomWidget;
import net.fightingpainter.mc.coined.util.RenderHelper;
import net.fightingpainter.mc.coined.util.Txt;
import net.fightingpainter.mc.coined.util.widgets.CustomButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.ScreenEvent;

public class Withdraw extends AbstractWidget {

    private static final int WITHDRAW_BUTTON_OFFSET_X = 73;
    private static final int WITHDRAW_BUTTON_OFFSET_Y = 0;
    private final Button withdrawButton;
    
    private static final int WITHDRAW_FIELD_OFFSET_X = 0;
    private static final int WITHDRAW_FIELD_OFFSET_Y = 0;
    private final WithdrawField withdrawField = new WithdrawField();
    
    public Withdraw(Consumer<Void> onWithdraw) {
        super(0, 0, 0, 0, Txt.empty());
        this.active = false;

        this.withdrawButton = new WithdrawButton(button -> onWithdraw.accept(null));
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
        
        public WithdrawButton(OnPress onPress) {super(BUTTON_TEXTURE, BUTTON_WIDTH, BUTTON_HEIGHT, onPress);}

        @Override
        protected void renderTooltip() {
            setTooltip(Tooltip.create(Txt.trans("purse.button.withdraw")));
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
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {} //no rendering needed

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {} //No narration for now
}
