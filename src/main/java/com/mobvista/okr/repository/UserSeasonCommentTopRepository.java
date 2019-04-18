package com.mobvista.okr.repository;

import com.mobvista.okr.domain.UserSeasonCommentTop;
import com.mobvista.okr.repository.base.BaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 顾炜[GuWei]
 */
@Mapper
public interface UserSeasonCommentTopRepository extends BaseRepository<UserSeasonCommentTop> {
    /**
     * 根据用户id和评论id 统计评论点赞数量
     *
     * @param userId
     * @param commentId
     * @return
     */
    long countByUserIdAndCommentId(@Param("userId") Long userId, @Param("commentId") Long commentId);

    /**
     * 更加用户id和评论id集合查询点赞信息
     *
     * @param userId
     * @param commentIds
     * @return
     */
    List<UserSeasonCommentTop> findAllByUserIdAndCommentIdIn(@Param("userId") Long userId, @Param("commentIds") List<Long> commentIds);
}