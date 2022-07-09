CREATE TABLE `tb_ssr_type` (
  `app_code` varchar(10) NOT NULL,
  `type` varchar(10) NOT NULL COMMENT 'SSR type, ie. "TD" means Travel Documents',
  `value` varchar(10) NOT NULL COMMENT 'field value of <type/>, ie. "DOCS"',
  `action` varchar(10) NOT NULL COMMENT 'ie. "PTD" means this record is primary Travel Docs',
  `last_update_source` varchar(8) DEFAULT NULL,
  `last_update_datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`app_code`,`type`,`action`,`value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
