/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoneshop.dao;

/**
 *
 * @author Admin
 */
public interface AccountDAO {

    public Account check(String acc, String password);
    
    public Account insertNewMem(Account newMember);
}
