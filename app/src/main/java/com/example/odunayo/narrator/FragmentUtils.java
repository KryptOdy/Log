package com.example.odunayo.narrator;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;

import com.example.odunayo.narrator.Framework.Log;
import com.example.odunayo.narrator.Framework.Story;
import com.example.odunayo.narrator.Framework.UiUtils;

public class FragmentUtils {
    private static final String TAG = "FragmentUtils";



    private static void showOverlayElement(MapsActivity activity, Fragment fragment) {
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        transaction.replace(R.id.overlay_placeholder, fragment);
        transaction.commit();
    }

    private static void closeOverlayElement(MapsActivity activity, Fragment fragment) {
        if (fragment != null) {
            FragmentManager fm = activity.getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            transaction.remove(fragment);
            transaction.commit();
        }

        UiUtils.hideKeyboard(activity);
    }


    // fragment for login or signup pages
    public static Fragment launchScreen = null;

    public static void showLaunchScreenFragment(MapsActivity activity) {
        launchScreen = new LaunchFragment();
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.launch_screen_placeholder, launchScreen);
        transaction.commitAllowingStateLoss();
    }

    public static void closeLaunchScreenFragment(MapsActivity activity) {
        Log.d(TAG, "Closing Launch Screen?");
        if (launchScreen != null) {
            FragmentManager fm = activity.getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
           transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            transaction.remove(launchScreen);
            transaction.commitAllowingStateLoss();
            launchScreen = null;
            Log.d(TAG, "Should be closed");
        }
    }

    public static Fragment storyFragment;

    public static void showStoryFragment(MapsActivity activity) {
        storyFragment = new StoryFragment();
        showOverlayElement(activity, storyFragment);
//        FragmentManager fm = activity.getFragmentManager();
//        FragmentTransaction transaction = fm.beginTransaction();
//        transaction.replace(R.id.launch_screen_placeholder, launchScreen);
//        transaction.commitAllowingStateLoss();
    }

    public static void closeStoryFragment(MapsActivity activity) {
        closeOverlayElement(activity,storyFragment);
        storyFragment = null;
    }

    public static Fragment viewStoryFragment;

    public static void showViewStoryFragment(MapsActivity activity, Story story){
        viewStoryFragment =  ViewStoryFragment.newInstance(story);
        showOverlayElement(activity, viewStoryFragment);
    }

    public static void closeViewStoryFragment(MapsActivity activity){
        closeOverlayElement(activity,viewStoryFragment);
        viewStoryFragment = null;

    }




}
