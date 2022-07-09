CREATE TABLE `tb_cabin_class` (
  `app_code` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `basic_class` varchar(10) CHARACTER SET utf8 NOT NULL,
  `subclass` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_update_source` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_update_timestamp` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`app_code`,`basic_class`,`subclass`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
