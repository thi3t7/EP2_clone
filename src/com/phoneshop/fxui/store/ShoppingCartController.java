package com.phoneshop.fxui.store;

import com.phoneshop.dao.*;
import com.phoneshop.dbconnection.DbFactory;
import com.phoneshop.dbconnection.DbType;
import com.phoneshop.fxui.Navigator;
import com.phoneshop.model.UserName;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ShoppingCartController implements Initializable {

    // DAO
    private final SmartPhoneDAO smartphoneDAO = new SmartPhoneDAOImp();
    public GridPane mainGrid;
    public VBox headerContainer;
    public MFXButton btnSearch;
    public MFXButton btnLogOut;
    public VBox mainContent;
    private OrderDAO orderDAO;

    // Header / common
    @FXML private TextField searchBar;
    @FXML private Label txtUserName;
    @FXML private ImageView Img;
    @FXML private Label topBannerText;
    @FXML private MFXButton btnBack;
    @FXML private MFXButton btnLogout;

    // Cart UI
    @FXML private VBox cartList;
    @FXML private ComboBox<String> paymentMethodComboBox;
    @FXML private MFXButton btnCheckout;

    // Totals (một trong hai có thể null tùy FXML)
    @FXML private Label totalLabel;        // nếu FXML đang dùng totalLabel
    @FXML private Label finalTotalLabel;   // nếu FXML đang dùng finalTotalLabel
    @FXML private Label itemCountLabel;    // đếm số item được tick


    private TranslateTransition marquee;
    // State
    private final SimpleIntegerProperty total = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty itemCount = new SimpleIntegerProperty(0);

    private static final NumberFormat VND = NumberFormat.getInstance(new Locale("vi", "VN"));

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtUserName.setText(UserName.username);
        startMarquee();

        System.out.println("🛒 Cart ID: " + UserName.CartID);

        // Kết nối DAO dùng chung connection
        try {
            Connection conn = DbFactory.getConnection(DbType.MYSQL);
            orderDAO = new OrderDAOImp(conn);
        } catch (SQLException ex) {
            System.out.println("❌ DB connect fail: " + ex.getMessage());
        }

        // Bind tổng tiền → label (hỗ trợ cả 2 tên label)
        total.addListener((obs, o, n) -> {
            String txt = VND.format(n.intValue()) + " VNĐ";
            if (finalTotalLabel != null) finalTotalLabel.setText(txt);
            if (totalLabel != null)      totalLabel.setText(txt);
        });

        // Bind số lượng item tick
        if (itemCountLabel != null) {
            itemCount.addListener((obs, o, n) -> itemCountLabel.setText(String.valueOf(n.intValue())));
        }

        // Phương thức thanh toán
        paymentMethodComboBox.getItems().setAll(
                "Thanh toán khi nhận hàng (COD)",
                "Chuyển khoản ngân hàng",
                "Ví điện tử (Momo, ZaloPay)"
        );
        paymentMethodComboBox.getSelectionModel().selectFirst();

        // Load cart
        loadCartItems(UserName.CartID);
    }

    private void loadCartItems(int cartId) {
        cartList.getChildren().clear();
        ObservableList<SmartPhone> items = smartphoneDAO.selectAllCart(cartId);
        total.set(0);
        itemCount.set(0);

        try {
            for (SmartPhone phone : items) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/store/ProductInfo.fxml"));
                AnchorPane pane = loader.load();

                ProductInfoController ctrl = loader.getController();
                ctrl.setData(phone);
                ctrl.initialize(phone.getAmount());

                // Lưu controller để dùng lại
                pane.getProperties().put("controller", ctrl);

                // Mặc định chưa tick
                ctrl.checkbox.setSelected(false);

                // Khi tick/untick
                ctrl.checkbox.selectedProperty().addListener((obs, oldVal, isSelected) -> {
                    int lineTotal = parsePrice(ctrl.total_price.getText());
                    if (isSelected) {
                        total.set(total.get() + lineTotal);
                        itemCount.set(itemCount.get() + 1);
                    } else {
                        total.set(total.get() - lineTotal);
                        itemCount.set(itemCount.get() - 1);
                    }
                });

                // Khi đổi số lượng
                ctrl.amount.valueProperty().addListener((obs, oldVal, newVal) -> {
                    int unit = parsePrice(ctrl.price.getText());
                    int newLine = newVal * unit;
                    ctrl.total_price.setText(VND.format(newLine) + " VNĐ");
                    if (ctrl.checkbox.isSelected()) {
                        int delta = (newVal - oldVal) * unit;
                        total.set(total.get() + delta);
                    }
                });

                cartList.getChildren().add(pane);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Lỗi tải giỏ hàng", "Không thể tải danh sách sản phẩm trong giỏ hàng.");
        }
    }

    private int parsePrice(String text) {
        // Lấy số từ "123,456 VNĐ"
        return Integer.parseInt(text.replaceAll("[^\\d]", ""));
    }

    // ==== Actions ====

    @FXML
    private void btnSearchClick(ActionEvent e) throws IOException {
        UserName.search = searchBar.getText();
        Navigator.getInstance().goToStore(UserName.search);
    }

    @FXML
    private void btnCheckoutClick(ActionEvent e) {
        if (total.get() == 0) {
            showWarn("Giỏ hàng trống", "Bạn chưa chọn sản phẩm nào để thanh toán!");
            return;
        }

        // Nhập địa chỉ
        TextInputDialog addressDialog = new TextInputDialog();
        addressDialog.setTitle("Địa chỉ giao hàng");
        addressDialog.setHeaderText("Nhập địa chỉ nhận hàng");
        addressDialog.setContentText("Địa chỉ:");
        Optional<String> addressOpt = addressDialog.showAndWait();
        String address = addressOpt.map(String::trim).orElse("");
        if (address.isEmpty()) {
            showWarn("Thiếu địa chỉ", "Bạn cần nhập địa chỉ để thanh toán.");
            return;
        }

        String pm = paymentMethodComboBox.getValue();
        String summary = "Phương thức: " + pm
                + "\nTổng tiền: " + (finalTotalLabel != null ? finalTotalLabel.getText() : totalLabel.getText())
                + (itemCountLabel != null ? ("\nSố lượng sản phẩm: " + itemCountLabel.getText()) : "")
                + "\nĐịa chỉ: " + address
                + "\n\nXác nhận thanh toán?";
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, summary, ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Xác nhận thanh toán");
        confirm.setHeaderText(null);
        confirm.showAndWait();
        if (confirm.getResult() != ButtonType.YES) return;

        // Gom các dòng đã tick
        List<Node> toRemove = new ArrayList<>();
        List<CartLine> lines = cartList.getChildren().stream()
                .map(n -> (ProductInfoController) n.getProperties().get("controller"))
                .filter(Objects::nonNull)
                .filter(c -> c.checkbox.isSelected())
                .peek(c -> toRemove.add(((Node)c.checkbox.getScene().lookup("#" + c.checkbox.getId())).getParent()))
                .map(c -> new CartLine(c.getProductID(), c.getAmount(), c.getUnitPrice()))
                .collect(Collectors.toList());

        if (lines.isEmpty()) {
            showWarn("Chưa chọn sản phẩm", "Bạn cần tick những sản phẩm muốn thanh toán.");
            return;
        }

        // Transaction: tạo order + order_item + xóa các dòng trong cart_detail
        int orderId = orderDAO.checkout(UserName.CartID, address, lines);
        if (orderId > 0) {
            // Xóa trên UI
            cartList.getChildren().removeIf(n -> {
                ProductInfoController c = (ProductInfoController) n.getProperties().get("controller");
                return c != null && c.checkbox.isSelected();
            });
            total.set(0);
            itemCount.set(0);
            new Alert(Alert.AlertType.INFORMATION, """
                    Thanh toán thành công!
                    Chúng tôi sẽ liên hệ với bạn qua số điện thoại.
                    Mã đơn:""" + orderId, ButtonType.OK).showAndWait();
        } else {
            showError("Lỗi thanh toán", "Có lỗi khi tạo đơn hàng. Vui lòng thử lại.");
        }
    }

    @FXML
    private void btnBackClick(ActionEvent e) throws IOException {
        if (UserName.search != null && !UserName.search.isEmpty())
            Navigator.getInstance().goToStore(UserName.search);
        else
            Navigator.getInstance().goToStore("");
    }

    @FXML
    private void btnLogOut(ActionEvent e) throws IOException {
        Navigator.getInstance().goToLogin();
    }

    // Overload nếu cần
    public void initialize(int cartId) { }

    // ==== Helpers ====
    private void showWarn(String title, String content) {
        Alert a = new Alert(Alert.AlertType.WARNING, content, ButtonType.OK);
        a.setTitle(title);
        a.setHeaderText(null);
        a.showAndWait();
    }

    private void showError(String title, String content) {
        Alert a = new Alert(Alert.AlertType.ERROR, content, ButtonType.OK);
        a.setTitle(title);
        a.setHeaderText(null);
        a.showAndWait();
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
}
