package com.example.xavi.comandesidi.data;

/**
 * Created by xavi on 06/12/15.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GestorBD extends SQLiteOpenHelper {

    private static GestorBD instance;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Dades";

    public static final String PLATS_TABLE ="Plats";
    public static final String PLATS_COL_NAME ="name";
    public static final String PLATS_COL_PRICE ="price";
    public static final String PLATS_COL_IMG ="img";

    public static final String PLATS_TABLE_CREATE = "CREATE TABLE " + PLATS_TABLE +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PLATS_COL_NAME + " TEXT, " +
            PLATS_COL_PRICE + " REAL, " +
            PLATS_COL_IMG + " INTEGER);";

    public static final String PLATS_TABLE_RESET = "DELETE FROM " + PLATS_TABLE;

    public GestorBD(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized GestorBD getInstance(Context context){
        if (instance == null){
            instance = new GestorBD(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PLATS_TABLE_CREATE);
    }

    public void insertPlat (int img, double price, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLATS_COL_NAME, name);
        contentValues.put(PLATS_COL_PRICE, price);
        contentValues.put(PLATS_COL_IMG, img);
        db.insert(PLATS_TABLE, null, contentValues);
    }

    public Cursor getAllPlats (){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {PLATS_COL_NAME, PLATS_COL_PRICE, PLATS_COL_IMG};
        Cursor c = db.query(
                PLATS_TABLE,                            // The table to query
                columns,                                // The columns to return
                null,                                   // The columns for the WHERE clause
                null,                                   // The values for the WHERE clause
                null,                                   // don't group the rows
                null,                                   // don't filter by row groups
                null                                    // The sort order
        );
        return c;
    }

    public void resetTablePlats(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(PLATS_TABLE_RESET);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PLATS_TABLE);
        onCreate(db);
    }
}
