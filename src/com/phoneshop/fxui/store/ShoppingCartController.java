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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    @FXML private GridPane GpPhone;
    @FXML private ImageView Img;
    @FXML private Label total_all;
    @FXML private MFXButton btnOrder;
    @FXML private VBox cartList;
    @FXML private Label totalLabel;
    @FXML private MFXButton btnBack;
    @FXML private MFXButton btnLogout;
    @FXML private Label topBannerText;

    private final SimpleIntegerProperty total = new SimpleIntegerProperty(0);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtUserName.setText(UserName.username);
        System.out.println("🛒 Cart ID: " + UserName.CartID); // Kiểm tra có cartID không

        total.addListener((obs, oldVal, newVal) ->
                total_all.setText("Tổng tiền: " + newVal + " $")
        );

        loadCartItems(UserName.CartID);
    }

    private void loadCartItems(int cartId) {
        GpPhone.getChildren().clear();
        ObservableList<SmartPhone> items = smartphoneDAO.selectAllCart(cartId);
        int row = 0;

        try {
            for (SmartPhone phone : items) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/store/ProductInfo.fxml"));
                AnchorPane pane = loader.load();

                ProductInfoController ctrl = loader.getController();
                ctrl.setData(phone);
                ctrl.initialize(phone.getAmount());

                ctrl.amount.valueProperty().addListener((obs, oldVal, newVal) -> {
                    if (ctrl.checkbox.isSelected()) {
                        int price = parsePrice(ctrl.price.getText());
                        int delta = (newVal - oldVal) * price;
                        total.set(total.get() + delta);
                        ctrl.total_price.setText((newVal * price) + "$");
                    }
                });

                ctrl.checkbox.selectedProperty().addListener(
                        (ObservableValue<? extends Boolean> obs, Boolean oldVal, Boolean newVal) -> {
                            int itemTotal = parsePrice(ctrl.total_price.getText());
                            if (newVal) {
                                total.set(total.get() + itemTotal);
                            } else {
                                total.set(total.get() - itemTotal);
                            }
                        }
                );

                GpPhone.add(pane, 0, row++);
                GridPane.setMargin(pane, new Insets(5, 40, 5, 40));
                setGridSize();
            }
        } catch (IOException e) {
            System.out.println("❌ Error loading cart items: " + e.getMessage());
        }
    }

    private int parsePrice(String priceText) {
        return Integer.parseInt(priceText.replaceAll("[^\\d]", ""));
    }

    private void setGridSize() {
        GpPhone.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        GpPhone.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        GpPhone.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    }

    // ===== Sự kiện người dùng =====

    @FXML
    private void btnSearchClick(ActionEvent e) throws IOException {
        UserName.search = searchBar.getText();
        Navigator.getInstance().goToStore(UserName.search);
    }

    @FXML
    private void btnOrderClick(ActionEvent e) {
        smartphoneDAO.ordered(UserName.CartID);
        // Gợi ý: thêm hộp thoại xác nhận thành công tại đây
    }

    @FXML
    private void btnBackClick(ActionEvent e) throws IOException {
        if (!UserName.search.isEmpty()) {
            Navigator.getInstance().goToStore(UserName.search);
        } else {
            Navigator.getInstance().goToStore("");
        }
    }

    @FXML
    private void HomeClick(ActionEvent e) throws IOException {
        Navigator.getInstance().goToStore("");
    }

    @FXML
    private void btnCartClick(ActionEvent e) throws IOException {
        Navigator.getInstance().goToShoppingCart(UserName.CartID);
    }

    @FXML
    private void btnLogOut(ActionEvent e) throws IOException {
        Navigator.getInstance().goToLogin();
    }

    public void initialize(int cartId) {
        // Phương thức overload nếu Navigator cần truyền cartId
    }
}
