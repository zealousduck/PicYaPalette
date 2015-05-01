package edu.auburn.eng.csse.comp3710.team8;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class FavoritePalettesActivity extends Activity {

    private PaletteStorageHelper psh;

    private ListView mList;
    private PaletteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_palettes);

        psh = new PaletteStorageHelper(FavoritePalettesActivity.this);

        mList = (ListView)findViewById(R.id.list_favorited);
        mAdapter = new PaletteAdapter(FavoritePalettesActivity.this, psh.getAllPalettes());
        mList.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        mAdapter = new PaletteAdapter(FavoritePalettesActivity.this, psh.getAllPalettes());
        mList.setAdapter(mAdapter);
    }

}
