package com.bingle.ameba.bingle_bar.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bingle.ameba.bingle_bar.R;
import com.bingle.ameba.bingle_bar.activities.MainActivity;
import com.bingle.ameba.bingle_bar.activities.SimpleTabsActivity;
import com.bingle.ameba.bingle_bar.common_functions.BackGroundService;
import com.bingle.ameba.bingle_bar.common_functions.Bean_CommonResponse;
import com.bingle.ameba.bingle_bar.common_functions.CommonMethods;
import com.bingle.ameba.bingle_bar.common_functions.Constants;
import com.bingle.ameba.bingle_bar.common_functions.ServicesResponse;
import com.bingle.ameba.bingle_bar.common_functions.Users;
import com.bingle.ameba.bingle_bar.common_functions.firebase_chat_managers.FirebaseChatManager;
import com.bingle.ameba.bingle_bar.common_functions.server_api_manager.ServerAPIDataListner;
import com.bingle.ameba.bingle_bar.common_functions.server_api_manager.ServerAPIManager;
import com.bingle.ameba.bingle_bar.receivers.ConnectivityReceiver;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pojo.RestaurantDetailsByLatLong;

import static com.bingle.ameba.bingle_bar.common_functions.Constants.chatWindow;

/**
 * Created by Master Gaurav Singla on 28/3/18.
 */

