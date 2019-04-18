package com.mobvista.okr.repository;

import com.mobvista.okr.domain.ScoreUserDetail;
import com.mobvista.okr.dto.score.ScoreUserDetailDTO;
import com.mobvista.okr.repository.base.BaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Weier Gu (顾炜)
 */
@Mapper
public interface ScoreUserDetailRepository extends BaseRepository<ScoreUserDetail> {

    /**
     * 统计分页积分明细
     *
     * @param scoreUserDetail
     * @return
     */
    long countPageByScoreUserDetail(ScoreUserDetail scoreUserDetail);

    /**
     * 获取页面积分明细
     *
     * @param scoreUserDetail
     * @param start
     * @param size
     * @return
     */
    List<ScoreUserDetailDTO> queryPageByScoreUserDetail(@Param("scoreUserDetail") ScoreUserDetail scoreUserDetail, @Param("start") int start, @Param("size") int size);

    /**
     * 批量插入
     *
     * @param list
     */
    void insertList(List<ScoreUserDetail> list);
}