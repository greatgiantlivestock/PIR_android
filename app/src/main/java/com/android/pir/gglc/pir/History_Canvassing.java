package com.android.pir.gglc.pir;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pir.gglc.absen.AppVar;
import com.android.pir.gglc.absen.ChangePassword;
import com.android.pir.gglc.absen.NavigationDrawerCallbacks;
import com.android.pir.gglc.absen.NavigationDrawerFragment;
import com.android.pir.gglc.absen.Orderan;
import com.android.pir.gglc.database.DatabaseHandler;
import com.android.pir.gglc.database.History_canvassing;
import com.android.pir.gglc.database.Pakan;
import com.android.pir.gglc.fragment1.PakanFragment;
import com.android.pir.mobile.R;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.android.pir.gglc.absen.AppVar.SHARED_PREFERENCES_NAME;

@SuppressWarnings("deprecation")
public class History_Canvassing extends ActionBarActivity implements
		NavigationDrawerCallbacks {
	private Context act;
	private Toolbar mToolbar;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private DatabaseHandler databaseHandler;
	private ListView listview;
	private LinearLayout layout;
	private ArrayList<History_canvassing> canvassing_list = new ArrayList<History_canvassing>();
	private ListViewAdapter cAdapter;
	private ProgressDialog progressDialog;
	private Handler handler = new Handler();
	private String message;
	private String response_data;
	private static final String LOG_TAG = History_Canvassing.class
			.getSimpleName();
	private EditText searchCustomer;
	private Typeface typefaceSmall;
    private String response;
    private History_canvassing customer;
	private Button chat,map;
	private EditText mulai,sampai;
	private Button history;
	final int Date_Dialog_ID=0;
	final int Date_Dialog_ID1=1;
	int cDay,cMonth,cYear;
	Calendar cDate;
	int sDay,sMonth,sYear;
	private ProgressBar progressBar;
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main_history_canvassing);

		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		mulai = (EditText)findViewById(R.id.date_from);
		sampai = (EditText)findViewById(R.id.date_to);
		history = (Button)findViewById(R.id.search);
		layout=(LinearLayout)findViewById(R.id.activity_customer_linear_title);

		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle(R.string.title_activity_all);

		chat = (Button) findViewById(R.id.chat);
		act = this;

		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(getApplicationContext().getResources()
				.getString(R.string.app_name));
		progressDialog.setMessage("Downloading...");
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.fragment_drawer);
		mNavigationDrawerFragment.setup(R.id.fragment_drawer,
				(DrawerLayout) findViewById(R.id.drawer), mToolbar);
		databaseHandler = new DatabaseHandler(this);
		mNavigationDrawerFragment.selectItem(4);
		listview = (ListView) findViewById(R.id.list);
		listview.setItemsCanFocus(false);

		mulai.setClickable(true);
		mulai.setFocusable(false);
		mulai.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(Date_Dialog_ID);
			}
		});

		sampai.setClickable(true);
		sampai.setFocusable(false);
		sampai.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(Date_Dialog_ID1);
			}
		});

		cDate= Calendar.getInstance();
		cDay=cDate.get(Calendar.DAY_OF_MONTH);
		cMonth=cDate.get(Calendar.MONTH);
		cYear=cDate.get(Calendar.YEAR);
		sDay=cDay;
		sMonth=cMonth;
		sYear=cYear;

		history.setEnabled(false);

		listview.setVisibility(View.INVISIBLE);
		layout.setVisibility(View.INVISIBLE);
		history.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(mulai.getText().toString().equals("") && sampai.getText().toString().equals("")){
					Toast.makeText(History_Canvassing.this, "Kolom tangal masih kosong",Toast.LENGTH_LONG).show();
				}else{
					final String tanggal1 = mulai.getText().toString();
					final String tanggal2 = sampai.getText().toString();
					saveAppDatatanggal(tanggal1, tanggal2);
				}
				layout.setVisibility(View.VISIBLE);
			}
		});
	}

	public void saveAppDatatanggal(String tanggal1,String tanggal2) {
		Editor editor = getSharedPreferences(AppVar.SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit();
		editor.putString("tanggal1", tanggal1);
		editor.putString("tanggal2", tanggal2);
		editor.apply();
		new DownloadDataCustomer().execute();
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case Date_Dialog_ID:
				return new DatePickerDialog(this, onDateSet, cYear, cMonth,
						cDay);

			case Date_Dialog_ID1:
				return new DatePickerDialog(this, onDateSet1, cYear, cMonth,
						cDay);
		}
		return null;
	}

	private void updateDateDisplay(int year,int month,int date) {
		String adate;
		month++;
		if (date < 10) {
			adate = "0" + date + "-";
		} else {
			adate = date + "-";
		}
		if (month < 10) {
			adate += "0" + month + "-";
		} else {
			adate += month + "-";
		}

		adate += year;
		mulai.setText(adate);
	}

	private void updateDateDisplay1(int year,int month,int date) {
		String adate;
		month++;
		if (date < 10) {
			adate = "0" + date + "-";
		} else {
			adate = date + "-";
		}
		if (month < 10) {
			adate += "0" + month + "-";
		} else {
			adate += month + "-";
		}

		adate += year;
		sampai.setText(adate);
		history.setEnabled(true);
	}
	private DatePickerDialog.OnDateSetListener onDateSet=new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
							  int dayOfMonth) {

			System.out.println("Mulai");
			sYear=year;
			sMonth=monthOfYear;
			sDay=dayOfMonth;
			updateDateDisplay(sDay,sMonth,sYear);
		}
	};

	private DatePickerDialog.OnDateSetListener onDateSet1=new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
							  int dayOfMonth) {

			System.out.println("Sampai");
			sYear=year;
			sMonth=monthOfYear;
			sDay=dayOfMonth;
			updateDateDisplay1(sDay,sMonth,sYear);
		}
	};

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

	private class DownloadDataCustomer extends
			AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog.setMessage("Checking history");
			progressDialog.show();
			progressDialog
					.setOnCancelListener(new DialogInterface.OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							String msg = "Cancled";
							showCustomDialog(msg);
						}
					});
		}

		@Override
		protected String doInBackground(String... params) {
			SharedPreferences spPreferences = getSharedPrefereces();

			SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
			String tanggal1 = prefs.getString("tanggal1","null");
			String tanggal2 = prefs.getString("tanggal2","null");
			String id_user = prefs.getString("id_user","null");

			String download_data_url = AppVar.CONFIG_APP_URL_PUBLIC
					+ AppVar.CONFIG_APP_URL_DOWNLOAD_CANVASSING_HISTORY + "?id_user="
					+ id_user + "&tanggal1=" + tanggal1 + "&tanggal2=" + tanggal2;
			HttpResponse response = getDownloadData(download_data_url);

			int retCode = (response != null) ? response.getStatusLine()
					.getStatusCode() : -1;
			if (retCode != 200) {
				message = "Internet tidak ada";
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(message);
					}
				});
			} else {
				try {
					response_data = EntityUtils.toString(response.getEntity());

					String main_app_table_data = spPreferences.getString(
							AppVar.SHARED_PREFERENCES_TABLE_HISTORY_CANVASSING, null);
					if (main_app_table_data != null) {
						if (main_app_table_data.equalsIgnoreCase(response_data)) {
							saveAppDataHistoryCanvssingSameData(act
									.getApplicationContext().getResources()
									.getString(R.string.app_value_true));
						} else {
							databaseHandler.deleteTableHistoryCanvassing();
							saveAppDataHistoryCanvssingSameData(act
									.getApplicationContext().getResources()
									.getString(R.string.app_value_false));
						}
					} else {
						databaseHandler.deleteTableHistoryCanvassing();
						saveAppDataHistoryCanvssingSameData(act.getApplicationContext()
								.getResources()
								.getString(R.string.app_value_false));
					}
				} catch (ParseException e) {
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
				saveAppDataHistoryCanvssing(response_data);
				extractDataHistoryCanvassing();
				message = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.MSG_DLG_LABEL_SYNRONISASI_DATA_SUCCESS);
				showCustomDialogDownloadSuccess(message);
			} else if (response_data == null){
				message = act.getApplicationContext().getResources()
						.getString(R.string.MSG_DLG_LABEL_DOWNLOAD_FAILED);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(message);
					}
				});
			}else{
				message = act.getApplicationContext().getResources()
						.getString(R.string.MSG_DLG_LABEL_DOWNLOAD_NULL_DATA);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(message);
					}
				});
			}
		}
	}

	public void saveAppDataHistoryCanvssing(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_TABLE_HISTORY_CANVASSING, responsedata);
		editor.commit();
	}

	public void saveAppDataHistoryCanvssingSameData(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_TABLE_HISTORY_CANVASSING_SAME_DATA,
				responsedata);
		editor.commit();
	}

	public void saveAppDataCustomerIdCustomer(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_TABLE_HISTORY_CANVASSING_ID_CANVASSING,
				responsedata);
		editor.commit();
	}

	public void showCustomDialogDownloadSuccess(String msg) {
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
                                updateContentRefreshCustomer();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public void extractDataHistoryCanvassing() {
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_table_same_data = spPreferences.getString(
				AppVar.SHARED_PREFERENCES_TABLE_HISTORY_CANVASSING_SAME_DATA, null);
		String main_app_table = spPreferences.getString(
				AppVar.SHARED_PREFERENCES_TABLE_HISTORY_CANVASSING, null);
		if (main_app_table_same_data.equalsIgnoreCase(act
				.getApplicationContext().getResources()
				.getString(R.string.app_value_false))) {
			JSONObject oResponse;
			try {
				oResponse = new JSONObject(main_app_table);
				JSONArray jsonarr = oResponse.getJSONArray("visit");
				for (int i = 0; i < jsonarr.length(); i++) {
					JSONObject oResponsealue = jsonarr.getJSONObject(i);
					String nama_customer = oResponsealue.isNull("name1") ? null
							: oResponsealue.getString("name1");
					String nomor_rencana = oResponsealue
							.isNull("nomor_rencana") ? null : oResponsealue
							.getString("nomor_rencana");
					String alamat = oResponsealue.isNull("desa") ? null
							: oResponsealue.getString("desa");
					String tanggal_checkin = oResponsealue.isNull("tanggal_checkin") ? null
							: oResponsealue.getString("tanggal_checkin");
					String tanggal_checkout = oResponsealue.isNull("tanggal_checkout") ? null
							: oResponsealue.getString("tanggal_checkout");
					String lats = oResponsealue.isNull("lats") ? null
							: oResponsealue.getString("lats");
					String longs = oResponsealue.isNull("longs") ? null
							: oResponsealue.getString("longs");
					String foto = oResponsealue.isNull("foto") ? null
							: oResponsealue.getString("foto");
					String indnr = oResponsealue.isNull("indnr") ? null
							: oResponsealue.getString("indnr");
					String id_rencana_detail = oResponsealue.isNull("id_rencana_detail") ? null
							: oResponsealue.getString("id_rencana_detail");

					databaseHandler.addHistoryCanvassing(new History_canvassing(nama_customer,nomor_rencana,alamat,tanggal_checkin,tanggal_checkout,lats,longs,foto,indnr,id_rencana_detail));
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

	/*
	public void updateContentCustomer() {
		customer_list.clear();
		ArrayList<Absen> customer_from_db = null;
		customer_from_db = databaseHandler.getAllAbsen();

		if (customer_from_db.size() > 0) {
			listview.setVisibility(View.VISIBLE);
			layout.setVisibility(View.VISIBLE);
			for (int i = 0; i < customer_from_db.size(); i++) {
				int id_customer = customer_from_db.get(i).getId_absen();
				String kode_customer = customer_from_db.get(i)
						.getNama_karyawan();
				String email = customer_from_db.get(i).getWaktu();
				String alamat = customer_from_db.get(i).getLokasi();

				Absen customer = new Absen();
				customer.setId_absen(id_customer);
				customer.setNama_karyawan(kode_customer);
				customer.setWaktu(email);
				customer.setLokasi(alamat);

				//customer.setId_group_outlet(id_group_outlet);
				customer_list.add(customer);
			}

		} else {
			listview.setVisibility(View.INVISIBLE);
		}
		showListCustomer();
	}
	*/

	public void updateContentRefreshCustomer() {
		canvassing_list.clear();
		ArrayList<History_canvassing> customer_from_db = databaseHandler
				.getAllHistoryCanvassing();
		if (customer_from_db.size() > 0) {
			listview.setVisibility(View.VISIBLE);
			for (int i = 0; i < customer_from_db.size(); i++) {
				int id_canvassing = customer_from_db.get(i).getId_canvassing();
				String nama_customer = customer_from_db.get(i).getNama_customer();
				String nomor_rencana = customer_from_db.get(i).getNomor_rencana();
				String alamat = customer_from_db.get(i).getAlamat();
				String waktu_checkin = customer_from_db.get(i).getWaktu_checkin();
				String waktu_checkout = customer_from_db.get(i).getWaktu_checkout();
				String lats = customer_from_db.get(i).getLats();
				String longs = customer_from_db.get(i).getLongs();
				String foto = customer_from_db.get(i).getFoto();
				String indnr = customer_from_db.get(i).getIndnr();
				String id_rencana_detail = customer_from_db.get(i).getId_rencana_detail();

				History_canvassing canvasing = new History_canvassing();
				canvasing.setId_canvassing(id_canvassing);
				canvasing.setNama_customer(nama_customer);
				canvasing.setNomor_rencana(nomor_rencana);
				canvasing.setAlamat(alamat);
				canvasing.setWaktu_checkin(waktu_checkin);
				canvasing.setWaktu_checkout(waktu_checkout);
				canvasing.setLats(lats);
				canvasing.setLongs(longs);
				canvasing.setFoto(foto);
				canvasing.setIndnr(indnr);
				canvasing.setId_rencana_detail(id_rencana_detail);

				canvassing_list.add(canvasing);
			}

		} else {
			listview.setVisibility(View.INVISIBLE);
		}
		showListCustomer();
	}

	public void showListCustomer() {
		canvassing_list.clear();
		SharedPreferences spPreferences = getSharedPrefereces();

		ArrayList<History_canvassing> customer_from_db = null;
		customer_from_db = databaseHandler.getAllHistoryCanvassing();

		if (customer_from_db.size() > 0) {
			listview.setVisibility(View.VISIBLE);
			for (int i = 0; i < customer_from_db.size(); i++) {
				int id_canvassing = customer_from_db.get(i).getId_canvassing();
				String nama_customer = customer_from_db.get(i).getNama_customer();
				String nomor_rencana = customer_from_db.get(i).getNomor_rencana();
				String alamat = customer_from_db.get(i).getAlamat();
				String waktu_checkin = customer_from_db.get(i).getWaktu_checkin();
				String waktu_checkout = customer_from_db.get(i).getWaktu_checkout();
				String lats = customer_from_db.get(i).getLats();
				String longs = customer_from_db.get(i).getLongs();
				String foto = customer_from_db.get(i).getFoto();
				String indnr = customer_from_db.get(i).getIndnr();
				String id_rencana_detail = customer_from_db.get(i).getId_rencana_detail();

				History_canvassing canvasing = new History_canvassing();
				canvasing.setId_canvassing(id_canvassing);
				canvasing.setNama_customer(nama_customer);
				canvasing.setNomor_rencana(nomor_rencana);
				canvasing.setAlamat(alamat);
				canvasing.setWaktu_checkin(waktu_checkin);
				canvasing.setWaktu_checkout(waktu_checkout);
				canvasing.setLats(lats);
				canvasing.setLongs(longs);
				canvasing.setFoto(foto);
				canvasing.setIndnr(indnr);
				canvasing.setId_rencana_detail(id_rencana_detail);

				canvassing_list.add(canvasing);
				/*
				int id_customer = customer_from_db.get(i).getId_absen();
				String kode_customer = customer_from_db.get(i)
						.getNama_karyawan();
				String email = customer_from_db.get(i).getWaktu();
				String alamat = customer_from_db.get(i).getLokasi();

				Absen customer = new Absen();
				customer.setId_absen(id_customer);
				customer.setNama_karyawan(kode_customer);
				customer.setWaktu(email);
				customer.setLokasi(alamat);
				canvassing_list.add(customer);
				*/
			}

			cAdapter = new ListViewAdapter(this, R.layout.list_item_customer,
					canvassing_list);
			listview.setAdapter(cAdapter);
			cAdapter.notifyDataSetChanged();
		} else {
			listview.setVisibility(View.INVISIBLE);
		}
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

    public class ListViewAdapter extends ArrayAdapter<History_canvassing> {
		Activity activity;
		int layoutResourceId;
		History_canvassing customerData;
		ArrayList<History_canvassing> data = new ArrayList<History_canvassing>();

		public ListViewAdapter(Activity act, int layoutResourceId,
							   ArrayList<History_canvassing> data) {
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
				holder.list_nama = (TextView) row.findViewById(R.id.customer_title_nama_customer);
				holder.list_index = (TextView) row.findViewById(R.id.index);
				holder.list_alamat = (TextView) row.findViewById(R.id.customer_title_alamat_customer);
				holder.list_checkin = (TextView) row.findViewById(R.id.checkin);
				row.setTag(holder);
			} else {
				holder = (UserHolder) row.getTag();
			}
			customerData = data.get(position);
			Geocoder geocoder;
			List<Address> addresses = null;
			geocoder = new Geocoder(History_Canvassing.this, Locale.getDefault());

			try {
				addresses = geocoder.getFromLocation(Double.parseDouble(customerData.getLats()),Double.parseDouble(customerData.getLongs()), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
			} catch (IOException e) {
				e.printStackTrace();
			}

			String address = addresses.get(0).getAddressLine(0);
			holder.list_nama.setText(customerData.getNama_customer());
			holder.list_alamat.setText(customerData.getAlamat());
			holder.list_index.setText(customerData.getIndnr());
			holder.list_checkin.setText(address+", "+customerData.getWaktu_checkout());
			row.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String id_customer = String.valueOf(data.get(position).getId_rencana_detail());
					ChooseCustomerDialog(id_customer);
//					saveAppDataCustomerIdCustomer(id_customer);
//					SharedPreferences spPreferences = getSharedPrefereces();
//					String main_app_staff_level = spPreferences.getString(AppVar.SHARED_PREFERENCES_STAFF_LEVEL, null);
				}
			});
			return row;

		}

		class UserHolder {
			TextView list_nama;
			TextView list_index;
			TextView list_alamat;
//			WebView list_foto;
			TextView list_checkin;
		}

	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		if (mNavigationDrawerFragment != null) {
			if (mNavigationDrawerFragment.getCurrentSelectedPosition() != 4) {
				if (position ==0) {
					Intent intentActivity = new Intent(this,
							PlanVisitActivity.class);
					startActivity(intentActivity);
					finish();
				}else if (position ==1) {
					Intent intentActivity = new Intent(this,
							PlanVisitActivity2.class);
					startActivity(intentActivity);
					finish();
				}else if (position ==2) {
					Intent intentActivity = new Intent(this,
							ProspectPlanVisit.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 3) {
					Intent intentActivity = new Intent(this,
							DashboardActivity.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 5) {
					Intent intentActivity = new Intent(this,
							ChangePassword.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 6) {
					showCustomDialogExit();
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

	private void ChooseCustomerDialog(String type) {
		Log.d(LOG_TAG, "id_rencana_detail history: "+type);
		final Dialog chooseCustomerDialog = new Dialog(act);
		chooseCustomerDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		chooseCustomerDialog.setContentView(R.layout.activity_main_detail_history);
		chooseCustomerDialog.setCanceledOnTouchOutside(true);
		chooseCustomerDialog.setCancelable(true);
		Window window = chooseCustomerDialog.getWindow();
		window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		Button button = (Button) chooseCustomerDialog.findViewById(R.id.close);
		webView = (WebView) chooseCustomerDialog.findViewById(R.id.webV);
//		progressBar = (ProgressBar) findViewById(R.id.progweb);
//		progressBar.setVisibility(View.INVISIBLE);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				chooseCustomerDialog.dismiss();
			}
		});
		chooseCustomerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				chooseCustomerDialog.dismiss();
			}
		});

		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);

		this.getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
//		webView.setWebChromeClient(new WebChromeClient() {
//			public void onProgressChanged(WebView view, int progress) {
//				progressBar.setProgress(progress);
//				if (progress == 100) {
//					progressBar.setVisibility(View.GONE);
//
//				} else {
//					progressBar.setVisibility(View.VISIBLE);
//				}
//			}
//		});

		webView.setWebViewClient(new WebViewClient());
		webView.loadUrl("https://dev-pir-visit.gg-livestock.com/Rekap_salesman_mobile/ambil_data/"+type);

		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webView.getSettings().setBuiltInZoomControls(false);
		webView.setOnKeyListener(new View.OnKeyListener(){
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
					webView.goBack();
					return true;
				}
				return false;
			}
		});

		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

//		final ArrayList<Pakan> customer_list = new ArrayList<Pakan>();
//		final ListView listview = (ListView) chooseCustomerDialog.findViewById(R.id.list);
//
//		listview.setItemsCanFocus(false);
//		ArrayList<Pakan> pakan_from_db = databaseHandler.getAllPakan(String.valueOf(indnr),type);
//		if (pakan_from_db.size() > 0) {
//			listview.setVisibility(View.VISIBLE);
//			for (int i = 0; i < pakan_from_db.size(); i++) {
//				int indnr = pakan_from_db.get(i).getIndnr();
//				String kode_pakan= pakan_from_db.get(i).getKode_pakan();
//				String desc_pakan= pakan_from_db.get(i).getDesc_pakan();
//				String std= pakan_from_db.get(i).getStd();
//				int budget= pakan_from_db.get(i).getBudget();
//				int terkirim= pakan_from_db.get(i).getTerkirim();
//				int sisa= pakan_from_db.get(i).getSisa();
//				int nofanim= pakan_from_db.get(i).getNofanim();
//				String dof= pakan_from_db.get(i).getDof();
//				String satuan= pakan_from_db.get(i).getSatuan();
//				String tanggal_kirim= pakan_from_db.get(i).getTanggal_kirim();
//				int qty_terima= pakan_from_db.get(i).getQty_terima();
//				String create_date= pakan_from_db.get(i).getCreate_date();
//				String pakan_type= pakan_from_db.get(i).getPakan_type();
//
//				Pakan pakan = new Pakan();
//				pakan.setIndnr(indnr);
//				pakan.setKode_pakan(kode_pakan);
//				pakan.setDesc_pakan(desc_pakan);
//				pakan.setStd(std);
//				pakan.setBudget(budget);
//				pakan.setTerkirim(terkirim);
//				pakan.setSisa(sisa);
//				pakan.setNofanim(nofanim);
//				pakan.setDof(dof);
//				pakan.setSatuan(satuan);
//				pakan.setTanggal_kirim(tanggal_kirim);
//				pakan.setQty_terima(qty_terima);
//				pakan.setCreate_date(create_date);
//				pakan.setPakan_type(pakan_type);
//
//				customer_list.add(pakan);
//				cAdapterChooseAdapter = new PakanFragment.ListViewChooseAdapter(PakanFragment.this.getActivity(),R.layout.list_item_detail_pakan,customer_list, chooseCustomerDialog);
//				listview.setAdapter(cAdapterChooseAdapter);
//				cAdapterChooseAdapter.notifyDataSetChanged();
//			}
//		} else {
//			listview.setVisibility(View.INVISIBLE);
//		}
		Handler handler = new Handler();
		handler.post(new Runnable() {
			public void run() {
				chooseCustomerDialog.show();
			}
		});
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//			showCustomDialogExit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
