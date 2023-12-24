CREATE TABLE `restaurant` (
   `id` bigint NOT NULL AUTO_INCREMENT,
   `description` varchar(255) DEFAULT NULL,
   `image` varchar(255) DEFAULT NULL,
   `name` varchar(255) NOT NULL,
   `rating` float NOT NULL,
   PRIMARY KEY (`id`)
 );

CREATE TABLE `table_info` (
   `id` bigint NOT NULL AUTO_INCREMENT,
   `description` varchar(255) DEFAULT NULL,
   `person_capacity` int NOT NULL,
   `table_number` int NOT NULL,
   `restaurant_id` bigint DEFAULT NULL,
   PRIMARY KEY (`id`),
   FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`id`)
 );

CREATE TABLE `rating` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `description` varchar(255) DEFAULT NULL,
    `rating_type` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
 );

 CREATE TABLE `restaurant_rating` (
    `rating_id` bigint NOT NULL,
    `restaurant_id` bigint NOT NULL,
    FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`id`),
    FOREIGN KEY (`rating_id`) REFERENCES `rating` (`id`)
  );

CREATE TABLE `working_time` (
   `id` bigint NOT NULL AUTO_INCREMENT,
   `day_of_week` varchar(255) DEFAULT NULL,
   `description` varchar(255) DEFAULT NULL,
   `end_time` time NOT NULL,
   `start_time` time NOT NULL,
   `restaurant_id` bigint DEFAULT NULL,
   PRIMARY KEY (`id`),
   FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`id`)
 );

CREATE TABLE `non_working_day` (
   `id` bigint NOT NULL AUTO_INCREMENT,
   `non_working_day_date` date NOT NULL,
   `description` varchar(255) DEFAULT NULL,
   `restaurant_id` bigint DEFAULT NULL,
   PRIMARY KEY (`id`),
   FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`id`)
 );

 CREATE TABLE `reservation` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `customer_name` varchar(255) DEFAULT NULL,
    `is_completed` BOOLEAN NOT NULL DEFAULT 1,
    `persons` int NOT NULL,
    `time` datetime(6) DEFAULT NULL,
    `restaurant_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
   FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`id`)
  );

CREATE TABLE `table_info_reservations` (
   `table_info_id` bigint NOT NULL,
   `reservation_id` bigint NOT NULL,
   FOREIGN KEY (`reservation_id`) REFERENCES `reservation` (`id`),
   FOREIGN KEY (`table_info_id`) REFERENCES `table_info` (`id`)
 );

