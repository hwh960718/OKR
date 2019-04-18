package com.mobvista.okr.repository;

import com.mobvista.okr.dto.StatisticsUserSeasonCountDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 数据统计
 *
 * @author 顾炜(GUWEI)
 * @date 2018/5/28 18:11
 */
@Mapper
public interface StatisticsUserSeasonRepository {


    List<StatisticsUserSeasonCountDTO> getUserAssessCountTop10(Long seasonId);
}
