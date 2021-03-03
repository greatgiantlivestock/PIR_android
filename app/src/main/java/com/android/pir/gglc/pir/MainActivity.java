//package com.android.pir.gglc.pir;
//
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.design.widget.BottomNavigationView;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.view.KeyEvent;
//import android.view.MenuItem;
//
//import com.android.pir.gglc.fragment1.CartFragment;
//import com.android.pir.gglc.fragment1.GiftsFragment;
//import com.android.pir.gglc.fragment1.ProfileFragment;
//import com.android.pir.gglc.fragment1.StoreFragment;
//import com.android.pir.mobile.R;
//
//
//public class MainActivity extends AppCompatActivity {
//
////    private ActionBar toolbar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
////        toolbar = getSupportActionBar();
//
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//
//        // attaching bottom sheet behaviour - hide / show on scroll
//        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
//        layoutParams.setBehavior(new BottomNavigationBehavior());
//
//        // load the store fragment by default
////        toolbar.setTitle("Shop");
//        loadFragment(new StoreFragment());
//    }
//
//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            Fragment fragment;
//            switch (item.getItemId()) {
//                case R.id.navigation_shop:
////                    toolbar.setTitle("Shop");
//                    fragment = new StoreFragment();
//                    loadFragment(fragment);
//                    return true;
//                case R.id.navigation_gifts:
////                    toolbar.setTitle("My Gifts");
//                    fragment = new GiftsFragment();
//                    loadFragment(fragment);
//                    return true;
//                case R.id.navigation_cart:
////                    toolbar.setTitle("Cart");
//                    fragment = new CartFragment();
//                    loadFragment(fragment);
//                    return true;
//                case R.id.navigation_profile:
////                    toolbar.setTitle("Profile");
//                    fragment = new ProfileFragment();
//                    loadFragment(fragment);
//                    return true;
//            }
//
//            return false;
//        }
//    };
//
//    /**
//     * loading fragment into FrameLayout
//     *
//     * @param fragment
//     */
//    private void loadFragment(Fragment fragment) {
//        // load fragment
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.frame_container, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
//
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//            showCustomDialogExit();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//
//    public void showCustomDialogExit() {
//        String msg = "Anda yakin ingin keluar aplikasi?";
//        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                this);
//        alertDialogBuilder
//                .setMessage(msg)
//                .setCancelable(false)
//                .setNegativeButton("Yes",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int id) {
//                                android.os.Process
//                                        .killProcess(android.os.Process.myPid());
//
//                            }
//                        })
//                .setPositiveButton("No",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int id) {
//                                AlertDialog alertDialog = alertDialogBuilder
//                                        .create();
//                                alertDialog.dismiss();
//
//                            }
//                        });
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//    }
//}
