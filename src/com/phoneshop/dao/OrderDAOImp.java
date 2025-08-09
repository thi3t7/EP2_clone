package com.phoneshop.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.List;
import java.util.StringJoiner;

public class OrderDAOImp implements OrderDAO {

    private final Connection conn;

    public OrderDAOImp(Connection conn) {
        this.conn = conn;
    }

    // ======= MÀN ADMIN: LOAD DANH SÁCH ORDER =======

    @Override
    public ObservableList<Order> selectAllOrders() {
        ObservableList<Order> orders = FXCollections.observableArrayList();

        // Nếu bạn đã lưu price tại thời điểm mua vào order_item.price thì nên dùng giá này
        String sql = """
    SELECT 
        o.OrderID,
        a.name AS CustomerName,
        p.name AS ProductName,
        oi.amount,
        (oi.amount * oi.price) AS Total,
        o.Date,
        o.address AS Address,
        o.status AS Status
    FROM `order` o
    JOIN cart c        ON o.CartID = c.CartID
    JOIN admin a       ON c.CustomerID = a.id
    JOIN order_item oi ON o.OrderID = oi.orderID
    JOIN product p     ON oi.productID = p.productID
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
                        rs.getString("Date"),
                        rs.getString("address"),
                        rs.getString("Status")
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching orders: " + e.getMessage());
        }

        return orders;
    }

    // ======= CHECKOUT: BƯỚC 1 – TẠO ORDER HEADER =======
    @Override
    public int createOrder(int cartId, String address) throws Exception {
        final String sql = "INSERT INTO `order` (CartID, `Date`, `address`) VALUES (?, NOW(), ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, cartId);
            ps.setString(2, address);
            int affected = ps.executeUpdate();
            if (affected != 1) {
                throw new SQLException("Insert order failed, affected rows = " + affected);
            }
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            throw new SQLException("Insert order succeeded but no generated key returned");
        }
    }

    // ======= CHECKOUT: BƯỚC 2 – LƯU ORDER ITEMS =======
    @Override
    public void saveOrderItems(int orderId, List<CartLine> lines) throws Exception {
        if (lines == null || lines.isEmpty()) return;

        final String sql = "INSERT INTO order_item (orderId, productId, amount, price) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (CartLine line : lines) {
                ps.setInt(1, orderId);
                ps.setInt(2, line.getProductId());
                ps.setInt(3, line.getAmount());
                ps.setInt(4, line.getPrice());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    // ======= CHECKOUT: BƯỚC 3 – XÓA DÒNG ĐÃ MUA KHỎI CART =======
    @Override
    public void deletePurchasedFromCart(int cartId, List<Integer> productIds) throws Exception {
        if (productIds == null || productIds.isEmpty()) return;

        StringJoiner sj = new StringJoiner(",", "(", ")");
        for (int i = 0; i < productIds.size(); i++) sj.add("?");

        final String sql = "DELETE FROM cart_detail WHERE cartID = ? AND productID IN " + sj;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            int idx = 2;
            for (Integer pid : productIds) {
                ps.setInt(idx++, pid);
            }
            ps.executeUpdate();
        }
    }

    // ======= CHECKOUT: GÓI GỌN TOÀN BỘ QUY TRÌNH TRONG TRANSACTION =======
    @Override
    public int checkout(int cartId, String address, List<CartLine> lines) {
        if (lines == null || lines.isEmpty()) return -1;

        boolean oldAutoCommit;
        try {
            oldAutoCommit = conn.getAutoCommit();
        } catch (SQLException e) {
            System.out.println("❌ Cannot read autoCommit: " + e.getMessage());
            return -1;
        }

        try {
            conn.setAutoCommit(false);

            // 1) order header
            int orderId = createOrder(cartId, address);

            // 2) order items
            saveOrderItems(orderId, lines);

            // 3) delete purchased from cart
            deletePurchasedFromCart(
                    cartId,
                    lines.stream().map(CartLine::getProductId).distinct().toList()
            );

            conn.commit();
            return orderId;

        } catch (Exception ex) {
            try { conn.rollback(); } catch (SQLException ignore) {}
            System.out.println("❌ Checkout failed, rolled back: " + ex.getMessage());
            return -1;
        } finally {
            try { conn.setAutoCommit(oldAutoCommit); } catch (SQLException ignore) {}
        }
    }


    public boolean updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE `order` SET status=? WHERE OrderID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("❌ Update status error: " + e.getMessage());
            return false;
        }
    }

}
