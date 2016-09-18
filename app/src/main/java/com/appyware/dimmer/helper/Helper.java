package com.appyware.dimmer.helper;

import android.annotation.SuppressLint;
import android.graphics.Color;

/**
 * Created by
 * --Vatsal Bajpai on
 * --18/09/16 at
 * --7:52 AM
 */
public class Helper {

    /**
     * converts int value to color
     *
     * @param value The integer value of service layer
     * @return The parsed color from input int
     */
    public static int getColorInt(int value) {
        return Color.parseColor("#" + doublePrecision(value) + "000000");
    }

    /**
     * coverts int to string with precision to two places
     *
     * @param time The input int
     * @return The string value
     */
    @SuppressLint("DefaultLocale")
    public static String doublePrecision(int time) {
        return String.format("%02d", time);
    }

}
