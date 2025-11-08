package net.fightingpainter.mc.coined.util;

import net.fightingpainter.mc.coined.Coined;
import net.fightingpainter.mc.coined.gui.currency.CoinType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;


/**
 * an Util Class I made to help with Text Components as using them is a bit of a pain,
 * especially when you want to do complexer stuff than just text.
*/
public class Txt {
    public static final int DEFAULT = 0xffffff;
    public static final int TOOLTIP = 0xaaaaaa;
    public static final int TOOLTIP2 = 0x555555;
    public static final int HINT = 0x5555ff;

    public static final int GREEN = 0x55ff55;

    public static final int TOTAL = 0xbdc9bf;
    public static final int COPPER = CoinType.COPPER.getNameColor();
    public static final int SILVER = CoinType.SILVER.getNameColor();
    public static final int GOLD = CoinType.GOLD.getNameColor();
    public static final int PLATINUM = CoinType.PLATINUM.getNameColor();

    /**
    * Creates a new text component from the given text. (in NeoForge They are Components not Text just fyi)
    * @param text The text to create the component from (can be a string, a component, or any object)
    * @return The given text as a component
    */
    public static Component text(Object text) {
        Component resultText;
        if (text instanceof Component) {resultText = (Component)text;}
        else if (text instanceof String) {resultText = Component.literal((String)text);}
        else {resultText = Component.literal(String.valueOf(text));}

        return resultText;
    }


    public static Component concat(Object... texts) {
        Component resultText = Component.literal("");
        for (Object text : texts) {
            resultText = resultText.copy().append(text(text));
        }
        return resultText;
    }

    public static Component trans(String key) {
        return Component.translatable(Coined.MOD_ID+"."+key);
    }

    public static Component colored(Object text, int color) {
        return text(text).copy().setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color)));
    }
    
    /** Returns an empty text component. */
    public static Component empty() {return Component.empty();}
}
