package com.mobvista.okr.repository;

import com.mobvista.okr.domain.UserSeasonItem;
import com.mobvista.okr.repository.base.BaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 顾炜[GuWei]
 */
@Mapper
public interface UserSeasonItemRepository extends BaseRepository<UserSeasonItem> {
//    List<UserSeasonItem> findAllByUserSeasonIdExceptInvalidUser(Long userSeasonId);

    /**
     * 根据用户考核id集合查询 考核明细
     *
     * @param userSeasonIdList
     * @return
     */
    List<UserSeasonItem> findAllByUserSeasonIdIn(List<Long> userSeasonIdList);

    /**
     * 根据用户考核id和选项id查询考核详情
     *
     * @param userSeasonId
     * @param optionId
     * @return
     */
    UserSeasonItem findOneByUserSeasonIdAndOptionId(@Param("userSeasonId") Long userSeasonId, @Param("optionId") Long optionId);

    void insertList(List<UserSeasonItem> list);

    void updateList(List<UserSeasonItem> list);

}