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








    
}
