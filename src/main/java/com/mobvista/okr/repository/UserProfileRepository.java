package com.mobvista.okr.repository;

import com.mobvista.okr.domain.UserProfile;
import com.mobvista.okr.dto.SeasonUserDTO;
import com.mobvista.okr.dto.UserInfoDTO;
import com.mobvista.okr.repository.base.BaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 顾炜[GuWei]
 */
@Mapper
public interface UserProfileRepository extends BaseRepository<UserProfile> {


    /**
     * 查询所有用户
     *
     * @return
     */
    List<UserProfile> findAll();

    /**
     * 根据id集合查询用户信息
     *
     * @param idList
     * @return
     */
    List<UserProfile> findAllByIdIn(List<Long> idList);

    List<UserInfoDTO> findUserDetailByIdIn(List<Long> idList);

//    UserProfile findOneByEmail(String email);
//
//    List<UserProfile> findAllByRealNameOrderById(String realName);

    /**
     * 根据部门id获取用户信息
     *
     * @param deptIds
     * @return
     */
    List<UserProfile> findAllByDepartmentIdIn(List<Long> deptIds);

    /**
     * 根据部门id获取用户信息 排除无效离职用户
     *
     * @param deptIds
     * @return
     */
    List<Long> findAllByDepartmentIdInExceptInvalidUser(List<Long> deptIds);


    /**
     * 查询已参与考核的用户
     *
     * @param seasonId
     * @return
     */
    List<Long> findAllBySeasonId(Long seasonId);


    long countAllByName(@Param("name") String name, @Param("depIds") List<Long> depIds);

    List<UserInfoDTO> queryAllByName(@Param("name") String name, @Param("start") int start, @Param("size") int size, @Param("depIds") List<Long> depIds);

    /**
     * 根据用户名模糊统计，排除失效的用户
     *
     * @param name
     * @param seasonId
     * @param userId
     * @param depIds
     * @return
     */
    long countAllByAdjusterUser(@Param("name") String name, @Param("seasonId") Long seasonId, @Param("userId") Long userId, @Param("depIds") List<Long> depIds);

    /**
     * 根据用户名模糊查询，排除失效的用户
     *
     * @param name
     * @param seasonId
     * @param userId
     * @param depIds
     * @param start
     * @param size
     * @return
     */
    List<UserInfoDTO> queryAllByAdjusterUser(@Param("name") String name, @Param("seasonId") Long seasonId, @Param("userId") Long userId, @Param("depIds") List<Long> depIds, @Param("start") int start, @Param("size") int size);

    /**
     * 批量插入
     *
     * @param list
     */
    void saveList(List<UserProfile> list);

    /**
     * 批量更新
     *
     * @param list
     */
    void updateList(List<UserProfile> list);

    /**
     * 批量修改用户图片
     *
     * @param list
     */
    void updateListPicture(List<UserProfile> list);


    /**
     * 根据用户名和用户考核id查询对应的数量
     *
     * @param name
     * @param seasonId
     * @return
     */
    int countAllByUserNameAndSeasonId(@Param("name") String name, @Param("seasonId") Long seasonId);


    /**
     * 根据用户名查询用户考核对应的用户信息
     *
     * @param name
     * @param seasonId
     * @param start
     * @param size
     * @return
     */
    List<SeasonUserDTO> queryAllByUserNameAndSeasonId(@Param("name") String name, @Param("seasonId") Long seasonId, @Param("start") int start, @Param("size") int size);

}