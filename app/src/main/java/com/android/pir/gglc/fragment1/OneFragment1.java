package com.android.pir.gglc.fragment1;

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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

//import com.android.canvasing.gglc.canvassing.SalesKanvasActivity;


//public class OneFragment1 extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
public class OneFragment1 extends Fragment{
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
    private Spinner spinnerRencanaMaster;
    private Spinner spinnerCustomer;

    private ArrayList<DetailRencana> rencanaDetailList;
    private ArrayList<Mst_Customer> customerList;
    private ArrayList<MasterRencana> rencanaMasterList;

    private ArrayList<String> rencanaDetailStringList;
    private ArrayList<String> rencanaMasterStringList;
    private ArrayList<String> customerStringList;

    private int idrencanaDetail = 0;
    private int id_rencanaHeader = 0;
    private int idCustomer1 = 0;
    private int id_customer = 0;

    private Button foto,checkin,kanvassing;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private Uri fileUri1, fileUri2, fileUri3; // file url to store image/video
    private File mediaFile;
    private String newImageName1;
    private String response_data;
    private TextView tvFotoCustomer,id_usr,id_rcn;
    private ImageView imgCust;
    private EditText no_checkin1;
    private Mst_Customer mst_customer;
    private double latitude, longitude;
    private Location location;
    private Location location1;
    private Location location2;
    private String IMAGE_DIRECTORY_NAME = "Kanvas";
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    private static final String LOG_TAG = OneFragment1.class.getSimpleName();
    private int id_checkin,id_user;
    private MstUser user;
//    private SwipeRefreshLayout swipeRefreshLayout;
    //private Trx_Checkin checkin_data;
    private String idrd,idcst,fto,nmr,lts,lng,idus,tgl;
    public OneFragment1() {
        //no coding in here
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    @Override
//    public void onRefresh() {
//        //databaseHandler.deleteRencanaMaster();
//        //databaseHandler.deleteRencanaDetail();
//
//        //Intent intent = new Intent(act, IconTextTabsActivity.class);
//        //startActivity(intent);
//        //getActivity().finish();
//        //Toast.makeText(getActivity(),"Test refresh",Toast.LENGTH_LONG).show();
//        //swipeRefreshLayout.setRefreshing(false);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pir_fragment_one, container, false);

//        act=this.getActivity();
//        databaseHandler = new DatabaseHandler(this.getActivity());
//        spinnerRencanaDetail = (Spinner) view.findViewById(R.id.id_rencna_detail);
//        spinnerRencanaMaster = (Spinner) view.findViewById(R.id.nomor_rencana_value);
//        spinnerCustomer = (Spinner) view.findViewById(R.id.id_customer);
//        tvFotoCustomer = (TextView) view.findViewById(R.id.title_foto_customer);
//        foto = (Button) view.findViewById(R.id.foto);
//        checkin = (Button) view.findViewById(R.id.checkin);
//        kanvassing = (Button) view.findViewById(R.id.kanvassing);
//        imgCust = (ImageView) view.findViewById(R.id.ImgCust);
//        no_checkin1 = (EditText) view.findViewById(R.id.no_checkin);
//        no_checkin1.setEnabled(false);
//
//        spinnerCustomer.setClickable(false);
//
//        SharedPreferences spPreferences = getSharedPrefereces();
//        final String main_app_id_detail_jadwal = spPreferences.getString(
//                AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_JADWAL, null);
//
//        if(databaseHandler.getCountDetailrencana()!=0){
//
//            ArrayList<MstUser> staff_list = databaseHandler.getAllUser();
//            user = new MstUser();
//
//            for (MstUser tempStaff : staff_list)
//                user = tempStaff;
//            id_user=user.getId_user();
//
//            //set list master rencana
//            rencanaMasterList = new ArrayList<MasterRencana>();
//            rencanaMasterStringList = new ArrayList<String>();
//            List<MasterRencana> dataMasterRencana = databaseHandler.getAllMasterRencana();
//            for (MasterRencana masterRencana : dataMasterRencana) {
//                rencanaMasterList.add(masterRencana);
//                rencanaMasterStringList.add(String.valueOf(masterRencana.getNomor_rencana()));
//            }
//
//            //set value spinner  master rencana
//            ArrayAdapter<String> adapterRencanaMaster = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, rencanaMasterStringList);
//            adapterRencanaMaster.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinnerRencanaMaster.setAdapter(adapterRencanaMaster);
//
//            spinnerRencanaMaster.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                public void onItemSelected(AdapterView<?> parent,
//                                           View view, int position, long id) {
//                    id_rencanaHeader = rencanaMasterList.get(position).getId_rencana_header();
//                }
//                public void onNothingSelected(AdapterView<?> parent) {
//                }
//            });
//
//            if(rencanaMasterList.isEmpty()){
//                id_rencanaHeader = 0;
//            }else {
//                id_rencanaHeader = rencanaMasterList.get(0).getId_rencana_header();
//            }
//
//            //set list detail rencana
//            rencanaDetailList = new ArrayList<DetailRencana>();
//            rencanaDetailStringList = new ArrayList<String>();
//            List<DetailRencana> dataTypeCustomer = databaseHandler.getAllDetailRencanaNEW(Integer.parseInt(main_app_id_detail_jadwal));
//            for (DetailRencana typeCustomer : dataTypeCustomer) {
//                rencanaDetailList.add(typeCustomer);
//                rencanaDetailStringList.add(String.valueOf(typeCustomer.getNomor_rencana_detail()));
//            }
//
//            //set value spinner rencana detail
//            ArrayAdapter<String> adapterRencanaDetail = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, rencanaDetailStringList);
//            adapterRencanaDetail.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinnerRencanaDetail.setAdapter(adapterRencanaDetail);
//            spinnerRencanaDetail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                public void onItemSelected(AdapterView<?> parent,
//                                           View view, int position, long id) {
//                    idrencanaDetail = rencanaDetailList.get(position).getId_rencana_detail();
//                    List<Mst_Customer> dataCluster = databaseHandler.getAllCustomer();
//                    ArrayList<DetailRencana> detren_list = databaseHandler.getAlldetailRencanaParam(idrencanaDetail);
//                    DetailRencana detrencana = new DetailRencana();
//                    for (DetailRencana a : detren_list)
//                        detrencana = a;
//                    int id_customer1 = detrencana.getId_customer();
//                    int index1 = 0;
//                    for (Mst_Customer customer : dataCluster) {
//                        if (customer.getId_customer() == id_customer1)
//                            break;
//                        index1 += 1;
//                    }
//                    spinnerCustomer.setSelection(index1);
//                }
//                public void onNothingSelected(AdapterView<?> parent) {
//                }
//            });
//
//            if(rencanaDetailList.isEmpty()){
//                idrencanaDetail = 0;
//            }else{
//                idrencanaDetail = rencanaDetailList.get(0).getId_rencana_detail();
//            }
//
//            //set list type customer
//            customerList = new ArrayList<Mst_Customer>();
//            customerStringList = new ArrayList<String>();
//            List<Mst_Customer> dataCustomer = databaseHandler.getAllCustomer();
//            for (Mst_Customer customer : dataCustomer) {
//                customerList.add(customer);
//                customerStringList.add(customer.getNama_customer());
//            }
//
//            //set value spinner customer
//            ArrayAdapter<String> adapterCustomer = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, customerStringList);
//            adapterCustomer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinnerCustomer.setAdapter(adapterCustomer);
//
//            spinnerCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                public void onItemSelected(AdapterView<?> parent,
//                                           View view, int position, long id) {
//                    id_customer = customerList.get(position).getId_customer();
//
//                    //auto set spinner
//                    mst_customer = databaseHandler.getCustomer(id_customer);
//                    String kode_customer=mst_customer.getKode_customer();
//                    List<DetailRencana> datadetailRencana = databaseHandler.getAllDetailRencanaNEW(Integer.parseInt(main_app_id_detail_jadwal));
//                    ArrayList<Mst_Customer> detren_list = databaseHandler.getAllCustomerParam(kode_customer);
//                    Mst_Customer detrencana = new Mst_Customer();
//                    for (Mst_Customer a : detren_list)
//                        detrencana = a;
//                    int id_customer1 = detrencana.getId_customer();
//                    int index1 = 0;
//                    for (DetailRencana customer : datadetailRencana) {
//                        if (customer.getId_customer() == id_customer1)
//                            break;
//                        index1 += 1;
//                    }
//                    spinnerRencanaDetail.setSelection(index1);
//
//                    //reset value foto & no checkin
//                    tvFotoCustomer.setText("");
//                    no_checkin1.setText("");
//                }
//                public void onNothingSelected(AdapterView<?> parent) {
//                }
//            });
//
//            if(customerList.isEmpty()){
//                id_customer = 0;
//            }else{
//                id_customer = customerList.get(0).getId_customer();
//            }
//
//        }else{
//            showCustomDialog("Download dencana detail terlebih dahulu");
//        }
//
//        foto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(id_customer!=0){
//                    gotoCaptureImage1();
//                }else{
//                    showCustomDialog("Pilih customer terlebih dahulu");
//                }
//            }
//        });
//        checkin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                checkinSave();
//            }
//        });
//
//        kanvassing.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //gotoKanvassing();
//            }
//        });
//
//        return view;

        act=this.getActivity();
//        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        databaseHandler = new DatabaseHandler(this.getActivity());
        spinnerRencanaDetail = (Spinner) view.findViewById(R.id.id_rencna_detail);
        spinnerRencanaMaster = (Spinner) view.findViewById(R.id.nomor_rencana_value);
        spinnerCustomer = (Spinner) view.findViewById(R.id.id_customer);
        tvFotoCustomer = (TextView) view.findViewById(R.id.title_foto_customer);
        foto = (Button) view.findViewById(R.id.foto);
        checkin = (Button) view.findViewById(R.id.checkin);
        kanvassing = (Button) view.findViewById(R.id.kanvassing);
        imgCust = (ImageView) view.findViewById(R.id.ImgCust);
        no_checkin1 = (EditText) view.findViewById(R.id.no_checkin);
        no_checkin1.setEnabled(false);

//        swipeRefreshLayout.setOnRefreshListener(this);

        if(databaseHandler.getCountDetailrencana()!=0){

            ArrayList<MstUser> staff_list = databaseHandler.getAllUser();
            user = new MstUser();

            for (MstUser tempStaff : staff_list)
                user = tempStaff;
            id_user=user.getId_user();

            //set list master rencana
            rencanaMasterList = new ArrayList<MasterRencana>();
            rencanaMasterStringList = new ArrayList<String>();
            List<MasterRencana> dataMasterRencana = databaseHandler.getAllMasterRencana();
            for (MasterRencana masterRencana : dataMasterRencana) {
                rencanaMasterList.add(masterRencana);
                rencanaMasterStringList.add(String.valueOf(masterRencana.getNomor_rencana()));
            }

            //set value spinner  master rencana
            ArrayAdapter<String> adapterRencanaMaster = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, rencanaMasterStringList);
            adapterRencanaMaster.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerRencanaMaster.setAdapter(adapterRencanaMaster);

            spinnerRencanaMaster.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent,
                                           View view, int position, long id) {
                    id_rencanaHeader = rencanaMasterList.get(position).getId_rencana_header();
                }
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            if(rencanaMasterList.isEmpty()){
                id_rencanaHeader = 0;
            }else {
                id_rencanaHeader = rencanaMasterList.get(0).getId_rencana_header();
            }

