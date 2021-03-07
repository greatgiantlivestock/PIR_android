package com.android.pir.gglc.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 3;
	private static final String DATABASE_NAME = "visit_pir";

	//to define table name
	private static final String TABLE_MST_USER= "mst_user";
	private static final String TABLE_DETAIL_RENCANA = "trx_rencana_detail";
	private static final String TABLE_MASTER_RENCANA = "trx_rencana_master";
	private static final String TABLE_MST_CUSTOMER = "mst_customer";
	private static final String TABLE_MST_USER1 = "mst_user1";
	private static final String TABLE_TMP_CUSTOMER = "tmp_customer";
	private static final String TABLE_TRX_CHECKIN = "trx_checkin";
	private static final String TABLE_UPLOAD_DATA_SAPI = "upload_data_sapi";
	private static final String TABLE_TRX_CHECKOUT = "trx_checkout";
	private static final String TABLE_HISTORY_CANVASSING = "history_canvassing";
	private static final String TABLE_DATA_SAPI = "data_sapi";
	private static final String TABLE_PRODUCT= "mst_product";

	//define field on ms_user
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_PH_NO = "phone_number";

	//define DetailRencana field
	private static final String KEY_MASTER_RENCANA_ID = "id";
	private static final String KEY_MASTER_RENCANA_ID_RENCANA_HEADER = "id_rencana_header";
	private static final String KEY_MASTER_RENCANA_NOMOR_RENCANA = "nomor_rencana";
	private static final String KEY_MASTER_RENCANA_TANGGAL_PENETAPAN = "tanggal_penetapan";
	private static final String KEY_MASTER_RENCANA_TANGGAL_RENCANA = "tanggal_rencana";
	private static final String KEY_MASTER_RENCANA_ID_USER_INPUT_RENCANA = "id_user_input_rencana";
	private static final String KEY_MASTER_RENCANA_KETERANGAN = "keterangan";
	private static final String KEY_MASTER_RENCANA_APROVED = "aproved";

	//define DetailRencana field
	private static final String KEY_DETAIL_RENCANA_ID_RENCANA_DETAIL = "id_rencana_detail";
	private static final String KEY_DETAIL_RENCANA_ID_RENCANA_HEADER = "id_rencana_header";
	private static final String KEY_DETAIL_RENCANA_ID_KEGIATAN = "id_kegiatan";
	private static final String KEY_DETAIL_RENCANA_ID_CUSTOMER = "id_customer";
	private static final String KEY_DETAIL_RENCANA_ID_KARYAWAN = "id_karyawan";
	private static final String KEY_DETAIL_RENCANA_STATUS_RENCANA = "status_rencana";
	private static final String KEY_DETAIL_RENCANA_NOMOR_RENCANA_EDTAIL = "nomor_rencana_detail";

	//define DetailRencana field
	private static final String KEY_DATA_SAPI_ID_DATA_SAPI = "id_data_sapi";
	private static final String KEY_DATA_SAPI_INDNR = "indnr";
	private static final String KEY_DATA_SAPI_LIFNR = "lifnr";
	private static final String KEY_DATA_SAPI_BEASTID = "beastid";
	private static final String KEY_DATA_SAPI_VISTGID = "vistgid";

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
	private static final String KEY_TRX_CHECKIN_ID_RENCANA_HEADER ="id_rencana_header";
	private static final String KEY_TRX_CHECKIN_KODE_CUSTOMER ="kode_customer";
	private static final String KEY_TRX_CHECKIN_LATS ="lats";
	private static final String KEY_TRX_CHECKIN_LONGS ="longs";
	private static final String KEY_TRX_CHECKIN_FOTO ="foto";
	private static final String KEY_TRX_CHECKIN_STATUS ="status";
	private static final String KEY_TRX_CHECKIN_PROSPECT ="prospect";

	//define trx_checkout field
	private static final String KEY_TRX_CHECKOUT_ID_CHECKOUT ="id_chekout";
	private static final String KEY_TRX_CHECKOUT_ID_CHECKIN ="id_rencana_detail";
	private static final String KEY_TRX_CHECKOUT_TANGGAL_CHECKOUT ="tanggal_checkout";
	private static final String KEY_TRX_CHECKOUT_ID_USER ="id_user";
	private static final String KEY_TRX_CHECKOUT_REALISASI_KEGIATAN ="realisasi_kegiatan";
	private static final String KEY_TRX_CHECKOUT_LATS ="lats";
	private static final String KEY_TRX_CHECKOUT_LONGS ="longs";

	//define trx_checkout field
	private static final String KEY_HISTORY_CANVASSING_ID_CANVASSING="id_canvassing";
	private static final String KEY_HISTORY_CANVASSING_NAMA_CUSTOMER="nama_customer";
	private static final String KEY_HISTORY_CANVASSING_ALAMAT="alamat";
	private static final String KEY_HISTORY_CANVASSING_NOMOR_RENCANA="nomor_rencana";
	private static final String KEY_HISTORY_CANVASSING_WAKTU_CHECKIN="waktu_checkin";
	private static final String KEY_HISTORY_CANVASSING_WAKTU_CHECKOUT="waktu_checkout";

	//define trx_checkout field
	private static final String KEY_UPLOAD_DATA_SAPI_ID_UPLOAD="id_upload";
	private static final String KEY_UPLOAD_DATA_SAPI_ID_RENCANA_DETAIL="id_rencana_detail";
	private static final String KEY_UPLOAD_DATA_SAPI_EARTAG="eartag";
	private static final String KEY_UPLOAD_DATA_SAPI_FOTO="foto";
	private static final String KEY_UPLOAD_DATA_SAPI_KETERANGAN="keterangan";
	private static final String KEY_UPLOAD_DATA_SAPI_ASSESSMENT="assessment";
	private static final String KEY_UPLOAD_DATA_SAPI_TANGGAL="tanggal";


	//define trx_checkout field
	private static final String KEY_PRODUCT_ID_PRODUCT="id_product";
	private static final String KEY_PRODUCT_NAMA_PRODUCT="nama_product";

	//ArrayList table
	private final ArrayList<User> user_list = new ArrayList<User>();
	private final ArrayList<TrackingLogs> tracking_logs_list = new ArrayList<TrackingLogs>();
	private final ArrayList<DetailRencana> detailRencanaArraylist = new ArrayList<DetailRencana>();
	private final ArrayList<DataSapi> dataSapiArraylist = new ArrayList<DataSapi>();
	private final ArrayList<DetailReqLoadNew> detailReqLoadNews = new ArrayList<DetailReqLoadNew>();
	private final ArrayList<MasterRencana> masterRencanaArraylist = new ArrayList<MasterRencana>();
	private final ArrayList<MasterRencanaParam> masterRencanaParamArraylist = new ArrayList<MasterRencanaParam>();
	private final ArrayList<Mst_Customer> mst_customerArrayList = new ArrayList<Mst_Customer>();
	private final ArrayList<MstUser> mst_userArrayList =new ArrayList<MstUser>();
	private final ArrayList<TmpCustomer> tmpCustomerArrayList =new ArrayList<TmpCustomer>();
	private final ArrayList<Trx_Checkin> trx_checkinArrayList =new ArrayList<Trx_Checkin>();
	private final ArrayList<Trx_Checkout> trx_checkoutArrayList =new ArrayList<Trx_Checkout>();
	private final ArrayList<History_canvassing> historyCamvassingArrayList =new ArrayList<History_canvassing>();
	private final ArrayList<Rencana> rencanaArrayList =new ArrayList<Rencana>();
	private final ArrayList<Product> productArrayList =new ArrayList<Product>();
	private final ArrayList<UploadDataSapi> uploadDataSapiArrayList=new ArrayList<UploadDataSapi>();

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MST_USER + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_PH_NO + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);

		String CREATE_TABLE_DETAIL_RENCANA = "CREATE TABLE " + TABLE_DETAIL_RENCANA + "("
				+ KEY_DETAIL_RENCANA_ID_RENCANA_DETAIL + " INTEGER PRIMARY KEY," + KEY_DETAIL_RENCANA_ID_RENCANA_HEADER + " TEXT,"
				+ KEY_DETAIL_RENCANA_ID_KEGIATAN + " TEXT," + KEY_DETAIL_RENCANA_ID_CUSTOMER + " TEXT,"
				+ KEY_DETAIL_RENCANA_ID_KARYAWAN + " TEXT," + KEY_DETAIL_RENCANA_STATUS_RENCANA + " TEXT,"
				+ KEY_DETAIL_RENCANA_NOMOR_RENCANA_EDTAIL + " TEXT"
				+ ")";
		db.execSQL(CREATE_TABLE_DETAIL_RENCANA);

		String CREATE_TABLE_DATA_SAPI = "CREATE TABLE " + TABLE_DATA_SAPI + "("
				+ KEY_DATA_SAPI_ID_DATA_SAPI + " INTEGER PRIMARY KEY," + KEY_DATA_SAPI_INDNR + " TEXT,"
				+ KEY_DATA_SAPI_LIFNR + " TEXT," + KEY_DATA_SAPI_BEASTID + " TEXT,"
				+ KEY_DATA_SAPI_VISTGID + " TEXT"+ ")";
		db.execSQL(CREATE_TABLE_DATA_SAPI);

		String CREATE_TABLE_MASTER_RENCANA = "CREATE TABLE " + TABLE_MASTER_RENCANA + "("
				+ KEY_MASTER_RENCANA_ID + " INTEGER PRIMARY KEY," + KEY_MASTER_RENCANA_ID_RENCANA_HEADER + " TEXT,"
				+ KEY_MASTER_RENCANA_NOMOR_RENCANA + " TEXT," + KEY_MASTER_RENCANA_TANGGAL_PENETAPAN + " TEXT,"
				+ KEY_MASTER_RENCANA_TANGGAL_RENCANA + " TEXT," + KEY_MASTER_RENCANA_ID_USER_INPUT_RENCANA + " TEXT,"
				+ KEY_MASTER_RENCANA_KETERANGAN + " TEXT," + KEY_MASTER_RENCANA_APROVED + " TEXT"
				+ ")";
		db.execSQL(CREATE_TABLE_MASTER_RENCANA);

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
				+ KEY_MST_USER_ID_KARYAWAN +" TEXT," + KEY_MST_USER_NO_HP +" TEXT,"
				+ KEY_MST_USER_ID_ROLE + " TEXT"
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
				+ KEY_TRX_CHECKIN_ID_RENCANA_HEADER + " TEXT,"
				+ KEY_TRX_CHECKIN_KODE_CUSTOMER + " TEXT," + KEY_TRX_CHECKIN_LATS + " TEXT,"
				+ KEY_TRX_CHECKIN_LONGS + " TEXT," + KEY_TRX_CHECKIN_FOTO + " TEXT,"+ KEY_TRX_CHECKIN_STATUS + " TEXT,"+ KEY_TRX_CHECKIN_PROSPECT + " TEXT"
				+ ")";
		db.execSQL(CREATE_TABLE_TRX_CHECKIN);
		String CREATE_TABLE_UPLOAD_DATA_SAPI = "CREATE TABLE " + TABLE_UPLOAD_DATA_SAPI + "("
				+ KEY_UPLOAD_DATA_SAPI_ID_UPLOAD + " INTEGER PRIMARY KEY," + KEY_UPLOAD_DATA_SAPI_ID_RENCANA_DETAIL + " TEXT,"
				+ KEY_UPLOAD_DATA_SAPI_EARTAG + " TEXT,"+ KEY_UPLOAD_DATA_SAPI_FOTO + " TEXT,"
				+ KEY_UPLOAD_DATA_SAPI_KETERANGAN + " TEXT,"
				+ KEY_UPLOAD_DATA_SAPI_ASSESSMENT + " TEXT,"
				+ KEY_UPLOAD_DATA_SAPI_TANGGAL + " TEXT"
				+ ")";
		db.execSQL(CREATE_TABLE_UPLOAD_DATA_SAPI);

		String CREATE_TABLE_TRX_CHECKOUT = "CREATE TABLE " + TABLE_TRX_CHECKOUT + "("
				+ KEY_TRX_CHECKOUT_ID_CHECKOUT + " INTEGER PRIMARY KEY," + KEY_TRX_CHECKOUT_ID_CHECKIN + " TEXT,"
				+ KEY_TRX_CHECKOUT_TANGGAL_CHECKOUT + " TEXT," + KEY_TRX_CHECKOUT_ID_USER + " TEXT,"
				+ KEY_TRX_CHECKOUT_REALISASI_KEGIATAN + " TEXT," + KEY_TRX_CHECKOUT_LATS + " TEXT,"
				+ KEY_TRX_CHECKOUT_LONGS + " TEXT"
				+ ")";
		db.execSQL(CREATE_TABLE_TRX_CHECKOUT);

		String CREATE_TABLE_HISTORY_CANVASSING = "CREATE TABLE " + TABLE_HISTORY_CANVASSING + "("
				+ KEY_HISTORY_CANVASSING_ID_CANVASSING + " INTEGER PRIMARY KEY," + KEY_HISTORY_CANVASSING_NAMA_CUSTOMER + " TEXT,"
				+ KEY_HISTORY_CANVASSING_NOMOR_RENCANA + " TEXT," + KEY_HISTORY_CANVASSING_ALAMAT + " TEXT,"
				+ KEY_HISTORY_CANVASSING_WAKTU_CHECKIN + " TEXT," + KEY_HISTORY_CANVASSING_WAKTU_CHECKOUT+ " TEXT"
				+ ")";
		db.execSQL(CREATE_TABLE_HISTORY_CANVASSING);

		String CREATE_TABLE_PRODUCT = "CREATE TABLE " + TABLE_PRODUCT + "("
				+ KEY_PRODUCT_ID_PRODUCT + " INTEGER PRIMARY KEY," + KEY_PRODUCT_NAMA_PRODUCT + " TEXT"
				+ ")";
		db.execSQL(CREATE_TABLE_PRODUCT);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MST_USER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETAIL_RENCANA);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA_SAPI);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_RENCANA);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MST_CUSTOMER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MST_USER1);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TMP_CUSTOMER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRX_CHECKIN);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_UPLOAD_DATA_SAPI);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRX_CHECKOUT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY_CANVASSING);
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
		values.put(KEY_DETAIL_RENCANA_NOMOR_RENCANA_EDTAIL, detail_rencana.getNomor_rencana_detail());

		db.insert(TABLE_DETAIL_RENCANA, null, values);
		db.close();
	}

	//adding data sapi
	public void addDataSapi (DataSapi dataSapi){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_DATA_SAPI_ID_DATA_SAPI, dataSapi.getId_data_sapi());
		values.put(KEY_DATA_SAPI_INDNR, dataSapi.getIndnr());
		values.put(KEY_DATA_SAPI_LIFNR, dataSapi.getLifnr());
		values.put(KEY_DATA_SAPI_BEASTID, dataSapi.getBeastid());
		values.put(KEY_DATA_SAPI_VISTGID, dataSapi.getVistgid());

		db.insert(TABLE_DATA_SAPI, null, values);
		db.close();
	}

	//adding detail rencana
	public void addMasterRencana (MasterRencana master_rencana){
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_MASTER_RENCANA_ID_RENCANA_HEADER, master_rencana.getId_rencana_header());
		values.put(KEY_MASTER_RENCANA_NOMOR_RENCANA, master_rencana.getNomor_rencana());
		values.put(KEY_MASTER_RENCANA_TANGGAL_PENETAPAN, master_rencana.getTanggal_penetapan());
		values.put(KEY_MASTER_RENCANA_TANGGAL_RENCANA, master_rencana.getTanggal_rencana());
		values.put(KEY_MASTER_RENCANA_ID_USER_INPUT_RENCANA, master_rencana.getId_user_input_rencana());
		values.put(KEY_MASTER_RENCANA_KETERANGAN, master_rencana.getKeterangan());
		values.put(KEY_MASTER_RENCANA_APROVED, master_rencana.getAproved());

		db.insert(TABLE_MASTER_RENCANA, null, values);
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
		//values.put(KEY_TRX_CHECKIN_ID_CHECKIN, trx_checkin.getId_checkin());
		values.put(KEY_TRX_CHECKIN_TANGGAL_CHECKIN, trx_checkin.getTanggal_checkin());
		values.put(KEY_TRX_CHECKIN_NOMOR_CHECKIN, trx_checkin.getNomor_checkin());
		values.put(KEY_TRX_CHECKIN_ID_USER, trx_checkin.getId_user());
		values.put(KEY_TRX_CHECKIN_ID_RENCANA_DETAIL, trx_checkin.getId_rencana_detail());
		values.put(KEY_TRX_CHECKIN_ID_RENCANA_HEADER, trx_checkin.getId_rencana_header());
		values.put(KEY_TRX_CHECKIN_KODE_CUSTOMER, trx_checkin.getKode_customer());
		values.put(KEY_TRX_CHECKIN_LATS, trx_checkin.getLats());
		values.put(KEY_TRX_CHECKIN_LONGS, trx_checkin.getLongs());
		values.put(KEY_TRX_CHECKIN_FOTO, trx_checkin.getFoto());
		values.put(KEY_TRX_CHECKIN_STATUS, trx_checkin.getStatus());
		values.put(KEY_TRX_CHECKIN_PROSPECT, trx_checkin.getProspect());

		db.insert(TABLE_TRX_CHECKIN, null, values);
		db.close();
	}
	public void addUploadDataSapi (UploadDataSapi upload_data_sapi){
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_UPLOAD_DATA_SAPI_ID_RENCANA_DETAIL, upload_data_sapi.getId_rencana_detail());
		values.put(KEY_UPLOAD_DATA_SAPI_EARTAG, upload_data_sapi.getEartag());
		values.put(KEY_UPLOAD_DATA_SAPI_FOTO, upload_data_sapi.getFoto());
		values.put(KEY_UPLOAD_DATA_SAPI_KETERANGAN, upload_data_sapi.getKeterangan());
		values.put(KEY_UPLOAD_DATA_SAPI_ASSESSMENT, upload_data_sapi.getAssessment());
		values.put(KEY_UPLOAD_DATA_SAPI_TANGGAL, upload_data_sapi.getTanggal());

		db.insert(TABLE_UPLOAD_DATA_SAPI, null, values);
		db.close();
	}

	public void addFullCheckin (Trx_Checkin trx_checkin){
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TRX_CHECKIN_ID_CHECKIN, trx_checkin.getId_checkin());
		values.put(KEY_TRX_CHECKIN_TANGGAL_CHECKIN, trx_checkin.getTanggal_checkin());
		values.put(KEY_TRX_CHECKIN_NOMOR_CHECKIN, trx_checkin.getNomor_checkin());
		values.put(KEY_TRX_CHECKIN_ID_USER, trx_checkin.getId_user());
		values.put(KEY_TRX_CHECKIN_ID_RENCANA_DETAIL, trx_checkin.getId_rencana_detail());
		values.put(KEY_TRX_CHECKIN_ID_RENCANA_HEADER, trx_checkin.getId_rencana_header());
		values.put(KEY_TRX_CHECKIN_KODE_CUSTOMER, trx_checkin.getKode_customer());
		values.put(KEY_TRX_CHECKIN_LATS, trx_checkin.getLats());
		values.put(KEY_TRX_CHECKIN_LONGS, trx_checkin.getLongs());
		values.put(KEY_TRX_CHECKIN_FOTO, trx_checkin.getFoto());
		values.put(KEY_TRX_CHECKIN_STATUS, trx_checkin.getStatus());
		values.put(KEY_TRX_CHECKIN_PROSPECT, trx_checkin.getProspect());

		db.insert(TABLE_TRX_CHECKIN, null, values);
		db.close();
	}

	public void addCheckout (Trx_Checkout trx_checkout){
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TRX_CHECKOUT_ID_CHECKIN, trx_checkout.getId_rencana_detail());
		values.put(KEY_TRX_CHECKOUT_TANGGAL_CHECKOUT, trx_checkout.getTanggal_checkout());
		values.put(KEY_TRX_CHECKOUT_ID_USER, trx_checkout.getId_user());
		values.put(KEY_TRX_CHECKOUT_REALISASI_KEGIATAN, trx_checkout.getRealisasi_kegiatan());
		values.put(KEY_TRX_CHECKOUT_LATS, trx_checkout.getLats());
		values.put(KEY_TRX_CHECKOUT_LONGS, trx_checkout.getLongs());

		db.insert(TABLE_TRX_CHECKOUT, null, values);
		db.close();
	}

	//adding detail rencana
	public void addHistoryCanvassing (History_canvassing history_canvassing){
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_HISTORY_CANVASSING_NAMA_CUSTOMER, history_canvassing.getNama_customer());
		values.put(KEY_HISTORY_CANVASSING_NOMOR_RENCANA, history_canvassing.getNomor_rencana());
		values.put(KEY_HISTORY_CANVASSING_ALAMAT, history_canvassing.getAlamat());
		values.put(KEY_HISTORY_CANVASSING_WAKTU_CHECKIN, history_canvassing.getWaktu_checkin());
		values.put(KEY_HISTORY_CANVASSING_WAKTU_CHECKOUT, history_canvassing.getWaktu_checkout());

		db.insert(TABLE_HISTORY_CANVASSING, null, values);
		db.close();
	}
	//adding product
	public void addProduct (Product product){
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PRODUCT_ID_PRODUCT, product.getId_product());
		values.put(KEY_PRODUCT_NAMA_PRODUCT, product.getNama_product());

		db.insert(TABLE_PRODUCT, null, values);
		db.close();
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
					user.setNo_hp(cursor.getString(7));
					user.setId_role(cursor.getInt(8));

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

	//getting All user
	public ArrayList<DetailRencana> getAllRencanaParam(int id_rencana_header) {
		try {
			detailRencanaArraylist.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_DETAIL_RENCANA + " WHERE id_rencana_detail='"+id_rencana_header+"'";

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					DetailRencana detailRencana = new DetailRencana();
					detailRencana.setId_rencana_detail(cursor.getInt(0));
					detailRencana.setId_rencana_header(cursor.getInt(1));
					detailRencana.setId_kegiatan(cursor.getInt(2));
					detailRencana.setId_customer(cursor.getInt(3));
					detailRencana.setId_karyawan(cursor.getInt(4));
					detailRencana.setStatus_rencana(cursor.getInt(5));
					detailRencana.setNomor_rencana_detail(cursor.getString(6));

					// Adding staff to list
					detailRencanaArraylist.add(detailRencana);
				} while (cursor.moveToNext());
			}

			// return staff_list
			cursor.close();
			db.close();
			return detailRencanaArraylist;
		} catch (Exception e) {
			Log.e("user_list", "" + e);
		}
		return detailRencanaArraylist;
	}

	//getting All user
	public ArrayList<DetailReqLoadNew> getDetailRencanaParam(String nomor_rencana) {
		try {
			detailReqLoadNews.clear();

			// Select All Query
			String selectQuery = "SELECT "+KEY_MST_CUSTOMER_NAMA_CUSTOMER+","+TABLE_DETAIL_RENCANA+"."+KEY_MST_CUSTOMER_ID_CUSTOMER+","+KEY_MST_CUSTOMER_ALAMAT+" FROM "+
					TABLE_DETAIL_RENCANA+" JOIN "+ TABLE_MST_CUSTOMER+" ON "+TABLE_DETAIL_RENCANA+"."+KEY_DETAIL_RENCANA_ID_CUSTOMER+"="+
					TABLE_MST_CUSTOMER+"."+KEY_MST_CUSTOMER_ID_CUSTOMER+" JOIN "+TABLE_MASTER_RENCANA+" ON "+TABLE_MASTER_RENCANA+"."+
					KEY_MASTER_RENCANA_ID_RENCANA_HEADER+"="+TABLE_DETAIL_RENCANA+"."+KEY_DETAIL_RENCANA_ID_RENCANA_HEADER+" WHERE "+
					KEY_MASTER_RENCANA_NOMOR_RENCANA+"='"+nomor_rencana+"'";

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					DetailReqLoadNew detailRencana = new DetailReqLoadNew();
					detailRencana.setNama_product(cursor.getString(0));
					detailRencana.setId_product(cursor.getInt(1));
					detailRencana.setAlamat(cursor.getString(2));

					// Adding staff to list
					detailReqLoadNews.add(detailRencana);
				} while (cursor.moveToNext());
			}

			// return staff_list
			cursor.close();
			db.close();
			return detailReqLoadNews;
		} catch (Exception e) {
			Log.e("user_list", "" + e);
		}
		return detailReqLoadNews;
	}

	//getting All Checkin
	public ArrayList<Trx_Checkin> getAllCheckinParam(String nomor) {
		try {
			trx_checkinArrayList.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_TRX_CHECKIN +" WHERE nomor_checkin ='"+nomor+"'";

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
					trx_checkin.setStatus(cursor.getString(9));
					trx_checkin.setProspect(cursor.getString(10));

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
	//getting All Checkin
	public ArrayList<Trx_Checkin> getAllCheckin() {
		try {
			trx_checkinArrayList.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_TRX_CHECKIN ;

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
					trx_checkin.setId_rencana_header(cursor.getInt(5));
					trx_checkin.setKode_customer(cursor.getString(6));
					trx_checkin.setLats(cursor.getString(7));
					trx_checkin.setLongs(cursor.getString(8));
					trx_checkin.setFoto(cursor.getString(9));
					trx_checkin.setStatus(cursor.getString(10));
					trx_checkin.setProspect(cursor.getString(11));

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

	//getting All Checkout
	public ArrayList<Trx_Checkout> getAllCheckout() {
		try {
			trx_checkoutArrayList.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_TRX_CHECKOUT ;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Trx_Checkout trx_checkout = new Trx_Checkout();
					trx_checkout.setId_checkout(cursor.getInt(0));
					trx_checkout.setId_rencana_detail(cursor.getInt(1));
					trx_checkout.setTanggal_checkout(cursor.getString(2));
					trx_checkout.setId_user(cursor.getInt(3));
					trx_checkout.setRealisasi_kegiatan(cursor.getString(4));
					trx_checkout.setLats(cursor.getString(5));
					trx_checkout.setLongs(cursor.getString(6));

					// Adding staff to list
					trx_checkoutArrayList.add(trx_checkout);
				} while (cursor.moveToNext());
			}

			// return staff_list
			cursor.close();
			db.close();
			return trx_checkoutArrayList;
		} catch (Exception e) {
			Log.e("Number_checkin_list", "" + e);
		}
		return trx_checkoutArrayList;
	}

	//getting All Checkin
	public ArrayList<UploadDataSapi> getAllUploadDataSapi() {
		try {
			uploadDataSapiArrayList.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_UPLOAD_DATA_SAPI ;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					UploadDataSapi uploadDataSapi = new UploadDataSapi();
					uploadDataSapi.setId_upload(cursor.getInt(0));
					uploadDataSapi.setId_rencana_detail(cursor.getString(1));
					uploadDataSapi.setEartag(cursor.getString(2));
					uploadDataSapi.setFoto(cursor.getString(3));
					uploadDataSapi.setKeterangan(cursor.getString(4));
					uploadDataSapi.setAssessment(cursor.getString(5));
					uploadDataSapi.setTanggal(cursor.getString(6));

					// Adding staff to list
					uploadDataSapiArrayList.add(uploadDataSapi);
				} while (cursor.moveToNext());
			}

			// return staff_list
			cursor.close();
			db.close();
			return uploadDataSapiArrayList;
		} catch (Exception e) {
			Log.e("Number_checkin_list", "" + e);
		}
		return uploadDataSapiArrayList;
	}

	//getting All Checkin
	public ArrayList<Trx_Checkin> getAllCheckinIDParam(int idrencana) {
		try {
			trx_checkinArrayList.clear();

			// Select All Query
			String selectQuery = "SELECT * FROM " + TABLE_TRX_CHECKIN +" WHERE id_rencana_detail ='"+idrencana+"'";

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
					trx_checkin.setStatus(cursor.getString(9));
					trx_checkin.setProspect(cursor.getString(10));

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

	//getting All user
	public ArrayList<History_canvassing> getAllHistoryCanvassing() {
		try {
			historyCamvassingArrayList.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_HISTORY_CANVASSING;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					History_canvassing canvassing = new History_canvassing();
					canvassing.setId_canvassing(cursor.getInt(0));
					canvassing.setNama_customer(cursor.getString(1));
					canvassing.setNomor_rencana(cursor.getString(2));
					canvassing.setAlamat(cursor.getString(3));
					canvassing.setWaktu_checkin(cursor.getString(4));
					canvassing.setWaktu_checkout(cursor.getString(5));

					// Adding absen to list
					historyCamvassingArrayList.add(canvassing);
				} while (cursor.moveToNext());
			}

			// return absen_list
			cursor.close();
			db.close();
			return historyCamvassingArrayList;
		} catch (Exception e) {
			Log.e("history_canvassing", "" + e);
		}
		return historyCamvassingArrayList;
	}

	//getting All user
	public ArrayList<Rencana> getAllRencana() {
		try {
			rencanaArrayList.clear();

			// Select All Query
			String selectQuery = "SELECT  id_rencana_detail,nama_customer, alamat, status_rencana, tanggal_rencana FROM trx_rencana_detail " +
					"JOIN mst_customer ON trx_rencana_detail.id_customer = mst_customer.id_customer JOIN trx_rencana_master ON " +
					"trx_rencana_detail.id_rencana_header = trx_rencana_master.id_rencana_header WHERE status_rencana !='2' AND aproved='1'";

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Rencana rencana = new Rencana();
					rencana.setId_rencana_detail(cursor.getInt(0));
					rencana.setNama_customer(cursor.getString(1));
					rencana.setAlamat(cursor.getString(2));
					rencana.setStatus(cursor.getInt(3));
					rencana.setTanggal_rencana(cursor.getString(4));

					// Adding absen to list
					rencanaArrayList.add(rencana);
				} while (cursor.moveToNext());
			}

			// return absen_list
			cursor.close();
			db.close();
			return rencanaArrayList;
		} catch (Exception e) {
			Log.e("rencana", "" + e);
		}
		return rencanaArrayList;
	}

	public ArrayList<DetailRencana> getAllDetailRencana() {
		try {
			detailRencanaArraylist.clear();

			String selectQuery = "SELECT  trd.* FROM " +TABLE_MASTER_RENCANA +" trm JOIN "+ TABLE_DETAIL_RENCANA +" trd ON trm.id_rencana_header =" +
					" trd.id_rencana_header";

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					DetailRencana detailRencana = new DetailRencana();
					detailRencana.setId_rencana_detail(cursor.getInt(0));
					detailRencana.setId_rencana_header(cursor.getInt(1));
					detailRencana.setId_kegiatan(cursor.getInt(2));
					detailRencana.setId_customer(cursor.getInt(3));
					detailRencana.setId_karyawan(cursor.getInt(4));
					detailRencana.setStatus_rencana(cursor.getInt(5));
					detailRencana.setNomor_rencana_detail(cursor.getString(6));

					// Adding staff to list
					detailRencanaArraylist.add(detailRencana);
				} while (cursor.moveToNext());
			}else{
				do {
					DetailRencana detailRencana = new DetailRencana();
					detailRencana.setId_rencana_detail(0);
					detailRencana.setId_rencana_header(1);
					detailRencana.setId_kegiatan(2);
					detailRencana.setId_customer(3);
					detailRencana.setId_karyawan(4);
					detailRencana.setStatus_rencana(5);
					detailRencana.setNomor_rencana_detail(null);

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

	public ArrayList<DataSapi> getAllDataSapiParam(String lifnr) {
		try {
			dataSapiArraylist.clear();
			String selectQuery = "SELECT * FROM " + TABLE_DATA_SAPI + " WHERE lifnr='"+lifnr+"' OR lifnr='0'";

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					DataSapi dataSapi = new DataSapi();
					dataSapi.setId_data_sapi(cursor.getInt(0));
					dataSapi.setIndnr(cursor.getString(1));
					dataSapi.setLifnr(cursor.getString(2));
					dataSapi.setBeastid(cursor.getString(3));
					dataSapi.setVistgid(cursor.getString(4));

					// Adding staff to list
					dataSapiArraylist.add(dataSapi);
				} while (cursor.moveToNext());
			}else{
				do {
					DataSapi dataSapi = new DataSapi();
					dataSapi.setId_data_sapi(0);
					dataSapi.setIndnr(null);
					dataSapi.setLifnr(null);
					dataSapi.setBeastid(null);
					dataSapi.setVistgid(null);

					// Adding staff to list
					dataSapiArraylist.add(dataSapi);
				} while (cursor.moveToNext());
			}

			// return staff_list
			cursor.close();
			db.close();
			return dataSapiArraylist;
		} catch (Exception e) {
			Log.e("Data_sapi_list", "" + e);
		}
		return dataSapiArraylist;
	}

	public ArrayList<DetailRencana> getAllDetailRencanaNEW(int id_rencana_detail) {
		try {
			detailRencanaArraylist.clear();

			String selectQuery = "SELECT  trd.* FROM " +TABLE_MASTER_RENCANA +" trm JOIN "+ TABLE_DETAIL_RENCANA +" trd ON trm.id_rencana_header =" +
					" trd.id_rencana_header WHERE id_rencana_detail='"+id_rencana_detail+"'";

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					DetailRencana detailRencana = new DetailRencana();
					detailRencana.setId_rencana_detail(cursor.getInt(0));
					detailRencana.setId_rencana_header(cursor.getInt(1));
					detailRencana.setId_kegiatan(cursor.getInt(2));
					detailRencana.setId_customer(cursor.getInt(3));
					detailRencana.setId_karyawan(cursor.getInt(4));
					detailRencana.setStatus_rencana(cursor.getInt(5));
					detailRencana.setNomor_rencana_detail(cursor.getString(6));

					// Adding staff to list
					detailRencanaArraylist.add(detailRencana);
				} while (cursor.moveToNext());
			}else{
				do {
					DetailRencana detailRencana = new DetailRencana();
					detailRencana.setId_rencana_detail(0);
					detailRencana.setId_rencana_header(0);
					detailRencana.setId_kegiatan(0);
					detailRencana.setId_customer(0);
					detailRencana.setId_karyawan(0);
					detailRencana.setStatus_rencana(0);
					detailRencana.setNomor_rencana_detail(null);

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

	public ArrayList<MasterRencana> getAllMasterRencana() {
		try {
			masterRencanaArraylist.clear();

			String selectQuery = "SELECT  trm.* FROM " +TABLE_MASTER_RENCANA +" trm JOIN "+ TABLE_DETAIL_RENCANA +" trd ON trm.id_rencana_header =" +
					" trd.id_rencana_header GROUP BY trm.id_rencana_header";

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					MasterRencana masterRencana = new MasterRencana();
					masterRencana.setId(cursor.getInt(0));
					masterRencana.setId_rencana_header(cursor.getInt(1));
					masterRencana.setNomor_rencana(cursor.getString(2));
					masterRencana.setTanggal_penetapan(cursor.getString(3));
					masterRencana.setTanggal_rencana(cursor.getString(4));
					masterRencana.setId_user_input_rencana(cursor.getInt(5));
					masterRencana.setKeterangan(cursor.getString(6));

					// Adding staff to list
					masterRencanaArraylist.add(masterRencana);
				} while (cursor.moveToNext());
			}else{
				do {
					MasterRencana masterRencana = new MasterRencana();
					masterRencana.setId(0);
					masterRencana.setId_rencana_header(0);
					masterRencana.setNomor_rencana(null);
					masterRencana.setTanggal_penetapan(null);
					masterRencana.setTanggal_rencana(null);
					masterRencana.setId_user_input_rencana(0);
					masterRencana.setKeterangan(null);

					// Adding staff to list
					masterRencanaArraylist.add(masterRencana);
				} while (cursor.moveToNext());
			}

			// return staff_list
			cursor.close();
			db.close();
			return masterRencanaArraylist;
		} catch (Exception e) {
			Log.e("Master_rencana_list", "" + e);
		}
		return masterRencanaArraylist;
	}
	public ArrayList<MasterRencanaParam> getMasterRencanaParam(String nomor_rencana) {
		try {
			masterRencanaParamArraylist.clear();
			String selectQuery = "SELECT  * FROM " +TABLE_MASTER_RENCANA +" WHERE nomor_rencana='"+nomor_rencana+"'";

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					MasterRencanaParam masterRencana = new MasterRencanaParam();
					masterRencana.setId(cursor.getInt(0));
					masterRencana.setId_rencana_header(cursor.getInt(1));
					masterRencana.setNomor_rencana(cursor.getString(2));
					masterRencana.setTanggal_penetapan(cursor.getString(3));
					masterRencana.setTanggal_rencana(cursor.getString(4));
					masterRencana.setId_user_input_rencana(cursor.getInt(5));
					masterRencana.setKeterangan(cursor.getString(6));

					// Adding staff to list
					masterRencanaParamArraylist.add(masterRencana);
				} while (cursor.moveToNext());
			}else{
				do {
					MasterRencanaParam masterRencana = new MasterRencanaParam();
					masterRencana.setId(0);
					masterRencana.setId_rencana_header(0);
					masterRencana.setNomor_rencana(null);
					masterRencana.setTanggal_penetapan(null);
					masterRencana.setTanggal_rencana(null);
					masterRencana.setId_user_input_rencana(0);
					masterRencana.setKeterangan(null);

					// Adding staff to list
					masterRencanaParamArraylist.add(masterRencana);
				} while (cursor.moveToNext());
			}

			// return staff_list
			cursor.close();
			db.close();
			return masterRencanaParamArraylist;
		} catch (Exception e) {
			Log.e("Master_rencana_list", "" + e);
		}
		return masterRencanaParamArraylist;
	}

	//get Max rencana master
	public ArrayList<MasterRencana> getMaxMasterRencana() {
		try {
			masterRencanaArraylist.clear();
			String selectQuery = "SELECT "+TABLE_MASTER_RENCANA+".* FROM (SELECT max("+KEY_DETAIL_RENCANA_ID_RENCANA_HEADER+
					") as "+KEY_DETAIL_RENCANA_ID_RENCANA_HEADER+" FROM " +TABLE_MASTER_RENCANA+") as data1 JOIN "+
					TABLE_MASTER_RENCANA+" ON "+TABLE_MASTER_RENCANA+"."+KEY_DETAIL_RENCANA_ID_RENCANA_HEADER+
					"=data1."+KEY_DETAIL_RENCANA_ID_RENCANA_HEADER;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					MasterRencana masterRencana = new MasterRencana();
					masterRencana.setId(cursor.getInt(0));
					masterRencana.setId_rencana_header(cursor.getInt(1));
					masterRencana.setNomor_rencana(cursor.getString(2));
					masterRencana.setTanggal_penetapan(cursor.getString(3));
					masterRencana.setTanggal_rencana(cursor.getString(4));
					masterRencana.setId_user_input_rencana(cursor.getInt(5));
					masterRencana.setKeterangan(cursor.getString(6));

					// Adding staff to list
					masterRencanaArraylist.add(masterRencana);
				} while (cursor.moveToNext());
			}else{
				do {
					MasterRencana masterRencana = new MasterRencana();
					masterRencana.setId(0);
					masterRencana.setId_rencana_header(0);
					masterRencana.setNomor_rencana(null);
					masterRencana.setTanggal_penetapan(null);
					masterRencana.setTanggal_rencana(null);
					masterRencana.setId_user_input_rencana(0);
					masterRencana.setKeterangan(null);

					// Adding staff to list
					masterRencanaArraylist.add(masterRencana);
				} while (cursor.moveToNext());
			}

			// return staff_list
			cursor.close();
			db.close();
			return masterRencanaArraylist;
		} catch (Exception e) {
			Log.e("Master_rencana_list", "" + e);
		}
		return masterRencanaArraylist;
	}

	//getting All user
	public ArrayList<DetailRencana> getStatusRencana(int id) {
		try {
			detailRencanaArraylist.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_DETAIL_RENCANA +" WHERE id_rencana_detail='"+id+"'";

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					DetailRencana detailRencana = new DetailRencana();
					detailRencana.setId_rencana_detail(cursor.getInt(0));
					detailRencana.setId_rencana_header(cursor.getInt(1));
					detailRencana.setId_kegiatan(cursor.getInt(2));
					detailRencana.setId_customer(cursor.getInt(3));
					detailRencana.setId_karyawan(cursor.getInt(4));
					detailRencana.setStatus_rencana(cursor.getInt(5));
					detailRencana.setNomor_rencana_detail(cursor.getString(6));

					// Adding staff to list
					detailRencanaArraylist.add(detailRencana);
				} while (cursor.moveToNext());
			}

			// return staff_list
			cursor.close();
			db.close();
			return detailRencanaArraylist;
		} catch (Exception e) {
			Log.e("status_rencana", "" + e);
		}
		return detailRencanaArraylist;
	}

	// Getting All Product
	public ArrayList<Product> getAllProductBaseOnSearch(String search) {
		try {
			productArrayList.clear();


			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT + " WHERE "
					+ KEY_PRODUCT_NAMA_PRODUCT + " LIKE '%" + search + "%'";

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Product product = new Product();
					product.setId_product(cursor.getInt(0));
					product.setNama_product(cursor.getString(1));

					// Adding product to list
					productArrayList.add(product);
				} while (cursor.moveToNext());
			}

			// return product_list
			cursor.close();
			db.close();
			return productArrayList;
		} catch (Exception e) {
			Log.e("product_list", "" + e);
		}

		return productArrayList;
	}
	// Getting All customer search
	public ArrayList<Mst_Customer> getAllCustomerBaseOnSearch(String search) {
		try {
			mst_customerArrayList.clear();
			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_MST_CUSTOMER + " WHERE "
					+ KEY_MST_CUSTOMER_NAMA_CUSTOMER + " LIKE '%" + search + "%' OR " + KEY_MST_CUSTOMER_ALAMAT +" LIKE '%"+search+"%'";
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Mst_Customer customer = new Mst_Customer();
					customer.setId_customer(cursor.getInt(0));
					customer.setNama_customer(cursor.getString(2));
					customer.setAlamat(cursor.getString(3));

					// Adding product to list
					mst_customerArrayList.add(customer);
				} while (cursor.moveToNext());
			}

			// return product_list
			cursor.close();
			db.close();
			return mst_customerArrayList;
		} catch (Exception e) {
			Log.e("customer_list", "" + e);
		}

		return mst_customerArrayList;
	}
	//getting All product
	public ArrayList<Product> getAllProduct() {
		try {
			productArrayList.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Product product = new Product();
					product.setId_product(cursor.getInt(0));
					product.setNama_product(cursor.getString(1));

					// Adding staff to list
					productArrayList.add(product);
				} while (cursor.moveToNext());
			}

			// return staff_list
			cursor.close();
			db.close();
			return productArrayList;
		} catch (Exception e) {
			Log.e("user_list", "" + e);
		}
		return productArrayList;
	}

	public ArrayList<Trx_Checkin> getAllCheckinNumber() {
		try {
			trx_checkinArrayList.clear();

			// Select All Query
			String selectQuery = "SELECT  tc.* FROM " + TABLE_TRX_CHECKIN +" tc JOIN "+TABLE_DETAIL_RENCANA+
					" trd ON tc.id_rencana_detail=trd.id_rencana_detail WHERE status_rencana='1'  OR tc.status='1'";

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
					trx_checkin.setStatus(cursor.getString(9));
					trx_checkin.setProspect(cursor.getString(10));

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
				cursor.getInt(5), cursor.getInt(6), cursor.getString(7), cursor.getInt(8));
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
						KEY_TRX_CHECKIN_ID_RENCANA_DETAIL,KEY_TRX_CHECKIN_ID_RENCANA_HEADER,KEY_TRX_CHECKIN_KODE_CUSTOMER,KEY_TRX_CHECKIN_LATS,KEY_TRX_CHECKIN_LONGS,
						KEY_TRX_CHECKIN_FOTO});

		if (cursor != null)
			cursor.moveToFirst();

		Trx_Checkin checkin = new Trx_Checkin(cursor.getString(0),
				cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4),
				cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9),cursor.getString(10));
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

			String selectQuery = "SELECT mc.* FROM "+TABLE_MST_CUSTOMER+" mc JOIN "+TABLE_DETAIL_RENCANA+" trd ON trd.id_customer = mc.id_customer" +
					" JOIN " +TABLE_MASTER_RENCANA+" trm ON trm.id_rencana_header = trd.id_rencana_header";

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
			}else{
				do {
					Mst_Customer customer = new Mst_Customer();
					customer.setId_customer(0);
					customer.setKode_customer(null);
					customer.setNama_customer(null);
					customer.setAlamat(null);
					customer.setNo_hp(null);
					customer.setLats(null);
					customer.setLongs(null);
					customer.setId_wilayah(0);

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
	public ArrayList<Mst_Customer> getAllCustomerOnly(String nama) {
		if(nama.equals("SUYANTO")||nama.equals("IT Dev")){
			try {
				mst_customerArrayList.clear();
				String selectQuery = "SELECT * FROM "+TABLE_MST_CUSTOMER ;

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
				}else{
					do {
						Mst_Customer customer = new Mst_Customer();
						customer.setId_customer(0);
						customer.setKode_customer(null);
						customer.setNama_customer(null);
						customer.setAlamat(null);
						customer.setNo_hp(null);
						customer.setLats(null);
						customer.setLongs(null);
						customer.setId_wilayah(0);

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
		}else{
			try {
				mst_customerArrayList.clear();
				String selectQuery = "SELECT * FROM "+TABLE_MST_CUSTOMER+ " WHERE "+KEY_MST_CUSTOMER_NO_HP+" LIKE '%"+nama+"%'" ;

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
				}else{
					do {
						Mst_Customer customer = new Mst_Customer();
						customer.setId_customer(0);
						customer.setKode_customer(null);
						customer.setNama_customer(null);
						customer.setAlamat(null);
						customer.setNo_hp(null);
						customer.setLats(null);
						customer.setLongs(null);
						customer.setId_wilayah(0);

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
	}

	public ArrayList<Mst_Customer> getAllCustomerParamRencana(int id_rencana_detail) {
		try {
			mst_customerArrayList.clear();

			String selectQuery = "SELECT mc.* FROM "+TABLE_MST_CUSTOMER+" mc JOIN "+TABLE_DETAIL_RENCANA+" tc ON mc.id_customer = tc.id_customer" +
					" WHERE tc.id_rencana_detail ='"+id_rencana_detail+"'";

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
	public ArrayList<Mst_Customer> getAllCustomerParamCheckin(String no_checkin) {
		try {
			mst_customerArrayList.clear();

			String selectQuery = "SELECT mc.* FROM "+TABLE_MST_CUSTOMER+" mc JOIN "+TABLE_TRX_CHECKIN+" tc ON mc.kode_customer = tc.kode_customer" +
					" WHERE tc.nomor_checkin ='"+no_checkin+"'";

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

	public ArrayList<TmpCustomer> getAllTmpCustomerParamCheckin(String no_checkin) {
		try {
			tmpCustomerArrayList.clear();

			String selectQuery = "SELECT tmp.* FROM "+TABLE_TMP_CUSTOMER+" tmp JOIN "+TABLE_TRX_CHECKIN+" tc ON tmp.kode_customer = tc.kode_customer" +
					" WHERE tc.nomor_checkin ='"+no_checkin+"'";

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					TmpCustomer tmpCustomer = new TmpCustomer();
					tmpCustomer.setId_customer_tmp(cursor.getInt(0));
					tmpCustomer.setKode_customer(cursor.getString(1));
					tmpCustomer.setNama_customer(cursor.getString(2));
					tmpCustomer.setNo_hp(cursor.getString(3));
					tmpCustomer.setAlamat(cursor.getString(4));
					tmpCustomer.setNama_usaha(cursor.getString(5));

					// Adding staff to list
					tmpCustomerArrayList.add(tmpCustomer);
				} while (cursor.moveToNext());
			}

			// return staff_list
			cursor.close();
			db.close();
			return tmpCustomerArrayList;
		} catch (Exception e) {
			Log.e("tmp_customer_list", "" + e);
		}
		return tmpCustomerArrayList;
	}

	public ArrayList<Mst_Customer> getAllCustomerParam(String kode) {
		try {
			mst_customerArrayList.clear();

			String selectQuery = "SELECT * FROM "+ TABLE_MST_CUSTOMER+" WHERE kode_customer = '"+kode+"'";

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

	public ArrayList<DetailRencana> getAlldetailRencanaParam(int id) {
		try {
			detailRencanaArraylist.clear();

			String selectQuery = "SELECT * FROM "+ TABLE_DETAIL_RENCANA+" WHERE id_rencana_detail = '"+id+"'";

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					DetailRencana detailRencana = new DetailRencana();
					detailRencana.setId_rencana_detail(cursor.getInt(0));
					detailRencana.setId_rencana_header(cursor.getInt(1));
					detailRencana.setId_kegiatan(cursor.getInt(2));
					detailRencana.setId_customer(cursor.getInt(3));
					detailRencana.setId_karyawan(cursor.getInt(4));
					detailRencana.setStatus_rencana(cursor.getInt(5));
					detailRencana.setNomor_rencana_detail(cursor.getString(6));

					// Adding staff to list
					detailRencanaArraylist.add(detailRencana);
				} while (cursor.moveToNext());
			}

			// return staff_list
			cursor.close();
			db.close();
			return detailRencanaArraylist;
		} catch (Exception e) {
			Log.e("mst_customer_list", "" + e);
		}
		return detailRencanaArraylist;
	}

	public ArrayList<MasterRencana> getAllmasterRencanaParam(int id) {
		try {
			masterRencanaArraylist.clear();

			String selectQuery = "SELECT * FROM "+ TABLE_MASTER_RENCANA+" WHERE id_rencana_header = '"+id+"'";

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					MasterRencana masterRencana = new MasterRencana();
					masterRencana.setId(cursor.getInt(0));
					masterRencana.setId_rencana_header(cursor.getInt(1));
					masterRencana.setNomor_rencana(cursor.getString(2));
					masterRencana.setTanggal_penetapan(cursor.getString(3));
					masterRencana.setTanggal_rencana(cursor.getString(4));
					masterRencana.setId_user_input_rencana(cursor.getInt(5));
					masterRencana.setKeterangan(cursor.getString(6));

					// Adding staff to list
					masterRencanaArraylist.add(masterRencana);
				} while (cursor.moveToNext());
			}

			// return staff_list
			cursor.close();
			db.close();
			return masterRencanaArraylist;
		} catch (Exception e) {
			Log.e("mst_customer_list", "" + e);
		}
		return masterRencanaArraylist;
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

	public int getCountUser() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db
				.rawQuery("select count(*) from " + TABLE_MST_USER1, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountProduct() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("SELECT count(*) from " + TABLE_PRODUCT, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getTanggal_rencana_available() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_MASTER_RENCANA, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCheckinAlready(String kode, int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("SELECT count(*) from " + TABLE_TRX_CHECKIN +" where kode_customer='" +kode+"' " +
						" AND id_rencana_detail = '"+id+"'", null);
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
	public int getCountMasterRencana() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db
				.rawQuery("select count(*) from " + TABLE_MASTER_RENCANA +" JOIN "+ TABLE_DETAIL_RENCANA+" ON "+
						TABLE_MASTER_RENCANA+"."+KEY_MASTER_RENCANA_ID_RENCANA_HEADER +"="+ TABLE_DETAIL_RENCANA+
						"."+KEY_DETAIL_RENCANA_ID_RENCANA_HEADER+" WHERE status_rencana !='2'", null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountRencanaDetailWhereValidAndUpdateAll() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db
				.rawQuery("select count(*) from " + TABLE_DETAIL_RENCANA +" WHERE status_rencana='1'", null);
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
		Cursor mCount = db.rawQuery("SELECT COUNT(tc.id_chekin) FROM " + TABLE_TRX_CHECKIN +" tc JOIN "+TABLE_DETAIL_RENCANA+
				" trd ON tc.id_rencana_detail=trd.id_rencana_detail WHERE status_rencana='1' OR tc.status='1'", null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}
	public int getCountAllTrxcheckin() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("SELECT COUNT(id_chekin) FROM " + TABLE_TRX_CHECKIN , null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}
	public int getCountUploadAllDataSapi() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_UPLOAD_DATA_SAPI , null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}
	public int getCountUploadDataSapi(int id_rencana_detail) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_UPLOAD_DATA_SAPI + " WHERE id_rencana_detail="+ id_rencana_detail, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}
	public int getCountTrxcheckinParam(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("SELECT COUNT(tc.id_chekin) FROM " + TABLE_TRX_CHECKIN +" tc JOIN "+TABLE_DETAIL_RENCANA+
				" trd ON tc.id_rencana_detail=trd.id_rencana_detail WHERE trd.id_rencana_detail="+id+" AND status_rencana='1' " +
				"OR trd.id_rencana_detail="+id+" AND tc.status='1'", null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountTrxcheckout(int id_rencana_detail) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_TRX_CHECKOUT + " Where id_rencana_detail="+id_rencana_detail, null);
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
	public int getStatusRencanaHeader(int id_rencana_header) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select aproved from " + TABLE_MASTER_RENCANA +" WHERE id_rencana_header='"+id_rencana_header+"'",
				null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}
	public int getCountSapi() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_DATA_SAPI +" WHERE lifnr='0'",
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

	public void deleteTableHistoryCanvassing() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_HISTORY_CANVASSING);
	}

	public void deleteTableMSTCustomer() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_MST_CUSTOMER);
	}public void deleteTableMSTUser() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_MST_USER1);
	}public void deleteTableRencanaDetail() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_DETAIL_RENCANA);
	}public void deleteTableDataSapi() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_DATA_SAPI);
	}public void deleteTableCheckin(String idRencanaDetail) {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_TRX_CHECKIN +" WHERE id_rencana_detail='"+idRencanaDetail+"'");
	}public void deleteTableUploadDataSapi() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_UPLOAD_DATA_SAPI );
	}public void deleteTableRencanaMaster() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_MASTER_RENCANA);
	}public void deleteCheckin() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_TRX_CHECKIN);
	}public void deleteCheckout() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_TRX_CHECKOUT);
	}public void deleteTableProduct() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_PRODUCT);
	}

	public void deleteRencanaMaster() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_MASTER_RENCANA);
	}public void deleteRencanaDetail() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_DETAIL_RENCANA);
	}

	public void updateCheckin(String nomor) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE "+TABLE_DETAIL_RENCANA+" SET "+KEY_DETAIL_RENCANA_STATUS_RENCANA+" ='1' where "+KEY_DETAIL_RENCANA_ID_RENCANA_DETAIL+
				" = '"+nomor+"'");
	}
	public void updateCheckout(String nomor) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE "+TABLE_DETAIL_RENCANA+" SET "+KEY_DETAIL_RENCANA_STATUS_RENCANA+" ='2' where "+KEY_DETAIL_RENCANA_ID_RENCANA_DETAIL+
				" = '"+nomor+"'");
	}
	public void updateRencanaDetailfromCheckout(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE "+TABLE_DETAIL_RENCANA+" SET "+KEY_DETAIL_RENCANA_STATUS_RENCANA+" ='2' where "+KEY_DETAIL_RENCANA_ID_RENCANA_DETAIL+
				" = '"+id+"'");
	}

	public void updateTrxCheckin(String nomor) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE "+TABLE_TRX_CHECKIN+" SET "+KEY_TRX_CHECKIN_STATUS+" ='2' where "+KEY_TRX_CHECKIN_NOMOR_CHECKIN+
				" = '"+nomor+"'");
	}
}
