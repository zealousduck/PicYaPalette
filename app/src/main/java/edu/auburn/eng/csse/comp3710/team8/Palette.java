package edu.auburn.eng.csse.comp3710.team8;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;

import java.util.Random;

/**
 * Primary Model class for PicYaPalette
 */
public class Palette {
    /* Storage for various Algorithm name keys. */
    public final class PaletteAlgorithm {
        public static final String ANY = "Any"; // Allows user to use any of the algorithms
        public static final String DEFAULT = "Default";
        public static final String COMPLEMENTARY = "Complementary";
        /* Actual algorithms: */
        public static final String RANDOM = "Random";
        public static final String BTCH_IM_FABULOUS = "Fabulous";
        public static final String GRADIENT = "Gradient";
        public static final String DOMINANT = "Dominant";

        private static final int numAlgorithms = 4;

        /* Constants for generation preferences */
        public static final String PREF_DARK = "Dark";
        public static final String PREF_NONE = "None";
        public static final String PREF_LIGHT = "Light";
    }

    /* Bundle key data */
    public static final String BUNDLE_NAME = "PALETTE_NAME_KEY";
    public static final String BUNDLE_ARRAY = "PALETTE_INT[]_KEY";
    public static final String BUNDLE_ALGORITHM = "PALETTE_ALG_KEY";

    /* Information for all Palettes to use */
    protected static int COUNTER = 0; // Used for numbering palettes for default names
    protected static final String DEFAULT_NAME = "Palette #";
    protected static int numColors = 3;
    protected static int brightPreference = 0;
    protected static final int brightFactor = 0x35; // Colors ~20% brighter/darker

    /* Palette-specific data */
    protected String name;
    protected String algorithmUsed;
    protected int[] colors; // Array implementation supports arbitrary number of colors

    /* Constructor. Accepts an array of color "seeds", which will populate the color array
     * and be used as a reference for any additional colors generated by the algorithm.
     * See Palette.PaletteAlgorithm for Algorithm types.
     */
    protected Palette(int[] colorSeeds, String algorithm) {
        name = DEFAULT_NAME + COUNTER++;
        algorithmUsed = algorithm;
        colors = new int[numColors];
        if (algorithm == null) { // Just read in seed colors, no generation
            for (int i = 0; i < colorSeeds.length; i++) {
                colors[i] = (colorSeeds[i] | 0xFF000000); // Nullify transparency
            }
        }
        else { // Standard use case, fill in based on seeds, then generate remaining
            int i = 0;
            for (int j = 0; j < numColors; j++) {
                if (i < colorSeeds.length) { // If colors[j] found in seed, fill it in
                    colors[j] = (colorSeeds[i] | 0xFF000000); // Nullify transparency
                    i++;
                } else { // If colors[j] not found, generate it
                    colors[j] = generateColor(colors, algorithm);
                }
            }
        }
    }

    /* Constructor. Rebuilds the Palette from a Bundle,
     * ASSUMING that the Bundle has been created by getPaletteBundle().
     */
    protected Palette(Bundle paletteBundle) {
        if (paletteBundle != null) {
            name = paletteBundle.getString(BUNDLE_NAME);
            colors = paletteBundle.getIntArray(BUNDLE_ARRAY);
            algorithmUsed = paletteBundle.getString(BUNDLE_ALGORITHM);
        }
    }

    /* Packages the Palette into a bundle so it can be easily passed between activities.
     * To be used with the Bundle constructor.
     */
    public Bundle getPaletteBundle() {
        Bundle b = new Bundle();
        b.putString(BUNDLE_NAME, name);
        b.putIntArray(BUNDLE_ARRAY, colors);
        b.putString(BUNDLE_ALGORITHM, algorithmUsed);
        return b;
    }

    /* Returns the color int array */
    public int[] getColors() {
        return colors;
    }

    /* Returns the algorithm used to generate this palette */
    public String getAlgorithmUsed() { return algorithmUsed; }

    /* Sets the Palette's name (for saving purposes) */
    public void setName(String nameIn) {
        name = nameIn;
    }

    /* Returns the Palette's name */
    public String getName() {
        return name;
    }

    /* Return the number of colors Palettes generate */
    public static int getNumColors() {
        return numColors;
    }

