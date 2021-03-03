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
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pir.gglc.absen.AppVar;
import com.android.pir.gglc.database.DataSapi;
import com.android.pir.gglc.database.DatabaseHandler;
import com.android.pir.gglc.database.DetailRencana;
import com.android.pir.gglc.database.MstUser;
import com.android.pir.gglc.database.Mst_Customer;
import com.android.pir.gglc.database.Trx_Checkin;
import com.android.pir.gglc.database.UploadDataSapi;
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
import java.util.List;
import java.util.Locale;

import static com.android.pir.gglc.absen.AppVar.SHARED_PREFERENCES_NAME;

//import com.android.canvasing.gglc.canvassing.SalesKanvasActivity;


//public class OneFragment1 extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
public class DataSapiFragment extends Fragment{
    Context thiscontext;
    private DatabaseHandler databaseHandler;
    private Context act;
    private ProgressDialog pDialog;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog;
    private String message,aertag_value,aertag_value1,aertag_value2,aertag_value3;
    protected LocationManager locationManager;
    private int idrencanaDetail = 0;
    private int nilai_ass= 0;

    private Button foto,checkin,foto1,foto2,foto3;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private Uri fileUri,fileUri1,fileUri2,fileUri3; // file url to store image/video
    private File mediaFile,mediaFile1,mediaFile2,mediaFile3;
    private String newImageName,newImageName1,newImageName2,newImageName3;
    private String response_data,response_data_download,main_app_id_detail_jadwal;
    private TextView tvFotoCustomer,tvnama_petani,tvalamat_petani,tvstatus_checkin,tvFotoCustomer1,tvFotoCustomer2,tvFotoCustomer3;
    private ImageView imgCust,imgCust1,imgCust2,imgCust3;
    private EditText edketerangan,edketerangan1,edketerangan2,edketerangan3;
    private Spinner edeartag,edeartag1,edeartag2,edeartag3;
    private Mst_Customer mst_customer;
    private double latitude, longitude;
    private CheckBox assesment;
    private String IMAGE_DIRECTORY_NAME = "Data_sapi";
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    private static final String LOG_TAG = DataSapiFragment.class.getSimpleName();
    private int id_checkin,id_user,id_customer;
    private MstUser user;
    private DetailRencana rencanaDetail;
    private String idrd,idcst,fto,nmr,lts,lng,idus,tgl,kode_customer,status_checkin;
    private LinearLayout lsstatus;
    private ArrayList<DataSapi> eartagList;
    private ArrayList<DataSapi> eartagList1;
    private ArrayList<DataSapi> eartagList2;
    private ArrayList<DataSapi> eartagList3;
    private ArrayList<String> eartagStringList;
    public DataSapiFragment() {
        //no coding in here
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pir_fragment_data_sapi, container, false);
        act=this.getActivity();

        progressDialog = new ProgressDialog(this.getActivity());
        progressDialog.setTitle(getActivity().getApplicationContext().getResources()
                .getString(R.string.app_name));
        progressDialog.setMessage("Downloading yaa...");
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);

        databaseHandler = new DatabaseHandler(this.getActivity());
        tvFotoCustomer = (TextView) view.findViewById(R.id.foto_sapi);
        tvFotoCustomer1 = (TextView) view.findViewById(R.id.foto_sapi1);
        tvFotoCustomer2 = (TextView) view.findViewById(R.id.foto_sapi2);
        tvFotoCustomer3 = (TextView) view.findViewById(R.id.foto_sapi3);
        tvnama_petani = (TextView) view.findViewById(R.id.nama_petani);
        tvalamat_petani = (TextView) view.findViewById(R.id.alamat_pertani);
        tvstatus_checkin = (TextView) view.findViewById(R.id.status_checkin);
        lsstatus = (LinearLayout) view.findViewById(R.id.lstatus_checkin);
        assesment = (CheckBox)view.findViewById(R.id.assessment);
        LinearLayout linAll = (LinearLayout)view.findViewById(R.id.linAll);
        foto = (Button) view.findViewById(R.id.foto);
        foto1 = (Button) view.findViewById(R.id.foto1);
        foto2 = (Button) view.findViewById(R.id.foto2);
        foto3 = (Button) view.findViewById(R.id.foto3);
        checkin = (Button) view.findViewById(R.id.checkin);
        imgCust = (ImageView) view.findViewById(R.id.ImgCust);
        imgCust1 = (ImageView) view.findViewById(R.id.ImgCust1);
        imgCust2 = (ImageView) view.findViewById(R.id.ImgCust2);
        imgCust3 = (ImageView) view.findViewById(R.id.ImgCust3);
        edeartag = (Spinner) view.findViewById(R.id.eartag);
        edeartag1 = (Spinner) view.findViewById(R.id.eartag1);
        edeartag2 = (Spinner) view.findViewById(R.id.eartag2);
        edeartag3 = (Spinner) view.findViewById(R.id.eartag3);
        edketerangan = (EditText) view.findViewById(R.id.keterangan);
        edketerangan1 = (EditText) view.findViewById(R.id.keterangan1);
        edketerangan2 = (EditText) view.findViewById(R.id.keterangan2);
        edketerangan3 = (EditText) view.findViewById(R.id.keterangan3);
        SharedPreferences spPreferences = getSharedPrefereces();
        idrencanaDetail = Integer.parseInt(spPreferences.getString(AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_JADWAL, null));
