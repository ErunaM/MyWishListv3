package dataV;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import dataV.constants;
import model.MyWish;

/**
 * Created by Dee on 26/02/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private final ArrayList<MyWish> wishList = new ArrayList<>();

    public DatabaseHandler(Context context) {
        super(context, constants.DATABASE_NAME, null, constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create our data Tables
        String CREATE_WISHES_TABLE = "CREATE TABLE " + constants.TABLE_NAME + "(" + constants.KEY_ID + " INTEGER PRIMARY KEY," + constants.TITLE_NAME
                + " TEXT, " + constants.CONTENT_NAME + " TEXT, " + constants.DATE_NAME + " LONG);";
        sqLiteDatabase.execSQL(CREATE_WISHES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + constants.TABLE_NAME);
        onCreate(sqLiteDatabase);


    }
    //Delete a wish
    public void deleteWish(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(constants.TABLE_NAME, constants.KEY_ID + " = ? ", new String[]{String.valueOf(id)});
        db.close();
    }
    //add Content to table
    public void addWishes(MyWish wish)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues(); // kind of like a hasmap with key value pairs
        values.put(constants.TITLE_NAME, wish.getTitle());
        values.put(constants.CONTENT_NAME, wish.getContent());
        values.put(constants.DATE_NAME, java.lang.System.currentTimeMillis());

        db.insert(constants.TABLE_NAME, null, values);
        Log.v("Sucess!!!!!!", "Yeah!!");
        db.close();
    }
    //Get all Wishes
    public ArrayList<MyWish> getWishes(){
        String selectQuery = "SELECT * FROM " + constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(constants.TABLE_NAME, new String[]{constants.KEY_ID, constants.TITLE_NAME, constants.CONTENT_NAME, constants.DATE_NAME
        },null,null,null,null, constants.DATE_NAME + " DESC "); // we want our cursor to point to our table name, inside name we want it to fetch key name name date, we also want it to be orderd
        //by decending order.
        // loop through cursor
        if(cursor.moveToFirst())
        {
            do{
                MyWish wish = new MyWish();
                wish.setTitle(cursor.getString(cursor.getColumnIndex(constants.TITLE_NAME)));
                wish.setContent(cursor.getString(cursor.getColumnIndex(constants.CONTENT_NAME)));
                wish.setItemId(cursor.getInt(cursor.getColumnIndex(constants.KEY_ID)));

                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance(); // formats our date
                String dateData = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(constants.DATE_NAME))).getTime());

                wish.setRecordDate(dateData);
                wishList.add(wish);
            }while(cursor.moveToNext());
        }

        return wishList;
    }
}
