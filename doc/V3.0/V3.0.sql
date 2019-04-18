
-- 添加字段
ALTER TABLE `user_report` ADD COLUMN `adjuster_count` int(11) NOT NULL DEFAULT 0 COMMENT '选择评价人数量' AFTER `assess_count`;
-- 初始化用户选择评价人
UPDATE (SELECT adjuster_id,COUNT(1) num FROM adjuster GROUP BY adjuster_id
) t1, user_report ur set ur.adjuster_count = t1.num WHERE ur.user_id = t1.adjuster_id;

-- 初始化评价人数量
UPDATE (SELECT assessor_id,COUNT(1) num FROM assess_task where status=2  GROUP BY assessor_id
) t1, user_report ur set ur.assess_count = t1.num WHERE ur.user_id = t1.assessor_id;

-- 惩罚积分总数
ALTER TABLE `score_user` ADD COLUMN `penalty_score_total` BIGINT(20) NULL DEFAULT 0 COMMENT '惩罚积分总数' AFTER `abatement_score_total`;
-- 将积分惩罚设置
UPDATE score_user set penalty_score_total = abatement_score_total;
-- 消减积分数设置为0
UPDATE score_user set abatement_score_total = 0;

-- 评价任务添加用户id
ALTER TABLE `user_season_comment` ADD COLUMN `user_id` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '用户id' AFTER `user_season_id`;

-- 用户评论初始化用户id
UPDATE user_season_comment usc ,user_season us
set usc.user_id = us.user_id
WHERE usc.user_season_id = us.id;

-- 移除用户评价id
ALTER TABLE `user_season_comment` DROP COLUMN `user_season_id` ;

-- 举报内容
ALTER TABLE `user_report_detail` ADD COLUMN `content` VARCHAR(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '举报内容' AFTER `assess_task_id`;

-- 用户标签
ALTER TABLE `user_season_tip` ADD COLUMN `user_id` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '用户id' AFTER `user_season_id`;
-- 初始化用户标签数据
UPDATE user_season_tip ust ,user_season us
set ust.user_id = us.user_id
WHERE ust.user_season_id = us.id;
-- 删除用户标签user_season_id
ALTER TABLE `user_season_tip` DROP COLUMN `user_season_id` ;
-- 用户考核详情字段长度放开
ALTER TABLE `okr_content` CHANGE COLUMN `okr_content` `okr_content` TEXT CHARACTER SET utf8 COLLATE utf8_bin NULL  COMMENT 'okr目标内容' AFTER `okr_title`;
-- 评论内容
ALTER TABLE `user_season_comment` CHANGE COLUMN `content` `content` TEXT CHARACTER SET utf8 COLLATE utf8_bin NOT NULL  COMMENT '评论内容' AFTER `assessor_id`;