//        status_checkin = spPreferences.getString(AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_STATUS, null);

        ArrayList<DetailRencana> rencana_list = databaseHandler.getAlldetailRencanaParam(idrencanaDetail);
        rencanaDetail = new DetailRencana();
        for (DetailRencana detailRencana : rencana_list)
            rencanaDetail = detailRencana;
        id_customer=rencanaDetail.getId_customer();
        status_checkin = String.valueOf(rencanaDetail.getStatus_rencana());
        Log.d(LOG_TAG, "id_customer: "+id_customer);

        //set list detail rencana
        eartagList = new ArrayList<DataSapi>();
        eartagStringList = new ArrayList<String>();
        List<DataSapi> dataSapi = databaseHandler.getAllDataSapiParam(String.valueOf(id_customer));
        for (DataSapi sapi : dataSapi) {
            eartagList.add(sapi);
            eartagStringList.add(String.valueOf(sapi.getVistgid()));
        }
        //set value spinner eartag
        ArrayAdapter<String> adapterEartag = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, eartagStringList);
        adapterEartag.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edeartag.setAdapter(adapterEartag);
        edeartag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                aertag_value = eartagList.get(position).getVistgid();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if(eartagList.isEmpty()){
            aertag_value = "";
        }else{
            aertag_value = eartagList.get(0).getVistgid();
        }

        //set list detail rencana1
        eartagList1 = new ArrayList<DataSapi>();
        for (DataSapi sapi : dataSapi) {
            eartagList1.add(sapi);
        }
        //set value spinner eartag1
        edeartag1.setAdapter(adapterEartag);
        edeartag1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                aertag_value1 = eartagList1.get(position).getVistgid();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if(eartagList1.isEmpty()){
            aertag_value1 = "";
        }else{
            aertag_value1 = eartagList1.get(0).getVistgid();
        }

        //set list detail rencana2
        eartagList2 = new ArrayList<DataSapi>();
        for (DataSapi sapi : dataSapi) {
            eartagList2.add(sapi);
        }
        //set value spinner eartag2
        edeartag2.setAdapter(adapterEartag);
        edeartag2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                aertag_value2 = eartagList2.get(position).getVistgid();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if(eartagList2.isEmpty()){
            aertag_value2 = "";
        }else{
            aertag_value2 = eartagList2.get(0).getVistgid();
        }

        //set list detail rencana3
        eartagList3 = new ArrayList<DataSapi>();
        for (DataSapi sapi : dataSapi) {
            eartagList3.add(sapi);
        }
        //set value spinner eartag3
        edeartag3.setAdapter(adapterEartag);
        edeartag3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                aertag_value3 = eartagList3.get(position).getVistgid();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if(eartagList3.isEmpty()){
            aertag_value3 = "";
        }else{
            aertag_value3 = eartagList3.get(0).getVistgid();
        }


        if(status_checkin.equals("0")){
            linAll.setEnabled(false);
            showCustomDialog("Anda Belum Melakukan checkin, silahkan lakukan checkin terlebih dahulu.");
            checkin.setClickable(false);
        }

        assesment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(assesment.isChecked()){
                    nilai_ass=1;
                }else{
                    nilai_ass=0;
                }
            }
        });
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
                if(edeartag.getSelectedItem().equals("Pilih Eartag")){
                    Toast.makeText(getActivity().getApplicationContext(), "Pilih Eartag Terlebih Dahulu",Toast.LENGTH_LONG).show();
                }else{
                    gotoCaptureImage();
                }
            }
        });
        foto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edeartag1.getSelectedItem().equals("Pilih Eartag")){
                    Toast.makeText(getActivity().getApplicationContext(), "Pilih Eartag Terlebih Dahulu",Toast.LENGTH_LONG).show();
                }else{
                    if(tvFotoCustomer.getText().toString().equals("")){
                        Toast.makeText(getActivity().getApplicationContext(),"Sebelum mengambil foto 2, ambil foto 1 terlebih dahulu",Toast.LENGTH_LONG).show();
                    }else{
                        gotoCaptureImage1();
                    }
                }
            }
        });
        foto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edeartag2.getSelectedItem().equals("Pilih Eartag")){
                    Toast.makeText(getActivity().getApplicationContext(), "Pilih Eartag Terlebih Dahulu",Toast.LENGTH_LONG).show();
                }else{
                    if(tvFotoCustomer1.getText().toString().equals("")){
                        Toast.makeText(getActivity().getApplicationContext(),"Sebelum mengambil foto 3, ambil foto 2 terlebih dahulu",Toast.LENGTH_LONG).show();
                    }else{
                        gotoCaptureImage2();
                    }
                }
            }
        });
        foto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edeartag3.getSelectedItem().equals("Pilih Eartag")){
                    Toast.makeText(getActivity().getApplicationContext(), "Pilih Eartag Terlebih Dahulu",Toast.LENGTH_LONG).show();
                }else{
                    if(tvFotoCustomer2.getText().toString().equals("")){
                        Toast.makeText(getActivity().getApplicationContext(),"Sebelum mengambil foto 4, ambil foto 3 terlebih dahulu",Toast.LENGTH_LONG).show();
                    }else{
                        gotoCaptureImage3();
                    }
                }
            }
        });
        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAppEartag(aertag_value);
                saveAppEartag1(aertag_value1);
                saveAppEartag2(aertag_value2);
                saveAppEartag3(aertag_value3);
                saveAppKeterangan(edketerangan.getText().toString());
                saveAppKeterangan1(edketerangan1.getText().toString());
                saveAppKeterangan2(edketerangan2.getText().toString());
                saveAppKeterangan3(edketerangan3.getText().toString());
                saveAppAssessment(String.valueOf(nilai_ass));
                if(tvFotoCustomer.getText().toString()==""){
                    showCustomDialog("Belum ada foto sapi yang di ambil");
                }else{
                    new UploadData().execute();
                }
            }
        });
