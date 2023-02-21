package com.bingle.ameba.bingle_bar;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;

    private GoogleApiClient googleApiClient;

    private static final int LOCATION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        googleApiClient = new GoogleApiClient.Builder(this)
               // .addApi(LOCATION_SERVICE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();





    }


    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();

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

        checkLocationandAddToMap();
    }

    @Override
    public void onConnected(Bundle bundle) {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }



    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    checkLocationandAddToMap();
                } else
                    Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show();
                break;

        }

    }

    private void checkLocationandAddToMap() {
//Checking if the user has granted the permission
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//Requesting the Location permission
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }

//Fetching the last known location using the Fus
       //Location location = Location.FusedLocationApi.getLastLocation(googleApiClient);




//MarkerOptions are used to create a new Marker.You can specify location, title etc with MarkerOptions
       // MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(location_s.getLatitude(), location_s.getLongitude())).title("You are Here");

//Adding the created the marker on the map
      //  mMap.addMarker(markerOptions);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
