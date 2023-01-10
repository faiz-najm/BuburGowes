package com.buburgowes.model;

/**
 * @author faiz
 */
public abstract class User {

    protected int user_id;
    protected String user_username;
    protected String user_fullName;
    protected String user_pass;
    protected String user_address;
    protected String user_phoneNumber;
    protected int user_type;
    protected int user_token, status;

    public User(String current_user) {
        this.user_username = current_user;
    }

    public User(int user_id,
                String user_username,
                String user_fullName,
                String user_pass,
                String user_address,
                String user_phoneNumber,
                int user_type,
                int user_token,
                int status) {
        this.user_id = user_id;
        this.user_username = user_username;
        this.user_fullName = user_fullName;
        this.user_pass = user_pass;
        this.user_address = user_address;
        this.user_phoneNumber = user_phoneNumber;
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

    public String getUser_username() {
        return user_username;
    }

    public String getUser_fullName() {
        return user_fullName;
    }

    public String getUser_address() {
        return user_address;
    }

    public String getUser_phoneNumber() {
        return user_phoneNumber;
    }

    public int getUser_id() {
        return user_id;
    }
}

