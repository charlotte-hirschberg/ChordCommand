-- MySQL dump 10.13  Distrib 5.7.18, for Win64 (x86_64)
--
-- Host: localhost    Database: chorddb
-- ------------------------------------------------------
-- Server version	5.7.18-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `chordstructure`
--

DROP TABLE IF EXISTS `chordstructure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chordstructure` (
  `ChordStrucID` int(11) NOT NULL AUTO_INCREMENT,
  `ChordStruc` varchar(20) NOT NULL COMMENT 'A series of comma delimited integers. Valid range 1-7 (inclusive) and 9, 11, or 13. The integers tell the calling script which MajorScale indices to retrieve. Example: 1,4,5,7,9 results in C,F,G,B,D being retrieved from MajorKeyLU.MajorScale',
  PRIMARY KEY (`ChordStrucID`),
  UNIQUE KEY `ChordStruct_UNIQUE` (`ChordStruc`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chordstructure`
--

LOCK TABLES `chordstructure` WRITE;
/*!40000 ALTER TABLE `chordstructure` DISABLE KEYS */;
INSERT INTO `chordstructure` VALUES (5,'1,3,5'),(6,'1,3,5,6'),(2,'1,3,5,7'),(1,'1,3,5,7,9'),(3,'1,3,5,7,9,11'),(4,'1,4,5,7,9');
/*!40000 ALTER TABLE `chordstructure` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `majorkeylu`
--

DROP TABLE IF EXISTS `majorkeylu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `majorkeylu` (
  `MajorKeyName` varchar(2) NOT NULL COMMENT '1 or 2 characters: a letter and an optional flat sign. A Java ENUM will indicate the starting pitch, with 0=A, 1=B..7=G. The program will treat the ENUMs as a circular array and use the modulo operator to construct pitches 1-7. Example: To construct D or Db pitch structure, the program notes that 3=D. Program performs 7 iterations, in which pitches[i] is set to i % 8',
  `MajorSuffixes` varchar(20) NOT NULL COMMENT 'Basically records the key signature. Contains 13 or fewer char. Will always contain 6 commas and 0-7 characters, each of which is a flat or sharp symbol. Example: for Eb, the field will have value b,"","",b,b,"","" to represent that Eb contains Eb, Ab, and Bb. Must have same number of values as corresponding MajorPitches entry',
  `MajorKeyID` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`MajorKeyID`),
  UNIQUE KEY `MajorKeyID_UNIQUE` (`MajorKeyName`),
  UNIQUE KEY `MajorKeyLUcol_UNIQUE` (`MajorSuffixes`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `majorkeylu`
--

LOCK TABLES `majorkeylu` WRITE;
/*!40000 ALTER TABLE `majorkeylu` DISABLE KEYS */;
INSERT INTO `majorkeylu` VALUES ('A','0,0,1,0,0,1,1',1),('Ab','-1,-1,0,-1,-1,0,0',2),('B','0,1,1,0,1,1,1',3),('Bb','-1,0,0,-1,0,0,0',4),('C','0,0,0,0,0,0,0',5),('C#','1,1,1,1,1,1,1',6),('Cb','-1,-1,-1,-1,-1,-1,-1',7),('D','0,0,1,0,0,0,1',8),('Db','-1,-1,0,-1,-1,-1,0',9),('E','0,1,1,0,0,1,1',10),('Eb','-1,0,0,-1,-1,0,0',11),('F','0,0,0,-1,0,0,0',12),('F#','1,1,1,0,1,1,1',13),('G','0,0,0,0,0,0,1',14),('Gb','-1,-1,-1,-1,-1,-1,0',15);
/*!40000 ALTER TABLE `majorkeylu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scales`
--

DROP TABLE IF EXISTS `scales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scales` (
  `ScaleID` int(11) NOT NULL AUTO_INCREMENT,
  `ScaleName` varchar(35) NOT NULL COMMENT 'The name of a scale or mode. May have "Unnamed" as a value. Must contain only letters, spaces, numbers, #, hyphen, and /',
  `WHForm` varchar(20) NOT NULL COMMENT 'A space-delimited list of 8 or fewer values. Each value may be a ''-3'', W, or H.',
  `ScaleSuffixes` varchar(25) NOT NULL COMMENT 'Comma-delimited string with no more than 8 values. Each value is either 0, 1, -1, or -2. -2 represents a pitch that is lowered two half steps, as with the 7th in a diminished scale. +1 represents a raised pitch. Must have the same number of values as corr',
  `ScaleStrucID` int(3) NOT NULL COMMENT 'A comma-delimited string of 8 or fewer values. Each value is an integer 1-7 (inclusive). Some integers may be repeated. Example: The 7th is altered twice in the diminished scale, so our representation will be 1,2,3,4,5,6,7,7. The doubled 7s let the progra',
  PRIMARY KEY (`ScaleID`),
  UNIQUE KEY `ScaleSuffixes_UNIQUE` (`ScaleSuffixes`),
  UNIQUE KEY `ScaleID_UNIQUE` (`ScaleID`),
  UNIQUE KEY `WHForm_UNIQUE` (`WHForm`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scales`
--

LOCK TABLES `scales` WRITE;
/*!40000 ALTER TABLE `scales` DISABLE KEYS */;
INSERT INTO `scales` VALUES (3,'Major','W,W,H,W,W,W,H','0,0,0,0,0,0,0',1),(4,'Mixolydian','W,W,H,W,W,H,W','0,0,0,0,0,0,-1',1),(5,'Minor/Dorian','W,H,W,W,W,H,W','0,0,-1,0,0,0,-1',1),(6,'Half Diminished','H,W,W,H,W,W,W','0,-1,-1,0,-1,-1,-1',1),(7,'Diminished','W,H,W,H,W,H,W,H','0,0,-1,0,-1,-1,-2,0',2),(14,'Major Pentatonic','W,W,-3,W,-3','0,0,0,0,0',3),(15,'Lydian','W,W,W,H,W,W,H','0,0,0,1,0,0,0',1),(16,'Bebop (Major)','W,W,H,W,H,H,W,H','0,0,0,0,0,1,0,0',4),(17,'Harmonic Major','W,W,H,W,H,-3,H','0,0,0,0,0,-1,0',1),(18,'Lydian Augmented','W,W,W,W,H,W,H','0,0,0,1,1,0,0',1),(19,'Augmented','-3,H,-3,H,-3,H','0,1,0,0,-1,0',5),(20,'6th Mode of Harmonic Minor','-3,H,W,H,W,W,H','0,1,0,1,0,0,0',1),(21,'Diminished','H,W,H,W,H,W,H,W','0,-1,1,0,1,0,0,-1',6),(22,'Blues','-3,W,H,H,-3,W','0,-1,0,1,0,-1',7),(23,'Bebop (Dominant)','W,W,H,W,W,H,H,H','0,0,0,0,0,0,-1,0',2),(24,'Spanish','H,-3,H,W,H,W,W','0,-1,0,0,0,-1,-1',1),(25,'Lydian Dominant','W,W,W,H,W,H,W','0,0,0,1,0,0,-1',1),(26,'Hindu','W,W,H,W,H,W,W','0,0,0,0,0,-1,-1',1),(27,'Whole Tone','W,W,W,W,W,W','0,0,0,1,1,-1',8),(28,'Diminished Whole Tone','H,W,H,W,W,W,W','0,-1,1,0,1,1,-1',9),(29,'Minor Pentatonic','-3,W,W,-3,W','0,-1,0,0,-1',10),(30,'Minor Bebop No. 1','W,H,H,H,W,W,H,W','0,0,-1,0,0,0,0,-1',11),(31,'Melodic Minor (ascending)','W,H,W,W,W,W,H','0,0,1,0,0,0,0',1),(32,'Minor Bebop No. 2','W,H,W,W,H,H,W,H','0,0,-1,0,0,1,0,0',4),(33,'Harmonic Minor','W,H,W,W,H,-3,H','0,0,-1,0,0,-1,0',1),(34,'Phrygian','H,W,W,W,H,W,W','0,-1,-1,0,0,-1,-1',1),(35,'Natural Minor','W,H,W,W,H,W,W','0,0,-1,0,0,-1,-1',1),(36,'Half Diminished #2','W,H,W,H,W,W,W','0,0,-1,0,-1,-1,-1',1),(37,'Bebop (Half Dim.)','H,W,W,H,H,H,W,W','0,-1,-1,0,-1,0,-1,-1',4);
/*!40000 ALTER TABLE `scales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scaleschords`
--

DROP TABLE IF EXISTS `scaleschords`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scaleschords` (
  `ChordID` int(11) NOT NULL,
  `ScaleID` int(11) NOT NULL,
  PRIMARY KEY (`ChordID`,`ScaleID`),
  KEY `ScalesBridge_idx` (`ScaleID`),
  CONSTRAINT `ChordBridge` FOREIGN KEY (`ChordID`) REFERENCES `symbolconversion` (`ChordID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `ScalesBridge` FOREIGN KEY (`ScaleID`) REFERENCES `scales` (`ScaleID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scaleschords`
--

LOCK TABLES `scaleschords` WRITE;
/*!40000 ALTER TABLE `scaleschords` DISABLE KEYS */;
INSERT INTO `scaleschords` VALUES (1,3),(7,4),(19,4),(28,4),(8,5),(26,5),(27,5),(9,6),(8,7),(10,7),(26,7),(27,7),(1,14),(7,14),(19,14),(28,14),(11,15),(1,16),(19,16),(12,17),(13,18),(1,19),(1,20),(24,20),(25,20),(1,21),(14,21),(24,21),(25,21),(1,22),(7,22),(8,22),(24,22),(25,22),(26,22),(27,22),(28,22),(7,23),(28,23),(14,24),(15,25),(16,26),(17,27),(18,28),(8,29),(26,29),(27,29),(8,30),(26,30),(27,30),(20,31),(8,32),(20,32),(29,32),(20,33),(8,34),(22,34),(8,35),(21,35),(26,35),(27,35),(23,36),(9,37),(23,37);
/*!40000 ALTER TABLE `scaleschords` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scalestructure`
--

DROP TABLE IF EXISTS `scalestructure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scalestructure` (
  `ScaleStrucID` int(11) NOT NULL AUTO_INCREMENT,
  `ScaleStruc` varchar(20) NOT NULL,
  PRIMARY KEY (`ScaleStrucID`),
  UNIQUE KEY `ScaleStructurecol_UNIQUE` (`ScaleStruc`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scalestructure`
--

LOCK TABLES `scalestructure` WRITE;
/*!40000 ALTER TABLE `scalestructure` DISABLE KEYS */;
INSERT INTO `scalestructure` VALUES (6,'1,2,2,3,4,5,6,7'),(9,'1,2,2,3,4,5,7'),(11,'1,2,3,3,4,5,6,7'),(4,'1,2,3,4,5,5,6,7'),(1,'1,2,3,4,5,6,7'),(2,'1,2,3,4,5,6,7,7'),(8,'1,2,3,4,5,7'),(3,'1,2,3,5,6'),(5,'1,2,3,5,6,7'),(7,'1,3,4,4,5,7'),(10,'1,3,4,5,7');
/*!40000 ALTER TABLE `scalestructure` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `symbolconversion`
--

DROP TABLE IF EXISTS `symbolconversion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `symbolconversion` (
  `ChordID` int(11) NOT NULL AUTO_INCREMENT,
  `ChordStructID` int(11) NOT NULL COMMENT 'Example: Cmaj, C7, and C7+4 are all based off variations (flat, sharp, unaltered) of 1, 3, 5, and 7, so they will all reference the same chord ID.',
  `ChordSuffixes` varchar(15) NOT NULL COMMENT 'A comma delimited set of signed integers representing how the basic notes of a chord are altered to produce a specific sound. Example: Cdim = 0,-1,-1,-1 represents the fact that the tonic needs no alteration and the 3rd, 5th, and 7th are lowered a half step. 1 will indicate a pitch that is raised a half-step; -2 represents a double flat. The number of values in this field must match the number of values in the related ChordStruc field (but their lengths may differ)',
  `ChordSymbol` varchar(10) NOT NULL COMMENT 'May include - (minor), M (major), the diminished and half diminished symbols, ''sus'', + (augmented), the flat symbol, and #, along with 2, 4, 5, 6, 7, 9, 11, and/or 13. ',
  PRIMARY KEY (`ChordID`),
  UNIQUE KEY `ChordSymbolID_UNIQUE` (`ChordID`),
  KEY `ChordStructFK_idx` (`ChordStructID`),
  CONSTRAINT `ChordStructFK` FOREIGN KEY (`ChordStructID`) REFERENCES `chordstructure` (`ChordStrucID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `symbolconversion`
--

LOCK TABLES `symbolconversion` WRITE;
/*!40000 ALTER TABLE `symbolconversion` DISABLE KEYS */;
INSERT INTO `symbolconversion` VALUES (1,5,'0,0,0','M'),(7,1,'0,0,0,-1','7'),(8,5,'0,-1,0','-'),(9,2,'0,-1,-1,-1','ø'),(10,2,'0,-1,-1,-2','°'),(11,1,'0,0,0,0,0','M+4'),(12,1,'0,0,0,0,0','Mb6'),(13,1,'0,0,1,0,0','M+5+4'),(14,1,'0,0,0,-1,-1','7b9'),(15,1,'0,0,0,-1,0','7+4'),(16,1,'0,0,0,-1,0','7b6'),(17,1,'0,0,1,-1,0','7+'),(18,1,'0,0,1,-1,1','7+9'),(19,4,'0,0,0,-1,0','7sus4'),(20,3,'0,-1,0,0,0,0','-M'),(21,3,'0,-1,0,-1,0,0','-b6'),(22,2,'0,-1,0,-1','-b9b6'),(23,1,'0,-1,-1,-1,0','ø#2'),(24,2,'0,0,0,0','M7'),(25,1,'0,0,0,0,0','M9'),(26,2,'0,-1,0,-1','-7'),(27,1,'0,-1,0,-1,0','-9'),(28,1,'0,0,0,-1,0','9'),(29,6,'0,-1,0,0','-6');
/*!40000 ALTER TABLE `symbolconversion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `symbolformatlu`
--

DROP TABLE IF EXISTS `symbolformatlu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `symbolformatlu` (
  `SymbolInput` varchar(5) NOT NULL COMMENT 'The variation of a symbol input by a user. Example: a major chord can be represented by maj, ma, M, or a triangle/delta, and we want to convert it to M for database purposes.',
  `DBSymbol` varchar(1) NOT NULL COMMENT 'The format that a user''s input should be converted to. Acceptable values: M (maj, ma, delta), - (m, mi, min), + (aug, #), and diminished symbol (dim).',
  PRIMARY KEY (`SymbolInput`),
  UNIQUE KEY `SymbolInput_UNIQUE` (`SymbolInput`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `symbolformatlu`
--

LOCK TABLES `symbolformatlu` WRITE;
/*!40000 ALTER TABLE `symbolformatlu` DISABLE KEYS */;
INSERT INTO `symbolformatlu` VALUES ('aug','+'),('dim','°'),('m','-'),('ma','M'),('maj','M'),('mi','-'),('min','-'),('Δ','M');
/*!40000 ALTER TABLE `symbolformatlu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transposingkeylu`
--

DROP TABLE IF EXISTS `transposingkeylu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `transposingkeylu` (
  `MajorKeyID` int(11) NOT NULL COMMENT 'May only have the value C, Eb, Bb, or F.',
  `Instrument` varchar(20) NOT NULL COMMENT 'Simply the name of an instrument, with standard capitalization',
  PRIMARY KEY (`Instrument`),
  UNIQUE KEY `Instrument_UNIQUE` (`Instrument`),
  KEY `MajorKeyFK_idx` (`MajorKeyID`),
  CONSTRAINT `MajorKeyFK` FOREIGN KEY (`MajorKeyID`) REFERENCES `majorkeylu` (`MajorKeyID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transposingkeylu`
--

LOCK TABLES `transposingkeylu` WRITE;
/*!40000 ALTER TABLE `transposingkeylu` DISABLE KEYS */;
INSERT INTO `transposingkeylu` VALUES (1,'A Clarinet'),(4,'Bass Clarinet'),(4,'Bass Sax'),(4,'Bb Clarinet'),(4,'Bb Soprano Sax'),(4,'Cornet'),(4,'Tenor Sax'),(4,'Trumpet'),(5,'Baritone B.C.'),(5,'Bassoon'),(5,'C Soprano Sax'),(5,'Flute'),(5,'Oboe'),(5,'Piccolo'),(5,'Trombone'),(5,'Tuba'),(9,'Db Flute'),(9,'Db Piccolo'),(11,'Alto Clarinet'),(11,'Alto Sax'),(11,'Bari Sax'),(11,'Eb Clarinet'),(12,'English Horn'),(12,'French Horn');
/*!40000 ALTER TABLE `transposingkeylu` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-08-08 14:22:30
