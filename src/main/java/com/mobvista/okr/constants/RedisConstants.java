package com.mobvista.okr.constants;

/**
 * redis常量
 *
 * @author Weier Gu (顾炜)
 */
public final class RedisConstants {

    /**
     * T1
     */
    public final static String T1 = "t1";
    /**
     * T2
     */
    public final static String T2 = "t2";
    /**
     * T3
     */
    public final static String T3 = "t3";

    /**
     * redis 锁定过期时间 10s
     */
    public static final int REDIS_LOCK_EXPIRED_TIME = 10;
    /**
     * redis 锁定过期时间 5s
     */
    public static final int REDIS_LOCK_EXPIRED_TIME_5S = 5;


    /**
     * 评价人选择的最大数据
     */
    public static final int ADJUSTER_SELECTED_MAX = 30;

    /**
     * 过期时间（秒）过期一个小时
     */
    public final static long EXPIRE_TIME_1H = 3600;
    /**
     * 过期时间（秒）过期24小时
     */
    public final static long EXPIRE_TIME_24H = 86400;

    /**
     * 需要使用okr的部门名称
     */
    public final static String USE_OKR_DEPARTMENT_IDS = "use:okr:department:ids";


    /**
     * 考核所有年份对应的key
     */
    public final static String SEASON_ALL_YEARS = "season:okr:all:years";
    /**
     * 获取所有评分维度
     */
    public final static String SCORE_OPTION_ALL = "okr:score:option:all";

    /**
     * 所有评分维度map
     */
    public final static String SCORE_OPTION_ALL_NAME_MAP = "okr:score:option:all:name:map";

    /**
     * redis 锁常量
     */
    public static final String LOCK = "LOCK";

    /**
     * 有效公告列表key
     */
    public final static String NOTICE_VALID_LIST_KEY = "notice:valid:list:key";

    /**
     * 需要评论用户数对应redis key
     */
    public final static String ADJUSTER_COUNT_KEY = "adjuster:count:key";


    /**
     * T1等级需要评论用户数对应redis key
     */
    public final static String T1_ADJUSTER_COUNT_KEY = T1 + CommonConstants.APPEND_COLON + ADJUSTER_COUNT_KEY;
    /**
     * T2等级需要评论用户数对应redis key
     */
    public final static String T2_ADJUSTER_COUNT_KEY = T2 + CommonConstants.APPEND_COLON + ADJUSTER_COUNT_KEY;
    /**
     * T3等级需要评论用户数对应redis key
     */
    public final static String T3_ADJUSTER_COUNT_KEY = T3 + CommonConstants.APPEND_COLON + ADJUSTER_COUNT_KEY;
    /**
     * 考评完成度百分比对应redis key
     */
    public final static String FINISH_ASSESS_MIN_SCALE_KEY = "finish:assess:min:scale:key";

    /**
     * 生成积分加锁key
     */
    public final static String MAKE_SCORE_LOCK_KEY = "make_score_lock";


    /**
     * 评论分组一级部门id集合
     */
    public final static String ASSESS_GROUP_ONE_LEVEL_DEPARTMENT_IDS = "assess:group:one:level:department:ids";
    /**
     * 评论分组 二级部门下用户id
     */
    public final static String ASSESS_GROUP_SECOND_LEVEL_DEPARTMENT_USER_IDS = "assess:group:second:level:department:user:ids";


    /**
     * 职级对应其他部门需要人员数
     * T1:rank:other:department:need:count
     * T2:rank:other:department:need:count
     * T3:rank:other:department:need:count
     */
    public final static String RANK_OTHER_DEPARTMENT_NEED_COUNT = "rank:other:department:need:count";

    /**
     * 用户职级权重key
     */
    public final static String USER_RANK_WEIGHT_KEY = "user:rank:weight:key";


    /**
     * 订单号的key
     */
    public static final String ORDER_NO_KEY = "order:no:key:";
    /**
     * 上架项目key
     */
    public static final String PRODUCT_SHELF_KEY = "product:shelf:key:";
    /**
     * 上架项目当前商品价值
     */
    public static final String PRODUCT_SHELF_ORDER_CURRENT_PRICE_KEY = "product:shelf:order:current:price_key:";
    /**
     * redis 锁定key 商品订单
     */
    public static final String REDIS_LOCK_KEY_PRODUCT_ORDER = "redis:lock:key:product:order:";

    /**
     * okr所有使用用户的邮箱
     */
    public static final String OKR_ALL_USED_USER_EMAIL_KEY = "okr:all:used:user:email:key";


}
