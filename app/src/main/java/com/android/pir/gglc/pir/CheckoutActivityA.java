package com.android.pir.gglc.pir;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pir.gglc.absen.AppVar;
import com.android.pir.gglc.absen.RequestHandler;
import com.android.pir.gglc.database.Absen;
import com.android.pir.gglc.database.DatabaseHandler;
import com.android.pir.gglc.database.Jenis_kendaraan;
import com.android.pir.gglc.database.MstUser;
import com.android.pir.gglc.database.Mst_Customer;
import com.android.pir.gglc.database.TmpCustomer;
import com.android.pir.gglc.database.Trx_Checkin;
import com.android.pir.mobile.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("deprecation")
public class CheckoutActivityA extends FragmentActivity {
	private Context act;
	private Toolbar mToolbar;
//	private NavigationDrawerFragment mNavigationDrawerFragment;
	private DatabaseHandler databaseHandler;
	private ListView listview;
	private LinearLayout layout;
	private ArrayList<Absen> customer_list = new ArrayList<Absen>();
	private ProgressDialog progressDialog;
	private Handler handler = new Handler();
	private String message;
	private String response_data;
	private static final String LOG_TAG = CheckoutActivityA.class.getSimpleName();
	private EditText description_kegiatan;
	private Spinner checkin_number,jenis_kendaraan;
	private ArrayList<Trx_Checkin> checkinNumber;
	private ArrayList<Jenis_kendaraan> jenisKendaraan;
	private ArrayList<String> checkinNumberStringList;
	private ArrayList<String> jenisKendaraanStringList;
	private int id_checkin = 0;
	private String nomor_checkin_ = null;
	private int id_jenis_kendaraan = 0;
	private int id_user;
	private MstUser user;
	private Typeface typefaceSmall;
	private TextView tvNamaCustomer;
	private TextView tvNamaAlamat;
    private String response;
    private Absen customer;
	private Button chat,map;
	private EditText mulai,sampai;
	private Button checkout;
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


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main_checkouta);

		act = this;

		description_kegiatan = (EditText)findViewById(R.id.description);
		checkin_number = (Spinner)findViewById(R.id.nomor_checkin);
		jenis_kendaraan = (Spinner)findViewById(R.id.id_jenis_kendaraan);
		tvNamaCustomer = (TextView)findViewById(R.id.nm_cust);
		checkout = (Button)findViewById(R.id.checkout);
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(getApplicationContext().getResources()
				.getString(R.string.app_name));
		progressDialog.setMessage("Downloading yaa...");
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
//		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
//				.findFragmentById(R.id.fragment_drawer);
//		mNavigationDrawerFragment.setup(R.id.fragment_drawer,
//				(DrawerLayout) findViewById(R.id.drawer), mToolbar);
		databaseHandler = new DatabaseHandler(this);
