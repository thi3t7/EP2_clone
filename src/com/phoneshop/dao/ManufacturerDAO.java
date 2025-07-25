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
public interface ManufacturerDAO {

    public ObservableList<Manufacturer> selectAll();

    public Manufacturer insert(Manufacturer newmfg);
//
    public boolean update(Manufacturer updateM);
//
    public boolean delete(Manufacturer deleteM);
    
}
