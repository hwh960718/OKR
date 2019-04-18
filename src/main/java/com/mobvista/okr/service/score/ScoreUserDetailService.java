package com.mobvista.okr.service.score;

import com.google.common.collect.Lists;
import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.domain.ScoreUserDetail;
import com.mobvista.okr.dto.score.ScoreUserDetailDTO;
import com.mobvista.okr.repository.ScoreUserDetailRepository;
import com.mobvista.okr.util.AwsPictureProcessUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户积分明细
 *
 * @author Weier Gu (顾炜)
 * @date 2018/7/23 17:19
 */
@Service
public class ScoreUserDetailService {

    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(ScoreUserDetailService.class);

    @Resource
    private ScoreUserDetailRepository scoreUserDetailRepository;

    /**
     * 查询用户积分明细
     *
     * @param userId
     * @param pageable
     * @return
     */
    public CommonResult queryScoreUserDetail(Long userId, Pageable pageable) {
        if (null == userId || userId <= 0) {
            log.error("查询用户积分明细----入参为空----{}", userId);
            return CommonResult.error("入参为空");
        }
        ScoreUserDetail query = new ScoreUserDetail();
        query.setUserId(userId);
        long count = scoreUserDetailRepository.countPageByScoreUserDetail(query);
        List<ScoreUserDetailDTO> poList = Lists.newArrayList();
        if (count > 0) {
            poList = scoreUserDetailRepository.queryPageByScoreUserDetail(query, pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
            AwsPictureProcessUtil.assembleScoreUserDetailDTO(poList);
        }
        Page<ScoreUserDetailDTO> page = new PageImpl<>(poList, pageable, count);
        return CommonResult.success(page);
    }


}
