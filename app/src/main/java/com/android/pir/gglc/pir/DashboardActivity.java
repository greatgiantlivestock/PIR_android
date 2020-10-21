package com.android.pir.gglc.pir;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Icon;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.pir.gglc.absen.AppVar;
import com.android.pir.gglc.absen.ChangePassword;
import com.android.pir.gglc.absen.NavigationDrawerCallbacks;
import com.android.pir.gglc.absen.NavigationDrawerFragment;
import com.android.pir.gglc.absen.Orderan;
import com.android.pir.gglc.database.Absen;
import com.android.pir.gglc.database.DatabaseHandler;
import com.android.pir.gglc.database.DetailRencana;
import com.android.pir.gglc.database.MasterRencana;
import com.android.pir.gglc.database.Rencana;
import com.android.pir.gglc.database.MstUser;
import com.android.pir.gglc.database.Mst_Customer;
import com.android.pir.gglc.database.Trx_Checkin;
import com.android.pir.mobile.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;

import static com.android.pir.gglc.absen.AppVar.SHARED_PREFERENCES_NAME;

@SuppressWarnings("deprecation")
public class DashboardActivity extends ActionBarActivity implements
		NavigationDrawerCallbacks {
	private Context act;
	private Toolbar mToolbar;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private DatabaseHandler databaseHandler;
	private ListView listview;
	private LinearLayout layout;
	private ArrayList<Absen> customer_list = new ArrayList<Absen>();
	private ArrayList<Rencana> rencana_list = new ArrayList<Rencana>();
	private ListViewAdapter cAdapter;
	private ProgressDialog progressDialog;
	private Handler handler = new Handler();
	private String message,msg_success;
	private String response_data;
	private static final String LOG_TAG = DashboardActivity.class.getSimpleName();
	private EditText description_kegiatan;
	private Spinner checkin_number;
	private ArrayList<Trx_Checkin> checkinNumber;
	private ArrayList<String> checkinNumberStringList;
	private int id_checkin = 0;
	private String nomor_checkin_ = null;
	private int id_user;
	private MstUser user;
	private Typeface typefaceSmall;
	private TextView jml_ckin,jml_rcn,percent;
    private String response;
    private Absen customer;
	private Button chat,map;
	private EditText mulai,sampai;
	private Button checkin,checkout;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main_dashboard);

		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle(R.string.title_activity_all);
		act = this;
		db = new DatabaseHandler(this);

		checkin = (Button)findViewById(R.id.checkin);
		checkout = (Button)findViewById(R.id.checkout);
//		jml_ckin = (TextView)findViewById(R.id.jml_ckin);
//		jml_rcn = (TextView)findViewById(R.id.jml_rcn);
//		percent = (TextView)findViewById(R.id.percentase);
		listview = (ListView) findViewById(R.id.list);
		listview.setItemsCanFocus(false);

		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(getApplicationContext().getResources()
				.getString(R.string.app_name));
		progressDialog.setMessage("Downloading yaa...");
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.fragment_drawer);
		mNavigationDrawerFragment.setup(R.id.fragment_drawer,
				(DrawerLayout) findViewById(R.id.drawer), mToolbar);
		databaseHandler = new DatabaseHandler(this);
		mNavigationDrawerFragment.selectItem(0);

		ArrayList<MstUser> staff_list = databaseHandler.getAllUser();
		user = new MstUser();
		for (MstUser tempStaff : staff_list)
			user = tempStaff;
		id_user=user.getId_user();

//		getCkin();
//		getRencana();
		updateContentRefreshRencana();

		checkin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				gotoCheckin();
			}
		});

		checkout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				gotoCheckout();
			}
		});

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
					new DownloadDataCustomer().execute();
