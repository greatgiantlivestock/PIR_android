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
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.pir.gglc.absen.AppVar;
import com.android.pir.gglc.database.DatabaseHandler;
import com.android.pir.gglc.database.DetailReqLoad;
import com.android.pir.gglc.database.Product;
import com.android.pir.mobile.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

@SuppressWarnings("deprecation")
public class DataPenjualan extends FragmentActivity {
	private Context act;
	private ProgressDialog progressDialog;
	private Handler handler = new Handler();
	private String message;
	private String response_data;
	private static final String LOG_TAG = DataPenjualan.class
			.getSimpleName();
	private Typeface typefaceSmall;
	private ImageView menuBackButton;
	private Button btnUpload, refresh;
	private Button btnFoto1;
	private Button btnFoto2;
	private Button btnFoto3;
	private Button btnFoto4;
	private double latitude; // latitude
	private double longitude; // longitude
	private Location location; // location
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	private Uri fileUri; // file url to store image/video
	private final int MEDIA_TYPE_IMAGE = 1;
	private final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private String IMAGE_DIRECTORY_NAME = "Spg";
	private String id_Image = "";
	private String newImageName;
	private ImageView imageView1;
	private ImageView imageView2;
	private ImageView imageView3;
	private ImageView imageView4;
	private EditText edtSupplierKeterangan, gps, alamat;
	private String keterangan,id_user,mulai_data,sampai_data;
	private String streetName = "";
	private int counterFoto = 0;
	private LocationManager locationManager;
	private static Context _context;
	private Button mButtonAddProduct,mButtonSave;
	private EditText mulai,sampai;
	final int Date_Dialog_ID=0;
	final int Date_Dialog_ID1=1;
	int cDay,cMonth,cYear;
	Calendar cDate;
	int sDay,sMonth,sYear;
	private ListView listview;
	private DatabaseHandler databaseHandler;
	private int main_app_id_detail_jadwal;
	private String main_app_nama_cst;
	private ListViewChooseAdapter cAdapterChooseAdapter;
	private ArrayList<DetailReqLoad> detailReqLoadList = new ArrayList<DetailReqLoad>();
//	private ListViewAdapter cAdapter;

	private ListViewAdapter cAdapter;
	private EditText jumlahProduct,batch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_penjualan);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		act = this;

		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);

		mButtonAddProduct = (Button) findViewById(R.id.activity_sales_order_btn_add_product);
		mButtonSave = (Button) findViewById(R.id.activity_sales_order_btn_save);
		listview = (ListView) findViewById(R.id.list);
		mulai = (EditText)findViewById(R.id.date_from);
		sampai = (EditText)findViewById(R.id.date_to);
