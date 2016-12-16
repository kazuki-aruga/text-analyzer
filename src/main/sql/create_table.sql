CREATE TABLE `comp` (
  `comp_code` varchar(4) NOT NULL COMMENT '企業コード',
  `comp_name` varchar(256) NOT NULL COMMENT '企業名',
  `jisa` bit(1) NOT NULL COMMENT 'JISAに加入しているかどうか',
  PRIMARY KEY (`comp_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='企業マスター';
CREATE TABLE `vocab` (
  `vocab_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '語彙ID',
  `proto` varchar(256) NOT NULL COMMENT '原形',
  `pos` varchar(100) NOT NULL COMMENT '品詞',
  `pos1` varchar(45) NOT NULL COMMENT '品詞細分類1',
  `pos2` varchar(100) NOT NULL COMMENT '品詞細分類2',
  `pos3` varchar(100) NOT NULL COMMENT '品詞細分類3',
  `conj` varchar(256) NOT NULL COMMENT '活用形',
  `type` varchar(100) NOT NULL COMMENT '活用型',
  `furi` varchar(256) NOT NULL COMMENT '読み',
  `pronun` varchar(256) NOT NULL COMMENT '発音',
  `sections` int(11) NOT NULL DEFAULT '0' COMMENT '出現セクション数',
  `reports` int(11) NOT NULL DEFAULT '0' COMMENT '出現有価証券報告書数',
  `except` bit(1) NOT NULL DEFAULT b'0' COMMENT '除外するかどうか',
  `available` bit(1) DEFAULT b'0',
  PRIMARY KEY (`vocab_id`),
  UNIQUE KEY `idx_vocab` (`proto`,`pos`,`pos1`,`pos2`,`pos3`,`conj`)
) ENGINE=InnoDB AUTO_INCREMENT=19594 DEFAULT CHARSET=utf8 COMMENT='単語辞書マスター';
CREATE TABLE `report` (
  `comp_code` varchar(4) NOT NULL COMMENT '企業コード',
  `year` year(4) NOT NULL COMMENT '年度',
  `sales` int(11) DEFAULT NULL COMMENT '売上高',
  `asset` int(11) DEFAULT NULL COMMENT '資産合計',
  `debt` int(11) DEFAULT NULL COMMENT '負債合計',
  `ebitda` int(11) DEFAULT NULL COMMENT 'EBITDA',
  `rd` int(11) DEFAULT NULL COMMENT '研究開発費',
  `ni` int(11) DEFAULT NULL COMMENT '当期純利益',
  `wc_sec1` int(11) NOT NULL DEFAULT '0' COMMENT '対処すべき課題単語数',
  `wc_sec2` int(11) NOT NULL DEFAULT '0' COMMENT '研究開発活動単語数',
  `wc_total` int(11) NOT NULL DEFAULT '0' COMMENT '単語合計',
  `vc_sec1` int(11) NOT NULL DEFAULT '0' COMMENT '対処すべき課題語彙数',
  `new_vc_sec1` int(11) NOT NULL DEFAULT '0' COMMENT '新出語彙',
  `vc_sec2` int(11) NOT NULL DEFAULT '0' COMMENT '研究開発活動語彙数',
  `vc_total` int(11) NOT NULL DEFAULT '0' COMMENT '語彙合計',
  `active` bit(1) NOT NULL DEFAULT b'1' COMMENT '有効な有報かどうか',
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
  `first_app` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`comp_code`,`year`,`section`,`seq`),
  KEY `fk_vocab_idx` (`vocab_id`),
  CONSTRAINT `fk_report` FOREIGN KEY (`comp_code`, `year`) REFERENCES `report` (`comp_code`, `year`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_vocab` FOREIGN KEY (`vocab_id`) REFERENCES `vocab` (`vocab_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='有価証券報告書単語データ';
CREATE TABLE `report_new_vocab` (
  `comp_code` varchar(4) NOT NULL COMMENT '企業コード',
  `year` year(4) NOT NULL COMMENT '会計年度',
  `vocab_id` int(11) NOT NULL COMMENT '語彙ID',
  `wc` int(11) NOT NULL COMMENT '出現回数',
  PRIMARY KEY (`comp_code`,`year`,`vocab_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='新出単語';
