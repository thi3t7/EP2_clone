/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoneshop.dao;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

/**
 *
 * @author anhoa
 */
public class SmartPhone {

//    private static Logger logger = LogManager.getLogger(SmartPhone.class);

    private ObjectProperty<Integer> productID;
    private ObjectProperty<Integer> mfgID;
    private StringProperty mfgName;
    private StringProperty name;
    private StringProperty price;
    private StringProperty screen;
    private StringProperty system;
    private StringProperty camera;
    private StringProperty chip;
    private StringProperty memory;
    private StringProperty battery;
    private StringProperty link;
    private ObjectProperty<Integer> amount;

    public SmartPhone() {
        productID = new SimpleObjectProperty<>(null);
        mfgID = new SimpleObjectProperty<>(null);
        mfgName = new SimpleStringProperty();
        name = new SimpleStringProperty();
        price = new SimpleStringProperty();
        screen = new SimpleStringProperty();
        system = new SimpleStringProperty();
        camera = new SimpleStringProperty();
        chip = new SimpleStringProperty();
        memory = new SimpleStringProperty();
        battery = new SimpleStringProperty();
        link = new SimpleStringProperty();
        amount = new SimpleObjectProperty<>(null);
    }

    public Integer getProductID() {
        return productID.get();
    }
    
    public Integer getAmount() {
        return amount.get();
    }
    
    public Integer getMfgID() {
        return mfgID.get();
    }
    
    public String getMfgName() {
        return mfgName.get();
    }

    public String getName() {
        return name.get();
    }

    public String getPrice() {
        return price.get();
    }

    public String getScreen() {
        return screen.get();
    }

    public String getSystem() {
        return system.get();
    }

    public String getCamera() {
        return camera.get();
    }

    public String getChip() {
        return chip.get();
    }

    public String getMemory() {
        return memory.get();
    }
    
    public String getBattery() {
        return battery.get();
    }
    
    public String getLink() {
        return link.get();
    }
    
    public void setProductID(int productID) {
        this.productID.set(productID);
    }
    
    public void setAmount(int amount) {
        this.amount.set(amount);
    }
    
    public void setMfgID(int mfgID) {
        this.mfgID.set(mfgID);
    }
    
    public void setMfgName(String mfgname) {
        this.mfgName.set(mfgname);
    }
    
    public void setName(String name) {
        this.name.set(name);
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public void setScreen(String screen) {
        this.screen.set(screen);
    }

    public void setSystem(String system) {
        this.system.set(system);
    }
    
    public void setCamera(String camera) {
        this.camera.set(camera);
    }
    
    public void setChip(String chip) {
        this.chip.set(chip);
    }
    
    public void setMemory(String memory) {
        this.memory.set(memory);
    }
    
    public void setBattery(String battery) {
        this.battery.set(battery);
    }
    
    public void setLink(String link) {
        this.link.set(link);
    }

    public ObjectProperty<Integer> getProductIDProperty() {
        return this.productID;
    }
    
    public ObjectProperty<Integer> getAmountProperty() {
        return this.amount;
    }
    
    public ObjectProperty<Integer> getMfgIDProperty() {
        return this.mfgID;
    }
    
    public StringProperty getMfgNameProperty() {
        return this.mfgName;
    }


    public StringProperty getNameProperty() {
        return this.name;
    }

    public StringProperty getPriceProperty() {
        return this.price;
    }
    
    public StringProperty getScreenProperty() {
        return this.screen;
    }

    public StringProperty getSystemProperty() {
        return this.system;
    }

    public StringProperty getCameraProperty() {
        return this.camera;
    }

    public StringProperty getChipProperty() {
        return this.chip;
    }
    
    public StringProperty getMemoryProperty() {
        return this.memory;
    }
    
    public StringProperty getBatteryProperty() {
        return this.battery;
    }
    
    public StringProperty getLinkProperty() {
        return this.link;
    }

}
