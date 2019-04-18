package com.mobvista.okr.repository;

import com.mobvista.okr.domain.OkrContent;
import com.mobvista.okr.repository.base.BaseRepository;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 顾炜[GuWei]
 */
@Mapper
public interface OkrContentRepository extends BaseRepository<OkrContent> {


    /**
     * 根据用户考核id查询考核内容
     *
     * @param userSeasonId
     * @return
     */
    List<OkrContent> findAllByUserSeasonId(Long userSeasonId);

    /**
     * 根据用户考核ID集合查询考核内容
     *
     * @param list
     * @return
     */
    List<OkrContent> findAllByUserSeasonIdList(List<Long> list);

}