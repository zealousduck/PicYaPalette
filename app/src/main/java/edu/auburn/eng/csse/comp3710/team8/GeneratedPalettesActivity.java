package edu.auburn.eng.csse.comp3710.team8;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class GeneratedPalettesActivity extends Activity {

    private static int NUM_PALETTES = 10;       // Settings?
    private static String ALGORITHM = Palette.PaletteAlgorithm.ANY; // Set via settings?
    private static final String OUTSTATE_KEY = "GEN_PALETTE#";
    private static String BRIGHT_PREFERENCE = Palette.PaletteAlgorithm.PREF_NONE;

    private int baseColors[];
    private Palette[] palettes;


    private ListView mList;
    private PaletteAdapter mAdapter;
    private TextView mAlgorithm;

    private Spinner mAlgorithms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_palettes);
        readSettings(); // Check shared preferences for generation settings

        baseColors = new int[Palette.getNumColors()];
        Intent intent = getIntent();
        baseColors = intent.getIntArrayExtra(ImageChooserActivity.COLOR_KEY);

        if(savedInstanceState != null) {
            palettes = new Palette[NUM_PALETTES];
            for (int i = 0; i < NUM_PALETTES; i++) {
                palettes[i] = new Palette(savedInstanceState.getBundle(OUTSTATE_KEY+i));
            }
        }
        else{
            final ProgressDialog pd =
                    ProgressDialog.show(this, "Processing...", "Hold on...", true, false);
            // Start thread to populate list of palettes!
            new Thread(new Runnable() {
                @Override
                public void run() {
                    palettes = new Palette[NUM_PALETTES];
                    for (int i = 0; i < NUM_PALETTES; i++) {
                        if (ALGORITHM.equals(Palette.PaletteAlgorithm.ANY)) {
                            palettes[i] = new Palette(baseColors, Palette.any());
                        } else {
                            palettes[i] = new Palette(baseColors, ALGORITHM);
                        }
                    }
                    mAdapter = new PaletteAdapter(GeneratedPalettesActivity.this, palettes);
                    pd.dismiss();
                }
            }).run();
        }
        mList = (ListView)findViewById(R.id.list_generated);
        //mAdapter = new PaletteAdapter(GeneratedPalettesActivity.this, palettes);

        mList.setAdapter(mAdapter);

        mAlgorithms = (Spinner)findViewById(R.id.spinner_algorithm);
        String[] algs = Palette.getAlgorithmChoices();
        ArrayAdapter<String> algorithmAdapter = new ArrayAdapter<String>(
                GeneratedPalettesActivity.this,
                R.layout.white_text_spinner,
                algs);
        algorithmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAlgorithms.setAdapter(algorithmAdapter);
        mAlgorithms.setSelection(algorithmAdapter.getPosition(ALGORITHM));
        mAlgorithms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                reroll(selectedItemView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            for (int i = 0; i < NUM_PALETTES; i++) {
                outState.putBundle(OUTSTATE_KEY + i, palettes[i].getPaletteBundle());
            }
        }
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
        readSettings();
        final ProgressDialog pd =
                ProgressDialog.show(this,"Processing...", "Hold on...",true,false);
        // Start thread to populate list of palettes!
        new Thread(new Runnable() {
            @Override
            public void run() {
                ALGORITHM = Palette.getAlgorithmChoices()[mAlgorithms.getSelectedItemPosition()];
                palettes = new Palette[NUM_PALETTES];
                for (int i = 0; i < NUM_PALETTES; i++) {
                    if (ALGORITHM.equals(Palette.PaletteAlgorithm.ANY)) {
                        palettes[i] = new Palette(baseColors, Palette.any());
                    }
                    else {
                        palettes[i] = new Palette(baseColors, ALGORITHM);
                    }
                }
                mAdapter = new PaletteAdapter(GeneratedPalettesActivity.this, palettes);
                pd.dismiss();
            }
        }).run();
        //mAdapter = new PaletteAdapter(this, palettes);
        mList.setAdapter(mAdapter);
    }

    public void readSettings() {
        // TO-DO: Read a preferences file to determine algorithm!
        SharedPreferences preferences = getSharedPreferences(SettingsActivity.PREF_FILE_NAME, MODE_PRIVATE);
        NUM_PALETTES = preferences.getInt(SettingsActivity.NUM_PAL_PREF, 10);
        ALGORITHM = preferences.getString(SettingsActivity.ALGORITHM_PREF, Palette.PaletteAlgorithm.ANY);
        BRIGHT_PREFERENCE = preferences.getString(SettingsActivity.GEN_BRIGHT_PREF, Palette.PaletteAlgorithm.PREF_NONE);
        Palette.setBrightPreference(BRIGHT_PREFERENCE);
    }

    public void helpAlgorithm(View view) {
        final String msg = "Pick a different palette generating algorithm! Choosing 'Any' will allow palettes to be created by a variety of the available algorithms.";
        AlertDialog.Builder builder = new AlertDialog.Builder(GeneratedPalettesActivity.this,
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setIcon(R.drawable.ic_help);
        builder.setTitle("Algorithm Used:");
        builder.setMessage(msg);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

}
