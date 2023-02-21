package com.bingle.ameba.bingle_bar.fragments;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bingle.ameba.bingle_bar.R;
import com.bingle.ameba.bingle_bar.common_functions.CommonMethods;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MapDetailFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "MapDetailFragment";
    ResturantDetailFragment resturantDetailFragment;
    String mName, mAddress, latitude, longitude, state, zipCode, country, imgPath, contactNo,
            open, close, address2, DisplayOpeningClosing, CompleteAddress, rating, barStatus;
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.btn_rating)
    Button btnRating;
    CommonMethods commonMethods = new CommonMethods();
    private GoogleMap mMap;
    private TextView name, address, open_close, txt_address_full,open_detail;
    private ImageView menu, view, img_res;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_detail, container, false);

        Log.e("in map detail", "in map detail");

        ButterKnife.bind(this, v);
        name = v.findViewById(R.id.res_name);
        address = v.findViewById(R.id.address_id);
        open_close = v.findViewById(R.id.open_close);
        txt_address_full = v.findViewById(R.id.txt_address_full);
        open_detail = v.findViewById(R.id.open_detail);
        img_res = v.findViewById(R.id.img_res);
        menu = v.findViewById(R.id.sett);
        view = v.findViewById(R.id.map_icon_id);
        view.setVisibility(View.GONE);
        menu.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);

        SupportMapFragment supportMapFragment = ((SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map));
        supportMapFragment.getMapAsync(this);

        try {
            //Get pass data with its key value
            mName = getArguments().getString("name");
            mAddress = getArguments().getString("address");
            contactNo = getArguments().getString("contact_no");
            latitude = getArguments().getString("lati");
            longitude = getArguments().getString("longi");
            open = getArguments().getString("open");
            close = getArguments().getString("close");
            Log.e("latitude is=", latitude);
            state = getArguments().getString("state");
            zipCode = getArguments().getString("zipCode");
            country = getArguments().getString("country");
            imgPath = getArguments().getString("img");
            address2 = getArguments().getString("address2");
            DisplayOpeningClosing = getArguments().getString("displayOpeningClosing");
            CompleteAddress = getArguments().getString("completeAddress");
            rating = getArguments().getString("rating");
            barStatus=getArguments().getString("barStatus");

            //Set Restaurant Rating
            if (rating == null || rating.equalsIgnoreCase("NULL")) {
                btnRating.setText("NA");
                btnRating.getBackground().setAlpha(128);
            } else {
                btnRating.setText(rating);
            }

            // Set Image
            if (imgPath != null && !imgPath.isEmpty()) {
                Glide.with(getActivity()).load(imgPath)
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(img_res);
            } else {
                Glide.with(getActivity()).load(R.drawable.com_facebook_profile_picture_blank_portrait)
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(img_res);
            }

            name.setText(mName);

            /****setFont in textView*************/
            commonMethods = CommonMethods.getInstance();
            commonMethods.setFont(name);
            address.setText(mAddress);

            /****setFont in textView*************/
            commonMethods = CommonMethods.getInstance();
            commonMethods.setFontGray(name);

            open_close.setText(DisplayOpeningClosing);
            txt_address_full.setText(CompleteAddress);

            open_detail.setText(barStatus);

        } catch (Exception e) {

        }

        return v;
    }

    @OnClick({R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                getFragmentManager().popBackStack();
                break;
        }
    }


    @Override
    public void onMapReady(GoogleMap gmap) {

        Log.e(TAG, "onMapReady: gggggggggg");

        mMap = gmap;
        showOnMap();

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) getActivity().findViewById(R.id.map), false);

                TextView title = ((TextView) infoWindow.findViewById(R.id.title));

                title.setText(marker.getTitle());
                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                Log.e("snippet", "snippet=" + snippet.toString());

                snippet.setText(marker.getSnippet());
                Location myLocation = mMap.getMyLocation();
                //mMap.setOnInfoWindowClickListener(MapDetailFragment.this);
                return infoWindow;
            }
        });


    }


    @SuppressLint("MissingPermission")
    public void showOnMap() {
        mMap.setMyLocationEnabled(true);
       // String lat = MainActivity.m_activity.get_data_fb("latitude");

        double d = Double.parseDouble(latitude);
      //  String lng = MainActivity.m_activity.get_data_fb("longitude");
        double d1 = Double.parseDouble(longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(d, d1);
        markerOptions.position(latLng);
        markerOptions.title(name.getText().toString());
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bingle_marker));
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

    }

}
