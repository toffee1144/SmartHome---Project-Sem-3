create database IoTAndroid;
use IoTAndroid;

CREATE TABLE `lampMenu`(
	`id` int(11) NOT NULL AUTO_INCREMENT,
    `r` int,
    `g`int,
    `b` int,
    `intensity` int,
    PRIMARY KEY (`id`)
);

CREATE TABLE `water`(
	`id` int(11) NOT NULL AUTO_INCREMENT,
    `water_Value` int,
    PRIMARY KEY (`id`)
);

CREATE TABLE `temp`(
	`id` int(11) NOT NULL AUTO_INCREMENT,
    `temp_C` int,
    PRIMARY KEY (`id`)
);

CREATE TABLE `door`(
	`id` int(11) NOT NULL auto_increment,
    `door_val` int,
    PRIMARY KEY (`id`)
);

drop table Water;
SELECt * FROM water;
SELECT * FROM water ORDER BY id DESC LIMIT 1;