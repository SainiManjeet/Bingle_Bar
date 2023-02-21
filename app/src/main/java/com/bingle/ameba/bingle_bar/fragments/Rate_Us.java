package com.bingle.ameba.bingle_bar.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bingle.ameba.bingle_bar.R;
import com.bingle.ameba.bingle_bar.activities.MainActivity;
import com.bingle.ameba.bingle_bar.common_functions.Bean_CommonResponse;
import com.bingle.ameba.bingle_bar.common_functions.CommonMethods;
import com.bingle.ameba.bingle_bar.common_functions.Constants;
import com.bingle.ameba.bingle_bar.common_functions.ServicesResponse;
import com.bingle.ameba.bingle_bar.common_functions.Users;
import com.bingle.ameba.bingle_bar.receivers.ConnectivityReceiver;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by vineet on 15/5/18.
 */


public class Rate_Us extends Fragment implements ServicesResponse {
    private static final String TAG = "Rate_Us";
    @BindView(R.id.lin_main2)
    LinearLayout parentLayout2;
    @BindView(R.id.cancel_id)
    ImageView imgCancel;
    Users users;


    CommonMethods commonMethods = new CommonMethods();
    private Toolbar toolbar_obj;
    private DrawerLayout drawer_obj;
    private ImageView chat_obj, drawer_icon, profile_image;
    private TextView rate_obj, send_obj, rate_name_obj;
    private LinearLayout rate_uss_obj;
    private EditText title_obj, reviews_obj;
    private RatingBar ratingBar;

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rate_us, container, false);

        ButterKnife.bind(this, view);

        // need to do in a method

        rate_obj = view.findViewById(R.id.rate_id);
        send_obj = view.findViewById(R.id.send_id);
        rate_name_obj = view.findViewById(R.id.rate_name_id);
        title_obj = view.findViewById(R.id.title_id);
        reviews_obj = view.findViewById(R.id.review_name_id);
        ratingBar = view.findViewById(R.id.rating);
        profile_image = view.findViewById(R.id.profile_image);
        imgCancel = view.findViewById(R.id.cancel_id);

        //set font
        commonMethods = CommonMethods.getInstance();
        commonMethods.setFont(rate_obj);
        commonMethods.setFont(rate_name_obj);


        //users object
        users = new Users(getActivity());


        //display Images
        String mType = "";
        SharedPreferences sharedPreferences_getfb = getActivity().getSharedPreferences("God", Context.MODE_PRIVATE);
        mType = sharedPreferences_getfb.getString("nirankar", "");

        if (mType.equalsIgnoreCase("Fb")) {
            try {
                JSONObject profile_pic_data, profile_pic_url;

                String fb_login = MainActivity.m_activity.get_data_fb("fb_name");
                rate_name_obj.setText(fb_login);
                String fb_pic = MainActivity.m_activity.get_data_fb("fb_picture");
                Log.d(TAG, "onCreateView: " + fb_login);
                profile_pic_data = new JSONObject(fb_pic);
                profile_pic_url = new JSONObject(profile_pic_data.getString("data"));


                if (profile_pic_url.getString("url") != null && !profile_pic_url.getString("url").isEmpty() && !profile_pic_url.getString("url").equalsIgnoreCase("null")) {
                    Glide.with(getContext()).load(profile_pic_url.getString("url"))
                            .thumbnail(0.5f)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(profile_image);
                }


            } catch (Exception e) {

            }

        }
        if (mType.equalsIgnoreCase("Google")) {
            String g_name = MainActivity.m_activity.get_data_fb("display_name");
            String g_pic = MainActivity.m_activity.get_data_fb("display_pic");
            rate_name_obj.setText(g_name);

            if (g_pic != null && !g_pic.isEmpty() && !g_pic.equalsIgnoreCase("null")) {
                Glide.with(getContext()).load(g_pic).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(profile_image);
            }
        }

        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#43aef5"), PorterDuff.Mode.SRC_ATOP);

        //Canceled_onclickListner
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.m_activity.swapContentFragment(new HomeFragment(), "HomeFragment", true);
            }
        });

        //Send_onclickListner
        send_obj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!ConnectivityReceiver.isConnected()) {
                    Toast.makeText(commonMethods, "Internet connection not available", Toast.LENGTH_SHORT).show();
                } else {
                        callRatingApi();
                        title_obj.setText("");
                        reviews_obj.setText("");
                }
            }
        });
        return view;
    }

    private void callRatingApi() {
        String title = title_obj.getText().toString();
        String reviews = reviews_obj.getText().toString();
        int rating = (int) ratingBar.getRating();
        if (rating==0){
            Toast.makeText(commonMethods, "Please add rating", Toast.LENGTH_SHORT).show();
            return;
        }

        String rating_Num = String.valueOf(rating);
        String id = users.getUserId();

        saveAppRatingByUser(id, title, reviews, rating_Num);
    }

    private void saveAppRatingByUser(String id, String title, String reviews, String rating_Num) {
        CommonMethods.getInstance().createRetrofitBuilderWithHeader(getActivity());

        try {
            HashMap <String, String> params = new HashMap <>();

            params.put("UserID", id);
            params.put("Rating", rating_Num);
            params.put("Title", title);
            params.put("Review", reviews);

            CommonMethods.getInstance().callService(getActivity(), this, parentLayout2, Constants.saveAppRatingByUser, params);

        } catch (Exception e) {

            CommonMethods.getInstance().dismissProgressDialog();

            Log.e("in_exception_" + Constants.saveAppRatingByUser, "" + e.toString());
        }
    }

    @Override
    public void onResponseSuccess(Bean_CommonResponse response) throws JSONException {
        CommonMethods.getInstance().dismissProgressDialog();
        JSONObject jsonObject = response.getJsonObject();

        Boolean status = jsonObject.getBoolean("success");
        Log.e(TAG, "onResponseSuccess:njnjhjj ");

        if (status) {

            Toast.makeText(commonMethods, "Your Rating added successfully", Toast.LENGTH_SHORT).show();


        } else {
            Toast.makeText(commonMethods, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}
