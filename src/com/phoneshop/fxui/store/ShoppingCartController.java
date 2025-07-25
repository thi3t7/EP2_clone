/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoneshop.fxui.store;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.phoneshop.dao.SmartPhone;
import com.phoneshop.dao.SmartPhoneDAO;
import com.phoneshop.dao.SmartPhoneDAOImp;
import com.phoneshop.fxui.Navigator;
import com.phoneshop.model.UserName;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;



public class ShoppingCartController implements Initializable {

    private SmartPhoneDAO smartphonedao = new SmartPhoneDAOImp();

    @FXML
    private TextField searchBar;
    
    @FXML
    private Label txtUserName;
    
    @FXML
    private MFXButton btnLogout;
    
    @FXML
    private MFXButton btnSearch;
    
    @FXML
    private MFXButton HomeBtn;
    
    @FXML
    private MFXButton btnBack;

    @FXML
    private GridPane GpPhone;

    @FXML
    private Label total_all;

    @FXML
    private MFXButton btnOrder;
    
    @FXML
    private MFXComboBox<String> cbbMfg;
    
    @FXML
    private MFXButton btnCart;
    
    @FXML
    private void btnCartClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToShoppingCart(UserName.CartID);
    }
    
    @FXML
    void cbbMfgClick(ActionEvent event) throws IOException {
        UserName.sbb = cbbMfg.getValue().toString();
        Navigator.getInstance().goToStore("");
    }

    /**
     * Initializes the controller class.
     */
    ObjectProperty<Integer> count = new SimpleObjectProperty<>(0);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtUserName.setText(UserName.username);
        cbbMfg.setItems(smartphonedao.selectmanu());
        cbbMfg.setValue(UserName.sbb);
        count.addListener((observable, oldValue, newValue) -> {
            total_all.setText("Tổng tiền: " + newValue.toString() + "$");
        });
    }

    public void initialize(int id) {

        int row = 0;
        ObservableList<SmartPhone> carts = smartphonedao.selectAllCart(id);
        try {
            for (int i = 0; i < carts.size(); i++) {
                FXMLLoader fxmlloader = new FXMLLoader();
                fxmlloader.setLocation(getClass().getResource("ProductInfo.fxml"));
                AnchorPane anchorpane = fxmlloader.load();
                ProductInfoController productInfoController = fxmlloader.getController();
                productInfoController.setData(carts.get(i));
                productInfoController.initialize(carts.get(i).getAmount());

                productInfoController.amount.valueProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != oldValue) {
                        productInfoController.total_price.setText(Integer.toString(Integer.parseInt(productInfoController.price.getText().substring(0, productInfoController.price.getText().length() - 1)) * productInfoController.amount.getValue()) + "$");
                    }
                });

                productInfoController.checkbox.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (newValue) {
                        productInfoController.amount.valueProperty().addListener((obser, old, newv) -> {
                            if (newv > old && productInfoController.checkbox.isSelected()) {
                                productInfoController.total_price.setText(Integer.toString(Integer.parseInt(productInfoController.price.getText().substring(0, productInfoController.price.getText().length() - 1)) * productInfoController.amount.getValue()) + "$");
                                count.set(count.get() + (Integer.parseInt((productInfoController.price.getText().substring(0, productInfoController.price.getText().length() - 1)))));
                            }
                            if (newv < old && productInfoController.checkbox.isSelected()) {
                                productInfoController.total_price.setText(Integer.toString(Integer.parseInt(productInfoController.price.getText().substring(0, productInfoController.price.getText().length() - 1)) * productInfoController.amount.getValue()) + "$");
                                count.set(count.get() - (Integer.parseInt((productInfoController.price.getText().substring(0, productInfoController.price.getText().length() - 1)))));
                            }
                            //... fix
                        });
                        count.set(count.get() + (Integer.parseInt(productInfoController.total_price.getText().substring(0, productInfoController.total_price.getText().length() - 1))));

                    } else {
                        count.set(count.get() - (Integer.parseInt(productInfoController.total_price.getText().substring(0, productInfoController.total_price.getText().length() - 1))));
                    }
                });

                GpPhone.add(anchorpane, 0, row++);
                GridPane.setMargin(anchorpane, new Insets(5, 40, 5, 40));

                GpPhone.setMinWidth(Region.USE_COMPUTED_SIZE);
                GpPhone.setPrefWidth(Region.USE_COMPUTED_SIZE);
                GpPhone.setMaxWidth(Region.USE_PREF_SIZE);

                GpPhone.setMinHeight(Region.USE_COMPUTED_SIZE);
                GpPhone.setPrefHeight(Region.USE_COMPUTED_SIZE);
                GpPhone.setMaxHeight(Region.USE_PREF_SIZE);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @FXML
    private void btnLogOut(ActionEvent event) throws IOException {
        UserName.sbb = "";
        Navigator.getInstance().goToLogin();
    }

    @FXML
    private void btnSearchClick(ActionEvent event) throws IOException {
        UserName.sbb = "";
        Navigator.getInstance().goToStore(searchBar.getText());
        UserName.search = searchBar.getText();
    }

    @FXML
    private void HomeClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToStore("");
    }

    @FXML
    void btnBackClick(ActionEvent event) throws IOException {
        if (!UserName.search.isEmpty()) {
            Navigator.getInstance().goToStore(UserName.search);
        } else if (!UserName.sbb.isEmpty()) {
            Navigator.getInstance().goToStore("");
        } else {
            Navigator.getInstance().goToStore("");
        }
    }

    @FXML
    private void btnOrderClick(ActionEvent event) {
        smartphonedao.ordered(UserName.CartID);
    }

}
