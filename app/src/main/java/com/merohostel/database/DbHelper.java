package com.merohostel.database;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.merohostel.Constants;

import java.util.ArrayList;

/**
 * Created by asimsangram on 2/5/16.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String HOSTEL_CREATE_ENTRIES =
            "CREATE TABLE " + HostelEntry.TABLE_NAME + " (" +
                    HostelEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY" + Constants.COMMA_SEP +
                    HostelEntry.COLUMN_NAME_HID + Constants.INTEGER_TYPE + Constants.COMMA_SEP +
                    HostelEntry.COLUMN_NAME_HNAME + Constants.TEXT_TYPE + Constants.COMMA_SEP +
                    HostelEntry.COLUMN_NAME_GENDER + Constants.TEXT_TYPE + Constants.COMMA_SEP +
                    HostelEntry.COLUMN_NAME_LOCATION + Constants.TEXT_TYPE + " )";

    private static final String HOSTEL_PHOTO_CREATE_ENTRIES =
            "CREATE TABLE " + HostelPhotoEntry.TABLE_NAME + " (" +
                    HostelPhotoEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY" + Constants.COMMA_SEP +
                    HostelPhotoEntry.COLUMN_NAME_HID + Constants.INTEGER_TYPE + Constants.COMMA_SEP +
                    HostelPhotoEntry.COLUMN_NAME_PID + Constants.INTEGER_TYPE + " )";

    private static final String PHOTO_CREATE_ENTRIES =
            "CREATE TABLE " + PhotoEntry.TABLE_NAME + " (" +
                    PhotoEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY" + Constants.COMMA_SEP +
                    PhotoEntry.COLUMN_NAME_PHOTO_URL + Constants.TEXT_TYPE + Constants.COMMA_SEP +
                    PhotoEntry.COLUMN_NAME_MAIN + Constants.INTEGER_TYPE + " )";

    private static final String HOSTEL_FEE_CREATE_ENTRIES =
            "CREATE TABLE " + HostelFeeEntry.TABLE_NAME + " (" +
                    HostelFeeEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY" + Constants.COMMA_SEP +
                    HostelFeeEntry.COLUMN_NAME_HID + Constants.INTEGER_TYPE + Constants.COMMA_SEP +
                    HostelFeeEntry.COLUMN_NAME_ADMISSION + Constants.INTEGER_TYPE + Constants.COMMA_SEP +
                    HostelFeeEntry.COLUMN_NAME_SEC_DEPOSIT + Constants.INTEGER_TYPE + Constants.COMMA_SEP +
                    HostelFeeEntry.COLUMN_NAME_ONE_BED + Constants.INTEGER_TYPE + Constants.COMMA_SEP +
                    HostelFeeEntry.COLUMN_NAME_TWO_BED + Constants.INTEGER_TYPE + Constants.COMMA_SEP +
                    HostelFeeEntry.COLUMN_NAME_THREE_BED + Constants.INTEGER_TYPE + Constants.COMMA_SEP +
                    HostelFeeEntry.COLUMN_NAME_FOUR_BED + Constants.INTEGER_TYPE + " )";

    private static final String HOSTEL_FACILITY_CREATE_ENTRIES =
            "CREATE TABLE " + HostelFacilityEntry.TABLE_NAME + " (" +
                    HostelFacilityEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY" + Constants.COMMA_SEP +
                    HostelFacilityEntry.COLUMN_NAME_HID + Constants.INTEGER_TYPE + Constants.COMMA_SEP +
                    HostelFacilityEntry.COLUMN_NAME_FACILITY + Constants.TEXT_TYPE + " ) ";

    private static final String HOSTEL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + HostelEntry.TABLE_NAME;

    private static final String HOSTEL_PHOTO_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + HostelPhotoEntry.TABLE_NAME;

    private static final String PHOTO_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PhotoEntry.TABLE_NAME;

    private static final String HOSTEL_FEE_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + HostelFeeEntry.TABLE_NAME;

    private static final String HOSTEL_FACILITY_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + HostelFacilityEntry.TABLE_NAME;

    public DbHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(HOSTEL_CREATE_ENTRIES);
        sqLiteDatabase.execSQL(HOSTEL_PHOTO_CREATE_ENTRIES);
        sqLiteDatabase.execSQL(PHOTO_CREATE_ENTRIES);
        sqLiteDatabase.execSQL(HOSTEL_FEE_CREATE_ENTRIES);
        sqLiteDatabase.execSQL(HOSTEL_FACILITY_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(HOSTEL_DELETE_ENTRIES);
        sqLiteDatabase.execSQL(HOSTEL_PHOTO_DELETE_ENTRIES);
        sqLiteDatabase.execSQL(PHOTO_DELETE_ENTRIES);
        sqLiteDatabase.execSQL(HOSTEL_FEE_DELETE_ENTRIES);
        sqLiteDatabase.execSQL(HOSTEL_FACILITY_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public ArrayList<Cursor> getData(String Query){
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            Cursor c = sqlDB.rawQuery(Query, null);
            Cursor2.addRow(new Object[] { "Success" });
            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();
                return alc ;
            }
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }
}
