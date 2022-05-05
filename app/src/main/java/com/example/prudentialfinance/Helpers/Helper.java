package com.example.prudentialfinance.Helpers;

import android.graphics.Color;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Transformation;

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

    /*
    * 123456 -> 123,456
    * */
    public static String formatIntegerNumber(int input)
    {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(input);
    }

    public static String formatDoubleNumber(Double input)
    {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(input);
    }

    public static String formatCardNumber(String input)
    {
        if( input.length() <= 4)
            return input;

        int first = (input.length() - 1) % 4 + 1;
        StringBuilder ouput = new StringBuilder(input.substring(0, first));

        for( int i = first ; i <input.length() ;i+=4)
        {
            String fourNumber = input.substring(i, i+4);
            ouput.append(' ').append( fourNumber );
        }

        return ouput.toString();
    }

    public static Transformation getRoundedTransformationBuilder()
    {
        return new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(3)
                .cornerRadiusDp(50)
                .oval(false)
                .build();
    }
}