//		listview.setItemsCanFocus(false);
		databaseHandler = new DatabaseHandler(this);

		mulai.setClickable(true);
		mulai.setFocusable(false);
		mulai.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(Date_Dialog_ID);
			}
		});

		sampai.setClickable(true);
		sampai.setFocusable(false);
		sampai.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(Date_Dialog_ID1);
			}
		});

		cDate= Calendar.getInstance();
		cDay=cDate.get(Calendar.DAY_OF_MONTH);
		cMonth=cDate.get(Calendar.MONTH);
		cYear=cDate.get(Calendar.YEAR);
		sDay=cDay;
		sMonth=cMonth;
		sYear=cYear;

		mButtonSave.setOnClickListener(maddSalesOrderButtonOnClickListener);
		mButtonAddProduct.setOnClickListener(maddSalesOrderButtonOnClickListener);

		SharedPreferences spPreferences = getSharedPrefereces();
		main_app_id_detail_jadwal = Integer.parseInt(spPreferences.getString(
				AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_JADWAL, null));
		main_app_nama_cst = spPreferences.getString(
				AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_NAMA, null);
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case Date_Dialog_ID:
				return new DatePickerDialog(this, onDateSet, cYear, cMonth,
						cDay);

			case Date_Dialog_ID1:
				return new DatePickerDialog(this, onDateSet1, cYear, cMonth,
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

	private void updateDateDisplay1(int year,int month,int date) {
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
		sampai.setText(adate);
//		history.setEnabled(true);
	}
	private void updateDateDisplay2(int year,int month,int date) {
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
		batch.setText(adate);
//		history.setEnabled(true);
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

	private DatePickerDialog.OnDateSetListener onDateSet1=new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
							  int dayOfMonth) {

			System.out.println("Sampai");
			sYear=year;
			sMonth=monthOfYear;
			sDay=dayOfMonth;
			updateDateDisplay1(sDay,sMonth,sYear);
		}
	};

	private final View.OnClickListener maddSalesOrderButtonOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			int getId = arg0.getId();
			switch (getId) {
				case R.id.activity_sales_order_btn_add_product:
					int countProduct = databaseHandler.getCountProduct();
					if (countProduct == 0) {
						String msg = getApplicationContext()
								.getResources()
								.getString(R.string.app_sales_order_no_data_product);
						showCustomDialog(msg);
					} else {
						ChooseProductDialog();
					}
					break;
				case R.id.activity_sales_order_btn_save:
					if (detailReqLoadList.isEmpty()){
						String msg = getApplicationContext()
								.getResources()
								.getString(
										R.string.app_reqload_product_empty_box);
						showCustomDialog(msg);
					}else {
						mulai_data = mulai.getText().toString();
						sampai_data = sampai.getText().toString();
						new UploadData().execute();

//						int countData = databaseHandler.getCountReqLoad();
//						countData = countData + 1;
//						String datetime = dateOutput ;
//
//						SharedPreferences spPreferences = getSharedPrefereces();
//						String kodeBranch = spPreferences.getString(
//								CONFIG.SHARED_PREFERENCES_STAFF_KODE_BRANCH, null);
//						String username = spPreferences.getString(
//								CONFIG.SHARED_PREFERENCES_STAFF_USERNAME, null);
//						String main_app_id_staff = spPreferences.getString(
//								CONFIG.SHARED_PREFERENCES_STAFF_ID_STAFF, null);
//						Branch branch = databaseHandler.getBranch(Integer
//								.parseInt(kodeBranch));
//						String nomerRequestLoad = CONFIG.CONFIG_APP_KODE_RL_HEADER
//								+ branch.getKode_branch()+"."+ username + "." + datetime;
//
//						final String date2 = "yyyy-MM-dd";
//						Calendar calendar2 = Calendar.getInstance();
//						SimpleDateFormat dateFormat2 = new SimpleDateFormat(date2);
//						final String checkDate = dateFormat2.format(calendar2
//								.getTime());
//						Calendar now = Calendar.getInstance();
//						int hrs = now.get(Calendar.HOUR_OF_DAY);
//						int min = now.get(Calendar.MINUTE);
//						int sec = now.get(Calendar.SECOND);
//						final String time = zero(hrs) + zero(min) + zero(sec);
//
//						ArrayList<ReqLoad> tempReq_load_list = databaseHandler
//								.getAllReqLoad();
//						int tempIndex = 0;
//						for (ReqLoad reqLoad : tempReq_load_list) {
//							tempIndex = reqLoad.getId_sales_order();
//						}
						String msg = getApplicationContext().getResources()
								.getString(R.string.app_req_load_save_success);
//						showCustomDialogSaveSuccess(msg);

						break;
					}
				default:
					break;
			}
		}
	};

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
//			ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//			NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			String url_add_req_load = AppVar.CONFIG_APP_URL_PUBLIC
				+ AppVar.CONFIG_APP_URL_UPLOAD_PENJUALAN;
