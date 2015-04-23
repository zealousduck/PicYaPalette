package edu.auburn.eng.csse.comp3710.team8;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * From https://www.caveofprogramming.com/guest-posts/custom-listview-with-imageview-and-textview-in-android.html
 * Created by patrickstewart on 4/23/15.
 */
public class PaletteAdapter extends BaseAdapter {

    private         Context         context;
    private         Palette[]       palettesList;
    private static  LayoutInflater  inflater = null;

    public PaletteAdapter(Activity activityIn, Palette[] paletteIn) {
        this.context =      activityIn;
        this.palettesList = paletteIn;
        inflater =          (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    public class Holder {
        TextView    paletteName;
        ImageView   paletteRender;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView = inflater.inflate(R.layout.listed_palette, null);
        holder.paletteName = (TextView) rowView.findViewById(R.id.palette_name);
        holder.paletteRender = (ImageView) rowView.findViewById(R.id.palette_render);

        holder.paletteName.setText(palettesList[position].getName());
        holder.paletteRender.setImageDrawable(palettesList[position].render());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open up the PaletteDetails!

            }
        });

        return rowView;
    }
}
