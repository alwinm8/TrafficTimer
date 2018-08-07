package com.weezy.alwinmathew.traffictimer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleMap mGoogleMap;
    private Location currentLocation;

    private static final String TAG = "MyActivity";
    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 50000;
    final int LOCATION_PERMISSION_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Log.v(TAG, "Started Maps Activity!");
        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment;
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Initialize Fused Location Client
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.v(TAG, "OnMapReady function.");
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        mGoogleMap.setOnMarkerClickListener(this);

        setUpMap();
    }

    //Marker clicker options
    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    //Function to request ACCESS_FINE_LOCATION if not already granted
    private void setUpMap()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        if(mGoogleMap.isMyLocationEnabled())
        {
            //
            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>()
            {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Log.v(TAG, "Fused Location Client has gotten current location!");
                        currentLocation = location;
                        LatLng currentLatLng = new LatLng(location.getLongitude(), location.getLongitude());
                        mGoogleMap.addMarker(new MarkerOptions().position(currentLatLng).title("Marker in Sydney"));
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12.0f));
                    }
                    else
                    {
                        Log.v(TAG, "Fused Location Client reports current location is null!");
                    }
                }
            });

        }
    }
}
