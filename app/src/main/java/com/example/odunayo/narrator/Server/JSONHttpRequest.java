package com.example.odunayo.narrator.Server;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.example.odunayo.narrator.Framework.Log;

public class JSONHttpRequest extends AsyncTask<String, String, String>{
    private final String TAG = "JSONHttpRequest";

    // Inputs for the data and json parts of the request, like "email" or "password"
    private JSONObject params;

    // Cookies for protected calls
    private CookieStore cookieStore;

    // Interface that allows callback function on completion
    private Callback cb;

    // One of the types below
    private int type;

    // The status of the response
    private int status;

    // Types of HttpRequest, need to add more as necessary
    public static final int POST = 0;
    public static final int DELETE = 1;
    public static final int GET = 2;
    public static final int PUT = 3;

    // params and cookieStore may be null, but type and cb must be non-null
    public JSONHttpRequest(int type, JSONObject params, CookieStore cookieStore, Callback cb) {
        this.type = type;
        this.params = params;
        this.cookieStore = cookieStore;
        this.cb = cb;
    }

    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpRequestBase request;

        Log.d(TAG, "type: "+type);
        try{
            switch (type) {
                case POST:
                    request = new HttpPost(uri[0]);
                    if (params != null) {
                        StringEntity se = new StringEntity(params.toString(), HTTP.UTF_8);
                        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded"));
                        ((HttpPost) request).setEntity(se);
                    }
                    break;
                case DELETE:
                    request = new HttpDelete(uri[0]);
                    break;
                case GET:
                    request = new HttpGet(uri[0]);
                    request.setHeader("Accept", "*/*");
                    break;
                case PUT:
                    request = new HttpPut(uri[0]);
                    if (params != null) {
                        StringEntity se = new StringEntity(params.toString(), HTTP.UTF_8);
                        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded"));
                        ((HttpPut) request).setEntity(se);
                    }
                    break;
                default:
                    Log.e(TAG, "Wrong request type/not specified, needs to be POST or DELETE or GET or PUT");
                    return null;
            }

            if (cookieStore != null) {
                ((DefaultHttpClient) httpclient).setCookieStore(cookieStore);
            }

            if (isCancelled())
                return null;

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (isCancelled())
                return null;

            Log.d(TAG, response.getStatusLine().getStatusCode()+"");
            Log.d(TAG, response.getStatusLine().getReasonPhrase());

            status = response.getStatusLine().getStatusCode();

            if (entity != null) {
                String result = ServerUtils.entityToString(response.getEntity(), HTTP.UTF_8);

                if(result != null) {
                    JSONObject json = ServerUtils.getJSONFromString(result);
                    cb.background(json, status);
                } else
                    cb.background(null, status);
                return result;
            }

        }catch (Exception e) {
            Log.d(TAG, e.toString());
            if (Log.LOG)
                e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(result != null) {
            JSONObject json = ServerUtils.getJSONFromString(result);
//            if (!KillswitchUtils.checkForKillswitch(status, json))
                cb.postExecute(json, status);
        } else
            cb.postExecute(null, status);
    }
}