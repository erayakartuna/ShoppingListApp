package com.erayakartuna.alisverislistesi;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.R.attr.id;
import static android.os.Build.ID;

public class Database extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "shop_list_database";

    private static final String TABLE_NAME = "shop_list";
    private static final String ITEM_TABLE_NAME = "shop_list_items";
    private static String SHOP_ID = "shop_id";
    private static String SHOP_TITLE = "title";
    private static String ITEM_ID = "item_id";
    private static String ITEMS_LIST_ID = "list_no";
    private static String ITEM_TITLE = "title";
    private static String ITEM_CHECKED = "checked";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + SHOP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SHOP_TITLE + " TEXT)";
        db.execSQL(CREATE_TABLE);

        String CREATE_ITEM_TABLE = "CREATE TABLE " + ITEM_TABLE_NAME + "("
                + ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ITEM_CHECKED + " INTEGER,"
                + ITEMS_LIST_ID + " INTEGER,"
                + ITEM_TITLE + " TEXT)";
        db.execSQL(CREATE_ITEM_TABLE);
    }

    /**
     * Delete List Method
     *
     * @param id
     */

    public void removeList(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, SHOP_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    /**
     * Create List Method
     *
     * @param shop_title
     */

    public void addList(String shop_title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SHOP_TITLE, shop_title);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    /**
     *
     * @param id
     */

    public void removeItem(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ITEM_TABLE_NAME, ITEM_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    /**
     *
     * @param item_title
     * @param list_id
     */

    public void addItem(String item_title,int list_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ITEM_TITLE, item_title);
        values.put(ITEMS_LIST_ID, Integer.toString(list_id));
        values.put(ITEM_CHECKED, "0");

        db.insert(ITEM_TABLE_NAME, null, values);

        db.close();
    }

    public void updateItem(int id,int checked)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ITEM_CHECKED, checked);
        // updating row
        db.update(ITEM_TABLE_NAME, values, ITEM_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    public  ArrayList<HashMap<String, String>> lists(){


        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " order by "+SHOP_ID+" DESC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> shopList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                shopList.add(map);
            } while (cursor.moveToNext());
        }
        db.close();

        return shopList;
    }

    public  ArrayList<HashMap<String, String>> items(int id){


        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + ITEM_TABLE_NAME + " where "+ ITEMS_LIST_ID + " = " + id + " order by "+ ITEM_ID +" DESC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> shopList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                shopList.add(map);
            } while (cursor.moveToNext());
        }
        db.close();

        return shopList;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); //5
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE_NAME); //5

        onCreate(db); //6
    }

}
