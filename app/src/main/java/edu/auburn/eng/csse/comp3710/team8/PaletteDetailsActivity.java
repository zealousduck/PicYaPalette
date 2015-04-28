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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class PaletteDetailsActivity extends Activity {

    public static final int NUM_TEXT_VIEWS = 5;
    private static final String SAVE_TEXT = "Save Palette to Favorites";
    private static final String UNSAVE_TEXT = "Remove Palette from Favorites";

    private ImageView mPaletteRender;
    private Button    mFavoriteButton;
    private TextView  mAlgorithm;

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

        // Display palette render
        mPaletteRender = (ImageView)findViewById(R.id.image_palette_detail);
        Bitmap bmp = palette.render(2*size.x, 2*size.y); // Constants to enlarge image
        if (bmp == null) {
            Log.i("PaletteDetailsActivity", "bmp nulL!");
        }
        else mPaletteRender.setImageBitmap(bmp);

        //Display palette render
        mAlgorithm = (TextView)findViewById(R.id.text_details_algorithm);
        mAlgorithm.setText(palette.getAlgorithmUsed() + " Palette");

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

        psh = new PaletteStorageHelper(PaletteDetailsActivity.this);
        psh.open();
        saved = psh.isSaved(palette);
        psh.close();
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
                    Toast toast = Toast.makeText(PaletteDetailsActivity.this,
                                  "Palette Removed from Favorites",
                                  Toast.LENGTH_SHORT);
                    toast.show();
                    saved = false;
                    mFavoriteButton.setText(SAVE_TEXT);
                }
                else {
                    // Allow user to set name?
                    final EditText input = new EditText(PaletteDetailsActivity.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    input.setHint("Palette Name Here");
                    AlertDialog.Builder builder = new AlertDialog.Builder(PaletteDetailsActivity.this,
                            AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                    builder.setView(input);
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
                            if (tempName != null && !tempName.equals("")){
                                palette.setName(tempName);
                            }
                            result = psh.save(palette);
                            saved = true;
                            mFavoriteButton.setText(UNSAVE_TEXT);
                            Toast toast;
                            if (result == PaletteStorageHelper.SUCCESS) {
                                toast = Toast.makeText(PaletteDetailsActivity.this,
                                        "Palette Saved to Favorites",
                                        Toast.LENGTH_SHORT);
                            }
                            else if (result == PaletteStorageHelper.DUPLICATE) {
                                toast = Toast.makeText(PaletteDetailsActivity.this,
                                        "Duplicate Palette Detected",
                                        Toast.LENGTH_SHORT);
                            }
                            else {
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
