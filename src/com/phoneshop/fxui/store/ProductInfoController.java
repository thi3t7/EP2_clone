/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoneshop.fxui.store;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import com.phoneshop.dao.SmartPhone;
import com.phoneshop.dao.SmartPhoneDAO;
import com.phoneshop.dao.SmartPhoneDAOImp;
import com.phoneshop.fxui.Navigator;
import com.phoneshop.model.UserName;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class ProductInfoController implements Initializable {

    private SmartPhoneDAO smartphonedao = new SmartPhoneDAOImp();

    @FXML
    private ImageView image;
    @FXML
    private Label name;
    @FXML
     Label price;
    @FXML
     Spinner<Integer> amount;
    @FXML
    Label total_price;
    @FXML
    private Label manufacturer;
    @FXML
    private MFXButton deleteBtn;
    @FXML
    CheckBox checkbox;

    @FXML
    private int productID;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void initialize(int i){
        int initialValue = i;
        SpinnerValueFactory<Integer> valueFactory
                = //
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99999, initialValue);
        amount.setValueFactory(valueFactory);
    }

    @FXML
    private void deleteBtnClick(ActionEvent event) throws IOException {
        int productID = smartphonedao.selectProductIdByName(name.getText());
        smartphonedao.deleteCart(UserName.CartID, productID); // ✅ Xóa đúng của user hiện tại
        Navigator.getInstance().goToShoppingCart(UserName.CartID);
    }


    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }


    public void setData(SmartPhone smartphone) {

        try {
            String imageName = smartphonedao.SelectImg(smartphone.getProductID().toString());  // e.g., "vsmart.jpg"
            if (imageName == null || imageName.isEmpty()) {
                System.err.println("⚠ Không có tên ảnh từ DB!");
                return;
            }

            System.out.println("🖼️ Đang load ảnh từ: " + imageName);

            InputStream is = getClass().getClassLoader().getResourceAsStream(imageName);
            if (is == null) {
                System.err.println("⚠ Ảnh không tồn tại trong resource: " + imageName);
                return;
            }

            Image imageFile = new Image(is);
            image.setImage(imageFile);
            manufacturer.setText("Hãng: " + smartphone.getMfgName());
            name.setText(smartphone.getName());
            price.setText(smartphone.getPrice() + "$");
            total_price.setText((Integer.parseInt(smartphone.getPrice()) * smartphone.getAmount()) + "$");
            productID = smartphone.getProductID();
            System.out.println(productID);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getUnitPrice() { return parsePrice(price.getText()); }
    public int getAmount()    { return amount.getValue(); }

    private int parsePrice(String priceText) {
        return Integer.parseInt(priceText.replaceAll("[^\\d]", ""));
    }





}
