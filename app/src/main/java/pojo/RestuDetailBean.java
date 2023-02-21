package pojo;

public class RestuDetailBean {
    private String name;
    private String phnCode;
    private String fullName;
    private String img;

    public RestuDetailBean(String name, String img) {
        this.name = name;
        this.img = img;
    }

    /*Countries Bean*/
    public RestuDetailBean(String name, String img, String fullName, String phnCode) {
        this.name = name;
        this.img = img;
        this.fullName = fullName;
        this.phnCode = phnCode;
    }

    public String getPhnCode() {
        return phnCode;
    }

    public void setPhnCode(String phnCode) {
        this.phnCode = phnCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
