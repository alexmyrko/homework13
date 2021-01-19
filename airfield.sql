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