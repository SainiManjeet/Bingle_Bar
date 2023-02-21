package com.bingle.ameba.bingle_bar.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bingle.ameba.bingle_bar.R;
import com.bingle.ameba.bingle_bar.common_functions.Bean_CommonResponse;
import com.bingle.ameba.bingle_bar.common_functions.CommonMethods;
import com.bingle.ameba.bingle_bar.common_functions.Constants;
import com.bingle.ameba.bingle_bar.common_functions.ServicesResponse;
import com.bingle.ameba.bingle_bar.common_functions.Users;
import com.bingle.ameba.bingle_bar.receivers.ConnectivityReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by vineet on 15/5/18.
 */

public class SendFeedback extends Fragment implements ServicesResponse {
    private static final String TAG = "SendFeedback";
    @BindView(R.id.lin_main2)
    LinearLayout parentLayout2;
    String feedback_data;
    Users users;
    CommonMethods commonMethods = new CommonMethods();
    private ImageView map_icon_obj, drawer_icon;
    private Toolbar toolbar_obj;
    private DrawerLayout drawer_obj;
    private TextView send_feedback_obj, Send_feedback_content_obj, send_feedback_content2_obj, send_feedback_content3;
    private Button submit;
    private EditText feedback_obj;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sendfeedback, container, false);

        //Casting
        map_icon_obj = view.findViewById(R.id.map_icon_id);
        drawer_icon = view.findViewById(R.id.sett);
        drawer_obj = (DrawerLayout) view.findViewById(R.id.drawerlayout_id);
        send_feedback_obj = (TextView) view.findViewById(R.id.send_feedback_id);
        Send_feedback_content_obj = (TextView) view.findViewById(R.id.send_feedback_content);
        send_feedback_content2_obj = (TextView) view.findViewById(R.id.send_feedback_content2);
        //Gmail Link
        send_feedback_content3 = (TextView) view.findViewById(R.id.send_feedback_content3);
        feedback_obj = (EditText) view.findViewById(R.id.edt_comment);
        submit = (Button) view.findViewById(R.id.Submit);
        users = new Users(getActivity());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ConnectivityReceiver.isConnected()) {
                    Toast.makeText(commonMethods, "Internet connection not available", Toast.LENGTH_SHORT).show();
                } else {
                    feedback_data = feedback_obj.getText().toString();
                    if (feedback_obj.getText().toString().length() > 0) {
                        callFeedbackApi();
                        feedback_obj.setText("");
                    } else {
                        Toast.makeText(commonMethods, "Please enter your feedback!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        send_feedback_content3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFeedback();
            }
        });

        //set font
        commonMethods = CommonMethods.getInstance();
        commonMethods.setFont(send_feedback_obj);
        commonMethods.setFontGray(submit);

        //visiblity
        map_icon_obj.setVisibility(View.GONE);

        //clickListner
        drawer_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_obj.openDrawer(GravityCompat.START);
                try {
                    CommonMethods.getInstance().hidekeyboard(view);
                } catch (Exception e) {
                }

            }
        });

        return view;
    }

    private void callFeedbackApi() {
        String id = users.getUserId();
        saveAppFeedbackByUserId(id, feedback_data);
    }

    private void saveAppFeedbackByUserId(String userId, String feedback_data) {
        CommonMethods.getInstance().createRetrofitBuilderWithHeader(getActivity());

        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("UserID", userId);
            params.put("Feedback", feedback_data);

            CommonMethods.getInstance().callService(getActivity(), this, parentLayout2, Constants.saveAppFeedbackByUserId, params);

        } catch (Exception e) {
            CommonMethods.getInstance().dismissProgressDialog();
        }
    }

    @Override
    public void onResponseSuccess(Bean_CommonResponse response) throws JSONException {

        CommonMethods.getInstance().dismissProgressDialog();
        JSONObject jsonObject = response.getJsonObject();

        Boolean status = jsonObject.getBoolean("success");

        if (status) {
            Toast.makeText(commonMethods, "Your Feedback added successfully", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(commonMethods, "Error", Toast.LENGTH_SHORT).show();

        }
    }

    private void sendFeedback() {
        // Open only Email Client
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:contact@bingleinc.com?subject=" + "" + "&body=" + "");
        intent.setData(data);
        startActivity(intent);
    }
}
