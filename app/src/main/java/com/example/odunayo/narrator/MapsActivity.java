package com.example.odunayo.narrator;

import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.odunayo.narrator.Framework.Log;
import com.example.odunayo.narrator.Framework.Story;
import com.example.odunayo.narrator.Framework.User;
import com.example.odunayo.narrator.Server.Callback;
import com.example.odunayo.narrator.Server.NarratorServerCalls;
import com.example.odunayo.narrator.Server.ServerTests;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    //Google Maps Fragment and Location
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest locationRequest;
    private SupportMapFragment mapFragment;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private FrameLayout mapLayout;

    // splash screen for loading
    public View splash;

    // Session ids
    public String authToken;
    public String userId;

    public double latitude;
    public double longitude;

    //Current user
    public User user;

    private Button postButton;

    //Is User LoggedIn
    private boolean loggedIn = false;

    //Stories by User
    public ArrayList<Story> localStories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        postButton = (Button)findViewById(R.id.post);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.showStoryFragment(MapsActivity.this);
            }
        });
        mDrawerList = (ListView)findViewById(R.id.navList);
        mapLayout = (FrameLayout)findViewById(R.id.maplayout);
        addDrawerItems();


        setupGoogleMaps();
        FragmentUtils.showLaunchScreenFragment(this);


    }

    public void login(){
        mapLayout.setVisibility(View.VISIBLE);
        FragmentUtils.closeLaunchScreenFragment(MapsActivity.this);


    }

    public void logout(){

        NarratorServerCalls.deleteSession(authToken, userId, new Callback() {
            @Override
            public void postExecute(JSONObject json, int status, String... strings) {
            }
        });

        restartActivity();

    }

    private void restartActivity() {
        try {
            this.finish();
        } catch (Exception e) {
        }

        // avoids having to clear every variable
        Intent freshActivity = new Intent(this, MapsActivity.class);
        startActivity(freshActivity);
    }

    private void addDrawerItems() {
        String[] osArray = {"My Stories", "Log Out"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1){
                    logout();
                }
            }
        });

    }

    public void setupGoogleMaps(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mDrawerList = (ListView)findViewById(R.id.navList);

        //Setup map if we already have the location
        if(mLastLocation != null){
            mapFragment.getMapAsync(this);
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mapFragment.getMapAsync(this);
    }


    @Override
    public void onConnected(Bundle connectionHint) {

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000);

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                locationRequest, this);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection has been interrupted.
        // Disable any UI components that depend on Google APIs
        // until onConnected() is called.
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // This callback is important for handling errors that
        // may occur while attempting to connect with Google.
        //
        // More about this in the 'Handle Connection Failures' section.

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        //Move camera to current location
       if (mLastLocation != null){
            LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        }


    }

    public void serverTest(){
     //  ServerTests.setActivty(this);
     //   ServerTests.testLogin();
     //   ServerTests.deleteSession();


    }



    protected void onStart() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