//					updateContentRefreshRencana();
					new DownloadDataRencanaDetail().execute();
					db.deleteTableStock();
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
	private class DownloadDataCustomer extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog.setMessage(getApplicationContext().getResources()
					.getString(R.string.MSG_DLG_LABEL_SYNRONISASI_DATA));
			progressDialog.show();
			progressDialog
					.setOnCancelListener(new DialogInterface.OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							String msg = getApplicationContext()
									.getResources()
									.getString(
											R.string.MSG_DLG_LABEL_SYNRONISASI_DATA_CANCEL);
							showCustomDialog(msg);
						}
					});
		}

		@Override
		protected String doInBackground(String... params) {
			SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
			String id_wilayah= prefs.getString("id_wilayah","null");

			String download_data_url = AppVar.CONFIG_APP_URL_PUBLIC
					+ AppVar.CONFIG_APP_URL_DOWNLOAD_CUSTOMER+ "?id_wilayah="
					+ id_wilayah;
			HttpResponse response = getDownloadData(download_data_url);
			int retCode = (response != null) ? response.getStatusLine()
					.getStatusCode() : -1;
			if (retCode != 200) {
				message = act.getApplicationContext().getResources()
						.getString(R.string.MSG_DLG_LABEL_URL_NOT_FOUND);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(message);
					}
				});
			} else {
				try {
					response_data = EntityUtils.toString(response.getEntity());

					SharedPreferences spPreferences = getSharedPrefereces();
					String main_app_table_data = spPreferences.getString(
							AppVar.SHARED_PREFERENCES_TABLE_MST_CUSTOMER, null);
					if (main_app_table_data != null) {
						if (main_app_table_data.equalsIgnoreCase(response_data)) {
							saveAppDataBranchSameData(act
									.getApplicationContext().getResources()
									.getString(R.string.app_value_true));
						} else {
							db.deleteTableMSTCustomer();
							saveAppDataBranchSameData(act
									.getApplicationContext().getResources()
									.getString(R.string.app_value_false));
						}
					} else {
						db.deleteTableMSTCustomer();
						saveAppDataBranchSameData(act.getApplicationContext()
								.getResources()
								.getString(R.string.app_value_false));
					}
				} catch (org.apache.http.ParseException e) {
					message = e.toString();
					handler.post(new Runnable() {
						public void run() {
							showCustomDialog(message);
						}
					});
				} catch (IOException e) {
					message = e.toString();
					handler.post(new Runnable() {
						public void run() {
							showCustomDialog(message);
						}
					});
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (response_data != null) {
				saveAppDataBranch(response_data);
				extractDataBranch();
				if (progressDialog != null) {
					progressDialog.dismiss();
					//new DownloadDataKegiatan().execute();
				}
				showCustomDialog("Data Customer Berhasil Di Sikronkan dengan Server");
				//new DownloadDataKegiatan().execute();
				//new DownloadDataTypeCustomer().execute();
			} else {
				message = act.getApplicationContext().getResources()
						.getString(R.string.MSG_DLG_LABEL_DOWNLOAD_FAILED);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(message);
					}
				});
			}
		}
	}

	public void updateContentRefreshRencana() {
		rencana_list.clear();
		ArrayList<Rencana> rencana_from_db = databaseHandler
				.getAllRencana();
		if(databaseHandler.getCountDetailrencana()==0){
			new DownloadDataRencanaDetail().execute();
		}

		if (rencana_from_db.size() > 0) {
			listview.setVisibility(View.VISIBLE);
			for (int i = 0; i < rencana_from_db.size(); i++) {
				int id_rencana_detail = rencana_from_db.get(i).getId_rencana_detail();
				String nama_customer = rencana_from_db.get(i).getNama_customer();
				String alamat = rencana_from_db.get(i).getAlamat();
				int status_renana = rencana_from_db.get(i).getStatus();

				Rencana rencana = new Rencana();
				rencana.setId_rencana_detail(id_rencana_detail);
				rencana.setNama_customer(nama_customer);
				rencana.setAlamat(alamat);
				rencana.setStatus(status_renana);

				rencana_list.add(rencana);
			}
		} else {
			listview.setVisibility(View.INVISIBLE);
		}
		showListRencana();
	}

	public void showListRencana() {
		rencana_list.clear();
//		SharedPreferences spPreferences = getSharedPrefereces();
		ArrayList<Rencana> rencana_from_db = null;
		rencana_from_db = databaseHandler.getAllRencana();

		if (rencana_from_db.size() > 0) {
			listview.setVisibility(View.VISIBLE);
			for (int i = 0; i < rencana_from_db.size(); i++) {
				int id_rencana_detail = rencana_from_db.get(i).getId_rencana_detail();
				String nama_customer = rencana_from_db.get(i).getNama_customer();
				String alamat = rencana_from_db.get(i).getAlamat();
				int status = rencana_from_db.get(i).getStatus();
				String tanggal_rencana = rencana_from_db.get(i).getTanggal_rencana();

				Rencana rencana = new Rencana();
				rencana.setId_rencana_detail(id_rencana_detail);
				rencana.setNama_customer(nama_customer);
				rencana.setAlamat(alamat);
				rencana.setStatus(status);
				rencana.setTanggal_rencana(tanggal_rencana);

				rencana_list.add(rencana);
			}

			cAdapter = new ListViewAdapter(this, R.layout.list_item_customer_rencana,
					rencana_list);
			listview.setAdapter(cAdapter);
			cAdapter.notifyDataSetChanged();
		} else {
			listview.setVisibility(View.INVISIBLE);
		}
	}

	private class DownloadDataRencanaDetail extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog.setMessage(getApplicationContext().getResources()
					.getString(R.string.MSG_DLG_LABEL_SYNRONISASI_DATA));
			progressDialog.show();
			progressDialog
					.setOnCancelListener(new DialogInterface.OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							String msg = getApplicationContext()
									.getResources()
									.getString(
											R.string.MSG_DLG_LABEL_SYNRONISASI_DATA_CANCEL);
							showCustomDialog(msg);
						}
					});
		}

		@Override
		protected String doInBackground(String... params) {
			SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
			String id_karyawan = prefs.getString("id_awo","null");

			String download_data_url = AppVar.CONFIG_APP_URL_PUBLIC
					+ AppVar.CONFIG_APP_URL_DOWNLOAD_RENCANA_DETAIL+ "?id_karyawan="
					+ id_karyawan;
			HttpResponse response = getDownloadData(download_data_url);
			int retCode = (response != null) ? response.getStatusLine()
					.getStatusCode() : -1;
			if (retCode != 200) {
				message = act.getApplicationContext().getResources()
						.getString(R.string.MSG_DLG_LABEL_URL_NOT_FOUND);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(message);
					}
				});
			} else {
				try {
					response_data = EntityUtils.toString(response.getEntity());

					SharedPreferences spPreferences = getSharedPrefereces();
					String main_app_table_data = spPreferences.getString(
							AppVar.SHARED_PREFERENCES_TABLE_RENCANA_DETAIL, null);
					if (main_app_table_data != null) {
						if (main_app_table_data.equalsIgnoreCase(response_data)) {
							saveAppDataRencanaDetailSameData(act
									.getApplicationContext().getResources()
									.getString(R.string.app_value_true));
						} else {
							databaseHandler.deleteTableRencanaDetail();
							saveAppDataRencanaDetailSameData(act
									.getApplicationContext().getResources()
									.getString(R.string.app_value_false));
						}
					} else {
						databaseHandler.deleteTableRencanaDetail();
						saveAppDataRencanaDetailSameData(act.getApplicationContext()
								.getResources()
								.getString(R.string.app_value_false));
					}
				} catch (org.apache.http.ParseException e) {
					message = e.toString();
					handler.post(new Runnable() {
						public void run() {
							showCustomDialog(message);
						}
					});
				} catch (IOException e) {
					message = e.toString();
					handler.post(new Runnable() {
						public void run() {
							showCustomDialog(message);
						}
					});
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (response_data != null) {
				saveAppDataRencanaDetail(response_data);
				extractDataRencanaDetail();
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				new DownloadDataRencanaMaster().execute();
			} else {
				message = act.getApplicationContext().getResources()
						.getString(R.string.MSG_DLG_LABEL_DOWNLOAD_FAILED);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(message);
					}
				});
			}
		}
	}

	private class DownloadDataRencanaMaster extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog.setMessage(getApplicationContext().getResources()
					.getString(R.string.MSG_DLG_LABEL_SYNRONISASI_DATA));
			progressDialog.show();
			progressDialog
					.setOnCancelListener(new DialogInterface.OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							String msg = getApplicationContext()
									.getResources()
									.getString(
											R.string.MSG_DLG_LABEL_SYNRONISASI_DATA_CANCEL);
							showCustomDialog(msg);
						}
					});
		}

		@Override
		protected String doInBackground(String... params) {
			SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
			String id_karyawan = prefs.getString("id_awo","null");

			String download_data_url = AppVar.CONFIG_APP_URL_PUBLIC
					+ AppVar.CONFIG_APP_URL_DOWNLOAD_RENCANA_MASTER+ "?id_karyawan="
					+ id_karyawan;
			HttpResponse response = getDownloadData(download_data_url);
			int retCode = (response != null) ? response.getStatusLine()
					.getStatusCode() : -1;
			if (retCode != 200) {
				message = act.getApplicationContext().getResources()
						.getString(R.string.MSG_DLG_LABEL_URL_NOT_FOUND);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(message);
					}
				});
			} else {
				try {
					response_data = EntityUtils.toString(response.getEntity());

					SharedPreferences spPreferences = getSharedPrefereces();
					String main_app_table_data = spPreferences.getString(
							AppVar.SHARED_PREFERENCES_TABLE_RENCANA_MASTER, null);
					if (main_app_table_data != null) {
						if (main_app_table_data.equalsIgnoreCase(response_data)) {
							saveAppDataRencanaMasterSameData(act
									.getApplicationContext().getResources()
									.getString(R.string.app_value_true));
						} else {
							databaseHandler.deleteTableRencanaMaster();
							saveAppDataRencanaMasterSameData(act
									.getApplicationContext().getResources()
									.getString(R.string.app_value_false));
						}
					} else {
						databaseHandler.deleteTableRencanaMaster();
						saveAppDataRencanaMasterSameData(act.getApplicationContext()
								.getResources()
								.getString(R.string.app_value_false));
					}
				} catch (org.apache.http.ParseException e) {
					message = e.toString();
					handler.post(new Runnable() {
						public void run() {
							showCustomDialog(message);
						}
					});
				} catch (IOException e) {
					message = e.toString();
					handler.post(new Runnable() {
						public void run() {
							showCustomDialog(message);
						}
					});
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (response_data != null) {
				saveAppDataRencanaMaster(response_data);
				extractDataRencanaMaster();
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				msg_success=act.getApplicationContext().getResources()
						.getString(R.string.MSG_DLG_LABEL_SYNRONISASI_DATA_SUCCESS);
				showCustomDialog(msg_success);
			} else {
				message = act.getApplicationContext().getResources()
						.getString(R.string.MSG_DLG_LABEL_DOWNLOAD_FAILED);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(message);
					}
				});
			}
			showListRencana();
		}
	}

	public void saveAppDataRencanaDetail(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_TABLE_RENCANA_DETAIL, responsedata);
		editor.commit();
	}

	public void saveAppDataRencanaMaster(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_TABLE_RENCANA_MASTER, responsedata);
		editor.commit();
	}

	public void saveAppDataRencanaDetailSameData(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_TABLE_RENCANA_DETAIL_SAME_DATA,
				responsedata);
		editor.commit();
	}

	public void saveAppDataRencanaMasterSameData(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_TABLE_RENCANA_MASTER_SAME_DATA,
				responsedata);
		editor.commit();
	}

	public void extractDataRencanaDetail() {
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_table_same_data = spPreferences.getString(
				AppVar.SHARED_PREFERENCES_TABLE_RENCANA_DETAIL_SAME_DATA, null);
		String main_app_table = spPreferences.getString(
				AppVar.SHARED_PREFERENCES_TABLE_RENCANA_DETAIL, null);
		if (main_app_table_same_data.equalsIgnoreCase(act
				.getApplicationContext().getResources()
				.getString(R.string.app_value_false))) {
			JSONObject oResponse;
			try {
				oResponse = new JSONObject(main_app_table);
				JSONArray jsonarr = oResponse.getJSONArray("rencana_detail");
				for (int i = 0; i < jsonarr.length(); i++) {
					JSONObject oResponsealue = jsonarr.getJSONObject(i);
					String id_rencana_detail = oResponsealue.isNull("id_rencana_detail") ? null
							: oResponsealue.getString("id_rencana_detail");
					String id_rencana_header = oResponsealue.isNull("id_rencana_header") ? null
							: oResponsealue.getString("id_rencana_header");
					String id_kegiatan = oResponsealue.isNull("id_kegiatan") ? null
							: oResponsealue.getString("id_kegiatan");
					String id_customer = oResponsealue.isNull("id_customer") ? null
							: oResponsealue.getString("id_customer");
					String id_karyawan = oResponsealue.isNull("id_karyawan") ? null
							: oResponsealue.getString("id_karyawan");
					String status_rencana = oResponsealue.isNull("status_rencana") ? null
							: oResponsealue.getString("status_rencana");
					String nomor_rencana_detail = oResponsealue.isNull("nomor_rencana_detail") ? null
							: oResponsealue.getString("nomor_rencana_detail");
					Log.d(LOG_TAG, "id_rencana_detail:" + id_rencana_detail);
					Log.d(LOG_TAG, "id_rencana_header:" + id_rencana_header);
					Log.d(LOG_TAG, "id_kegiatan:" + id_kegiatan);
					Log.d(LOG_TAG, "id_customer:" + id_customer);
					databaseHandler.addDetailRencana(new DetailRencana(Integer.parseInt(id_rencana_detail),Integer.parseInt(id_rencana_header),
							Integer.parseInt(id_kegiatan),Integer.parseInt(id_customer),Integer.parseInt(id_karyawan),
							Integer.parseInt(status_rencana),nomor_rencana_detail));
				}
			} catch (JSONException e) {
				final String message = e.toString();
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(message);
					}
				});
			}
		}
	}

	public void extractDataRencanaMaster() {
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_table_rm_same_data = spPreferences.getString(
				AppVar.SHARED_PREFERENCES_TABLE_RENCANA_MASTER_SAME_DATA, null);
		String main_app_table = spPreferences.getString(
				AppVar.SHARED_PREFERENCES_TABLE_RENCANA_MASTER, null);
		if (main_app_table_rm_same_data.equalsIgnoreCase(act
				.getApplicationContext().getResources()
				.getString(R.string.app_value_false))) {
			JSONObject oResponse;
			try {
				oResponse = new JSONObject(main_app_table);
				JSONArray jsonarr = oResponse.getJSONArray("rencana_master");
				for (int i = 0; i < jsonarr.length(); i++) {
					JSONObject oResponsealue = jsonarr.getJSONObject(i);
					String id_rencana_header = oResponsealue.isNull("id_rencana_header") ? null
							: oResponsealue.getString("id_rencana_header");
					String nomor_rencana = oResponsealue.isNull("nomor_rencana") ? null
							: oResponsealue.getString("nomor_rencana");
					String tanggal_penetapan = oResponsealue.isNull("tanggal_penetapan") ? null
							: oResponsealue.getString("tanggal_penetapan");
					String tanggal_rencana = oResponsealue.isNull("tanggal_rencana") ? null
							: oResponsealue.getString("tanggal_rencana");
					String id_user_input_rencana = oResponsealue.isNull("id_user_input_rencana") ? null
							: oResponsealue.getString("id_user_input_rencana");
					String keterangan = oResponsealue.isNull("keterangan") ? null
							: oResponsealue.getString("keterangan");
					Log.d(LOG_TAG, "id_rencana_header:" + id_rencana_header);
					Log.d(LOG_TAG, "nomor_rencana:" + nomor_rencana);
					databaseHandler.addMasterRencana(new MasterRencana(Integer.parseInt(id_rencana_header),nomor_rencana,
							tanggal_penetapan,tanggal_rencana,Integer.parseInt(id_user_input_rencana),keterangan));
				}
			} catch (JSONException e) {
				final String message = e.toString();
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(message);
					}
				});
			}
		}
	}

	public class ListViewAdapter extends ArrayAdapter<Rencana> {
		Activity activity;
		int layoutResourceId;
		Rencana rencanaData;
		ArrayList<Rencana> data = new ArrayList<Rencana>();

		public ListViewAdapter(Activity act, int layoutResourceId,
							   ArrayList<Rencana> data) {
			super(act, layoutResourceId, data);
			this.layoutResourceId = layoutResourceId;
			this.activity = act;
			this.data = data;
			notifyDataSetChanged();
		}

		@Override
		public View getView(final int position, View convertView,
							ViewGroup parent) {
			View row = convertView;
			UserHolder holder = null;

			if (row == null) {
				LayoutInflater inflater = LayoutInflater.from(activity);
				row = inflater.inflate(layoutResourceId, parent, false);
				holder = new UserHolder();
				holder.list_namaCustomer = (TextView) row
						.findViewById(R.id.customer_title_nama_customer);
				holder.list_alamatCustomer = (TextView) row
						.findViewById(R.id.customer_title_alamat_customer);
				holder.list_status = (TextView) row
						.findViewById(R.id.customer_title_status_customer);
				holder.list_tanggal = (TextView) row
						.findViewById(R.id.customer_title_tanggal_rencana);
				row.setTag(holder);
			} else {
				holder = (UserHolder) row.getTag();
			}
			rencanaData = data.get(position);
			holder.list_namaCustomer.setText(rencanaData.getNama_customer());
			holder.list_alamatCustomer.setText(rencanaData.getAlamat());
			if (rencanaData.getStatus() == 0) {
				holder.list_status.setText("Baru");
			}else if (rencanaData.getStatus() == 1){
				holder.list_status.setText("Checkin");
			}else if (rencanaData.getStatus() == 2){
				holder.list_status.setText("Selesai");
			}
			holder.list_tanggal.setText(rencanaData.getTanggal_rencana());
			holder.list_namaCustomer.setTypeface(typefaceSmall);
			holder.list_alamatCustomer.setTypeface(typefaceSmall);
			holder.list_status.setTypeface(typefaceSmall);

			row.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String rencana_detail = String.valueOf(data.get(position).getId_rencana_detail());
					String nama = String.valueOf(data.get(position).getNama_customer());
					String alamat = String.valueOf(data.get(position).getAlamat());
					String status = String.valueOf(data.get(position).getStatus());
					saveAppDataDetailJadwal(rencana_detail);
					saveAppDataNamaCst(nama);
					saveAppDataAlamatCst(alamat);
					saveAppDataStatusCst(status);
					gotoDetailJadwal();
				}
			});

			return row;
		}

		class UserHolder {
			TextView list_namaCustomer;
			TextView list_alamatCustomer;
			TextView list_status;
			TextView list_tanggal;
		}
	}

	public void saveAppDataDetailJadwal(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_JADWAL,
				responsedata);
		editor.commit();
	}
	public void saveAppDataNamaCst(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_NAMA,
				responsedata);
		editor.commit();
	}
	public void saveAppDataAlamatCst(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_ALAMAT,
				responsedata);
		editor.commit();
	}
	public void saveAppDataStatusCst(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_STATUS,
				responsedata);
		editor.commit();
	}

	public void gotoDetailJadwal() {
//		Intent i = new Intent(this, DetailJadwalActivity.class);
		Intent i = new Intent(this, DashboardTabsActivity.class);
		startActivity(i);
		finish();
	}

	public void extractDataBranch() {
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_table_same_data = spPreferences.getString(
				AppVar.SHARED_PREFERENCES_TABLE_MST_CUSTOMER_SAME_DATA, null);
		String main_app_table = spPreferences.getString(
				AppVar.SHARED_PREFERENCES_TABLE_MST_CUSTOMER, null);
		if (main_app_table_same_data.equalsIgnoreCase(act
				.getApplicationContext().getResources()
				.getString(R.string.app_value_false))) {
			JSONObject oResponse;
			try {
				oResponse = new JSONObject(main_app_table);
				JSONArray jsonarr = oResponse.getJSONArray("customer");
				for (int i = 0; i < jsonarr.length(); i++) {
					JSONObject oResponsealue = jsonarr.getJSONObject(i);
					String id_customer = oResponsealue.isNull("id_customer") ? null
							: oResponsealue.getString("id_customer");
					String kode_customer = oResponsealue.isNull("kode_customer") ? null
							: oResponsealue.getString("kode_customer");
					String nama_customer = oResponsealue.isNull("nama_customer") ? null
							: oResponsealue.getString("nama_customer");
					String alamat = oResponsealue.isNull("alamat") ? null
							: oResponsealue.getString("alamat");
					String no_hp = oResponsealue.isNull("no_hp") ? null
							: oResponsealue.getString("no_hp");
					String lats = oResponsealue.isNull("lats") ? null
							: oResponsealue.getString("lats");
					String longs = oResponsealue.isNull("longs") ? null
							: oResponsealue.getString("longs");
					String id_wilayah = oResponsealue.isNull("id_wilayah") ? null
							: oResponsealue.getString("id_wilayah");
					Log.d(LOG_TAG, "id_customer:" + id_customer);
					Log.d(LOG_TAG, "kode_customer:" + kode_customer);
					Log.d(LOG_TAG, "nama_customer:" + nama_customer);
					Log.d(LOG_TAG, "lats:" + lats);
					Log.d(LOG_TAG, "longs:" + longs);
					db.addMst_customer(new Mst_Customer(Integer.parseInt(id_customer),kode_customer,nama_customer,alamat,no_hp,lats,longs,Integer.parseInt(id_wilayah)));
				}

			} catch (JSONException e) {
				final String message = e.toString();
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(message);
					}
				});

			}
		}
	}

	private SharedPreferences getSharedPrefereces() {
		return act.getSharedPreferences(AppVar.SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
	}

	public void saveAppDataBranch(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_TABLE_MST_CUSTOMER, responsedata);
		editor.commit();
	}

	public void saveAppDataBranchSameData(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_TABLE_MST_CUSTOMER_SAME_DATA,
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

//	private void getCkin() {
//		class getCkinClass extends AsyncTask<Void, Void, String> {
//			ProgressDialog loading;
//
//			@Override
//			protected void onPreExecute() {
//				super.onPreExecute();
//				loading = ProgressDialog.show(DashboardActivity.this,"Loading..","mohon tunggu..",false,false);
//			}
//
//			@Override
//			protected void onPostExecute(String s) {
//				super.onPostExecute(s);
//				loading.dismiss();
//				showDataCkin(s);
//			}
//
//			@Override
//			protected String doInBackground(Void... params) {
//				SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
//				String id_user = prefs.getString("id_user","0");
//
//				RequestHandler rh = new RequestHandler();
//				String s = rh.sendGetRequestParam(AppVar.GET_CKIN,id_user);
//				return s;
//			}
//		}
//
//		getCkinClass ge = new getCkinClass();
//		ge.execute();
//	}

	private void showDataCkin(String json){
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONObject out = jsonObject.getJSONObject("out");

			String ckin = out.getString("ckin");
			jml_ckin.setText(ckin);


		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

//	private void getRencana() {
//		class getAbsenClass extends AsyncTask<Void, Void, String> {
//			ProgressDialog loading;
//
//			@Override
//			protected void onPreExecute() {
//				super.onPreExecute();
//				loading = ProgressDialog.show(DashboardActivity.this,"Loading..","mohon tunggu..",false,false);
//			}
//
//			@Override
//			protected void onPostExecute(String s) {
//				super.onPostExecute(s);
//				loading.dismiss();
//				showDataRencana(s);
//			}
//
//			@Override
//			protected String doInBackground(Void... params) {
//				SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
//				String id_user = prefs.getString("id_user","0");
//
//				RequestHandler rh = new RequestHandler();
//				String s = rh.sendGetRequestParam(AppVar.GET_RENCANA,id_user);
//				return s;
//			}
//		}
//
//		getAbsenClass ge = new getAbsenClass();
//		ge.execute();
//	}

	private void showDataRencana(String json){
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONObject out = jsonObject.getJSONObject("out");

			String ckin = out.getString("rencana");
			jml_rcn.setText(ckin);
			hitungPersentase();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void hitungPersentase() {
		double ckin = Double.parseDouble(jml_ckin.getText().toString());
		double rencana = Double.parseDouble(jml_rcn.getText().toString());
		double persentase = ckin/rencana*100;
		percent.setText(String.valueOf(persentase));

	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		if (mNavigationDrawerFragment != null) {
			if (mNavigationDrawerFragment.getCurrentSelectedPosition() != 0) {
//				if (position == 1) {
//					Intent intentActivity = new Intent(this,
//							IconTextTabsActivity.class);
//					startActivity(intentActivity);
//					finish();
//				}else if (position == 2) {
//					Intent intentActivity = new Intent(this,
//							CheckoutActivity.class);
//					startActivity(intentActivity);
//					finish();
//				} else
				if (position ==1) {
					Intent intentActivity = new Intent(this,
							IconTextTabsActivity.class);
					startActivity(intentActivity);
					finish();
				}else if (position ==2) {
					Intent intentActivity = new Intent(this,
							CheckoutActivity.class);
					startActivity(intentActivity);
					finish();
				}else if (position ==3) {
					Intent intentActivity = new Intent(this,
							History_Canvassing.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 4) {
					Intent intentActivity = new Intent(this,
							ChangePassword.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 5) {
					Intent intentActivity = new Intent(this,
							Orderan.class);
					startActivity(intentActivity);
					finish();
				}
			}
		}
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

	public void gotoCheckin(){
		Intent intentActivity = new Intent(this,
				IconTextTabsActivity.class);
		startActivity(intentActivity);
		finish();
	}

	public void gotoCheckout(){
		Intent intentActivity = new Intent(this,
				CheckoutActivity.class);
		startActivity(intentActivity);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			showCustomDialogExit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
