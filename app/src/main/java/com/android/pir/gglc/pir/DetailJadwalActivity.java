package com.android.pir.gglc.pir;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.pir.gglc.absen.AppVar;
import com.android.pir.gglc.absen.NavigationDrawerFragment;
import com.android.pir.gglc.database.DatabaseHandler;
import com.android.pir.gglc.database.DetailRencana;
import com.android.pir.gglc.database.MstUser;
import com.android.pir.gglc.database.Trx_Checkin;
import com.android.pir.mobile.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;

@SuppressWarnings("deprecation")
public class DetailJadwalActivity extends FragmentActivity{
	private Context act;
	private Toolbar mToolbar;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private DatabaseHandler databaseHandler;
	private ListView listview;
	private LinearLayout layout;
//	private ArrayList<Absen> customer_list = new ArrayList<Absen>();
//	private ArrayList<Stock_customer> stock_customer_list = new ArrayList<Stock_customer>();
//	private ListViewAdapter cAdapter;
	private ProgressDialog progressDialog;
	private Handler handler = new Handler();
	private String message,msg_success;
	private String response_data;
	private static final String LOG_TAG = DetailJadwalActivity.class.getSimpleName();
	private EditText description_kegiatan;
	private Spinner checkin_number;
	private ArrayList<Trx_Checkin> checkinNumber;
	private ArrayList<String> checkinNumberStringList;
	private int id_checkin = 0;
	private String nomor_checkin_ = null;
	private int id_user,status_Rencana;
	private MstUser user;
	private DetailRencana detailRencana;
	private Typeface typefaceSmall;
	private TextView test,jml_ckin,jml_rcn,percent, nama,alamat,status,status_,status_1;
    private String response;
//    private Absen customer;
	private Button chat,map;
	private EditText mulai,sampai;
	private Button checkin,checkin1,checkout;
	private LinearLayout bar,bar1;
	final int Date_Dialog_ID=0;
	final int Date_Dialog_ID1=1;
	int cDay,cMonth,cYear;
	Calendar cDate;
	int sDay,sMonth,sYear;
	private double latitude, longitude;
	private Location location,location1,location2;
	protected LocationManager locationManager;
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	private DatabaseHandler db;
	private ViewPager viewPager;
	private ImageView menuBackButton;
	private Button btnUpload, refresh;
	private Button stockFisik,chiller, kompetitor,spg,displayProduct,penjualan;
	private Button btnFoto2;
	private Button btnFoto3;
	private Button btnFoto4;
	private String main_app_id_detail_jadwal;
	private String main_app_id_detail_nama;
	private String main_app_id_detail_alamat;
//	private String main_app_id_detail_status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_detail_jadwal);

		act = this;
		db = new DatabaseHandler(this);

		checkin = (Button)findViewById(R.id.checkin);
		checkin1 = (Button)findViewById(R.id.checkin1);
		stockFisik = (Button)findViewById(R.id.stockFisik);
		displayProduct = (Button)findViewById(R.id.displayProduct);
		chiller = (Button)findViewById(R.id.chiller);
		kompetitor = (Button)findViewById(R.id.kompetitor);
		spg = (Button)findViewById(R.id.spg);
		checkout = (Button)findViewById(R.id.checkout);
		penjualan = (Button)findViewById(R.id.penjualan);
		listview = (ListView) findViewById(R.id.list);
		nama = (TextView) findViewById(R.id.nama_customer);
		alamat = (TextView) findViewById(R.id.alamat_customer);
		status = (TextView) findViewById(R.id.status_kunjungan);
		status_ = (TextView) findViewById(R.id.status_);
		status_1 = (TextView) findViewById(R.id.status_1);
		bar = (LinearLayout) findViewById(R.id.bar_);
		bar1 = (LinearLayout) findViewById(R.id.bar_1);
		listview.setItemsCanFocus(false);

		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(getApplicationContext().getResources()
				.getString(R.string.app_name));
		progressDialog.setMessage("Downloading yaa...");
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);

		databaseHandler = new DatabaseHandler(this);

		ArrayList<MstUser> staff_list = databaseHandler.getAllUser();
		user = new MstUser();
		for (MstUser tempStaff : staff_list)
			user = tempStaff;
		id_user=user.getId_user();

		checkin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				gotoCheckin();
			}
		});
//		checkin1.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				gotoCheckin1();
//			}
//		});
//		stockFisik.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				gotoStockFisik();
//			}
//		});
//		displayProduct.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				gotoDisplay();
//			}
//		});
		chiller.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				gotoChiller();
			}
		});
