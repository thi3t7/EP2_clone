/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoneshop.dao;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;

/**
 *
 * @author Admin
 */
public interface SmartPhoneDAO {

    public ObservableList<SmartPhone> selectAll();

    public ArrayList<SmartPhone> selectAlltoArray();

    public ObservableList<SmartPhone> selectByName(String name);

    public SmartPhone insert(SmartPhone newSmartPhone);

    void insertImage(int productId, String path);

    public boolean delete(SmartPhone deleteSmartPhone);

//    public ObservableList selectidManuByName(String id);
    public String SelectImg(String id);

    public ObservableList selectmanu();

    public Integer selectIdByManuName(String name);

//    public SmartPhone insert(SmartPhone newSmartphone);
//
    public boolean update(SmartPhone updateSmartphone);

    public ObservableList<SmartPhone> selectByManu(String name);
//
//    public boolean delete(SmartPhone deleteSmartphone);

    public boolean addtocartdetail(int cart, int id, int a);

    public boolean addtocartforuser(int id);

    public Integer selectIdUser(String namee);

    public boolean ifnotexits(int id);

    public int selectCart(int id);

    public int selectAmount(int id, int productid);

    public boolean updateCart(int cart, int id, int amount);

    public boolean isProductInCart(int cart, int id);

    public ObservableList<SmartPhone> selectAllCart(int cartID);
    
    public boolean ordered(int id);
    
    public int selectProductIdByName(String name);
    
    public boolean deleteCart(int id);

    public List<String> selectImagesByProductID(int productId);
}
