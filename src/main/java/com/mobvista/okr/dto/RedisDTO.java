package com.mobvista.okr.dto;

/**
 * @author Weier Gu (顾炜)
 * @date 2018/7/2 11:03
 */
public class RedisDTO {


    /**
     * key
     */
    private String key;
    /**
     * key对应的文本
     */
    private String keyText;

    /**
     * 需要被评论的数据
     */
    private Integer assessedUserCount;
    /**
     * 需要被评论的其他部门数量
     */
    private Integer assessedOtherDepUserCount;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKeyText() {
        return keyText;
    }

    public void setKeyText(String keyText) {
        this.keyText = keyText;
    }

    public int getAssessedUserCount() {
        return assessedUserCount;
    }

    public void setAssessedUserCount(Integer assessedUserCount) {
        this.assessedUserCount = assessedUserCount;
    }

    public Integer getAssessedOtherDepUserCount() {
        return assessedOtherDepUserCount;
    }

    public void setAssessedOtherDepUserCount(Integer assessedOtherDepUserCount) {
        this.assessedOtherDepUserCount = assessedOtherDepUserCount;
    }
}
