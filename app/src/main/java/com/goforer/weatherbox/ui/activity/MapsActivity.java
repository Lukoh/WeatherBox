package com.goforer.weatherbox.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.goforer.weatherbox.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final float MAP_ZOOM = 18;

    private GoogleMap mMap;

    private double mLatitude;
    private double mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLatitude = getIntent().getDoubleExtra(MainActivity.EXTRA_LATITUDE, -1);
        mLongitude = getIntent().getDoubleExtra(MainActivity.EXTRA_LONGITUDE, -1);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near my location.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Hide the zoom controls as the button panel will cover it.
        mMap.getUiSettings().setZoomControlsEnabled(false);

        // Add a marker in my location and move the camera
        LatLng myLocation = new LatLng(mLatitude, mLongitude);
        mMap.addMarker(new MarkerOptions().position(myLocation)
                .title("Marker in my location").infoWindowAnchor(0.5f, 0.5f));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, MAP_ZOOM));
    }
}

