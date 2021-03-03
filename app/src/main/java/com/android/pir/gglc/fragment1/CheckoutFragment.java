package com.android.pir.gglc.fragment1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pir.gglc.absen.AppVar;
import com.android.pir.gglc.database.DatabaseHandler;
import com.android.pir.gglc.database.DetailRencana;
import com.android.pir.gglc.database.MstUser;
import com.android.pir.gglc.database.Mst_Customer;
import com.android.pir.gglc.database.Trx_Checkin;
import com.android.pir.gglc.database.Trx_Checkout;
import com.android.pir.gglc.database.UploadDataSapi;
import com.android.pir.gglc.pir.DashboardActivity;
import com.android.pir.mobile.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.android.pir.gglc.absen.AppVar.SHARED_PREFERENCES_NAME;

//import com.android.canvasing.gglc.canvassing.SalesKanvasActivity;


//public class OneFragment1 extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
public class CheckoutFragment extends Fragment{
    Context thiscontext;
    private DatabaseHandler databaseHandler;
    private Context act;
    private ProgressDialog pDialog;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog;
    private String message;
    protected LocationManager locationManager;
    private int idrencanaDetail = 0;

    private Button checkin;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private Uri fileUri1; // file url to store image/video
    private File mediaFile;
    private String newImageName1;
    private String response_data,response_data_download,main_app_id_detail_jadwal;
    private TextView tvnama_petani,tvalamat_petani,tvstatus_checkin;
    private ImageView imgCust;
    private EditText keterangan;
    private Mst_Customer mst_customer;
    private double latitude, longitude;
    private Location location;
    private Location location1;
    private Location location2;
    private String IMAGE_DIRECTORY_NAME = "Checkin_pir";
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    private static final String LOG_TAG = CheckoutFragment.class.getSimpleName();
    private int id_checkin,id_user,id_customer;
    private MstUser user;
    private DetailRencana rencanaDetail;
    private Trx_Checkin trxCheckin;
    private String idrd,idcst,fto,nmr,lts,lng,idus,tgl,kode_customer,status_checkin;
    private LinearLayout lsstatus;
    public CheckoutFragment() {
        //no coding in here
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pir_fragment_checkout, container, false);
        act=this.getActivity();

        progressDialog = new ProgressDialog(this.getActivity());
        progressDialog.setTitle(getActivity().getApplicationContext().getResources()
                .getString(R.string.app_name));
        progressDialog.setMessage("Downloading yaa...");
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);

        databaseHandler = new DatabaseHandler(this.getActivity());
        tvnama_petani = (TextView) view.findViewById(R.id.nama_petani);
        tvalamat_petani = (TextView) view.findViewById(R.id.alamat_pertani);
        tvstatus_checkin = (TextView) view.findViewById(R.id.status_checkin);
        keterangan = (EditText) view.findViewById(R.id.edketerangan);
        lsstatus = (LinearLayout) view.findViewById(R.id.lstatus_checkin);
        checkin = (Button) view.findViewById(R.id.checkin);
        imgCust = (ImageView) view.findViewById(R.id.ImgCust);
        SharedPreferences spPreferences = getSharedPrefereces();
        idrencanaDetail = Integer.parseInt(spPreferences.getString(AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_JADWAL, null));
