--  建表语句 
CREATE TABLE `user_profile` (
  `id` bigint(20) NOT NULL,
  `user_name` varchar(50) DEFAULT NULL COMMENT '名称',
  `real_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '真实名称',
  `department_id` bigint(20) DEFAULT NULL COMMENT '部门id',
  `leader_id` bigint(20) DEFAULT NULL COMMENT '直属领导id',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '邮箱',
  `job_name` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '职位名称',
  `profile_photo` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '头像',
  `workplace` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '工作地',
  `rank` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '职级',
  `gender` tinyint(4) DEFAULT NULL COMMENT '性别',
  `college` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '毕业院校',
  `major` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '专业',
  `degree` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '学历',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `entry_time` datetime DEFAULT NULL COMMENT '入职时间',
  `leaving_time` datetime DEFAULT NULL COMMENT '离职时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 数据同步语句

INSERT INTO user_profile 
SELECT 
		u.id,
		u.login,
		u.realname,
		i.department_id,
		i.leader_id,
		u.email,
		i.job_name,
		u.profile_photo,
		i.workplace,
		i.rank,
		i.sex,
		i.college,
		i.major,
		i.degree,
		CASE u.`status` WHEN 'FORBIDDEN' THEN 0 ELSE 1 END,
		i.entry_time,
		i.leaving_time
		FROM jhi_user u,user_info i WHERE u.id=i.user_id
;

-- 删除外键
alter table jhi_user_authority drop foreign key fk_user_id;
-- 更新user_id
UPDATE adjuster a,user_info ui SET a.user_id = ui.user_code WHERE ui.user_id = a.user_id; 

UPDATE adjuster a,user_info ui SET a.adjuster_id = ui.user_code WHERE ui.user_id = a.adjuster_id; 

UPDATE assess_task a,user_info ui SET a.user_id = ui.user_code WHERE ui.user_id = a.user_id; 

UPDATE assess_task a,user_info ui SET a.assessor_id = ui.user_code WHERE ui.user_id = a.assessor_id; 

UPDATE jhi_user_authority a,user_info ui SET a.user_id = ui.user_code WHERE ui.user_id = a.user_id; 


UPDATE user_season a,user_info ui SET a.user_id = ui.user_code WHERE ui.user_id = a.user_id; 

UPDATE user_season_comment a,user_info ui SET a.assessor_id = ui.user_code WHERE ui.user_id = a.assessor_id; 


UPDATE user_season_comment_top a,user_info ui SET a.user_id = ui.user_code WHERE ui.user_id = a.user_id; 


UPDATE user_season_tip a,user_info ui SET a.user_id = ui.user_code WHERE ui.user_id = a.user_id; 

UPDATE user_season_tip a,user_info ui SET a.assessor_id = ui.user_code WHERE ui.user_id = a.assessor_id; 

UPDATE user_profile up ,user_info ui SET up.id = ui.user_code WHERE ui.user_id=up.id;
UPDATE user_profile up ,user_info ui SET up.leader_id = ui.user_code WHERE ui.user_id=up.leader_id;

UPDATE user_report ur ,user_info ui SET ur.user_id = ui.user_code WHERE ui.user_id=ur.user_id;

-- UPDATE jhi_user a,user_info ui SET a.id = ui.user_code WHERE ui.user_id = a.id; 

-- UPDATE user_info ui SET ui.user_id = ui.user_code ;

