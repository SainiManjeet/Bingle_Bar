package chat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bingle.ameba.bingle_bar.R;
import com.bingle.ameba.bingle_bar.common_functions.Users;
import com.bingle.ameba.bingle_bar.common_functions.firebase_chat_managers.FirebaseChatManager;
import com.bingle.ameba.bingle_bar.common_functions.firebase_chat_managers.FirebaseResponseListner;
import com.bingle.ameba.bingle_bar.receivers.ConnectivityReceiver;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import adapters.ChatAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pojo.UserList;

import static com.bingle.ameba.bingle_bar.common_functions.Constants.chatWindowActive;

public class Chat extends AppCompatActivity {
    LinearLayout layout;
    ImageView sendButton, profile_pic, map_icon_obj, sett_obj, back_obj;
    EditText messageArea;
    ScrollView scrollView;
    TextView txtUserName, txtStatus;
    UserList selectedUserData;
    String channelId;
    Toolbar toolbar_obj;

    @BindView(R.id.recycler_chat)
    RecyclerView recyclerView;

    @BindView(R.id.lin_profile)
    LinearLayout linProfile;

    @BindView(R.id.user_pic)
    ImageView userPic;

    List <UserList> beanList1;

    String FcmToken, blockStatus, blockedBy, userHashChatId, currentUserBlockedBy;

    Bundle bundle;
    private MenuItem itemToHide;
    private MenuItem itemToShow;


    //private int blockSt=0;

    private int blockSt = 2; // 2 means first time user come here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fcm_activity_chat);

        Log.e("blockStatus", "blockStatus" + blockStatus);

        chatWindowActive = "true"; // to check wether user is active or not

        ButterKnife.bind(this, this);

        beanList1 = new ArrayList <>();
        linProfile.setVisibility(View.VISIBLE);
        userPic.setVisibility(View.VISIBLE);
        setupView();
        getUserObjectFromIntentBundle();
        isBlocked();

        map_icon_obj.setVisibility(View.GONE);
        sett_obj.setVisibility(View.GONE);
        back_obj.setVisibility(View.VISIBLE);
        profile_pic.setVisibility(View.VISIBLE);
        txtUserName.setVisibility(View.VISIBLE);
        txtStatus.setVisibility(View.VISIBLE);

        setSupportActionBar(toolbar_obj);

