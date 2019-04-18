package com.mobvista.okr.repository.base;

/**
 * @author 顾炜(GUWEI)
 * @date 2018/5/25 14:21
 */
public interface BaseRepository<T> {

    int deleteByPrimaryKey(Long id);

    int insert(T record);

    int insertSelective(T record);

    T selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);
}
