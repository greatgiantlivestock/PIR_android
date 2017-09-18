package com.android.canvasing.gglc.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
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
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.canvasing.gglc.absen.ChangePassword;
import com.android.canvasing.gglc.absen.RequestHandler;
import com.android.canvasing.gglc.canvassing.IconTextTabsActivity;
import com.android.canvasing.gglc.database.MstUser;
import com.android.canvasing.gglc.database.Trx_Checkin;
import com.android.canvasing.gglc.database.User;
import com.android.canvasing.mobile.R;
import com.android.canvasing.gglc.absen.AppVar;
import com.android.canvasing.gglc.absen.NavigationDrawerCallbacks;
import com.android.canvasing.gglc.database.DatabaseHandler;
import com.android.canvasing.gglc.database.DetailRencana;
import com.android.canvasing.gglc.database.Mst_Customer;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static com.android.canvasing.gglc.absen.AppVar.SHARED_PREFERENCES_NAME;


public class OneFragment extends Fragment{
    Context thiscontext;
    private DatabaseHandler databaseHandler;
    private Context act;
    private ProgressDialog pDialog;
    //private Toolbar mToolbar;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog;
    private String message;
    protected LocationManager locationManager;

    private Spinner spinnerRencanaDetail;
    private ArrayList<DetailRencana> rencanaDetailList;
    private ArrayList<String> rencanaDetailStringList;
    private int idrencanaDetail = 0;

    private Spinner spinnerCustomer;
    private ArrayList<Mst_Customer> customerList;
    private ArrayList<String> customerStringList;
    private int id_customer = 0;

    private Button foto,checkin;

    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private Uri fileUri1, fileUri2, fileUri3; // file url to store image/video
    private File mediaFile;
    private String newImageName1;
    private TextView tvKodeCustomer,id_usr,id_rcn;
    private ImageView imgCust;
    private EditText no_checkin1;
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

    public OneFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        act=this.getActivity();
        databaseHandler = new DatabaseHandler(this.getActivity());
        spinnerRencanaDetail = (Spinner) view.findViewById(R.id.id_rencna_detail);
        spinnerCustomer = (Spinner) view.findViewById(R.id.id_customer);
        tvKodeCustomer = (TextView) view.findViewById(R.id.title_foto_customer);
        foto = (Button) view.findViewById(R.id.foto);
        checkin = (Button) view.findViewById(R.id.checkin);
        imgCust = (ImageView) view.findViewById(R.id.ImgCust);
        no_checkin1 = (EditText) view.findViewById(R.id.no_checkin);
        no_checkin1.setEnabled(false);

