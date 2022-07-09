CREATE TABLE `tb_booking_status` (
  `app_code` varchar(5) COLLATE utf8_unicode_ci NOT NULL,
  `status_code` varchar(5) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `action` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_update_source` varchar(8) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_update_timestamp` datetime DEFAULT NULL,
  PRIMARY KEY (`app_code`,`status_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
