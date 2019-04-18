package com.mobvista.okr.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mobvista.okr.constants.RedisConstants;
import com.mobvista.okr.domain.ScoreOption;
import com.mobvista.okr.dto.ScoreOptionDTO;
import com.mobvista.okr.repository.ScoreOptionRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ScoreOption.
 */
@Service
public class ScoreOptionService {

    @Resource
    private ScoreOptionRepository scoreOptionRepository;
    @Resource
    private RedisService redisService;

    /**
     * 获取所有评分项
     *
     * @return
     */
    public List<ScoreOptionDTO> getAllOptions() {
        String value = redisService.get(RedisConstants.SCORE_OPTION_ALL);
        if (StringUtils.isBlank(value)) {
            List<ScoreOption> optionList = scoreOptionRepository.findAllByType(null);
            List<ScoreOptionDTO> list = optionList.stream()
                    .map(option -> new ScoreOptionDTO(option))
                    .collect(Collectors.toList());
            redisService.set(RedisConstants.SCORE_OPTION_ALL, JSON.toJSONString(list), RedisConstants.EXPIRE_TIME_1H);
            return list;
        }
        return JSON.parseArray(value, ScoreOptionDTO.class);

    }


    /**
     * 获取所有枚举项map
     *
     * @return
     */
    public Map<Long, String> getAllOptionsMap() {
        String value = redisService.get(RedisConstants.SCORE_OPTION_ALL_NAME_MAP);
        if (StringUtils.isBlank(value)) {
            List<ScoreOption> optionList = scoreOptionRepository.findAllByType(null);
            Map<Long, String> map = optionList.stream().collect(Collectors.toMap(ScoreOption::getId, ScoreOption::getName));
            redisService.set(RedisConstants.SCORE_OPTION_ALL_NAME_MAP, JSON.toJSONString(map), RedisConstants.EXPIRE_TIME_1H);
            return map;
        }
        return JSON.parseObject(value, new TypeReference<Map<Long, String>>() {
        });

    }


    /**
     * 加载评分项配置
     *
     * @param optionType
     * @return
     */
    public List<ScoreOptionDTO> findListByType(Byte optionType) {
        List<ScoreOption> optionList = scoreOptionRepository.findAllByType(optionType);
        List<ScoreOptionDTO> optionListDTOs = optionList.stream()
                .map(option -> new ScoreOptionDTO(option))
                .collect(Collectors.toList());
        return optionListDTOs;
    }


    /**
     * 修改配置项评分
     *
     * @param id
     * @param weight
     */
    @Transactional
    public void updateWeight(Long id, Integer weight) {
        ScoreOption scoreOption = scoreOptionRepository.selectByPrimaryKey(id);
        scoreOption.setWeight(weight);
        scoreOptionRepository.updateByPrimaryKeySelective(scoreOption);
    }
}
