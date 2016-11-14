CREATE TABLE `comp` (
  `comp_code` varchar(4) NOT NULL COMMENT '企業コード',
  `comp_name` varchar(256) NOT NULL COMMENT '企業名',
  PRIMARY KEY (`comp_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='企業マスター';
CREATE TABLE `vocab` (
  `vocab_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '語彙ID',
  `proto` varchar(256) NOT NULL COMMENT '原形',
  `pos` varchar(100) NOT NULL COMMENT '品詞',
  `pos1` varchar(45) NOT NULL COMMENT '品詞細分類1',
  `pos2` varchar(100) NOT NULL COMMENT '品詞細分類2',
  `pos3` varchar(100) NOT NULL COMMENT '品詞細分類3',
  `conj` varchar(256) DEFAULT NULL COMMENT '活用形',
  `type` varchar(100) DEFAULT NULL COMMENT '活用型',
  `furi` varchar(256) DEFAULT NULL COMMENT '読み',
  `pronun` varchar(256) DEFAULT NULL COMMENT '発音',
  PRIMARY KEY (`vocab_id`),
  UNIQUE KEY `idx_vocab` (`proto`,`pos`,`pos1`,`pos2`,`pos3`,`conj`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COMMENT='単語辞書マスター';
CREATE TABLE `report` (
  `comp_code` varchar(4) NOT NULL COMMENT '企業コード',
  `year` year(4) NOT NULL COMMENT '年度',
  PRIMARY KEY (`comp_code`,`year`),
  CONSTRAINT `fk_comp` FOREIGN KEY (`comp_code`) REFERENCES `comp` (`comp_code`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='有価証券報告書データ';
CREATE TABLE `report_word` (
  `comp_code` varchar(4) NOT NULL COMMENT '企業コード',
  `year` year(4) NOT NULL COMMENT '年度',
  `section` int(1) NOT NULL COMMENT '区分(1:対処すべき課題, 2:研究開発活動)',
  `seq` int(11) NOT NULL COMMENT '順序',
  `surface` varchar(256) NOT NULL COMMENT '表層形',
  `vocab_id` int(11) NOT NULL COMMENT '語彙ID',
  PRIMARY KEY (`comp_code`,`year`,`section`,`seq`),
  KEY `fk_vocab_idx` (`vocab_id`),
  CONSTRAINT `fk_report` FOREIGN KEY (`comp_code`, `year`) REFERENCES `report` (`comp_code`, `year`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_vocab` FOREIGN KEY (`vocab_id`) REFERENCES `vocab` (`vocab_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='有価証券報告書単語データ';
