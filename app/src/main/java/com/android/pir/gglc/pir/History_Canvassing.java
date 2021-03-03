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
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pir.gglc.absen.AppVar;
import com.android.pir.gglc.absen.ChangePassword;
import com.android.pir.gglc.absen.NavigationDrawerCallbacks;
import com.android.pir.gglc.absen.NavigationDrawerFragment;
import com.android.pir.gglc.absen.Orderan;
import com.android.pir.gglc.database.DatabaseHandler;
import com.android.pir.gglc.database.History_canvassing;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;

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

	private TextView tvKodeCustomer;
	private TextView tvNamaCustomer;
	private TextView tvNamaAlamat;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main_history_canvassing);

		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);

		tvKodeCustomer = (TextView) findViewById(R.id.activity_customer_title_kode_customer);
		tvNamaCustomer = (TextView) findViewById(R.id.activity_customer_title_nama_customer);
		tvNamaAlamat = (TextView) findViewById(R.id.activity_customer_title_alamat_customer);
		mulai = (EditText)findViewById(R.id.date_from);
		sampai = (EditText)findViewById(R.id.date_to);
		history = (Button)findViewById(R.id.search);
		layout=(LinearLayout)findViewById(R.id.activity_customer_linear_title);

		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle(R.string.title_activity_all);

		tvKodeCustomer.setTypeface(typefaceSmall);
		tvNamaCustomer.setTypeface(typefaceSmall);
		tvNamaAlamat.setTypeface(typefaceSmall);
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
			progressDialog.setMessage("download dulu ya");
			progressDialog.show();
			progressDialog
					.setOnCancelListener(new DialogInterface.OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							String msg = "Cancle ya";
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
				JSONArray jsonarr = oResponse.getJSONArray("canvassing");
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

					databaseHandler.addHistoryCanvassing(new History_canvassing(nama_customer,nomor_rencana,alamat,tanggal_checkin,tanggal_checkout));
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

				History_canvassing canvasing = new History_canvassing();
				canvasing.setId_canvassing(id_canvassing);
				canvasing.setNama_customer(nama_customer);
				canvasing.setNomor_rencana(nomor_rencana);
				canvasing.setAlamat(alamat);
				canvasing.setWaktu_checkin(waktu_checkin);
				canvasing.setWaktu_checkout(waktu_checkout);

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

				History_canvassing canvasing = new History_canvassing();
				canvasing.setId_canvassing(id_canvassing);
				canvasing.setNama_customer(nama_customer);
				canvasing.setNomor_rencana(nomor_rencana);
				canvasing.setAlamat(alamat);
				canvasing.setWaktu_checkin(waktu_checkin);
				canvasing.setWaktu_checkout(waktu_checkout);

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
				holder.list_kodeCustomer = (TextView) row
						.findViewById(R.id.customer_title_kode_customer);
				holder.list_namaCustomer = (TextView) row
						.findViewById(R.id.customer_title_nama_customer);
				holder.list_alamat = (TextView) row
						.findViewById(R.id.customer_title_alamat_customer);
				row.setTag(holder);
			} else {
				holder = (UserHolder) row.getTag();
			}
			customerData = data.get(position);
			holder.list_kodeCustomer.setText(customerData.getNama_customer());
			holder.list_namaCustomer.setText(customerData.getAlamat());
			holder.list_alamat.setText(customerData.getWaktu_checkout());
			// Wilayah wilayah = databaseHandler.getWilayah(customerData
			// .getId_wilayah());
			holder.list_kodeCustomer.setTypeface(typefaceSmall);
			holder.list_namaCustomer.setTypeface(typefaceSmall);
			holder.list_alamat.setTypeface(typefaceSmall);
			/*
			row.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String id_customer = String.valueOf(data.get(position)
							.getId_canvassing());
					saveAppDataCustomerIdCustomer(id_customer);
					SharedPreferences spPreferences = getSharedPrefereces();
					String main_app_staff_level = spPreferences.getString(AppVar.SHARED_PREFERENCES_STAFF_LEVEL, null);
					//int levelStaff = Integer.parseInt(main_app_staff_level);

					//gotoDetailCustomer();
				}
			});
			*/
			return row;

		}

		class UserHolder {
			TextView list_kodeCustomer;
			TextView list_namaCustomer;
			TextView list_alamat;
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
				}
//				else if (position ==3) {
//					Intent intentActivity = new Intent(this,
//							IconTextTabsActivity_lap.class);
//					startActivity(intentActivity);
//					finish();
//				}else if (position == 4) {
//					Intent intentActivity = new Intent(this,
//							CheckoutActivity.class);
//					startActivity(intentActivity);
//					finish();
//				}
				else if (position == 3) {
					Intent intentActivity = new Intent(this,
							DashboardActivity.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 5) {
					Intent intentActivity = new Intent(this,
							ChangePassword.class);
					startActivity(intentActivity);
					finish();
				}
//				else if (position == 5) {
//					Intent intentActivity = new Intent(this,
//							Orderan.class);
//					startActivity(intentActivity);
//					finish();
//				}
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			showCustomDialogExit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
