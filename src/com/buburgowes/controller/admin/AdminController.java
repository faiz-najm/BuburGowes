package com.buburgowes.controller.admin;

import com.buburgowes.controller.Controller;
import com.buburgowes.controller.ProductTabelModel;
import com.buburgowes.model.Admin;

public class AdminController extends Controller {

    public Admin currentUser;
    public ProductTabelModel tabelModel;

    public AdminController() {
        super();
        setCurrentUser(currentUser);
    }

    public void showAdminView() {
        /*AdminView adminView = new AdminView();
        adminView.setVisible(true);*/
    }

    public void setCurrentUser(Admin currentUser) {
        this.currentUser = currentUser;
    }
}
