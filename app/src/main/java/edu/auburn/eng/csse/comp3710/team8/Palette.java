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

        private static final int numAlgorithms = 3;
    }

    /* Bundle key data */
    public static final String BUNDLE_NAME = "PALETTE_NAME_KEY";
    public static final String BUNDLE_ARRAY = "PALETTE_INT[]_KEY";

    /* Information for all Palettes to use */
    protected static int COUNTER = 0; // Used for numbering palettes for default names
    protected static final String DEFAULT_NAME = "Palette #";
    protected static int numColors = 3;

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
        }
    }

    /* Packages the Palette into a bundle so it can be easily passed between activities.
     * To be used with the Bundle constructor.
     */
    public Bundle getPaletteBundle() {
        Bundle b = new Bundle();
        b.putString(BUNDLE_NAME, name);
        b.putIntArray(BUNDLE_ARRAY, colors);
        return b;
    }

    /* Returns the color int array */
    public int[] getColors() {
        return colors;
    }

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
        for (int i = 0; i < numColors; i++) {
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
        Random rng = new Random();
        if (algorithm.equals(PaletteAlgorithm.COMPLEMENTARY)) {
            return Color.RED;
        }
        else if (algorithm.equals(PaletteAlgorithm.RANDOM)) {
            // Return a random number in the range 0 to 2^24
            return (rng.nextInt(0x01000000) | 0xFF000000);
        }
        else if (algorithm.equals(PaletteAlgorithm.BTCH_IM_FABULOUS)) {
            int base = 0;
            for (int i = 0; i < colorsIn.length; i++) {
                base += (colorsIn[i] & 0x00FF0000);
            }
            return ((((rng.nextInt(base)+0x00770000) & 0xFFFF55FF)+ 0x00040404) | 0xFF000077);
        }
        else if (algorithm.equals(PaletteAlgorithm.GRADIENT)) {  // Set colors manually for testing purposes
            //for (int i = 0; )
            colorsIn[0] = 0xFF2D397E;
            colorsIn[1] = 0xFFFFFFFF;
            colorsIn[2] = 0xFFE47F13;
            return 0xFFE47F13;
        }
        else if (algorithm.equals(PaletteAlgorithm.DEFAULT)) {  // Set colors manually for testing purposes
            colorsIn[0] = 0xFF2D397E;
            colorsIn[1] = 0xFFFFFFFF;
            colorsIn[2] = 0xFFE47F13;
            return 0xFFE47F13;
        }
        else return 0;
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
        return algs;
    }

}
