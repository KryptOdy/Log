package com.example.odunayo.narrator;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.odunayo.narrator.Framework.Log;
import com.example.odunayo.narrator.Framework.UiUtils;
import com.example.odunayo.narrator.Server.Callback;
import com.example.odunayo.narrator.Server.NarratorServerCalls;

import org.apache.http.HttpStatus;
import org.json.JSONObject;


public class LaunchFragment extends Fragment{
    private static final String TAG = "LaunchFragment";

    private MapsActivity activity;

    private Button signup;
    private Button signin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = (MapsActivity) getActivity();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.launch_screen, container, false);


        signup = (Button) rootView.findViewById(R.id.sign_up_button);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignup();

            }
        });
        signin = (Button) rootView.findViewById(R.id.sign_in_button);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogin();
            }
        });


        return rootView;

    }

    public void showSignup(){

        View signupView = getActivity().getLayoutInflater().inflate(R.layout.launch_view, null);
        final EditText username = (EditText) signupView .findViewById(R.id.username);
        final EditText password = (EditText) signupView .findViewById(R.id.password);

        AlertDialog.Builder alertDialogBuilder = UiUtils.getNewAlertDialogBuilder(activity);

        alertDialogBuilder
                .setView(signupView)
                .setMessage("Signup Here")
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String sendUserName = username.getText().toString();
                        String sendPassword = password.getText().toString();

                        if (sendSignup(sendUserName, sendPassword)) {
                            UiUtils.hideKeyboard(activity, username);
                            UiUtils.hideKeyboard(activity, password);
                            dialog.cancel();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        UiUtils.hideKeyboard(activity, username);
                        UiUtils.hideKeyboard(activity, password);
                        dialog.cancel();
                    }
                });


        UiUtils.showAlertDialog(alertDialogBuilder);

    }

    public boolean sendSignup(String username, String password){

        final boolean[] success = {false};

        NarratorServerCalls.signup(username, password, new Callback() {
            @Override
            public void postExecute(JSONObject json, int status,
                                    String... strings) {

                Log.d(TAG, "STATUS of signup: " + status);
                if (status == HttpStatus.SC_BAD_REQUEST || status == HttpStatus.SC_CREATED) {
                    try {
                        if (status == HttpStatus.SC_BAD_REQUEST) {
                            Toast.makeText(activity, json.getString("message"), Toast.LENGTH_SHORT).show();
                        } else if (status == HttpStatus.SC_CREATED) {
                            String userId = json.getString("user_id");
                            activity.userId = userId;

                            success[0] = true;
                            Toast.makeText(activity, "Successfully created your account!", Toast.LENGTH_SHORT).show();


                        }
                    } catch (Exception e) {
                        Toast.makeText(activity, "Bad Connection.", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(activity, "Bad Connection.", Toast.LENGTH_SHORT).show();

            }
        });

        if (success[0])
            return true;

        return false;
    }

    public void showLogin(){

            View signupView = getActivity().getLayoutInflater().inflate(R.layout.launch_view, null);
            final EditText username = (EditText) signupView .findViewById(R.id.username);
            final EditText password = (EditText) signupView .findViewById(R.id.password);

            AlertDialog.Builder alertDialogBuilder = UiUtils.getNewAlertDialogBuilder(activity);

            alertDialogBuilder
                    .setView(signupView)
                    .setMessage("Login Here")
                    .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            String sendUserName = username.getText().toString();
                            String sendPassword = password.getText().toString();

//                            if (sendLogin(sendUserName, sendPassword)) {
//                                UiUtils.hideKeyboard(activity, username);
//                                UiUtils.hideKeyboard(activity, password);
//                                dialog.cancel();
//                                Toast.makeText(activity, "Starting activity.login()", Toast.LENGTH_SHORT).show();
//                                activity.login();
//                            }


                            sendLogin(sendUserName, sendPassword);
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            UiUtils.hideKeyboard(activity, username);
                            UiUtils.hideKeyboard(activity, password);
                            dialog.cancel();
                        }
                    });


            UiUtils.showAlertDialog(alertDialogBuilder);

    }

    public void sendLogin(String username, String password){

        Log.d(TAG, "Creating Session");
        NarratorServerCalls.createSession(username, password, null, 0, new Callback() {
            @Override
            public void postExecute(JSONObject json, int status,
                                    String... strings) {

                Log.d(TAG, "STATUS of login: " + status);
                if (status == HttpStatus.SC_BAD_REQUEST || status == HttpStatus.SC_CREATED) {
                    try {
                        if (status == HttpStatus.SC_BAD_REQUEST) {
                            Toast.makeText(activity, json.getString("message"), Toast.LENGTH_SHORT).show();
                        } else if (status == HttpStatus.SC_CREATED) {

                            activity.userId = json.getString("user_id");
                            activity.authToken = json.getString("auth_token");
                            activity.login();

                        }
                    } catch (Exception e) {
                        Toast.makeText(activity, "Bad Connection.", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(activity, "Bad Connection.", Toast.LENGTH_SHORT).show();

            }
        });

    }


}
