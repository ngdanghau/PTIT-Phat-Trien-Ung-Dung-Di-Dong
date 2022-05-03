package com.example.prudentialfinance.Helpers;

import java.text.DecimalFormat;
import java.text.ParseException;

public class Helper {

    public static String convertStringToDate(String input) throws ParseException {
        String output = "01/05";

        String day = input.substring(8,10);
        String month = input.substring(5,7);

        output = day + "/" + month;
        return output;
    }

    public static String formatNumber(int input)
    {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(input);
    }

    public static String formatDoubleNumber(Double input)
    {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(input);
    }
}
