package com.mobvista.okr.dto;

import com.mobvista.okr.domain.OkrContent;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * A season.
 */
@ApiModel("okr目标DTO")
public class OkrContentDTO {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("ID")
    private Long id;


    @ApiModelProperty("用户考核ID")
    private Long userSeasonId;

    @ApiModelProperty("okr目标标题")
    private String okrTitle;

    @ApiModelProperty("okr目标内容")
    private String okrContent;

    public OkrContentDTO() {
    }

    public OkrContentDTO(OkrContent okrContent) {
        this.setId(okrContent.getId());
        this.setUserSeasonId(okrContent.getUserSeasonId());
        this.setOkrTitle(okrContent.getOkrTitle());
        this.setOkrContent(okrContent.getOkrContent());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserSeasonId() {
        return userSeasonId;
    }

    public void setUserSeasonId(Long userSeasonId) {
        this.userSeasonId = userSeasonId;
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

    @Override
    public String toString() {
        return "OkrContent{" +
                ", userSeasonId='" + userSeasonId + '\'' +
                ", startTime=" + okrTitle +
                ", endTime=" + okrContent +
                '}';
    }
}
