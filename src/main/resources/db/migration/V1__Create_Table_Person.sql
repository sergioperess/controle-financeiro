
DROP TABLE IF EXISTS `person`;

CREATE TABLE `person` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `first_name` varchar(80) NOT NULL,
  `last_name` varchar(80) NOT NULL,
  `user_name` varchar(80) NOT NULL,  
  `password` varchar(100) NOT NULL,
  `saldo` float NOT NULL, 
  PRIMARY KEY (`id`)
);

