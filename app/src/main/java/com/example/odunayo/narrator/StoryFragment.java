package com.example.odunayo.narrator;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.odunayo.narrator.Framework.Story;
import com.example.odunayo.narrator.Server.Callback;
import com.example.odunayo.narrator.Server.NarratorServerCalls;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

public class StoryFragment extends Fragment {

    private static final String TAG = "StoryFragment";

    private MapsActivity activity;

    private double latitude;
    private double longitude;

    private EditText storyEdit;

    private Button postStory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = (MapsActivity) getActivity();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.story_layout, container, false);

        storyEdit = (EditText) rootView.findViewById(R.id.storytext);
        postStory = (Button)rootView.findViewById(R.id.postButton);

        postStory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                postStory();
            }
        });


        return rootView;

    }

    public void postStory(){
        final String story = storyEdit.getText().toString();

        NarratorServerCalls.postStory(story
                , activity.latitude, activity.longitude, activity.authToken, activity.userId, new Callback() {
            @Override
            public void postExecute(JSONObject json, int status,
                                    String... strings) {
                if (status == HttpStatus.SC_BAD_REQUEST || status == HttpStatus.SC_CREATED) {
                    try {
                        if (status == HttpStatus.SC_BAD_REQUEST) {
                            Toast.makeText(activity, json.getString("message"), Toast.LENGTH_SHORT).show();
                        } else if (status == HttpStatus.SC_CREATED) {
                            String storyid = json.getString("story_id");

//                            Story newStory = new Story(storyid, activity.userId, story,
//                                    0, 0, null, 0, null, 0);

//                            activity.user.addStory(newStory);
                            FragmentUtils.closeStoryFragment(activity);
                            Toast.makeText(activity, "Successfully Posted a Story!", Toast.LENGTH_SHORT).show();

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
