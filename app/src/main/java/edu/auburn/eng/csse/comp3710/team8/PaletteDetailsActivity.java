package edu.auburn.eng.csse.comp3710.team8;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class PaletteDetailsActivity extends Activity {

    public static final int NUM_TEXT_VIEWS = 5;
    private static final String SAVE_TEXT = "Save Palette to Favorites";
    private static final String UNSAVE_TEXT = "Remove Palette from Favorites";

    private ImageView mPaletteRender;
    private Button    mFavoriteButton;

    private PaletteStorageHelper psh;

    private Palette palette;
    private String  paletteStgs[];
    private int     textViews[];
    private boolean saved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette_details);

        // Necessary for render() !
        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        textViews = new int[NUM_TEXT_VIEWS];
        textViews[0] = (R.id.text_palette_string0);
        textViews[1] = (R.id.text_palette_string1);
        textViews[2] = (R.id.text_palette_string2);
        textViews[3] = (R.id.text_palette_string3);
        textViews[4] = (R.id.text_palette_string4);

        // Load palette from Bundle!
        palette = new Palette(
                getIntent().getExtras().getBundle(PaletteAdapter.PALETTE_KEY));
        paletteStgs = palette.getDetailedStrings();

        mPaletteRender = (ImageView)findViewById(R.id.image_palette_detail);
        Bitmap bmp = palette.render(2*size.x, 2*size.y); // Constants to enlarge image
        if (bmp == null) {
            Log.i("PaletteDetailsActivity", "bmp nulL!");
        }
        else mPaletteRender.setImageBitmap(bmp);
        if (palette.numColors <= 3) {   // Center the text views for less than 3 colors
            for (int i = 0; i < paletteStgs.length; i++) {
                TextView temp = (TextView)findViewById(textViews[i+1]);
                temp.setText(paletteStgs[i]);
            }
        }
        else {  // Display 4-5 colors
            for (int i = 0; i < paletteStgs.length; i++) {
                TextView temp = (TextView)findViewById(textViews[i]);
                temp.setText(paletteStgs[i]);
            }
        }

        psh = new PaletteStorageHelper(PaletteDetailsActivity.this);
        saved = psh.isSaved(palette);
        mFavoriteButton = (Button)findViewById(R.id.button_save);
        if (saved) {
            mFavoriteButton.setText(UNSAVE_TEXT);
        }
        else {
            mFavoriteButton.setText(SAVE_TEXT);
        }
        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saved) {
                    // Remove palette
                    psh.remove(palette);
                    saved = false;
                    mFavoriteButton.setText(SAVE_TEXT);
                }
                else {
                    // Allow user to set name?
                    // Save the palette!
                    psh.save(palette);
                    saved = true;
                    mFavoriteButton.setText(UNSAVE_TEXT);
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_palette_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
