package edu.auburn.eng.csse.comp3710.team8;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends Activity {
    public static final String PREF_FILE_NAME = "appPreferences";
    public static final String ALGORITHM_PREF = "Algorithm_Preference_Key";
    public static final String NUM_PAL_PREF = "Number_Palettes_Preference_Key";
    public static final String LIGHT_PREF = "Light_Preference_Key";
    public static final String GEN_BRIGHT_PREF = "Generation_Brightness_Key";

    private Spinner mAlgorithms;
    private Spinner mNumPalettes;
    private Spinner mLightConditions;
    private Spinner mGenPreferences;

    private Integer[] numOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Algorithms Options
        mAlgorithms = (Spinner)findViewById(R.id.spinner_algorithm);
        String[] algs = Palette.getAlgorithmChoices();
        ArrayAdapter<String> algorithmAdapter = new ArrayAdapter<String>(
                SettingsActivity.this,
                R.layout.large_spinner_item,
                algs);
        algorithmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAlgorithms.setAdapter(algorithmAdapter);
        String algorithm = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE).getString(ALGORITHM_PREF, "any");
        mAlgorithms.setSelection(algorithmAdapter.getPosition(algorithm));

        // Palettes per Roll Options
        mNumPalettes = (Spinner)findViewById(R.id.spinner_num_palettes);
        numOptions = new Integer[4];
        for (int i = 0; i < numOptions.length; i++) {
            numOptions[i] = 5*(i+1);
        }
        ArrayAdapter<Integer> numAdapter = new ArrayAdapter<Integer>(
                SettingsActivity.this,
                R.layout.large_spinner_item,
                numOptions);
        numAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mNumPalettes.setAdapter(numAdapter);
        int numPalettes = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE).getInt(NUM_PAL_PREF, 5);
        mNumPalettes.setSelection((numPalettes / 5)-1);

        // Light Conditions Options
        mLightConditions = (Spinner)findViewById(R.id.spinner_light_conditions);
        String[] light = ImageProcessor.getLightOptions();
        ArrayAdapter<String> lightAdapter = new ArrayAdapter<String>(
                SettingsActivity.this,
                R.layout.large_spinner_item,
                light);
        lightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLightConditions.setAdapter(lightAdapter);
        String lightConditions = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE).getString(LIGHT_PREF, "Normal");
        mLightConditions.setSelection(lightAdapter.getPosition(lightConditions));

        // Generation Preferences Options
        mGenPreferences = (Spinner)findViewById(R.id.spinner_generation_preference);
        String[] genPrefs = Palette.getGenerationPreferences();
        ArrayAdapter<String> genPrefAdapter = new ArrayAdapter<String>(
                SettingsActivity.this,
                R.layout.large_spinner_item,
                genPrefs);
        genPrefAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGenPreferences.setAdapter(genPrefAdapter);
        String genPreferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE).getString(GEN_BRIGHT_PREF, "None");
        mGenPreferences.setSelection(genPrefAdapter.getPosition(genPreferences));

    }

    // Save Button onClick
    public void savePreferences(View view) {
        SharedPreferences.Editor editor = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE).edit();
        editor.putString(ALGORITHM_PREF, Palette.getAlgorithmChoices()[mAlgorithms.getSelectedItemPosition()]);
        editor.putInt(NUM_PAL_PREF, numOptions[mNumPalettes.getSelectedItemPosition()]);
        editor.putString(LIGHT_PREF, ImageProcessor.getLightOptions()[mLightConditions.getSelectedItemPosition()]);
        editor.putString(GEN_BRIGHT_PREF, Palette.getGenerationPreferences()[mGenPreferences.getSelectedItemPosition()]);
        editor.commit();

        Toast toast = Toast.makeText(SettingsActivity.this,
                "Settings Saved",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    // Delete Button onClick
    public void deleteFavorites(View view) {
        // Handle the deletion of all favorites!
        // Warn the user in a dialog that this cannot be undone!
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this,
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setIcon(R.drawable.ic_help);
        builder.setTitle("Delete All Favorites?");
        builder.setMessage("This action cannot be undone.");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Delete All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PaletteStorageHelper psh = new PaletteStorageHelper(SettingsActivity.this);
                psh.removeAll();  // Completely clear database!

                Toast toast = Toast.makeText(SettingsActivity.this,
                        "Favorites Deleted",
                        Toast.LENGTH_SHORT);
                toast.show();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    // Display Help for Algorithms onClick
    public void helpAlgorithm(View view) {
        final String msg = "This setting controls which algorithm the app uses to generate color palettes by default. Choosing 'Any' will allow palettes to be created by a variety of the available algorithms.";
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this,
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

    // Display Help for Palettes per Roll onClick
    public void helpNumPerRoll(View view) {
        final String msg = "This setting controls how many palettes are generated at a time. High values may result in degraded performance.";
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this,
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setIcon(R.drawable.ic_help);
        builder.setTitle("Palettes per Roll:");
        builder.setMessage(msg);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    // Display Help for Light Conditions onClick
    public void helpLightConditions(View view) {
        final String msg = "This setting designates how bright or dark the light was when a picture is taken. The app will do brightness correction based on this setting.";
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this,
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setIcon(R.drawable.ic_help);
        builder.setTitle("Light Conditions:");
        builder.setMessage(msg);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    // Display Help for Generation Preferences onClick
    public void helpGenPrefs(View view) {
        final String msg = "This setting controls the lightness preference of the algorithm. Generated colors will tend to be lighter or darker based on this setting.";
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this,
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setIcon(R.drawable.ic_help);
        builder.setTitle("Generation Preferences:");
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
