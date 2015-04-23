package edu.auburn.eng.csse.comp3710.team8;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by patrickstewart on 4/22/15.
 */
public abstract class Palette {

    protected static int COUNTER = 0; // Used for numbering palettes for default names
    protected static final String DEFAULT_NAME = "Palette #";

    protected String name;
    // Colors are only int values of the format (red << 16) | (green << 8) | (blue)
    protected int primary, secondary, tertiary;
    // List<Color> colors;

    public Palette(int base) {
        this.primary = base;
        this.name = DEFAULT_NAME + COUNTER++;
        // Generate secondary and tertiary colors!
    }

    public Palette(int primary, int secondary) {
        this.primary = primary;
        this.secondary = secondary;
        this.name = DEFAULT_NAME + COUNTER++;
        // Generate tertiary colors!
    }

    public Palette(int primary, int secondary, int tertiary) {
        this.primary = primary;
        this.secondary = secondary;
        this.tertiary = tertiary;
        this.name = DEFAULT_NAME + COUNTER++;
    }

    public void setName(String nameIn) {
        this.name = nameIn;
    }

    public String getName() {
        return name;
    }

    public Bitmap render() {
        // Return an image of the palette!
        return null;
    }

}
