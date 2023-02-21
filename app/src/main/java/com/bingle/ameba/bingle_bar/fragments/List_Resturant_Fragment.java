package com.bingle.ameba.bingle_bar.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bingle.ameba.bingle_bar.R;
import com.bingle.ameba.bingle_bar.common_functions.CommonMethods;
import com.bingle.ameba.bingle_bar.common_functions.Constants;
import com.bingle.ameba.bingle_bar.interfaces.ApiInterface;
import com.bingle.ameba.bingle_bar.java_classes.ApiClient;
import com.bingle.ameba.bingle_bar.receivers.ConnectivityReceiver;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import adapters.ResturantAdapter;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pojo.RestaurantDetailsByLatLong;
import pojo.ResturantDetailFields;

/**
 * Created by ameba on 4/4/18.
 */

public class List_Resturant_Fragment extends Fragment {
    private static final int REQUEST_PLACE_PICKER = 1;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    ImageView chat_obj, help_obj;
    @BindView(R.id.my_recycler_view)
    RecyclerView recyclerView;
    @BindViews({R.id.txt_loc, R.id.txt_change_loc})
    List <TextView> txtLocation;
    @BindView(R.id.edit_search)
    EditText edtSearch;
    @BindView(R.id.txt_no_data)
    TextView txtNoData;
    ApiInterface apiService;
    @BindView(R.id.lin_main)
    LinearLayout parentLayout;
    List <RestaurantDetailsByLatLong> beanListFilter = new ArrayList <>();
    ResturantAdapter resturantAdapter;
    @BindView(R.id.txt_title)
    TextView txtMap;
    private RecyclerView.Adapter mAdapter;
    private Toolbar toolbar_obj;
    private DrawerLayout drawer_obj;
    private ImageView drawer_icon;
    private CommonMethods commonMethods;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View V = inflater.inflate(R.layout.list_resturant_fragment, container, false);

        ButterKnife.bind(this, V);
        commonMethods = CommonMethods.getInstance();
        //Set Title
        txtMap.setVisibility(View.VISIBLE);
        txtMap.setText("List View");
        apiService = ApiClient.getClient().create(ApiInterface.class);
        chat_obj = (ImageView) V.findViewById(R.id.map_icon_id);
        help_obj = (ImageView) V.findViewById(R.id.list_icon_id);

        drawer_icon = (ImageView) V.findViewById(R.id.sett);
        drawer_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    CommonMethods.getInstance().hidekeyboard(view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                drawer_obj.openDrawer(GravityCompat.START);

            }
        });
        chat_obj.setVisibility(View.GONE);
        help_obj.setVisibility(View.VISIBLE);
        help_obj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!ConnectivityReceiver.isConnected()) {
                    Toast.makeText(commonMethods, "Internet connection not available", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        getFragmentManager().popBackStack();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        });

        toolbar_obj = (Toolbar) V.findViewById(R.id.toolbar_id);
        drawer_obj = (DrawerLayout) V.findViewById(R.id.drawerlayout_id2);

        CommonMethods.getInstance().setFont(txtLocation.get(0));
        txtLocation.get(0).setText(Constants.CURRENT_LOC);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());


        if (ConnectivityReceiver.isConnected()) {
            try {
                getDataFromAPI();

            } catch (Exception e) {
            }

        } else {

            Toast.makeText(getActivity(), "Internet Not Connected!", Toast.LENGTH_SHORT).show();
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        txtLocation.get(1).setVisibility(View.GONE);


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence cs, int start, int count, int after) {
                Log.e("beforeTextChanged", "beforeTextChanged=");
                beanListFilter.clear();
            }

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {

                ArrayList <ResturantDetailFields> resturantDetailFields = new ArrayList <>();

                if (HomeFragment.m_Home.beanList != null) {

                    beanListFilter.clear();

                    beanListFilter.add(new RestaurantDetailsByLatLong());
                }

                if (cs.length() > 0) {

                    for (int i = 0; i < HomeFragment.m_Home.beanList.get(0).getData().size(); i++) {

                        if (String.valueOf(HomeFragment.m_Home.beanList.get(0).getData().get(i).getName()).toLowerCase().contains(cs.toString().toLowerCase())) {

                            resturantDetailFields.add(HomeFragment.m_Home.beanList.get(0).getData().get(i));
                        }

                    }

                    beanListFilter.get(0).setData(resturantDetailFields);
                }


                if (beanListFilter.size() > 0) {

                    resturantAdapter.setItems(beanListFilter);

                } else {

                    recyclerView.setVisibility(View.GONE);

                    txtNoData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable cs) {

                if (cs.length() == 0) {

                    resturantAdapter.setItems(HomeFragment.m_Home.beanList);

                    recyclerView.setVisibility(View.VISIBLE);

                    txtNoData.setVisibility(View.GONE);
                }

            }
        });
        return V;
    }

    @OnClick({R.id.txt_change_loc})
    public void onClk(View view) {
        switch (view.getId()) {
            case R.id.txt_change_loc:
                try {
                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    Intent intent = intentBuilder.build(getActivity());
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
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PLACE_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                /* User has picked a place, extract data.
                   Data is extracted from the returned intent by retrieving a Place object from
                   the PlacePicker.
                 */
                final Place place = PlacePicker.getPlace(data, getActivity());
                final CharSequence name = place.getName();
                final CharSequence address = place.getAddress();
                final CharSequence phone = place.getPhoneNumber();
                final String placeId = place.getId();
                String attribution = PlacePicker.getAttributions(data);
                Log.e("name is=", "name==" + name);
                try {
                    Constants.CURRENT_LOC = (String) name;
                    Log.e("yeah=", "i m List" + Constants.CURRENT_LOC);
                    txtLocation.get(0).setText(Constants.CURRENT_LOC);
                    Constants.CHANGED = "List";
                } catch (Exception e) {

                }
                final LatLng lat = place.getLatLng();

                if (attribution == null) {
                    attribution = "";
                }

            } else {
                // User has not selected a place, hide the card.
                //getCardStream().hideCard(CARD_DETAIL);
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void getDataFromAPI() {
        if (HomeFragment.m_Home.beanList.size() > 0) { //Critical June 13

            resturantAdapter = new ResturantAdapter(getActivity(), HomeFragment.m_Home.beanList) {
            };
            recyclerView.setAdapter(resturantAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromAPI();
    }
}
