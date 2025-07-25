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
public class Admin {

    private ObjectProperty<Integer> id;
    private StringProperty account;
    private StringProperty password;
    private ObjectProperty<Integer> LogBy;
    private StringProperty name;
    private StringProperty email;
    private StringProperty numberPhone;

    public Admin() {
        id = new SimpleObjectProperty<>(null);
        account = new SimpleStringProperty();
        password = new SimpleStringProperty();
        LogBy = new SimpleObjectProperty<>(null);
        name = new SimpleStringProperty();
        email = new SimpleStringProperty();
        numberPhone = new SimpleStringProperty();
    }
    
    public Integer getId() {
        return id.get();
    }

    public String getAccount() {
        return account.get();
    }
    
    public Integer getLogBy() {
        return LogBy.get();
    }
    
    public void setLogBy(Integer Password) {
        this.LogBy.set(Password);
    }
    
    public ObjectProperty<Integer> getLogByProperty() {
        return this.LogBy;
    }

    public String getPassword() {
        return password.get();
    }

    public String getName() {
        return name.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getNumberPhone() {
        return numberPhone.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setAccount(String account) {
        this.account.set(account);
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone.set(numberPhone);
    }

    public ObjectProperty<Integer> getIdProperty() {
        return this.id;
    }

    public StringProperty getAccountProperty() {
        return this.account;
    }

    public StringProperty getPasswordProperty() {
        return this.password;
    }

    public StringProperty getNameProperty() {
        return this.name;
    }

    public StringProperty getEmailProperty() {
        return this.email;
    }

    public StringProperty getNumberPhoneProperty() {
        return this.numberPhone;
    }
}
