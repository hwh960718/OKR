package com.mobvista.okr.repository;

import com.mobvista.okr.domain.UserSeason;
import com.mobvista.okr.dto.RankingDTO;
import com.mobvista.okr.dto.UserInfoDTO;
import com.mobvista.okr.dto.UserInfoLiteDTO;
import com.mobvista.okr.dto.UserSeasonDTO;
import com.mobvista.okr.repository.base.BaseRepository;
import com.mobvista.okr.vm.XYAxisVM;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 顾炜[GuWei]
 */
@Mapper
public interface UserSeasonRepository extends BaseRepository<UserSeason> {

    /**
     * 根据考核id和用户id获取用户考核信息
     *
     * @param seasonId
     * @param userId
     * @return
     */
    UserSeason findOneBySeasonIdAndUserId(@Param("seasonId") Long seasonId, @Param("userId") Long userId);

    List<UserSeason> findOneBySeasonIdAndUserIdIn(@Param("seasonId") Long seasonId, @Param("userIdList") List<Long> userIdList);

    /**
     * 根据考核id获取用户考核信息集合
     *
     * @param seasonId
     * @return
     */
    List<UserSeason> findAllBySeasonId(Long seasonId);

    /**
     * 根据考核id获取用户考核信息，排除无效（离职）用户
     *
     * @param seasonId
     * @return
     */
    List<UserSeason> findAllBySeasonIdExceptInvalidUser(Long seasonId);

//    List<UserSeason> findAllBySeasonIdAndIsFilledOkrIsTrueAndIsSelectedAssessorIsTrue(Long seasonId);
//
//    List<UserSeason> findAllBySeasonIdAndIsFilledOkrIsTrue(Long seasonId);

    void insertList(List<UserSeason> list);

    /**
     * 批量更新
     *
     * @param list
     */
    void updateList(List<UserSeason> list);


    int getUserSeasonRank(@Param("seasonId") Long seasonId, @Param("score") BigDecimal score);

    List<UserSeasonDTO> queryByUserIdAndStatus(@Param("assessStatus") Byte assessStatus, @Param("userId") Long userId, @Param("start") int start, @Param("size") int size);

    long countByUserIdAndStatus(@Param("assessStatus") Byte assessStatus, @Param("userId") Long userId);

    long countAssessStatusData(@Param("assessStatus") Byte assessStatus, @Param("userId") Long userId);

    List<UserInfoDTO> getUnFinishFilledOkrList(Long seasonId);


    long countRankingList(@Param("seasonId") Long seasonId, @Param("scoreOptionId") Long scoreOptionId, @Param("assessResult") Integer assessResult);

    /**
     * 统计所有参与OKR考核的人数
     *
     * @param seasonId
     * @return
     */
    long countOKRUser(@Param("seasonId") Long seasonId);

    List<RankingDTO> queryRankingList(@Param("seasonId") Long seasonId, @Param("scoreOptionId") Long scoreOptionId, @Param("assessResult") Integer assessResult, @Param("start") int start, @Param("size") int size);

    long countCompanyOkrOverview(@Param("seasonId") Long seasonId, @Param("depIds") List<Long> depIds);

    List<RankingDTO> queryCompanyOkrOverview(@Param("seasonId") Long seasonId, @Param("depIds") List<Long> depIds, @Param("start") int start, @Param("size") int size);

    long countUnFinishFilledOkrList(@Param("seasonId") Long seasonId);

    List<UserInfoLiteDTO> queryUnFinishFilledOkrList(@Param("seasonId") Long seasonId, @Param("start") int start, @Param("size") int size);

    /**
     * 获取未完成选人的员工 统计
     *
     * @param seasonId
     * @return
     */
    long countUnSelectedAssessorList(@Param("seasonId") Long seasonId);

    /**
     * 获取未完成选人的员工
     *
     * @param seasonId
     * @param start
     * @param size
     * @return
     */
    List<UserInfoLiteDTO> queryUnSelectedAssessorList(@Param("seasonId") Long seasonId, @Param("start") int start, @Param("size") int size);

    long countUnFinishAssessList(@Param("seasonId") Long seasonId, @Param("statusList") List<Byte> statusList);

    /**
     * 统计所偶有评价员工数
     *
     * @param seasonId
     * @return
     */
    long countAllAssess(@Param("seasonId") Long seasonId);

    List<UserInfoLiteDTO> queryUnFinishAssessList(@Param("seasonId") Long seasonId, @Param("statusList") List<Byte> statusList, @Param("start") int start, @Param("size") int size);


    /**
     * 根据用户考核id查询考核信息
     *
     * @param idList
     * @return
     */
    List<UserSeason> findAllByIdList(List<Long> idList);


    /**
     * 查询okr考核视图数据
     *
     * @param assessStatus
     * @param userId
     * @return
     */
    List<UserSeasonDTO> queryViewDataByUserIdAndStatus(@Param("assessStatus") Byte assessStatus, @Param("userId") Long userId);


    /**
     * 查询用户所有季度考核得分排名情况
     *
     * @param userId
     * @return
     */
    List<XYAxisVM> queryUserAllSeasonRank(Long userId);

    /**
     * 查询用户所有季度考核得分情况
     *
     * @param userId
     * @return
     */
    List<XYAxisVM> queryUserAllSeasonScore(Long userId);


    /**
     * 查询用户最后一个考核记录
     *
     * @param userId
     * @return
     */
    UserSeason queryLastInfoByUserId(Long userId);


    /**
     * 根据季度考核id查询 该季度考核排名积分情况
     *
     * @param seasonId
     * @param size
     * @return
     */
    List<UserSeasonDTO> queryUserRankingInfoBySeasonIdAndNum(@Param("seasonId") Long seasonId, @Param("size") int size);

}
