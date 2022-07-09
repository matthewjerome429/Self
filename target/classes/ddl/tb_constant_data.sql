CREATE TABLE `tb_constant_data` (
  `app` varchar(5) CHARACTER SET utf8 NOT NULL,
  `type` varchar(10) CHARACTER SET utf8 NOT NULL,
  `value` varchar(500) CHARACTER SET utf8 NOT NULL,
  `last_update_source` varchar(8) CHARACTER SET utf8 NOT NULL,
  `last_update_timestamp` datetime NOT NULL,
  PRIMARY KEY (`app`,`type`,`value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
