package edu.auburn.eng.csse.comp3710.team8;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import java.util.Random;

public class ImageChooserActivity extends Activity {

    private Button mTakePic;
    private Button mChoosePic;
    private Button mFavorites;
    private Button mSettings;

    private Camera mCamera;
    public final static String COLOR_KEY = "COLOR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_chooser);
    }

    public void takeAPicture(View view) {
        // Handle taking a picture!
        // http://developer.android.com/reference/android/hardware/Camera.html
        //mCamera = Camera.open();
        //SurfaceHolder holder =

        final Intent i = new Intent(ImageChooserActivity.this, GeneratedPalettesActivity.class);
        // Analyze image in new thread!
        final ProgressDialog pd =
                ProgressDialog.show(ImageChooserActivity.this,"Processing...", "Hold on...",true,false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Process blueberries for now...
                Bitmap bmp = BitmapFactory.decodeResource(ImageChooserActivity.this.getResources(),
                        R.drawable.blueberries);
                if (bmp != null) {
                    int[] colors = new int[1];
                    SharedPreferences prefs = getSharedPreferences(SettingsActivity.PREF_FILE_NAME, MODE_PRIVATE);
                    colors[0] = ImageProcessor.getColorInt(bmp,
                            prefs.getString(SettingsActivity.LIGHT_PREF, ImageProcessor.NORMAL));
                    i.putExtra(COLOR_KEY, colors);
                    Log.i("mTakePic", "Color:" + colors[0]);
                } else {
                    Log.i("mTakePic", "bitmap null!");
                }
                pd.dismiss();
            }
        }).run();

        // Pass result of image processing to GeneratedPalettesActivity, through bundle
        ImageChooserActivity.this.startActivity(i);
    }

    public void chooseAPicture(View view) {
        // Moved from Listener implementation to onClick implementation!
        // Handle choosing a picture from the photo library
    }

    public void picForMe(View view) {
        // Moved from Listener implementation to onClick implementation!
        // generate palettes for a random color!
        final Intent i = new Intent(ImageChooserActivity.this, GeneratedPalettesActivity.class);
        // Analyze image in new thread!
        final ProgressDialog pd =
                ProgressDialog.show(ImageChooserActivity.this,"Processing...", "Hold on...",true,false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int seed = new Random().nextInt(0x01000000);
                int[] colors = new int[1];
                colors[0] = seed;
                i.putExtra(COLOR_KEY, colors);
                Log.i("Pic For Me", "Color:" + colors[0]);
                pd.dismiss();
            }
        }).run();

        // Pass result of image processing to GeneratedPalettesActivity, through bundle
        ImageChooserActivity.this.startActivity(i);
    }

    public void openFavorites(View view) {
        // Moved from Listener implementation to onClick implementation!
        Intent i = new Intent(ImageChooserActivity.this, FavoritePalettesActivity.class);
        startActivity(i);
    }

    public void openSettings(View view) {
        // Moved from Listener implementation to onClick implementation!
        Intent i = new Intent(ImageChooserActivity.this, SettingsActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_chooser, menu);
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
