package com.mobvista.okr.repository;

import com.mobvista.okr.domain.ScoreRule;
import com.mobvista.okr.dto.score.ScoreRuleDTO;
import com.mobvista.okr.repository.base.BaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author Weier Gu (顾炜)
 */
@Mapper
public interface ScoreRuleRepository extends BaseRepository<ScoreRule> {

    /**
     * 根据触发方式和状态查询对应规则
     *
     * @param triggerModel
     * @param status
     * @return
     */
    List<ScoreRule> findByTriggerModelAndStatus(@Param("triggerModel") Byte triggerModel, @Param("status") Byte status);


    /**
     * 分页查询总数
     *
     * @param scoreRule
     * @return
     */
    long countPageByScoreRule(ScoreRule scoreRule);

    /**
     * 分页查询对应的数据
     *
     * @param scoreRule
     * @param start
     * @param size
     * @return
     */
    List<ScoreRuleDTO> queryPageByScoreRule(@Param("scoreRule") ScoreRule scoreRule, @Param("start") int start, @Param("size") int size);


    /**
     * 根据id更新状态
     *
     * @param expireDate
     * @param newStatus
     * @param oldStatus
     */
    void updateExpiredRuleStatus(@Param("expireDate") Date expireDate, @Param("oldStatus") Byte oldStatus, @Param("newStatus") Byte newStatus);

    /**
     * 更新可以规则
     *
     * @param enableDate
     * @param oldStatus
     * @param newStatus
     */
    void updateEnableRuleStatus(@Param("enableDate") Date enableDate, @Param("oldStatus") Byte oldStatus, @Param("newStatus") Byte newStatus);


    /**
     * 根据渠道和状态查询规则
     *
     * @param triggerMode
     * @param status
     * @return
     */
    List<ScoreRuleDTO> queryScoreRuleListByTriggerModeAndStatus(@Param("triggerMode") Byte triggerMode, @Param("status") Byte status);
}