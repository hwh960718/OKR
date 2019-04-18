package com.mobvista.okr.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户考核比对
 *
 * @author Weier Gu (顾炜)
 * @date 2018/6/26 17:36
 */
public class UserSeasonCompareDTO {
    /**
     * 用户信息
     */
    private UserInfoDTO userInfoDTO;
    /**
     * 季度评分
     */
    private BigDecimal score;
    /**
     * 排名
     */
    private Integer ranking;

    /**
     * OKR 季度目标
     */
    private List<OkrContentDTO> okrContentDTOList;
    /**
     * 评分详情
     */
    private List<AssessScoreDTO> assessScoreDTOList;

    /**
     * 考评项得分
     */
    private List<UserScoreItemDTO> userScoreItemDTOS;

    public UserInfoDTO getUserInfoDTO() {
        return userInfoDTO;
    }

    public void setUserInfoDTO(UserInfoDTO userInfoDTO) {
        this.userInfoDTO = userInfoDTO;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public List<OkrContentDTO> getOkrContentDTOList() {
        return okrContentDTOList;
    }

    public void setOkrContentDTOList(List<OkrContentDTO> okrContentDTOList) {
        this.okrContentDTOList = okrContentDTOList;
    }

    public List<AssessScoreDTO> getAssessScoreDTOList() {
        return assessScoreDTOList;
    }

    public void setAssessScoreDTOList(List<AssessScoreDTO> assessScoreDTOList) {
        this.assessScoreDTOList = assessScoreDTOList;
    }

    public List<UserScoreItemDTO> getUserScoreItemDTOS() {
        return userScoreItemDTOS;
    }

    public void setUserScoreItemDTOS(List<UserScoreItemDTO> userScoreItemDTOS) {
        this.userScoreItemDTOS = userScoreItemDTOS;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }
}
