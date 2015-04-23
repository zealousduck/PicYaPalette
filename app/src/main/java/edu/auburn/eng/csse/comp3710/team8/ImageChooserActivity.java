package edu.auburn.eng.csse.comp3710.team8;

import android.app.Activity;
import android.content.Intent;
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

public class ImageChooserActivity extends Activity {

    private Button mTakePic;
    private Button mChoosePic;
    private Button mFavorites;

    private Camera mCamera;
    private final static String COLOR_KEY = "COLOR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_chooser);

        // Hook up Buttons
        mTakePic =      (Button)findViewById(R.id.button_take);
        mTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle taking a picture!
                // http://developer.android.com/reference/android/hardware/Camera.html
                //mCamera = Camera.open();
                //SurfaceHolder holder =

                Intent i = new Intent(ImageChooserActivity.this, GeneratedPalettesActivity.class);
                final Bundle extras = i.getExtras();
                // Analyze image in new thread!
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Process blueberries for now...
                        Bitmap bmp = BitmapFactory.decodeFile("/res/drawable/blueberries.bmp");
                        if (bmp != null) {
                            extras.putInt(COLOR_KEY, ImageProcessor.getColorInt(bmp));
                        } else {
                            Log.i("mTakePic", "bitmap null!");
                        }
                    }
                }).run();

                // Pass result of image processing to GeneratedPalettesActivity, through bundle
                startActivity(i);

            }
        });
        mChoosePic =    (Button)findViewById(R.id.button_choose);
        mChoosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle choosing a picture!
            }
        });
        mFavorites =    (Button)findViewById(R.id.button_favorites);
        mFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Favorites activity!
                Intent i = new Intent(ImageChooserActivity.this, FavoritePalettesActivity.class);
                startActivity(i);
            }
        });

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
