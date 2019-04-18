package com.mobvista.okr.repository;

import com.mobvista.okr.domain.UserRank;
import com.mobvista.okr.repository.base.BaseRepository;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 顾炜[GuWei]
 */
@Mapper
public interface UserRankRepository extends BaseRepository<UserRank> {

    List<UserRank> findAll();

}