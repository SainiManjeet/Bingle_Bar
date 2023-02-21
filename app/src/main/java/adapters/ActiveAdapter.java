package adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import chat.Chat;
import pojo.UserList;

import static com.bingle.ameba.bingle_bar.common_functions.Constants.messageUnread;

public class ActiveAdapter extends RecyclerView.Adapter<ActiveAdapter.MyHolder> implements ServicesResponse {
    Context applicationContext;
    List<UserList> userData;
    String restaurantId, currentUserblockedBy,from;

    public ActiveAdapter(Context applicationContext, List<UserList> userData, String restaurantId, String currentUserblockedBy, String from) {
        this.applicationContext = applicationContext;
        this.userData = userData;
        this.restaurantId = restaurantId;
        this.currentUserblockedBy = currentUserblockedBy;
        this.from=from;
    }

    @Override
    public ActiveAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_chat, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(ActiveAdapter.MyHolder holder, final int position) {

      /*if( tabType.equalsIgnoreCase("1")){
          holder.txtUnreadMsg.setVisibility(View.VISIBLE);
      }
      else{
          holder.txtUnreadMsg.setVisibility(View.GONE);
      }*/
        Log.e("messageUnread", "messageUnread" + messageUnread);
        if(messageUnread.equalsIgnoreCase("true")){
            holder.txtUnreadMsg.setVisibility(View.GONE);
        }




        // User Itself(Should't show in the user list)
        if (userData.get(position).getUserName().equals(MainActivity.m_activity.get_data_fb("display_name"))) {
           /*holder.tLayout.setVisibility(View.GONE);
            holder.view_empty.setVisibility(View.GONE);*/
            //Remove user from the list
            //  userData.remove(position);
            //  notifyItemRemoved(position);

            //must uncomment
           /* holder.tLayout.setVisibility(View.GONE);
            holder.view_empty.setVisibility(View.GONE);*/

        } else {

            holder.txtUserName.setText(userData.get(position).getUserName());
            holder.txtUserStatus.setText(userData.get(position).getUserStatus());

            //Change the text color according to User Presence Status

            if (userData.get(position).getUserStatus().equalsIgnoreCase("Online")) {
                holder.txtUserStatus.setTextColor(Color.parseColor("#5ec639"));
                if(from.equalsIgnoreCase("Users")){
                    holder.txtUserStatus.setText("Available");
                }

            } else {
                holder.txtUserStatus.setTextColor(Color.parseColor("#878787"));

            }





            Log.e("Pic", "Pic" + userData.get(position).getUserPic());

            if (!userData.get(position).getUserPic().equals("NOT_FOUND") && !userData.get(position).getUserPic().isEmpty()) {
                Glide.with(applicationContext).load(userData.get(position).getUserPic())
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.i1);
            } else {
                Glide.with(applicationContext).load(R.drawable.com_facebook_profile_picture_blank_portrait)
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.i1);

            }

        }


    }


    @Override
    public int getItemCount() {
        return userData.size();
    }

    //just added june 4
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private void blockUserPopUp(final int pos) {
        LayoutInflater layoutInflater = LayoutInflater.from(applicationContext);
        View promptView = layoutInflater.inflate(R.layout.block_user, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(applicationContext);
        alertDialogBuilder.setTitle("Block User");

        alertDialogBuilder.setMessage("Are you sure you want to block this user?");
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Users users = new Users(applicationContext);
                        blockUser(userData.get(pos).getUserId(), "1", users.getFirebaseChatUserHashId());
                        //Api is called in case of Restaurant specific chat only
                        if (restaurantId != null && !restaurantId.isEmpty()) {
                            blockUserAPI(editText.getText().toString(), users.getFirebaseChatUserHashId());
                        }

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
        //Block user(add and update Block Status and Blocked by)
        FirebaseChatManager.getInstance().setUserBlockStatus(channelId, blockStatus, blockedBy);
        Toast.makeText(applicationContext, "User has been Blocked!", Toast.LENGTH_SHORT).show();
    }

    public void blockUserAPI(String blockMessage, String firebaseChatUserHashId) {
        Users users = new Users(applicationContext);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = sdf.format(new Date()).replace(" ", "T");
        CommonMethods.getInstance().createRetrofitBuilderWithHeader(applicationContext);
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("BlockedByUserID", users.getUserId());
            params.put("BlockedUserFirebaseHashChatKey", firebaseChatUserHashId);
            if (restaurantId != null && !restaurantId.isEmpty()) {
                params.put("RestaurantId", restaurantId);
            }
            params.put("Message", blockMessage);
            params.put("BlockedAt", currentDateTime);


            Log.e("params", "params" + params.toString());

            // CommonMethods.getInstance().callService(applicationContext, this, tLayout, Constants.getRestaurantImagesUsingRestaurantID, params);
            CommonMethods.getInstance().callServiceBackground(applicationContext, this, Constants.BLOCK_USER, params);

        } catch (Exception e) {
            CommonMethods.getInstance().dismissProgressDialog();
            Log.e("in_exception_" + Constants.BLOCK_USER, "" + e.toString());
        }


    }

    @Override
    public void onResponseSuccess(Bean_CommonResponse response) throws JSONException {
        JSONObject jsonObject = response.getJsonObject();

        Log.e("responseBlock", "responseBlock" + jsonObject.toString());

        Boolean status = jsonObject.getBoolean("success");

        if (status) {
            Toast.makeText(applicationContext, "User has been Blocked!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(applicationContext, "Failed to Block!Try Again", Toast.LENGTH_SHORT).show();
        }

    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public final LinearLayout tLayout, tbl_row;
        TextView txtUserName, txtUserStatus, t3,txtUnreadMsg;
        ImageView i1;
        View view_empty;

        public MyHolder(View itemView) {
            super(itemView);

            itemView.setOnLongClickListener(this);


            txtUserName = (TextView) itemView.findViewById(R.id.text);
            txtUserStatus = (TextView) itemView.findViewById(R.id.text2);
            t3 = (TextView) itemView.findViewById(R.id.text3);
            i1 = (ImageView) itemView.findViewById(R.id.image);
            tLayout = (LinearLayout) itemView.findViewById(R.id.tb_lay);
            tbl_row = (LinearLayout) itemView.findViewById(R.id.tbl_row);
            view_empty = (View) itemView.findViewById(R.id.view_empty);
            txtUnreadMsg= (TextView) itemView.findViewById(R.id.txt_unread);
            tLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!ConnectivityReceiver.isConnected()) {
                        Toast.makeText(applicationContext, "Internet connection not available", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    int adapterPos = getAdapterPosition();
                    Log.e("longPos", "longPos" + adapterPos);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user_obj", userData.get(adapterPos));
                    String channelId = "";

                    if (userData.get(adapterPos).getChatUsers() == null || userData.get(adapterPos).getChatUsers().size() > 0) {


                        if (userData.get(adapterPos).getChatUsers() == null) {

                            channelId = FirebaseChatManager.getInstance().setupChatWithUserId(userData.get(adapterPos).getUserId());

                            bundle.putString("ChannelId", channelId);

                            //Issue found  Userget CurrentUser() is null
                        } else if (!userData.get(adapterPos).getChatUsers().containsKey(FirebaseChatManager.getInstance().getCurrentUser())) {

                            Log.e("chat with new", "chat with new");
                            // For chat with new users: This is a setup chat functionality implementation between two users.

                            channelId = FirebaseChatManager.getInstance().setupChatWithUserId(userData.get(adapterPos).getUserId());

                            bundle.putString("ChannelId", channelId);
                        }
                    }

                    Log.e("ClickedChat", "ClickedChat");
                    messageUnread="true"; // When user have clicked on any user it means message i seen(Because is going on chat window with that particular user)

                    Log.e("currentUserBl", "currentUserBl" + currentUserblockedBy);
                    Intent intent = new Intent(applicationContext, Chat.class);
                    intent.putExtras(bundle);
                    intent.putExtra("currentUserBlockedBy", currentUserblockedBy);
                    applicationContext.startActivity(intent);
                }
            });
        }


        @Override
        public void onClick(View view) {

        }
        @Override
        public boolean onLongClick(View view) {
            int adapterPos = getAdapterPosition();
            Log.e("longPos", "longPos" + adapterPos);
            //blockUserPopUp(adapterPos);
            return false;
        }


    }




}
