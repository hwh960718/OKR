package com.mobvista.okr.dto.mbacs;

import com.mobvista.okr.enums.YesOrNoEnum;
import com.mobvista.okr.security.OperatorConstants;

import java.util.List;

/**
 * 操作权限
 *
 * @author 顾炜(GUWEI)
 * @date 2018/6/5 11:48
 */
public class OperatorPerm {

    /**
     * 季度考核是否可以查看排名
     */
    private Integer canSeeSeasonDetailRankAndScore = YesOrNoEnum.NO.getCode();
    /**
     * 获取季度评分是否可看
     */
    private Integer canSeeUserSeasonDetailForUserScoreItem = YesOrNoEnum.NO.getCode();
    /**
     * 是否可看评价列表用户信息
     */
    private Integer canSeeUserDetailForAssessScoreList = YesOrNoEnum.NO.getCode();
    /**
     * 获取季度排行榜
     */
    private Integer canSeeRankingList = YesOrNoEnum.NO.getCode();
    /**
     * 是否可以修改用户管理信息
     */
    private Integer canModifyUserManageUserData = YesOrNoEnum.NO.getCode();

    /**
     * 是否是管理员用户
     */
    private Integer canSeeAllOrderList = YesOrNoEnum.NO.getCode();

    public OperatorPerm() {
    }

    public OperatorPerm(Integer canSeeSeasonDetailRankAndScore, Integer canSeeUserSeasonDetailForUserScoreItem, Integer canSeeUserDetailForAssessScoreList, Integer canSeeRankingList, Integer canModifyUserManageUserData) {
        this.canSeeSeasonDetailRankAndScore = canSeeSeasonDetailRankAndScore;
        this.canSeeUserSeasonDetailForUserScoreItem = canSeeUserSeasonDetailForUserScoreItem;
        this.canSeeUserDetailForAssessScoreList = canSeeUserDetailForAssessScoreList;
        this.canSeeRankingList = canSeeRankingList;
        this.canModifyUserManageUserData = canModifyUserManageUserData;
    }

    public OperatorPerm(List<String> opers) {
        if (null != opers && opers.size() > 0) {
            canSeeSeasonDetailRankAndScore = YesOrNoEnum.getCodeByBool(opers.contains(OperatorConstants.CAN_SEE_SEASON_DETAIL_RANK_AND_SCORE));
            canSeeUserSeasonDetailForUserScoreItem = YesOrNoEnum.getCodeByBool(opers.contains(OperatorConstants.CAN_SEE_USER_SEASON_DETAIL_FOR_USER_SCORE_ITEM));
            canSeeUserDetailForAssessScoreList = YesOrNoEnum.getCodeByBool(opers.contains(OperatorConstants.CAN_SEE_USER_DETAIL_FOR_ASSESS_SCORE_LIST));
            canSeeRankingList = YesOrNoEnum.getCodeByBool(opers.contains(OperatorConstants.CAN_SEE_RANKING_LIST));
            canModifyUserManageUserData = YesOrNoEnum.getCodeByBool(opers.contains(OperatorConstants.CAN_MODIFY_USER_MANAGE_USER_DATA));
            canSeeAllOrderList = YesOrNoEnum.getCodeByBool(opers.contains(OperatorConstants.CAN_SEE_ALL_ORDER_LIST));

        }
    }

    public Integer getCanSeeSeasonDetailRankAndScore() {
        return canSeeSeasonDetailRankAndScore;
    }

    public void setCanSeeSeasonDetailRankAndScore(Integer canSeeSeasonDetailRankAndScore) {
        this.canSeeSeasonDetailRankAndScore = canSeeSeasonDetailRankAndScore;
    }

    public Integer getCanSeeUserSeasonDetailForUserScoreItem() {
        return canSeeUserSeasonDetailForUserScoreItem;
    }

    public void setCanSeeUserSeasonDetailForUserScoreItem(Integer canSeeUserSeasonDetailForUserScoreItem) {
        this.canSeeUserSeasonDetailForUserScoreItem = canSeeUserSeasonDetailForUserScoreItem;
    }

    public Integer getCanSeeUserDetailForAssessScoreList() {
        return canSeeUserDetailForAssessScoreList;
    }

    public void setCanSeeUserDetailForAssessScoreList(Integer canSeeUserDetailForAssessScoreList) {
        this.canSeeUserDetailForAssessScoreList = canSeeUserDetailForAssessScoreList;
    }

    public Integer getCanSeeRankingList() {
        return canSeeRankingList;
    }

    public void setCanSeeRankingList(Integer canSeeRankingList) {
        this.canSeeRankingList = canSeeRankingList;
    }

    public Integer getCanModifyUserManageUserData() {
        return canModifyUserManageUserData;
    }

    public void setCanModifyUserManageUserData(Integer canModifyUserManageUserData) {
        this.canModifyUserManageUserData = canModifyUserManageUserData;
    }

    public Integer getCanSeeAllOrderList() {
        return canSeeAllOrderList;
    }

    public void setCanSeeAllOrderList(Integer canSeeAllOrderList) {
        this.canSeeAllOrderList = canSeeAllOrderList;
    }
}
