package com.mobvista.okr.vm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel("修改密码")
public class UpdatePwdVM {

    /**
     * 新密码
     */
    @ApiModelProperty("新密码")
    @NotBlank(message = "新密码不能为空")
    @Length(min = 6, max = 16, message = "密码为6-16位数字、字母")
    private String password;

    /**
     * 重复密码
     */
    @ApiModelProperty("重复密码")
    @NotBlank(message = "重复密码不能为空")
    @Length(min = 6, max = 16, message = "密码为6-16位数字、字母")
    private String rePassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    @Override
    public String toString() {
        return "UpdatePwdVM{" +
                "password='" + password + '\'' +
                ", rePassword='" + rePassword + '\'' +
                '}';
    }
}
