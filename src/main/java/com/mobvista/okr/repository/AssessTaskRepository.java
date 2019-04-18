package com.mobvista.okr.repository;

import com.mobvista.okr.domain.AssessTask;
import com.mobvista.okr.dto.AssessTaskDTO;
import com.mobvista.okr.dto.AssessTaskSeasonDTO;
import com.mobvista.okr.dto.SubordinateAssessTaskDTO;
import com.mobvista.okr.dto.UserInfoDTO;
import com.mobvista.okr.repository.base.BaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 顾炜[GuWei]
 */
@Mapper
public interface AssessTaskRepository extends BaseRepository<AssessTask> {
    /**
     * 根据用户考评id查询评价信息
     *
     * @param userSeasonId
     * @return
     */
    List<AssessTask> findAllByUserSeasonId(Long userSeasonId);

    List<AssessTask> findAllByUserSeasonIdList(List<Long> userSeasonIdList);

    /**
     * 根据用户考评id查询积分不为空的评价信息
     *
     * @param userSeasonId
     * @return
     */
    List<AssessTask> findAllByUserSeasonIdAndScoreIsNotNull(Long userSeasonId);

//    List<AssessTask> findAllBySeasonIdAndUserIdAndScoreIsNotNull(Long seasonId, Long userId);

    /**
     * 根据用户考评id和状态查询评价信息
     *
     * @param userSeasonId
     * @param status
     * @return
     */
    List<AssessTask> findAllByUserSeasonIdAndStatus(@Param("userSeasonId") Long userSeasonId, @Param("status") Byte status);

    /**
     * 根据用户考评id和状态统计评价数量
     *
     * @param userSeasonId
     * @return
     */
    int countByUserSeasonIdAndScoreIsNotNull(Long userSeasonId);


    List<AssessTask> findAllBySeasonId(Long seasonId);

    /**
     * 更加考核Id和状态获取评价任务
     *
     * @param seasonId
     * @return
     */
    List<AssessTask> findAllBySeasonIdAndStatus(@Param("seasonId") Long seasonId, @Param("status") Byte status);

    /**
     * 更新积分和状态
     *
     * @param status
     * @param score
     * @param id
     */
    void updateStatusAndScoreById(@Param("status") Byte status, @Param("score") BigDecimal score, @Param("id") Long id);

    long countAssessTask(@Param("assessorId") Long assessorId, @Param("status") Byte status);

    /**
     * 获取有效评价数量
     *
     * @param assessorId
     * @param status
     * @return
     */
    int countValidAssessTask(@Param("assessorId") Long assessorId, @Param("status") Byte status);

    /**
     * 根据状态和评论人查询 统计
     *
     * @param assessorId
     * @param status
     * @return
     */
    long countPageByAssessorIdAndStatus(@Param("assessorId") Long assessorId, @Param("status") Byte status);

    /**
     * 查询季度考核的数据
     *
     * @param assessorId
     * @param status
     * @return
     */
    long countSeasonPageByAssessorIdAndStatus(@Param("assessorId") Long assessorId, @Param("status") Byte status);

    /**
     * 查询季度考核的数据
     *
     * @param assessorId
     * @param status
     * @return
     */
    List<AssessTaskSeasonDTO> querySeasonPageByAssessorIdAndStatus(@Param("assessorId") Long assessorId, @Param("status") Byte status, @Param("start") int start, @Param("pageSize") int pageSize);


    /**
     * 根据状态和评论人查询
     *
     * @param assessorId
     * @param status
     * @param seasonIdList
     * @return
     */
    List<AssessTaskDTO> queryListByAssessorIdAndStatus(@Param("assessorId") Long assessorId, @Param("status") Byte status, @Param("seasonIdList") List<Long> seasonIdList);

    /**
     * 根据评价任务获取人员信息
     *
     * @param taskId
     * @return
     */
    UserInfoDTO getDetailForUserInfoByTaskId(Long taskId);


    List<UserInfoDTO> queryAssessTaskUserByStatusUnderway(Long seasonId);


    long countSubordinateAssessTaskList(@Param("seasonId") Long seasonId, @Param("deptIds") List<Long> deptIds, @Param("leaderId") Long leaderId, @Param("assessorRealName") String assessorRealName, @Param("userRealName") String userRealName);

    List<SubordinateAssessTaskDTO> querySubordinateAssessTaskList(@Param("seasonId") Long seasonId, @Param("deptIds") List<Long> deptIds, @Param("leaderId") Long leaderId, @Param("assessorRealName") String assessorRealName, @Param("userRealName") String userRealName, @Param("start") int start, @Param("size") int size);

    void insertList(List<AssessTask> list);

    void updateStatusByIdList(@Param("status") Byte status, @Param("idList") List<Long> idList);

    /**
     * 更加id集合查询任务
     *
     * @param idList
     * @return
     */
    List<AssessTask> findByIdIn(List<Long> idList);
}