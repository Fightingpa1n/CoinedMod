package net.fightingpainter.mc.coined.currency;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import net.fightingpainter.mc.coined.Coined;
import net.fightingpainter.mc.coined.util.Txt;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

/**
 * an Extension of my Txt Util Class but this one specifically contains  
*/
public class CurrencyText {
    //============================== Colors ==============================\\
    public static final int TOTAL = 0xbdc9bf;
    public static final int COPPER = CoinType.COPPER.getNameColor();
    public static final int SILVER = CoinType.SILVER.getNameColor();
    public static final int GOLD = CoinType.GOLD.getNameColor();
    public static final int PLATINUM = CoinType.PLATINUM.getNameColor();

    private static final String currency_symbol = "G"; //TODO: make this Configurable



    public static String currencyValue(long value) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('\''); //Set the custom grouping separator
        DecimalFormat formatter = new DecimalFormat("#,###", symbols); // Create a new DecimalFormat with the custom symbols
        String text = formatter.format(value);
        return text;
    }

    public static Component Label(String key, long value, int labelColor) {
        Component labelComponent = Component.translatable(Coined.MOD_ID+".label."+key, currencyValue(value)+currency_symbol);
        return Txt.colored(labelComponent, labelColor);
    }

    public static Component Label(String key, long value, int valueColor, int labelColor) {
        Component valueComponent = Txt.colored(currencyValue(value)+currency_symbol, valueColor);
        Component labelComponent = Component.translatable(Coined.MOD_ID+".label."+key, valueComponent).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(labelColor)));
        return labelComponent;
    }

    public static Component Label(String key, long value, int valueColor, int labelColor, int currencySymbolColor) {
        Component valueComponent = Txt.concat(Txt.colored(currencyValue(value), valueColor), Txt.colored(currency_symbol, currencySymbolColor));
        Component labelComponent = Component.translatable(Coined.MOD_ID+".label."+key, valueComponent).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(labelColor)));
        return labelComponent;
    }

    /**
     * Formats a long value by adding a custom grouping separator or something like that
     * @param value The value to format
     * @return The formatted value as a string
    */
    public static String formatValue(long value) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('\''); //set the separator
        DecimalFormat formatter = new DecimalFormat("#,###", symbols); //create the formatter
        String text = formatter.format(value); //format the value
        return text; //return the formatted value
    }

    //============================== Colored Text ==============================\\
    /**
     * Colors a Given Text to the Total Color (more easy then having to use Txt.colored and setting the color manually) 
     * @param text The text
     * @return The colored text
    */
    public static Component totalTxt(Object text) {
        return Txt.colored(text, TOTAL);
    }

    /**
     * Colors a Given Text to the Copper Color (more easy then having to use Txt.colored and setting the color manually) 
     * @param text The text
     * @return The colored text
    */
    public static Component copperTxt(Object text) {
        return Txt.colored(text, COPPER);
    }

    /**
     * Colors a Given Text to the Silver Color (more easy then having to use Txt.colored and setting the color manually) 
     * @param text The text
     * @return The colored text
    */
    public static Component silverTxt(Object text) {
        return Txt.colored(text, SILVER);
    }

    /**
     * Colors a Given Text to the Gold Color (more easy then having to use Txt.colored and setting the color manually) 
     * @param text The text
     * @return The colored text
    */
    public static Component goldTxt(Object text) {
        return Txt.colored(text, GOLD);
    }

    /**
     * Colors a Given Text to the Platinum Color (more easy then having to use Txt.colored and setting the color manually) 
     * @param text The text
     * @return The colored text
    */
    public static Component platinumTxt(Object text) {
        return Txt.colored(text, PLATINUM);
    }

    //============================== Labels ==============================\\
    /**
     * Creates a Label Component with the given key and value (for Manual Label Creation)
     * @param key The key of the label
     * @param value The value of the label
     * @param labelColor The color of the label
     * @return The created Label Component
    */
    public static Component label(String key, long value, int labelColor) {
        key = Coined.MOD_ID+".label."+key; //get the key
        String valueText = formatValue(value)+currency_symbol; //format the value
        Component labelComponent = Component.translatable(key, valueText); //create the label component
        return Txt.colored(labelComponent, labelColor); //return the colored label component
    }

    /**
     * Creates a Value Label from a value and a color
     * @param value The value
     * @param labelColor The color of the label (since Value may be used in different contexts the color is not hardcoded)
     * @return The created Value Label Component
    */
    public static Component valueLabel(long value, int labelColor) {
        return Label("value", value, labelColor);
    }

    /**
     * Creates a Total Label from a value
     * @param value The value
     * @return The created Total Label Component
    */
    public static Component totalLabel(long value) {
        return label("total", value, TOTAL);
    }

    /**
     * Creates a Copper Label from a value
     * @param value The value
     * @return The created Copper Label Component
    */
    public static Component copperLabel(long value) {
        return label("copper", value, COPPER);
    }

    /**
     * Creates a Silver Label from a value
     * @param value The value
     * @return The created Silver Label Component
    */



    










    
}
