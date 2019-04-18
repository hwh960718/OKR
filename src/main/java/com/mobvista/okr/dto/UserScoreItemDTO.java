package com.mobvista.okr.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 评价得分DTO
 *
 * @author jiahuijie
 * @date 2017/12/30
 */
@ApiModel("评价得分DTODTO")
public class UserScoreItemDTO {

    /**
     * 评价项id
     */
    @ApiModelProperty("评价项id")
    private Long optionId;

    /**
     * 评价项名称
     */
    @ApiModelProperty("评价项名称")
    private String optionName;

    /**
     * 评价项类型
     */
    @ApiModelProperty("评价项类型CODE")
    private byte optionTypeCode;
    /**
     * 评价项类型
     */
    @ApiModelProperty("评价项类型Value")
    private String optionTypeValue;

    /**
     * 评价项得分
     */
    @ApiModelProperty("评价项得分")
    private BigDecimal score;

    /**
     * 排名
     */
    private Integer ranking;

    public UserScoreItemDTO() {
    }


    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public byte getOptionTypeCode() {
        return optionTypeCode;
    }

    public void setOptionTypeCode(byte optionTypeCode) {
        this.optionTypeCode = optionTypeCode;
    }

    public String getOptionTypeValue() {
        return optionTypeValue;
    }

    public void setOptionTypeValue(String optionTypeValue) {
        this.optionTypeValue = optionTypeValue;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    @Override
    public String toString() {
        return "UserScoreItemDTO{" +
                "optionId=" + optionId +
                ", optionName='" + optionName + '\'' +
                ", optionTypeCode=" + optionTypeCode +
                ", optionTypeValue='" + optionTypeValue + '\'' +
                ", score=" + score +
                ", ranking=" + ranking +
                '}';
    }
}
