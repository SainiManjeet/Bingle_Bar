package chat;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import adapters.ActiveAdapter;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import pojo.UserList;

import static com.google.android.gms.internal.zzahn.runOnUiThread;

/**
 * Created by Er.Manjeet Kaur Saini on 16/5/18.
 */

public class UsersList extends Fragment {
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


   /* @BindView(R.id.txt_restaurant_name)
    TextView restaurantName;*/

    @BindViews({R.id.txt_restaurant_name, R.id.txt_connectivity})
    List <TextView> textViews;
    Map <String, Integer> fullnameMap = new HashMap <>();
    Map <String, Integer> fullnameSortedMap = new HashMap <>();
    ArrayList <String> sortedList = new ArrayList <>();


    // Comparator<String> StringAscComparator = new Comparator<String>()
    private DrawerLayout drawer_obj;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chat, container, false);


        ButterKnife.bind(this, v);

        beanList1 = new ArrayList <>();


        Log.e("UsersListM", "UsersListM");


        drawer_obj = (DrawerLayout) v.findViewById(R.id.drawerlayout_id2);

        imgViews.get(1).setVisibility(View.GONE);

        noUsersText = (TextView) v.findViewById(R.id.noUsersText);

        Constants.USER_TYPE = "Active";// rmv

        //@ manjeet updation
        fromView = "MainChat";

        getUsersList();
        //Open Drawer
        imgViews.get(0).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
               /* if (fromView.equalsIgnoreCase("Restaurant")) {
                    getFragmentManager().popBackStack();
                } else {
                    drawer_obj.openDrawer(GravityCompat.START);
                    try {
                        CommonMethods.getInstance().hidekeyboard(view);
                    } catch (Exception e) {
                    }
                }*/
                    drawer_obj.openDrawer(GravityCompat.START);
                    try {
                        CommonMethods.getInstance().hidekeyboard(view);
                    } catch (Exception e) {
                        e.printStackTrace();

                }
            }
        });


        return v;
    }


    public void parseUsersDataNew(JSONObject dataValue) {

        CommonMethods.getInstance().dismissProgressDialog();

        // Clear all the data from bean list so that fresh data can be loaded.
        beanList1.clear();

        Iterator i = dataValue.keys();
        String key = "";
        ArrayList<String> galaxiesList = new ArrayList<String>();


        while (i.hasNext()) {
            key = i.next().toString();

            try {
                Users users = new Users(getActivity());

                JSONObject userData = dataValue.getJSONObject(key);
                Log.e("PresenceStatus=", "PresenceStatus=" + userData.getString("PresenceStatus"));
                //PresenceStatus
                if (userData.getString("UserId").contains(users.getFirebaseChatUserHashId())) {


                    myBlockBy = userData.getString("BlockedBy");

                    //Do Nothing
                } else {
                    Log.e("notMatched", "notMatched");
                    Gson gson = new Gson();
                    UserList userList = gson.fromJson(String.valueOf(userData), UserList.class);
                    Log.e("CurrentUserHash=", "CurrentUserHash=" + users.getFirebaseChatUserHashId());

                    String PresenceStatus = userData.getString("PresenceStatus");
                    if (PresenceStatus.equalsIgnoreCase("Online")) {
                        beanList1.add(userList);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            totalUsers++;
        }

      //  Collections.sort(galaxiesList);
       /* Log.e("galaxiesList=", "galaxiesList="+galaxiesList);
        for(int cnt=0;cnt<=galaxiesList.size();cnt++) {
            Log.e("galaxiesList=", "galaxiesList=" + galaxiesList.get(cnt));
        }*/

        if (totalUsers < 1) {
            noUsersText.setVisibility(View.VISIBLE);
        } else {
            noUsersText.setVisibility(View.GONE);
            ActiveUserData();
        }
    }


    private void ActiveUserData() {
        Log.e("in ActiveUserDataUser", "ActiveUserDataUser" + beanList1.size());
        recyclerView.removeAllViewsInLayout();
        recyclerView.setAdapter(null);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);


        ActiveAdapter myListAdapter = new ActiveAdapter(getActivity(), beanList1, RestaurantId, myBlockBy,"Users"); //Id added
        recyclerView.setAdapter(myListAdapter);

    }


    public void getUsersList() {
        ////Open All User List
        //getUsersList(USER_LIST_REQUEST_TYPE.ACTIVE_USERS);

        /*Users users = new Users(getActivity());
        RestaurantId=users.getRestaurantId();*/


        RestaurantId="1";
        getUsersList(USER_LIST_REQUEST_TYPE.WITH_RESTAURANT_ID);
    }


    public void getUsersList(USER_LIST_REQUEST_TYPE requestTypeValue) {

        if (!ConnectivityReceiver.isConnected()) {
            Toast.makeText(getActivity(), "Internet connection not available", Toast.LENGTH_SHORT).show();
            return;
        }

        // New Get Users Firebase Request
        //  CommonMethods.getInstance().setProgressDialog(getActivity(), "Loading Users...", false);


        CommonMethods.getInstance().setProgressDialog(getActivity(), "Loading Users...", true);

       /* if (requestTypeValue == USER_LIST_REQUEST_TYPE.ACTIVE_USERS) {
            CommonMethods.getInstance().dismissProgressDialog();//Manjeet

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

        }*/

        if (requestTypeValue == USER_LIST_REQUEST_TYPE.WITH_RESTAURANT_ID) {
            CommonMethods.getInstance().dismissProgressDialog();//Manjeet

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

        }

//        if (requestTypeValue == USER_LIST_REQUEST_TYPE.ALL_USERS) {
//
//            FirebaseChatManager.getInstance().getUsersListWithBlock(new FirebaseResponseListner() {
//                @Override
//                public void onCompleted(final Object firebaseResponseData) {
//
//                    runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            //TODO: update your UI
//                            parseUsersDataNew((JSONObject) firebaseResponseData);
//                        }
//
//                    });
//                }
//            });
//
//        } else if (requestTypeValue == USER_LIST_REQUEST_TYPE.WITH_RESTAURANT_ID) {
//
//            FirebaseChatManager.getInstance().getUsersListWithRestaurantID(RestaurantId, new FirebaseResponseListner() {
//                @Override
//                public void onCompleted(final Object firebaseResponseData) {
//
//                    runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            //TODO: update your UI
//
//                            //  parseUsersDataNew(firebaseResponseData);
//                            parseUsersDataNew((JSONObject) firebaseResponseData);
//                        }
//
//                    });
//                }
//            });
//
//        } else if (requestTypeValue == USER_LIST_REQUEST_TYPE.ACTIVE_USERS) {
//
//            FirebaseChatManager.getInstance().getUsersListWithPresenceStatus("Online", new FirebaseResponseListner() {
//                @Override
//                public void onCompleted(final Object firebaseResponseData) {
//
//                    runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            //TODO: update your UI
//
//                            parseUsersDataNew((JSONObject) firebaseResponseData);
//                        }
//
//                    });
//                }
//            });
//
//        } else if (requestTypeValue == USER_LIST_REQUEST_TYPE.ACTIVE_WITH_RESTAURANT_ID) {
//
//            FirebaseChatManager.getInstance().getUsersListWithRestaurantIDAndPresenceStatus(RestaurantId, "Online", new FirebaseResponseListner() {
//                @Override
//                public void onCompleted(final Object firebaseResponseData) {
//
//                    runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            //TODO: update your UI
//                            // parseUsersDataNew(firebaseResponseData);
//                            parseUsersDataNew((JSONObject) firebaseResponseData);
//                        }
//
//                    });
//                }
//            });
//        }
        else {

            CommonMethods.getInstance().dismissProgressDialog();
        }
    }



    private enum USER_LIST_REQUEST_TYPE {

        ALL_USERS,
        WITH_RESTAURANT_ID,
        ACTIVE_USERS,
        ACTIVE_WITH_RESTAURANT_ID;
    }





}

