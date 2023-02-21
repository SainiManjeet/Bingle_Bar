package com.bingle.ameba.bingle_bar.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import pojo.PhoneVerify;


/**
 * Created by ameba on 12/2/18.
 */

public class LoginFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, ServicesResponse {
    private static final String TAG = "LoginFragment";
    private static final int RC_SIGN_IN = 007;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;
    public static GoogleApiClient mGoogleApiClient;
    //VideoView videoView;
    @BindView(R.id.lin_parent_layout)
    LinearLayout parentLayout;
    String socialLoginUniqueId;
    String Country_code;
    String country_code_dummy = "+", add_concat, phone_number, verfication_code;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    private Button button_fb, buttonGoogle;
    private CallbackManager callbackManager;
    private LoginButton loginButton_fb;
    private TextView sign_up_obj;
    private ProgressDialog mProgressDialog;
    private String personPhotoUrl;
    private boolean mLocationPermissionGranted;
    private boolean firstRunFlag = true;
    private boolean mTelephonyPermissionGranted;
    @BindView(R.id.img_gf)
    ImageView img;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login, container, false);
        ButterKnife.bind(this, view);

        //Load Gif Image
        Glide.with(getContext()).load(R.drawable.login_gif)
            .into(img);

        //Play Video(Functionality hidden for now)
       /* videoView = (VideoView) view.findViewById(R.id.video_view);
        String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.login_v;
        videoView.setVideoURI(Uri.parse(path));
        videoView.start();*/
        //google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        try {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity(), this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        } catch (Exception e) {
        }

        try {
            if (Constants.loginType == "Fb") {
                LoginManager.getInstance().logOut();

            } else {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback <Status>() {
                            @Override
                            public void onResult(Status status) {

                            }
                        });
            }
        } catch (Exception e) {
        }


        setupView(view);
        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connectivityManager
                        = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null) {

                    //@Manjeet Code June 1
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback <Status>() {
                                @Override
                                public void onResult(Status status) {
                                }
                            });
                    //
                    Constants.loginType = "Google";
                    //DO CHANGE THE SharedPreferences NAME AND MAKE IT GLOBAL
                    SharedPreferences.Editor sharedPreferences_fb = getActivity().getSharedPreferences("God", Context.MODE_PRIVATE).edit();
                    sharedPreferences_fb.putString("nirankar", "Google");
                    sharedPreferences_fb.commit();
                    showSocialprogressDialog();
                    signIn();
                } else {
                    Toast.makeText(getContext(), "Internet connection not working.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        callbackManager = CallbackManager.Factory.create();

        button_fb.setOnClickListener(this);
        loginButton_fb.setReadPermissions("email");
        loginButton_fb.setFragment(this);
        loginButton_fb.registerCallback(callbackManager, new FacebookCallback <LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("onSuccess", "onSuccess");
                getUserDetails(loginResult);
            }

            @Override
            public void onCancel() {
                Log.e("onCancel", "onCancel");
                //videoView.start();
                Toast.makeText(getContext(), "Cancel by user", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("onError", "onError");
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        LoginManager.getInstance().logOut();

        //videoView.start();
    }

    private void showSocialprogressDialog() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Verifying credentials");
        mProgressDialog.setCancelable(true);
        mProgressDialog.setCanceledOnTouchOutside(true);
        mProgressDialog.show();
    }

    private void signIn() {
        Log.e("i m here", "clk");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        try {
            mProgressDialog.dismiss();
        } catch (Exception e) {

        }

        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            handleSignInResult(result);
        }
    }

    public void getUserDetails(LoginResult loginResult) {
        // Make a graph request for more information about the user.
        // Creating graph request object by passing valid access token and graph completion callback.
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {


                    // Found the bug here
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {


//crashhhhh here

                        Log.d(TAG, "onCompleted: " + object);
                        String json = object.toString();
                        JSONObject response1 = null;

                        try {

                            Users users = new Users(getActivity());
                            response1 = new JSONObject(json);
                            String name = response1.getString("name");

                            socialLoginUniqueId = response1.getString("id");
                            MainActivity.m_activity.set_data_fb("loginchoiceUniqueID", socialLoginUniqueId);
                            MainActivity.m_activity.set_data_fb("loginChoiceID", "1");

                            MainActivity.m_activity.set_data_fb("fb_name", name);
//                           String email = response1.getString("email");
                            String picture = response1.getString("picture");

                            users.setName(name);

                            MainActivity.m_activity.set_data_fb("fb_name", name); //must commented but for now dont remove
                            MainActivity.m_activity.set_data_fb("fb_picture", picture);
                            // to save data to show on slider
                            MainActivity.m_activity.set_data_fb("display_name", name);
                            // MainActivity.m_activity.set_data_fb("display_email", email);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        checkIsUserLoggedInWithLoginType(socialLoginUniqueId);
                    }
                });

        // Set the permission
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(120).height(120)");
        request.setParameters(parameters);
        request.executeAsync();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fb_button_id:
                ConnectivityManager connectivityManager
                        = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null) {
                    Constants.loginType = "Fb";

                    SharedPreferences.Editor sharedPreferences_fb = getActivity().getSharedPreferences("God", Context.MODE_PRIVATE).edit();
                    sharedPreferences_fb.putString("nirankar", "Fb");
                    sharedPreferences_fb.commit();

                    showSocialprogressDialog();
                    loginButton_fb.performClick();
                } else {
                    Toast.makeText(getContext(), "Internet connection not working.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.Sign_up_id:

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onStart() {
        super.onStart();
        OptionalPendingResult <GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.

            // showProgressDialog(); //Do uncomment
            if (firstRunFlag) {
//
            } else {
                opr.setResultCallback(new ResultCallback <GoogleSignInResult>() {
                    @Override
                    public void onResult(GoogleSignInResult googleSignInResult) {
                        hideProgressDialog();
                        handleSignInResult(googleSignInResult);
                    }
                });
            }
        }
    }


    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();

        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {

            Users users = new Users(getActivity());
            users.remove();

            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            socialLoginUniqueId = acct.getId();
            MainActivity.m_activity.set_data_fb("loginchoiceUniqueID", socialLoginUniqueId);
            //loginChoiceID
            MainActivity.m_activity.set_data_fb("loginChoiceID", "2");


            String personName = acct.getDisplayName();
            try {
                personPhotoUrl = acct.getPhotoUrl().toString();
                Log.e("Pic=", "" + personPhotoUrl);
            } catch (Exception e) {
            }
            String email = acct.getEmail();

            Users users1 = new Users(getActivity());
            users1.setName(email);

            firstRunFlag = false;
            MainActivity.m_activity.set_data_fb("display_name", personName);
            MainActivity.m_activity.set_data_fb("display_pic", personPhotoUrl);
            MainActivity.m_activity.set_data_fb("display_email", email);
            // CALL WEB SERVICES HERE
            checkIsUserLoggedInWithLoginType(socialLoginUniqueId);

            //   updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            // updateUI(false);
            Toast.makeText(getContext(), "Process Failed", Toast.LENGTH_SHORT).show();
        }
    }
    private void setupView(View view){
        buttonGoogle = (Button) view.findViewById(R.id.btn_google);
        button_fb = (Button) view.findViewById(R.id.fb_button_id);
        loginButton_fb = (LoginButton) view.findViewById(R.id.login_fb_id);
        sign_up_obj = (TextView) view.findViewById(R.id.Sign_up_id);
        button_fb = (Button) view.findViewById(R.id.fb_button_id);

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

    private void checkIsUserLoggedInWithLoginType(String loginchoiceUniqueID) {
        CommonMethods.getInstance().createRetrofitBuilderWithHeader(getActivity());
        try {
            HashMap <String, String> params = new HashMap <>();

           // CommonMethods.getInstance().sharePrefForLoginType();

            SharedPreferences sharedPreferences_getfb = getActivity().getSharedPreferences("God", Context.MODE_PRIVATE);

            String socialType = sharedPreferences_getfb.getString("nirankar", "");

            params.put("loginchoiceUniqueID", loginchoiceUniqueID);
            if (socialType.equalsIgnoreCase("Fb")) {
                params.put("loginChoiceID", "1");
            } else {
                params.put("loginChoiceID", "2");
            }
            Log.e("params", "=>" + params.toString());
            CommonMethods.getInstance().callService(getActivity(), this, parentLayout, Constants.checkIsUserLoggedInWithLoginType, params);

        } catch (Exception e) {

            CommonMethods.getInstance().dismissProgressDialog();

            Log.e("in_exception_" + Constants.checkIsUserLoggedInWithLoginType, "" + e.toString());
        }
    }
    @Override
    public void onResponseSuccess(Bean_CommonResponse response) throws JSONException {

        CommonMethods.getInstance().dismissProgressDialog();

        JSONObject jsonObject = response.getJsonObject();

        Log.e("response","response"+response.toString());

        Boolean status = jsonObject.getBoolean("success");

        Log.e("staus","status"+status);

        if (status) {

            Gson gson = new Gson();

            PhoneVerify phoneVerify = gson.fromJson(String.valueOf(jsonObject.getJSONObject("data")), PhoneVerify.class);

            Users users = new Users(getActivity());
           // Set User Name
            users.setName(phoneVerify.getUserName());
            // Check whether do we need to update the FirebaseAuth object or not.
            /* Scanerio:
            *   1. Check if existing user and using different login than before.
            *   2. App Reinstall or using old login in new device.
            * */
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                // 2. App Reinstall or using old login in new device.
                phoneNoVerification(phoneVerify.getContactNumber());
                return;
            } else {
                if (!phoneVerify.getFirebaseChatID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    // 1. User is existing user and using different login than before.

                    Log.e("phn", "phn"+"User is existing user");

                    phoneNoVerification(phoneVerify.getContactNumber());
                    return;
                }
            }
            // Set SharedPreferences with UserId, FirebaseHashChatId, FirebaseOriginalKey, VersionUpgradeCode(Server Generated).
            // Set UserId
            users.setUserId(phoneVerify.getUserID());

            // Set Original FirebaseChatIDKey
            users.setFirebaseChatID(phoneVerify.getFirebaseChatID());

            // Check and Set VersionUpgradeCode
            if (phoneVerify.getVersionUpgradeCode() != null && !phoneVerify.getVersionUpgradeCode().isEmpty()) {

                users.setAppUpgradeVersionKey(phoneVerify.getVersionUpgradeCode());

            }

            // Check and Set FirebaseHashChatKey.
            if (phoneVerify.getFirebaseHashChatKey() != null && !phoneVerify.getFirebaseHashChatKey().isEmpty()) {
                users.setFirebaseChatUserHashId(phoneVerify.getFirebaseHashChatKey());
            }

            /* //Do match here the current location IF MAtched with the Restaurant then only do entry in the DB
             enterUserInFirebaseDataBase();   For now removed:DB base entry will be from Home Fragment(To Shorten the code)*/

            MainActivity.m_activity.swapContentFragment(new HomeFragment(), "Home_Fragment", false);

        } else {

            Log.e("else", "else");

            MainActivity.m_activity.swapContentFragment(new Phone_Otp(), "Phone_Otp", false);
        }

    }
    private void enterUserInFirebaseDataBase() {

        FirebaseChatManager.getInstance().setUserPresenceStatus("Online"); // testing only

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

    private void phoneNoVerification(final String contactNumber) {
        if ((int) Build.VERSION.SDK_INT <= 23) {
            getTelephonyPermission();
        }
        // Setup Country Code
        Country_code = country_code_dummy + getCountryCodeFromCustomMethod();
        if (!ConnectivityReceiver.isConnected()) {
            Toast.makeText(getActivity(), "Internet connection not available", Toast.LENGTH_SHORT).show();
            return;
        }

        CommonMethods.getInstance().setProgressDialog(getActivity());
        phone_number = contactNumber;
        Users users = new Users(getActivity());
        users.setPhone(phone_number);
        add_concat = Country_code + phone_number;

        Log.e("phnoneNumber", "phnoneNumber"+add_concat);

        /***********send this phone number to Receiver otp fragment*******/
        MainActivity.m_activity.set_data_fb("PhoneNo", add_concat);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(add_concat, 60, TimeUnit.SECONDS, getActivity(), new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                Log.e("VerificationCompletedL", "VerificationCompletedL:"+phoneAuthCredential);


              // crash found here on Le2
                //Toast.makeText(getContext(), "Verification Code not received!", Toast.LENGTH_SHORT).show();

                try{
                Toast.makeText(getContext(), "Verification Code not received!", Toast.LENGTH_SHORT).show();}
                catch(Exception e){
                    e.printStackTrace();
                }
                //Log.e("code","code"+phoneAuthCredential.getSmsCode());

                CommonMethods.getInstance().dismissProgressDialog();

            }
            @Override
            public void onVerificationFailed(FirebaseException e) {

                // Found the bug here
                Log.e("onVerificationFailed", "onVerificationFailed " + e);
                CommonMethods.getInstance().dismissProgressDialog();

                Toast.makeText(getContext(), "Invalid Number!Try again", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {


                Log.d(TAG, "onCodeSent: s");

               // super.onCodeSent(s, forceResendingToken);


                // Use foreceResendingToken object for resend the code laters ********************
                mResendToken = forceResendingToken;

                Log.e(TAG, "onCodeSent: " + mResendToken);

                ReceiveOtp fragment = new ReceiveOtp();

                Bundle bundle = new Bundle();

                bundle.putString("ResendOTP", mResendToken.toString());

                fragment.setArguments(bundle);
                // ********************
                MainActivity.m_activity.set_data_fb("mResendToken", mResendToken.toString());

                verfication_code = s;

                MainActivity.m_activity.set_data_fb("verification", verfication_code);

                // mResendToken=forceResendingToken;onResume

                Toast.makeText(getContext(), "Verification Code has been sent to your number", Toast.LENGTH_SHORT).show();

                phone_number = contactNumber;
                MainActivity.m_activity.set_data_fb("register_number", phone_number);
                CommonMethods.getInstance().dismissProgressDialog();
                MainActivity.m_activity.swapContentFragment(fragment, "ReceiveOtp", false);
            }
        });

    }

    /*
     * Request Telephony permission,
     * The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
    private void getTelephonyPermission() {

        if (ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {

            mTelephonyPermissionGranted = true;

            Country_code = country_code_dummy + getCountryCodeFromCustomMethod();

        } else {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }
    }

    /**
     * Handles the result of the request for Read Phone state permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mTelephonyPermissionGranted = false;
        switch (requestCode) {

            case PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                Log.e("grant", "=" + grantResults.length);
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mTelephonyPermissionGranted = true;
                }
            }
            Country_code = country_code_dummy + getCountryCodeFromCustomMethod();
            Log.e("Country_codegrant", Country_code);
        }
        // Permissions granted
    }

    private String getCountryCodeFromCustomMethod() {
        String countryLocale = "";

        if (getContext().checkCallingOrSelfPermission(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

        }

        TelephonyManager tm = (TelephonyManager) getContext().getApplicationContext()
                .getSystemService(Context.TELEPHONY_SERVICE);

        Log.d(TAG, "getCountryCodeFromCustomMethod: " + tm);

        countryLocale = tm.getSimCountryIso().toUpperCase();

        Locale l = new Locale("", countryLocale);
        String country = l.getDisplayCountry();


        String countryZipCode = "";

        String[] rl = getResources().getStringArray(R.array.CountryCodes);

        for (int i = 0; i < rl.length; i++) {

            String[] g = rl[i].split(",");

            if (g[1].trim().equals(countryLocale.trim())) {
                countryZipCode = g[0];
                break;
            }
        }

        return countryZipCode;
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

}



