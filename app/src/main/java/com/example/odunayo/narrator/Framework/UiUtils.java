package com.example.odunayo.narrator.Framework;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Odunayo on 1/6/2016.
 */
public class UiUtils {

    private static Typeface type;

    private static Typeface regularType;


    public static void showKeyboard(Activity activity, EditText focus) {
        try {
            final InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(focus, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            // if no keyboard, do nothing
        }
    }

    public static void hideKeyboard(Activity activity) {
        hideKeyboard(activity, activity.getCurrentFocus());
    }

    public static void hideKeyboard(Activity activity, View focus) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(focus.getWindowToken(), 0);
        } catch (Exception e) {
            // if no keyboard, do nothing
        }
    }


    public static AlertDialog showAlertDialog(AlertDialog.Builder builder) {
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
        TextView dialogMessage = (TextView) dialog.findViewById(android.R.id.message);
//        if (dialogMessage != null)
//            dialogMessage.setTypeface(UiUtils.getTypeface(builder.getContext().getApplicationContext()));
        ArrayList<Button> dialogButtons = new ArrayList<Button>();
        dialogButtons.add(dialog.getButton(DialogInterface.BUTTON_POSITIVE));
        dialogButtons.add(dialog.getButton(DialogInterface.BUTTON_NEGATIVE));
        dialogButtons.add(dialog.getButton(DialogInterface.BUTTON_NEUTRAL));
//        for (Button button : dialogButtons)
//            if (button != null)
//                button.setTypeface(UiUtils.getTypeface(builder.getContext().getApplicationContext()));

        return dialog;
    }

    public static AlertDialog.Builder getNewAlertDialogBuilder(Context context) {

            return new AlertDialog.Builder(context);
    }

    public static Typeface getTypeface(Context applicationContext) {
//        if (type == null)
//            type = Typeface.createFromAsset(applicationContext.getAssets());
        return type;
    }

    public static Typeface getTypefaceRegular(Context applicationContext) {
        if (regularType == null)
            regularType = Typeface.createFromAsset(applicationContext.getAssets(), "Lato-Regular.ttf");
        return regularType;
    }

}