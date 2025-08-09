package com.phoneshop.dao;

/**
 * Đại diện một dòng hàng người dùng chọn để thanh toán.
 * Dùng khi truyền vào DAO để ghi order_item.
 */
public class CartLine {
    private final int productId;
    private final int amount;
    private final int price; // đơn giá tại thời điểm mua (theo DB của bạn là INT)

    public CartLine(int productId, int amount, int price) {
        this.productId = productId;
        this.amount = amount;
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public int getAmount() {
        return amount;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "CartLine{productId=" + productId +
                ", amount=" + amount +
                ", price=" + price + "}";
    }
}
