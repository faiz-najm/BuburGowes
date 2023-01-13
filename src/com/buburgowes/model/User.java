package com.buburgowes.model;

/**
 * @author faiz
 */
public abstract class User {

    protected int userId;
    protected String userUsername;
    protected String userFullName;
    protected String userPass;
    protected String userAddress;
    protected String userPhoneNumber;
    protected int user_type;
    protected int user_token, status;

    public User(String current_user) {
        this.userUsername = current_user;
    }

    public User(int userId,
                String userUsername,
                String userFullName,
                String userPass,
                String userAddress,
                String userPhoneNumber,
                int user_type,
                int user_token,
                int status) {
        this.userId = userId;
        this.userUsername = userUsername;
        this.userFullName = userFullName;
        this.userPass = userPass;
        this.userAddress = userAddress;
        this.userPhoneNumber = userPhoneNumber;
        this.user_type = user_type;
        this.user_token = user_token;
        this.status = status;
    }

    public int getUser_token() {
        return user_token;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLoginStatus() {
        String result = "";

        switch (getStatus()) {
            case 0:
                result = "Offline";
                break;

            case 1:
                result = "Online";
                break;

            case 2:
                result = "Suspended";
                break;
        }
        return result;
    }

    public int getUser_type() {
        return user_type;
    }

    public String getUserType() {
        String result = "";

        switch (getUser_type()) {
            case 1:
                result = "Admin";
                break;

            case 2:
                result = "Customer";
                break;
        }
        return result;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public int getUserId() {
        return userId;
    }
}

