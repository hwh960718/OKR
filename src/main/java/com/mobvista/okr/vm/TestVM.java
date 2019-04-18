package com.mobvista.okr.vm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * 测试类VM
 */
@ApiModel("测试类")
public class TestVM {

    private Long id;

    @ApiModelProperty("名称")
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;

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

    @Override
    public String toString() {
        return "TestVM{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
