package com.android.canvasing.gglc.absen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.absen.mobile.gglc.R;
import com.android.canvasing.gglc.database.DatabaseHandler;
import com.android.canvasing.gglc.database.User;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    private EditText txt_username;
    private EditText txt_password;
    private Button btn_login;
    private Context context;
    private ProgressDialog pDialog;
    private Context act;
    private Toolbar mToolbar;
    private DatabaseHandler database;
    private Handler handler = new Handler();


    protected LocationManager locationManager;
    private ImageButton btn_img;
    private TextView txt_tanggal, txt_jam;
    private double latitude, longitude;
    private Location location;
    private Location location1;
    private Location location2;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    //LoginDataBaseAdapter loginDataBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = LoginActivity.this;
        startMonitoring();
        checkGPS();
        /*
        DatabaseHandler db = new DatabaseHandler(this);

        // Inserting Contacts
        Log.d("Insert: ", "Inserting ..");
        db.addLabAbsen(new Absen("Ravi","2017-08-02 08:00", "Jl. Udang"));

        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        List<Absen> absen = db.getAllAbsen();

        for (Absen cn : absen) {
            String log = "id absen: "+cn.getId_absen()+" ,nama karyawan: " + cn.getNama_karyawan() + " ,waktu: " + cn.getWaktu()
                       + " ,lokasi: " + cn.getLokasi();
            // Writing Contacts to log
            Log.d("nama karyawan: ", log);
        }
        */

        pDialog = new ProgressDialog(context);
        txt_username = (EditText) findViewById(R.id.txt_username);
        txt_password = (EditText) findViewById(R.id.txt_password);
        btn_login = (Button) findViewById(R.id.btn_login);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        //act = this;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.title_activity_all);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_username.getText().length()==0 && txt_password.getText().length()==0){
                    Toast.makeText(LoginActivity.this, "Isi kolom username dan password terlebih dahulu",Toast.LENGTH_LONG).show();
                }else if(txt_username.getText().length()==0){
                    Toast.makeText(LoginActivity.this, "Kolom username kosong",Toast.LENGTH_LONG).show();
                }else if(txt_password.getText().length()==0){
                Toast.makeText(LoginActivity.this, "Kolom password kosong",Toast.LENGTH_LONG).show();
                }else{
                    login();
                }
            }
        });
    }
    private void login() {
        TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        final String mac = mngr.getDeviceId();

        final String email = txt_username.getText().toString().trim();
        final String password = txt_password.getText().toString().trim();
        pDialog.setMessage("Login Process...");
        showDialog();
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppVar.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            JSONObject out = jObj.getJSONObject("out");
                            String rsp = out.getString("response");
                            String id_awo = out.getString("id_awo");
                            String id_user = out.getString("id_user");
                            String nama_awo = out.getString("nama_awo");
                            String no_hp = out.getString("no_hp");
                            String password = txt_password.getText().toString();
                            if (rsp.equals(AppVar.LOGIN_SUCCESS)) {
                                hideDialog();
                                saveAppDataAwoNama(id_awo,id_user,nama_awo,password,no_hp);
                                savetoDB(nama_awo,no_hp);
                                gotoNavigationdrawer();
                            } else if (rsp.equals("successy")) {
                                hideDialog();
                                login_looping();
                                //Toast.makeText(context, "anda tidak login pada perangkat yang benar", Toast.LENGTH_LONG).show();

                            }else {
                                hideDialog();
                                Toast.makeText(context, "username atau password tidak sesuai", Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (JSONException e) {

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        Toast.makeText(context, "Gagal konek ke server", Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put(AppVar.KEY_EMAIL, email);
                params.put(AppVar.KEY_PASSWORD, password);
                params.put(AppVar.KEY_MAC, mac);

                //returning parameter
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);

    }

    private void login_looping() {

        TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        final String mac = mngr.getDeviceId();

        final String email = txt_username.getText().toString().trim();
        final String password = txt_password.getText().toString().trim();
        pDialog.setMessage("Login Process...");
        showDialog();
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppVar.LOGIN_URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            JSONObject out = jObj.getJSONObject("out");
                            String rsp = out.getString("response");
                            String id_awo = out.getString("id_awo");
                            String id_user = out.getString("id_user");
                            String no_hp = out.getString("no_hp");
                            String nama_awo = out.getString("nama_awo");
                            String password = txt_password.getText().toString();
                            if (rsp.equals(AppVar.LOGIN_SUCCESS)) {
                                hideDialog();
                                saveAppDataAwoNama(id_awo,id_user,nama_awo,no_hp,password);
                                savetoDB(nama_awo,no_hp);
                                gotoNavigationdrawer();
                                //finish();

                            } else if (rsp.equals("successy")) {
                                hideDialog();
                                Toast.makeText(context, "anda tidak login pada perangkat yang benar", Toast.LENGTH_LONG).show();

                            }else {
                                hideDialog();
                                Toast.makeText(context, "username atau password tidak sesuai", Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (JSONException e) {

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        Toast.makeText(context, "Gagal konek ke server", Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put(AppVar.KEY_EMAIL, email);
                params.put(AppVar.KEY_PASSWORD, password);
                params.put(AppVar.KEY_MAC, mac);

                //returning parameter
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);

    }

    private void showDialog() {
        if (!pDialog.isShowing())
        pDialog.show();
    }

    private void gotoNavigationdrawer(){
        Intent intent = new Intent(context, AbsenActivity.class);
        startActivity(intent);
        finish();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void saveAppDataAwoNama(String id_awo,String id_user, String nama_awo,String no_hp, String password) {
        SharedPreferences.Editor editor = getSharedPreferences(AppVar.SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit();
        editor.putString("id_awo", id_awo);
        editor.putString("id_user", id_user);
        editor.putString("nama_awo", nama_awo);
        editor.putString("no_hp", no_hp);
        editor.putString("password", password);
        editor.apply();
    }

    public void startMonitoring() {
        context.startService(new Intent(context, TrackingService.class));
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

    public void savetoDB( String nama_awo,String no_hp) {
        DatabaseHandler db = new DatabaseHandler(this);

        if(db.getCountUser()==0){
            db.addUser(new User(nama_awo, no_hp));
        }else{
            db.deleteContact();
            db.addUser(new User(nama_awo, no_hp));
        }

    }
}
