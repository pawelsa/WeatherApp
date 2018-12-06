package com.example.pawel.weatherapp.project;

import android.graphics.Color;

import androidx.annotation.ColorInt;

public class ColorManager {

	@ColorInt
	public static int getContrastColor(@ColorInt int color) {
		// Counting the perceptive luminance - human eye favors green color...
		double a = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;

		int d;
		if (a < 0.5) {
			d = 0; // bright colors - black font
		} else {
			d = 255; // dark colors - white font
		}

		return Color.rgb(d, d, d);
	}

	@ColorInt
	public static int getColorFromCityID(@ColorInt int color) {
		float hsv[] = new float[3];
		Color.colorToHSV(color, hsv);
		return Color.HSVToColor(hsv);
	}
}
