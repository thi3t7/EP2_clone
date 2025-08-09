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
    @FXML private Label totalLabel;   // ✅ Tổng tiền hàng
    @FXML private Label finalTotalLabel; // ✅ Tổng tiền cuối cùng
    @FXML private Label itemCountLabel; // ✅ Số lượng sản phẩm
    @FXML private MFXButton btnBack;
    @FXML private MFXButton btnLogout;
    @FXML private Label topBannerText;
    @FXML private VBox cartList;
    @FXML private ComboBox<String> paymentMethodComboBox;
    @FXML private MFXButton btnCheckout;

    private final SimpleIntegerProperty total = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty itemCount = new SimpleIntegerProperty(0);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtUserName.setText(UserName.username);
        System.out.println("🛒 Cart ID: " + UserName.CartID);

        // ✅ Bind tổng tiền với label
        total.addListener((obs, oldVal, newVal) -> {
            finalTotalLabel.setText(String.format("%,d VNĐ", newVal.intValue())); // Chỉ hiển thị tổng cộng
        });

        // ✅ Bind số lượng sản phẩm
        itemCount.addListener((obs, oldVal, newVal) -> {
            itemCountLabel.setText(String.valueOf(newVal.intValue()));
        });

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
        cartList.getChildren().clear();
        ObservableList<SmartPhone> items = smartphoneDAO.selectAllCart(cartId);
        total.set(0); // Reset tổng tiền
        itemCount.set(0); // Reset số lượng

        try {
            for (SmartPhone phone : items) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/store/ProductInfo.fxml"));
                AnchorPane pane = loader.load();

                ProductInfoController ctrl = loader.getController();
                ctrl.setData(phone);
                ctrl.initialize(phone.getAmount());

                pane.getProperties().put("controller", ctrl);
                // Checkbox mặc định bỏ chọn
                ctrl.checkbox.setSelected(false);

                // Tick chọn sản phẩm
                ctrl.checkbox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                    int itemTotal = parsePrice(ctrl.total_price.getText());
                    if (newVal) {
                        total.set(total.get() + itemTotal);
                        itemCount.set(itemCount.get() + 1);
                    } else {
                        total.set(total.get() - itemTotal);
                        itemCount.set(itemCount.get() - 1);
                    }
                });

                // Thay đổi số lượng khi tick chọn
                ctrl.amount.valueProperty().addListener((obs, oldVal, newVal) -> {
                    int price = parsePrice(ctrl.price.getText());
                    ctrl.total_price.setText(String.format("%,d VNĐ", newVal * price));
                    if (ctrl.checkbox.isSelected()) {
                        int delta = (newVal - oldVal) * price;
                        total.set(total.get() + delta);
                    }
                });

                cartList.getChildren().add(pane);
            }
        } catch (IOException e) {
            System.out.println("❌ Error loading cart items: " + e.getMessage());
            showErrorAlert("Lỗi tải giỏ hàng", "Không thể tải danh sách sản phẩm trong giỏ hàng.");
        }
    }

    private int parsePrice(String priceText) {
        return Integer.parseInt(priceText.replaceAll("[^\\d]", ""));
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    // ===== Sự kiện người dùng =====

    @FXML
    private void btnSearchClick(ActionEvent e) throws IOException {
        UserName.search = searchBar.getText();
        Navigator.getInstance().goToStore(UserName.search);
    }

    @FXML
    private void btnCheckoutClick(ActionEvent e) {
        if (total.get() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, 
                "Bạn chưa chọn sản phẩm nào để thanh toán!", ButtonType.OK);
            alert.setTitle("Giỏ hàng trống");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        String paymentMethod = paymentMethodComboBox.getValue();
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn đã chọn phương thức: " + paymentMethod +
                        "\nTổng tiền: " + finalTotalLabel.getText() +
                        "\nSố lượng sản phẩm: " + itemCountLabel.getText() + " sản phẩm" +
                        "\n\nXác nhận thanh toán?",
                ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Xác nhận thanh toán");
        confirm.setHeaderText(null);
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            try {
                // ✅ 1. Xử lý thanh toán trong DB
                smartphoneDAO.ordered(UserName.CartID);

                // ✅ 2. Xóa các sản phẩm đã tick trong giao diện
                cartList.getChildren().removeIf(node -> {
                    ProductInfoController ctrl = (ProductInfoController) node.getProperties().get("controller");
                    return ctrl != null && ctrl.checkbox.isSelected();
                });

                // ✅ 3. Reset tổng tiền và số lượng
                total.set(0);
                itemCount.set(0);

                Alert success = new Alert(Alert.AlertType.INFORMATION, 
                    "✅ Thanh toán thành công!\n\nCác sản phẩm đã chọn đã được xử lý.\nCảm ơn bạn đã mua hàng!", 
                    ButtonType.OK);
                success.setTitle("Thanh toán thành công");
                success.setHeaderText(null);
                success.showAndWait();
                
            } catch (Exception ex) {
                System.out.println("❌ Error during checkout: " + ex.getMessage());
                showErrorAlert("Lỗi thanh toán", "Có lỗi xảy ra trong quá trình thanh toán. Vui lòng thử lại.");
            }
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
