package com.example.odunayo.narrator.Framework;

import android.text.Spanned;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class Marker  {

    private String name;
    private String Description;
    private Spanned content;


    private Story story;
    private double latitude;
    private double longitude;
    private LatLng location;

    public Marker(Story story) {
        this.story = story;

        latitude = story.getLatitude();
        longitude = story.getLongitude();
    }


    public double getLatitude(){

        return latitude;

    }

    public double getLongitude(){
        return longitude;
    }

    public LatLng setLatLng(double latitude, double longitude){
        location = new LatLng(latitude, longitude);

        return location;
    }


    public LatLng getLatLng() {
        return location;
    }

//    public String getDescription() {
//
//
//        return Description;
//    }

    //    public String getName() {
//        if (name.length() == 0)
//            return "This is your Current Location";
//        return name;
//    }
//

}