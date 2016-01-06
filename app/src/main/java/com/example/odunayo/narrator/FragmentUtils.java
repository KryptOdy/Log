package com.example.odunayo.narrator;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

public class FragmentUtils {
    private static final String TAG = "FragmentUtils";

    // fragment for login or signup pages
    public static Fragment launchScreen = null;

    public static void showLaunchScreenFragment(MapsActivity activity) {
        launchScreen = new LaunchFragment();
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.launch_screen_placeholder, launchScreen);
        transaction.commitAllowingStateLoss();

//        activity.splash.setVisibility(View.GONE);
    }

    public static void closeLaunchScreenFragment(MapsActivity activity) {
        if (launchScreen != null) {
            FragmentManager fm = activity.getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
//          transaction.setCustomAnimations(android.R.animator.fade_in, R.animator.no_change);
            transaction.remove(launchScreen);
            transaction.commitAllowingStateLoss();
            launchScreen = null;
        }
    }


}
