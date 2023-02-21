package com.bingle.ameba.bingle_bar.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bingle.ameba.bingle_bar.R;
import com.bingle.ameba.bingle_bar.activities.MainActivity;
import com.bingle.ameba.bingle_bar.common_functions.CommonMethods;
import com.bingle.ameba.bingle_bar.common_functions.Constants;
import com.bingle.ameba.bingle_bar.common_functions.Users;
import com.bingle.ameba.bingle_bar.receivers.ConnectivityReceiver;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pojo.RestuDetailBean;

import static com.bingle.ameba.bingle_bar.fragments.LoginFragment.mGoogleApiClient;

/**
 * Created by ameba on 12/2/18.
 */
public class Phone_Otp extends Fragment {

    private static final String TAG = "Phone_Otp";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;
    public static String verfication_code;
    String Country_code;
    // ApiInterface apiService;
    //   ApiInterface apiService1;
    // ProgressBar simpleProgressBar;
    String abb, number, abbc;
    String country_code_dummy = "+";
    String add_concat;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    List list = new ArrayList();
    ArrayAdapter arrayAdapter;
    TextView txtCountry;
    List <RestuDetailBean> beanList;
    RelativeLayout relativeLayout;
    CountDownTimer CDT;
    CommonMethods commonMethods = new CommonMethods();
    @BindView(R.id.img_flag)
    ImageView imgFlag;
    //private TextView txt_email;
    private Button send_otp_obj;
    private EditText phone_obj;
    private String phone_number;
    private Button forward_icon_obj;
    private String arr[] = {"one", "two", "three"};
    private RelativeLayout phone_otp_design;
    private AlertDialog alert11;
    private boolean mTelephonyPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private ProgressDialog mProgressDialog;



    public static void onBackPressed() {
        /*
        if (Constants.loginType == "Fb") {
            LoginManager.getInstance().logOut();
            Log.e(TAG, "onBackPressed: fb");

        } else {
            Log.e(TAG, "onBackPressed: gmail");
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback <Status>() {
                        @Override
                        public void onResult(Status status) {

                            // ...
                            // users.remove();
                            //  Toast.makeText(getContext(),"Logged Out google",Toast.LENGTH_SHORT).show();

                        }
                    });
        }
        */
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.phone_otp, container, false);

        String aaa = MainActivity.m_activity.get_data_fb("fb_name");
        Log.e(TAG, "onCreateView: " + aaa);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String myInt = bundle.getString("fb_name");

