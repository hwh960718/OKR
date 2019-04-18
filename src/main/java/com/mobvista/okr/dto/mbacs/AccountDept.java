package com.mobvista.okr.dto.mbacs;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

/**
 * 注释：账号中心部门信息
 * 作者：刘腾飞【liutengfei】
 * 时间：2017-03-24 上午11:53
 */
public class AccountDept implements Serializable {
    //部门id
    private Long id;
    //部门父级id
    @JSONField(name = "parent_id")
    private Long parentId;
    //部门层级
    private int level;
    //部门名称
    private String name;
    //部门领导
    private List<Long> leader;
    //创建时间
    @JSONField(name = "created_at")
    private Long createdAt;
    //更新时间
    @JSONField(name = "updated_at")
    private Long updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getLeader() {
        return leader;
    }

    public void setLeader(List<Long> leader) {
        this.leader = leader;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
