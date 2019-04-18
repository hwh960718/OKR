SET FOREIGN_KEY_CHECKS=0;

ALTER TABLE `adjuster` ADD COLUMN `user_season_id` bigint(20) NOT NULL COMMENT '用户id' AFTER `id`;

ALTER TABLE `assess_task` ADD COLUMN `user_season_id` bigint(20) NULL DEFAULT NULL COMMENT '用户考核id' AFTER `assessor_id`;

ALTER TABLE `user_season_tip` ADD COLUMN `user_season_id` bigint(20) NOT NULL COMMENT '用户id' AFTER `id`;

-- adjuster user_season_id 同步
UPDATE adjuster a,user_season us SET a.user_season_id = us.id WHERE a.user_id = us.user_id AND a.season_id = us.season_id;

UPDATE assess_task a,user_season us SET a.user_season_id = us.id WHERE a.user_id = us.user_id AND a.season_id = us.season_id;

UPDATE user_season_tip a,user_season us SET a.user_season_id = us.id WHERE a.user_id = us.user_id AND a.season_id = us.season_id;

-- 标签库
CREATE TABLE `tip`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标签内容',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ;
-- 将存在的标签去重后插入标签库
INSERT INTO tip(title,user_id) SELECT DISTINCT title,user_id FROM user_season_tip ;
--
ALTER TABLE `user_season_tip` ADD COLUMN `tip_id` bigint(20) NULL DEFAULT NULL COMMENT '标签id' AFTER `assessor_id`;
-- 标签库索引更新
UPDATE user_season_tip ust ,tip t SET ust.tip_id = t.id WHERE ust.title = t.title;


ALTER TABLE `adjuster` DROP COLUMN `user_id`;

ALTER TABLE `adjuster` DROP COLUMN `season_id`;

ALTER TABLE `adjuster` DROP COLUMN `enabled`;

ALTER TABLE `adjuster` DROP COLUMN `created_date`;

ALTER TABLE `adjuster` DROP COLUMN `last_modified_date`;


UPDATE assess_task SET `status` = 1 WHERE `status`= 'UNDERWAY';
UPDATE assess_task SET `status` = 2 WHERE `status`= 'FINISHED';
UPDATE assess_task SET `status` = 3 WHERE `status`= 'NOT_ASSESS';
UPDATE assess_task SET `status` = 4 WHERE `status`= 'INVALID_ASSESS';
ALTER TABLE `assess_task` MODIFY COLUMN `status` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '评价状态[进行中：UNDERWAY,1;考核完成：FINISHED,2;未评价:NOT_ASSESS,3;无效评价:INVALID_ASSESS,4;]' AFTER `score`;





ALTER TABLE `assess_task` DROP COLUMN `user_id`;

ALTER TABLE `assess_task` DROP COLUMN `season_id`;

ALTER TABLE `assess_task` DROP COLUMN `created_date`;

ALTER TABLE `assess_task` DROP COLUMN `last_modified_date`;

ALTER TABLE `department` DROP COLUMN `created_date`;

ALTER TABLE `notice` ADD COLUMN `status` tinyint(4) NULL DEFAULT NULL COMMENT '状态(有效:1;无效:2;删除:3)' AFTER `content`;

ALTER TABLE `notice` ADD COLUMN `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人' AFTER `status`;

ALTER TABLE `notice` ADD COLUMN `modify_user_id` bigint(20) NULL DEFAULT NULL COMMENT '修改人' AFTER `create_user_id`;

ALTER TABLE `notice` ADD COLUMN `valid_date` timestamp(0) NULL DEFAULT NULL COMMENT '有效时间' AFTER `modify_user_id`;


UPDATE season SET `status` = 1 WHERE `status` = 'NEW';
UPDATE season SET `status` = 2 WHERE `status` = 'PUBLISHED';
ALTER TABLE `season` MODIFY COLUMN `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '状态(新建:NEW,1;已发布:PUBLISHED,2;结束:3)' AFTER `is_make_season_score`;


ALTER TABLE `user_profile` ADD COLUMN `last_modified_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间' AFTER `leaving_time`;
UPDATE user_profile SET gender = 2 WHERE gender = 0;
UPDATE user_profile SET gender = 3 WHERE gender IS NULL;
ALTER TABLE `user_profile` MODIFY COLUMN `gender` tinyint(4) NULL DEFAULT 3 COMMENT '性别(男:1;女:2;未知:3;)' AFTER `rank`;


UPDATE user_profile SET `status` = 4 WHERE `status` =0;

ALTER TABLE `user_profile` MODIFY COLUMN `status` tinyint(4) NOT NULL DEFAULT 2 COMMENT '状态:(正常：NORMAL,1;未激活：NOT_ACTIVE,2;禁用：FORBIDDEN,3;失效：INVALID,4;)' AFTER `degree`;


UPDATE user_season SET assess_status =1 WHERE assess_status = 'UNDERWAY';
UPDATE user_season SET assess_status =2 WHERE assess_status = 'FAIL';
UPDATE user_season SET assess_status =3 WHERE assess_status = 'SUCCESS';
ALTER TABLE `user_season` MODIFY COLUMN `assess_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '1' COMMENT '考核状态（默认1）[进行中：UNDERWAY,1;考核失败：FAIL,2;考核完成：SUCCESS,3;]' AFTER `season_id`;


-- UPDATE user_season SET assess_result = 1 WHERE assess_result = 'VALID';
-- UPDATE user_season SET assess_result = 2 WHERE assess_result = 'INVALID';
ALTER TABLE `user_season` MODIFY COLUMN `assess_result` tinyint(4) NULL DEFAULT NULL COMMENT '考核结果：有效:VALID,1无效:INVALID,2' AFTER `assess_status`;



ALTER TABLE `user_season` MODIFY COLUMN `created_date` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间' AFTER `assess_result`;


UPDATE score_option SET type =1 WHERE type = 'ACTUALIZE';
UPDATE score_option SET type =2 WHERE type = 'ATTITUDE';

ALTER TABLE `score_option` MODIFY COLUMN `type` tinyint(4) NOT NULL COMMENT '类型(1:ACTUALIZE,价值与能力;2:ATTITUDE,态度与为人)' AFTER `weight`;


ALTER TABLE `user_season` DROP COLUMN `okr_title`;

ALTER TABLE `user_season` DROP COLUMN `okr_content`;

ALTER TABLE `user_season` DROP COLUMN `adjuster_ids`;

ALTER TABLE `user_season` DROP COLUMN `score`;

ALTER TABLE `user_season` DROP COLUMN `ranking`;

ALTER TABLE `user_season` DROP COLUMN `assess_level`;

ALTER TABLE `user_season` DROP COLUMN `is_filled_okr`;

ALTER TABLE `user_season` DROP COLUMN `is_selected_assessor`;

ALTER TABLE `user_season` DROP COLUMN `is_selected_users`;

ALTER TABLE `user_season` DROP COLUMN `is_assess_finished`;

ALTER TABLE `user_season` DROP COLUMN `last_modified_date`;

ALTER TABLE `user_season_comment` DROP COLUMN `last_modified_date`;

ALTER TABLE `user_season_comment_top` DROP COLUMN `last_modified_date`;



ALTER TABLE `user_season_tip` DROP COLUMN `user_id`;

ALTER TABLE `user_season_tip` DROP COLUMN `season_id`;

ALTER TABLE `user_season_tip` DROP COLUMN `title`;

ALTER TABLE `user_season_tip` DROP COLUMN `last_modified_date`;

SET FOREIGN_KEY_CHECKS=1;