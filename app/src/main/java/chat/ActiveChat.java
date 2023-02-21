package chat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.bingle.ameba.bingle_bar.common_functions.CommonMethods;
import com.bingle.ameba.bingle_bar.common_functions.Constants;
import com.bingle.ameba.bingle_bar.common_functions.Users;
import com.bingle.ameba.bingle_bar.common_functions.firebase_chat_managers.FirebaseChatManager;
import com.bingle.ameba.bingle_bar.common_functions.firebase_chat_managers.FirebaseResponseListner;
import com.bingle.ameba.bingle_bar.receivers.ConnectivityReceiver;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import adapters.ActiveAdapter;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pojo.UserList;

import static com.google.android.gms.internal.zzahn.runOnUiThread;

/**
 * Created by ErAshwini on 12/6/18.
 */

public class ActiveChat extends Fragment {
    TextView noUsersText;
    int totalUsers = 0;

    @BindViews({R.id.sett, R.id.map_icon_id})
    List <ImageView> imgViews;


    @BindView(R.id.recycler_id)
    RecyclerView recyclerView;
    List <UserList> beanList1;

    @BindViews({R.id.active_user_id, R.id.user_list_id})
    List <Button> buttons;

    @BindView(R.id.lin_button)
    LinearLayout linLayoutButton;

    String RestaurantId, RestaurantName, fromView, myBlockBy;

