/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoneshop.fxui.store;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.phoneshop.dao.SmartPhone;
import com.phoneshop.dao.SmartPhoneDAO;
import com.phoneshop.dao.SmartPhoneDAOImp;
import com.phoneshop.fxui.Navigator;
import com.phoneshop.model.UserName;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.util.Duration;


public class StoreUIController implements Initializable {

    private SmartPhoneDAO smartphonedao = new SmartPhoneDAOImp();

    private List<SmartPhone> neww;

    @FXML
    private Label txtUserName;

    @FXML
    private TilePane GpPhone;

    @FXML
    private MFXButton btnLogout;

    @FXML
    private TextField searchBar;

    @FXML
    private MFXButton btnSearch;

    @FXML
    private MFXButton HomeBtn;

    @FXML
    private MFXComboBox<String> cbbMfg;
    
    @FXML
    private MFXButton btnCart;

    // Trong StoreUIController

    @FXML
    private Label topBannerText;

    private int currentBannerIndex = 0;
    private Timeline bannerTimeline;

    private TranslateTransition marquee;


    @FXML
    private Button btnBannerLeft;

    @FXML
    private Button btnBannerRight;

    // Danh sách câu slogan/banner
    private List<String> bannerMessages = List.of(
            "SẢN PHẨM CHÍNH HÃNG - CAM KẾT LỖI ĐỔI LIỀN - HOTLINE 1900.2091",
            "THU CŨ GIÁ CAO TOÀN BỘ SẢN PHẨM",
            "MIỄN PHÍ VẬN CHUYỂN TOÀN QUỐC - HOÀN TIỀN 200% NẾU HÀNG GIẢ"
    );

    private final String[] bannerTexts = {
            "SẢN PHẨM CHÍNH HÃNG",
            "CAM KẾT LỖI ĐỔI LIỀN",
            "HOTLINE 1900.2091",
            "MIỄN PHÍ VẬN CHUYỂN TOÀN QUỐC"
    };


    @FXML
    void HomeClick(ActionEvent event) throws IOException {
        UserName.sbb = "";
        Navigator.getInstance().goToStore("");
    }

    @FXML
    void btnLogOut(ActionEvent event) throws IOException {
        UserName.sbb = "";
        Navigator.getInstance().goToLogin();
    }

    @FXML
    void btnSearchClick(ActionEvent event) {
        UserName.search = searchBar.getText();
        // Gọi hàm search sản phẩm thôi, không load lại Store.fxml
        filterProducts(UserName.search);
    }

    private void filterProducts(String keyword) {
        System.out.println("Filtering products by keyword: " + keyword);
        GpPhone.getChildren().clear();

        ObservableList<SmartPhone> products;

        if (keyword == null || keyword.trim().isEmpty()) {
            products = FXCollections.observableArrayList(smartphonedao.selectAlltoArray());
        } else {
            products = smartphonedao.selectByName(keyword);
            if (products.isEmpty()) {
                Warning(keyword);
            }
        }

        fill(products);
    }





    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Hiển thị tên người dùng
        txtUserName.setText(UserName.username);

        // Khởi động banner chạy
        topBannerText.setText(bannerMessages.get(currentBannerIndex));
        startMarquee();

        // Xử lý ComboBox nếu bạn còn dùng
        if (cbbMfg != null) {
            cbbMfg.setItems(smartphonedao.selectmanu());
            cbbMfg.setValue(UserName.sbb);
        }

        // Xác nhận CartID của user
        int id = smartphonedao.selectIdUser(UserName.username);
        if (!smartphonedao.ifnotexits(id)) {
            smartphonedao.addtocartforuser(id);
        }
        UserName.CartID = smartphonedao.selectCart(id);

        // ✅ Search bar luôn hiển thị keyword nếu có
        if (UserName.search != null && !UserName.search.isEmpty()) {
            searchBar.setText(UserName.search);
            filterProducts(UserName.search);
        } else {
            // Nếu không có keyword -> clear SearchBar và load ALL
            searchBar.clear();
            filterProducts("");
        }
    }



    private void fill(ObservableList<SmartPhone> neww) {
        try {
            GpPhone.getChildren().clear();
            if (neww == null || neww.isEmpty()) {
                System.out.println("No products to display.");
                return;
            }
            for (SmartPhone phone : neww) {
                FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/fxml/store/Product.fxml"));
                URL url = getClass().getResource("/fxml/store/Product.fxml");
                System.out.println("Product.fxml URL: " + url);

                AnchorPane anchorpane = fxmlloader.load();
                ProductController productController = fxmlloader.getController();
                productController.setData(phone);

                GpPhone.getChildren().add(anchorpane);
            }
            System.out.println("Loaded " + neww.size() + " products.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void btnCartClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToShoppingCart(UserName.CartID);
    }
    
    private void Warning(String name) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("không tìm thấy kết quả nào phù hợp với từ khóa " + name);
        alert.setTitle("Thông báo");
        alert.showAndWait();
    }

    @FXML
    private void onSortRelevant(ActionEvent e) {
        neww = smartphonedao.selectAlltoArray(); // lấy lại toàn bộ
        fill(FXCollections.observableArrayList(neww));
    }


    @FXML
    private void onSortNewest(ActionEvent e) {
        neww.sort((a, b) -> b.getProductID() - a.getProductID());
        fill(FXCollections.observableArrayList(neww));
    }

    @FXML
    private void onSortPriceAsc(ActionEvent e) {
        neww.sort((a, b) ->
                Double.compare(Double.parseDouble(a.getPrice()), Double.parseDouble(b.getPrice()))
        );
        fill(FXCollections.observableArrayList(neww));
    }


    @FXML
    private void onSortPriceDesc(ActionEvent e) {
        neww.sort((a, b) ->
                Double.compare(Double.parseDouble(b.getPrice()), Double.parseDouble(a.getPrice()))
        );
        fill(FXCollections.observableArrayList(neww));
    }


    @FXML
    private void onSortBestSeller(ActionEvent e) {
        // Nếu bạn chưa có field sold thì comment hoặc viết tạm
        System.out.println("Sort by best seller clicked");
        // neww.sort((a, b) -> b.getSold() - a.getSold());
        // fill(FXCollections.observableArrayList(neww));
    }

    private void startMarquee() {
        double bannerWidth = 1200; // Banner rộng 1200px
        double textWidth = topBannerText.getText().length() * 7; // Ước tính độ dài text

        marquee = new TranslateTransition(Duration.seconds(8), topBannerText);
        marquee.setFromX(bannerWidth);
        marquee.setToX(-textWidth);
        marquee.setCycleCount(TranslateTransition.INDEFINITE);
        marquee.play();
    }

    @FXML
    private void onBannerLeftClick() {
        marquee.pause();
        marquee.setRate(-1); // Chạy ngược
        marquee.play();
    }

    @FXML
    private void onBannerRightClick() {
        marquee.pause();
        marquee.setRate(1); // Chạy thuận
        marquee.play();
    }

    private void updateBannerText() {
        topBannerText.setText(bannerTexts[currentBannerIndex]);
        currentBannerIndex = (currentBannerIndex + 1) % bannerTexts.length;
    }

    private void startBannerAnimation() {
        bannerTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> updateBannerText()),
                new KeyFrame(Duration.seconds(1.5))
        );
        bannerTimeline.setCycleCount(Animation.INDEFINITE);
        bannerTimeline.play();
    }




}
