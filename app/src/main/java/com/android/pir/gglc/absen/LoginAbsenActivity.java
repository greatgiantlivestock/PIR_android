package com.android.pir.gglc.absen;

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
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
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


import com.android.pir.gglc.database.Checkpoint_absen;
import com.android.pir.gglc.database.DatabaseHandler;
import com.android.pir.gglc.database.User;
import com.android.pir.mobile.R;
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

public class LoginAbsenActivity extends AppCompatActivity implements LocationAssistantLogin.Listener {
    private static final String LOG_TAG = LoginAbsenActivity.class.getSimpleName();
    private EditText txt_username;
    private EditText txt_password;
    private Button btn_login;
    private Context context;
    private ProgressDialog pDialog;
    private Context act;
    private Toolbar mToolbar;
    private DatabaseHandler database;
    private Handler handler = new Handler();

    private ProgressDialog progressDialog;
    protected LocationManager locationManager;
    private ImageButton btn_img;
    private TextView txt_tanggal, txt_jam;
    private double latitude, longitude;
    private Location location;
    private Location location1;
    private Location location2;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    private String message;
    private String response_data;
    private DatabaseHandler databaseHandler;

    //private TextView tvLocation;
    private LocationAssistantLogin assistant;

    //LoginDataBaseAdapter loginDataBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_absen);

        assistant = new LocationAssistantLogin(this, this, LocationAssistantLogin.Accuracy.HIGH, 5000, false);
        assistant.setVerbose(true);
        context = LoginAbsenActivity.this;
        //startMonitoring();
        //checkGPS();
        databaseHandler = new DatabaseHandler(this);
        act=this;
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
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getApplicationContext().getResources()
                .getString(R.string.app_name));
        progressDialog.setMessage(getApplicationContext().getResources()
                .getString(R.string.app_login_processing));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);

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
                    Toast.makeText(LoginAbsenActivity.this, "Isi kolom username dan password terlebih dahulu",Toast.LENGTH_LONG).show();
                }else if(txt_username.getText().length()==0){
                    Toast.makeText(LoginAbsenActivity.this, "Kolom username kosong",Toast.LENGTH_LONG).show();
                }else if(txt_password.getText().length()==0){
                Toast.makeText(LoginAbsenActivity.this, "Kolom password kosong",Toast.LENGTH_LONG).show();
                }else{
                    login();
                }
            }
        });

        //download checkpoint jika dibutuhkan looping
        //if(databaseHandler.getCountCheckpoint()==0){
        ///    new DownloadDataCheckpoint().execute();
        //}
    }

    @Override
    protected void onResume() {
        super.onResume();
        assistant.start();
    }

//    @Override
//    protected void onPause() {
//        assistant.stop();
//        super.onPause();
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        assistant.onActivityResult(requestCode, resultCode);
    }

    @Override
    public void onNeedLocationPermission() {
        assistant.requestAndPossiblyExplainLocationPermission();
    }

    @Override
    public void onExplainLocationPermission() {
        new android.app.AlertDialog.Builder(this)
                .setMessage(R.string.permissionExplanation)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        assistant.requestLocationPermission();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //tvLocation.setOnClickListener(new View.OnClickListener() {
                        //    @Override
                        //    public void onClick(View v) {
                        //        assistant.requestLocationPermission();
                        //    }
                        //});
                    }
                })
                .show();
    }

    @Override
    public void onLocationPermissionPermanentlyDeclined(View.OnClickListener fromView,
                                                        DialogInterface.OnClickListener fromDialog) {
        new android.app.AlertDialog.Builder(this)
                .setMessage(R.string.permissionPermanentlyDeclined)
                .setPositiveButton(R.string.ok, fromDialog)
                .show();
    }

    @Override
    public void onNeedLocationSettingsChange() {
        new android.app.AlertDialog.Builder(this)
                .setMessage(R.string.switchOnLocationShort)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        assistant.changeLocationSettings();
                    }
                })
                .show();
    }

    @Override
    public void onFallBackToSystemSettings(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {
        new android.app.AlertDialog.Builder(this)
                .setMessage(R.string.switchOnLocationLong)
                .setPositiveButton(R.string.ok, fromDialog)
                .show();
    }

    @Override
    public void onNewLocationAvailable(Location location) {
        if (location == null) return;
        //tvLocation.setOnClickListener(null);
        //tvLocation.setText(location.getLongitude() + "\n" + location.getLatitude());
        //tvLocation.setAlpha(1.0f);
        //tvLocation.animate().alpha(0.5f).setDuration(400);
    }

    @Override
    public void onError(LocationAssistantLogin.ErrorType type, String message) {
        //tvLocation.setText(getString(R.string.error));
    }

    private class DownloadDataCheckpoint extends AsyncTask<String, Integer, String> {
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
                String download_data_url = AppVar.CONFIG_APP_URL_PUBLIC
                        + AppVar.CONFIG_APP_URL_DOWNLOAD_CHECKPOINT;
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
                                AppVar.SHARED_PREFERENCES_TABLE_CHECKPOINT, null);
                        if (main_app_table_data != null) {
                            if (main_app_table_data.equalsIgnoreCase(response_data)) {
                                saveAppDataBranchSameData(act
                                        .getApplicationContext().getResources()
                                        .getString(R.string.app_value_true));
                            } else {
                                databaseHandler.deleteTableCheckpoint();
                                saveAppDataBranchSameData(act
                                        .getApplicationContext().getResources()
                                        .getString(R.string.app_value_false));
                            }
                        } else {
                            databaseHandler.deleteTableCheckpoint();
                            saveAppDataBranchSameData(act.getApplicationContext()
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
                saveAppDataBranch(response_data);
                extractDataBranch();
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
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

    public void saveAppDataBranch(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_TABLE_CHECKPOINT, responsedata);
        editor.commit();
    }

    public void extractDataBranch() {
        SharedPreferences spPreferences = getSharedPrefereces();
        String main_app_table_same_data = spPreferences.getString(
                AppVar.SHARED_PREFERENCES_TABLE_CHECKPOINT_SAME_DATA, null);
        String main_app_table = spPreferences.getString(
                AppVar.SHARED_PREFERENCES_TABLE_CHECKPOINT, null);
        if (main_app_table_same_data.equalsIgnoreCase(act
                .getApplicationContext().getResources()
                .getString(R.string.app_value_false))) {
            JSONObject oResponse;
            try {
                oResponse = new JSONObject(main_app_table);
                JSONArray jsonarr = oResponse.getJSONArray("checkpoint_absen");
                for (int i = 0; i < jsonarr.length(); i++) {
                    JSONObject oResponsealue = jsonarr.getJSONObject(i);
                    String id_branch = oResponsealue.isNull("nama_checkpoint_absen") ? null
                            : oResponsealue.getString("nama_checkpoint_absen");
                    String kode_branch = oResponsealue.isNull("lats") ? null
                            : oResponsealue.getString("lats");
                    String deskripsi = oResponsealue.isNull("longs") ? null
                            : oResponsealue.getString("longs");
                    Log.d(LOG_TAG, "nama_checkpoint_absen:" + id_branch);
                    Log.d(LOG_TAG, "lats:" + kode_branch);
                    Log.d(LOG_TAG, "longs:" + deskripsi);
                    databaseHandler.addCheckpoint_absen(new Checkpoint_absen(id_branch, kode_branch, deskripsi));
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

    public void saveAppDataBranchSameData(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_TABLE_CHECKPOINT_SAME_DATA,
                responsedata);
        editor.commit();
    }

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

                            }else if (rsp.equals("successx")){
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

                            }else if (rsp.equals("successx")){
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
