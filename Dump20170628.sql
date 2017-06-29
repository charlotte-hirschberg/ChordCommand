CREATE DATABASE  IF NOT EXISTS `chorddb` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `chorddb`;
-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: localhost    Database: chorddb
-- ------------------------------------------------------
-- Server version	5.7.11-log

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chordstructure`
--

LOCK TABLES `chordstructure` WRITE;
/*!40000 ALTER TABLE `chordstructure` DISABLE KEYS */;
INSERT INTO `chordstructure` VALUES (2,'1,3,5,7'),(1,'1,3,5,7,9'),(3,'1,3,5,7,9,11'),(4,'1,4,5,7,9');
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
  `ScaleID` int(11) NOT NULL,
  `Name` varchar(35) NOT NULL COMMENT 'The name of a scale or mode. May have "Unnamed" as a value. Must contain only letters, spaces, numbers, #, hyphen, and /',
  `WHForm` varchar(20) NOT NULL COMMENT 'A space-delimited list of 8 or fewer values. Each value may be a ''-3'', W, or H.',
  `ScaleSuffixes` varchar(25) NOT NULL COMMENT 'Comma-delimited string with no more than 8 values. Each value is either 0, 1, -1, or -2. -2 represents a pitch that is lowered two half steps, as with the 7th in a diminished scale. +1 represents a raised pitch. Must have the same number of values as corr',
  `ScaleStruc` varchar(15) NOT NULL COMMENT 'A comma-delimited string of 8 or fewer values. Each value is an integer 1-7 (inclusive). Some integers may be repeated. Example: The 7th is altered twice in the diminished scale, so our representation will be 1,2,3,4,5,6,7,7. The doubled 7s let the progra',
  PRIMARY KEY (`ScaleID`),
  UNIQUE KEY `ScaleSuffixes_UNIQUE` (`ScaleSuffixes`),
  UNIQUE KEY `ScaleID_UNIQUE` (`ScaleID`),
  UNIQUE KEY `WHForm_UNIQUE` (`WHForm`),
  UNIQUE KEY `ScaleStruc_UNIQUE` (`ScaleStruc`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scales`
--

LOCK TABLES `scales` WRITE;
/*!40000 ALTER TABLE `scales` DISABLE KEYS */;
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
/*!40000 ALTER TABLE `scaleschords` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `symbolconversion`
--

LOCK TABLES `symbolconversion` WRITE;
/*!40000 ALTER TABLE `symbolconversion` DISABLE KEYS */;
INSERT INTO `symbolconversion` VALUES (1,1,'0,0,0,0,0','M'),(7,1,'0,0,0,-1,0','7'),(8,1,'0,-1,0,-1,0','-'),(9,2,'0,-1,-1,-1','ø'),(10,2,'0,-1,-1,-2','°'),(11,1,'0,0,0,0,0','M+4'),(12,1,'0,0,0,0,0','Mb6'),(13,1,'0,0,1,0,0','M+5+4'),(14,1,'0,0,0,-1,-1','7b9'),(15,1,'0,0,0,-1,0','7+4'),(16,1,'0,0,0,-1,0','7b6'),(17,1,'0,0,1,-1,0','7+'),(18,1,'0,0,1,-1,1','7+9'),(19,4,'0,0,0,-1,0','7sus4'),(20,3,'0,-1,0,0,0,0','-M'),(21,3,'0,-1,0,-1,0,0','-b6'),(22,2,'0,-1,0,-1','-b9b6'),(23,1,'0,-1,-1,-1,0','ø#2');
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

