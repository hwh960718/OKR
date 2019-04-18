package com.mobvista.okr.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;
import com.mobvista.okr.constants.RedisConstants;
import com.mobvista.okr.domain.UserRank;
import com.mobvista.okr.dto.UserRankDTO;
import com.mobvista.okr.repository.UserRankRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 用户职级
 *
 * @author 顾炜(GUWEI)
 * @date 2018/5/15 13:56
 */
@Service
public class UserRankService {

    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(UserRankService.class);

    @Resource
    private UserRankRepository userRankRepository;

    @Resource
    private RedisService redisService;

    /**
     * 获取用户职级
     *
     * @return
     */
    public List<UserRank> getUserRankInfo() {
        return userRankRepository.findAll();
    }

    /**
     * 修改用户权重
     *
     * @param userRankId
     * @param weight
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean updateUserRank(Long userRankId, Integer weight) {
        boolean bool = false;
        UserRank userRank = userRankRepository.selectByPrimaryKey(userRankId);
        if (null != userRank) {
            userRank.setWeight(weight);
            userRankRepository.updateByPrimaryKeySelective(userRank);
            bool = true;
        }
        return bool;
    }


//    public List<UserRankDTO> getUserRankListByUserIdList(List<Long> userIdList) {
//        return userRankRepository.getUserRankListByUserIdList(userIdList);
//    }


    public Map<String, Integer> getUserRankMapByRedis() {
        String value = redisService.get(RedisConstants.USER_RANK_WEIGHT_KEY);
        if (StringUtils.isNotBlank(value)) {
            return JSON.parseObject(value, new TypeReference<Map<String, Integer>>() {
            });
        }
        List<UserRank> list = getUserRankInfo();
        Map<String, Integer> map = Maps.newHashMap();
        for (UserRank ur : list) {
            map.put(ur.getRank().toUpperCase(), ur.getRankNo());
        }
        redisService.set(RedisConstants.USER_RANK_WEIGHT_KEY, JSON.toJSONString(map), RedisConstants.EXPIRE_TIME_1H);
        return map;
    }


    private UserRankDTO convertPo2Dto(UserRank po) {
        UserRankDTO dto = new UserRankDTO();
        dto.setRank(po.getRank());
        dto.setAscription(po.getAscription());
        dto.setRankNo(po.getRankNo());
        dto.setWeight(po.getWeight());
        return dto;
    }


}
