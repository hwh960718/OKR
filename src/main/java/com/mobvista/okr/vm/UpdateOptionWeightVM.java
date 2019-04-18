package com.mobvista.okr.vm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 修改评分项权重
 */
@ApiModel("修改评分项权重")
public class UpdateOptionWeightVM {

    @NotNull
    private Long id;

    @ApiModelProperty("权重")
    @NotNull(message = "权重不能为空")
    private Integer weight;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "UpdateOptionWeightVM{" +
                "id=" + id +
                ", weight=" + weight +
                '}';
    }
}
