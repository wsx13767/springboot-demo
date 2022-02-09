-- 箱子資訊
CREATE TABLE `box_info` (
  `carton` varchar(20) NOT NULL COMMENT '箱子編號',
  `qty` bigint NOT NULL COMMENT '最大數量',
  `len` int NOT NULL COMMENT '長',
  `width` int NOT NULL COMMENT '寬',
  `height` int NOT NULL COMMENT '高',
  `unit` varchar(5) NOT NULL COMMENT '單位',
  PRIMARY KEY (`carton`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- 排程
CREATE TABLE `batch_schedule` (
  `task_id` varchar(30) NOT NULL,
  `name` varchar(45) NOT NULL,
  `description` varchar(255) NOT NULL,
  `cron_expression` varchar(255) NOT NULL,
  `status` tinyint NOT NULL,
  `class_name` varchar(255) NOT NULL,
  `system` varchar(45) NOT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- 客戶可使用哪些箱子
CREATE TABLE `box_of_business` (
  `business_id` varchar(50) NOT NULL COMMENT '客戶編號',
  `box_carton` varchar(20) NOT NULL COMMENT '箱子編號',
  PRIMARY KEY (`business_id`,`box_carton`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- excel讀取後裝箱
CREATE TABLE `box_of_excel` (
  `excel_id` varchar(36) NOT NULL,
  `box_series` int NOT NULL,
  `box_carton` varchar(45) NOT NULL,
  PRIMARY KEY (`excel_id`,`box_series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- excel讀取
CREATE TABLE `excel_file` (
  `id` varchar(36) NOT NULL DEFAULT (uuid()),
  `source` varchar(45) DEFAULT NULL,
  `file_path` varchar(255) NOT NULL,
  `file_name` varchar(45) NOT NULL,
  `done` tinyint DEFAULT '0',
  `error` tinyint DEFAULT '0',
  `msg` varchar(3000) DEFAULT NULL,
  `size` bigint DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- 商品資訊
CREATE TABLE `item_info` (
  `sku_prefix` varchar(45) NOT NULL COMMENT 'sku前綴',
  `sku_postfix` varchar(45) NOT NULL COMMENT 'sku後綴',
  `category` varchar(45) NOT NULL COMMENT '分類',
  `volume` decimal(10,4) NOT NULL COMMENT '材積',
  `weight` decimal(10,5) NOT NULL COMMENT '重量',
  `sequence` int NOT NULL COMMENT '順序',
  PRIMARY KEY (`sku_prefix`,`sku_postfix`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- 已分箱的商品
CREATE TABLE `items_of_box` (
  `sku` varchar(30) NOT NULL,
  `excel_id` varchar(36) NOT NULL,
  `box_series` int NOT NULL,
  `position` varchar(20) DEFAULT NULL,
  `fn_sku` varchar(45) DEFAULT NULL,
  `ean` varchar(45) DEFAULT NULL,
  `num` int DEFAULT NULL,
  `volume` decimal(10,4) DEFAULT NULL,
  `weight` decimal(10,6) DEFAULT NULL,
  `title` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`sku`,`excel_id`,`box_series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- 客戶excel客製化必要欄位mapping
CREATE TABLE `packing_config` (
  `id` varchar(20) NOT NULL,
  `sku` varchar(45) NOT NULL,
  `fnsku` varchar(45) NOT NULL,
  `title` varchar(45) NOT NULL,
  `num` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
