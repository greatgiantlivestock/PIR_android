package com.android.canvasing.gglc.absen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.absen.mobile.gglc.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;

@SuppressWarnings("deprecation")
public class AbsenActivity extends ActionBarActivity implements
		NavigationDrawerCallbacks, OnMapReadyCallback {
	private static final String TAG = null;
	private Toolbar mToolbar;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	protected LocationManager locationManager;
	private Button btn_img;
	private TextView txt_tanggal, txt_jam;
	private double latitude, longitude;
	private Location location;
	private Location location1;
	private Location location2;
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	private static final String LOG_TAG = AbsenActivity.class.getSimpleName();
	private Context act;
	private TextView ip_address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		act = getApplicationContext();

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_absensi);
		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		act = this;
		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle(R.string.title_activity_all);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.fragment_drawer);
		mNavigationDrawerFragment.setup(R.id.fragment_drawer,
				(DrawerLayout) findViewById(R.id.drawer), mToolbar);
		mNavigationDrawerFragment.selectItem(0);

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		btn_img = (Button) findViewById(R.id.btn_image);
		txt_tanggal = (TextView) findViewById(R.id.txt_tanggal);
		txt_jam = (TextView) findViewById(R.id.txt_jam);
		ip_address = (TextView) findViewById(R.id.textView12);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		getAbsen();

		getLocalIpAddress();
		btn_img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				insertToDatabase();
			}
		});
		checkGPS();
	}

	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						String ip = Formatter.formatIpAddress(inetAddress.hashCode());
						ip_address.setText(ip);
						Log.i(TAG, "***** IP="+ ip);
						return ip;
					}
				}
			}
		} catch (SocketException ex) {
			Log.e(TAG, ex.toString());
		}
		return null;
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		//LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager = (LocationManager) getApplicationContext()
				.getSystemService(Context.LOCATION_SERVICE);
		//locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
		//		1000L, 1.0f, locationListener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000L, 1.0f, locationListener);

		googleMap.addMarker(new MarkerOptions()
				.position(new LatLng(0, 0))
				.title("Blank Location"));

		boolean isGPSEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean isNetworkEnabled = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		if (!isGPSEnabled) {
			startActivityForResult(new Intent(
					Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
		}else{
			if(isNetworkEnabled) {
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
						1000L, 1.0f, locationListener);
				location1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				if(location1!=null){
					latitude = location1.getLatitude();
					longitude = location1.getLongitude();
					LatLng latLng = new LatLng(latitude, longitude);
					googleMap.setMyLocationEnabled(true);
					googleMap.getUiSettings().setZoomControlsEnabled(true);
					googleMap.getUiSettings().setAllGesturesEnabled(true);
					googleMap.getUiSettings().setMyLocationButtonEnabled(true);
					googleMap.getUiSettings().setCompassEnabled(true);
					googleMap.getUiSettings().setRotateGesturesEnabled(true);
					googleMap.getUiSettings().setZoomGesturesEnabled(true);
					googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
				}else{
					latitude = -4.82426757;
					longitude = 105.2717632;

					LatLng latLng = new LatLng(latitude, longitude);
					googleMap.setMyLocationEnabled(true);
					googleMap.getUiSettings().setZoomControlsEnabled(true);
					googleMap.getUiSettings().setAllGesturesEnabled(true);
					googleMap.getUiSettings().setMyLocationButtonEnabled(true);
					googleMap.getUiSettings().setCompassEnabled(true);
					googleMap.getUiSettings().setRotateGesturesEnabled(true);
					googleMap.getUiSettings().setZoomGesturesEnabled(true);
					googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
				}

				if (String.valueOf(latitude).length()==0){
					latitude = -4.82426757;
					longitude = 105.2717632;

					LatLng latLng = new LatLng(latitude, longitude);
					googleMap.setMyLocationEnabled(true);
					googleMap.getUiSettings().setZoomControlsEnabled(true);
					googleMap.getUiSettings().setAllGesturesEnabled(true);
					googleMap.getUiSettings().setMyLocationButtonEnabled(true);
					googleMap.getUiSettings().setCompassEnabled(true);
					googleMap.getUiSettings().setRotateGesturesEnabled(true);
					googleMap.getUiSettings().setZoomGesturesEnabled(true);
					googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
				}

			}else if (isGPSEnabled) {
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
						1000L, 1.0f, locationListener);
				location2 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				latitude = location2.getLatitude();
				longitude = location2.getLongitude();

				LatLng latLng = new LatLng(latitude, longitude);
				googleMap.setMyLocationEnabled(true);
				googleMap.getUiSettings().setZoomControlsEnabled(true);
				googleMap.getUiSettings().setAllGesturesEnabled(true);
				googleMap.getUiSettings().setMyLocationButtonEnabled(true);
				googleMap.getUiSettings().setCompassEnabled(true);
				googleMap.getUiSettings().setRotateGesturesEnabled(true);
				googleMap.getUiSettings().setZoomGesturesEnabled(true);
				googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));

			}else{
				Toast.makeText(AbsenActivity.this, "Silahkan periksa lokasi anda dahulu", Toast.LENGTH_LONG).show();
			}
		}
	}

	private void checkGPS() {
		locationManager = (LocationManager) getApplicationContext()
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000L, 1.0f, locationListener);
		boolean isGPSEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (!isGPSEnabled) {
			startActivityForResult(new Intent(
							android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
					0);
		} else {
			// if GPS Enabled get lat/long using GPS Services
			if (isGPSEnabled) {
				if (location == null) {
					locationManager.requestLocationUpdates(
							LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
					Log.d(LOG_TAG, "GPS Enabled");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();


						}
					}
				}
			} else {
				Intent intentGps = new Intent(
						"android.location.GPS_ENABLED_CHANGE");
				intentGps.putExtra("enabled", true);
				act.sendBroadcast(intentGps);
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

	private void insertToDatabase() {
		//LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager = (LocationManager) getApplicationContext()
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000L, 1.0f, locationListener);
		//locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
		//		1000L, 1.0f, locationListener);

		boolean isGPSEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean isNetworkEnabled = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		if (!isGPSEnabled) {
			startActivityForResult(new Intent(
					Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
		}else{
			if(isGPSEnabled){
				locationManager = (LocationManager) getApplicationContext()
						.getSystemService(Context.LOCATION_SERVICE);
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
						1000L, 1.0f, locationListener);
				location2 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) ;

				if (location2 != null){
					latitude=location2.getLatitude();
					longitude=location2.getLongitude();

					final String vlats=String.valueOf(latitude);
					final String vlongs=String.valueOf(longitude);
					final String ip=ip_address.getText().toString();

					SharedPreferences prefs = getSharedPreferences(AppVar.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
					final String id_awo = prefs.getString("id_awo","0");
					final String id_user = prefs.getString("id_user","0");

					class insertToDatabase extends AsyncTask<Void, Void, String> {
						ProgressDialog loading;

						@Override
						protected void onPreExecute() {
							super.onPreExecute();
							loading = ProgressDialog.show(AbsenActivity.this,"Proses...","Silahkan Tunggu...",false,false);
						}

						@Override
						protected void onPostExecute(String s) {
							super.onPostExecute(s);
							loading.dismiss();
							Toast.makeText(AbsenActivity.this,"Absen Berhasil !",Toast.LENGTH_LONG).show();
						}

						@Override
						protected String doInBackground(Void... params) {
							HashMap<String,String> hashMap  = new HashMap<>();
							hashMap.put("lat",vlats);
							hashMap.put("lng",vlongs);
							hashMap.put("id_awo",id_awo);
							hashMap.put("id_user",id_user);
							hashMap.put("ip",ip);

							RequestHandler rh = new RequestHandler();
							String res = rh.sendPostRequest(AppVar.POST_ABSEN, hashMap);
							return res;
						}
					}
					insertToDatabase in = new insertToDatabase();
					in.execute();

					getAbsen();
				}else{
					locationManager = (LocationManager) getApplicationContext()
							.getSystemService(Context.LOCATION_SERVICE);
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
							1000L, 1.0f, locationListener);
					location1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

					latitude=location1.getLatitude();
					longitude=location1.getLongitude();
					final String vlats=String.valueOf(latitude);
					final String vlongs=String.valueOf(longitude);
					final String ip=ip_address.getText().toString();

					SharedPreferences prefs = getSharedPreferences(AppVar.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
					final String id_awo = prefs.getString("id_awo","0");
					final String id_user = prefs.getString("id_user","0");

					class insertToDatabase extends AsyncTask<Void, Void, String> {
						ProgressDialog loading;

						@Override
						protected void onPreExecute() {
							super.onPreExecute();
							loading = ProgressDialog.show(AbsenActivity.this,"Proses...","Silahkan Tunggu...",false,false);
						}

						@Override
						protected void onPostExecute(String s) {
							super.onPostExecute(s);
							loading.dismiss();
							Toast.makeText(AbsenActivity.this,"Absen Berhasil !",Toast.LENGTH_LONG).show();
						}

						@Override
						protected String doInBackground(Void... params) {
							HashMap<String,String> hashMap  = new HashMap<>();
							hashMap.put("lat",vlats);
							hashMap.put("lng",vlongs);
							hashMap.put("id_awo",id_awo);
							hashMap.put("id_user",id_user);
							hashMap.put("ip",ip);

							RequestHandler rh = new RequestHandler();
							String res = rh.sendPostRequest(AppVar.POST_ABSEN, hashMap);
							return res;
						}
					}
					insertToDatabase in = new insertToDatabase();
					in.execute();

					getAbsen();
				}
			}else if(isNetworkEnabled){
				locationManager = (LocationManager) getApplicationContext()
						.getSystemService(Context.LOCATION_SERVICE);
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
						1000L, 1.0f, locationListener);
				location1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

				if(location1 != null){
					latitude=location1.getLatitude();
					longitude=location1.getLongitude();
					final String vlats=String.valueOf(latitude);
					final String vlongs=String.valueOf(longitude);
					final String ip=ip_address.getText().toString();

					SharedPreferences prefs = getSharedPreferences(AppVar.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
					final String id_awo = prefs.getString("id_awo","0");
					final String id_user = prefs.getString("id_user","0");

					class insertToDatabase extends AsyncTask<Void, Void, String> {
						ProgressDialog loading;

						@Override
						protected void onPreExecute() {
							super.onPreExecute();
							loading = ProgressDialog.show(AbsenActivity.this,"Proses...","Silahkan Tunggu...",false,false);
						}

						@Override
						protected void onPostExecute(String s) {
							super.onPostExecute(s);
							loading.dismiss();
							Toast.makeText(AbsenActivity.this,"Absen Berhasil !",Toast.LENGTH_LONG).show();
						}

						@Override
						protected String doInBackground(Void... params) {
							HashMap<String,String> hashMap  = new HashMap<>();
							hashMap.put("lat",vlats);
							hashMap.put("lng",vlongs);
							hashMap.put("id_awo",id_awo);
							hashMap.put("id_user",id_user);
							hashMap.put("ip",ip);

							RequestHandler rh = new RequestHandler();
							String res = rh.sendPostRequest(AppVar.POST_ABSEN, hashMap);
							return res;
						}
					}
					insertToDatabase in = new insertToDatabase();
					in.execute();

					getAbsen();
				}else{
					Toast.makeText(this, "Buka google map pada handphone anda, lalu login ulang aplikasi absen online",Toast.LENGTH_LONG).show();
				}
			}else {
				Toast.makeText(AbsenActivity.this, "Silahkan periksa lokasi anda dahulu",Toast.LENGTH_LONG).show();
			}
		}
	}

	private void getAbsen() {
		class getAbsenClass extends AsyncTask<Void, Void, String> {
			ProgressDialog loading;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				loading = ProgressDialog.show(AbsenActivity.this,"Mengambil data..","menunggu..",false,false);
			}

			@Override
			protected void onPostExecute(String s) {
				super.onPostExecute(s);
				loading.dismiss();
				showAbsen(s);
			}

			@Override
			protected String doInBackground(Void... params) {
				SharedPreferences prefs = getSharedPreferences(AppVar.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
				String id_awo = prefs.getString("id_awo","0");

				RequestHandler rh = new RequestHandler();
				String s = rh.sendGetRequestParam(AppVar.GET_ABSEN,id_awo);
				return s;
			}
		}

		getAbsenClass ge = new getAbsenClass();
		ge.execute();
	}

	/*
	public String getAddress(double lat, double lng) {
		String strAdd = "";
		Geocoder geocoder = new Geocoder(_context, Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
			if (addresses != null){
				Address returnedAddress = addresses.get(0);
				StringBuilder strReturnedAddress = new StringBuilder("");

				for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
					strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(" ");
				}
				strAdd = strReturnedAddress.toString();
			} else {
				Log.w(LOG_TAG,
						"My Current loction address No Address returned!");
			}
		} catch (Exception e) {
			Log.w(LOG_TAG, "My Current loction address Canont get Address!");
		}
		return strAdd;
	}
	*/


	private void showAbsen(String json){
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONObject out = jsonObject.getJSONObject("out");

			SimpleDateFormat tanggal_tampil_in = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat tanggal_tampil_out = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat jam_tampil = new SimpleDateFormat("HH:mm");

			try {
				Date dtanggal = tanggal_tampil_in.parse(out.getString("tanggal"));
				Date djam = jam_tampil.parse(out.getString("jam"));
				String tanggalx = tanggal_tampil_out.format(dtanggal);
				String jamx = jam_tampil.format(djam);
				txt_tanggal.setText(tanggalx);
				txt_jam.setText(jamx);
			}catch (ParseException e) {
				e.printStackTrace();
			}


		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		if (mNavigationDrawerFragment != null) {
			if (mNavigationDrawerFragment.getCurrentSelectedPosition() != 0) {
				if (position == 1) {
					Intent intentActivity = new Intent(this,
							History_absen.class);
					startActivity(intentActivity);
					finish();
				} else if (position == 2) {
					Intent intentActivity = new Intent(this,
							ChangePassword.class);
					startActivity(intentActivity);
					finish();
				}/* else if (position == 3) {
					Intent intentActivity = new Intent(this,
							CstomerActivity.class);
					startActivity(intentActivity);
					finish();
				}*/
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
