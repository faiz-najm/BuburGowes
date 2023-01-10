package com.buburgowes.model;

/**
 * @author faiz
 */
public class Admin extends User {
    public Admin(int user_id,
                 String user_username,
                 String user_fullName,
                 String user_pass,
                 String user_address,
                 String user_phoneNumber,
                 int user_type,
                 int user_token,
                 int status) {
        super(user_id,
                user_username,
                user_fullName,
                user_pass,
                user_address,
                user_phoneNumber,
                user_type,
                user_token,
                status);
    }


}