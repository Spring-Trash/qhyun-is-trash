-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `springtrash` DEFAULT CHARACTER SET utf8 ;
USE `springtrash` ;

-- -----------------------------------------------------
-- Table `mydb`.`Members`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `springtrash`.`Members` (
                                                       `member_id` INT NOT NULL AUTO_INCREMENT,
                                                       `login_id` VARCHAR(100) NOT NULL,
    `password` VARCHAR(100) NOT NULL,
    `nickname` VARCHAR(100) NOT NULL,
    `email` VARCHAR(100) NOT NULL,
    `name` VARCHAR(100) NOT NULL,
    `status_message` VARCHAR(100) NULL,
    `join_date` TIMESTAMP NOT NULL,
    `last_login_date` TIMESTAMP NULL,
    `last_modified_date` TIMESTAMP NULL,
    `status` VARCHAR(100) NOT NULL,
    `role` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`member_id`),
    UNIQUE INDEX `member_id_UNIQUE` (`member_id` ASC) VISIBLE,
    UNIQUE INDEX `login_id_UNIQUE` (`login_id` ASC) VISIBLE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Board`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `springtrash`.`Board` (
                                                     `board_id` INT NOT NULL AUTO_INCREMENT,
                                                     `title` VARCHAR(100) NOT NULL,
    `content` text NOT NULL,
    `type` VARCHAR(100) NOT NULL,
    `register_date` TIMESTAMP NOT NULL,
    `last_modified_date` TIMESTAMP NULL,
    `writer_id` INT NOT NULL,
    PRIMARY KEY (`board_id`),
    UNIQUE INDEX `board_id_UNIQUE` (`board_id` ASC) VISIBLE,
    INDEX `fk_board_to_members_writer_id_idx` (`writer_id` ASC) VISIBLE,
    CONSTRAINT `fk_board_to_members_writer_id`
    FOREIGN KEY (`writer_id`)
    REFERENCES `mydb`.`Members` (`member_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
