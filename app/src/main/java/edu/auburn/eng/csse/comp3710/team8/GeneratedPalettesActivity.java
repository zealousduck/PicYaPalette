package edu.auburn.eng.csse.comp3710.team8;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;



public class GeneratedPalettesActivity extends Activity {

    private static final int NUM_PALETTES = 10;

    private int baseColor;
    private Palette[] palettes;

    private ListView mList;
    private PaletteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_palettes);

        Intent i = getIntent();
        baseColor = i.getIntExtra(ImageChooserActivity.COLOR_KEY, 0xFFFFFFFF);

        // Start thread to populate list of palettes!
        new Thread(new Runnable() {
            @Override
            public void run() {
                rerollPalettes();
            }
        }).run();

        mList = (ListView)findViewById(R.id.list_generated);
        mAdapter = new PaletteAdapter(this, palettes);
        mList.setAdapter(mAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_generated_palettes, menu);
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

    public void rerollPalettes() {
        palettes = new Palette[NUM_PALETTES];

        for (int i = 0; i < NUM_PALETTES; i++) {
            palettes[i] = Palette.createPalette(baseColor, Palette.RANDOM);
        }
    }
}
