package com.mobvista.okr.dto;

import java.util.List;

/**
 * 评价任务列表DTO
 *
 * @author jiahuijie
 * @date 2017/12/30
 */

public class AssessTaskSeasonDTO {

    /**
     * 季度id
     */
    private Long seasonId;

    /**
     * 季度标题
     */
    private String seasonTitle;


    /**
     * 考评任务信息
     */
    private List<AssessTaskDTO> assessTaskDTOList;

    /**
     * 未评价数量
     */
    private int underwayNum = 0;

    public Long getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(Long seasonId) {
        this.seasonId = seasonId;
    }

    public String getSeasonTitle() {
        return seasonTitle;
    }

    public void setSeasonTitle(String seasonTitle) {
        this.seasonTitle = seasonTitle;
    }

    public List<AssessTaskDTO> getAssessTaskDTOList() {
        return assessTaskDTOList;
    }

    public void setAssessTaskDTOList(List<AssessTaskDTO> assessTaskDTOList) {
        this.assessTaskDTOList = assessTaskDTOList;
    }

    public int getUnderwayNum() {
        return underwayNum;
    }

    public void setUnderwayNum(int underwayNum) {
        this.underwayNum = underwayNum;
    }
}