//		mNavigationDrawerFragment.selectItem(2);

		if(databaseHandler.getCountTrxcheckin()==0){
			showCustomDialogInvalidCheckin("Data checkin anda kosong, silahkan checkin terlebih dahulu..");
		}else{
			//set list nomor checkin
			checkinNumber = new ArrayList<Trx_Checkin>();
			checkinNumberStringList = new ArrayList<String>();
			List<Trx_Checkin> dataCheckinNumber = databaseHandler.getAllCheckinNumber();
			for (Trx_Checkin nomor_checkin_1 : dataCheckinNumber) {
				checkinNumber.add(nomor_checkin_1);
				checkinNumberStringList.add(String.valueOf(nomor_checkin_1.getNomor_checkin()));
			}

			//set spinner nomor checkin
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, checkinNumberStringList);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			checkin_number.setAdapter(adapter);
			checkin_number.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent,
										   View view, int position, long id) {
					nomor_checkin_ = checkinNumber.get(position).getNomor_checkin();

					List<Mst_Customer> dataCst = databaseHandler.getAllCustomerParamCheckin(nomor_checkin_);
					List<TmpCustomer> dataTmp = databaseHandler.getAllTmpCustomerParamCheckin(nomor_checkin_);

					Mst_Customer customer = new Mst_Customer();
					for (Mst_Customer Customer : dataCst)
						customer = Customer;

					TmpCustomer tmpCustomer = new TmpCustomer();
					for (TmpCustomer tempCustomer : dataTmp)
						tmpCustomer = tempCustomer;
					if(customer.getNama_customer()==null){
						tvNamaCustomer.setText(tmpCustomer.getNama_customer());
					}else if(customer.getNama_customer()!=null){
						tvNamaCustomer.setText(customer.getNama_customer());
					}

				}
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});

			nomor_checkin_ = checkinNumber.get(0).getNomor_checkin();

			//set list jenis kendaraan
			jenisKendaraan = new ArrayList<Jenis_kendaraan>();
			jenisKendaraanStringList= new ArrayList<String>();
			List<Jenis_kendaraan> dataJenisKedaraan = databaseHandler.getAllJenisKendaraan();
			for (Jenis_kendaraan jeniskendaraan : dataJenisKedaraan) {
				jenisKendaraan.add(jeniskendaraan);
				jenisKendaraanStringList.add(String.valueOf(jeniskendaraan.getNama_jenis()));
			}

			//set spinner jenis kendaraan
			ArrayAdapter<String> adapter_jenis = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, jenisKendaraanStringList);
			adapter_jenis.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			jenis_kendaraan.setAdapter(adapter_jenis);
			jenis_kendaraan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent,View view, int position, long id) {
					id_jenis_kendaraan = jenisKendaraan.get(position).getId_jenis_kendaraan();
				}
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});
			id_jenis_kendaraan = jenisKendaraan.get(0).getId_jenis_kendaraan();
		}

		ArrayList<MstUser> staff_list = databaseHandler.getAllUser();
		user = new MstUser();
		for (MstUser tempStaff : staff_list)
			user = tempStaff;
		id_user=user.getId_user();

		checkout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				checkoutSave();
			}
		});
	}

	public void checkoutSave() {
		locationManager = (LocationManager) getApplicationContext()
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000L, 1.0f, locationListener);

		boolean isGPSEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean isNetworkEnabled = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		if (!isGPSEnabled) {
			startActivityForResult(new Intent(
					Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
		}else{
			if(isGPSEnabled){
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
						1000L, 1.0f, locationListener);
				location2 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) ;
				if (location2 != null){
					latitude=location2.getLatitude();
					longitude=location2.getLongitude();

					final String vlats=String.valueOf(latitude);
					final String vlongs=String.valueOf(longitude);
					//disini
					if (description_kegiatan.getText().length()!=0) {
						//String curLatitude = String.valueOf(1);
						//String curLongitude = String.valueOf(1);
//						int posSpinnerJenis = 2;//jenis_kendaraan.getSelectedItemPosition();
//						if(posSpinnerJenis!=0){
							if (vlats.equalsIgnoreCase("0")
									|| vlongs.equalsIgnoreCase("0")) {
								String msg = getApplicationContext()
										.getResources()
										.getString(
												R.string.MSG_DLG_LABEL_FAILED_CURRENT_GPS_DIALOG);
								showCustomDialog(msg);
							} else {
								int countId_checkin = databaseHandler.getCountTrxcheckin();
								final String date = "yyyy-MM-dd";
								Calendar calendar = Calendar.getInstance();
								SimpleDateFormat dateFormat = new SimpleDateFormat(date);
								final String checkDate = dateFormat.format(calendar.getTime());
								//String nomor_chekin=kode_customer+checkDate+timeStamp;
								id_checkin=countId_checkin+1;

								final String tanggal_checkout = checkDate;
								final String realisasi_kegiatan = description_kegiatan.getText().toString();
								final String id_user_checkout = String.valueOf(id_user);
								final String nomor_checkin = nomor_checkin_;
//								final String jenis_kendaraan = String.valueOf(id_jenis_kendaraan);
								final String jenis_kendaraan = "2";
								final String lats_checkin = vlats;
								final String longs_checkin = vlongs;

								class insertToDatabase extends AsyncTask<Void, Void, String> {
									ProgressDialog loading;

									@Override
									protected void onPreExecute() {
										super.onPreExecute();
										loading = ProgressDialog.show(CheckoutActivityA.this,"Proses...","Silahkan Tunggu...",false,false);
									}

									@Override
									protected void onPostExecute(String s) {
										super.onPostExecute(s);
										loading.dismiss();
										ArrayList<Trx_Checkin> checkin_list = databaseHandler.getAllCheckinParam(nomor_checkin);
										Trx_Checkin ckin = new Trx_Checkin();
										for (Trx_Checkin tmpcheckin : checkin_list)
											ckin = tmpcheckin;

										databaseHandler.updateRencanaDetailfromCheckout(String.valueOf(ckin.getId_rencana_detail()));
										databaseHandler.updateTrxCheckin(nomor_checkin);
										showCustomDialogSukses("CheckOut Sukses");
									}

									@Override
									protected String doInBackground(Void... params) {
										HashMap<String,String> hashMap  = new HashMap<>();
										hashMap.put("nomor_checkin",nomor_checkin);
										hashMap.put("id_jenis_kendaraan",jenis_kendaraan);
										hashMap.put("realisasi_kegiatan",realisasi_kegiatan);
										hashMap.put("tanggal_checkout",tanggal_checkout);
										hashMap.put("id_user",id_user_checkout);
										hashMap.put("lats",lats_checkin);
										hashMap.put("longs",longs_checkin);

										RequestHandler rh = new RequestHandler();
										String res = rh.sendPostRequest(AppVar.POST_CHECKOUT, hashMap);
										return res;
									}
								}
								insertToDatabase in = new insertToDatabase();
								in.execute();
								//updateStatusRencana(nomor_checkin);
							}
//						}else{
//							String msg = getApplicationContext()
//									.getResources().getString(R.string.app_checkin_incorrect_jenis_kendaraan);
//							showCustomDialog(msg);
//						}
					} else {
						String msg = getApplicationContext()
								.getResources().getString(R.string.app_checkin_incorrect_value);
						showCustomDialog(msg);
					}

				}else{
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
							1000L, 1.0f, locationListener);
					location1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

					if(location1 != null){
						latitude=location1.getLatitude();
						longitude=location1.getLongitude();
						final String vlats=String.valueOf(latitude);
						final String vlongs=String.valueOf(longitude);

						//disini
						if (description_kegiatan.getText().length()!=0) {
							//String curLatitude = String.valueOf(1);
							//String curLongitude = String.valueOf(1);
//							int posSpinnerJenis = jenis_kendaraan.getSelectedItemPosition();
//							if(posSpinnerJenis!=0){
								if (vlats.equalsIgnoreCase("0")
										|| vlongs.equalsIgnoreCase("0")) {
									String msg = getApplicationContext()
											.getResources()
											.getString(
													R.string.MSG_DLG_LABEL_FAILED_CURRENT_GPS_DIALOG);
									showCustomDialog(msg);
								} else {
									int countId_checkin = databaseHandler.getCountTrxcheckin();
									final String date = "yyyy-MM-dd";
									Calendar calendar = Calendar.getInstance();
									SimpleDateFormat dateFormat = new SimpleDateFormat(date);
									final String checkDate = dateFormat.format(calendar.getTime());
									//String nomor_chekin=kode_customer+checkDate+timeStamp;

									id_checkin=countId_checkin+1;

									final String tanggal_checkout = checkDate;
									final String realisasi_kegiatan = description_kegiatan.getText().toString();
									final String id_user_checkout = String.valueOf(id_user);
									final String nomor_checkin = nomor_checkin_;
									final String jenis_kendaraan = String.valueOf(id_jenis_kendaraan);
									final String lats_checkin = vlats;
									final String longs_checkin = vlongs;

									class insertToDatabase extends AsyncTask<Void, Void, String> {
										ProgressDialog loading;

										@Override
										protected void onPreExecute() {
											super.onPreExecute();
											loading = ProgressDialog.show(CheckoutActivityA.this,"Proses...","Silahkan Tunggu...",false,false);
										}

										@Override
										protected void onPostExecute(String s) {
											super.onPostExecute(s);
											loading.dismiss();
											//Toast.makeText(CheckoutActivity.this,"Checkin Berhasil !",Toast.LENGTH_LONG).show();
											ArrayList<Trx_Checkin> checkin_list = databaseHandler.getAllCheckinParam(nomor_checkin);
											Trx_Checkin ckin = new Trx_Checkin();
											for (Trx_Checkin tmpcheckin : checkin_list)
												ckin = tmpcheckin;

											databaseHandler.updateRencanaDetailfromCheckout(String.valueOf(ckin.getId_rencana_detail()));
											databaseHandler.updateTrxCheckin(nomor_checkin);
											showCustomDialogSukses("CheckOut Sukses");
										}

										@Override
										protected String doInBackground(Void... params) {
											HashMap<String,String> hashMap  = new HashMap<>();
											hashMap.put("nomor_checkin",nomor_checkin);
											hashMap.put("id_jenis_kendaraan",jenis_kendaraan);
											hashMap.put("realisasi_kegiatan",realisasi_kegiatan);
											hashMap.put("tanggal_checkout",tanggal_checkout);
											hashMap.put("id_user",id_user_checkout);
											hashMap.put("lats",lats_checkin);
											hashMap.put("longs",longs_checkin);

											RequestHandler rh = new RequestHandler();
											String res = rh.sendPostRequest(AppVar.POST_CHECKOUT, hashMap);
											return res;
										}
									}
									insertToDatabase in = new insertToDatabase();
									in.execute();
								}
//							}else{
//								String msg = getApplicationContext()
//										.getResources().getString(R.string.app_checkin_incorrect_jenis_kendaraan);
//								showCustomDialog(msg);
//							}
						} else {
							String msg = getApplicationContext()
									.getResources().getString(R.string.app_checkin_incorrect_value);
							showCustomDialog(msg);
						}

					}else{
						showCustomDialog("Mohon tunggu, Aplikasi sedang membaca lokasi absen");
					}
				}
			}else if(isNetworkEnabled){
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
						1000L, 1.0f, locationListener);
				location1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

				if(location1 != null){
					latitude=location1.getLatitude();
					longitude=location1.getLongitude();
					final String vlats=String.valueOf(latitude);
					final String vlongs=String.valueOf(longitude);

					//disini
					if (description_kegiatan.getText().length()!=0) {
						//String curLatitude = String.valueOf(1);
						//String curLongitude = String.valueOf(1);
						int posSpinnerJenis = jenis_kendaraan.getSelectedItemPosition();
						if(posSpinnerJenis!=0){
							if (vlats.equalsIgnoreCase("0")
									|| vlongs.equalsIgnoreCase("0")) {
								String msg = getApplicationContext()
										.getResources()
										.getString(
												R.string.MSG_DLG_LABEL_FAILED_CURRENT_GPS_DIALOG);
								showCustomDialog(msg);
							} else {
								int countId_checkin = databaseHandler.getCountTrxcheckin();
								final String date = "yyyy-MM-dd";
								Calendar calendar = Calendar.getInstance();
								SimpleDateFormat dateFormat = new SimpleDateFormat(date);
								final String checkDate = dateFormat.format(calendar.getTime());
								//String nomor_chekin=kode_customer+checkDate+timeStamp;

								id_checkin=countId_checkin+1;

								final String tanggal_checkout = checkDate;
								final String realisasi_kegiatan = description_kegiatan.getText().toString();
								final String id_user_checkout = String.valueOf(id_user);
								final String nomor_checkin = nomor_checkin_;
								final String jenis_kendaraan = String.valueOf(id_jenis_kendaraan);
								final String lats_checkin = vlats;
								final String longs_checkin = vlongs;

								class insertToDatabase extends AsyncTask<Void, Void, String> {
									ProgressDialog loading;

									@Override
									protected void onPreExecute() {
										super.onPreExecute();
										loading = ProgressDialog.show(CheckoutActivityA.this,"Proses...","Silahkan Tunggu...",false,false);
									}

									@Override
									protected void onPostExecute(String s) {
										super.onPostExecute(s);
										loading.dismiss();
										//Toast.makeText(CheckoutActivity.this,"Checkin Berhasil !",Toast.LENGTH_LONG).show();
										ArrayList<Trx_Checkin> checkin_list = databaseHandler.getAllCheckinParam(nomor_checkin);
										Trx_Checkin ckin = new Trx_Checkin();
										for (Trx_Checkin tmpcheckin : checkin_list)
											ckin = tmpcheckin;

										databaseHandler.updateRencanaDetailfromCheckout(String.valueOf(ckin.getId_rencana_detail()));
										databaseHandler.updateTrxCheckin(nomor_checkin);
										showCustomDialogSukses("CheckOut Sukses");
									}

									@Override
									protected String doInBackground(Void... params) {
										HashMap<String,String> hashMap  = new HashMap<>();
										hashMap.put("nomor_checkin",nomor_checkin);
										hashMap.put("id_jenis_kendaraan",jenis_kendaraan);
										hashMap.put("realisasi_kegiatan",realisasi_kegiatan);
										hashMap.put("tanggal_checkout",tanggal_checkout);
										hashMap.put("id_user",id_user_checkout);
										hashMap.put("lats",lats_checkin);
										hashMap.put("longs",longs_checkin);

										RequestHandler rh = new RequestHandler();
										String res = rh.sendPostRequest(AppVar.POST_CHECKOUT, hashMap);
										return res;
									}
								}
								insertToDatabase in = new insertToDatabase();
								in.execute();
							}
						}else{
							String msg = getApplicationContext()
									.getResources().getString(R.string.app_checkin_incorrect_jenis_kendaraan);
							showCustomDialog(msg);
						}
					} else {
						String msg = getApplicationContext()
								.getResources().getString(R.string.app_checkin_incorrect_value);
						showCustomDialog(msg);
					}

				}else{
					Toast.makeText(this, "Buka google map pada handphone anda, lalu login ulang aplikasi absen online",Toast.LENGTH_LONG).show();
				}
			}else {
				Toast.makeText(CheckoutActivityA.this, "Silahkan periksa lokasi anda dahulu",Toast.LENGTH_LONG).show();
			}
		}
	}

	private LocationListener locationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			try {
				String strStatus = "";
				switch (status) {
					case GpsStatus.GPS_EVENT_FIRST_FIX:
						strStatus = "GPS_EVENT_FIRST_FIX";
						break;
					case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
						strStatus = "GPS_EVENT_SATELLITE_STATUS";
						break;
					case GpsStatus.GPS_EVENT_STARTED:
						strStatus = "GPS_EVENT_STARTED";
						break;
					case GpsStatus.GPS_EVENT_STOPPED:
						strStatus = "GPS_EVENT_STOPPED";
						break;
					default:
						strStatus = String.valueOf(status);
						break;
				}
				Log.i(LOG_TAG, "locationListener " + strStatus);
			} catch (Exception e) {
				Log.d(LOG_TAG, "locationListener Error");
			}
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onLocationChanged(Location location) {
			try {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				Log.d(LOG_TAG, "latitude " + latitude);
				Log.d(LOG_TAG, "longitude " + longitude);
			} catch (Exception e) {
				Log.i(LOG_TAG, "onLocationChanged " + e.toString());
			}
		}
	};

	public void resetCheckout() {
		checkin_number.setSelection(0);
		description_kegiatan.setText("");
		//databaseHandler.deleteCheckin(nomor_checkin_);
//		Intent intentActivity = new Intent(this,
//				CheckoutActivityA.class);
//		startActivity(intentActivity);
//		finish();
	}

//	@Override
//	public void onBackPressed() {
//		if (mNavigationDrawerFragment.isDrawerOpen())
//			mNavigationDrawerFragment.closeDrawer();
//		else
//			super.onBackPressed();
//	}

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

	public void showCustomDialogSukses(String msg) {
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
//								resetCheckout();
								gotoDashboard();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public void showCustomDialogInvalidCheckin(String msg) {
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
								gotoDashboard();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public void gotoDashboard(){
		checkin_number.setSelection(0);
		description_kegiatan.setText("");
		Intent intentActivity = new Intent(this,
				DashboardActivity.class);
		startActivity(intentActivity);
		finish();
	}

	public void gotoInventory(){
		Intent intentActivity = new Intent(this,
				DetailJadwalActivity.class);
		startActivity(intentActivity);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			gotoInventory();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