            //set list detail rencana
            rencanaDetailList = new ArrayList<DetailRencana>();
            rencanaDetailStringList = new ArrayList<String>();
            List<DetailRencana> dataTypeCustomer = databaseHandler.getAllDetailRencana();
            for (DetailRencana typeCustomer : dataTypeCustomer) {
                rencanaDetailList.add(typeCustomer);
                rencanaDetailStringList.add(String.valueOf(typeCustomer.getNomor_rencana_detail()));
            }

            //set value spinner rencana detail
            ArrayAdapter<String> adapterRencanaDetail = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, rencanaDetailStringList);
            adapterRencanaDetail.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerRencanaDetail.setAdapter(adapterRencanaDetail);
            spinnerRencanaDetail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent,
                                           View view, int position, long id) {
                    idrencanaDetail = rencanaDetailList.get(position).getId_rencana_detail();
                    List<Mst_Customer> dataCluster = databaseHandler.getAllCustomer();
                    ArrayList<DetailRencana> detren_list = databaseHandler.getAlldetailRencanaParam(idrencanaDetail);
                    DetailRencana detrencana = new DetailRencana();
                    for (DetailRencana a : detren_list)
                        detrencana = a;
                    int id_customer1 = detrencana.getId_customer();
                    int index1 = 0;
                    for (Mst_Customer customer : dataCluster) {
                        if (customer.getId_customer() == id_customer1)
                            break;
                        index1 += 1;
                    }
                    spinnerCustomer.setSelection(index1);
                }
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            if(rencanaDetailList.isEmpty()){
                idrencanaDetail = 0;
            }else{
                idrencanaDetail = rencanaDetailList.get(0).getId_rencana_detail();
            }

            //set list type customer
            customerList = new ArrayList<Mst_Customer>();
            customerStringList = new ArrayList<String>();
            List<Mst_Customer> dataCustomer = databaseHandler.getAllCustomer();
            for (Mst_Customer customer : dataCustomer) {
                customerList.add(customer);
                customerStringList.add(customer.getNama_customer());
            }

            //set value spinner customer
            ArrayAdapter<String> adapterCustomer = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, customerStringList);
            adapterCustomer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCustomer.setAdapter(adapterCustomer);

            spinnerCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent,
                                           View view, int position, long id) {
                    id_customer = customerList.get(position).getId_customer();

                    //auto set spinner
                    mst_customer = databaseHandler.getCustomer(id_customer);
                    String kode_customer=mst_customer.getKode_customer();
                    List<DetailRencana> datadetailRencana = databaseHandler.getAllDetailRencana();
                    ArrayList<Mst_Customer> detren_list = databaseHandler.getAllCustomerParam(kode_customer);
                    Mst_Customer detrencana = new Mst_Customer();
                    for (Mst_Customer a : detren_list)
                        detrencana = a;
                    int id_customer1 = detrencana.getId_customer();
                    int index1 = 0;
                    for (DetailRencana customer : datadetailRencana) {
                        if (customer.getId_customer() == id_customer1)
                            break;
                        index1 += 1;
                    }
                    spinnerRencanaDetail.setSelection(index1);

                    //reset value foto & no checkin
                    tvFotoCustomer.setText("");
                    no_checkin1.setText("");
                }
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            if(customerList.isEmpty()){
                id_customer = 0;
            }else{
                id_customer = customerList.get(0).getId_customer();
            }

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
            }
        });

        kanvassing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //gotoKanvassing();
            }
        });

        return  view;
    }

