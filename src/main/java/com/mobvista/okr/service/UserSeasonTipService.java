package com.mobvista.okr.service;

import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.domain.Tip;
import com.mobvista.okr.domain.UserSeasonTip;
import com.mobvista.okr.dto.UserTipDTO;
import com.mobvista.okr.repository.TipRepository;
import com.mobvista.okr.repository.UserSeasonTipRepository;
import com.mobvista.okr.security.SecurityUtils;
import com.mobvista.okr.vm.UserSeasonTipVM;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Service class for managing UserSeasonTip.
 *
 * @author 顾炜[GuWei]
 */
@Service
public class UserSeasonTipService {

    private final Logger log = LoggerFactory.getLogger(UserSeasonTipService.class);

    @Resource
    private UserSeasonTipRepository userSeasonTipRepository;

    @Resource
    private TipRepository tipRepository;

    /**
     * 打标签
     *
     * @param userSeasonTipVM
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult create(UserSeasonTipVM userSeasonTipVM) {
        Long userId = SecurityUtils.getCurrentUserId();
        Long tipUserId = userSeasonTipVM.getUserId();
        if (userId.equals(tipUserId)) {
            return CommonResult.error("不要太自恋哦!");
        }

        String title = userSeasonTipVM.getTitle();
        Tip tip = tipRepository.findByTitle(title);
        if (null == tip) {
            tip = new Tip();
            tip.setTitle(title);
            tip.setCreateTime(new Date());
            tip.setUserId(userId);
            tipRepository.insert(tip);
        } else {
            long count = userSeasonTipRepository.countByUserIdAndTipIdAndUserId(tipUserId, tip.getId(), SecurityUtils.getCurrentUserId());
            if (count > 0) {
                return CommonResult.error("你已经给打过该标签了!");
            }
        }
        UserSeasonTip userSeasonTip = new UserSeasonTip();
        userSeasonTip.setColor(userSeasonTipVM.getColor());
        userSeasonTip.setAssessorId(userId);
        userSeasonTip.setUserId(tipUserId);
        userSeasonTip.setTipId(tip.getId());
        userSeasonTip.setCreatedDate(new Date());
        userSeasonTipRepository.insert(userSeasonTip);
        return CommonResult.success();
    }


    /**
     * 获取季度标签
     *
     * @param userId
     * @return
     */
    public List<UserTipDTO> getListByUserId(Long userId) {
        return userSeasonTipRepository.findAllByUserId(userId);
    }


    /**
     * 删除标签
     *
     * @param title
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult deleteTitle(Long userId, String title) {
        if (StringUtils.isBlank(title)) {
            return CommonResult.error("需要删除的标签不能为空");
        }

        Tip tip = tipRepository.findByTitle(title);
        if (null == tip) {
            return CommonResult.error("需要删除的标签不存在");
        }
        long count = userSeasonTipRepository.delByTipIdAndAssessorId(tip.getId(), SecurityUtils.getCurrentUserId(), userId);
        if (count > 0) {
            return CommonResult.success();
        }
        return CommonResult.error("不能删除他人的标签");
    }
}
