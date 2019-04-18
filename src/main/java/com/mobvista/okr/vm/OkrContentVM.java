package com.mobvista.okr.vm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A season.
 */
@ApiModel("okr目标")
public class OkrContentVM {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("okr目标id")
    private Long id;

    @ApiModelProperty("用户季度考核Id")
    @NotNull
    private Long userSeasonId;

    @ApiModelProperty("目标标题")
    @NotBlank
    @Size(min = 1, max = 200, message = "目标标题长度1-200个字符")
    private String okrTitle;

    @ApiModelProperty("目标内容")
    @NotBlank
    private String okrContent;

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
        if (StringUtils.isNotBlank(okrTitle)) {
            okrTitle = okrTitle.trim();
        }
        this.okrTitle = okrTitle;
    }

    public String getOkrContent() {
        return okrContent;
    }

    public void setOkrContent(String okrContent) {
        if (StringUtils.isNotBlank(okrContent)) {
            okrContent = okrContent.trim();
        }
        this.okrContent = okrContent;
    }

    @Override
    public String toString() {
        return "OkrContent{" +
                "id='" + id + '\'' +
                ", userSeasonId='" + userSeasonId + '\'' +
                ", startTime=" + okrTitle +
                ", endTime=" + okrContent +
                '}';
    }
}
