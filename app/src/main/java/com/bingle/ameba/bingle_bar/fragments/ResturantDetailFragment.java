package com.bingle.ameba.bingle_bar.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bingle.ameba.bingle_bar.R;
import com.bingle.ameba.bingle_bar.activities.MainActivity;
import com.bingle.ameba.bingle_bar.common_functions.Bean_CommonResponse;
import com.bingle.ameba.bingle_bar.common_functions.CommonMethods;
import com.bingle.ameba.bingle_bar.common_functions.Constants;
import com.bingle.ameba.bingle_bar.common_functions.ServicesResponse;
import com.bingle.ameba.bingle_bar.common_functions.Users;
import com.bingle.ameba.bingle_bar.common_functions.firebase_chat_managers.FirebaseChatManager;
import com.bingle.ameba.bingle_bar.receivers.ConnectivityReceiver;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.GoogleMap;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.UsersList;
import pojo.RestaurantPhotos;
import pojo.RestaurantPhotosAPI;
import pojo.ResturantDetailFields;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Master Gaurav Singla on 9/4/18.
 */

public class ResturantDetailFragment extends Fragment implements ServicesResponse {
    private static final String TAG = "ResturantDetailFragment";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    @BindViews({R.id.img_phone_call, R.id.image_back})
    List <ImageView> imageViews;
    List <ResturantDetailFields> beanList1;
    TextView t1, t11, t2, t22, t3, t33, t4, t44, t5, t55;
    RelativeLayout rating1, rating2, rating3, rating4, rating5;
    String mRatingValue;
    @BindViews({R.id.txt_name, R.id.txt_address_sec, R.id.txt_contact_no, R.id.txt_address_full, R.id.open_close, R.id.open_detail, R.id.txt_restaurant_status})
    List <TextView> txtViews;
    @BindView(R.id.rec)
    RecyclerView recyclerView;
    @BindView(R.id.lin_main)
    LinearLayout parentLayout;
    @BindView(R.id.imageView2)
    ImageView imgView;
    String open, close;

    @BindView(R.id.location_map)
    ImageView locationMap;


  /*  @BindViews({R.id.photo, R.id.txt_map, R.id.reviews})
    List <TextView> topTextView;*/

    @BindViews({R.id.rl_one, R.id.rl_two, R.id.rl_three, R.id.rl_four, R.id.rl_five})
    List <RelativeLayout> relativeLayouts;

   /* @BindViews({R.id.rl_photo, R.id.rl_map, R.id.rl_reviews})
    List <RelativeLayout> relativeLayoutsSwitchViews;*/

