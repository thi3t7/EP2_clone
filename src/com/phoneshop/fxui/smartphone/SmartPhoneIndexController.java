/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoneshop.fxui.smartphone;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.phoneshop.dao.SmartPhone;
import com.phoneshop.dao.SmartPhoneDAO;
import com.phoneshop.dao.SmartPhoneDAOImp;
import com.phoneshop.fxui.Navigator;
import com.phoneshop.model.UserName;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;


public class SmartPhoneIndexController implements Initializable {

    private SmartPhoneDAO smartphonedao = new SmartPhoneDAOImp();

    @FXML
    private TableView<SmartPhone> tvSmartPhone;

    @FXML
    private TableColumn<SmartPhone, String> tcName;

    @FXML
    private TableColumn<SmartPhone, String> tcMfg;

    @FXML
    private TableColumn<SmartPhone, String> tcPrice;

    @FXML
    private TableColumn<SmartPhone, String> tcScreen;

    @FXML
    private TableColumn<SmartPhone, String> tcSystem;

    @FXML
    private TableColumn<SmartPhone, String> tcCamera;

    @FXML
    private TableColumn<SmartPhone, String> tcChip;

    @FXML
    private TableColumn<SmartPhone, String> tcMemory;

    @FXML
    private TableColumn<SmartPhone, String> tcBattery;

    @FXML
    private MFXComboBox cbbMfg;

    @FXML
    private MFXTextField txtName;

    @FXML
    private MFXTextField txtPrice;

    @FXML
    private MFXTextField txtScreen;

    @FXML
    private MFXTextField txtSystem;
    
    @FXML
    private Label lbMessage;

    @FXML
    private MFXButton btnClear;

    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXButton btnEdit;

    @FXML
    private MFXButton btnSave;

    List<String> files;

    @FXML
    private MFXTextField txtCamera;

    @FXML
    private MFXTextField txtChip;

    @FXML
    private MFXTextField txtBattery;

    @FXML
    private MFXTextField txtMemory;

    @FXML
    private MFXButton btnReset;

    @FXML
    private MFXButton btnSmartPhone;

    @FXML
    private MFXButton btnMfg;

    @FXML
    private MFXButton btnImage;

    @FXML
    private MFXButton btnMfg11;

    @FXML
    private MFXButton btnAdmin;

    @FXML
    private ImageView image;

    @FXML
    private Label nameOfImage;

    @FXML
    private MFXButton fileChooser;

    @FXML
    private MFXTextField link;

    @FXML
    private Label lbAccount;

    @FXML
    private MFXButton LogOut;

    @FXML
    private Label txtUsername;
    
    @FXML
    private MFXButton btnOrder;
    
