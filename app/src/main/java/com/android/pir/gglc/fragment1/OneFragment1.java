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
import android.graphics.Matrix;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pir.gglc.absen.AppVar;
import com.android.pir.gglc.absen.RequestHandler;
import com.android.pir.gglc.database.DatabaseHandler;
import com.android.pir.gglc.database.DetailRencana;
import com.android.pir.gglc.database.MasterRencana;
import com.android.pir.gglc.database.MstUser;
import com.android.pir.gglc.database.Mst_Customer;
import com.android.pir.gglc.database.Trx_Checkin;
import com.android.pir.gglc.fragment.OneFragment;
import com.android.pir.gglc.pir.CheckinOneFragmentActivity;
import com.android.pir.gglc.pir.DashboardActivity;
import com.android.pir.gglc.pir.DetailJadwalActivity;
import com.android.pir.gglc.pir.IconTextTabsActivity;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.android.pir.gglc.absen.AppVar.SHARED_PREFERENCES_NAME;

//import com.android.canvasing.gglc.canvassing.SalesKanvasActivity;


//public class OneFragment1 extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
public class OneFragment1 extends Fragment{
    Context thiscontext;
    private DatabaseHandler databaseHandler;
    private Context act;
    private ProgressDialog pDialog;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog;
    private String message;
    protected LocationManager locationManager;
    private int idrencanaDetail = 0;

    private Button foto,checkin;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private Uri fileUri1; // file url to store image/video
    private File mediaFile;
    private String newImageName1;
    private String response_data,response_data_download,main_app_id_detail_jadwal;
    private TextView tvFotoCustomer,tvnama_petani,tvalamat_petani,tvstatus_checkin;
    private ImageView imgCust;
//    private EditText no_checkin1;
    private Mst_Customer mst_customer;
    private double latitude, longitude;
    private Location location;
    private Location location1;
    private Location location2;
    private String IMAGE_DIRECTORY_NAME = "Checkin_pir";
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    private static final String LOG_TAG = OneFragment1.class.getSimpleName();
    private int id_checkin,id_user,id_customer,rotateV=90;
    private MstUser user;
    private DetailRencana rencanaDetail;
    private String idrd,idcst,fto,nmr,lts,lng,idus,tgl,kode_customer;
    private LinearLayout lsstatus;
    public OneFragment1() {
        //no coding in here
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pir_fragment_one, container, false);
        act=this.getActivity();


        progressDialog = new ProgressDialog(this.getActivity());
        progressDialog.setTitle(getActivity().getApplicationContext().getResources()
                .getString(R.string.app_name));
        progressDialog.setMessage("Downloading yaa...");
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);

        databaseHandler = new DatabaseHandler(this.getActivity());
        tvFotoCustomer = (TextView) view.findViewById(R.id.title_foto_customer);
        tvnama_petani = (TextView) view.findViewById(R.id.nama_petani);
        tvalamat_petani = (TextView) view.findViewById(R.id.alamat_pertani);
        tvstatus_checkin = (TextView) view.findViewById(R.id.status_checkin);
        lsstatus = (LinearLayout) view.findViewById(R.id.lstatus_checkin);
        foto = (Button) view.findViewById(R.id.foto);
        checkin = (Button) view.findViewById(R.id.checkin);
        imgCust = (ImageView) view.findViewById(R.id.ImgCust);
//        no_checkin1 = (EditText) view.findViewById(R.id.no_checkin);
//        no_checkin1.setEnabled(false);
        SharedPreferences spPreferences = getSharedPrefereces();
        idrencanaDetail = Integer.parseInt(spPreferences.getString(AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_JADWAL, null));
//        status_checkin = spPreferences.getString(AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_STATUS, null);
        ArrayList<DetailRencana> rencana_list = databaseHandler.getAlldetailRencanaParam(idrencanaDetail);
        rencanaDetail = new DetailRencana();
        for (DetailRencana detailRencana : rencana_list)
            rencanaDetail = detailRencana;
        int id_status =rencanaDetail.getStatus_rencana();

        if(id_status==0){
            tvstatus_checkin.setText("Baru (Belum Checkin)");
            lsstatus.setBackgroundColor(Color.parseColor("#ed190e"));
        }else if (id_status==1){
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
        }else{
            showCustomDialog("Download dencana detail terlebih dahulu");
        }

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    gotoCaptureImage1();
            }
        });
        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkinSave();
            }
        });
        return  view;
    }

