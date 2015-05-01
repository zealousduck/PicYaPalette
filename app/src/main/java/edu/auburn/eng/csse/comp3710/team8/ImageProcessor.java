package edu.auburn.eng.csse.comp3710.team8;

import android.graphics.Bitmap;
import android.graphics.Color;
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
    protected static final double saturationFactor = 0.20;
    protected static final double valueFactor = 0.30;

    public static int getColorInt(Bitmap bmp, String light) {
        Bitmap onePixelBmp = Bitmap.createScaledBitmap(bmp, 1, 1, true);
        int pixel = onePixelBmp.getPixel(0,0);
        int red = Color.red(pixel);
        int blue = Color.blue(pixel);
        int green = Color.green(pixel);
        float[] hsv = new float[3];
        // Color correction based on light conditions!
        setLightConditions(light);
        Color.RGBToHSV(red,green,blue,hsv);
        hsv[1] *= (1 + saturationFactor * Math.abs(lightConditions));
        hsv[2] *= (1 + valueFactor * lightConditions);
        int color = Color.HSVToColor(hsv);
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
