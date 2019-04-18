-- 修改公告内容对应数据库类型
ALTER TABLE notice MODIFY content text COMMENT '公告内容';

ALTER TABLE order_info ADD modify_user_id bigint(22) NULL COMMENT '修改人';
ALTER TABLE order_info ADD modify_date datetime NULL COMMENT '修改时间';