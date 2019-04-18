package com.mobvista.okr.repository;

import com.mobvista.okr.domain.ScoreUser;
import com.mobvista.okr.dto.score.ScoreUserDTO;
import com.mobvista.okr.repository.base.BaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Weier Gu (顾炜)
 */
@Mapper
public interface ScoreUserRepository extends BaseRepository<ScoreUser> {

    /**
     * 根据名称查询分页总数
     *
     * @param name
     * @return
     */
    long countPageByNameLike(@Param("name") String name, @Param("departIdList") List<Long> departIdList);

    /**
     * 更加名字分页查询
     *
     * @param name
     * @param start
     * @param size
     * @return
     */
    List<ScoreUserDTO> queryPageByNameLike(@Param("name") String name, @Param("departIdList") List<Long> departIdList, @Param("start") int start, @Param("size") int size);

    /**
     * 批量查询用户积分
     *
     * @param list
     */
    void insertList(List<ScoreUser> list);

    /**
     * 获取所有用户Id集合
     *
     * @return
     */
    List<Long> getAllUserId();


    /**
     * 批量更新用户积分数据
     *
     * @param list
     */
    void updateUserScoreList(List<ScoreUser> list);

    /**
     * 更新用户可用积分至消减积分
     *
     * @param userId
     * @param score
     * @return
     */
    int updateUserValidScore2AbatementScore(@Param("userId") Long userId, @Param("score") int score);


    /**
     * 批量修改用户 减可用积分 加消减积分，减锁定积分
     *
     * @param list
     * @return
     */
    int updateUserValidAbatementUnLockScoreList(List<ScoreUserDTO> list);

    /**
     * 批量修改用户锁定积分
     *
     * @param list
     * @return
     */
    int updateUserLockScoreList(List<ScoreUserDTO> list);
}