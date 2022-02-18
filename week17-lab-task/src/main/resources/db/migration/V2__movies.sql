CREATE TABLE IF NOT EXISTS `movies` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255),
  `release_date` date,
  PRIMARY KEY (`id`)
)