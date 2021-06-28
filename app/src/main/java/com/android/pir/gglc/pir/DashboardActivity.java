package com.android.pir.gglc.pir;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pir.gglc.absen.AppVar;
import com.android.pir.gglc.absen.ChangePassword;
import com.android.pir.gglc.absen.NavigationDrawerCallbacks;
import com.android.pir.gglc.absen.NavigationDrawerFragment;
import com.android.pir.gglc.absen.Orderan;
import com.android.pir.gglc.database.DataSapi;
import com.android.pir.gglc.database.DatabaseHandler;
import com.android.pir.gglc.database.DetailRencana;
import com.android.pir.gglc.database.DetailReqLoadNew;
import com.android.pir.gglc.database.FeedbackPakan;
import com.android.pir.gglc.database.MasterRencana;
import com.android.pir.gglc.database.Pengobatan;
import com.android.pir.gglc.database.Rencana;
import com.android.pir.gglc.database.MstUser;
import com.android.pir.gglc.database.Mst_Customer;
import com.android.pir.gglc.database.Trx_Checkin;
import com.android.pir.gglc.database.Trx_Checkout;
import com.android.pir.gglc.database.UploadDataSapi;
import com.android.pir.gglc.fragment1.OneFragment1;
import com.android.pir.mobile.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.android.pir.gglc.absen.AppVar.SHARED_PREFERENCES_NAME;

@SuppressWarnings("deprecation")
public class DashboardActivity extends ActionBarActivity implements
		NavigationDrawerCallbacks {
	private Context act;
	private Toolbar mToolbar;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private DatabaseHandler databaseHandler;
	private ListView listview;
	private LinearLayout layout;
//	private ArrayList<Absen> customer_list = new ArrayList<Absen>();
	private ArrayList<Rencana> rencana_list = new ArrayList<Rencana>();
	private ListViewAdapter cAdapter;
	private ProgressDialog progressDialog;
	private Handler handler = new Handler();
	private String message,msg_success;
	private String response_data;
	private String response_data3;
	private String response_dataFeedback;
	private String response_data4;
	private String response_data1;
	private String response_data2;
	private static final String LOG_TAG = DashboardActivity.class.getSimpleName();
	private EditText description_kegiatan;
	private Spinner checkin_number;
	private ArrayList<Trx_Checkin> checkinNumber;
	private ArrayList<String> checkinNumberStringList;
	private int id_checkin = 0;
	private String nomor_checkin_ = null;
	private int id_user;
	private String nama_user;
	private MstUser user;
	private Typeface typefaceSmall;
	private TextView jml_ckin,jml_rcn,percent;
    private String response;
//    private Absen customer;
	private Button chat,map;
	private EditText mulai,sampai;
//	private Button checkin,checkout;
	final int Date_Dialog_ID=0;
	final int Date_Dialog_ID1=1;
	int cDay,cMonth,cYear;
	Calendar cDate;
	int sDay,sMonth,sYear;
	private double latitude, longitude;
	private UploadDataSapi uploadDataSapi;
	private Location location,location1,location2;
	protected LocationManager locationManager;
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	private ViewPager viewPager;
	private int id=0;
	private ArrayList<FeedbackPakan> uploadFeedbackArrayList = new ArrayList<FeedbackPakan>();
	private ArrayList<UploadDataSapi> uploadDataSapiArrayList = new ArrayList<UploadDataSapi>();
	private ArrayList<Pengobatan> pengobatanArrayList = new ArrayList<Pengobatan>();
	private ArrayList<Trx_Checkin> checkinArrayList = new ArrayList<Trx_Checkin>();
	private ArrayList<Trx_Checkout> checkoutArrayList = new ArrayList<Trx_Checkout>();
	private String IMAGE_DIRECTORY_NAME_PAKAN = "Pakan";
	private String IMAGE_DIRECTORY_NAME = "Data_sapi";
	private String IMAGE_DIRECTORY_PENGOBATAN = "Pengobatan";
	private String IMAGE_DIRECTORY_CHECKIN_NAME = "Checkin_pir";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main_dashboard);

		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle(R.string.title_activity_all);
		act = this;

		listview = (ListView) findViewById(R.id.list);
		listview.setItemsCanFocus(false);

		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(getApplicationContext().getResources()
				.getString(R.string.app_name));
		progressDialog.setMessage("Downloading yaa...");
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.fragment_drawer);
		mNavigationDrawerFragment.setup(R.id.fragment_drawer,
				(DrawerLayout) findViewById(R.id.drawer), mToolbar);
		databaseHandler = new DatabaseHandler(this);
		mNavigationDrawerFragment.selectItem(3);

		ArrayList<MstUser> staff_list = databaseHandler.getAllUser();
		user = new MstUser();
		for (MstUser tempStaff : staff_list)
			user = tempStaff;
		id_user=user.getId_user();
		nama_user=user.getNama();
		updateContentRefreshRencana();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main4, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_refresh:
				if (GlobalApp.checkInternetConnection(act)) {
					int countChk = databaseHandler.getCountAllTrxcheckin();
					if(countChk==0){
						new DownloadDataCustomer().execute();
					}else{
						showCustomDialog("Ada data ofline yang masih tersimpan di HP, lakukan upload terlebih dahulu sebelum memperbarui data petani.");
					}
				} else {
					String message = act.getApplicationContext().getResources()
							.getString(R.string.app_customer_processing_empty);
					showCustomDialog(message);
				}
				return true;
			case R.id.menu_upload:
				int CountDs = databaseHandler.getCountUploadAllDataSapi();
				int CountPg = databaseHandler.getCountPengobatan();
				int CountFP = databaseHandler.getCountAllFeedback();
				int stts = databaseHandler.getCountDetailRencanaSelesai();
				if(stts>0){
					if(CountFP>0){
						new UploadDataIMGPakan().execute();
					}else {
						if (CountDs > 0) {
							new UploadDataIMG().execute();
						} else {
							if (progressDialog != null) {
								progressDialog.dismiss();
							}
							if (CountPg > 0) {
								new UploadDataPengobatan().execute();
							} else {
								new UploadData().execute();
							}
						}
					}
				}else{
					showCustomDialog("Belum ada data visit yang terselesaikan, silahkan selesaikan visit terlebih dahulu untuk upload.");
				}

