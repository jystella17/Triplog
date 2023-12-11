create database traveldb;
use traveldb;

DROP TABLE IF EXISTS `comment`;
DROP TABLE IF EXISTS `RouteImage`;
DROP TABLE IF EXISTS `ReviewImage`;
DROP TABLE IF EXISTS `review`;
DROP TABLE IF EXISTS `route_place`;
DROP TABLE IF EXISTS `route`;
DROP TABLE IF EXISTS `board`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `spot_info`;
DROP TABLE IF EXISTS `cities`;
DROP TABLE IF EXISTS `keyword_list`;
DROP TABLE IF EXISTS `Image`;
DROP TABLE IF EXISTS `s3image`;

CREATE TABLE `User` (
	`uid` bigint AUTO_INCREMENT	NOT NULL primary key,
    `unique_id` varchar(128) NOT NULL,
    `pw` varchar(128),
	`email`	varchar(128) NOT NULL,
	`nickname` varchar(30) NOT NULL,
	`role_type`	enum('USER', 'ADMIN') NOT NULL,
	`provider_type` enum('NO', 'KAKAO', 'NAVER', 'GOOGLE') NOT NULL,
	`created_at` datetime,
	`modified_at` datetime,
	`travel_count` int NOT NULL default 0,
	`level`	int NOT NULL default 1
);

CREATE TABLE `Spot_Info` (
	`spot_id` bigint AUTO_INCREMENT NOT NULL primary key,
	`spot_name`	varchar(30) NOT NULL,
	`address` varchar(50),
	`latitude` float,
	`longitude` float
);

CREATE TABLE `Keyword_List` (
	`key_id` bigint	AUTO_INCREMENT NOT NULL primary key,
	`keyword` varchar(10) NOT NULL
);

CREATE TABLE `Cities` (
	`city_id` bigint AUTO_INCREMENT	NOT NULL primary key,
	`city_name`	varchar(20) NOT NULL
);

CREATE TABLE `Route` (
    `route_id` bigint AUTO_INCREMENT NOT NULL primary key,
    `uid` bigint NOT NULL,
    `city_id` bigint NOT NULL,
    `key_id` bigint NOT NULL,
    `route_name` varchar(50) NOT NULL,
    `route_day`	date NOT NULL
);

CREATE TABLE `Route_Place` (
    `place_id` bigint AUTO_INCREMENT NOT NULL primary key,
    `spot_id` bigint NOT NULL,
    `route_id` bigint NOT NULL,
    `rp_name` varchar(30) NOT NULL,
    `priority` int	NOT NULL,
    `visit_time` datetime
);

CREATE TABLE `Review` (
	`review_id` bigint AUTO_INCREMENT NOT NULL primary key,
	`place_id` bigint NOT NULL,
	`review_text` VARCHAR(255)
);

CREATE TABLE `Comment` (
	`comment_id` bigint AUTO_INCREMENT NOT NULL primary key,
    `review_id` bigint not null,
	`comment_text` text
);


CREATE TABLE `board` (
    `article_no` int NOT NULL AUTO_INCREMENT,
    `uid` BIGINT DEFAULT NULL,
    `subject` varchar(100) DEFAULT NULL,
    `content` varchar(2000) DEFAULT NULL,
    `hit` int DEFAULT '0',
    `register_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`article_no`),
    KEY `board_to_members_uid_fk` (`uid`),
    CONSTRAINT `board_to_user_uid_fk` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Image` (
    `img_id` bigint AUTO_INCREMENT NOT NULL primary key,
    `img_name` VARCHAR(128),
    `img_key` VARCHAR(128),
    `img_url` VARCHAR(255)
);

CREATE TABLE `RouteImage` (
    `rt_id` BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `route_id` BIGINT NOT NULL,
    `img_id` BIGINT NOT NULL,
    `img_url` VARCHAR(255)
);

CREATE TABLE `ReviewImage`(
	`rv_id` bigint AUTO_INCREMENT NOT NULL primary key,
    `review_id` bigint not null unique,
    `img_id` bigint not null,
    `img_url` VARCHAR(255)
);

CREATE TABLE `S3Image` (
    `s3_id` bigint AUTO_INCREMENT NOT NULL primary key,
    `s3_url` VARCHAR(255)
);
ALTER TABLE spot_info ADD CONSTRAINT uq_spot_name UNIQUE (spot_name);
ALTER TABLE `Route_Place` ADD CONSTRAINT `FK_Spot_Info_TO_Route_Place_1` FOREIGN KEY (
	`spot_id`
)
REFERENCES `Spot_Info` (
	`spot_id`
)ON DELETE CASCADE;


ALTER TABLE `Route_Place` ADD CONSTRAINT `FK_Route_TO_Route_Place_1` FOREIGN KEY (
	`route_id`
)
REFERENCES `Route` (
	`route_id`
)ON DELETE CASCADE;

ALTER TABLE `Route` ADD CONSTRAINT `FK_User_TO_Route_1` FOREIGN KEY (
	`uid`
)
REFERENCES `User` (
	`uid`
)ON DELETE CASCADE;

ALTER TABLE `Route` ADD CONSTRAINT `FK_Cities_TO_Route_1` FOREIGN KEY (
	`city_id`
)
REFERENCES `Cities` (
	`city_id`
)ON DELETE CASCADE;

ALTER TABLE `Review` ADD CONSTRAINT `FK_Route_Place_TO_Review_1` FOREIGN KEY (
	`place_id`
)
REFERENCES `Route_Place` (
	`place_id`
)ON DELETE CASCADE;

ALTER TABLE `Route` ADD CONSTRAINT `FK_Keyword_List_TO_Route_1` FOREIGN KEY (
	`key_id`
)
REFERENCES `Keyword_List` (
	`key_id`
)ON DELETE CASCADE;

ALTER TABLE `ReviewImage` ADD CONSTRAINT `FK_Image_TO_ReviewImage_1` FOREIGN KEY (
    `img_id`
)
REFERENCES `Image` (
    `img_id`
)ON DELETE CASCADE;
ALTER TABLE `ReviewImage` ADD CONSTRAINT `FK_Review_TO_ReviewImage_2` FOREIGN KEY (
    `review_id`
)
REFERENCES `Review` (
    `review_id`
)ON DELETE CASCADE;
ALTER TABLE `RouteImage` ADD CONSTRAINT `FK_Image_TO_RouteImage_1` FOREIGN KEY (
    `img_id`
)
REFERENCES `Image` (
    `img_id`
)ON DELETE CASCADE;
ALTER TABLE `RouteImage` ADD CONSTRAINT `FK_Image_TO_RouteImage_2` FOREIGN KEY (
    `route_id`
)
REFERENCES `Route` (
    `route_id`
)ON DELETE CASCADE;
