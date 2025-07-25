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
public class Image {

    private ObjectProperty<Integer> imgId;
    private ObjectProperty<Integer> productId;
    private StringProperty url;

    public Image() {
        imgId = new SimpleObjectProperty<>();
        productId = new SimpleObjectProperty<>();
        url = new SimpleStringProperty();
    }

    public Image(String link) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Integer getImgId() {
        return imgId.get();
    }
    
    public Integer getProductId() {
        return productId.get();
    }

    public String getUrl() {
        return url.get();
    }
    
    public void setImgId(int imgId) {
        this.imgId.set(imgId);
    }
    
    public void setProductId(int productId) {
        this.productId.set(productId);
    }
    
    public void setUrl(String url) {
        this.url.set(url);
    }
    
    public ObjectProperty<Integer> getImgIdProperty() {
        return this.imgId;
    }
    
    public ObjectProperty<Integer> getProductIdProperty() {
        return this.productId;
    }
    
    public StringProperty getUrlProperty() {
        return this.url;
    }
    
    
}

