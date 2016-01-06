package com.example.odunayo.narrator.Framework;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Story {
    private final String TAG = "Story";

    private String storyId;
    private String userId;
    private String storyContent;
    private double latitude;
    private double longitude;
    private String timeSincePosted;
    private int numUpvotes;
    private ArrayList<Comment> comments;
    private double distance;

    public Story (String storyId, String userId, String storyContent, double latitude, double longitude,
                  String timeSincePosted, int numUpvotes, ArrayList<Comment> comments, double distance){

        this.storyId = storyId;
        this.userId = userId;
        this.storyContent = storyContent;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeSincePosted = timeSincePosted;
        this.numUpvotes = numUpvotes;
        this.comments = comments;
        this.distance = distance;
    }

    public Story(JSONObject json) {
        try {
            Log.d(TAG, json.toString());
            this.storyId = json.getString("story_id");
            this.userId = json.getString("user_id");
            this.storyContent= json.getString("story_content");
            this.latitude = json.getDouble("latitude");
            this.longitude = json.getDouble("longitude");
            this.timeSincePosted = json.getString("time_since_posted");
            this.numUpvotes = json.getInt("num_upvotes");
            this.distance = json.getDouble("distance");
        } catch (Exception e) {
            Log.e(TAG, "json story error");
        }

        try {
            this.comments = new ArrayList<Comment>();
            JSONArray commentsArray = json.getJSONArray("comments");

            for (int i = 0; i < commentsArray.length(); i++){
                JSONObject newCommentJson = commentsArray.getJSONObject(i);
                newCommentJson.getString("comment_id");
                Comment newComment = new Comment(
                        newCommentJson.getString("comment_id"),
                        null,
                        newCommentJson.getString("user_id"),
                        newCommentJson.getString("comment_content"),
                        newCommentJson.getInt("num_upvotes"),
                        false,
                        newCommentJson.getString("time_since_posted"));
                comments.add(newComment);
            }
        } catch (Exception e) {
            Log.e(TAG, "json story comments error");
        }
    }

    public String getStoryId(){
        return storyId;
    }

    public String getUserId(){
        return userId;
    }

    public String getStoryContent(){
        return storyContent;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public LatLng getLatLng(){
        LatLng latlng = new LatLng(latitude, longitude);
        return latlng;
    }

    public String getTimeSincePosted(){
        return timeSincePosted;
    }

    public int getNumUpvotes(){
        return numUpvotes;
    }

    public void setNumUpvotes(int numUpvotes){
        this.numUpvotes = numUpvotes;
    }

    public ArrayList<Comment> getComments(){
        return comments;
    }


}
