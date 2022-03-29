package com.example.georgepromax;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {


    private static final String PLAYER_TABLE = "PLAYER_TABLE";
    private static final String COLUMN_PLAYER_NAME = "PLAYER_NAME";
    private static final String COLUMN_PLAYER_PASSWORD = "PLAYER_PASSWORD";
    private static final String COLUMN_PLAYER_PHONE = "PLAYER_PHONE";
    private static final String COLUMN_PLAYER_EMAIL = "PLAYER_EMAIL";
    private static final String COLUMN_ID = "ID";

    public String getPlayerTable() {
        return PLAYER_TABLE;
    }

    public String getColumnPlayerName() {
        return COLUMN_PLAYER_NAME;
    }

    public String getColumnPlayerPassword() {
        return COLUMN_PLAYER_PASSWORD;
    }

    public String getColumnPlayerPhone() {
        return COLUMN_PLAYER_PHONE;
    }

    public String getColumnPlayerEmail() {
        return COLUMN_PLAYER_EMAIL;
    }

    public String getColumnId() {
        return COLUMN_ID;
    }

    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }



    /*public DataBaseHelper(@Nullable Class<MainActivity> context) {
        super(context, "player.db", null, 1);
    }*/

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + PLAYER_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PLAYER_NAME + " TEXT, " + COLUMN_PLAYER_PASSWORD + " TEXT , "
                + COLUMN_PLAYER_PHONE +" TEXT , " + COLUMN_PLAYER_EMAIL +" TEXT)";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne(PlayerModel playerModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PLAYER_NAME, playerModel.getUserName());
        cv.put(COLUMN_PLAYER_PASSWORD, playerModel.getPassword());
        cv.put(COLUMN_PLAYER_PHONE, playerModel.getPhoneNumber());
        cv.put(COLUMN_PLAYER_EMAIL, playerModel.getEmail());

        long insert = db.insert(PLAYER_TABLE, null, cv);
        if(insert==-1)
            return false;
        else
            return true;
    }
}
