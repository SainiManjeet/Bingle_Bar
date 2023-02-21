package pojo;

import java.util.ArrayList;

public class OwerBean {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private String address;
    private String longi;
    private String state;
    private String zipCode;

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    private String open;
    private String close;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    private String rating;



    String img;
    String nameRestu;
    String ratingRestu;
    String time;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getNameRestu() {
        return nameRestu;
    }

    public void setNameRestu(String nameRestu) {
        this.nameRestu = nameRestu;
    }

    public String getRatingRestu() {
        return ratingRestu;
    }

    public void setRatingRestu(String ratingRestu) {
        this.ratingRestu = ratingRestu;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    String comment,profilePic;


    public ArrayList <String> getImageArray() {
        return imageArray;
    }

    public void setImageArray(ArrayList <String> imageArray) {
        this.imageArray = imageArray;
    }

    private  ArrayList<String>imageArray=new ArrayList <>();

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    private String country;

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    private String contactNo;

    public String getLati() {
        return lati;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }

    private String lati;

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    private String addr2;

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public OwerBean(String name, String address, String contactNo, String lati, String longi, String state, String zipCode, String country, ArrayList <String> imageArray,
                    String rating ,String open,String close,String addr2) {
        this.name = name;
        this.address = address;
        this.contactNo = contactNo;
        this.lati = lati;
        this.longi = longi;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.imageArray = imageArray;
        this.rating=rating;
        this.open=open;
        this.close=close;
        this.addr2=addr2;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    //For rating only
    public OwerBean(String img, String nameRestu, String ratingRestu,String time ,String comment,String profilePic) {
        this.img = img;
        this.nameRestu = nameRestu;
        this.ratingRestu = ratingRestu;
        this.time = time;
        this.comment = comment;
        this.profilePic = profilePic;

    }
}
