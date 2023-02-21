package com.bingle.ameba.bingle_bar.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bingle.ameba.bingle_bar.R;
import com.bingle.ameba.bingle_bar.common_functions.Bean_CommonResponse;
import com.bingle.ameba.bingle_bar.common_functions.CommonMethods;
import com.bingle.ameba.bingle_bar.common_functions.Constants;
import com.bingle.ameba.bingle_bar.common_functions.ServicesResponse;
import com.bingle.ameba.bingle_bar.receivers.ConnectivityReceiver;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapters.PhotosAdapter;
import butterknife.BindView;
import pojo.RestaurantPhotos;
import pojo.RestaurantPhotosAPI;
import pojo.ResturantDetailFields;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by vineet on 27/4/18.
 */

public class Photos extends Fragment implements ServicesResponse {
    private static final String TAG = "Photos";


    //List <ResturantDetailFields> beanList1;
    List<RestaurantPhotos> beanList1;
    String mName, mAddress, address2;
    @BindView(R.id.lin_main2)
    LinearLayout parentLayout2;
    ResturantDetailFields resturantDetailFields;
    private ImageView back, menu, view;
    private TextView Resturents_obj, sectors_obj;
    private RecyclerView recyclerView;
    private CommonMethods commonMethods;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.photos, container, false);

        recyclerView = v.findViewById(R.id.recycler_id);
        Resturents_obj = v.findViewById(R.id.Resturents_id);
        sectors_obj = v.findViewById(R.id.sectors_id);
        back = v.findViewById(R.id.back);
        menu = v.findViewById(R.id.sett);
        view = v.findViewById(R.id.map_icon_id);
        view.setVisibility(View.GONE);
        menu.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getFragmentManager().popBackStack();

            }
        });

        commonMethods = CommonMethods.getInstance();
        beanList1 = new ArrayList<>();
        recyclerView.setHasFixedSize(true);


        resturantDetailFields = (ResturantDetailFields) getArguments().getSerializable("restaurant_data");

        if (!ConnectivityReceiver.isConnected()) {
            Toast.makeText(commonMethods, "Internet connection not available", Toast.LENGTH_SHORT).show();
        } else {
            getRestaurantImagesUsingRestaurantID(resturantDetailFields.getRestaurantId());
        }

        try {
            //Get pass data with its key value  : API must be implemented to get the data

            Log.e(TAG, "namne: " + resturantDetailFields.getName());
            Resturents_obj.setText(resturantDetailFields.getName());

          /*  mName = getArguments().getString("name");
            mAddress = getArguments().getString("address");
            address2 = getArguments().getString("address2");
            Resturents_obj.setText(mName);
            sectors_obj.setText(mAddress);*/

        } catch (Exception e) {

        }

        String re = resturantDetailFields.getRestaurantId();
        Log.e(TAG, "onCreateView: " + re);

        return v;
    }


    private void getRestaurantImagesUsingRestaurantID(String RestaurantId) {

        CommonMethods.getInstance().createRetrofitBuilderWithHeader(getActivity());

        try {
            HashMap<String, String> params = new HashMap<>();

            params.put("restaurantId", RestaurantId);

            CommonMethods.getInstance().callService(getActivity(), this, parentLayout2, Constants.getRestaurantImagesUsingRestaurantID, params);

        } catch (Exception e) {

            CommonMethods.getInstance().dismissProgressDialog();

            Log.e("in_exception_" + Constants.getRestaurantImagesUsingRestaurantID, "" + e.toString());
        }

    }


    @Override
    public void onResponseSuccess(Bean_CommonResponse response) throws JSONException {
        CommonMethods.getInstance().dismissProgressDialog();

        JSONObject jsonObject = response.getJsonObject();

        Boolean status = jsonObject.getBoolean("success");

        if (status) {

            Gson gson = new Gson();

            RestaurantPhotosAPI restaurantPhotosAPI = gson.fromJson(String.valueOf(jsonObject), RestaurantPhotosAPI.class);

            for (RestaurantPhotos restaurantPhotos:
                 restaurantPhotosAPI.getData()) {

                beanList1.add(restaurantPhotos);
            }

            setAdapter();

        } else {

        }
    }

    private void setAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        PhotosAdapter myMainAdapter = new PhotosAdapter(getActivity(), beanList1);
        recyclerView.setAdapter(myMainAdapter);
    }
}
