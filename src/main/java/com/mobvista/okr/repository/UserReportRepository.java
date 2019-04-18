package com.mobvista.okr.repository;

import com.mobvista.okr.domain.UserReport;
import com.mobvista.okr.dto.UserReportDTO;
import com.mobvista.okr.repository.base.BaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户举报
 *
 * @author 顾炜(GUWEI)
 * @date 2018/5/30 14:42
 */
@Mapper
public interface UserReportRepository extends BaseRepository<UserReport> {

    /**
     * 根据用户id查询举报信息
     *
     * @param userId
     * @return
     */
    UserReport findByUserId(Long userId);

    /**
     * 更新用户考核被举报数
     *
     * @param id
     */
    void updateUserReportCount(Long id);

    /**
     * 更新用户考评数量
     *
     * @param id
     */
    void updateAssessCount(Long id);

    /**
     * 更新用户选择评价数
     *
     * @param id
     */
    void updateAdjusterCount(@Param("id") Long id, @Param("num") int num);


    /**
     * 查询评价数
     *
     * @param size
     * @return
     */
    List<UserReportDTO> queryAssessCountInfo(int size);

    /**
     * 查询选择评价数
     *
     * @param size
     * @return
     */
    List<UserReportDTO> queryAdjusterCountInfo(int size);

}