public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, ServicesResponse {
    private static final String TAG = "HomeFragment";
    //private static final int DEFAULT_ZOOM = 15;
    private static final int DEFAULT_ZOOM = 5;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    // Used for selecting the current place.
    private static final int REQUEST_PLACE_PICKER = 1;
    public static HomeFragment m_Home = null;
    // A default location_s (Sydney, Australia) and default zoom to use when location_s permission is
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    public List <RestaurantDetailsByLatLong> beanList;
    public String mLati = "", mLongi = "", mLatitude = "", mLongitude;
    public String restaurantLatitude = "", restaurantLongitude = "", restaurantId = "", loginChoiceID;
    LinearLayout linLayout;
    @BindViews({R.id.txt_loc, R.id.txt_change_loc})
    List <TextView> txtLocation;
    JSONArray dataArry;
    @BindView(R.id.lin_loc)
    LinearLayout linLoctaion;
    ArrayList <Double> latArray = new ArrayList <>();
    @BindView(R.id.lin_main)
    LinearLayout parentLayout;
    LocationRequest mLocationRequest;
    int detectGps = 0;
    Bundle savedInstanceStateM;
    TextView pass;
    // WifiManager mainWifiObj;

    String[] wifis, temp;
    Dialog dialog;
    RestaurantDetailsByLatLong restaurantDetailsByLatLong;


    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List <Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.e("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());

                mLati = String.valueOf(location.getLatitude());
                mLongi = String.valueOf(location.getLongitude());
            }
        }
    };
    @BindView(R.id.txt_title)
    TextView txtMap;
    /* WifiManager wifi;
     WifiScanReceiver wifiReciever;*/
    private int PROXIMITY_RADIUS = 1000;
    //Maps
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    private DrawerLayout drawer_obj;
    private ImageView chat_obj, drawer_icon;
    private SupportMapFragment mapFragment;
    private CommonMethods commonMethods;
    private String mNameAddr = "no", aLat;

    public static void onBackPressed() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("onCreateView", "onCreateView");
        View v = null;
        v = inflater.inflate(R.layout.fragment_home, container, false);
        savedInstanceStateM = savedInstanceState;
        commonMethods = CommonMethods.getInstance();

        m_Home = this;
        beanList = new ArrayList <>();


        //XMl view setup
        setupView(v);
        drawer_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_obj.openDrawer(GravityCompat.START);
                try {
                    CommonMethods.getInstance().hidekeyboard(view);
                } catch (Exception e) {
                }

            }
        });
        chat_obj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!ConnectivityReceiver.isConnected()) {

                    Toast.makeText(commonMethods, "Internet connection not available", Toast.LENGTH_SHORT).show();

                } else {

                    //TODO: update your UI

                    MainActivity.m_activity.swapContentFragment(new List_Resturant_Fragment(), "List_Resturant_Fragment", true);
                }
            }
        });

        ButterKnife.bind(this, v);
        commonMethods = CommonMethods.getInstance();

        //Set Title
        txtMap.setVisibility(View.VISIBLE);
        txtMap.setText("Map View");

        CommonMethods.getInstance().setFont(txtLocation.get(0));
        try {

            Log.e("oncreate=", "loc" + Constants.CURRENT_LOC);

            txtLocation.get(0).setText(Constants.CURRENT_LOC);
        } catch (Exception e) {
        }
        String data = MainActivity.m_activity.get_data_fb("data");

        try {
            if (savedInstanceState != null) {

                Log.e("null state", "null State");
                mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
                mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);

                savedInstanceStateM = savedInstanceState;
            }
        } catch (Exception e) {
        }

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(getActivity(), null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(getActivity(), null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        //To save the state that user have login successfully once
        SharedPreferences.Editor sharedPreferences = getActivity().getSharedPreferences("sharePef", Context.MODE_PRIVATE).edit();
        sharedPreferences.putString("home", "home");
        sharedPreferences.commit();

        return v;

    }


    //broadcast receiver for wifi connectivity

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
       /* try{
            if (!mainWifiObj.isWifiEnabled()) {
                //wifiReciever = new WifiScanReceiver();
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/
        if (mLongi != null && !mLongi.isEmpty()) {
            Log.e("inside", "inside");
            getDataFromAPI();

        }

        if (CommonMethods.getInstance().progress != null) {

            CommonMethods.getInstance().dismissProgressDialog();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //stop location updates when Activity is no longer active
        if (mFusedLocationProviderClient != null) {
            mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: suppper");
        super.onActivityCreated(savedInstanceState);
    }

    private void setupView(View v) {
        //Map Fragment loading
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map_main);
        mapFragment.getMapAsync(this);

        linLayout = (LinearLayout) v.findViewById(R.id.lin_layout_map);
        linLayout.setVisibility(View.VISIBLE);
        Toolbar toolbar_obj = (Toolbar) v.findViewById(R.id.toolbar_id);
        drawer_obj = (DrawerLayout) v.findViewById(R.id.drawerlayout_id);
        chat_obj = (ImageView) v.findViewById(R.id.map_icon_id);
        drawer_icon = (ImageView) v.findViewById(R.id.sett);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) getActivity().findViewById(R.id.map), false);

                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());
                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                Log.e("snippet", "snippet=" + snippet.toString());
                snippet.setText(marker.getSnippet());
                mMap.setOnInfoWindowClickListener(HomeFragment.this);
                return infoWindow;
            }
        });
        if (ConnectivityReceiver.isConnected()) {

            Log.e("connected", "connected");
            if (MainActivity.m_activity.isLocationEnabled()) {

                // Test Condition Added
                if (ServerAPIManager.getInstance().restaurantsListData == null) {

                    Log.e("enabled", "enabled");  //first time


                    getLocationPermission();
                    updateLocationUI();
                    getDeviceLocation();
                } else {

                    showOnMap();
                }

            } else {
                detectGps = 1;
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        } else {
            Toast.makeText(getActivity(), "Internet connection not available", Toast.LENGTH_SHORT).show();
        }

    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location_s of the device, which may be null in rare
         * cases when a location_s is not available.
         */
        try {

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                CommonMethods.getInstance().setProgressDialog(getActivity(), "Getting current location...", false);

                Task <Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener <Location>() {
                    @Override
                    public void onComplete(@NonNull Task <Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();

                            if (mLastKnownLocation != null) {

                                Log.e("Latitude=", "lll" + mLastKnownLocation.getLatitude());
                                Log.e("Longilll", "lll" + mLastKnownLocation.getLongitude());
                                //Get Nearby Resturants
                                mMap.clear();


                                mLati = String.valueOf(mLastKnownLocation.getLatitude());
                                mLongi = String.valueOf(mLastKnownLocation.getLongitude());


                                CommonMethods.getInstance().dismissProgressDialog();
                                CommonMethods.getInstance().setProgressDialog(getActivity(), "Loading Restaurants...", false);


                                //First time API call
                                getDataFromAPI();

                                //Get Address from Lat Long
                                Geocoder geocoder;
                                List <Address> addresses = null;
                                geocoder = new Geocoder(getActivity(), Locale.getDefault());
                                try {
                                    addresses = geocoder.getFromLocation(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                    Log.e("complete addresses", "complete addresses" + addresses);
                                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                    String city = addresses.get(0).getSubLocality();


                                    String state = addresses.get(0).getAdminArea();
                                    String country = addresses.get(0).getCountryName();
                                    String postalCode = addresses.get(0).getPostalCode();


                                    if (address.length() > 0) {


                                        mNameAddr = address;

                                        txtLocation.get(0).setText(mNameAddr);

                                        Constants.CURRENT_LOC = txtLocation.get(0).getText().toString();

                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                // txtLocation.setText(address);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            } else {

                                Toast.makeText(getActivity(), "Location not found", Toast.LENGTH_SHORT).show();
                                CommonMethods.getInstance().dismissProgressDialog();
                            }
                        } else {
                            Log.d("", "Current location is null. Using defaults.");
                            Log.e("", "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);


                            Toast.makeText(getActivity(), "Location not found", Toast.LENGTH_SHORT).show();
                            CommonMethods.getInstance().dismissProgressDialog();
                        }


                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {
        /*
         * Request location_s permission, so that we can get the location_s of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {

                        getDeviceLocation();

                        return true;
                    }
                });

                return;

            } else {

                mMap.setMyLocationEnabled(false);

                mMap.getUiSettings().setMyLocationButtonEnabled(false);

                mLastKnownLocation = null;

                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
            getDeviceLocation();
        }
    }

    @OnClick({R.id.txt_change_loc})
    public void onClk(View view) {
        switch (view.getId()) {
            case R.id.txt_change_loc:

                Constants.CHANGED = "";

                try {
                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    Intent intent = intentBuilder.build(getActivity());
                    // Start the Intent by requesting a result, identified by a request code.
                    startActivityForResult(intent, REQUEST_PLACE_PICKER);
                    // Hide the pick option in the UI to prevent users from starting the picker
                    // multiple times.
                    // showPickAction(false);

                } catch (GooglePlayServicesRepairableException e) {
                    GooglePlayServicesUtil
                            .getErrorDialog(e.getConnectionStatusCode(), getActivity(), 0);
                } catch (GooglePlayServicesNotAvailableException e) {
                    Toast.makeText(getActivity(), "Google Play Services is not available.",
                            Toast.LENGTH_LONG)
                            .show();
                }
                ;

                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // BEGIN_INCLUDE(activity_result)
        if (requestCode == REQUEST_PLACE_PICKER) {
            // This result is from the PlacePicker dialog.

            // Enable the picker option
            //showPickAction(true);

            if (resultCode == Activity.RESULT_OK) {
                /* User has picked a place, extract data.
                   Data is extracted from the returned intent by retrieving a Place object from
                   the PlacePicker.
                 */
                final Place place = PlacePicker.getPlace(data, getActivity());

                /* A Place object contains details about that place, such as its name, address
                and phone number. Extract the name, address, phone number, place ID and place types.
                 */
                final CharSequence name = place.getName();
                final CharSequence address = place.getAddress();
                final CharSequence phone = place.getPhoneNumber();
                final String placeId = place.getId();
                String attribution = PlacePicker.getAttributions(data);

                try {
                    Constants.CURRENT_LOC = (String) name;
                    txtLocation.get(0).setText(Constants.CURRENT_LOC);
                    mNameAddr = (String) name;


                } catch (Exception e) {

                }

                final LatLng lat = place.getLatLng();

                String s = lat.toString();

                String[] separated = s.split(",");
                String lat1 = separated[0];
                String lon1 = separated[1];


                String ri = lon1.toString();
                String[] seperator = ri.split("\\)");
                String lon2 = seperator[0];

                String ri2 = lat1.toString();
                String[] seperator2 = ri2.split("\\(");
                String lat21 = seperator2[0];
                String lat2 = seperator2[1];

                Log.e("lat r=", "rr    " + lat2);
                Log.e("lon r=", "rr   " + lon2);

                mLati = lat2;
                mLongi = lon2;

                aLat = lat2;
                //aLong = lon2;
                Log.e("on API****=", "API**" + aLat.length());


                //Address Update
                getDataFromAPI();

                if (attribution == null) {
                    attribution = "";
                }
                // Show the card.

            } else {
                // User has not selected a place, hide the card.
                //getCardStream().hideCard(CARD_DETAIL);
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void showOnMap() {

        // *****************
        // This bean list setting is kind of hack here, we should've maintain the session of this beanlist while getting the API data and
        // this fragment should maintain its state.
        // NEED TO FIND SOLUTION FOR THIS.
        beanList.clear();

        beanList.add(ServerAPIManager.getInstance().restaurantsListData);

        // *****************

        mMap.clear();

        RestaurantDetailsByLatLong restaurantDetailsByLatLong = ServerAPIManager.getInstance().restaurantsListData;

        LatLng latLng = null;
        Log.e("latLongArray", "===" + latArray.size());
        for (int i = 0; i < restaurantDetailsByLatLong.getData().size(); i++) {

            String lat = restaurantDetailsByLatLong.getData().get(i).getGeoLatitude();
            String lng = restaurantDetailsByLatLong.getData().get(i).getGeoLongitude();

            if (lat != null && lng != null && !lat.isEmpty() && !lng.isEmpty()) {
                MarkerOptions markerOptions = new MarkerOptions();
                latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                markerOptions.position(latLng);
                markerOptions.title(restaurantDetailsByLatLong.getData().get(i).getName() + " : " + restaurantDetailsByLatLong.getData().get(i).getAddressLine2());
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.bing));
                Marker restaurantMarker = mMap.addMarker(markerOptions);
                restaurantMarker.setTag(i);
            }
        }

      /*  String lat = restaurantDetailsByLatLong.getData().get(0).getGeoLatitude();
        String lng = restaurantDetailsByLatLong.getData().get(0).getGeoLongitude();

        if (lat != null && lng != null && !lat.isEmpty() && !lng.isEmpty()) {
            MarkerOptions markerOptions = new MarkerOptions();
            latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            markerOptions.position(latLng);
            markerOptions.title(restaurantDetailsByLatLong.getData().get(0).getName() + " : " + restaurantDetailsByLatLong.getData().get(0).getAddressLine2());
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.bing));
            Marker restaurantMarker = mMap.addMarker(markerOptions);
            restaurantMarker.setTag(0);
        }*/


        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        updateLocationUI();

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        int pos = (int) marker.getTag();

        if (ServerAPIManager.getInstance().restaurantsListData.getData().get(pos).getIsActiveStatus()) {
            ResturantDetailFragment resturantDetailFragment = new ResturantDetailFragment();
            //create bundle instance
            Bundle data = new Bundle();
            data.putSerializable("restaurant_data", ServerAPIManager.getInstance().restaurantsListData.getData().get(pos));
            resturantDetailFragment.setArguments(data);

            MainActivity.m_activity.swapContentFragment(resturantDetailFragment, "ResturantDetailFragment", true);
        }
    }

  /*  public void getDataFromAPI() {

        ServerAPIManager.getInstance().getAllDetailsByLatLong(mLati, mLongi, getContext(), new ServerAPIDataListner() {
            @Override
            public void onCompleted(JSONObject jsonObject) {


                Log.e(TAG, "getDataFromAPI" + mLati + "mLongi=" + mLongi);

                try {
                    Users users = new Users(getActivity());




                    String status = jsonObject.getString("success");

                    if (status != null && status.equalsIgnoreCase("true")) {

                        Gson gson = new Gson();
                        ServerAPIManager.getInstance().restaurantsListData = gson.fromJson(String.valueOf(jsonObject), RestaurantDetailsByLatLong.class);

                        //Traverse data to match current location with Restaurant Location
                        RestaurantDetailsByLatLong restaurantDetailsByLatLong = ServerAPIManager.getInstance().restaurantsListData;


                        LatLng latLng = null;
                        for (int i = 0; i < restaurantDetailsByLatLong.getData().size(); i++) {





                            //for (int i = 0; i < 0; i++) {

                            users.setRestaurantId(restaurantDetailsByLatLong.getData().get(i).getRestaurantId());

                            restaurantLatitude = restaurantDetailsByLatLong.getData().get(i).getGeoLatitude();
                            restaurantLongitude = restaurantDetailsByLatLong.getData().get(i).getGeoLongitude();


                            String reduce_restaurant_lati = new DecimalFormat("##.###").format(Double.parseDouble(restaurantLatitude));
                            String reduce_restaurant_longi = new DecimalFormat("##.###").format(Double.parseDouble(restaurantLongitude));

                            //
                            String current_latii = mLati;
                            String current_longii = mLongi;

                            String reduce_current_lati = new DecimalFormat("##.###").format(Double.parseDouble(current_latii));
                            String reduce_current_longi = new DecimalFormat("##.###").format(Double.parseDouble(current_longii));


                            Log.e(TAG, "Latitude " + reduce_restaurant_lati + " current " + reduce_current_lati);

                            Log.e(TAG, "Lon:fileeeeee " + reduce_restaurant_longi + " current " + reduce_current_longi);

                            restaurantId = restaurantDetailsByLatLong.getData().get(i).getRestaurantId();
                            //Comparing Current Lat Long with Restaurant Lat Long
                            if (reduce_restaurant_lati.equals(reduce_current_lati) && reduce_restaurant_longi.equals(reduce_current_longi)) {
                                Log.e("Restaurant matched", "Restaurant matched");

                                //Toast.makeText(commonMethods, "Restaurant matched ", Toast.LENGTH_SHORT).show();

                                mLatitude = reduce_current_lati;
                                mLongitude = reduce_current_longi;

                                //Do match here the current location IF Matched with the Restaurant then only do entry in the DB
                                enterUserInFirebaseDataBase();
                                //Add update RestaurantId in FCM data base TableName:Users-Live

                                //FirebaseChatManager.getInstance().setUserRestaurantId(restaurantDetailsByLatLong.getData().get(i).getRestaurantId());

                                FirebaseChatManager.getInstance().setUserRestaurantId("1");

                                users.setGpsLocation("true");

                                //Send Push notification to Login user when current location matched with Restaurant location
                                //Users users = new Users(getActivity());
                                Log.e("Current Token", "Current Token" + FirebaseInstanceId.getInstance().getToken());

                                pushNotificationCall(FirebaseInstanceId.getInstance().getToken(), "Hi" + " " + users.getName() + " Welcome to " + restaurantDetailsByLatLong.getData().get(i).getName());

                                // WelcomePopUp();//Critical June 13

                                if (chatWindow.equalsIgnoreCase("0")) {
                                    Intent in = new Intent(getActivity(), SimpleTabsActivity.class);
                                    startActivity(in);
                                }

                                //Call API Here
                                SharedPreferences sharedPreferences_getfb = getActivity().getSharedPreferences("God", Context.MODE_PRIVATE);
                                String socialType = sharedPreferences_getfb.getString("nirankar", "");
                                if (socialType.equalsIgnoreCase("Fb")) {
                                    loginChoiceID = "1";
                                } else {
                                    loginChoiceID = "2";
                                }
                                userLoginApiCall(restaurantDetailsByLatLong.getData().get(i).getRestaurantId(), loginChoiceID, Constants.androidId);


                               *//* Intent in = new Intent(getActivity(), SimpleTabsActivity.class);//@Mann
                                startActivity(in);*//*
                            } else {
                                Log.e("Not Restaurant matched", "Not Restaurant matched");

                            }
                        }

                   *//*     WelcomePopUp();//Critical June 13
                       *//*


                        showOnMap();

                        CommonMethods.getInstance().dismissProgressDialog();

                    } else {

                        CommonMethods.getInstance().showMessage("No Data Found!", parentLayout);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

                Log.e(TAG, "onError: " + error);

                CommonMethods.getInstance().showMessage(error, parentLayout);

                CommonMethods.getInstance().dismissProgressDialog();

            }
        });
    }*/

    //Mann
    public void getDataFromAPI() {

        ServerAPIManager.getInstance().getAllDetailsByLatLong(mLati, mLongi, getContext(), new ServerAPIDataListner() {
            @Override
            public void onCompleted(JSONObject jsonObject) {


                Log.e(TAG, "getDataFromAPI" + mLati + "mLongi=" + mLongi);

                try {
                    Users users = new Users(getActivity());



                    String status = jsonObject.getString("success");

                    if (status != null && status.equalsIgnoreCase("true")) {

                        Gson gson = new Gson();
                        ServerAPIManager.getInstance().restaurantsListData = gson.fromJson(String.valueOf(jsonObject), RestaurantDetailsByLatLong.class);

                        //Traverse data to match current location with Restaurant Location
                         restaurantDetailsByLatLong = ServerAPIManager.getInstance().restaurantsListData;


                        LatLng latLng = null;
                        for (int i = 0; i < restaurantDetailsByLatLong.getData().size(); i++) {


                            calculateDistance(mLati, mLongi, restaurantDetailsByLatLong.getData().get(i).getGeoLatitude(),restaurantDetailsByLatLong.getData().get(i).getGeoLongitude(),i);


                            users.setRestaurantId(restaurantDetailsByLatLong.getData().get(i).getRestaurantId());

                            restaurantLatitude = restaurantDetailsByLatLong.getData().get(i).getGeoLatitude();
                            restaurantLongitude = restaurantDetailsByLatLong.getData().get(i).getGeoLongitude();


                            String reduce_restaurant_lati = new DecimalFormat("##.###").format(Double.parseDouble(restaurantLatitude));
                            String reduce_restaurant_longi = new DecimalFormat("##.###").format(Double.parseDouble(restaurantLongitude));

                            //
                            String current_latii = mLati;
                            String current_longii = mLongi;

                            String reduce_current_lati = new DecimalFormat("##.###").format(Double.parseDouble(current_latii));
                            String reduce_current_longi = new DecimalFormat("##.###").format(Double.parseDouble(current_longii));


                            Log.e(TAG, "Latitude " + reduce_restaurant_lati + " current " + reduce_current_lati);

                            Log.e(TAG, "Lon:fileeeeee " + reduce_restaurant_longi + " current " + reduce_current_longi);

                            restaurantId = restaurantDetailsByLatLong.getData().get(i).getRestaurantId();
                            //Comparing Current Lat Long with Restaurant Lat Long
                          /*  if (reduce_restaurant_lati.equals(reduce_current_lati) && reduce_restaurant_longi.equals(reduce_current_longi)) {
                                Log.e("Restaurant matched", "Restaurant matched");

                                //Toast.makeText(commonMethods, "Restaurant matched ", Toast.LENGTH_SHORT).show();

                                mLatitude = reduce_current_lati;
                                mLongitude = reduce_current_longi;

                                //Do match here the current location IF Matched with the Restaurant then only do entry in the DB
                                enterUserInFirebaseDataBase();
                                //Add update RestaurantId in FCM data base TableName:Users-Live

                                //FirebaseChatManager.getInstance().setUserRestaurantId(restaurantDetailsByLatLong.getData().get(i).getRestaurantId());

                                FirebaseChatManager.getInstance().setUserRestaurantId("1");

                                users.setGpsLocation("true");

                                //Send Push notification to Login user when current location matched with Restaurant location
                                //Users users = new Users(getActivity());
                                Log.e("Current Token", "Current Token" + FirebaseInstanceId.getInstance().getToken());

                                pushNotificationCall(FirebaseInstanceId.getInstance().getToken(), "Hi" + " " + users.getName() + " Welcome to " + restaurantDetailsByLatLong.getData().get(i).getName());

                                // WelcomePopUp();//Critical June 13

                                if (chatWindow.equalsIgnoreCase("0")) {
                                    Intent in = new Intent(getActivity(), SimpleTabsActivity.class);
                                    startActivity(in);
                                }

                                //Call API Here
                                SharedPreferences sharedPreferences_getfb = getActivity().getSharedPreferences("God", Context.MODE_PRIVATE);
                                String socialType = sharedPreferences_getfb.getString("nirankar", "");
                                if (socialType.equalsIgnoreCase("Fb")) {
                                    loginChoiceID = "1";
                                } else {
                                    loginChoiceID = "2";
                                }
                                userLoginApiCall(restaurantDetailsByLatLong.getData().get(i).getRestaurantId(), loginChoiceID, Constants.androidId);


                               *//* Intent in = new Intent(getActivity(), SimpleTabsActivity.class);//@Mann
                                startActivity(in);*//*
                            } else {
                                Log.e("Not Restaurant matched", "Not Restaurant matched");

                            }*/
                        }

                   /*     WelcomePopUp();//Critical June 13
                       */


                        showOnMap();

                        CommonMethods.getInstance().dismissProgressDialog();

                    } else {

                        CommonMethods.getInstance().showMessage("No Data Found!", parentLayout);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

                Log.e(TAG, "onError: " + error);

                CommonMethods.getInstance().showMessage(error, parentLayout);

                CommonMethods.getInstance().dismissProgressDialog();

            }
        });
    }



    private void userLoginApiCall(String RestaurantId, String loginChoiceId, String IMEI) {
        CommonMethods.getInstance().createRetrofitBuilderWithHeader(getActivity());

        try {
            HashMap <String, String> params = new HashMap <>();
            Users users = new Users(getActivity());
            params.put("userID", users.getUserId());
            params.put("RestaurantId", RestaurantId);
            params.put("loginChoiceId", loginChoiceId);
            params.put("deviceNumber_IMEI", IMEI);

            Log.e("params", params.toString());
            CommonMethods.getInstance().callService(getActivity(), this, parentLayout, Constants.USER_LOGIN_SERVICE_ID, params);

        } catch (Exception e) {

            CommonMethods.getInstance().dismissProgressDialog();

            Log.e("in_exception_" + Constants.USER_LOGIN_SERVICE_ID, "" + e.toString());
        }
    }

    @Override
    public void onResponseSuccess(Bean_CommonResponse response) throws JSONException {
        //Fetch Api Response for userLogin

        CommonMethods.getInstance().dismissProgressDialog();

        JSONObject jsonObject = response.getJsonObject();

        Boolean responseStatus = jsonObject.getBoolean("response");

        if (responseStatus) {
            Log.e("True", "True");
            //Start a Background Service

            Intent serviceIntent = new Intent(getActivity(), BackGroundService.class);
            serviceIntent.putExtra("latitude", mLati);
            serviceIntent.putExtra("longitude", mLongi);

            serviceIntent.putExtra("restaurantLatitude", restaurantLatitude);
            serviceIntent.putExtra("restaurantLongitude", restaurantLongitude);
            //new parameters
            serviceIntent.putExtra("restaurantId", restaurantId);
            serviceIntent.putExtra("loginChoiceID", loginChoiceID);
            serviceIntent.putExtra("IMEI", Constants.androidId);

            getActivity().startService(serviceIntent);


        } else {
            Log.e("False", "False");
        }
    }

    private void enterUserInFirebaseDataBase() {

        FirebaseChatManager.getInstance().setUserPresenceStatus("Online");

        String userProfilePic = getProfilePic();
        String fullNameValue = MainActivity.m_activity.get_data_fb("display_name");

        if (userProfilePic.isEmpty()) {
            userProfilePic = "NOT_FOUND";
        }
        if (fullNameValue.isEmpty()) {
            fullNameValue = "NOT_FOUND";
        }

        if (!FirebaseChatManager.getInstance().addOrUpdateNewUser(fullNameValue, "Online", userProfilePic)) {
            // No user is signed in.
        }

    }

    private String getProfilePic() {
        String profilePic = "";
        SharedPreferences sharedPreferences_getData = getActivity().getSharedPreferences("God", Context.MODE_PRIVATE);
        String mType = sharedPreferences_getData.getString("nirankar", "");
        if (mType.equalsIgnoreCase("Fb")) {
            JSONObject profile_pic_data, profile_pic_url;
            try {
                String fb_pic = MainActivity.m_activity.get_data_fb("fb_picture");
                profile_pic_data = new JSONObject(fb_pic);
                profile_pic_url = new JSONObject(profile_pic_data.getString("data"));
                profilePic = profile_pic_url.getString("url");
            } catch (Exception e) {
            }
        } else {
            profilePic = MainActivity.m_activity.get_data_fb("display_pic");
        }

        Log.e("profilePic", "profilePic " + profilePic);
        return profilePic;
    }

    //Send Push Notification when user is inside the Restaurant
    private void pushNotificationCall(final String token, final String message) {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    String welcome = "Welcome";
                    Log.e("pushNotificationCall", "pushNotificationCall" + token);
                    OkHttpClient client = new OkHttpClient();
                    MediaType mediaType = MediaType.parse("application/json");

                    RequestBody body = RequestBody.create(mediaType, "{\n  \"to\" : \"" + token + "\",\n  \"notification\" : {\n    \"body\" : \"" + message + "\",\n    \"title\" : \"" + welcome + "\"\n    }\n}");

                    Request request = new Request.Builder()
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .addHeader("authorization", "key=AAAAx_ZE8Rk:APA91bGJCtFSvs6nL-_larRg1Fuhjsts_dM5nI7i2OXgKjzDgb_2k8faP3XpIVFddPZkz_VrlEpxNJIBqdXfSojQyt99mx7vSKbrJyawY-jLo0hB7-xyxcYTT3tbOPcz6eL-wITnxmB0")
                            .addHeader("content-type", "application/json")
                            .addHeader("cache-control", "no-cache")
                            .build();
                    Log.e("request", "request" + request.toString());
                    try {

                        Response response = client.newCall(request).execute();
                        Log.e("response", "response" + response);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


    private void WelcomePopUp() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Welcome In TNDC");
        alertDialogBuilder.setMessage("Do you want to Chat?");
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent in = new Intent(getActivity(), SimpleTabsActivity.class);
                        startActivity(in);
                    }
                })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    private void calculateDistance(String lat, String longi, String restaurantLatitude, String restaurantLongitude ,int pos) {
        Log.e("calculateDistance", "calculateDistance");
        // Toast.makeText(getApplicationContext(), "calculateDistance!", Toast.LENGTH_SHORT).show();


        Location selected_location = new Location("locationA");
        selected_location.setLatitude(Double.parseDouble(lat));
        selected_location.setLongitude(Double.parseDouble(longi));

        Location near_locations = new Location("locationB");
        near_locations.setLatitude(Double.parseDouble(restaurantLatitude));
        near_locations.setLongitude(Double.parseDouble(restaurantLongitude));

        int distance = (int) selected_location.distanceTo(near_locations);
        Log.e("distanceM", "distanceM" + distance);
        // return distance;

        if(distance<=100){
            // Toast.makeText(getApplicationContext(), "NotMatched!", Toast.LENGTH_SHORT).show();


            //Do match here the current location IF Matched with the Restaurant then only do entry in the DB
            enterUserInFirebaseDataBase();
            //Add update RestaurantId in FCM data base TableName:Users-Live

            //FirebaseChatManager.getInstance().setUserRestaurantId(restaurantDetailsByLatLong.getData().get(i).getRestaurantId());

            FirebaseChatManager.getInstance().setUserRestaurantId("1");
            Users users = new Users(getActivity());
            users.setGpsLocation("true");
            //Send Push notification to Login user when current location matched with Restaurant location
            //Users users = new Users(getActivity());
            Log.e("Current Token", "Current Token" + FirebaseInstanceId.getInstance().getToken());

            pushNotificationCall(FirebaseInstanceId.getInstance().getToken(), "Hi" + " " + users.getName() + " Welcome to " + restaurantDetailsByLatLong.getData().get(pos).getName());

            // WelcomePopUp();//Critical June 13

            if (chatWindow.equalsIgnoreCase("0")) {
                Intent in = new Intent(getActivity(), SimpleTabsActivity.class);
                startActivity(in);
            }

            //Call API Here
            SharedPreferences sharedPreferences_getfb = getActivity().getSharedPreferences("God", Context.MODE_PRIVATE);
            String socialType = sharedPreferences_getfb.getString("nirankar", "");
            if (socialType.equalsIgnoreCase("Fb")) {
                loginChoiceID = "1";
            } else {
                loginChoiceID = "2";
            }
            userLoginApiCall(restaurantDetailsByLatLong.getData().get(pos).getRestaurantId(), loginChoiceID, Constants.androidId);

            return;
        }
        else{
            Log.e("Matched:", "Matched");
        }


    }

}
