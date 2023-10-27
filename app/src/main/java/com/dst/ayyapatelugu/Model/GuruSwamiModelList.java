package com.dst.ayyapatelugu.Model;

public class GuruSwamiModelList {

    private String guruswamiName;
    private String cityName;
    private String profilePic;
    private String templeName;
    private String mobileNo;

    public GuruSwamiModelList() {

    }

    public GuruSwamiModelList(String guruswamiName, String cityName, String profilePic, String templeName, String mobileNo) {
        this.guruswamiName = guruswamiName;
        this.cityName = cityName;
        this.profilePic = profilePic;
        this.templeName = templeName;
        this.mobileNo = mobileNo;
    }

    public String getGuruswamiName() {
        return guruswamiName;
    }

    public void setGuruswamiName(String guruswamiName) {
        this.guruswamiName = guruswamiName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getTempleName() {
        return templeName;
    }

    public void setTempleName(String templeName) {
        this.templeName = templeName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}
