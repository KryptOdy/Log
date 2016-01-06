package com.example.odunayo.narrator.Server;

import org.json.JSONObject;

public abstract class Callback {

    public void background(JSONObject json, int status, String... strings) { }

    public abstract void postExecute(JSONObject json, int status, String... strings);
}