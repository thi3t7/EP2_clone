package com.phoneshop.dao;

import javafx.collections.ObservableList;
import java.util.List;

public interface OrderDAO {

    // --- ĐÃ CÓ: liệt kê đơn hàng cho màn Order ---
    ObservableList<Order> selectAllOrders();

    // --- MỚI: tách các bước checkout ---
    /**
     * Tạo bản ghi order (header) và trả về orderId vừa tạo.
     * Ghi địa chỉ giao hàng vào cột address.
     */
    int createOrder(int cartId, String address) throws Exception;

    /**
     * Lưu các dòng sản phẩm đã mua vào bảng order_item (orderId, productId, amount, price).
     */
    void saveOrderItems(int orderId, List<CartLine> lines) throws Exception;

    /**
     * Xóa các dòng đã mua khỏi cart_detail (chỉ xóa những productId đã chọn).
     */
    void deletePurchasedFromCart(int cartId, List<Integer> productIds) throws Exception;

    /**
     * Thực hiện toàn bộ quy trình checkout trong 1 transaction:
     *  - insert order
     *  - insert order_item cho từng CartLine
     *  - delete các cart_detail đã mua
     *  Trả về orderId nếu thành công, -1 nếu thất bại.
     */
    int checkout(int cartId, String address, List<CartLine> lines);

    public boolean updateOrderStatus(int orderId, String status);
}
