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
import java.util.Timer;
import java.util.TimerTask;

import com.phoneshop.dao.Account;
import com.phoneshop.dao.AccountDAO;
import com.phoneshop.dao.AccountDAOImp;
import com.phoneshop.fxui.Navigator;
import com.phoneshop.model.UserName;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

/**
 *
 * @author Admin
 */
public class LoginController implements Initializable {

    private AccountDAO accountdao = new AccountDAOImp();

    @FXML
    private MFXTextField txtUserName;

    @FXML
    private MFXButton btnSignUp;

    @FXML
    private MFXPasswordField txtPassword;

    @FXML
    private MFXButton btnLogin;

    @FXML
    private AnchorPane root;

    public void initialize(URL location, ResourceBundle resources) {
        txtUserName.setText("");
        txtPassword.setText("");
    }

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

            // Hiển thị loading screen
            showLoadingScreen();

            // Gọi DAO: check theo username + password đã hash
            Account dbAcc = accountdao.check(inputAcc.getUserName(), inputAcc.getPassword());

            if (dbAcc != null) {
                // Đăng nhập thành công
                int logbyID = dbAcc.getLogBy(); // 1: admin, 2: user

                // Gán thông tin đăng nhập toàn cục
                UserName.username = dbAcc.getUserName();
                UserName.name = dbAcc.getName();

                // Delay để hiển thị loading
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            try {
                                if (logbyID == 1) {
                                    Navigator.getInstance().goToAdminIndex();
                                } else if (logbyID == 2) {
                                    Navigator.getInstance().goToStore("");
                                } else {
                                    warning4(); // Nếu logbyID khác giá trị dự kiến
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }, 2000); // 2 giây delay

            } else {
                // Ẩn loading screen nếu đăng nhập thất bại
                hideLoadingScreen();
                warning2(); // Sai tài khoản hoặc mật khẩu
            }

        } catch (Exception e) {
            hideLoadingScreen();
            e.printStackTrace();
            warning2();
        }
    }

    private void showLoadingScreen() {
        try {
            System.out.println("🔍 Showing loading screen...");
            
            // Tạo loading screen đơn giản
            VBox loadingContainer = new VBox(20);
            loadingContainer.setAlignment(javafx.geometry.Pos.CENTER);
            loadingContainer.setStyle("-fx-background-color: rgba(255,255,255,0.95); -fx-background-radius: 25; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 30, 0, 0, 10); -fx-padding: 40;");
            loadingContainer.setPrefSize(500, 400);

            // Logo
            ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/images/logo6.png")));
            logo.setFitHeight(80);
            logo.setFitWidth(80);
            logo.setPreserveRatio(true);

            // Title
            Label title = new Label("MOBILESHOP");
            title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #1f2937;");

            Label subtitle = new Label("Đang xác thực thông tin...");
            subtitle.setStyle("-fx-font-size: 16px; -fx-text-fill: #6b7280;");

            // Progress indicator
            ProgressIndicator progress = new ProgressIndicator();
            progress.setStyle("-fx-progress-color: linear-gradient(to right, #667eea 0%, #764ba2 100%);");
            progress.setPrefSize(100, 100);

            // Loading text
            Label loadingText = new Label("Vui lòng chờ trong giây lát");
            loadingText.setStyle("-fx-font-size: 14px; -fx-text-fill: #6b7280;");

            // Add all elements
            loadingContainer.getChildren().addAll(logo, title, subtitle, progress, loadingText);

            // Tạo StackPane để overlay loading screen
            StackPane overlay = new StackPane();
            overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
            overlay.getChildren().add(loadingContainer);
            
            // Thêm vào root và căn giữa
            if (root != null) {
                System.out.println("✅ Root found, adding loading overlay");
                root.getChildren().add(overlay);
                
                // Căn overlay ra giữa màn hình
                AnchorPane.setTopAnchor(overlay, 0.0);
                AnchorPane.setBottomAnchor(overlay, 0.0);
                AnchorPane.setLeftAnchor(overlay, 0.0);
                AnchorPane.setRightAnchor(overlay, 0.0);
                
                // Căn loadingContainer ra giữa overlay
                StackPane.setAlignment(loadingContainer, javafx.geometry.Pos.CENTER);
                
                System.out.println("✅ Loading screen added successfully and centered");
            } else {
                System.out.println("❌ Root is null, cannot show loading screen");
            }
        } catch (Exception e) {
            System.out.println("❌ Error showing loading screen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void hideLoadingScreen() {
        try {
            if (root != null && root.getChildren().size() > 1) {
                System.out.println("🔍 Hiding loading screen...");
                root.getChildren().remove(root.getChildren().size() - 1);
                System.out.println("✅ Loading screen hidden successfully");
            }
        } catch (Exception e) {
            System.out.println("❌ Error hiding loading screen: " + e.getMessage());
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
