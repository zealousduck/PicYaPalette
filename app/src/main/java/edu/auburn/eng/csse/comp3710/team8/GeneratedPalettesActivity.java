package edu.auburn.eng.csse.comp3710.team8;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class GeneratedPalettesActivity extends Activity {

    private static final int NUM_PALETTES = 10;
    private static String ALGORITHM = Palette.PaletteAlgorithm.ANY; // Set via settings?

    private int baseColors[];
    private Palette[] palettes;

    private ListView mList;
    private PaletteAdapter mAdapter;
    private TextView mAlgorithm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_palettes);
        baseColors = new int[Palette.getNumColors()];
        Intent i = getIntent();
        baseColors = i.getIntArrayExtra(ImageChooserActivity.COLOR_KEY);

        final ProgressDialog pd =
                ProgressDialog.show(this,"Processing...", "Hold on...",true,false);
        // Start thread to populate list of palettes!
        new Thread(new Runnable() {
            @Override
            public void run() {
                palettes = new Palette[NUM_PALETTES];
                for (int i = 0; i < NUM_PALETTES; i++) {
                    if (ALGORITHM.equals(Palette.PaletteAlgorithm.ANY)) {
                        palettes[i] = new Palette(baseColors, Palette.any());
                    }
                    else {
                        palettes[i] = new Palette(baseColors, ALGORITHM);
                    }
                }
                pd.dismiss();
            }
        }).run();

        mAlgorithm = (TextView)findViewById(R.id.text_algorithm_used);
        mAlgorithm.setText("Algorithm used: " + ALGORITHM);

        mList = (ListView)findViewById(R.id.list_generated);
        mAdapter = new PaletteAdapter(GeneratedPalettesActivity.this, palettes);
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

    /* WARNING: Any changes to the reroll() method must be replicated in onCreate()!
     * Rerolls our generated palettes without needing to get a new picture!
     */
    public void reroll(View view) {
        final ProgressDialog pd =
                ProgressDialog.show(this,"Processing...", "Hold on...",true,false);
        // Start thread to populate list of palettes!
        new Thread(new Runnable() {
            @Override
            public void run() {
                palettes = new Palette[NUM_PALETTES];
                for (int i = 0; i < NUM_PALETTES; i++) {
                    if (ALGORITHM.equals(Palette.PaletteAlgorithm.ANY)) {
                        palettes[i] = new Palette(baseColors, Palette.any());
                    }
                    else {
                        palettes[i] = new Palette(baseColors, ALGORITHM);
                    }
                }
                pd.dismiss();
            }
        }).run();

        mAdapter = new PaletteAdapter(this, palettes);
        mList.setAdapter(mAdapter);
    }

    public void readSettings() {
        // TO-DO: Read a preferences file to determine algorithm!
    }

    public void setAlgorithm(String algorithmIn) {
        // Allow user to set algorithm being used (called from settings?)
    }
}
