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
    protected static int numColors = 3;

    public static final String RANDOM = "RANDOM";
    public static final String COMPLEMENTARY = "COMPLEMENTARY";
    public static final String DEFAULT = "DEFAULT";

    protected static int COUNTER = 0; // Used for numbering palettes for default names
    protected static final String DEFAULT_NAME = "Palette #";

    protected String name;
    // Colors are only int values of the format (red << 16) | (green << 8) | (blue)
    protected int[] colors; // Array implementation supports arbitrary number of colors

    protected Palette(int[] colorSeeds, String algorithm) {
        name = DEFAULT_NAME + COUNTER++;
        colors = new int[numColors];
        if (algorithm == null) { // Just read in seed colors, no generation
            for (int i = 0; i < colorSeeds.length; i++) {
                colors[i] = colorSeeds[i];
            }
        }
        else if (algorithm.equals(DEFAULT)) {  // Set colors manually for testing purposes
            colors[0] = Color.CYAN;
            colors[1] = Color.LTGRAY;
            colors[2] = Color.MAGENTA;
            return;
        }
        else { // Standard use case, fill in based on seeds, then generate remaining
            int i = 0;
            for (int j = 0; j < numColors; j++) {
                if (i < colorSeeds.length) { // If colors[j] found in seed, fill it in
                    colors[j] = colorSeeds[i];
                    i++;
                } else { // If colors[j] not found, generate it
                    colors[j] = generateColor(colors, algorithm);
                }
            }
        }
    }

    public int[] getColors() {
        return colors;
    }

    public void setName(String nameIn) {
        name = nameIn;
    }

    public String getName() {
        return name;
    }

    public static int getNumColors() {
        return numColors;
    }

    @Override
    public String toString() {
        String stg = "";
        return stg;
    }

    public String[] getDetailedStrings() {
        String stgs[] = new String[numColors];
        for (int i = 0; i < numColors; i++) {
            String stg = "";
            stg += "0x" + Integer.toHexString(colors[i]).substring(2).toUpperCase() + "\n"
                +    "R: " + Color.red(colors[i]) + "\n"
                +    "G: " + Color.green(colors[i]) + "\n"
                +    "B: " + Color.blue(colors[i]) + "\n";
            stgs[i] = stg;
        }
        return stgs;
    }

    /* Renders the palette to a Bitmap. Size scales based on screen dimensions.
     */
    public Bitmap render(int screenWidth, int screenHeight) {
        final int RENDER_WIDTH = screenWidth/2;
        final int RENDER_HEIGHT = screenHeight/7;
        final int RENDER_RADIUS = RENDER_HEIGHT/(numColors+1);
        // Return an image of the palette!
        Bitmap bmp = (Bitmap.createBitmap(RENDER_WIDTH, RENDER_HEIGHT, Bitmap.Config.ARGB_8888));
        Canvas canvas = new Canvas(bmp);
        int x = canvas.getWidth();
        int y = canvas.getHeight();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < numColors; i++) {
            paint.setColor(colors[i]);
            Log.i("render()", "Draw circle " + i + " Color: " + colors[i]);
            canvas.drawCircle(((i+1)*x)/(numColors+1), y/2, RENDER_RADIUS, paint);
        }
        return bmp;
    }

    // Make sure to OR our results with 0xFF000000 in order to nullify transparency!
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
