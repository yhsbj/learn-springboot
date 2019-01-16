CREATE TABLE `ic_sign_credential` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uname` varchar(32) NOT NULL,
  `pwd` varchar(64) NOT NULL,
  `ctime` datetime DEFAULT NULL,
  `last_login` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `pk_ic_sign_creditial` (`uname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;