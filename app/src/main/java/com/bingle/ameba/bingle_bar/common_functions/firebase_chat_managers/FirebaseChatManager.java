package com.bingle.ameba.bingle_bar.common_functions.firebase_chat_managers;

import android.util.Log;

import com.bingle.ameba.bingle_bar.common_functions.Users;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FirebaseChatManager {

    private static final String TAG = "FirebaseChatMessengeError";
    // Singleton Implementation.
    private static final FirebaseChatManager ourInstance = new FirebaseChatManager();
    private DatabaseReference mDatabase;

    private FirebaseChatManager() {

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseChatManager getInstance() {
        return ourInstance;
    }

    // Global ValueEventListner Methods

    private Object getDataSnapshotWithCallBack(DataSnapshot dataSnapshotValue) {

        Map <String, String> value = (Map <String, String>) dataSnapshotValue.getValue();


        if (value == null) {

            return (dataSnapshotValue);
        }

        // Crash Found here
        return (new JSONObject(value));
    }

    private ValueEventListener getValueListnerWithResponseCallBack(final FirebaseResponseListner callBack) {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                // Crash Found here
                try {

                    callBack.onCompleted(getDataSnapshotWithCallBack(dataSnapshot));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        return valueEventListener;
    }

    // User Methods Manager

    // Get current user from Firebase.
    public String getCurrentUser() {

        Users users = new Users(getApplicationContext());

        return users.getFirebaseChatUserHashId();
    }
    // Add or Update logged-In user's data.
    public Boolean addOrUpdateNewUser(String fullNameString, String availabilityStatus, String userProfilePicValue) {

        if (getCurrentUser() != null) {

            Map <String, Object> childUpdates = new HashMap <>();
            childUpdates.put("/" + FirebaseDatabaseSchemaManager.DATABASE_STORE_LIVE.USERS_STORE + "/" + getCurrentUser() + "/" + FirebaseDatabaseSchemaManager.FIELDS.USER_FIELDS.FULL_NAME, fullNameString);
            childUpdates.put("/" + FirebaseDatabaseSchemaManager.DATABASE_STORE_LIVE.USERS_STORE + "/" + getCurrentUser() + "/" + FirebaseDatabaseSchemaManager.FIELDS.USER_FIELDS.USER_ID, getCurrentUser());
            childUpdates.put("/" + FirebaseDatabaseSchemaManager.DATABASE_STORE_LIVE.USERS_STORE + "/" + getCurrentUser() + "/" + FirebaseDatabaseSchemaManager.FIELDS.USER_FIELDS.PRESENCE_STATUS, availabilityStatus);
            childUpdates.put("/" + FirebaseDatabaseSchemaManager.DATABASE_STORE_LIVE.USERS_STORE + "/" + getCurrentUser() + "/" + FirebaseDatabaseSchemaManager.FIELDS.USER_FIELDS.USER_PROFILE_PIC, userProfilePicValue);
            //Block user
            childUpdates.put("/" + FirebaseDatabaseSchemaManager.DATABASE_STORE_LIVE.USERS_STORE + "/" + getCurrentUser() + "/" + FirebaseDatabaseSchemaManager.FIELDS.USER_FIELDS.BLOCK_STATUS, "");
            childUpdates.put("/" + FirebaseDatabaseSchemaManager.DATABASE_STORE_LIVE.USERS_STORE + "/" + getCurrentUser() + "/" + FirebaseDatabaseSchemaManager.FIELDS.USER_FIELDS.BLOCK_BY, "");
            //Save User Token
            childUpdates.put("/" + FirebaseDatabaseSchemaManager.DATABASE_STORE_LIVE.USERS_STORE + "/" + getCurrentUser() + "/" + FirebaseDatabaseSchemaManager.FIELDS.USER_FIELDS.USER_TOKEN, FirebaseInstanceId.getInstance().getToken());
            //Save Message Time
           // childUpdates.put("/" + FirebaseDatabaseSchemaManager.DATABASE_STORE_LIVE.USERS_STORE + "/" + getCurrentUser() + "/" + FirebaseDatabaseSchemaManager.FIELDS.USER_FIELDS.MESSAGE_TIME, getCurrentTime());
            mDatabase.updateChildren(childUpdates);
            return true;
        }

        // Return false: Couldn't found Firebase current user instance or user not logged in or not authorised with firebase yet.
        return false;

    }

    // Set user's presence whether user is online or offine.
    public Boolean setUserPresenceStatus(String presenceStatusValue) {

        if (getCurrentUser() != null) {

            mDatabase.child(FirebaseDatabaseSchemaManager.DATABASE_STORE_LIVE.USERS_STORE).child(getCurrentUser()).child(FirebaseDatabaseSchemaManager.FIELDS.USER_FIELDS.PRESENCE_STATUS).setValue(presenceStatusValue);

            return true;
        }

        // Return false: Couldn't found Firebase current user instance or user not logged in or not authorised with firebase yet.
        return false;
    }


    // Update Restaurant Id
    public Boolean setUserRestaurantId(String RestaurantValue) {

        if (getCurrentUser() != null) {

            mDatabase.child(FirebaseDatabaseSchemaManager.DATABASE_STORE_LIVE.USERS_STORE).child(getCurrentUser()).child(FirebaseDatabaseSchemaManager.FIELDS.USER_FIELDS.RESTAURANT_ID).setValue(RestaurantValue);

            return true;
        }
        // Return false: Couldn't found Firebase current user instance or user not logged in or not authorised with firebase yet.
        return false;
    }

    //isBlockUserWithChannelID
    //Block user(add and update Block Status and Blocked by)
    public Boolean setUserBlockStatus(String channelId, String blockStatusValue, String blockedBy) {

        if (getCurrentUser() != null) {

            mDatabase.child(FirebaseDatabaseSchemaManager.DATABASE_STORE_LIVE.USERS_STORE).child(channelId).child(FirebaseDatabaseSchemaManager.FIELDS.USER_FIELDS.BLOCK_STATUS).setValue(blockStatusValue);
            mDatabase.child(FirebaseDatabaseSchemaManager.DATABASE_STORE_LIVE.USERS_STORE).child(channelId).child(FirebaseDatabaseSchemaManager.FIELDS.USER_FIELDS.BLOCK_BY).setValue(blockedBy);

            return true;
        }

        // Return false: Couldn't found Firebase current user instance or user not logged in or not authorised with firebase yet.
        return false;
    }

    //get block status just try june 4

    /*public void getBlockBy(final String getBlockStatus, final FirebaseResponseListner callBack) {

        mDatabase.child(FirebaseDatabaseSchemaManager.DATABASE_STORE_LIVE.USERS_STORE).
                orderByChild(FirebaseDatabaseSchemaManager.FIELDS.USER_FIELDS.BLOCK_BY).equalTo(getBlockStatus).
                addValueEventListener(getValueListnerWithResponseCallBack(callBack));
    }*/


    //June 5 get block by value
    public void getUsersListWithBlockedBy(final FirebaseResponseListner callBack) {
        mDatabase.child(FirebaseDatabaseSchemaManager.DATABASE_STORE_LIVE.USERS_STORE + "/" + getCurrentUser()
                + FirebaseDatabaseSchemaManager.FIELDS.USER_FIELDS.BLOCK_BY).addValueEventListener(getValueListnerWithResponseCallBack(callBack));
    }
    //till


   public void getUsersListWithPresenceStatus(final String presenceStatusValue, final FirebaseResponseListner callBack) {
        mDatabase.child(FirebaseDatabaseSchemaManager.DATABASE_STORE_LIVE.USERS_STORE).
                orderByChild(FirebaseDatabaseSchemaManager.FIELDS.USER_FIELDS.PRESENCE_STATUS).equalTo(presenceStatusValue).
                addValueEventListener(getValueListnerWithResponseCallBack(callBack));
    }


    public void getUsersListWithRestaurantID(final String restaurantIdValue, final FirebaseResponseListner callBack) {
        mDatabase.child(FirebaseDatabaseSchemaManager.DATABASE_STORE_LIVE.USERS_STORE).
                orderByChild(FirebaseDatabaseSchemaManager.FIELDS.USER_FIELDS.RESTAURANT_ID).equalTo(restaurantIdValue).
                addValueEventListener(getValueListnerWithResponseCallBack(callBack));

    }

    //Filter User according to Restaurant Id
    public void getUsersListWithRestaurantIDAndPresenceStatus(final String restaurantIdValue, final String presenceStatusValue, final FirebaseResponseListner callBack) {

       /* mDatabase.child(FirebaseDatabaseSchemaManager.DATABASE_STORE_LIVE.USERS_STORE).
                orderByChild(FirebaseDatabaseSchemaManager.FIELDS.USER_FIELDS.RESTAURANT_ID).equalTo(restaurantIdValue).
                addValueEventListener(getValueListnerWithResponseCallBack(callBack));*/

        mDatabase.child(FirebaseDatabaseSchemaManager.DATABASE_STORE_LIVE.USERS_STORE).
                orderByChild(FirebaseDatabaseSchemaManager.FIELDS.USER_FIELDS.RESTAURANT_ID).equalTo(restaurantIdValue).
                addValueEventListener(getValueListnerWithResponseCallBack(callBack));

    }

    // Get users list

    public void getUsersListWithBlock(final FirebaseResponseListner callBack) {

        mDatabase.child(FirebaseDatabaseSchemaManager.DATABASE_STORE_LIVE.USERS_STORE).limitToFirst(20).addValueEventListener(getValueListnerWithResponseCallBack(callBack));
    }

    // Listen for channelID (if not available) on main chat screen.

    public void listenForChannelWithFriendId(String friendIdValue, final FirebaseResponseListner callBack) {

        mDatabase.child(FirebaseDatabaseSchemaManager.DATABASE_STORE_LIVE.USERS_STORE + "/" + getCurrentUser() + "/" + FirebaseDatabaseSchemaManager.FIELDS.USER_FIELDS.CHAT_USERS + "/" + friendIdValue).addValueEventListener(getValueListnerWithResponseCallBack(callBack));
    }


    // Chat Methods Manager

    // Setup chat between two users.
    public String setupChatWithUserId(String toUserIdValue) {

        String chatChannelIdKey = mDatabase.child(FirebaseDatabaseSchemaManager.DATABASE_STORE_LIVE.CHAT_CHANNELS).push().getKey();

        Map <String, Object> channelPost = new HashMap <>();
        channelPost.put(FirebaseDatabaseSchemaManager.FIELDS.CHAT_CHANNEL_FIELDS.CHANNEL_ID, chatChannelIdKey);

        Map <String, Object> childUpdates = new HashMap <>();
        childUpdates.put("/" + FirebaseDatabaseSchemaManager.DATABASE_STORE_LIVE.USERS_STORE + "/" + getCurrentUser() + "/" + FirebaseDatabaseSchemaManager.FIELDS.USER_FIELDS.CHAT_USERS + "/" + toUserIdValue, channelPost);
        childUpdates.put("/" + FirebaseDatabaseSchemaManager.DATABASE_STORE_LIVE.USERS_STORE + "/" + toUserIdValue + "/" + FirebaseDatabaseSchemaManager.FIELDS.USER_FIELDS.CHAT_USERS + "/" + getCurrentUser(), channelPost);

        mDatabase.updateChildren(childUpdates);
        return chatChannelIdKey;
    }

    // Send chat message in channel id.
    public Boolean sendChatMessageInChannelId(String channelIdValue, String senderIdValue, String messageValue,String messageTime) {

        if (!channelIdValue.isEmpty() && !senderIdValue.isEmpty() && !messageValue.isEmpty()) {

            // if (channelIdValue!=null&&!channelIdValue.isEmpty() && !senderIdValue.isEmpty() && !messageValue.isEmpty()) {//June 11

            String messageIdKey = mDatabase.push().getKey();

            Map <String, Object> messagePost = new HashMap <>();


            messagePost.put(FirebaseDatabaseSchemaManager.FIELDS.CHAT_CHANNEL_FIELDS.SENDER_ID, senderIdValue);
            messagePost.put(FirebaseDatabaseSchemaManager.FIELDS.CHAT_CHANNEL_FIELDS.MESSAGE, messageValue);
            messagePost.put(FirebaseDatabaseSchemaManager.FIELDS.CHAT_CHANNEL_FIELDS.MESSAGE_TIME, messageTime);

            Map <String, Object> childUpdates = new HashMap <>();
            childUpdates.put("/" + FirebaseDatabaseSchemaManager.DATABASE_STORE_LIVE.CHAT_CHANNELS + "/" + channelIdValue + "/" + messageIdKey, messagePost);
            Log.e("sendChatMessageInC", "sendChatMessageInC");

            mDatabase.updateChildren(childUpdates);
            return true;

        }

        // All parameters must hold a valid value to insert in Firebase database.
        return false;
    }

    // Listen for chat messages of particular Channel.
    public void getChatMessagesWithChannelId(String channelIdValue, final FirebaseResponseListner callBack) {

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("yeah hear", "yeah here" + s);
                callBack.onCompleted(getDataSnapshotWithCallBack(dataSnapshot));

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.e("onChildChanged", "onChildChanged" + s);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                Log.e("onChildRemoved", "onChildRemoved");

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.e("onChildMoved", "onChildMoved");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        mDatabase.child(FirebaseDatabaseSchemaManager.DATABASE_STORE_LIVE.CHAT_CHANNELS + "/" + channelIdValue).addChildEventListener(childEventListener);
    }

}