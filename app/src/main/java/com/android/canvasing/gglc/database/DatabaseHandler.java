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
	private static final String TABLE_DETAIL_RENCANA = "trx_rencana_detail";
	private static final String TABLE_KEGIATAN = "mst_kegiatan";
	private static final String TABLE_MST_CUSTOMER = "mst_customer";
	private static final String TABLE_MST_USER1 = "mst_user1";
	private static final String TABLE_TMP_CUSTOMER = "tmp_customer";
	private static final String TABLE_TRX_CHECKIN = "trx_checkin";
	private static final String TABLE_TRX_CHECKOUT = "trx_checkout";

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

	//define DetailRencana field
	private static final String KEY_DETAIL_RENCANA_ID_RENCANA_DETAIL = "id_rencana_detail";
	private static final String KEY_DETAIL_RENCANA_ID_RENCANA_HEADER = "id_rencana_header";
	private static final String KEY_DETAIL_RENCANA_ID_KEGIATAN = "id_kegiatan";
	private static final String KEY_DETAIL_RENCANA_ID_CUSTOMER = "id_customer";
	private static final String KEY_DETAIL_RENCANA_ID_KARYAWAN = "id_karyawan";
	private static final String KEY_DETAIL_RENCANA_STATUS_RENCANA = "status_rencana";

	//define KEGIATAN field
	private static final String KEY_KEGIATAN_ID_KEGIATAN ="id_kegiatan";
	private static final String KEY_KEGIATAN_NAMA_KEGIATAN ="nama_kegiatan";
	private static final String KEY_KEGIATAN_ID_DEPARTEMEN ="nama_departemen";
	private static final String KEY_KEGIATAN_ID_WILAYAH ="id_wilayah";

	//define mst_customer field
	private static final String KEY_MST_CUSTOMER_ID_CUSTOMER ="id_customer";
	private static final String KEY_MST_CUSTOMER_KODE_CUSTOMER ="kode_customer";
	private static final String KEY_MST_CUSTOMER_NAMA_CUSTOMER ="nama_customer";
	private static final String KEY_MST_CUSTOMER_ALAMAT ="alamat";
	private static final String KEY_MST_CUSTOMER_NO_HP ="no_hp";
	private static final String KEY_MST_CUSTOMER_LATS ="lats";
	private static final String KEY_MST_CUSTOMER_LONGS ="longs";
	private static final String KEY_MST_CUSTOMER_ID_WILAYAH ="id_wilayah";

	//define mst_user field
	private static final String KEY_MST_USER_ID_USER ="id_user";
	private static final String KEY_MST_USER_NAMA ="nama";
	private static final String KEY_MST_USER_USERNAME ="username";
	private static final String KEY_MST_USER_PASSWORD ="password";
	private static final String KEY_MST_USER_ID_DEPARTEMEN ="id_departemen";
	private static final String KEY_MST_USER_ID_WILAYAH ="id_wilayah";
	private static final String KEY_MST_USER_ID_KARYAWAN ="id_karyawan";
	private static final String KEY_MST_USER_HAK_AKSES ="hak_akses";
	private static final String KEY_MST_USER_NO_HP ="no_hp";
	private static final String KEY_MST_USER_ID_ROLE ="id_role";

	//define tmp_customer field
	private static final String KEY_TMP_CUSTOMER_ID_CUSTOMER_TMP ="id_customer_tmp";
	private static final String KEY_TMP_CUSTOMER_KODE_CUSTOMER ="kode_customer";
	private static final String KEY_TMP_CUSTOMER_NAMA_CUSTOMER ="nama_customer";
	private static final String KEY_TMP_CUSTOMER_NO_HP ="no_hp";
	private static final String KEY_TMP_CUSTOMER_ALAMAT ="alamat";
	private static final String KEY_TMP_CUSTOMER_NAMA_USAHA ="nama_usaha";

	//define trx_checkin field
	private static final String KEY_TRX_CHECKIN_ID_CHECKIN ="id_chekin";
	private static final String KEY_TRX_CHECKIN_TANGGAL_CHECKIN ="tanggal_checkin";
	private static final String KEY_TRX_CHECKIN_NOMOR_CHECKIN ="nomor_checkin";
	private static final String KEY_TRX_CHECKIN_ID_USER ="id_user";
	private static final String KEY_TRX_CHECKIN_ID_RENCANA_DETAIL ="id_rencana_detail";
	private static final String KEY_TRX_CHECKIN_KODE_CUSTOMER ="kode_customer";
	private static final String KEY_TRX_CHECKIN_LATS ="lats";
	private static final String KEY_TRX_CHECKIN_LONGS ="longs";
	private static final String KEY_TRX_CHECKIN_FOTO ="foto";

	//define trx_checkout field
	private static final String KEY_TRX_CHECKOUT_ID_CHECKOUT ="id_chekout";
	private static final String KEY_TRX_CHECKOUT_ID_CHECKIN ="id_checkin";
	private static final String KEY_TRX_CHECKOUT_TANGGAL_CHECKOUT ="tanggal_checkout";
	private static final String KEY_TRX_CHECKOUT_ID_USER ="id_user";
	private static final String KEY_TRX_CHECKOUT_REALISASI_KEGIATAN ="realisasi_kegiatan";
	private static final String KEY_TRX_CHECKOUT_LATS ="lats";
	private static final String KEY_TRX_CHECKOUT_LONGS ="longs";

	//ArrayList table
	private final ArrayList<User> user_list = new ArrayList<User>();
	private final ArrayList<Absen> absen_list = new ArrayList<Absen>();
	private final ArrayList<TrackingLogs> tracking_logs_list = new ArrayList<TrackingLogs>();
	private final ArrayList<DetailRencana> detailRencanaArraylist = new ArrayList<DetailRencana>();
	private final ArrayList<Kegiatan> kegiatanArrayList = new ArrayList<Kegiatan>();
	private final ArrayList<Mst_Customer> mst_customerArrayList = new ArrayList<Mst_Customer>();
	private final ArrayList<MstUser> mst_userArrayList =new ArrayList<MstUser>();
	private final ArrayList<TmpCustomer> tmpCustomerArrayList =new ArrayList<TmpCustomer>();
	private final ArrayList<Trx_Checkin> trx_checkinArrayList =new ArrayList<Trx_Checkin>();
	private final ArrayList<Trx_Checkout> trx_checkoutArrayList =new ArrayList<Trx_Checkout>();

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

		String CREATE_TABLE_DETAIL_RENCANA = "CREATE TABLE " + TABLE_DETAIL_RENCANA + "("
				+ KEY_DETAIL_RENCANA_ID_RENCANA_DETAIL + " INTEGER PRIMARY KEY," + KEY_DETAIL_RENCANA_ID_RENCANA_HEADER + " TEXT,"
				+ KEY_DETAIL_RENCANA_ID_KEGIATAN + " TEXT," + KEY_DETAIL_RENCANA_ID_CUSTOMER + " TEXT,"
				+ KEY_DETAIL_RENCANA_ID_KARYAWAN + " TEXT," + KEY_DETAIL_RENCANA_STATUS_RENCANA + " TEXT" + ")";
		db.execSQL(CREATE_TABLE_DETAIL_RENCANA);

		String CREATE_TABLE_KEGIATAN = "CREATE TABLE " + TABLE_KEGIATAN + "("
				+ KEY_KEGIATAN_ID_KEGIATAN + " INTEGER PRIMARY KEY," + KEY_KEGIATAN_NAMA_KEGIATAN + " TEXT,"
				+ KEY_KEGIATAN_ID_DEPARTEMEN + " TEXT," + KEY_KEGIATAN_ID_WILAYAH + " TEXT" + ")";
		db.execSQL(CREATE_TABLE_KEGIATAN);

		String CREATE_TABLE_MST_CUSTOMER = "CREATE TABLE " + TABLE_MST_CUSTOMER + "("
				+ KEY_MST_CUSTOMER_ID_CUSTOMER + " INTEGER PRIMARY KEY," + KEY_MST_CUSTOMER_KODE_CUSTOMER + " TEXT,"
				+ KEY_MST_CUSTOMER_NAMA_CUSTOMER + " TEXT," + KEY_MST_CUSTOMER_ALAMAT + " TEXT,"
				+ KEY_MST_CUSTOMER_NO_HP +" TEXT," + KEY_MST_CUSTOMER_LATS +" TEXT,"
				+ KEY_MST_CUSTOMER_LONGS +" TEXT," + KEY_MST_CUSTOMER_ID_WILAYAH + " TEXT"
				+ ")";
		db.execSQL(CREATE_TABLE_MST_CUSTOMER);

		String CREATE_TABLE_MST_USER = "CREATE TABLE " + TABLE_MST_USER1 + "("
				+ KEY_MST_USER_ID_USER + " INTEGER PRIMARY KEY," + KEY_MST_USER_NAMA + " TEXT,"
				+ KEY_MST_USER_USERNAME + " TEXT," + KEY_MST_USER_PASSWORD + " TEXT,"
				+ KEY_MST_USER_ID_DEPARTEMEN +" TEXT," + KEY_MST_USER_ID_WILAYAH +" TEXT,"
				+ KEY_MST_USER_ID_KARYAWAN +" TEXT," + KEY_MST_USER_HAK_AKSES + " TEXT,"
				+ KEY_MST_USER_NO_HP +" TEXT," + KEY_MST_USER_ID_ROLE + " TEXT"
				+ ")";
		db.execSQL(CREATE_TABLE_MST_USER);

		String CREATE_TABLE_TMP_CUSTOMER = "CREATE TABLE " + TABLE_TMP_CUSTOMER + "("
				+ KEY_TMP_CUSTOMER_ID_CUSTOMER_TMP + " INTEGER PRIMARY KEY," + KEY_TMP_CUSTOMER_KODE_CUSTOMER + " TEXT,"
				+ KEY_TMP_CUSTOMER_NAMA_CUSTOMER + " TEXT," + KEY_TMP_CUSTOMER_NO_HP + " TEXT,"
				+ KEY_TMP_CUSTOMER_ALAMAT + " TEXT,"+ KEY_TMP_CUSTOMER_NAMA_USAHA + " TEXT"
				+ ")";
		db.execSQL(CREATE_TABLE_TMP_CUSTOMER);

		String CREATE_TABLE_TRX_CHECKIN = "CREATE TABLE " + TABLE_TRX_CHECKIN + "("
				+ KEY_TRX_CHECKIN_ID_CHECKIN + " INTEGER PRIMARY KEY," + KEY_TRX_CHECKIN_TANGGAL_CHECKIN + " TEXT,"
				+ KEY_TRX_CHECKIN_NOMOR_CHECKIN + " TEXT,"+ KEY_TRX_CHECKIN_ID_USER + " TEXT,"
				+ KEY_TRX_CHECKIN_ID_RENCANA_DETAIL + " TEXT,"
				+ KEY_TRX_CHECKIN_KODE_CUSTOMER + " TEXT," + KEY_TRX_CHECKIN_LATS + " TEXT,"
				+ KEY_TRX_CHECKIN_LONGS + " TEXT," + KEY_TRX_CHECKIN_FOTO + " TEXT"
				+ ")";
		db.execSQL(CREATE_TABLE_TRX_CHECKIN);

		String CREATE_TABLE_TRX_CHECKOUT = "CREATE TABLE " + TABLE_TRX_CHECKOUT + "("
				+ KEY_TRX_CHECKOUT_ID_CHECKOUT + " INTEGER PRIMARY KEY," + KEY_TRX_CHECKOUT_ID_CHECKIN + " TEXT,"
				+ KEY_TRX_CHECKOUT_TANGGAL_CHECKOUT + " TEXT," + KEY_TRX_CHECKOUT_ID_USER + " TEXT,"
				+ KEY_TRX_CHECKOUT_REALISASI_KEGIATAN + " TEXT," + KEY_TRX_CHECKOUT_LATS + " TEXT,"
				+ KEY_TRX_CHECKOUT_LONGS + " TEXT"
				+ ")";
		db.execSQL(CREATE_TABLE_TRX_CHECKOUT);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MST_USER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LAP_ABSEN);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETAIL_RENCANA);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_KEGIATAN);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MST_CUSTOMER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MST_USER1);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TMP_CUSTOMER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRX_CHECKIN);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRX_CHECKOUT);
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

	//adding detail rencana
	public void addDetailRencana (DetailRencana detail_rencana){
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DETAIL_RENCANA_ID_RENCANA_DETAIL, detail_rencana.getId_rencana_detail());
		values.put(KEY_DETAIL_RENCANA_ID_RENCANA_HEADER, detail_rencana.getId_rencana_header());
		values.put(KEY_DETAIL_RENCANA_ID_KEGIATAN, detail_rencana.getId_kegiatan());
		values.put(KEY_DETAIL_RENCANA_ID_CUSTOMER, detail_rencana.getId_customer());
		values.put(KEY_DETAIL_RENCANA_ID_KARYAWAN, detail_rencana.getId_karyawan());
		values.put(KEY_DETAIL_RENCANA_STATUS_RENCANA, detail_rencana.getStatus_rencana());

		db.insert(TABLE_DETAIL_RENCANA, null, values);
		db.close();
	}

	public void addKegiatan (Kegiatan kegiatan){
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_KEGIATAN_ID_KEGIATAN, kegiatan.getId_kegiatan());
		values.put(KEY_KEGIATAN_NAMA_KEGIATAN, kegiatan.getNama_kegiatan());
		values.put(KEY_KEGIATAN_ID_DEPARTEMEN, kegiatan.getId_departemen());
		values.put(KEY_KEGIATAN_ID_WILAYAH, kegiatan.getId_wilayah());

		db.insert(TABLE_KEGIATAN, null, values);
		db.close();
	}

	public void addMst_customer (Mst_Customer mst_customer){
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_MST_CUSTOMER_ID_CUSTOMER, mst_customer.getId_customer());
		values.put(KEY_MST_CUSTOMER_KODE_CUSTOMER, mst_customer.getKode_customer());
		values.put(KEY_MST_CUSTOMER_NAMA_CUSTOMER, mst_customer.getNama_customer());
		values.put(KEY_MST_CUSTOMER_ALAMAT, mst_customer.getAlamat());
		values.put(KEY_MST_CUSTOMER_NO_HP, mst_customer.getNo_hp());
		values.put(KEY_MST_CUSTOMER_LATS, mst_customer.getLats());
		values.put(KEY_MST_CUSTOMER_LONGS, mst_customer.getLongs());
		values.put(KEY_MST_CUSTOMER_ID_WILAYAH, mst_customer.getId_wilayah());

		db.insert(TABLE_MST_CUSTOMER, null, values);
		db.close();
	}

	public void addMst_user (MstUser mst_user){
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_MST_USER_ID_USER, mst_user.getId_user());
		values.put(KEY_MST_USER_NAMA, mst_user.getNama());
		values.put(KEY_MST_USER_USERNAME, mst_user.getUsername());
		values.put(KEY_MST_USER_PASSWORD, mst_user.getPassword());
		values.put(KEY_MST_USER_ID_DEPARTEMEN, mst_user.getId_departemen());
		values.put(KEY_MST_USER_ID_WILAYAH, mst_user.getId_wilayah());
		values.put(KEY_MST_USER_ID_KARYAWAN, mst_user.getId_karyawan());
		values.put(KEY_MST_USER_HAK_AKSES, mst_user.getHak_akses());
		values.put(KEY_MST_USER_NO_HP, mst_user.getNo_hp());
		values.put(KEY_MST_USER_ID_ROLE, mst_user.getId_role());

		db.insert(TABLE_MST_USER1, null, values);
		db.close();
	}

	public void addTmp_customer (TmpCustomer tmp_Customer){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		//values.put(KEY_TMP_CUSTOMER_ID_CUSTOMER_TMP, tmp_Customer.getId_customer_tmp());
		values.put(KEY_TMP_CUSTOMER_KODE_CUSTOMER, tmp_Customer.getKode_customer());
		values.put(KEY_TMP_CUSTOMER_NAMA_CUSTOMER, tmp_Customer.getNama_customer());
		values.put(KEY_TMP_CUSTOMER_NO_HP, tmp_Customer.getNo_hp());
		values.put(KEY_TMP_CUSTOMER_ALAMAT, tmp_Customer.getAlamat());
		values.put(KEY_TMP_CUSTOMER_NAMA_USAHA, tmp_Customer.getNama_usaha());

		db.insert(TABLE_TMP_CUSTOMER, null, values);
		db.close();
	}

	public void addCheckin (Trx_Checkin trx_checkin){
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TRX_CHECKIN_ID_CHECKIN, trx_checkin.getId_checkin());
		values.put(KEY_TRX_CHECKIN_TANGGAL_CHECKIN, trx_checkin.getTanggal_checkin());
		values.put(KEY_TRX_CHECKIN_NOMOR_CHECKIN, trx_checkin.getNomor_checkin());
		values.put(KEY_TRX_CHECKIN_ID_USER, trx_checkin.getId_user());
		values.put(KEY_TRX_CHECKIN_ID_RENCANA_DETAIL, trx_checkin.getId_rencana_detail());
		values.put(KEY_TRX_CHECKIN_KODE_CUSTOMER, trx_checkin.getKode_customer());
		values.put(KEY_TRX_CHECKIN_LATS, trx_checkin.getLats());
		values.put(KEY_TRX_CHECKIN_LONGS, trx_checkin.getLongs());
		values.put(KEY_TRX_CHECKIN_FOTO, trx_checkin.getFoto());

		db.insert(TABLE_TRX_CHECKIN, null, values);
		db.close();
	}

	public void addCheckout (Trx_Checkout trx_checkout){
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TRX_CHECKOUT_ID_CHECKOUT, trx_checkout.getId_checkout());
		values.put(KEY_TRX_CHECKOUT_ID_CHECKIN, trx_checkout.getId_checkin());
		values.put(KEY_TRX_CHECKOUT_TANGGAL_CHECKOUT, trx_checkout.getTanggal_checkout());
		values.put(KEY_TRX_CHECKOUT_ID_USER, trx_checkout.getId_user());
		values.put(KEY_TRX_CHECKOUT_REALISASI_KEGIATAN, trx_checkout.getRealisasi_kegiatan());
		values.put(KEY_TRX_CHECKOUT_LATS, trx_checkout.getLats());
		values.put(KEY_TRX_CHECKOUT_LONGS, trx_checkout.getLongs());

		db.insert(TABLE_TRX_CHECKOUT, null, values);
		db.close();
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
	public ArrayList<MstUser> getAllUser() {
		try {
			mst_userArrayList.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_MST_USER1;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					MstUser user = new MstUser();
					user.setId_user(cursor.getInt(0));
					user.setNama(cursor.getString(1));
					user.setUsername(cursor.getString(2));
					user.setPassword(cursor.getString(3));
					user.setId_departemen(cursor.getInt(4));
					user.setId_wilayah(cursor.getInt(5));
					user.setId_karyawan(cursor.getInt(6));
					user.setHak_akses(cursor.getString(7));
					user.setNo_hp(cursor.getString(8));
					user.setId_role(cursor.getInt(9));

					// Adding staff to list
					mst_userArrayList.add(user);
				} while (cursor.moveToNext());
			}

			// return staff_list
			cursor.close();
			db.close();
			return mst_userArrayList;
		} catch (Exception e) {
			Log.e("user_list", "" + e);
		}
		return mst_userArrayList;
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

	public ArrayList<DetailRencana> getAllDetailRencana() {
		try {
			detailRencanaArraylist.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_DETAIL_RENCANA;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					DetailRencana detailRencana = new DetailRencana();
					detailRencana.setId_rencana_detail(cursor.getInt(0));
					detailRencana.setId_kegiatan(cursor.getInt(1));
					detailRencana.setId_customer(cursor.getInt(2));
					detailRencana.setId_karyawan(cursor.getInt(3));
					detailRencana.setStatus_rencana(cursor.getInt(4));

					// Adding staff to list
					detailRencanaArraylist.add(detailRencana);
				} while (cursor.moveToNext());
			}

			// return staff_list
			cursor.close();
			db.close();
			return detailRencanaArraylist;
		} catch (Exception e) {
			Log.e("Detail_rencana_list", "" + e);
		}
		return detailRencanaArraylist;
	}

	public ArrayList<Trx_Checkin> getAllCheckinNumber() {
		try {
			trx_checkinArrayList.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_TRX_CHECKIN;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Trx_Checkin trx_checkin = new Trx_Checkin();
					trx_checkin.setId_checkin(cursor.getInt(0));
					trx_checkin.setTanggal_checkin(cursor.getString(1));
					trx_checkin.setNomor_checkin(cursor.getString(2));
					trx_checkin.setId_user(cursor.getInt(3));
					trx_checkin.setId_rencana_detail(cursor.getInt(4));
					trx_checkin.setKode_customer(cursor.getString(5));
					trx_checkin.setLats(cursor.getString(6));
					trx_checkin.setLongs(cursor.getString(7));
					trx_checkin.setFoto(cursor.getString(8));

					// Adding staff to list
					trx_checkinArrayList.add(trx_checkin);
				} while (cursor.moveToNext());
			}

			// return staff_list
			cursor.close();
			db.close();
			return trx_checkinArrayList;
		} catch (Exception e) {
			Log.e("Number_checkin_list", "" + e);
		}
		return trx_checkinArrayList;
	}

	// Getting single Customer per baris
	public MstUser getUser(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_MST_USER1, new String[] {
						KEY_MST_USER_ID_USER,KEY_MST_USER_NAMA,KEY_MST_USER_USERNAME,KEY_MST_USER_PASSWORD,
						KEY_MST_USER_ID_DEPARTEMEN,KEY_MST_USER_ID_WILAYAH,KEY_MST_USER_ID_KARYAWAN,KEY_MST_USER_HAK_AKSES,
						KEY_MST_USER_NO_HP,KEY_MST_USER_ID_ROLE},KEY_MST_USER_ID_KARYAWAN + "=?",new String[] { String.valueOf(1605) },
						null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		MstUser user = new MstUser(cursor.getInt(0), cursor.getString(1),
				cursor.getString(2), cursor.getString(3), cursor.getInt(4),
				cursor.getInt(5), cursor.getInt(6), cursor.getString(7), cursor.getString(8), cursor.getInt(9));
		// return customer
		cursor.close();
		db.close();

		return user;
	}

	// Getting single max checkin
	public Trx_Checkin getCheckin() {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM (SELECT MAX(id_checkin) AS id_checkin FROM trx_checkin)AS DATA JOIN trx_checkin ON data.id_checkin = trx_checkin.id_checkin", new String[] {
						KEY_TRX_CHECKIN_ID_CHECKIN,KEY_TRX_CHECKIN_TANGGAL_CHECKIN,KEY_TRX_CHECKIN_NOMOR_CHECKIN,KEY_TRX_CHECKIN_ID_USER,
						KEY_TRX_CHECKIN_ID_RENCANA_DETAIL,KEY_TRX_CHECKIN_KODE_CUSTOMER,KEY_TRX_CHECKIN_LATS,KEY_TRX_CHECKIN_LONGS,
						KEY_TRX_CHECKIN_FOTO});

		if (cursor != null)
			cursor.moveToFirst();

		Trx_Checkin checkin = new Trx_Checkin(cursor.getInt(0), cursor.getString(1),
				cursor.getString(2), cursor.getInt(3), cursor.getInt(4),
				cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
		// return customer
		cursor.close();
		db.close();

		return checkin;
	}

	// Getting single Customer per baris
	public Mst_Customer getCustomer(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_MST_CUSTOMER, new String[] {
						KEY_MST_CUSTOMER_ID_CUSTOMER,KEY_MST_CUSTOMER_KODE_CUSTOMER,KEY_MST_CUSTOMER_NAMA_CUSTOMER,KEY_MST_CUSTOMER_ALAMAT,
						KEY_MST_CUSTOMER_NO_HP,KEY_MST_CUSTOMER_LATS,KEY_MST_CUSTOMER_LONGS,KEY_MST_CUSTOMER_ID_WILAYAH},
				//group outlet
				//KEY_GROUP_OUTLET_ID_GROUP_OUTLET},
				KEY_MST_CUSTOMER_ID_CUSTOMER + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);


		if (cursor != null)
			cursor.moveToFirst();

		Mst_Customer customer = new Mst_Customer(cursor.getInt(0), cursor.getString(1),
				cursor.getString(2), cursor.getString(3), cursor.getString(4),
				cursor.getString(5), cursor.getString(6), cursor.getInt(7));
		// return customer
		cursor.close();
		db.close();

		return customer;
	}

	public ArrayList<Mst_Customer> getAllCustomer() {
		try {
			mst_customerArrayList.clear();

			String selectQuery = "SELECT * FROM "+ TABLE_MST_CUSTOMER;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Mst_Customer customer = new Mst_Customer();
					customer.setId_customer(cursor.getInt(0));
					customer.setKode_customer(cursor.getString(1));
					customer.setNama_customer(cursor.getString(2));
					customer.setAlamat(cursor.getString(3));
					customer.setNo_hp(cursor.getString(4));
					customer.setLats(cursor.getString(5));
					customer.setLongs(cursor.getString(6));
					customer.setId_wilayah(cursor.getInt(7));

					// Adding staff to list
					mst_customerArrayList.add(customer);
				} while (cursor.moveToNext());
			}

			// return staff_list
			cursor.close();
			db.close();
			return mst_customerArrayList;
		} catch (Exception e) {
			Log.e("mst_customer_list", "" + e);
		}
		return mst_customerArrayList;
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

	public void deleteMst_user() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_MST_USER1);
	}

	public void deleteTableTrackingLogs() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_TRACKING_LOGS);
	}

	public int getCountUser() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db
				.rawQuery("select count(*) from " + TABLE_MST_USER1, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
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

	public int getCountDetailrencana() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db
				.rawQuery("select count(*) from " + TABLE_DETAIL_RENCANA, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountKegiatan() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db
				.rawQuery("select count(*) from " + TABLE_KEGIATAN, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountMstcustomer() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db
				.rawQuery("select count(*) from " + TABLE_MST_CUSTOMER, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountMstuser() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db
				.rawQuery("select count(*) from " + TABLE_MST_USER1, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountTmpCustomer() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db
				.rawQuery("select count(*) from " + TABLE_TMP_CUSTOMER, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountTrxcheckin() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db
				.rawQuery("select count(*) from " + TABLE_TRX_CHECKIN, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountTrxcheckout() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db
				.rawQuery("select count(*) from " + TABLE_TRX_CHECKOUT, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountAsben() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_LAP_ABSEN,
				null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountCustomer() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_MST_CUSTOMER,
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

	public void deleteTableAbsen() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_LAP_ABSEN);
	}

	public void deleteTableMSTCustomer() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_MST_CUSTOMER);
	}public void deleteTableMSTUser() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_MST_USER1);
	}public void deleteTableMSTKegiattan() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_KEGIATAN);
	}public void deleteTableRencanaDetail() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_DETAIL_RENCANA);
	}public void deleteCheckin(String nomer_checkin) {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_TRX_CHECKIN+" WHERE nomor_checkin = '"+ nomer_checkin+"'");
	}

}
