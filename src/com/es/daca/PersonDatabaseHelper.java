package com.es.daca;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Class PersonDatabaseHelper<br>
 * PersonDatabaseHelper manage all about database SQLite: insert, delete and get information
 *
 * @author  Carlos Martínez Wahnon
 */
public class PersonDatabaseHelper {
	private static final String TAG = PersonDatabaseHelper.class.getSimpleName();
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "dacadatabase.db";
	private static final String TABLE_NAME = "borrowing_table";        
	private static final String PERSON_TABLE_COLUMN_ID = "_id";  
	private static final String PERSON_TABLE_COLUMN_NAME = "person_name";
	private static final String PERSON_TABLE_COLUMN_OBJECT = "person_object";
	private static final String YEAR0 = "year0";
	private static final String MONTH0 = "month0";
	private static final String DAY0 = "day0";
	private static final String YEAR = "year";
	private static final String MONTH = "month";
	private static final String DAY = "day";
	private static final String LATITUDE = "latitude";
	private static final String LONGITUDE = "longitude";
	private DatabaseOpenHelper openHelper;
	private SQLiteDatabase database;

	public PersonDatabaseHelper(Context aContext) {
		openHelper = new DatabaseOpenHelper(aContext);
		database = openHelper.getWritableDatabase();
	}

	public void insertData (String aPersonName, String aPersonOBJECT, int year0, int month0, int day0, int year, int month, int day, double latitude, double longitude) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(PERSON_TABLE_COLUMN_NAME, aPersonName);
		contentValues.put(PERSON_TABLE_COLUMN_OBJECT, aPersonOBJECT);
		contentValues.put(YEAR0, year0);
		contentValues.put(MONTH0, month0);
		contentValues.put(DAY0, day0);
		contentValues.put(YEAR, year);
		contentValues.put(MONTH, month);
		contentValues.put(DAY, day);
		contentValues.put(LATITUDE, latitude);
		contentValues.put(LONGITUDE, longitude);
		database.insert(TABLE_NAME, null, contentValues);
	}

	public void deleteData(long id)	{
		database.delete(TABLE_NAME, "_id" + "=" + id, null);
	}

	public String getName(long id)	{
		String column_cat = "person_name";	
		String queryname = "SELECT " + column_cat  + " FROM " + TABLE_NAME + " WHERE _id = '" + id + "'";
		Cursor cursorName = database.rawQuery(queryname, null);
		if(cursorName.moveToFirst())
			return cursorName.getString(cursorName.getColumnIndex(column_cat));
		return "";
	}

	public String getObject(long id)	{
		String column_cat = "person_object";	
		String queryname = "SELECT " + column_cat  + " FROM " + TABLE_NAME + " WHERE _id = '" + id + "'";
		Cursor cursorName = database.rawQuery(queryname, null);
		if(cursorName.moveToFirst())
			return cursorName.getString(cursorName.getColumnIndex(column_cat));
		return "";
	}

	public double getLatitude(long id)	{
		String column_cat = "latitude";	
		String queryname = "SELECT " + column_cat  + " FROM " + TABLE_NAME + " WHERE _id = '" + id + "'";
		Cursor cursorName = database.rawQuery(queryname, null);
		if(cursorName.moveToFirst())
			return cursorName.getDouble(cursorName.getColumnIndex(column_cat));
		return (Double) null;
	}

	public double getLongitude(long id)	{
		String column_cat = "longitude";	
		String queryname = "SELECT " + column_cat  + " FROM " + TABLE_NAME + " WHERE _id = '" + id + "'";
		Cursor cursorName = database.rawQuery(queryname, null);
		if(cursorName.moveToFirst())
			return cursorName.getDouble(cursorName.getColumnIndex(column_cat));
		return (Double) null;
	}

	public Cursor getAllData () {
		String buildSQL = "SELECT * FROM " + TABLE_NAME;
		Log.d(TAG, "getAllData SQL: " + buildSQL);
		return database.rawQuery(buildSQL, null);
	}

	/**
	 * Class DatabaseOpenHelper<br>
	 * DatabaseOpenHelper create database SQLite and table information 
	 *
	 * @author  Carlos Martínez Wahnon
	 */
	private class DatabaseOpenHelper extends SQLiteOpenHelper {

		public DatabaseOpenHelper(Context aContext) {
			super(aContext, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase sqLiteDatabase) {
			String buildSQL = "CREATE TABLE " + TABLE_NAME + "( " + 
					PERSON_TABLE_COLUMN_ID + " INTEGER PRIMARY KEY, " +
					PERSON_TABLE_COLUMN_NAME + " TEXT, " + 
					PERSON_TABLE_COLUMN_OBJECT + " TEXT, " +
					YEAR0 + " INTEGER, " + 
					MONTH0 + " INTEGER, " +
					DAY0 + " INTEGER, " +
					YEAR + " INTEGER, " +
					MONTH + " INTEGER, " +
					DAY + " INTEGER, " +
					LATITUDE + " REAL, " +
					LONGITUDE + " REAL )";
			Log.d(TAG, "onCreate SQL: " + buildSQL);
			sqLiteDatabase.execSQL(buildSQL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
			String buildSQL = "DROP TABLE IF EXISTS " + TABLE_NAME;
			Log.d(TAG, "onUpgrade SQL: " + buildSQL);
			sqLiteDatabase.execSQL(buildSQL);
			onCreate(sqLiteDatabase);              
		}
	}
}

