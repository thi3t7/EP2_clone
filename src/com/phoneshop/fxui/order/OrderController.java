/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoneshop.fxui.order;


import java.io.IOException;

import com.phoneshop.fxui.Navigator;
import com.phoneshop.model.UserName;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class OrderController {

    @FXML
    private MFXButton btnAdmin;

    @FXML
    private MFXButton btnSmartPhone;

    @FXML
    private MFXButton btnMfg;

    @FXML
    private MFXButton LogOut;

    @FXML
    private Label txtUsername;

    @FXML
    private MFXButton btnOrder;

    @FXML
    private TableView<?> tvOrder;
    
    @FXML
    private TableColumn<?, ?> tcOrderID;

    @FXML
    private TableColumn<?, ?> tcCustomerName;

    @FXML
    private TableColumn<?, ?> tcProductName;

    @FXML
    private TableColumn<?, ?> tcAmount;

    @FXML
    private TableColumn<?, ?> tcTotal;
    
    @FXML
    private TableColumn<?, ?> tcDate;

    @FXML
    private MFXButton btnReset;

    @FXML
    void btnAdminClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToAdminIndex();
    }

    @FXML
    void btnLogoutClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToLogin();
    }

    @FXML
    void btnMfgClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToManufacturerIndex();
    }

    @FXML
    void btnOrderClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToOrder();
    }

    @FXML
    void btnResetClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToOrder();
    }

    @FXML
    void btnSmartPhoneClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToSmartPhoneIndex();
    }

    public void initialize() {
        txtUsername.setText(UserName.username);
    }
}
