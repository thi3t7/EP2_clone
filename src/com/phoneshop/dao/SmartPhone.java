/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoneshop.dao;

import javafx.beans.property.StringProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.List;

public class SmartPhone {
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
    private ObjectProperty<Integer> amount;
    private String link;


    // ✅ Danh sách đường dẫn ảnh
    private List<String> imageLinks = new ArrayList<>();

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
        amount = new SimpleObjectProperty<>(null);
    }

    // ==== Getter và Setter bình thường ====
    public Integer getProductID() { return productID.get(); }
    public void setProductID(int productID) { this.productID.set(productID); }

    public Integer getAmount() { return amount.get(); }
    public void setAmount(int amount) { this.amount.set(amount); }

    public Integer getMfgID() { return mfgID.get(); }
    public void setMfgID(int mfgID) { this.mfgID.set(mfgID); }

    public String getMfgName() { return mfgName.get(); }
    public void setMfgName(String mfgname) { this.mfgName.set(mfgname); }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

    public String getPrice() { return price.get(); }
    public void setPrice(String price) { this.price.set(price); }

    public String getScreen() { return screen.get(); }
    public void setScreen(String screen) { this.screen.set(screen); }

    public String getSystem() { return system.get(); }
    public void setSystem(String system) { this.system.set(system); }

    public String getCamera() { return camera.get(); }
    public void setCamera(String camera) { this.camera.set(camera); }

    public String getChip() { return chip.get(); }
    public void setChip(String chip) { this.chip.set(chip); }

    public String getMemory() { return memory.get(); }
    public void setMemory(String memory) { this.memory.set(memory); }

    public String getBattery() { return battery.get(); }
    public void setBattery(String battery) { this.battery.set(battery); }

    // ==== Property (nếu cần bind với TableView) ====
    public ObjectProperty<Integer> getProductIDProperty() { return this.productID; }
    public ObjectProperty<Integer> getAmountProperty() { return this.amount; }
    public ObjectProperty<Integer> getMfgIDProperty() { return this.mfgID; }
    public StringProperty getMfgNameProperty() { return this.mfgName; }
    public StringProperty getNameProperty() { return this.name; }
    public StringProperty getPriceProperty() { return this.price; }
    public StringProperty getScreenProperty() { return this.screen; }
    public StringProperty getSystemProperty() { return this.system; }
    public StringProperty getCameraProperty() { return this.camera; }
    public StringProperty getChipProperty() { return this.chip; }
    public StringProperty getMemoryProperty() { return this.memory; }
    public StringProperty getBatteryProperty() { return this.battery; }

    // ==== Ảnh ====
    public List<String> getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(List<String> imageLinks) {
        this.imageLinks = imageLinks;
    }

    public String getFirstImageLink() {
        return (imageLinks != null && !imageLinks.isEmpty()) ? imageLinks.get(0) : "";
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
