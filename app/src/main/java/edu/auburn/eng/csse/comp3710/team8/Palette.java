package edu.auburn.eng.csse.comp3710.team8;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by patrickstewart on 4/22/15.
 */
public class Palette {

    public static String RANDOM = "RANDOM";
    public static String COMPLEMENTARY = "COMPLEMENTARY";

    protected static int COUNTER = 0; // Used for numbering palettes for default names
    protected static final String DEFAULT_NAME = "Palette #";

    protected String name;
    protected static final int numColors = 3;
    // Colors are only int values of the format (red << 16) | (green << 8) | (blue)
    protected int[] colors; // Array implementation supports arbitrary number of colors

    protected Palette(int primary, String algorithm) {
        name = DEFAULT_NAME + COUNTER++;
        colors = new int[numColors];
        colors[0] = primary;
        // Generate additional colors!
        for (int i = 1; i < numColors; i++) {
            // Calculate colors here!
            colors[i] = generateColor(this.colors, algorithm);
        }
    }

    /*
    protected Palette(int primary, int secondary) {
        //this.primary = primary;
        //this.secondary = secondary;
        name = DEFAULT_NAME + COUNTER++;
        colors[0] = primary;
        colors[1] = secondary;
        // Generate additional colors!
        for (int i = 2; i < numColors; i++) {
            int tempColor = 0;
            // Calculate colors here!
            tempColor = generateColor(colors, RANDOM);
            colors[i] = tempColor;
        }
    }

    protected Palette(int primary, int secondary, int tertiary) {
        //this.primary = primary;
        //this.secondary = secondary;
        //this.tertiary = tertiary;
        name = DEFAULT_NAME + COUNTER++;
        colors[0] = primary;
        colors[1] = secondary;
        colors[2] = tertiary;
        // Generate additional colors!
        for (int i = 3; i < numColors; i++) {
            int tempColor = 0;
            // Calculate colors here!
            tempColor = generateColor(colors, RANDOM);
            colors[i] = tempColor;
        }
    }
    */
    public int[] getColors() {
        return colors;
    }

    public void setName(String nameIn) {
        name = nameIn;
    }

    public String getName() {
        return name;
    }

    public Bitmap render(int screenWidth, int screenHeight) {
        final int RENDER_WIDTH = screenWidth/2;
        final int RENDER_HEIGHT = screenHeight/7;
        final int RENDER_RADIUS = RENDER_HEIGHT/(numColors+1);
        // Return an image of the palette!
        Bitmap bmp = (Bitmap.createBitmap(RENDER_WIDTH, RENDER_HEIGHT, Bitmap.Config.ARGB_8888));
        Canvas canvas = new Canvas(bmp);
        int x = canvas.getWidth();
        int y = canvas.getHeight();
        Log.i("render()", "(x,y) = (" + x + "," + y + ")");
        Paint paint = new Paint();
        //paint.setStyle(Paint.Style.FILL);
        // Background color!
        //paint.setColor(Color.WHITE);
        //canvas.drawPaint(paint);
        // Draw circles!
        for (int i = 0; i < numColors; i++) {
            Log.i("render()", "Draw circle " + i);
            paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(colors[i]);
            Log.i("render()", "Draw circle " + i + " Color: " + colors[i]);
            canvas.drawCircle(((i+1)*x)/(numColors+1), y/2, RENDER_RADIUS, paint);
        }
        Log.i("render()", "render() called!");
        return bmp;
    }

    public static int generateColor(int[] colorsIn, String algorithm) {
        if (algorithm.equals(COMPLEMENTARY)) {
            return Color.RED;
        }
        else if (algorithm.equals(RANDOM)) {
            return Color.GREEN;
        }
        else return 0;
    }

}
