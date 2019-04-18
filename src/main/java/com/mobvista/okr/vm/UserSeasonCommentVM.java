package com.mobvista.okr.vm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 用户季度评论VM
 *
 * @author guwei
 */
@ApiModel("用户季度评论")
public class UserSeasonCommentVM {

    @ApiModelProperty("评论id")
    private Long commentId;

    @ApiModelProperty("用户季度考核id")
    @NotNull
    private Long userId;

    @ApiModelProperty("评论内容")
    @NotBlank(message = "评论内容不能为空")
    private String content;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    @NotNull
    public Long getUserId() {
        return userId;
    }

    public void setUserId(@NotNull Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
