package com.buburgowes.model;

/**
 * @author faiz
 */
public class Admin extends User {
    public Admin(int userId,
                 String userUsername,
                 String userFullName,
                 String userPass,
                 String userAddress,
                 String userPhoneNumber,
                 int userType,
                 int userToken,
                 int status) {
        super(userId,
                userUsername,
                userFullName,
                userPass,
                userAddress,
                userPhoneNumber,
                userType,
                userToken,
                status);
    }


}