/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoneshop.dao;

import javafx.collections.ObservableList;

/**
 *
 * @author Admin
 */
public interface ImageDAO {
    
    public ObservableList<Image> selectAll();
    
    public Integer SelectIdByNameProduct(String name);

    public ObservableList SelectAllProductName();

    public void InsertIntoImages(Integer id, String link);
}
