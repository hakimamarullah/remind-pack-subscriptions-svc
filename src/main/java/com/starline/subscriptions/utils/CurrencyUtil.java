package com.starline.subscriptions.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class CurrencyUtil {

    private CurrencyUtil() {}

    public static String formatIDR(double amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');  // Use dot as thousands separator
        symbols.setDecimalSeparator(',');   // Optional: change decimal separator

        DecimalFormat df = new DecimalFormat("#,##0", symbols); // No decimals
        return "IDR " + df.format(amount);
    }
}
