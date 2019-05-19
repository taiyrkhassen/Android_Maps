package com.example.mapsgoogle2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.security.PublicKey;
import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "places.db";
    public static final String DATABASE_TABLE = "places";

    public static final String ID = "id";
    public static final String PLACE_LOCATION = "loc";
    public static final String PLACE_TITLE = "title";
    public static final String LONGTITUDE = "long";
    public static final String LANGTITUDE = "lang";



    public DataBaseHelper(@Nullable Context context) {
        super(context,DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DATABASE_TABLE + "(" +
                ID + " integer primary key autoincrement, "+PLACE_LOCATION+" text not null, "+ PLACE_TITLE + " " +
                "text not null, "+ LONGTITUDE + " double not null, "+LANGTITUDE + " double not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+DATABASE_TABLE);
        onCreate(db);
    }

    public void insertNote(Place pl){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PLACE_LOCATION,pl.getName());
        cv.put(PLACE_TITLE,pl.getDescr());
        cv.put(LONGTITUDE,pl.getLatlng().longitude);
        cv.put(LANGTITUDE,pl.getLatlng().latitude);
        db.insert(DATABASE_TABLE,null,cv);

    }
    public ArrayList<Place> getNotes() {
        ArrayList<Place> places = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String get_places = "select * from "+DATABASE_TABLE;
        Cursor c = db.rawQuery(get_places,null);
        for (c.moveToFirst();!c.isAfterLast();c.moveToNext()){
            Place place = new Place();
            place.name = c.getString(c.getColumnIndex(PLACE_LOCATION));
            place.descr = c.getString(c.getColumnIndex(PLACE_TITLE));
            place.latlng = new LatLng(c.getDouble(c.getColumnIndex(LANGTITUDE)),c.getDouble(c.getColumnIndex(LONGTITUDE)));
            places.add(place);
        }
        return places;
    }
    public void drop(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + DATABASE_TABLE );
    }

}
