package com.mobvista.okr.repository;

import com.mobvista.okr.domain.Tip;
import com.mobvista.okr.repository.base.BaseRepository;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 顾炜[GuWei]
 */
@Mapper
public interface TipRepository extends BaseRepository<Tip> {

    /**
     * 根据标签名称查询标签信息
     *
     * @param title
     * @return
     */
    Tip findByTitle(String title);


    /**
     * 根据标签模糊查询
     *
     * @param title
     * @return
     */
    List<Tip> queryLikeTitle(String title);
}