    /* Returns an array of Strings, specially formatted for use with
     * PaletteDetailsActivity.
     */
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
        final int RENDER_WIDTH = 7*screenWidth/16;
        final int RENDER_HEIGHT = screenHeight/8;
        final int RENDER_RADIUS = RENDER_HEIGHT/(numColors);
        // Return an image of the palette!
        Bitmap bmp = (Bitmap.createBitmap(RENDER_WIDTH, RENDER_HEIGHT, Bitmap.Config.ARGB_8888));
        Canvas canvas = new Canvas(bmp);
        int x = canvas.getWidth();
        int y = canvas.getHeight();
        Paint paint = new Paint();
        paint.setAntiAlias(true); paint.setFilterBitmap(true);
        paint.setStyle(Paint.Style.FILL);
        for (int i = numColors-1; i >= 0; i--) {
            // Draw outline
            paint.setColor(Color.DKGRAY);
            canvas.drawCircle(((i+1)*x)/(numColors+1), y/2, RENDER_RADIUS+3, paint);
            // Draw color
            paint.setColor(colors[i]);
            Log.i("render()", "Draw circle " + i + " Color: " + Integer.toHexString(colors[i]));
            canvas.drawCircle(((i+1)*x)/(numColors+1), y/2, RENDER_RADIUS, paint);

        }
        return bmp;
    }

    /* Generates a new color to add to the Palette.
     * colorsIn[] can be read for reference as to which colors are already used.
     * algorithm is used to determine which generation algorithm will be used.
     * Make sure to OR our results with 0xFF000000 in order to nullify transparency!
     */
    public static int generateColor(int[] colorsIn, String algorithm) {
        //setBrightPreference(brightness);
        int red, green, blue;
        int color, lastColor = 0;
        for (int i = 0; i < colorsIn.length; i++) {
            if (colorsIn[i] != 0) lastColor = i;
        }
        Random rng = new Random();
        color = 0;
        if (algorithm.equals(PaletteAlgorithm.COMPLEMENTARY)) {
            return Color.RED;
        } else if (algorithm.equals(PaletteAlgorithm.RANDOM)) {
            // Return a random number in the range 0 to 2^24
            color = (rng.nextInt(0x01000000) | 0xFF000000);
        } else if (algorithm.equals(PaletteAlgorithm.BTCH_IM_FABULOUS)) {
            int base = 0;
            for (int i = 0; i < colorsIn.length; i++) {
                base += (colorsIn[i] & 0x00FF0000);
            }
            color = ((((rng.nextInt(base) + 0x00770000) & 0xFFFF55FF) + 0x00040404) | 0xFF000077);
        } else if (algorithm.equals(PaletteAlgorithm.GRADIENT)) {  // Set colors manually for testing purposes
            //for (int i = 0; )
            colorsIn[0] = 0xFF2D397E;
            colorsIn[1] = 0xFFFFFFFF;
            colorsIn[2] = 0xFFE47F13;
            color = 0xFFE47F13;
        } else if (algorithm.equals(PaletteAlgorithm.DOMINANT)) {
            int dominantId = 0;
            int dominant = 0;
            for (int i = 0; i < 3; i++) {
                if (((colorsIn[0] >> 8 * i) & 0xFF) > dominant) {
                    dominantId = i;   // 0 -> Blue, 1 -> Green, 2 -> Red
                    dominant = (colorsIn[0] >> 8 * i) & 0x000000FF;
                    Log.i("DOMINANT", "dominant = " + i);
                }
            }
            switch (dominantId) { // Preserve the dominant, roll for others
                case 0: // BLUE
                    color = (0xFF << 24) | (rng.nextInt(0x100) << 16) | (rng.nextInt(0x100) << 8) | (dominant);
                    break;
                case 1: // GREEN
                    color = (0xFF << 24) | (rng.nextInt(0x100) << 16) | (dominant << 8) | (rng.nextInt(0x100));
                    break;
                case 2: // RED
                    color = (0xFF << 24) | (dominant << 16) | (rng.nextInt(0x100) << 8) | (rng.nextInt(0x100));
                    break;
                default: // Error, shouldn't happen...
                    color = 0;
            }
        } else if (algorithm.equals(PaletteAlgorithm.DEFAULT)) {  // Set colors manually for testing purposes
            colorsIn[0] = 0xFF2D397E;
            colorsIn[1] = 0xFFFFFFFF;
            colorsIn[2] = 0xFFE47F13;
            color = 0xFFE47F13;
        }

        // Safely adjust colors according to brightness preference!
        red = Color.red(color);
        blue = Color.blue(color);
        green = Color.green(color);
        if (brightPreference != 0) { // Skip this math if we don't use it...
            int tempColor = (red + brightFactor * brightPreference);
            if (tempColor <= 0xFF && tempColor >= 0) {
                red = tempColor;
            } else {
                if (tempColor > 0xFF) red = 0xFF;
                else red = 0;
            }
            tempColor = (green + brightFactor * brightPreference);
            if (tempColor <= 0xFF && tempColor >= 0) {
                green = tempColor;
            } else {
                if (tempColor > 0xFF) green = 0xFF;
                else green = 0;
            }
            tempColor = (blue + brightFactor * brightPreference);
            if (tempColor <= 0xFF && tempColor >= 0) {
                blue = tempColor;
            } else {
                if (tempColor > 0xFF) blue = 0xFF;
                else blue = 0;
            }
        }
        /*
        // Prevent colors being too similar...?
        if (red - Color.red(colorsIn[lastColor]) < 0x15)     red = (red - 0x15) % 0xFF;
        if (green - Color.green(colorsIn[lastColor]) < 0x15) green = (green - 0x15) % 0xFF;
        if (blue - Color.blue(colorsIn[lastColor]) < 0x15)   blue = ...
        */

        color = (0xFF << 24) | (red << 16) | (green << 8) | (blue);

        return color;
    }

   /* public static Color RandomMix(Color color1, Color color2, Color color3,
                                  float greyControl) {
        Random random = new Random();
        byte[] bytes = new byte[1];
        random.nextBytes(bytes);
        int randomIndex = bytes[0] % 3;

        float mixRatio1 =
                (randomIndex == 0) ? random.nextFloat() * greyControl : random.nextFloat();

        float mixRatio2 =
                (randomIndex == 1) ? random.nextFloat() * greyControl : random.nextFloat();

        float mixRatio3 =
                (randomIndex == 2) ? random.nextFloat() * greyControl : random.nextFloat();

        float sum = mixRatio1 + mixRatio2 + mixRatio3;

        mixRatio1 /= sum;
        mixRatio2 /= sum;
        mixRatio3 /= sum;
        return Color(
                (byte)(mixRatio1 * color1.getRed() + mixRatio2 * color2.getRed() + mixRatio3 * color3.getRed()),
                (byte)(mixRatio1 * color1.getGreen() + mixRatio2 * color2.getGreen() + mixRatio3 * color3.getGreen()),
                (byte)(mixRatio1 * color1.getBlue() + mixRatio2 * color2.getBlue() + mixRatio3 * color3.getBlue()),
                255);
    }*/

    /* Call this whenever ANY is passed to generateColor()
         */
    public static String any() {
        String algorithm;
        Random rng = new Random();
        switch (rng.nextInt(PaletteAlgorithm.numAlgorithms)) {
            case 0:
                algorithm = PaletteAlgorithm.RANDOM;
                break;
            case 1:
                algorithm = PaletteAlgorithm.BTCH_IM_FABULOUS;
                break;
            case 2:
                algorithm = PaletteAlgorithm.GRADIENT;
                break;
            case 3:
                algorithm = PaletteAlgorithm.DOMINANT;
                break;
            default:
                algorithm = PaletteAlgorithm.DEFAULT;
        }
        Log.i("any()", algorithm);
        return algorithm;
    }

    /* Used for preference spinner. New algorithms must be added here to be available for
     * selection.
     */
    public static String[] getAlgorithmChoices() {
        String[] algs = new String[PaletteAlgorithm.numAlgorithms+1];
        algs[0] = PaletteAlgorithm.ANY;
        algs[1] = PaletteAlgorithm.RANDOM;
        algs[2] = PaletteAlgorithm.BTCH_IM_FABULOUS;
        algs[3] = PaletteAlgorithm.GRADIENT;
        algs[4] = PaletteAlgorithm.DOMINANT;
        return algs;
    }

    /* Used for preference spinner.
     */
    public static String[] getGenerationPreferences() {
        String[] prefs = new String[3];
        prefs[0] = PaletteAlgorithm.PREF_NONE;
        prefs[1] = PaletteAlgorithm.PREF_LIGHT;
        prefs[2] = PaletteAlgorithm.PREF_DARK;
        return prefs;
    }

    /* Sets the brightness skew of the palette algorithms!
     */
    public static void setBrightPreference(String pref) {
        if (pref.equals(PaletteAlgorithm.PREF_DARK)) {
            brightPreference = -1;
        }
        else if (pref.equals(PaletteAlgorithm.PREF_NONE)) {
            brightPreference = 0;
        }
        else if (pref.equals(PaletteAlgorithm.PREF_LIGHT)) {
            brightPreference = 1;
        }
        else
            brightPreference = 0;
    }

}
