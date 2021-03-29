package com.android.pir.gglc.pir;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.android.pir.gglc.database.DetailReqLoad;
import com.android.pir.gglc.database.DetailReqLoadNew;
import com.android.pir.gglc.database.MasterRencana;
import com.android.pir.gglc.database.MasterRencanaParam;
import com.android.pir.gglc.database.MstUser;
import com.android.pir.gglc.database.Product;
import com.android.pir.gglc.database.Mst_Customer;
import com.android.pir.gglc.database.Rencana;
import com.android.pir.mobile.R;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.android.pir.gglc.absen.AppVar.SHARED_PREFERENCES_NAME;

@SuppressWarnings("deprecation")
public class PlanVisitActivity extends ActionBarActivity implements NavigationDrawerCallbacks {
	private Context act;
	private Toolbar mToolbar;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private DatabaseHandler databaseHandler;
	private ListView listview;
	private Handler handler = new Handler();
	private ArrayList<Rencana> rencana_list = new ArrayList<Rencana>();
	private ProgressDialog progressDialog;
	private static final String LOG_TAG = PlanVisitActivity.class.getSimpleName();
	private int id_rencanaHeader = 0;
	private MstUser user;
	private DataSapi sapi;
	private DetailReqLoadNew detailReqLoadNew;
	private MasterRencana mstRencana;
	private MasterRencanaParam mstRencanaParam;
	private Typeface typefaceSmall;
	private TextView jml_ckin,jml_rcn,percent;
    private String nama_customer,response,mulai_data,response_data,main_app_nama_cst,keterangan_data,message,msg_success,nomor_rencana,tanggal_edit,keterangan_edit;
    private Button mButtonAddPetani,mButtonSaveUpload,btn_new,btn_edit,save_edit;
	private EditText mulai,keterangan;
	final int Date_Dialog_ID=0;
	private int id=0;
	int cDay,cMonth,cYear,main_app_id_detail_jadwal,id_user,sDay,sMonth,sYear,id_customer,id_rencanaHeaderEdit;
	Calendar cDate;
	protected LocationManager locationManager;
	private DatabaseHandler db;
//	private ViewPager viewPager;
	private Spinner spinnerRencana;
	private ArrayList<DetailReqLoadNew> detailReqLoadList = new ArrayList<DetailReqLoadNew>();
	private ListViewChooseAdapter cAdapterChooseAdapter;
	private ListViewAdapter cAdapter;
    private ArrayList<MasterRencana> rencanaMasterList;
    private ArrayList<String> rencanaMasterStringList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_plan_visit);
		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle(R.string.title_activity_all);
		act = this;
		db = new DatabaseHandler(this);
		spinnerRencana = (Spinner)findViewById(R.id.no_rencana);
		mulai = (EditText)findViewById(R.id.date_from);
		keterangan = (EditText)findViewById(R.id.keterangan);
		mButtonAddPetani = (Button)findViewById(R.id.activity_sales_order_btn_add_product);
		mButtonSaveUpload = (Button)findViewById(R.id.activity_sales_order_btn_save);
		btn_new = (Button)findViewById(R.id.btn_new);
		btn_edit = (Button)findViewById(R.id.btn_edit);
		save_edit = (Button)findViewById(R.id.activity_sales_order_btn_edit);
		listview = (ListView)findViewById(R.id.list);
		cDate= Calendar.getInstance();
		cDay=cDate.get(Calendar.DAY_OF_MONTH);
		cMonth=cDate.get(Calendar.MONTH);
		cYear=cDate.get(Calendar.YEAR);
		sDay=cDay;
		sMonth=cMonth;
		sYear=cYear;

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
		mNavigationDrawerFragment.selectItem(0);

		spinnerRencana.setVisibility(View.INVISIBLE);
		save_edit.setVisibility(View.INVISIBLE);
		mButtonAddPetani.setOnClickListener(maddSalesOrderButtonOnClickListener);
		mButtonSaveUpload.setOnClickListener(maddSalesOrderButtonOnClickListener);
		save_edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showCustomDialogSaveEdit();
			}
		});
		mulai.setClickable(true);
		mulai.setFocusable(false);
		mulai.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(Date_Dialog_ID);
			}
		});

		btn_new.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				spinnerRencana.setVisibility(View.INVISIBLE);
				mButtonSaveUpload.setVisibility(View.VISIBLE);
				save_edit.setVisibility(View.INVISIBLE);
				resetForm();
			}
		});
