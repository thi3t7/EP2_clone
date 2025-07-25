/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoneshop.fxui.login;


import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.phoneshop.dao.Account;
import com.phoneshop.dao.AccountDAO;
import com.phoneshop.dao.AccountDAOImp;
import com.phoneshop.fxui.Navigator;
import com.phoneshop.model.UserName;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

/**
 *
 * @author Admin
 */
public class LoginController implements Initializable {

    private AccountDAO accountdao = new AccountDAOImp();

    ObservableList<String> options
            = FXCollections.observableArrayList(
                    "Quản trị viên",
                    "Khách hàng"
            );

    @FXML
    private MFXTextField txtUserName;

    @FXML
    MFXComboBox<String> comboBox;

    @FXML
    private MFXButton btnSignUp;

    public void initialize(URL location, ResourceBundle resources) {
        comboBox.setItems(options);
        txtUserName.setText("");
        txtPassword.setText("");
    }

    @FXML
    private MFXPasswordField txtPassword;

    @FXML
    private MFXButton btnLogin;

    @FXML
    void btnLoginClick(ActionEvent event) throws SQLException, IOException {
        try {
            // Lấy dữ liệu người dùng nhập
            Account inputAcc = extractFromFields();

            // Validate input: bắt buộc phải nhập
            if (inputAcc.getUserName().isEmpty() || inputAcc.getPassword().isEmpty()) {
                warning1(); // Thiếu tài khoản hoặc mật khẩu
                return;
            }

            // Gọi DAO: check theo username + password đã hash
            Account dbAcc = accountdao.check(inputAcc.getUserName(), inputAcc.getPassword());

            if (dbAcc != null) {
                // Đăng nhập thành công
                int logbyID = dbAcc.getLogBy(); // 1: admin, 2: user

                // Gán thông tin đăng nhập toàn cục
                UserName.username = dbAcc.getUserName();
                UserName.name = dbAcc.getName();

                if (logbyID == 1) {
                    Navigator.getInstance().goToAdminIndex();
                } else if (logbyID == 2) {
                    Navigator.getInstance().goToWelcome();
                } else {
                    warning4(); // Nếu logbyID khác giá trị dự kiến
                }

            } else {
                warning2(); // Sai tài khoản hoặc mật khẩu
            }

        } catch (Exception e) {
            e.printStackTrace();
            warning2();
        }
    }



    private Account extractFromFields() {
        Account account = new Account();
        account.setUserName(txtUserName.getText());
        account.setPassword(txtPassword.getText());

        return account;
    }

    private void warning1() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Hãy nhập tài khoản và mật khẩu");
        alert.setTitle("WARNING");
        alert.showAndWait();
    }

    private void warning2() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Bạn đã nhập sai tài khoản hoặc mật khẩu. Vui lòng nhập lại!");
        alert.setTitle("WARNING");
        alert.showAndWait();
    }

    private void warning3() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Vui lòng chọn đăng nhập bởi Quản trị viên hoặc Khách hàng");
        alert.setTitle("WARNING");
        alert.showAndWait();
    }

    private void warning4() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Bạn không phải là quản trị viên");
        alert.setTitle("WARNING");
        alert.showAndWait();
    }

    private boolean CheckPass(String inputPassword, String hashPassWord) {
        String myChecksum = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(inputPassword.getBytes());
            byte[] digest = md.digest();

            // Chuyển mảng byte thành chuỗi HEX (giống printHexBinary)
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02X", b));  // viết hoa, tương tự .toUpperCase()
            }
            myChecksum = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }

        return hashPassWord.equals(myChecksum);
    }


    @FXML
    private void btnSignUpClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToSignUp();
    }

//    private void loading() throws IOException, InterruptedException {
//        FXMLLoader fxmlloader = new FXMLLoader();
//        fxmlloader.setLocation(getClass().getResource("Splash.fxml"));
//        AnchorPane anchorpane = fxmlloader.load();
//        SplashController sp = fxmlloader.getController();
//        splash.add(anchorpane, 0, 1);
//        GridPane.setMargin(anchorpane, new Insets(10));
//
//        sp.initialize();
//        Timer myTimer = new Timer();
//        myTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Platform.runLater(() -> {
//                    try {
//                        Navigator.getInstance().goToStore("");
//                    } catch (Exception ex) {
//                        System.out.println(ex.getMessage());
//                    }
//                });
//            }
//        }, 1000);
//    }
}
