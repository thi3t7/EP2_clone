package com.phoneshop.fxui.order;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import com.phoneshop.dao.OrderDAO;
import com.phoneshop.dao.OrderDAOImp;
import com.phoneshop.fxui.Navigator;
import com.phoneshop.dao.Order;
import com.phoneshop.model.UserName;
import com.phoneshop.dbconnection.DbFactory;
import com.phoneshop.dbconnection.DbType;// ✅ Bạn cần class này trả về Connection (tương tự SmartPhoneDAOImp đang dùng)

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

public class OrderController implements Initializable {

    @FXML private MFXButton btnAdmin;
    @FXML private MFXButton btnSmartPhone;
    @FXML private MFXButton btnMfg;
    @FXML private MFXButton LogOut;
    @FXML private Label txtUsername;
    @FXML private MFXButton btnOrder;
    @FXML private TableView<Order> tvOrder;
    @FXML private TableColumn<Order, Integer> tcOrderID;
    @FXML private TableColumn<Order, String> tcCustomerName;
    @FXML private TableColumn<Order, String> tcProductName;
    @FXML private TableColumn<Order, Integer> tcAmount;
    @FXML private TableColumn<Order, Integer> tcTotal;
    @FXML private TableColumn<Order, String> tcDate;
    @FXML private TableColumn<Order, String>  tcAddress;
    @FXML private TableColumn<Order, String> tcStatus;
    @FXML private MFXButton btnReset;

    private final ObservableList<String> STATUSES =
            FXCollections.observableArrayList("PENDING","PROCESSING","SHIPPING","DELIVERED");

    private OrderDAO orderDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtUsername.setText(UserName.username);

        // ✅ Kết nối DB và khởi tạo DAO
        Connection conn;
        try {
            conn = DbFactory.getConnection(DbType.MYSQL);
            orderDAO = new OrderDAOImp(conn);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Database connection failed: " + e.getMessage());
        }; // giống cách bạn đang làm ở DAO khác


        // ✅ Gán dữ liệu cho từng cột
        tcOrderID.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        tcCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        tcProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        tcAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        tcTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        tcDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tcAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tcStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tcStatus.setCellFactory(ComboBoxTableCell.forTableColumn(STATUSES));
        tcStatus.setEditable(true);           // cột có thể sửa
        tvOrder.setEditable(true);

        tcStatus.setOnEditCommit(evt -> {
            Order row = evt.getRowValue();
            String newStatus = evt.getNewValue();

            // gọi DAO update
            boolean ok = orderDAO.updateOrderStatus(row.getOrderID(), newStatus);
            if (ok) {
                row.setStatus(newStatus);  // cập nhật model
            } else {
                // nếu lỗi, revert hiển thị về giá trị cũ
                row.setStatus(evt.getOldValue());
                tvOrder.refresh();
                new Alert(Alert.AlertType.ERROR, "Cập nhật trạng thái thất bại!").showAndWait();
            }
        });
        // 1) Bảo đảm cột Address thực sự nằm trong bảng và nằm ở vị trí bạn muốn
        if (!tvOrder.getColumns().contains(tcAddress)) {
            tvOrder.getColumns().add(tcAddress);          // hoặc add(2, tcAddress) nếu muốn đưa lên sớm
        } else {
            // Đưa Address ra trước để chắc chắn nhìn thấy
            tvOrder.getColumns().remove(tcAddress);
            tvOrder.getColumns().add(2, tcAddress);       // ví dụ: đứng sau Product name
        }

// 2) Cho phép bảng rộng ra/hiện thanh cuộn ngang
        tvOrder.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

// 3) Nới (hoặc giảm) độ rộng cho hợp lý
        tcOrderID.setPrefWidth(90);
        tcCustomerName.setPrefWidth(180);
        tcProductName.setPrefWidth(160);
        tcAmount.setPrefWidth(90);
        tcTotal.setPrefWidth(110);
        tcDate.setPrefWidth(150);
        tcAddress.setPrefWidth(260);          // Address thường dài

        // ✅ Load dữ liệu từ DB
        loadOrders();
    }

    private void loadOrders() {
        ObservableList<Order> orders = orderDAO.selectAllOrders();
        System.out.println("Loaded orders: " + orders.size()); // ✅ kiểm tra số lượng
        for (Order o : orders) {
            System.out.println(o.getOrderID() + " - " + o.getAddress() + " - " + o.getProductName());
        }
        tvOrder.setItems(orders);
    }


    // ====== Các sự kiện điều hướng ======

    @FXML
    void btnAdminClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToAdminIndex();
    }

    @FXML
    void btnLogoutClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToLogin();
    }

    @FXML
    void btnMfgClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToManufacturerIndex();
    }

    @FXML
    void btnOrderClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToOrder();
    }

    @FXML
    void btnResetClick(ActionEvent event) throws IOException {
        loadOrders(); // ✅ Reset chỉ cần reload dữ liệu
    }

    @FXML
    void btnSmartPhoneClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToSmartPhoneIndex();
    }

    @FXML
    void onUpdateStatusClick(ActionEvent e) {
        Order sel = tvOrder.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        List<String> options = List.of("PENDING","PAID","PROCESSING","SHIPPED","DELIVERED","CANCELED");
        ChoiceDialog<String> dlg = new ChoiceDialog<>(sel.getStatus(), options);
        dlg.setTitle("Update Status");
        dlg.setHeaderText("Chọn trạng thái mới cho Order #" + sel.getOrderID());
        dlg.setContentText("Status:");

        dlg.showAndWait().ifPresent(newStatus -> {
            if (orderDAO.updateOrderStatus(sel.getOrderID(), newStatus)) {
                sel.setStatus(newStatus);
                tvOrder.refresh();
            }
        });
    }

}
