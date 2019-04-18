package com.mobvista.okr.repository;

import com.mobvista.okr.domain.UserSeasonComment;
import com.mobvista.okr.repository.base.BaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 顾炜[GuWei]
 */
@Mapper
public interface UserSeasonCommentRepository extends BaseRepository<UserSeasonComment> {

//
//    Page<UserSeasonComment> findAllByUserSeasonIdExceptInvalidUser(Long userSeasonId, Pageable pageable);
//
//    Page<UserSeasonComment> findAllByUserIdAndAssessorId(Long userSeasonId, Long assessorId, Pageable pageable);

    /**
     * 根据用户考核id和评论人id查询
     *
     * @param userId
     * @param assessorId
     * @param start
     * @param pageSize
     * @return
     */
    List<UserSeasonComment> findAllByUserIdAndAssessorId(@Param("userId") Long userId, @Param("assessorId") Long assessorId, @Param("start") int start, @Param("pageSize") int pageSize);

    /**
     * 根据用户考核id和评论人id统计
     *
     * @param userId
     * @param assessorId
     * @return
     */
    int countByUserIdAndAssessorId(@Param("userId") Long userId, @Param("assessorId") Long assessorId);


}