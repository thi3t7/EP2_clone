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
        smartphonedao.deleteCart(smartphonedao.selectProductIdByName(name.getText()));
        Navigator.getInstance().goToShoppingCart(UserName.CartID);
    }

    public void setData(SmartPhone smartphone) {
        try {
            String imageName = smartphonedao.SelectImg(smartphone.getProductID().toString());  // ví dụ: "vsmart-active-3-6gb.jpg"
            String imagePath = "images/" + imageName;
            System.out.println("image path: " + imagePath);

            InputStream is = getClass().getClassLoader().getResourceAsStream(imagePath);

            if (is == null) {
                System.out.println("⚠ Ảnh không tồn tại trong resource: " + imagePath);
                return;
            }

            Image imageFile = new Image(is);
            image.setImage(imageFile);
            manufacturer.setText("Hãng: " + smartphone.getMfgName()); // ví dụ nếu bạn có mfgName từ SQL
            name.setText(smartphone.getName());
            price.setText(smartphone.getPrice() + "$");
            total_price.setText(Integer.toString(Integer.parseInt(smartphone.getPrice()) * smartphone.getAmount()) + "$");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
