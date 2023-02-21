package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ErAshwini on 13/5/18.
 */

public class ChatUser implements Serializable {

    @SerializedName("ChannelId")
    @Expose
    public String channelId;

//    public Map <String, ChatUserChannel> getChannelUserChannelData() {
//        return channelUserChannelData;
//    }
//
//    public void setChannelUserChannelData(Map <String, ChatUserChannel> channelUserChannelData) {
//        this.channelUserChannelData = channelUserChannelData;
//    }
//
//    private Map <String, ChatUserChannel> channelUserChannelData;


    //public static class ChatUserChannel
    {


    }

}
