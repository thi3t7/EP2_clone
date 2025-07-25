-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: eproject2
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `id` int NOT NULL AUTO_INCREMENT,
  `account` varchar(50) NOT NULL,
  `password` varchar(32) NOT NULL,
  `name` varchar(255) NOT NULL,
  `loginbyID` int NOT NULL,
  `numberphone` varchar(20) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `role` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (13,'toilaai','202CB962AC59075B964B07152D234B70','hiden',2,'0972055595','toilaai@gmail.com',NULL),(14,'thiet','202CB962AC59075B964B07152D234B70','Kim Thiet',2,'0886909796','stmtpkim@gmail.com',NULL),(15,'khanh','202CB962AC59075B964B07152D234B70','khanh1',2,'0996909324','stmtpkim@gmail.com',NULL),(16,'admin123','202cb962ac59075b964b07152d234b70','Admin Mới',1,'0912345678','admin@example.com',NULL),(19,'emmy','202CB962AC59075B964B07152D234B70','Emmy McKenzie',2,'0996688732','stmtpkim@gmail.com',NULL);
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart` (
  `CartID` int NOT NULL AUTO_INCREMENT,
  `CustomerID` int DEFAULT NULL,
  `Total_Price` int DEFAULT NULL,
  PRIMARY KEY (`CartID`),
  KEY `FK_cart_admin` (`CustomerID`),
  CONSTRAINT `FK_cart_admin` FOREIGN KEY (`CustomerID`) REFERENCES `admin` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
INSERT INTO `cart` VALUES (7,13,0),(8,14,0),(10,19,0);
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart_detail`
--

DROP TABLE IF EXISTS `cart_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_detail` (
  `cartID` int NOT NULL,
  `amount` int DEFAULT NULL,
  `productID` int NOT NULL,
  PRIMARY KEY (`cartID`,`productID`),
  KEY `productID` (`productID`),
  CONSTRAINT `cart_detail_ibfk_2` FOREIGN KEY (`productID`) REFERENCES `product` (`productID`),
  CONSTRAINT `cart_detail_ibfk_3` FOREIGN KEY (`cartID`) REFERENCES `cart` (`CartID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_detail`
--

LOCK TABLES `cart_detail` WRITE;
/*!40000 ALTER TABLE `cart_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `cart_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `loginby`
--

DROP TABLE IF EXISTS `loginby`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `loginby` (
  `id` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loginby`
--

LOCK TABLES `loginby` WRITE;
/*!40000 ALTER TABLE `loginby` DISABLE KEYS */;
INSERT INTO `loginby` VALUES (1,'Admin'),(2,'Customer');
/*!40000 ALTER TABLE `loginby` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manufacturer`
--

DROP TABLE IF EXISTS `manufacturer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `manufacturer` (
  `mfgID` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `country` varchar(50) NOT NULL,
  PRIMARY KEY (`mfgID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manufacturer`
--

LOCK TABLES `manufacturer` WRITE;
/*!40000 ALTER TABLE `manufacturer` DISABLE KEYS */;
INSERT INTO `manufacturer` VALUES (1,'Apple','United States'),(2,'Samsung','Korea'),(3,'Xiaomi','China'),(4,'Vsmart','Vietnam');
/*!40000 ALTER TABLE `manufacturer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order` (
  `OrderID` int NOT NULL AUTO_INCREMENT,
  `CartID` int DEFAULT NULL,
  `Date` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`OrderID`),
  KEY `FK_order_cart` (`CartID`),
  CONSTRAINT `FK_order_cart` FOREIGN KEY (`CartID`) REFERENCES `cart` (`CartID`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

LOCK TABLES `order` WRITE;
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
/*!40000 ALTER TABLE `order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `productID` int NOT NULL AUTO_INCREMENT,
  `mfgID` int NOT NULL,
  `name` varchar(50) NOT NULL,
  `price` int NOT NULL,
  `screen` varchar(100) NOT NULL,
  `system` varchar(100) NOT NULL,
  `camera` varchar(100) NOT NULL,
  `chip` varchar(100) NOT NULL,
  `memory` varchar(100) NOT NULL,
  `battery` varchar(50) NOT NULL,
  `link` varchar(150) NOT NULL,
  PRIMARY KEY (`productID`),
  KEY `mfgID` (`mfgID`),
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`mfgID`) REFERENCES `manufacturer` (`mfgID`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,1,'iphone 12 pro max',999,'OLED 6.7\" Super Retina XDR','IOS 14','02 camere sau 12MP, camera trước: 12MP','Apple A14 Bionic','Ram 6GB, Bộ nhớ trong 512GB','3687 mAh, sạc 20 W','C:\\\\Users\\\\Admin\\\\Desktop\\\\eProject2\\\\SourceCode\\\\eProject2_set\\\\src\\\\eProject2\\\\fxui\\\\Store\\\\Image\\\\iphone-12-pro-max.png'),(2,1,'iphone 12 64g',888,'OLED 6.1\" Super Retina XDR','IOS 14','02 camere sau 12 MP, camera trước: 12 MP','Apple A14 Bionic','Ram 4GB, Bộ nhớ trong 64GB','2815 mAh, sạc 20 W','C:\\\\Users\\\\Admin\\\\Desktop\\\\eProject2\\\\SourceCode\\\\eProject2_set\\\\src\\\\eProject2\\\\fxui\\\\Store\\\\Image\\\\iphone-12-64g.png'),(36,1,'iphone 7 ',599,'LCD 4,7\"','IOS 10','1 camera','Apple A10','32GB','2000mAh','C:\\\\Users\\\\Admin\\\\Desktop\\\\eProject2\\\\SourceCode\\\\eProject2_set\\\\src\\\\eProject2\\\\fxui\\\\Store\\\\Image\\\\iphone-se.png'),(41,1,'iphone XR 128GB',599,'IPS LCD 6.1\" Liquid Retina','camera sau 12 MP, camera trước:  7 MP',' IOS 14','Apple A12 Bionic','Ram 3GB, Bộ nhớ trong 128GB','2942 mAh, sạc 15 W','C:\\\\Users\\\\Admin\\\\Desktop\\\\eProject2\\\\SourceCode\\\\eProject2_set\\\\src\\\\eProject2\\\\fxui\\\\Store\\\\Image\\\\iphone-xr.png');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-05 17:35:04
