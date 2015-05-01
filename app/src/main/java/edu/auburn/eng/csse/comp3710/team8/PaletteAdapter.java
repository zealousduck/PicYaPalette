package edu.auburn.eng.csse.comp3710.team8;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * From https://www.caveofprogramming.com/guest-posts/custom-listview-with-imageview-and-textview-in-android.html
 */
public class PaletteAdapter extends BaseAdapter {

    public final static String PALETTE_KEY = "LISTITEM_KEY";

    private         Context         context;
    private         Palette[]       palettesList;
    private         Bitmap[]        bmpsList;
    private static  LayoutInflater  inflater = null;

    /* Constructor. Accepts a context and an array of Palettes.
     */
    public PaletteAdapter(Activity activityIn, Palette[] palettesIn) {
        this.context =      activityIn;
        this.palettesList = palettesIn;
        // Populate bmpsList ahead of time, prevent heap overflows!
        // Get screen size info to pass to render() !
        WindowManager wm =  (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display =   wm.getDefaultDisplay();
        Point size =        new Point();
        display.getSize(size);
        bmpsList = new Bitmap[palettesList.length];
        for (int i = 0; i < palettesList.length; i++) {
            if (size.x < size.y) {
                bmpsList[i] = palettesList[i].render(size.x, size.y - (2 * size.y / 10));
            } else {
                bmpsList[i] = palettesList[i].render(size.y + (2 * size.y / 10), size.x - (size.x / 10));
            }
        }
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return palettesList.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /* Storage for the Views that this adapter writes to.
     */
    public class Holder {
        TextView    paletteName;
        ImageView   paletteRender;
        Bitmap      bmp;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Initialize View and Holder
        Holder holder =         new Holder();
        View   rowView =        inflater.inflate(R.layout.listed_palette, null);
        holder.paletteName =    (TextView) rowView.findViewById(R.id.palette_name);
        holder.paletteRender =  (ImageView) rowView.findViewById(R.id.palette_render);

        // Update Views with corresponding information
        holder.paletteName.setText(palettesList[position].getName() + "\n"
                                    + palettesList[position].getAlgorithmUsed());
        holder.bmp = bmpsList[position];
        holder.paletteRender.setImageBitmap(holder.bmp);

        // Handle selection of List items
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open up the PaletteDetails!
                Intent i = new Intent(context, PaletteDetailsActivity.class);
                i.putExtra(PALETTE_KEY, palettesList[position].getPaletteBundle());
                context.startActivity(i);
            }
        });

        return rowView;
    }
}
