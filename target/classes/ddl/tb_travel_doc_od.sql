CREATE TABLE `tb_travel_doc_od` (
  `app_code` varchar(5) NOT NULL,
  `origin` varchar(2) NOT NULL,
  `destination` varchar(2) NOT NULL,
  `travel_doc_version` varchar(3) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `last_update_source` varchar(8) NOT NULL,
  `last_update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`app_code`,`origin`,`destination`,`travel_doc_version`,`start_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
