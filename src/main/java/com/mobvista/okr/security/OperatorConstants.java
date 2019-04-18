package com.mobvista.okr.security;


/**
 * 操作url
 *
 * @author guwei
 */
public final class OperatorConstants {

    /**
     * 操作权限是否可以查看季度考核排名
     */
    public static final String CAN_SEE_SEASON_DETAIL_RANK_AND_SCORE = "oper_can_see_season_detail_rank_and_score";
    /**
     * 操作权限是否可以获取季度评分
     */
    public static final String CAN_SEE_USER_SEASON_DETAIL_FOR_USER_SCORE_ITEM = "oper_can_see_user_season_detail_for_user_score_item";
    /**
     * 操作权限是否可以查看评价列表用户信息
     */
    public static final String CAN_SEE_USER_DETAIL_FOR_ASSESS_SCORE_LIST = "oper_can_see_user_detail_for_assess_score_list";
    /**
     * 操作权限是否可以获取季度排行榜
     */
    public static final String CAN_SEE_RANKING_LIST = "oper_can_see_ranking_list";
    /**
     * 是否可以修改用户管理数据
     */
    public static final String CAN_MODIFY_USER_MANAGE_USER_DATA = "oper_can_modify_user_manage_user_data";

    /**
     * 是否可以查看所有的订单
     */
    public static final String CAN_SEE_ALL_ORDER_LIST = "oper_can_see_all_order_list";


    private OperatorConstants() {
    }
}
