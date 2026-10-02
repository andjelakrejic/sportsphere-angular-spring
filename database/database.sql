CREATE DATABASE  IF NOT EXISTS `pia_project` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `pia_project`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: pia_project
-- ------------------------------------------------------
-- Server version	8.4.7

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
  `user_id` int NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (9);
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `athlete`
--

DROP TABLE IF EXISTS `athlete`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `athlete` (
  `user_id` int NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `athlete`
--

LOCK TABLES `athlete` WRITE;
/*!40000 ALTER TABLE `athlete` DISABLE KEYS */;
INSERT INTO `athlete` VALUES (1),(2),(3),(8),(10),(11),(12);
/*!40000 ALTER TABLE `athlete` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `athlete_facility_block`
--

DROP TABLE IF EXISTS `athlete_facility_block`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `athlete_facility_block` (
  `id` int NOT NULL AUTO_INCREMENT,
  `athlete_id` int NOT NULL,
  `facility_id` int NOT NULL,
  `no_show_count` int NOT NULL DEFAULT '0',
  `blocked` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_athlete_facility` (`athlete_id`,`facility_id`),
  KEY `fk_afb_facility` (`facility_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `athlete_facility_block`
--

LOCK TABLES `athlete_facility_block` WRITE;
/*!40000 ALTER TABLE `athlete_facility_block` DISABLE KEYS */;
INSERT INTO `athlete_facility_block` VALUES (1,1,1,4,1),(2,2,1,4,1);
/*!40000 ALTER TABLE `athlete_facility_block` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `athlete_sport`
--

DROP TABLE IF EXISTS `athlete_sport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `athlete_sport` (
  `athlete_id` int NOT NULL,
  `sport_id` int NOT NULL,
  PRIMARY KEY (`athlete_id`,`sport_id`),
  KEY `sport_id` (`sport_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `athlete_sport`
--

LOCK TABLES `athlete_sport` WRITE;
/*!40000 ALTER TABLE `athlete_sport` DISABLE KEYS */;
INSERT INTO `athlete_sport` VALUES (1,1),(1,2),(1,4),(2,1),(2,3),(3,2),(3,4),(8,1),(8,5),(11,3),(11,6),(12,5);
/*!40000 ALTER TABLE `athlete_sport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `court`
--

DROP TABLE IF EXISTS `court`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `court` (
  `id` int NOT NULL AUTO_INCREMENT,
  `facility_id` int NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` enum('OPEN','CLOSED','HALL') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `capacity` int NOT NULL,
  `equipment_description` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sport_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `facility_id` (`facility_id`),
  KEY `fk_court_sport` (`sport_id`)
) ENGINE=MyISAM AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `court`
--

LOCK TABLES `court` WRITE;
/*!40000 ALTER TABLE `court` DISABLE KEYS */;
INSERT INTO `court` VALUES (1,1,'Main Indoor Court A','CLOSED',200,'basketball hoops, electronic scoreboard, wooden flooring',1),(2,1,'Main Indoor Court B','OPEN',180,'basketball hoops, training equipment, seating benches',1),(3,1,'Indoor Training Court','CLOSED',150,'adjustable hoops, cones, agility ladders',1),(4,2,'Outdoor Court North','OPEN',120,'basic hoops, concrete surface, night lighting',2),(5,3,'Stadium Court 1','OPEN',300,'professional hoops, bleachers, sound system',3),(6,4,'Community Court','OPEN',100,'basic hoops, asphalt surface',5),(7,2,'Outdoor Court South','CLOSED',160,'multi-sport flooring, portable hoops, scoreboard',2),(8,5,'Recreation Court','OPEN',140,'standard hoops, fenced perimeter, lighting system',3),(9,2,'Secondary Training Court','OPEN',130,'training cones, adjustable hoops, rubber surface',2),(10,1,'Training Court C','CLOSED',5,'Small enclosed practice court with rubber flooring and portable hoops.',1),(11,7,'Olimpijska Arena Nord','CLOSED',24,'Sertifikovani tarafet pod, podesive mreže za odbojku, elektronski semafor i 10 Mikasa lopti.',2),(12,7,'Letnji Kavez 3x3','OPEN',8,'Zvanična 3x3 gumirana podloga, zglobni obruči, reflektori za noćne termine i profesionalne Molten lopte.',3),(13,1,'Indoor Volleyball Hall','HALL',60,'Regulation volleyball net, padded flooring, scoreboard',4),(14,8,'Tennis Court 1','OPEN',4,'Available tennis balls and rackets',2),(15,9,'Olimpijska Arena Nord','CLOSED',24,'Sertifikovani tarafet pod, podesive mreže za odbojku, elektronski semafor i 10 Mikasa lopti.',2),(16,9,'Letnji Kavez 3x3','OPEN',8,'Zvanična 3x3 gumirana podloga, zglobni obruči, reflektori za noćne termine i profesionalne Molten lopte.',3);
/*!40000 ALTER TABLE `court` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equipment`
--

DROP TABLE IF EXISTS `equipment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `sport_id` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `stock_quantity` int NOT NULL DEFAULT '0',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `sport_id` (`sport_id`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipment`
--

LOCK TABLES `equipment` WRITE;
/*!40000 ALTER TABLE `equipment` DISABLE KEYS */;
INSERT INTO `equipment` VALUES (1,'Football Pro3',1,2999.00,11,'football.jpg','Professional match football'),(2,'Shin Guards',1,899.00,28,'shinguards.jpg','Lightweight protective shin guards'),(3,'Tennis Racket',2,5499.00,10,'racket.jpg','Carbon fiber tennis racket'),(4,'Tennis Balls (3pk)',2,499.00,50,'tennisballs.jpg','Official pressure tennis balls'),(5,'Basketball',3,3299.00,12,'basketball.jpg','Indoor/outdoor basketball size 7'),(6,'Basketball Jersey',3,1899.00,20,'jersey.jpg','Breathable mesh jersey'),(7,'Volleyball',4,2499.00,8,'volleyball.jpg','Official size volleyball'),(8,'Knee Pads',4,1199.00,25,'kneepads.jpg','Protective volleyball knee pads'),(9,'Dumbbell Set 10kg',5,4999.00,6,'dumbbells.jpg','Rubber coated dumbbell pair'),(10,'Resistance Bands',5,799.00,39,'bands.jpg','Set of 5 resistance bands'),(11,'Football Pro2',1,3499.00,10,'football_pro2.jpg','2026 version Football Pro'),(12,'Tennis Racket Pro',2,7000.00,10,'equipment_1785072016223_racket.jpg','Wilson Pro 2026');
/*!40000 ALTER TABLE `equipment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equipment_orders`
--

DROP TABLE IF EXISTS `equipment_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipment_orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `equipment_id` int NOT NULL,
  `quantity` int NOT NULL,
  `price_at_purchase` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  KEY `equipment_id` (`equipment_id`)
) ENGINE=MyISAM AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipment_orders`
--

LOCK TABLES `equipment_orders` WRITE;
/*!40000 ALTER TABLE `equipment_orders` DISABLE KEYS */;
INSERT INTO `equipment_orders` VALUES (1,1,1,1,2999.00),(2,1,2,2,899.00),(3,2,3,1,5499.00),(4,3,5,1,3299.00),(5,3,8,1,1199.00),(6,4,1,3,2999.00),(7,5,9,1,4999.00),(8,6,1,1,2999.00),(9,7,2,1,899.00),(10,8,1,1,2999.00),(11,9,3,1,5499.00),(12,10,7,2,2499.00),(13,11,1,1,2999.00),(14,12,2,1,899.00),(15,13,10,1,799.00);
/*!40000 ALTER TABLE `equipment_orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `facility`
--

DROP TABLE IF EXISTS `facility`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `facility` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `city` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `working_hours_from` time DEFAULT NULL,
  `working_hours_to` time DEFAULT NULL,
  `price_per_hour` decimal(10,2) DEFAULT NULL,
  `max_no_shows` int DEFAULT '3',
  `status` enum('ACTIVE','INACTIVE','PENDING') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'PENDING',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `facility`
--

LOCK TABLES `facility` WRITE;
/*!40000 ALTER TABLE `facility` DISABLE KEYS */;
INSERT INTO `facility` VALUES (1,'Sport Arena Belgrade','Belgrade','Bulevar Arsenija Carnojevica 58','Modern multisport center','07:00:00','23:00:00',1200.00,4,'ACTIVE'),(2,'Tennis Club Zemun','Belgrade','Cara Dusana 12','Professional tennis club','08:00:00','22:00:00',800.00,3,'ACTIVE'),(3,'Multisport Novi Sad','Novi Sad','Bulevar Oslobodjenja 5','Sports center in the heart of NS','06:00:00','22:00:00',1000.00,3,'ACTIVE'),(4,'Fitness Center Plus','Nis','Vozdova 10','Modern fitness center','06:00:00','23:00:00',600.00,3,'INACTIVE'),(5,'Basketball Arena BG','Belgrade','Vojvode Stepe 22','Professional basketball arena','09:00:00','21:00:00',1500.00,3,'ACTIVE'),(6,'New Fitness Hub Belgrade','Belgrade','Ulica 11','Newly registered facility awaiting admin approval','08:00:00','23:00:00',1500.00,1,'PENDING'),(7,'Panonija Sport Hub','Novi Sad','Futoški put 12A','Savremeni trenažni kompleks prilagođen za rekreativce i profesionalne timove.','06:30:00','23:30:00',1500.00,3,'ACTIVE'),(8,'Sportski Centar Banjica','Belgrade','Krunska 37','New sports facility with Tennis courts and Olympic sized swimming pools','08:00:00','22:00:00',2000.00,2,'ACTIVE');
/*!40000 ALTER TABLE `facility` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `facility_image`
--

DROP TABLE IF EXISTS `facility_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `facility_image` (
  `id` int NOT NULL AUTO_INCREMENT,
  `facility_id` int NOT NULL,
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `facility_id` (`facility_id`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `facility_image`
--

LOCK TABLES `facility_image` WRITE;
/*!40000 ALTER TABLE `facility_image` DISABLE KEYS */;
INSERT INTO `facility_image` VALUES (1,1,'facility13.jpg'),(2,1,'facility11.jpg'),(3,1,'facility12.jpg'),(4,2,'facility22.jpg'),(5,2,'facility21.jpg'),(6,3,'facility3.jpg'),(7,5,'facility5.jpg'),(8,7,'facility72.jpg'),(9,7,'facility71.jpg');
/*!40000 ALTER TABLE `facility_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `facility_reaction`
--

DROP TABLE IF EXISTS `facility_reaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `facility_reaction` (
  `id` int NOT NULL AUTO_INCREMENT,
  `facility_id` int NOT NULL,
  `athlete_id` int NOT NULL,
  `type` enum('LIKE','DISLIKE','COMMENT') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `facility_id` (`facility_id`),
  KEY `athlete_id` (`athlete_id`)
) ENGINE=MyISAM AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `facility_reaction`
--

LOCK TABLES `facility_reaction` WRITE;
/*!40000 ALTER TABLE `facility_reaction` DISABLE KEYS */;
INSERT INTO `facility_reaction` VALUES (1,1,1,'LIKE',NULL,'2026-06-17 15:37:26'),(2,2,1,'LIKE',NULL,'2026-06-17 15:37:26'),(3,2,2,'LIKE',NULL,'2026-06-17 15:37:26'),(4,3,1,'LIKE',NULL,'2026-06-17 15:37:26'),(5,3,2,'LIKE',NULL,'2026-06-17 15:37:26'),(6,3,3,'LIKE',NULL,'2026-06-17 15:37:26'),(7,5,1,'LIKE',NULL,'2026-06-17 15:37:26'),(8,1,1,'COMMENT','Really well maintained courts, will book again.','2026-06-18 07:10:00'),(9,1,2,'COMMENT','Good location, easy to find parking.','2026-06-19 10:00:00'),(10,1,1,'COMMENT','Came back for a second session, still great.','2026-06-20 15:45:00'),(11,1,3,'COMMENT','Staff were friendly and helpful.','2026-06-21 06:30:00'),(12,1,2,'COMMENT','Bit crowded on weekends but worth it.','2026-06-22 17:15:00'),(13,1,1,'COMMENT','Lighting in the evening could be improved.','2026-06-23 18:00:00'),(14,2,1,'COMMENT','Nice smaller facility, less crowded.','2026-06-19 08:00:00'),(15,2,2,'COMMENT','Equipment was a bit outdated.','2026-06-20 09:30:00'),(16,3,1,'COMMENT','Best courts I have used so far.','2026-06-21 12:00:00'),(17,3,2,'COMMENT','Clean facilities, would recommend.','2026-06-22 14:20:00'),(18,5,1,'COMMENT','Solid experience overall, good value.','2026-06-24 11:10:00'),(19,1,2,'LIKE',NULL,'2026-07-02 13:17:06'),(20,7,1,'LIKE',NULL,'2026-07-10 10:00:00'),(21,7,2,'LIKE',NULL,'2026-07-11 11:00:00'),(22,7,3,'LIKE',NULL,'2026-07-12 09:15:00'),(23,5,3,'DISLIKE',NULL,'2026-06-25 09:00:00'),(24,2,3,'DISLIKE',NULL,'2026-06-26 09:30:00');
/*!40000 ALTER TABLE `facility_reaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `facility_sport`
--

DROP TABLE IF EXISTS `facility_sport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `facility_sport` (
  `facility_id` int NOT NULL,
  `sport_id` int NOT NULL,
  PRIMARY KEY (`facility_id`,`sport_id`),
  KEY `sport_id` (`sport_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `facility_sport`
--

LOCK TABLES `facility_sport` WRITE;
/*!40000 ALTER TABLE `facility_sport` DISABLE KEYS */;
INSERT INTO `facility_sport` VALUES (1,1),(1,3),(1,4),(2,2),(3,3),(3,4),(4,5),(5,3),(7,2),(7,3),(8,2);
/*!40000 ALTER TABLE `facility_sport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `individual_training`
--

DROP TABLE IF EXISTS `individual_training`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `individual_training` (
  `id` int NOT NULL AUTO_INCREMENT,
  `athlete_id` int NOT NULL,
  `trainer_id` int NOT NULL,
  `facility_id` int NOT NULL,
  `sport_id` int NOT NULL,
  `scheduled_at` datetime NOT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'BOOKED',
  `training_date` date NOT NULL DEFAULT (curdate()),
  `time_from` time NOT NULL DEFAULT '00:00:00',
  `time_to` time NOT NULL DEFAULT '00:00:00',
  `court_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `athlete_id` (`athlete_id`),
  KEY `trainer_id` (`trainer_id`),
  KEY `facility_id` (`facility_id`),
  KEY `sport_id` (`sport_id`),
  KEY `fk_training_court` (`court_id`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `individual_training`
--

LOCK TABLES `individual_training` WRITE;
/*!40000 ALTER TABLE `individual_training` DISABLE KEYS */;
INSERT INTO `individual_training` VALUES (1,1,6,1,1,'2026-07-07 09:00:00','BOOKED','2026-08-07','09:00:00','10:00:00',1),(9,1,7,2,2,'2026-07-26 14:50:29','BOOKED','2026-07-28','17:00:00','18:00:00',4),(4,1,6,1,1,'2026-07-23 11:35:50','BOOKED','2026-08-01','13:00:00','14:00:00',1),(5,1,6,1,1,'2026-07-24 14:15:55','NO_SHOW','2026-07-26','11:00:00','12:00:00',1),(6,1,6,1,1,'2026-07-25 10:56:01','CONFIRMED','2026-07-26','10:00:00','11:00:00',1),(7,1,6,1,1,'2026-07-25 14:39:09','BOOKED','2026-07-31','18:00:00','19:00:00',1),(8,1,6,1,1,'2025-11-14 09:30:00','NO_SHOW','2025-11-14','10:00:00','11:00:00',NULL),(10,1,7,2,2,'2026-07-26 14:51:09','BOOKED','2026-08-01','16:00:00','17:00:00',4),(11,12,7,2,2,'2026-07-26 20:48:48','BOOKED','2026-07-31','16:00:00','17:00:00',4);
/*!40000 ALTER TABLE `individual_training` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `athlete_id` int NOT NULL,
  `total_price` decimal(10,2) NOT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ORDERED',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `athlete_id` (`athlete_id`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,1,4797.00,'CANCELED','2025-06-20 08:30:00'),(2,1,5499.00,'PICKED UP','2025-06-10 12:00:00'),(3,1,4498.00,'CANCELED','2025-06-05 07:15:00'),(4,1,8997.00,'ORDERED','2026-07-24 12:30:12'),(5,1,4999.00,'ORDERED','2026-07-24 12:31:46'),(6,1,2999.00,'PICKED UP','2026-07-24 12:35:15'),(7,1,899.00,'PICKED UP','2026-07-24 12:37:19'),(8,1,2999.00,'PICKED UP','2026-07-24 12:39:31'),(9,2,5499.00,'PICKED UP','2025-09-12 10:20:00'),(10,3,4998.00,'PICKED UP','2026-07-20 16:45:00'),(11,1,2999.00,'ORDERED','2026-07-25 22:21:42'),(12,1,899.00,'CANCELED','2026-07-26 12:59:39'),(13,12,799.00,'CANCELED','2026-07-26 18:36:14');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `promotion`
--

DROP TABLE IF EXISTS `promotion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `promotion` (
  `id` int NOT NULL AUTO_INCREMENT,
  `facility_id` int NOT NULL,
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `sport_id` int DEFAULT NULL,
  `date_from` date NOT NULL,
  `date_to` date NOT NULL,
  `discount_type` enum('PERCENTAGE','FIXED') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `discount_value` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `facility_id` (`facility_id`),
  KEY `sport_id` (`sport_id`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promotion`
--

LOCK TABLES `promotion` WRITE;
/*!40000 ALTER TABLE `promotion` DISABLE KEYS */;
INSERT INTO `promotion` VALUES (1,1,'Summer Football Deal',1,'2026-06-01','2026-07-31','PERCENTAGE',20.00),(2,2,'Weekend Tennis Special',2,'2026-06-15','2026-06-30','FIXED',500.00),(3,3,'Basketball Summer Camp',3,'2026-06-10','2026-08-10','PERCENTAGE',15.00),(4,5,'Early Bird Discount',3,'2026-06-01','2026-06-20','FIXED',300.00),(5,1,'Spring Volleyball Offer',4,'2026-04-01','2026-05-31','PERCENTAGE',15.00),(6,1,'Football Sale',1,'2026-07-21','2026-07-25','PERCENTAGE',10.00),(7,7,'Grand Opening Discount',2,'2026-07-20','2026-08-15','PERCENTAGE',25.00),(8,8,'Sale for court bookings before 15h!',2,'2026-08-01','2026-08-08','PERCENTAGE',20.00),(9,8,'Weekend tennis ball discount! ',2,'2026-07-25','2026-07-25','FIXED',1000.00);
/*!40000 ALTER TABLE `promotion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation` (
  `id` int NOT NULL AUTO_INCREMENT,
  `court_id` int NOT NULL,
  `athlete_id` int NOT NULL,
  `sport_id` int NOT NULL,
  `date` date NOT NULL,
  `time_from` time NOT NULL,
  `time_to` time NOT NULL,
  `status` enum('BOOKED','CONFIRMED','NO_SHOW','CANCELLED') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `court_id` (`court_id`),
  KEY `athlete_id` (`athlete_id`),
  KEY `sport_id` (`sport_id`)
) ENGINE=MyISAM AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
INSERT INTO `reservation` VALUES (1,1,1,1,'2026-07-26','14:00:00','15:00:00','CONFIRMED'),(5,3,1,1,'2026-01-08','18:00:00','19:00:00','CONFIRMED'),(9,1,1,1,'2026-07-26','08:00:00','10:00:00','CONFIRMED'),(19,2,1,1,'2026-07-26','11:00:00','12:00:00','NO_SHOW'),(11,4,2,2,'2026-06-29','09:00:00','10:00:00','NO_SHOW'),(12,2,1,1,'2026-05-14','08:00:00','09:00:00','NO_SHOW'),(13,13,1,4,'2026-07-18','19:00:00','20:00:00','CONFIRMED'),(15,12,1,3,'2026-07-25','09:30:00','11:00:00','NO_SHOW'),(16,5,3,3,'2026-08-10','11:00:00','12:00:00','CONFIRMED'),(17,4,3,2,'2026-08-14','12:00:00','13:00:00','CONFIRMED'),(18,1,1,1,'2026-07-26','07:00:00','08:00:00','NO_SHOW'),(22,14,12,2,'2026-07-27','17:00:00','18:00:00','BOOKED'),(23,14,12,2,'2026-07-27','18:00:00','20:00:00','BOOKED'),(24,1,1,1,'2026-07-27','18:00:00','19:00:00','BOOKED');
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sport`
--

DROP TABLE IF EXISTS `sport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sport` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sport`
--

LOCK TABLES `sport` WRITE;
/*!40000 ALTER TABLE `sport` DISABLE KEYS */;
INSERT INTO `sport` VALUES (1,'Football'),(2,'Tennis'),(3,'Basketball'),(4,'Volleyball'),(5,'Fitness'),(6,'Swimming');
/*!40000 ALTER TABLE `sport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teammate_ad`
--

DROP TABLE IF EXISTS `teammate_ad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teammate_ad` (
  `id` int NOT NULL AUTO_INCREMENT,
  `athlete_id` int NOT NULL,
  `sport_id` int NOT NULL,
  `city` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `date` date NOT NULL,
  `time_slot` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `total_players_needed` int NOT NULL,
  `missing_players` int NOT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `athlete_id` (`athlete_id`),
  KEY `sport_id` (`sport_id`)
) ENGINE=MyISAM AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teammate_ad`
--

LOCK TABLES `teammate_ad` WRITE;
/*!40000 ALTER TABLE `teammate_ad` DISABLE KEYS */;
INSERT INTO `teammate_ad` VALUES (1,1,1,'Beograd','2026-06-25','18:00-19:00',4,1,'INACTIVE','2026-06-23 16:14:34'),(2,1,2,'Novi Sad','2026-06-26','20:00-21:30',2,0,'INACTIVE','2026-06-23 16:14:34'),(3,1,3,'Beograd','2026-06-20','16:00-17:00',5,0,'INACTIVE','2026-06-23 16:14:34'),(4,2,1,'Beograd','2026-06-27','19:00-20:00',6,2,'INACTIVE','2026-06-23 16:14:34'),(5,3,4,'Niš','2026-06-28','17:00-18:30',3,0,'INACTIVE','2026-06-23 16:14:34'),(6,4,5,'Kragujevac','2026-06-29','21:00-22:00',2,2,'INACTIVE','2026-06-23 16:14:34'),(7,1,2,'Belgrade','2026-07-09','18:00-19:00',1,0,'INACTIVE','2026-07-06 13:35:08'),(8,1,4,'Nis','2026-07-09','15:00-16:00',1,0,'INACTIVE','2026-07-06 14:00:58'),(9,2,1,'Beograd','2026-08-02','19:00-20:00',4,1,'ACTIVE','2026-07-24 09:00:00'),(10,1,3,'Novi Sad','2026-08-05','17:00-18:00',2,0,'INACTIVE','2026-07-25 08:30:00'),(11,1,2,'Belgrade','2026-08-01','18:00-19:00',1,1,'INACTIVE','2026-07-25 13:51:10'),(12,2,4,'Nis','2026-08-01','10:00-11:00',2,1,'INACTIVE','2026-07-25 14:43:21'),(13,1,6,'Nis','2026-07-25','15:00-16:00',2,2,'INACTIVE','2026-07-25 23:33:48'),(16,12,2,'Belgrade','2026-08-08','18:00-19:00',2,2,'ACTIVE','2026-07-26 18:36:01');
/*!40000 ALTER TABLE `teammate_ad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teammate_request`
--

DROP TABLE IF EXISTS `teammate_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teammate_request` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ad_id` int NOT NULL,
  `athlete_id` int NOT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_ad_athlete` (`ad_id`,`athlete_id`),
  KEY `athlete_id` (`athlete_id`)
) ENGINE=MyISAM AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teammate_request`
--

LOCK TABLES `teammate_request` WRITE;
/*!40000 ALTER TABLE `teammate_request` DISABLE KEYS */;
INSERT INTO `teammate_request` VALUES (1,1,2,'REJECTED','2026-06-23 16:14:50'),(2,1,3,'APPROVED','2026-06-23 16:14:50'),(3,1,4,'APPROVED','2026-06-23 16:14:50'),(4,4,1,'APPROVED','2026-06-23 16:14:50'),(5,5,1,'REJECTED','2026-06-23 16:14:50'),(6,6,2,'PENDING','2026-06-24 14:20:18'),(7,5,2,'APPROVED','2026-06-24 14:21:12'),(8,2,3,'APPROVED','2026-06-24 14:39:23'),(9,6,1,'PENDING','2026-07-06 13:36:17'),(10,7,2,'APPROVED','2026-07-06 13:46:41'),(11,2,2,'APPROVED','2026-07-06 13:49:21'),(12,8,3,'APPROVED','2026-07-06 14:01:31'),(13,9,3,'REJECTED','2026-07-24 10:15:00'),(14,10,2,'REJECTED','2026-07-25 09:00:00'),(15,9,1,'APPROVED','2026-07-25 13:51:21'),(16,11,2,'REJECTED','2026-07-25 13:52:38'),(17,12,1,'REJECTED','2026-07-25 14:43:40'),(18,12,8,'APPROVED','2026-07-25 14:46:11'),(19,10,8,'REJECTED','2026-07-25 15:05:19'),(20,13,2,'REJECTED','2026-07-25 23:47:44'),(21,14,1,'APPROVED','2026-07-25 23:57:09'),(22,10,12,'APPROVED','2026-07-26 18:49:13'),(23,10,11,'APPROVED','2026-07-26 18:52:13');
/*!40000 ALTER TABLE `teammate_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trainer`
--

DROP TABLE IF EXISTS `trainer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trainer` (
  `user_id` int NOT NULL,
  `specialization` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `price_per_hour` decimal(10,2) DEFAULT NULL,
  `facility_id` int NOT NULL,
  `status` enum('ACTIVE','INACTIVE') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE',
  PRIMARY KEY (`user_id`),
  KEY `facility_id` (`facility_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trainer`
--

LOCK TABLES `trainer` WRITE;
/*!40000 ALTER TABLE `trainer` DISABLE KEYS */;
INSERT INTO `trainer` VALUES (6,'Strength & Conditioning',25.00,1,'ACTIVE'),(7,'Tennis Coaching',30.00,2,'INACTIVE');
/*!40000 ALTER TABLE `trainer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trainer_sport`
--

DROP TABLE IF EXISTS `trainer_sport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trainer_sport` (
  `trainer_id` int NOT NULL,
  `sport_id` int NOT NULL,
  PRIMARY KEY (`trainer_id`,`sport_id`),
  KEY `sport_id` (`sport_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trainer_sport`
--

LOCK TABLES `trainer_sport` WRITE;
/*!40000 ALTER TABLE `trainer_sport` DISABLE KEYS */;
INSERT INTO `trainer_sport` VALUES (6,1),(7,2);
/*!40000 ALTER TABLE `trainer_sport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `firstname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `lastname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `profile_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` enum('PENDING','APPROVED','REJECTED') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'PENDING',
  `role` enum('ATHLETE','WORKER','ADMIN','TRAINER') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'marko_j','$2a$10$tECKJL/OOx/hsMfdgFSCM.YO59f5xOUdAgy6yD6BUihmaIL77Nr82','Marko','Jović','marko@email.com','+381601112223','1783344395455_image2.png','APPROVED','ATHLETE','2026-06-17 10:34:53'),(2,'ana_ns','$2a$10$AR9Cz8q3O4O/rkmvKjYUQeYvZhir87nBjZ7AcEDUyL6mH20zA.1MS','Ana','Nikolić','ana@email.com','+381611234567','1783345035965_image3.png','APPROVED','ATHLETE','2026-06-17 10:34:53'),(3,'petar_bg','$2a$10$AR9Cz8q3O4O/rkmvKjYUQeYvZhir87nBjZ7AcEDUyL6mH20zA.1MS','Petar','Bogdanović','petar@email.com','+381621234567','image1.png','PENDING','ATHLETE','2026-06-17 10:34:53'),(4,'milan_w','$2a$10$AR9Cz8q3O4O/rkmvKjYUQeYvZhir87nBjZ7AcEDUyL6mH20zA.1MS','Milan','Milovic','milan@email.com','+381631234567','1784897243930_image4.png','APPROVED','WORKER','2026-06-17 10:34:53'),(5,'jelena_w','$2a$10$AR9Cz8q3O4O/rkmvKjYUQeYvZhir87nBjZ7AcEDUyL6mH20zA.1MS','Jelena','Stanic','jelena@email.com','+381641234567','image9.png','APPROVED','WORKER','2026-06-17 10:34:53'),(6,'marko_pet','$2a$10$dummyhashforsakeoftesting','Marko','Petrović','marko.trainer@test.com','+381623526166','trainer1.png','APPROVED','TRAINER','2026-07-03 14:04:23'),(7,'ana_jov','$2a$10$dummyhashforsakeoftesting','Ana','Jovanović','ana.trainer@test.com','+38169776544','trainer2.png','PENDING','TRAINER','2026-07-03 14:04:27'),(8,'andjela_k','$2a$10$JhTzGQyC60Qn2nTfVKMSMOYLQG8x9XMVQ.OmJ/9ILrNMv3E/QBThy','Andjela','Krejic','andjelakrejic@gmail.com','+381691234123','1784801799781_avatar_AK_1784801795156.png','APPROVED','ATHLETE','2026-07-23 10:16:39'),(9,'admin','$2a$10$AR9Cz8q3O4O/rkmvKjYUQeYvZhir87nBjZ7AcEDUyL6mH20zA.1MS','Admin','Admin','admin@sportsphere.com','+381600000000','default-avatar.png','APPROVED','ADMIN','2026-07-23 11:04:28'),(10,'una_k','$2a$10$LbmER1ebwHXii2FlTQSRReI9PMd5NZ8QcPNtBa4jKvM.dw33SSs9W','Una','Lastname2','una@test.com','+38167888888','1784877213560_image3.png','REJECTED','ATHLETE','2026-07-24 07:13:33'),(11,'andj_k','$2a$10$VoFOHYigZwvu9XtLkfALfeE7gmm1Df0tZZT0ewQcSx32j5ryceRy2','Andjela','Lastname1','andjela@test.com','+381691234123','default-avatar.png','APPROVED','ATHLETE','2026-07-24 07:25:55'),(12,'zoka_z','$2a$10$Dsy64lDNOUv/HpBV9OsAFe46b7oQ0pSTsWIOVVOsxs83rqlxLEMJm','Zoka','Zokic','zoka@gmail.com','+3816777777','1785090844149_image7.png','APPROVED','ATHLETE','2026-07-26 18:32:04'),(13,'neko','$2a$10$mzeCm06sx/yhWXNqH6qfPeYnVbPXc7aWEcn6swX5bFtyRFpPr2/M6','neko','neko','neko','+381691234123','default-avatar.png','APPROVED','WORKER','2026-07-27 11:37:46');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `worker`
--

DROP TABLE IF EXISTS `worker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `worker` (
  `user_id` int NOT NULL,
  `facility_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `registration_number` char(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tax_id` char(9) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `worker`
--

LOCK TABLES `worker` WRITE;
/*!40000 ALTER TABLE `worker` DISABLE KEYS */;
INSERT INTO `worker` VALUES (4,'Sport Arena Belgrade','Bulevar Arsenija Carnojevica 58','12345678','123456789'),(5,'Tennis Club Zemun','Cara Dusana 12','87654321','987654321'),(13,'Sport Arena Belgrade','Bulevar Arsenija Carnojevica 58','12345678','123456789');
/*!40000 ALTER TABLE `worker` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `worker_facility`
--

DROP TABLE IF EXISTS `worker_facility`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `worker_facility` (
  `worker_id` int NOT NULL,
  `facility_id` int NOT NULL,
  PRIMARY KEY (`worker_id`,`facility_id`),
  KEY `facility_id` (`facility_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `worker_facility`
--

LOCK TABLES `worker_facility` WRITE;
/*!40000 ALTER TABLE `worker_facility` DISABLE KEYS */;
INSERT INTO `worker_facility` VALUES (4,1),(4,6),(4,7),(4,9),(5,2),(13,1);
/*!40000 ALTER TABLE `worker_facility` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-02 14:49:14
