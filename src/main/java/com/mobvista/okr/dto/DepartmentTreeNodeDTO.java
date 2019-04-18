package com.mobvista.okr.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 部门树节点DTO
 *
 * @author jiahuijie
 * @date 2017/12/30
 */
@ApiModel("部门树节点DTO")
public class DepartmentTreeNodeDTO {

    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty("部门名称")
    private String title;

    private List<DepartmentTreeNodeDTO> children;

    public DepartmentTreeNodeDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<DepartmentTreeNodeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<DepartmentTreeNodeDTO> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "DepartmentTreeNodeDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", children=" + children +
                '}';
    }
}
