package com.mobvista.okr.repository;

import com.mobvista.okr.domain.Department;
import com.mobvista.okr.repository.base.BaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 顾炜[GuWei]
 */
@Mapper
public interface DepartmentRepository extends BaseRepository<Department> {


    /**
     * 根据部门等级和名称列表查询部门信息
     *
     * @param level
     * @param nameList
     * @return
     */
    List<Department> findAllByLevelAndNameIn(@Param("level") Integer level, @Param("nameList") List<String> nameList);

    /**
     * 根据父级部门id查询部门信息
     *
     * @param parentIds
     * @return
     */
    List<Department> findAllByParentIdIn(List<Long> parentIds);

    /**
     * 根据领导code查询部门负责人
     *
     * @param leaderCode
     * @return
     */
    List<Department> findAllByLeaderCode(Long leaderCode);


    /**
     * 获取所有部门数据
     *
     * @return
     */
    List<Department> findAll();

    /**
     * 批量插入
     *
     * @param list
     */
    void insertList(List<Department> list);

    /**
     * 批量更新
     *
     * @param list
     */
    void updateList(List<Department> list);

}