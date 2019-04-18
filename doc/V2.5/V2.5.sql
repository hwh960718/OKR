CREATE TABLE `score_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '规则ID',
  `rule_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `rule_desc` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '规则描述',
  `type` tinyint(4) DEFAULT NULL COMMENT '类型（1：获取积分；2：扣减积分）',
  `score` int(11) NOT NULL DEFAULT '0' COMMENT '积分数',
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '状态(1：有效；2：无效)',
  `valid_date_start` datetime DEFAULT NULL COMMENT '有效时间',
  `trigger_mode` tinyint(4) DEFAULT NULL COMMENT '渠道',
  `create_user_id` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `modify_user_id` bigint(20) DEFAULT NULL,
  `modify_date` datetime DEFAULT NULL,
  `valid_date_end` datetime DEFAULT NULL COMMENT '有效结束时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `score_rule_id_uindex` (`id`)
) DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='积分规则表';


CREATE TABLE `score_user` (
  `id` bigint(20) NOT NULL COMMENT '用户id',
  `valid_score_total` bigint(20) NOT NULL DEFAULT '0' COMMENT '有效积分总数',
  `grant_score_total` bigint(20) DEFAULT '0' COMMENT '发放积分总数',
  `abatement_score_total` bigint(20) DEFAULT '0' COMMENT '使用积分总数',
  `modify_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_score_id_uindex` (`id`)
) DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户积分';


CREATE TABLE `score_user_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `type` tinyint(4) DEFAULT NULL COMMENT '类型（1：获取积分；2：扣减积分）',
  `score` bigint(20) DEFAULT '0' COMMENT '积分',
  `rule_id` bigint(20) DEFAULT NULL COMMENT '规则id',
  `related_id` bigint(20) NOT NULL COMMENT '关联Id',
  `related_flag` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '关联标识',
  `related_desc` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '渠道描述',
  `create_user_id` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_score_detail_id_uindex` (`id`),
  UNIQUE KEY `score_user_detail_type_related_id_related_flag_uindex` (`type`,`related_id`,`related_flag`)
) DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户积分明细表';