        if(databaseHandler.getCountDetailrencana()!=0){

            ArrayList<MstUser> staff_list = databaseHandler.getAllUser();
            user = new MstUser();
            for (MstUser tempStaff : staff_list)
                user = tempStaff;
            id_user=user.getId_user();

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

            idrencanaDetail = rencanaDetailList.get(0).getId_rencana_detail();

            //set list type customer
            customerList = new ArrayList<Mst_Customer>();
            customerStringList = new ArrayList<String>();
            List<Mst_Customer> dataCustomer = databaseHandler.getAllCustomer();
            for (Mst_Customer customer : dataCustomer) {
                customerList.add(customer);
                customerStringList.add(customer.getNama_customer());
            }

            ArrayAdapter<String> adapterCustomer = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, customerStringList);
            adapterCustomer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCustomer.setAdapter(adapterCustomer);
            spinnerCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent,
                                           View view, int position, long id) {
                    id_customer = customerList.get(position).getId_customer();
                    tvKodeCustomer.setText("");
                    no_checkin1.setText("");
                }
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            id_customer = customerList.get(0).getId_customer();
        }else{
            showCustomDialog("Download dencana detail terlebih dahulu");
        }

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id_customer!=0){
                    gotoCaptureImage1();
                }else{
                    showCustomDialog("Pilih customer terlebih dahulu");
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

    public void generateNomercheckpoint(){
        mst_customer = databaseHandler.getCustomer(id_customer);
        String timeStamp = new SimpleDateFormat("HHmmss",
                Locale.getDefault()).format(new Date());
        final String date = "yyyyMMdd";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(date);
        final String checkDate = dateFormat.format(calendar.getTime());
        String kode_customer=mst_customer.getKode_customer();
        String nomor_chekin=kode_customer+"."+checkDate+"."+timeStamp;
        no_checkin1.setText(nomor_chekin);
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
                    if (idrencanaDetail != 0 || id_customer != 0) {
                        if (vlats.equalsIgnoreCase("0")
                                || vlongs.equalsIgnoreCase("0")) {
                            String msg = getActivity().getApplicationContext()
                                    .getResources()
                                    .getString(
                                            R.string.MSG_DLG_LABEL_FAILED_CURRENT_GPS_DIALOG);
                            showCustomDialog(msg);
                        } else {
                            if (tvKodeCustomer.getText().length()!=0) {
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
                                String kode_customer=mst_customer.getKode_customer();
                                //String nomor_chekin=kode_customer+checkDate+timeStamp;

                                id_checkin=countId_checkin+1;


                                Trx_Checkin checkin = new Trx_Checkin();
                                checkin.setId_checkin(id_checkin);
                                checkin.setTanggal_checkin(checkDate);
                                checkin.setNomor_checkin(no_checkin1.getText().toString());
                                checkin.setId_user(id_user);
                                checkin.setId_rencana_detail(idrencanaDetail);
                                checkin.setKode_customer(kode_customer);
                                checkin.setLats(vlats);
                                checkin.setLongs(vlongs);
                                checkin.setFoto(tvKodeCustomer.getText().toString());

                                databaseHandler.addCheckin(checkin);

                                final String tanggal_checkin_checkin = checkDate;
                                final String nomor_checkin_checkin = no_checkin1.getText().toString();
                                final String id_user_checkin_checkin = String.valueOf(id_user);
                                final String id_rencana_detail_checkin = String.valueOf(idrencanaDetail);
                                final String kode_customer_checkin = kode_customer;
                                final String lats_checkin = vlats;
                                final String longs_checkin = vlongs;
                                final String foto_checkin = tvKodeCustomer.getText().toString();

                                class insertToDatabase extends AsyncTask<Void, Void, String> {
                                    ProgressDialog loading;

                                    @Override
                                    protected void onPreExecute() {
                                        super.onPreExecute();
                                        loading = ProgressDialog.show(OneFragment.this.getActivity(),"Proses...","Silahkan Tunggu...",false,false);
                                    }

                                    @Override
                                    protected void onPostExecute(String s) {
                                        super.onPostExecute(s);
                                        loading.dismiss();
                                        Toast.makeText(OneFragment.this.getActivity(),"Checkin Berhasil !",Toast.LENGTH_LONG).show();
                                        resetCheckin();
                                    }

                                    @Override
                                    protected String doInBackground(Void... params) {
                                        HashMap<String,String> hashMap  = new HashMap<>();
                                        hashMap.put("tanggal_checkin",tanggal_checkin_checkin);
                                        hashMap.put("nomor_checkin",nomor_checkin_checkin);
                                        hashMap.put("id_user",id_user_checkin_checkin);
                                        hashMap.put("id_rencana_detail",id_rencana_detail_checkin);
                                        hashMap.put("kode_customer",kode_customer_checkin);
                                        hashMap.put("lats",lats_checkin);
                                        hashMap.put("longs",longs_checkin);
                                        hashMap.put("foto",foto_checkin);

                                        RequestHandler rh = new RequestHandler();
                                        String res = rh.sendPostRequest(AppVar.POST_CHECKIN, hashMap);
                                        return res;
                                    }
                                }
                                insertToDatabase in = new insertToDatabase();
                                in.execute();

                                String msg = getActivity().getApplicationContext()
                                        .getResources()
                                        .getString(
                                                R.string.app_customer_checkin_save_success);
                                //showCustomDialog(msg);

                                //resetCheckin();
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
                        if (idrencanaDetail != 0 || id_customer != 0) {
                            if (vlats.equalsIgnoreCase("0")
                                    || vlongs.equalsIgnoreCase("0")) {
                                String msg = getActivity().getApplicationContext()
                                        .getResources()
                                        .getString(
                                                R.string.MSG_DLG_LABEL_FAILED_CURRENT_GPS_DIALOG);
                                showCustomDialog(msg);
                            } else {
                                if (tvKodeCustomer.getText().length()!=0) {
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
                                    String kode_customer=mst_customer.getKode_customer();
                                    //String nomor_chekin=kode_customer+checkDate+timeStamp;

                                    id_checkin=countId_checkin+1;


                                    Trx_Checkin checkin = new Trx_Checkin();
                                    checkin.setId_checkin(id_checkin);
                                    checkin.setTanggal_checkin(checkDate);
                                    checkin.setNomor_checkin(no_checkin1.getText().toString());
                                    checkin.setId_user(id_user);
                                    checkin.setId_rencana_detail(idrencanaDetail);
                                    checkin.setKode_customer(kode_customer);
                                    checkin.setLats(vlats);
                                    checkin.setLongs(vlongs);
                                    checkin.setFoto(tvKodeCustomer.getText().toString());

                                    databaseHandler.addCheckin(checkin);

                                    final String tanggal_checkin_checkin = checkDate;
                                    final String nomor_checkin_checkin = no_checkin1.getText().toString();
                                    final String id_user_checkin_checkin = String.valueOf(id_user);
                                    final String id_rencana_detail_checkin = String.valueOf(idrencanaDetail);
                                    final String kode_customer_checkin = kode_customer;
                                    final String lats_checkin = vlats;
                                    final String longs_checkin = vlongs;
                                    final String foto_checkin = tvKodeCustomer.getText().toString();

                                    class insertToDatabase extends AsyncTask<Void, Void, String> {
                                        ProgressDialog loading;

                                        @Override
                                        protected void onPreExecute() {
                                            super.onPreExecute();
                                            loading = ProgressDialog.show(OneFragment.this.getActivity(),"Proses...","Silahkan Tunggu...",false,false);
                                        }

                                        @Override
                                        protected void onPostExecute(String s) {
                                            super.onPostExecute(s);
                                            loading.dismiss();
                                            Toast.makeText(OneFragment.this.getActivity(),"Checkin Berhasil !",Toast.LENGTH_LONG).show();
                                            resetCheckin();
                                        }

                                        @Override
                                        protected String doInBackground(Void... params) {
                                            HashMap<String,String> hashMap  = new HashMap<>();
                                            hashMap.put("tanggal_checkin",tanggal_checkin_checkin);
                                            hashMap.put("nomor_checkin",nomor_checkin_checkin);
                                            hashMap.put("id_user",id_user_checkin_checkin);
                                            hashMap.put("id_rencana_detail",id_rencana_detail_checkin);
                                            hashMap.put("kode_customer",kode_customer_checkin);
                                            hashMap.put("lats",lats_checkin);
                                            hashMap.put("longs",longs_checkin);
                                            hashMap.put("foto",foto_checkin);

                                            RequestHandler rh = new RequestHandler();
                                            String res = rh.sendPostRequest(AppVar.POST_CHECKIN, hashMap);
                                            return res;
                                        }
                                    }
                                    insertToDatabase in = new insertToDatabase();
                                    in.execute();

                                    String msg = getActivity().getApplicationContext()
                                            .getResources()
                                            .getString(
                                                    R.string.app_customer_checkin_save_success);
                                    //showCustomDialog(msg);

                                    //resetCheckin();
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
                    if (idrencanaDetail != 0 || id_customer != 0) {
                        if (vlats.equalsIgnoreCase("0")
                                || vlongs.equalsIgnoreCase("0")) {
                            String msg = getActivity().getApplicationContext()
                                    .getResources()
                                    .getString(
                                            R.string.MSG_DLG_LABEL_FAILED_CURRENT_GPS_DIALOG);
                            showCustomDialog(msg);
                        } else {
                            if (tvKodeCustomer.getText().length()!=0) {
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
                                String kode_customer=mst_customer.getKode_customer();
                                //String nomor_chekin=kode_customer+checkDate+timeStamp;

                                id_checkin=countId_checkin+1;


                                Trx_Checkin checkin = new Trx_Checkin();
                                checkin.setId_checkin(id_checkin);
                                checkin.setTanggal_checkin(checkDate);
                                checkin.setNomor_checkin(no_checkin1.getText().toString());
                                checkin.setId_user(id_user);
                                checkin.setId_rencana_detail(idrencanaDetail);
                                checkin.setKode_customer(kode_customer);
                                checkin.setLats(vlats);
                                checkin.setLongs(vlongs);
                                checkin.setFoto(tvKodeCustomer.getText().toString());

                                databaseHandler.addCheckin(checkin);

                                final String tanggal_checkin_checkin = checkDate;
                                final String nomor_checkin_checkin = no_checkin1.getText().toString();
                                final String id_user_checkin_checkin = String.valueOf(id_user);
                                final String id_rencana_detail_checkin = String.valueOf(idrencanaDetail);
                                final String kode_customer_checkin = kode_customer;
                                final String lats_checkin = vlats;
                                final String longs_checkin = vlongs;
                                final String foto_checkin = tvKodeCustomer.getText().toString();

                                class insertToDatabase extends AsyncTask<Void, Void, String> {
                                    ProgressDialog loading;

                                    @Override
                                    protected void onPreExecute() {
                                        super.onPreExecute();
                                        loading = ProgressDialog.show(OneFragment.this.getActivity(),"Proses...","Silahkan Tunggu...",false,false);
                                    }

                                    @Override
                                    protected void onPostExecute(String s) {
                                        super.onPostExecute(s);
                                        loading.dismiss();
                                        Toast.makeText(OneFragment.this.getActivity(),"Checkin Berhasil !",Toast.LENGTH_LONG).show();
                                        resetCheckin();
                                    }

                                    @Override
                                    protected String doInBackground(Void... params) {
                                        HashMap<String,String> hashMap  = new HashMap<>();
                                        hashMap.put("tanggal_checkin",tanggal_checkin_checkin);
                                        hashMap.put("nomor_checkin",nomor_checkin_checkin);
                                        hashMap.put("id_user",id_user_checkin_checkin);
                                        hashMap.put("id_rencana_detail",id_rencana_detail_checkin);
                                        hashMap.put("kode_customer",kode_customer_checkin);
                                        hashMap.put("lats",lats_checkin);
                                        hashMap.put("longs",longs_checkin);
                                        hashMap.put("foto",foto_checkin);

                                        RequestHandler rh = new RequestHandler();
                                        String res = rh.sendPostRequest(AppVar.POST_CHECKIN, hashMap);
                                        return res;
                                    }
                                }
                                insertToDatabase in = new insertToDatabase();
                                in.execute();

                                String msg = getActivity().getApplicationContext()
                                        .getResources()
                                        .getString(
                                                R.string.app_customer_checkin_save_success);
                                //showCustomDialog(msg);

                                //resetCheckin();
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

    /*
    private void insertToDatabase() {

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
                locationManager = (LocationManager) getActivity().getApplicationContext()
                        .getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        1000L, 1.0f, locationListener);
                location2 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) ;

                if (location2 != null){
                    latitude=location2.getLatitude();
                    longitude=location2.getLongitude();

                    final String vlats=String.valueOf(latitude);
                    final String vlongs=String.valueOf(longitude);

                    SharedPreferences prefs = act.getSharedPreferences(AppVar.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                    final String id_awo = prefs.getString("id_awo","0");
                    final String id_user = prefs.getString("id_user","0");

                    class insertToDatabase extends AsyncTask<Void, Void, String> {
                        ProgressDialog loading;

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            loading = ProgressDialog.show(OneFragment.this.getActivity(),"Proses...","Silahkan Tunggu...",false,false);
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                            loading.dismiss();
                            Toast.makeText(OneFragment.this.getActivity(),"Absen Berhasil !",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        protected String doInBackground(Void... params) {
                            HashMap<String,String> hashMap  = new HashMap<>();
                            hashMap.put("lat",vlats);
                            hashMap.put("lng",vlongs);
                            hashMap.put("id_awo",id_awo);
                            hashMap.put("id_user",id_user);

                            RequestHandler rh = new RequestHandler();
                            String res = rh.sendPostRequest(AppVar.POST_ABSEN, hashMap);
                            return res;
                        }
                    }
                    insertToDatabase in = new insertToDatabase();
                    in.execute();

                }else{
                    locationManager = (LocationManager) getActivity().getApplicationContext()
                            .getSystemService(Context.LOCATION_SERVICE);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            1000L, 1.0f, locationListener);
                    location1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    latitude=location1.getLatitude();
                    longitude=location1.getLongitude();
                    final String vlats=String.valueOf(latitude);
                    final String vlongs=String.valueOf(longitude);

                    SharedPreferences prefs = act.getSharedPreferences(AppVar.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                    final String id_awo = prefs.getString("id_awo","0");
                    final String id_user = prefs.getString("id_user","0");

                    class insertToDatabase extends AsyncTask<Void, Void, String> {
                        ProgressDialog loading;

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            loading = ProgressDialog.show(OneFragment.this.getActivity(),"Proses...","Silahkan Tunggu...",false,false);
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                            loading.dismiss();
                            Toast.makeText(OneFragment.this.getActivity(),"Absen Berhasil !",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        protected String doInBackground(Void... params) {
                            HashMap<String,String> hashMap  = new HashMap<>();
                            hashMap.put("lat",vlats);
                            hashMap.put("lng",vlongs);
                            hashMap.put("id_awo",id_awo);
                            hashMap.put("id_user",id_user);

                            RequestHandler rh = new RequestHandler();
                            String res = rh.sendPostRequest(AppVar.POST_ABSEN, hashMap);
                            return res;
                        }
                    }
                    insertToDatabase in = new insertToDatabase();
                    in.execute();

                }
            }else if(isNetworkEnabled){
                locationManager = (LocationManager) getActivity().getApplicationContext()
                        .getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        1000L, 1.0f, locationListener);
                location1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if(location1 != null){
                    latitude=location1.getLatitude();
                    longitude=location1.getLongitude();
                    final String vlats=String.valueOf(latitude);
                    final String vlongs=String.valueOf(longitude);

                    SharedPreferences prefs = act.getSharedPreferences(AppVar.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                    final String id_awo = prefs.getString("id_awo","0");
                    final String id_user = prefs.getString("id_user","0");

                    class insertToDatabase extends AsyncTask<Void, Void, String> {
                        ProgressDialog loading;

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            loading = ProgressDialog.show(OneFragment.this.getActivity(),"Proses...","Silahkan Tunggu...",false,false);
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                            loading.dismiss();
                            Toast.makeText(OneFragment.this.getActivity(),"Absen Berhasil !",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        protected String doInBackground(Void... params) {
                            HashMap<String,String> hashMap  = new HashMap<>();
                            hashMap.put("lat",vlats);
                            hashMap.put("lng",vlongs);
                            hashMap.put("id_awo",id_awo);
                            hashMap.put("id_user",id_user);

                            RequestHandler rh = new RequestHandler();
                            String res = rh.sendPostRequest(AppVar.POST_CHECKIN, hashMap);
                            return res;
                        }
                    }
                    insertToDatabase in = new insertToDatabase();
                    in.execute();
                }else{
                    Toast.makeText(OneFragment.this.getActivity(), "Buka google map pada handphone anda, lalu login ulang aplikasi absen online",Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(OneFragment.this.getActivity(), "Silahkan periksa lokasi anda dahulu",Toast.LENGTH_LONG).show();
            }
        }
    }
    */

    public void gotoCaptureImage1() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri1 = getOutputMediaFileUri1(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri1);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void resetCheckin() {
        spinnerRencanaDetail.setSelection(0);
        spinnerCustomer.setSelection(0);
        imgCust.setImageResource(R.drawable.avatar);
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
        Mst_Customer mst_customer = databaseHandler.getCustomer(id_customer);
        String kode_customer=mst_customer.getKode_customer();
        //mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(dir.getPath() + File.separator
                    + kode_customer +  "_"
                    + "IMG_" + timeStamp + ".png");
            newImageName1 = kode_customer + "_" + "IMG_" + timeStamp + ".png";

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
                    tvKodeCustomer.setText(newImageName1);
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

    public void showCustomDialogExit() {
        String msg = getActivity().getApplicationContext().getResources().getString(
                R.string.MSG_DLG_LABEL_EXIT_DIALOG);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                act);
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setNegativeButton(
                        getActivity().getApplicationContext().getResources().getString(
                                R.string.MSG_DLG_LABEL_YES),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                android.os.Process
                                        .killProcess(android.os.Process.myPid());

                            }
                        })
                .setPositiveButton(
                        getActivity().getApplicationContext().getResources().getString(
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
}
