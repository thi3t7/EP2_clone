package com.phoneshop.fxui.store;

import com.phoneshop.dao.SmartPhone;
import com.phoneshop.dao.SmartPhoneDAO;
import com.phoneshop.dao.SmartPhoneDAOImp;
import com.phoneshop.fxui.Navigator;
import com.phoneshop.model.UserName;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ShoppingCartController implements Initializable {

    private final SmartPhoneDAO smartphoneDAO = new SmartPhoneDAOImp();

    @FXML private TextField searchBar;
    @FXML private Label txtUserName;
    @FXML private ImageView Img;
    @FXML private Label totalLabel;   // ✅ Dùng label này cho tổng tiền
    @FXML private MFXButton btnBack;
    @FXML private MFXButton btnLogout;
    @FXML private Label topBannerText;
    @FXML private VBox cartList;
    @FXML private ComboBox<String> paymentMethodComboBox;
    @FXML private MFXButton btnCheckout;

    private final SimpleIntegerProperty total = new SimpleIntegerProperty(0);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtUserName.setText(UserName.username);
        System.out.println("🛒 Cart ID: " + UserName.CartID);

        // ✅ Bind tổng tiền với label
        total.addListener((obs, oldVal, newVal) ->
                totalLabel.setText(String.format("%,d VNĐ", newVal.intValue()))
        );

        // ✅ Thêm phương thức thanh toán vào ComboBox
        paymentMethodComboBox.getItems().addAll(
                "Thanh toán khi nhận hàng (COD)",
                "Chuyển khoản ngân hàng",
                "Ví điện tử (Momo, ZaloPay)"
        );
        paymentMethodComboBox.getSelectionModel().selectFirst();

        // ✅ Load giỏ hàng
        loadCartItems(UserName.CartID);
    }

    private void loadCartItems(int cartId) {
        cartList.getChildren().clear(); // ✅ thay vì GpPhone.getChildren().clear();
        ObservableList<SmartPhone> items = smartphoneDAO.selectAllCart(cartId);
        total.set(0); // Reset tổng tiền

        try {
            for (SmartPhone phone : items) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/store/ProductInfo.fxml"));
                AnchorPane pane = loader.load();

                ProductInfoController ctrl = loader.getController();
                ctrl.setData(phone);
                ctrl.initialize(phone.getAmount());

                // Checkbox mặc định bỏ chọn
                ctrl.checkbox.setSelected(false);

                // Tick chọn sản phẩm
                ctrl.checkbox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                    int itemTotal = parsePrice(ctrl.total_price.getText());
                    if (newVal) total.set(total.get() + itemTotal);
                    else total.set(total.get() - itemTotal);
                });

                // Thay đổi số lượng khi tick chọn
                ctrl.amount.valueProperty().addListener((obs, oldVal, newVal) -> {
                    int price = parsePrice(ctrl.price.getText());
                    ctrl.total_price.setText((newVal * price) + " VNĐ");
                    if (ctrl.checkbox.isSelected()) {
                        int delta = (newVal - oldVal) * price;
                        total.set(total.get() + delta);
                    }
                });

                cartList.getChildren().add(pane); // ✅ thêm vào cartList
            }
        } catch (IOException e) {
            System.out.println("❌ Error loading cart items: " + e.getMessage());
        }
    }

    private int parsePrice(String priceText) {
        return Integer.parseInt(priceText.replaceAll("[^\\d]", ""));
    }

//    private void setGridSize() {
//        GpPhone.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
//        GpPhone.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
//        GpPhone.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
//    }

    // ===== Sự kiện người dùng =====

    @FXML
    private void btnSearchClick(ActionEvent e) throws IOException {
        UserName.search = searchBar.getText();
        Navigator.getInstance().goToStore(UserName.search);
    }

    @FXML
    private void btnCheckoutClick(ActionEvent e) throws IOException {
        if (total.get() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Bạn chưa chọn sản phẩm nào!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        String paymentMethod = paymentMethodComboBox.getValue();
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn đã chọn phương thức: " + paymentMethod +
                        "\nTổng tiền: " + totalLabel.getText() +
                        "\n\nXác nhận thanh toán?",
                ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Xác nhận thanh toán");
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            smartphoneDAO.ordered(UserName.CartID);
            Alert success = new Alert(Alert.AlertType.INFORMATION, "Thanh toán thành công!", ButtonType.OK);
            success.showAndWait();
            Navigator.getInstance().goToLogin(); // Chuyển về store hoặc trang khác
        }
    }

    @FXML
    private void btnBackClick(ActionEvent e) throws IOException {
        if (!UserName.search.isEmpty()) Navigator.getInstance().goToStore(UserName.search);
        else Navigator.getInstance().goToStore("");
    }

    @FXML
    private void btnLogOut(ActionEvent e) throws IOException {
        Navigator.getInstance().goToLogin();
    }

    public void initialize(int cartId) {
        // Overload nếu Navigator cần truyền cartId
    }
}
