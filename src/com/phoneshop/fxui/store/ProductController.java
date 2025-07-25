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

        // Load hình ảnh
        String link = smartphone.getLink(); // link kiểu "images/iphone.png"
        File file = new File(link);
        String localUrl = file.toURI().toString();
        Img.setImage(new Image(localUrl));

        // Tên sản phẩm
        lbName.setText(smartphone.getName());

        // Tính giá
        try {
            double oldPrice = Double.parseDouble(smartphone.getPrice());

            // Giả sử có discount (ví dụ hardcode nếu bạn chưa có trường)
            int discount = 15; // hoặc gán tạm = 15;
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
