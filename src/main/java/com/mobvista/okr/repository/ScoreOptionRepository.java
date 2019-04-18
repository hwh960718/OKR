package com.mobvista.okr.repository;

import com.mobvista.okr.domain.ScoreOption;
import com.mobvista.okr.repository.base.BaseRepository;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 顾炜[GuWei]
 */
@Mapper
public interface ScoreOptionRepository extends BaseRepository<ScoreOption> {

    /**
     * 根据type查询评分选项
     *
     * @param type 若设置为null查询全部
     * @return
     */
    List<ScoreOption> findAllByType(Byte type);


}