package com.guanzhuli.zestate.model;

/**
 * Created by shashank reddy on 1/24/2017.
 */
public class AgentInfo {

    String userName;
    String phoneNumber;
    String email;

    public AgentInfo(String userName, String phoneNumber, String email) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }


    public String getUserName() {
        return userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }
}
