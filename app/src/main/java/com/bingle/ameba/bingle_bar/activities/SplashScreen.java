package com.bingle.ameba.bingle_bar.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.bingle.ameba.bingle_bar.R;
import com.bingle.ameba.bingle_bar.common_functions.Bean_CommonResponse;
import com.bingle.ameba.bingle_bar.common_functions.Constants;
import com.bingle.ameba.bingle_bar.common_functions.ServicesResponse;
import com.google.android.gms.plus.Plus;

import org.json.JSONException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bingle.ameba.bingle_bar.fragments.LoginFragment.mGoogleApiClient;

/*public class SplashScreen extends AppCompatActivity {*/
public class SplashScreen extends Activity implements ServicesResponse {
    private static int SPLASH_TIME_OUT = 3000;
    String mHome = "";

    @BindView(R.id.video_view_sp)
    VideoView videoView;


    @BindView(R.id.parent)
    RelativeLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this, this);

    //Check set user initial availability
      /*  Users users = new Users(this);
        users.setGpsLocation("false");*/

        try {
            // pushNotificationCall(FirebaseInstanceId.getInstance().getToken());

            PackageInfo info = getPackageManager().getPackageInfo("com.bingle.ameba.bingle_bar", PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("MY KEY HASH:", "hash" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        Constants.androidId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.e("androidId==", "androidId===" + Constants.androidId);
        Log.e("splash==", "InSplah");

        String path = "android.resource://" + getPackageName() + "/" + R.raw.splash_v;
        videoView.setVideoURI(Uri.parse(path));
        videoView.start();

        //  getCountry(); // Recheck this vineet haev comment this code

        try {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        } catch (Exception e) {
        }

        // else {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();


            }
        }, SPLASH_TIME_OUT);
        //  }
    }


    @Override
    public void onResponseSuccess(Bean_CommonResponse response) throws JSONException {

    }
    //DateFormat dateFormat = new SimpleDateFormat("hh:mm a");

    public CharSequence createDate(long timestamp) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestamp);
        Date d = c.getTime();
        //  SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        return sdf.format(d);
    }

}

