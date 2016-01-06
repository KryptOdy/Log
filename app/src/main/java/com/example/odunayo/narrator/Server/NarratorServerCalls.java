package com.example.odunayo.narrator.Server;

import com.example.odunayo.narrator.Framework.Log;

import org.json.JSONObject;

import org.apache.http.client.CookieStore;


public class NarratorServerCalls {

    private final static String TAG = "NarratorServerCalls";

    private static final String PREFIX = "https://blooming-sierra-3886.herokuapp.com/api/api.php/";
    //Does Nothing so far
    private static final String DOMAIN = ".blooming-sierra-3886.herokuapp.com";

    // stores the CookieStore so it doesn't have to be made every time
    private static CookieStore cookieStore;

    // stores the authToken and userId to check for changes
    private static String prevAuthToken;
    private static String prevUserId;

    private static void loadCookieStore(String authToken, String userId) {
        if (cookieStore == null || prevAuthToken == null || prevUserId == null ||
                !prevAuthToken.equals(authToken) || !prevUserId.equals(userId)) {
            cookieStore = ServerUtils.getAuthCookies(authToken, userId, DOMAIN);
            prevAuthToken = authToken;
            prevUserId = userId;
        }
    }

    // Calls a new JSONHttpRequest with a custom callback for login
    public static JSONHttpRequest signup(String username, String password, Callback cb) {
        final String url = PREFIX + "users";

        try {
            JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("password", password);

            JSONHttpRequest request = new JSONHttpRequest(JSONHttpRequest.POST, json, null, cb);
            request.execute(url);
            return request;

        } catch (Exception e) {
            Log.e(TAG, "sessions login call json error");
        }

        return null;

    }

    // Calls a new JSONHttpRequest with a custom callback for login
    public static JSONHttpRequest createSession(String username, String password, String gcmId, int version, Callback cb) {
        final String url = PREFIX + "sessions";
        try {
            JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("password", password);

            json.put("apn_device_token", gcmId);
            json.put("type", "Android"); // so the server knows how to use the gcmId
            json.put("version", version);

            Log.d(TAG, "login data: " + json.toString());

            JSONHttpRequest request = new JSONHttpRequest(JSONHttpRequest.POST, json, null, cb);
            request.execute(url);
            return request;
        } catch (Exception e) {
            Log.e(TAG, "sessions login call json error");
        }

        return null;
    }

    // Calls a new JSONHttpRequest with a custom callback for logout
    // userId is assumed to be a valid url-encoded id
    public static JSONHttpRequest deleteSession(String authToken, String userId, Callback cb) {
        final String url = PREFIX + "sessions";

        try {
            loadCookieStore(authToken, userId);

            JSONHttpRequest request = new JSONHttpRequest(JSONHttpRequest.DELETE, null, cookieStore, cb);
            request.execute(url);
            cookieStore = null;
            return request;
        } catch (Exception e) {
            Log.e(TAG, "sessions logout call cookie error");
        }

        return null;
    }

    //Post Story to specific location
    public static JSONHttpRequest postStory(String content, double latitude, double longitude,
                                            String authToken, String userId, Callback cb) {
        final String url = PREFIX + "story";

        try {

            loadCookieStore(authToken, userId);

            JSONObject json = new JSONObject();
            json.put("story_content",content);
            json.put("latitude", latitude);
            json.put("longitude", longitude);

            JSONHttpRequest request = new JSONHttpRequest(JSONHttpRequest.POST, json, cookieStore, cb);
            request.execute(url);
            cookieStore = null;
            return request;

        } catch (Exception e) {
            Log.e(TAG, "sessions login call json error");
        }

        return null;

    }

    //Get stories from certain radius
    public static JSONHttpRequest getStories(double radius, double latitude, double longitude,
                                                           String authToken, String userId, Callback cb) {

        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX + "stories/radius?latitude=" + latitude + "&longitude=" + longitude
        + "&radius=" + radius);
        String url = sb.toString();

        try {

           loadCookieStore(authToken, userId);
            JSONHttpRequest request = new JSONHttpRequest(JSONHttpRequest.GET, null, cookieStore, cb);
            request.execute(url);
            cookieStore = null;
            return request;

        } catch (Exception e) {
            Log.e(TAG, "sessions login call json error");
        }

        return null;

    }

    //Get stories from a certain user
    public static JSONHttpRequest getStoriesUser(String searchUserId,String authToken, String userId, Callback cb) {

        String url = PREFIX + "stories/user?user_id=" + searchUserId;


        try {
            loadCookieStore(authToken, userId);
            JSONHttpRequest request = new JSONHttpRequest(JSONHttpRequest.GET, null, cookieStore, cb);
            request.execute(url);
            cookieStore = null;
            return request;

        } catch (Exception e) {
            Log.e(TAG, "sessions login call json error");
        }

        return null;

    }

    //Post comment to specific story
    public static JSONHttpRequest postComment(String content, String storyId, String authToken, String userId, Callback cb) {
        final String url = PREFIX + "comment";

        try {

            loadCookieStore(authToken, userId);

            JSONObject json = new JSONObject();
            json.put("comment_content",content);
            json.put("story_id", storyId);

            JSONHttpRequest request = new JSONHttpRequest(JSONHttpRequest.POST, json, cookieStore, cb);
            request.execute(url);
            cookieStore = null;
            return request;

        } catch (Exception e) {
            Log.e(TAG, "sessions login call json error");
        }

        return null;

    }

    //Upvote Story
    public static JSONHttpRequest upvoteStory(String storyId, String authToken, String userId, Callback cb) {
        final String url = PREFIX + "upvote/story";

        try {

            loadCookieStore(authToken, userId);

            JSONObject json = new JSONObject();
            json.put("story_id", storyId);

            JSONHttpRequest request = new JSONHttpRequest(JSONHttpRequest.POST, json, cookieStore, cb);
            request.execute(url);
            cookieStore = null;
            return request;

        } catch (Exception e) {
            Log.e(TAG, "sessions login call json error");
        }

        return null;
    }


    //Upvote Comment
    public static JSONHttpRequest upvoteComment(String commentId, String authToken, String userId, Callback cb) {
        final String url = PREFIX + "upvote/comment";

        try {

            loadCookieStore(authToken, userId);

            JSONObject json = new JSONObject();
            json.put("comment_id", commentId);

            JSONHttpRequest request = new JSONHttpRequest(JSONHttpRequest.POST, json, cookieStore, cb);
            request.execute(url);
            cookieStore = null;
            return request;

        } catch (Exception e) {
            Log.e(TAG, "sessions login call json error");
        }

        return null;

    }







}
