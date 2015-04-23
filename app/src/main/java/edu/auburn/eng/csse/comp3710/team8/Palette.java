package edu.auburn.eng.csse.comp3710.team8;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;

/**
 * Created by patrickstewart on 4/22/15.
 */
public abstract class Palette {

    protected static int COUNTER = 0; // Used for numbering palettes for default names
    protected static final String DEFAULT_NAME = "Palette #";

    protected String name;
    protected int numColors = 3;
    // Colors are only int values of the format (red << 16) | (green << 8) | (blue)
    //protected int primary, secondary, tertiary;
    protected int[] colors; // Array implementation supports arbitrary number of colors
    // List<Color> colors;

    public Palette(int primary) {
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

    public Palette(int primary, int secondary) {
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

    public Palette(int primary, int secondary, int tertiary) {
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

    public void setName(String nameIn) {
        this.name = nameIn;
    }

    public String getName() {
        return name;
    }

    public ShapeDrawable render() {
        // Return an image of the palette!
        Canvas canvas = new Canvas();
        int x = canvas.getWidth();
        int y = canvas.getHeight();
        int radius = 100;    // May need tinkering!
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        // Background color!
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);
        // Draw circles!
        for (int i = 0; i < numColors; i++) {
            paint.setColor(colors[i]);
            canvas.drawCircle(x/numColors, y/numColors, radius/numColors, paint);
        }
        ShapeDrawable drawable = new ShapeDrawable();
        drawable.draw(canvas);
        return drawable;
    }

    public abstract int generateColor(int[] colorsIn);

}
