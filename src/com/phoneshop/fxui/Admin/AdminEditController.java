/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoneshop.fxui.Admin;


import com.phoneshop.dao.Account;
import com.phoneshop.dao.Admin;
import com.phoneshop.dao.AdminDAO;
import com.phoneshop.dao.AdminDAOImp;
import com.phoneshop.fxui.Navigator;
import com.phoneshop.model.UserName;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 *
 * @author Admin
 */
public class AdminEditController implements Initializable {

    private AdminDAO admindao = new AdminDAOImp();
    
    private Account a = new Account();

    private Admin editAdmin = null;

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
    private MFXPasswordField txtNewPassword;

    @FXML
    private MFXTextField txtName;

    @FXML
    private MFXTextField txtEmail;

    @FXML
    private MFXTextField txtNumberPhone;

    @FXML
    private Label lbMessage;

    @FXML
    private MFXButton btnClear;

    @FXML
    private MFXButton btnNew;

    @FXML
    private MFXButton btnSave;

    @FXML
    private MFXButton btnReset;

    @FXML
    private MFXPasswordField txtOldPassword;

    @FXML
    private MFXButton btnAdmin;

    @FXML
    private MFXButton btnSmartPhone;

    @FXML
    private MFXButton btnMfg;

    @FXML
    private MFXButton btnImage;
    
    @FXML
    private MFXButton btnOrder;

    @FXML
    private MFXButton LogOut;
    
    @FXML
    private Label txtUsername;
    
    @FXML
    void btnOrderClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToOrder();
    }

    @FXML
    void btnAdminClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToAdminIndex();
    }
    
    @FXML
    void btnLogoutClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToLogin();
    }

    @FXML
    void btnClear(ActionEvent event) {
        txtAccount.setText("");
        txtOldPassword.setText("");
        txtNewPassword.setText("");
        txtName.setText("");
        txtEmail.setText("");
        txtNumberPhone.setText("");
    }

    @FXML
    void btnNewClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToAdminIndex();
    }

    @FXML
    void btnMfgClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToManufacturerIndex();
    }

    @FXML
    void btnImageClick(ActionEvent event) {

    }

    @FXML
    void btnResetClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToAdminIndex();
    }

    @FXML
    void btnSaveClick(ActionEvent event) {
        if (check()) {
            try {
                Admin updateAdmin = news();
                updateAdmin.setId(this.editAdmin.getId());

                boolean result = admindao.update(updateAdmin);

                if (result) {
                    lbMessage.setText("update successfully");
                    Navigator.getInstance().goToAdminIndex();
                } else {
                    lbMessage.setText("update unsuccessfully");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    void btnSmartPhoneClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToSmartPhoneIndex(); 
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.out.println("#IndexUIController initialized!");
        ObservableList<Admin> admin = admindao.selectAll();
        tvAdmin.setItems(admin);
        txtUsername.setText(UserName.username);

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

    public void initialize(Admin editAdmin) {
        this.editAdmin = editAdmin;

        txtAccount.setText(editAdmin.getAccount());
        txtName.setText(editAdmin.getName());
        txtEmail.setText(editAdmin.getEmail());
        txtNumberPhone.setText(editAdmin.getNumberPhone());

        lbMessage.setText("Update SmartPhone");
    }

    private Admin news() {
        Admin admin = new Admin();
        admin.setAccount(txtAccount.getText());
        admin.setPassword(txtNewPassword.getText());
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

        if (txtOldPassword.getText().isEmpty()) {
            lbMessage.setText("Password cannot be empty");
            return false;
        }
        
        if (txtNewPassword.getText().isEmpty()) {
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