//    public void generateNomercheckpoint(){
//        mst_customer = databaseHandler.getCustomer(id_customer);
//        String timeStamp = new SimpleDateFormat("HHmmss",
//                Locale.getDefault()).format(new Date());
//        final String date = "yyyyMMdd";
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat dateFormat = new SimpleDateFormat(date);
//        final String checkDate = dateFormat.format(calendar.getTime());
//        String kode_customer=mst_customer.getKode_customer();
//        String nomor_chekin=kode_customer+"."+checkDate+"."+timeStamp;
//        no_checkin1.setText(nomor_chekin);
//    }

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
    public void showCustomDialogCK(String msg) {
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
                                tvstatus_checkin.setText("Sudah Checkin");
                                lsstatus.setBackgroundColor(Color.parseColor("#24ed0e"));
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

//    public void showCustomDialogCheckinSuccess(String msg) {
//        if (progressDialog != null) {
//            progressDialog.dismiss();
//        }
//        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                act);
//        alertDialogBuilder
//                .setMessage(msg)
//                .setCancelable(false)
//                .setPositiveButton(
//                        act.getApplicationContext().getResources()
//                                .getString(R.string.MSG_DLG_LABEL_OK),
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int id) {
//                                AlertDialog alertDialog = alertDialogBuilder.create();
//                                //idrenca naDetail = rencanaDetailList.get(0).getId_rencana_detail();
//                                //id_customer = customerList.get(0).getId_customer();
//                                alertDialog.dismiss();
//
//                            }
//                        });
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//
//    }

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
//                                int countId_checkin = databaseHandler.getCountTrxcheckin();
//                                final String date = "yyyy-MM-dd";
//                                Calendar calendar = Calendar.getInstance();
//                                SimpleDateFormat dateFormat = new SimpleDateFormat(date);
//                                final String checkDate = dateFormat.format(calendar.getTime());
//                                String kode_customer=mst_customer.getKode_customer();
//                                id_checkin=countId_checkin+1;
//
//                                int already_checkin = databaseHandler.getCheckinAlready(kode_customer, idrencanaDetail);
//
//                                if(already_checkin==1){
//                                    ArrayList<Mst_Customer> customer_list = databaseHandler.getAllCustomerParam(kode_customer);
//                                    Mst_Customer customer_param = new Mst_Customer();
//                                    for (Mst_Customer customer : customer_list)
//                                        customer_param = customer;
//
//                                    String nama = customer_param.getNama_customer();
//
//                                    showCustomDialog("Anda sudah checkin pada customer atas nama "+nama+" di hari yang sama");
//                                }else{
//                                    Trx_Checkin checkin = new Trx_Checkin();
//                                    //checkin.setId_checkin(id_checkin);
//                                    checkin.setTanggal_checkin(checkDate);
//                                    checkin.setNomor_checkin(no_checkin1.getText().toString());
//                                    checkin.setId_user(id_user);
//                                    checkin.setId_rencana_detail(idrencanaDetail);
////                                    checkin.setId_rencana_header(id_rencanaHeader);
//                                    checkin.setKode_customer(kode_customer);
//                                    checkin.setLats(vlats);
//                                    checkin.setLongs(vlongs);
//                                    checkin.setFoto(tvFotoCustomer.getText().toString());
//                                    checkin.setStatus("1");
//
//                                    databaseHandler.addCheckin(checkin);
//
//                                    final String tanggal_checkin_checkin = checkDate;
//                                    final String nomor_checkin_checkin = no_checkin1.getText().toString();
//                                    final String id_user_checkin_checkin = String.valueOf(id_user);
//                                    final String id_rencana_detail_checkin = String.valueOf(idrencanaDetail);
////                                    final String id_rencana_master_checkin = String.valueOf(id_rencanaHeader);
//                                    final String kode_customer_checkin = kode_customer;
//                                    final String lats_checkin = vlats;
//                                    final String longs_checkin = vlongs;
//                                    final String foto_checkin = tvFotoCustomer.getText().toString();
//
//                                    class insertToDatabase extends AsyncTask<Void, Void, String> {
//                                        ProgressDialog loading;
//
//                                        @Override
//                                        protected void onPreExecute() {
//                                            super.onPreExecute();
//                                            loading = ProgressDialog.show(OneFragment1.this.getActivity(),"Proses...","Silahkan Tunggu...",false,false);
//                                        }
//
//                                        @Override
//                                        protected void onPostExecute(String s) {
//                                            super.onPostExecute(s);
//                                            loading.dismiss();
//                                            Toast.makeText(OneFragment1.this.getActivity(),"Checkin Berhasil !",Toast.LENGTH_LONG).show();
//                                            //resetCheckin();
//                                            databaseHandler.updateCheckin(id_rencana_detail_checkin);
//                                            gotoDashboard();
//                                            Log.d(LOG_TAG, "sampe sini");
//                                        }
//
//                                        @Override
//                                        protected String doInBackground(Void... params) {
//                                            HashMap<String,String> hashMap  = new HashMap<>();
//                                            hashMap.put("tanggal_checkin",tanggal_checkin_checkin);
//                                            hashMap.put("nomor_checkin",nomor_checkin_checkin);
//                                            hashMap.put("id_user",id_user_checkin_checkin);
//                                            hashMap.put("id_rencana_detail",id_rencana_detail_checkin);
////                                            hashMap.put("id_rencana_header",id_rencana_master_checkin);
//                                            hashMap.put("kode_customer",kode_customer_checkin);
//                                            hashMap.put("lats",lats_checkin);
//                                            hashMap.put("longs",longs_checkin);
//                                            hashMap.put("foto",foto_checkin);
//
//                                            RequestHandler rh = new RequestHandler();
//                                            String res = rh.sendPostRequest(AppVar.POST_NEW_CHECKIN, hashMap);
//                                            return res;
//                                        }
//                                    }
//                                    insertToDatabase in = new insertToDatabase();
//                                    in.execute();
//                                }
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
//                            if (vlats.equalsIgnoreCase("0")
//                                    || vlongs.equalsIgnoreCase("0")) {
//                                String msg = getActivity().getApplicationContext()
//                                        .getResources()
//                                        .getString(
//                                                R.string.MSG_DLG_LABEL_FAILED_CURRENT_GPS_DIALOG);
//                                showCustomDialog(msg);
//                            } else {
//                                if (tvFotoCustomer.getText().length()!=0) {
//                                    int countId_checkin = databaseHandler.getCountTrxcheckin();
//                                    final String date = "yyyy-MM-dd";
//                                    Calendar calendar = Calendar.getInstance();
//                                    SimpleDateFormat dateFormat = new SimpleDateFormat(date);
//                                    final String checkDate = dateFormat.format(calendar.getTime());
//                                    String kode_customer=mst_customer.getKode_customer();
//
//                                    id_checkin=countId_checkin+1;
//
//
//                                    Trx_Checkin checkin = new Trx_Checkin();
//                                    //checkin.setId_checkin(id_checkin);
//                                    checkin.setTanggal_checkin(checkDate);
//                                    checkin.setNomor_checkin(no_checkin1.getText().toString());
//                                    checkin.setId_user(id_user);
//                                    checkin.setId_rencana_detail(idrencanaDetail);
////                                    checkin.setId_rencana_header(id_rencanaHeader);
//                                    checkin.setKode_customer(kode_customer);
//                                    checkin.setLats(vlats);
//                                    checkin.setLongs(vlongs);
//                                    checkin.setFoto(tvFotoCustomer.getText().toString());
//                                    checkin.setStatus("1");
//
//                                    databaseHandler.addCheckin(checkin);
//
//                                    final String tanggal_checkin_checkin = checkDate;
//                                    final String nomor_checkin_checkin = no_checkin1.getText().toString();
//                                    final String id_user_checkin_checkin = String.valueOf(id_user);
//                                    final String id_rencana_detail_checkin = String.valueOf(idrencanaDetail);
////                                    final String id_rencana_master_checkin = String.valueOf(id_rencanaHeader);
//                                    final String kode_customer_checkin = kode_customer;
//                                    final String lats_checkin = vlats;
//                                    final String longs_checkin = vlongs;
//                                    final String foto_checkin = tvFotoCustomer.getText().toString();
//
//                                    class insertToDatabase extends AsyncTask<Void, Void, String> {
//                                        ProgressDialog loading;
//
//                                        @Override
//                                        protected void onPreExecute() {
//                                            super.onPreExecute();
//                                            loading = ProgressDialog.show(OneFragment1.this.getActivity(),"Proses...","Silahkan Tunggu...",false,false);
//                                        }
//
//                                        @Override
//                                        protected void onPostExecute(String s) {
//                                            super.onPostExecute(s);
//                                            loading.dismiss();
//                                            Toast.makeText(OneFragment1.this.getActivity(),"Checkin Berhasil !",Toast.LENGTH_LONG).show();
//                                            resetCheckin();
//                                            //gotoCheckin();
//                                            gotoDashboard();
//                                            Log.d(LOG_TAG, "sampe sini 1");
//                                        }
//
//                                        @Override
//                                        protected String doInBackground(Void... params) {
//                                            HashMap<String,String> hashMap  = new HashMap<>();
//                                            hashMap.put("tanggal_checkin",tanggal_checkin_checkin);
//                                            hashMap.put("nomor_checkin",nomor_checkin_checkin);
//                                            hashMap.put("id_user",id_user_checkin_checkin);
//                                            hashMap.put("id_rencana_detail",id_rencana_detail_checkin);
////                                            hashMap.put("id_rencana_header",id_rencana_master_checkin);
//                                            hashMap.put("kode_customer",kode_customer_checkin);
//                                            hashMap.put("lats",lats_checkin);
//                                            hashMap.put("longs",longs_checkin);
//                                            hashMap.put("foto",foto_checkin);
//
//                                            RequestHandler rh = new RequestHandler();
//                                            String res = rh.sendPostRequest(AppVar.POST_NEW_CHECKIN, hashMap);
//                                            return res;
//                                        }
//                                    }
//                                    insertToDatabase in = new insertToDatabase();
//                                    in.execute();
//                                } else {
//                                    String msg = getActivity().getApplicationContext()
//                                            .getResources()
//                                            .getString(
//                                                    R.string.app_customer_checkin_save_failed_no_image);
//                                    showCustomDialog(msg);
//                                }
//                            }
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
//            String uploadSupplier = AppVar.CONFIG_APP_URL_UPLOAD_INSERT_CHECKIN_NEW;
//            String upload_image_supplier_url = url + uploadSupplier;
            SharedPreferences spPreferences = getSharedPrefereces();
            String lats = spPreferences.getString(AppVar.SHARED_PREFERENCES_LATS, null);
            String longs = spPreferences.getString(AppVar.SHARED_PREFERENCES_LONGS, null);
            String foto = spPreferences.getString(AppVar.SHARED_PREFERENCES_CHILLER_FOTO_1, null);
            /***********************
             * Upload Image Supplier
             */
            if(foto=="") {
//                response_data = uploadImage1(upload_image_supplier_url, String.valueOf(id_user), String.valueOf(idrencanaDetail), lats, longs);
                saveCkNI(String.valueOf(id_user), String.valueOf(idrencanaDetail), lats, longs);
            }else{
//                response_data = uploadImage(upload_image_supplier_url, String.valueOf(id_user), String.valueOf(idrencanaDetail), lats, longs, foto);
                saveCkI(String.valueOf(id_user), String.valueOf(idrencanaDetail), lats, longs,foto);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            int cekCK = databaseHandler.getCountTrxcheckin();
            if(cekCK > 0){
                showCustomDialogCK("Checkin Sukses");
            }else{
                showCustomDialog("Checkin Gagal");
            }
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
//                            new DownloadDataRencanaDetail().execute();
//                            SharedPreferences spPreferences = getSharedPrefereces();
//                            String foto = spPreferences.getString(AppVar.SHARED_PREFERENCES_CHILLER_FOTO_1, null);
//                            Log.d(LOG_TAG, "foto : "+ foto);
//                            if(foto!=null) {
//                                extractUpload();
//                            }
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

    protected String uploadImage(final String url,  final String id_user,
                                 final String id_rencana_detail, final String lats, final String longs, final String foto_1) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        String responseString = null;
        Log.d(LOG_TAG, "Cek Foto : "+foto_1);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            MultipartEntity entity = new MultipartEntity();
            File dir1 = new File(AppVar.getFolderPath() + "/"
                    + IMAGE_DIRECTORY_NAME + "/"
                    + foto_1);
            if (dir1.exists() && foto_1 != null) {
                entity.addPart("image_1", new FileBody(dir1));
                entity.addPart("foto1", new StringBody(foto_1));
            }
            else {
                entity.addPart("foto1", new StringBody(""));
            }
            entity.addPart("id_user", new StringBody(id_user));
            entity.addPart("id_rencana_detail", new StringBody(id_rencana_detail));
            entity.addPart("lats", new StringBody(lats));
            entity.addPart("longs", new StringBody(longs));
            httppost.setEntity(entity);
            Log.d(LOG_TAG, "uploadImage: "+entity);

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

    protected String uploadImage1(final String url,  final String id_user,
                                 final String id_rencana_detail, final String lats, final String longs) {
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
            httppost.setEntity(entity);
            Log.d(LOG_TAG, "uploadImage: "+entity);

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

    public void saveCkNI(final String id_user_,final String id_rencana_detail_, final String lats_, final String longs_) {
        final String date = "yyyy-MM-dd HH:mm:ss";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(date);
        final String checkDate = dateFormat.format(calendar.getTime());

        Trx_Checkin checkin = new Trx_Checkin();
        checkin.setTanggal_checkin(checkDate);
        checkin.setNomor_checkin(idrencanaDetail+checkDate);
        checkin.setId_user(Integer.parseInt(id_user_));
        checkin.setId_rencana_detail(Integer.parseInt(id_rencana_detail_));
        checkin.setKode_customer(kode_customer);
        checkin.setLats(lats_);
        checkin.setLongs(longs_);
        checkin.setFoto(tvFotoCustomer.getText().toString());
        checkin.setStatus("1");
        checkin.setProspect("0");
        databaseHandler.addCheckin(checkin);
        databaseHandler.updateCheckin(id_rencana_detail_);
    }

    public void saveCkI(final String id_user_,final String id_rencana_detail_, final String lats_, final String longs_, String foto_checkin) {
        final String date = "yyyy-MM-dd HH:mm:ss";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(date);
        final String checkDate = dateFormat.format(calendar.getTime());

        Trx_Checkin checkin = new Trx_Checkin();
        checkin.setTanggal_checkin(checkDate);
        checkin.setNomor_checkin(idrencanaDetail+checkDate);
        checkin.setId_user(Integer.parseInt(id_user_));
        checkin.setId_rencana_detail(Integer.parseInt(id_rencana_detail_));
        checkin.setKode_customer(kode_customer);
        checkin.setLats(lats_);
        checkin.setLongs(longs_);
        checkin.setFoto(tvFotoCustomer.getText().toString());
        checkin.setStatus("1");
        checkin.setProspect("0");
        checkin.setFoto(foto_checkin);
        databaseHandler.addCheckin(checkin);
        databaseHandler.updateCheckin(id_rencana_detail_);
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
                    saveAppSupplierFoto1(null);
                    File dir = new File(AppVar.getFolderPath() + "/"
                            + IMAGE_DIRECTORY_NAME);
                    List<File> fileFoto = getListFiles(dir);
                    for (File tempFile : fileFoto) {
                        tempFile.delete();
                    }
                    final String msg = act
                            .getApplicationContext()
                            .getResources()
                            .getString(
                                    R.string.app_supplier_processing_sukses);
                    showCustomDialog(msg);
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

    public void saveAppSupplierFoto1(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_CHILLER_FOTO_1,
                responsedata);
        editor.commit();
    }

    protected List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            inFiles.add(file);
            if (file.isDirectory())
                inFiles.addAll(getListFiles(file));
        }
        return inFiles;
    }


//    public void showCustomDialogDownloadSuccess(String msg) {
//        if (progressDialog != null) {
//            progressDialog.dismiss();
//        }
//        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                act);
//        alertDialogBuilder
//                .setMessage(msg)
//                .setCancelable(false)
//                .setPositiveButton(
//                        act.getApplicationContext().getResources()
//                                .getString(R.string.MSG_DLG_LABEL_OK),
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int id) {
//                                AlertDialog alertDialog = alertDialogBuilder
//                                        .create();
//                                alertDialog.dismiss();
//                            }
//                        });
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//
//    }

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

    public void gotoCaptureImage1() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri1 = getOutputMediaFileUri1(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri1);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void gotoDashboard(){
        Intent intentActivity = new Intent(this.getActivity(),
                DashboardActivity.class);
        startActivity(intentActivity);
        getActivity().finish();
    }

    public void resetCheckin() {
//        spinnerRencanaDetail.setSelection(0);
//        spinnerCustomer.setSelection(0);
        tvFotoCustomer.setText("");
        imgCust.setImageResource(R.drawable.avatar);
        foto.setClickable(false);
        checkin.setClickable(false);
    }

    public Uri getOutputMediaFileUri1(int type) {
        return Uri.fromFile(getOutputMediaFile1(type));
    }

    private SharedPreferences getSharedPrefereces() {
        return act.getSharedPreferences(AppVar.SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
    }

    private File getOutputMediaFile1(int type) {
        File dir = new File(AppVar.getFolderPath() + "/"
                + IMAGE_DIRECTORY_NAME);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        ArrayList<Mst_Customer> staff_list = databaseHandler.getAllCustomerParamRencana(idrencanaDetail);
        mst_customer = new Mst_Customer();

        for (Mst_Customer tempStaff : staff_list)
            mst_customer = tempStaff;
        //mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(dir.getPath() + File.separator
                    + idrencanaDetail +  "_"
                    + timeStamp + ".png");
            newImageName1 = idrencanaDetail + "_" + timeStamp + ".png";

        } else {
            return null;
        }
        return mediaFile;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
//                    saveAppSupplierFoto1(newImageName1);
//                Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(mediaFile.getAbsolutePath()), 1280, 720, true);
//                Bitmap myBitmap = BitmapFactory.decodeFile(mediaFile.getAbsolutePath());
//                imgCust.setImageBitmap(rotateImage(myBitmap,-90));
                File newFile = new File(AppVar.getFolderPath() + "/" + IMAGE_DIRECTORY_NAME + "/" + newImageName1);
                Uri contentUri = Uri.fromFile(newFile);
                Bitmap  photo = null;
                try {
                    photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),contentUri);
                    String photoPath = AppVar.getFolderPath() + "/" + IMAGE_DIRECTORY_NAME + "/" + newImageName1;
                    ExifInterface ei = new ExifInterface(photoPath);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);

                    Bitmap rotatedBitmap = null;
                    switch(orientation) {

                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotatedBitmap = rotateImage(photo, 90);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotatedBitmap = rotateImage(photo, 180);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotatedBitmap = rotateImage(photo, 270);
                            break;

                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            rotatedBitmap = photo;
                    }
                    OutputStream fOut = new FileOutputStream(newFile);
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 30, fOut);
                    fOut.flush();
                    fOut.close();
                    imgCust.setImageBitmap(rotatedBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (newImageName1 != null)
                    tvFotoCustomer.setText(newImageName1);
                saveAppSupplierFoto1(newImageName1);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getActivity().getApplicationContext(),"Batal mengambil foto terbaru!", Toast.LENGTH_SHORT).show();
            } else {
                // failed to capture image
                Toast.makeText(getActivity().getApplicationContext(),
                        "Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private class DownloadDataRencanaDetail extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage(getActivity().getApplicationContext().getResources()
                    .getString(R.string.MSG_DLG_LABEL_SYNRONISASI_DATA));
            progressDialog.show();
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
            SharedPreferences prefs = act.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
            String id_karyawan = prefs.getString(AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_JADWAL, null);
            Log.d(LOG_TAG, "idrencanadetail_ : "+id_karyawan);

            String download_data_url = AppVar.CONFIG_APP_URL_PUBLIC
                    + AppVar.CONFIG_APP_URL_DOWNLOAD_CHECKIN+ "?id_rencana_detail="
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
                    Log.d(LOG_TAG, "response_data_checkin : "+response_data);
                    SharedPreferences spPreferences = getSharedPrefereces();
                    String main_app_table_data = spPreferences.getString(
                            AppVar.SHARED_PREFERENCES_TABLE_CHECKIN, null);
                    if (main_app_table_data != null) {
                        if (main_app_table_data.equalsIgnoreCase(response_data)) {
                            saveAppDataRencanaDetailSameData(act
                                    .getApplicationContext().getResources()
                                    .getString(R.string.app_value_true));
                        } else {
                            databaseHandler.deleteTableCheckin(id_karyawan);
                            saveAppDataRencanaDetailSameData(act
                                    .getApplicationContext().getResources()
                                    .getString(R.string.app_value_false));
                        }
                    } else {
                        databaseHandler.deleteTableCheckin(id_karyawan);
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
                databaseHandler.updateCheckin(String.valueOf(idrencanaDetail));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                String msg_success=act.getApplicationContext().getResources()
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
        }
    }

    public void saveAppDataRencanaDetailSameData(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_TABLE_CHECKIN_SAME_DATA,
                responsedata);
        editor.commit();
    }

    public void extractDataRencanaDetail() {
        Log.d(LOG_TAG, "masuk sini checkin: ");
        SharedPreferences spPreferences = getSharedPrefereces();
        String main_app_table_same_data = spPreferences.getString(
                AppVar.SHARED_PREFERENCES_TABLE_CHECKIN_SAME_DATA, null);
        String main_app_table = spPreferences.getString(
                AppVar.SHARED_PREFERENCES_TABLE_CHECKIN, null);
        if (main_app_table_same_data.equalsIgnoreCase(act
                .getApplicationContext().getResources()
                .getString(R.string.app_value_false))) {
            JSONObject oResponse;
            try {
                oResponse = new JSONObject(main_app_table);
                JSONArray jsonarr = oResponse.getJSONArray("trx_checkin");
                for (int i = 0; i < jsonarr.length(); i++) {
                    JSONObject oResponsealue = jsonarr.getJSONObject(i);
                    String id_checkin= oResponsealue.isNull("id_checkin") ? null
                            : oResponsealue.getString("id_checkin");
                    String tanggal_checkin = oResponsealue.isNull("tanggal_checkin") ? null
                            : oResponsealue.getString("tanggal_checkin");
                    String nomor_checkin = oResponsealue.isNull("nomor_checkin") ? null
                            : oResponsealue.getString("nomor_checkin");
                    String id_user = oResponsealue.isNull("id_user") ? null
                            : oResponsealue.getString("id_user");
                    String id_rencana_detail = oResponsealue.isNull("id_rencana_detail") ? null
                            : oResponsealue.getString("id_rencana_detail");
                    String id_rencana_header = oResponsealue.isNull("id_rencana_header") ? null
                            : oResponsealue.getString("id_rencana_header");
                    String kode_customer = oResponsealue.isNull("kode_customer") ? null
                            : oResponsealue.getString("kode_customer");
                    String lats = oResponsealue.isNull("lats") ? null
                            : oResponsealue.getString("lats");
                    String longs = oResponsealue.isNull("longs") ? null
                            : oResponsealue.getString("longs");
                    String foto = oResponsealue.isNull("foto") ? null
                            : oResponsealue.getString("foto");
                    Log.d(LOG_TAG, "id_rencana_detail_checkin:" + id_rencana_detail);
                    Log.d(LOG_TAG, "id_rencana_header_checkin:" + id_rencana_header);
                    databaseHandler.addFullCheckin(new Trx_Checkin(Integer.parseInt(id_checkin),tanggal_checkin,nomor_checkin,Integer.parseInt(id_user),Integer.parseInt(id_rencana_detail),Integer.parseInt(id_rencana_header),kode_customer,lats,longs,foto,"1","0"));
                }
                tvstatus_checkin.setText("Sudah Checkin");
                lsstatus.setBackgroundColor(Color.parseColor("#24ed0e"));
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

    public void saveAppDataRencanaDetail(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_TABLE_CHECKIN, responsedata);
        editor.commit();
    }

//    private class DownloadCheckin extends AsyncTask<String, Integer, String> {
//        @Override
//        protected void onPreExecute() {
//            progressDialog.setMessage(getActivity().getApplicationContext().getResources()
//                    .getString(R.string.MSG_DLG_LABEL_SYNRONISASI_DATA));
//            progressDialog.show();
//            progressDialog
//                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
//                        @Override
//                        public void onCancel(DialogInterface dialog) {
//                            String msg = getActivity().getApplicationContext()
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
//            SharedPreferences spPreferences = getSharedPrefereces();
//            idrencanaDetail = Integer.parseInt(spPreferences.getString(AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_JADWAL, null));
//            String download_data_url = AppVar.CONFIG_APP_URL_PUBLIC
//                    + AppVar.CONFIG_APP_URL_DOWNLOAD_CHECKIN+ "?id_rencana_detail="
//                    + idrencanaDetail;
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
//                    response_data_download = EntityUtils.toString(response.getEntity());
//                    String main_app_table_data = spPreferences.getString(
//                            AppVar.SHARED_PREFERENCES_TABLE_CHECKIN, null);
//                    if (main_app_table_data != null) {
//                        if (main_app_table_data.equalsIgnoreCase(response_data)) {
//                            saveAppDataCheckinSameData(act
//                                    .getApplicationContext().getResources()
//                                    .getString(R.string.app_value_true));
//                        } else {
//                            databaseHandler.deleteTableCheckin(String.valueOf(idrencanaDetail));
//                            saveAppDataCheckinSameData(act
//                                    .getApplicationContext().getResources()
//                                    .getString(R.string.app_value_false));
//                        }
//                    } else {
//                        databaseHandler.deleteTableCheckin(String.valueOf(idrencanaDetail));
//                        saveAppDataCheckinSameData(act.getApplicationContext()
//                                .getResources()
//                                .getString(R.string.app_value_false));
//                    }
//                } catch (org.apache.http.ParseException e) {
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
//                saveAppDataCheckin(response_data);
//                extractDataCheckin();
//                databaseHandler.updateCheckin(String.valueOf(idrencanaDetail));
//                extractUpload();
//                if (progressDialog != null) {
//                    progressDialog.dismiss();
//                }
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
//
//    public void extractDataCheckin() {
//        SharedPreferences spPreferences = getSharedPrefereces();
//        String main_app_table_same_data = spPreferences.getString(
//                AppVar.SHARED_PREFERENCES_TABLE_CHECKIN_SAME_DATA, null);
//        String main_app_table = spPreferences.getString(
//                AppVar.SHARED_PREFERENCES_TABLE_CHECKIN, null);
//        Log.d(LOG_TAG, "Respons yang diterima: "+main_app_table);
//        if (main_app_table_same_data.equalsIgnoreCase(act
//                .getApplicationContext().getResources()
//                .getString(R.string.app_value_false))) {
//            JSONObject oResponse;
//            try {
//                oResponse = new JSONObject(main_app_table);
//                JSONArray jsonarr = oResponse.getJSONArray("trx_checkin");
//                for (int i = 0; i < jsonarr.length(); i++) {
//                    JSONObject oResponsealue = jsonarr.getJSONObject(i);
//                    String id_checkin= oResponsealue.isNull("id_checkin") ? null
//                            : oResponsealue.getString("id_checkin");
//                    String tanggal_checkin = oResponsealue.isNull("tanggal_checkin") ? null
//                            : oResponsealue.getString("tanggal_checkin");
//                    String nomor_checkin = oResponsealue.isNull("nomor_checkin") ? null
//                            : oResponsealue.getString("nomor_checkin");
//                    String id_user = oResponsealue.isNull("id_user") ? null
//                            : oResponsealue.getString("id_user");
//                    String id_rencana_detail = oResponsealue.isNull("id_rencana_detail") ? null
//                            : oResponsealue.getString("id_rencana_detail");
//                    String id_rencana_header = oResponsealue.isNull("id_rencana_header") ? null
//                            : oResponsealue.getString("id_rencana_header");
//                    String kode_customer = oResponsealue.isNull("kode_customer") ? null
//                            : oResponsealue.getString("kode_customer");
//                    String lats = oResponsealue.isNull("lats") ? null
//                            : oResponsealue.getString("lats");
//                    String longs = oResponsealue.isNull("longs") ? null
//                            : oResponsealue.getString("longs");
//                    String foto = oResponsealue.isNull("foto") ? null
//                            : oResponsealue.getString("foto");
//                    Log.d(LOG_TAG, "id_rencana_detail_checkin:" + id_rencana_detail);
//                    Log.d(LOG_TAG, "id_rencana_header_checkin:" + id_rencana_header);
//                    databaseHandler.addFullCheckin(new Trx_Checkin(Integer.parseInt(id_checkin),tanggal_checkin,nomor_checkin,Integer.parseInt(id_user),Integer.parseInt(id_rencana_detail),Integer.parseInt(id_rencana_header),kode_customer,lats,longs,foto,"1"));
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
//
//    public void saveAppDataCheckinSameData(String responsedata) {
//        SharedPreferences sp = getSharedPrefereces();
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString(AppVar.SHARED_PREFERENCES_TABLE_CHECKIN_SAME_DATA,
//                responsedata);
//        editor.commit();
//    }
//
//    public void saveAppDataCheckin(String responsedata) {
//        SharedPreferences sp = getSharedPrefereces();
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString(AppVar.SHARED_PREFERENCES_TABLE_CHECKIN, responsedata);
//        editor.commit();
//    }

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

}