//		kompetitor.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				gotoKompetitor();
//			}
//		});
//		spg.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				gotoSpg();
//			}
//		});
		checkout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				gotoCheckout();
			}
		});
//		penjualan.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				gotoPenjualan();
//			}
//		});

		menuBackButton = (ImageView) findViewById(R.id.menuBackButton);
		menuBackButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				gotoDashboard();
			}
		});

		SharedPreferences spPreferences = getSharedPrefereces();
		main_app_id_detail_jadwal = spPreferences.getString(
				AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_JADWAL, null);
		main_app_id_detail_nama = spPreferences.getString(
				AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_NAMA, null);
		main_app_id_detail_alamat = spPreferences.getString(
				AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_ALAMAT, null);
//		main_app_id_detail_status = spPreferences.getString(
//				AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_STATUS, null);

		ArrayList<DetailRencana> status_rencana = databaseHandler.getStatusRencana(Integer.parseInt(main_app_id_detail_jadwal));
		detailRencana = new DetailRencana();
		for (DetailRencana rencanatmp : status_rencana)
			detailRencana = rencanatmp;
		status_Rencana=detailRencana.getStatus_rencana();

		nama.setText(main_app_id_detail_nama);
		alamat.setText(main_app_id_detail_alamat);
		if (status_Rencana == 0) {
			status.setText("Baru");
			status_.setText("Anda belum melakukan checkin");
			bar.setBackgroundColor(Color.parseColor("#FF0000"));
		}else if (status_Rencana == 1){
			status.setText("Chekcin");
			status_.setText("Anda sudah checkin");
			bar.setBackgroundColor(Color.parseColor("#00FF00"));
			if(databaseHandler.getCountTrxcheckinParam(Integer.parseInt(main_app_id_detail_jadwal))==0){
				status_1.setText("Namun data checkin belum lengkap, mohon checkin kembali");
				bar1.setBackgroundColor(Color.parseColor("#FF0000"));
			}
		}else if (status_Rencana == 2){
			status.setText("Selesai (Checkout)");
		}

//		test.setText(main_app_id_detail_jadwal);

