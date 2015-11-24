package com.wearefunctional.mobileassassin;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        ConnectionCallbacks, OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    //    private Location mLastLocation;
//    private Location mCurrentLocation;
    // protected Marker mMarker;
    protected Location mCurrentLocation;
    private LocationListener mLocationListener;
    private double currentPlayerLatitude;
    private double currentPlayerLongitude;
    private static Marker targetLoc;
    public static boolean mapMade;
    private LocationRequest mLocationRequest;

    //navi bar
    private NavigationView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    //player reference for updating database
    static Player player;

    //@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient)
        mapFragment.getMapAsync(this);

        mDrawerList = (NavigationView)findViewById(R.id.nav_view);
        addDrawerItems();

//        targetLoc = null;
        currentPlayerLatitude = 34.021442;
        currentPlayerLongitude = -118.288108;
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
//                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    private void addDrawerItems() {
        String[] osArray = { "Android", "iOS", "Windows", "OS X", "Linux" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
//        mDrawerList.setAdapter(mAdapter);
    }

    //private

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

        // Add a marker to CSCI 201 9:30am lecture location
//        LatLng location = new LatLng(34, -118);
//        mMap.addMarker(new MarkerOptions().position(location).title("Current Location"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 16.0f));
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);
        mMap.setMyLocationEnabled(true);
//        targetLoc = mMap.addMarker(new MarkerOptions().position(new LatLng(34.021494,-118.283708))
//                .title("target loc").snippet("snip"));
        System.out.println("creating map");
        mapMade = true;
        System.out.println("Got through map create");
        // mMap.
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            System.out.println("BBB");
            System.out.println("Lat: " + location.getLatitude());
            System.out.println("Long: " + location.getLongitude());
            if(player != null){
                //updates player values
                System.out.println("Attempting to print on server");
                currentPlayerLongitude = location.getLongitude();
                currentPlayerLatitude = location.getLatitude();
                player.setPlayerLongitude(currentPlayerLongitude);
                player.setPlayerLatitude(currentPlayerLatitude);
            } else{
                System.out.println("Player is null");
            }
            if(mMap != null){
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
//            String targerName = player.getTargetName();

        }
    };

    public double getCurrentPlayerLatitude() {
        return currentPlayerLatitude;
    }

    public double getCurrentPlayerLongitude() {
        return currentPlayerLongitude;
    }

    public void setTargetLocation(LatLng loc) {
        if (!this.mapMade) {
            System.out.println("map is null");
        }
        else if (this.mapMade) {
            System.out.println("setTarget lat "+loc.latitude);
            System.out.println("setTarget long" + loc.longitude);
//                targetLoc.remove();
//                targetLoc = mMap.addMarker(new MarkerOptions().position(loc).title("target loc").snippet("snip"));
                targetLoc.setPosition(loc);
        }
    }

    @Override
//    public void onLocationChanged(Location location) {
//        mCurrentLocation = location;
//        System.out.println("AAAAA");
//        System.out.println("Lat: " + location.getLatitude());
//        System.out.println("Long: " + location.getLongitude());
//        currentPlayerLatitude = location.getLatitude();
//        currentPlayerLongitude = location.getLongitude();
//        player.setPlayerLatitude(currentPlayerLatitude);
//        player.setPlayerLongitude(currentPlayerLongitude);
////        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
//        updateUI();
//    }

    public void onConnected(Bundle connectionHint) {
        startLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void updateUI() {
//        mLatitudeTextView.setText(String.valueOf(mCurrentLocation.getLatitude()));
//        mLongitudeTextView.setText(String.valueOf(mCurrentLocation.getLongitude()));
//        mLastUpdateTimeTextView.setText(mLastUpdateTime);
        //mMarker.setPosition(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, mLocationListener);
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void setPlayer(Player p){
        System.out.println("\n\nSetting player to: " + p.getDisplayName()+"\n\n");
        player = p;
    }

}
