package com.phoneshop.dao;

import com.phoneshop.dao.Order;
import javafx.collections.ObservableList;

public interface OrderDAO {

    /**
     * Lấy toàn bộ danh sách đơn hàng (Order) từ database.
     * @return ObservableList chứa các đơn hàng.
     */
    ObservableList<Order> selectAllOrders();

    /**
     * Lấy danh sách đơn hàng theo Customer ID (nếu cần lọc theo khách hàng).
     * @param customerId ID của khách hàng.
     * @return ObservableList chứa đơn hàng của khách hàng đó.
     */
    ObservableList<Order> selectOrdersByCustomer(int customerId);
}
