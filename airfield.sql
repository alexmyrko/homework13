CREATE SCHEMA IF NOT EXISTS `airport`;
USE `airport`;
SET NAMES utf8 ;

DROP TABLE IF EXISTS `planes`;
CREATE TABLE IF NOT EXISTS `planes` (
`plane_id` INT AUTO_INCREMENT NOT NULL,
`model` VARCHAR(20) NOT NULL,
`seats` INT NOT NULL,
PRIMARY KEY (`plane_id`));

DROP TABLE IF EXISTS `serial_numbers`;
CREATE TABLE IF NOT EXISTS `serial_numbers` (
`id` INT AUTO_INCREMENT NOT NULL,
`plane_id` INT NOT NULL,
`serial_number` VARCHAR(16) NOT NULL,
PRIMARY KEY (`id`));

DROP TABLE IF EXISTS `pilots`;
CREATE TABLE IF NOT EXISTS `pilots` (
`pilot_id` INT AUTO_INCREMENT NOT NULL,
`name` VARCHAR(40) NOT NULL,
`age` INT NOT NULL,
PRIMARY KEY (`pilot_id`));

DROP TABLE IF EXISTS `planes_and_pilots`;
CREATE TABLE IF NOT EXISTS `planes_and_pilots`(
`id` INT AUTO_INCREMENT NOT NULL,
`pilot_id` INT NOT NULL,
`plane_id` INT NOT NULL,
PRIMARY KEY (`id`)) ;

INSERT INTO planes VALUES (1,'Airbus A320',150), (2, 'Boeing 747',410), (3,'Embraer E195',115);
INSERT INTO serial_numbers VALUES (1,1,'HA-LSC'), (2,1,'HA-SCM'), (3,2,'UR-PSK'),(4,3,'UR-EMG');
INSERT INTO pilots VALUES (1,'Erich Smidt',30),(2,'Jonas Ericsson',47), (3,'Peter Kuba',27),(4,'Umberto Gomes',38);
INSERT INTO `planes_and_pilots` VALUES (1,1,2),(2,1,3),(3,2,1),(4,2,2),(5,3,3),(6,4,1),(7,4,3);