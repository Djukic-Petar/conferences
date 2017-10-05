-- Adminer 4.2.5 MySQL dump

SET NAMES utf8;
SET time_zone = '+00:00';

DROP DATABASE IF EXISTS `pia_jul`;
CREATE DATABASE `pia_jul` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;
USE `pia_jul`;

DROP TABLE IF EXISTS `agenda`;
CREATE TABLE `agenda` (
  `idAgenda` int(11) NOT NULL AUTO_INCREMENT,
  `Program_idProgram` int(11) NOT NULL,
  `Subsctiption_idSubscription` int(11) NOT NULL,
  PRIMARY KEY (`idAgenda`),
  KEY `fk_Agenda_Program1_idx` (`Program_idProgram`),
  KEY `fk_Agenda_Subsctiption1_idx` (`Subsctiption_idSubscription`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS `author`;
CREATE TABLE `author` (
  `idAutor` int(11) NOT NULL AUTO_INCREMENT,
  `User_idUser` int(11) NOT NULL,
  `Workshop_Program_idProgram` int(11) DEFAULT NULL,
  `Talk_idTalk` int(11) DEFAULT NULL,
  `Presenting` tinyint(1) NOT NULL DEFAULT '0',
  `OpeningOrClosingTalk_idOpeningOrClosingTalk` int(11) DEFAULT NULL,
  `WelcomingSpeech_idWelcomingSpeech` int(11) DEFAULT NULL,
  PRIMARY KEY (`idAutor`),
  KEY `fk_Autor_User1_idx` (`User_idUser`),
  KEY `fk_Autor_Workshop1_idx` (`Workshop_Program_idProgram`),
  KEY `fk_Autor_Talk1_idx` (`Talk_idTalk`),
  KEY `fk_Author_OpeningOrClosingTalk1_idx` (`OpeningOrClosingTalk_idOpeningOrClosingTalk`),
  KEY `fk_Author_WelcomingSpeech1_idx` (`WelcomingSpeech_idWelcomingSpeech`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


DROP TABLE IF EXISTS `ceremony`;
CREATE TABLE `ceremony` (
  `Program_idProgram` int(11) NOT NULL,
  `Occasion` char(1) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`Program_idProgram`),
  KEY `fk_Ceremony_Program1_idx` (`Program_idProgram`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `idComment` int(11) NOT NULL AUTO_INCREMENT,
  `Content` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Liked` tinyint(1) DEFAULT NULL,
  `User_idUser` int(11) NOT NULL,
  `Program_idProgram` int(11) NOT NULL,
  PRIMARY KEY (`idComment`),
  KEY `fk_Comment_User1_idx` (`User_idUser`),
  KEY `fk_Comment_Program1_idx` (`Program_idProgram`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


DROP TABLE IF EXISTS `conference`;
CREATE TABLE `conference` (
  `idConference` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `StartDate` date NOT NULL,
  `EndDate` date NOT NULL,
  `Field_idField` int(11) NOT NULL,
  `Password` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `Location` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CalledOff` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idConference`),
  KEY `fk_Conference_Field_idx` (`Field_idField`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


DROP TABLE IF EXISTS `favourite`;
CREATE TABLE `favourite` (
  `User_idUserSrc` int(11) NOT NULL,
  `User_idUserDst` int(11) NOT NULL,
  `pad` int(11) DEFAULT NULL,
  PRIMARY KEY (`User_idUserSrc`,`User_idUserDst`),
  KEY `fk_Favourite_User2_idx` (`User_idUserDst`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


DROP TABLE IF EXISTS `field`;
CREATE TABLE `field` (
  `idField` int(11) NOT NULL AUTO_INCREMENT,
  `Field` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`idField`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


DROP TABLE IF EXISTS `file`;
CREATE TABLE `file` (
  `idFile` int(11) NOT NULL AUTO_INCREMENT,
  `Link` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `Talk_idTalk` int(11) NOT NULL,
  PRIMARY KEY (`idFile`),
  KEY `fk_File_Talk1_idx` (`Talk_idTalk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;



DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `idMessage` int(11) NOT NULL AUTO_INCREMENT,
  `Content` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `User_idUser` int(11) NOT NULL,
  `From` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`idMessage`),
  KEY `fk_Message_User1_idx` (`User_idUser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;



DROP TABLE IF EXISTS `moderator`;
CREATE TABLE `moderator` (
  `User_idUser` int(11) NOT NULL,
  `Conference_idConference` int(11) NOT NULL,
  `pad` int(11) DEFAULT NULL,
  PRIMARY KEY (`User_idUser`,`Conference_idConference`),
  KEY `fk_Moderator_Conference1_idx` (`Conference_idConference`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;



DROP TABLE IF EXISTS `openingorclosingtalk`;
CREATE TABLE `openingorclosingtalk` (
  `idOpeningOrClosingTalk` int(11) NOT NULL AUTO_INCREMENT,
  `StartTime` time NOT NULL,
  `EndTime` time NOT NULL,
  `Ceremony_Program_idProgram` int(11) DEFAULT NULL,
  `Session_Program_idProgram` int(11) DEFAULT NULL,
  PRIMARY KEY (`idOpeningOrClosingTalk`),
  KEY `fk_OpeningOrClosingTalk_Ceremony1_idx` (`Ceremony_Program_idProgram`),
  KEY `fk_OpeningOrClosingTalk_Session1_idx` (`Session_Program_idProgram`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;



DROP TABLE IF EXISTS `photo`;
CREATE TABLE `photo` (
  `idPhoto` int(11) NOT NULL AUTO_INCREMENT,
  `Link` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `Program_idProgram` int(11) NOT NULL,
  PRIMARY KEY (`idPhoto`),
  KEY `fk_Photo_Program1_idx` (`Program_idProgram`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;



DROP TABLE IF EXISTS `program`;
CREATE TABLE `program` (
  `idProgram` int(11) NOT NULL AUTO_INCREMENT,
  `Conference_idConference` int(11) NOT NULL,
  `StartDateTime` datetime NOT NULL,
  `EndDateTime` datetime NOT NULL,
  `Hall` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`idProgram`),
  KEY `fk_Program_Conference1_idx` (`Conference_idConference`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


DROP TABLE IF EXISTS `rating`;
CREATE TABLE `rating` (
  `User_idUser` int(11) NOT NULL,
  `Talk_idTalk` int(11) NOT NULL,
  `Rating` int(11) NOT NULL,
  PRIMARY KEY (`User_idUser`,`Talk_idTalk`),
  KEY `fk_Rating_Talk1_idx` (`Talk_idTalk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


DROP TABLE IF EXISTS `session`;
CREATE TABLE `session` (
  `Program_idProgram` int(11) NOT NULL,
  `Title` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`Program_idProgram`),
  KEY `fk_Session_Program1_idx` (`Program_idProgram`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


DROP TABLE IF EXISTS `subsctiption`;
CREATE TABLE `subsctiption` (
  `idSubscription` int(11) NOT NULL AUTO_INCREMENT,
  `User_idUser` int(11) NOT NULL,
  `Conference_idConference` int(11) NOT NULL,
  PRIMARY KEY (`idSubscription`),
  KEY `fk_Subsctiption_Conference1_idx` (`Conference_idConference`),
  KEY `fk_Subsctiption_User1` (`User_idUser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


DROP TABLE IF EXISTS `talk`;
CREATE TABLE `talk` (
  `idTalk` int(11) NOT NULL AUTO_INCREMENT,
  `Title` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `StartTime` time NOT NULL,
  `EndTime` time NOT NULL,
  `Session_idSession` int(11) NOT NULL,
  `Session_Program_idProgram` int(11) NOT NULL,
  PRIMARY KEY (`idTalk`),
  KEY `fk_Talk_Session1_idx` (`Session_Program_idProgram`),
  CONSTRAINT `fk_Talk_Session1` FOREIGN KEY (`Session_Program_idProgram`) REFERENCES `session` (`Program_idProgram`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `idUser` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `Surname` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `Email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `Institution` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `Username` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `Password` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `Gender` char(1) COLLATE utf8_unicode_ci NOT NULL,
  `Size` varchar(3) COLLATE utf8_unicode_ci NOT NULL,
  `LinkedIn` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isAdmin` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idUser`),
  UNIQUE KEY `Email_UNIQUE` (`Email`),
  UNIQUE KEY `Username_UNIQUE` (`Username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


DROP TABLE IF EXISTS `welcomingspeech`;
CREATE TABLE `welcomingspeech` (
  `idWelcomingSpeech` int(11) NOT NULL AUTO_INCREMENT,
  `StartTime` time NOT NULL,
  `EndTime` time NOT NULL,
  `Ceremony_idCeremony` int(11) NOT NULL,
  `Ceremony_Program_idProgram` int(11) NOT NULL,
  PRIMARY KEY (`idWelcomingSpeech`),
  KEY `fk_WelcomingSpeech_Ceremony1_idx` (`Ceremony_Program_idProgram`),
  CONSTRAINT `fk_WelcomingSpeech_Ceremony1` FOREIGN KEY (`Ceremony_Program_idProgram`) REFERENCES `ceremony` (`Program_idProgram`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


DROP TABLE IF EXISTS `workshop`;
CREATE TABLE `workshop` (
  `Program_idProgram` int(11) NOT NULL,
  `Title` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `StartDateTime` datetime DEFAULT NULL,
  `EndDateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`Program_idProgram`),
  KEY `fk_Workshop_Program1_idx` (`Program_idProgram`),
  CONSTRAINT `fk_Workshop_Program1` FOREIGN KEY (`Program_idProgram`) REFERENCES `program` (`idProgram`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


-- 2017-06-27 11:47:51
ALTER TABLE `agenda` 
  ADD CONSTRAINT `fk_Agenda_Program1` FOREIGN KEY (`Program_idProgram`) REFERENCES `program` (`idProgram`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `agenda` ADD CONSTRAINT `fk_Agenda_Subsctiption1` FOREIGN KEY (`Subsctiption_idSubscription`) REFERENCES `subsctiption` (`idSubscription`) ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE `author` 
  ADD CONSTRAINT `fk_Author_OpeningOrClosingTalk1` FOREIGN KEY (`OpeningOrClosingTalk_idOpeningOrClosingTalk`) REFERENCES `openingorclosingtalk` (`idOpeningOrClosingTalk`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `author` ADD CONSTRAINT `fk_Author_WelcomingSpeech1` FOREIGN KEY (`WelcomingSpeech_idWelcomingSpeech`) REFERENCES `welcomingspeech` (`idWelcomingSpeech`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `author` ADD CONSTRAINT `fk_Autor_Talk1` FOREIGN KEY (`Talk_idTalk`) REFERENCES `talk` (`idTalk`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `author` ADD CONSTRAINT `fk_Autor_User1` FOREIGN KEY (`User_idUser`) REFERENCES `user` (`idUser`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `author` ADD CONSTRAINT `fk_Autor_Workshop1` FOREIGN KEY (`Workshop_Program_idProgram`) REFERENCES `workshop` (`Program_idProgram`) ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE `ceremony` 
  ADD CONSTRAINT `fk_Ceremony_Program1` FOREIGN KEY (`Program_idProgram`) REFERENCES `program` (`idProgram`) ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE `comment` 
  ADD CONSTRAINT `fk_Comment_Program1` FOREIGN KEY (`Program_idProgram`) REFERENCES `program` (`idProgram`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `comment`   
  ADD CONSTRAINT `fk_Comment_User1` FOREIGN KEY (`User_idUser`) REFERENCES `user` (`idUser`) ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE `conference` 
 ADD CONSTRAINT `fk_Conference_Field` FOREIGN KEY (`Field_idField`) REFERENCES `field` (`idField`) ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE `favourite` 
  ADD CONSTRAINT `fk_Favourite_User1` FOREIGN KEY (`User_idUserSrc`) REFERENCES `user` (`idUser`) ON DELETE NO ACTION ON UPDATE NO ACTION;
  ALTER TABLE `favourite` ADD CONSTRAINT `fk_Favourite_User2` FOREIGN KEY (`User_idUserDst`) REFERENCES `user` (`idUser`) ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE `file` 
  ADD CONSTRAINT `fk_File_Talk1` FOREIGN KEY (`Talk_idTalk`) REFERENCES `talk` (`idTalk`) ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE `message` 
  ADD CONSTRAINT `fk_Message_User1` FOREIGN KEY (`User_idUser`) REFERENCES `user` (`idUser`) ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE `moderator` 
  ADD CONSTRAINT `fk_Moderator_Conference1` FOREIGN KEY (`Conference_idConference`) REFERENCES `conference` (`idConference`) ON DELETE NO ACTION ON UPDATE NO ACTION;
  ALTER TABLE `moderator` ADD CONSTRAINT `fk_Moderator_User1` FOREIGN KEY (`User_idUser`) REFERENCES `user` (`idUser`) ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE `openingorclosingtalk` 
  ADD CONSTRAINT `fk_OpeningOrClosingTalk_Ceremony1` FOREIGN KEY (`Ceremony_Program_idProgram`) REFERENCES `ceremony` (`Program_idProgram`) ON DELETE NO ACTION ON UPDATE NO ACTION;
  ALTER TABLE `openingorclosingtalk` ADD CONSTRAINT `fk_OpeningOrClosingTalk_Session1` FOREIGN KEY (`Session_Program_idProgram`) REFERENCES `session` (`Program_idProgram`) ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE `photo` 
  ADD CONSTRAINT `fk_Photo_Program1` FOREIGN KEY (`Program_idProgram`) REFERENCES `program` (`idProgram`) ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE `program` 
  ADD CONSTRAINT `fk_Program_Conference1` FOREIGN KEY (`Conference_idConference`) REFERENCES `conference` (`idConference`) ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE `rating` 
  ADD CONSTRAINT `fk_Rating_Talk1` FOREIGN KEY (`Talk_idTalk`) REFERENCES `talk` (`idTalk`) ON DELETE NO ACTION ON UPDATE NO ACTION;
  ALTER TABLE `rating` ADD CONSTRAINT `fk_Rating_User1` FOREIGN KEY (`User_idUser`) REFERENCES `user` (`idUser`) ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE `session` 
  ADD CONSTRAINT `fk_Session_Program1` FOREIGN KEY (`Program_idProgram`) REFERENCES `program` (`idProgram`) ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE `subsctiption` 
  ADD CONSTRAINT `fk_Subsctiption_Conference1` FOREIGN KEY (`Conference_idConference`) REFERENCES `conference` (`idConference`) ON DELETE NO ACTION ON UPDATE NO ACTION;
  ALTER TABLE `subsctiption` ADD CONSTRAINT `fk_Subsctiption_User1` FOREIGN KEY (`User_idUser`) REFERENCES `user` (`idUser`) ON DELETE NO ACTION ON UPDATE NO ACTION;