//		if(databaseHandler.getCountStockCustomerParam(Integer.parseInt(main_app_id_detail_jadwal))==0){
//			new DownloadDataStockCustomer().execute();
//		}else{
//			updateContentRefreshRencana();
//		}
		if(databaseHandler.getCountTrxcheckinParam(Integer.parseInt(main_app_id_detail_jadwal))==0){
			stockFisik.setClickable(false);
			displayProduct.setClickable(false);
			chiller.setClickable(false);
			kompetitor.setClickable(false);
			spg.setClickable(false);
			penjualan.setClickable(false);
			checkout.setClickable(false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main3, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_refresh:
				if (GlobalApp.checkInternetConnection(act)) {
//					new DownloadDataCustomer().execute();
//					updateContentRefreshRencana();
				} else {
					String message = act.getApplicationContext().getResources()
							.getString(R.string.app_customer_processing_empty);
					showCustomDialog(message);
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	//download data customer to sqlite
//	private class DownloadDataStockCustomer extends AsyncTask<String, Integer, String> {
//		@Override
//		protected void onPreExecute() {
//			progressDialog.setMessage(getApplicationContext().getResources()
//					.getString(R.string.MSG_DLG_LABEL_SYNRONISASI_DATA));
//			progressDialog.show();
//			progressDialog
//					.setOnCancelListener(new DialogInterface.OnCancelListener() {
//						@Override
//						public void onCancel(DialogInterface dialog) {
//							String msg = getApplicationContext()
//									.getResources()
//									.getString(
//											R.string.MSG_DLG_LABEL_SYNRONISASI_DATA_CANCEL);
//							showCustomDialog(msg);
//						}
//					});
//		}
//
//		@Override
//		protected String doInBackground(String... params) {
//
//			String download_data_url = AppVar.CONFIG_APP_URL_PUBLIC
//					+ AppVar.CONFIG_APP_URL_DOWNLOAD_STOCK+ "?id_rencana_detail="
//					+ main_app_id_detail_jadwal;
//			HttpResponse response = getDownloadData(download_data_url);
//			int retCode = (response != null) ? response.getStatusLine()
//					.getStatusCode() : -1;
//			if (retCode != 200) {
//				message = act.getApplicationContext().getResources()
//						.getString(R.string.MSG_DLG_LABEL_URL_NOT_FOUND);
//				handler.post(new Runnable() {
//					public void run() {
//						showCustomDialog(message);
//					}
//				});
//			} else {
//				try {
//					response_data = EntityUtils.toString(response.getEntity());
//
//					SharedPreferences spPreferences = getSharedPrefereces();
//					String main_app_table_data = spPreferences.getString(
//							AppVar.SHARED_PREFERENCES_TABLE_STOCK, null);
//					if (main_app_table_data != null) {
//						if (main_app_table_data.equalsIgnoreCase(response_data)) {
//							saveAppDataStockSameData(act
//									.getApplicationContext().getResources()
//									.getString(R.string.app_value_true));
//						} else {
////							db.deleteTableStock();
//							saveAppDataStockSameData(act
//									.getApplicationContext().getResources()
//									.getString(R.string.app_value_false));
//						}
//					} else {
////						db.deleteTableStock();
//						saveAppDataStockSameData(act.getApplicationContext()
//								.getResources()
//								.getString(R.string.app_value_false));
//					}
//				} catch (org.apache.http.ParseException e) {
//					message = e.toString();
//					handler.post(new Runnable() {
//						public void run() {
//							showCustomDialog(message);
//						}
//					});
//				} catch (IOException e) {
//					message = e.toString();
//					handler.post(new Runnable() {
//						public void run() {
//							showCustomDialog(message);
//						}
//					});
//				}
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			super.onPostExecute(result);
//			if (response_data != null) {
//				saveAppDataStock(response_data);
//				extractDataStock();
//				if (progressDialog != null) {
//					progressDialog.dismiss();
//				}
//				showCustomDialogStock("Data Stock Customer Berhasil Di Sikronkan dengan Server");
//
//			} else {
//				message = act.getApplicationContext().getResources()
//						.getString(R.string.MSG_DLG_LABEL_DOWNLOAD_FAILED);
//				handler.post(new Runnable() {
//					public void run() {
//						showCustomDialog(message);
//					}
//				});
//			}
//		}
//	}

//	public void updateContentRefreshRencana() {
//		stock_customer_list.clear();
//		ArrayList<Stock_customer> stock_customer_from_db = databaseHandler
//				.getAllStockCustomer(Integer.parseInt(main_app_id_detail_jadwal));
//
//		if (stock_customer_from_db.size() > 0) {
//			listview.setVisibility(View.VISIBLE);
//			for (int i = 0; i < stock_customer_from_db.size(); i++) {
//				String nama_product = stock_customer_from_db.get(i).getNama_product();
//				int qty = stock_customer_from_db.get(i).getQty();
//				String satuan = stock_customer_from_db.get(i).getSatuan();
//				String tanggal_update = stock_customer_from_db.get(i).getTanggal_update();
//
//				Stock_customer stock_customer = new Stock_customer();
//				stock_customer.setNama_product(nama_product);
//				stock_customer.setQty(qty);
//				stock_customer.setSatuan(satuan);
//				stock_customer.setTanggal_update(tanggal_update);
//
//				stock_customer_list.add(stock_customer);
//			}
//		} else {
//			listview.setVisibility(View.INVISIBLE);
//		}
//		showListStock();
//	}

//	public void showListStock() {
//		stock_customer_list.clear();
//		ArrayList<Stock_customer> stockrencana_from_db = null;
//		stockrencana_from_db = databaseHandler.getAllStockCustomer(Integer.parseInt(main_app_id_detail_jadwal));
//
//		if (stockrencana_from_db.size() > 0) {
//			listview.setVisibility(View.VISIBLE);
//			for (int i = 0; i < stockrencana_from_db.size(); i++) {
//				int id_stock = stockrencana_from_db.get(i).getId_stock();
//				int id_customer = stockrencana_from_db.get(i).getId_customer();
//				int id_product = stockrencana_from_db.get(i).getId_product();
//				String nama_product = stockrencana_from_db.get(i).getNama_product();
//				int qty = stockrencana_from_db.get(i).getQty();
//				String satuan = stockrencana_from_db.get(i).getSatuan();
//				String tanggal_update = stockrencana_from_db.get(i).getTanggal_update();
//
//				Stock_customer stock_customer = new Stock_customer();
//				stock_customer.setId_stock(id_stock);
//				stock_customer.setId_customer(id_customer);
//				stock_customer.setId_product(id_product);
//				stock_customer.setNama_product(nama_product);
//				stock_customer.setQty(qty);
//				stock_customer.setSatuan(satuan);
//				stock_customer.setTanggal_update(tanggal_update);
//
//				stock_customer_list.add(stock_customer);
//			}
//
//			cAdapter = new ListViewAdapter(this, R.layout.list_item_stock_customer,
//					stock_customer_list);
//			listview.setAdapter(cAdapter);
//			cAdapter.notifyDataSetChanged();
//		} else {
//			listview.setVisibility(View.INVISIBLE);
//		}
//	}

//	public class ListViewAdapter extends ArrayAdapter<Stock_customer> {
//		Activity activity;
//		int layoutResourceId;
//		Stock_customer stockCustomer;
//		ArrayList<Stock_customer> data = new ArrayList<Stock_customer>();
//
//		class UserHolder {
//			TextView list_namaProduct;
//			TextView list_qty;
//			TextView list_satuan;
//			TextView list_tanggal;
//		}
//
//		public ListViewAdapter(Activity act, int layoutResourceId,
//							   ArrayList<Stock_customer> data) {
//			super(act, layoutResourceId, data);
//			this.layoutResourceId = layoutResourceId;
//			this.activity = act;
//			this.data = data;
//			notifyDataSetChanged();
//		}
//
//		@Override
//		public View getView(final int position, View convertView,
//							ViewGroup parent) {
//			View row = convertView;
//			UserHolder holder = null;
//
//			if (row == null) {
//				LayoutInflater inflater = LayoutInflater.from(activity);
//				row = inflater.inflate(layoutResourceId, parent, false);
//				holder = new UserHolder();
//				holder.list_namaProduct= (TextView) row
//						.findViewById(R.id.customer_title_nama_product);
//				holder.list_qty = (TextView) row
//						.findViewById(R.id.customer_title_qty);
//				holder.list_satuan = (TextView) row
//						.findViewById(R.id.customer_title_satuan);
//				holder.list_tanggal = (TextView) row
//						.findViewById(R.id.customer_title_tanggal_update);
//				row.setTag(holder);
//			} else {
//				holder = (UserHolder) row.getTag();
//			}
//			stockCustomer = data.get(position);
//			holder.list_namaProduct.setText(stockCustomer.getNama_product());
//			holder.list_qty.setText(String.valueOf(stockCustomer.getQty()));
//			holder.list_satuan.setText(stockCustomer.getSatuan());
//			holder.list_tanggal.setText(stockCustomer.getTanggal_update());
//			holder.list_namaProduct.setTypeface(typefaceSmall);
//			holder.list_qty.setTypeface(typefaceSmall);
//			holder.list_satuan.setTypeface(typefaceSmall);
//
//			return row;
//		}
//	}

//	public void extractDataStock() {
//		SharedPreferences spPreferences = getSharedPrefereces();
//		String main_app_table_same_data = spPreferences.getString(
//				AppVar.SHARED_PREFERENCES_TABLE_STOCK_SAME_DATA, null);
//		String main_app_table = spPreferences.getString(
//				AppVar.SHARED_PREFERENCES_TABLE_STOCK, null);
//		if (main_app_table_same_data.equalsIgnoreCase(act
//				.getApplicationContext().getResources()
//				.getString(R.string.app_value_false))) {
//			JSONObject oResponse;
//			try {
//				oResponse = new JSONObject(main_app_table);
//				JSONArray jsonarr = oResponse.getJSONArray("stock_customer");
//				for (int i = 0; i < jsonarr.length(); i++) {
//					JSONObject oResponsealue = jsonarr.getJSONObject(i);
//					String id_stock = oResponsealue.isNull("id_stock") ? null
//							: oResponsealue.getString("id_stock");
//					String id_customer = oResponsealue.isNull("id_customer") ? null
//							: oResponsealue.getString("id_customer");
//					String id_product = oResponsealue.isNull("id_product") ? null
//							: oResponsealue.getString("id_product");
//					String nama_product = oResponsealue.isNull("nama_product") ? null
//							: oResponsealue.getString("nama_product");
//					String satuan = oResponsealue.isNull("nama_satuan") ? null
//							: oResponsealue.getString("nama_satuan");
//					String qty = oResponsealue.isNull("qty") ? null
//							: oResponsealue.getString("qty");
//					String tanggal_update = oResponsealue.isNull("tanggal_update") ? null
//							: oResponsealue.getString("tanggal_update");
//					Log.d(LOG_TAG, "id_stock:" + id_stock);
//					Log.d(LOG_TAG, "id_product:" + id_product);
//					Log.d(LOG_TAG, "nama_product:" + nama_product);
//					db.addStockCustomer(new Stock_customer(Integer.parseInt(id_stock),Integer.parseInt(id_customer),Integer.parseInt(id_product),nama_product,satuan,Integer.parseInt(qty),tanggal_update));
//				}
//
//			} catch (JSONException e) {
//				final String message = e.toString();
//				handler.post(new Runnable() {
//					public void run() {
//						showCustomDialog(message);
//					}
//				});
//
//			}
//		}
//	}

	private SharedPreferences getSharedPrefereces() {
		return act.getSharedPreferences(AppVar.SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
	}

	public void saveAppDataStock(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_TABLE_STOCK, responsedata);
		editor.commit();
	}

	public void saveAppDataStockSameData(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_TABLE_STOCK_SAME_DATA,
				responsedata);
		editor.commit();
	}

	public HttpResponse getDownloadData(String url) {
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		HttpResponse response;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			response = client.execute(get);
		} catch (UnsupportedEncodingException e1) {
			response = null;
		} catch (Exception e) {
			e.printStackTrace();
			response = null;
		}

		return response;
	}

	@Override
	public void onBackPressed() {
		if (mNavigationDrawerFragment.isDrawerOpen())
			mNavigationDrawerFragment.closeDrawer();
		else
			super.onBackPressed();
	}

	public void showCustomDialogExit() {
		String msg = getApplicationContext().getResources().getString(
				R.string.MSG_DLG_LABEL_EXIT_DIALOG);
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				act);
		alertDialogBuilder
				.setMessage(msg)
				.setCancelable(false)
				.setNegativeButton(
						getApplicationContext().getResources().getString(
								R.string.MSG_DLG_LABEL_YES),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								android.os.Process
										.killProcess(android.os.Process.myPid());

							}
						})
				.setPositiveButton(
						getApplicationContext().getResources().getString(
								R.string.MSG_DLG_LABEL_NO),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								AlertDialog alertDialog = alertDialogBuilder
										.create();
								alertDialog.dismiss();

							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void showCustomDialog(String msg) {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				act);
		alertDialogBuilder
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton(
						act.getApplicationContext().getResources()
								.getString(R.string.MSG_DLG_LABEL_OK),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								AlertDialog alertDialog = alertDialogBuilder
										.create();
								alertDialog.dismiss();

							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

//	public void showCustomDialogStock(String msg) {
//		if (progressDialog != null) {
//			progressDialog.dismiss();
//		}
//		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//				act);
//		alertDialogBuilder
//				.setMessage(msg)
//				.setCancelable(false)
//				.setPositiveButton(
//						act.getApplicationContext().getResources()
//								.getString(R.string.MSG_DLG_LABEL_OK),
//						new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialog, int id) {
//								AlertDialog alertDialog = alertDialogBuilder
//										.create();
//								alertDialog.dismiss();
//								updateContentRefreshRencana();
//							}
//						});
//		AlertDialog alertDialog = alertDialogBuilder.create();
//		alertDialog.show();
//
//	}

	public void gotoCheckin(){
		Intent intentActivity = new Intent(this,
				CheckinOneFragmentActivity.class);
		startActivity(intentActivity);
		finish();
	}
//	public void gotoCheckin1(){
//		Intent intentActivity = new Intent(this,
//				CheckinOneFragmentActivity1.class);
//		startActivity(intentActivity);
//		finish();
//	}

//	public void gotoStockFisik(){
//		Intent intentActivity = new Intent(this,
//				DataStockFisik.class);
//		startActivity(intentActivity);
//		finish();
//	}

//	public void gotoDisplay(){
//		Intent intentActivity = new Intent(this,
//				DisplayActivity.class);
//		startActivity(intentActivity);
//		finish();
//	}

	public void gotoChiller(){
		Intent intentActivity = new Intent(this,
				Chiller.class);
		startActivity(intentActivity);
		finish();
	}

//	public void gotoKompetitor(){
//		Intent intentActivity = new Intent(this,
//				Kompetitor.class);
//		startActivity(intentActivity);
//		finish();
//	}

//	public void gotoSpg(){
//		Intent intentActivity = new Intent(this,
//				Spg.class);
//		startActivity(intentActivity);
//		finish();
//	}

	public void gotoCheckout(){
		Intent intentActivity = new Intent(this,
				CheckoutActivityA.class);
		startActivity(intentActivity);
		finish();
	}
//	public void gotoPenjualan(){
//		Intent intentActivity = new Intent(this,
//				DataPenjualan.class);
//		startActivity(intentActivity);
//		finish();
//	}

	public void gotoDashboard() {
		Intent i = new Intent(this, DashboardActivity.class);
		startActivity(i);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			gotoDashboard();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
