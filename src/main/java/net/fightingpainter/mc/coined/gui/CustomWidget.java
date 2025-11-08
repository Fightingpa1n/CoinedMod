package net.fightingpainter.mc.coined.gui;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.neoforged.neoforge.client.event.ScreenEvent;

import net.fightingpainter.mc.coined.util.Txt;

public abstract class CustomWidget extends AbstractWidget {

    //CustomWidget is an abstract Widget class that does not render anything by default and cannot be focused.
    //It is intended to be subclassed and have its render method overridden.

    //======================================== Variables ========================================\\
    protected final int width; //width of the widget
    protected final int height; //height of the widget
    protected final int offsetX; //x offset of the widget
    protected final int offsetY; //y offset of the widget
    protected List<CustomWidget> children = new ArrayList<>(); //list of child widgets
    
    //======================================== Constructors ========================================\\
    /**
     * Constructor for a custom widget and position offsets.
     * @param width the width of the widget
     * @param height the height of the widget
     * @param offsetX the x offset of the widget relative to where it is rendered (i.e. the parent widget's position)
     * @param offsetY the y offset of the widget relative to where it is rendered (i.e. the parent widget's position)
    */
    protected CustomWidget(int width, int height, int offsetX, int offsetY) {
        super(offsetX, offsetY, width, height, Txt.empty()); //call the super constructor so it's happy
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    /**
     * Constructor for a custom widget
     * @param width the width of the widget
     * @param height the height of the widget
    */
    protected CustomWidget(int width, int height) {
        this(width, height, 0, 0);
    }


    //======================================== Public Methods ========================================\\
    //==================== Init ====================\\
    /**
     * Initialize the widget by adding it to the event listeners so it gets rendered and can receive input.
     * Also initializes all child widgets. (if it has any)
     * @param event The ScreenEvent.Init event to add the widget to.
    */
    public final void init(ScreenEvent.Init event) {
        event.addListener(this); //add self to the event listeners so it gets rendered and can receive input
        if (!children.isEmpty()) { //if it has children
            for (CustomWidget child : children) {child.init(event);} //init all the children
        }
    }

    //==================== Render ====================\\
    /**
     * Render the custom Widget (ment for parent classes to call this method when wanting to render children)
     * @param graphics The GuiGraphics to render with.
     * @param posX The x position to render at.
     * @param posY The y position to render at.
    */
    public final void renderCW(@Nonnull GuiGraphics graphics, int posX, int posY) { //this method should be called outside of the class in order to render the widget
        posX = posX + offsetX; //apply the offset
        posY = posY + offsetY; //apply the offset
        this.setX(posX); //update the widget's position
        this.setY(posY); //update the widget's position
        render(graphics, posX, posY); //call the abstract render method
        if (this.isHovered()) {renderTooltip();} //render the tooltip if hovered
    }

    public void render(@Nonnull GuiGraphics graphics, int posX, int posY) {} //override this method to define the widget's rendering
    
    //==================== Tooltip ====================\\
    protected void renderTooltip() {} //override this method to set a tooltip for the widget

    //==================== Children ====================\\
    /**
     * Add a child widget to this widget. The child will be initialized when this widget is initialized.
     * @param child The child widget to add.
    */
    protected CustomWidget addChild(CustomWidget child) {this.children.add(child); return child;}
    
    //======================================== Disabled Old Methods ========================================\\
    @Override
    public void renderWidget(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!this.visible) return; //do not render if not visible


    }

    @Override
    public boolean isFocused() {return false;} //disable focus for the widget

    @Override
    protected void updateWidgetNarration(@Nonnull NarrationElementOutput narrationElementOutput) {} //idk what this does exactly but i'll disable it for now as it's required to implement
}
