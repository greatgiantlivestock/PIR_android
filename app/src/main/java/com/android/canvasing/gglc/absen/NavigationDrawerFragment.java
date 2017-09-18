package com.android.canvasing.gglc.absen;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.canvasing.gglc.database.MstUser;
import com.android.canvasing.mobile.R;
import com.android.canvasing.gglc.database.DatabaseHandler;
import com.android.canvasing.gglc.database.User;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

@SuppressWarnings("deprecation")
public class NavigationDrawerFragment extends Fragment implements
		NavigationDrawerCallbacks {
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
	private static final String PREFERENCES_FILE = "my_app_settings";
	private NavigationDrawerCallbacks mCallbacks;
	private RecyclerView mDrawerList;
	private View mFragmentContainerView;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mActionBarDrawerToggle;
	private boolean mUserLearnedDrawer;
	// private boolean mFromSavedInstanceState;
	private int mCurrentSelectedPosition;
	private TextView txtKodeStaff;
	private ImageView foto;
	private TextView txtNamaLengkap;
	private TextView txtBranch;
	private Typeface typefaceSmall;
	private DatabaseHandler db;
	private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_navigation_menu,
				container, false);

		mDrawerList = (RecyclerView) view.findViewById(R.id.drawerList);
//		typefaceSmall = Typeface.createFromAsset(getActivity().getAssets(),
//				"fonts/AliquamREG.ttf");
		db = new DatabaseHandler(getActivity());
		txtKodeStaff = (TextView) view.findViewById(R.id.txtUsername);
		txtKodeStaff.setTypeface(typefaceSmall);
		txtNamaLengkap = (TextView) view.findViewById(R.id.txtName);
		//foto = (ImageView) view.findViewById(R.id.imgAvatar);

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_new);
		RoundedBitmapDrawable rounded = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
		rounded.setCornerRadius(bitmap.getWidth());

		ImageView drawerProfile = (ImageView)view.findViewById(R.id.imgAvatar);
		drawerProfile.setImageDrawable(rounded);

		txtNamaLengkap.setTypeface(typefaceSmall);
		txtBranch = (TextView) view.findViewById(R.id.txtBranch);
		txtBranch.setTypeface(typefaceSmall);

		LinearLayoutManager layoutManager = new LinearLayoutManager(
				getActivity());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mDrawerList.setLayoutManager(layoutManager);
		mDrawerList.setHasFixedSize(true);

		final List<NavigationItem> navigationItems = getMenu();
		NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(
				navigationItems);
		adapter.setNavigationDrawerCallbacks(this);
		mDrawerList.setAdapter(adapter);
		selectItem(mCurrentSelectedPosition);

		ArrayList<MstUser> staff_list = db.getAllUser();
		MstUser user = new MstUser();
		for (MstUser tempStaff : staff_list)
			user = tempStaff;

		txtKodeStaff.setText(user.getNama());
		txtNamaLengkap.setText(user.getNo_hp());
		txtBranch.setText("");

		return view;
	}

	private void setupImageRounded() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUserLearnedDrawer = Boolean.valueOf(readSharedSetting(getActivity(),
				PREF_USER_LEARNED_DRAWER, "false"));
		if (savedInstanceState != null) {
			mCurrentSelectedPosition = savedInstanceState
					.getInt(STATE_SELECTED_POSITION);
			// mFromSavedInstanceState = true;
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (NavigationDrawerCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement NavigationDrawerCallbacks.");
		}
	}

	public ActionBarDrawerToggle getActionBarDrawerToggle() {
		return mActionBarDrawerToggle;
	}

	public void setActionBarDrawerToggle(
			ActionBarDrawerToggle actionBarDrawerToggle) {
		mActionBarDrawerToggle = actionBarDrawerToggle;
	}

	public void setup(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
		mFragmentContainerView = getActivity().findViewById(fragmentId);
		if (mFragmentContainerView.getParent() instanceof ScrimInsertsFrameLayout) {
			mFragmentContainerView = (View) mFragmentContainerView.getParent();
		}
		mDrawerLayout = drawerLayout;
		//mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(
		//		R.color.myPrimaryDarkColor));

		mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(),
				mDrawerLayout, toolbar, R.string.drawer_open,
				R.string.drawer_close) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				if (!isAdded())
					return;
				getActivity().invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				if (!isAdded())
					return;
				if (!mUserLearnedDrawer) {
					mUserLearnedDrawer = true;
					saveSharedSetting(getActivity(), PREF_USER_LEARNED_DRAWER,
							"true");
				}

				getActivity().invalidateOptionsMenu();
			}
		};

		// if (!mUserLearnedDrawer && !mFromSavedInstanceState)
		// mDrawerLayout.openDrawer(mFragmentContainerView);

		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mActionBarDrawerToggle.syncState();
			}
		});

		mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
	}

	public void openDrawer() {
		mDrawerLayout.openDrawer(mFragmentContainerView);
	}

	public void closeDrawer() {
		mDrawerLayout.closeDrawer(mFragmentContainerView);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	public List<NavigationItem> getMenu() {
		List<NavigationItem> items = new ArrayList<NavigationItem>();
		items.add(new NavigationItem(getResources().getString(R.string.menu_1),
				getResources().getDrawable(R.drawable.location_mentah)));
		items.add(new NavigationItem(getResources().getString(R.string.menu_2),
				getResources().getDrawable(R.drawable.history)));
		items.add(new NavigationItem(getResources().getString(R.string.menu_3),
				getResources().getDrawable(R.drawable.change)));
		items.add(new NavigationItem(getResources().getString(R.string.menu_4),
				getResources().getDrawable(R.drawable.ckin)));
		items.add(new NavigationItem(getResources().getString(R.string.menu_5),
				getResources().getDrawable(R.drawable.ckout)));
		return items;
	}

	/**
	 * Changes the icon of the drawer to back
	 */
	public void showBackButton() {
		if (getActivity() instanceof ActionBarActivity) {
			((ActionBarActivity) getActivity()).getSupportActionBar()
					.setDisplayHomeAsUpEnabled(true);
		}
	}

	/**
	 * Changes the icon of the drawer to menu
	 */
	public void showDrawerButton() {
		if (getActivity() instanceof ActionBarActivity) {
			((ActionBarActivity) getActivity()).getSupportActionBar()
					.setDisplayHomeAsUpEnabled(false);
		}
		mActionBarDrawerToggle.syncState();
	}

	public void selectItem(int position) {
		mCurrentSelectedPosition = position;
		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		}
		if (mCallbacks != null) {
			mCallbacks.onNavigationDrawerItemSelected(position);
		}
		((NavigationDrawerAdapter) mDrawerList.getAdapter())
				.selectPosition(position);
	}

	public int getCurrentSelectedPosition() {
		return mCurrentSelectedPosition;
	}

	public boolean isDrawerOpen() {
		return mDrawerLayout != null
				&& mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mActionBarDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		selectItem(position);
	}

	public DrawerLayout getDrawerLayout() {
		return mDrawerLayout;
	}

	public void setDrawerLayout(DrawerLayout drawerLayout) {
		mDrawerLayout = drawerLayout;
	}

	public static void saveSharedSetting(Context ctx, String settingName,
			String settingValue) {
		SharedPreferences sharedPref = ctx.getSharedPreferences(
				PREFERENCES_FILE, MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(settingName, settingValue);
		editor.apply();
	}

	public static String readSharedSetting(Context ctx, String settingName,
			String defaultValue) {
		SharedPreferences sharedPref = ctx.getSharedPreferences(
				PREFERENCES_FILE, MODE_PRIVATE);
		return sharedPref.getString(settingName, defaultValue);
	}
}