    @FXML
    void btnOrderClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToOrder();
    }

    @FXML
    void btnLogoutClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToLogin();
    }

    @FXML
    void btnClear(ActionEvent event) {
        txtName.setText("");
        txtPrice.setText("");
        txtScreen.setText("");
        txtSystem.setText("");
        txtCamera.setText("");
        txtChip.setText("");
        txtMemory.setText("");
        txtBattery.setText("");
    }

    @FXML
    void btnDelete(ActionEvent event) {
        SmartPhone selectS = tvSmartPhone.getSelectionModel().getSelectedItem();
        if (selectS == null) {
            selectAdminWarning();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Delete a SmartPhone");
            alert.setHeaderText("Are you sure you want to delete the selected SmartPhone?");
            Optional<ButtonType> ok = alert.showAndWait();
            if (ok.get() == ButtonType.OK) {
                SmartPhone deleteS = selectS;
                boolean result = smartphonedao.delete(deleteS);
                if (result) {
                    tvSmartPhone.getItems().remove(deleteS);
                    System.out.println("SmartPhone is deleted");
//                    logger.info("Delete completed");
                } else {
                    System.out.println("No SmartPhone is deleted");
//                    logger.info("Delete not completed");
                }
            }
        }
    }

    @FXML
    void btnEditClick(ActionEvent event) throws IOException {
        SmartPhone editPhone = tvSmartPhone.getSelectionModel().getSelectedItem();
        if (editPhone == null) {
            selectAdminWarning();
        } else {
            Navigator.getInstance().goToSmartPhoneEdit(editPhone);
        }
    }

    @FXML
    void btnMfgClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToManufacturerIndex();
    }

    @FXML
    void btnResetClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToSmartPhoneIndex();
    }

    @FXML
    void btnSaveClick(ActionEvent event) throws IOException {
        if (check()) {
            try {
                SmartPhone in = news();
                System.out.println(smartphonedao.selectIdByManuName(cbbMfg.getValue().toString()));
                in = smartphonedao.insert(in);
                Success();
                Navigator.getInstance().goToSmartPhoneIndex();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
    }

    @FXML
    void btnSmartPhoneClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToSmartPhoneIndex();
    }

    @FXML
    void btnAdminClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToAdminIndex();
    }

    @FXML
    void btnViewClick(MouseEvent event) {
        try {
            SmartPhone imgsmart = tvSmartPhone.getSelectionModel().getSelectedItem();
            String link = smartphonedao.SelectImg(imgsmart.getProductID().toString());
            File file = new File(link);
            String localUrl = file.toURI().toString();
            Image img = new Image(localUrl);
            image.setImage(img);
            nameOfImage.setText("Image of " + imgsmart.getName() + ":");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void initialize() {

    }

    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("#SmartPhone IndexUIController initialized!");

        cbbMfg.setItems(smartphonedao.selectmanu());
        txtUsername.setText(UserName.username);

        ObservableList<SmartPhone> smartphone = smartphonedao.selectAll();
        tvSmartPhone.setItems(smartphone);

        files = new ArrayList<>();
        files.add("*.png");
        files.add("*.jpg");

        tcName.setCellValueFactory((phone) -> {
            return phone.getValue().getNameProperty();
        });

        tcMfg.setCellValueFactory((phone) -> {
            return phone.getValue().getMfgNameProperty();
        });

        tcPrice.setCellValueFactory((phone) -> {
            return phone.getValue().getPriceProperty();
        });

        tcScreen.setCellValueFactory((phone) -> {
            return phone.getValue().getScreenProperty();
        });

        tcSystem.setCellValueFactory((phone) -> {
            return phone.getValue().getSystemProperty();
        });

        tcCamera.setCellValueFactory((phone) -> {
            return phone.getValue().getCameraProperty();
        });

        tcChip.setCellValueFactory((phone) -> {
            return phone.getValue().getChipProperty();
        });

        tcMemory.setCellValueFactory((phone) -> {
            return phone.getValue().getMemoryProperty();
        });

        tcBattery.setCellValueFactory((phone) -> {
            return phone.getValue().getBatteryProperty();
        });

    }

    private SmartPhone news() {
        SmartPhone smartphone = new SmartPhone();
        smartphone.setMfgID(smartphonedao.selectIdByManuName(cbbMfg.getValue().toString()));
        smartphone.setName(txtName.getText());
        smartphone.setPrice(txtPrice.getText());
        smartphone.setSystem(txtSystem.getText());
        smartphone.setScreen(txtScreen.getText());
        smartphone.setCamera(txtCamera.getText());
        smartphone.setChip(txtChip.getText());
        smartphone.setMemory(txtMemory.getText());
        smartphone.setBattery(txtBattery.getText());
        smartphone.setLink(link.getText());
        return smartphone;
    }

    private void selectAdminWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Please select a SmartPhone");
        alert.setHeaderText("A SmartPhone must be selected for the operation");
        alert.showAndWait();
    }

    private boolean check() {

        if (txtName.getText().isEmpty()) {
            lbMessage.setText("Name cannot be empty");
            return false;
        }

        if (txtName.getText().length() > 50) {
            lbMessage.setText("Name cannot be larger than 50 characters");
            return false;
        }

        if (cbbMfg.getValue().toString() == null) {
            lbMessage.setText("Price cannot be empty");
            return false;
        }

        if (txtPrice.getText().isEmpty()) {
            lbMessage.setText("Price cannot be empty");
            return false;
        }

        if (txtScreen.getText().isEmpty()) {
            lbMessage.setText("Screen cannot be empty");
            return false;
        }

        if (txtSystem.getText().isEmpty()) {
            lbMessage.setText("System cannot be empty");
            return false;
        }

        if (txtCamera.getText().isEmpty()) {
            lbMessage.setText("Camera cannot be empty");
            return false;
        }

        if (txtChip.getText().isEmpty()) {
            lbMessage.setText("Chip cannot be empty");
            return false;
        }

        if (txtMemory.getText().isEmpty()) {
            lbMessage.setText("Memory cannot be empty");
            return false;
        }

        if (txtBattery.getText().isEmpty()) {
            lbMessage.setText("Battery cannot be empty");
            return false;
        }
        return true;
    }

    @FXML
    void fileChooserClick(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("World Files", files));
        File f = fc.showOpenDialog(null);

        if (f != null) {
            link.setText(f.getAbsolutePath().replace("\\", "\\\\"));

        }
    }

    private void Success() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Success");
        alert.setHeaderText("You have inserted a Phone!");
        alert.showAndWait();
    }

}
