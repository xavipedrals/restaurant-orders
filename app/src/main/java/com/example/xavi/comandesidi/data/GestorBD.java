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

import java.beans.PropertyChangeSupport;
import java.util.Date;

public class GestorBD extends SQLiteOpenHelper {

    private static GestorBD instance;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Dades";

    //PLATS
    public static final String PLATS_TABLE ="Plats";
    public static final String PLATS_COL_NAME ="name";
    public static final String PLATS_COL_PRICE ="price";
    public static final String PLATS_COL_IMG ="img";
    public static final String PLATS_COL_STOCK ="stoc";

    public static final String PLATS_TABLE_CREATE = "CREATE TABLE " + PLATS_TABLE +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PLATS_COL_NAME + " TEXT, " +
            PLATS_COL_PRICE + " REAL, " +
            PLATS_COL_STOCK + " INTEGER, " +
            PLATS_COL_IMG + " INTEGER);";

    public static final String PLATS_TABLE_RESET = "DELETE FROM " + PLATS_TABLE;

    //COMANDES
    public static final String COMANDES_TABLE ="Comandes";
    public static final String COMANDES_COL_DATA ="date";
    public static final String COMANDES_COL_PRICE ="price";
    public static final String COMANDES_COL_NUM_TABLE ="numtaula";

    public static final String COMANDES_TABLE_CREATE = "CREATE TABLE " + COMANDES_TABLE +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COMANDES_COL_NUM_TABLE + " INTEGER, " +
            COMANDES_COL_DATA + " TEXT, " +
            COMANDES_COL_PRICE + " REAL);";

    public static final String COMANDES_TABLE_RESET = "DELETE FROM " + COMANDES_TABLE;

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
        db.execSQL(COMANDES_TABLE_CREATE);
    }

//    public void insertPlat (int img, double price, String name){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(PLATS_COL_NAME, name);
//        contentValues.put(PLATS_COL_PRICE, price);
//        contentValues.put(PLATS_COL_IMG, img);
//        contentValues.put(PLATS_COL_STOCK, 100);
//        db.insert(PLATS_TABLE, null, contentValues);
//    }

    public void insertPlat (int img, double price, String name, int stoc){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLATS_COL_NAME, name);
        contentValues.put(PLATS_COL_PRICE, price);
        contentValues.put(PLATS_COL_IMG, img);
        contentValues.put(PLATS_COL_STOCK, stoc);
        db.insert(PLATS_TABLE, null, contentValues);
    }

    public void updatePlat(int mipmapId, double price, String name, String legacyName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLATS_COL_PRICE, price);
        contentValues.put(PLATS_COL_NAME, name);
        contentValues.put(PLATS_COL_IMG, mipmapId);
        Log.d("DADES", "Vaig a fer un update");
        db.update(PLATS_TABLE, contentValues, PLATS_COL_NAME + "=?", new String[]{legacyName});
    }

    public void deletePlat (String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = PLATS_COL_NAME + "=?";
        String[] whereArgs = new String[] { name };
        db.delete(PLATS_TABLE, whereClause, whereArgs);
    }

    public Cursor getAllPlats (){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {PLATS_COL_NAME, PLATS_COL_PRICE, PLATS_COL_STOCK, PLATS_COL_IMG};
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

    public void insertComanda(double price, String date, int numTaula){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COMANDES_COL_PRICE, price);
        contentValues.put(COMANDES_COL_DATA, date);
        contentValues.put(COMANDES_COL_NUM_TABLE, numTaula);
        db.insert(COMANDES_TABLE, null, contentValues);
    }

    public void resetTablePlats(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(PLATS_TABLE_RESET);
    }

    public void resetTableComandes(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(COMANDES_TABLE_RESET);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PLATS_TABLE);
        onCreate(db);
    }
}
