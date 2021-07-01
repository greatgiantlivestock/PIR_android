package com.android.pir.gglc.fragment1;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pir.gglc.absen.AppVar;
import com.android.pir.gglc.database.DataSapi;
import com.android.pir.gglc.database.DatabaseHandler;
import com.android.pir.gglc.database.DetailRencana;
import com.android.pir.gglc.database.DetailReqLoadNew;
import com.android.pir.gglc.database.FeedbackPakan;
import com.android.pir.gglc.database.MstUser;
import com.android.pir.gglc.database.Mst_Customer;
import com.android.pir.gglc.database.Pakan;
import com.android.pir.gglc.database.Rencana;
import com.android.pir.gglc.database.UploadDataSapi;
import com.android.pir.gglc.pir.DashboardActivity;
import com.android.pir.gglc.pir.PlanVisitActivity;
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
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//import com.android.canvasing.gglc.canvassing.SalesKanvasActivity;


//public class OneFragment1 extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
public class PakanFragment extends Fragment{
    Context thiscontext;
    private DatabaseHandler databaseHandler;
    private Context act;
    private ProgressDialog pDialog;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog;
    protected LocationManager locationManager;
    private int idrencanaDetail = 0;
    private int indnr = 0;
    private int nilai_ass= 0;
    private int index = 0;
    private int jmlEkor = 0;
    private Pakan pakann;

    private Button checkin;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private String response_data,response_data_download,main_app_id_detail_jadwal;
    private ArrayList<Pakan> pakan_list = new ArrayList<Pakan>();
    private ArrayList<Pakan> pakan_list_child = new ArrayList<Pakan>();
    private TextView tvnama_petani,tvalamat_petani,desc_foto,tvindex_petani,tvdof,tvjmlEkor;
    private Button foto;
    private Mst_Customer mst_customer;
    private double latitude, longitude;
    private static final String LOG_TAG = PakanFragment.class.getSimpleName();
    private int id_checkin,id_user,id_customer;
    private MstUser user;
    private DetailRencana rencanaDetail;
    private String idrd,idcst,fto,nmr,lts,lng,idus,tgl,kode_customer,status_checkin;
    private LinearLayout lsstatus;
    private EditText desc;
    private String newImageName;
    private File mediaFile;
    private Uri fileUri;
    private ListView listView;
    private ListViewAdapter cAdapter;
    private String IMAGE_DIRECTORY_NAME = "Pakan";
    private ListView listViewchild;
    private ListViewChooseAdapter cAdapterChooseAdapter;
    public PakanFragment() {
        //no coding in here
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pir_fragment_pakan, container, false);
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
        tvindex_petani = (TextView) view.findViewById(R.id.index);
        tvjmlEkor = (TextView) view.findViewById(R.id.jmlEkor);
        tvdof = (TextView) view.findViewById(R.id.dof);
        lsstatus = (LinearLayout) view.findViewById(R.id.lstatus_checkin);
        listView = (ListView) view.findViewById(R.id.list);
        desc = (EditText) view.findViewById(R.id.desc_pakan);
        foto = (Button) view.findViewById(R.id.foto);
        desc_foto = (TextView) view.findViewById(R.id.desc_foto);
        checkin = (Button) view.findViewById(R.id.save_pakan_feedback);
        SharedPreferences spPreferences = getSharedPrefereces();
        idrencanaDetail = Integer.parseInt(spPreferences.getString(AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_JADWAL, null));
        indnr = Integer.parseInt(spPreferences.getString(AppVar.SHARED_PREFERENCES_TABLE_INDEX_NUMBER, null));

        ArrayList<DetailRencana> pakan_list = databaseHandler.getAlldetailRencanaParam(idrencanaDetail);
        rencanaDetail = new DetailRencana();
        for (DetailRencana detailRencana : pakan_list)
            rencanaDetail = detailRencana;
        id_customer=rencanaDetail.getId_customer();
        status_checkin = String.valueOf(rencanaDetail.getStatus_rencana());

