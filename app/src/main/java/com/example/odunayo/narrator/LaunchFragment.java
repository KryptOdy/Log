package com.example.odunayo.narrator;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class LaunchFragment extends Fragment{

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
//                FriendsyFragmentUtils.showSignupFragment(activity);
            }
        });
        signin = (Button) rootView.findViewById(R.id.sign_in_button);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FriendsyFragmentUtils.showLoginFragment(activity);
            }
        });




        return rootView;

    }
}
