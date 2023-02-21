package com.bingle.ameba.bingle_bar.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bingle.ameba.bingle_bar.R;
import com.bingle.ameba.bingle_bar.activities.MainActivity;
import com.bingle.ameba.bingle_bar.common_functions.Bean_CommonResponse;
import com.bingle.ameba.bingle_bar.common_functions.CommonMethods;
import com.bingle.ameba.bingle_bar.common_functions.Constants;
import com.bingle.ameba.bingle_bar.common_functions.ServicesResponse;
import com.bingle.ameba.bingle_bar.receivers.ConnectivityReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Welcome_Fragment extends Fragment implements ServicesResponse {
    private static final String TAG = "Welcome_Fragment";
    @BindView(R.id.txt_user)
    TextView txtUserName;
    @BindView(R.id.btn_ok)
    Button btnOk;
    @BindView(R.id.lin)
    LinearLayout parentLayout;
    int m = 0;
    int userId;
    String socialType;
    SharedPreferences sharedPreferences_getData;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    String gmail_name = MainActivity.m_activity.get_data_fb("splited");
    private CommonMethods commonMethods;
    private boolean mLocationPermissionGranted;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thank_you, container, false);
        ButterKnife.bind(this, view);
        commonMethods = CommonMethods.getInstance();
        commonMethods.dismissProgressDialog();
        sharedPreferences_getData = getActivity().getSharedPreferences("God", Context.MODE_PRIVATE);

       // Firebase.setAndroidContext(getActivity());
        getLocationPermission();
        getUserDetail();
        return view;
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
     //   updateLocationUI();
    }

    private void getUserDetail() {
        if (Constants.loginType == "Fb") {
            getfacebookResponse();
        } else {
            String gmail_name = MainActivity.m_activity.get_data_fb("display_name");
            commonMethods.splitData(gmail_name);
            String gmail = MainActivity.m_activity.get_data_fb("splited");
            txtUserName.setText(gmail);
        }

    }

    private void getfacebookResponse() {
        String fb_login = MainActivity.m_activity.get_data_fb("fb_name");
        Log.e(TAG, "getfacebookResponse: " + fb_login);
        commonMethods.splitData(fb_login);
        txtUserName.setText(gmail_name);
    }

    @OnClick({R.id.btn_ok})
    public void onClk(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                SubmitButton();
                break;
        }
    }
    private void SubmitButton() {
        if (!ConnectivityReceiver.isConnected()) {
            Toast.makeText(commonMethods, "Internet connection not available", Toast.LENGTH_SHORT).show();
        } else {
            MainActivity.m_activity.swapContentFragment(new HomeFragment(), "Home_Fragment", false);
        }
    }
    @Override
    public void onResponseSuccess(Bean_CommonResponse response) throws JSONException {
        Log.e("getDataSignUp response", response.toString());
        commonMethods.dismissProgressDialog();
        if (m == 0) {//Signup Data
            JSONObject jsonObject = response.getJsonObject();
            Log.e("getData jsonObject", jsonObject.toString());
            String status = jsonObject.getString("success");
            if (status.equalsIgnoreCase("true")) {
                Log.e("getData Trueee", status);
                userId = jsonObject.getInt("data");
                Log.e("userId =", "userId" + userId);
                MainActivity.m_activity.set_data_fb("userId", String.valueOf(userId)); //@M
                m = 1;
              //  callServices();
            }
        }
    }
}