    @BindViews({R.id.txt_restaurant_name, R.id.txt_connectivity})
    List <TextView> textViews;
    private DrawerLayout drawer_obj;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chat, container, false);

        ButterKnife.bind(this, v);

        beanList1 = new ArrayList <>();

        Log.e("ActiveChatM", "ActiveChatM");


        drawer_obj = (DrawerLayout) v.findViewById(R.id.drawerlayout_id2);

        imgViews.get(1).setVisibility(View.GONE);

        noUsersText = (TextView) v.findViewById(R.id.noUsersText);

        Constants.USER_TYPE = "Active";// rmv

        //@ manjeet updation
        fromView = "MainChat";

        if (fromView.equalsIgnoreCase("MainChat")) {

        } else {
            RestaurantId = "";
        }


        //Open Drawer
        imgViews.get(0).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (fromView.equalsIgnoreCase("Restaurant")) {
                    getFragmentManager().popBackStack();
                } else {
                    drawer_obj.openDrawer(GravityCompat.START);
                    try {
                        CommonMethods.getInstance().hidekeyboard(view);
                    } catch (Exception e) {
                    }
                }
            }
        });


        getUsersList();


        return v;
    }

    public void parseUsersDataNew(JSONObject dataValue) {

        Log.e("parseUsersDataNew", "parseUsersDataNew");

        CommonMethods.getInstance().dismissProgressDialog();

        ArrayList<String>chatKeyArray=new ArrayList <>();

        // Clear all the data from bean list so that fresh data can be loaded.
        beanList1.clear();
        Iterator i = dataValue.keys();
        String key = "";

        try {
            while (i.hasNext()) {

                key = i.next().toString();

                Users users = new Users(getActivity());

                JSONObject userData = dataValue.getJSONObject(key);

                // Get the Users List with whom the current user has done chat in past

                if (userData.getString("UserId").contains(users.getFirebaseChatUserHashId())) {

                    if (userData.has("ChatUsers")) {
                        Log.e("hasChat", "hasChat");
                        JSONObject ChatUsersObj = userData.getJSONObject("ChatUsers");
                        Iterator chatKey = ChatUsersObj.keys();

                        while (chatKey.hasNext()) {
                            chatKeyArray.add(String.valueOf(chatKey.next()));
                        }
                        Log.e("chatKeyArray", "chatKeyArray" + chatKeyArray.toString());

                        break;
                    }


                }
            }

            // Iterate through all the users in response and fill the chat tab with the appropiate users.
            while (i.hasNext()) {

                key = i.next().toString();

                Users users = new Users(getActivity());

                JSONObject userData = dataValue.getJSONObject(key);

                if (userData.getString("UserId").contains(users.getFirebaseChatUserHashId()) || chatKeyArray.size()==0) {
                    // Do not Add to Chat List
                    Log.e("BlockBy", "BlockBy" + userData.getString("BlockedBy"));
                    Log.e("FullName=", "FullName=" + userData.getString("FullName"));

                    myBlockBy = userData.getString("BlockedBy");
                }

                else if (chatKeyArray.contains(userData.getString("UserId"))){
                    Gson gson = new Gson();
                    UserList userList = gson.fromJson(String.valueOf(userData), UserList.class);

                    Log.e("CurrentUserHash=", "CurrentUserHash=" + users.getFirebaseChatUserHashId());
                    beanList1.add(userList);

                }

                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (totalUsers < 1) {
            noUsersText.setVisibility(View.VISIBLE);
        } else {
            noUsersText.setVisibility(View.GONE);
            ActiveUserData();
        }
    }


    private void ActiveUserData() {
        Log.e("in ActiveUserData=", "ActiveUserData=" + beanList1.size());


        recyclerView.removeAllViewsInLayout();
        recyclerView.setAdapter(null);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        ActiveAdapter myListAdapter = new ActiveAdapter(getActivity(), beanList1, RestaurantId, myBlockBy, "Chats");
        recyclerView.setAdapter(myListAdapter);
    }


    @OnClick({R.id.active_user_id, R.id.user_list_id})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_list_id:

                Log.e("All click", "All click" + beanList1.size());

                Constants.USER_TYPE = "All"; //trying do rmv

                Log.e("Constant1", "Constant1" + Constants.USER_TYPE);

                beanList1.clear();
                recyclerView.removeAllViewsInLayout();
                recyclerView.setAdapter(null);

                buttons.get(1).setBackgroundResource(R.drawable.chat_bd_button3);
                buttons.get(0).setBackgroundResource(R.drawable.chat_bg_button4);
                buttons.get(1).setTextColor(Color.parseColor("#FFFFFF"));
                buttons.get(0).setTextColor(Color.parseColor("#00b2ff"));

                if (fromView.equalsIgnoreCase("Restaurant")) {
                    Constants.USER_TYPE = "All";
                    // Constants.USER_TYPE="Active";
                    ////Open All User List
                    //getUsersList(Users.USER_LIST_REQUEST_TYPE.WITH_RESTAURANT_ID);
                    getUsersList(USER_LIST_REQUEST_TYPE.WITH_RESTAURANT_ID);
                } else {
                    Constants.USER_TYPE = "All";
                    ////Open All User List
                    getUsersList(USER_LIST_REQUEST_TYPE.ALL_USERS);

                }
                break;

            case R.id.active_user_id:
                Log.e("Active click", "Active click");

                Constants.USER_TYPE = "Active";

                Log.e("Constant1", "Constant1" + Constants.USER_TYPE);

                beanList1.clear();
                recyclerView.removeAllViewsInLayout();
                recyclerView.setAdapter(null);
                buttons.get(0).setBackgroundResource(R.drawable.chat_bg_button);
                buttons.get(1).setBackgroundResource(R.drawable.chat_bg_button2);
                buttons.get(0).setTextColor(Color.parseColor("#FFFFFF"));
                buttons.get(1).setTextColor(Color.parseColor("#00b2ff"));

                if (fromView.equalsIgnoreCase("Restaurant")) {
                    Constants.USER_TYPE = "Active";
                    // Need to apply case here, i.e. show only active users of particular restaurant.
                    ////Open All User List
                    getUsersList(USER_LIST_REQUEST_TYPE.ACTIVE_WITH_RESTAURANT_ID);

                } else {
                    Constants.USER_TYPE = "All";
                    ////Open All User List
                    getUsersList(USER_LIST_REQUEST_TYPE.ALL_USERS);
                }

        }
    }

    public void getUsersList() {
        ////Open All User List
        getUsersList(USER_LIST_REQUEST_TYPE.ALL_USERS);

    }


    public void getUsersList(USER_LIST_REQUEST_TYPE requestTypeValue) {

        if (!ConnectivityReceiver.isConnected()) {

            Toast.makeText(getActivity(), "Internet connection not available", Toast.LENGTH_SHORT).show();

            return;
        }

        // New Get Users Firebase Request
        // CommonMethods.getInstance().setProgressDialog(getActivity(), "Loading Users...", true);
   /*    // progress dialog timmer
        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                CommonMethods.getInstance().dismissProgressDialog();
            }
        };
        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 2000);*/


        if (requestTypeValue == USER_LIST_REQUEST_TYPE.ALL_USERS) {

            //Get All Chat Ids for Current User


            FirebaseChatManager.getInstance().getUsersListWithBlock(new FirebaseResponseListner() {
                @Override
                public void onCompleted(final Object firebaseResponseData) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //TODO: update your UI
                            parseUsersDataNew((JSONObject) firebaseResponseData);
                        }

                    });
                }
            });

        }

        else if (requestTypeValue == USER_LIST_REQUEST_TYPE.WITH_RESTAURANT_ID) {



            FirebaseChatManager.getInstance().getUsersListWithRestaurantID(RestaurantId, new FirebaseResponseListner() {
                @Override
                public void onCompleted(final Object firebaseResponseData) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //TODO: update your UI

                            //  parseUsersDataNew(firebaseResponseData);
                            parseUsersDataNew((JSONObject) firebaseResponseData);
                        }

                    });
                }
            });

        } else if (requestTypeValue == USER_LIST_REQUEST_TYPE.ACTIVE_USERS) {

            FirebaseChatManager.getInstance().getUsersListWithPresenceStatus("Online", new FirebaseResponseListner() {
                @Override
                public void onCompleted(final Object firebaseResponseData) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //TODO: update your UI

                            parseUsersDataNew((JSONObject) firebaseResponseData);
                        }

                    });
                }
            });

        } else if (requestTypeValue == USER_LIST_REQUEST_TYPE.ACTIVE_WITH_RESTAURANT_ID) {

            FirebaseChatManager.getInstance().getUsersListWithRestaurantIDAndPresenceStatus(RestaurantId, "Online", new FirebaseResponseListner() {
                @Override
                public void onCompleted(final Object firebaseResponseData) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //TODO: update your UI
                            // parseUsersDataNew(firebaseResponseData);
                            parseUsersDataNew((JSONObject) firebaseResponseData);
                        }

                    });
                }
            });
        }

        else if (requestTypeValue == USER_LIST_REQUEST_TYPE.CHAT_USERS) {

        }


        else {

            CommonMethods.getInstance().dismissProgressDialog();
        }
    }

    private void currentUserStatus() {
        Log.e("inside=", "inside");
        FirebaseChatManager.getInstance().getUsersListWithBlockedBy(new FirebaseResponseListner() {
            @Override
            public void onCompleted(final Object firebaseResponseData) {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        //TODO: update your UI
                        parseUsersDataBlockedStatus((JSONObject) firebaseResponseData);
                    }

                });
            }
        });
    }

    //new method to parse block by data(Current User)
    public void parseUsersDataBlockedStatus(JSONObject dataValue) {

        CommonMethods.getInstance().dismissProgressDialog();

        Iterator i = dataValue.keys();
        String key = "";
        while (i.hasNext()) {
            key = i.next().toString();

            try {

                JSONObject userData = dataValue.getJSONObject(key);

                Log.e("ResponseBlock=", "ResponseBlock=" + userData.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            totalUsers++;
        }

    }

    private enum USER_LIST_REQUEST_TYPE {

        ALL_USERS,
        WITH_RESTAURANT_ID,
        ACTIVE_USERS,
        ACTIVE_WITH_RESTAURANT_ID,
        CHAT_USERS,
        ;
    }


}