//        status_checkin = spPreferences.getString(AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_STATUS, null);

        ArrayList<DetailRencana> rencana_list = databaseHandler.getAlldetailRencanaParam(idrencanaDetail);
        rencanaDetail = new DetailRencana();
        for (DetailRencana detailRencana : rencana_list)
            rencanaDetail = detailRencana;
        id_customer=rencanaDetail.getId_customer();
        status_checkin = String.valueOf(rencanaDetail.getStatus_rencana());

        if(status_checkin.equals("0")){
            tvstatus_checkin.setText("Baru (Belum Checkin)");
            lsstatus.setBackgroundColor(Color.parseColor("#ed190e"));
        }else if (status_checkin.equals("1")){
            tvstatus_checkin.setText("Sudah Checkin");
            lsstatus.setBackgroundColor(Color.parseColor("#24ed0e"));
        }

        ArrayList<Mst_Customer> customer_list = databaseHandler.getAllCustomerParamRencana(idrencanaDetail);
        mst_customer = new Mst_Customer();
        for (Mst_Customer customer : customer_list)
            mst_customer = customer;
        tvnama_petani.setText(mst_customer.getNama_customer());
        tvalamat_petani.setText(mst_customer.getAlamat());

        if(databaseHandler.getCountDetailrencana()!=0){
            ArrayList<MstUser> user_list = databaseHandler.getAllUser();
            user = new MstUser();
            for (MstUser mstUser : user_list)
                user = mstUser;
            id_user=user.getId_user();
            ArrayList<Trx_Checkin> checkin_list = databaseHandler.getAllCheckinIDParam(idrencanaDetail);
            trxCheckin = new Trx_Checkin();
            for (Trx_Checkin mstUser : checkin_list)
                trxCheckin = mstUser;
            id_checkin=trxCheckin.getId_checkin();
        }else{
            showCustomDialog("Download dencana detail terlebih dahulu");
        }
        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAppKeterangan(keterangan.getText().toString());
                checkinSave();
            }
        });
        return  view;
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
    public void showCustomDialogRS(String msg) {
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
                                resetCheckin();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void checkinSave() {
        locationManager = (LocationManager) getActivity().getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000L, 1.0f, locationListener);

        boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

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
                    if (idrencanaDetail != 0 ) {
                                SharedPreferences sp = getSharedPrefereces();
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString(AppVar.SHARED_PREFERENCES_LATS,vlats);
                                editor.putString(AppVar.SHARED_PREFERENCES_LONGS,vlongs);
                                editor.commit();
                                new UploadData().execute();
                    } else {
                        String msg = "ID Rencana Kosong, silahkan lakukan refresh pada menu visit";
                        showCustomDialog(msg);
                    }

                }
                else{
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            1000L, 1.0f, locationListener);
                    location1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    if(location1 != null){
                        latitude=location1.getLatitude();
                        longitude=location1.getLongitude();
                        final String vlats=String.valueOf(latitude);
                        final String vlongs=String.valueOf(longitude);

                        //disini
                        if (idrencanaDetail != 0) {
                            SharedPreferences sp = getSharedPrefereces();
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString(AppVar.SHARED_PREFERENCES_LATS,vlats);
                            editor.putString(AppVar.SHARED_PREFERENCES_LONGS,vlongs);
                            editor.commit();
                            new UploadData().execute();
                        } else {
                            String msg = "ID Rencana Kosong, silahkan lakukan refresh pada menu visit";
                            showCustomDialog(msg);
                        }

                    }else{
                        showCustomDialog("Mohon tunggu, Aplikasi sedang membaca lokasi absen");
                    }
                }
            }else{
                showCustomDialog("Mohon tunggu, Aplikasi sedang membaca lokasi absen");
            }
        }
    }

    public class UploadData extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            progressDialog
                    .setMessage(getActivity().getApplicationContext()
                            .getResources()
                            .getString(
                                    R.string.app_supplier_processing));
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            String msg = getActivity().getApplicationContext()
                                    .getResources()
                                    .getString(
                                            R.string.MSG_DLG_LABEL_SYNRONISASI_DATA_CANCEL);
                            showCustomDialog(msg);
                        }
                    });
        }

        @Override
        protected String doInBackground(String... params) {
//            String url = AppVar.CONFIG_APP_URL_PUBLIC;
//            String uploadSupplier = AppVar.CONFIG_APP_URL_UPLOAD_INSERT_CHECKOUT;
//            String upload_image_supplier_url = url + uploadSupplier;
            SharedPreferences spPreferences = getSharedPrefereces();
            String lats = spPreferences.getString(AppVar.SHARED_PREFERENCES_LATS, null);
            String longs = spPreferences.getString(AppVar.SHARED_PREFERENCES_LONGS, null);
            String data_keterangan = spPreferences.getString(AppVar.SHARED_PREFERENCES_KETERANGAN_CHECKOUT, null);
            /***********************
             * Upload Image Supplier
             */
//            response_data = uploadImage(upload_image_supplier_url,String.valueOf(id_user),String.valueOf(idrencanaDetail),lats,longs,String.valueOf(id_checkin),data_keterangan);
            SaveuploadImage(String.valueOf(id_user),String.valueOf(idrencanaDetail),lats,longs,data_keterangan);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            int cekCKO = databaseHandler.getCountTrxcheckout(idrencanaDetail);
            if(cekCKO > 0){
                showCustomDialogRS("Checkout Berhasil.");
                databaseHandler.updateCheckout(String.valueOf(idrencanaDetail));
            }else{
                showCustomDialog("Checkout Gagal.");
            }
//            super.onPostExecute(result);
//            Log.d(LOG_TAG, "response:" + response_data);
//            if (response_data != null && response_data.length() > 0) {
//                if (response_data.startsWith("Error occurred")) {
//                    final String msg = act
//                            .getApplicationContext()
//                            .getResources()
//                            .getString(
//                                    R.string.app_supplier_processing_failed);
//                    handler.post(new Runnable() {
//                        public void run() {
//                            showCustomDialog(msg);
//                        }
//                    });
//                } else {
//                    handler.post(new Runnable() {
//                        public void run() {
//                            extractUpload();
//                        }
//                    });
//                }
//            } else {
//                final String msg = act
//                        .getApplicationContext()
//                        .getResources()
//                        .getString(
//                                R.string.app_supplier_processing_failed);
//                handler.post(new Runnable() {
//                    public void run() {
//                        showCustomDialog(msg);
//                    }
//                });
//            }
        }
    }

    protected String uploadImage(final String url,  final String id_user,final String id_rencana_detail, final String lats,
                                 final String longs, final String id_checkin, final String keterangan_) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        String responseString = null;
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            MultipartEntity entity = new MultipartEntity();
            entity.addPart("id_user", new StringBody(id_user));
            entity.addPart("id_rencana_detail", new StringBody(id_rencana_detail));
            entity.addPart("lats", new StringBody(lats));
            entity.addPart("longs", new StringBody(longs));
            entity.addPart("id_checkin", new StringBody(id_checkin));
            entity.addPart("keterangan", new StringBody(keterangan_));
            httppost.setEntity(entity);
            // Making server call
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Server response
                responseString = EntityUtils.toString(r_entity);
            } else {
                responseString = "Error occurred! Http Status Code: "
                        + statusCode;
            }
        } catch (ClientProtocolException e) {
            responseString = e.toString();
        } catch (IOException e) {
            responseString = e.toString();
        }
        return responseString;
    }

    private void SaveuploadImage(final String id_user,final String id_rencana_detail, final String lats,
                                 final String longs, final String keterangan_) {
        final String date = "yyyy-MM-dd HH:mm:ss";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(date);
        final String checkDate = dateFormat.format(calendar.getTime());
        Trx_Checkout trx_checkout = new Trx_Checkout();

        trx_checkout.setId_rencana_detail(Integer.parseInt(id_rencana_detail));
        trx_checkout.setTanggal_checkout(checkDate);
        trx_checkout.setId_user(Integer.parseInt(id_user));
        trx_checkout.setRealisasi_kegiatan(keterangan_);
        trx_checkout.setLats(lats);
        trx_checkout.setLongs(longs);
        databaseHandler.addCheckout(trx_checkout);
    }

    public void extractUpload() {
        JSONObject oResponse;
        try {
            oResponse = new JSONObject(response_data);
            String status = oResponse.isNull("error") ? "True" : oResponse
                    .getString("error");
            if (response_data.isEmpty()) {
                final String msg = act
                        .getApplicationContext()
                        .getResources()
                        .getString(
                                R.string.app_supplier_processing_failed);
                showCustomDialog(msg);
            } else {
                Log.d(LOG_TAG, "status=" + status);
                if (status.equalsIgnoreCase("False")) {
                    final String msg = act
                            .getApplicationContext()
                            .getResources()
                            .getString(
                                    R.string.app_supplier_processing_sukses);
                    showCustomDialog(msg);
                    databaseHandler.updateCheckout(String.valueOf(idrencanaDetail));
                    resetCheckin();
                } else {
                    final String msg = act
                            .getApplicationContext()
                            .getResources()
                            .getString(
                                    R.string.app_supplier_processing_failed);
                    showCustomDialog(msg);
                }
            }
        } catch (JSONException e) {
            final String message = e.toString();
            showCustomDialog(message);
        }
    }

    public void saveAppKeterangan(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_KETERANGAN_CHECKOUT,
                responsedata);
        editor.commit();
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

    public void resetCheckin(){
        Intent intentActivity = new Intent(this.getActivity(),
                DashboardActivity.class);
        startActivity(intentActivity);
        getActivity().finish();
    }

    private SharedPreferences getSharedPrefereces() {
        return act.getSharedPreferences(AppVar.SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
    }
}
