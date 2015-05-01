package edu.auburn.eng.csse.comp3710.team8;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PaletteDetailsActivity extends Activity {

    public static final int NUM_TEXT_VIEWS = 5;
    private static final String SAVE_TEXT = "Save Palette to Favorites";
    private static final String UNSAVE_TEXT = "Remove Palette from Favorites";
    private static final String UPDATED_NAME_KEY = "Palette_Name_Updated";

    private ImageView mPaletteRender;
    private TextView  mFavoriteButton;
    private TextView  mAlgorithm;

    private PaletteStorageHelper psh;

    private Palette palette;
    private String  paletteStgs[];
    private int     textViews[];
    private boolean saved;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette_details);
        if (savedInstanceState != null) {
            extras = savedInstanceState;
        } else extras = new Bundle();

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

        psh = new PaletteStorageHelper(PaletteDetailsActivity.this);
        saved = psh.isSaved(palette);
        mFavoriteButton = (TextView)findViewById(R.id.button_save);
        if (saved) {
            mFavoriteButton.setText(UNSAVE_TEXT);
            //palette.setName("Duplicate");
        }
        else {
            mFavoriteButton.setText(SAVE_TEXT);
        }

        // Display palette render
        mPaletteRender = (ImageView)findViewById(R.id.image_palette_detail);
        Bitmap bmp;
        if (size.x < size.y) { // Portrait Mode
            bmp = palette.render(size.x + (8 * size.x / 20), size.y + (5 * size.y / 20)); // Constants to enlarge image
        } else {                // Landscape Mode
            bmp = palette.render( size.y + (18 * size.y / 20), size.x + (9 * size.x / 20)); // Constants to enlarge image
        }
        if (bmp == null) {
            Log.i("PaletteDetailsActivity", "bmp nulL!");
        }
        else mPaletteRender.setImageBitmap(bmp);

        // Display palette name
        mAlgorithm = (TextView)findViewById(R.id.text_details_algorithm);
        extras.putString(UPDATED_NAME_KEY, palette.getName() + "\n" + palette.getAlgorithmUsed() + " Palette");

        // Display palette color codes
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        extras.putString(UPDATED_NAME_KEY, palette.getName() + "\n" + palette.getAlgorithmUsed() + " Palette");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAlgorithm.setText(extras.getString(UPDATED_NAME_KEY));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        Log.i("onRestoreInstanceState", "r");
        if (savedState != null) {
            extras.putString(UPDATED_NAME_KEY, savedState.getString(UPDATED_NAME_KEY));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i("onSaveInstanceState", "s");
        if (outState != null) {
            outState.putString(UPDATED_NAME_KEY, palette.getName() + "\n" + palette.getAlgorithmUsed() + " Palette");
        }
    }

    // Save to Favorites onClick
    public void saveToFavorites(View view) {
        if (saved) {
            // Remove palette
            psh.remove(palette);
            Toast toast = Toast.makeText(PaletteDetailsActivity.this,
                    "Palette Removed from Favorites",
                    Toast.LENGTH_SHORT);
            toast.show();
            saved = false;
            mFavoriteButton.setText(SAVE_TEXT);
        } else {
            // Allow user to set name?
            final EditText input = new EditText(PaletteDetailsActivity.this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setHint("Palette Name Here");
            AlertDialog.Builder builder = new AlertDialog.Builder(PaletteDetailsActivity.this,
                    AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            builder.setView(input);
            builder.setIcon(R.drawable.ic_help);
            builder.setTitle("Enter a name for the Palette:");
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Save Palette", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Save the palette!
                    int result = PaletteStorageHelper.SUCCESS;
                    String tempName = input.getText().toString();
                    if (tempName != null && !tempName.equals("")) {
                        palette.setName(tempName);
                    }
                    result = psh.save(palette);
                    saved = true;
                    mFavoriteButton.setText(UNSAVE_TEXT);
                    mAlgorithm.setText(palette.getName() + "\n" + palette.getAlgorithmUsed() + " Palette");
                    Toast toast;
                    if (result == PaletteStorageHelper.SUCCESS) {
                        toast = Toast.makeText(PaletteDetailsActivity.this,
                                "Palette Saved to Favorites",
                                Toast.LENGTH_SHORT);
                    } else if (result == PaletteStorageHelper.DUPLICATE) {
                        toast = Toast.makeText(PaletteDetailsActivity.this,
                                "Duplicate Palette Detected",
                                Toast.LENGTH_SHORT);
                    } else {
                        toast = Toast.makeText(PaletteDetailsActivity.this,
                                "Failed to Save Palette",
                                Toast.LENGTH_SHORT);
                    }
                    toast.show();
                    dialog.dismiss();
                }
            });
            builder.show();
        }

    }
}
