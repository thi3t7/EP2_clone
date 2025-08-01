package com.phoneshop;


import com.phoneshop.fxui.Navigator;
import com.phoneshop.fxui.store.ProductController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.InputStream;

public class Main extends Application {

//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        URL fxmlPath = getClass().getResource("/fxml/login/Welcome.fxml");
//        System.out.println("FXML path: " + fxmlPath);
//        // Đường dẫn chính xác đến file FXML
//        Parent root = FXMLLoader.load(fxmlPath);
//
//        primaryStage.setTitle("Phone Shop");
//        primaryStage.setScene(new Scene(root));
//        primaryStage.show();
//    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // GÁN stage vào Navigator
        Navigator.getInstance().setStage(primaryStage);

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login/LoginUI.fxml"));


        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.show();



    }




    public static void main(String[] args) {
        launch(args);
    }
}
