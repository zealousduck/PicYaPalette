package edu.auburn.eng.csse.comp3710.team8;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/* USED AS A SPLASH SCREEN!
 */
public class PicYaPaletteActivity extends Activity {

    private static int SPLASH_TIMER = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_ya_palette);

        // Time out after SPLASH_TIMER milliseconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(PicYaPaletteActivity.this, ImageChooserActivity.class);
                startActivity(i);
            }
        }, SPLASH_TIMER);

    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }

}