//		btn_edit.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				spinnerRencana.setVisibility(View.VISIBLE);
//				mButtonSaveUpload.setVisibility(View.INVISIBLE);
//				resetForm();
//				configSpinner();
//				save_edit.setVisibility(View.VISIBLE);
//			}
//		});
		updateContentRefreshRencana();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main3, menu);
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
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void updateContentRefreshRencana() {
		rencana_list.clear();
		ArrayList<Rencana> rencana_from_db = databaseHandler.getAllRencana();
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

				Rencana rencana = new Rencana();
				rencana.setId_rencana_detail(id_rencana_detail);
				rencana.setNama_customer(nama_customer);
				rencana.setAlamat(alamat);
				rencana.setStatus(status_renana);

				rencana_list.add(rencana);
			}
		} else {
			listview.setVisibility(View.INVISIBLE);
		}
		showCustomDialog("Jangan lupa update data petani setiap hari sebelum membuat rencana kunjungan. Caranya tekan titik tiga di pojok kanan atas dan pilih refresh.");
	}
	public void updateContentRefreshRencana1() {
		new DownloadDataCustomer().execute();
	}

	private void configSpinner(){
		if(databaseHandler.getCountMasterRencana()!=0){
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
			ArrayAdapter<String> adapterRencanaMaster = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, rencanaMasterStringList);
			adapterRencanaMaster.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerRencana.setAdapter(adapterRencanaMaster);

			spinnerRencana.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent,
										   View view, int position, long id) {
					resetForm();
					nomor_rencana = rencanaMasterList.get(position).getNomor_rencana();
					id_rencanaHeaderEdit =  rencanaMasterList.get(position).getId_rencana_header();
					Log.d(LOG_TAG, "nomor rencana : "+nomor_rencana);

					ArrayList<MasterRencanaParam> rencana_list = databaseHandler.getMasterRencanaParam(nomor_rencana);
					mstRencanaParam = new MasterRencanaParam();

					for (MasterRencanaParam tempRencana : rencana_list)
						mstRencanaParam = tempRencana;
					tanggal_edit=mstRencanaParam.getTanggal_rencana();
					keterangan_edit=mstRencanaParam.getKeterangan();
					mulai.setText(tanggal_edit);
					keterangan.setText(keterangan_edit);

					ArrayList<DetailReqLoadNew> staff_list = databaseHandler.getDetailRencanaParam(nomor_rencana);
					for(DetailReqLoadNew detailReqLoadNew : staff_list){
						updateListViewDetailOrder(detailReqLoadNew);
						Log.d(LOG_TAG, "id_customer : "+detailReqLoadNew.getId_product());
						Log.d(LOG_TAG, "nama_customer : "+detailReqLoadNew.getNama_product());
					}
					String substrN=nomor_rencana.substring(0,1);
					save_edit.setVisibility(View.VISIBLE);
					if(substrN.equals("U")){
						save_edit.setVisibility(View.INVISIBLE);
					}else{
						String timeRencana = tanggal_edit + " 09:00:00";
						String timeNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault()).format(new Date());
						Long a = null;
						try {
							a = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeRencana).getTime();
						} catch (java.text.ParseException e) {
							e.printStackTrace();
						}
						Long b = null;
						try {
							b = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeNow).getTime();
						} catch (java.text.ParseException e) {
							e.printStackTrace();
						}

						Log.d(LOG_TAG, "date rencana: "+timeRencana);
						Log.d(LOG_TAG, "date now: "+timeNow);
						Log.d(LOG_TAG, "date rencana: "+a);
						Log.d(LOG_TAG, "date now: "+b);
						int countCustomer = databaseHandler.getStatusRencanaHeader(id_rencanaHeaderEdit);
						if(countCustomer>0){
							save_edit.setVisibility(View.INVISIBLE);
							showCustomDialog("Anda tidak dapat merubah rencana ini karena data rencana sudah di aprove");
						}else{
							if(b>a){
								save_edit.setVisibility(View.INVISIBLE);
								showCustomDialog("Rencana ini tidak dapat diedit karena sudah melebihi batas waktu yang ditentukan. " +
										"Batas waktu yang dibolehkan untuk mengedit rencana ini yaitu tanggal "+tanggal_edit+" jam 09:00");
							}
						}
					}
				}
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});

			if(rencanaMasterList.isEmpty()){
				id_rencanaHeader = 0;
			}else {
				id_rencanaHeader = rencanaMasterList.get(0).getId_rencana_header();
			}

		}else{
			showCustomDialog("Download dencana detail terlebih dahulu");
		}
	}

	public void printDifference(Date startDate, Date endDate) {
		//milliseconds
		long different = endDate.getTime() - startDate.getTime();

		System.out.println("startDate : " + startDate);
		System.out.println("endDate : "+ endDate);
		System.out.println("different : " + different);

		long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
		long hoursInMilli = minutesInMilli * 60;
		long daysInMilli = hoursInMilli * 24;

		long elapsedDays = different / daysInMilli;
		different = different % daysInMilli;

		long elapsedHours = different / hoursInMilli;
		different = different % hoursInMilli;

		long elapsedMinutes = different / minutesInMilli;
		different = different % minutesInMilli;

		long elapsedSeconds = different / secondsInMilli;

		System.out.printf(
				"%d days, %d hours, %d minutes, %d seconds%n",
				elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
	}

	private void resetForm(){
		mulai.setText("");
		keterangan.setText("");
		detailReqLoadList.clear();
		listview.setAdapter(null);
	}

	private final View.OnClickListener maddSalesOrderButtonOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			int getId = arg0.getId();
			switch (getId) {
				case R.id.activity_sales_order_btn_add_product:
					int countCustomer = databaseHandler.getCountCustomer();
					if (countCustomer == 0) {
						String msg = getApplicationContext()
								.getResources()
								.getString(R.string.app_sales_order_no_data_product);
						showCustomDialog(msg);
					} else {
						ChooseCustomerDialog();
					}
					break;
				case R.id.activity_sales_order_btn_save:
					if (detailReqLoadList.isEmpty()){
						String msg = getApplicationContext()
								.getResources()
								.getString(R.string.app_reqload_customer_empty_box);
						showCustomDialog(msg);
					}else {
						if(passValidationForUpload()){
							mulai_data = mulai.getText().toString();
							keterangan_data = keterangan.getText().toString();
							new UploadData().execute();
							break;
						}
					}
//				case R.id.activity_sales_order_btn_edit:
//					if (detailReqLoadList.isEmpty()){
//						String msg = getApplicationContext()
//								.getResources()
//								.getString(R.string.app_reqload_customer_empty_box);
//						showCustomDialog(msg);
//					}else {
//						if(passValidationForUpload()){
//							mulai_data = mulai.getText().toString();
//							keterangan_data = keterangan.getText().toString();
//							new UploadDataEdit().execute();
//							break;
//						}
//					}
				default:
					break;
			}
		}
	};

	private void ChooseCustomerDialog() {
		final Dialog chooseCustomerDialog = new Dialog(act);
		chooseCustomerDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		chooseCustomerDialog
				.setContentView(R.layout.activity_main_product_choose_dialog_req_loadnew);
		chooseCustomerDialog.setCanceledOnTouchOutside(false);
		chooseCustomerDialog.setCancelable(true);

		chooseCustomerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				chooseCustomerDialog.dismiss();
			}
		});
		TextView tvHeaderKodeProduct = (TextView) chooseCustomerDialog
				.findViewById(R.id.activity_sales_order_title_kode_product);
		TextView tvHeaderAction = (TextView) chooseCustomerDialog
				.findViewById(R.id.activity_sales_order_title_action);
		tvHeaderKodeProduct.setTypeface(typefaceSmall);
		tvHeaderAction.setTypeface(typefaceSmall);
		EditText searchCustomer = (EditText) chooseCustomerDialog
				.findViewById(R.id.activity_product_edittext_search);
		final ArrayList<Mst_Customer> customer_list = new ArrayList<Mst_Customer>();
		final ListView listview = (ListView) chooseCustomerDialog
				.findViewById(R.id.list);
		searchCustomer.setFocusable(true);
		searchCustomer.setFocusableInTouchMode(true);
		searchCustomer.requestFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(searchCustomer, InputMethodManager.SHOW_IMPLICIT);

		listview.setItemsCanFocus(false);
		final ArrayList<MstUser> staff_list = databaseHandler.getAllUser();
		user = new MstUser();

		for (MstUser tempStaff : staff_list)
			user = tempStaff;
		String nama1=user.getNama();
		Log.d(LOG_TAG, "name1: "+nama1);
		ArrayList<Mst_Customer> customer_from_db = databaseHandler.getAllCustomerOnly(nama1);
		if (customer_from_db.size() > 0) {
			listview.setVisibility(View.VISIBLE);
			for (int i = 0; i < customer_from_db.size(); i++) {
				int id_customer = customer_from_db.get(i).getId_customer();
				String nama_customer = customer_from_db.get(i).getNama_customer();
				String alamat = customer_from_db.get(i).getAlamat();
				String indnr = customer_from_db.get(i).getIndnr();

				Mst_Customer customer = new Mst_Customer();
				customer.setId_customer(id_customer);
				customer.setNama_customer(nama_customer);
				customer.setAlamat(alamat);
				customer.setIndnr(indnr);
				customer_list.add(customer);
				cAdapterChooseAdapter = new ListViewChooseAdapter(
						this,
//						R.layout.list_item_product_sales_order, jumlahProduct,batch,product_list, chooseProductDialog);
						R.layout.list_item_product_sales_order,customer_list, chooseCustomerDialog);
				listview.setAdapter(cAdapterChooseAdapter);
				cAdapterChooseAdapter.notifyDataSetChanged();
			}
		} else {
			listview.setVisibility(View.INVISIBLE);
		}

		searchCustomer.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
									  int arg3) {
				if (cs.toString().length() > 0) {
					customer_list.clear();
					for (MstUser tempStaff : staff_list)
						user = tempStaff;
					String nama1=user.getNama();
					Log.d(LOG_TAG, "name1: "+nama1);
					ArrayList<Mst_Customer> customer_from_db = databaseHandler
							.getAllCustomerBaseOnSearch(nama1, cs.toString());
					if (customer_from_db.size() > 0) {
						listview.setVisibility(View.VISIBLE);
						for (int i = 0; i < customer_from_db.size(); i++) {
							int id_customer = customer_from_db.get(i)
									.getId_customer();
							String nama_customer = customer_from_db.get(i)
									.getNama_customer();
							String alamat = customer_from_db.get(i).getAlamat();
							String indnr = customer_from_db.get(i).getIndnr();

							Mst_Customer customer = new Mst_Customer();
							customer.setId_customer(id_customer);
							customer.setNama_customer(nama_customer);
							customer.setAlamat(alamat);
							customer.setIndnr(indnr);
							customer_list.add(customer);
							cAdapterChooseAdapter = new ListViewChooseAdapter(
									PlanVisitActivity.this,
//									R.layout.list_item_product_sales_order,jumlahProduct,batch, product_list,chooseProductDialog);
									R.layout.list_item_product_sales_order, customer_list,chooseCustomerDialog);
							listview.setAdapter(cAdapterChooseAdapter);
							cAdapterChooseAdapter.notifyDataSetChanged();
						}
					} else {
						listview.setVisibility(View.INVISIBLE);
					}

				} else {
					ArrayList<MstUser> staff_list = databaseHandler.getAllUser();
					user = new MstUser();

					for (MstUser tempStaff : staff_list)
						user = tempStaff;
					String nama1=user.getNama();
					ArrayList<Mst_Customer> customer_from_db = databaseHandler
							.getAllCustomerOnly(nama1);
					if (customer_from_db.size() > 0) {
						listview.setVisibility(View.VISIBLE);
						for (int i = 0; i < customer_from_db.size(); i++) {
							int id_customer = customer_from_db.get(i)
									.getId_customer();
							String nama_customer = customer_from_db.get(i)
									.getNama_customer();
							String alamat = customer_from_db.get(i).getAlamat();
							String indnr = customer_from_db.get(i).getIndnr();

							Mst_Customer customer = new Mst_Customer();
							customer.setId_customer(id_customer);
							customer.setNama_customer(nama_customer);
							customer.setAlamat(alamat);
							customer.setIndnr(indnr);
							customer_list.add(customer);
							cAdapterChooseAdapter = new ListViewChooseAdapter(
									PlanVisitActivity.this,
//									R.layout.list_item_product_sales_order,jumlahProduct, batch, product_list,chooseProductDialog);
									R.layout.list_item_product_sales_order, customer_list,chooseCustomerDialog);
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

	public class UploadData extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog.setMessage(getApplicationContext().getResources()
					.getString(R.string.app_reqload_processing_upload));
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
			String url_add_req_load = AppVar.CONFIG_APP_URL_PUBLIC
					+ AppVar.CONFIG_APP_URL_UPLOAD_RENCANA;
				response_data = uploadReqLoad(url_add_req_load,
						mulai_data,keterangan_data,
						String.valueOf(id_user)
				);
//			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (response_data != null && response_data.length() > 0) {
				if (response_data.startsWith("Error occurred")) {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_sales_order_processing_upload_failed);
					handler.post(new Runnable() {
						public void run() {
							showCustomDialog(msg);
						}
					});
				} else {
					handler.post(new Runnable() {
						public void run() {
//							initUploadReqLoad();
							new DownloadDataRencanaMaster().execute();
						}
					});
				}
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
				new UploadDataDetail().execute();
//				if (progressDialog != null) {
//					progressDialog.dismiss();
//				}
//				msg_success=act.getApplicationContext().getResources()
//						.getString(R.string.MSG_DLG_LABEL_SYNRONISASI_DATA_SUCCESS);
//				showCustomDialog(msg_success);
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
					Log.d(LOG_TAG, "aproved:" + aproved);
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
//			SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
//			String id_wilayah= prefs.getString("id_wilayah","null");

//			String download_data_url = AppVar.CONFIG_APP_URL_PUBLIC
//					+ AppVar.CONFIG_APP_URL_DOWNLOAD_CUSTOMER+ "?id_wilayah="
//					+ id_wilayah;
			String download_data_url = AppVar.CONFIG_APP_URL_PUBLIC
					+ AppVar.CONFIG_APP_URL_DOWNLOAD_CUSTOMER_NEW;
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
							db.deleteTableMSTCustomer();
							saveAppDataBranchSameData(act
									.getApplicationContext().getResources()
									.getString(R.string.app_value_false));
						}
					} else {
						db.deleteTableMSTCustomer();
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
					db.addMst_customer(new Mst_Customer(Integer.parseInt(id_customer),kode_customer,nama_customer,alamat,no_hp,lats,longs,Integer.parseInt(id_wilayah),indnr));
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

	public void saveAppDataRencanaMaster(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_TABLE_RENCANA_MASTER, responsedata);
		editor.commit();
	}

	public void saveAppDataRencanaMasterSameData(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_TABLE_RENCANA_MASTER_SAME_DATA,
				responsedata);
		editor.commit();
	}

	public class UploadDataDetail extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {
			ArrayList<MasterRencana> staff_list = databaseHandler.getMaxMasterRencana();
			mstRencana = new MasterRencana();

			for (MasterRencana tempStaff : staff_list)
				mstRencana = tempStaff;
			String id_rencana_header=String.valueOf(mstRencana.getId_rencana_header());
			SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
			String id_karyawan = prefs.getString("id_awo","null");
			String url_add_req_load = AppVar.CONFIG_APP_URL_PUBLIC
					+ AppVar.CONFIG_APP_URL_UPLOAD_RENCANA_DETAIL;
//			if(!id_rencana_header.equals("0")){
				for (DetailReqLoadNew detailReqLoad : detailReqLoadList) {
					response_data = uploadReqLoadDetail(
							url_add_req_load,
							id_rencana_header,
							String.valueOf(detailReqLoad.getId_product()),
							id_karyawan,detailReqLoad.getIndnr()
					);
				}
//			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (response_data != null && response_data.length() > 0) {
				if (response_data.startsWith("Error occurred")) {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_sales_order_processing_upload_failed);
					handler.post(new Runnable() {
						public void run() {
							showCustomDialog(msg);
						}
					});
				} else {
					handler.post(new Runnable() {
						public void run() {
//							initUploadReqLoad();
							new DownloadDataRencanaDetail().execute();
//							if (progressDialog != null) {
//								progressDialog.dismiss();
//							}
//							resetForm();
//							msg_success=act.getApplicationContext().getResources()
//									.getString(R.string.MSG_DLG_LABEL_SYNRONISASI_DATA_SUCCESS);
//							showCustomDialog(msg_success);
						}
					});
				}
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
				spinnerRencana.setVisibility(View.INVISIBLE);
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
	public void saveAppDataRencanaDetailSameData(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_TABLE_RENCANA_DETAIL_SAME_DATA,
				responsedata);
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
				resetForm();
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
				resetForm();
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

	public class UploadDataEdit extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog.setMessage(getApplicationContext().getResources()
					.getString(R.string.app_reqload_processing_upload));
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
			String url_add_req_load = AppVar.CONFIG_APP_URL_PUBLIC
					+ AppVar.CONFIG_APP_URL_UPLOAD_RENCANA_EDIT;
				response_data = uploadReqLoadEdit(url_add_req_load,
						mulai_data,keterangan_data,
						String.valueOf(id_user),String.valueOf(id_rencanaHeaderEdit)
				);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (response_data != null && response_data.length() > 0) {
				if (response_data.startsWith("Error occurred")) {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_sales_order_processing_upload_failed);
					handler.post(new Runnable() {
						public void run() {
							showCustomDialog(msg);
						}
					});
				} else {
					handler.post(new Runnable() {
						public void run() {
							new DownloadDataRencanaMaster().execute();
						}
					});
				}
			}
		}
	}

	//get download for all
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

	class ListViewChooseAdapter extends ArrayAdapter<Mst_Customer> {
		int layoutResourceId;
		Mst_Customer productData;
		ArrayList<Mst_Customer> data = new ArrayList<Mst_Customer>();
		Activity mainActivity;
		Dialog chooseProductDialog;

//		public ListViewChooseAdapter(Activity mainActivity,int layoutResourceId, EditText jumlahProduct, EditText batch,ArrayList<Product> data, Dialog chooseProductDialog) {
		public ListViewChooseAdapter(Activity mainActivity,int layoutResourceId, ArrayList<Mst_Customer> data, Dialog chooseProductDialog) {
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
				holder.list_indnr = (TextView) row
						.findViewById(R.id.indnr);
				holder.list_namaProduct = (TextView) row
						.findViewById(R.id.sales_order_title_nama_product);
				holder.list_desa= (TextView) row
						.findViewById(R.id.pir_kegiatan_desa);
				holder.mButtonAddItem = (Button) row
						.findViewById(R.id.sales_order_dialog_btn_add_item);

				row.setTag(holder);
			} else {
				holder = (ListViewChooseAdapter.UserHolder) row.getTag();
			}
			productData = data.get(position);
			holder.list_indnr.setText(productData.getIndnr());
			holder.list_namaProduct.setText(productData.getNama_customer());
			holder.list_desa.setText(productData.getAlamat());
			holder.mButtonAddItem.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					updateListViewDetailOrder(new DetailReqLoadNew(data.get(position).getNama_customer(), data.get(position).getId_customer(),data.get(position).getAlamat(),"",data.get(position).getIndnr()));
					chooseProductDialog.hide();
					listview.setVisibility(View.VISIBLE);
					Log.d(LOG_TAG, "click listener rencana: "+data.get(position).getAlamat()+data.get(position).getNama_customer()+data.get(position).getId_customer());
				}
			});
			return row;
		}

		class UserHolder {
//			ImageView list_img;
			TextView list_indnr;
			TextView list_namaProduct;
			TextView list_desa;
			Button mButtonAddItem;
		}
	}

	private String uploadReqLoad(final String url,
								 final String date_from,
								 final String keterangan,
								 final String id_user) {

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
			entity.addPart("date_from", new StringBody(date_from));
			entity.addPart("keterangan", new StringBody(keterangan));
			entity.addPart("id_user", new StringBody(id_user));
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

	private String uploadReqLoadDetail(final String url,
								 final String id_rencana_header,
								 final String id_customer,
								 final String id_karyawan,
                                 final String indnr) {

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		String responseString = null;
		try {
			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}
//			Log.d(LOG_TAG, "id_rencana_header: "+id_rencana_header);
//			Log.d(LOG_TAG, "id_customer_plan: "+id_customer);
//			Log.d(LOG_TAG, "id_karyawan: "+id_karyawan);
			MultipartEntity entity = new MultipartEntity();
			entity.addPart("id_rencana_header", new StringBody(id_rencana_header));
			entity.addPart("id_customer", new StringBody(id_customer));
			entity.addPart("id_karyawan", new StringBody(id_karyawan));
			entity.addPart("indnr", new StringBody(indnr));
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

	private String uploadReqLoadEdit(final String url,
								 final String date_from,
								 final String keterangan,
								 final String id_user, final String id_rencana_header) {

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
			entity.addPart("date_from", new StringBody(date_from));
			entity.addPart("keterangan", new StringBody(keterangan));
			entity.addPart("id_user", new StringBody(id_user));
			entity.addPart("id_rencana_header", new StringBody(id_rencana_header));
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

	private String uploadReqLoadDetailEdit(final String url,
								 final String id_rencana_header,
								 final String id_customer,
								 final String id_karyawan) {

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
			entity.addPart("id_rencana_header", new StringBody(id_rencana_header));
			entity.addPart("id_customer", new StringBody(id_customer));
			entity.addPart("id_karyawan", new StringBody(id_karyawan));
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

	private SharedPreferences getSharedPrefereces() {
		return act.getSharedPreferences(AppVar.SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
	}

	public void initUploadReqLoad() {
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
								R.string.app_reqload_penjualan_upload_failed);
				showCustomDialog(msg);
			} else {
				Log.d(LOG_TAG, "status=" + status);
				if (status.equalsIgnoreCase("True")) {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_reqload_penjualan_upload_failed);
					showCustomDialog(msg);
				} else {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_penjualan_upload_success);
					CustomDialogUploadSuccess(msg);
				}
			}
		} catch (JSONException e) {
			final String message = e.toString();
			showCustomDialog(message);
		}
	}

	public void CustomDialogUploadSuccess(String msg) {
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
//								databaseHandler.deleteTableReqLoad();
//								finish();
//								startActivity(getIntent());
//								gotoInventory();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public boolean passValidationForUpload() {
		if (GlobalApp.isBlank(mulai)) {
			GlobalApp.takeDefaultAction(
					mulai,
					this,
					getApplicationContext().getResources().getString(
							R.string.app_supplier_failed_tanggal));
			return false;
		}
		return true;
	}

	private void updateListViewDetailOrder(DetailReqLoadNew detailReqLoad) {
		detailReqLoadList.add(detailReqLoad);
		cAdapter = new ListViewAdapter(this,
				R.layout.list_item_detail_penjualan, detailReqLoadList);
		listview.setAdapter(cAdapter);
		cAdapter.notifyDataSetChanged();
	}

	public class ListViewAdapter extends ArrayAdapter<DetailReqLoadNew> {
		Activity activity;
		int layoutResourceId;
		DetailReqLoadNew productData;
		ArrayList<DetailReqLoadNew> data = new ArrayList<DetailReqLoadNew>();

		public ListViewAdapter(Activity act, int layoutResourceId,
							   ArrayList<DetailReqLoadNew> data) {
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
				holder.list_indnr = (TextView) row
						.findViewById(R.id.sales_order_title_indnr);
				holder.list_nama_product = (TextView) row
						.findViewById(R.id.sales_order_title_nama_product);
				holder.alamat= (TextView) row
						.findViewById(R.id.alamat);
				row.setTag(holder);
			} else {
				holder = (ListViewAdapter.UserHolder) row.getTag();
			}
			productData = data.get(position);
			holder.list_indnr.setText(productData.getIndnr());
			holder.list_nama_product.setText(productData.getNama_product());
			holder.alamat.setText(productData.getAlamat());
			row.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					showDeleteDialog(position);
				}
			});
			return row;
		}

		public void showDeleteDialog(final int position) {
			String msg = getApplicationContext().getResources().getString(
					R.string.MSG_DLG_LABEL_DATA_DELETE_DIALOG);
			final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(act);
			alertDialogBuilder
					.setMessage(msg)
					.setCancelable(true)
					.setNegativeButton(
							getApplicationContext().getResources().getString(
									R.string.MSG_DLG_LABEL_YES),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									detailReqLoadList.remove(position);
									cAdapter = new ListViewAdapter(
											PlanVisitActivity.this,
											R.layout.list_item_detail_penjualan,
											detailReqLoadList);
									listview.setAdapter(cAdapter);
									cAdapter.notifyDataSetChanged();
								}
							})
					.setPositiveButton(
							getApplicationContext().getResources().getString(
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
			TextView list_indnr;
			TextView list_nama_product;
			TextView alamat;
		}
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case Date_Dialog_ID:
				return new DatePickerDialog(this, onDateSet, cYear, cMonth,
						cDay);
		}
		return null;
	}

	private void updateDateDisplay(int year,int month,int date) {
		String adate;
		month++;
		if (date < 10) {
			adate = "0" + date + "-";
		} else {
			adate = date + "-";
		}
		if (month < 10) {
			adate += "0" + month + "-";
		} else {
			adate += month + "-";
		}
		adate += year;
		mulai.setText(adate);
	}

	private DatePickerDialog.OnDateSetListener onDateSet=new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
							  int dayOfMonth) {

			System.out.println("Mulai");
			sYear=year;
			sMonth=monthOfYear;
			sDay=dayOfMonth;
			updateDateDisplay(sDay,sMonth,sYear);
		}
	};

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		if (mNavigationDrawerFragment != null) {
			if (mNavigationDrawerFragment.getCurrentSelectedPosition() != 0) {
				if (position ==1) {
					Intent intentActivity = new Intent(this,
							PlanVisitActivity2.class);
					startActivity(intentActivity);
					finish();
				}else if (position ==2) {
					Intent intentActivity = new Intent(this,
							ProspectPlanVisit.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 3) {
					Intent intentActivity = new Intent(this,
							DashboardActivity.class);
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

	public void showCustomDialogSaveEdit() {
		String msg = getApplicationContext().getResources().getString(
				R.string.MSG_DLG_LABEL_EXIT_DIALOG_EDIT_RENCANA);
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
								mulai_data = mulai.getText().toString();
								keterangan_data = keterangan.getText().toString();
								new UploadDataEdit().execute();
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

	public void gotoCheckin(){
		Intent intentActivity = new Intent(this,
				IconTextTabsActivity.class);
		startActivity(intentActivity);
		finish();
	}

	public void gotoCheckout(){
		Intent intentActivity = new Intent(this,
				CheckoutActivity.class);
		startActivity(intentActivity);
		finish();
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
