package com.example.odunayo.narrator;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.odunayo.narrator.Framework.GPSTracker;
import com.example.odunayo.narrator.Framework.Log;
import com.example.odunayo.narrator.Framework.Story;
import com.example.odunayo.narrator.Framework.User;
import com.example.odunayo.narrator.Server.Callback;
import com.example.odunayo.narrator.Server.NarratorServerCalls;
import com.example.odunayo.narrator.Server.ServerTests;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "MainActivity";

    //Google Maps Fragment and Location
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest locationRequest;
    private SupportMapFragment mapFragment;

    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;

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
    private Button getStoriesButton;

    //Is User LoggedIn
    private boolean loggedIn = false;

    //Stories by User
    public ArrayList<Story> localStories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        postButton = (Button)findViewById(R.id.post);
        getStoriesButton = (Button)findViewById(R.id.find);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.showStoryFragment(MapsActivity.this);
            }
        });

        getStoriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStories();
            }
        });


        mDrawerList = (ListView)findViewById(R.id.navList);
        mapLayout = (FrameLayout)findViewById(R.id.maplayout);
        addDrawerItems();
        createLocationRequest();
        setupGoogleMaps();

        // get previous login info, if it exists
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.session_preferences), Context.MODE_PRIVATE);
        userId = sharedPrefs.getString(getString(R.string.user_id), null);
        authToken = sharedPrefs.getString(getString(R.string.auth_token), null);


        if (userId != null && authToken != null) {
            loggedIn = true;
            login();
           // splash.setVisibility(View.VISIBLE);
        }

        if (!loggedIn){
            FragmentUtils.showLaunchScreenFragment(this);
        }
        else {
            login();
        }

    }

    //Get Stories within certain radius and place the markers on the map
    public void getStories(){


        NarratorServerCalls.getStories(3, latitude, longitude, authToken, userId, new Callback() {
            @Override
            public void postExecute(JSONObject json, int status,
                                    String... strings) {
                if (status == HttpStatus.SC_BAD_REQUEST || status == HttpStatus.SC_OK) {
                    try {
                        if (status == HttpStatus.SC_BAD_REQUEST) {
                        } else if (status == HttpStatus.SC_OK) {

                            ArrayList<Story> storiesList = new ArrayList<Story>();
                            JSONArray stories = json.getJSONArray("stories");

                            for (int i = 0; i < stories.length(); i++) {
                                Story newStory = new Story(stories.getJSONObject(i));
                                storiesList.add(newStory);
                            }

                            if (storiesList.size() == 0) {
                                Toast.makeText(MapsActivity.this, "Sorry there's no Stories around you!", Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(MapsActivity.this, "There are " + storiesList.size() + " Stories around you!", Toast.LENGTH_SHORT).show();
                                postStoriesToMap(storiesList);
                            }

                        }
                    } catch (Exception e) {
                        Toast.makeText(MapsActivity.this, "Bad Connection1", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(MapsActivity.this, "Bad Connection2", Toast.LENGTH_SHORT).show();

            }
        });

    }



    public void postStoriesToMap(ArrayList<Story> stories){

        final Map<Marker, Story> storyHashMap = new HashMap<Marker, Story>();

        Log.d(TAG, "Stories length" + stories.size());
//        Toast.makeText(MapsActivity.this, "Length of Array " + stories.size(), Toast.LENGTH_SHORT).show();
        for  (Story story : stories){

            LatLng location = new LatLng(story.getLatitude(), story.getLongitude());
            Log.d(TAG, "Story lat " + story.getLatitude() + "Story long " + story.getLongitude());

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .title("Story")
                    .snippet("Test Snippet")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .position(location));

            //Map Marker to a specific Story
            storyHashMap.put(marker, story);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {

                Story story = storyHashMap.get(marker);

                FragmentUtils.showViewStoryFragment(MapsActivity.this, story);

                //Toast.makeText(MapsActivity.this, "Story Id " + story.getStoryId(), Toast.LENGTH_SHORT).show();

                return true;
            }

        });

        Toast.makeText(MapsActivity.this, "Loaded Stories!", Toast.LENGTH_SHORT).show();

    }



    public void login(){
        mapLayout.setVisibility(View.VISIBLE);
        if (FragmentUtils.launchScreen != null)
        FragmentUtils.closeLaunchScreenFragment(MapsActivity.this);


        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.session_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putString(getString(R.string.user_id), userId);
        editor.putString(getString(R.string.auth_token), authToken);
        editor.commit();

        loggedIn = true;



    }

    public void logout() {

        NarratorServerCalls.deleteSession(authToken, userId, new Callback() {
            @Override
            public void postExecute(JSONObject json, int status, String... strings) {
            }
        });

        clearVariablesAndScreens();

        restartActivity();

    }

    public void clearVariablesAndScreens() {
        loggedIn = false;

        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.session_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.remove(getString(R.string.user_id));
        editor.remove(getString(R.string.auth_token));

        editor.commit();
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
                if (position == 1) {
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


    }

    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
      //  Toast.makeText(MapsActivity.this, "startLocationUpdates()", Toast.LENGTH_SHORT).show();
//        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
//                mGoogleApiClient, locationRequest, this);

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, locationRequest, this);

        Log.d(TAG, "Location update started ..............: ");

        //starts map fragment
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onConnected(Bundle connectionHint) {

       // Toast.makeText(MapsActivity.this, "onConnected Fired", Toast.LENGTH_SHORT).show();
        startLocationUpdates();
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
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        latitude = mLastLocation.getLatitude();
        longitude = mLastLocation.getLongitude();

//        Location m = mLastLocation;
//        m.setLatitude(45.341837);
//        m.setLongitude(-75.655595);

//        LocationServices.FusedLocationApi.setMockMode(mGoogleApiClient, true);
//        LocationServices.FusedLocationApi.setMockLocation(mGoogleApiClient, m);

        if (mLastLocation != null){
//            Toast.makeText(MapsActivity.this, "Current Location " + mLastLocation.getLatitude()
//                    + " " + mLastLocation.getLongitude() , Toast.LENGTH_SHORT).show();
            LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        }
        else{
            Toast.makeText(MapsActivity.this, "Failed to get location", Toast.LENGTH_SHORT).show();
        }


    }


    protected void onStart() {

        super.onStart();
        mGoogleApiClient.connect();

        // Store our shared preference
        SharedPreferences sp = getSharedPreferences(getString(R.string.session_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(getString(R.string.active), true);
        ed.commit();
    }

    protected void onStop() {

        super.onStop();
        mGoogleApiClient.disconnect();

        Log.d(TAG, "onStop fired ..............");

        // Store our shared preference
        SharedPreferences sp = getSharedPreferences(getString(R.string.session_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(getString(R.string.active), false);
        ed.commit();

    }

    @Override
    public void onBackPressed() {
        if (FragmentUtils.storyFragment != null){
            FragmentUtils.closeStoryFragment(this);
        }
        else if(FragmentUtils.viewStoryFragment != null){
            FragmentUtils.closeViewStoryFragment(this);
        }
        else {
            backOutOfApp();
        }
    }

    private void backOutOfApp() {
        super.onBackPressed();

    }


    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }



    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
       mLastLocation = location;

//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

    }




}
