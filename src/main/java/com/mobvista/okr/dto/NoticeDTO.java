package com.mobvista.okr.dto;

import com.mobvista.okr.enums.NoticeStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 公告DTO
 *
 * @author jiahuijie
 * @date 2017/12/30
 */
@ApiModel("公告DTO")
public class NoticeDTO {

    private Long id;
    @ApiModelProperty("公告内容")
    private String content;
    @ApiModelProperty("创建时间")
    private Date createdDate;
    @ApiModelProperty("状态")
    private Byte status;
    @ApiModelProperty("状态描述")
    private String statusText;
    @ApiModelProperty("有效时间")
    private Date validDate;
    @ApiModelProperty("修改时间")
    private Date lastModifiedDate;
    @ApiModelProperty("创建人id")
    private Long createUserId;
    @ApiModelProperty("修改人id")
    private Long modifyUserId;
    @ApiModelProperty("创建人")
    private String createUserName;
    @ApiModelProperty("修改人")
    private String modifyUserName;


    public NoticeDTO() {
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getStatusText() {
        if (null != status) {
            statusText = NoticeStatus.getValueByCode(status);
        }
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public Date getValidDate() {
        return validDate;
    }

    public void setValidDate(Date validDate) {
        this.validDate = validDate;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Long getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(Long modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getModifyUserName() {
        return modifyUserName;
    }

    public void setModifyUserName(String modifyUserName) {
        this.modifyUserName = modifyUserName;
    }

    @Override
    public String toString() {
        return "NoticeDTO{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createdDate=" + createdDate +
                ", status=" + status +
                ", statusText='" + statusText + '\'' +
                ", validDate=" + validDate +
                ", lastModifiedDate=" + lastModifiedDate +
                ", createUserId=" + createUserId +
                ", modifyUserId=" + modifyUserId +
                ", createUserName='" + createUserName + '\'' +
                ", modifyUserName='" + modifyUserName + '\'' +
                '}';
    }
}
