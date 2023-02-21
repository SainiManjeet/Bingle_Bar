package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

public class UserList implements Serializable {
    String message;
    int type;
    String fcmToken;
    String messageTime;
    // Block Status
    @SerializedName("BlockedStatus")
    @Expose
    private String BlockedStatus;
    @SerializedName("BlockedBy")
    @Expose
    private String BlockedBy;
    @SerializedName("FcmToken")
    @Expose
    private String FcmToken;
    @SerializedName("FullName")
    @Expose
    private String userName;
    @SerializedName("UserId")
    @Expose
    private String userId;
    @SerializedName("PresenceStatus")
    @Expose
    private String userStatus;
    @SerializedName("UserProfilePic")
    @Expose
    private String userPic;
    @SerializedName("ChatUsers")
    @Expose
    private Map <String, ChatUser> chatUsers;


    public UserList(String userId, String userName, String userStatus, String userPic, String type) {
        this.userName = userName;
        this.userStatus = userStatus;
        this.userPic = userPic;
        this.userId = userId;
        //@Added my Manjeet Saini for testing purpose
        //this.type = type;
    }

    public UserList(int type, String message, String fcmToken, String messageTime) {
        this.type = type;
        this.message = message;
        this.fcmToken = fcmToken;
        this.messageTime = messageTime;


    }

    public String getBlockedStatus() {
        return BlockedStatus;
    }

    public void setBlockedStatus(String blockedStatus) {
        BlockedStatus = blockedStatus;
    }

    public String getBlockedBy() {
        return BlockedBy;
    }

    public void setBlockedBy(String blockedBy) {
        BlockedBy = blockedBy;
    }

    public String getFcmToken() {
        return FcmToken;
    }


    //Chat messages

    public void setFcmToken(String fcmToken) {
        FcmToken = fcmToken;
    }

    public Map <String, ChatUser> getChatUsers() {
        return chatUsers;
    }

    public void setChatUsers(Map <String, ChatUser> chatUsers) {
        this.chatUsers = chatUsers;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }
}
