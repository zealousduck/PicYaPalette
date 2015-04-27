package edu.auburn.eng.csse.comp3710.team8;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.audiofx.BassBoost;
import android.util.Log;

/**
 * Simple class for processing images provided by the Camera or Photo Library.
 * Compresses the provided bitmap to a single pixel, then reads the color
 * out of that pixel.
 * It abuses the createScaledBitmap algorithm's functionality of creating the
 * "dominant" color.
 */
public class ImageProcessor {
    public static final String V_LOW_LIGHT = "Very Low Light";
    public static final String LOW_LIGHT = "Low Light";
    public static final String NORMAL = "Normal";
    public static final String BRIGHT = "Bright";
    public static final String V_BRIGHT = "Very Bright";

    private static int lightConditions = 0; // Default Normal conditions
    private static int correctionFactor = 0x0B;

    public static int getColorInt(Bitmap bmp, String light) {
        Bitmap onePixelBmp = Bitmap.createScaledBitmap(bmp, 1, 1, true);
        int pixel = onePixelBmp.getPixel(0,0);
        int red = Color.red(pixel);
        int blue = Color.blue(pixel);
        int green = Color.green(pixel);

        setLightConditions(light);
        // Safely adjust colors with respect to Light conditions!
        if ((red + correctionFactor * lightConditions) <= 0xFF) {
            red += correctionFactor * lightConditions;
        } else {
            red = 0xFF;
        }
        if ((green + correctionFactor * lightConditions) <= 0xFF) {
            green += correctionFactor * lightConditions;
        } else {
            green = 0xFF;
        }
        if ((blue + correctionFactor * lightConditions) <= 0xFF) {
            blue += correctionFactor * lightConditions;
        } else {
            blue = 0xFF;
        }

        // Build color!
        int color = (0xFF << 24) | (red << 16) | (green << 8) | (blue);
        return color;
    }

    public static String[] getLightOptions() {
        String[] light = new String[5];
        light[0] = NORMAL;  // Default
        light[1] = LOW_LIGHT;
        light[2] = V_LOW_LIGHT;
        light[3] = BRIGHT;
        light[4] = V_BRIGHT;
        return light;
    }

    public static void setLightConditions(String light) {
        if (light.equals(NORMAL)) {
            lightConditions = (0); // NORMAL = 0
        } else if (light.equals(V_LOW_LIGHT)) {
            lightConditions = (2);
        } else if (light.equals(LOW_LIGHT)) {
            lightConditions = (1);
        } else if (light.equals(BRIGHT)) {
            lightConditions = (-1);
        } else if (light.equals(V_BRIGHT)) {
            lightConditions = (-2);
        } else {
            lightConditions = (0); // NORMAL = 0
        }
        Log.i("ImageProcessor", "lightConditions = " + lightConditions);
    }
}
