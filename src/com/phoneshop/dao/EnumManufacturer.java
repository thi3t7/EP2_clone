/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoneshop.dao;

import java.util.HashMap;

/**
 *
 * @author anhoa
 */
public enum EnumManufacturer {
    Huawei, Apple, Samsung, Oppo, Xiaomi;
    
     private static HashMap<String, EnumManufacturer> manufacturer;
        static {
            manufacturer = new HashMap<>();
            for (EnumManufacturer m : EnumManufacturer.values()) {
                manufacturer.put(m.toString(), m);
            }
        }

        public static EnumManufacturer parse(String m) {
            return manufacturer.get(m);
        }        
}
