/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoneshop.fxui.Admin;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.phoneshop.dao.Account;
import com.phoneshop.dao.Admin;
import com.phoneshop.dao.AdminDAO;
import com.phoneshop.dao.AdminDAOImp;
import com.phoneshop.fxui.Navigator;
import com.phoneshop.model.UserName;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class AdminIndexController implements Initializable {

    private AdminDAO admindao = new AdminDAOImp();
//    private AccountDAO accountdao = new AccountDAOImp();
    private Account a = new Account();

    @FXML
    private TableView<Admin> tvAdmin;

    @FXML
    private TableColumn<Admin, String> tcAccount;

    @FXML
    private TableColumn<Admin, String> tcPassword;

    @FXML
    private TableColumn<Admin, String> tcName;

    @FXML
    private TableColumn<Admin, String> tcEmail;

    @FXML
    private TableColumn<Admin, String> tcNumberPhone;

    @FXML
    private MFXTextField txtAccount;

    @FXML
    private MFXPasswordField txtPassword;

    @FXML
    private MFXTextField txtName;

    @FXML
    private MFXTextField txtEmail;

    @FXML
    private MFXTextField txtNumberPhone;

    @FXML
    private Label lbMessage;

    @FXML
    private Label lbAccount;

    @FXML
    private MFXButton btnClear;

    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXButton btnEdit;

    @FXML
    private MFXButton btnSave;

    @FXML
    private MFXButton btnReset;

    @FXML
    private MFXButton btnAdmin;

    @FXML
    private MFXButton btnSmartPhone;

    @FXML
    private MFXButton btnMfg;
    
    @FXML
    private MFXButton btnOrder;

    @FXML
    private MFXButton btnImage;

    @FXML
    private Label txtUsername;

    @FXML
    void btnAdminClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToAdminIndex();
    }
    
    @FXML
    void btnOrderClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToOrder();
    }

    @FXML
    void btnClear(ActionEvent event) {
        txtAccount.setText("");
        txtPassword.setText("");
        txtName.setText("");
        txtEmail.setText("");
        txtNumberPhone.setText("");

    }

    @FXML
    void btnDelete(ActionEvent event) {
        Admin selectAdmin = tvAdmin.getSelectionModel().getSelectedItem();

        if (selectAdmin == null) {
            selectAdminWarning();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Delete a Admin");
            alert.setHeaderText("Are you sure you want to delete the selected Admin?");
            Optional<ButtonType> ok = alert.showAndWait();
            if (ok.get() == ButtonType.OK) {
                Admin deleteAdmin = selectAdmin;
                boolean result = admindao.delete(deleteAdmin);
                if (result) {
                    tvAdmin.getItems().remove(deleteAdmin); //update TableView
                    System.out.println("Admin is deleted");
//                    logger.info("Delete completed");
                } else {
                    System.out.println("No Admin is deleted");
//                    logger.info("Delete not completed");
                }
            }
        }
    }

    @FXML
    void btnEditClick(ActionEvent event) throws IOException {
        Admin editAdmin = tvAdmin.getSelectionModel().getSelectedItem();
        if (editAdmin == null) {
            selectAdminWarning();
        } else {
            Navigator.getInstance().goToAdminEdit(editAdmin);
        }
    }

    @FXML
    void btnMfgClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToManufacturerIndex();
    }

    @FXML
    void btnResetClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToAdminIndex();
    }

    @FXML
    private MFXButton LogOut;

    @FXML
    void btnLogoutClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToLogin();
    }

    @FXML
    void btnSaveClick(ActionEvent event) throws IOException {
        if (check()) {
            Admin insertAdmin = news();
            insertAdmin = admindao.insert(insertAdmin);
            lbMessage.setText("Insert Successful");
            Navigator.getInstance().goToAdminIndex();
        }
    }

    @FXML
    void btnSmartPhoneClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToSmartPhoneIndex();
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        txtUsername.setText(UserName.username);
        ObservableList<Admin> admin = admindao.selectAll();
//        System.out.println(a.getUserName());

        tvAdmin.setItems(admin);

        tcAccount.setCellValueFactory((a) -> {
            return a.getValue().getAccountProperty();
        });

        tcPassword.setCellValueFactory((a) -> {
            return a.getValue().getPasswordProperty();
        });

        tcName.setCellValueFactory((a) -> {
            return a.getValue().getNameProperty();
        });

        tcEmail.setCellValueFactory((a) -> {
            return a.getValue().getEmailProperty();
        });

        tcNumberPhone.setCellValueFactory((a) -> {
            return a.getValue().getNumberPhoneProperty();
        });
    }

    public void initialize() {

    }

    private void selectAdminWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Please select a Admin");
        alert.setHeaderText("A admin must be selected for the operation");
        alert.showAndWait();
    }

    private Admin news() {
        Admin admin = new Admin();
        admin.setAccount(txtAccount.getText());
        admin.setPassword(txtPassword.getText());
        admin.setName(txtName.getText());
        admin.setEmail(txtEmail.getText());
        admin.setNumberPhone(txtNumberPhone.getText());
        return admin;
    }

    private boolean check() {

        if (txtAccount.getText().isEmpty()) {
            lbMessage.setText("Account cannot be empty");
            return false;
        } else if (txtAccount.getText().length() > 50) {
            lbMessage.setText("Account cannot be larger than 50 characters");
            return false;
        }

        if (txtPassword.getText().isEmpty()) {
            lbMessage.setText("Password cannot be empty");
            return false;
        }

        if (txtName.getText().isEmpty()) {
            lbMessage.setText("Name cannot be empty");
            return false;
        } else if (txtName.getText().length() > 255) {
            lbMessage.setText("Name cannot be larger than 255 characters");
            return false;
        }

        if (txtEmail.getText().isEmpty()) {
            lbMessage.setText("Email cannot be empty");
            return false;
        } else if (txtEmail.getText().length() > 255) {
            lbMessage.setText("Email cannot be larger than 255 characters");
            return false;
        } else if (checkEmail(txtEmail.getText()) == false) {
            lbMessage.setText("Email is incorrect");
            return false;
        }

        if (txtNumberPhone.getText().isEmpty()) {
            lbMessage.setText("Number Phone cannot be empty");
            return false;
        } else if (txtNumberPhone.getText().length() > 255) {
            lbMessage.setText("Number cannot be larger than 255 characters");
            return false;
        } else if (chekcNumberPhone(txtNumberPhone.getText()) == false) {
            lbMessage.setText("Number Phone is incorrect");
            return false;
        }

        return true;
    }

    private boolean checkEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    private boolean chekcNumberPhone(String numberPhone) {
        String regex = "^(03|05|07|08|09)+([0-9]{8})$";
        return numberPhone.matches(regex);
    }

}
