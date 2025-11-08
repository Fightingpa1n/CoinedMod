package net.fightingpainter.mc.coined.gui.purse;

import javax.annotation.Nonnull;

import net.fightingpainter.mc.coined.Coined;
import net.fightingpainter.mc.coined.gui.currency.BalanceManager;
import net.fightingpainter.mc.coined.gui.currency.CurrencyTxt;
import net.fightingpainter.mc.coined.util.RenderHelper;
import net.fightingpainter.mc.coined.util.Txt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.ScreenEvent;

public class PurseWindow extends AbstractWidget {

        
    private static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(Coined.MOD_ID, "textures/gui/purse/bg.png");
    private static final int WINDOW_WIDTH = 80;
    private static final int WINDOW_HEIGHT = 96;
    
    // Window position Info
    private int windowPosX = 0;
    private int windowPosY = 0;
    
    private EditBox withdrawField;
    private long withdrawAmount = 0L;
    
    private Button depositButton;

    public PurseWindow() {
        super(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, CommonComponents.EMPTY);
        this.createDepositButton();
    }

    private void createDepositButton() {
        Component buttonText = Txt.trans("purse.button.deposit_all");
        Component tooltipText = Txt.colored(Txt.trans("purse.button.deposit_all"), Txt.TOOLTIP);
        
        this.depositButton = Button.builder(
            buttonText,
            button -> onDepositAll()
        )
        .bounds(50, 3, 12, 8)  // x, y, width, height (relative to window)
        .tooltip(Tooltip.create(tooltipText))
        .build();
    }


    public void init(ScreenEvent.Init event) {
        // Update button position based on window position
        this.depositButton.setX(windowPosX + 50);
        this.depositButton.setY(windowPosY + 3);
        
        // Add the deposit button to the screen's listeners
        event.addListener(depositButton);

        // --- Text input (EditBox) ---
        int x = windowPosX + 5;
        int y = windowPosY + 88;          // inside your 96px high window
        int w = 70;
        int h = 14;

        withdrawField = new EditBox(
            Minecraft.getInstance().font,
            x, y, w, h,
            Component.translatable("purse.withdraw.input")
        );

        // Optional hint/placeholder (method name differs by MC version; try setHint first, else setSuggestion)
        try { withdrawField.setHint(Component.literal("Amount")); }
        catch (Throwable t) { withdrawField.setSuggestion("Amount"); }

        // Limit & numeric-only filter
        withdrawField.setMaxLength(18);
        withdrawField.setFilter(s -> s.matches("\\d*"));

        // Keep your long in sync when the text changes
        withdrawField.setResponder(s -> {
            if (s == null || s.isEmpty()) { withdrawAmount = 0L; return; }
            try {
                withdrawAmount = Long.parseLong(s);
            } catch (NumberFormatException e) {
                withdrawAmount = 0L;
            }
        });

        // Initial value (optional)
        withdrawField.setValue("");

        // Register with the screen so it receives focus, typing, mouse, etc.
        event.addListener(withdrawField);
    }

    public void onOpen() {
        this.visible = true;
        this.active = true;
    }

    public void onClose() {
        this.visible = false;
        this.active = false;
    }
    


    public void render(ScreenEvent.Render.Post event) { //render the GUI (each frame I think)
        GuiGraphics graphics = event.getGuiGraphics();

        // Update widget positions if they have changed
        if (withdrawField != null) {
            withdrawField.setX(windowPosX + 5);
            withdrawField.setY(windowPosY + 88);
        }
        
        if (depositButton != null) {
            depositButton.setX(windowPosX + 50);
            depositButton.setY(windowPosY + 3);
        }

        //let's try to add elements to make an ingame mockup
        graphics.drawString(Minecraft.getInstance().font, Txt.trans("purse.title"), windowPosX + 5, windowPosY + 5, 0xFFFFFF);
        
        long balance = BalanceManager.getPlayerBalance(Minecraft.getInstance().player);

        graphics.drawString(Minecraft.getInstance().font, CurrencyTxt.totalLabel(balance), windowPosX + 5, windowPosY + 20, 0xFFFFFF);

        // Note: The depositButton and withdrawField are now handled by the Minecraft GUI system
        // They will render themselves automatically since they're registered as listeners
    }

    public boolean isDragArea(double mouseX, double mouseY) { //since I can't just return the dragArea as a whole this is a function instead that will check that directly (Defining the drag area in here)
        int dragAreaStartX = windowPosX; //top left corner of the window
        int dragAreaStartY = windowPosY; //top left corner of the window
        int dragAreaEndX = windowPosX + WINDOW_WIDTH; //the entire width of the window
        int dragAreaEndY = windowPosY + 20; //Only the top 20 pixels are draggable

        boolean mouseInDragX = mouseX >= dragAreaStartX && mouseX <= dragAreaEndX; //check if the mouse is within the X bounds of the drag area
        boolean mouseInDragY = mouseY >= dragAreaStartY && mouseY <= dragAreaEndY; //check if the mouse is within the Y bounds of the drag area

        if (depositButton != null && depositButton.isHoveredOrFocused()) {return false;} //don't allow dragging if the mouse is over the deposit button

        return mouseInDragX && mouseInDragY; //return if the mouse is within the bounds of the drag area
    }


    /** returns the X postion of the Purse Window */
    public int getPosX() {return windowPosX;}
    /** returns the Y postion of the Purse Window */
    public int getPosY() {return windowPosY;}

    /** returns the X size of the Purse Window */
    public int getSizeX() {return WINDOW_WIDTH;}
    /** returns the Y size of the Purse Window */
    public int getSizeY() {return WINDOW_HEIGHT;}

    /** 
     * sets the X and Y postion of the Purse Window
     * @param x the X postion
     * @param y the Y postion
    */
    public void setPos(int x, int y) {
        windowPosX = x;
        windowPosY = y;
        
        // Update positions of child widgets
        if (depositButton != null) {
            depositButton.setX(windowPosX + 50);
            depositButton.setY(windowPosY + 3);
        }
        
        if (withdrawField != null) {
            withdrawField.setX(windowPosX + 5);
            withdrawField.setY(windowPosY + 88);
        }
    }

    private void onDepositAll() {
        //TODO: send a packet to the server to deposit all currency items in the players inventory to their purse
        Coined.LOGGER.info("Deposit All clicked");
    }


    @Override
    protected void renderWidget(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (!this.visible) return; //do not render if not visible

        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, 300); // Move to a higher Z-level (300 is above most elements)

        //Render the window background
        RenderHelper.renderTexture(graphics, BACKGROUND_TEXTURE, WINDOW_WIDTH, WINDOW_HEIGHT, windowPosX, windowPosY);

        RenderHelper.renderText(graphics, Txt.trans("purse.title"), 0xFFFFFF, false, windowPosX + 5, windowPosY + 5);
        
        graphics.pose().popPose();
    }

    //======================================== Disabled Methods ========================================\\
    @Override
    protected void updateWidgetNarration(@Nonnull NarrationElementOutput narrationElementOutput) {} 
}
