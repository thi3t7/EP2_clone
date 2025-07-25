/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoneshop.fxui.login;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.phoneshop.dao.Account;
import com.phoneshop.dao.AccountDAO;
import com.phoneshop.dao.AccountDAOImp;
import com.phoneshop.fxui.Navigator;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class SignUpUIController implements Initializable {

    private AccountDAO accountdao = new AccountDAOImp();

    @FXML
    private MFXTextField txtUserName;

    @FXML
    private MFXPasswordField txtPassword;

    @FXML
    private MFXButton btnSubmit;

    @FXML
    private MFXButton btnBack;

    @FXML
    private MFXTextField txtName;

    @FXML
    private MFXTextField txtPhoneNum;

    @FXML
    private MFXTextField txtEmail;

    @FXML
    private Label lbMessage1;
    
    @FXML
    private Label lbMessage2;
    
    @FXML
    private Label lbMessage3;
    
    @FXML
    private Label lbMessage4;
    
    @FXML
    private Label lbMessage5;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void btnSubmitClick(ActionEvent event) throws Exception {
        if (check()) {
            try {
                Account newMem = extractFromFields();
                System.out.println(newMem.getName());
                newMem = accountdao.insertNewMem(newMem);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            Success();
            Navigator.getInstance().goToLogin();
        }
    }

    @FXML
    private void btnBackClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToLogin();
    }

    private Account extractFromFields() throws Exception {
        Account account = new Account();
        account.setUserName(txtUserName.getText());
        account.setPassword(txtPassword.getText());
        account.setName(txtName.getText());
        account.setNumberPhone(txtPhoneNum.getText());
        account.setEmail(txtEmail.getText());

        return account;
    }

    private void Success() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Thank you for Sign Up ");
        alert.showAndWait();
    }

    private boolean check() {
        
        boolean check = true;

        //UserName
        if (txtUserName.getText().isEmpty()) {
            lbMessage1.setText("Bạn cần nhập tài khoản");
            check = false;
        } else if (txtUserName.getText().length() > 50) {
            lbMessage1.setText("Tài khoản không được nhiều hơn 50 ký tự");
            check = false; 
        } else if (!txtUserName.getText().isEmpty()) {
            lbMessage1.setText("");
            check = true;
        } else if (!(txtUserName.getText().length() > 50)) {
            lbMessage1.setText("");
            check = true;
        }

        //Password
        if (txtPassword.getText().isEmpty()) {
            lbMessage2.setText("Bạn cần nhập mật khẩu");
            check = false;
        } else if (!txtPassword.getText().isEmpty()) {
            lbMessage2.setText("");
            check = true;
        }

        //Name
        if (txtName.getText().isEmpty()) {
            lbMessage3.setText("Bạn cần nhập Họ và tên");
            check = false;
        } else if (txtName.getText().length() > 255) {
            lbMessage3.setText("Họ và tên không được nhiều hơn 255 ký tự");
            check = false;
        } else if (!txtName.getText().isEmpty()) {
            lbMessage3.setText("");
            check = true;
        } else if (!(txtName.getText().length() > 255)) {
            lbMessage3.setText("");
            check = true;
        }

        //PhoneNum
        if (txtPhoneNum.getText().isEmpty()) {
            lbMessage4.setText("Bạn cần nhập số điện thoại");
            check = false;
        } else if (txtPhoneNum.getText().length() > 255) {
            lbMessage4.setText("Số điện thoại không được nhiều hơn 255 ký tự");
            check = false;
        } else if (chekcNumberPhone(txtPhoneNum.getText()) == false) {
            lbMessage4.setText("Số điện thoại không chính xác");
            check = false;
        } else if (!txtPhoneNum.getText().isEmpty()) {
            lbMessage4.setText("");
            check = true;
        } else if (!(txtPhoneNum.getText().length() > 255)) {
            lbMessage4.setText("");
            check = true;
        } else if (!(chekcNumberPhone(txtPhoneNum.getText()) == false)) {
            lbMessage4.setText("");
            check = true;
        }
        
        //Email
        if (txtEmail.getText().isEmpty()) {
            lbMessage5.setText("Bạn cần nhập Email");
            check = false;
        } else if (txtEmail.getText().length() > 255) {
            lbMessage5.setText("Email không được nhiều hơn 255 ký tự");
            check = false;
        } else if (checkEmail(txtEmail.getText()) == false) {
            lbMessage5.setText("Email không chính xác");
            check = false;
        } else if (!txtEmail.getText().isEmpty()) {
            lbMessage5.setText("");
            check = true;
        } else if (!(txtEmail.getText().length() > 255)) {
            lbMessage5.setText("");
            check = true;
        } else if (!(checkEmail(txtEmail.getText()) == false)) {
            lbMessage5.setText("");
            check = true;
        }

        return check;
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
