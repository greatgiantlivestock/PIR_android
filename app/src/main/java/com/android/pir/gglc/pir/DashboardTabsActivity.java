package com.android.pir.gglc.pir;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.android.pir.gglc.absen.AppVar;
import com.android.pir.gglc.absen.ChangePassword;
import com.android.pir.gglc.absen.NavigationDrawerCallbacks;
import com.android.pir.gglc.absen.NavigationDrawerFragment;
import com.android.pir.gglc.absen.Orderan;
import com.android.pir.gglc.database.DatabaseHandler;
import com.android.pir.gglc.database.DetailRencana;
import com.android.pir.gglc.database.MasterRencana;
import com.android.pir.gglc.database.Mst_Customer;
import com.android.pir.gglc.fragment.OneFragment;
import com.android.pir.gglc.fragment.ThreeFragment;
import com.android.pir.gglc.fragment.TwoFragment;
import com.android.pir.gglc.fragment1.CheckoutFragment;
import com.android.pir.gglc.fragment1.DataSapiFragment;
import com.android.pir.gglc.fragment1.OneFragment1;
import com.android.pir.gglc.fragment1.PakanFragment;
import com.android.pir.gglc.fragment1.PengobatanFragment;
import com.android.pir.gglc.fragment1.ThreeFragment1;
import com.android.pir.gglc.fragment1.ThreeFragment2;
import com.android.pir.gglc.fragment1.ThreeFragmentweb_view1;
import com.android.pir.gglc.fragment1.TwoFragment1;
import com.android.pir.mobile.R;

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
import java.util.ArrayList;
import java.util.List;

import static com.android.pir.gglc.absen.AppVar.SHARED_PREFERENCES_NAME;

public class DashboardTabsActivity extends AppCompatActivity{
    private Toolbar mToolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Context act;
    private DetailRencana rencanaDetail;
    private int idrencanaDetail = 0;
    private DatabaseHandler databaseHandler;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog;
    private String message, response_data, msg_success;
    private static final String LOG_TAG = DashboardTabsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_icon_text_tabs);