        ArrayList<Mst_Customer> customer_list = databaseHandler.getAllCustomerParamRencana(idrencanaDetail);
        mst_customer = new Mst_Customer();
        for (Mst_Customer customer : customer_list)
            mst_customer = customer;
        ArrayList<Pakan> pakan_list1 = databaseHandler.getMaxPakan(String.valueOf(indnr));
        pakann = new Pakan();
        for (Pakan paakan : pakan_list1)
            pakann = paakan;
        tvindex_petani.setText(String.valueOf(pakann.getIndnr()));
        tvjmlEkor.setText(String.valueOf(pakann.getNofanim()));
        tvdof.setText(pakann.getDof()+" Hari");
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
                    gotoCaptureImage();
            }
        });

        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status_checkin.equals("0")){
                    showCustomDialog("Anda belum checkin, silahkan checkin terlebih dahulu");
                }else {
                    if (desc_foto.getText().toString().equals("") || desc.getText().toString().equals("")) {
                        showCustomDialog("Ambil foto dan isi keterangan terlebih dahulu");
                    } else {
                        saveAppKeterangan(desc.getText().toString());
                        new UploadData().execute();
                    }
                }
            }
        });
        updateContentRefreshRencana();
        return  view;
    }

    public void updateContentRefreshRencana() {
        pakan_list.clear();
        ArrayList<Pakan> pakan_from_db = databaseHandler.getAllPakanType(String.valueOf(indnr));

        if (pakan_from_db.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            for (int i = 0; i < pakan_from_db.size(); i++) {
                int indnr = pakan_from_db.get(i).getIndnr();
                String kode_pakan= pakan_from_db.get(i).getKode_pakan();
                String desc_pakan= pakan_from_db.get(i).getDesc_pakan();
                String std= pakan_from_db.get(i).getStd();
                int budget= pakan_from_db.get(i).getBudget();
                int terkirim= pakan_from_db.get(i).getTerkirim();
                int sisa= pakan_from_db.get(i).getSisa();
                int nofanim= pakan_from_db.get(i).getNofanim();
                String dof= pakan_from_db.get(i).getDof();
                String satuan= pakan_from_db.get(i).getSatuan();
                String tanggal_kirim= pakan_from_db.get(i).getTanggal_kirim();
                int qty_terima= pakan_from_db.get(i).getQty_terima();
                String create_date= pakan_from_db.get(i).getCreate_date();
                String pakan_type= pakan_from_db.get(i).getPakan_type();

                Pakan pakan = new Pakan();
                pakan.setIndnr(indnr);
                pakan.setKode_pakan(kode_pakan);
                pakan.setDesc_pakan(desc_pakan);
                pakan.setStd(std);
                pakan.setBudget(budget);
                pakan.setTerkirim(terkirim);
                pakan.setSisa(sisa);
                pakan.setNofanim(nofanim);
                pakan.setDof(dof);
                pakan.setSatuan(satuan);
                pakan.setTanggal_kirim(tanggal_kirim);
                pakan.setQty_terima(qty_terima);
                pakan.setCreate_date(create_date);
                pakan.setPakan_type(pakan_type);

                pakan_list.add(pakan);
            }
        } else {
            listView.setVisibility(View.INVISIBLE);
        }
        showListRencana();
    }

    public void showListRencana() {
        pakan_list.clear();
        ArrayList<Pakan> pakan_from_db = null;
        pakan_from_db = databaseHandler.getAllPakanType(String.valueOf(indnr));

        if (pakan_from_db.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            for (int i = 0; i < pakan_from_db.size(); i++) {
                int indnr = pakan_from_db.get(i).getIndnr();
                String kode_pakan= pakan_from_db.get(i).getKode_pakan();
                String desc_pakan= pakan_from_db.get(i).getDesc_pakan();
                String std= pakan_from_db.get(i).getStd();
                int budget= pakan_from_db.get(i).getBudget();
                int terkirim= pakan_from_db.get(i).getTerkirim();
                int sisa= pakan_from_db.get(i).getSisa();
                int nofanim= pakan_from_db.get(i).getNofanim();
                String dof= pakan_from_db.get(i).getDof();
                String satuan= pakan_from_db.get(i).getSatuan();
                String tanggal_kirim= pakan_from_db.get(i).getTanggal_kirim();
                int qty_terima= pakan_from_db.get(i).getQty_terima();
                String create_date= pakan_from_db.get(i).getCreate_date();
                String pakan_type= pakan_from_db.get(i).getPakan_type();

                Pakan pakan = new Pakan();
                pakan.setIndnr(indnr);
                pakan.setKode_pakan(kode_pakan);
                pakan.setDesc_pakan(desc_pakan);
                pakan.setStd(std);
                pakan.setBudget(budget);
                pakan.setTerkirim(terkirim);
                pakan.setSisa(sisa);
                pakan.setNofanim(nofanim);
                pakan.setDof(dof);
                pakan.setSatuan(satuan);
                pakan.setTanggal_kirim(tanggal_kirim);
                pakan.setQty_terima(qty_terima);
                pakan.setCreate_date(create_date);
                pakan.setPakan_type(pakan_type);

                pakan_list.add(pakan);
            }

            cAdapter = new ListViewAdapter(PakanFragment.this.getActivity(), R.layout.list_item_pakan,
                    pakan_list);
            listView.setAdapter(cAdapter);
            cAdapter.notifyDataSetChanged();
        } else {
            listView.setVisibility(View.INVISIBLE);
        }
    }
    public class ListViewAdapter extends ArrayAdapter<Pakan> {
        Activity activity;
        int layoutResourceId;
        Pakan pakanData;

        ArrayList<Pakan> data = new ArrayList<Pakan>();

        public ListViewAdapter(Activity act, int layoutResourceId,
                               ArrayList<Pakan> data) {
            super(act, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.activity = act;
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            View row = convertView;
            ListViewAdapter.UserHolder holder = null;

            if (row == null) {
                LayoutInflater inflater = LayoutInflater.from(activity);
                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new ListViewAdapter.UserHolder();
                holder.list_namaPakan = (TextView) row
                        .findViewById(R.id.nama_pakan);
                holder.list_budget = (TextView) row
                        .findViewById(R.id.budget_value);
                holder.list_terkirim = (TextView) row
                        .findViewById(R.id.terkirim_value);
                holder.list_sisa = (TextView) row
                        .findViewById(R.id.sisa_value);
                holder.list_std = (TextView) row
                        .findViewById(R.id.std_value);
                row.setTag(holder);
            } else {
                holder = (ListViewAdapter.UserHolder) row.getTag();
            }
            pakanData = data.get(position);
            String satuan = pakanData.getSatuan();
            holder.list_namaPakan.setText(pakanData.getPakan_type());
            holder.list_budget.setText(pakanData.getBudget()+" "+satuan);
            holder.list_terkirim.setText(pakanData.getTerkirim()+" "+satuan);
//            holder.list_sisa.setText(pakanData.getSisa()+" "+satuan);
            holder.list_sisa.setText(pakanData.getBudget()-pakanData.getTerkirim()+" "+satuan);
            holder.list_std.setText("("+pakanData.getStd()+" Kg/Ekor/Hari)");

            row.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String pakan_type = String.valueOf(data.get(position).getPakan_type());
                    ChooseCustomerDialog(pakan_type);
                }
            });
            return row;
        }
        class UserHolder {
            TextView list_namaPakan;
            TextView list_budget;
            TextView list_terkirim;
            TextView list_sisa;
            TextView list_std;
        }
    }

    private void ChooseCustomerDialog(String type) {
        final Dialog chooseCustomerDialog = new Dialog(act);
        chooseCustomerDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        chooseCustomerDialog
                .setContentView(R.layout.activity_main_detail_pakan);
        chooseCustomerDialog.setCanceledOnTouchOutside(false);
        chooseCustomerDialog.setCancelable(true);
        Button button = (Button) chooseCustomerDialog
                .findViewById(R.id.close);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseCustomerDialog.dismiss();
            }
        });
        chooseCustomerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                chooseCustomerDialog.dismiss();
            }
        });
        final ArrayList<Pakan> customer_list = new ArrayList<Pakan>();
        final ListView listview = (ListView) chooseCustomerDialog.findViewById(R.id.list);

        listview.setItemsCanFocus(false);
        ArrayList<Pakan> pakan_from_db = databaseHandler.getAllPakan(String.valueOf(indnr),type);
        if (pakan_from_db.size() > 0) {
            listview.setVisibility(View.VISIBLE);
            for (int i = 0; i < pakan_from_db.size(); i++) {
                int indnr = pakan_from_db.get(i).getIndnr();
                String kode_pakan= pakan_from_db.get(i).getKode_pakan();
                String desc_pakan= pakan_from_db.get(i).getDesc_pakan();
                String std= pakan_from_db.get(i).getStd();
                int budget= pakan_from_db.get(i).getBudget();
                int terkirim= pakan_from_db.get(i).getTerkirim();
                int sisa= pakan_from_db.get(i).getSisa();
                int nofanim= pakan_from_db.get(i).getNofanim();
                String dof= pakan_from_db.get(i).getDof();
                String satuan= pakan_from_db.get(i).getSatuan();
                String tanggal_kirim= pakan_from_db.get(i).getTanggal_kirim();
                int qty_terima= pakan_from_db.get(i).getQty_terima();
                String create_date= pakan_from_db.get(i).getCreate_date();
                String pakan_type= pakan_from_db.get(i).getPakan_type();

                Pakan pakan = new Pakan();
                pakan.setIndnr(indnr);
                pakan.setKode_pakan(kode_pakan);
                pakan.setDesc_pakan(desc_pakan);
                pakan.setStd(std);
                pakan.setBudget(budget);
                pakan.setTerkirim(terkirim);
                pakan.setSisa(sisa);
                pakan.setNofanim(nofanim);
                pakan.setDof(dof);
                pakan.setSatuan(satuan);
                pakan.setTanggal_kirim(tanggal_kirim);
                pakan.setQty_terima(qty_terima);
                pakan.setCreate_date(create_date);
                pakan.setPakan_type(pakan_type);

                customer_list.add(pakan);
                cAdapterChooseAdapter = new ListViewChooseAdapter(PakanFragment.this.getActivity(),R.layout.list_item_detail_pakan,customer_list, chooseCustomerDialog);
                listview.setAdapter(cAdapterChooseAdapter);
                cAdapterChooseAdapter.notifyDataSetChanged();
            }
        } else {
            listview.setVisibility(View.INVISIBLE);
        }
        Handler handler = new Handler();
        handler.post(new Runnable() {
            public void run() {
                chooseCustomerDialog.show();
            }
        });
    }

    class ListViewChooseAdapter extends ArrayAdapter<Pakan> {
        int layoutResourceId;
        Pakan productData;
        ArrayList<Pakan> data = new ArrayList<Pakan>();
        Activity mainActivity;
        Dialog chooseProductDialog;

        public ListViewChooseAdapter(Activity mainActivity,int layoutResourceId, ArrayList<Pakan> data, Dialog chooseProductDialog) {
            super(mainActivity, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.data = data;
            this.chooseProductDialog = chooseProductDialog;
            this.mainActivity = mainActivity;
            notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            View row = convertView;
            ListViewChooseAdapter.UserHolder holder = null;

            if (row == null) {
                LayoutInflater inflater = LayoutInflater.from(mainActivity);

                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new ListViewChooseAdapter.UserHolder();
                holder.list_nama = (TextView) row
                        .findViewById(R.id.desc_pakan);
                holder.list_jml = (TextView) row
                        .findViewById(R.id.jml);
                holder.list_tanggal = (TextView) row
                        .findViewById(R.id.tanggal);

                row.setTag(holder);
            } else {
                holder = (ListViewChooseAdapter.UserHolder) row.getTag();
            }
            productData = data.get(position);
            holder.list_nama.setText(productData.getDesc_pakan());
            holder.list_jml.setText(String.valueOf(productData.getQty_terima()));
            holder.list_tanggal.setText(productData.getTanggal_kirim());
            return row;
        }

        class UserHolder {
            //			ImageView list_img;
            TextView list_nama;
            TextView list_jml;
            TextView list_tanggal;
        }
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
            SharedPreferences spPreferences = getSharedPrefereces();
            String foto = spPreferences.getString(AppVar.SHARED_PREFERENCES_PIR_FOTO_PAKAN, "");
            String data_keterangan = spPreferences.getString(AppVar.SHARED_PREFERENCES_KETERANGAN_PAKAN, "");
            /***********************
             * Upload Image Supplier
             */
            SaveuploadImage1(String.valueOf(idrencanaDetail),data_keterangan,foto);

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            int cekDS = databaseHandler.getCountUploadFeedbackPakan(idrencanaDetail);
            if(cekDS > 0){
                saveAppSupplierFoto("");
                saveAppKeterangan("");
                showCustomDialogImg("Simpan Data Feedback Pakan Berhasil.");
            }else{
                showCustomDialog("Data Feedback Pakan Gagal Disimpan.");
            }
        }
    }

