/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoneshop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.phoneshop.dbconnection.DbFactory;
import com.phoneshop.dbconnection.DbType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Admin
 */
public class ImageDAOImp implements ImageDAO {

    public DbType database = DbType.MYSQL;

    public ObservableList<Image> selectAll() {
        ObservableList<Image> image = FXCollections.observableArrayList();
        try (Connection conn = DbFactory.getConnection(database);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select * from image");) {
            while (rs.next()) {
                Image i = new Image();
                i.setImgId(rs.getInt("imgID"));
                i.setProductId(rs.getInt("productID"));
                i.setUrl(rs.getString("url"));

                image.add(i);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return image;
    }

    public Integer SelectIdByNameProduct(String name) {
        Integer id = 0;

        try (
                Connection conn = DbFactory.getConnection(database);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT `productID` FROM product WHERE name = '" + name + "'")) {
            while (rs.next()) {
                id = Integer.parseInt(rs.getString("productID"));
                
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return id;
    }

    public ObservableList SelectAllProductName() {
        ObservableList name = FXCollections.observableArrayList();

        try (
                Connection conn = DbFactory.getConnection(database);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT `name` FROM product")) {
            while (rs.next()) {
                name.add(rs.getString("name"));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return name;
    }
    
    public void InsertIntoImages(Integer id, String link) {
        String sql = "INSERT INTO `image`(productID, link)\n"
                        + "VALUES(?,?)";
        ResultSet key = null;
        try (
                Connection conn = DbFactory.getConnection(database);
                PreparedStatement stmt = 
                    conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ) {
            
            stmt.setInt(1, id);
            stmt.setString(2, link);
            int rowInserted = stmt.executeUpdate();
              
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    
    
}
