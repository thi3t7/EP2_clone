/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoneshop.dao;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
public class AdminDAOImp implements AdminDAO {

    public DbType database = DbType.MYSQL;

    public ObservableList<Admin> selectAll() {
        ObservableList<Admin> admin = FXCollections.observableArrayList();
        try (
                Connection conn = DbFactory.getConnection(database);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select * from admin");) {
            while (rs.next()) {
                Admin a = new Admin();
                a.setId(rs.getInt("id"));
                a.setAccount(rs.getString("account"));
                a.setPassword(rs.getString("password"));
                a.setName(rs.getString("name"));
                a.setNumberPhone(rs.getString("numberPhone"));
                a.setEmail(rs.getString("email"));

                admin.add(a);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return admin;
    }

    public Admin insert(Admin newAdmin) {
        String sql = "INSERT INTO admin (account, password,loginbyID, name, numberPhone, email) "
                + "VALUES (?, ?,?, ?, ?, ?)";

        ResultSet key = null;
        try (
                Connection conn = DbFactory.getConnection(database);
                PreparedStatement stmt
                = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            stmt.setString(1, newAdmin.getAccount());
            stmt.setString(2, HashPass(newAdmin.getPassword()));
            stmt.setInt(3,1);
            stmt.setString(4, newAdmin.getName());
            stmt.setString(5, newAdmin.getNumberPhone());
            stmt.setString(6, newAdmin.getEmail());

            int rowInserted = stmt.executeUpdate();

            if (rowInserted == 1) {
                key = stmt.getGeneratedKeys();
                key.next();
                int newkey = key.getInt(1);
                newAdmin.setId(newkey);
                return newAdmin;
            } else {
                System.out.println("No admin insert");
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

    public boolean update(Admin updateAdmin) {
        String sql = "UPDATE admin set "
                + "account = ?, "
                + "password = ?, "
                + "name = ?, "
                + "numberPhone = ?, "
                + "email = ? "
                + "WHERE id = ?";
        try (
                Connection conn = DbFactory.getConnection(database);
                PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setString(1, updateAdmin.getAccount());
            stmt.setString(2, HashPass(updateAdmin.getPassword()));
            stmt.setString(3, updateAdmin.getName());
            stmt.setString(4, updateAdmin.getNumberPhone());
            stmt.setString(5, updateAdmin.getEmail());
            stmt.setInt(6, updateAdmin.getId());

            int rowUpdate = stmt.executeUpdate();

            if (rowUpdate == 1) {
                return true;
            } else {
                System.out.println("No Admin updated");
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean delete(Admin deleteAdmin) {
        String sqlCartDetail = "DELETE FROM cart_detail WHERE cartID IN (SELECT CartID FROM cart WHERE CustomerID = ?)";
        String sqlOrder = "DELETE FROM `order` WHERE CartID IN (SELECT CartID FROM cart WHERE CustomerID = ?)";
        String sqlCart = "DELETE FROM cart WHERE CustomerID = ?";
        String sqlAdmin = "DELETE FROM admin WHERE id = ?";

        try (Connection conn = DbFactory.getConnection(database)) {
            conn.setAutoCommit(false); // Start transaction

            // 1) Xoá cart_detail
            try (PreparedStatement stmtCartDetail = conn.prepareStatement(sqlCartDetail)) {
                stmtCartDetail.setInt(1, deleteAdmin.getId());
                stmtCartDetail.executeUpdate();
            }

            // 2) Xoá order
            try (PreparedStatement stmtOrder = conn.prepareStatement(sqlOrder)) {
                stmtOrder.setInt(1, deleteAdmin.getId());
                stmtOrder.executeUpdate();
            }

            // 3) Xoá cart
            try (PreparedStatement stmtCart = conn.prepareStatement(sqlCart)) {
                stmtCart.setInt(1, deleteAdmin.getId());
                stmtCart.executeUpdate();
            }

            // 4) Xoá admin
            int rowDelete = 0;
            try (PreparedStatement stmtAdmin = conn.prepareStatement(sqlAdmin)) {
                stmtAdmin.setInt(1, deleteAdmin.getId());
                rowDelete = stmtAdmin.executeUpdate();
            }

            conn.commit();

            if (rowDelete == 1) {
                return true;
            } else {
                System.out.println("No admin deleted");
                return false;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage() + " Delete Admin");
            return false;
        }
    }



    public static String HashPass(String password) {
        String myHash = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02X", b));
            }
            myHash = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return myHash;
    }

//    public boolean checkPassword(int id, String oldpassword) {
//        boolean check = false;
//        try (
//                Connection conn = DbFactory.getConnection(database);
//                Statement stmt = conn.createStatement();
//                ResultSet rs = stmt.executeQuery("select * from admin "
//                        + "WHERE id = " + id)) {    
//            String i = rs.getString("password");
//            check = i.equals(oldpassword);
//        } catch (Exception e) {
//            System.out.println(e.getMessage() + " admin");
//        }
//        return check;
//    }
}
