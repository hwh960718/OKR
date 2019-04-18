package com.mobvista.okr.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 用户标签DTO
 *
 * @author jiahuijie
 * @date 2017/12/30
 */
@ApiModel("用户标签DTO")
public class UserTipDTO {

    /**
     * 用户标签id
     */
    private Long userSeasonTipId;

    /**
     * 标签title
     */
    @ApiModelProperty("标签")
    private String title;

    /**
     * 标签颜色
     */
    @ApiModelProperty("标签颜色")
    private String color;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createdDate;

    /**
     * 标签个数
     */
    private int titleCount;


    public UserTipDTO() {
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getUserSeasonTipId() {
        return userSeasonTipId;
    }

    public void setUserSeasonTipId(Long userSeasonTipId) {
        this.userSeasonTipId = userSeasonTipId;
    }

    public int getTitleCount() {
        return titleCount;
    }

    public void setTitleCount(int titleCount) {
        this.titleCount = titleCount;
    }

    @Override
    public String toString() {
        return "UserTipDTO{" +
                "userSeasonTipId=" + userSeasonTipId +
                ", title='" + title + '\'' +
                ", color='" + color + '\'' +
                ", createdDate=" + createdDate +
                ", titleCount=" + titleCount +
                '}';
    }
}
