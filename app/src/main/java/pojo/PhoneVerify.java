package pojo;

import java.io.Serializable;

public class PhoneVerify implements Serializable{

    private String firebaseHashChatKey;

    private String contactNumber;

    private String firebaseChatID;

    private String loginChoiceID;

    private String country;

    private String createdOn;

    private String userID;

    private String loginchoiceUniqueID;

    private Boolean accountStatus;

    private String userName;

    private String gender;

    private String fullName;

    private String updatedOn;

    private String versionUpgradeCode;

    public String getVersionUpgradeCode() {
        return versionUpgradeCode;
    }

    public void setVersionUpgradeCode(String versionUpgradeCode) {
        this.versionUpgradeCode = versionUpgradeCode;
    }

    public String getFirebaseHashChatKey ()
    {
        return firebaseHashChatKey;
    }

    public void setFirebaseHashChatKey (String firebaseHashChatKey)
    {
        this.firebaseHashChatKey = firebaseHashChatKey;
    }

    public String getContactNumber ()
    {
        return contactNumber;
    }

    public void setContactNumber (String contactNumber)
    {
        this.contactNumber = contactNumber;
    }

    public String getFirebaseChatID ()
    {
        return firebaseChatID;
    }

    public void setFirebaseChatID (String firebaseChatID)
    {
        this.firebaseChatID = firebaseChatID;
    }

    public String getLoginChoiceID ()
    {
        return loginChoiceID;
    }

    public void setLoginChoiceID (String loginChoiceID)
    {
        this.loginChoiceID = loginChoiceID;
    }

    public String getCountry ()
    {
        return country;
    }

    public void setCountry (String country)
    {
        this.country = country;
    }

    public String getCreatedOn ()
    {
        return createdOn;
    }

    public void setCreatedOn (String createdOn)
    {
        this.createdOn = createdOn;
    }

    public String getUserID ()
    {
        return userID;
    }

    public void setUserID (String userID)
    {
        this.userID = userID;
    }

    public String getLoginchoiceUniqueID ()
    {
        return loginchoiceUniqueID;
    }

    public void setLoginchoiceUniqueID (String loginchoiceUniqueID)
    {
        this.loginchoiceUniqueID = loginchoiceUniqueID;
    }

    public Boolean getAccountStatus ()
    {
        return accountStatus;
    }

    public void setAccountStatus (Boolean accountStatus)
    {
        this.accountStatus = accountStatus;
    }

    public String getUserName ()
    {
        return userName;
    }

    public void setUserName (String userName)
    {
        this.userName = userName;
    }

    public String getGender ()
    {
        return gender;
    }

    public void setGender (String gender)
    {
        this.gender = gender;
    }

    public String getFullName ()
    {
        return fullName;
    }

    public void setFullName (String fullName)
    {
        this.fullName = fullName;
    }

    public String getUpdatedOn ()
    {
        return updatedOn;
    }

    public void setUpdatedOn (String updatedOn)
    {
        this.updatedOn = updatedOn;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [firebaseHashChatKey = "+firebaseHashChatKey+", contactNumber = "+contactNumber+", firebaseChatID = "+firebaseChatID+", loginChoiceID = "+loginChoiceID+", country = "+country+", createdOn = "+createdOn+", userID = "+userID+", loginchoiceUniqueID = "+loginchoiceUniqueID+", accountStatus = "+accountStatus+", userName = "+userName+", gender = "+gender+", fullName = "+fullName+", updatedOn = "+updatedOn+"]";
    }
}
