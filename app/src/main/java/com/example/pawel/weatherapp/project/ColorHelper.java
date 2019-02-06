package com.example.pawel.weatherapp.project;

import android.graphics.Color;

public class ColorHelper {

    static String TAG = "ColorHelper";
    private static int MAX_TEMP = 40;
    private static int MIN_TEMP = -20;
    private static int TEMP_DIFF = MAX_TEMP - MIN_TEMP;
    private static int HOT_COLOR = Color.parseColor("#FF8000");
    static int[] START_COLOR = colorToBasic(HOT_COLOR);
    private static int COLD_COLOR = Color.parseColor("#165B88");
    static int[] END_COLOR = colorToBasic(COLD_COLOR);

    private static int[] colorToBasic(int color) {

        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return new int[]{alpha, red, green, blue};
    }

    public static int pickHex(float progress) {
        return pickHex(START_COLOR, END_COLOR, progress, 1 - progress);
    }

    public static int pickHex(int[] color1, int[] color2, float weight) {
        return pickHex(color1, color2, weight, 1 - weight);
    }

    public static int pickHex(int[] color1, int[] color2, float weight, float restWeight) {
        return Color.argb(Math.round(color2[0] * weight + color1[0] * restWeight),
                Math.round(color2[1] * weight + color1[1] * restWeight),
                Math.round(color2[2] * weight + color1[2] * restWeight),
                Math.round(color2[3] * weight + color1[3] * restWeight));
    }

    public static int colorForTemperature(float temp) {

        if (temp > MAX_TEMP)
            return HOT_COLOR;
        else if (temp < MIN_TEMP)
            return COLD_COLOR;
        else {
            float tempPercent = 1 - Math.abs(temp / TEMP_DIFF);
            return pickHex(START_COLOR, END_COLOR, tempPercent);
        }
    }

    public static int getThumbColor(int[] tempArray) {
        return getThumbColor(tempArray, 0);
    }

    public static int getThumbColor(int[] tempArray, int progress) {
        return shadeColor(getTempColor(tempArray, progress));
    }

    public static int getTempColor(int[] tempArray) {
        return Color.argb(tempArray[0], tempArray[1], tempArray[2], tempArray[3]);
    }

    public static int getTempColor(int[] tempArray, int progress) {

        if (progress < 25)
            return pickHex(colorToBasic(tempArray[0]), colorToBasic(tempArray[1]), progress / 5f);
        else if (progress < 50)
            return pickHex(colorToBasic(tempArray[1]), colorToBasic(tempArray[2]), progress / 5f);
        else if (progress < 75)
            return pickHex(colorToBasic(tempArray[2]), colorToBasic(tempArray[3]), progress / 5f);
        else
            return pickHex(colorToBasic(tempArray[3]), colorToBasic(tempArray[4]), progress / 5f);
    }

    private static int shadeColor(int color) {
        int[] base = colorToBasic(color);
        for (int i = 1; i < 4; i++) {
            base[i] += base[i] / 3;
        }
        return Color.argb(base[0], base[1], base[2], base[3]);
    }
}
