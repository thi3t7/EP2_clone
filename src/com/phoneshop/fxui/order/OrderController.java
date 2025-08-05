package com.phoneshop.fxui.order;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.phoneshop.dao.OrderDAO;
import com.phoneshop.dao.OrderDAOImp;
import com.phoneshop.fxui.Navigator;
import com.phoneshop.dao.Order;
import com.phoneshop.model.UserName;
import com.phoneshop.dbconnection.DbFactory;
import com.phoneshop.dbconnection.DbType;// ✅ Bạn cần class này trả về Connection (tương tự SmartPhoneDAOImp đang dùng)

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    @FXML private MFXButton btnReset;

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

        // ✅ Load dữ liệu từ DB
        loadOrders();
    }

    private void loadOrders() {
        ObservableList<Order> orders = orderDAO.selectAllOrders();
        System.out.println("Loaded orders: " + orders.size()); // ✅ kiểm tra số lượng
        for (Order o : orders) {
            System.out.println(o.getOrderID() + " - " + o.getCustomerName() + " - " + o.getProductName());
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
}
