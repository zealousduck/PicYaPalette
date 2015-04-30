package edu.auburn.eng.csse.comp3710.team8;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Sam on 4/25/15.
 */
public class DBManager extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "YaSavedPalettes.db";
    public static final String TABLE_PALETTES = "palettes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_COLORS = "colors";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ALGORITHM = "algorithm";
    private static final int DATABASE_VERSION = 4;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_PALETTES + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null, " + COLUMN_COLORS + " text not null, " + COLUMN_ALGORITHM + " text not null);";

    public DBManager(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("DBManager",
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PALETTES);
        onCreate(db);

    }
}
