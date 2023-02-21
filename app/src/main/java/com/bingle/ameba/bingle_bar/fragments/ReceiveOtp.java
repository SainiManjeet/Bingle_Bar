package com.bingle.ameba.bingle_bar.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
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
import com.bingle.ameba.bingle_bar.common_functions.Users;
import com.bingle.ameba.bingle_bar.common_functions.firebase_chat_managers.FirebaseChatManager;
import com.bingle.ameba.bingle_bar.receivers.ConnectivityReceiver;
import com.facebook.login.LoginManager;
import com.goodiebag.pinview.Pinview;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import pojo.PhoneVerify;

import static com.bingle.ameba.bingle_bar.fragments.LoginFragment.mGoogleApiClient;
import static com.google.android.gms.internal.zzahn.runOnUiThread;

/**
 * Created by ameba on 13/2/18.
 */

public class ReceiveOtp extends Fragment implements ServicesResponse {

    private static final String TAG = "ReceiveOtp";
    static int count = 1;
    Pinview pin;
    TextView user_obj, welcome_obj, resend_otp, otp_obj;
    String verification, total_value, phoneNo, ResendOTP;
    Button submit_obj;
    Animation animBounce;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    @BindView(R.id.lin_parent_layout)
    LinearLayout parentLayout;
    String gmail_name1 = MainActivity.m_activity.get_data_fb("splited");
    String add_concat = MainActivity.m_activity.get_data_fb("PhoneNo");
    private CommonMethods commonMethods;

    public static void onBackPressed() {

        if (Constants.loginType == "Fb") {

            LoginManager.getInstance().logOut();
            Log.e(TAG, "onBackPressed: fb");

        } else {

            Log.e(TAG, "onBackPressed: gmail");

            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {


                        }
                    });
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recived_otp, container, false);
        //Handle Network Thread Run time Exceptions
        manageNetworkThread();
        resend_otp = view.findViewById(R.id.resend_otp);
        welcome_obj = view.findViewById(R.id.welcome_id);
        otp_obj = view.findViewById(R.id.otp_id);
        CommonMethods.getInstance().setFontGray(otp_obj);

        animBounce = AnimationUtils.loadAnimation(getContext(),
                R.anim.bounce);

        phoneNo = MainActivity.m_activity.get_data_fb("PhoneNo");
        Bundle bundle = getArguments();
        if (bundle != null) {
            ResendOTP = bundle.getString("ResendOTP");
            Log.e(TAG, "ResendOTP: " + ResendOTP);
        }


        welcome_obj.setText(Html.fromHtml("<u>WELCOME</u>"));

        CommonMethods.getInstance().setFontGray(welcome_obj);


        ButterKnife.bind(this, view);
        if (MainActivity.m_activity.isLocationEnabled()) {
        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

        //init pinview
        pin = (Pinview) view.findViewById(R.id.pinview);
        submit_obj = (Button) view.findViewById(R.id.forward_icon);
        CommonMethods.getInstance().hidekeyboard(view);
        commonMethods = CommonMethods.getInstance();

        user_obj = (TextView) view.findViewById(R.id.user_id);

        pin.requestFocus();

        //Set user Name(FB and Gmail)
        Users users = new Users(getActivity());
        user_obj.setText(users.getName());

        if (Constants.loginType == "Fb") {
            getfacebookResponse();
        } else {

            /*************get name from gmail****************/
            String gmail_name = MainActivity.m_activity.get_data_fb("display_name");
            commonMethods.splitData(gmail_name);
            CommonMethods.getInstance().setFontGray(user_obj);
            // user_obj.setText(gmail_name1);
        }


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.e("", "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w("", "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(commonMethods, "onFail", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                Log.e("", "verificationId:" + verificationId);

                verification = verificationId;
                mResendToken = token;
                Log.e(TAG, "mResendToken: " + mResendToken);
                commonMethods.dismissProgressDialog();

                Toast.makeText(getContext(), "Verification Code has been sent to your number", Toast.LENGTH_SHORT).show();
            }
        };

        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                commonMethods.setProgressDialog(getActivity());

                resend_otp.startAnimation(animBounce);


                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNo,        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        getActivity(),               // Activity (for callback binding)
                        mCallbacks,         // OnVerificationStateChangedCallbacks
                        mResendToken);

            }
        });

        submit_obj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!ConnectivityReceiver.isConnected()) {
                    Toast.makeText(commonMethods, "Internet connection not available", Toast.LENGTH_SHORT).show();
                    pin.setValue("0");

                } else {
                    commonMethods.setProgressDialog(getActivity());
                    total_value = pin.getValue().toString();



                    Log.d(TAG, "onClick: " + total_value);

                    if (total_value.length() == 6) {
                        try {
                            InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            im.hideSoftInputFromWindow(pin.getWindowToken(), 0);
                            String verification = MainActivity.m_activity.get_data_fb("verification");
                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification, total_value);

                            signInWithPhoneAuthCredential(credential);

                            //  AttemptOtp();

                        } catch (Exception e) {
                        }

                        // Pass the verificationId and code here and complete the phone verfication process by calling firebase verification method.
                        //  PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

                    } else {
                        Toast.makeText(getActivity(), "Please Enter 6 digit OTP code", Toast.LENGTH_SHORT).show();
                        commonMethods.dismissProgressDialog();
                        try {
                            InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            im.hideSoftInputFromWindow(pin.getWindowToken(), 0);
                        } catch (Exception e) {
                        }
                    }
                }
            }


        });


        return view;
    }
