package edu.auburn.eng.csse.comp3710.team8;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Simple class for processing images provided by the Camera or Photo Library.
 * Compresses the provided bitmap to a single pixel, then reads the color
 * out of that pixel.
 * It abuses the createScaledBitmap algorithm's functionality of creating the
 * "dominant" color.
 */
public class ImageProcessor {
    public static int getColorInt(Bitmap bmp) {
        Bitmap onePixelBmp = Bitmap.createScaledBitmap(bmp, 1, 1, true);
        int pixel = onePixelBmp.getPixel(0,0);
        int red = Color.red(pixel);
        int blue = Color.blue(pixel);
        int green = Color.green(pixel);

        int color = (0xFF << 24) | (red << 16) | (green << 8) | (blue);
        color += (0x000F0F0F); // DARKEN THE COLOR SLIGHTLY. EXPERIMENTAL
        return color;
    }

    public static Color getColorObject(Bitmap bmp) {
        return null;
    }
}
