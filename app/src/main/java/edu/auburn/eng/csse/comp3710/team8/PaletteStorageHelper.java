package edu.auburn.eng.csse.comp3710.team8;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.SQLException;
import android.content.ContentValues;
import android.util.Log;

/**
 * NOTES: Okay so I really don't have any good suggestions for exactly how this class will
 * be implemented. I'm starting with static methods so that we can access the data
 * without needing to instantiate the Helper -- this COULD be a terrible way to do it though.
 *
 * It is a kind of bad way to do it - simply because it's hard to initialize the db - but I will
 * create simple methods to be called to set up the databases on initialization.
 */
public class PaletteStorageHelper {

    private SQLiteDatabase database;
    private DBManager dBManager;
    private String[] allColumns = { DBManager.COLUMN_ID, DBManager.COLUMN_NAME, DBManager.COLUMN_COLORS, DBManager.COLUMN_ALGORITHM};
    public static final int SUCCESS = 0;
    public static final int DUPLICATE = 1;
    public static final int FAILURE = 2;

    public PaletteStorageHelper(Context context) {
        dBManager = new DBManager(context);
        open();
    }

    public void open() throws SQLException {
        database = dBManager.getWritableDatabase();
    }

    public void close() {
        dBManager.close();
    }

    /*
     * Saves the palette and returns a code
     */
    public int save(Palette p) {
        ContentValues values = new ContentValues();
        values.put(DBManager.COLUMN_NAME, p.getName());
        String temp = colorsToString(p);
        if(!isSaved(p)) {
            values.put(DBManager.COLUMN_COLORS, temp);
            values.put(DBManager.COLUMN_ALGORITHM, p.getAlgorithmUsed());
            Long insertId = database.insert(DBManager.TABLE_PALETTES, null, values);
            if (insertId != null) {
                return SUCCESS;
            }
            else return FAILURE;
        }
        return DUPLICATE;
    }

    /*
     * Removes the palette from the database.
     */
    public boolean remove(Palette p) {
        String temp = colorsToString(p);
        String[] args = {temp};
        database.delete(DBManager.TABLE_PALETTES, DBManager.COLUMN_COLORS + "=?", args);
        return true; // Return true for success
    }


    public boolean removeAll() {
        database.delete(DBManager.TABLE_PALETTES, null, null);
        Palette[] all = getAllPalettes();
        Log.i("DEBUG", Integer.toString(all.length) + " left");
        return true; // Return true for success
    }

    /* While not strictly necessary, this could be useful for checking if we're trying to save
     * an identical palette / seeing if a palette has already been saved.
     */
    public boolean isSaved(Palette p) {
        String temp = colorsToString(p);
        Cursor cursor = database.rawQuery("SELECT " + DBManager.COLUMN_COLORS
                + " FROM " + DBManager.TABLE_PALETTES + " WHERE "
                + DBManager.COLUMN_COLORS + " = '" + temp + "'", null);
        if(cursor.getCount() > 0) {
            return true; // Return true for success
        }
        return false;

    }

    /* TOP-TIER IMPORTANCE: PaletteAdapter REQUIRES a Palette[] in order to function,
     * and I'll be giving a PaletteAdapter to FavoritePalettesActivity to populate
     * the ListView.
     */
    public Palette[] getAllPalettes() {
        Cursor cursor = database.query(DBManager.TABLE_PALETTES,
                allColumns, null, null, null, null, null);
        Palette[] result = new Palette[cursor.getCount()];
        cursor.moveToFirst();
        int i = 0;
        while (!cursor.isAfterLast() && i < result.length) {
            Palette p = cursorToPalette(cursor);
            result[i] = p;
            i++;
            cursor.moveToNext();
        }
        return result;
    }

    private String colorsToString(Palette p) {
        String ret = "";
        for(int i = 0; i < p.getColors().length; i++) {
            ret += Integer.toString(p.getColors()[i]);
            if(i < p.getColors().length - 1) ret += ",";
        }
        return ret;
    }

    private Palette cursorToPalette(Cursor cursor) {
        String[] colorTemp = cursor.getString(2).split(",");
        int[] colors = new int[colorTemp.length];
        for(int i = 0; i < colorTemp.length; i++) colors[i] = Integer.parseInt(colorTemp[i]);
        String name = cursor.getString(1);
        Palette p = new Palette(colors, cursor.getString(3));
        p.setName(name);
        return p;
    }

}
