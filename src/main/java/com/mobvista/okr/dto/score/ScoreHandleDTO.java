package com.mobvista.okr.dto.score;

/**
 * 积分处理对象
 *
 * @author Weier Gu (顾炜)
 * @date 2018/7/25 17:38
 */
public class ScoreHandleDTO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 关联id
     */
    private Long relatedId;

    /**
     * 关联标识
     */
    private String relatedFlag;

    /**
     * 关联描述
     */
    private String relatedDesc;


    /**
     * 处理人id
     */
    private Long handleUserId;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Long relatedId) {
        this.relatedId = relatedId;
    }

    public String getRelatedFlag() {
        return relatedFlag;
    }

    public void setRelatedFlag(String relatedFlag) {
        this.relatedFlag = relatedFlag;
    }

    public String getRelatedDesc() {
        return relatedDesc;
    }

    public void setRelatedDesc(String relatedDesc) {
        this.relatedDesc = relatedDesc;
    }

    public Long getHandleUserId() {
        return handleUserId;
    }

    public void setHandleUserId(Long handleUserId) {
        this.handleUserId = handleUserId;
    }
}
