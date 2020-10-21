package com.android.pir.gglc.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pir.gglc.absen.AppVar;
import com.android.pir.gglc.absen.RequestHandler;
import com.android.pir.gglc.pir.CheckoutActivity;
import com.android.pir.gglc.database.DatabaseHandler;
import com.android.pir.gglc.database.DetailRencana;
import com.android.pir.gglc.database.MasterRencana;
import com.android.pir.gglc.database.MstUser;
import com.android.pir.gglc.database.Mst_Customer;
import com.android.pir.gglc.database.TmpCustomer;
import com.android.pir.gglc.database.Trx_Checkin;
import com.android.pir.mobile.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class TwoFragment extends Fragment {
    Context thiscontext;
    private DatabaseHandler databaseHandler;
    private Context act;
    private ProgressDialog pDialog;
    private Toolbar mToolbar;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog;
    private String message;
    protected LocationManager locationManager;

    private Spinner spinnerRencanaDetail;
    private ArrayList<DetailRencana> rencanaDetailList;
    private ArrayList<String> rencanaDetailStringList;
    private int idrencanaDetail = 0;

    private Spinner spinnerNomorRencana;
    private ArrayList<MasterRencana> rencanaMasterList;
    private ArrayList<String> rencanaMasterStringList;
    private int id_rencanaHeader = 0;

    private ArrayList<Mst_Customer> customerList;
    private ArrayList<String> customerStringList;
    private int id_customer = 0;

    private Button foto,checkin;

    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private Uri fileUri1, fileUri2, fileUri3; // file url to store image/video
    private File mediaFile;
    private String newImageName1;
    private TextView titleFotoCustomer,id_usr,id_rcn,kode_customer;
    private ImageView imgCust;
    private EditText no_checkin1,nama_customervalue,no_hpvalue,alamat_value,nama_usaha;
    private Mst_Customer mst_customer;

    private double latitude, longitude;
    private Location location;
    private Location location1;
    private Location location2;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    private static final String LOG_TAG = OneFragment.class.getSimpleName();
    private int id_checkin,id_user;
    private MstUser user;
    private String response_data;

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_two, container, false);
        View view = inflater.inflate(R.layout.fragment_two, container, false);

        act=this.getActivity();
        databaseHandler = new DatabaseHandler(this.getActivity());
        spinnerRencanaDetail = (Spinner) view.findViewById(R.id.id_rencna_detail);
        spinnerNomorRencana = (Spinner) view.findViewById(R.id.nomor_rencana);
        titleFotoCustomer = (TextView) view.findViewById(R.id.title_foto_customer);
        foto = (Button) view.findViewById(R.id.foto);
        checkin = (Button) view.findViewById(R.id.checkin);
        imgCust = (ImageView) view.findViewById(R.id.ImgCust);
        no_checkin1 = (EditText) view.findViewById(R.id.no_checkin);
        nama_customervalue = (EditText) view.findViewById(R.id.nama_customer);
        no_hpvalue = (EditText) view.findViewById(R.id.nohp_customer);
        nama_usaha = (EditText) view.findViewById(R.id.nama_usaha);
        alamat_value = (EditText) view.findViewById(R.id.alamat_customer);
        kode_customer = (TextView) view.findViewById(R.id.kode_customer);
        no_checkin1.setEnabled(false);

        ArrayList<MstUser> staff_list = databaseHandler.getAllUser();
        user = new MstUser();
        for (MstUser tempStaff : staff_list)
            user = tempStaff;
        id_user=user.getId_user();

        String jam = new SimpleDateFormat("HHmmss",
                Locale.getDefault()).format(new Date());
        final String date = "yyMMdd";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(date);
        final String tanggal = dateFormat.format(calendar.getTime());

        kode_customer.setText("NC"+id_user+"."+tanggal+"."+jam);

        if(databaseHandler.getCountDetailrencana()!=0){
            //set list type customer
            rencanaDetailList = new ArrayList<DetailRencana>();
            rencanaDetailStringList = new ArrayList<String>();
            List<DetailRencana> dataTypeCustomer = databaseHandler.getAllDetailRencana();
            for (DetailRencana typeCustomer : dataTypeCustomer) {
                rencanaDetailList.add(typeCustomer);
                rencanaDetailStringList.add(String.valueOf(typeCustomer.getId_rencana_detail()));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, rencanaDetailStringList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerRencanaDetail.setAdapter(adapter);
            spinnerRencanaDetail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent,
                                           View view, int position, long id) {
                    idrencanaDetail = rencanaDetailList.get(position).getId_rencana_detail();
                }
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            if(rencanaDetailList.isEmpty()){
                idrencanaDetail = 0;
            }else {
                idrencanaDetail = rencanaDetailList.get(0).getId_rencana_detail();
            }

            //set value spinner nomor rencana
            rencanaMasterList = new ArrayList<MasterRencana>();
            rencanaMasterStringList = new ArrayList<String>();
            List<MasterRencana> dataMasterRencana = databaseHandler.getAllMasterRencana();
            for (MasterRencana masterRencana : dataMasterRencana) {
                rencanaMasterList.add(masterRencana);
                rencanaMasterStringList.add(String.valueOf(masterRencana.getTanggal_rencana()));
            }

            //set value spinner  master rencana
            ArrayAdapter<String> adapterRencanaMaster = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, rencanaMasterStringList);
            adapterRencanaMaster.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerNomorRencana.setAdapter(adapterRencanaMaster);

            spinnerNomorRencana.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent,
                                           View view, int position, long id) {
                    id_rencanaHeader = rencanaMasterList.get(position).getId_rencana_header();
                    //tvFotoCustomer.setText("");
                    //no_checkin1.setText("");
                }
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            if(rencanaMasterList.isEmpty()){
                id_rencanaHeader = 0;
            }else{
                id_rencanaHeader = rencanaMasterList.get(0).getId_rencana_header();
            }
        }else{
            showCustomDialog("Download dencana detail terlebih dahulu");
        }

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nama_customervalue.getText().length()!=0 || alamat_value.getText().length()!=0 ||
                        no_hpvalue.getText().length()!=0 ||nama_usaha.getText().length()!=0){
                    gotoCaptureImage1();
                }else{
                    showCustomDialog("Anda harus mengisi data customer dengan lengkap");
                }
            }
        });
        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkinSave();
                //resetCheckin();
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

    public void checkinSave() {
        locationManager = (LocationManager) getActivity().getApplicationContext()
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
                    if (idrencanaDetail != 0 && nama_customervalue.getText().length()!=0&& no_hpvalue.getText().length()!=0&&
                            alamat_value.getText().length()!=0 && nama_usaha.getText().length()!=0) {
                        if (vlats.equalsIgnoreCase("0")
                                || vlongs.equalsIgnoreCase("0")) {
                            String msg = getActivity().getApplicationContext()
                                    .getResources()
                                    .getString(
                                            R.string.MSG_DLG_LABEL_FAILED_CURRENT_GPS_DIALOG);
                            showCustomDialog(msg);
                        } else {
                            if (titleFotoCustomer.getText().length()!=0) {
                                int countId_checkin = databaseHandler.getCountTrxcheckin();
                                final String date = "yyyy-MM-dd";
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat dateFormat = new SimpleDateFormat(date);
                                final String checkDate = dateFormat.format(calendar.getTime());
                                id_checkin=countId_checkin+1;

                                Trx_Checkin checkin = new Trx_Checkin();
                                checkin.setId_checkin(id_checkin);
                                checkin.setTanggal_checkin(checkDate);
                                checkin.setNomor_checkin(no_checkin1.getText().toString());
                                checkin.setId_user(id_user);
                                checkin.setId_rencana_detail(idrencanaDetail);
                                checkin.setId_rencana_header(id_rencanaHeader);
                                checkin.setKode_customer(kode_customer.getText().toString());
                                checkin.setLats(vlats);
                                checkin.setLongs(vlongs);
                                checkin.setFoto(titleFotoCustomer.getText().toString());
                                checkin.setStatus("1");
                                databaseHandler.addCheckin(checkin);

                                TmpCustomer tmpCustomer = new TmpCustomer();
                                tmpCustomer.setKode_customer(kode_customer.getText().toString());
                                tmpCustomer.setNama_customer(nama_customervalue.getText().toString());
                                tmpCustomer.setNo_hp(no_hpvalue.getText().toString());
                                tmpCustomer.setNama_usaha(nama_usaha.getText().toString());
                                tmpCustomer.setAlamat(alamat_value.getText().toString());
                                databaseHandler.addTmp_customer(tmpCustomer);

                                final String tanggal_checkin_checkin = checkDate;
                                final String nomor_checkin_checkin = no_checkin1.getText().toString();
                                final String id_user_checkin_checkin = String.valueOf(id_user);
                                final String id_rencana_detail_checkin = String.valueOf(idrencanaDetail);
                                final String id_rencana_master_checkin = String.valueOf(id_rencanaHeader);
                                final String kode_customer_checkin = kode_customer.getText().toString();
                                final String lats_checkin = vlats;
                                final String longs_checkin = vlongs;
                                final String foto_checkin = titleFotoCustomer.getText().toString();

                                final String kode_customer_tmp = kode_customer.getText().toString();
                                final String nama_customer_tmp = nama_customervalue.getText().toString();
                                final String no_hp_tmp = no_hpvalue.getText().toString();
                                final String nama_usaha_tmp = nama_usaha.getText().toString();
                                final String alamat_tmp =  alamat_value.getText().toString();

                                class insertTrxCheckin extends AsyncTask<Void, Void, String> {
                                    ProgressDialog loading;

                                    @Override
                                    protected void onPreExecute() {
                                        super.onPreExecute();
                                        loading = ProgressDialog.show(TwoFragment.this.getActivity(),"Proses...","Silahkan Tunggu...",false,false);
                                    }

                                    @Override
                                    protected void onPostExecute(String s) {
                                        super.onPostExecute(s);
                                        loading.dismiss();
                                    }

                                    @Override
                                    protected String doInBackground(Void... params) {
                                        HashMap<String,String> hashMap  = new HashMap<>();
                                        hashMap.put("tanggal_checkin",tanggal_checkin_checkin);
                                        hashMap.put("nomor_checkin",nomor_checkin_checkin);
                                        hashMap.put("id_user",id_user_checkin_checkin);
                                        hashMap.put("id_rencana_detail",id_rencana_detail_checkin);
                                        hashMap.put("id_rencana_header",id_rencana_master_checkin);
                                        hashMap.put("kode_customer",kode_customer_checkin);
                                        hashMap.put("lats",lats_checkin);
                                        hashMap.put("longs",longs_checkin);
                                        hashMap.put("foto",foto_checkin);

                                        RequestHandler rh = new RequestHandler();
                                        String res = rh.sendPostRequest(AppVar.POST_CHECKIN_PROSPECT, hashMap);
                                        return res;
                                    }
                                }
                                insertTrxCheckin in = new insertTrxCheckin();
                                in.execute();

                                class InsertTmpCustomer extends AsyncTask<Void, Void, String> {
                                    ProgressDialog loading;

                                    @Override
                                    protected void onPreExecute() {
                                        super.onPreExecute();
                                        loading = ProgressDialog.show(TwoFragment.this.getActivity(),"Proses...","Silahkan Tunggu...",false,false);
                                    }

                                    @Override
                                    protected void onPostExecute(String s) {
                                        super.onPostExecute(s);
                                        loading.dismiss();
                                        Toast.makeText(TwoFragment.this.getActivity(),"Checkin Berhasil !",Toast.LENGTH_LONG).show();
                                        //resetCheckin();
//                                        gotoDashboard();
                                        new UploadData().execute();
                                    }

                                    @Override
                                    protected String doInBackground(Void... params) {
                                        HashMap<String,String> hashMap  = new HashMap<>();
                                        hashMap.put("kode_customer",kode_customer_tmp);
                                        hashMap.put("nama_customer",nama_customer_tmp);
                                        hashMap.put("no_hp",no_hp_tmp);
                                        hashMap.put("nama_usaha",nama_usaha_tmp);
                                        hashMap.put("alamat",alamat_tmp);

                                        RequestHandler rh = new RequestHandler();
                                        String res = rh.sendPostRequest(AppVar.POST_CUSTOMER_TMP, hashMap);
                                        return res;
                                    }
                                }
                                InsertTmpCustomer intmp = new InsertTmpCustomer();
                                intmp.execute();

                                String msg = getActivity().getApplicationContext()
                                        .getResources()
                                        .getString(
                                                R.string.app_customer_checkin_save_success);
                                //showCustomDialog(msg);
                            } else {
                                String msg = getActivity().getApplicationContext()
                                        .getResources()
                                        .getString(
                                                R.string.app_customer_checkin_save_failed_no_image);
                                showCustomDialog(msg);
                            }
                        }
                    } else {
                        String msg = getActivity().getApplicationContext()
                                .getResources().getString(R.string.app_checkin_incorrect_value);
                        showCustomDialogCheckinSuccess(msg);
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
                        if (idrencanaDetail != 0 && nama_customervalue.getText().length()!=0&& no_hpvalue.getText().length()!=0&&
                                alamat_value.getText().length()!=0 && nama_usaha.getText().length()!=0) {
                            if (vlats.equalsIgnoreCase("0")
                                    || vlongs.equalsIgnoreCase("0")) {
                                String msg = getActivity().getApplicationContext()
                                        .getResources()
                                        .getString(
                                                R.string.MSG_DLG_LABEL_FAILED_CURRENT_GPS_DIALOG);
                                showCustomDialog(msg);
                            } else {
                                if (titleFotoCustomer.getText().length()!=0) {
                                    //SharedPreferences prefs = act.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                                    //String id_karyawan = prefs.getString("id_awo","null");

                                    //Mst_Customer mst_customer = databaseHandler.getCustomer(id_customer);
                                    //MstUser user = databaseHandler.getUser(Integer.parseInt(id_karyawan));
                                    int countId_checkin = databaseHandler.getCountTrxcheckin();
                                    //String timeStamp = new SimpleDateFormat("HHmmss",
                                    //        Locale.getDefault()).format(new Date());
                                    final String date = "yyyy-MM-dd";
                                    Calendar calendar = Calendar.getInstance();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat(date);
                                    final String checkDate = dateFormat.format(calendar.getTime());
                                    //String kode_customer=mst_customer.getKode_customer();
                                    //String nomor_chekin=kode_customer+checkDate+timeStamp;
                                    id_checkin=countId_checkin+1;

                                    Trx_Checkin checkin = new Trx_Checkin();
                                    checkin.setId_checkin(id_checkin);
                                    checkin.setTanggal_checkin(checkDate);
                                    checkin.setNomor_checkin(no_checkin1.getText().toString());
                                    checkin.setId_user(id_user);
                                    checkin.setId_rencana_detail(idrencanaDetail);
                                    checkin.setId_rencana_header(id_rencanaHeader);
                                    checkin.setKode_customer(kode_customer.getText().toString());
                                    checkin.setLats(vlats);
                                    checkin.setLongs(vlongs);
                                    checkin.setFoto(titleFotoCustomer.getText().toString());
                                    checkin.setStatus("1");
                                    databaseHandler.addCheckin(checkin);

                                    TmpCustomer tmpCustomer = new TmpCustomer();
                                    //tmpCustomer.setId_customer_tmp(id_checkin);
                                    tmpCustomer.setKode_customer(kode_customer.getText().toString());
                                    tmpCustomer.setNama_customer(nama_customervalue.getText().toString());
                                    tmpCustomer.setNo_hp(no_hpvalue.getText().toString());
                                    tmpCustomer.setNama_usaha(nama_usaha.getText().toString());
                                    tmpCustomer.setAlamat(alamat_value.getText().toString());
                                    databaseHandler.addTmp_customer(tmpCustomer);

                                    final String tanggal_checkin_checkin = checkDate;
                                    final String nomor_checkin_checkin = no_checkin1.getText().toString();
                                    final String id_user_checkin_checkin = String.valueOf(id_user);
                                    final String id_rencana_detail_checkin = String.valueOf(0);
                                    final String id_rencana_master_checkin = String.valueOf(id_rencanaHeader);
                                    final String kode_customer_checkin = kode_customer.getText().toString();
                                    final String lats_checkin = vlats;
                                    final String longs_checkin = vlongs;
                                    final String foto_checkin = titleFotoCustomer.getText().toString();

                                    final String kode_customer_tmp = kode_customer.getText().toString();
                                    final String nama_customer_tmp = nama_customervalue.getText().toString();
                                    final String no_hp_tmp = no_hpvalue.getText().toString();
                                    final String nama_usaha_tmp = nama_usaha.getText().toString();
                                    final String alamat_tmp =  alamat_value.getText().toString();

                                    class insertTrxCheckin extends AsyncTask<Void, Void, String> {
                                        ProgressDialog loading;

                                        @Override
                                        protected void onPreExecute() {
                                            super.onPreExecute();
                                            loading = ProgressDialog.show(TwoFragment.this.getActivity(),"Proses...","Silahkan Tunggu...",false,false);
                                        }

                                        @Override
                                        protected void onPostExecute(String s) {
                                            super.onPostExecute(s);
                                            loading.dismiss();
                                            //Toast.makeText(TwoFragment1.this.getActivity(),"Checkin Berhasil !",Toast.LENGTH_LONG).show();
                                            //resetCheckin();
                                        }

                                        @Override
                                        protected String doInBackground(Void... params) {
                                            HashMap<String,String> hashMap  = new HashMap<>();
                                            hashMap.put("tanggal_checkin",tanggal_checkin_checkin);
                                            hashMap.put("nomor_checkin",nomor_checkin_checkin);
                                            hashMap.put("id_user",id_user_checkin_checkin);
                                            hashMap.put("id_rencana_detail",id_rencana_detail_checkin);
                                            hashMap.put("id_rencana_header",id_rencana_master_checkin);
                                            hashMap.put("kode_customer",kode_customer_checkin);
                                            hashMap.put("lats",lats_checkin);
                                            hashMap.put("longs",longs_checkin);
                                            hashMap.put("foto",foto_checkin);

                                            RequestHandler rh = new RequestHandler();
                                            String res = rh.sendPostRequest(AppVar.POST_CHECKIN_PROSPECT, hashMap);
                                            return res;
                                        }
                                    }
                                    insertTrxCheckin in = new insertTrxCheckin();
                                    in.execute();

                                    class InsertTmpCustomer extends AsyncTask<Void, Void, String> {
                                        ProgressDialog loading;

                                        @Override
                                        protected void onPreExecute() {
                                            super.onPreExecute();
                                            loading = ProgressDialog.show(TwoFragment.this.getActivity(),"Proses...","Silahkan Tunggu...",false,false);
                                        }

                                        @Override
                                        protected void onPostExecute(String s) {
                                            super.onPostExecute(s);
                                            loading.dismiss();
                                            Toast.makeText(TwoFragment.this.getActivity(),"Checkin Berhasil !",Toast.LENGTH_LONG).show();
                                            //resetCheckin();
//                                            gotoDashboard();
                                            new UploadData().execute();
                                        }

                                        @Override
                                        protected String doInBackground(Void... params) {
                                            HashMap<String,String> hashMap  = new HashMap<>();
                                            hashMap.put("kode_customer",kode_customer_tmp);
                                            hashMap.put("nama_customer",nama_customer_tmp);
                                            hashMap.put("no_hp",no_hp_tmp);
                                            hashMap.put("nama_usaha",nama_usaha_tmp);
                                            hashMap.put("alamat",alamat_tmp);

                                            RequestHandler rh = new RequestHandler();
                                            String res = rh.sendPostRequest(AppVar.POST_CUSTOMER_TMP, hashMap);
                                            return res;
                                        }
                                    }
                                    InsertTmpCustomer intmp = new InsertTmpCustomer();
                                    intmp.execute();

                                    String msg = getActivity().getApplicationContext()
                                            .getResources()
                                            .getString(
                                                    R.string.app_customer_checkin_save_success);
                                    //showCustomDialog(msg);
                                } else {
                                    String msg = getActivity().getApplicationContext()
                                            .getResources()
                                            .getString(
                                                    R.string.app_customer_checkin_save_failed_no_image);
                                    showCustomDialog(msg);
                                }
                            }
                        } else {
                            String msg = getActivity().getApplicationContext()
                                    .getResources().getString(R.string.app_checkin_incorrect_value);
                            showCustomDialogCheckinSuccess(msg);
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
                    if (idrencanaDetail != 0 && nama_customervalue.getText().length()!=0&& no_hpvalue.getText().length()!=0&&
                            alamat_value.getText().length()!=0 ||nama_usaha.getText().length()!=0) {
                        if (vlats.equalsIgnoreCase("0")
                                || vlongs.equalsIgnoreCase("0")) {
                            String msg = getActivity().getApplicationContext()
                                    .getResources()
                                    .getString(
                                            R.string.MSG_DLG_LABEL_FAILED_CURRENT_GPS_DIALOG);
                            showCustomDialog(msg);
                        } else {
                            if (titleFotoCustomer.getText().length()!=0) {
                                //SharedPreferences prefs = act.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                                //String id_karyawan = prefs.getString("id_awo","null");

                                //Mst_Customer mst_customer = databaseHandler.getCustomer(id_customer);
                                //MstUser user = databaseHandler.getUser(Integer.parseInt(id_karyawan));
                                int countId_checkin = databaseHandler.getCountTrxcheckin();
                                //String timeStamp = new SimpleDateFormat("HHmmss",
                                //        Locale.getDefault()).format(new Date());
                                final String date = "yyyy-MM-dd";
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat dateFormat = new SimpleDateFormat(date);
                                final String checkDate = dateFormat.format(calendar.getTime());
                                //String kode_customer=mst_customer.getKode_customer();
                                //String nomor_chekin=kode_customer+checkDate+timeStamp;
                                id_checkin=countId_checkin+1;

                                Trx_Checkin checkin = new Trx_Checkin();
                                checkin.setId_checkin(id_checkin);
                                checkin.setTanggal_checkin(checkDate);
                                checkin.setNomor_checkin(no_checkin1.getText().toString());
                                checkin.setId_user(id_user);
                                checkin.setId_rencana_detail(idrencanaDetail);
                                checkin.setId_rencana_header(id_rencanaHeader);
                                checkin.setKode_customer(kode_customer.getText().toString());
                                checkin.setLats(vlats);
                                checkin.setLongs(vlongs);
                                checkin.setFoto(titleFotoCustomer.getText().toString());
                                checkin.setStatus("1");
                                databaseHandler.addCheckin(checkin);

                                TmpCustomer tmpCustomer = new TmpCustomer();
                                //tmpCustomer.setId_customer_tmp(id_checkin);
                                tmpCustomer.setKode_customer(kode_customer.getText().toString());
                                tmpCustomer.setNama_customer(nama_customervalue.getText().toString());
                                tmpCustomer.setNo_hp(no_hpvalue.getText().toString());
                                tmpCustomer.setNama_usaha(nama_usaha.getText().toString());
                                tmpCustomer.setAlamat(alamat_value.getText().toString());
                                databaseHandler.addTmp_customer(tmpCustomer);

                                final String tanggal_checkin_checkin = checkDate;
                                final String nomor_checkin_checkin = no_checkin1.getText().toString();
                                final String id_user_checkin_checkin = String.valueOf(id_user);
                                final String id_rencana_detail_checkin = String.valueOf(0);
                                final String id_rencana_master_checkin = String.valueOf(id_rencanaHeader);
                                final String kode_customer_checkin = kode_customer.getText().toString();
                                final String lats_checkin = vlats;
                                final String longs_checkin = vlongs;
                                final String foto_checkin = titleFotoCustomer.getText().toString();

                                final String kode_customer_tmp = kode_customer.getText().toString();
                                final String nama_customer_tmp = nama_customervalue.getText().toString();
                                final String no_hp_tmp = no_hpvalue.getText().toString();
                                final String nama_usaha_tmp = nama_usaha.getText().toString();
                                final String alamat_tmp =  alamat_value.getText().toString();

                                class insertTrxCheckin extends AsyncTask<Void, Void, String> {
                                    ProgressDialog loading;

                                    @Override
                                    protected void onPreExecute() {
                                        super.onPreExecute();
                                        loading = ProgressDialog.show(TwoFragment.this.getActivity(),"Proses...","Silahkan Tunggu...",false,false);
                                    }

                                    @Override
                                    protected void onPostExecute(String s) {
                                        super.onPostExecute(s);
                                        loading.dismiss();
                                        //Toast.makeText(TwoFragment1.this.getActivity(),"Checkin Berhasil !",Toast.LENGTH_LONG).show();
                                        //resetCheckin();
                                    }

                                    @Override
                                    protected String doInBackground(Void... params) {
                                        HashMap<String,String> hashMap  = new HashMap<>();
                                        hashMap.put("tanggal_checkin",tanggal_checkin_checkin);
                                        hashMap.put("nomor_checkin",nomor_checkin_checkin);
                                        hashMap.put("id_user",id_user_checkin_checkin);
                                        hashMap.put("id_rencana_detail",id_rencana_detail_checkin);
                                        hashMap.put("id_rencana_header",id_rencana_master_checkin);
                                        hashMap.put("kode_customer",kode_customer_checkin);
                                        hashMap.put("lats",lats_checkin);
                                        hashMap.put("longs",longs_checkin);
                                        hashMap.put("foto",foto_checkin);

                                        RequestHandler rh = new RequestHandler();
                                        String res = rh.sendPostRequest(AppVar.POST_CHECKIN_PROSPECT, hashMap);
                                        return res;
                                    }
                                }
                                insertTrxCheckin in = new insertTrxCheckin();
                                in.execute();

                                class InsertTmpCustomer extends AsyncTask<Void, Void, String> {
                                    ProgressDialog loading;

                                    @Override
                                    protected void onPreExecute() {
                                        super.onPreExecute();
                                        loading = ProgressDialog.show(TwoFragment.this.getActivity(),"Proses...","Silahkan Tunggu...",false,false);
                                    }

                                    @Override
                                    protected void onPostExecute(String s) {
                                        super.onPostExecute(s);
                                        loading.dismiss();
                                        Toast.makeText(TwoFragment.this.getActivity(),"Checkin Berhasil !",Toast.LENGTH_LONG).show();
                                        //resetCheckin();
//                                        gotoDashboard();
                                        new UploadData().execute();
                                    }

                                    @Override
                                    protected String doInBackground(Void... params) {
                                        HashMap<String,String> hashMap  = new HashMap<>();
                                        hashMap.put("kode_customer",kode_customer_tmp);
                                        hashMap.put("nama_customer",nama_customer_tmp);
                                        hashMap.put("no_hp",no_hp_tmp);
                                        hashMap.put("nama_usaha",nama_usaha_tmp);
                                        hashMap.put("alamat",alamat_tmp);

                                        RequestHandler rh = new RequestHandler();
                                        String res = rh.sendPostRequest(AppVar.POST_CUSTOMER_TMP, hashMap);
                                        return res;
                                    }
                                }
                                InsertTmpCustomer intmp = new InsertTmpCustomer();
                                intmp.execute();

                                String msg = getActivity().getApplicationContext()
                                        .getResources()
                                        .getString(
                                                R.string.app_customer_checkin_save_success);
                                //showCustomDialog(msg);
                            } else {
                                String msg = getActivity().getApplicationContext()
                                        .getResources()
                                        .getString(
                                                R.string.app_customer_checkin_save_failed_no_image);
                                showCustomDialog(msg);
                            }
                        }
                    } else {
                        String msg = getActivity().getApplicationContext()
                                .getResources().getString(R.string.app_checkin_incorrect_value);
                        showCustomDialogCheckinSuccess(msg);
                    }
                }else{
                    Toast.makeText(getActivity(), "Buka google map pada handphone anda, lalu login ulang aplikasi absen online",Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(getActivity(), "Silahkan periksa lokasi anda dahulu",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void showCustomDialogCheckinSuccess(String msg) {
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
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                //idrencanaDetail = rencanaDetailList.get(0).getId_rencana_detail();
                                //id_customer = customerList.get(0).getId_customer();
                                alertDialog.dismiss();

                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void generateNomercheckpoint(){
        final String date = "yyMMdd";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(date);
        final String checkDate = dateFormat.format(calendar.getTime());
        String kode_customerv=kode_customer.getText().toString();
        String nomor_chekin=kode_customerv+"_"+checkDate;
        no_checkin1.setText(nomor_chekin);
    }

    public void gotoCaptureImage1() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri1 = getOutputMediaFileUri1(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri1);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void resetCheckin() {
        spinnerRencanaDetail.setSelection(0);
        spinnerNomorRencana.setSelection(0);
        nama_customervalue.setText("");
        alamat_value.setText("");
        no_hpvalue.setText("");
        nama_usaha.setText("");
        titleFotoCustomer.setText("");
        no_checkin1.setText("");
        imgCust.setImageResource(R.drawable.avatar);
        String jam = new SimpleDateFormat("HHmmss",
                Locale.getDefault()).format(new Date());
        final String date = "yyMMdd";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(date);
        final String tanggal = dateFormat.format(calendar.getTime());

        kode_customer.setText("NC"+id_user+"."+tanggal+"."+jam);
    }

    public void gotoDashboard(){
        Intent intentActivity = new Intent(this.getActivity(),
                CheckoutActivity.class);
        startActivity(intentActivity);
        getActivity().finish();
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
                + AppVar.CONFIG_APP_FOLDER_CUSTOMER_PROSPECT);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        //SharedPreferences spPreferences = getSharedPrefereces();
        //Mst_Customer mst_customer = databaseHandler.getCustomer(id_customer);
        String kode_customerv=kode_customer.getText().toString();
        //mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(dir.getPath() + File.separator
                    + kode_customerv +  "_"
                    + "IMG_" + timeStamp + ".png");
            newImageName1 = kode_customerv + "_" + "IMG_" + timeStamp + ".png";

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
                //Log.d(LOG_TAG, "take image success");
                if (newImageName1 != null)
                    titleFotoCustomer.setText(newImageName1);
                    saveAppSupplierFoto1(newImageName1);
                generateNomercheckpoint();

                Bitmap myBitmap = BitmapFactory.decodeFile(mediaFile.getAbsolutePath());
                imgCust.setImageBitmap(myBitmap);
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

    public class UploadData extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
//            progressDialog
//                    .setMessage(getApplicationContext()
//                            .getResources()
//                            .getString(
//                                    R.string.app_supplier_processing));
//            progressDialog.show();
//            progressDialog.setCancelable(false);
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
        }

        @Override
        protected String doInBackground(String... params) {
            String url = AppVar.CONFIG_APP_URL_PUBLIC;
            String uploadSupplier = AppVar.CONFIG_APP_URL_UPLOAD_INSERT_CHECKIN_PROSPECT;
            String upload_image_supplier_url = url
                    + uploadSupplier;
            SharedPreferences spPreferences = getSharedPrefereces();
            String main_app_id_foto1 = spPreferences.getString(
                    AppVar.SHARED_PREFERENCES_CHECKIN_PROSPECT, null);;
            /***********************
             * Upload Image Supplier
             */
            response_data = uploadImage(
                    upload_image_supplier_url,
                    main_app_id_foto1);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(LOG_TAG, "response:" + response_data);
            if (response_data != null && response_data.length() > 0) {
                if (response_data.startsWith("Error occurred")) {
                    final String msg = act
                            .getApplicationContext()
                            .getResources()
                            .getString(
                                    R.string.app_supplier_processing_failed);
                    handler.post(new Runnable() {
                        public void run() {
                            showCustomDialog(msg);
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            extractUpload();
                        }
                    });
                }
            } else {
                final String msg = act
                        .getApplicationContext()
                        .getResources()
                        .getString(
                                R.string.app_supplier_processing_failed);
                handler.post(new Runnable() {
                    public void run() {
                        showCustomDialog(msg);
                    }
                });
            }
        }
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
                    saveAppSupplierFoto1("");

                    File dir = new File(AppVar.getFolderPath() + "/"
                            + AppVar.CONFIG_APP_FOLDER_CUSTOMER_PROSPECT);

                    List<File> fileFoto = getListFiles(dir);
                    for (File tempFile : fileFoto) {
                        tempFile.delete();
                    }

                    final String msg = act
                            .getApplicationContext()
                            .getResources()
                            .getString(
                                    R.string.app_supplier_processing_sukses);
                    showCustomDialogDownloadSuccess(msg);

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

                                gotoDashboard();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    protected String uploadImage(final String url, final String foto_1) {
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
            File dir1 = new File(AppVar.getFolderPath() + "/"
                    + AppVar.CONFIG_APP_FOLDER_CUSTOMER_PROSPECT + "/"
                    + foto_1);
            if (dir1.exists() && foto_1 != null) {
                entity.addPart("image_1", new FileBody(dir1));
                entity.addPart("foto1", new StringBody(foto_1));
            }
            else {
                entity.addPart("foto1", new StringBody(""));
            }

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

    public void saveAppSupplierFoto1(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_CHECKIN_PROSPECT,
                responsedata);
        editor.commit();
    }

}
