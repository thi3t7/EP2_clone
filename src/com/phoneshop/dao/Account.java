/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoneshop.dao;

import javafx.beans.property.SimpleStringProperty;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Account {
    
    private ObjectProperty<Integer> id;
    private StringProperty UserName;
    private StringProperty Password;
    private ObjectProperty<Integer> LogBy;
    private String account;
    private StringProperty Name;
    private StringProperty NumberPhone;
    private StringProperty Email;



    public void setAccount(String username) {
        this.account = username;
    }
    
    public String getAccount() {
        return this.account;
    }

    public Account() {
        id = new SimpleObjectProperty<>(null);
        UserName = new SimpleStringProperty();
        Password = new SimpleStringProperty();
        LogBy = new SimpleObjectProperty<>(null);
        Name = new SimpleStringProperty();
        NumberPhone = new SimpleStringProperty();
        Email = new SimpleStringProperty();
    }
    
    public Integer getId() {
        return id.get();
    }
    
    public void setId(int id) {
        this.id.set(id);
    }
    
     public ObjectProperty<Integer> getIdProperty() {
        return this.id;
    }
    
    public String getUserName() {
        return UserName.get();
    }

    public String getPassword() {
        return Password.get();
    }

    public Integer getLogBy() {
        return LogBy.get();
    }
    
    public String getName() {
        return Name.get();
    }
    
    public String getNumberPhone() {
        return NumberPhone.get();
    }
    
    public String getEmail() {
        return Email.get();
    }
    
    public void setUserName(String UserName) {
        this.UserName.set(UserName);
    }

    public void setPassword(String Password) {
        this.Password.set(Password);
    }

    public void setLogBy(Integer logBy) {
        this.LogBy.set(logBy);
    }
    
    public void setName(String Password) {
        this.Name.set(Password);
    }
    
    public void setNumberPhone(String Password) {
        this.NumberPhone.set(Password);
    }
    
    public void setEmail(String Password) {
        this.Email.set(Password);
    }


    public StringProperty getUserNameProperty() {
        return this.UserName;
    }

    public StringProperty getPasswordProperty() {
        return this.Password;
    }

    public ObjectProperty<Integer> getLogByProperty() {
        return this.LogBy;
    }
    
    public StringProperty getNameProperty() {
        return this.Name;
    }
    public StringProperty getNumberPhoneProperty() {
        return this.NumberPhone;
    }
    public StringProperty getEmailProperty() {
        return this.Email;
    }
}
