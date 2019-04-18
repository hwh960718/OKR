package com.mobvista.okr.repository;

import com.mobvista.okr.domain.Season;
import com.mobvista.okr.dto.SeasonDTO;
import com.mobvista.okr.repository.base.BaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 顾炜[GuWei]
 */
@Mapper
public interface SeasonRepository extends BaseRepository<Season> {

    /**
     * 根据考核标题查询考核信息
     *
     * @param title
     * @return
     */
    Season findOneByTitle(String title);

//    List<Season> findAllBySecondStageStartTimeBeforeAndStatusAndIsMakeAssessTaskIsFalse(Date date, SeasonStatus seasonStatus);

//    List<Season> findAllBySecondStageEndTimeBeforeAndStatusAndIsMakeSeasonScoreIsFalse(Date date, SeasonStatus seasonStatus);

//    Page<Season> findAllByStatus(SeasonStatus seasonStatus, Pageable pageable);
//
//    List<Season> findAllByStatus(SeasonStatus seasonStatus);
//
//    Page<Season> findAllByStatusAndIsMakeSeasonScoreIsTrue(SeasonStatus seasonStatus, Pageable pageable);


    /**
     * 根据对象查询考核信息
     *
     * @param season
     * @return
     */
    List<Season> findAllBySeason(@Param("season") Season season, @Param("start") int start, @Param("pageSize") int pageSize);


    /**
     * 查询最近的一次考核
     *
     * @param seasonId
     * @return
     */
    Season findFirstByCreatedDateDesc(Long seasonId);

    /**
     * 根据id集合查询考核信息
     *
     * @param idList
     * @return
     */
    List<Season> findByIdIn(List<Long> idList);

    /**
     * 分页查询统计
     *
     * @return
     */
    long countPage();

    /**
     * 分页查询
     *
     * @param start
     * @param pageSize
     * @return
     */
    List<Season> queryPage(@Param("start") int start, @Param("pageSize") int pageSize);


    /**
     * 查询考核的年份
     *
     * @return
     */
    List<String> queryAllSeasonYear(List<Byte> list);

    List<SeasonDTO> queryAllBySeasonStatusAndYear(@Param("list") List<Byte> list, @Param("year") String year);


}