        back_obj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chat.this.finish();
            }
        });

        //get data from FCM Database
        Firebase.setAndroidContext(this);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check user block status

                Log.e("blockStatusF*", "blockStatusF*" + blockStatus);

                Log.e("currentUserBlockedByP", "currentUserBlockedByP" + currentUserBlockedBy + "selectedId" + userHashChatId);

                Log.e("sttttt", "sttttt" + blockSt);

                if (blockStatus != null && !blockStatus.isEmpty() && blockStatus.equalsIgnoreCase("1") || blockSt == 1) {
                    Toast.makeText(Chat.this, "You can't reply to this conversation", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (currentUserBlockedBy != null && !currentUserBlockedBy.isEmpty() && currentUserBlockedBy.equalsIgnoreCase(userHashChatId) || blockSt == 1) {
                    Toast.makeText(Chat.this, "Can't message!You are Blocked by this user", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Send Message
               /* String messageText = messageArea.getText().toString().trim();
                if (!messageText.equals("") && !messageText.isEmpty() && messageText.length() > 0) {
                    checkInternetConnection();
                    //Crash Found here June 11(User:Rose Mann)
                    Log.e("channelId", "channelId" + channelId);
                    FirebaseChatManager.getInstance().sendChatMessageInChannelId(channelId, FirebaseChatManager.getInstance().getCurrentUser(), messageText, getCurrentTime());//added June11
                }
                messageArea.setText("");*/


                String messageText = messageArea.getText().toString().trim();
                if (!messageText.equals("") && !messageText.isEmpty() && messageText.length() > 0) {
                    checkInternetConnection();
                    //Crash Found here June 11(User:Rose Mann)
                    Log.e("channelId", "channelId" + channelId);
                    FirebaseChatManager.getInstance().sendChatMessageInChannelId(channelId, FirebaseChatManager.getInstance().getCurrentUser(), messageText, getCurrentTime());//added June11
                }
                messageArea.setText("");


                /*if(blockSt==0){
                    if (!messageText.equals("") && !messageText.isEmpty() && messageText.length() > 0) {
                        checkInternetConnection();
                        //Crash Found here June 11(Rose Mann)
                        Log.e("channelId","channelId"+channelId);
                        FirebaseChatManager.getInstance().sendChatMessageInChannelId(channelId, FirebaseChatManager.getInstance().getCurrentUser(), messageText);
                    }
                    messageArea.setText("");
                }*/


            }
        });


    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        if (isBlocked()) {
            itemToHide = menu.findItem(R.id.block);
            itemToShow = menu.findItem(R.id.unblock);

            itemToHide.setVisible(false);
            itemToShow.setVisible(true);
        } else {
            itemToHide = menu.findItem(R.id.unblock);
            itemToShow = menu.findItem(R.id.block);

            itemToHide.setVisible(false);
            itemToShow.setVisible(true);
        }

        return true;
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        if (isBlocked()) {
            itemToHide = menu.findItem(R.id.block);
            itemToShow = menu.findItem(R.id.unblock);

            itemToHide.setVisible(false);
            itemToShow.setVisible(true);
        } else {
            itemToHide = menu.findItem(R.id.unblock);
            itemToShow = menu.findItem(R.id.block);

            itemToHide.setVisible(false);
            itemToShow.setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
           /* case R.id.block:
               if (isBlocked()) {
                    Toast.makeText(this, "unblocked selected", Toast.LENGTH_SHORT)
                            .show();
                    unblockUserPopUp();
                }
                break;
            case R.id.unblock:
                if (!isBlocked()) {
                    Toast.makeText(this, "blocked selected", Toast.LENGTH_SHORT)
                            .show();
                    blockUserPopUp();

                }
                break;
                */


            //2

                /*if (isBlocked()) {
                    Toast.makeText(this, "" +
                            "do unblocked", Toast.LENGTH_SHORT)
                            .show();
                    unblockUserPopUp();
                }
                else{
                    Toast.makeText(this, "do block", Toast.LENGTH_SHORT)
                            .show();
                    blockUserPopUp();
                }*/


            //2
            case R.id.block:
                blockUserPopUp();
                break;
            case R.id.unblock:
                unblockUserPopUp();
                break;

            default:
                break;
        }

        return true;

    }

    public void checkInternetConnection() {

        if (!ConnectivityReceiver.isConnected()) {

            Toast.makeText(Chat.this, "Internet connection not available", Toast.LENGTH_SHORT).show();

            return;
        }
    }

    public void getUserObjectFromIntentBundle() {

        //Get BlockedBy data from intent(Via Chat class)
        Intent intent = getIntent();
        currentUserBlockedBy = intent.getStringExtra("currentUserBlockedBy");
        Log.e("currentUserBlockedBy", "currentUserBlockedBy" + currentUserBlockedBy);

        bundle = getIntent().getExtras();

        selectedUserData = (UserList) bundle.getSerializable("user_obj");

        FcmToken = selectedUserData.getFcmToken();

        Log.e("FcmToken1", "FcmToken1" + FcmToken);

        blockStatus = selectedUserData.getBlockedStatus();
        blockedBy = selectedUserData.getBlockedBy();
        userHashChatId = selectedUserData.getUserId();

        txtUserName.setText(selectedUserData.getUserName());
        txtStatus.setText(selectedUserData.getUserStatus());

        //Disable Message Send Button
        if (selectedUserData.getUserStatus().equalsIgnoreCase("Offline")) {
            sendButton.setEnabled(false);
            AlphaAnimation alpha = new AlphaAnimation(0.5F, 0.5F);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            sendButton.startAnimation(alpha);
        }

        if (selectedUserData.getUserPic() != null && !selectedUserData.getUserPic().isEmpty() && !selectedUserData.getUserPic().equals("NOT_FOUND")) {

            Glide.with(getApplicationContext()).load(selectedUserData.getUserPic())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profile_pic);

        } else {
            Glide.with(getApplicationContext()).load(R.drawable.com_facebook_profile_picture_blank_portrait)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profile_pic);
        }


        try {

            channelId = bundle.getString("ChannelId");

            if (channelId == null || channelId.isEmpty()) {

                listenForChannelIdOfThisFriend();

            } else {


                getChattingList();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void getChattingList() {
        checkInternetConnection();
        FirebaseChatManager.getInstance().getChatMessagesWithChannelId(channelId, new FirebaseResponseListner() {
            @Override
            public void onCompleted(final Object firebaseResponseData) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        //TODO: update your UI
                        try {
                            if (((JSONObject) firebaseResponseData).getString("SenderId").equals(FirebaseChatManager.getInstance().getCurrentUser())) {

                                addMessageBox(((JSONObject) firebaseResponseData).getString("Message"), 1, FcmToken, ((JSONObject) firebaseResponseData).getString("MessageTime"));

                            } else {

                                addMessageBox(((JSONObject) firebaseResponseData).getString("Message"), 2, FcmToken, ((JSONObject) firebaseResponseData).getString("MessageTime"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                });
            }
        });
    }


    public void listenForChannelIdOfThisFriend() {

        checkInternetConnection();

        FirebaseChatManager.getInstance().listenForChannelWithFriendId(selectedUserData.getUserId(), new FirebaseResponseListner() {

            @Override
            public void onCompleted(Object firebaseResponseData) {

                try {

                    channelId = ((JSONObject) firebaseResponseData).getString("ChannelId");
                    Log.e("channelIdTry", "channelIdTry" + channelId);

                    getChattingList();

                } catch (JSONException e) {
                    Log.e("catch", "catch" + e.toString());
                    e.printStackTrace();
                }
            }
        });
    }

    public void addMessageBox(String message, int type, String FcmToken, String messageTime) {
        Log.e("type i", "type" + FcmToken);

        beanList1.add(new UserList(type, message, FcmToken, messageTime));//added June11

        Users users = new Users(Chat.this);

        Log.e("FcmToken", "FcmToken" + FcmToken);
        if (FcmToken != null && !FcmToken.isEmpty() && type != 2) {
            pushNotificationCall(FcmToken, message, users.getName());
        }
        setAdapter();
    }

    private void setupView() {
        layout = (LinearLayout) findViewById(R.id.layout1);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        map_icon_obj = (ImageView) findViewById(R.id.map_icon_id);
        profile_pic = (ImageView) findViewById(R.id.user_pic);
        sett_obj = (ImageView) findViewById(R.id.sett);
        back_obj = (ImageView) findViewById(R.id.back);

        toolbar_obj = (Toolbar) findViewById(R.id.toolbar_id);

        txtUserName = (TextView) findViewById(R.id.txt_name);
        messageArea = (EditText) findViewById(R.id.messageArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        txtStatus = (TextView) findViewById(R.id.txt_status);

    }

    private void setAdapter() {
        recyclerView.removeAllViewsInLayout();
        recyclerView.setAdapter(null);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        ChatAdapter myListAdapter = new ChatAdapter(this, beanList1);
        recyclerView.setAdapter(myListAdapter);
        recyclerView.scrollToPosition(beanList1.size() - 1);
    }

    //Send Push Notification
    private void pushNotificationCall(final String token, final String message, final String userName) {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {

                    Log.e("pushNotificationCall", "pushNotificationCall" + token);
                    OkHttpClient client = new OkHttpClient();
                    MediaType mediaType = MediaType.parse("application/json");

                    RequestBody body = RequestBody.create(mediaType, "{\n  \"to\" : \"" + token + "\",\n  \"notification\" : {\n    \"body\" : \"" + message + "\",\n    \"title\" : \"" + userName + "\"\n    }\n}");

                    Request request = new Request.Builder()
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .addHeader("authorization", "key=AAAAx_ZE8Rk:APA91bGJCtFSvs6nL-_larRg1Fuhjsts_dM5nI7i2OXgKjzDgb_2k8faP3XpIVFddPZkz_VrlEpxNJIBqdXfSojQyt99mx7vSKbrJyawY-jLo0hB7-xyxcYTT3tbOPcz6eL-wITnxmB0")
                            .addHeader("content-type", "application/json")
                            .addHeader("cache-control", "no-cache")
                            .build();
                    Log.e("request", "request" + request.toString());
                    try {

                        Response response = client.newCall(request).execute();
                        Log.e("response", "response" + response);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void blockUserPopUp() {
        LayoutInflater layoutInflater = LayoutInflater.from(Chat.this);
        View promptView = layoutInflater.inflate(R.layout.block_user, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Chat.this);
        alertDialogBuilder.setTitle("Block User");

        alertDialogBuilder.setMessage("Are you sure you want to block this user?");
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Users users = new Users(Chat.this);
                        blockUser(userHashChatId, "1", users.getFirebaseChatUserHashId());

                        refreshChat();


                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    //Block user using Firebase
    public void blockUser(String channelId, String blockStatus, String blockedBy) {
        blockSt = 1;
        //Block user(add and update Block Status and Blocked by)
        FirebaseChatManager.getInstance().setUserBlockStatus(channelId, blockStatus, blockedBy);
        Toast.makeText(Chat.this, "User has been Blocked!" + blockSt, Toast.LENGTH_SHORT).show();


    }

    private void unblockUserPopUp() {
        LayoutInflater layoutInflater = LayoutInflater.from(Chat.this);
        View promptView = layoutInflater.inflate(R.layout.block_user, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Chat.this);
        alertDialogBuilder.setTitle("Block User");

        alertDialogBuilder.setMessage("Are you sure you want to unblock this user?");
        alertDialogBuilder.setView(promptView);

        //  final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        unblockUser(userHashChatId, "", "");

                        refreshChat();

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    //unblock user using Firebase
    private void unblockUser(String channelId, String blockStatusm, String blockedBy) {
        blockSt = 0;

        //Block user(add and update Block Status and Blocked by)
        FirebaseChatManager.getInstance().setUserBlockStatus(channelId, blockStatusm, blockedBy);
        blockStatus = "";
        Toast.makeText(Chat.this, "User has been unblocked!" + blockSt, Toast.LENGTH_SHORT).show();

    }

    private boolean isBlocked() {
        boolean isBlockedUser = false;
        if (blockStatus != null && !blockStatus.isEmpty() && blockStatus.equalsIgnoreCase("1")) {
            isBlockedUser = true;
        }
        if (currentUserBlockedBy != null && !currentUserBlockedBy.isEmpty() && currentUserBlockedBy.equalsIgnoreCase(userHashChatId)) {
            isBlockedUser = true;
        }
        return isBlockedUser;
    }

    private void refreshChat() {
       /* finish();
        startActivity(getIntent());*/
    }

    private String getCurrentTime() {
        String currentDateAndTime;
        currentDateAndTime = String.valueOf(new Date().getTime());
        return currentDateAndTime;
    }
}
