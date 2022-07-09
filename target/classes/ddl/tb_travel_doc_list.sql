CREATE TABLE `tb_travel_doc_list` (
  `travel_doc_version` int(3) NOT NULL,
  `travel_doc_code` varchar(2) NOT NULL,
  `travel_doc_type` varchar(2) NOT NULL,
  `last_update_source` varchar(8) NOT NULL,
  `last_update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`travel_doc_version`,`travel_doc_code`,`travel_doc_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
