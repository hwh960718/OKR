package com.mobvista.okr.dto;

import com.mobvista.okr.domain.ScoreOption;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 评分项DTO
 *
 * @author jiahuijie
 * @date 2017/12/30
 */
@ApiModel("评分项DTO")
public class ScoreOptionDTO {

    private Long id;

    /**
     * 指标名称
     */
    @ApiModelProperty("指标名称")
    private String name;

    /**
     * 指标权重
     */
    @ApiModelProperty("指标权重")
    private Integer weight;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private Byte type;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createdDate;

    public ScoreOptionDTO() {
    }

    public ScoreOptionDTO(ScoreOption scoreOption) {
        this.setId(scoreOption.getId());
        this.setName(scoreOption.getName());
        this.setType(scoreOption.getType());
        this.setCreatedDate(scoreOption.getCreatedDate());
        this.setWeight(scoreOption.getWeight());
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "ScoreOptionDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", weight=" + weight +
                ", type=" + type +
                ", createdDate=" + createdDate +
                '}';
    }
}
