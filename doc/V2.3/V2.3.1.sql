-- 添加职级编号
ALTER TABLE `user_rank` ADD COLUMN `rank_no` varchar(10)  DEFAULT NULL COMMENT '职级编号' AFTER `rank`;