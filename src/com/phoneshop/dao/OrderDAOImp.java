package com.phoneshop.dao;

import com.phoneshop.dao.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDAOImp implements OrderDAO {

    private final Connection conn;

    // ✅ Constructor nhận connection (có thể thay bằng singleton DB connection bạn đang dùng)
    public OrderDAOImp(Connection conn) {
        this.conn = conn;
    }

    @Override
    public ObservableList<Order> selectAllOrders() {
        ObservableList<Order> orders = FXCollections.observableArrayList();

        String sql = """
    SELECT o.OrderID, a.name AS CustomerName, p.name AS ProductName,
           cd.amount, (cd.amount * p.price) AS Total, o.Date
    FROM `order` o
    JOIN cart c ON o.CartID = c.CartID
    JOIN admin a ON c.CustomerID = a.id
    JOIN cart_detail cd ON c.CartID = cd.cartID
    JOIN product p ON cd.productID = p.productID
    ORDER BY o.OrderID DESC
""";


        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("OrderID"),
                        rs.getString("CustomerName"),
                        rs.getString("ProductName"),
                        rs.getInt("amount"),
                        rs.getInt("Total"),
                        rs.getString("Date")
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching orders: " + e.getMessage());
        }

        return orders;
    }

    @Override
    public ObservableList<Order> selectOrdersByCustomer(int customerId) {
        ObservableList<Order> orders = FXCollections.observableArrayList();

        String sql = """
        SELECT o.OrderID,
               l.Name AS CustomerName,
               p.name AS ProductName,
              cd.amount,
               (cd.amount * p.price) AS Total,
               o.Date
    FROM `order` o
    JOIN cart c ON o.CartID = c.CartID
    JOIN loginby l ON c.CustomerID = l.id
    JOIN cart_detail cd ON c.CartID = cd.cartID
    JOIN product p ON cd.productID = p.productID
    ORDER BY o.OrderID DESC
""";


        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("OrderID"),
                        rs.getString("CustomerName"),
                        rs.getString("ProductName"),
                        rs.getInt("amount"),
                        rs.getInt("Total_Price"),
                        rs.getString("Date")
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching orders by customer: " + e.getMessage());
        }

        return orders;
    }
}