// attempt otp by user


    private void manageNetworkThread() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {

        Log.e("signInWith","signInWith");

        if (!ConnectivityReceiver.isConnected()) {

            Toast.makeText(getActivity(), "Internet connection not available", Toast.LENGTH_SHORT).show();

            return;
        }

        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            Log.e("task","task"+total_value);


                           // Log.e("code","code"+credential.getSmsCode());

                            Users users = new Users(getActivity());
                            users.setOtp(total_value);

                            commonMethods.dismissProgressDialog();

                            checkIsUserLoggedInWithLoginType();

                        } else {

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                                try {
                                    Users users = new Users(getActivity());
                                    users.setOtp("");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                commonMethods.dismissProgressDialog();

                                if (count <= 2) {
                                    Toast.makeText(commonMethods, "You have Attempt " + count + " time Invalid OTP", Toast.LENGTH_LONG).show();


                                    count++;
                                } else {

                                    Toast.makeText(commonMethods, "OTP has been Expired! Please click on Resend Button ", Toast.LENGTH_LONG).show();
                                }


                                //  Toast.makeText(getContext(), "Invalid OTP!", Toast.LENGTH_SHORT).show();

                                //For testing only remove it
                            }
                        }
                    }
                });
    }

    private void enterUserInFirebaseDataBase() {
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

    /*******************get name from fb********************/
    private void getfacebookResponse() {
        String name_fb = MainActivity.m_activity.get_data_fb("fb_name");
        commonMethods.splitData(name_fb);

        //user_obj.setText(gmail_name1);
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

    private void checkIsUserLoggedInWithLoginType() {

        CommonMethods.getInstance().createRetrofitBuilderWithHeader(getActivity());
        try {

            final HashMap<String, String> params = new HashMap<>();
            // CommonMethods.getInstance().sharePrefForLoginType(); //

            params.put("userName", MainActivity.m_activity.get_data_fb("display_name"));
            params.put("fullName", MainActivity.m_activity.get_data_fb("display_name"));
            params.put("gender", "");
            params.put("country", MainActivity.m_activity.get_data_fb("country").toUpperCase());
            params.put("contactNumber", MainActivity.m_activity.get_data_fb("register_number"));
            params.put("firebaseChatID", FirebaseAuth.getInstance().getUid());
            params.put("loginChoiceID", MainActivity.m_activity.get_data_fb("loginChoiceID"));
            params.put("loginchoiceUniqueID", MainActivity.m_activity.get_data_fb("loginchoiceUniqueID"));

            String userProfilePic = getProfilePic();
            Log.e("userProfilePic", "userProfilePic=" + userProfilePic);
            if (userProfilePic != null && !userProfilePic.isEmpty()) {
                URL url = null;
                try {
                    url = new URL(userProfilePic);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                final URL finalUrl = url;
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            Bitmap bmp = BitmapFactory.decodeStream(finalUrl.openConnection().getInputStream());
                            params.put("userProfilePicUrl", bitmapToBase64(bmp));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                });

            }


            CommonMethods.getInstance().callService(getActivity(), this, parentLayout, Constants.checkIsUserLoggedInWithLoginType, params);

        } catch (Exception e) {

            Log.e("reciveOtp" + "reciveOtp", "" + e.toString());

            Log.e("in_exception_" + Constants.checkIsUserLoggedInWithLoginType, "" + e.toString());
            CommonMethods.getInstance().dismissProgressDialog();
        }

    }

    @Override
    public void onResponseSuccess(Bean_CommonResponse response) throws JSONException {

        CommonMethods.getInstance().dismissProgressDialog();

        JSONObject jsonObject = response.getJsonObject();

        Boolean status = jsonObject.getBoolean("success");

        //accountStatus
        if (status) {

            Gson gson = new Gson();

            PhoneVerify phoneVerify = gson.fromJson(String.valueOf(jsonObject.getJSONObject("data")), PhoneVerify.class);

            if (phoneVerify.getAccountStatus() == true) {

                Users users = new Users(getActivity());
                users.setUserId(phoneVerify.getUserID());

                if (phoneVerify.getVersionUpgradeCode() != null && !phoneVerify.getVersionUpgradeCode().isEmpty()) {

                    users.setAppUpgradeVersionKey(phoneVerify.getVersionUpgradeCode());

                }

                if (phoneVerify.getFirebaseHashChatKey() != null && !phoneVerify.getFirebaseHashChatKey().isEmpty()) {

                    users.setFirebaseChatUserHashId(phoneVerify.getFirebaseHashChatKey());
                }

                enterUserInFirebaseDataBase();

                // Hack: Remove existing fragment from backstack. Need to find proper solution for this.
                getFragmentManager().popBackStack();

                thankYouPopUp();

                //MainActivity.m_activity.swapContentFragment(new Welcome_Fragment(), "Welcome_Fragment", false);

            } else {

                // Show dialogue that "Your account is in inactive state."
            }
        } else {

            Toast.makeText(getContext(), "Registration Failed!", Toast.LENGTH_SHORT).show();
        }
    }


    //method to convert the selected image to base64 encoded string
    private String bitmapToBase64(Bitmap bm) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;

    }
    private void thankYouPopUp(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        Users users = new Users(getActivity());
        alertDialogBuilder.setTitle("Hello".concat(" ").concat(users.getName()).concat(" Thank you for registering with Bingle"));
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        SubmitButton();
                    }
                });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

        parentLayout.setAlpha(1);
    }

    private void SubmitButton() {
        if (!ConnectivityReceiver.isConnected()) {
            Toast.makeText(commonMethods, "Internet connection not available", Toast.LENGTH_SHORT).show();
        } else {
            MainActivity.m_activity.swapContentFragment(new HomeFragment(), "Home_Fragment", false);
        }
    }
}