    String mName, mAddress, latitude, longitude, state, zipCode, country, displayOpeningClosing, CompleteAddress, coverUrl, barStatus;
    MapDetailFragment mapDetailFragment;
   Photos photos;
    String bar_colon, address2, colon = ":";
    ResturantDetailFields resturantDetailFields;
    @BindViews({R.id.btn_chat, R.id.button_rating})
    List <Button> buttonList;
    List <RestaurantPhotos> beanListPhotos;
    //  ImageView location_obj;
    private boolean mLocationPermissionGranted;
    private CommonMethods commonMethods;
    private String contactNo, restaurantRating;
    private GoogleMap mMap;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ressss, container, false);
        Log.e("ResturantDetailFragment", "ResturantDetailFragment");
        //RecyclerView data
        recyclerView = v.findViewById(R.id.rec);
        commonMethods = CommonMethods.getInstance();
        beanList1 = new ArrayList <>();
        beanListPhotos = new ArrayList <>();
        // Initialize Layout Views
        setupViews(v);

       /* SupportMapFragment supportMapFragment = ((SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map));
        supportMapFragment.getMapAsync(this);*/
        recyclerView.setHasFixedSize(true);
        ButterKnife.bind(this, v);

        try {
            initViews();

            MainActivity.m_activity.set_data_fb("latitude", latitude);
            MainActivity.m_activity.set_data_fb("longitude", longitude);

        } catch (Exception e) {

        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void setupViews(View v) {
        t1 = v.findViewById(R.id.one_txt);
        t11 = v.findViewById(R.id.star1);
        t2 = v.findViewById(R.id.two_txt);
        t22 = v.findViewById(R.id.star2);
        t3 = v.findViewById(R.id.three_txt);
        t33 = v.findViewById(R.id.star3);
        t4 = v.findViewById(R.id.four_txt);
        t44 = v.findViewById(R.id.star4);
        t5 = v.findViewById(R.id.five_txt);
        t55 = v.findViewById(R.id.star5);
        rating1 = v.findViewById(R.id.rl_one);
        rating2 = v.findViewById(R.id.rl_two);
        rating3 = v.findViewById(R.id.rl_three);
        rating4 = v.findViewById(R.id.rl_four);
        rating5 = v.findViewById(R.id.rl_five);
        //location_obj = v.findViewById(R.id.location_id);
    }

    private void initViews() {

        resturantDetailFields = (ResturantDetailFields) getArguments().getSerializable("restaurant_data");
        mName = resturantDetailFields.getName();
        mAddress = resturantDetailFields.getAddressLine1();  //cross check feild
        address2 = resturantDetailFields.getAddressLine2();
        contactNo = resturantDetailFields.getContactNumber();
        latitude = resturantDetailFields.getGeoLatitude();
        longitude = resturantDetailFields.getGeoLongitude();
        open = resturantDetailFields.getTimingsOpen();
        close = resturantDetailFields.getTimingClose();
        state = resturantDetailFields.getState();
        zipCode = resturantDetailFields.getZipCode();
        country = resturantDetailFields.getCountry();
        displayOpeningClosing = resturantDetailFields.getDisplayOpeningClosingTime();
        CompleteAddress = resturantDetailFields.getCompleteAddress();
        restaurantRating = resturantDetailFields.getRating();
        coverUrl = resturantDetailFields.getCoverUrl();
        barStatus = resturantDetailFields.getBarStatus();

        //Set Restaurant availability status

      /*  if (resturantDetailFields.getAvailableStatus() != null && !resturantDetailFields.getAvailableStatus().isEmpty()) {
            txtViews.get(6).setVisibility(View.VISIBLE);
            txtViews.get(6).setText("Availability Status:" + resturantDetailFields.getAvailableStatus());
        }
*/
        bar_colon = barStatus + colon;

        Log.e(TAG, "initViews:CompleteAddress " + CompleteAddress);

        if (!ConnectivityReceiver.isConnected()) {

            Toast.makeText(commonMethods, "Internet connection not available", Toast.LENGTH_SHORT).show();

        } else {
            getRestaurantImagesUsingRestaurantID(resturantDetailFields.getRestaurantId());

        }

        setData(mName, mAddress, contactNo, state, zipCode, country, address2, resturantDetailFields.getLogoUrl(), restaurantRating);
    }


    private void getRestaurantImagesUsingRestaurantID(String RestaurantId) {

        CommonMethods.getInstance().createRetrofitBuilderWithHeader(getActivity());

        try {
            HashMap <String, String> params = new HashMap <>();

            params.put("restaurantId", RestaurantId);

            CommonMethods.getInstance().callService(getActivity(), this, parentLayout, Constants.getRestaurantImagesUsingRestaurantID, params);

        } catch (Exception e) {

            CommonMethods.getInstance().dismissProgressDialog();

            Log.e("in_exception_" + Constants.getRestaurantImagesUsingRestaurantID, "" + e.toString());
        }

    }

    private void getCallPermission() {
        if (ContextCompat.checkSelfPermission(this.getActivity(),
                android.Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            CallActivated();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.CALL_PHONE},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void CallActivated() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + contactNo));
        startActivity(callIntent);
    }


    @OnClick({R.id.img_phone_call, R.id.image_back, R.id.rl_one, R.id.rl_two, R.id.rl_three, R.id.rl_four, R.id.rl_five,
             R.id.btn_chat, R.id.location_map})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_phone_call:
                getCallPermission();
                break;
            case R.id.image_back:
                if (!ConnectivityReceiver.isConnected()) {
                    Toast.makeText(commonMethods, "Internet connection not available", Toast.LENGTH_SHORT).show();
                } else {
                    getFragmentManager().popBackStack();
                }
                break;
            case R.id.rl_one:
                rating1.setBackgroundResource(R.drawable.rating_selected_background);
                t11.setBackgroundResource(R.drawable.star_white);
                t1.setTextColor(Color.parseColor("#ffffff"));

                rating2.setBackgroundResource(R.drawable.rating_background);
                t2.setTextColor(Color.parseColor("#AAAAAA"));
                t22.setBackgroundResource(R.drawable.rate_star_button);

                rating3.setBackgroundResource(R.drawable.rating_background);
                t3.setTextColor(Color.parseColor("#AAAAAA"));
                t33.setBackgroundResource(R.drawable.rate_star_button);

                rating4.setBackgroundResource(R.drawable.rating_background);
                t4.setTextColor(Color.parseColor("#AAAAAA"));
                t44.setBackgroundResource(R.drawable.rate_star_button);

                rating5.setBackgroundResource(R.drawable.rating_background);
                t5.setTextColor(Color.parseColor("#AAAAAA"));
                t55.setBackgroundResource(R.drawable.rate_star_button);
                break;
            case R.id.rl_two:
                rating1.setBackgroundResource(R.drawable.rating_selected_background);
                t11.setBackgroundResource(R.drawable.star_white);
                t1.setTextColor(Color.parseColor("#ffffff"));
                rating2.setBackgroundResource(R.drawable.rating_selected_background);
                t2.setTextColor(Color.parseColor("#ffffff"));
                t22.setBackgroundResource(R.drawable.star_white);

                rating3.setBackgroundResource(R.drawable.rating_background);
                t3.setTextColor(Color.parseColor("#AAAAAA"));
                t33.setBackgroundResource(R.drawable.rate_star_button);

                rating4.setBackgroundResource(R.drawable.rating_background);
                t4.setTextColor(Color.parseColor("#AAAAAA"));
                t44.setBackgroundResource(R.drawable.rate_star_button);

                rating5.setBackgroundResource(R.drawable.rating_background);
                t5.setTextColor(Color.parseColor("#AAAAAA"));
                t55.setBackgroundResource(R.drawable.rate_star_button);
                break;
            case R.id.rl_three:
                rating1.setBackgroundResource(R.drawable.rating_selected_background);
                t11.setBackgroundResource(R.drawable.star_white);
                t1.setTextColor(Color.parseColor("#ffffff"));
                rating2.setBackgroundResource(R.drawable.rating_selected_background);
                t2.setTextColor(Color.parseColor("#ffffff"));
                t22.setBackgroundResource(R.drawable.star_white);
                rating3.setBackgroundResource(R.drawable.rating_selected_background);
                t3.setTextColor(Color.parseColor("#ffffff"));
                t33.setBackgroundResource(R.drawable.star_white);

                rating4.setBackgroundResource(R.drawable.rating_background);
                t4.setTextColor(Color.parseColor("#AAAAAA"));
                t44.setBackgroundResource(R.drawable.rate_star_button);

                rating5.setBackgroundResource(R.drawable.rating_background);
                t5.setTextColor(Color.parseColor("#AAAAAA"));
                t55.setBackgroundResource(R.drawable.rate_star_button);
                break;
            case R.id.rl_four:
                rating1.setBackgroundResource(R.drawable.rating_selected_background);
                t11.setBackgroundResource(R.drawable.star_white);
                t1.setTextColor(Color.parseColor("#ffffff"));
                rating2.setBackgroundResource(R.drawable.rating_selected_background);
                t2.setTextColor(Color.parseColor("#ffffff"));
                t22.setBackgroundResource(R.drawable.star_white);
                rating3.setBackgroundResource(R.drawable.rating_selected_background);
                t3.setTextColor(Color.parseColor("#ffffff"));
                t33.setBackgroundResource(R.drawable.star_white);
                rating4.setBackgroundResource(R.drawable.rating_selected_background);
                t4.setTextColor(Color.parseColor("#ffffff"));
                t44.setBackgroundResource(R.drawable.star_white);

                rating5.setBackgroundResource(R.drawable.rating_background);
                t5.setTextColor(Color.parseColor("#AAAAAA"));
                t55.setBackgroundResource(R.drawable.rate_star_button);
                break;
            case R.id.rl_five:
                rating1.setBackgroundResource(R.drawable.rating_selected_background);
                t11.setBackgroundResource(R.drawable.star_white);
                t1.setTextColor(Color.parseColor("#ffffff"));
                rating2.setBackgroundResource(R.drawable.rating_selected_background);
                t2.setTextColor(Color.parseColor("#ffffff"));
                t22.setBackgroundResource(R.drawable.star_white);
                rating3.setBackgroundResource(R.drawable.rating_selected_background);
                t3.setTextColor(Color.parseColor("#ffffff"));
                t33.setBackgroundResource(R.drawable.star_white);
                rating4.setBackgroundResource(R.drawable.rating_selected_background);
                t4.setTextColor(Color.parseColor("#ffffff"));
                t44.setBackgroundResource(R.drawable.star_white);
                rating5.setBackgroundResource(R.drawable.rating_selected_background);
                t5.setTextColor(Color.parseColor("#ffffff"));
                t55.setBackgroundResource(R.drawable.star_white);
                break;
            case R.id.btn_chat:
                //Add update RestaurantId in FCM data base TableName:Users-Live
                FirebaseChatManager.getInstance().setUserRestaurantId(resturantDetailFields.getRestaurantId());
                //Open Chat Users
                openUsersView(resturantDetailFields.getRestaurantId());
                break;
        }

    }

    private void openUsersView(String restaurantIdValue) {
        UsersList fragment = new UsersList();

        Bundle bundle = new Bundle();
        bundle.putString("from", "Restaurant");
        bundle.putString("RestaurantId", restaurantIdValue);
        bundle.putString("RestaurantName", mName);

        fragment.setArguments(bundle);

        MainActivity.m_activity.swapContentFragment(fragment, "UsersList", true);
    }

    private void setData(String mName, String mAddress, String contactNo, String state, String zipCode,
                         String country, String address2, String imgPath, String rating) {
        txtViews.get(0).setText(mName);
        txtViews.get(1).setText(mAddress);
        txtViews.get(2).setText(contactNo);
        /*txtViews.get(3).setText(address2);//cross check
        txtViews.get(4).setText(open + " to " + close);*/
        txtViews.get(3).setText(CompleteAddress);//cross check
        txtViews.get(4).setText(displayOpeningClosing);
        txtViews.get(5).setText(bar_colon);

        setRatingDisplay(rating);


        if (coverUrl != null && !coverUrl.isEmpty()) {

            Glide.with(getActivity())
                    .load(coverUrl)
                    .override(600, 200) // resizes the image to these dimensions (in pixel)
                    .centerCrop() // this cropping technique scales the image so that it fills the requested bounds and then crops the extra.
                    .into(imgView);
        }
    }

    public void setRatingDisplay(String ratingValue) {
        restaurantRating = ratingValue;
        //Restaurant Rating
        if (ratingValue == null || ratingValue.equalsIgnoreCase("NULL")) {
            buttonList.get(1).setText("NA");
            buttonList.get(1).getBackground().setAlpha(128);
        } else {
            buttonList.get(1).setText(ratingValue);
        }
    }

    @Override
    public void onResponseSuccess(Bean_CommonResponse response) throws JSONException {
        Log.e("getData response", response.toString());

        switch (response.getServiceId()) {
            case Constants.RATING_SERVICE_ID:
                onSuccessRating(response);
                break;

            case Constants.getRestaurantImagesUsingRestaurantID:
                onSuccessPhotos(response);
                break;
        }
    }

    private void onSuccessRating(Bean_CommonResponse response) {
        try {
            JSONObject jsonObject = response.getJsonObject();
            Log.e("getDataRating=", jsonObject.toString());
            String status = jsonObject.getString("success");
            Log.e("getDataRating Trueee", status);
            if (status.equalsIgnoreCase("true")) {
                Log.e("getDataRating Trueee", status);

                if (jsonObject.getJSONObject("data").getString("AverageRating") != null) {

                    resturantDetailFields.setRating(jsonObject.getJSONObject("data").getString("AverageRating"));

                    setRatingDisplay(jsonObject.getJSONObject("data").getString("AverageRating"));
                }

            } else {
                commonMethods.showMessage("No Data", parentLayout);
            }
            commonMethods.dismissProgressDialog();
        } catch (Exception e) {
            Log.e("in_exception_parse", e.toString());
            commonMethods.dismissProgressDialog();
        }
    }

    //Get photos

    private void onSuccessPhotos(Bean_CommonResponse response) {

        CommonMethods.getInstance().dismissProgressDialog();

        JSONObject jsonObject = response.getJsonObject();


        Boolean status = null;
        try {
            status = jsonObject.getBoolean("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (status) {

            Gson gson = new Gson();
            RestaurantPhotosAPI restaurantPhotosAPI = gson.fromJson(String.valueOf(jsonObject), RestaurantPhotosAPI.class);

            for (RestaurantPhotos restaurantPhotos :
                    restaurantPhotosAPI.getData()) {

                beanListPhotos.add(restaurantPhotos);
            }

            setAdapter();

        } else {

        }
    }


    private void setAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        MyMainAdapter myMainAdapter = new MyMainAdapter(getActivity(), beanListPhotos);
        recyclerView.setAdapter(myMainAdapter);

    }

    @OnClick({R.id.rl_one, R.id.rl_two, R.id.rl_three, R.id.rl_four, R.id.rl_five,
            R.id.location_map})
    public void onClk(View view) {
        switch (view.getId()) {
            /*case R.id.rl_map:*/
            case R.id.location_map:
                MapDetail();
                MainActivity.m_activity.swapContentFragment(mapDetailFragment, "MapDetailFragment", true);
                break;
           /* case R.id.rl_reviews:
                Intent in = new Intent(getActivity(), ReviewsActivity.class);
                in.putExtra("name", mName);
                in.putExtra("mAddress", mAddress);
                in.putExtra("RestaurantId", resturantDetailFields.getRestaurantId());
                getActivity().startActivity(in);
                break;*/
           /* case R.id.rl_photo:
                PhotosDetail();
                MainActivity.m_activity.swapContentFragment(photos, "Photos", true);
                break;*/
            case R.id.rl_one:
                mRatingValue = "1";
                ratingPopUp(mRatingValue);
                break;
            case R.id.rl_two:
                mRatingValue = "2";
                ratingPopUp(mRatingValue);
                break;
            case R.id.rl_three:
                mRatingValue = "3";
                ratingPopUp(mRatingValue);
                break;
            case R.id.rl_four:
                mRatingValue = "4";
                ratingPopUp(mRatingValue);
                break;
            case R.id.rl_five:
                mRatingValue = "5";
                ratingPopUp(mRatingValue);
                break;
        }
    }

    private void PhotosDetail() {
        photos = new Photos();
        //create bundle instance
        Bundle data = new Bundle();
        data.putString("name", mName);
        data.putString("address", mAddress);
        data.putString("address2", address2);
        data.putSerializable("restaurant_data", resturantDetailFields);

        photos.setArguments(data);
    }

    private void MapDetail() {
        mapDetailFragment = new MapDetailFragment();
        //create bundle instance
        Bundle data = new Bundle();
        data.putString("name", mName);
        data.putString("address", mAddress);
        data.putString("contact_no", contactNo);
        data.putString("lati", latitude);
        data.putString("longi", longitude);
        data.putString("open", open);
        data.putString("close", close);
        data.putString("state", state);
        data.putString("zipCode", zipCode);
        data.putString("country", country);
        data.putString("img", resturantDetailFields.getLogoUrl());
        data.putString("address2", address2);
        data.putString("displayOpeningClosing", displayOpeningClosing);
        data.putString("completeAddress", CompleteAddress);
        data.putString("rating", restaurantRating);
        data.putString("barStatus", bar_colon);

        mapDetailFragment.setArguments(data);
    }

    private void ratingPopUp(String ratingVal) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rating_pop);
        final TextView btnRating = (TextView) dialog.findViewById(R.id.one_txt);

        btnRating.setText(ratingVal);
        dialog.show();

        final EditText edtComment = (EditText) dialog.findViewById(R.id.edt_comment);
        Button submit = (Button) dialog.findViewById(R.id.btn_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if (ConnectivityReceiver.isConnected()) {
                    Users users = new Users(getActivity());
                    ratingServices(users.getUserId(), resturantDetailFields.getRestaurantId()
                            , btnRating.getText().toString(), edtComment.getText().toString());
                } else {
                    Toast.makeText(getActivity(), "Internet connection not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ratingServices(String id, String RestaurantId, String rating, String comments) {
        commonMethods.createRetrofitBuilderWithHeader(getActivity());
        try {

            if (id == null || id.isEmpty()) {
                // Toast.makeText(commonMethods, "Add Ratings under development if logged in with Facebook", Toast.LENGTH_SHORT).show();
                return;
            }
            HashMap <String, String> params = new HashMap <>();
            params.put("userID", id);
            params.put("RestaurantId", RestaurantId);
            params.put("Rating", rating);
            params.put("Comments", comments);
            Log.e("has val-", "" + params);
            commonMethods.callService(getActivity(), this, parentLayout, Constants.RATING_SERVICE_ID, params);
        } catch (Exception e) {
            commonMethods.dismissProgressDialog();
            Log.e("in_exception_" + Constants.RATING_SERVICE_ID, "" + e.toString());
        }
    }


    /*@Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        showOnMap();
    }

    @SuppressLint("MissingPermission")
    private void showOnMap() {
        mMap.setMyLocationEnabled(false);
        // String lat = MainActivity.m_activity.get_data_fb("latitude");

        double d = Double.parseDouble(latitude);
        //  String lng = MainActivity.m_activity.get_data_fb("longitude");
        double d1 = Double.parseDouble(longitude);

        Log.e(TAG, "showOnMap: " + d + " long " + d1);
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(d, d1);
        markerOptions.position(latLng);
        //markerOptions.title(name.getText().toString());
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bingle_marker));
        mMap.addMarker(markerOptions);

        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(8));
    }*/



    //Adapter


    public class MyMainAdapter extends RecyclerView.Adapter <MyMainAdapter.MyHolderMM> {
        private static final String TAG = "MyMainAdapter";
        Context applicationContext;
   /* List <ResturantDetailFields> img;*/

        List <RestaurantPhotos> img;

        private CommonMethods commonMethods;

        public MyMainAdapter(Context applicationContext, List <RestaurantPhotos> img) {
            this.applicationContext = applicationContext;
            this.img = img;
            commonMethods = CommonMethods.getInstance();
        }

        @Override
        public MyMainAdapter.MyHolderMM onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_restuant_detail, parent, false);
            return new MyMainAdapter.MyHolderMM(view);
        }

        @Override
        public void onBindViewHolder(MyMainAdapter.MyHolderMM holder, int position) {
            Log.e("MyMainAdapter", "MyMainAdapter:");

            try {

                if(img.get(position).getImageUrl()!=null){
                    Glide.with(applicationContext).load(img.get(position).getImageUrl())
                            .thumbnail(0.5f)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.imgRes);}
                else{
                    Glide.with(applicationContext).load(R.drawable.default_listing)
                            .thumbnail(0.5f)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.imgRes);
                }


                holder. imgRes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PhotosDetail();
                        MainActivity.m_activity.swapContentFragment(photos, "Photos", true);
                    }
                });


            } catch (Exception e) {

                e.printStackTrace();
            }


        }

        @Override
        public int getItemCount() {
            //return (img != null) ? img.get(0).getLogoUrl() : 0;
            return img.size();
        }

        public class MyHolderMM extends RecyclerView.ViewHolder {
            ImageView imgRes;

            public MyHolderMM(View itemView) {
                super(itemView);
                imgRes = itemView.findViewById(R.id.img1);
            }
        }
    }




    //till
}
