package edu.auburn.eng.csse.comp3710.team8;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by Patrick on 4/22/2015.
 */
public class ImageProcessor {
    public static int getColorInt(Bitmap bmp) {
        Bitmap onePixelBmp = Bitmap.createScaledBitmap(bmp, 1, 1, true);
        int pixel = onePixelBmp.getPixel(0,0);
        int red = Color.red(pixel);
        int blue = Color.blue(pixel);
        int green = Color.green(pixel);

        int color = (red << 16) | (green << 8) | (blue);

        return color;
    }

    public static Color getColorObject(Bitmap bmp) {
        return null;
    }
}
