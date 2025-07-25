/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoneshop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.phoneshop.dbconnection.DbFactory;
import com.phoneshop.dbconnection.DbType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Admin
 */
public class ManufacturerDAOImp implements ManufacturerDAO{

    public DbType database = DbType.MYSQL;
    
    public ObservableList<Manufacturer> selectAll() {
        ObservableList<Manufacturer> mfg = FXCollections.observableArrayList();
        try (
                Connection conn = DbFactory.getConnection(database);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select * from manufacturer");){
            while(rs.next()){
                Manufacturer m = new Manufacturer();
                m.setMfgID(rs.getInt("mfgID"));
                m.setName(rs.getString("name"));
                m.setCountry(rs.getString("country"));
                mfg.add(m);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage() + ":insert manufacturer");
        }
        return mfg;
    }

    public Manufacturer insert(Manufacturer newmfg) {
        String sql = "INSERT INTO manufacturer (`name`, `country`) "
                + "VALUES (?, ?)";
        ResultSet key = null;
        try (Connection conn = DbFactory.getConnection(database);
                PreparedStatement stmt
                = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);){
            
            stmt.setString(1, newmfg.getName());
            stmt.setString(2, newmfg.getCountry());
            
            int rowInserted = stmt.executeUpdate();
            
            if (rowInserted == 1) {
                key = stmt.getGeneratedKeys();
                key.next();
                int newkey = key.getInt(1);
                newmfg.setMfgID(newkey);
                return newmfg;
            } else {
                System.out.println("No manufacturer insert");
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        } finally {
            try {
                if (key != null) {
                    key.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public boolean delete(Manufacturer deleteM) {
        String sql = "DELETE from manufacturer WHERE mfgID = ?";
        try (
                Connection conn = DbFactory.getConnection(database);
                PreparedStatement stmt = conn.prepareStatement(sql);){
            
            stmt.setInt(1, deleteM.getMfgID());
            
            int rowDelete = stmt.executeUpdate();
            
            if (rowDelete == 1) {
                return true;
            } else {
                System.out.println("No manufacturer deleted");
                return false;
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage() + ": delete");
            return false;
        }
    }

    public boolean update(Manufacturer updateM) {
        String sql = "UPDATE manufacturer set "
                + "`name` = ?,"
                + "`country` = ? "
                + "WHERE mfgID = ?";
        
        try (
                Connection conn = DbFactory.getConnection(database);
                PreparedStatement stmt = conn.prepareStatement(sql);){
            
            stmt.setString(1, updateM.getName());
            stmt.setString(2, updateM.getCountry());
            stmt.setInt(3, updateM.getMfgID());
            
            int rowUpdate = stmt.executeUpdate();

            if (rowUpdate == 1) {
                return true;
            } else {
                System.out.println("No Manufacturer updated");
                return false;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage() + ": update manufacturer");
            return false;
        }
    }
    
}
