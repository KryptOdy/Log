package com.example.odunayo.narrator.Framework;


import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

public class User {
    private final String TAG = "User";

    private String userId;
    private String userName;
    private ArrayList<Story> storiesList;

    public User (String userId, String userName){
        this.userId = userId;
        this.userName = userName;
        storiesList = new ArrayList<>();

    }

    public User (JSONObject json){
        try {
            Log.d(TAG, json.toString());
            this.userId = json.getString("user_id");
            this.userName = json.getString("username");
            storiesList = new ArrayList<>();
        } catch (Exception e) {
            Log.e(TAG, "json error");

        }
    }

    public String getUserId(){
        return userId;
    }

    public String getUserName(){
        return userName;
    }

    public void addStory(Story story){
        storiesList.add(story);
    }
}
