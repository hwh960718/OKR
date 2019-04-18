package com.mobvista.okr.vm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 制定自己OKR VM
 */
@ApiModel("制定自己OKR")
public class MakeOkrVM {

    @ApiModelProperty("季度id")
    @NotNull
    private Long seasonId;

    @ApiModelProperty("目标标题")
    @NotBlank
    @Size(min = 1, max = 200, message = "目标标题长度1-200个字符")
    private String title;

    @ApiModelProperty("目标内容")
    @NotBlank
    @Size(min = 1, max = 500, message = "目标内容长度1-500个字符")
    private String content;

    public Long getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(Long seasonId) {
        this.seasonId = seasonId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MakeOkrVM{" +
                "seasonId=" + seasonId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
