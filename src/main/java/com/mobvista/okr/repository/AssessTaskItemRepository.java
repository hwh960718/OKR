package com.mobvista.okr.repository;

import com.mobvista.okr.domain.AssessTaskItem;
import com.mobvista.okr.repository.base.BaseRepository;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 顾炜[GuWei]
 */
@Mapper
public interface AssessTaskItemRepository extends BaseRepository<AssessTaskItem> {
    /**
     * 根据待评任务id查询评分信息
     *
     * @param taskId
     * @return
     */
    List<AssessTaskItem> findAllByTaskId(Long taskId);

    /**
     * 根据待评任务id集合查询评分信息
     *
     * @param list
     * @return
     */
    List<AssessTaskItem> findAllByTaskIdList(List<Long> list);

    /**
     * 根据任务id删除
     *
     * @param taskId
     * @return
     */
    int deleteByTaskId(Long taskId);


    void insertList(List<AssessTaskItem> list);
}