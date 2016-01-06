package com.example.odunayo.narrator.Server;


import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;


import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.CharArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.odunayo.narrator.Framework.Log;

public class ServerUtils {
    private static final String TAG = "ServerUtils";

    // Client must check that entity is not null
    public static String getStringFromEntity(HttpEntity entity) {
        try {
            InputStream instream = entity.getContent();
            StringBuilder sBuild = new StringBuilder();
            try {
                int i;

                while((i=instream.read())!=-1)
                    sBuild.append((char) i);
            } finally {
                instream.close();
            }

            return sBuild.toString();
        } catch (Exception e) { Log.e(TAG, "getStringFromEntity: Error making string from entity"); }
        return null;
    }

    public static JSONObject getJSONFromString(String str) {
        JSONObject json = null;

        try {
            json = new JSONObject(str);
        } catch (Exception e) {}

        return json;
    }

    // from apache entityutils
    public static String entityToString(
            final HttpEntity entity, final String defaultCharset) throws IOException, ParseException {
        if (entity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }
        InputStream instream = entity.getContent();
        if (instream == null) {
            return null;
        }
        if (entity.getContentLength() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
        }
        int i = (int)entity.getContentLength();
        if (i < 0) {
            i = 4096;
        }
        String charset = defaultCharset;

        if (charset == null) {
            charset = HTTP.DEFAULT_CONTENT_CHARSET;
        }

        Reader reader = new InputStreamReader(instream, charset);
        CharArrayBuffer buffer = new CharArrayBuffer(i);
        try {
            char[] tmp = new char[1024];
            int l;
            while((l = reader.read(tmp)) != -1) {
                buffer.append(tmp, 0, l);
            }
        } finally {
            reader.close();
        }
        return buffer.toString();
    }

    // Client must check that entity is not null
    public static JSONObject getJSONFromEntity(HttpEntity entity) {
        String str = getStringFromEntity(entity);
        return getJSONFromString(str);
    }

    // Checks whether the device is online
    public static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public static CookieStore getAuthCookies(String authToken, String userId, String domain) {
        CookieStore cookieStore = new BasicCookieStore();

        Log.d(TAG, "auth: " + authToken);

        BasicClientCookie authCookie = new BasicClientCookie("auth_token", authToken);
        authCookie.setDomain(domain);
        authCookie.setPath("/");
        cookieStore.addCookie(authCookie);

        BasicClientCookie userCookie = new BasicClientCookie("user_id", userId);
        userCookie.setDomain(domain);
        userCookie.setPath("/");
        cookieStore.addCookie(userCookie);

        return cookieStore;
    }


    public static boolean idInJSONArray(int id, JSONArray arr) {
        try {
            for (int i = 0; i < arr.length(); i++) {
                if (arr.getInt(i) == id)
                    return true;
            }
        } catch (Exception e) { Log.e(TAG, "json error"); }

        return false;
    }
}