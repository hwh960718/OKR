package com.mobvista.okr.dto;

import com.mobvista.okr.domain.UserSeasonComment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 用户季度评论DTO
 *
 * @author jiahuijie
 * @date 2017/12/30
 */
@ApiModel("用户季度评论DTO")
public class UserSeasonCommentDTO {

    @ApiModelProperty("评论id")
    private Long id;

    @ApiModelProperty("评论者名字")
    private String assessorRealname;

    @ApiModelProperty("评论者头像")
    private String assessorProfilePhoto;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("顶的数量")
    private Long topCount;

    @ApiModelProperty("是否可顶")
    private Boolean canComment;

    @ApiModelProperty("评论时间")
    private Date createdDate;

    @ApiModelProperty("是否可编辑删除")
    private Boolean canUpdate;

    public UserSeasonCommentDTO() {
    }

    public UserSeasonCommentDTO(UserSeasonComment userSeasonComment, boolean canComment) {
        this.setCanComment(canComment);
        this.setTopCount(userSeasonComment.getTopCount());
        this.setId(userSeasonComment.getId());
        this.setCreatedDate(userSeasonComment.getCreatedDate());
        this.setContent(userSeasonComment.getContent());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTopCount() {
        return topCount;
    }

    public void setTopCount(Long topCount) {
        this.topCount = topCount;
    }

    public Boolean getCanComment() {
        return canComment;
    }

    public void setCanComment(Boolean canComment) {
        this.canComment = canComment;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getAssessorRealname() {
        return assessorRealname;
    }

    public void setAssessorRealname(String assessorRealname) {
        this.assessorRealname = assessorRealname;
    }

    public String getAssessorProfilePhoto() {
        return assessorProfilePhoto;
    }

    public void setAssessorProfilePhoto(String assessorProfilePhoto) {
        this.assessorProfilePhoto = assessorProfilePhoto;
    }

    public Boolean getCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(Boolean canUpdate) {
        this.canUpdate = canUpdate;
    }

    @Override
    public String toString() {
        return "UserSeasonCommentDTO{" +
                "id=" + id +
                ", assessorRealname='" + assessorRealname + '\'' +
                ", assessorProfilePhoto='" + assessorProfilePhoto + '\'' +
                ", content='" + content + '\'' +
                ", topCount=" + topCount +
                ", canComment=" + canComment +
                ", createdDate=" + createdDate +
                ", canUpdate=" + canUpdate +
                '}';
    }
}
