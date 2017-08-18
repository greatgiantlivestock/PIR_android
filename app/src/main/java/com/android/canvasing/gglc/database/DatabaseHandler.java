package com.android.canvasing.gglc.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "absen_mobile";

	//to define table name
	private static final String TABLE_MST_USER= "mst_user";
	private static final String TABLE_LAP_ABSEN= "mst_absen";
	private static final String TABLE_TRACKING_LOGS = "tracking_logs";

	//define field on ms_user
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_PH_NO = "phone_number";

	//define field on mst_absen
	private static final String KEY_ID_ABSEN = "id_absen";
	private static final String KEY_NAMA_KARYAWAN = "nama_karyawan";
	private static final String KEY_WAKTU = "waktu";
	private static final String KEY_LOKASI = "lokasi";

	//define Tracking logs field
	private static final String KEY_TRACKING_LOGS_ID_LOCATOR = "id_locator";
	private static final String KEY_TRACKING_LOGS_USERNAME = "username";
	private static final String KEY_TRACKING_LOGS_NAMA_LENGKAP = "nama_lengkap";
	private static final String KEY_TRACKING_LOGS_LEVEL = "level";
	private static final String KEY_TRACKING_LOGS_LATS = "lats";
	private static final String KEY_TRACKING_LOGS_LONGS = "longs";
	private static final String KEY_TRACKING_LOGS_ADDRESS = "address";
	private static final String KEY_TRACKING_LOGS_IMEI = "imei";
	private static final String KEY_TRACKING_LOGS_MCC = "mcc";
	private static final String KEY_TRACKING_LOGS_MNC = "mnc";
	private static final String KEY_TRACKING_LOGS_DATE = "date";
	private static final String KEY_TRACKING_LOGS_TIME = "time";

	//ArrayList table
	private final ArrayList<User> user_list = new ArrayList<User>();
	private final ArrayList<Absen> absen_list = new ArrayList<Absen>();
	private final ArrayList<TrackingLogs> tracking_logs_list = new ArrayList<TrackingLogs>();

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MST_USER + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_PH_NO + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);

		String CREATE_LAB_ABSEN_TABLE = "CREATE TABLE " + TABLE_LAP_ABSEN + "("
				+ KEY_ID_ABSEN + " INTEGER PRIMARY KEY," + KEY_NAMA_KARYAWAN + " TEXT,"
				+ KEY_WAKTU + " TEXT,"+ KEY_LOKASI + " TEXT" + ")";
		db.execSQL(CREATE_LAB_ABSEN_TABLE);

		String CREATE_TABLE_TRACKING_LOGS = "CREATE TABLE " + TABLE_TRACKING_LOGS + "("
				+ KEY_TRACKING_LOGS_ID_LOCATOR + " INTEGER PRIMARY KEY,"
				+ KEY_TRACKING_LOGS_USERNAME + " TEXT," + KEY_TRACKING_LOGS_NAMA_LENGKAP
				+ " TEXT," + KEY_TRACKING_LOGS_LEVEL + " INTEGER,"
				+ KEY_TRACKING_LOGS_LATS + " TEXT," + KEY_TRACKING_LOGS_LONGS + " TEXT,"
				+ KEY_TRACKING_LOGS_ADDRESS + " TEXT," + KEY_TRACKING_LOGS_IMEI
				+ " TEXT," + KEY_TRACKING_LOGS_MCC + " TEXT," + KEY_TRACKING_LOGS_MNC
				+ " TEXT," + KEY_TRACKING_LOGS_DATE + " TEXT," + KEY_TRACKING_LOGS_TIME
				+ " TEXT" + ")";
		db.execSQL(CREATE_TABLE_TRACKING_LOGS);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MST_USER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LAP_ABSEN);
		onCreate(db);
	}

	// Adding new contact
	public void addUser(User user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, user.getName());
		values.put(KEY_PH_NO, user.getPhoneNumber());

		// Inserting Row
		db.insert(TABLE_MST_USER, null, values);
		db.close(); // Closing database connection
	}

	//adding trakcing service
	public void add_TrackingLogs(TrackingLogs tracking) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TRACKING_LOGS_ID_LOCATOR, tracking.getId_locator());
		values.put(KEY_TRACKING_LOGS_USERNAME, tracking.getUsername());
		values.put(KEY_TRACKING_LOGS_NAMA_LENGKAP, tracking.getNama_lengkap());
		values.put(KEY_TRACKING_LOGS_LEVEL, tracking.getLevel());
		values.put(KEY_TRACKING_LOGS_LATS, tracking.getLats());
		values.put(KEY_TRACKING_LOGS_LONGS, tracking.getLongs());
		values.put(KEY_TRACKING_LOGS_ADDRESS, tracking.getAddress());
		values.put(KEY_TRACKING_LOGS_IMEI, tracking.getImei());
		values.put(KEY_TRACKING_LOGS_MCC, tracking.getMcc());
		values.put(KEY_TRACKING_LOGS_MNC, tracking.getMnc());
		values.put(KEY_TRACKING_LOGS_DATE, tracking.getDate());
		values.put(KEY_TRACKING_LOGS_TIME, tracking.getTime());
		// Inserting Row
		db.insert(TABLE_TRACKING_LOGS, null, values);

		db.close(); // Closing database connection
	}

	// Adding LAB absen
	public void addLabAbsen(Absen absen) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAMA_KARYAWAN, absen.getNama_karyawan());
		values.put(KEY_WAKTU, absen.getWaktu());
		values.put(KEY_LOKASI, absen.getLokasi());

		// Inserting Row
		db.insert(TABLE_LAP_ABSEN, null, values);
		db.close(); // Closing database connection
	}

	// Getting single contact
	User getContact(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_MST_USER, new String[] { KEY_ID,
				KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		User user = new User(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2));
		// return contact
		return user;
	}

	//getting All user
	public ArrayList<User> getAllUser() {
		try {
			user_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_MST_USER;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					User user = new User();
					user.setID(cursor.getInt(0));
					user.setName(cursor.getString(1));
					user.setPhoneNumber(cursor.getString(2));

					// Adding staff to list
					user_list.add(user);
				} while (cursor.moveToNext());
			}

			// return staff_list
			cursor.close();
			db.close();
			return user_list;
		} catch (Exception e) {
			Log.e("user_list", "" + e);
		}
		return user_list;
	}

	public ArrayList<TrackingLogs> getAllTrackingLogs() {
		try {
			tracking_logs_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_TRACKING_LOGS;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					TrackingLogs tracking = new TrackingLogs();
					tracking.setId_locator(cursor.getInt(0));
					tracking.setUsername(cursor.getString(1));
					tracking.setNama_lengkap(cursor.getString(2));
					tracking.setLevel(cursor.getInt(3));
					tracking.setLats(cursor.getString(4));
					tracking.setLongs(cursor.getString(5));
					tracking.setAddress(cursor.getString(6));
					tracking.setImei(cursor.getString(7));
					tracking.setMcc(cursor.getString(8));
					tracking.setMnc(cursor.getString(9));
					tracking.setDate(cursor.getString(10));
					tracking.setTime(cursor.getString(11));
					// Adding tracking_logs_list to list
					tracking_logs_list.add(tracking);
				} while (cursor.moveToNext());
			}

			// return tracking_logs_list
			cursor.close();
			db.close();
			return tracking_logs_list;
		} catch (Exception e) {
			Log.e("tracking_logs_list", "" + e);
		}

		return tracking_logs_list;
	}

	//getting All user
	public ArrayList<Absen> getAllAbsen() {
		try {
			absen_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_LAP_ABSEN;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Absen absen = new Absen();
					absen.setId_absen(cursor.getInt(0));
					absen.setNama_karyawan(cursor.getString(1));
					absen.setWaktu(cursor.getString(2));
					absen.setLokasi(cursor.getString(3));

					// Adding absen to list
					absen_list.add(absen);
				} while (cursor.moveToNext());
			}

			// return absen_list
			cursor.close();
			db.close();
			return absen_list;
		} catch (Exception e) {
			Log.e("absen_list", "" + e);
		}
		return absen_list;
	}

	// Updating single contact
	public int updateContact(User user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, user.getName());
		values.put(KEY_PH_NO, user.getPhoneNumber());

		// updating row
		return db.update(TABLE_MST_USER, values, KEY_ID + " = ?",
				new String[] { String.valueOf(user.getID()) });
	}

	// Deleting single contact
	public void deleteContact() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_MST_USER);
	}

	public int getCountUser() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db
				.rawQuery("select count(*) from " + TABLE_MST_USER, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public void deleteTableTrackingLogs() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_TRACKING_LOGS);
	}

	public int getCountTrackingLogs() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db
				.rawQuery("select count(*) from " + TABLE_TRACKING_LOGS, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountCustomer() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_LAP_ABSEN,
				null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	// Getting contacts Count
	public int getContactsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_MST_USER;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

	public void deleteTableCustomer() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_LAP_ABSEN);
	}

}
