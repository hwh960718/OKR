package com.mobvista.okr.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mobvista.okr.dto.mbacs.AccountUserProfile;

import java.util.List;

/**
 * 注释：账号中心分页对象
 * 作者：刘腾飞【liutengfei】
 * 时间：2017-03-08 下午4:32
 */
public class AccountPageData {
    private Integer page;

    @JsonProperty("per_page")
    private Integer perPage;

    private Integer total;

    private List<AccountUserProfile> contents;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<AccountUserProfile> getContents() {
        return contents;
    }

    public void setContents(List<AccountUserProfile> contents) {
        this.contents = contents;
    }
}