//    protected String uploadImage1(final String url, final String id_rencana_detail, final String data_assessment,final String data_eartag, final String data_keterangan,
//                                 final String foto) {
//        HttpClient httpclient = new DefaultHttpClient();
//        HttpPost httppost = new HttpPost(url);
//        String responseString = null;
//        try {
//            if (android.os.Build.VERSION.SDK_INT > 9) {
//                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                        .permitAll().build();
//                StrictMode.setThreadPolicy(policy);
//            }
//            MultipartEntity entity = new MultipartEntity();
//            File dir = new File(AppVar.getFolderPath() + "/"+ IMAGE_DIRECTORY_NAME + "/"+ foto);
//            if (dir.exists() && foto != null) {
//                entity.addPart("image_1", new FileBody(dir));
//                entity.addPart("foto1", new StringBody(foto));
//            } else {
//                entity.addPart("foto1", new StringBody(""));
//            }
//            entity.addPart("id_rencana_detail", new StringBody(id_rencana_detail));
//            entity.addPart("assessment", new StringBody(data_assessment));
//            entity.addPart("eratag1", new StringBody(data_eartag));
//            entity.addPart("keterangan1", new StringBody(data_keterangan));
//            httppost.setEntity(entity);
//
//            // Making server call
//            HttpResponse response = httpclient.execute(httppost);
//            HttpEntity r_entity = response.getEntity();
//
//            int statusCode = response.getStatusLine().getStatusCode();
//            if (statusCode == 200) {
//                // Server response
//                responseString = EntityUtils.toString(r_entity);
//            } else {
//                responseString = "Error occurred! Http Status Code: "
//                        + statusCode;
//            }
//        } catch (ClientProtocolException e) {
//            responseString = e.toString();
//        } catch (IOException e) {
//            responseString = e.toString();
//        }
//        return responseString;
//    }

    private void SaveuploadImage1(final String id_rencana_detail,
                                 final String data_keterangan3, final String foto3) {
        FeedbackPakan feedbackPakan = new FeedbackPakan();
        feedbackPakan.setId_rencana_detail(id_rencana_detail);
        feedbackPakan.setFeedback_pakan(data_keterangan3);
        feedbackPakan.setFoto(foto3);
        databaseHandler.addFeedbackPakan(feedbackPakan);
    }

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
//                    saveAppSupplierFoto("");
//                    saveAppKeterangan("");
//                    File dir = new File(AppVar.getFolderPath() + "/"
//                            + IMAGE_DIRECTORY_NAME);
//                    List<File> fileFoto = getListFiles(dir);
//                    for (File tempFile : fileFoto) {
//                        tempFile.delete();
//                    }
//                    final String msg = act
//                            .getApplicationContext()
//                            .getResources()
//                            .getString(
//                                    R.string.app_supplier_processing_sukses);
//                    showCustomDialogImg(msg);
//                } else {
//                    final String msg = act
//                            .getApplicationContext()
//                            .getResources()
//                            .getString(
//                                    R.string.app_supplier_processing_failed);
//                    showCustomDialog(msg);
//                }
//            }
//        } catch (JSONException e) {
//            final String message = e.toString();
//            showCustomDialog(message);
//        }
//    }

    public void saveAppKeterangan(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_KETERANGAN_PAKAN,
                responsedata);
        editor.commit();
    }
    public void saveAppSupplierFoto(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_PIR_FOTO_PAKAN,
                responsedata);
        editor.commit();
    }

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

    public void gotoCaptureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void resetCheckin() {
        desc_foto.setText("");
        desc.setText("");
//        Intent intr = getActivity().getIntent();
//        getActivity().finish();
//        getActivity().startActivity(intr);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                //Log.d(LOG_TAG, "take image success");
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
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    desc_foto.setText(newImageName);
                    saveAppSupplierFoto(newImageName);
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