            Log.e(TAG, "onCreateView: " + myInt);
        }


        ButterKnife.bind(this, view);
        phone_obj = (EditText) view.findViewById(R.id.phone_edtxt_id);
        txtCountry = (TextView) view.findViewById(R.id.txt_country);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());


        if ((int) Build.VERSION.SDK_INT <= 23) {
            getTelephonyPermission();
        }


        // Setup Country Code
        Country_code = country_code_dummy + getCountryCodeFromCustomMethod();

        beanList = new ArrayList <>();
        //apiService = APIClientCountry.getClient().create(ApiInterface.class);

        phone_otp_design = (RelativeLayout) view.findViewById(R.id.phone_otp_design);

        relativeLayout = (RelativeLayout) view.findViewById(R.id.phone_otp_design);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                }
            }

        });


        forward_icon_obj = (Button) view.findViewById(R.id.forward_icon);


        forward_icon_obj.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                if (!ConnectivityReceiver.isConnected()) {

                    Toast.makeText(getActivity(), "Internet connection not available", Toast.LENGTH_SHORT).show();

                    return;
                }

                commonMethods.setProgressDialog(getActivity());

                phone_number = phone_obj.getText().toString();
                Users users = new Users(getActivity());
                users.setPhone(phone_number);
                add_concat = Country_code + phone_number;


                Log.d(TAG, "phone_number: " + phone_number);
                Log.d(TAG, "adddxsdsddsd: " + add_concat);

                /***********send this phone number to Receiver otp fragment*******/
                MainActivity.m_activity.set_data_fb("PhoneNo", add_concat);

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        add_concat,
                        60,
                        TimeUnit.SECONDS,
                        getActivity(),
                        mCallbacks
                );
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential var1) {
                Log.e("onVerificationCompleted", "onVerificationCompleted" + var1);
                //   signInWithPhoneAuthCredential(var1);

                     signInWithPhoneAuthCredential(var1);

                     commonMethods.dismissProgressDialog();




            }
            @Override
            public void onVerificationFailed(FirebaseException var1) {

                // Found the bug here
                Log.e("onVerificationFailed", "onVerificationFailed " + var1);
                commonMethods.dismissProgressDialog();

                // Found the issue here. No simplePregressBar Found.
                //simpleProgressBar.setVisibility(View.GONE);
                txtCountry.setEnabled(true);
                phone_obj.setEnabled(true);
                Toast.makeText(getContext(), "Invalid Number!Try again", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

              //  super.onCodeSent(s, forceResendingToken);

                Log.e("ccccccc", "code sent " + s);


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

                    // mResendToken=forceResendingToken;

                    Toast.makeText(getContext(), "Verification Code has been sent to your number", Toast.LENGTH_SHORT).show();

                    phone_number = phone_obj.getText().toString();

                    MainActivity.m_activity.set_data_fb("register_number", phone_number);

                    commonMethods.dismissProgressDialog();

                    MainActivity.m_activity.swapContentFragment(fragment, "ReceiveOtp", true);


            }
        };


        //W Bf
        Log.e("Login type=", "Loginis=" + Constants.loginType);

        if (Constants.loginType == "Fb") {

            getfacebookResponse();

        } else {

            Log.e("else=", "else=" + Constants.loginType);
            String g_name = MainActivity.m_activity.get_data_fb("display_name");

            commonMethods.splitData(g_name);
            Log.e("g_name=", "g_name=" + g_name);
            String g_pic = MainActivity.m_activity.get_data_fb("display_pic");
            String g_email = MainActivity.m_activity.get_data_fb("display_email");

        }
        return view;
    }

    private void getfacebookResponse() {
        String getfacebookResponse = MainActivity.m_activity.get_data_fb("fb_name");

        Log.e(TAG, "getfacebookResponse: " + getfacebookResponse);

        commonMethods.splitData(getfacebookResponse);
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

    @OnClick({R.id.txt_country})
    public void onClk(View view) {
        switch (view.getId()) {
            case R.id.txt_country:
                // countryDialog();
                break;
        }
    }


    private void getCountryFlag(String countryNameValue, String countryCodeValue) {

        String fName = "";

        txtCountry.setText(countryNameValue);

        MainActivity.m_activity.set_data_fb("country", txtCountry.getText().toString());

        fName = countryCodeValue.toLowerCase();

        String uri = "";

        if (fName.equalsIgnoreCase("do")) {

            uri = "@drawable/" + "doo";

        } else {

            uri = "@drawable/" + fName;
        }
        try {
            int imageResource = getActivity().getResources().getIdentifier(uri, null, getActivity().getPackageName());
            Drawable res = getActivity().getResources().getDrawable(imageResource);
            imgFlag.setImageDrawable(res);
        } catch (Exception e) {
        }
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

        Log.e("countryLocale", "countryLocale: " + "countryLocale" + countryLocale);


        getCountryFlag(country, countryLocale);


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
            Log.e("Country_codeIf", Country_code);

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

    // trial method
  public void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
      Log.e("inside", "signInWithCredential:success");
      FirebaseAuth mAuth;
      mAuth = FirebaseAuth.getInstance();
      mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e("success", "signInWithCredential:success"+credential.getSmsCode());


                            FirebaseUser user = task.getResult().getUser();

                            Log.e("success", "signInWithCredential:success"+user);

                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("failure", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

}
