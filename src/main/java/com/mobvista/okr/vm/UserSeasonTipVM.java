package com.mobvista.okr.vm;

import io.swagger.annotations.ApiModel;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 用户季度标签VM
 */
@ApiModel("用户季度标签")
public class UserSeasonTipVM {

    /**
     * 被评价用户id
     */
    @NotNull
    private Long userId;

    /**
     * 标签内容
     */
    @NotBlank(message = "标签内容不能为空")
    @Size(min = 1, max = 20, message = "标签长度为1-20个字符")
    private String title;

    private String color;

    @NotNull
    public Long getUserId() {
        return userId;
    }

    public void setUserId(@NotNull Long userId) {
        this.userId = userId;
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
}
