package com.android.canvasing.gglc.absen;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.canvasing.mobile.R;

import java.util.Calendar;

import static com.android.canvasing.gglc.absen.AppVar.SHARED_PREFERENCES_NAME;

@SuppressWarnings("deprecation")
public class UselessHistoryActivity extends ActionBarActivity implements
		NavigationDrawerCallbacks {
	private Context act;
	private Toolbar mToolbar;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private EditText mulai,sampai;
	private Button history;
	final int Date_Dialog_ID=0;
	final int Date_Dialog_ID1=1;
	int cDay,cMonth,cYear;
	Calendar cDate;
	int sDay,sMonth,sYear;
	private WebView webView;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_history_jadwal);

		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		act = this;
		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle(R.string.title_activity_all);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.fragment_drawer);
		mNavigationDrawerFragment.setup(R.id.fragment_drawer,
				(DrawerLayout) findViewById(R.id.drawer), mToolbar);
		mNavigationDrawerFragment.selectItem(1);

		mulai = (EditText)findViewById(R.id.date_from);
		sampai = (EditText)findViewById(R.id.date_to);
		history = (Button)findViewById(R.id.search);
		webView = (WebView)findViewById(R.id.webView1);
		progressBar = (ProgressBar)findViewById(R.id.progweb);
		progressBar.setVisibility(View.INVISIBLE);

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

		cDate=Calendar.getInstance();
		cDay=cDate.get(Calendar.DAY_OF_MONTH);
		cMonth=cDate.get(Calendar.MONTH);
		cYear=cDate.get(Calendar.YEAR);
		sDay=cDay;
		sMonth=cMonth;
		sYear=cYear;

		history.setEnabled(false);

		history.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(mulai.getText().toString().equals("") && sampai.getText().toString().equals("")){
					Toast.makeText(UselessHistoryActivity.this, "Kolom tangal masih kosong",Toast.LENGTH_LONG).show();
				}else{
					SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
					final String nama_awo = prefs.getString("nama_awo","null");
					final String id_awo = prefs.getString("id_awo","null");
					final String tanggal1 = mulai.getText().toString();
					final String tanggal2 = sampai.getText().toString();

					getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
					webView.setWebChromeClient(new WebChromeClient() {
						public void onProgressChanged(WebView view, int progress) {
							progressBar.setProgress(progress);
							if (progress == 100) {
								progressBar.setVisibility(View.GONE);

							} else {
								progressBar.setVisibility(View.VISIBLE);

							}
						}
					});

					webView.setWebViewClient(new myWebClient());

					String postData = ("nama_awo="+nama_awo+"&id_awo="+id_awo+"&tanggal1="+tanggal1+"&tanggal2="+tanggal2);
					webView.getSettings().setJavaScriptEnabled(true);
					webView.postUrl(AppVar.HISTORY_URL, postData.getBytes());
				}
			}
		});
	}



	public class myWebClient extends WebViewClient{


		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
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
		history.setEnabled(true);
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

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		if (mNavigationDrawerFragment != null) {
			if (mNavigationDrawerFragment.getCurrentSelectedPosition() != 1) {
				if (position == 0) {
					Intent intentActivity = new Intent(this,
							AbsenActivity.class);
					startActivity(intentActivity);
					finish();
				} else if (position == 2) {
					Intent intentActivity = new Intent(this,
							ChangePassword.class);
					startActivity(intentActivity);
					finish();
				}/*else if (position == 3) {
					Intent intentActivity = new Intent(this,
							CtomerActivity.class);
					startActivity(intentActivity);
					finish();
				}*/
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
