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
public abstract class Palette {

    public static String RANDOM = "RANDOM";
    public static String COMPLEMENTARY = "COMPLEMENTARY";

    protected static int COUNTER = 0; // Used for numbering palettes for default names
    protected static final String DEFAULT_NAME = "Palette #";

    protected String name;
    protected int numColors = 3;
    // Colors are only int values of the format (red << 16) | (green << 8) | (blue)
    protected int[] colors; // Array implementation supports arbitrary number of colors

    protected Palette(int primary) {
        //this.primary = base;
        this.name = DEFAULT_NAME + COUNTER++;
        this.colors = new int[numColors];
        this.colors[0] = primary;
        // Generate additional colors!
        for (int i = 1; i < numColors; i++) {
            int tempColor = 0;
            // Calculate colors here!
            tempColor = generateColor(this.colors);
            this.colors[i] = tempColor;
        }
    }

    protected Palette(int primary, int secondary) {
        //this.primary = primary;
        //this.secondary = secondary;
        this.name = DEFAULT_NAME + COUNTER++;
        this.colors[0] = primary;
        this.colors[1] = secondary;
        // Generate additional colors!
        for (int i = 2; i < numColors; i++) {
            int tempColor = 0;
            // Calculate colors here!
            tempColor = generateColor(this.colors);
            this.colors[i] = tempColor;
        }
    }

    protected Palette(int primary, int secondary, int tertiary) {
        //this.primary = primary;
        //this.secondary = secondary;
        //this.tertiary = tertiary;
        this.name = DEFAULT_NAME + COUNTER++;
        this.colors[0] = primary;
        this.colors[1] = secondary;
        this.colors[2] = tertiary;
        // Generate additional colors!
        for (int i = 3; i < numColors; i++) {
            int tempColor = 0;
            // Calculate colors here!
            tempColor = generateColor(this.colors);
            this.colors[i] = tempColor;
        }
    }

    public int[] getColors() {
        return this.colors;
    }

    public void setName(String nameIn) {
        this.name = nameIn;
    }

    public String getName() {
        return name;
    }

    public Bitmap render(int screenWidth, int screenHeight) {
        final int RENDER_WIDTH = screenWidth/2;
        final int RENDER_HEIGHT = screenHeight/7;
        final int RENDER_RADIUS = (screenWidth/2)/numColors;
        // Return an image of the palette!
        Bitmap bmp = (Bitmap.createBitmap(RENDER_WIDTH, RENDER_HEIGHT, Bitmap.Config.ARGB_8888));
        Canvas canvas = new Canvas(bmp);
        int x = canvas.getWidth();
        int y = canvas.getHeight();
        Log.i("render()", "(x,y) = (" + x + "," + y + ")");
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        // Background color!
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);
        // Draw circles!
        for (int i = 0; i < numColors; i++) {
            Log.i("render()", "Draw circle " + i);
            paint = new Paint();
            paint.setColor(colors[i]);
            canvas.drawCircle(((i+1)*(x))/(numColors+1), y/2, RENDER_RADIUS/numColors, paint);
        }
        Log.i("render()", "render() called!");
        return bmp;
    }

    public abstract int generateColor(int[] colorsIn);

    public static Palette createPalette(int primary, String paletteType) {
        Palette palette;
        if (paletteType.equals(COMPLEMENTARY)) {
            // palette = new ComplentaryPalette(
            palette = null; // Remove this line later!
        }
        else { // Default to random palette!
            palette = new RandomPalette(primary);
        }
        return palette;
    }

}
