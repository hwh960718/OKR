package com.mobvista.okr.dto;

import com.mobvista.okr.enums.AssessLevel;
import com.mobvista.okr.vm.XYAxisVM;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 用户季度考核排行DTO
 *
 * @author jiahuijie
 * @date 2017/12/30
 */
@ApiModel("用户季度考核任务列表")
public class RankingDTO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("姓名")
    private String realName;

    @ApiModelProperty("头像")
    private String profilePhoto;

    @ApiModelProperty("部门id")
    private Long departmentId;

    @ApiModelProperty("部门名称")
    private String departmentName;

    @ApiModelProperty("职级")
    private String rank;

    @ApiModelProperty("季度id")
    private Long seasonId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("目标标题")
    private String okrTitle;

    @ApiModelProperty("季度得分")
    private BigDecimal score;

    @ApiModelProperty("排名")
    private Integer ranking;

    @ApiModelProperty("评价等级")
    private AssessLevel assessLevel;

    private XYAxisVM xyAxisVM;

    public RankingDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Long getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(Long seasonId) {
        this.seasonId = seasonId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOkrTitle() {
        return okrTitle;
    }

    public void setOkrTitle(String okrTitle) {
        this.okrTitle = okrTitle;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public AssessLevel getAssessLevel() {
        return assessLevel;
    }

    public void setAssessLevel(AssessLevel assessLevel) {
        this.assessLevel = assessLevel;
    }

    public XYAxisVM getXyAxisVM() {
        return xyAxisVM;
    }

    public void setXyAxisVM(XYAxisVM xyAxisVM) {
        this.xyAxisVM = xyAxisVM;
    }

    @Override
    public String toString() {
        return "RankingDTO{" +
                "id=" + id +
                ", realName='" + realName + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                ", departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                ", rank='" + rank + '\'' +
                ", seasonId=" + seasonId +
                ", userId=" + userId +
                ", okrTitle='" + okrTitle + '\'' +
                ", score=" + score +
                ", ranking=" + ranking +
                ", assessLevel=" + assessLevel +
                '}';
    }
}
