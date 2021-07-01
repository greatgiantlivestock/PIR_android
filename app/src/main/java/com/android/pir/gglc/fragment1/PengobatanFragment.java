package com.android.pir.gglc.fragment1;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pir.gglc.absen.AppVar;
import com.android.pir.gglc.database.DatabaseHandler;
import com.android.pir.gglc.database.DetailRencana;
import com.android.pir.gglc.database.DetailReqLoadNew;
import com.android.pir.gglc.database.DetailReqObat;
import com.android.pir.gglc.database.MstUser;
import com.android.pir.gglc.database.Mst_Customer;
import com.android.pir.gglc.database.Obat;
import com.android.pir.gglc.database.Pakan;
import com.android.pir.gglc.database.Pengobatan;
import com.android.pir.gglc.pir.PlanVisitActivity;
import com.android.pir.mobile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PengobatanFragment extends Fragment{
    private DatabaseHandler databaseHandler;
    private Context act;
    private ProgressDialog progressDialog;
    private TextView nama,desa,descFoto,cSaved,tvindex_petani,tvdof,tvjmlEkor;
    private Mst_Customer mst_customer;
    private int id_rencana_detail=0;
    private ImageView imgCust;
    private Button addObat,addFoto,simpanPengobatan;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private String IMAGE_DIRECTORY_NAME = "Pengobatan";
    private Uri fileUri;
    private File mediaFile;
    private String newImageName;
    private Obat obat;
    private ListView listView;
    private EditText qtyObat;
    private ListViewChooseAdapter cAdapterChooseAdapter;
    private ArrayList<DetailReqObat> detailReqLoadList = new ArrayList<DetailReqObat>();
    private ListViewAdapter cAdapter;
    private LinearLayout ln_count;
    int jml = 0;
    private int index = 0;
    private int jmlEkor = 0;
    private Pakan pakann;
    private int id_status = 0;
//    private ListViewAdapter cAdapter;

    public PengobatanFragment() {
        //no coding in here
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pir_fragment_pengobatan, container, false);
        act=this.getActivity();

        progressDialog = new ProgressDialog(this.getActivity());
        progressDialog.setTitle(getActivity().getApplicationContext().getResources().getString(R.string.app_name));
        progressDialog.setMessage("Downloading yaa...");
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        databaseHandler = new DatabaseHandler(this.getActivity());
        nama = (TextView) view.findViewById(R.id.nama_petani);
        desa = (TextView) view.findViewById(R.id.alamat_pertani);
        cSaved = (TextView) view.findViewById(R.id.cSave);
        tvindex_petani = (TextView) view.findViewById(R.id.index);
        tvjmlEkor = (TextView) view.findViewById(R.id.jmlEkor);
        tvdof = (TextView) view.findViewById(R.id.dof);
        imgCust = (ImageView) view.findViewById(R.id.ImgCust_obat);
        addObat = (Button) view.findViewById(R.id.addObat);
        addFoto = (Button) view.findViewById(R.id.addFoto);
        simpanPengobatan = (Button) view.findViewById(R.id.save_pir_obat);
        descFoto = (TextView) view.findViewById(R.id.desc_foto);
        listView = (ListView) view.findViewById(R.id.list);
        ln_count = (LinearLayout) view.findViewById(R.id.ln_cout);

        SharedPreferences spPreferences = getSharedPrefereces();
        id_rencana_detail = Integer.parseInt(spPreferences.getString(AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_JADWAL, null));
        index = Integer.parseInt(spPreferences.getString(AppVar.SHARED_PREFERENCES_TABLE_INDEX_NUMBER, null));
        ArrayList<Mst_Customer> customer_list = databaseHandler.getAllCustomerParamRencana(id_rencana_detail);
        mst_customer = new Mst_Customer();
        for (Mst_Customer customer : customer_list)
            mst_customer = customer;
        ArrayList<Pakan> pakan_list = databaseHandler.getMaxPakan(String.valueOf(index));
        pakann = new Pakan();
        for (Pakan paakan : pakan_list)
            pakann = paakan;
        int idrencanaDetail = Integer.parseInt(spPreferences.getString(AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_JADWAL, null));
        ArrayList<DetailRencana> rencana_list = databaseHandler.getAlldetailRencanaParam(idrencanaDetail);
        DetailRencana rencanaDetail = new DetailRencana();
        for (DetailRencana detailRencana : rencana_list)
            rencanaDetail = detailRencana;
        id_status =rencanaDetail.getStatus_rencana();
        tvindex_petani.setText(String.valueOf(pakann.getIndnr()));
        tvjmlEkor.setText(String.valueOf(pakann.getNofanim()));
        tvdof.setText(pakann.getDof()+" Hari");
        nama.setText(mst_customer.getNama_customer());
        desa.setText(mst_customer.getAlamat());

        addFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoCaptureImage();
            }
        });
        addObat.setOnClickListener(maddSalesOrderButtonOnClickListener);
        simpanPengobatan.setOnClickListener(maddSalesOrderButtonOnClickListener);
        jml = databaseHandler.getCountPengobatanParam(id_rencana_detail);
        if(jml>0){
            ln_count.setBackgroundColor(Color.parseColor("#00cc66"));
        }else{
            ln_count.setBackgroundColor(Color.parseColor("#ff0000"));
        }
        cSaved.setText(String.valueOf(jml));

        return  view;
    }

    public void gotoCaptureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
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
                    + "obat_" + id_rencana_detail +  "_"
                    + timeStamp + ".png");
            newImageName = "obat_" + id_rencana_detail + "_" + timeStamp + ".png";

        } else {
            return null;
        }
        return mediaFile;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
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
                                descFoto.setText(newImageName);
                                saveAppSupplierFoto(newImageName);
                            }else{
                                Toast.makeText(PengobatanFragment.this.getActivity(), "Foto Kosong", Toast.LENGTH_LONG).show();
                            }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity().getApplicationContext(),"Batal mengambil foto terbaru!", Toast.LENGTH_SHORT).show();
            } else {
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

    private SharedPreferences getSharedPrefereces() {
        return act.getSharedPreferences(AppVar.SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
    }
    public void saveAppSupplierFoto(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_PIR_FOTO_OBAT,
                responsedata);
        editor.commit();
    }

    private final View.OnClickListener maddSalesOrderButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            int getId = arg0.getId();
            switch (getId) {
                case R.id.addObat:
                    int countCustomer = databaseHandler.getCountCustomer();
                    if (countCustomer == 0) {
                        String msg = getActivity().getApplicationContext().getResources().getString(R.string.app_sales_order_no_data_product);
                        showCustomDialog(msg);
                    } else {
                        ChooseCustomerDialog();
                    }
                    break;
                case R.id.save_pir_obat:
                    if(id_status==0){
                        showCustomDialog("Anda belum checkin, silahkan checkin terlebih dahulu");
                    }else {
                        if (detailReqLoadList.isEmpty()) {
                            String msg = getActivity().getApplicationContext()
                                    .getResources()
                                    .getString(R.string.app_reqload_pengobatan_empty_box);
                            showCustomDialog(msg);
                        } else {
                            if (descFoto.getText().toString().equals("")) {
                                showCustomDialog("Ambil Foto Terlebih Dahulu Sebelum Menyimpan");
                            } else {
                                showCustomDialogConfirm("Data yang disimpan akan langsung terinterface ke SAP setelah proses upload dilakukan. Lanjutkan jika data sudah benar");
                            }
                            break;
                        }
                    }
                default:
                    break;
            }
        }
    };

    private void ChooseCustomerDialog() {
        final Dialog chooseCustomerDialog = new Dialog(act);
        chooseCustomerDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        chooseCustomerDialog
                .setContentView(R.layout.activity_main_obat_choose_dialog);
        chooseCustomerDialog.setCanceledOnTouchOutside(false);
        chooseCustomerDialog.setCancelable(true);

        chooseCustomerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                chooseCustomerDialog.dismiss();
            }
        });

        EditText searchObat = (EditText) chooseCustomerDialog
                .findViewById(R.id.activity_product_edittext_search);
        qtyObat = (EditText) chooseCustomerDialog
                .findViewById(R.id.activity_product_edittext_qty);
        final ArrayList<Obat> customer_list = new ArrayList<Obat>();
        final ListView listview = (ListView) chooseCustomerDialog
                .findViewById(R.id.list);
        searchObat.setFocusable(true);
        searchObat.setFocusableInTouchMode(true);
        searchObat.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchObat, InputMethodManager.SHOW_IMPLICIT);

        listview.setItemsCanFocus(false);
        ArrayList<Obat> customer_from_db = databaseHandler.getAllObat();
        if (customer_from_db.size() > 0) {
            listview.setVisibility(View.VISIBLE);
            for (int i = 0; i < customer_from_db.size(); i++) {
                int id_obat = customer_from_db.get(i).getId_obat();
                String kode_obat = customer_from_db.get(i).getKode_obat();
                String nama_obat = customer_from_db.get(i).getNama_obat();
                String unit_obat = customer_from_db.get(i).getUnit_obat();

                Obat customer = new Obat();
                customer.setId_obat(id_obat);
                customer.setKode_obat(kode_obat);
                customer.setNama_obat(nama_obat);
                customer.setUnit_obat(unit_obat);
                customer_list.add(customer);
                cAdapterChooseAdapter = new ListViewChooseAdapter(
                        PengobatanFragment.this.getActivity(),
                        R.layout.list_item_obat,customer_list, chooseCustomerDialog);
                listview.setAdapter(cAdapterChooseAdapter);
                cAdapterChooseAdapter.notifyDataSetChanged();
            }
        } else {
            listview.setVisibility(View.INVISIBLE);
        }

        searchObat.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                if (cs.toString().length() > 0) {
                    customer_list.clear();
                    ArrayList<Obat> customer_from_db = databaseHandler
                            .getAllObatBaseOnSearch(cs.toString());
                    if (customer_from_db.size() > 0) {
                        listview.setVisibility(View.VISIBLE);
                        for (int i = 0; i < customer_from_db.size(); i++) {
                            int id_obat= customer_from_db.get(i)
                                    .getId_obat();
                            String kode_obat = customer_from_db.get(i)
                                    .getKode_obat();
                            String nama_obat = customer_from_db.get(i)
                                    .getNama_obat();
                            String unit_obat = customer_from_db.get(i).getUnit_obat();

                            Obat customer = new Obat();
                            customer.setId_obat(id_obat);
                            customer.setKode_obat(kode_obat);
                            customer.setNama_obat(nama_obat);
                            customer.setUnit_obat(unit_obat);
                            customer_list.add(customer);
                            cAdapterChooseAdapter = new ListViewChooseAdapter(
                                    PengobatanFragment.this.getActivity(),
                                    R.layout.list_item_obat, customer_list,chooseCustomerDialog);
                            listview.setAdapter(cAdapterChooseAdapter);
                            cAdapterChooseAdapter.notifyDataSetChanged();
                        }
                    } else {
                        listview.setVisibility(View.INVISIBLE);
                    }

                } else {
                    customer_list.clear();
                    ArrayList<Obat> customer_from_db = databaseHandler
                            .getAllObat();
                    if (customer_from_db.size() > 0) {
                        listview.setVisibility(View.VISIBLE);
                        for (int i = 0; i < customer_from_db.size(); i++) {
                            int id_obat= customer_from_db.get(i)
                                    .getId_obat();
                            String kode_obat = customer_from_db.get(i)
                                    .getKode_obat();
                            String nama_obat = customer_from_db.get(i)
                                    .getNama_obat();
                            String unit_obat = customer_from_db.get(i).getUnit_obat();

                            Obat customer = new Obat();
                            customer.setId_obat(id_obat);
                            customer.setKode_obat(kode_obat);
                            customer.setNama_obat(nama_obat);
                            customer.setUnit_obat(unit_obat);
                            customer_list.add(customer);
                            cAdapterChooseAdapter = new ListViewChooseAdapter(
                                    PengobatanFragment.this.getActivity(),
                                    R.layout.list_item_obat, customer_list,chooseCustomerDialog);
                            listview.setAdapter(cAdapterChooseAdapter);
                            cAdapterChooseAdapter.notifyDataSetChanged();
                        }
                    } else {
                        listview.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
        Handler handler = new Handler();
        handler.post(new Runnable() {
            public void run() {
                chooseCustomerDialog.show();
            }
        });
    }

    public void savePengobatan() {
        int countScc = 0;final String date = "yyyy-MM-dd HH:mm:ss";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(date);
        final String checkDate = dateFormat.format(calendar.getTime());
        for (DetailReqObat detailReqObat : detailReqLoadList) {
            int jml = databaseHandler.getCountPengobatan();
            databaseHandler.addPengobatan(new Pengobatan(jml+1,id_rencana_detail,detailReqObat.getKode_obat(),detailReqObat.getQty_obat(),newImageName,checkDate));
            countScc = countScc+1;
        }
        if(countScc>0){
            jml = databaseHandler.getCountPengobatanParam(id_rencana_detail);
            ln_count.setBackgroundColor(Color.parseColor("#00cc66"));
            cSaved.setText(String.valueOf(jml));
            resetForm();
            showCustomDialog("Data Berhasil Disimpan.");
        }else{
            showCustomDialog("Gagal Menyimpan..");
        }

    }

    public void resetForm(){
        listView.setAdapter(null);
        newImageName="";
        descFoto.setText("");
        imgCust.setImageBitmap(null);
        imgCust.setBackgroundResource(R.drawable.avatar);
        detailReqLoadList.clear();
    }

    class ListViewChooseAdapter extends ArrayAdapter<Obat> {
        int layoutResourceId;
        Obat productData;
        ArrayList<Obat> data = new ArrayList<Obat>();
        Activity mainActivity;
        Dialog chooseProductDialog;

        public ListViewChooseAdapter(Activity mainActivity,int layoutResourceId, ArrayList<Obat> data, Dialog chooseProductDialog) {
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
                holder.list_namaObat= (TextView) row
                        .findViewById(R.id.choose_nama_obat);
                holder.list_KodeObat= (TextView) row
                        .findViewById(R.id.choose_kode_obat);
                holder.mButtonAddItem = (Button) row
                        .findViewById(R.id.obat_dialog_btn_add_item);
                row.setTag(holder);
            } else {
                holder = (ListViewChooseAdapter.UserHolder) row.getTag();
            }
            productData = data.get(position);
            holder.list_namaObat.setText(productData.getNama_obat());
            holder.list_KodeObat.setText(productData.getKode_obat());
            holder.mButtonAddItem.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(qtyObat.getText().toString().equals("")){
                        Toast.makeText(PengobatanFragment.this.getActivity(), "Input Qty Terlebih dahulu", Toast.LENGTH_LONG).show();
                    }else{
                        int qty = Integer.parseInt(qtyObat.getText().toString());
                        updateListViewDetailOrder(new DetailReqObat(data.get(position).getId_obat(), data.get(position).getKode_obat(),data.get(position).getNama_obat(),qty,data.get(position).getUnit_obat()));
                        chooseProductDialog.hide();
                        listView.setVisibility(View.VISIBLE);
                    }
                }
            });
            return row;
        }

        class UserHolder {
            TextView list_namaObat;
            TextView list_KodeObat;
            Button mButtonAddItem;
        }
    }

    private void updateListViewDetailOrder(DetailReqObat detailReqLoad) {
        detailReqLoadList.add(detailReqLoad);
        cAdapter = new ListViewAdapter(PengobatanFragment.this.getActivity(), R.layout.list_item_detail_obat, detailReqLoadList);
        listView.setAdapter(cAdapter);
        cAdapter.notifyDataSetChanged();
    }

    public class ListViewAdapter extends ArrayAdapter<DetailReqObat> {
        Activity activity;
        int layoutResourceId;
        DetailReqObat productData;
        ArrayList<DetailReqObat> data = new ArrayList<DetailReqObat>();

        public ListViewAdapter(Activity act, int layoutResourceId,
                               ArrayList<DetailReqObat> data) {
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
                holder.list_nama_obat= (TextView) row
                        .findViewById(R.id.obat_nama_obat_list);
                holder.list_kode_obat= (TextView) row
                        .findViewById(R.id.obat_kode_obat_list);
                holder.list_qty_obat= (TextView) row
                        .findViewById(R.id.obat_qty);
                row.setTag(holder);
            } else {
                holder = (ListViewAdapter.UserHolder) row.getTag();
            }
            productData = data.get(position);
            holder.list_nama_obat.setText(productData.getNama_obat());
            holder.list_kode_obat.setText(productData.getKode_obat());
            holder.list_qty_obat.setText(String.valueOf(productData.getQty_obat()));
            row.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    showDeleteDialog(position);
                }
            });
            return row;
        }

        public void showDeleteDialog(final int position) {
            String msg = getActivity().getApplicationContext().getResources().getString(
                    R.string.MSG_DLG_LABEL_DATA_DELETE_DIALOG);
            final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(act);
            alertDialogBuilder
                    .setMessage(msg)
                    .setCancelable(true)
                    .setNegativeButton(
                            getActivity().getApplicationContext().getResources().getString(
                                    R.string.MSG_DLG_LABEL_YES),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    detailReqLoadList.remove(position);
                                    cAdapter = new ListViewAdapter(
                                            PengobatanFragment.this.getActivity(),
                                            R.layout.list_item_detail_obat,
                                            detailReqLoadList);
                                    listView.setAdapter(cAdapter);
                                    cAdapter.notifyDataSetChanged();
                                }
                            })
                    .setPositiveButton(
                            getActivity().getApplicationContext().getResources().getString(
                                    R.string.MSG_DLG_LABEL_NO),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    android.app.AlertDialog alertDialog = alertDialogBuilder
                                            .create();
                                    alertDialog.dismiss();
                                }
                            });
            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        class UserHolder {
            TextView list_nama_obat;
            TextView list_kode_obat;
            TextView list_qty_obat;
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
    public void showCustomDialogConfirm(String msg) {
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
                                savePengobatan();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
