/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoneshop.fxui.login;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.phoneshop.fxui.Navigator;
import javafx.application.Platform;

public class WelcomeController {
    
    public void initialize() throws IOException, InterruptedException {
        loading();
    }

//    private void loading() throws IOException {
//        Navigator.getInstance().goToLogin(); // ví dụ có hàm này
//    }


    private void loading() throws IOException, InterruptedException {
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        System.out.println(">>> WelcomeController: navigating to store with user: " + com.phoneshop.model.UserName.username);
                        Navigator.getInstance().goToStore(com.phoneshop.model.UserName.username);
                    } catch (IOException ex) {
                        ex.printStackTrace(); // in chi tiết lỗi
                    }
                });
            }
        }, 2000);
    }



}