//        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setTitle(R.string.title_activity_all);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseHandler = new DatabaseHandler(this);
        act = this;

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getApplicationContext().getResources()
                .getString(R.string.app_name));
        progressDialog.setMessage("Downloading...");
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
//        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
//                .findFragmentById(R.id.fragment_drawer);
//        mNavigationDrawerFragment.setup(R.id.fragment_drawer,
//                (DrawerLayout) findViewById(R.id.drawer), mToolbar);
//        mNavigationDrawerFragment.selectItem(1);

        if(databaseHandler.getCountDetailrencana()==0){
           new DownloadDataRencanaDetail().execute();
        }else if(databaseHandler.getCountDetailrencana() > 0){
            if(databaseHandler.getTanggal_rencana_available()==0){
                new DownloadDataRencanaDetail().execute();
            }else{
                viewPager = (ViewPager) findViewById(R.id.viewpager);
                setupViewPager(viewPager);
                tabLayout = (TabLayout) findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(viewPager);
            }
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main1, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_refresh:
//                if (GlobalApp.checkInternetConnection(act)) {
////                    int countUpload = databaseHandler.getCountRencanaDetailWhereValidAndUpdateAll();
//                    new DownloadDataRencanaDetail().execute();
////                    if(countUpload == 0){
////                        new DownloadDataRencanaDetail().execute();
////                    }else{
////                        String message = act
////                                .getApplicationContext()
////                                .getResources()
////                                .getString(
////                                        R.string.app_customer_processing_refresh_failed);
////                        showCustomDialog(message);
////                    }
//
//                } else {
//                    String message = act.getApplicationContext().getResources()
//                            .getString(R.string.app_customer_processing_empty);
//                    showCustomDialog(message);
//                }
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    private class DownloadDataRencanaDetail extends AsyncTask<String, Integer, String> {
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
            SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
            String id_karyawan = prefs.getString("id_awo","null");

            String download_data_url = AppVar.CONFIG_APP_URL_PUBLIC
                    + AppVar.CONFIG_APP_URL_DOWNLOAD_RENCANA_DETAIL_APROVED+ "?id_karyawan="
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

                    SharedPreferences spPreferences = getSharedPrefereces();
                    String main_app_table_data = spPreferences.getString(
                            AppVar.SHARED_PREFERENCES_TABLE_RENCANA_DETAIL, null);
                    if (main_app_table_data != null) {
                        if (main_app_table_data.equalsIgnoreCase(response_data)) {
                            saveAppDataRencanaDetailSameData(act
                                    .getApplicationContext().getResources()
                                    .getString(R.string.app_value_true));
                        } else {
                            databaseHandler.deleteTableRencanaDetail();
                            saveAppDataRencanaDetailSameData(act
                                    .getApplicationContext().getResources()
                                    .getString(R.string.app_value_false));
                        }
                    } else {
                        databaseHandler.deleteTableRencanaDetail();
                        saveAppDataRencanaDetailSameData(act.getApplicationContext()
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
                saveAppDataRencanaDetail(response_data);
                extractDataRencanaDetail();
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                new DownloadDataRencanaMaster().execute();
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

    private class DownloadDataRencanaMaster extends AsyncTask<String, Integer, String> {
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
            SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
            String id_karyawan = prefs.getString("id_awo","null");

            String download_data_url = AppVar.CONFIG_APP_URL_PUBLIC
                    + AppVar.CONFIG_APP_URL_DOWNLOAD_RENCANA_MASTER+ "?id_karyawan="
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

                    SharedPreferences spPreferences = getSharedPrefereces();
                    String main_app_table_data = spPreferences.getString(
                            AppVar.SHARED_PREFERENCES_TABLE_RENCANA_MASTER, null);
                    if (main_app_table_data != null) {
                        if (main_app_table_data.equalsIgnoreCase(response_data)) {
                            saveAppDataRencanaMasterSameData(act
                                    .getApplicationContext().getResources()
                                    .getString(R.string.app_value_true));
                        } else {
                            databaseHandler.deleteTableRencanaMaster();
                            saveAppDataRencanaMasterSameData(act
                                    .getApplicationContext().getResources()
                                    .getString(R.string.app_value_false));
                        }
                    } else {
                        databaseHandler.deleteTableRencanaMaster();
                        saveAppDataRencanaMasterSameData(act.getApplicationContext()
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
                saveAppDataRencanaMaster(response_data);
                extractDataRencanaMaster();
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                msg_success=act.getApplicationContext().getResources()
                        .getString(R.string.MSG_DLG_LABEL_SYNRONISASI_DATA_SUCCESS);
                if(databaseHandler.getTanggal_rencana_available()==0){
                    showDataOnserverNull("Tidak ada data rencana terbaru, silahkan hubungi admin bagian anda, terkait rencana kegiatan");
                }else{
                    showCustomDialogSuccess(msg_success);
                }
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

    public void extractDataRencanaDetail() {
        SharedPreferences spPreferences = getSharedPrefereces();
        String main_app_table_same_data = spPreferences.getString(
                AppVar.SHARED_PREFERENCES_TABLE_RENCANA_DETAIL_SAME_DATA, null);
        String main_app_table = spPreferences.getString(
                AppVar.SHARED_PREFERENCES_TABLE_RENCANA_DETAIL, null);
        if (main_app_table_same_data.equalsIgnoreCase(act
                .getApplicationContext().getResources()
                .getString(R.string.app_value_false))) {
            JSONObject oResponse;
            try {
                oResponse = new JSONObject(main_app_table);
                JSONArray jsonarr = oResponse.getJSONArray("rencana_detail");
                for (int i = 0; i < jsonarr.length(); i++) {
                    JSONObject oResponsealue = jsonarr.getJSONObject(i);
                    String id_rencana_detail = oResponsealue.isNull("id_rencana_detail") ? null
                            : oResponsealue.getString("id_rencana_detail");
                    String id_rencana_header = oResponsealue.isNull("id_rencana_header") ? null
                            : oResponsealue.getString("id_rencana_header");
                    String id_kegiatan = oResponsealue.isNull("id_kegiatan") ? null
                            : oResponsealue.getString("id_kegiatan");
                    String id_customer = oResponsealue.isNull("id_customer") ? null
                            : oResponsealue.getString("id_customer");
                    String id_karyawan = oResponsealue.isNull("id_karyawan") ? null
                            : oResponsealue.getString("id_karyawan");
                    String status_rencana = oResponsealue.isNull("status_rencana") ? null
                            : oResponsealue.getString("status_rencana");
                    String nomor_rencana_detail = oResponsealue.isNull("nomor_rencana_detail") ? null
                            : oResponsealue.getString("nomor_rencana_detail");
                    String indnr = oResponsealue.isNull("indnr") ? null
                            : oResponsealue.getString("indnr");
                    Log.d(LOG_TAG, "id_rencana_detail:" + id_rencana_detail);
                    Log.d(LOG_TAG, "id_rencana_header:" + id_rencana_header);
                    Log.d(LOG_TAG, "id_kegiatan:" + id_kegiatan);
                    Log.d(LOG_TAG, "id_customer:" + id_customer);
                    databaseHandler.addDetailRencana(new DetailRencana(Integer.parseInt(id_rencana_detail),Integer.parseInt(id_rencana_header),
                            Integer.parseInt(id_kegiatan),Integer.parseInt(id_customer),Integer.parseInt(id_karyawan),
                            Integer.parseInt(status_rencana),nomor_rencana_detail,indnr));
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

    public void extractDataRencanaMaster() {
        SharedPreferences spPreferences = getSharedPrefereces();
        String main_app_table_rm_same_data = spPreferences.getString(
                AppVar.SHARED_PREFERENCES_TABLE_RENCANA_MASTER_SAME_DATA, null);
        String main_app_table = spPreferences.getString(
                AppVar.SHARED_PREFERENCES_TABLE_RENCANA_MASTER, null);
        if (main_app_table_rm_same_data.equalsIgnoreCase(act
                .getApplicationContext().getResources()
                .getString(R.string.app_value_false))) {
            JSONObject oResponse;
            try {
                oResponse = new JSONObject(main_app_table);
                JSONArray jsonarr = oResponse.getJSONArray("rencana_master");
                for (int i = 0; i < jsonarr.length(); i++) {
                    JSONObject oResponsealue = jsonarr.getJSONObject(i);
                    String id_rencana_header = oResponsealue.isNull("id_rencana_header") ? null
                            : oResponsealue.getString("id_rencana_header");
                    String nomor_rencana = oResponsealue.isNull("nomor_rencana") ? null
                            : oResponsealue.getString("nomor_rencana");
                    String tanggal_penetapan = oResponsealue.isNull("tanggal_penetapan") ? null
                            : oResponsealue.getString("tanggal_penetapan");
                    String tanggal_rencana = oResponsealue.isNull("tanggal_rencana") ? null
                            : oResponsealue.getString("tanggal_rencana");
                    String id_user_input_rencana = oResponsealue.isNull("id_user_input_rencana") ? null
                            : oResponsealue.getString("id_user_input_rencana");
                    String keterangan = oResponsealue.isNull("keterangan") ? null
                            : oResponsealue.getString("keterangan");
                    String aproved = oResponsealue.isNull("aproved") ? null
                            : oResponsealue.getString("aproved");
                    Log.d(LOG_TAG, "id_rencana_header:" + id_rencana_header);
                    Log.d(LOG_TAG, "nomor_rencana:" + nomor_rencana);
                    databaseHandler.addMasterRencana(new MasterRencana(Integer.parseInt(id_rencana_header),nomor_rencana,
                            tanggal_penetapan,tanggal_rencana,Integer.parseInt(id_user_input_rencana),keterangan,aproved));
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

    public void saveAppDataRencanaDetail(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_TABLE_RENCANA_DETAIL, responsedata);
        editor.commit();
    }

    public void saveAppDataRencanaMaster(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_TABLE_RENCANA_MASTER, responsedata);
        editor.commit();
    }

    public void saveAppDataRencanaDetailSameData(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_TABLE_RENCANA_DETAIL_SAME_DATA,
                responsedata);
        editor.commit();
    }

    public void saveAppDataRencanaMasterSameData(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_TABLE_RENCANA_MASTER_SAME_DATA,
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

    public void showCustomDialogSuccess(String msg) {
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
                                viewPager = (ViewPager) findViewById(R.id.viewpager);
                                setupViewPager(viewPager);
                                tabLayout = (TabLayout) findViewById(R.id.tabs);
                                tabLayout.setupWithViewPager(viewPager);
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void showDataOnserverNull(String msg) {
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

    /*
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        //tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }
    */

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new OneFragment1(), "Check In");
        adapter.addFrag(new DataSapiFragment(), "Data Sapi");
        adapter.addFrag(new PakanFragment(), "Pakan");
        adapter.addFrag(new PengobatanFragment(), "Pengobatan");
        adapter.addFrag(new CheckoutFragment(), "Check Out");
        viewPager.setAdapter(adapter);

        SharedPreferences spPreferences = getSharedPrefereces();
//        String status_checkin = spPreferences.getString(AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_STATUS, null);
        idrencanaDetail = Integer.parseInt(spPreferences.getString(AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_JADWAL, null));
        ArrayList<DetailRencana> rencana_list = databaseHandler.getAlldetailRencanaParam(idrencanaDetail);
        rencanaDetail = new DetailRencana();
        for (DetailRencana detailRencana : rencana_list)
            rencanaDetail = detailRencana;
        int id_status =rencanaDetail.getStatus_rencana();
        if(id_status==1){
            viewPager.setCurrentItem(1);
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

//    @Override
//    public void onNavigationDrawerItemSelected(int position) {
//        if (mNavigationDrawerFragment != null) {
//            if (mNavigationDrawerFragment.getCurrentSelectedPosition() != 1) {
//                if (position == 0) {
//                    Intent intentActivity = new Intent(this,
//                            DashboardActivity.class);
//                    startActivity(intentActivity);
//                    finish();
//                }else if (position == 2) {
//                    Intent intentActivity = new Intent(this,
//                            CheckoutActivity.class);
//                    startActivity(intentActivity);
//                    finish();
//                }else if (position == 3) {
//                    Intent intentActivity = new Intent(this,
//                            History_Canvassing.class);
//                    startActivity(intentActivity);
//                    finish();
//                }else if (position == 4) {
//                    Intent intentActivity = new Intent(this,
//                            ChangePassword.class);
//                    startActivity(intentActivity);
//                    finish();
//                }else if (position == 5) {
//                    Intent intentActivity = new Intent(this,
//                            Orderan.class);
//                    startActivity(intentActivity);
//                    finish();
//                }
//            }
//        }
//    }

    public void gotoDashboard(){
        Intent intentActivity = new Intent(this,
                DashboardActivity.class);
        startActivity(intentActivity);
        finish();
    }

    public void showCustomDialogExit() {
        String msg = getApplicationContext().getResources().getString(
                R.string.MSG_DLG_LABEL_EXIT_DIALOG);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                act);
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setNegativeButton(
                        getApplicationContext().getResources().getString(
                                R.string.MSG_DLG_LABEL_YES),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                android.os.Process
                                        .killProcess(android.os.Process.myPid());

                            }
                        })
                .setPositiveButton(
                        getApplicationContext().getResources().getString(
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            gotoDashboard();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