//			List<ReqLoad> dataReqLoad = databaseHandler
//				.getAllReqLoad();
			for (DetailReqLoad detailReqLoad : detailReqLoadList) {
				response_data = uploadReqLoad(url_add_req_load,
						mulai_data,sampai_data,
						String.valueOf(main_app_id_detail_jadwal),
						main_app_nama_cst,
						detailReqLoad.getJumlah_order(),
						String.valueOf(detailReqLoad.getId_product()),
						detailReqLoad.getBatch());
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
//			List<ReqLoad> dataAddReqLoad = databaseHandler
//					.getAllReqLoad();
//			int countData = dataAddReqLoad.size();
//			if (countData > 0) {
//				try {
//					Thread.sleep(dataAddReqLoad.size() * 1000 * 3);
//				} catch (final InterruptedException e) {
//					Log.d(LOG_TAG, "InterruptedException " + e.getMessage());
//					handler.post(new Runnable() {
//						public void run() {
//							showCustomDialog(e.getMessage());
//						}
//					});
//				}
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
								initUploadReqLoad();
							}
						});
					}
				}
//			} else {
//				final String msg = act
//						.getApplicationContext()
//						.getResources()
//						.getString(
//								R.string.app_reqload_penjualan_upload_failed);
//				handler.post(new Runnable() {
//					public void run() {
//						showCustomDialog(msg);
//					}
//				});
//			}
		}
	}

	private String uploadReqLoad(final String url,
								 final String date_from,
								 final String date_to,
								 final String id_rencana_detail,
								 final String nama_customer,
								 final String jumlah_order,
								 final String id_product,
								 final String batch) {

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
			entity.addPart("date_to", new StringBody(date_to));
			entity.addPart("id_rencana_detail", new StringBody(id_rencana_detail));
			entity.addPart("nama_customer", new StringBody(nama_customer));
			entity.addPart("jumlah_order", new StringBody(jumlah_order));
			entity.addPart("id_product", new StringBody(id_product));
			entity.addPart("batch", new StringBody(batch));

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
								gotoInventory();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}


	private void ChooseProductDialog() {
		final Dialog chooseProductDialog = new Dialog(act);
		chooseProductDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		chooseProductDialog
				.setContentView(R.layout.activity_main_product_choose_dialog_req_load);
		chooseProductDialog.setCanceledOnTouchOutside(false);
		chooseProductDialog.setCancelable(true);

		chooseProductDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				chooseProductDialog.dismiss();
			}
		});
		TextView tvHeaderKodeProduct = (TextView) chooseProductDialog
				.findViewById(R.id.activity_sales_order_title_kode_product);
//		TextView tvHeaderNamaProduct = (TextView) chooseProductDialog
//				.findViewById(R.id.activity_sales_order_title_nama_product);
//		TextView tvHeaderHargaProduct = (TextView) chooseProductDialog
//				.findViewById(R.id.activity_sales_order_title_harga_product);
		TextView tvHeaderAction = (TextView) chooseProductDialog
				.findViewById(R.id.activity_sales_order_title_action);
		tvHeaderKodeProduct.setTypeface(typefaceSmall);
		tvHeaderAction.setTypeface(typefaceSmall);
		EditText searchProduct = (EditText) chooseProductDialog
				.findViewById(R.id.activity_product_edittext_search);
		final ArrayList<Product> product_list = new ArrayList<Product>();
		final ListView listview = (ListView) chooseProductDialog
				.findViewById(R.id.list);
		jumlahProduct = (EditText) chooseProductDialog
				.findViewById(R.id.activity_product_edittext_pieces);
		batch = (EditText) chooseProductDialog
				.findViewById(R.id.activity_product_batch);
