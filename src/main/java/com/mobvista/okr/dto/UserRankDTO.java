package com.mobvista.okr.dto;

import java.io.Serializable;

/**
 * @author 顾炜(GUWEI)
 * @date 2018/5/15 13:47
 */
public class UserRankDTO implements Serializable {

    private static final long serialVersionUID = -848578068824925427L;
    /**
     * 职级
     */
    private String rank;

    /**
     * 职级序号
     */
    private Integer rankNo;

    /**
     * 归属职级
     */
    private String ascription;

    /**
     * 权重
     */
    private Integer weight;

    public UserRankDTO() {
    }

    public UserRankDTO(Integer weight) {
        this.weight = weight;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getAscription() {
        return ascription;
    }

    public void setAscription(String ascription) {
        this.ascription = ascription;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getRankNo() {
        return rankNo;
    }

    public void setRankNo(Integer rankNo) {
        this.rankNo = rankNo;
    }

    @Override
    public String toString() {
        return "UserRankDTO{" +
                "rank='" + rank + '\'' +
                ", rankNo=" + rankNo +
                ", ascription='" + ascription + '\'' +
                ", weight=" + weight +
                '}';
    }
}
