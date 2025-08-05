package com.phoneshop.fxui.store;

import java.io.IOException;
import java.io.InputStream;
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
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.util.Duration;
import javafx.fxml.FXMLLoader;

public class StoreUIController implements Initializable {

    public MFXButton btnLogOut;
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

    @FXML
    private Label topBannerText;

    private int currentBannerIndex = 0;
    private Timeline bannerTimeline;

    private TranslateTransition marquee;

    @FXML
    private Button btnPrev; // Nút mũi tên trái cho banner ảnh

    @FXML
    private Button btnNext; // Nút mũi tên phải cho banner ảnh

    @FXML
    private ImageView bannerImage; // Hiển thị ảnh banner

    // Danh sách câu slogan/banner text
    private List<String> bannerMessages = List.of(
            "SẢN PHẨM CHÍNH HÃNG - CAM KẾT LỖI ĐỔI LIỀN - HOTLINE 1900.2091",
            "THU CŨ GIÁ CAO TOÀN BỘ SẢN PHẨM",
            "MIỄN PHÍ VẬN CHUYỂN TOÀN QUỐC - HOÀN TIỀN 200% NẾU HÀNG GIẢ"
    );

    // Danh sách ảnh banner
    private List<Image> bannerImages = new ArrayList<>();
    private int currentImageIndex = 0;

    @FXML
    void HomeClick(ActionEvent event) throws IOException {
        UserName.sbb = "";
        Navigator.getInstance().goToStore("");
    }


    @FXML
    void btnSearchClick(ActionEvent event) {
        UserName.search = searchBar.getText();
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

        // Khởi động banner text chạy
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
            searchBar.clear();
            filterProducts("");
        }

        // Khởi tạo banner ảnh
        initializeBannerImages();
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
                AnchorPane anchorpane = fxmlloader.load();

                ProductController productController = fxmlloader.getController();

                if (productController == null) {
                    System.out.println("⚠ Controller của Product.fxml bị null!");
                } else {
                    productController.setData(phone);
                    anchorpane.setOnMouseClicked(event -> {
                        try {
                            Navigator.getInstance().goToProductView2(phone.getName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }

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
        neww = smartphonedao.selectAlltoArray();
        fill(FXCollections.observableArrayList(neww));
    }

    @FXML
    private void onSortNewest(ActionEvent e) {
        neww.sort((a, b) -> b.getProductID() - a.getProductID());
        fill(FXCollections.observableArrayList(neww));
    }

    @FXML
    private void onSortPriceAsc(ActionEvent e) {
        neww.sort((a, b) -> Double.compare(Double.parseDouble(a.getPrice()), Double.parseDouble(b.getPrice())));
        fill(FXCollections.observableArrayList(neww));
    }

    @FXML
    private void onSortPriceDesc(ActionEvent e) {
        neww.sort((a, b) -> Double.compare(Double.parseDouble(b.getPrice()), Double.parseDouble(a.getPrice())));
        fill(FXCollections.observableArrayList(neww));
    }

    @FXML
    private void onSortBestSeller(ActionEvent e) {
        System.out.println("Sort by best seller clicked");
    }

    private void startMarquee() {
        double bannerWidth = 1200;
        double textWidth = topBannerText.getText().length() * 7;

        marquee = new TranslateTransition(Duration.seconds(8), topBannerText);
        marquee.setFromX(bannerWidth);
        marquee.setToX(-textWidth);
        marquee.setCycleCount(TranslateTransition.INDEFINITE);
        marquee.play();
    }

    @FXML
    private void onBannerLeftClick() {
        marquee.pause();
        marquee.setRate(-1);
        marquee.play();
    }

    @FXML
    private void onBannerRightClick() {
        marquee.pause();
        marquee.setRate(1);
        marquee.play();
    }

    private void initializeBannerImages() {
        // Thêm các ảnh banner (đường dẫn tương đối trong resources/images)
        String[] bannerPaths = {"/images/banner1.jpg", "/images/banner2.jpg"};
        bannerImages.clear(); // Xóa danh sách cũ để tránh trùng lặp
        for (String path : bannerPaths) {
            InputStream inputStream = getClass().getResourceAsStream(path);
            if (inputStream == null) {
                System.err.println("Resource not found: " + path);
                continue; // Bỏ qua file không tìm thấy và tiếp tục với file khác
            }
            Image image = new Image(inputStream);
            if (image != null && !image.isError()) {
                bannerImages.add(image);
            } else {
                System.err.println("Failed to load image: " + path);
            }
        }
        if (!bannerImages.isEmpty() && bannerImage != null) {
            bannerImage.setImage(bannerImages.get(currentImageIndex));
        } else {
            System.err.println("No valid banner images loaded.");
        }
    }

    @FXML
    private void prevBannerImage() {
        if (!bannerImages.isEmpty() && bannerImage != null) {
            currentImageIndex = (currentImageIndex - 1 + bannerImages.size()) % bannerImages.size();
            bannerImage.setImage(bannerImages.get(currentImageIndex));
        }
    }

    @FXML
    private void nextBannerImage() {
        if (!bannerImages.isEmpty() && bannerImage != null) {
            currentImageIndex = (currentImageIndex + 1) % bannerImages.size();
            bannerImage.setImage(bannerImages.get(currentImageIndex));
        }
    }

    @FXML
    void btnLogOut(ActionEvent event) throws IOException {
        Navigator.getInstance().goToLogin();
    }

}