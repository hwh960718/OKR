package com.mobvista.okr.vm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;


/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/3/30 11:30
 */
@ApiModel("修改用户信息")
public class UpdateUserVM {

    @ApiModelProperty(value = "本系统用户ID")
    @NotNull(message = "userId不能为空")
    private Long userId;

    @ApiModelProperty(value = "工作地")
    @Length(min = 0, max = 200, message = "工作地0~200个字符")
    private String workplace;

    /**
     * 职级
     */
    @ApiModelProperty(value = "职级")
    @Length(min = 0, max = 50, message = "职级0~20个字符")
    private String rank;

    /**
     * 毕业院校
     */
    @ApiModelProperty(value = "毕业院校")
    @Length(max = 50, message = "毕业院校0~50个字符")
    private String college;

    /**
     * 专业
     */
    @ApiModelProperty(value = "专业")
    @Length(max = 50, message = "专业0~50个字符")
    private String major;

    /**
     * 学历
     */
    @ApiModelProperty(value = "学历")
    private String degree;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }
}