//		final EditText jumlahProduct1 = (EditText) chooseProductDialog
//				.findViewById(R.id.activity_product_edittext_renceng);
//		final EditText jumlahProduct2 = (EditText) chooseProductDialog
//				.findViewById(R.id.activity_product_edittext_pack);
//		final EditText jumlahProduct3 = (EditText) chooseProductDialog
//				.findViewById(R.id.activity_product_edittext_dus);
		searchProduct.setFocusable(true);
		searchProduct.setFocusableInTouchMode(true);
		searchProduct.requestFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(searchProduct, InputMethodManager.SHOW_IMPLICIT);


		listview.setItemsCanFocus(false);
		ArrayList<Product> product_from_db = databaseHandler.getAllProduct();
		if (product_from_db.size() > 0) {
			listview.setVisibility(View.VISIBLE);
			for (int i = 0; i < product_from_db.size(); i++) {
				int id_product = product_from_db.get(i).getId_product();
				String nama_product = product_from_db.get(i).getNama_product();

				Product product = new Product();
				product.setId_product(id_product);
				product.setNama_product(nama_product);
				product_list.add(product);
				cAdapterChooseAdapter = new ListViewChooseAdapter(
						DataPenjualan.this,
						R.layout.list_item_product_sales_order, jumlahProduct,
						batch,product_list, chooseProductDialog);
				listview.setAdapter(cAdapterChooseAdapter);
				cAdapterChooseAdapter.notifyDataSetChanged();
			}
		} else {
			listview.setVisibility(View.INVISIBLE);
		}

		searchProduct.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
									  int arg3) {
				if (cs.toString().length() > 0) {
					product_list.clear();
					ArrayList<Product> product_from_db = databaseHandler
							.getAllProductBaseOnSearch(cs.toString());
					if (product_from_db.size() > 0) {
						listview.setVisibility(View.VISIBLE);
						for (int i = 0; i < product_from_db.size(); i++) {
							int id_product = product_from_db.get(i)
									.getId_product();
							String nama_product = product_from_db.get(i)
									.getNama_product();

							Product product = new Product();
							product.setId_product(id_product);
							product.setNama_product(nama_product);
							product_list.add(product);
							cAdapterChooseAdapter = new ListViewChooseAdapter(
									DataPenjualan.this,
									R.layout.list_item_product_sales_order,
									jumlahProduct,batch, product_list,
									chooseProductDialog);
							listview.setAdapter(cAdapterChooseAdapter);
							cAdapterChooseAdapter.notifyDataSetChanged();
						}
					} else {
						listview.setVisibility(View.INVISIBLE);
					}

				} else {
					ArrayList<Product> product_from_db = databaseHandler
							.getAllProduct();
					if (product_from_db.size() > 0) {
						listview.setVisibility(View.VISIBLE);
						for (int i = 0; i < product_from_db.size(); i++) {
							int id_product = product_from_db.get(i)
									.getId_product();
							String nama_product = product_from_db.get(i)
									.getNama_product();

							Product product = new Product();
							product.setId_product(id_product);
							product.setNama_product(nama_product);
							product_list.add(product);
							cAdapterChooseAdapter = new ListViewChooseAdapter(
									DataPenjualan.this,
									R.layout.list_item_product_sales_order,
									jumlahProduct, batch, product_list,
									chooseProductDialog);
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
				chooseProductDialog.show();
			}
		});
	}

	class ListViewChooseAdapter extends ArrayAdapter<Product> {
		int layoutResourceId;
		Product productData;
		ArrayList<Product> data = new ArrayList<Product>();
		Activity mainActivity;
		EditText jumlahProduct;
		EditText batch;

//		EditText jumlahProduct2;
//		EditText jumlahProduct3;
		Dialog chooseProductDialog;

		public ListViewChooseAdapter(Activity mainActivity,
									 int layoutResourceId, EditText jumlahProduct, EditText batch,
									 ArrayList<Product> data, Dialog chooseProductDialog) {
			super(mainActivity, layoutResourceId, data);
			this.layoutResourceId = layoutResourceId;
			this.data = data;
			this.chooseProductDialog = chooseProductDialog;
			this.mainActivity = mainActivity;
			this.jumlahProduct = jumlahProduct;
			this.batch = batch;
//			this.jumlahProduct1 = jumlahProduct1;
//			this.jumlahProduct2 = jumlahProduct2;
//			this.jumlahProduct3 = jumlahProduct3;
			notifyDataSetChanged();
		}

		@Override
		public View getView(final int position, View convertView,
							ViewGroup parent) {
			View row = convertView;
			UserHolder holder = null;

			if (row == null) {
				LayoutInflater inflater = LayoutInflater.from(mainActivity);

				row = inflater.inflate(layoutResourceId, parent, false);
				holder = new UserHolder();
//				holder.list_img = (ImageView) row.findViewById(R.id.image);
//				holder.list_kodeProduct = (TextView) row
//						.findViewById(R.id.sales_order_title_kode_product);
				holder.list_namaProduct = (TextView) row
						.findViewById(R.id.sales_order_title_nama_product);
//				holder.list_harga = (TextView) row
//						.findViewById(R.id.sales_order_title_harga_product);
				holder.mButtonAddItem = (Button) row
						.findViewById(R.id.sales_order_dialog_btn_add_item);

				row.setTag(holder);
			} else {
				holder = (UserHolder) row.getTag();
			}
			productData = data.get(position);
//			File dir = new File(AppVar.getFolderPath() + "/"
//					+ AppVar.CONFIG_APP_FOLDER_PRODUCT + "/"
//					+ data.get(position).getFoto());
//			holder.list_img.setImageBitmap(BitmapFactory.decodeFile(dir.getAbsolutePath()));
			//holder.list_kodeProduct.setText(productData.getKode_product());
			holder.list_namaProduct.setText(productData.getNama_product());
//			Float priceIDR = Float.valueOf(productData.getHarga_jual());
//			DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
//			otherSymbols.setDecimalSeparator(',');
//			otherSymbols.setGroupingSeparator('.');

//			DecimalFormat df = new DecimalFormat("#,##0", otherSymbols);
//			holder.list_harga.setText("Rp. " + df.format(priceIDR));
//			holder.list_kodeProduct.setTypeface(getTypefaceSmall());
//			holder.list_namaProduct.setTypeface(getTypefaceSmall());
//			holder.list_harga.setTypeface(getTypefaceSmall());
			holder.mButtonAddItem.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

//					if (jumlahProduct.getText().length()==0&&jumlahProduct1.getText().length()==0&&
//							jumlahProduct2.getText().length()==0&&jumlahProduct3.getText().length()==0) {
//
//						String msg = getApplicationContext()
//								.getResources()
//								.getString(
//										R.string.app_sales_order_failed_please_add_jumlah);
//						showCustomDialog(msg);
//					} else if(jumlahProduct.getText().length()==0||jumlahProduct1.getText().length()==0||
//							jumlahProduct2.getText().length()==0||jumlahProduct3.getText().length()==0){
//						String msg = getApplicationContext()
//								.getResources()
//								.getString(
//										R.string.app_sales_order_failed_please_add_0);
//						showCustomDialog(msg);
//					}else if(jumlahProduct.getText().toString().equals("0")&&jumlahProduct1.getText().toString().equals("0")&&
//							jumlahProduct2.getText().toString().equals("0")&&jumlahProduct3.getText().toString().equals("0")){
//
//						String msg = getApplicationContext()
//								.getResources()
//								.getString(
//										R.string.app_sales_order_failed_please_add_jumlah);
//						showCustomDialog(msg);
//					}else{
//						boolean containSameProduct = false;
//						for (DetailReqLoad detailReqLoad : detailReqLoadList) {
//							if (detailReqLoad.getKode_product()
//									.equalsIgnoreCase(
//											data.get(position)
//													.getKode_product())) {
//								containSameProduct = true;
//								break;
//							}
//						}
						if (passValidationForUpload()) {
//							int count = detailReqLoadList.size() + 1;
							updateListViewDetailOrder(new DetailReqLoad(
									data.get(position).getNama_product(),
									jumlahProduct.getText().toString(),
									data.get(position).getId_product(),
									batch.getText().toString()
							));
							chooseProductDialog.hide();
						}
//					}

				}
			});
			return row;
		}

		class UserHolder {
			ImageView list_img;
			TextView list_kodeProduct;
			TextView list_namaProduct;
			TextView list_harga;
			Button mButtonAddItem;
		}
	}

	public boolean passValidationForUpload() {
		if (GlobalApp.isBlank(jumlahProduct)) {
			GlobalApp.takeDefaultAction(
					jumlahProduct,
					DataPenjualan.this,
					getApplicationContext().getResources().getString(
							R.string.app_supplier_failed_jumlah));
			return false;
		}
		if (GlobalApp.isBlank(batch)) {
			GlobalApp.takeDefaultAction(
					batch,
					DataPenjualan.this,
					getApplicationContext().getResources().getString(
							R.string.app_supplier_failed_batch));
			return false;
		}
		return true;
	}

	private void updateListViewDetailOrder(DetailReqLoad detailReqLoad) {
		detailReqLoadList.add(detailReqLoad);
		cAdapter = new ListViewAdapter(DataPenjualan.this,
				R.layout.list_item_detail_penjualan, detailReqLoadList);
		listview.setAdapter(cAdapter);
		cAdapter.notifyDataSetChanged();
	}

	public class ListViewAdapter extends ArrayAdapter<DetailReqLoad> {
		Activity activity;
		int layoutResourceId;
		DetailReqLoad productData;
		ArrayList<DetailReqLoad> data = new ArrayList<DetailReqLoad>();

		public ListViewAdapter(Activity act, int layoutResourceId,
							   ArrayList<DetailReqLoad> data) {
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
				holder.list_nama_product = (TextView) row
						.findViewById(R.id.sales_order_title_nama_product);
//				holder.list_tanggal_penjualan= (TextView) row
//						.findViewById(R.id.sales_order_title_penjualan);
				holder.list_batch_product= (TextView) row
						.findViewById(R.id.sales_order_title_batch);
				holder.list_jumlah_order = (TextView) row
						.findViewById(R.id.sales_order_title_jumlah_order);
//				holder.list_jumlah_order1 = (TextView) row
//						.findViewById(R.id.sales_order_title_jumlah_order1);
//				holder.list_jumlah_order2 = (TextView) row
//						.findViewById(R.id.sales_order_title_jumlah_order2);
//				holder.list_jumlah_order3 = (TextView) row
//						.findViewById(R.id.sales_order_title_jumlah_order3);

				row.setTag(holder);
			} else {
				holder = (UserHolder) row.getTag();
			}
			productData = data.get(position);
			holder.list_nama_product.setText(productData.getNama_product());
//			holder.list_tanggal_penjualan.setText(productData.getBatch()+" - "+productData.getBatch());
			holder.list_batch_product.setText(productData.getBatch());
			holder.list_jumlah_order.setText(productData.getJumlah_order());
//			holder.list_kode_product.setTypeface(typefaceSmall);
			holder.list_jumlah_order.setTypeface(typefaceSmall);
//			holder.list_jumlah_order1.setTypeface(typefaceSmall);
//			holder.list_jumlah_order2.setTypeface(typefaceSmall);
//			holder.list_harga_jual.setTypeface(typefaceSmall);

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
											DataPenjualan.this,
											R.layout.list_item_detail_sales_order,
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
			TextView list_nama_product;
//			TextView list_tanggal_penjualan;
			TextView list_batch_product;
			TextView list_jumlah_order;
//			TextView list_jumlah_order1;
//			TextView list_jumlah_order2;
//			TextView list_jumlah_order3;
//			TextView list_harga_jual;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			gotoInventory();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void gotoInventory() {
		Intent i = new Intent(this, DetailJadwalActivity.class);
		startActivity(i);
		finish();
	}

	private SharedPreferences getSharedPrefereces() {
		return act.getSharedPreferences(AppVar.SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
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

}