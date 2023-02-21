package com.bingle.ameba.bingle_bar.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.bingle.ameba.bingle_bar.BuildConfig;
import com.bingle.ameba.bingle_bar.R;
import com.bingle.ameba.bingle_bar.common_functions.CommonMethods;
import com.bingle.ameba.bingle_bar.common_functions.Users;
import com.bingle.ameba.bingle_bar.fragments.HomeFragment;
import com.bingle.ameba.bingle_bar.fragments.LoginFragment;
import com.bingle.ameba.bingle_bar.receivers.ConnectivityReceiver;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.firebase.client.Firebase;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import java.util.Arrays;

import static com.bingle.ameba.bingle_bar.fragments.LoginFragment.mGoogleApiClient;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private static final String TAG = "MainActivity";
    public static MainActivity m_activity = null;
    private static long back_pressed;
    LocationManager locationManager;
    boolean GpsStatus;
    private Fragment m_fragment = null;
    private Handler m_handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("MainActivity", "MainActivity");

//On Android, the SDK's app activation helper should be invoked once when the application is created
        FacebookSdk.sdkInitialize(getApplicationContext());

        // FireBase SDK initialization
        Firebase.setAndroidContext(getApplicationContext());

        // Save the current version code to shared preferences so that it can be used for later version major upgrades.
        versionMaintenanceLogic();

        //Newer version of Firebase
       /* if(!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
*/

        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_main);


        TelephonyManager tm = (TelephonyManager) getApplicationContext()
                .getSystemService(Context.TELEPHONY_SERVICE);

        String countryLocale = tm.getSimCountryIso().toUpperCase();
        Log.e("countryLocale", "countryLocale" + countryLocale);

        m_activity = this;


        try {
            LoginManager.getInstance().logOut();
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();

        } catch (Exception e) {
        }

        isLocationEnabled();
        if ((int) Build.VERSION.SDK_INT <= 23) {
            if (!isLocationEnabled()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("GPS not enabled")
                        .setMessage("Enable location Permission")
                        .setPositiveButton("ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                    }
                                });

                AlertDialog alert = builder.create();
                alert.show();
            } else {
                //Location On
            }
        }


        Users users = new Users(MainActivity.this);
        Log.e("userID", "userID" + users.getUserId());
        if (users.getUserId() != null && users.getUserId() != "") {
            swapContentFragment(new HomeFragment(), "Home_Fragment", false);
        } else {
            swapContentFragment(new LoginFragment(), "LoginFragment", false);
        }

    }



    public void addFragmentToFirstPosition(final Fragment i_newFragment, final String tag) {
        try {
            FragmentManager fm = getSupportFragmentManager();

            for (Fragment fragment : getSupportFragmentManager().getFragments()) {

                fm.beginTransaction().remove(fragment).commit();
            }

            fm.beginTransaction().add(R.id.container, i_newFragment, tag).commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void swapContentFragment(final Fragment i_newFragment, final String tag, final boolean i_addToStack) {

        try {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.container, i_newFragment, tag);
            if (i_addToStack) {
                transaction.addToBackStack(null);
            }
            transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void set_data_fb(String key, String value) {
        SharedPreferences.Editor sharedPreferences_fb = getSharedPreferences("my_fb_data", Context.MODE_PRIVATE).edit();
        sharedPreferences_fb.putString(key, value);
        sharedPreferences_fb.commit();
        Log.d(TAG, "set_data: " + value);

    }

    public String get_data_fb(String key) {
        SharedPreferences sharedPreferences_getfb = getSharedPreferences("my_fb_data", Context.MODE_PRIVATE);
        return sharedPreferences_getfb.getString(key, "");
    }


    @Override
    public void onBackPressed() {

        FragmentManager fm = getSupportFragmentManager();

        if (fm.getBackStackEntryCount() == 0) {

            finish();

        } else {

            fm.popBackStack();
        }

        /*
        int k = 0;
        Log.e("My Tags", "onBackPressed");
        k++;
        if (k == 1) {
            super.onBackPressed();
        } else {
            finish();
        }
        */
    }

    public void CheckGpsStatus() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (GpsStatus == true) {
            Log.e("LocationEnabled", "LocationEnabled")
            ;
        } else {
            Log.e("LocationDnabled", "LocationDnabled");
            Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent1);
        }
    }


    public boolean isLocationEnabled() {
        String le = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(le);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();


        super.onStart();
    }


   /* @Override
    protected void onResume() {

        Log.e("onResume", "onResume");
        FirebaseChatManager.getInstance().setUserPresenceStatus("Online");

        super.onResume();
        CommonMethods.getInstance().setConnectivityListener(this);
    }*/

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        CommonMethods.getInstance().showSnack(isConnected,MainActivity.this);
    }

    // Version Maintenance Logic

    private void majorAppChangesUpgrade() {

        //do nothing - database already exist
        if (getAppVersionCode() == false) {

            // Perform all logics here.
            // User is using old version. Proceed with new version logic setup.

        } else {

            // User is using latest version.
        }
    }

    private Boolean getAppVersionCode() {

        // Latest running version.
        String runningAppVersionName = BuildConfig.VERSION_NAME;

        Users users = new Users(getApplicationContext());

        // Last version saved.
        String savedAppVersionName = users.getCurrentVersionKey();

        String[] existingVersions = {"1.0"};

        if (!runningAppVersionName.equals(savedAppVersionName) && !(Arrays.asList(existingVersions).contains(runningAppVersionName))) {

            // User is using old version. Proceed with new version logic setup.
            return false;

        } else {

            // User is using latest version.
            return true;
        }
    }

    private void versionMaintenanceLogic() {

        // Writing data to SharedPreferences
        Users users = new Users(getApplicationContext());
        users.setCurrentVersionKey(BuildConfig.VERSION_NAME);
    }


}
