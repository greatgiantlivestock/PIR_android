package com.android.pir.gglc.absen;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.pir.gglc.pir.CheckoutActivity;
import com.android.pir.gglc.pir.DashboardActivity;
import com.android.pir.gglc.pir.History_Canvassing;
import com.android.pir.gglc.pir.IconTextTabsActivity;
import com.android.pir.mobile.R;

@SuppressWarnings("deprecation")
public class Orderan extends ActionBarActivity implements
		NavigationDrawerCallbacks {
	private Toolbar mToolbar;
	private NavigationDrawerFragment mNavigationDrawerFragment;

	private static final String LOG_TAG = Orderan.class.getSimpleName();
	private WebView webView;
	private ProgressBar progressBar;
	private int int_ventilator;
	private Context act;
//	private ValueCallback<Uri> mUploadMessage;
//	private final static int FILECHOOSER_RESULTCODE=1;

	private ValueCallback<Uri> mUploadMessage;
	public ValueCallback<Uri[]> uploadMessage;
	public static final int REQUEST_SELECT_FILE = 100;
	private final static int FILECHOOSER_RESULTCODE = 1;

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode,
//									Intent intent) {
//		if(requestCode==FILECHOOSER_RESULTCODE)
//		{
//			if (null == mUploadMessage) return;
//			Uri result = intent == null || resultCode != RESULT_OK ? null
//					: intent.getData();
//			mUploadMessage.onReceiveValue(result);
//			mUploadMessage = null;
//		}
//	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			if (requestCode == REQUEST_SELECT_FILE)
			{
				if (uploadMessage == null)
					return;
				uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
				uploadMessage = null;
			}
		}
		else if (requestCode == FILECHOOSER_RESULTCODE)
		{
			if (null == mUploadMessage)
				return;
			// Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
			// Use RESULT_OK only if you're implementing WebView inside an Activity
			Uri result = intent == null || resultCode != Orderan.RESULT_OK ? null : intent.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
		}
		else
			Toast.makeText(getApplicationContext(), "Failed to Upload Image", Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main_orderan_new);
		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		act = this;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.title_activity_all);//untuk set title bar

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.fragment_drawer);
		mNavigationDrawerFragment.setup(R.id.fragment_drawer,
				(DrawerLayout) findViewById(R.id.drawer), mToolbar);
		mNavigationDrawerFragment.selectItem(5);

		webView = (WebView) findViewById(R.id.webViewfragment);
		progressBar = (ProgressBar)findViewById(R.id.progweb);
		progressBar.setVisibility(View.INVISIBLE);


		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
//		webView = new WebView(this);
//		webView.setWebViewClient(new myWebClient());
//		setContentView(webView);

		this.getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				progressBar.setProgress(progress);
				if (progress == 100) {
					progressBar.setVisibility(View.GONE);

				} else {
					progressBar.setVisibility(View.VISIBLE);
				}
			}

//			//For Android 4.1
//			public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
//				mUploadMessage = uploadMsg;
//				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//				i.addCategory(Intent.CATEGORY_OPENABLE);
//				i.setType("image/*");
//				Orderan.this.startActivityForResult( Intent.createChooser( i, "File Chooser" ), Orderan.FILECHOOSER_RESULTCODE );
//
//			}

			// For Lollipop 5.0+ Devices
			public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams)
			{
				if (mUploadMessage != null) {
					mUploadMessage.onReceiveValue(null);
					uploadMessage = null;
				}

				uploadMessage = filePathCallback;

				Intent intent = fileChooserParams.createIntent();
				try
				{
					startActivityForResult(intent, REQUEST_SELECT_FILE);
				} catch (ActivityNotFoundException e)
				{
					uploadMessage = null;
					Toast.makeText(getApplicationContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
					return false;
				}
				return true;
			}

		});

		webView.setWebViewClient(new WebViewClient());
		webView.loadUrl(AppVar.CONFIG_APP_URL_PUBLIC);

		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webView.getSettings().setBuiltInZoomControls(false);
		webView.setOnKeyListener(new View.OnKeyListener(){
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
					webView.goBack();
					return true;
				}
				return false;
			}
		});

		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
	}

	public static boolean checkInternetConnection(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isAvailable()
				&& cm.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			Log.w("internet", "Internet Connection Not Present");
			return false;
		}
	}

	public class myWebClient extends WebViewClient
	{
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub

			view.loadUrl(url);
			return true;

		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);

			progressBar.setVisibility(View.GONE);
		}
	}

	//flipscreen not loading again
	@Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
	}


	@Override
	public void onNavigationDrawerItemSelected(int position) {
		if (mNavigationDrawerFragment != null) {
			if (mNavigationDrawerFragment.getCurrentSelectedPosition() != 5) {
				if (position == 0) {
					Intent intentActivity = new Intent(this,
							DashboardActivity.class);
					startActivity(intentActivity);
					finish();
				}
				else if (position == 1) {
					Intent intentActivity = new Intent(this,
							IconTextTabsActivity.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 2) {
					Intent intentActivity = new Intent(this,
							CheckoutActivity.class);
					startActivity(intentActivity);
					finish();
				}
				else if (position == 3) {
					Intent intentActivity = new Intent(this,
							History_Canvassing.class);
					startActivity(intentActivity);
					finish();
				}
				else if (position == 4) {
					Intent intentActivity = new Intent(this,
							ChangePassword.class);
					startActivity(intentActivity);
					finish();
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			showCustomDialogExit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
