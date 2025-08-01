/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoneshop.fxui.store;



import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import com.phoneshop.dao.SmartPhone;
import com.phoneshop.dao.SmartPhoneDAO;
import com.phoneshop.dao.SmartPhoneDAOImp;
import com.phoneshop.fxui.Navigator;
import com.phoneshop.model.UserName;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;


public class ProductView2Controller implements Initializable {

    private SmartPhoneDAO smartphonedao = new SmartPhoneDAOImp();

    private SmartPhone SmartPhone = null;

    @FXML
    private ImageView Img;

    @FXML
    private HBox imageGallery;

    @FXML
    private TextField searchBar;

    @FXML
    private Label txtUserName;

    @FXML
    private MFXButton btnLogout;

    @FXML
    private MFXButton btnSearch;

    @FXML
    private MFXButton HomeBtn;

    @FXML
    private MFXButton btnBack;
    
    @FXML
    private MFXButton btnCart;

    @FXML
    private Label lbName;

    @FXML
    private Label lbPrice;

    @FXML
    private Label lbScreen;

    @FXML
    private Label lbCamera;

    @FXML
    private Label lbSystem;

    @FXML
    private Label lbChip;

    @FXML
    private Label lbMemory;

    @FXML
    private Label lbBattery;
    
    @FXML
    private MFXButton btnAddToCart;
    
    @FXML
    private Spinner<Integer> Spinner;
    
    @FXML
    private MFXButton btnCartClick;

    @FXML
    private MFXComboBox<String> cbbMfg;

    @FXML
    void HomeClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToStore("");
    }

    @FXML
    void btnBackClick(ActionEvent event) throws IOException {
        if (!UserName.search.isEmpty()) {
            Navigator.getInstance().goToStore(UserName.search);
        } else if (!UserName.sbb.isEmpty()) {
            Navigator.getInstance().goToStore("");
        } else {
            Navigator.getInstance().goToStore("");
        }
    }

    @FXML
    void btnLogOut(ActionEvent event) throws IOException {
        Navigator.getInstance().goToLogin();
    }

    @FXML
    void btnSearchClick(ActionEvent event) throws IOException {
        UserName.sbb = "";
        Navigator.getInstance().goToStore(searchBar.getText());
        UserName.search = searchBar.getText();
    }
    
    @FXML
    void cbbMfgClick(ActionEvent event) throws IOException {
        UserName.sbb = cbbMfg.getValue().toString();
        Navigator.getInstance().goToStore("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtUserName.setText(UserName.username);
        cbbMfg.setItems(smartphonedao.selectmanu());
        cbbMfg.setValue(UserName.sbb);
        int initialValue = 1;
        SpinnerValueFactory<Integer> valueFactory
                = //
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99999, initialValue);

        Spinner.setValueFactory(valueFactory);
        System.out.println("#ProductView2Controller initialized!");
    }



    public void setSmartPhoneByName(String name) {
        ObservableList<SmartPhone> smartphone = smartphonedao.selectByName(name);

        SmartPhone phone = smartphone.get(0);
        this.SmartPhone = phone;
        UserName.id = phone.getProductID();

        // ✅ Ảnh chính
        try {
            String firstImage = phone.getFirstImageLink();
            InputStream is = getClass().getClassLoader().getResourceAsStream(firstImage);
            if (is != null) {
                Img.setImage(new Image(is));
            }
        } catch (Exception e) {
            System.out.println("Không load được ảnh chính: " + e.getMessage());
        }

        // ✅ Ảnh phụ
        imageGallery.getChildren().clear();
        for (String link : phone.getImageLinks()) {
            try {
                InputStream is = getClass().getClassLoader().getResourceAsStream(link);
                if (is != null) {
                    Image img = new Image(is, 100, 100, true, true);
                    ImageView imgView = new ImageView(img);
                    imgView.setFitHeight(100);
                    imgView.setFitWidth(100);
                    imgView.setPreserveRatio(true);

                    imgView.setOnMouseClicked(e -> {
                        try {
                            Img.setImage(new Image(getClass().getClassLoader().getResourceAsStream(link)));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });

                    imageGallery.getChildren().add(imgView);
                }
            } catch (Exception e) {
                System.out.println("Lỗi ảnh phụ: " + link);
            }
        }

        // ✅ Thông tin sản phẩm
        lbName.setText(phone.getName());
        lbPrice.setText(phone.getPrice() + "$");
        lbScreen.setText(phone.getScreen());
        lbCamera.setText(phone.getCamera());
        lbSystem.setText(phone.getSystem());
        lbChip.setText(phone.getChip());
        lbMemory.setText(phone.getMemory());
        lbBattery.setText(phone.getBattery());
    }


    @FXML
    private void btnAddToCartClick(ActionEvent event) throws IOException {
        try {
            if (!smartphonedao.addamountifexit(UserName.CartID, UserName.id)) {
                smartphonedao.addtocartdetail(UserName.CartID, UserName.id, Spinner.getValue());
                warning4();
            } else {
                int newamount = smartphonedao.selectAmount(UserName.CartID, UserName.id);
                smartphonedao.updateCart(UserName.CartID, UserName.id, newamount + Spinner.getValue());
                warning4();
            };
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void warning4() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Sản phẩm đã được thêm vào giỏ hàng");
        alert.setTitle("Hoàn thành"); 
        alert.showAndWait();
    }

    @FXML
    private void btnCartClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToShoppingCart(UserName.CartID);
    }


}
