package edu.auburn.eng.csse.comp3710.team8;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Spinner;

public class SettingsActivity extends Activity {
    public static final String PREF_FILE_NAME = "appPreferences";
    public static final String ALGORITHM_PREF = "Algorithm_Preference_Key";
    public static final String NUM_PAL_PREF = "Number_Palettes_Preference_Key";
    public static final String LIGHT_PREF = "Light_Preference_Key";

    private Button mDelete;
    private Spinner mAlgorithms;
    private Spinner mNumPalettes;
    private Spinner mLightConditions;

    private Integer[] numOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mDelete = (Button)findViewById(R.id.button_delete_all);

        mAlgorithms = (Spinner)findViewById(R.id.spinner_algorithm);
        String[] algs = Palette.getAlgorithmChoices();
        ArrayAdapter<String> algorithmAdapter = new ArrayAdapter<String>(
                SettingsActivity.this,
                android.R.layout.simple_spinner_item,
                algs);
        algorithmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAlgorithms.setAdapter(algorithmAdapter);

        mNumPalettes = (Spinner)findViewById(R.id.spinner_num_palettes);
        numOptions = new Integer[20];
        for (int i = 0; i < numOptions.length; i++) {
            numOptions[i] = 5*(i+1);
        }
        ArrayAdapter<Integer> numAdapter = new ArrayAdapter<Integer>(
                SettingsActivity.this,
                android.R.layout.simple_spinner_item,
                numOptions);
        numAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mNumPalettes.setAdapter(numAdapter);

        mLightConditions = (Spinner)findViewById(R.id.spinner_light_conditions);
        String[] light = ImageProcessor.getLightOptions();
        ArrayAdapter<String> lightAdapter = new ArrayAdapter<String>(
                SettingsActivity.this,
                android.R.layout.simple_spinner_item,
                light);
        lightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLightConditions.setAdapter(lightAdapter);

        mDelete = (Button)findViewById(R.id.button_delete_all);
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a pop-up dialog!
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this,
                        AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                builder.setIcon(android.R.drawable.ic_dialog_alert);
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
                        // Completely clear database!

                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor editor = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE).edit();
        editor.putString(ALGORITHM_PREF, Palette.getAlgorithmChoices()[mAlgorithms.getSelectedItemPosition()]);
        editor.putInt(NUM_PAL_PREF, numOptions[mNumPalettes.getSelectedItemPosition()]);
        editor.putString(LIGHT_PREF, ImageProcessor.getLightOptions()[mLightConditions.getSelectedItemPosition()]);
        editor.commit();
    }

    public void deleteFavorites(View view) {
        // Handle the deletion of all favorites!
        // Warn the user in a dialog that this cannot be undone!

    }

}
