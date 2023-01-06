package com.buburgowes.model;

/**
 * @author andre
 */
public class Admin extends User {

    public Admin(String user_username,
                 String user_fullName,
                 String user_pass,
                 String user_address,
                 String user_phoneNumber,
                 int user_token,
                 int user_type,
                 int status) {
        super(user_username,
                user_fullName,
                user_pass,
                user_address,
                user_phoneNumber,
                user_token,
                user_type,
                status);
    }

    public Admin(int user_id,
                 String user_username,
                 String user_fullName,
                 String user_pass,
                 String user_address,
                 String user_phoneNumber,
                 int user_token,
                 int user_type,
                 int status) {
        this(user_username,
                user_fullName,
                user_pass,
                user_address,
                user_phoneNumber,
                user_token,
                user_type,
                status);
        this.user_id = user_id;
    }

}