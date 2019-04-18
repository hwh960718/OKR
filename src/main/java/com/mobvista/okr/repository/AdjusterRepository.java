package com.mobvista.okr.repository;

import com.mobvista.okr.domain.Adjuster;
import com.mobvista.okr.dto.UserInfoDTO;
import com.mobvista.okr.repository.base.BaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 顾炜[GuWei]
 */
@Mapper
public interface AdjusterRepository extends BaseRepository<Adjuster> {

    /**
     * 根据条件获取评价人 排除无效离职用户
     *
     * @param userSeasonId
     * @return
     */
    List<Adjuster> findAllByUserSeasonIdExceptInvalidUser(Long userSeasonId);

    /**
     * 根据评价人id和考核id查询被评价人id
     *
     * @param adjusterId
     * @param seasonId
     * @return
     */
    List<Long> findUserIdByAdjusterIdAndSeasonId(@Param("adjusterId") Long adjusterId, @Param("seasonId") Long seasonId);

    /**
     * 根据条件查询评价信息
     *
     * @param adjusterId
     * @param userSeasonId
     * @return
     */
    Adjuster findOneByAdjusterIdAndUserSeasonId(@Param("adjusterId") Long adjusterId, @Param("userSeasonId") Long userSeasonId);

    /**
     * 统计评价人数量，排除无效用户
     *
     * @param userSeasonId
     * @return
     */
    long countByUserSeasonIdExceptInvalidUser(Long userSeasonId);

    /**
     * 统计被评价人数量
     *
     * @param adjusterId
     * @param seasonId
     * @return
     */
    long countByAdjusterIdAndSeasonId(@Param("adjusterId") Long adjusterId, @Param("seasonId") Long seasonId);

    long deleteByUserIdAndUserSeasonId(@Param("userId") Long userId, @Param("userSeasonId") Long userSeasonId);

    List<UserInfoDTO> querySelectedUsers(@Param("userSeasonId") Long userSeasonId, @Param("userId") Long userId, @Param("userStatus") Byte userStatus);

    void insertList(List<Adjuster> list);
}