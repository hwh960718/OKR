package com.mobvista.okr.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户OKR目标DTO
 *
 * @author jiahuijie
 * @date 2017/12/30
 */
@ApiModel("用户OKR目标DTO")
public class UserOkrDTO {

    /**
     * 季度id
     */
    @ApiModelProperty("季度id")
    private Long seasonId;

    /**
     * 用户季度考核id
     */
    @ApiModelProperty("用户季度考核id")
    private Long userSeasonId;

    /**
     * 季度标题
     */
    @ApiModelProperty("季度标题")
    private String seasonTitle;

    /**
     * 目标标题
     */
    @ApiModelProperty("目标标题")
    private String okrTitle;

    /**
     * 目标详情
     */
    @ApiModelProperty("目标详情")
    private String okrContent;

    public UserOkrDTO() {
    }

    public Long getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(Long seasonId) {
        this.seasonId = seasonId;
    }

    public Long getUserSeasonId() {
        return userSeasonId;
    }

    public void setUserSeasonId(Long userSeasonId) {
        this.userSeasonId = userSeasonId;
    }

    public String getSeasonTitle() {
        return seasonTitle;
    }

    public void setSeasonTitle(String seasonTitle) {
        this.seasonTitle = seasonTitle;
    }

    public String getOkrTitle() {
        return okrTitle;
    }

    public void setOkrTitle(String okrTitle) {
        this.okrTitle = okrTitle;
    }

    public String getOkrContent() {
        return okrContent;
    }

    public void setOkrContent(String okrContent) {
        this.okrContent = okrContent;
    }

    public UserOkrDTO(Long seasonId, Long userSeasonId, String seasonTitle, String okrTitle, String okrContent) {
        this.seasonId = seasonId;
        this.userSeasonId = userSeasonId;
        this.seasonTitle = seasonTitle;
        this.okrTitle = okrTitle;
        this.okrContent = okrContent;
    }

    @Override
    public String toString() {
        return "UserOkrDTO{" +
                "seasonId=" + seasonId +
                ", userSeasonId=" + userSeasonId +
                ", seasonTitle='" + seasonTitle + '\'' +
                ", okrTitle='" + okrTitle + '\'' +
                ", okrContent='" + okrContent + '\'' +
                '}';
    }
}
