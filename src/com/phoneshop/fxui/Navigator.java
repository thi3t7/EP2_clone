package com.phoneshop.fxui;

import java.io.IOException;

import com.phoneshop.dao.Admin;
import com.phoneshop.dao.Manufacturer;
import com.phoneshop.dao.SmartPhone;
import com.phoneshop.fxui.Admin.AdminEditController;
import com.phoneshop.fxui.manufacturer.ManufacturerEditController;
import com.phoneshop.fxui.smartphone.SmartPhoneEditController;
import com.phoneshop.fxui.store.ProductView2Controller;
import com.phoneshop.fxui.store.ShoppingCartController;
import com.phoneshop.fxui.store.StoreUIController;

import com.phoneshop.model.UserName;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Navigator {

    public static final String LOGIN_FXML = "/fxml/login/LoginUI.fxml";
    public static final String SIGNUP_FXML = "/fxml/login/SignUpUI.fxml";
    public static final String WELCOME_FXML = "/fxml/login/Welcome.fxml";

    public static final String ADMIN_INDEX = "/fxml/Admin/AdminIndex.fxml";
    public static final String ADMIN_EDIT = "/fxml/Admin/AdminEdit.fxml";

    public static final String SMARTPHONE_INDEX = "/fxml/smartphone/SmartPhoneIndex.fxml";
    public static final String SMARTPHONE_EDIT = "/fxml/smartphone/SmartPhoneEdit.fxml";

    public static final String MANUFACTURER_INDEX = "/fxml/manufacturer/ManufacturerIndex.fxml";
    public static final String MANUFACTURER_EDIT = "/fxml/manufacturer/ManufacturerEdit.fxml";

    public static final String ORDER_FXML = "/fxml/order/Order.fxml";
    public static final String STORE_FXML = "/fxml/store/Store.fxml";
    public static final String PRODUCT_VIEW2 = "/fxml/store/ProductView2.fxml";
    public static final String SHOPPINGCART_FXML = "/fxml/store/ShoppingCart.fxml";

    private FXMLLoader loader;
    private Stage stage;

    private static Navigator nav;

    private Navigator() {
    }

    public static Navigator getInstance() {
        if (nav == null) {
            nav = new Navigator();
        }
        return nav;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return this.stage;
    }

    private FXMLLoader goTo(String fxml) throws IOException {
        if (this.stage == null) {
            throw new IllegalStateException("Stage has not been set. Use Navigator.setStage(stage) in your main application.");
        }

        this.loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        this.stage.setScene(scene);
        this.stage.centerOnScreen();

        return loader;
    }


    private FXMLLoader createLoader(String fxml) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        // loader.setResources(Translator.getResource()); // Uncomment if using i18n
        return loader;
    }

    public <T> T goToWithController(String fxml) throws IOException {
        FXMLLoader loader = createLoader(fxml);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        this.stage.setScene(scene);
        this.stage.centerOnScreen();
        return loader.getController();
    }

    public void goToAdminIndex() throws IOException {
        goTo(ADMIN_INDEX);
    }

    public void goToAdminEdit(Admin editAdmin) throws IOException {
        goTo(ADMIN_EDIT);
        AdminEditController ctrl = loader.getController();
        ctrl.initialize(editAdmin);
    }

    public void goToSmartPhoneIndex() throws IOException {
        goTo(SMARTPHONE_INDEX);
    }

    public void goToSmartPhoneEdit(SmartPhone editSmartPhone) throws IOException {
        goTo(SMARTPHONE_EDIT);
        SmartPhoneEditController ctrl = loader.getController();
        ctrl.initialize(editSmartPhone);
    }

    public void goToManufacturerIndex() throws IOException {
        goTo(MANUFACTURER_INDEX);
    }

    public void goToManufacturerEdit(Manufacturer editManufacturer) throws IOException {
        goTo(MANUFACTURER_EDIT);
        ManufacturerEditController ctrl = loader.getController();
        ctrl.initialize(editManufacturer);
    }

    public void goToLogin() throws IOException {
        goTo(LOGIN_FXML);
    }

    public void goToStore(String keyword) throws IOException {
         // Lưu keyword để StoreUIController đọc
        goToWithController(STORE_FXML);
    }


    public void goToSignUp() throws IOException {
        goTo(SIGNUP_FXML);
    }

    public void goToProductView2(String name) throws IOException {
        goTo(PRODUCT_VIEW2);
        ProductView2Controller ctrl = loader.getController();
        ctrl.initialize(name);
    }

    public void goToShoppingCart(int userId) throws IOException {
        goTo(SHOPPINGCART_FXML);
        ShoppingCartController ctrl = loader.getController();
        ctrl.initialize(userId);
    }

    public void goToOrder() throws IOException {
        goTo(ORDER_FXML);
    }

    public void goToWelcome() throws IOException {
        goTo(WELCOME_FXML);
    }
}
