package com.android.pir.gglc.pir;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.pir.gglc.absen.AppVar;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("deprecation")
public class Spg extends FragmentActivity {
	private Context act;
	private ProgressDialog progressDialog;
	private Handler handler = new Handler();
	private String message;
	private String response_data;
	private static final String LOG_TAG = Spg.class
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
	private String keterangan,id_user;
	private String streetName = "";
	private int counterFoto = 0;
	private LocationManager locationManager;
	private static Context _context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main_spg);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		act = this;
		menuBackButton = (ImageView) findViewById(R.id.menuBackButton);
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);

		edtSupplierKeterangan = (EditText) findViewById(R.id.supplier_et_keterangan);
		alamat = (EditText) findViewById(R.id.supplier_et_alamat);
		imageView1 = (ImageView) findViewById(R.id.supplier_foto1);
		btnUpload = (Button) findViewById(R.id.supplier_btn_upload);
		btnFoto1 = (Button) findViewById(R.id.supplier_btn_foto_1);

		SharedPreferences spPreferences = getSharedPrefereces();
		final String main_app_foto_1 = spPreferences.getString(
				AppVar.SHARED_PREFERENCES_SPG_FOTO_1, null);
		id_user = spPreferences.getString("id_user", null);
		if (main_app_foto_1 != null && main_app_foto_1.length() > 0) {
			counterFoto +=1;
			File dir = new File(AppVar.getFolderPath() + "/"
					+ IMAGE_DIRECTORY_NAME);
			String imgResource = dir.getPath() + "/" + main_app_foto_1;
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 3;
			final Bitmap bitmap = BitmapFactory.decodeFile(imgResource, options);
			imageView1.setImageBitmap(bitmap);

			imageView1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					final Dialog settingsDialog = new Dialog(act);
					settingsDialog.getWindow().requestFeature(
							Window.FEATURE_NO_TITLE);
					settingsDialog.setContentView(getLayoutInflater().inflate(
							R.layout.activity_popup_image_supplier, null));
					ImageView imgPreview = (ImageView) settingsDialog
							.findViewById(R.id.mygallery);
					imgPreview.setImageBitmap(bitmap);
					imgPreview.setImageBitmap(bitmap);
					Button button = (Button) settingsDialog
							.findViewById(R.id.btn);
					button.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							settingsDialog.dismiss();
						}
					});
					settingsDialog.show();

				}
			});

		}
		else {
			imageView1.setVisibility(View.INVISIBLE);
		}

		btnFoto1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				id_Image = "1";
				String msg = getApplicationContext().getResources().getString(R.string.app_supplier_already_take_foto_1);
				if(main_app_foto_1 != null && main_app_foto_1.length() > 0) {
					showCustomDialogReplaceOldFoto(msg);
				}
				else {
					gotoCaptureImage();
				}
			}
		});

		btnUpload.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(passValidationForUpload()) {
					keterangan = edtSupplierKeterangan.getText().toString();
					new UploadData().execute();
				}

			}
		});
	}

	protected String uploadImage(final String url,  final String id_staff,
								 final String foto_1, final String keterangan) {
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
					+ IMAGE_DIRECTORY_NAME + "/"
					+ foto_1);
			if (dir1.exists() && foto_1 != null) {
				entity.addPart("image_1", new FileBody(dir1));
				entity.addPart("foto1", new StringBody(foto_1));
			}
			else {
				entity.addPart("foto1", new StringBody(""));
			}

			entity.addPart("id_staff", new StringBody(id_staff));
			entity.addPart("keterangan", new StringBody(keterangan));

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
			String uploadSupplier = AppVar.CONFIG_APP_URL_UPLOAD_INSERT_SPG;
			String upload_image_supplier_url = url
					+ uploadSupplier;
			SharedPreferences spPreferences = getSharedPrefereces();
			String main_app_id_staff = spPreferences.getString(
					AppVar.SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_JADWAL, null);
			String main_app_id_foto1 = spPreferences.getString(
					AppVar.SHARED_PREFERENCES_SPG_FOTO_1, null);
			/***********************
			 * Upload Image Supplier
			 */
			response_data = uploadImage(
					upload_image_supplier_url,
					main_app_id_staff,
					main_app_id_foto1,
					keterangan);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Log.d(LOG_TAG, "response:" + response_data);
			if (response_data != null && response_data.length() > 0) {
				if (response_data.startsWith("Error occurred")) {
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
					saveAppSupplierFoto1("");

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
					showCustomDialogDownloadSuccess(msg);

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

	public boolean passValidationForUpload() {
		if (GlobalApp.isBlank(edtSupplierKeterangan)) {
			GlobalApp.takeDefaultAction(
					edtSupplierKeterangan,
					Spg.this,
					getApplicationContext().getResources().getString(
							R.string.app_supplier_failed_no_keterangan));
			return false;
		}
		return true;
	}

	public void showCustomDialogReplaceOldFoto(String msg) {
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				act);
		alertDialogBuilder
				.setMessage(msg)
				// .setTitle(title)
				.setCancelable(false)
				.setNegativeButton(
						getApplicationContext().getResources().getString(
								R.string.MSG_DLG_LABEL_YES),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								gotoCaptureImage();

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

	protected void refreshStatus() {
		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);
	}


	public void gotoCaptureImage() {

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		// start the image capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}

	/*
 * Creating file uri to store image/video
 */
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
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(dir.getPath() + File.separator + id_user+timeStamp + "_" + id_Image + ".jpg");
			newImageName = id_user+timeStamp + "_"
					+ id_Image + ".jpg";
		} else {
			return null;
		}
		return mediaFile;
	}

	/**
	 * Receiving activity result method will be called after closing the camera
	 * */
	@SuppressWarnings("static-access")
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// if the result is capturing Image
		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				if(id_Image.equalsIgnoreCase("1")) {
					File newFile = new File(AppVar.getFolderPath() + "/"
							+ IMAGE_DIRECTORY_NAME + "/" + newImageName);
					Uri contentUri = Uri.fromFile(newFile);
					Bitmap  photo = null;

					try {
						photo = MediaStore.Images.Media.getBitmap(getContentResolver(),
								contentUri);
						OutputStream fOut = new FileOutputStream(newFile);
						photo.compress(Bitmap.CompressFormat.JPEG, 20, fOut);
						fOut.flush();
						fOut.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

					saveAppSupplierFoto1(newImageName);
				}

				refreshStatus();
				// successfully captured the image
				// display it in image view
			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled Image capture
				Toast.makeText(getApplicationContext(),
						"User cancelled image capture", Toast.LENGTH_SHORT)
						.show();
			} else {
				// failed to capture image
				Toast.makeText(getApplicationContext(),
						"Sorry! Failed to capture image", Toast.LENGTH_SHORT)
						.show();
			}
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


	public void showCustomDialogDownloadSuccess(String msg) {
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

								edtSupplierKeterangan.setText("");

								refreshStatus();
								gotoInventory();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	private SharedPreferences getSharedPrefereces() {
		return act.getSharedPreferences(AppVar.SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
	}

	public void saveAppSupplierFoto1(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(AppVar.SHARED_PREFERENCES_SPG_FOTO_1,
				responsedata);
		editor.commit();
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