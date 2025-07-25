/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoneshop.dao;

import com.phoneshop.dbconnection.DbFactory;
import com.phoneshop.dbconnection.DbType;
import static com.phoneshop.dao.AdminDAOImp.HashPass;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class AccountDAOImp implements AccountDAO {

    public DbType database = DbType.MYSQL;

    public Account check(String acc, String password) {
        Account Acc = null;
        String sql = "SELECT account, password, loginbyID, name, numberphone, email "
                + "FROM admin WHERE BINARY account = ? AND password = ?";
        try (
                Connection conn = DbFactory.getConnection(database);
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setString(1, acc);
            stmt.setString(2, HashPass(password)); // Nếu bạn lưu pass đã hash

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Acc = new Account();
                Acc.setUserName(rs.getString("account"));
                Acc.setPassword(rs.getString("password"));
                Acc.setLogBy(rs.getInt("loginbyID"));
                Acc.setName(rs.getString("name"));
                Acc.setNumberPhone(rs.getString("numberphone"));
                Acc.setEmail(rs.getString("email"));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return Acc;
    }



    public Account insertNewMem(Account newMember){
        String sql = "INSERT INTO admin(account,password,name,loginbyID,numberphone,email) "
                + "VALUES (?,?,?,?,?,?)";
        ResultSet key = null;
       try (
                Connection conn = DbFactory.getConnection(database);
                PreparedStatement stmt
                = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString(1, newMember.getUserName());
            stmt.setString(2, HashPass(newMember.getPassword()));
            stmt.setString(3, newMember.getName());
            stmt.setInt(4, 2);
            stmt.setString(5, newMember.getNumberPhone());
            stmt.setString(6, newMember.getEmail());

            int rowInserted = stmt.executeUpdate();

            if (rowInserted == 1) {
                key = stmt.getGeneratedKeys();
                key.next();
                int newKey = key.getInt(1);
                newMember.setId(newKey);
                return newMember;

            } else {
                System.out.println("No member inserted");
                return null;
            }

        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            if (key != null) {
                try {
                    key.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

}