//        foto1.setClickable(false);
//        foto2.setClickable(false);
//        foto3.setClickable(false);
//        tvFotoCustomer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//
//            }
//        });
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
    public void showCustomDialogImg(String msg) {
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
            String url = AppVar.CONFIG_APP_URL_PUBLIC;
            String uploadSupplier = AppVar.CONFIG_APP_URL_UPLOAD_INSERT_DATA_SAPI;
            String upload_image_supplier_url = url + uploadSupplier;
            SharedPreferences spPreferences = getSharedPrefereces();
            String foto = spPreferences.getString(AppVar.SHARED_PREFERENCES_PIR_FOTO_1, "");
            String foto1 = spPreferences.getString(AppVar.SHARED_PREFERENCES_PIR_FOTO_2, "");
            String foto2 = spPreferences.getString(AppVar.SHARED_PREFERENCES_PIR_FOTO_3, "");
            String foto3 = spPreferences.getString(AppVar.SHARED_PREFERENCES_PIR_FOTO_4, "");
            String data_eartag = spPreferences.getString(AppVar.SHARED_PREFERENCES_EARTAG, "");
            String data_eartag1 = spPreferences.getString(AppVar.SHARED_PREFERENCES_EARTAG1, "");
            String data_eartag2 = spPreferences.getString(AppVar.SHARED_PREFERENCES_EARTAG2, "");
            String data_eartag3 = spPreferences.getString(AppVar.SHARED_PREFERENCES_EARTAG3, "");
            String data_keterangan = spPreferences.getString(AppVar.SHARED_PREFERENCES_KETERANGAN, "");
            String data_keterangan1 = spPreferences.getString(AppVar.SHARED_PREFERENCES_KETERANGAN1, "");
            String data_keterangan2 = spPreferences.getString(AppVar.SHARED_PREFERENCES_KETERANGAN2, "");
            String data_keterangan3 = spPreferences.getString(AppVar.SHARED_PREFERENCES_KETERANGAN3, "");
            String data_assessment = spPreferences.getString(AppVar.SHARED_PREFERENCES_ASSESSMENT, "");
            /***********************
             * Upload Image Supplier
             */
            if(!foto3.equals("")){
//                response_data = uploadImageAll(upload_image_supplier_url,String.valueOf(idrencanaDetail),data_assessment,data_eartag,data_keterangan,foto,data_eartag1,data_keterangan1,foto1,data_eartag2,data_keterangan2,foto2,data_eartag3,data_keterangan3,foto3);
                SaveuploadImageAll(String.valueOf(idrencanaDetail),data_assessment,data_eartag,data_keterangan,foto,data_eartag1,data_keterangan1,foto1,data_eartag2,data_keterangan2,foto2,data_eartag3,data_keterangan3,foto3);
            }else if(!foto2.equals("")){
//                response_data = uploadImage3(upload_image_supplier_url,String.valueOf(idrencanaDetail),data_assessment,data_eartag,data_keterangan,foto,data_eartag1,data_keterangan1,foto1,data_eartag2,data_keterangan2,foto2);
                SaveuploadImage3(String.valueOf(idrencanaDetail),data_assessment,data_eartag,data_keterangan,foto,data_eartag1,data_keterangan1,foto1,data_eartag2,data_keterangan2,foto2);
            }else if(!foto1.equals("")){
//                response_data = uploadImage2(upload_image_supplier_url,String.valueOf(idrencanaDetail),data_assessment,data_eartag,data_keterangan,foto,data_eartag1,data_keterangan1,foto1);
                SaveuploadImage2(String.valueOf(idrencanaDetail),data_assessment,data_eartag,data_keterangan,foto,data_eartag1,data_keterangan1,foto1);
            }else{
//                response_data = uploadImage1(upload_image_supplier_url,String.valueOf(idrencanaDetail),data_assessment,data_eartag,data_keterangan,foto);
                SaveuploadImage1(String.valueOf(idrencanaDetail),data_assessment,data_eartag,data_keterangan,foto);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            int cekDS = databaseHandler.getCountUploadDataSapi(idrencanaDetail);
            if(cekDS > 0){
                saveAppSupplierFoto("");
                saveAppSupplierFoto1("");
                saveAppSupplierFoto2("");
                saveAppSupplierFoto3("");
                saveAppEartag("");
                saveAppEartag1("");
                saveAppEartag2("");
                saveAppEartag3("");
                saveAppKeterangan("");
                saveAppKeterangan1("");
                saveAppKeterangan2("");
                saveAppKeterangan3("");
                saveAppAssessment("");
//                File dir = new File(AppVar.getFolderPath() + "/"
//                        + IMAGE_DIRECTORY_NAME);
//                List<File> fileFoto = getListFiles(dir);
//                for (File tempFile : fileFoto) {
//                    tempFile.delete();
//                }
                showCustomDialogImg("Simpan Data Sapi Berhasil.");
            }else{
                showCustomDialog("Data Sapi Gagal Disimpan.");
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

    protected String uploadImage1(final String url, final String id_rencana_detail, final String data_assessment,final String data_eartag, final String data_keterangan,
                                 final String foto) {
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
            File dir = new File(AppVar.getFolderPath() + "/"+ IMAGE_DIRECTORY_NAME + "/"+ foto);
            if (dir.exists() && foto != null) {
                entity.addPart("image_1", new FileBody(dir));
                entity.addPart("foto1", new StringBody(foto));
            } else {
                entity.addPart("foto1", new StringBody(""));
            }
            entity.addPart("id_rencana_detail", new StringBody(id_rencana_detail));
            entity.addPart("assessment", new StringBody(data_assessment));
            entity.addPart("eratag1", new StringBody(data_eartag));
            entity.addPart("keterangan1", new StringBody(data_keterangan));
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

    protected String uploadImage2(final String url, final String id_rencana_detail, final String data_assessment,final String data_eartag, final String data_keterangan,
                                 final String foto, final String data_eartag1, final String data_keterangan1, final String foto1) {
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
            File dir = new File(AppVar.getFolderPath() + "/"+ IMAGE_DIRECTORY_NAME + "/"+ foto);
            File dir1 = new File(AppVar.getFolderPath() + "/"+ IMAGE_DIRECTORY_NAME + "/"+ foto1);
            if (dir.exists() && foto != null) {
                entity.addPart("image_1", new FileBody(dir));
                entity.addPart("foto1", new StringBody(foto));
            } else {
                entity.addPart("foto1", new StringBody(""));
            }
            if (dir1.exists() && foto1 != null) {
                entity.addPart("image_2", new FileBody(dir1));
                entity.addPart("foto2", new StringBody(foto1));
            } else {
                entity.addPart("foto2", new StringBody(""));
            }
            entity.addPart("id_rencana_detail", new StringBody(id_rencana_detail));
            entity.addPart("assessment", new StringBody(data_assessment));
            entity.addPart("eratag1", new StringBody(data_eartag));
            entity.addPart("keterangan1", new StringBody(data_keterangan));
            entity.addPart("eratag2", new StringBody(data_eartag1));
            entity.addPart("keterangan2", new StringBody(data_keterangan1));
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

    protected String uploadImage3(final String url, final String id_rencana_detail, final String data_assessment,final String data_eartag, final String data_keterangan,
                                 final String foto, final String data_eartag1, final String data_keterangan1, final String foto1,
                                 final String data_eartag2, final String data_keterangan2, final String foto2) {
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
            File dir = new File(AppVar.getFolderPath() + "/"+ IMAGE_DIRECTORY_NAME + "/"+ foto);
            File dir1 = new File(AppVar.getFolderPath() + "/"+ IMAGE_DIRECTORY_NAME + "/"+ foto1);
            File dir2 = new File(AppVar.getFolderPath() + "/"+ IMAGE_DIRECTORY_NAME + "/"+ foto2);
            if (dir.exists() && foto != null) {
                entity.addPart("image_1", new FileBody(dir));
                entity.addPart("foto1", new StringBody(foto));
            } else {
                entity.addPart("foto1", new StringBody(""));
            }
            if (dir1.exists() && foto1 != null) {
                entity.addPart("image_2", new FileBody(dir1));
                entity.addPart("foto2", new StringBody(foto1));
            } else {
                entity.addPart("foto2", new StringBody(""));
            }
            if (dir2.exists() && foto2 != null) {
                entity.addPart("image_3", new FileBody(dir2));
                entity.addPart("foto3", new StringBody(foto2));
            } else {
                entity.addPart("foto3", new StringBody(""));
            }
            entity.addPart("id_rencana_detail", new StringBody(id_rencana_detail));
            entity.addPart("assessment", new StringBody(data_assessment));
            entity.addPart("eratag1", new StringBody(data_eartag));
            entity.addPart("keterangan1", new StringBody(data_keterangan));
            entity.addPart("eratag2", new StringBody(data_eartag1));
            entity.addPart("keterangan2", new StringBody(data_keterangan1));
            entity.addPart("eratag3", new StringBody(data_eartag2));
            entity.addPart("keterangan3", new StringBody(data_keterangan2));
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

    protected String uploadImageAll(final String url, final String id_rencana_detail, final String data_assessment,final String data_eartag, final String data_keterangan,
                                 final String foto, final String data_eartag1, final String data_keterangan1, final String foto1,
                                 final String data_eartag2, final String data_keterangan2, final String foto2, final String data_eartag3,
                                 final String data_keterangan3, final String foto3) {
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
            File dir = new File(AppVar.getFolderPath() + "/"+ IMAGE_DIRECTORY_NAME + "/"+ foto);
            File dir1 = new File(AppVar.getFolderPath() + "/"+ IMAGE_DIRECTORY_NAME + "/"+ foto1);
            File dir2 = new File(AppVar.getFolderPath() + "/"+ IMAGE_DIRECTORY_NAME + "/"+ foto2);
            File dir3 = new File(AppVar.getFolderPath() + "/"+ IMAGE_DIRECTORY_NAME + "/"+ foto3);
            if (dir.exists() && foto != null) {
                entity.addPart("image_1", new FileBody(dir));
                entity.addPart("foto1", new StringBody(foto));
            } else {
                entity.addPart("foto1", new StringBody(""));
            }
            if (dir1.exists() && foto1 != null) {
                entity.addPart("image_2", new FileBody(dir1));
                entity.addPart("foto2", new StringBody(foto1));
            } else {
                entity.addPart("foto2", new StringBody(""));
            }
            if (dir2.exists() && foto2 != null) {
                entity.addPart("image_3", new FileBody(dir2));
                entity.addPart("foto3", new StringBody(foto2));
            } else {
                entity.addPart("foto3", new StringBody(""));
            }
            if (dir3.exists() && foto3 != null) {
                entity.addPart("image_4", new FileBody(dir3));
                entity.addPart("foto4", new StringBody(foto3));
            } else {
                entity.addPart("foto4", new StringBody(""));
            }
            entity.addPart("id_rencana_detail", new StringBody(id_rencana_detail));
            entity.addPart("assessment", new StringBody(data_assessment));
            entity.addPart("eratag1", new StringBody(data_eartag));
            entity.addPart("keterangan1", new StringBody(data_keterangan));
            entity.addPart("eratag2", new StringBody(data_eartag1));
            entity.addPart("keterangan2", new StringBody(data_keterangan1));
            entity.addPart("eratag3", new StringBody(data_eartag2));
            entity.addPart("keterangan3", new StringBody(data_keterangan2));
            entity.addPart("eratag4", new StringBody(data_eartag3));
            entity.addPart("keterangan4", new StringBody(data_keterangan3));
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

    private void SaveuploadImageAll(final String id_rencana_detail, final String data_assessment,final String data_eartag, final String data_keterangan,
                                 final String foto, final String data_eartag1, final String data_keterangan1, final String foto1,
                                 final String data_eartag2, final String data_keterangan2, final String foto2, final String data_eartag3,
                                 final String data_keterangan3, final String foto3) {
        final String date = "yyyy-MM-dd HH:mm:ss";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(date);
        final String checkDate = dateFormat.format(calendar.getTime());
        UploadDataSapi uploadDataSapi = new UploadDataSapi();
        uploadDataSapi.setId_rencana_detail(id_rencana_detail);
        uploadDataSapi.setEartag(data_eartag);
        uploadDataSapi.setFoto(foto);
        uploadDataSapi.setKeterangan(data_keterangan);
        uploadDataSapi.setAssessment(data_assessment);
        uploadDataSapi.setTanggal(checkDate);
        databaseHandler.addUploadDataSapi(uploadDataSapi);

        uploadDataSapi.setId_rencana_detail(id_rencana_detail);
        uploadDataSapi.setEartag(data_eartag1);
        uploadDataSapi.setFoto(foto1);
        uploadDataSapi.setKeterangan(data_keterangan1);
        uploadDataSapi.setAssessment(data_assessment);
        uploadDataSapi.setTanggal(checkDate);
        databaseHandler.addUploadDataSapi(uploadDataSapi);

        uploadDataSapi.setId_rencana_detail(id_rencana_detail);
        uploadDataSapi.setEartag(data_eartag2);
        uploadDataSapi.setFoto(foto2);
        uploadDataSapi.setKeterangan(data_keterangan2);
        uploadDataSapi.setAssessment(data_assessment);
        uploadDataSapi.setTanggal(checkDate);
        databaseHandler.addUploadDataSapi(uploadDataSapi);

        uploadDataSapi.setId_rencana_detail(id_rencana_detail);
        uploadDataSapi.setEartag(data_eartag3);
        uploadDataSapi.setFoto(foto3);
        uploadDataSapi.setKeterangan(data_keterangan3);
        uploadDataSapi.setAssessment(data_assessment);
        uploadDataSapi.setTanggal(checkDate);
        databaseHandler.addUploadDataSapi(uploadDataSapi);
    }

    private void SaveuploadImage3(final String id_rencana_detail, final String data_assessment, final String data_eartag1, final String data_keterangan1, final String foto1,
                                 final String data_eartag2, final String data_keterangan2, final String foto2, final String data_eartag3,
                                 final String data_keterangan3, final String foto3) {
        final String date = "yyyy-MM-dd HH:mm:ss";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(date);
        final String checkDate = dateFormat.format(calendar.getTime());
        UploadDataSapi uploadDataSapi = new UploadDataSapi();

        uploadDataSapi.setId_rencana_detail(id_rencana_detail);
        uploadDataSapi.setEartag(data_eartag1);
        uploadDataSapi.setFoto(foto1);
        uploadDataSapi.setKeterangan(data_keterangan1);
        uploadDataSapi.setAssessment(data_assessment);
        uploadDataSapi.setTanggal(checkDate);
        databaseHandler.addUploadDataSapi(uploadDataSapi);

        uploadDataSapi.setId_rencana_detail(id_rencana_detail);
        uploadDataSapi.setEartag(data_eartag2);
        uploadDataSapi.setFoto(foto2);
        uploadDataSapi.setKeterangan(data_keterangan2);
        uploadDataSapi.setAssessment(data_assessment);
        uploadDataSapi.setTanggal(checkDate);
        databaseHandler.addUploadDataSapi(uploadDataSapi);

        uploadDataSapi.setId_rencana_detail(id_rencana_detail);
        uploadDataSapi.setEartag(data_eartag3);
        uploadDataSapi.setFoto(foto3);
        uploadDataSapi.setKeterangan(data_keterangan3);
        uploadDataSapi.setAssessment(data_assessment);
        uploadDataSapi.setTanggal(checkDate);
        databaseHandler.addUploadDataSapi(uploadDataSapi);
    }

    private void SaveuploadImage2(final String id_rencana_detail, final String data_assessment,
                                 final String data_eartag2, final String data_keterangan2, final String foto2, final String data_eartag3,
                                 final String data_keterangan3, final String foto3) {
        final String date = "yyyy-MM-dd HH:mm:ss";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(date);
        final String checkDate = dateFormat.format(calendar.getTime());
        UploadDataSapi uploadDataSapi = new UploadDataSapi();

        uploadDataSapi.setId_rencana_detail(id_rencana_detail);
        uploadDataSapi.setEartag(data_eartag2);
        uploadDataSapi.setFoto(foto2);
        uploadDataSapi.setKeterangan(data_keterangan2);
        uploadDataSapi.setAssessment(data_assessment);
        uploadDataSapi.setTanggal(checkDate);
        databaseHandler.addUploadDataSapi(uploadDataSapi);

        uploadDataSapi.setId_rencana_detail(id_rencana_detail);
        uploadDataSapi.setEartag(data_eartag3);
        uploadDataSapi.setFoto(foto3);
        uploadDataSapi.setKeterangan(data_keterangan3);
        uploadDataSapi.setAssessment(data_assessment);
        uploadDataSapi.setTanggal(checkDate);
        databaseHandler.addUploadDataSapi(uploadDataSapi);
    }

    private void SaveuploadImage1(final String id_rencana_detail, final String data_assessment, final String data_eartag3,
                                 final String data_keterangan3, final String foto3) {
        final String date = "yyyy-MM-dd HH:mm:ss";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(date);
        final String checkDate = dateFormat.format(calendar.getTime());
        UploadDataSapi uploadDataSapi = new UploadDataSapi();

        uploadDataSapi.setId_rencana_detail(id_rencana_detail);
        uploadDataSapi.setEartag(data_eartag3);
        uploadDataSapi.setFoto(foto3);
        uploadDataSapi.setKeterangan(data_keterangan3);
        uploadDataSapi.setAssessment(data_assessment);
        uploadDataSapi.setTanggal(checkDate);
        databaseHandler.addUploadDataSapi(uploadDataSapi);
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
                    saveAppSupplierFoto("");
                    saveAppSupplierFoto1("");
                    saveAppSupplierFoto2("");
                    saveAppSupplierFoto3("");
                    saveAppEartag("");
                    saveAppEartag1("");
                    saveAppEartag2("");
                    saveAppEartag3("");
                    saveAppKeterangan("");
                    saveAppKeterangan1("");
                    saveAppKeterangan2("");
                    saveAppKeterangan3("");
                    saveAppAssessment("");
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
                    showCustomDialogImg(msg);
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

    public void saveAppEartag(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_EARTAG,
                responsedata);
        editor.commit();
    }
    public void saveAppEartag1(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_EARTAG1,
                responsedata);
        editor.commit();
    }
    public void saveAppEartag2(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_EARTAG2,
                responsedata);
        editor.commit();
    }
    public void saveAppEartag3(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_EARTAG3,
                responsedata);
        editor.commit();
    }public void saveAppKeterangan(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_KETERANGAN,
                responsedata);
        editor.commit();
    }public void saveAppKeterangan1(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_KETERANGAN1,
                responsedata);
        editor.commit();
    }public void saveAppKeterangan2(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_KETERANGAN2,
                responsedata);
        editor.commit();
    }public void saveAppKeterangan3(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_KETERANGAN3,
                responsedata);
        editor.commit();
    }
    public void saveAppSupplierFoto(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_PIR_FOTO_1,
                responsedata);
        editor.commit();
    }
    public void saveAppSupplierFoto1(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_PIR_FOTO_2,
                responsedata);
        editor.commit();
    }
    public void saveAppSupplierFoto2(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_PIR_FOTO_3,
                responsedata);
        editor.commit();
    }
    public void saveAppSupplierFoto3(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_PIR_FOTO_4,
                responsedata);
        editor.commit();
    }
    public void saveAppAssessment(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_ASSESSMENT,
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

    public void gotoCaptureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void gotoCaptureImage1() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri1 = getOutputMediaFileUri1(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri1);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void gotoCaptureImage2() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri2 = getOutputMediaFileUri2(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri2);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void gotoCaptureImage3() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri3 = getOutputMediaFileUri3(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri3);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void resetCheckin() {
        Intent intr = getActivity().getIntent();
        getActivity().finish();
        getActivity().startActivity(intr);
//        tvFotoCustomer.setText("");
//        tvFotoCustomer1.setText("");
//        tvFotoCustomer2.setText("");
//        tvFotoCustomer3.setText("");
//        imgCust.setImageResource(R.drawable.avatar);
//        imgCust1.setImageResource(R.drawable.avatar);
//        imgCust2.setImageResource(R.drawable.avatar);
//        imgCust3.setImageResource(R.drawable.avatar);
//        edeartag.setSelection(0);
//        edeartag1.setSelection(0);
//        edeartag2.setSelection(0);
//        edeartag3.setSelection(0);
//        edketerangan.setText("");
//        edketerangan1.setText("");
//        edketerangan2.setText("");
//        edketerangan3.setText("");
//        saveAppSupplierFoto("");
//        saveAppSupplierFoto1("");
//        saveAppSupplierFoto2("");
//        saveAppSupplierFoto3("");
//        saveAppEartag("");
//        saveAppEartag1("");
//        saveAppEartag2("");
//        saveAppEartag3("");
//        saveAppKeterangan("");
//        saveAppKeterangan1("");
//        saveAppKeterangan2("");
//        saveAppKeterangan3("");
//        saveAppAssessment("");
//        assesment.setChecked(false);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    public Uri getOutputMediaFileUri1(int type) {
        return Uri.fromFile(getOutputMediaFile1(type));
    }
    public Uri getOutputMediaFileUri2(int type) {
        return Uri.fromFile(getOutputMediaFile2(type));
    }
    public Uri getOutputMediaFileUri3(int type) {
        return Uri.fromFile(getOutputMediaFile3(type));
    }

    private SharedPreferences getSharedPrefereces() {
        return act.getSharedPreferences(AppVar.SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
    }

    private File getOutputMediaFile(int type) {
        File dir = new File(AppVar.getFolderPath() + "/"
                + IMAGE_DIRECTORY_NAME);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        //mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(dir.getPath() + File.separator
                    + idrencanaDetail +  "_"
                    + timeStamp + ".png");
            newImageName = idrencanaDetail + "_" + timeStamp + ".png";

        } else {
            return null;
        }
        return mediaFile;
    }
    private File getOutputMediaFile1(int type) {
        File dir = new File(AppVar.getFolderPath() + "/"
                + IMAGE_DIRECTORY_NAME);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        //mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile1 = new File(dir.getPath() + File.separator
                    + idrencanaDetail +  "_"
                    + timeStamp + ".png");
            newImageName1 = idrencanaDetail + "_" + timeStamp + ".png";

        } else {
            return null;
        }
        return mediaFile1;
    }
    private File getOutputMediaFile2(int type) {
        File dir = new File(AppVar.getFolderPath() + "/"
                + IMAGE_DIRECTORY_NAME);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        //mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile2 = new File(dir.getPath() + File.separator
                    + idrencanaDetail +  "_"
                    + timeStamp + ".png");
            newImageName2 = idrencanaDetail + "_" + timeStamp + ".png";

        } else {
            return null;
        }
        return mediaFile2;
    }
    private File getOutputMediaFile3(int type) {
        File dir = new File(AppVar.getFolderPath() + "/"
                + IMAGE_DIRECTORY_NAME);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        //mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile3 = new File(dir.getPath() + File.separator
                    + idrencanaDetail +  "_"
                    + timeStamp + ".png");
            newImageName3 = idrencanaDetail + "_" + timeStamp + ".png";

        } else {
            return null;
        }
        return mediaFile3;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                //Log.d(LOG_TAG, "take image success");
                if (newImageName3 != null) {
                    File newFile = new File(AppVar.getFolderPath() + "/" + IMAGE_DIRECTORY_NAME + "/" + newImageName3);
                    Uri contentUri = Uri.fromFile(newFile);
                    Bitmap  photo = null;
                    try {
                        photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),contentUri);
                        String photoPath3 = AppVar.getFolderPath() + "/" + IMAGE_DIRECTORY_NAME + "/" + newImageName3;
                        ExifInterface ei3 = new ExifInterface(photoPath3);
                        int orientation3 = ei3.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED);
                        Bitmap rotatedBitmap3 = null;
                        switch(orientation3) {

                            case ExifInterface.ORIENTATION_ROTATE_90:
                                rotatedBitmap3 = rotateImage(photo, 90);
                                break;

                            case ExifInterface.ORIENTATION_ROTATE_180:
                                rotatedBitmap3 = rotateImage(photo, 180);
                                break;

                            case ExifInterface.ORIENTATION_ROTATE_270:
                                rotatedBitmap3 = rotateImage(photo, 270);
                                break;

                            case ExifInterface.ORIENTATION_NORMAL:
                            default:
                                rotatedBitmap3 = photo;
                        }
                        OutputStream fOut = new FileOutputStream(newFile);
                        rotatedBitmap3.compress(Bitmap.CompressFormat.JPEG, 30, fOut);
                        fOut.flush();
                        fOut.close();
                        imgCust3.setImageBitmap(rotatedBitmap3);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    tvFotoCustomer3.setText(newImageName3);
                    saveAppSupplierFoto3(newImageName3);
                }else{
                    if (newImageName2 != null) {
                        File newFile = new File(AppVar.getFolderPath() + "/" + IMAGE_DIRECTORY_NAME + "/" + newImageName2);
                        Uri contentUri = Uri.fromFile(newFile);
                        Bitmap  photo = null;
                        try {
                            photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),contentUri);
                            String photoPath2 = AppVar.getFolderPath() + "/" + IMAGE_DIRECTORY_NAME + "/" + newImageName2;
                            ExifInterface ei2 = new ExifInterface(photoPath2);
                            int orientation2 = ei2.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                    ExifInterface.ORIENTATION_UNDEFINED);

                            Bitmap rotatedBitmap2 = null;
                            switch(orientation2) {

                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    rotatedBitmap2 = rotateImage(photo, 90);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    rotatedBitmap2 = rotateImage(photo, 180);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    rotatedBitmap2 = rotateImage(photo, 270);
                                    break;

                                case ExifInterface.ORIENTATION_NORMAL:
                                default:
                                    rotatedBitmap2 = photo;
                            }
                            OutputStream fOut = new FileOutputStream(newFile);
                            rotatedBitmap2.compress(Bitmap.CompressFormat.JPEG, 30, fOut);
                            fOut.flush();
                            fOut.close();
                            imgCust2.setImageBitmap(rotatedBitmap2);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        tvFotoCustomer2.setText(newImageName2);
                        saveAppSupplierFoto2(newImageName2);
                    }else{
                        if (newImageName1 != null) {
                            File newFile1 = new File(AppVar.getFolderPath() + "/" + IMAGE_DIRECTORY_NAME + "/" + newImageName1);
                            Uri contentUri = Uri.fromFile(newFile1);
                            Bitmap  photo1 = null;
                            try {
                                photo1 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),contentUri);
                                String photoPath1 = AppVar.getFolderPath() + "/" + IMAGE_DIRECTORY_NAME + "/" + newImageName1;
                                ExifInterface ei1 = new ExifInterface(photoPath1);
                                int orientation1 = ei1.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                        ExifInterface.ORIENTATION_UNDEFINED);

                                Bitmap rotatedBitmap1 = null;
                                switch(orientation1) {

                                    case ExifInterface.ORIENTATION_ROTATE_90:
                                        rotatedBitmap1 = rotateImage(photo1, 90);
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_180:
                                        rotatedBitmap1 = rotateImage(photo1, 180);
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_270:
                                        rotatedBitmap1 = rotateImage(photo1, 270);
                                        break;

                                    case ExifInterface.ORIENTATION_NORMAL:
                                    default:
                                        rotatedBitmap1 = photo1;
                                }
                                OutputStream fOut1 = new FileOutputStream(newFile1);
                                rotatedBitmap1.compress(Bitmap.CompressFormat.JPEG, 30, fOut1);
                                fOut1.flush();
                                fOut1.close();
                                imgCust1.setImageBitmap(rotatedBitmap1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            tvFotoCustomer1.setText(newImageName1);
                            saveAppSupplierFoto1(newImageName1);
                        }else{
                            if (newImageName != null) {
                                File newFile = new File(AppVar.getFolderPath() + "/" + IMAGE_DIRECTORY_NAME + "/" + newImageName);
                                Uri contentUri = Uri.fromFile(newFile);
                                Bitmap  photo = null;
                                try {
                                    photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),
                                            contentUri);
                                    String photoPath = AppVar.getFolderPath() + "/" + IMAGE_DIRECTORY_NAME + "/" + newImageName;
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
                                tvFotoCustomer.setText(newImageName);
                                saveAppSupplierFoto(newImageName);
                            }
                        }
                    }
                }
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
}
