package com.android.pir.gglc.absen;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pir.gglc.pir.PlanVisitActivity;
import com.android.pir.mobile.R;
import com.android.pir.gglc.database.DatabaseHandler;
import com.android.pir.gglc.database.MstUser;
import com.android.pir.gglc.database.User;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
import java.util.HashMap;
import java.util.Map;

import static com.android.pir.gglc.absen.AppVar.SHARED_PREFERENCES_NAME;

//public class LoginActivity extends AppCompatActivity implements LocationAssistantLogin.Listener{
public class LoginActivity extends AppCompatActivity{
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    private EditText txt_username;
    private EditText txt_password;
    private TextView id_device;
    private Button btn_login;
    private ProgressDialog pDialog;
    private Context act;
    private Toolbar mToolbar;
    private DatabaseHandler db;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog;

    protected LocationManager locationManager;
    private double latitude, longitude;
    private Location location;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    private String message, response_data,mac;

    //LoginDataBaseAdapter loginDataBaseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!isTaskRoot()) {
            finish();
            return;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainold);

        act=this;
        db = new DatabaseHandler(this);
        pDialog = new ProgressDialog(act);
        txt_username = (EditText) findViewById(R.id.txt_username);
        txt_password = (EditText) findViewById(R.id.txt_password);
        id_device = (TextView) findViewById(R.id.id_device);
        btn_login = (Button) findViewById(R.id.btn_login);
        mac = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        id_device.setText(mac);
