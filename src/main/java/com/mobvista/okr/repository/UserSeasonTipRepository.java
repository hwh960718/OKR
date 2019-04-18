package com.mobvista.okr.repository;

import com.mobvista.okr.domain.UserSeasonTip;
import com.mobvista.okr.dto.UserTipDTO;
import com.mobvista.okr.repository.base.BaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 顾炜[GuWei]
 */
@Mapper
public interface UserSeasonTipRepository extends BaseRepository<UserSeasonTip> {
//        List<UserSeasonTip> findAllByUserIdAndSeasonId(Long userId, Long seasonId);
//    long countByUserIdAndSeasonIdAndAssessorId(Long userId, Long seasonId, Long assessorId);
//    long countByUserIdAndSeasonIdAndTitle(Long userId, Long seasonId, String title);

    /**
     * 根据用户考核id获取用户标签信息
     *
     * @param userSeasonId
     * @return
     */


    List<UserTipDTO> findAllByUserId(Long userSeasonId);


    /**
     * 查看是否重复添加标签
     *
     * @param userId
     * @param tipId
     * @param assessorId
     * @return
     */
    long countByUserIdAndTipIdAndUserId(@Param("userId") Long userId, @Param("tipId") Long tipId, @Param("assessorId") Long assessorId);


    /**
     * 根据标签id和评价人查询数据删除
     *
     * @param tipId
     * @param assessorId
     * @return
     */
    long delByTipIdAndAssessorId(@Param("tipId") Long tipId, @Param("assessorId") Long assessorId, @Param("userId") Long userId);
}