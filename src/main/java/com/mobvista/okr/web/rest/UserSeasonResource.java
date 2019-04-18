package com.mobvista.okr.web.rest;

import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.enums.AssessStatus;
import com.mobvista.okr.exception.ServiceErrorException;
import com.mobvista.okr.service.UserSeasonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户考核接口
 */
@RestController
@RequestMapping("/api/u/userSeason")
public class UserSeasonResource {

    private final Logger log = LoggerFactory.getLogger(UserSeasonResource.class);

    @Resource
    private UserSeasonService userSeasonService;

    /**
     * 我的季度考核列表
     *
     * @param pageable
     * @param assessStatus
     * @return
     */
    @GetMapping("/list")
    public CommonResult myList(Pageable pageable, String assessStatus) {
        log.debug("REST request to myList userSeason: {}", pageable);
        return CommonResult.success(userSeasonService.myList(pageable, AssessStatus.getCodeByName(assessStatus)));
    }

    /**
     * 考评状态对应的数据是否存在
     *
     * @param assessStatus
     * @return
     */
    @GetMapping(value = "/assessStatusDataIsExist")
    public CommonResult assessStatusDataIsExist(String assessStatus) {
        log.debug("REST request to assessStatusDataIsExist  assessStatus : {}", assessStatus);
        try {
            return CommonResult.success(userSeasonService.assessStatusDataIsExist(AssessStatus.getCodeByName(assessStatus)));
        } catch (ServiceErrorException e) {
            return CommonResult.error(e.getMessage());
        }
    }

    /**
     * 我的季度考核详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/detail")
    public CommonResult detail(Long id) {
        log.debug("REST request to userSeason detail: {}", id);
        try {
            return CommonResult.success(userSeasonService.getDetail(id));
        } catch (ServiceErrorException e) {
            log.error("ServiceErrorException[我的季度考核详情]", e);
            return CommonResult.error(e.getMessage());
        }
    }

    /**
     * 获取个人信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getDetailForUserInfo")
    public CommonResult getDetailForUserInfo(Long id) {
        log.debug("REST request to getDetailForUserInfo id: {}", id);
        try {
            return CommonResult.success(userSeasonService.getDetailForUserInfo(id));
        } catch (ServiceErrorException e) {
            log.error("ServiceErrorException[获取个人信息]", e);
            return CommonResult.error(e.getMessage());
        }
    }

    /**
     * 获取季度评分
     *
     * @param id
     * @return
     */
    @GetMapping("/getDetailForUserScoreItem")
    public CommonResult getDetailForUserScoreItem(Long id) {
        log.debug("REST request to getDetailForUserScoreItem id: {}", id);
        try {
            return CommonResult.success(userSeasonService.getDetailForUserScoreItem(id));
        } catch (ServiceErrorException e) {
            log.error("ServiceErrorException[获取季度评分]", e);
            return CommonResult.error(e.getMessage());
        }
    }


    /**
     * 通过被评价者ID和季度ID获取该被评价者的该季度被评价明细
     *
     * @param id
     * @return
     */
    @GetMapping("/getDetailForAssessScoreList")
    public CommonResult getDetailForAssessScoreList(Long id) {
        try {
            return CommonResult.success(userSeasonService.getDetailForAssessScoreList(id));
        } catch (ServiceErrorException e) {
            log.error("ServiceErrorException[通过被评价者ID和季度ID获取该被评价者的该季度被评价明细]", e);
            return CommonResult.error(e.getMessage());
        }
    }

    /**
     * 获得评价详情中的各个指标的细则
     *
     * @param taskId
     * @return
     */
    @GetMapping("/getDetailForAssessScoreItems")
    public CommonResult getDetailForAssessScoreItems(@RequestParam Long taskId) {
        return CommonResult.success(userSeasonService.getDetailForAssessScoreItems(taskId));
    }

    /**
     * 获取最新季度个人能力值
     *
     * @return
     */
    @GetMapping("/getLastSeasonScoreItem")
    public CommonResult getLastSeasonScoreItem() {
        log.debug("REST request to getLastSeasonScoreItem");
        return CommonResult.success(userSeasonService.getLastSeasonScoreItem());
    }

    /**
     * 获取季度排行榜
     *
     * @param seasonId
     * @param optionId
     * @param assessResult
     * @param pageable
     * @return
     */
    @GetMapping("/getRanking")
    public CommonResult getRanking(@RequestParam Long seasonId, @RequestParam(required = false) Long optionId, @RequestParam(required = false) Integer assessResult, Pageable pageable) {
        return CommonResult.success(userSeasonService.getRanking(seasonId, optionId, assessResult, pageable));
    }

    /**
     * 获取公司OKR概览
     *
     * @param seasonId
     * @param departmentId
     * @param pageable
     * @return
     */
    @GetMapping("getCompanyOkrOverview")
    public CommonResult getCompanyOkrOverview(@RequestParam Long seasonId, @RequestParam Long departmentId, Pageable pageable) {
        log.debug("REST request to getCompanyOkrOverview : {}, {}, {}", seasonId, departmentId, pageable);
        return CommonResult.success(userSeasonService.getCompanyOkrOverview(seasonId, departmentId, pageable));
    }

    /**
     * 获取用户考核视图
     *
     * @param isFirst
     * @return
     * @version v3.0
     */
    @GetMapping("queryUserSeasonView")
    public CommonResult queryUserSeasonView(boolean isFirst) {
        return userSeasonService.queryUserSeasonViews(isFirst);
    }

    /**
     * 查询当前用户考核信息
     *
     * @return
     * @version v3.0
     */
    @GetMapping("queryCurrentUserSeasonInfo")
    public CommonResult queryCurrentUserSeasonInfo() {
        return userSeasonService.queryCurrentUserSeasonInfo();
    }

    /**
     * 查询最近用户考核排名
     *
     * @return
     * @version v3.0
     */
    @GetMapping("queryLastUserRankingInfo")
    public CommonResult queryLastUserRankingInfo() {
        return userSeasonService.queryLastUserRankingInfo();
    }

    /**
     * 查询用户考核详情 综合评分和用户评分
     *
     * @return
     * @version v3.0
     */
    @GetMapping("queryUserSeasonItemDetail")
    public CommonResult queryUserSeasonItemDetail(Long userSeasonId) {
        return userSeasonService.queryUserSeasonItemDetail(userSeasonId);
    }

    /**
     * 获取已经结束的最近一次季度考核的userSeason数据
     *
     * @param userId
     * @return
     */
    @GetMapping("queryLastOverUserSeason")
    public CommonResult queryLastOverUserSeason(Long userId) {
        return userSeasonService.queryLastOverUserSeason(userId);
    }
}
