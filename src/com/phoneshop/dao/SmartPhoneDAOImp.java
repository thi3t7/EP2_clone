/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoneshop.dao;


import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.phoneshop.dbconnection.DbFactory;
import com.phoneshop.dbconnection.DbType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Admin
 */
public class SmartPhoneDAOImp implements SmartPhoneDAO {

    public DbType database = DbType.MYSQL;
    private ImageDAO imageDAO = new ImageDAOImp();

    public ObservableList<SmartPhone> selectAll() {
        ObservableList<SmartPhone> phone = FXCollections.observableArrayList();

        try (
                Connection conn = DbFactory.getConnection(database);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT `productID`, manufacturer.mfgID AS mfgID,manufacturer.`name` AS mfgName, product.`name` AS productName, `price`, `screen`, `camera`, `system`, `chip`, `memory`,`battery` \n"
                        + "FROM product JOIN manufacturer ON product.mfgID = manufacturer.mfgID");) {
            while (rs.next()) {
                SmartPhone s = new SmartPhone();
                s.setProductID(rs.getInt("productID"));
                s.setMfgName(rs.getString("mfgName"));
                s.setMfgID(rs.getInt("mfgID"));
                s.setName(rs.getString("productName"));
                s.setPrice(rs.getString("price"));
                s.setScreen(rs.getString("screen"));
                s.setSystem(rs.getString("system"));
                s.setCamera(rs.getString("camera"));
                s.setChip(rs.getString("chip"));
                s.setMemory(rs.getString("memory"));
                s.setBattery(rs.getString("battery"));

                // ✅ Gọi ảnh từ bảng image
                int productId = s.getProductID();
                java.util.List<String> imageLinks = imageDAO.getImagesByProductID(productId);
                s.setImageLinks(imageLinks);

                phone.add(s);
            }

        } catch (Exception e) {
            System.err.println(e.getMessage() + " SelectAll");
        }
        return phone;
    }

    public SmartPhone insert(SmartPhone newSmartPhone) {
        String sql = "INSERT INTO product(`mfgID`, `name`, `price`, `screen`, `system`, `camera`, `chip`, `memory`, `battery`) VALUES (?,?,?,?,?,?,?,?,?)";
        ResultSet key = null;

        try (
                Connection conn = DbFactory.getConnection(database);
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setInt(1, newSmartPhone.getMfgID());
            stmt.setString(2, newSmartPhone.getName());
            stmt.setString(3, newSmartPhone.getPrice());
            stmt.setString(4, newSmartPhone.getScreen());
            stmt.setString(5, newSmartPhone.getSystem());
            stmt.setString(6, newSmartPhone.getCamera());
            stmt.setString(7, newSmartPhone.getChip());
            stmt.setString(8, newSmartPhone.getMemory());
            stmt.setString(9, newSmartPhone.getBattery());

            int rowInserted = stmt.executeUpdate();

            if (rowInserted == 1) {
                key = stmt.getGeneratedKeys();
                key.next();
                int newkey = key.getInt(1);
                newSmartPhone.setProductID(newkey);

                // ✅ Thêm ảnh vào bảng image
                ImageDAO imageDAO = new ImageDAOImp();
                for (String fullPath : newSmartPhone.getImageLinks()) {
                    String fileName = new File(fullPath).getName();
                    String relativePath = "src/images/" + fileName;
                    imageDAO.InsertIntoImages(newkey, relativePath);
                }

                return newSmartPhone;
            } else {
                System.out.println("No SmartPhone insert");
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + ": insert");
            return null;
        } finally {
            try {
                if (key != null) key.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage() + " Insert");
            }
        }
    }


    public boolean delete(SmartPhone deleteSmartPhone) {
        String sqlCartDetail = "DELETE FROM cart_detail WHERE productID = ?";
        String sqlProduct = "DELETE FROM product WHERE productID = ?";

        try (
                Connection conn = DbFactory.getConnection(database);
        ) {
            conn.setAutoCommit(false); // bắt đầu transaction

            // 1) Xóa cart_detail trước
            try (PreparedStatement stmtCart = conn.prepareStatement(sqlCartDetail)) {
                stmtCart.setInt(1, deleteSmartPhone.getProductID());
                stmtCart.executeUpdate();
            }

            // 2) Sau đó xóa product
            int rowDelete = 0;
            try (PreparedStatement stmtProduct = conn.prepareStatement(sqlProduct)) {
                stmtProduct.setInt(1, deleteSmartPhone.getProductID());
                rowDelete = stmtProduct.executeUpdate();
            }

            conn.commit(); // commit transaction

            if (rowDelete == 1) {
                return true;
            } else {
                System.out.println("No smartphone deleted");
                return false;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage() + " Delete");
            return false;
        }
    }


    //    public ObservableList selectidManuByName(String id) {
