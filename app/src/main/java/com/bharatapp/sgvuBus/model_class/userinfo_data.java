package com.bharatapp.sgvuBus.model_class;

public class userinfo_data {
    String user_firstname,user_email,user_mobile,profile_image;

    public userinfo_data(String user_firstname, String user_email, String user_mobile, String profile_image) {
        this.user_firstname = user_firstname;
        this.user_email = user_email;
        this.user_mobile = user_mobile;
        this.profile_image = profile_image;
    }

    public userinfo_data() {
    }

    public String getUser_firstname() {
        return user_firstname;
    }

    public void setUser_firstname(String user_firstname) {
        this.user_firstname = user_firstname;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public void setUser_mobile(String user_mobile) {
        this.user_mobile = user_mobile;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }
}
