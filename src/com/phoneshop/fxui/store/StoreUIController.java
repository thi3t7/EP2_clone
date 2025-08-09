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

// ... imports giữ nguyên

public class StoreUIController implements Initializable {

    public MFXButton btnLogOut;
    private final SmartPhoneDAO smartphonedao = new SmartPhoneDAOImp();

    // ❌ bỏ: private List<SmartPhone> neww;
    private final ObservableList<SmartPhone> productList = FXCollections.observableArrayList();

    @FXML private Label txtUserName;
    @FXML private TilePane GpPhone;
    @FXML private MFXButton btnLogout;
    @FXML private TextField searchBar;
    @FXML private MFXButton btnSearch;
    @FXML private MFXButton HomeBtn;
    @FXML private MFXComboBox<String> cbbMfg;
    @FXML private MFXButton btnCart;
    @FXML private Label topBannerText;

    private int currentBannerIndex = 0;
    private Timeline bannerTimeline;
    private TranslateTransition marquee;

    @FXML private Button btnPrev;
    @FXML private Button btnNext;
    @FXML private ImageView bannerImage;

    private final List<String> bannerMessages = List.of(
            "SẢN PHẨM CHÍNH HÃNG - CAM KẾT LỖI ĐỔI LIỀN - HOTLINE 1900.2091",
            "THU CŨ GIÁ CAO TOÀN BỘ SẢN PHẨM",
            "MIỄN PHÍ VẬN CHUYỂN TOÀN QUỐC - HOÀN TIỀN 200% NẾU HÀNG GIẢ"
    );

    private final List<Image> bannerImages = new ArrayList<>();
    private int currentImageIndex = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtUserName.setText(UserName.username);

        topBannerText.setText(bannerMessages.get(currentBannerIndex));
        startMarquee();

        if (cbbMfg != null) {
            cbbMfg.setItems(smartphonedao.selectmanu());
            cbbMfg.setValue(UserName.sbb);
        }

        int id = smartphonedao.selectIdUser(UserName.username);
        if (!smartphonedao.ifnotexits(id)) {
            smartphonedao.addtocartforuser(id);
        }
        UserName.CartID = smartphonedao.selectCart(id);

        // Load lần đầu
        String kw = (UserName.search == null) ? "" : UserName.search;
        searchBar.setText(kw);
        filterProducts(kw);

        initializeBannerImages();
    }

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
        GpPhone.getChildren().clear();

        List<SmartPhone> source;
        if (keyword == null || keyword.trim().isEmpty()) {
            source = smartphonedao.selectAlltoArray();
        } else {
            ObservableList<SmartPhone> found = smartphonedao.selectByName(keyword);
            source = new ArrayList<>(found);
            if (source.isEmpty()) Warning(keyword);
        }

        productList.setAll(source);
        fill(productList);
    }

    private void fill(List<SmartPhone> list) {
        try {
            GpPhone.getChildren().clear();
            if (list == null || list.isEmpty()) return;

            for (SmartPhone phone : list) {
                FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/fxml/store/Product.fxml"));
                AnchorPane anchorpane = fxmlloader.load();

                ProductController pc = fxmlloader.getController();
                if (pc != null) {
                    pc.setData(phone);
                    anchorpane.setOnMouseClicked(e -> {
                        try { Navigator.getInstance().goToProductView2(phone.getName()); }
                        catch (IOException ex) { ex.printStackTrace(); }
                    });
                }
                GpPhone.getChildren().add(anchorpane);
            }
            System.out.println("Loaded " + list.size() + " products.");
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

    // ✅ Giữ lại nếu bạn vẫn muốn sắp xếp theo giá, không còn phụ thuộc biến neww
    @FXML
    private void onSortPriceAsc(ActionEvent e) {
        productList.sort((a, b) -> parsePrice(a.getPrice()) - parsePrice(b.getPrice()));
        fill(productList);
    }

    @FXML
    private void onSortPriceDesc(ActionEvent e) {
        productList.sort((a, b) -> parsePrice(b.getPrice()) - parsePrice(a.getPrice()));
        fill(productList);
    }

    private int parsePrice(String s) {
        // loại bỏ ký tự không phải số (VD: "1$" -> 1)
        return Integer.parseInt(s.replaceAll("\\D", ""));
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

    @FXML private void onBannerLeftClick() { marquee.pause(); marquee.setRate(-1); marquee.play(); }
    @FXML private void onBannerRightClick(){ marquee.pause(); marquee.setRate(1);  marquee.play(); }

    private void initializeBannerImages() {
        String[] bannerPaths = {"/images/banner1.jpg", "/images/banner2.jpg"};
        bannerImages.clear();
        for (String path : bannerPaths) {
            InputStream in = getClass().getResourceAsStream(path);
            if (in == null) { System.err.println("Resource not found: " + path); continue; }
            Image image = new Image(in);
            if (!image.isError()) bannerImages.add(image);
        }
        if (!bannerImages.isEmpty() && bannerImage != null) {
            bannerImage.setImage(bannerImages.get(currentImageIndex));
        }
    }

    @FXML private void prevBannerImage() {
        if (!bannerImages.isEmpty() && bannerImage != null) {
            currentImageIndex = (currentImageIndex - 1 + bannerImages.size()) % bannerImages.size();
            bannerImage.setImage(bannerImages.get(currentImageIndex));
        }
    }

    @FXML private void nextBannerImage() {
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