//        Toast.makeText(this, "Your ID : "+mac,Toast.LENGTH_LONG).show();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getApplicationContext().getResources()
                .getString(R.string.app_name));
        progressDialog.setMessage(getApplicationContext().getResources()
                .getString(R.string.app_login_processing));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.title_activity_all);

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied", id_device.getText().toString());
        clipboard.setPrimaryClip(clip);

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
//        TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//        final String mac = mngr.getDeviceId();
//        final String mac = "357325072176548";
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
                            String id_wilayah = out.getString("id_wilayah");
                            String password = txt_password.getText().toString();
                            if (rsp.equals(AppVar.LOGIN_SUCCESS)) {
                                hideDialog();
                                saveAppDataAwoNama(id_awo,id_user,nama_awo,no_hp,id_wilayah,password);
                                savetoDB(nama_awo,no_hp);
                            } else if (rsp.equals("successy")) {
                                hideDialog();
                                login_looping();
                                //Toast.makeText(context, "anda tidak login pada perangkat yang benar", Toast.LENGTH_LONG).show();
                            }else {
                                hideDialog();
                                Toast.makeText(act, "username atau password tidak sesuai", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(act, "Gagal konek ke server", Toast.LENGTH_LONG).show();
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
                            String id_wilayah = out.getString("id_wilayah");
                            String password = txt_password.getText().toString();
                            if (rsp.equals(AppVar.LOGIN_SUCCESS)) {
                                hideDialog();
                                saveAppDataAwoNama(id_awo,id_user,nama_awo,no_hp,id_wilayah,password);
                                savetoDB(nama_awo,no_hp);
                                //finish();
                            } else if (rsp.equals("successy")) {
                                hideDialog();
                                Toast.makeText(act, "anda tidak login pada perangkat yang benar", Toast.LENGTH_LONG).show();
                            }else {
                                hideDialog();
                                Toast.makeText(act, "username atau password tidak sesuai", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(act, "Gagal konek ke server", Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(act, PlanVisitActivity.class);
        startActivity(intent);
        finish();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void saveAppDataAwoNama(String id_awo,String id_user, String nama_awo,String no_hp, String id_wilayah, String password) {
        SharedPreferences.Editor editor = getSharedPreferences(AppVar.SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit();
        editor.putString("id_awo", id_awo);
        editor.putString("id_user", id_user);
        editor.putString("nama_awo", nama_awo);
        editor.putString("no_hp", no_hp);
        editor.putString("password", password);
        editor.putString("id_wilayah", id_wilayah);
        editor.apply();
    }

//    public void startMonitoring() {
//        act.startService(new Intent(act, TrackingService.class));
//    }

//    private void checkGPS() {
//        locationManager = (LocationManager) getApplicationContext()
//                .getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                1000L, 1.0f, locationListener);
//        boolean isGPSEnabled = locationManager
//                .isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//        if (!isGPSEnabled) {
//            startActivityForResult(new Intent(
//                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
//                    0);
//        } else {
//            // if GPS Enabled get lat/long using GPS Services
//            if (isGPSEnabled) {
//                if (location == null) {
//                    locationManager.requestLocationUpdates(
//                            LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
//                            MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
//                    Log.d(LOG_TAG, "GPS Enabled");
//                    if (locationManager != null) {
//                        location = locationManager
//                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                        if (location != null) {
//                            latitude = location.getLatitude();
//                            longitude = location.getLongitude();
//
//
//                        }
//                    }
//                }
//            } else {
//                Intent intentGps = new Intent(
//                        "android.location.GPS_ENABLED_CHANGE");
//                intentGps.putExtra("enabled", true);
//                act.sendBroadcast(intentGps);
//            }
//
//        }
//    }

//    private LocationListener locationListener = new LocationListener() {
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//            try {
//                String strStatus = "";
//                switch (status) {
//                    case GpsStatus.GPS_EVENT_FIRST_FIX:
//                        strStatus = "GPS_EVENT_FIRST_FIX";
//                        break;
//                    case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
//                        strStatus = "GPS_EVENT_SATELLITE_STATUS";
//                        break;
//                    case GpsStatus.GPS_EVENT_STARTED:
//                        strStatus = "GPS_EVENT_STARTED";
//                        break;
//                    case GpsStatus.GPS_EVENT_STOPPED:
//                        strStatus = "GPS_EVENT_STOPPED";
//                        break;
//                    default:
//                        strStatus = String.valueOf(status);
//                        break;
//                }
//                Log.i(LOG_TAG, "locationListener " + strStatus);
//            } catch (Exception e) {
//                Log.d(LOG_TAG, "locationListener Error");
//            }
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//        }
//
//        @Override
//        public void onLocationChanged(Location location) {
//            try {
//                latitude = location.getLatitude();
//                longitude = location.getLongitude();
//                Log.d(LOG_TAG, "latitude " + latitude);
//                Log.d(LOG_TAG, "longitude " + longitude);
//            } catch (Exception e) {
//                Log.i(LOG_TAG, "onLocationChanged " + e.toString());
//            }
//        }
//    };

    public void savetoDB( String nama_awo,String no_hp) {
        if(db.getCountUser()==0){
            db.addUser(new User(nama_awo, no_hp));
            new DownloadDataUser().execute();
        }else{
            db.deleteContact();
            db.addUser(new User(nama_awo, no_hp));
            gotoNavigationdrawer();
        }
    }

    //download data user to sqlite
    private class DownloadDataUser extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
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
                    + AppVar.CONFIG_APP_URL_DOWNLOAD_USER+ "?id_karyawan="
                    + id_karyawan;
            HttpResponse response = getDownloadData(download_data_url);
            int retCode = (response != null) ? response.getStatusLine()
                    .getStatusCode() : -1;
            if (retCode != 200) {
                message = act.getApplicationContext().getResources()
                        .getString(R.string.MSG_DLG_LABEL_URL_NOT_FOUND);
                handler.post(new Runnable() {
                    public void run() {
//                        showCustomDialog(message);
                        Toast.makeText(LoginActivity.this, message,Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                try {
                    response_data = EntityUtils.toString(response.getEntity());

                    SharedPreferences spPreferences = getSharedPrefereces();
                    String main_app_table_data = spPreferences.getString(
                            AppVar.SHARED_PREFERENCES_TABLE_MST_USER, null);
                    if (main_app_table_data != null) {
                        if (main_app_table_data.equalsIgnoreCase(response_data)) {
                            saveAppDataUserSameData(act
                                    .getApplicationContext().getResources()
                                    .getString(R.string.app_value_true));
                        } else {
                            db.deleteTableMSTUser();
                            saveAppDataUserSameData(act
                                    .getApplicationContext().getResources()
                                    .getString(R.string.app_value_false));
                        }
                    } else {
                        db.deleteTableMSTUser();
                        saveAppDataUserSameData(act.getApplicationContext()
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
                saveAppDataUser(response_data);
                extractDataUser();
//                new DownloadDataCustomer().execute();
            } else {
                message = act.getApplicationContext().getResources()
                        .getString(R.string.MSG_DLG_LABEL_DOWNLOAD_FAILED);
                handler.post(new Runnable() {
                    public void run() {
//                        showCustomDialog(message);
                        Toast.makeText(LoginActivity.this, message,Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

//    //download data customer to sqlite
//    private class DownloadDataCustomer extends AsyncTask<String, Integer, String> {
//        @Override
//        protected void onPreExecute() {
//            progressDialog.setMessage(getApplicationContext().getResources()
//                    .getString(R.string.MSG_DLG_LABEL_SYNRONISASI_DATA));
//            progressDialog.show();
//            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                        @Override
//                        public void onCancel(DialogInterface dialog) {
//                            String msg = getApplicationContext()
//                                    .getResources()
//                                    .getString(
//                                            R.string.MSG_DLG_LABEL_SYNRONISASI_DATA_CANCEL);
//                            showCustomDialog(msg);
//                        }
//                    });
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
//            String id_wilayah= prefs.getString("id_wilayah","null");
//
//            String download_data_url = AppVar.CONFIG_APP_URL_PUBLIC
//                    + AppVar.CONFIG_APP_URL_DOWNLOAD_CUSTOMER+ "?id_wilayah="
//                    + id_wilayah;
//            HttpResponse response = getDownloadData(download_data_url);
//            int retCode = (response != null) ? response.getStatusLine().getStatusCode() : -1;
//            if (retCode != 200) {
//                message = act.getApplicationContext().getResources()
//                        .getString(R.string.MSG_DLG_LABEL_URL_NOT_FOUND);
//                handler.post(new Runnable() {
//                    public void run() {
//                        showCustomDialog(message);
//                    }
//                });
//            } else {
//                try {
//                    response_data = EntityUtils.toString(response.getEntity());
//
//                    SharedPreferences spPreferences = getSharedPrefereces();
//                    String main_app_table_data = spPreferences.getString(
//                            AppVar.SHARED_PREFERENCES_TABLE_MST_CUSTOMER, null);
//                    if (main_app_table_data != null) {
//                        if (main_app_table_data.equalsIgnoreCase(response_data)) {
//                            saveAppDataBranchSameData(act
//                                    .getApplicationContext().getResources()
//                                    .getString(R.string.app_value_true));
//                        } else {
//                            db.deleteTableMSTCustomer();
//                            saveAppDataBranchSameData(act
//                                    .getApplicationContext().getResources()
//                                    .getString(R.string.app_value_false));
//                        }
//                    } else {
//                        db.deleteTableMSTCustomer();
//                        saveAppDataBranchSameData(act.getApplicationContext()
//                                .getResources()
//                                .getString(R.string.app_value_false));
//                    }
//                } catch (ParseException e) {
//                    message = e.toString();
//                    handler.post(new Runnable() {
//                        public void run() {
//                            showCustomDialog(message);
//                        }
//                    });
//                } catch (IOException e) {
//                    message = e.toString();
//                    handler.post(new Runnable() {
//                        public void run() {
//                            showCustomDialog(message);
//                        }
//                    });
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            if (response_data != null) {
//                saveAppDataBranch(response_data);
//                extractDataBranch();
////                new DownloadDataProduct().execute();
//            } else {
//                message = act.getApplicationContext().getResources()
//                        .getString(R.string.MSG_DLG_LABEL_DOWNLOAD_FAILED);
//                handler.post(new Runnable() {
//                    public void run() {
//                        showCustomDialog(message);
//                    }
//                });
//            }
//        }
//    }

    //download data user to sqlite
//    private class DownloadDataKegiatan extends AsyncTask<String, Integer, String> {
//        @Override
//        protected void onPreExecute() {
//            progressDialog.setMessage(getApplicationContext().getResources()
//                    .getString(R.string.MSG_DLG_LABEL_SYNRONISASI_DATA));
//            progressDialog.show();
//            progressDialog
//                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
//                        @Override
//                        public void onCancel(DialogInterface dialog) {
//                            String msg = getApplicationContext()
//                                    .getResources()
//                                    .getString(
//                                            R.string.MSG_DLG_LABEL_SYNRONISASI_DATA_CANCEL);
//                            showCustomDialog(msg);
//                        }
//                    });
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
//            String id_karyawan = prefs.getString("id_awo","null");
//
//            String download_data_url = AppVar.CONFIG_APP_URL_PUBLIC
//                    + AppVar.CONFIG_APP_URL_DOWNLOAD_KEGIATAN+ "?id_karyawan="
//                    + id_karyawan;
//            HttpResponse response = getDownloadData(download_data_url);
//            int retCode = (response != null) ? response.getStatusLine()
//                    .getStatusCode() : -1;
//            if (retCode != 200) {
//                message = act.getApplicationContext().getResources()
//                        .getString(R.string.MSG_DLG_LABEL_URL_NOT_FOUND);
//                handler.post(new Runnable() {
//                    public void run() {
//                        showCustomDialog(message);
//                    }
//                });
//            } else {
//                try {
//                    response_data = EntityUtils.toString(response.getEntity());
//
//                    SharedPreferences spPreferences = getSharedPrefereces();
//                    String main_app_table_data = spPreferences.getString(
//                            AppVar.SHARED_PREFERENCES_TABLE_MST_KEGIATAN, null);
//                    if (main_app_table_data != null) {
//                        if (main_app_table_data.equalsIgnoreCase(response_data)) {
//                            saveAppDataKegiatanSameData(act
//                                    .getApplicationContext().getResources()
//                                    .getString(R.string.app_value_true));
//                        } else {
//                            db.deleteTableMSTKegiattan();
//                            saveAppDataKegiatanSameData(act
//                                    .getApplicationContext().getResources()
//                                    .getString(R.string.app_value_false));
//                        }
//                    } else {
//                        db.deleteTableMSTKegiattan();
//                        saveAppDataKegiatanSameData(act.getApplicationContext()
//                                .getResources()
//                                .getString(R.string.app_value_false));
//                    }
//                } catch (ParseException e) {
//                    message = e.toString();
//                    handler.post(new Runnable() {
//                        public void run() {
//                            showCustomDialog(message);
//                        }
//                    });
//                } catch (IOException e) {
//                    message = e.toString();
//                    handler.post(new Runnable() {
//                        public void run() {
//                            showCustomDialog(message);
//                        }
//                    });
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            if (response_data != null) {
//                saveAppDataKegiatan(response_data);
//                extractDataKegiatan();
//                new DownloadDataProduct().execute();
//            } else {
//                message = act.getApplicationContext().getResources()
//                        .getString(R.string.MSG_DLG_LABEL_DOWNLOAD_FAILED);
//                handler.post(new Runnable() {
//                    public void run() {
//                        showCustomDialog(message);
//                    }
//                });
//            }
//        }
//
//    }

    //download data user to sqlite
//    private class DownloadDataProduct extends AsyncTask<String, Integer, String> {
//        @Override
//        protected void onPreExecute() {
//            progressDialog.setMessage(getApplicationContext().getResources()
//                    .getString(R.string.MSG_DLG_LABEL_SYNRONISASI_DATA));
//            progressDialog.show();
//            progressDialog
//                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
//                        @Override
//                        public void onCancel(DialogInterface dialog) {
//                            String msg = getApplicationContext()
//                                    .getResources()
//                                    .getString(
//                                            R.string.MSG_DLG_LABEL_SYNRONISASI_DATA_CANCEL);
//                            showCustomDialog(msg);
//                        }
//                    });
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String download_data_url = AppVar.CONFIG_APP_URL_PUBLIC
//                    + AppVar.CONFIG_APP_URL_DOWNLOAD_PRODUCT;
//            HttpResponse response = getDownloadData(download_data_url);
//            int retCode = (response != null) ? response.getStatusLine()
//                    .getStatusCode() : -1;
//            if (retCode != 200) {
//                message = act.getApplicationContext().getResources()
//                        .getString(R.string.MSG_DLG_LABEL_URL_NOT_FOUND);
//                handler.post(new Runnable() {
//                    public void run() {
//                        showCustomDialog(message);
//                    }
//                });
//            } else {
//                try {
//                    response_data = EntityUtils.toString(response.getEntity());
//
//                    SharedPreferences spPreferences = getSharedPrefereces();
//                    String main_app_table_data = spPreferences.getString(
//                            AppVar.SHARED_PREFERENCES_TABLE_PRODUCT, null);
//                    if (main_app_table_data != null) {
//                        if (main_app_table_data.equalsIgnoreCase(response_data)) {
//                            saveAppDataProductSameData(act
//                                    .getApplicationContext().getResources()
//                                    .getString(R.string.app_value_true));
//                        } else {
//                            db.deleteTableProduct();
//                            saveAppDataProductSameData(act
//                                    .getApplicationContext().getResources()
//                                    .getString(R.string.app_value_false));
//                        }
//                    } else {
//                        db.deleteTableProduct();
//                        saveAppDataProductSameData(act.getApplicationContext()
//                                .getResources()
//                                .getString(R.string.app_value_false));
//                    }
//                } catch (ParseException e) {
//                    message = e.toString();
//                    handler.post(new Runnable() {
//                        public void run() {
//                            showCustomDialog(message);
//                        }
//                    });
//                } catch (IOException e) {
//                    message = e.toString();
//                    handler.post(new Runnable() {
//                        public void run() {
//                            showCustomDialog(message);
//                        }
//                    });
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            if (response_data != null) {
//                saveAppDataProduct(response_data);
//                extractDataProduct();
////                new DownloadDataJenisKendaraan().execute();
//            } else {
//                message = act.getApplicationContext().getResources()
//                        .getString(R.string.MSG_DLG_LABEL_DOWNLOAD_FAILED);
//                handler.post(new Runnable() {
//                    public void run() {
//                        showCustomDialog(message);
//                    }
//                });
//            }
//        }
//
//    }

//    private class DownloadDataJenisKendaraan extends AsyncTask<String, Integer, String> {
//        @Override
//        protected void onPreExecute() {
//            progressDialog.setMessage(getApplicationContext().getResources()
//                    .getString(R.string.MSG_DLG_LABEL_SYNRONISASI_DATA));
//            progressDialog.show();
//            progressDialog
//                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
//                        @Override
//                        public void onCancel(DialogInterface dialog) {
//                            String msg = getApplicationContext()
//                                    .getResources()
//                                    .getString(
//                                            R.string.MSG_DLG_LABEL_SYNRONISASI_DATA_CANCEL);
//                            showCustomDialog(msg);
//                        }
//                    });
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
//            String id_karyawan = prefs.getString("id_awo","null");
//
//            String download_data_url = AppVar.CONFIG_APP_URL_PUBLIC
//                    + AppVar.CONFIG_APP_URL_DOWNLOAD_JENIS_KENDARAAN + "?id_karyawan="+ id_karyawan;
//            HttpResponse response = getDownloadData(download_data_url);
//            int retCode = (response != null) ? response.getStatusLine()
//                    .getStatusCode() : -1;
//            if (retCode != 200) {
//                message = act.getApplicationContext().getResources()
//                        .getString(R.string.MSG_DLG_LABEL_URL_NOT_FOUND);
//                handler.post(new Runnable() {
//                    public void run() {
//                        showCustomDialog(message);
//                    }
//                });
//            } else {
//                try {
//                    response_data = EntityUtils.toString(response.getEntity());
//
//                    SharedPreferences spPreferences = getSharedPrefereces();
//                    String main_app_table_data = spPreferences.getString(
//                            AppVar.SHARED_PREFERENCES_TABLE_JENIS_KEGIATAN, null);
//                    if (main_app_table_data != null) {
//                        if (main_app_table_data.equalsIgnoreCase(response_data)) {
//                            saveAppDataJenisKendaraanSameData(act
//                                    .getApplicationContext().getResources()
//                                    .getString(R.string.app_value_true));
//                        } else {
//                            db.deleteTableJenisKendaraan();
//                            saveAppDataJenisKendaraanSameData(act
//                                    .getApplicationContext().getResources()
//                                    .getString(R.string.app_value_false));
//                        }
//                    } else {
//                        db.deleteTableJenisKendaraan();
//                        saveAppDataJenisKendaraanSameData(act.getApplicationContext()
//                                .getResources()
//                                .getString(R.string.app_value_false));
//                    }
//                } catch (ParseException e) {
//                    message = e.toString();
//                    handler.post(new Runnable() {
//                        public void run() {
//                            showCustomDialog(message);
//                        }
//                    });
//                } catch (IOException e) {
//                    message = e.toString();
//                    handler.post(new Runnable() {
//                        public void run() {
//                            showCustomDialog(message);
//                        }
//                    });
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            if (response_data != null) {
//                saveAppDataJenisKendaraan(response_data);
//                extractDataJenisKendaraan();
//            } else {
//                message = act.getApplicationContext().getResources()
//                        .getString(R.string.MSG_DLG_LABEL_DOWNLOAD_FAILED);
//                handler.post(new Runnable() {
//                    public void run() {
//                        showCustomDialog(message);
//                    }
//                });
//            }
//        }
//
//    }
//
//    public void extractDataBranch() {
//        SharedPreferences spPreferences = getSharedPrefereces();
//        String main_app_table_same_data = spPreferences.getString(
//                AppVar.SHARED_PREFERENCES_TABLE_MST_CUSTOMER_SAME_DATA, null);
//        String main_app_table = spPreferences.getString(
//                AppVar.SHARED_PREFERENCES_TABLE_MST_CUSTOMER, null);
//        if (main_app_table_same_data.equalsIgnoreCase(act
//                .getApplicationContext().getResources()
//                .getString(R.string.app_value_false))) {
//            JSONObject oResponse;
//            try {
//                oResponse = new JSONObject(main_app_table);
//                JSONArray jsonarr = oResponse.getJSONArray("customer");
//                for (int i = 0; i < jsonarr.length(); i++) {
//                    JSONObject oResponsealue = jsonarr.getJSONObject(i);
//                    String id_customer = oResponsealue.isNull("id_customer") ? null
//                            : oResponsealue.getString("id_customer");
//                    String kode_customer = oResponsealue.isNull("kode_customer") ? null
//                            : oResponsealue.getString("kode_customer");
//                    String nama_customer = oResponsealue.isNull("nama_customer") ? null
//                            : oResponsealue.getString("nama_customer");
//                    String alamat = oResponsealue.isNull("alamat") ? null
//                            : oResponsealue.getString("alamat");
//                    String no_hp = oResponsealue.isNull("no_hp") ? null
//                            : oResponsealue.getString("no_hp");
//                    String lats = oResponsealue.isNull("lats") ? null
//                            : oResponsealue.getString("lats");
//                    String longs = oResponsealue.isNull("longs") ? null
//                            : oResponsealue.getString("longs");
//                    String id_wilayah = oResponsealue.isNull("id_wilayah") ? null
//                            : oResponsealue.getString("id_wilayah");
//                    Log.d(LOG_TAG, "id_customer:" + id_customer);
//                    Log.d(LOG_TAG, "kode_customer:" + kode_customer);
//                    Log.d(LOG_TAG, "nama_customer:" + nama_customer);
//                    Log.d(LOG_TAG, "lats:" + lats);
//                    Log.d(LOG_TAG, "longs:" + longs);
//                    db.addMst_customer(new Mst_Customer(Integer.parseInt(id_customer),kode_customer,nama_customer,alamat,no_hp,lats,longs,Integer.parseInt(id_wilayah)));
//                }
//
//            } catch (JSONException e) {
//                final String message = e.toString();
//                handler.post(new Runnable() {
//                    public void run() {
//                        showCustomDialog(message);
//                    }
//                });
//
//            }
//        }
//    }

    public void extractDataUser() {
        SharedPreferences spPreferences = getSharedPrefereces();
        String main_app_table_same_data = spPreferences.getString(
                AppVar.SHARED_PREFERENCES_TABLE_MST_USER_SAME_DATA, null);
        String main_app_table = spPreferences.getString(
                AppVar.SHARED_PREFERENCES_TABLE_MST_USER, null);
        if (main_app_table_same_data.equalsIgnoreCase(act
                .getApplicationContext().getResources()
                .getString(R.string.app_value_false))) {
            JSONObject oResponse;
            try {
                oResponse = new JSONObject(main_app_table);
                JSONArray jsonarr = oResponse.getJSONArray("user");
                for (int i = 0; i < jsonarr.length(); i++) {
                    JSONObject oResponsealue = jsonarr.getJSONObject(i);
                    String id_user = oResponsealue.isNull("id_user") ? null
                            : oResponsealue.getString("id_user");
                    String nama = oResponsealue.isNull("nama") ? null
                            : oResponsealue.getString("nama");
                    String username = oResponsealue.isNull("username") ? null
                            : oResponsealue.getString("username");
                    String password = oResponsealue.isNull("password") ? null
                            : oResponsealue.getString("password");
                    String id_departemen = oResponsealue.isNull("id_departemen") ? null
                            : oResponsealue.getString("id_departemen");
                    String id_wilayah = oResponsealue.isNull("id_wilayah") ? null
                            : oResponsealue.getString("id_wilayah");
                    String id_karyawan = oResponsealue.isNull("id_karyawan") ? null
                            : oResponsealue.getString("id_karyawan");
                    String no_hp = oResponsealue.isNull("no_hp") ? null
                            : oResponsealue.getString("no_hp");
                    String id_role = oResponsealue.isNull("id_role") ? null
                            : oResponsealue.getString("id_role");
                    Log.d(LOG_TAG, "id_user:" + id_user);
                    Log.d(LOG_TAG, "nama:" + nama);
                    Log.d(LOG_TAG, "username:" + username);
                    Log.d(LOG_TAG, "id_departemen:" + id_departemen);
                    Log.d(LOG_TAG, "id_wilayah:" + id_wilayah);
                    db.addMst_user(new MstUser(Integer.parseInt(id_user),nama,username,password,Integer.parseInt(id_departemen),
                            Integer.parseInt(id_wilayah),Integer.parseInt(id_karyawan),no_hp,Integer.parseInt(id_role)));
                }
                gotoNavigationdrawer();
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

//    public void extractDataKegiatan() {
//        SharedPreferences spPreferences = getSharedPrefereces();
//        String main_app_table_same_data = spPreferences.getString(
//                AppVar.SHARED_PREFERENCES_TABLE_MST_KEGIATAN_SAME_DATA, null);
//        String main_app_table = spPreferences.getString(
//                AppVar.SHARED_PREFERENCES_TABLE_MST_KEGIATAN, null);
//        if (main_app_table_same_data.equalsIgnoreCase(act
//                .getApplicationContext().getResources()
//                .getString(R.string.app_value_false))) {
//            JSONObject oResponse;
//            try {
//                oResponse = new JSONObject(main_app_table);
//                JSONArray jsonarr = oResponse.getJSONArray("kegiatan");
//                for (int i = 0; i < jsonarr.length(); i++) {
//                    JSONObject oResponsealue = jsonarr.getJSONObject(i);
//                    String id_kegiatan = oResponsealue.isNull("id_kegiatan") ? null
//                            : oResponsealue.getString("id_kegiatan");
//                    String nama_kegiatan = oResponsealue.isNull("nama_kegiatan") ? null
//                            : oResponsealue.getString("nama_kegiatan");
//                    String id_departemen = oResponsealue.isNull("id_departemen") ? null
//                            : oResponsealue.getString("id_departemen");
//                    String id_wilayah = oResponsealue.isNull("id_wilayah") ? null
//                            : oResponsealue.getString("id_wilayah");
//                    Log.d(LOG_TAG, "id_kegiatan:" + id_kegiatan);
//                    Log.d(LOG_TAG, "nama_kegiatan:" + nama_kegiatan);
//                    Log.d(LOG_TAG, "id_departemen:" + id_departemen);
//                    Log.d(LOG_TAG, "id_wilayah:" + id_wilayah);
//                    db.addKegiatan(new Kegiatan(Integer.parseInt(id_kegiatan),nama_kegiatan,Integer.parseInt(id_departemen),Integer.parseInt(id_wilayah)));
//                }
//            } catch (JSONException e) {
//                final String message = e.toString();
//                handler.post(new Runnable() {
//                    public void run() {
//                        showCustomDialog(message);
//                    }
//                });
//
//            }
//        }
//    }

//    public void extractDataProduct() {
//        SharedPreferences spPreferences = getSharedPrefereces();
//        String main_app_table_same_data = spPreferences.getString(
//                AppVar.SHARED_PREFERENCES_TABLE_PRODUCT_SAME_DATA, null);
//        String main_app_table = spPreferences.getString(
//                AppVar.SHARED_PREFERENCES_TABLE_PRODUCT, null);
//        if (main_app_table_same_data.equalsIgnoreCase(act
//                .getApplicationContext().getResources()
//                .getString(R.string.app_value_false))) {
//            JSONObject oResponse;
//            try {
//                oResponse = new JSONObject(main_app_table);
//                JSONArray jsonarr = oResponse.getJSONArray("product");
//                for (int i = 0; i < jsonarr.length(); i++) {
//                    JSONObject oResponsealue = jsonarr.getJSONObject(i);
//                    String id_product= oResponsealue.isNull("id_product") ? null
//                            : oResponsealue.getString("id_product");
//                    String nama_product= oResponsealue.isNull("nama_product") ? null
//                            : oResponsealue.getString("nama_product");
//                    Log.d(LOG_TAG, "id_product:" + id_product);
//                    Log.d(LOG_TAG, "nama_product:" + nama_product);
//                    db.addProduct(new Product(Integer.parseInt(id_product),nama_product));
//                }
//            } catch (JSONException e) {
//                final String message = e.toString();
//                handler.post(new Runnable() {
//                    public void run() {
//                        showCustomDialog(message);
//                    }
//                });
//
//            }
//        }
//    }

//    public void extractDataJenisKendaraan() {
//        SharedPreferences spPreferences = getSharedPrefereces();
//        String main_app_table_same_data = spPreferences.getString(
//                AppVar.SHARED_PREFERENCES_TABLE_JENIS_KENDARAAN_SAME_DATA, null);
//        String main_app_table = spPreferences.getString(
//                AppVar.SHARED_PREFERENCES_TABLE_JENIS_KENDARAAN, null);
//        if (main_app_table_same_data.equalsIgnoreCase(act
//                .getApplicationContext().getResources()
//                .getString(R.string.app_value_false))) {
//            JSONObject oResponse;
//            try {
//                oResponse = new JSONObject(main_app_table);
//                JSONArray jsonarr = oResponse.getJSONArray("jenis_kendaraan");
//                for (int i = 0; i < jsonarr.length(); i++) {
//                    JSONObject oResponsealue = jsonarr.getJSONObject(i);
//                    String id_jenis_kendaraan = oResponsealue.isNull("id_jenis_kendaraan") ? null
//                            : oResponsealue.getString("id_jenis_kendaraan");
//                    String nama_jenis = oResponsealue.isNull("nama_jenis") ? null
//                            : oResponsealue.getString("nama_jenis");
//                    Log.d(LOG_TAG, "id_jenis_kendaraan:" + id_jenis_kendaraan);
//                    Log.d(LOG_TAG, "nama_jenis:" + nama_jenis);
//                    db.addJenisKendaraan(new Jenis_kendaraan(Integer.parseInt(id_jenis_kendaraan),nama_jenis));
//                }
//            } catch (JSONException e) {
//                final String message = e.toString();
//                handler.post(new Runnable() {
//                    public void run() {
//                        showCustomDialog(message);
//                    }
//                });
//            }
//        }
//    }

//    public void saveAppDataBranch(String responsedata) {
//        SharedPreferences sp = getSharedPrefereces();
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString(AppVar.SHARED_PREFERENCES_TABLE_MST_CUSTOMER, responsedata);
//        editor.commit();
//    }

    public void saveAppDataUser(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_TABLE_MST_USER, responsedata);
        editor.commit();
    }

//    public void saveAppDataKegiatan(String responsedata) {
//        SharedPreferences sp = getSharedPrefereces();
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString(AppVar.SHARED_PREFERENCES_TABLE_MST_KEGIATAN, responsedata);
//        editor.commit();
//    }
//    public void saveAppDataProduct(String responsedata) {
//        SharedPreferences sp = getSharedPrefereces();
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString(AppVar.SHARED_PREFERENCES_TABLE_PRODUCT, responsedata);
//        editor.commit();
//    }

//    public void saveAppDataJenisKendaraan(String responsedata) {
//        SharedPreferences sp = getSharedPrefereces();
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString(AppVar.SHARED_PREFERENCES_TABLE_JENIS_KENDARAAN, responsedata);
//        editor.commit();
//    }

//    public void saveAppDataBranchSameData(String responsedata) {
//        SharedPreferences sp = getSharedPrefereces();
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString(AppVar.SHARED_PREFERENCES_TABLE_MST_CUSTOMER_SAME_DATA,
//                responsedata);
//        editor.commit();
//    }

    public void saveAppDataUserSameData(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_TABLE_MST_USER_SAME_DATA,
                responsedata);
        editor.commit();
    }

//    public void saveAppDataKegiatanSameData(String responsedata) {
//        SharedPreferences sp = getSharedPrefereces();
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString(AppVar.SHARED_PREFERENCES_TABLE_MST_KEGIATAN_SAME_DATA,
//                responsedata);
//        editor.commit();
//    }
//    public void saveAppDataProductSameData(String responsedata) {
//        SharedPreferences sp = getSharedPrefereces();
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString(AppVar.SHARED_PREFERENCES_TABLE_PRODUCT_SAME_DATA,
//                responsedata);
//        editor.commit();
//    }
//    public void saveAppDataJenisKendaraanSameData(String responsedata) {
//        SharedPreferences sp = getSharedPrefereces();
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString(AppVar.SHARED_PREFERENCES_TABLE_JENIS_KENDARAAN_SAME_DATA,
//                responsedata);
//        editor.commit();
//    }

    private SharedPreferences getSharedPrefereces() {
        return act.getSharedPreferences(AppVar.SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
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
}
