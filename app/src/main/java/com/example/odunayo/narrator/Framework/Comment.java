package com.example.odunayo.narrator.Framework;

import android.util.Log;

import org.json.JSONObject;

public class Comment {
    private final String TAG = "Comment";

    private String commentId;
    private String postId;
    private String userId;
    private String commentContent;
    private int upvotes;
    private boolean likedbyUser;
    private String dateTime;

    public Comment (String commentId, String postId, String userId, String commentContent,
                    int upvotes,  boolean likedbyUser, String dateTime) {

        this.commentId = commentId;
        this.postId = postId;
        this.userId = userId;
        this.commentContent = commentContent;
        this.upvotes = upvotes;
        this.likedbyUser = likedbyUser;
        this.dateTime = dateTime;

    }

    public Comment(JSONObject json) {
        try {
            Log.d(TAG, json.toString());
            this.commentId = json.getString("comment_id");
            this.postId = json.getString("post_id");
            this.userId = json.getString("user_id");
            this.commentContent = json.getString("comment_content");
            this.dateTime = json.getString("datetime");
            this.upvotes = json.getInt("num_upvotes");
        } catch (Exception e) {
            Log.e(TAG, "json error");

        }
    }

    public String getCommentId(){
        return commentId;
    }

    public String getPostId(){
        return postId;
    }

    public String getUserId(){
        return userId;
    }

    public String getCommentContent(){
        return commentContent;
    }

    public String getDateTime(){
        return dateTime;
    }

    public void setDateTime(String dateTime){
        this.dateTime = dateTime;
    }

    public int getUpvotes(){
        return upvotes;
    }




}

