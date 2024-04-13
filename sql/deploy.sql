CREATE DATABASE  IF NOT EXISTS `TicketBooking` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `TicketBooking`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: myrdsinstance.c3wmcmu04yc0.ap-southeast-1.rds.amazonaws.com    Database: TicketBooking
-- ------------------------------------------------------
-- Server version	8.0.35

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
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '';

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date_time` datetime(6) DEFAULT NULL,
  `event_name` varchar(255) DEFAULT NULL,
  `venue` varchar(255) DEFAULT NULL,
  `description` varchar(8000) DEFAULT NULL,
  `event_type` varchar(255) DEFAULT NULL,
  `status` varchar(9) DEFAULT 'Active',
  `image` varchar(255) DEFAULT NULL,
  `event_manager_id` int DEFAULT NULL,
  `event_manager_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
INSERT INTO `event` VALUES (1,'2024-04-11 23:59:00.000000','test event','SMU','For the very first time, KING GNU will be setting foot in Singapore, performing in THE STAR THEATRE on 10 April 2024. KING GNU will be bringing their Asia Tour “THE GREATEST UNKNOWN” to Singapore! The four-piece band from Tokyo, KING GNU, consist of Tsuneta Daiki, Seki Yuu, Arai Kazuki and Iguchi Satoru. KING GNU has formed fans not only in Japan but internationally. KING GNU was labelled as the only Japan band to fully sold-out show in all domes in a short period of time.','concert','Active','src/main/resources/static/event_images/test event.jpg',2,NULL),(4,'2024-03-03 16:12:05.400759','test event5','SMU','test','concert','Active','src/main/resources/static/event_images/default.jpg',1,NULL),(5,'2024-05-03 16:12:00.000000','test event6','SMU','testtesttesttest','sport','Active','src/main/resources/static/event_images/default.jpg',2,NULL),(6,'2024-05-03 16:12:00.000000','test event7','SMU','testtesttest','festival','Active','src/main/resources/static/event_images/test event7.jpg',2,NULL),(7,'2024-05-13 16:12:00.000000','test event111','SMU','testtesttest','test','Active','src/main/resources/static/event_images/default.jpg',2,NULL),(11,'2024-05-03 16:12:05.400759','test event511','SMU','testtesttest','test','Cancelled','src/main/resources/static/event_images/default.jpg',2,NULL),(33,'2024-03-31 00:26:00.000000','test9','smu','223','test','Active','src/main/resources/static/event_images/test9.jpg',2,NULL),(34,'2024-04-11 00:32:00.000000','test22','smu','223','test','Active','src/main/resources/static/event_images/test22.jpg',0,NULL),(35,'2024-04-25 00:36:00.000000','test23','smu','223','test','Cancelled','src/main/resources/static/event_images/test23.jpg',2,NULL),(36,'2024-10-18 19:00:00.000000','test 25','Miami, FL Hard Rock Stadium','Taylor Swift is a seven-time GRAMMY winner, and the youngest recipient in history of the music industry’s highest honor, the GRAMMY Award for Album of the Year. She is the only female artist in music history (and just the fourth artist ever) to twice have an album hit the 1 million first-week sales figure (2010’s Speak Now and 2012’s RED). She’s a household name whose insanely catchy yet deeply personal self-penned songs transcend music genres, and a savvy businesswoman who has built a childhood dream into an empire.','concert','Cancelled','src/main/resources/static/event_images/Taylor Swift.jpg',2,NULL),(37,'2024-04-13 22:00:00.000000','Bruno Mars','Singapore Stadium','Bruno Mars, born Peter Gene Hernandez, is a well-known American singer, songwriter, record producer, and dancer. He gained prominence in the music industry in the early 2010s with his hit singles like \"Just the Way You Are,\" \"Grenade,\" and \"The Lazy Song.\" Mars is known for his versatile musical style, which blends elements of pop, R&B, funk, soul, and reggae. He has won multiple Grammy Awards and has sold millions of albums worldwide. In addition to his solo career, Mars has also collaborated with other artists and has been involved in songwriting and producing for various projects.','concert','Active','src/main/resources/static/event_images/Bruno Mars Concert.jpg',2,NULL),(38,'2024-05-13 10:00:00.000000','Taufik Batisah','Botanic Gardens','Taufik Batisah is a Singaporean singer and winner of the inaugural season of the reality singing competition \"Singapore Idol\" in 2004. Born on December 10, 1981, he gained widespread popularity in Singapore and the region after winning the competition. Taufik is known for his soulful voice and has released several albums and singles over the years, establishing himself as one of Singapore\'s prominent musicians. He has also ventured into acting and hosting, further solidifying his presence in the entertainment industry.','concert','Active','src/main/resources/static/event_images/Taufik Batisah.jpg',2,NULL),(40,'2024-04-21 13:39:00.000000','Animenz piano concert','Esplanade concert hall','Animenz 2024 Asia tour Singapore Stop','Music concert','Cancelled','src/main/resources/static/event_images/Animenz piano concert.jpg',12,NULL),(42,'2024-04-26 13:50:00.000000','test3','test13','test1','concert','Active','src/main/resources/static/event_images/default.jpg',2,NULL),(43,'2024-04-20 13:51:00.000000','test5','SMU','nsdocasdncpas','concert','Active','src/main/resources/static/event_images/default.jpg',2,NULL),(45,'2024-10-18 19:30:00.000000','Taylor Swift','Miami, FL Hard Rock Stadium','Taylor Swift is a seven-time GRAMMY winner, and the youngest recipient in history of the music industry’s highest honor, the GRAMMY Award for Album of the Year. She is the only female artist in music history (and just the fourth artist ever) to twice have an album hit the 1 million first-week sales figure (2010’s Speak Now and 2012’s RED). She’s a household name whose insanely catchy yet deeply personal self-penned songs transcend music genres, and a savvy businesswoman who has built a childhood dream into an empire.','concert','Active','src/main/resources/static/event_images/Taylor Swift.jpg',2,NULL);
/*!40000 ALTER TABLE `event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ticket_officer_restriction`
--

DROP TABLE IF EXISTS `ticket_officer_restriction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ticket_officer_restriction` (
  `id` int NOT NULL AUTO_INCREMENT,
  `event_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ticket_officer_restriction`
--

LOCK TABLES `ticket_officer_restriction` WRITE;
/*!40000 ALTER TABLE `ticket_officer_restriction` DISABLE KEYS */;
INSERT INTO `ticket_officer_restriction` VALUES (1,1,4),(18,6,4),(19,7,4),(20,5,5),(21,5,4),(22,36,4),(23,36,5),(24,41,4),(26,45,4),(27,45,5),(28,37,4);
/*!40000 ALTER TABLE `ticket_officer_restriction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ticket_type`
--

DROP TABLE IF EXISTS `ticket_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ticket_type` (
  `ticket_type_id` int NOT NULL AUTO_INCREMENT,
  `event_cat` varchar(255) DEFAULT NULL,
  `event_id` int DEFAULT NULL,
  `event_price` decimal(38,2) DEFAULT '0.00',
  `number_of_tix` int DEFAULT NULL,
  `cancellation_fee_percentage` decimal(10,2) DEFAULT '0.00',
  PRIMARY KEY (`ticket_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ticket_type`
--

LOCK TABLES `ticket_type` WRITE;
/*!40000 ALTER TABLE `ticket_type` DISABLE KEYS */;
INSERT INTO `ticket_type` VALUES (1,'cat2',1,50.00,1,60.00),(2,'cat2',4,500.00,30,0.00),(3,'cat1',1,500.00,25,0.00),(4,'cat1',5,1000.00,0,12.00),(6,'cat1',4,500.00,12,0.00),(8,'cat113',1,200.00,0,1.00),(31,'cat5',6,1.00,0,1.00),(33,'cat6',6,1.00,0,1.00),(35,'1',7,2.00,0,2.00),(36,'cat1',6,1.00,0,1.00),(37,'cat11',7,1.00,0,1.00),(38,'cat12',7,1.00,0,1.00),(39,'20',7,2.00,0,2.00),(40,'200',7,2.00,1,2.00),(41,'cat50',7,2.00,0,2.00),(42,'cat1',33,1.00,1,1.00),(43,'nklcsa',1,213.00,117,30.00),(44,'inadscasd',5,12.00,12,20.00),(45,'tet32',1,20.00,19,2.00),(46,'valsdnas',5,23.00,11,3.00),(47,'cat1',36,100.00,2996,30.00),(48,'cat2',36,90.00,1997,20.00),(49,'Cat1',37,100.00,91,2.00),(50,'Cat2',37,50.00,490,2.00),(51,'Cat3',38,100.00,998,2.00),(52,'Cat1',38,300.00,100,2.00),(53,'Cat2',38,200.00,500,2.00),(54,'Stall',39,80.00,500,10.00),(55,'premium',39,180.00,20,20.00),(56,'premium',40,200.00,50,10.00),(57,'Stall',40,80.00,200,5.00),(58,'cat1',41,100.00,3000,20.00),(59,'cat2',41,200.00,1000,15.00),(60,'cat2',45,200.00,1000,20.00),(61,'cat1',45,100.00,3000,20.00);
/*!40000 ALTER TABLE `ticket_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction`
--

DROP TABLE IF EXISTS `transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction` (
  `ticket_id` int NOT NULL AUTO_INCREMENT,
  `booking_date_time` datetime(6) DEFAULT NULL,
  `event_id` int DEFAULT NULL,
  `ticket_type_id` int DEFAULT NULL,
  `transaction_id` int DEFAULT NULL,
  `user_email` varchar(255) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ticket_id`)
) ENGINE=InnoDB AUTO_INCREMENT=257 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction`
--

LOCK TABLES `transaction` WRITE;
/*!40000 ALTER TABLE `transaction` DISABLE KEYS */;
INSERT INTO `transaction` VALUES (1,'2024-03-12 17:42:48.375824',1,8,1,'junyang.ong.2021@scis.smu.edu.sg',3,'redeemed'),(2,'2024-03-12 17:42:48.375824',1,1,1,'junyang.ong.2021@scis.smu.edu.sg',3,'refunded'),(3,'2024-03-12 17:42:48.375824',1,1,1,'junyang.ong.2021@scis.smu.edu.sg',3,'refunded'),(4,'2024-03-12 17:50:00.110409',5,8,1,'junyang.ong.2021@scis.smu.edu.sg',3,'cancelled'),(5,'2024-03-12 17:50:19.437964',5,8,2,'test4@gmail.com',4,'cancelled'),(6,'2024-03-12 18:46:17.322705',6,31,3,'junyang.ong.2021@scis.smu.edu.sg',3,'active'),(146,'2024-04-10 00:31:58.559751',1,3,1,'junyang.ong.2021@scis.smu.edu.sg',3,'active'),(147,'2024-04-10 00:32:02.153766',1,3,1,'junyang.ong.2021@scis.smu.edu.sg',3,'active'),(148,'2024-04-10 00:33:22.483168',1,1,5,'customer@gmail.com',14,'active'),(149,'2024-04-10 00:33:22.483168',1,1,5,'customer@gmail.com',14,'active'),(150,'2024-04-10 00:45:24.967554',1,3,5,'customer@gmail.com',14,'active'),(183,'2024-04-10 05:10:04.797873',6,33,6,'hujingmin123@gmail.com',6,'refunded'),(185,'2024-04-10 15:31:10.133195',5,44,7,'hujingmin123@gmail.com',6,'refunded'),(186,'2024-04-10 15:32:25.753896',1,3,8,'hujingmin123@gmail.com',6,'active'),(187,'2024-04-10 15:39:55.547828',1,43,8,'hujingmin123@gmail.com',6,'active'),(188,'2024-04-10 15:40:51.679504',5,46,7,'hujingmin123@gmail.com',6,'active'),(189,'2024-04-10 15:41:58.066831',5,46,7,'hujingmin123@gmail.com',6,'active'),(191,'2024-04-10 21:48:04.529862',1,1,9,'junyang.ong.2021@scis.smu.edu.sg',4,'active'),(192,'2024-04-10 21:48:55.765570',1,1,9,'junyang.ong.2021@scis.smu.edu.sg',4,'active'),(193,'2024-04-10 21:57:41.410866',1,1,9,'ong_junyang@outlook.com',4,'active'),(194,'2024-04-10 22:04:22.313071',1,1,9,'ong_junyang@outlook.com',4,'active'),(195,'2024-04-10 22:18:45.721172',5,44,1,'junyang.ong.2021@scis.smu.edu.sg',3,'active'),(196,'2024-04-10 22:21:25.088666',1,3,9,'ong_junyang@outlook.com',4,'active'),(197,'2024-04-10 23:17:51.344054',6,33,3,'junyang.ong.2021@scis.smu.edu.sg',3,'active'),(198,'2024-04-10 23:19:42.488954',5,44,1,'junyang.ong.2021@scis.smu.edu.sg',3,'active'),(199,'2024-04-10 23:50:16.024290',5,44,1,'junyang.ong.2021@scis.smu.edu.sg',3,'active'),(200,'2024-04-11 00:00:01.942829',1,1,9,'hujingmin123@gmail.com',4,'active'),(201,'2024-04-11 00:00:35.538316',1,1,9,'hujingmin123@gmail.com',4,'active'),(202,'2024-04-11 00:02:51.632049',1,1,9,'ong_junyang@outlook.com',4,'active'),(203,'2024-04-11 00:08:25.877999',1,3,9,'junyang.ong.2021@scis.smu.edu.sg',4,'active'),(204,'2024-04-11 00:20:36.086746',7,35,10,'junyang.ong.2021@scis.smu.edu.sg',3,'active'),(205,'2024-04-11 00:20:57.564005',7,38,10,'junyang.ong.2021@scis.smu.edu.sg',3,'active'),(206,'2024-04-11 00:21:56.237616',1,3,9,'ong_junyang@outlook.com',4,'active'),(207,'2024-04-11 00:23:05.115540',1,3,9,'ong_junyang@outlook.com',4,'active'),(208,'2024-04-11 00:25:02.168930',1,3,9,'ong_junyang@outlook.com',4,'active'),(209,'2024-04-11 00:25:13.023795',1,3,9,'ong_junyang@outlook.com',4,'active'),(210,'2024-04-11 00:25:39.574037',1,3,9,'ong_junyang@outlook.com',4,'active'),(211,'2024-04-11 00:27:49.851026',1,3,9,'ong_junyang@outlook.com',4,'active'),(212,'2024-04-11 00:28:12.245173',1,3,9,'ong_junyang@outlook.com',4,'active'),(213,'2024-04-11 00:31:01.647175',1,43,9,'test3@gmail.com',4,'active'),(214,'2024-04-11 00:31:16.228859',1,3,9,'ong_junyang@outlook.com',4,'active'),(215,'2024-04-11 06:27:22.401276',1,3,11,'hujingmin123@gmail.com',5,'active'),(216,'2024-04-11 09:25:36.389184',1,3,9,'hujingmin123@gmail.com',4,'active'),(217,'2024-04-11 12:09:00.366475',5,44,12,'tester123@gmail.com',23,'active'),(218,'2024-04-11 12:09:00.366475',5,46,12,'tester123@gmail.com',23,'active'),(219,'2024-04-12 01:51:56.684392',5,46,12,'tester123@gmail.com',23,'active'),(223,'2024-04-12 21:50:11.912692',36,48,13,'junyang.ong.2021@scis.smu.edu.sg',3,'cancelled'),(224,'2024-04-12 21:50:50.489259',36,47,13,'junyang.ong.2021@scis.smu.edu.sg',3,'cancelled'),(225,'2024-04-12 21:53:10.587970',36,48,13,'junyang.ong.2021@scis.smu.edu.sg',3,'cancelled'),(226,'2024-04-12 21:54:17.450413',36,47,13,'junyang.ong.2021@scis.smu.edu.sg',3,'cancelled'),(228,'2024-04-12 22:41:22.392930',5,44,14,'customer@gmail.com',14,'active'),(231,'2024-04-12 23:31:10.217280',38,51,15,'dummydummy10293@gmail.com',26,'active'),(232,'2024-04-12 23:31:26.898779',38,51,15,'dummydummy10293@gmail.com',26,'refunded'),(233,'2024-04-12 23:57:21.740745',5,44,1,'junyang.ong.2021@scis.smu.edu.sg',3,'active'),(234,'2024-04-12 23:58:30.147538',7,35,10,'junyang.ong.2021@scis.smu.edu.sg',3,'active'),(235,'2024-04-12 23:59:44.332412',7,40,10,'junyang.ong.2021@scis.smu.edu.sg',3,'active'),(236,'2024-04-13 00:06:40.003732',7,39,10,'junyang.ong.2021@scis.smu.edu.sg',3,'active'),(237,'2024-04-13 14:15:34.538159',37,50,16,'hujingmin123@gmail.com',4,'active'),(238,'2024-04-13 15:38:39.587059',5,44,7,'hujingmin123@gmail.com',6,'active'),(239,'2024-04-13 18:43:45.752331',37,49,16,'hujingmin123@gmail.com',4,'active'),(240,'2024-04-13 18:43:45.752331',37,50,16,'hujingmin123@gmail.com',4,'active'),(246,'2024-04-13 18:51:56.995519',37,49,16,'hujingmin123@gmail.com',4,'active'),(256,'2024-04-13 19:33:11.363273',37,49,16,'kimm234432@gmail.com',4,'redeemed');
/*!40000 ALTER TABLE `transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `dtype` varchar(31) NOT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `wallet` decimal(38,2) DEFAULT NULL,
  `event_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('Event_Manager',1,'test1@gmail.com','test1','$2a$10$9ctZajbxx.kVcms/Yj58N.8zTOIL56VZV2FdkdG4MqOiPjG2wZDsW',1000.00,NULL,NULL),('Event_Manager',2,'test2@gmail.com','Jy','$2a$10$.FHFpbn13nqAxGsyRPmU4ubCmyTr4IHajVTs0k0o2xHMUouywcaPu',996.00,NULL,NULL),('Customer',3,'junyang.ong.2021@scis.smu.edu.sg','test3','$2a$10$DuUjdWnS0TFbzPTMFdBR0uRmt6umcbmEwmEBhzGlPbicf8OqImEhi',796.00,NULL,NULL),('Ticketing_Officer',4,'test4@gmail.com','Mary Jane','$2a$10$zZyQ258zHQaWKPl.3SvH9.YLqmptTN7cjBe2M9sE1.RPjloXW.pn6',800.00,NULL,NULL),('Ticketing_Officer',5,'test5@gmail.com','Ronald Mcmuffins','$2a$10$imt1La9qNVpW2C67Lqmyo.9osMP2Nrr2Czp4ORbVJ4p9PBXebeAci',895.00,NULL,NULL),('Customer',6,'hujingmin123@gmail.com','jm','$2a$10$DuUjdWnS0TFbzPTMFdBR0uRmt6umcbmEwmEBhzGlPbicf8OqImEhi',987578.80,NULL,NULL),('Event_Manager',12,'manager@gmail.com','manager','$2a$10$LiNN0zCRwohEu7RWURllSuo8km9Ux2VrSnsFHQKh/GfqVoOOz1FkK',1000.00,NULL,NULL),('Customer',13,'123@gmail.com','123','$2a$10$c.5NqL/1gAtGQ0xfkbnha.wYOZ1L8E6/kBMB/mX84TR1gXMIXkZ..',1000.00,NULL,NULL),('Customer',14,'customer@gmail.com','customer','$2a$10$UhAFilSQktR/i6JhoBQY2ulfeD5HarhnghJdGIGrba.DbXPjvUjlW',400.00,NULL,NULL),('Customer',21,'test23@gmail.com','test23','$2a$10$Jvhajc063UtrVJa.R45TFexbHBDVGHlTvUjR.XUL1tXBQISU3EhPq',1000.00,NULL,NULL),('Customer',22,'test24@gmail.com','test24','$2a$10$qHYgDUokNU0Qttfat1OUne5uNuj9KBYdmHE8kSOohTjun5pMl3Uuq',1000.00,NULL,NULL),('Customer',23,'tester123@gmail.com','tester','$2a$10$JhRcOz2t69rGlktNPDorM.nP3HD9v.gVINQPWPBsU5/iz98nPXxMC',1000.00,NULL,NULL),('Customer',26,'dummydummy10293@gmail.com','Daniel','$2a$10$0M5oMa8RzbtKh5bE9rgHpe.fycOkWOR.iFtfVdIASbojEcNjr0hBC',950.00,NULL,NULL),('Customer',27,'kimm234432@gmail.com','Kimmm','$2a$10$5XXFVEgBY.is3VVdwHTbleMlO5uM0M.aANF1d7CJoiMM4HyMps0fa',1000.00,NULL,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-13 21:35:23