--
-- Dumping routines for database 'chorddb'
--
/*!50003 DROP PROCEDURE IF EXISTS `areValidSuffixes` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `areValidSuffixes`(
IN str VARCHAR(25)
,OUT areValid INT
)
areValidSP:BEGIN
    SET areValid = true;
	IF(str REGEXP '^-?[0-2]{1}(,{1}-?[0-2]{1}){3,6}$') = 0
    THEN 
		SET areValid = false;
		LEAVE areValidSP;
    END IF;
    IF(str LIKE '%-0%' OR str LIKE '%,2%' OR str LIKE '2%')
    THEN
		SET areValid = false;
        LEAVE areValidSP;
	END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `ascends` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `ascends`(
IN strucStr	VARCHAR(15)
,OUT tf INT
)
isAsc:BEGIN
DECLARE strLen INT;
DECLARE i INT;
DECLARE currCh CHAR;
DECLARE prevCh CHAR;
SET tf = true;
SET strLen = LENGTH(strucStr);
SET i = 3;

WHILE i <= strLen DO
	SET currCh = SUBSTRING(strucStr, i, 1);
    SET prevCh = SUBSTRING(strucStr, i - 2, 1);
    
    IF(currCh < prevCh)
	THEN
		SET tf = false;
        LEAVE isAsc;
	END IF;
	SET i = i + 2;
END WHILE;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `countCommas` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `countCommas`(
IN str1 VARCHAR(25)
,OUT ct INT
)
BEGIN
	DECLARE totLen INT;		/*initial length*/
    DECLARE len2 INT;		/*length with commas removed*/
    SET totLen = LENGTH(str1);
    SET len2 = LENGTH(REPLACE(str1, ',', ''));
    SET ct = totLen - len2;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `insertChordRow` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertChordRow`(
IN chordFormID INT(11)		/*enforced by DB*/
,IN chordSuff VARCHAR(15)
,IN chordSymb VARCHAR(10)
)
addChord:BEGIN
DECLARE	chordForm VARCHAR(20);
SET chordForm = (SELECT ChordStruc FROM chordstructure WHERE ChordStrucID = chordFormID);

CALL countCommas(chordForm,@ct);
CALL countCommas(chordSuff,@ct2);
	IF(@ct = @ct2)
	THEN
		CALL areValidSuffixes(chordSuff,@isValid);
        IF(@isValid = 1)
        THEN
			CALL isValidSymb(chordSymb, @isValid);
            
            IF(@isValid=1)
            THEN
				INSERT INTO symbolconversion(ChordStructID, ChordSuffixes, ChordSymbol) VALUES (chordFormID,chordSuff,chordSymb);
            ELSE
				SELECT 'Chord symbol contains invalid characters or patterns of characters.';
                LEAVE addChord;
            END IF;
        ELSE
			SELECT 'Suffixes list contains invalid characters or patterns of characters.';
            LEAVE addChord;
        END IF;
	ELSE
		SELECT 'Chord structure and suffixes list contain different number of values.';
		LEAVE addChord;
	END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `insertScale` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertScale`(
IN _Name VARCHAR(35)
,IN _WHForm VARCHAR(20)
,IN _ScaleSuff VARCHAR(25)
,IN _ScaleStruc VARCHAR(15)
)
addScale:BEGIN
CALL countCommas(_WHFORM,@whCt);
CALL countCommas(_ScaleSuff,@sufCt);
CALL countCommas(_ScaleStruc,@strucCt);
IF(@whCt <> @sufCt OR @whCT <> @strucCt OR @whCT > 7)
THEN
	SELECT 'String-length mismatch';
    LEAVE addScale;
ELSE
	CALL ascends(_ScaleStruc,@strucTF);
	IF(@strucTF = 0)
	THEN
		SELECT 'Each number in ScaleStruc must equal or exceed the previous one.';
		LEAVE addScale;
	ELSE
		CALL isValidStr(_WHFORM,'^[-WH](,-?[3WH]){4,8}$',@whTF);
		CALL isValidStr(_ScaleSuff, '^[012](,-?[012]){4,8}$',@sufTF);
		CALL isValidStr(_ScaleStruc,'^[1-7](,[1-7]){4,8}$',@strucTF);
		IF(@whTF = 0 OR @sufTF = 0 OR @strucTF = 0)
		THEN
			SELECT 'Invalid characters';
			LEAVE addScale;
		ELSE
			IF(_ScaleSuff LIKE '2%' OR _ScaleSuff LIKE '%,2%' OR _ScaleSuff LIKE '%-0')
			THEN
					SELECT 'Hyphen can only precede the number 1';
			END IF;
		END IF;
    END IF;
END IF;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `isValidStr` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `isValidStr`(
IN testStr VARCHAR(25)
, IN regex VARCHAR(30)
, OUT isValid INT
)
isWH:BEGIN
SET isValid = TRUE;
IF(testStr REGEXP regex) = 0
THEN
    SET isValid = FALSE;
END IF;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `isValidSymb` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `isValidSymb`(
IN symb VARCHAR(10)
,OUT isValid INT
)
valSymb:BEGIN
DECLARE ending VARCHAR(9);
SET isValid = true;
/*starts with dim or half-dim symbol*/
IF(symb LIKE '°%' OR symb LIKE 'ø%')
THEN
	SET ending = SUBSTRING(symb FROM 2 FOR 8);
    IF(ending REGEXP '^[-M1-79#b+su]*$') = 0
    THEN
		SET isValid = false;  
	END IF;
/*not dim or half-dim*/
ELSE
	/*verify that it only contains valid char*/
	IF(symb REGEXP '^[-M1-79#b+su]+$') = 0
	THEN
		SET isValid = false;
	END IF;
END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-06-28 21:35:01