//    public class UploadData extends AsyncTask<String, Integer, String> {
//        ProgressDialog loading1;
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            loading1 = ProgressDialog.show(OneFragment1.this.getActivity(),"Mengunggah gambar...","Silahkan Tunggu...",false,false);
//            Log.d(LOG_TAG, "ok1");
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            Log.d(LOG_TAG, "ok2");
//            String url = AppVar.CONFIG_APP_URL_PUBLIC;
//            Log.d(LOG_TAG, "ok3");
//            String uploadSupplier = AppVar.CONFIG_APP_URL_UPLOAD_INSERT_CHECKIN;
//            Log.d(LOG_TAG, "ok4");
//            String upload_image_supplier_url = url + uploadSupplier;
//            Log.d(LOG_TAG, "ok5");
//            SharedPreferences spPreferences = getSharedPrefereces();
//            Log.d(LOG_TAG, "ok6");
//            String main_app_id_foto1 = spPreferences.getString(AppVar.SHARED_PREFERENCES_CHECKIN, null);
//            Log.d(LOG_TAG, "ok7");
//            /***********************
//             * Upload Image Supplier
//             */
//            response_data = uploadImage(
//                    upload_image_supplier_url,
//                    main_app_id_foto1);
//            Log.d(LOG_TAG, "Sukses");
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
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
//                            loading1.dismiss();
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
//        }
//    }
//
//    protected List<File> getListFiles(File parentDir) {
//        ArrayList<File> inFiles = new ArrayList<File>();
//        File[] files = parentDir.listFiles();
//        for (File file : files) {
//            inFiles.add(file);
//            if (file.isDirectory())
//                inFiles.addAll(getListFiles(file));
//        }
//        return inFiles;
//    }
//
//    public void extractUpload() {
//        JSONObject oResponse;
//        try {
//            oResponse = new JSONObject(response_data);
//            String status = oResponse.isNull("error") ? "True" : oResponse
//                    .getString("error");
//            if (response_data.isEmpty()) {
//                final String msg = act
//                        .getApplicationContext()
//                        .getResources()
//                        .getString(
//                                R.string.app_supplier_processing_failed);
//                showCustomDialog(msg);
//            } else {
//                Log.d(LOG_TAG, "status=" + status);
//                if (status.equalsIgnoreCase("False")) {
//                    saveAppSupplierFoto1("");
//
//                    File dir = new File(AppVar.getFolderPath() + "/"
//                            + AppVar.CONFIG_APP_FOLDER_CUSTOMER_PROSPECT);
//
//                    List<File> fileFoto = getListFiles(dir);
//                    for (File tempFile : fileFoto) {
//                        tempFile.delete();
//                    }
//
//                    final String msg = act
//                            .getApplicationContext()
//                            .getResources()
//                            .getString(
//                                    R.string.app_supplier_processing_sukses);
//                    showCustomDialogDownloadSuccess(msg);
//
//                } else {
//                    final String msg = act
//                            .getApplicationContext()
//                            .getResources()
//                            .getString(
//                                    R.string.app_supplier_processing_failed);
//                    showCustomDialog(msg);
//                }
//
//            }
//
//        } catch (JSONException e) {
//            final String message = e.toString();
//            showCustomDialog(message);
//
//        }
//    }
//
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
//
//                                gotoDetailJadwal();
//                            }
//                        });
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//
//    }
//
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
//
//    public void showCustomDialog(String msg) {
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
//
//                            }
//                        });
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//
//    }
//
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
//
//    public void checkinSave() {
//        locationManager = (LocationManager) getActivity().getApplicationContext()
//                .getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                1000L, 1.0f, locationListener);
//
//        boolean isGPSEnabled = locationManager
//                .isProviderEnabled(LocationManager.GPS_PROVIDER);
//        boolean isNetworkEnabled = locationManager
//                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//        if (!isGPSEnabled) {
//            startActivityForResult(new Intent(
//                    Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
//        }else{
//            if(isGPSEnabled){
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                        1000L, 1.0f, locationListener);
//                location2 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) ;
//                if (location2 != null){
//                    latitude=location2.getLatitude();
//                    longitude=location2.getLongitude();
//
//                    final String vlats=String.valueOf(latitude);
//                    final String vlongs=String.valueOf(longitude);
//
//                    //disini
//                    if (idrencanaDetail != 0 || id_customer != 0) {
//                        if (vlats.equalsIgnoreCase("0")
//                                || vlongs.equalsIgnoreCase("0")) {
//                            String msg = getActivity().getApplicationContext()
//                                    .getResources()
//                                    .getString(
//                                            R.string.MSG_DLG_LABEL_FAILED_CURRENT_GPS_DIALOG);
//                            showCustomDialog(msg);
//                        } else {
//                            if (tvFotoCustomer.getText().length()!=0) {
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
//                                    checkin.setId_rencana_header(id_rencanaHeader);
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
//                                    final String id_rencana_master_checkin = String.valueOf(id_rencanaHeader);
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
////                                            gotoDetailJadwal();
//                                            new UploadData().execute();
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
//                                            hashMap.put("id_rencana_header",id_rencana_master_checkin);
//                                            hashMap.put("kode_customer",kode_customer_checkin);
//                                            hashMap.put("lats",lats_checkin);
//                                            hashMap.put("longs",longs_checkin);
//                                            hashMap.put("foto",foto_checkin);
//
//                                            RequestHandler rh = new RequestHandler();
//                                            String res = rh.sendPostRequest(AppVar.POST_CHECKIN, hashMap);
//                                            return res;
//                                        }
//                                    }
//                                    insertToDatabase in = new insertToDatabase();
//                                    in.execute();
//                                }
//                            } else {
//                                String msg = getActivity().getApplicationContext()
//                                        .getResources()
//                                        .getString(
//                                                R.string.app_customer_checkin_save_failed_no_image);
//                                showCustomDialog(msg);
//                            }
//                        }
//                    } else {
//                        String msg = getActivity().getApplicationContext()
//                                .getResources().getString(R.string.app_checkin_incorrect_value);
//                        showCustomDialogCheckinSuccess(msg);
//                    }
//
//                }else{
//                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                            1000L, 1.0f, locationListener);
//                    location1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//
//                    if(location1 != null){
//                        latitude=location1.getLatitude();
//                        longitude=location1.getLongitude();
//                        final String vlats=String.valueOf(latitude);
//                        final String vlongs=String.valueOf(longitude);
//
//                        //disini
//                        if (idrencanaDetail != 0 || id_customer != 0) {
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
//                                    checkin.setId_rencana_header(id_rencanaHeader);
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
//                                    final String id_rencana_master_checkin = String.valueOf(id_rencanaHeader);
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
////                                            gotoDetailJadwal();
//                                            new UploadData().execute();
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
//                                            hashMap.put("id_rencana_header",id_rencana_master_checkin);
//                                            hashMap.put("kode_customer",kode_customer_checkin);
//                                            hashMap.put("lats",lats_checkin);
//                                            hashMap.put("longs",longs_checkin);
//                                            hashMap.put("foto",foto_checkin);
//
//                                            RequestHandler rh = new RequestHandler();
//                                            String res = rh.sendPostRequest(AppVar.POST_CHECKIN, hashMap);
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
//                        } else {
//                            String msg = getActivity().getApplicationContext()
//                                    .getResources().getString(R.string.app_checkin_incorrect_value);
//                            showCustomDialogCheckinSuccess(msg);
//                        }
//
//                    }else{
//                        showCustomDialog("Mohon tunggu, Aplikasi sedang membaca lokasi absen");
//                    }
//                }
//            }else if(isNetworkEnabled){
//                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                        1000L, 1.0f, locationListener);
//                location1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//
//                if(location1 != null){
//                    latitude=location1.getLatitude();
//                    longitude=location1.getLongitude();
//                    final String vlats=String.valueOf(latitude);
//                    final String vlongs=String.valueOf(longitude);
//
//                    //disini
//                    if (idrencanaDetail != 0 || id_customer != 0) {
//                        if (vlats.equalsIgnoreCase("0")
//                                || vlongs.equalsIgnoreCase("0")) {
//                            String msg = getActivity().getApplicationContext()
//                                    .getResources()
//                                    .getString(
//                                            R.string.MSG_DLG_LABEL_FAILED_CURRENT_GPS_DIALOG);
//                            showCustomDialog(msg);
//                        } else {
//                            if (tvFotoCustomer.getText().length()!=0) {
//                                int countId_checkin = databaseHandler.getCountTrxcheckin();
//                                final String date = "yyyy-MM-dd";
//                                Calendar calendar = Calendar.getInstance();
//                                SimpleDateFormat dateFormat = new SimpleDateFormat(date);
//                                final String checkDate = dateFormat.format(calendar.getTime());
//                                String kode_customer=mst_customer.getKode_customer();
//
//                                id_checkin=countId_checkin+1;
//
//
//                                Trx_Checkin checkin = new Trx_Checkin();
//                                //checkin.setId_checkin(id_checkin);
//                                checkin.setTanggal_checkin(checkDate);
//                                checkin.setNomor_checkin(no_checkin1.getText().toString());
//                                checkin.setId_user(id_user);
//                                checkin.setId_rencana_detail(idrencanaDetail);
//                                checkin.setId_rencana_header(id_rencanaHeader);
//                                checkin.setKode_customer(kode_customer);
//                                checkin.setLats(vlats);
//                                checkin.setLongs(vlongs);
//                                checkin.setFoto(tvFotoCustomer.getText().toString());
//                                checkin.setStatus("1");
//
//                                databaseHandler.addCheckin(checkin);
//
//                                final String tanggal_checkin_checkin = checkDate;
//                                final String nomor_checkin_checkin = no_checkin1.getText().toString();
//                                final String id_user_checkin_checkin = String.valueOf(id_user);
//                                final String id_rencana_detail_checkin = String.valueOf(idrencanaDetail);
//                                final String id_rencana_master_checkin = String.valueOf(id_rencanaHeader);
//                                final String kode_customer_checkin = kode_customer;
//                                final String lats_checkin = vlats;
//                                final String longs_checkin = vlongs;
//                                final String foto_checkin = tvFotoCustomer.getText().toString();
//
//                                class insertToDatabase extends AsyncTask<Void, Void, String> {
//                                    ProgressDialog loading;
//
//                                    @Override
//                                    protected void onPreExecute() {
//                                        super.onPreExecute();
//                                        loading = ProgressDialog.show(OneFragment1.this.getActivity(),"Proses...","Silahkan Tunggu...",false,false);
//                                    }
//
//                                    @Override
//                                    protected void onPostExecute(String s) {
//                                        super.onPostExecute(s);
//                                        loading.dismiss();
//                                        Toast.makeText(OneFragment1.this.getActivity(),"Checkin Berhasil !",Toast.LENGTH_LONG).show();
//                                        resetCheckin();
//                                    }
//
//                                    @Override
//                                    protected String doInBackground(Void... params) {
//                                        HashMap<String,String> hashMap  = new HashMap<>();
//                                        hashMap.put("tanggal_checkin",tanggal_checkin_checkin);
//                                        hashMap.put("nomor_checkin",nomor_checkin_checkin);
//                                        hashMap.put("id_user",id_user_checkin_checkin);
//                                        hashMap.put("id_rencana_detail",id_rencana_detail_checkin);
//                                        hashMap.put("id_rencana_header",id_rencana_master_checkin);
//                                        hashMap.put("kode_customer",kode_customer_checkin);
//                                        hashMap.put("lats",lats_checkin);
//                                        hashMap.put("longs",longs_checkin);
//                                        hashMap.put("foto",foto_checkin);
//
//                                        RequestHandler rh = new RequestHandler();
//                                        String res = rh.sendPostRequest(AppVar.POST_CHECKIN, hashMap);
//                                        return res;
//                                    }
//                                }
//                                insertToDatabase in = new insertToDatabase();
//                                in.execute();
//                            } else {
//                                String msg = getActivity().getApplicationContext()
//                                        .getResources()
//                                        .getString(
//                                                R.string.app_customer_checkin_save_failed_no_image);
//                                showCustomDialog(msg);
//                            }
//                        }
//                    } else {
//                        String msg = getActivity().getApplicationContext()
//                                .getResources().getString(R.string.app_checkin_incorrect_value);
//                        showCustomDialogCheckinSuccess(msg);
//                    }
//                }else{
//                    gotoDetailJadwal();
//                    Toast.makeText(this.getActivity(), "Buka google map pada handphone anda, lalu login ulang aplikasi absen online",Toast.LENGTH_LONG).show();
//                }
//            }else {
//                gotoDetailJadwal();
//                Toast.makeText(this.getActivity(), "Silahkan periksa lokasi anda dahulu",Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    protected String uploadImage(final String url, final String foto_1) {
//        Log.d(LOG_TAG, "ok8");
//        HttpClient httpclient = new DefaultHttpClient();
//        HttpPost httppost = new HttpPost(url);
//        String responseString = null;
//        Log.d(LOG_TAG, "ok9");
//        try {
//            if (android.os.Build.VERSION.SDK_INT > 9) {
//                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                        .permitAll().build();
//                StrictMode.setThreadPolicy(policy);
//            }
//            Log.d(LOG_TAG, "ok10");
//            MultipartEntity entity = new MultipartEntity();
//            File dir1 = new File(AppVar.getFolderPath() + "/"
//                    + AppVar.CONFIG_APP_FOLDER_CUSTOMER_PROSPECT + "/"
//                    + foto_1);
//            Log.d(LOG_TAG, "ok11");
//            if (dir1.exists() && foto_1 != null) {
//                entity.addPart("image_1", new FileBody(dir1));
//                entity.addPart("foto1", new StringBody(foto_1));
//                Log.d(LOG_TAG, "ok12a");
//            }else {
//                entity.addPart("foto1", new StringBody(""));
//                Log.d(LOG_TAG, "ok12");
//            }
//
//            httppost.setEntity(entity);
//            Log.d(LOG_TAG, String.valueOf(entity));
//            Log.d(LOG_TAG, "ok13");
//
//            // Making server call
//            HttpResponse response = httpclient.execute(httppost);
//            HttpEntity r_entity = response.getEntity();
//            Log.d(LOG_TAG, "ok14");
//
//            int statusCode = response.getStatusLine().getStatusCode();
//            if (statusCode == 200) {
//                // Server response
//                responseString = EntityUtils.toString(r_entity);
//                Log.d(LOG_TAG, "ok15");
//            } else {
//                responseString = "Error occurred! Http Status Code: "
//                        + statusCode;
//                Log.d(LOG_TAG, "ok15a");
//            }
//        } catch (ClientProtocolException e) {
//            responseString = e.toString();
//        } catch (IOException e) {
//            responseString = e.toString();
//        }
//        return responseString;
//    }
//
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
//
//    public void gotoCaptureImage1() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        fileUri1 = getOutputMediaFileUri1(MEDIA_TYPE_IMAGE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri1);
//        // start the image capture Intent
//        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
//    }
//
//    public void gotoCheckin(){
//        Intent intentActivity = new Intent(this.getActivity(),
//                IconTextTabsActivity.class);
//        startActivity(intentActivity);
//        getActivity().finish();
//    }
//
//    public void gotoDetailJadwal(){
//        Intent intentActivity = new Intent(this.getActivity(),
//                DetailJadwalActivity.class);
//        startActivity(intentActivity);
//        getActivity().finish();
//    }
//
////    @Override
////    public boolean onKeyDown(int keyCode, KeyEvent event) {
////        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
////            gotoDetailJadwal();
////            return true;
////        }
////        return super.onKeyDown(keyCode, event);
////    }
//
//    public void resetCheckin() {
//        spinnerRencanaDetail.setSelection(0);
//        spinnerCustomer.setSelection(0);
//        imgCust.setImageResource(R.drawable.avatar);
//    }
//
//    public Uri getOutputMediaFileUri1(int type) {
//        return Uri.fromFile(getOutputMediaFile1(type));
//    }
//
//    private SharedPreferences getSharedPrefereces() {
//        return act.getSharedPreferences(AppVar.SHARED_PREFERENCES_NAME,
//                Context.MODE_PRIVATE);
//    }
//
//    private File getOutputMediaFile1(int type) {
//
//        File dir = new File(AppVar.getFolderPath() + "/"
//                + AppVar.CONFIG_APP_FOLDER_CUSTOMER_PROSPECT);
//
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//        // Create a media file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
//                Locale.getDefault()).format(new Date());
//        //SharedPreferences spPreferences = getSharedPrefereces();
//        Mst_Customer mst_customer = databaseHandler.getCustomer(id_customer);
//        String kode_customer=mst_customer.getKode_customer();
//        //mediaFile;
//        if (type == MEDIA_TYPE_IMAGE) {
//            mediaFile = new File(dir.getPath() + File.separator
//                    + kode_customer +  "_"
//                    + "IMG_" + timeStamp + ".jpg");
//            newImageName1 = kode_customer + "_" + "IMG_" + timeStamp + ".jpg";
//
//        } else {
//            return null;
//        }
//        return mediaFile;
//    }
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // if the result is capturing Image
//        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                //Log.d(LOG_TAG, "take image success");
//                if (newImageName1 != null)
//                    tvFotoCustomer.setText(newImageName1);
//                saveAppSupplierFoto1(newImageName1);
//                generateNomercheckpoint();
//
//                int compressionRatio = 15; //1 == originalImage, 2 = 50% compression, 4=25% compress
//                try {
//                    Bitmap myBitmap = BitmapFactory.decodeFile(mediaFile.getAbsolutePath());
//                    myBitmap.compress (Bitmap.CompressFormat.JPEG, compressionRatio, new FileOutputStream(mediaFile));
//                    imgCust.setImageBitmap(myBitmap);
//                }
//                catch (Throwable t) {
//                    Log.e("ERROR", "Error compressing file." + t.toString ());
//                    t.printStackTrace ();
//                }
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                // user cancelled Image capture
//                Toast.makeText(getActivity().getApplicationContext(),"Batal mengambil foto terbaru!", Toast.LENGTH_SHORT).show();
//            } else {
//                // failed to capture image
//                Toast.makeText(getActivity().getApplicationContext(),
//                        "Failed to capture image", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    public void saveAppSupplierFoto1(String responsedata) {
//        SharedPreferences sp = getSharedPrefereces();
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString(AppVar.SHARED_PREFERENCES_CHECKIN,
//                responsedata);
//        editor.commit();
//    }
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
                                //idrenca naDetail = rencanaDetailList.get(0).getId_rencana_detail();
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
                            if (tvFotoCustomer.getText().length()!=0) {
                                int countId_checkin = databaseHandler.getCountTrxcheckin();
                                final String date = "yyyy-MM-dd";
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat dateFormat = new SimpleDateFormat(date);
                                final String checkDate = dateFormat.format(calendar.getTime());
                                String kode_customer=mst_customer.getKode_customer();
                                id_checkin=countId_checkin+1;

                                int already_checkin = databaseHandler.getCheckinAlready(kode_customer, idrencanaDetail);

                                if(already_checkin==1){
                                    ArrayList<Mst_Customer> customer_list = databaseHandler.getAllCustomerParam(kode_customer);
                                    Mst_Customer customer_param = new Mst_Customer();
                                    for (Mst_Customer customer : customer_list)
                                        customer_param = customer;

                                    String nama = customer_param.getNama_customer();

                                    showCustomDialog("Anda sudah checkin pada customer atas nama "+nama+" di hari yang sama");
                                }else{
                                    Trx_Checkin checkin = new Trx_Checkin();
                                    //checkin.setId_checkin(id_checkin);
                                    checkin.setTanggal_checkin(checkDate);
                                    checkin.setNomor_checkin(no_checkin1.getText().toString());
                                    checkin.setId_user(id_user);
                                    checkin.setId_rencana_detail(idrencanaDetail);
                                    checkin.setId_rencana_header(id_rencanaHeader);
                                    checkin.setKode_customer(kode_customer);
                                    checkin.setLats(vlats);
                                    checkin.setLongs(vlongs);
                                    checkin.setFoto(tvFotoCustomer.getText().toString());
                                    checkin.setStatus("1");

                                    databaseHandler.addCheckin(checkin);

                                    final String tanggal_checkin_checkin = checkDate;
                                    final String nomor_checkin_checkin = no_checkin1.getText().toString();
                                    final String id_user_checkin_checkin = String.valueOf(id_user);
                                    final String id_rencana_detail_checkin = String.valueOf(idrencanaDetail);
                                    final String id_rencana_master_checkin = String.valueOf(id_rencanaHeader);
                                    final String kode_customer_checkin = kode_customer;
                                    final String lats_checkin = vlats;
                                    final String longs_checkin = vlongs;
                                    final String foto_checkin = tvFotoCustomer.getText().toString();

                                    class insertToDatabase extends AsyncTask<Void, Void, String> {
                                        ProgressDialog loading;

                                        @Override
                                        protected void onPreExecute() {
                                            super.onPreExecute();
                                            loading = ProgressDialog.show(OneFragment1.this.getActivity(),"Proses...","Silahkan Tunggu...",false,false);
                                        }

                                        @Override
                                        protected void onPostExecute(String s) {
                                            super.onPostExecute(s);
                                            loading.dismiss();
                                            Toast.makeText(OneFragment1.this.getActivity(),"Checkin Berhasil !",Toast.LENGTH_LONG).show();
                                            //resetCheckin();
                                            databaseHandler.updateCheckin(id_rencana_detail_checkin);
                                            gotoDashboard();
                                            Log.d(LOG_TAG, "sampe sini");
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
                                    insertToDatabase in = new insertToDatabase();
                                    in.execute();
                                }
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
                                if (tvFotoCustomer.getText().length()!=0) {
                                    int countId_checkin = databaseHandler.getCountTrxcheckin();
                                    final String date = "yyyy-MM-dd";
                                    Calendar calendar = Calendar.getInstance();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat(date);
                                    final String checkDate = dateFormat.format(calendar.getTime());
                                    String kode_customer=mst_customer.getKode_customer();

                                    id_checkin=countId_checkin+1;


                                    Trx_Checkin checkin = new Trx_Checkin();
                                    //checkin.setId_checkin(id_checkin);
                                    checkin.setTanggal_checkin(checkDate);
                                    checkin.setNomor_checkin(no_checkin1.getText().toString());
                                    checkin.setId_user(id_user);
                                    checkin.setId_rencana_detail(idrencanaDetail);
                                    checkin.setId_rencana_header(id_rencanaHeader);
                                    checkin.setKode_customer(kode_customer);
                                    checkin.setLats(vlats);
                                    checkin.setLongs(vlongs);
                                    checkin.setFoto(tvFotoCustomer.getText().toString());
                                    checkin.setStatus("1");

                                    databaseHandler.addCheckin(checkin);

                                    final String tanggal_checkin_checkin = checkDate;
                                    final String nomor_checkin_checkin = no_checkin1.getText().toString();
                                    final String id_user_checkin_checkin = String.valueOf(id_user);
                                    final String id_rencana_detail_checkin = String.valueOf(idrencanaDetail);
                                    final String id_rencana_master_checkin = String.valueOf(id_rencanaHeader);
                                    final String kode_customer_checkin = kode_customer;
                                    final String lats_checkin = vlats;
                                    final String longs_checkin = vlongs;
                                    final String foto_checkin = tvFotoCustomer.getText().toString();

                                    class insertToDatabase extends AsyncTask<Void, Void, String> {
                                        ProgressDialog loading;

                                        @Override
                                        protected void onPreExecute() {
                                            super.onPreExecute();
                                            loading = ProgressDialog.show(OneFragment1.this.getActivity(),"Proses...","Silahkan Tunggu...",false,false);
                                        }

                                        @Override
                                        protected void onPostExecute(String s) {
                                            super.onPostExecute(s);
                                            loading.dismiss();
                                            Toast.makeText(OneFragment1.this.getActivity(),"Checkin Berhasil !",Toast.LENGTH_LONG).show();
                                            resetCheckin();
                                            //gotoCheckin();
                                            gotoDashboard();
                                            Log.d(LOG_TAG, "sampe sini 1");
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
                                    insertToDatabase in = new insertToDatabase();
                                    in.execute();
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
                            if (tvFotoCustomer.getText().length()!=0) {
                                int countId_checkin = databaseHandler.getCountTrxcheckin();
                                final String date = "yyyy-MM-dd";
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat dateFormat = new SimpleDateFormat(date);
                                final String checkDate = dateFormat.format(calendar.getTime());
                                String kode_customer=mst_customer.getKode_customer();

                                id_checkin=countId_checkin+1;


                                Trx_Checkin checkin = new Trx_Checkin();
                                //checkin.setId_checkin(id_checkin);
                                checkin.setTanggal_checkin(checkDate);
                                checkin.setNomor_checkin(no_checkin1.getText().toString());
                                checkin.setId_user(id_user);
                                checkin.setId_rencana_detail(idrencanaDetail);
                                checkin.setId_rencana_header(id_rencanaHeader);
                                checkin.setKode_customer(kode_customer);
                                checkin.setLats(vlats);
                                checkin.setLongs(vlongs);
                                checkin.setFoto(tvFotoCustomer.getText().toString());
                                checkin.setStatus("1");

                                databaseHandler.addCheckin(checkin);

                                final String tanggal_checkin_checkin = checkDate;
                                final String nomor_checkin_checkin = no_checkin1.getText().toString();
                                final String id_user_checkin_checkin = String.valueOf(id_user);
                                final String id_rencana_detail_checkin = String.valueOf(idrencanaDetail);
                                final String id_rencana_master_checkin = String.valueOf(id_rencanaHeader);
                                final String kode_customer_checkin = kode_customer;
                                final String lats_checkin = vlats;
                                final String longs_checkin = vlongs;
                                final String foto_checkin = tvFotoCustomer.getText().toString();

                                class insertToDatabase extends AsyncTask<Void, Void, String> {
                                    ProgressDialog loading;

                                    @Override
                                    protected void onPreExecute() {
                                        super.onPreExecute();
                                        loading = ProgressDialog.show(OneFragment1.this.getActivity(),"Proses...","Silahkan Tunggu...",false,false);
                                    }

                                    @Override
                                    protected void onPostExecute(String s) {
                                        super.onPostExecute(s);
                                        loading.dismiss();
                                        Toast.makeText(OneFragment1.this.getActivity(),"Checkin Berhasil !",Toast.LENGTH_LONG).show();
                                        resetCheckin();
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
                                insertToDatabase in = new insertToDatabase();
                                in.execute();
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

    protected String uploadImage(final String url,  final String id_staff,
                                 final String type_penjualan, final String no_penjualan,
                                 final String nama_customer, final String alamat,
                                 final String keterangan, final String lats,
                                 final String longs,final String foto1,
                                 final String foto2,final String ttd) {
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
                    + IMAGE_DIRECTORY_NAME + "/"+ foto1);
            if (dir1.exists() && foto1 != null) {
                entity.addPart("foto1", new FileBody(dir1));
                entity.addPart("foto1", new StringBody(foto1));
            }
            else {
                entity.addPart("foto1", new StringBody(""));
            }


            File dir2 = new File(AppVar.getFolderPath() + "/"
                    + IMAGE_DIRECTORY_NAME + "/"
                    + foto2);
            if (dir2.exists() && foto2 != null) {
                entity.addPart("foto2", new FileBody(dir2));
                entity.addPart("foto2", new StringBody(foto2));
            }
            else {
                entity.addPart("foto2", new StringBody(""));
            }

            File dir3 = new File(AppVar.getFolderPath() + "/"
                    + IMAGE_DIRECTORY_NAME + "/"
                    + ttd);
            if (dir3.exists() && ttd != null) {
                entity.addPart("ttd", new FileBody(dir3));
                entity.addPart("ttd", new StringBody(ttd));
            }
            else {
                entity.addPart("ttd", new StringBody(""));
            }

            entity.addPart("id_staff", new StringBody(id_staff));
            entity.addPart("type_penjualan", new StringBody(type_penjualan));
            entity.addPart("no_penjualan", new StringBody(no_penjualan));
            entity.addPart("nama_customer", new StringBody(nama_customer));
            entity.addPart("alamat", new StringBody(alamat));
            entity.addPart("keterangan", new StringBody(keterangan));
            entity.addPart("lats", new StringBody(lats));
            entity.addPart("longs", new StringBody(longs));

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

    public void gotoCheckin(){
        Intent intentActivity = new Intent(this.getActivity(),
                IconTextTabsActivity.class);
        startActivity(intentActivity);
        getActivity().finish();
    }

    public void gotoDashboard(){
        Intent intentActivity = new Intent(this.getActivity(),
                DashboardActivity.class);
        startActivity(intentActivity);
        getActivity().finish();
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
                    tvFotoCustomer.setText(newImageName1);
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

    /*
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
    */
}
