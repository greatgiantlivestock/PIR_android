package com.android.pir.gglc.absen;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.pir.gglc.pir.CheckoutActivity;
import com.android.pir.gglc.pir.DashboardActivity;
import com.android.pir.gglc.pir.History_Canvassing;
import com.android.pir.gglc.pir.IconTextTabsActivity_lap;
import com.android.pir.gglc.pir.PlanVisitActivity;
import com.android.pir.gglc.pir.PlanVisitActivity2;
import com.android.pir.gglc.pir.ProspectPlanVisit;
import com.android.pir.mobile.R;

import java.util.HashMap;

@SuppressWarnings("deprecation")
public class ChangePassword extends ActionBarActivity implements
		NavigationDrawerCallbacks {
	private Context act;
	private Toolbar mToolbar;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private EditText current,newpwd,confirmpwd;
	private Button save;
	private CheckBox mCbShowPwdcurrent,mCbShowPwdnew,mCbShowPwdconfirm;

	private static final String LOG_TAG = ChangePassword.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main_change_password);
		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		act = this;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.title_activity_all);//untuk set title bar

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.fragment_drawer);
		mNavigationDrawerFragment.setup(R.id.fragment_drawer,
				(DrawerLayout) findViewById(R.id.drawer), mToolbar);
		mNavigationDrawerFragment.selectItem(5);

		current = (EditText)findViewById(R.id.Current);
		newpwd = (EditText)findViewById(R.id.newpwd);
		confirmpwd = (EditText)findViewById(R.id.confirmpwd);
		save = (Button)findViewById(R.id.changepwd);

		mCbShowPwdcurrent = (CheckBox) findViewById(R.id.chbox_showpasswordcurrent);
		mCbShowPwdnew = (CheckBox) findViewById(R.id.chbox_showpasswordnew);
		mCbShowPwdconfirm = (CheckBox) findViewById(R.id.chbox_showpasswordconfirm);

		//Add onCheckedListener
		mCbShowPwdcurrent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (!isChecked) {
					//Show password
					current.setTransformationMethod(PasswordTransformationMethod.getInstance());
				} else {
					//Hide password
					current.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}
			}
		});

		mCbShowPwdnew.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (!isChecked) {
					//Show password
					newpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
				} else {
					//Hide password
					newpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}
			}
		});

		mCbShowPwdconfirm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (!isChecked) {
					//Show password
					confirmpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
				} else {
					//Hide password
					confirmpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}
			}
		});

		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
                if(current.getText().length()==0 && newpwd.getText().length()==0 && confirmpwd.getText().length()==0){
                    Toast.makeText(ChangePassword.this, "Semua kolom masih kosong",Toast.LENGTH_LONG).show();
                    current.requestFocus();
                }else if(newpwd.getText().length()==0 && confirmpwd.getText().length()==0){
                    Toast.makeText(ChangePassword.this, "Kolom password baru dan konfirmasi password masih kosong",Toast.LENGTH_LONG).show();
                    newpwd.requestFocus();
                }else if(current.getText().length()==0 && confirmpwd.getText().length()==0){
                    Toast.makeText(ChangePassword.this, "Kolom password lama dan konfirmasi masih kosong",Toast.LENGTH_LONG).show();
                    current.requestFocus();
                }else if(current.getText().length()==0){
                    Toast.makeText(ChangePassword.this, "Kolom password lama masih kosong",Toast.LENGTH_LONG).show();
                    current.requestFocus();
                }else{
                    UpdatePassword();
                }
			}
		});
	}

	private void UpdatePassword() {
		SharedPreferences prefs = getSharedPreferences(AppVar.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
		String pwd = prefs.getString("password","nopassword");

		if(pwd.equals("nopassword")){
			Toast.makeText(this, "Periksa data Password",Toast.LENGTH_LONG).show();
		}else{
			String currentpas = current.getText().toString();
			String newpas = newpwd.getText().toString();
			String confirmpas = confirmpwd.getText().toString();

			Log.d(LOG_TAG, "Password lama = "+pwd);
			if (!currentpas.equals(pwd)){
				Toast.makeText(this, "Password lama anda salah",Toast.LENGTH_LONG).show();
			}else{
				if(!newpas.equals(confirmpas)){
					Toast.makeText(this, "Password baru dan konfirmasi password belum sama",Toast.LENGTH_LONG).show();
				}else{
					showCustomDialogChangePassword();
				}
			}
		}
	}

	private void Changepwd() {
		if(checkInternetConnection(act)){
			SharedPreferences prefs = getSharedPreferences(AppVar.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
			final String id_user = prefs.getString("id_user","0");
			final String newpassword = confirmpwd.getText().toString();
			class insertToDatabase extends AsyncTask<Void, Void, String> {
				ProgressDialog loading;

				@Override
				protected void onPreExecute() {
				super.onPreExecute();
				loading = ProgressDialog.show(ChangePassword.this,"Proses...","Silahkan Tunggu...",false,false);
				}

				@Override
				protected void onPostExecute(String s) {
					super.onPostExecute(s);
					loading.dismiss();
				}

				@Override
				protected String doInBackground(Void... params) {
					HashMap<String,String> hashMap  = new HashMap<>();
					hashMap.put("id_user",id_user);
					hashMap.put("password",newpassword);

					RequestHandler rh = new RequestHandler();
					String res = rh.sendPostRequest(AppVar.POST_UPDATE_PASSWORD, hashMap);
					return res;
				}
			}
			insertToDatabase in = new insertToDatabase();
			in.execute();
			Toast.makeText(this, "Password telah diperbaharui",Toast.LENGTH_LONG).show();

			Intent i = getBaseContext().getPackageManager()
					.getLaunchIntentForPackage( getBaseContext().getPackageName() );
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
			//android.os.Process.killProcess(android.os.Process.myPid());
		}else{
			Toast.makeText(this, "Silahkan periksa koneksi internet anda",Toast.LENGTH_LONG).show();
		}

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


	@Override
	public void onNavigationDrawerItemSelected(int position) {
		if (mNavigationDrawerFragment != null) {
			if (mNavigationDrawerFragment.getCurrentSelectedPosition() != 5) {
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
				}else if (position ==2) {
					Intent intentActivity = new Intent(this,
							ProspectPlanVisit.class);
					startActivity(intentActivity);
					finish();
				}
//				else if (position ==3) {
//					Intent intentActivity = new Intent(this,
//							IconTextTabsActivity_lap.class);
//					startActivity(intentActivity);
//					finish();
//				}else if (position == 4) {
//					Intent intentActivity = new Intent(this,
//							CheckoutActivity.class);
//					startActivity(intentActivity);
//					finish();
//				}
				else if (position == 3) {
					Intent intentActivity = new Intent(this,
							DashboardActivity.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 4) {
					Intent intentActivity = new Intent(this,
							History_Canvassing.class);
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

	public void showCustomDialogChangePassword() {
		String msg = getApplicationContext().getResources().getString(
				R.string.MSG_DLG_LABEL_CHANGE_PASSWORD_DIALOG);
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
								Changepwd();
								//android.os.Process.killProcess(android.os.Process.myPid());

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
//			showCustomDialogExit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
