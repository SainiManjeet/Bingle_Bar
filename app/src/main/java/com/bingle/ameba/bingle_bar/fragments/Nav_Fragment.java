package com.bingle.ameba.bingle_bar.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bingle.ameba.bingle_bar.R;
import com.bingle.ameba.bingle_bar.activities.MainActivity;
import com.bingle.ameba.bingle_bar.activities.SimpleTabsActivity;
import com.bingle.ameba.bingle_bar.common_functions.Bean_CommonResponse;
import com.bingle.ameba.bingle_bar.common_functions.CommonMethods;
import com.bingle.ameba.bingle_bar.common_functions.Constants;
import com.bingle.ameba.bingle_bar.common_functions.ServicesResponse;
import com.bingle.ameba.bingle_bar.common_functions.Users;
import com.bingle.ameba.bingle_bar.common_functions.firebase_chat_managers.FirebaseChatManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import chat.UsersList;

import static com.bingle.ameba.bingle_bar.fragments.LoginFragment.mGoogleApiClient;


public class Nav_Fragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, ServicesResponse {
    private static final String TAG = "Nav_Fragment";
    String[] maintitle = {
            "Tell a friend", "Rate us",
            "send feedback", "Help"
    };

    Integer[] imgid = {
            R.drawable.friend, R.drawable.rate,
            R.drawable.feedback, R.drawable.help
    };
    private ListView listView;
    private TextView t1, t2, txtChat;
    private ImageView imageView;
    private RelativeLayout r1, r2, r3, r4;
    private AlertDialog alert11;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.nav_fragment, container, false);
        //Initialize Views
        setupView(v);

        String mType = "";
        MyListItemArtist myListItemArtist = new MyListItemArtist(getActivity(), maintitle, imgid);
        listView.setAdapter(myListItemArtist);
        SharedPreferences sharedPreferences_getfb = getActivity().getSharedPreferences("God", Context.MODE_PRIVATE);
        mType = sharedPreferences_getfb.getString("nirankar", "");
        Log.e("mType==", "mType==" + mType);

        if (mType.equalsIgnoreCase("Fb")) {
            try {
                JSONObject profile_pic_data, profile_pic_url;

                String fb_login = MainActivity.m_activity.get_data_fb("fb_name");
                t1.setText(fb_login);
                String fb_pic = MainActivity.m_activity.get_data_fb("fb_picture");
                Log.d(TAG, "onCreateView: " + fb_login);
                profile_pic_data = new JSONObject(fb_pic);
                profile_pic_url = new JSONObject(profile_pic_data.getString("data"));

                if (profile_pic_url.getString("url") != null) {
                    Glide.with(getContext()).load(profile_pic_url.getString("url"))
                            .thumbnail(0.5f)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView);
                } else {
                    Glide.with(getContext()).load(R.drawable.user2)
                            .thumbnail(0.5f)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView);
                }
            } catch (Exception e) {

            }

        }
        if (mType.equalsIgnoreCase("Google")) {
            String g_name = MainActivity.m_activity.get_data_fb("display_name");
            Log.e("g_name=", "g_name=" + g_name);
            String g_pic = MainActivity.m_activity.get_data_fb("display_pic");
            t1.setText(g_name);
            if (g_pic != null && !g_pic.isEmpty()) {
                Glide.with(getContext()).load(g_pic).thumbnail(0.5f).crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
            } else {
                Glide.with(getContext()).load(R.drawable.user2).thumbnail(0.5f).crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
            }
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.near_relative:
                MainActivity.m_activity.swapContentFragment(new HomeFragment(), "HomeFragment", true);
                break;
            case R.id.chat_relative:
                openUsersView();
                break;
            case R.id.setting1_relative:
                listView.setVisibility(View.VISIBLE);
                r4.setVisibility(View.VISIBLE);
                r3.setVisibility(View.GONE);
                break;
            case R.id.setting2_relative:
                listView.setVisibility(View.GONE);
                r4.setVisibility(View.GONE);
                r3.setVisibility(View.VISIBLE);
                break;
            case R.id.logout_button:
                Logout();
        }
    }

    private void Logout() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Are you sure you want to Logout?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final Users users = new Users(getActivity());

                        // Clear FirebaseHashChatId and UserID from Shared Preferences
                        // so that our server and firebase server queries couldn't work.

                        // Constants.CURRENT_LOC="";

                        //Update status Offline in FCM data base TableName:Users-Live
                        FirebaseChatManager.getInstance().setUserPresenceStatus("Offline");
                        //Call Logout API
                        userLogoutApiCall();

                        users.setUserId("");
                        users.setFirebaseChatUserHashId("");
                        users.remove();
                        dialog.cancel();
                        alert11.dismiss();

                        if (Constants.loginType == "Fb") {
                            LoginManager.getInstance().logOut();
                            SharedPreferences preferences = getActivity().getSharedPreferences("my_fb_data", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.commit();
                            getActivity().finish();

                            startActivity(new Intent(getActivity(), MainActivity.class));
                        } else {
                            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                    new ResultCallback <Status>() {
                                        @Override
                                        public void onResult(Status status) {
                                            SharedPreferences preferences = getActivity().getSharedPreferences("my_fb_data", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.clear();
                                            editor.commit();
                                            getActivity().finish();
                                            startActivity(new Intent(getActivity(), MainActivity.class));
                                        }
                                    });
                        }


                    }
                });
        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alert11 = builder1.create();
        alert11.show();
    }

    //Might Required dont delete for the time
    private void updateUI(boolean b) {
        if (!b) {
            startActivity(new Intent(getActivity(), MainActivity.class));
        }
    }

    private void openUsersView() {

        UsersList fragment = new UsersList();

        /*Bundle bundle = new Bundle();

        bundle.putString("from", "MainChat");

        fragment.setArguments(bundle);

        MainActivity.m_activity.swapContentFragment(fragment, "UsersList", true);*/


        Intent in = new Intent(getActivity(), SimpleTabsActivity.class);
        startActivity(in);


    }

    private void setupView(View v) {
        txtChat = v.findViewById(R.id.txt_chat);
        t1 = v.findViewById(R.id.Username);
        t2 = v.findViewById(R.id.email);
        r1 = v.findViewById(R.id.near_relative);
        r2 = v.findViewById(R.id.chat_relative);
        r3 = v.findViewById(R.id.setting1_relative);
        r4 = v.findViewById(R.id.setting2_relative);
        LinearLayout l1 = v.findViewById(R.id.logout_button);
        imageView = v.findViewById(R.id.profile_image);
        listView = v.findViewById(R.id.list);
        r1.setOnClickListener(this);
        r2.setOnClickListener(this);
        r3.setOnClickListener(this);
        r4.setOnClickListener(this);
        l1.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        r4.setOnClickListener(this);
        userCurrentStatus();
    }

    //To check weather user is inside the Restaurant or Outside
    private void userCurrentStatus() {
        Users users = new Users(getActivity());
        Log.e("getGpsLocation", "getGpsLocation" + users.getGpsLocation());
        if (users.getGpsLocation().equalsIgnoreCase("true")) {
            txtChat.setText("Chat");
        } else {
            txtChat.setText("Chat History");
        }
    }

    @Override
    public void onItemClick(AdapterView <?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                Share();
                break;
            case 1:
                RateUs();
                break;
            case 2:
                sendFeedback();
                break;
            case 3:
                help();
        }
    }

    private void Share() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.bingle.ameba.bingle_bar&hl=en");

        startActivity(Intent.createChooser(shareIntent, "Share link using"));
    }

    private void RateUs() {
        MainActivity.m_activity.swapContentFragment(new Rate_Us(), "Rate_Us", true);
    }

    private void sendFeedback() {
        MainActivity.m_activity.swapContentFragment(new SendFeedback(), "SendFeedback", true);
    }

    private void help() {
        MainActivity.m_activity.swapContentFragment(new HelpFragment(), "HelpFragment", true);
    }


    private void userLogoutApiCall() {
        CommonMethods.getInstance().createRetrofitBuilderWithHeader(getActivity());
        try {
            HashMap <String, String> params = new HashMap <>();
            Users users = new Users(getActivity());
            params.put("userID", users.getUserId());
            params.put("deviceNumber_IMEI", Constants.androidId);
            Log.e("params", params.toString());
            CommonMethods.getInstance().callServiceBackground(getActivity(), this, Constants.USER_LOGOUT_SERVICE_ID, params);
        } catch (Exception e) {
            Log.e("in_exception_" + Constants.USER_LOGOUT_SERVICE_ID, "" + e.toString());
        }
    }

    @Override
    public void onResponseSuccess(Bean_CommonResponse response) throws JSONException {
        JSONObject jsonObject = response.getJsonObject();
        Boolean responseStatus = jsonObject.getBoolean("response");
        Log.e("responseStatusLogout", "responseStatusLogout" + responseStatus);

        if (responseStatus) {
            Log.e("True", "True");

        } else {
            Log.e("False", "False");
        }

    }
}
