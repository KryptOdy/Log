package com.example.odunayo.narrator.Framework;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

public class Comment implements Parcelable{
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
            this.likedbyUser = json.getBoolean("self_has_upvoted");
        } catch (Exception e) {
            Log.e(TAG, "json error");

        }
    }

    private Comment (Parcel in) {
        this.commentId = in.readString();
        this.postId = in.readString();
        this.userId = in.readString();
        this.commentContent = in.readString();
        this.upvotes = in.readInt();
        this.likedbyUser = in.readByte() == 1;
        this.dateTime = in.readString();
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

    public void setNumUpvotes(int upvotes){
        this.upvotes = upvotes;
    }

    public boolean getLikedByUser() {
        return likedbyUser;
    }

    public void setLikedByUser(boolean isliked){
        likedbyUser = isliked;

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.commentId);
        dest.writeString(this.postId);
        dest.writeString(this.userId);
        dest.writeString(this.commentContent);
        dest.writeInt(this.upvotes);
        dest.writeString(this.dateTime);
        dest.writeByte(this.likedbyUser ? (byte) 1 : (byte) 0);
    }

    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };





}