//        ObservableList name = FXCollections.observableArrayList();
//        try (
//                Connection conn = DbFactory.getConnection(database);
//                Statement stmt = conn.createStatement();
//                ResultSet rs = stmt.executeQuery("SELECT `name` FROM manufacturer where mfgID = " + id);) {
//            while (rs.next()) {
//                name.add(rs.getString("name"));
//            }
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }
//
//        return name;
//    }
    public String SelectImg(String id) {
        String link = "";

        String sql = "SELECT `link` FROM product WHERE `productID` = ?";
        try (
                Connection conn = DbFactory.getConnection(database);
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                link = rs.getString("link");

                // Optional: kiểm tra xem có null hoặc empty không
                if (link == null || link.trim().isEmpty()) {
                    System.out.println("Image link is empty for productID = " + id);
                }
            } else {
                System.out.println("No image found for productID = " + id);
            }

            rs.close();
        } catch (Exception e) {
            System.err.println("SelectImg Error: " + e.getMessage());
        }

        return link;
    }


    public ObservableList selectmanu() {
        ObservableList manuname = FXCollections.observableArrayList();
        try (
                Connection conn = DbFactory.getConnection(database);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT `name` FROM manufacturer");) {
            while (rs.next()) {
                manuname.add(rs.getString("name"));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage() + " SelectManu");
        }

        return manuname;
    }

    public Integer selectIdByManuName(String namee) {
        Integer id = 0;
        try (
                Connection conn = DbFactory.getConnection(database);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT `mfgID` FROM manufacturer WHERE `name` = '" + namee + "'");) {
            while (rs.next()) {
                id = (Integer.parseInt(rs.getString("mfgID")));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage() + " SelectIdByManuName");
        }

        return id;
    }

    @Override
    public void insertImage(int productId, String path) {
        String sql = "INSERT INTO image(productID, link) VALUES (?, ?)";
        try (
                Connection conn = DbFactory.getConnection(database);
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setInt(1, productId);
            stmt.setString(2, path);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Lỗi khi insert ảnh phụ: " + e.getMessage());
        }
    }

    @Override
    public List<String> selectImagesByProductID(int productId) {
        List<String> imagePaths = new ArrayList<>();
        String sql = "SELECT link FROM image WHERE productID = ?";
        try (Connection conn = DbFactory.getConnection(database);
             PreparedStatement stmt = conn.prepareStatement(sql);)
        {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                imagePaths.add(rs.getString("link"));
            }
        } catch (Exception e) {
            System.out.println("SelectImg Error: " + e.getMessage());
        }
        return imagePaths;
    }



    public boolean update(SmartPhone editphone) {
        String sql = "UPDATE product set"
                + "`name` = ?,"
                + "`mfgID` = ?,"
                + "`price` = ?,"
                + "`screen` = ?,"
                + "`system` = ?,"
                + "`camera` = ?,"
                + "`chip` = ?,"
                + "`memory` = ?,"
                + "`battery` = ?"
                + "WHERE productID = ?";
        try (
                Connection conn = DbFactory.getConnection(database);
                PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setString(1, editphone.getName());
            stmt.setInt(2, editphone.getMfgID());
            stmt.setString(3, editphone.getPrice());
            stmt.setString(4, editphone.getScreen());
            stmt.setString(5, editphone.getSystem());
            stmt.setString(6, editphone.getCamera());
            stmt.setString(7, editphone.getChip());
            stmt.setString(8, editphone.getMemory());
            stmt.setString(9, editphone.getBattery());
            stmt.setInt(10, editphone.getProductID());

            int rowUpdate = stmt.executeUpdate();

            if (rowUpdate == 1) {
                return true;
            } else {
                System.out.println("No Phone updated");
                return false;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage() + ": update Phone");
            return false;
        }
    }

    public ArrayList<SmartPhone> selectAlltoArray() {
        ArrayList<SmartPhone> phone = new ArrayList();

        try (
                Connection conn = DbFactory.getConnection(database);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT `productID`, manufacturer.mfgID AS mfgID,manufacturer.`name` AS mfgName, product.`name` AS productName, `price`, `screen`, `camera`, `system`, `chip`, `memory`,`battery` \n"
                        + "FROM product JOIN manufacturer ON product.mfgID = manufacturer.mfgID");) {
            while (rs.next()) {
                SmartPhone s = new SmartPhone();
                s.setProductID(rs.getInt("productID"));
                s.setMfgName(rs.getString("mfgName"));
                s.setMfgID(rs.getInt("mfgID"));
                s.setName(rs.getString("productName"));
                s.setPrice(rs.getString("price"));
                s.setScreen(rs.getString("screen"));
                s.setSystem(rs.getString("system"));
                s.setCamera(rs.getString("camera"));
                s.setChip(rs.getString("chip"));
                s.setMemory(rs.getString("memory"));
                s.setBattery(rs.getString("battery"));

                // ✅ Gọi ảnh từ bảng image
                int productId = s.getProductID();
                java.util.List<String> imageLinks = imageDAO.getImagesByProductID(productId);
                s.setImageLinks(imageLinks);

                phone.add(s);
            }

        } catch (Exception e) {
            System.err.println(e.getMessage() + " SelectAlltoArray");
        }
        return phone;
    }

    public ObservableList<SmartPhone> selectByName(String name) {
        ObservableList phone = FXCollections.observableArrayList();

        try (
                Connection conn = DbFactory.getConnection(database);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT `productID`, manufacturer.mfgID AS mfgID,manufacturer.`name` AS mfgName, product.`name` AS productName, `price`, `screen`, `camera`, `system`, `chip`, `memory`,`battery`\n"
                        + "FROM product JOIN manufacturer ON product.mfgID = manufacturer.mfgID \n"
                        + "WHERE product.`name` LIKE" + "'" + "%" + name + "%" + "'");) {

            while (rs.next()) {
                SmartPhone s = new SmartPhone();
                s.setProductID(rs.getInt("productID"));
                s.setMfgName(rs.getString("mfgName"));
                s.setMfgID(rs.getInt("mfgID"));
                s.setName(rs.getString("productName"));
                s.setPrice(rs.getString("price"));
                s.setScreen(rs.getString("screen"));
                s.setSystem(rs.getString("system"));
                s.setCamera(rs.getString("camera"));
                s.setChip(rs.getString("chip"));
                s.setMemory(rs.getString("memory"));
                s.setBattery(rs.getString("battery"));

                // ✅ Gọi ảnh từ bảng image
                int productId = s.getProductID();
                java.util.List<String> imageLinks = imageDAO.getImagesByProductID(productId);
                s.setImageLinks(imageLinks);

                phone.add(s);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage() + " SelectByName");
        }

        return phone;
    }

    public ObservableList<SmartPhone> selectByManu(String name) {
        ObservableList phone = FXCollections.observableArrayList();

        try (
                Connection conn = DbFactory.getConnection(database);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT `productID`, manufacturer.mfgID AS mfgID,manufacturer.`name` AS mfgName, product.`name` AS productName, `price`, `screen`, `camera`, `system`, `chip`, `memory`,`battery`,`link` \n"
                        + "FROM product JOIN manufacturer ON product.mfgID = manufacturer.mfgID \n"
                        + "WHERE manufacturer.`name` LIKE" + "'" + "%" + name + "%" + "'");) {

            while (rs.next()) {
                SmartPhone s = new SmartPhone();
                s.setProductID(rs.getInt("productID"));
                s.setMfgName(rs.getString("mfgName"));
                s.setMfgID(rs.getInt("mfgID"));
                s.setName(rs.getString("productName"));
                s.setPrice(rs.getString("price"));
                s.setScreen(rs.getString("screen"));
                s.setSystem(rs.getString("system"));
                s.setCamera(rs.getString("camera"));
                s.setChip(rs.getString("chip"));
                s.setMemory(rs.getString("memory"));
                s.setBattery(rs.getString("battery"));

                // ✅ Gọi ảnh từ bảng image
                int productId = s.getProductID();
                java.util.List<String> imageLinks = imageDAO.getImagesByProductID(productId);
                s.setImageLinks(imageLinks);

                phone.add(s);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage() + " SelectByName");
        }

        return phone;
    }

    public boolean addtocartdetail(int cart, int id, int a) {
        String sql = "INSERT INTO cart_detail(cartID, `amount`, productID)"
                + "VALUES (?,?,?)";

        ResultSet key = null;
        try (
                Connection conn = DbFactory.getConnection(database);
                PreparedStatement stmt
                = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setInt(3, id);
            stmt.setInt(2, a);
            stmt.setInt(1, cart);

            int rowInserted = stmt.executeUpdate();

            if (rowInserted == 1) {
                return true;
            } else {
                System.out.println("No cart insert");
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + ": insert detail");
            return false;
        } finally {
            try {
                if (key != null) {
                    key.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage() + " Insert");
            }
        }
    }

    public boolean addtocartforuser(int id) {
        String sql = "INSERT INTO cart (CustomerID, Total_Price )"
                + "VALUES(?,?)";

        ResultSet key = null;
        try (
                Connection conn = DbFactory.getConnection(database);
                PreparedStatement stmt
                = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            stmt.setInt(1, id);
            stmt.setInt(2, 0);

            int rowInserted = stmt.executeUpdate();

            if (rowInserted == 1) {
                return true;
            } else {
                System.out.println("No cart insert");
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + ": insert for user");
            return false;
        } finally {
            try {
                if (key != null) {
                    key.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage() + " Insert");
            }
        }
    }

    public Integer selectIdUser(String namee) {
        Integer id = 0;
        try (
                Connection conn = DbFactory.getConnection(database);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT `id` FROM admin WHERE `account` = '" + namee + "'");) {
            while (rs.next()) {
                id = (rs.getInt("id"));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage() + " SelectId");
        }

        return id;
    }

    public boolean ifnotexits(int id) {
        try (
                Connection conn = DbFactory.getConnection(database);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT `CartID` FROM cart WHERE `CustomerID` = '" + id + "'");) {
            while (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage() + " SelectId");
        }

        return false;
    }

    public int selectCart(int id) {
        int cart = 0;
        try (
                Connection conn = DbFactory.getConnection(database);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT `CartID` FROM cart WHERE `CustomerID` = '" + id + "'");) {
            while (rs.next()) {
                cart = (rs.getInt("CartID"));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage() + " SelectId");
        }

        return cart;
    }

    public int selectAmount(int id, int productid) {
        int amount = 0;
        try (
                Connection conn = DbFactory.getConnection(database);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT `amount` FROM cart_detail WHERE `cartID` = '" + id + "'" + "AND `productID` = '" + productid + "'");) {
            while (rs.next()) {
                amount = (rs.getInt("amount"));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage() + " SelectAmount");
            return 0;
        }

        return amount;
    }

    public boolean updateCart(int cartId, int productId, int newAmount) {
        String sql = "UPDATE cart_detail SET amount = ? WHERE cartID = ? AND productID = ?";
        try (Connection conn = DbFactory.getConnection(database);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newAmount);
            stmt.setInt(2, cartId);
            stmt.setInt(3, productId);
            return stmt.executeUpdate() == 1;
        } catch (Exception e) {
            System.err.println("❌ updateCart: " + e.getMessage());
            return false;
        }
    }


    public boolean isProductInCart(int cartId, int productId) {
        String sql = "SELECT 1 FROM cart_detail WHERE cartID = ? AND productID = ?";
        try (Connection conn = DbFactory.getConnection(database);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            stmt.setInt(2, productId);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true nếu có bản ghi
        } catch (Exception e) {
            System.err.println("❌ isProductInCart: " + e.getMessage());
            return false;
        }
    }


    public ObservableList<SmartPhone> selectAllCart(int cartID) {
        ObservableList<SmartPhone> product = FXCollections.observableArrayList();
        String sql = "SELECT \n" +
                "    p.productID,\n" +
                "    p.mfgID,           -- dòng này để tránh lỗi\n" +
                "    p.name,\n" +
                "    p.price,\n" +
                "    p.chip,\n" +
                "    p.memory,\n" +
                "    p.screen,\n" +
                "    p.system,\n" +
                "    p.camera,\n" +
                "    p.battery,\n" +
                "    cd.amount,\n" +
                "    img.link AS imageLink\n" +
                "FROM cart_detail cd\n" +
                "JOIN product p ON cd.productID = p.productID\n" +
                "LEFT JOIN (\n" +
                "    SELECT i1.productID, i1.link\n" +
                "    FROM image i1\n" +
                "    JOIN (\n" +
                "        SELECT productID, MIN(imgID) AS minImgID\n" +
                "        FROM image\n" +
                "        GROUP BY productID\n" +
                "    ) i2 ON i1.productID = i2.productID AND i1.imgID = i2.minImgID\n" +
                ") img ON p.productID = img.productID\n" +
                "WHERE cd.cartID = ?;\n";
        try (
                Connection conn = DbFactory.getConnection(database);
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setInt(1, cartID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                SmartPhone s = new SmartPhone();
                s.setProductID(rs.getInt("productID"));
                s.setMfgID(rs.getInt("mfgID"));
                s.setMfgName(rs.getString("name"));  // ✅ Bây giờ sẽ có mfgName
                s.setName(rs.getString("name"));
                s.setPrice(rs.getString("price"));
                s.setScreen(rs.getString("screen"));
                s.setSystem(rs.getString("system"));
                s.setCamera(rs.getString("camera"));
                s.setChip(rs.getString("chip"));
                s.setMemory(rs.getString("memory"));
                s.setBattery(rs.getString("battery"));
                s.setAmount(rs.getInt("amount"));

                int productId = s.getProductID();
                s.setImageLinks(imageDAO.getImagesByProductID(productId));

                product.add(s);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage() + " SelectAllCart");
        }

        return product;
    }


    public boolean ordered(int id) {
        String sql = "INSERT INTO `order`(CartID, `Date`)"
                + "VALUES (?,?)";
        try (
                Connection conn = DbFactory.getConnection(database);
                PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setInt(1, id);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yy/MM/dd");
            LocalDateTime now = LocalDateTime.now();
            stmt.setString(2,now.toString());

            int rowUpdate = stmt.executeUpdate();

            if (rowUpdate == 1) {
                return true;
            } else {
                System.out.println("No order updated");
                return false;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage() + ": update order");
            return false;
        }
    }
    
    public int selectProductIdByName(String name){
        int a = 0;
        try (
                Connection conn = DbFactory.getConnection(database);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT `productID` FROM `product`"
                        + " WHERE `name` = '" + name + "'");) {
            while (rs.next()) {
                a = rs.getInt("productID");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage() + " SelectAmount");
        }

        return a;
    }
    
    public boolean deleteCart(int id){
        String sql = "DELETE from cart_detail WHERE productID = ?";

        try (
                Connection conn = DbFactory.getConnection(database);
                PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setInt(1, id);

            int rowDelete = stmt.executeUpdate();
            if (rowDelete == 1) {
                return true;
            } else {
                System.out.println("No cart deleted");
                return false;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage() + " Delete Cart");
            return false;
        }
    }
}