//				new UploadDataCkout().execute();
//				Toast.makeText(DashboardActivity.this, "upload",Toast.LENGTH_LONG).show();
//				if (GlobalApp.checkInternetConnection(act)) {
//					new DownloadDataCustomer().execute();
//				} else {
//					String message = act.getApplicationContext().getResources()
//							.getString(R.string.app_customer_processing_empty);
//					showCustomDialog(message);
//				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void updateContentRefreshRencana() {
		rencana_list.clear();
		ArrayList<Rencana> rencana_from_db = databaseHandler
				.getAllRencana();
		if(databaseHandler.getCountDetailrencana()==0){
			new DownloadDataCustomer().execute();
		}

		if (rencana_from_db.size() > 0) {
			listview.setVisibility(View.VISIBLE);
			for (int i = 0; i < rencana_from_db.size(); i++) {
				int id_rencana_detail = rencana_from_db.get(i).getId_rencana_detail();
				String nama_customer = rencana_from_db.get(i).getNama_customer();
				String alamat = rencana_from_db.get(i).getAlamat();
				int status_renana = rencana_from_db.get(i).getStatus();
				String indnr = rencana_from_db.get(i).getIndnr();

				Rencana rencana = new Rencana();
				rencana.setId_rencana_detail(id_rencana_detail);
				rencana.setNama_customer(nama_customer);
				rencana.setAlamat(alamat);
				rencana.setStatus(status_renana);
				rencana.setIndnr(indnr);

				rencana_list.add(rencana);
			}
		} else {
			listview.setVisibility(View.INVISIBLE);
		}
		showListRencana();
	}

	public class UploadDataIMG extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog
					.setMessage(getApplicationContext()
							.getResources()
							.getString(
									R.string.app_supplier_processing));
			progressDialog.show();
			progressDialog.setCancelable(false);
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
			int cekDS = databaseHandler.getCountUploadAllDataSapi();
			if(cekDS>0){
				String url = AppVar.CONFIG_APP_URL_PUBLIC;
				String uploadSupplier = AppVar.CONFIG_APP_URL_UPLOAD_INSERT_DATA_SAPI_ALL;
				String upload_image_supplier_url = url + uploadSupplier;

				ArrayList<UploadDataSapi> staff_list = databaseHandler.getAllUploadDataSapi();
				for(UploadDataSapi detailReqLoadNew : staff_list){
					updateListViewDetailOrder(detailReqLoadNew);
				}

				for (UploadDataSapi uploadDataSapi : uploadDataSapiArrayList) {
					response_data3 = uploadImageAll(upload_image_supplier_url,
							String.valueOf(uploadDataSapi.getId_rencana_detail()),
							uploadDataSapi.getAssessment(),
							uploadDataSapi.getEartag(),
							uploadDataSapi.getKeterangan(),
							uploadDataSapi.getFoto(),
							uploadDataSapi.getTanggal());
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(LOG_TAG, "response:" + response_data3);
            if (response_data3 != null && response_data3.length() > 0) {
                if (response_data3.startsWith("Error occurred")) {
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

	public class UploadDataIMGPakan extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog
					.setMessage(getApplicationContext()
							.getResources()
							.getString(
									R.string.app_supplier_processing));
			progressDialog.show();
			progressDialog.setCancelable(false);
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
			int cekFP = databaseHandler.getCountAllFeedback();
			if(cekFP>0){
				String url = AppVar.CONFIG_APP_URL_PUBLIC;
				String uploadSupplier = AppVar.CONFIG_APP_URL_UPLOAD_INSERT_DATA_PAKAN;
				String upload_image_supplier_url = url + uploadSupplier;

				ArrayList<FeedbackPakan> feedback_list = databaseHandler.getAllFeedbackPakan();
				for(FeedbackPakan feedbackPakan : feedback_list){
					updateListViewDetailFeedback(feedbackPakan);
				}

				for (FeedbackPakan uploadDataSapi : uploadFeedbackArrayList) {
					response_dataFeedback = uploadImageAllPakan(upload_image_supplier_url,
							String.valueOf(uploadDataSapi.getId_rencana_detail()),
							uploadDataSapi.getFeedback_pakan(),
							uploadDataSapi.getFoto());
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            Log.d(LOG_TAG, "response:" + response_dataFeedback);
            if (response_dataFeedback != null && response_dataFeedback.length() > 0) {
                if (response_dataFeedback.startsWith("Error occurred")) {
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
                            extractUploadPakan();
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

	public class UploadDataPengobatan extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog
					.setMessage(getApplicationContext()
							.getResources()
							.getString(
									R.string.app_supplier_processing));
			progressDialog.show();
			progressDialog.setCancelable(false);
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
			int cekPG = databaseHandler.getCountPengobatan();
			if(cekPG>0){
				String url = AppVar.CONFIG_APP_URL_PUBLIC;
				String uploadSupplier = AppVar.CONFIG_APP_URL_UPLOAD_INSERT_DATA_PENGOBATAN;
				String upload_image_supplier_url = url + uploadSupplier;

				ArrayList<Pengobatan> staff_list = databaseHandler.getAllPengobatan();
				for(Pengobatan detailReqLoadNew : staff_list){
					updateListViewPengobatan(detailReqLoadNew);
				}

				for (Pengobatan uploadDataSapi : pengobatanArrayList) {
					response_data4 = uploadImagePengobatan(upload_image_supplier_url,
							String.valueOf(uploadDataSapi.getId_rencana_detail()),
							uploadDataSapi.getKode_obat(),
							uploadDataSapi.getQty(),
							uploadDataSapi.getFoto_pengobatan(),
							uploadDataSapi.getTanggal());
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(LOG_TAG, "response:" + response_data4);
            if (response_data4 != null && response_data4.length() > 0) {
                if (response_data4.startsWith("Error occurred")) {
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
                            extractUploadPengobatan();
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

	private void updateListViewDetailFeedback(FeedbackPakan detailReqLoad) {
		uploadFeedbackArrayList.add(detailReqLoad);
	}
	private void updateListViewDetailOrder(UploadDataSapi detailReqLoad) {
		uploadDataSapiArrayList.add(detailReqLoad);
	}
	private void updateListViewPengobatan(Pengobatan detailReqLoad) {
		pengobatanArrayList.add(detailReqLoad);
	}
	private void updateListCheckin(Trx_Checkin detailReqLoad) {
		checkinArrayList.add(detailReqLoad);
	}
	private void updateListCheckout(Trx_Checkout detailReqLoad) {
		checkoutArrayList.add(detailReqLoad);
	}

	protected String uploadImageAll(final String url, final String id_rencana_detail, final String data_assessment,final String data_eartag, final String data_keterangan,
									final String foto, final String tanggal) {
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
			if (!dir.exists()) {
				dir.mkdirs();
			}
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
			entity.addPart("tanggal", new StringBody(tanggal));
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

	protected String uploadImageAllPakan(final String url, final String id_rencana_detail, final String feedback,final String foto) {
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
			File dir = new File(AppVar.getFolderPath() + "/"+ IMAGE_DIRECTORY_NAME_PAKAN + "/"+ foto);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			if (dir.exists() && foto != null) {
				entity.addPart("image_1", new FileBody(dir));
				entity.addPart("foto1", new StringBody(foto));
			} else {
				entity.addPart("foto1", new StringBody(""));
			}
			entity.addPart("id_rencana_detail", new StringBody(id_rencana_detail));
			entity.addPart("feedback", new StringBody(feedback));
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

	protected String uploadImagePengobatan(final String url, final String id_rencana_detail, final String kode_obat,final int qty, final String foto, final String tanggal) {
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
			File dir = new File(AppVar.getFolderPath() + "/"+ IMAGE_DIRECTORY_PENGOBATAN + "/"+ foto);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			if (dir.exists() && foto != null) {
				entity.addPart("image_pengobatan", new FileBody(dir));
				entity.addPart("foto_pengobatan", new StringBody(foto));
			} else {
				entity.addPart("foto_pengobatan", new StringBody(""));
			}
			entity.addPart("id_rencana_detail", new StringBody(id_rencana_detail));
			entity.addPart("kode_obat", new StringBody(kode_obat));
			entity.addPart("qty", new StringBody(String.valueOf(qty)));
			entity.addPart("tanggal", new StringBody(tanggal));
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

	public void extractUpload() {
		JSONObject oResponse;
		try {
			oResponse = new JSONObject(response_data3);
			String status = oResponse.isNull("error") ? "True" : oResponse
					.getString("error");
			if (response_data3.isEmpty()) {
				final String msg = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_supplier_processing_failed);
				showCustomDialog(msg);
			} else {
				Log.d(LOG_TAG, "status=" + status);
				if (status.equalsIgnoreCase("False")) {
					File dir = new File(AppVar.getFolderPath() + "/"
							+ IMAGE_DIRECTORY_NAME);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					List<File> fileFoto = getListFiles(dir);
					for (File tempFile : fileFoto) {
						tempFile.delete();
					}
					databaseHandler.deleteTableUploadDataSapi();
					if (progressDialog != null) {
						progressDialog.dismiss();
					}
					int CountPg = databaseHandler.getCountPengobatan();
					if(CountPg>0){
						new UploadDataPengobatan().execute();
					}else{
						new UploadData().execute();
					}
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

	public void extractUploadPakan() {
		JSONObject oResponse;
		try {
			oResponse = new JSONObject(response_dataFeedback);
			String status = oResponse.isNull("error") ? "True" : oResponse
					.getString("error");
			if (response_dataFeedback.isEmpty()) {
				final String msg = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_supplier_processing_failed);
				showCustomDialog(msg);
			} else {
				Log.d(LOG_TAG, "status=" + status);
				if (status.equalsIgnoreCase("False")) {
					File dir = new File(AppVar.getFolderPath() + "/"
							+ IMAGE_DIRECTORY_NAME_PAKAN);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					List<File> fileFoto = getListFiles(dir);
					for (File tempFile : fileFoto) {
						tempFile.delete();
					}
					databaseHandler.deleteTableFeedbackPakan();
					if (progressDialog != null) {
						progressDialog.dismiss();
					}
					int CountDs = databaseHandler.getCountUploadAllDataSapi();
					int CountPg = databaseHandler.getCountPengobatan();
					if (CountDs > 0) {
						new UploadDataIMG().execute();
					} else {
						if (progressDialog != null) {
							progressDialog.dismiss();
						}
						if(CountPg>0){
							new UploadDataPengobatan().execute();
						}else{
							new UploadData().execute();
						}
					}
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

	public void extractUploadPengobatan() {
		JSONObject oResponse;
		try {
			oResponse = new JSONObject(response_data4);
			String status = oResponse.isNull("error") ? "True" : oResponse
					.getString("error");
			if (response_data4.isEmpty()) {
				final String msg = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_supplier_processing_failed);
				showCustomDialog(msg);
			} else {
				Log.d(LOG_TAG, "status=" + status);
				if (status.equalsIgnoreCase("False")) {
					File dir = new File(AppVar.getFolderPath() + "/"
							+ IMAGE_DIRECTORY_PENGOBATAN);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					List<File> fileFoto = getListFiles(dir);
					for (File tempFile : fileFoto) {
						tempFile.delete();
					}
					databaseHandler.deleteTablePengobatan();
					if (progressDialog != null) {
						progressDialog.dismiss();
					}
					new UploadData().execute();
//					int CountPg = databaseHandler.getCountPengobatan();
//					if(CountPg>0){
//						new UploadDataPengobatan().execute();
//					}else{
//						new UploadData().execute();
//					}
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

	public class UploadData extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog
					.setMessage(getApplicationContext()
							.getResources()
							.getString(
									R.string.app_supplier_processing));
			progressDialog.show();
			progressDialog.setCancelable(false);
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
            String url = AppVar.CONFIG_APP_URL_PUBLIC;
            String uploadSupplier = AppVar.CONFIG_APP_URL_UPLOAD_INSERT_CHECKIN_NEW;
            String upload_image_supplier_url = url + uploadSupplier;

			ArrayList<Trx_Checkin> staff_list = databaseHandler.getAllCheckin();
			for(Trx_Checkin detailReqLoadNew : staff_list){
				updateListCheckin(detailReqLoadNew);
			}

//			String nilaiVal = "";
			for (Trx_Checkin uploadcheckin : checkinArrayList) {
//				nilaiVal = uploadcheckin.getFoto();
//				Log.d(LOG_TAG, "doInBackground: "+nilaiVal);
//				Toast.makeText(DashboardActivity.this, "Nilai Val :("+nilaiVal+")", Toast.LENGTH_LONG).show();
				if(uploadcheckin.getFoto()!=null) {
					response_data1 = uploadImage(upload_image_supplier_url, String.valueOf(id_user), String.valueOf(uploadcheckin.getId_rencana_detail()), uploadcheckin.getLats(), uploadcheckin.getLongs(),uploadcheckin.getTanggal_checkin(),uploadcheckin.getFoto());
				}else{
					response_data1 = uploadImage1(upload_image_supplier_url, String.valueOf(id_user), String.valueOf(uploadcheckin.getId_rencana_detail()), uploadcheckin.getLats(), uploadcheckin.getLongs(),uploadcheckin.getTanggal_checkin());
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
            Log.d(LOG_TAG, "response:" + response_data1);
            if (response_data1 != null && response_data1.length() > 0) {
                if (response_data1.startsWith("Error occurred")) {
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
//                            new DownloadDataRencanaDetail().execute();
//                            SharedPreferences spPreferences = getSharedPrefereces();
//                            String foto = spPreferences.getString(AppVar.SHARED_PREFERENCES_CHILLER_FOTO_1, null);
//                            Log.d(LOG_TAG, "foto : "+ foto);
//                            if(foto!=null) {
//                                extractUpload();
//                            }
							resetIMG();
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

	protected String uploadImage(final String url,  final String id_user,
								 final String id_rencana_detail, final String lats, final String longs,final String tanggal, final String foto_1) {
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
					+ IMAGE_DIRECTORY_CHECKIN_NAME + "/"
					+ foto_1);
			if (!dir1.exists()) {
				dir1.mkdirs();
			}
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
			entity.addPart("tanggal", new StringBody(tanggal));
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
								  final String id_rencana_detail, final String lats, final String longs, final String tanggal) {
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
			entity.addPart("tanggal", new StringBody(tanggal));
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

	public void resetIMG() {
		JSONObject oResponse;
		try {
			oResponse = new JSONObject(response_data1);
			String status = oResponse.isNull("error") ? "True" : oResponse
					.getString("error");
			if (response_data1.isEmpty()) {
				final String msg = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_supplier_processing_failed);
				showCustomDialog(msg);
			} else {
				Log.d(LOG_TAG, "status=" + status);
				if (status.equalsIgnoreCase("False")) {
					File dir = new File(AppVar.getFolderPath() + "/"
							+ IMAGE_DIRECTORY_CHECKIN_NAME);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					List<File> fileFoto = getListFiles(dir);
					for (File tempFile : fileFoto) {
						tempFile.delete();
					}
					if (progressDialog != null) {
						progressDialog.dismiss();
					}
					databaseHandler.deleteCheckin();
					new UploadDataCkout().execute();
//					final String msg = act
//							.getApplicationContext()
//							.getResources()
//							.getString(
//									R.string.app_supplier_processing_sukses);
//					showCustomDialog(msg);
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

	public class UploadDataCkout extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog
					.setMessage(getApplicationContext()
							.getResources()
							.getString(
									R.string.app_supplier_processing));
			progressDialog.show();
			progressDialog.setCancelable(false);
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
            String url = AppVar.CONFIG_APP_URL_PUBLIC;
            String uploadSupplier = AppVar.CONFIG_APP_URL_UPLOAD_INSERT_CHECKOUT;
            String upload_image_supplier_url = url + uploadSupplier;

			ArrayList<Trx_Checkout> staff_list = databaseHandler.getAllCheckout();
			for(Trx_Checkout detailReqLoadNew : staff_list){
				updateListCheckout(detailReqLoadNew);
			}

			for (Trx_Checkout dataCheckout : checkoutArrayList) {
				response_data2 = uploadCheckout(upload_image_supplier_url,
						String.valueOf(dataCheckout.getId_user()),
						String.valueOf(dataCheckout.getId_rencana_detail()),
						dataCheckout.getLats(),
						dataCheckout.getLongs(),
						dataCheckout.getRealisasi_kegiatan(),
						dataCheckout.getTanggal_checkout()
				);
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(LOG_TAG, "response:" + response_data2);
            if (response_data2 != null && response_data2.length() > 0) {
                if (response_data2.startsWith("Error occurred")) {
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
                            extractUploadCkout();
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

	public void extractUploadCkout() {
		JSONObject oResponse;
		try {
			oResponse = new JSONObject(response_data2);
			String status = oResponse.isNull("error") ? "True" : oResponse
					.getString("error");
			if (response_data2.isEmpty()) {
				final String msg = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_supplier_processing_failed);
				showCustomDialog(msg);
			} else {
				Log.d(LOG_TAG, "status=" + status);
				if (status.equalsIgnoreCase("False")) {
					databaseHandler.deleteCheckout();
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_supplier_processing_sukses);
					showCustomDialog(msg);
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

	protected String uploadCheckout(final String url,  final String id_user,final String id_rencana_detail, final String lats,
								 final String longs, final String keterangan_, final String tanggal_) {
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
			entity.addPart("keterangan", new StringBody(keterangan_));
			entity.addPart("tanggal", new StringBody(tanggal_));
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

	public void showListRencana() {
		rencana_list.clear();
//		SharedPreferences spPreferences = getSharedPrefereces();
		ArrayList<Rencana> rencana_from_db = null;
		rencana_from_db = databaseHandler.getAllRencana();

		if (rencana_from_db.size() > 0) {
			listview.setVisibility(View.VISIBLE);
			for (int i = 0; i < rencana_from_db.size(); i++) {
				int id_rencana_detail = rencana_from_db.get(i).getId_rencana_detail();
				String nama_customer = rencana_from_db.get(i).getNama_customer();
				String alamat = rencana_from_db.get(i).getAlamat();
				int status = rencana_from_db.get(i).getStatus();
				String tanggal_rencana = rencana_from_db.get(i).getTanggal_rencana();
				String indnr = rencana_from_db.get(i).getIndnr();

				Rencana rencana = new Rencana();
				rencana.setId_rencana_detail(id_rencana_detail);
				rencana.setNama_customer(nama_customer);
				rencana.setAlamat(alamat);
				rencana.setStatus(status);
				rencana.setTanggal_rencana(tanggal_rencana);
				rencana.setIndnr(indnr);

				rencana_list.add(rencana);
			}

			cAdapter = new ListViewAdapter(this, R.layout.list_item_customer_rencana,
					rencana_list);
			listview.setAdapter(cAdapter);
			cAdapter.notifyDataSetChanged();
		} else {
			listview.setVisibility(View.INVISIBLE);
		}
	}

	private class DownloadDataCustomer extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog.setMessage(getApplicationContext().getResources()
					.getString(R.string.MSG_DLG_LABEL_SYNRONISASI_DATA));
			progressDialog.show();
			progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
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
			String download_data_url = AppVar.CONFIG_APP_URL_PUBLIC
					+ AppVar.CONFIG_APP_URL_DOWNLOAD_CUSTOMER_NEW+ "?id_user="+1797;
			HttpResponse response = getDownloadData(download_data_url);
			int retCode = (response != null) ? response.getStatusLine().getStatusCode() : -1;
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
							AppVar.SHARED_PREFERENCES_TABLE_MST_CUSTOMER, null);
					if (main_app_table_data != null) {
						if (main_app_table_data.equalsIgnoreCase(response_data)) {
							saveAppDataBranchSameData(act
									.getApplicationContext().getResources()
									.getString(R.string.app_value_true));
						} else {
							databaseHandler.deleteTableMSTCustomer();
							saveAppDataBranchSameData(act
									.getApplicationContext().getResources()
									.getString(R.string.app_value_false));
						}
					} else {
						databaseHandler.deleteTableMSTCustomer();
						saveAppDataBranchSameData(act.getApplicationContext()
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
				saveAppDataBranch(response_data);
				extractDataBranch();
				new DownloadDataRencanaMaster1().execute();
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

	public void extractDataBranch() {
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_table_same_data = spPreferences.getString(
				AppVar.SHARED_PREFERENCES_TABLE_MST_CUSTOMER_SAME_DATA, null);
		String main_app_table = spPreferences.getString(
				AppVar.SHARED_PREFERENCES_TABLE_MST_CUSTOMER, null);
		if (main_app_table_same_data.equalsIgnoreCase(act
				.getApplicationContext().getResources()
				.getString(R.string.app_value_false))) {
			JSONObject oResponse;
			try {
				oResponse = new JSONObject(main_app_table);
				JSONArray jsonarr = oResponse.getJSONArray("customer");
				for (int i = 0; i < jsonarr.length(); i++) {
					JSONObject oResponsealue = jsonarr.getJSONObject(i);
					String id_customer = oResponsealue.isNull("lifnr") ? null
							: oResponsealue.getString("lifnr");
					String kode_customer = oResponsealue.isNull("lifnr") ? null
							: oResponsealue.getString("lifnr");
					String nama_customer = oResponsealue.isNull("name1") ? null
							: oResponsealue.getString("name1");
					String alamat = oResponsealue.isNull("desa") ? null
							: oResponsealue.getString("desa");
					String no_hp = oResponsealue.isNull("veraa_user") ? null
							: oResponsealue.getString("veraa_user");
					String indnr = oResponsealue.isNull("indnr") ? null
							: oResponsealue.getString("indnr");
					String lats = "0";
					String longs = "0";
					String id_wilayah = "3010";
					Log.d(LOG_TAG, "id_customer:" + id_customer);
					Log.d(LOG_TAG, "kode_customer:" + kode_customer);
					Log.d(LOG_TAG, "nama_customer:" + nama_customer);
					databaseHandler.addMst_customer(new Mst_Customer(Integer.parseInt(id_customer),kode_customer,nama_customer,alamat,no_hp,lats,longs,Integer.parseInt(id_wilayah),indnr));
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

	private class DownloadDataRencanaMaster1 extends AsyncTask<String, Integer, String> {
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
			ArrayList<MstUser> staff_list = databaseHandler.getAllUser();
			user = new MstUser();
			for (MstUser tempStaff : staff_list)
				user = tempStaff;
			id_user=user.getId_user();

			String download_data_url = AppVar.CONFIG_APP_URL_PUBLIC
					+ AppVar.CONFIG_APP_URL_DOWNLOAD_RENCANA_MASTER_ONLY+ "?id_karyawan="+id_user;
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
				saveAppDataRencanaMaster(response_data);
				extractDataRencanaMaster();
				new DownloadDataRencanaDetail().execute();
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
					Log.d(LOG_TAG, "tanggal_penetapan:" + tanggal_penetapan);
					Log.d(LOG_TAG, "tanggal_rencana:" + tanggal_rencana);
					Log.d(LOG_TAG, "id_user_input_rencana:" + id_user_input_rencana);
					Log.d(LOG_TAG, "keterangan:" + keterangan);
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
				new DownloadDataSapi().execute();
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

	private class DownloadDataSapi extends AsyncTask<String, Integer, String> {
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
			String download_data_url = AppVar.CONFIG_APP_URL_PUBLIC
					+ AppVar.CONFIG_APP_URL_DOWNLOAD_DATA_SAPI;
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
							AppVar.SHARED_PREFERENCES_TABLE_DATA_SAPI, null);
					if (main_app_table_data != null) {
						if (main_app_table_data.equalsIgnoreCase(response_data)) {
							saveAppDataDataSapiSameData(act
									.getApplicationContext().getResources()
									.getString(R.string.app_value_true));
						} else {
							databaseHandler.deleteTableDataSapi();
							saveAppDataDataSapiSameData(act
									.getApplicationContext().getResources()
									.getString(R.string.app_value_false));
						}
					} else {
						databaseHandler.deleteTableDataSapi();
						saveAppDataDataSapiSameData(act.getApplicationContext()
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
				saveAppDataDataSapi(response_data);
				extractDataDataSapi();
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				msg_success=act.getApplicationContext().getResources()
						.getString(R.string.MSG_DLG_LABEL_SYNRONISASI_DATA_SUCCESS);
				showCustomDialog(msg_success);
				listview.setVisibility(View.VISIBLE);
				showListRencana();
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

	public void saveAppDataDataSapiSameData(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_TABLE_DATA_SAPI_SAME_DATA,
				responsedata);
		editor.commit();
	}
	public void extractDataDataSapi() {
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_table_same_data = spPreferences.getString(
				AppVar.SHARED_PREFERENCES_TABLE_DATA_SAPI_SAME_DATA, null);
		String main_app_table = spPreferences.getString(
				AppVar.SHARED_PREFERENCES_TABLE_DATA_SAPI, null);
		if (main_app_table_same_data.equalsIgnoreCase(act
				.getApplicationContext().getResources()
				.getString(R.string.app_value_false))) {
			JSONObject oResponse;
			try {
				oResponse = new JSONObject(main_app_table);
				JSONArray jsonarr = oResponse.getJSONArray("data_sapi");
				id=databaseHandler.getCountSapi();
				if(id==0) {
					databaseHandler.addDataSapi(new DataSapi(0, "0", "0", "0", "Pilih Eartag"));
				}
				for (int i = 0; i < jsonarr.length(); i++) {
					JSONObject oResponsealue = jsonarr.getJSONObject(i);
					String id = oResponsealue.isNull("id") ? null
							: oResponsealue.getString("id");
					String indnr = oResponsealue.isNull("indnr") ? null
							: oResponsealue.getString("indnr");
					String lifnr = oResponsealue.isNull("lifnr") ? null
							: oResponsealue.getString("lifnr");
					String beastid = oResponsealue.isNull("beastid") ? null
							: oResponsealue.getString("beastid");
					String vistgid = oResponsealue.isNull("vistgid") ? null
							: oResponsealue.getString("vistgid");
					Log.d(LOG_TAG, "indnr:" + indnr);
					Log.d(LOG_TAG, "lifnr:" + lifnr);
					databaseHandler.addDataSapi(new DataSapi(Integer.parseInt(id),indnr,lifnr,beastid,vistgid));
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

	public void saveAppDataDataSapi(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_TABLE_DATA_SAPI, responsedata);
		editor.commit();
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
//					Log.d(LOG_TAG, "id_rencana_detail:" + id_rencana_detail);
//					Log.d(LOG_TAG, "id_rencana_header:" + id_rencana_header);
//					Log.d(LOG_TAG, "id_kegiatan:" + id_kegiatan);
//					Log.d(LOG_TAG, "id_customer:" + id_customer);
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

	public void saveAppDataBranch(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_TABLE_MST_CUSTOMER, responsedata);
		editor.commit();
	}

	public void saveAppDataBranchSameData(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_TABLE_MST_CUSTOMER_SAME_DATA,
				responsedata);
		editor.commit();
	}
	public class ListViewAdapter extends ArrayAdapter<Rencana> {
		Activity activity;
		int layoutResourceId;
		Rencana rencanaData;

		ArrayList<Rencana> data = new ArrayList<Rencana>();

		public ListViewAdapter(Activity act, int layoutResourceId,
							   ArrayList<Rencana> data) {
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
			UserHolder holder = null;

			if (row == null) {
				LayoutInflater inflater = LayoutInflater.from(activity);
				row = inflater.inflate(layoutResourceId, parent, false);
				holder = new UserHolder();
				holder.list_namaCustomer = (TextView) row
						.findViewById(R.id.customer_title_nama_customer);
				holder.list_alamatCustomer = (TextView) row
						.findViewById(R.id.customer_title_alamat_customer);
				holder.list_status = (TextView) row
						.findViewById(R.id.customer_title_status_customer);
				holder.list_tanggal = (TextView) row
						.findViewById(R.id.customer_title_tanggal_rencana);
				holder.list_index = (TextView) row
						.findViewById(R.id.index);
				row.setTag(holder);
			} else {
				holder = (UserHolder) row.getTag();
			}
			rencanaData = data.get(position);
			holder.list_namaCustomer.setText(rencanaData.getNama_customer());
			holder.list_alamatCustomer.setText(rencanaData.getAlamat());
			if (rencanaData.getStatus() == 0) {
				holder.list_status.setText("Baru");
			}else if (rencanaData.getStatus() == 1){
				holder.list_status.setText("Checkin");
			}else if (rencanaData.getStatus() == 2){
				holder.list_status.setText("Selesai");
			}
			holder.list_tanggal.setText(rencanaData.getTanggal_rencana());
			holder.list_index.setText(rencanaData.getIndnr());
			holder.list_namaCustomer.setTypeface(typefaceSmall);
			holder.list_alamatCustomer.setTypeface(typefaceSmall);
			holder.list_status.setTypeface(typefaceSmall);

			row.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String rencana_detail = String.valueOf(data.get(position).getId_rencana_detail());
					String nama = String.valueOf(data.get(position).getNama_customer());
					String alamat = String.valueOf(data.get(position).getAlamat());
					String status = String.valueOf(data.get(position).getStatus());
					String index = String.valueOf(data.get(position).getIndnr());
					saveAppDataDetailJadwal(rencana_detail);
					saveAppDataNamaCst(nama);
					saveAppDataAlamatCst(alamat);
					saveAppDataStatusCst(status);
					saveAppDataIndex(index);
					gotoDetailJadwal();
				}
			});

			return row;
		}
		class UserHolder {
			TextView list_namaCustomer;
			TextView list_alamatCustomer;
			TextView list_status;
			TextView list_tanggal;
			TextView list_index;
		}

	}
	public void saveAppDataDetailJadwal(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_JADWAL,
				responsedata);
		editor.commit();
	}
	public void saveAppDataNamaCst(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_NAMA,
				responsedata);
		editor.commit();
	}
	public void saveAppDataAlamatCst(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_ALAMAT,
				responsedata);
		editor.commit();
	}

	public void saveAppDataStatusCst(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_STATUS,
				responsedata);
		editor.commit();
	}
	public void saveAppDataIndex(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_TABLE_INDEX_NUMBER,
				responsedata);
		editor.commit();
	}

	public void gotoDetailJadwal() {
		Intent i = new Intent(this, DashboardTabsActivity.class);
		startActivity(i);
		finish();
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

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		if (mNavigationDrawerFragment != null) {
			if (mNavigationDrawerFragment.getCurrentSelectedPosition() != 3) {
				if (position ==0) {
					Intent intentActivity = new Intent(this,
							PlanVisitActivity.class);
					startActivity(intentActivity);
					finish();
				}else if (position ==1) {
					Intent intentActivity = new Intent(this,
							PlanVisitActivity2.class);
					startActivity(intentActivity);
					finish();
				}
				else if (position == 2) {
					Intent intentActivity = new Intent(this,
							ProspectPlanVisit.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 4) {
					Intent intentActivity = new Intent(this,
							History_Canvassing.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 5) {
					Intent intentActivity = new Intent(this,
							ChangePassword.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 6) {
					showCustomDialogExit();
				}
			}
		}
	}

	@Override
	public void onBackPressed() {
		if (mNavigationDrawerFragment.isDrawerOpen())
			mNavigationDrawerFragment.closeDrawer();
		else
			super.onBackPressed();
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//			showCustomDialogExit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
