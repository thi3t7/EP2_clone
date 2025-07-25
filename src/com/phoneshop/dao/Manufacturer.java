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

/**
 *
 * @author Admin
 */
public class Manufacturer {

    private ObjectProperty<Integer> mfgID;
    private StringProperty name;
    private StringProperty country;

    public Manufacturer() {
        mfgID = new SimpleObjectProperty<>(null);
        name = new SimpleStringProperty();
        country = new SimpleStringProperty();
    }
    
    public Integer getMfgID() {
        return mfgID.get();
    }

    public String getName() {
        return name.get();
    }

    public String getCountry() {
        return country.get();
    }
    
    public void setMfgID(int mfgID) {
        this.mfgID.set(mfgID);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setCountry(String country) {
        this.country.set(country);
    }
    
    public ObjectProperty<Integer> getMfgIDProperty() {
        return this.mfgID;
    }

    public StringProperty getNameProperty() {
        return this.name;
    }

    public StringProperty getCountryProperty() {
        return this.country;
    }
}
