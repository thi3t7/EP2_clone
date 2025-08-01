package com.phoneshop.fxui.store;

import com.phoneshop.dao.SmartPhone;
import com.phoneshop.fxui.Navigator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ProductController {

    @FXML
    private ImageView Img;

    @FXML
    private Label lbName;

    @FXML
    private Text txtOldPrice;

    @FXML
    private Label lbDiscountPercent;

    @FXML
    private Label lbNewPrice;

    private SmartPhone phone;


    public void setData(SmartPhone smartphone) {
        this.phone = smartphone;

        // ✅ Load ảnh đầu tiên trong danh sách
        try {
            String path = smartphone.getFirstImageLink(); // ảnh đầu tiên
            InputStream is = getClass().getClassLoader().getResourceAsStream(path);
            if (is != null) {
                Img.setImage(new Image(is));
            } else {
                System.out.println("❌ Không tìm thấy ảnh tại: " + path);
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi load ảnh: " + e.getMessage());
        }

        // ✅ Tên sản phẩm
        lbName.setText(smartphone.getName());

        // ✅ Tính giá
        try {
            double oldPrice = Double.parseDouble(smartphone.getPrice());
            int discount = 15;
            double newPrice = oldPrice * (100 - discount) / 100.0;

            txtOldPrice.setText(String.format("%,.0f ₫", oldPrice));
            lbDiscountPercent.setText("-" + discount + "%");
            lbNewPrice.setText(String.format("%,.0f ₫", newPrice));
        } catch (Exception e) {
            txtOldPrice.setText("");
            lbDiscountPercent.setText("");
            lbNewPrice.setText("Giá không hợp lệ");
        }
    }



    @FXML
    private void onImageClick() throws IOException {
        // Chuyển sang màn hình chi tiết
        Navigator.getInstance().goToProductView2(phone.getName());
    }
}
