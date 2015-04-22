package edu.auburn.eng.csse.comp3710.team8;

import android.graphics.Color;

/**
 * Palette generated by a "random" algorithm. Probably pretty ugly.
 */
public class RandomPalette extends Palette {
    public RandomPalette(Color base) {
        super(base);
        // Generate secondary and tertiary colors!

    }

    public RandomPalette(Color primary, Color secondary) {
        super(primary, secondary);
        // Generate tertiary colors!

    }

    public RandomPalette(Color primary, Color secondary, Color tertiary) {
        super(primary, secondary, tertiary);
    }
}
