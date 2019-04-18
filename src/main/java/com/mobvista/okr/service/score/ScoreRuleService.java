package com.mobvista.okr.service.score;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.constants.CommonConstants;
import com.mobvista.okr.domain.ScoreRule;
import com.mobvista.okr.dto.score.ScoreRuleDTO;
import com.mobvista.okr.enums.score.ScoreRuleStatusEnum;
import com.mobvista.okr.repository.ScoreRuleRepository;
import com.mobvista.okr.security.SecurityUtils;
import com.mobvista.okr.util.DateUtil;
import com.mobvista.okr.vm.ScoreRuleVM;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 积分规则
 *
 * @author Weier Gu (顾炜)
 * @date 2018/7/23 10:56
 */
@Service
public class ScoreRuleService {
    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(ScoreRuleService.class);

    @Resource
    private ScoreRuleRepository scoreRuleRepository;

    /**
     * 根据条件分页查询积分规则信息
     *
     * @param scoreRuleVM
     * @return
     */
    public CommonResult<Page<ScoreRuleDTO>> queryScoreRule(ScoreRuleVM scoreRuleVM, Pageable pageable) {
        ScoreRule scoreRule = convertVm2Po(scoreRuleVM);
        long count = scoreRuleRepository.countPageByScoreRule(scoreRule);
        List<ScoreRuleDTO> list = Lists.newArrayList();
        if (count > 0) {
            list = scoreRuleRepository.queryPageByScoreRule(scoreRule, pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
        }
        Page<ScoreRuleDTO> page = new PageImpl<>(list, pageable, count);
        return CommonResult.success(page);
    }


    /**
     * 获取对应的积分规则
     *
     * @param triggerMode
     * @return
     */
    public CommonResult queryVaildScoreRule(Byte triggerMode) {
        List<ScoreRuleDTO> ruleDTOList = scoreRuleRepository.queryScoreRuleListByTriggerModeAndStatus(triggerMode, ScoreRuleStatusEnum.Valid.getCode());
        return CommonResult.success(ruleDTOList);
    }


    /**
     * 保存积分规则
     *
     * @param scoreRuleVM
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)

    public CommonResult saveScoreRule(ScoreRuleVM scoreRuleVM) {
        try {
            if (null == scoreRuleVM) {
                return CommonResult.error("入参不能为空");
            } else if (null == scoreRuleVM.getScore()) {
                return CommonResult.error("积分不能为空");
            } else if (StringUtils.isBlank(scoreRuleVM.getRuleName())) {
                return CommonResult.error("积分规则标题不能为空");
            }

            ScoreRule scoreRule = convertVm2Po(scoreRuleVM);
            //新建时默认将状态设置为无效
            scoreRule.setStatus(ScoreRuleStatusEnum.Invalid.getCode());
            scoreRuleRepository.insert(scoreRule);
            return CommonResult.success();
        } catch (Exception e) {
            log.error("保存积分规则----异常", e);
            return CommonResult.error("新增规则失败");
        }
    }


    /**
     * 查询相同的触发方式对应的规则
     *
     * @param id
     * @return
     */
    public CommonResult querySameTriggerModeScoreRule(Long id) {
        if (null == id || id <= 0) {
            return CommonResult.error("入参ID为空");
        }
        ScoreRule scoreRule = scoreRuleRepository.selectByPrimaryKey(id);
        if (null == scoreRule) {
            log.error("修改积分规则----数据异常----{}对应的积分规则不存在", id);
            return CommonResult.error("规则信息不存在");
        }
        List<ScoreRule> list = scoreRuleRepository.findByTriggerModelAndStatus(scoreRule.getTriggerMode(), ScoreRuleStatusEnum.Valid.getCode());
        return CommonResult.success(list);

    }

    /**
     * 修改积分对应专题
     *
     * @param id
     * @param status 若是启用，判断当前是否存在通标签的规则，提示
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult updateScoreRuleStatus(Long id, Byte status) {
        if (null == id || id <= 0) {
            log.error("修改积分规则----入参异常----{}", JSON.toJSONString(id));
            return CommonResult.error("入参异常");
        }
        //校验状态的合法性
        if (!ScoreRuleStatusEnum.codeExists(status)) {
            return CommonResult.error("需要修改的状态错误");
        }
        ScoreRule scoreRule = scoreRuleRepository.selectByPrimaryKey(id);
        if (null == scoreRule) {
            log.error("修改积分规则----数据异常----{}对应的积分规则不存在", id);
            return CommonResult.error("规则信息不存在");
        }
        scoreRule.setStatus(status);
        //允许更新的内容
        scoreRuleRepository.updateByPrimaryKeySelective(scoreRule);
        return CommonResult.success();
    }


    /**
     * 过期积分规则
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void expiredScoreRule() {
        scoreRuleRepository.updateExpiredRuleStatus(new Date(), ScoreRuleStatusEnum.Valid.getCode(), ScoreRuleStatusEnum.Invalid.getCode());
    }

    /**
     * 启用积分规则
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void enableScoreRule() {
        scoreRuleRepository.updateExpiredRuleStatus(new Date(), ScoreRuleStatusEnum.Invalid.getCode(), ScoreRuleStatusEnum.Valid.getCode());
    }

    private ScoreRule convertVm2Po(ScoreRuleVM vm) {
        ScoreRule po = new ScoreRule();
        po.setRuleDesc(vm.getRuleDesc());
        po.setRuleName(vm.getRuleName());
        po.setScore(vm.getScore());
        po.setTriggerMode(vm.getTriggerMode());
        Long currentUserId = SecurityUtils.getCurrentUserId();
        po.setCreateUserId(currentUserId);
        po.setModifyUserId(currentUserId);
        Date date = new Date();
        po.setCreateDate(date);
        po.setModifyDate(date);
        po.setType(vm.getType());
        po.setStatus(vm.getStatus());
        po.setValidDateStart(vm.getValidDateStart());
        po.setValidDateEnd(vm.getValidDateEnd());
        if (vm.getValidType() > 0) {
            po.setValidDateStart(DateUtil.setDateStart(date));
            po.setValidDateEnd(DateUtil.setDateEnd(date, CommonConstants.NUM_100));
        }

        return po;
    }


}
