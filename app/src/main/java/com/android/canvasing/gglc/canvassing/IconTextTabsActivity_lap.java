package com.android.canvasing.gglc.canvassing;

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

import com.android.canvasing.gglc.absen.AbsenActivity;
import com.android.canvasing.gglc.absen.AppVar;
import com.android.canvasing.gglc.absen.ChangePassword;
import com.android.canvasing.gglc.absen.History_absen;
import com.android.canvasing.gglc.absen.NavigationDrawerCallbacks;
import com.android.canvasing.gglc.absen.NavigationDrawerFragment;
import com.android.canvasing.gglc.database.DatabaseHandler;
import com.android.canvasing.gglc.database.DetailRencana;
import com.android.canvasing.gglc.fragment.OneFragment;
import com.android.canvasing.gglc.fragment.ThreeFragmentweb_view;
import com.android.canvasing.mobile.R;

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

import static com.android.canvasing.gglc.absen.AppVar.SHARED_PREFERENCES_NAME;

public class IconTextTabsActivity_lap extends AppCompatActivity implements NavigationDrawerCallbacks{
    private Toolbar mToolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Context act;
    private DatabaseHandler databaseHandler;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog;
    private String message, response_data, msg_success;
    private static final String LOG_TAG = IconTextTabsActivity_lap.class.getSimpleName();
    //private int[] tabIcons = {
     //       R.drawable.ic_tab_contacts,
     //       R.drawable.ic_tab_call,
     //       //R.drawable.ic_tab_contacts
    //};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_text_tabs);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.title_activity_all);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseHandler = new DatabaseHandler(this);
        act = this;

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getApplicationContext().getResources()
                .getString(R.string.app_name));
        progressDialog.setMessage("Downloading...");
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer,
                (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        mNavigationDrawerFragment.selectItem(3);

        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //setupTabIcons();
        if(databaseHandler.getCountDetailrencana()==0){
           new DownloadDataRencanaDetail().execute();
        }else{
            viewPager = (ViewPager) findViewById(R.id.viewpager);
            setupViewPager(viewPager);
            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

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
                    + AppVar.CONFIG_APP_URL_DOWNLOAD_RENCANA_DETAIL+ "?id_karyawan="
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
                msg_success=act.getApplicationContext().getResources()
                        .getString(R.string.MSG_DLG_LABEL_SYNRONISASI_DATA_SUCCESS);
                showCustomDialogSuccess(msg_success);
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
                    Log.d(LOG_TAG, "id_rencana_detail:" + id_rencana_detail);
                    Log.d(LOG_TAG, "id_rencana_header:" + id_rencana_header);
                    Log.d(LOG_TAG, "id_kegiatan:" + id_kegiatan);
                    Log.d(LOG_TAG, "id_customer:" + id_customer);
                    databaseHandler.addDetailRencana(new DetailRencana(Integer.parseInt(id_rencana_detail),Integer.parseInt(id_rencana_header),
                            Integer.parseInt(id_kegiatan),Integer.parseInt(id_customer),Integer.parseInt(id_karyawan),Integer.parseInt(status_rencana)));
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

    public void saveAppDataRencanaDetailSameData(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AppVar.SHARED_PREFERENCES_TABLE_RENCANA_DETAIL_SAME_DATA,
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
                                viewPager = (ViewPager) findViewById(R.id.viewpager);
                                setupViewPager(viewPager);
                                tabLayout = (TabLayout) findViewById(R.id.tabs);
                                tabLayout.setupWithViewPager(viewPager);
                                alertDialog.dismiss();
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
        adapter.addFrag(new OneFragment(), "Register Customer");
        adapter.addFrag(new ThreeFragmentweb_view(), "Unregister Customer");
        //adapter.addFrag(new ThreeFragment(), "THREE");
        viewPager.setAdapter(adapter);
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

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (mNavigationDrawerFragment != null) {
            if (mNavigationDrawerFragment.getCurrentSelectedPosition() != 3) {
                if (position == 0) {
                    Intent intentActivity = new Intent(this,
                            AbsenActivity.class);
                    startActivity(intentActivity);
                    finish();
                }else if (position == 1) {
                    Intent intentActivity = new Intent(this,
                            History_absen.class);
                    startActivity(intentActivity);
                    finish();
                } else if (position == 2) {
                    Intent intentActivity = new Intent(this,
                            ChangePassword.class);
                    startActivity(intentActivity);
                    finish();
                }else if (position == 4) {
                    Intent intentActivity = new Intent(this,
                            CheckoutActivity.class);
                    startActivity(intentActivity);
                    finish();
                }/*else if (position == 3) {
                    Intent intentActivity = new Intent(this,
                            CheckoutActivity.class);
                    startActivity(intentActivity);
                    finish();
                }else if (position == 2) {
					Intent intentActivity = new Intent(this,
							ChangePassword.class);
					startActivity(intentActivity);
					finish();
				}*/
            }
        }


    }
}
