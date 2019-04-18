
-- 创建商品表
CREATE TABLE `product` (
  `id` bigint(22) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL COMMENT '商品名称',
  `desc` text COMMENT '描述',
  `inventory_count` int(10) NOT NULL DEFAULT '0' COMMENT '库存总数',
  `valid_count` int(10) NOT NULL DEFAULT '0' COMMENT '商品可用库存',
  `used_count` int(10) NOT NULL DEFAULT '0' COMMENT '使用统计',
  `locked_count` int(10) NOT NULL DEFAULT '0' COMMENT '锁定库存',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态 ：1、上架 2、下架',
  `is_group` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否组合商品 1：是 2：否',
  `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '商品金额',
  `picture_path` text COMMENT '图片上传地址 ;分隔',
  `create_user_id` bigint(18) DEFAULT '0',
  `create_date` datetime DEFAULT NULL,
  `modify_user_id` bigint(18) DEFAULT '0',
  `modify_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `order_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(128) CHARACTER SET latin1 DEFAULT NULL COMMENT '订单号',
  `product_id` bigint(22) DEFAULT '0' COMMENT '订单id',
  `shelf_id` bigint(22) DEFAULT '0' COMMENT '上架id',
  `shelf_type` tinyint(4) DEFAULT '0' COMMENT '上架类型 1、兑换 3、竞拍',
  `status` tinyint(4) DEFAULT '0' COMMENT '订单状态 1、有效 2、无效',
  `price` decimal(10,2) DEFAULT '0.00' COMMENT '价值',
  `order_num` int(8) DEFAULT '0' COMMENT '订单数量',
  `create_user_id` bigint(22) DEFAULT '0' COMMENT '创建人id',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



CREATE TABLE `product_group`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_product_id` bigint(22) NULL DEFAULT 0 COMMENT '组合商品id',
  `product_id` bigint(22) NULL DEFAULT 0 COMMENT '商品id',
  `group_count` int(8) NULL DEFAULT 0 COMMENT '商品组合数量',
  PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB DEFAULT CHARSET =utf8mb4;


CREATE TABLE `okr`.`product_shelf`  (
  `id` bigint(22) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(22) NULL DEFAULT 0 COMMENT '商品id',
  `type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '上架类型：1、兑换 2、竞拍',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 ：1、上架 2、下架',
  `price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '价值',
  `score_increment` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '积分增加幅度',
  `units` tinyint(4) NOT NULL DEFAULT 1 COMMENT '计量单位：1、积分',
  `shelf_count` int(8) NULL DEFAULT 0 COMMENT '上架数量',
  `shelf_use_count` int(8) NOT NULL DEFAULT 0 COMMENT '上架使用数量',
  `valid_date_start` datetime(0) NULL DEFAULT NULL COMMENT '上架开始时间',
  `valid_date_end` datetime(0) NULL DEFAULT NULL COMMENT '下架结束时间',
  `create_user_id` bigint(18) NULL DEFAULT 0,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `modify_user_id` bigint(18) NULL DEFAULT 0,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  DEFAULT CHARSET =utf8mb4 COMMENT = '商品上架' ;


-- 锁定几分熟
ALTER TABLE `score_user` ADD COLUMN `locked_score_total` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '锁定积分数' AFTER `penalty_score_total`;

ALTER TABLE `score_user_detail` MODIFY COLUMN `related_desc` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '渠道描述' AFTER `related_flag`;