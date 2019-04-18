package com.mobvista.okr.repository;

import com.mobvista.okr.domain.UserReportDetail;
import com.mobvista.okr.dto.UserReportDetailDTO;
import com.mobvista.okr.repository.base.BaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户举报明细
 *
 * @author 顾炜(GUWEI)
 * @date 2018/5/30 14:42
 */
@Mapper
public interface UserReportDetailRepository extends BaseRepository<UserReportDetail> {


    /**
     * 批量插入
     *
     * @param list
     */
    void saveList(List<UserReportDetail> list);

    /**
     * 根据考评任务id和举报人id查询举报信息
     *
     * @param assessTaskId
     * @param reportUserId
     * @return
     */
    UserReportDetail findByAssessTaskIdAndReportUserId(@Param("assessTaskId") Long assessTaskId, @Param("reportUserId") Long reportUserId);


    /**
     * 更加状态查询举报信息
     *
     * @param status
     * @return
     */
    List<UserReportDetail> findAllByStatus(byte status);

    /**
     * 根据考评任务id集合查询举报信息
     *
     * @param assessTaskIdList
     * @return
     */
    List<UserReportDetail> findAllByAssessTaskIdIn(List<Long> assessTaskIdList);


    long queryCountPage(@Param("userName") String userName);

    List<UserReportDetailDTO> queryPage(@Param("userName") String userName, @Param("start") int start, @Param("size") int size);


    List<UserReportDetailDTO> queryByIdListIn(List<Long> list);


    /**
     * 根据id list集合更新数据状态
     *
     * @param status
     * @param list
     */
    void updateUserReportDetailStatusByIds(@Param("status") byte status, @Param("list") List<Long> list);
}
