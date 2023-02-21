package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bingle.ameba.bingle_bar.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pojo.UserList;

/**
 * Created by Manjeet Saini on 28/5/18.
 */

public class ChatAdapter extends RecyclerView.Adapter <ChatAdapter.MyHolder> {
    Context applicationContext;
    List <UserList> userData;

    public ChatAdapter(Context applicationContext, List <UserList> userData) {
        this.applicationContext = applicationContext;
        this.userData = userData;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_msg_custom, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {


        // User Itself(Should't show in the user list)
        // if (userData.get(position).getUserName().equals(MainActivity.m_activity.get_data_fb("display_name"))) {
           /*holder.tLayout.setVisibility(View.GONE);
            holder.view_empty.setVisibility(View.GONE);*/

        // }
        Log.e("get type", "get type" + userData.get(position).getType());

        Log.e("msg", "msg" + userData.get(position).getMessage());

        if (userData.get(position).getType() == 1) {
            holder.linYellow.setVisibility(View.VISIBLE);
            holder.linWhite.setVisibility(View.GONE);

            Log.e("msgMy", "msgMy" + userData.get(position).getMessage());
            holder.txtOthersMsg.setText(userData.get(position).getMessage());

            Log.e("get time", "get time" + userData.get(position).getMessageTime() + userData.get(position).getMessage());
            //added June11

            if (userData.get(position).getMessageTime() != null && !userData.get(position).getMessageTime().isEmpty()) {
                Log.e("dateM", "dateM"+createDate(Long.parseLong(userData.get(position).getMessageTime())));
                holder.txtTimeOther.setText(createDate(Long.parseLong(userData.get(position).getMessageTime())));
              //  holder.txtTimeOther.setReferenceTime(Long.parseLong(userData.get(position).getMessageTime()));
            }
        } else {
            holder.linWhite.setVisibility(View.VISIBLE);
            holder.linYellow.setVisibility(View.GONE);
            Log.e("msgOther", "msgOther" + userData.get(position).getMessage());
            holder.txtMyMsg.setText(userData.get(position).getMessage());
            Log.e("get time", "get time" + userData.get(position).getMessageTime() + userData.get(position).getMessage());
            //added June11
            if (userData.get(position).getMessageTime() != null && !userData.get(position).getMessageTime().isEmpty()) {
               // holder.txtTimeMine.setReferenceTime(Long.parseLong(userData.get(position).getMessageTime()));
                Log.e("dateM", "dateM"+createDate(Long.parseLong(userData.get(position).getMessageTime())));

                holder.txtTimeMine.setText(createDate(Long.parseLong(userData.get(position).getMessageTime())));
            }

        }

    }


    @Override
    public int getItemCount() {
        return userData.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtMyMsg, txtOthersMsg,txtTimeMine, txtTimeOther;
        LinearLayout linWhite, linYellow;


        public MyHolder(View itemView) {
            super(itemView);
            txtMyMsg = (TextView) itemView.findViewById(R.id.txt_white);
            txtOthersMsg = (TextView) itemView.findViewById(R.id.txt_yellow);
            linWhite = (LinearLayout) itemView.findViewById(R.id.lin_white);
            linYellow = (LinearLayout) itemView.findViewById(R.id.lin_yellow);
            txtTimeMine = (TextView) itemView.findViewById(R.id.txt_time_mine);
            txtTimeOther = (TextView) itemView.findViewById(R.id.txt_time_other);

        }
    }
    public CharSequence createDate(long timestamp) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestamp);
        Date d = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        return sdf.format(d);
    }

}
