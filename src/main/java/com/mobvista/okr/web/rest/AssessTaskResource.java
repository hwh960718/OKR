package com.mobvista.okr.web.rest;

import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.dto.AssessTaskSeasonDTO;
import com.mobvista.okr.dto.SubordinateAssessTaskDTO;
import com.mobvista.okr.enums.AssessTaskStatus;
import com.mobvista.okr.exception.ServiceErrorException;
import com.mobvista.okr.service.AssessTaskService;
import com.mobvista.okr.vm.MakeScoreVM;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/u/assessTask")
public class AssessTaskResource {

    private final Logger log = LoggerFactory.getLogger(AssessTaskResource.class);

    @Resource
    private AssessTaskService assessTaskService;

    /**
     * 我的待评价列表 待评价
     *
     * @param pageable
     * @return
     * @version v3.0
     */
    @GetMapping("/underway")
    public CommonResult myAssessTaskUnderwayList(Pageable pageable) {
        log.debug("REST request to myAssessTaskUnderwayList: {}", pageable);
        Page<AssessTaskSeasonDTO> page = assessTaskService.myAssessTaskSeasonList(pageable, AssessTaskStatus.UNDERWAY.getCode());
        return CommonResult.success(page);
    }

    /**
     * 我的待评价列表 待评价数量
     *
     * @param pageable
     * @return
     */
    @GetMapping("/underwayCount")
    public CommonResult myAssessTaskUnderwayCount(Pageable pageable) {
        log.debug("REST request to myAssessTaskUnderwayCount: {}", pageable);
        return CommonResult.success(assessTaskService.myAssessTaskCount(AssessTaskStatus.UNDERWAY.getCode()));
    }

    /**
     * 我的待评价列表 全部
     *
     * @param pageable
     * @return
     * @version v3.0
     */
    @GetMapping("/all")
    public CommonResult myAssessTaskFinishedList(Pageable pageable) {
        log.debug("REST request to myAssessTaskFinishedList: {}", pageable);
        Page<AssessTaskSeasonDTO> page = assessTaskService.myAssessTaskSeasonList(pageable, null);
        return CommonResult.success(page);
    }

//    @ApiOperation(value = "我的待评价列表 未评价")
//    @GetMapping("/notAssess")
//    public CommonResult myAssessTaskNotAssessList(Pageable pageable) {
//        log.debug("REST request to myAssessTaskFinishedList: {}", pageable);
//        Page<AssessTaskSeasonDTO> page = assessTaskService.myAssessTaskSeasonList(pageable, AssessTaskStatus.NOT_ASSESS.getCode());
//        return CommonResult.success(page);
//    }
//
//    @ApiOperation(value = "我的待评价列表 无效评价")
//    @GetMapping("/invalidAssess")
//    public CommonResult myAssessTaskInvalidAssessList(Pageable pageable) {
//        log.debug("REST request to myAssessTaskFinishedList: {}", pageable);
//        Page<AssessTaskSeasonDTO> page = assessTaskService.myAssessTaskSeasonList(pageable, AssessTaskStatus.INVALID_ASSESS.getCode());
//        return CommonResult.success(page);
//    }

    @ApiOperation(value = "获取个人信息")
    @GetMapping("/getDetailForUserInfo")
    public CommonResult getDetailForUserInfo(Long id) {
        log.debug("REST request to getDetailForUserInfo id: {}", id);
        return CommonResult.success(assessTaskService.getDetailForUserInfo(id));
    }

    @ApiOperation(value = "获取季度目标")
    @GetMapping("/getDetailForUserSeason")
    public CommonResult getDetailForUserSeason(Long id) {
        log.debug("REST request to getDetailForUserSeason id: {}", id);
        try {
            return CommonResult.success(assessTaskService.getDetailForUserSeason(id));
        } catch (ServiceErrorException e) {
            log.error("ServiceErrorException[获取季度目标]", e);
            return CommonResult.error(e.getMessage());
        }
    }

    @ApiOperation(value = "获取季度评分")
    @GetMapping("/getDetailForUserScore")
    public CommonResult getDetailForUserScore(Long id) {
        log.debug("REST request to getDetailForUserScore id: {}", id);
        try {
            return CommonResult.success(assessTaskService.getDetailForUserScore(id));
        } catch (ServiceErrorException e) {
            log.error("ServiceErrorException[获取季度评分]", e);
            return CommonResult.error(e.getMessage());
        }
    }

    /**
     * 获取任务xy折线图
     *
     * @param taskId
     * @return
     * @version v3.0
     */
    @GetMapping("/queryTaskXYAxisVM")
    public CommonResult queryTaskXYAxisVM(Long taskId) {
        log.debug("REST request to getDetailForUserScore taskId: {}", taskId);
        try {
            return CommonResult.success(assessTaskService.queryTaskXYAxisVM(taskId));
        } catch (ServiceErrorException e) {
            log.error("ServiceErrorException[获取季度评分]", e);
            return CommonResult.error(e.getMessage());
        }
    }

//    @ApiOperation(value = "获取用户所有的季度评分")
//    @GetMapping("/getUserAllItemScore")
//    public CommonResult getUserAllItemScore(@RequestParam Long userSeasonId) {
//        log.debug("REST request to getUserAllItemScore userSeasonId: {} ", userSeasonId);
//        return CommonResult.success(assessTaskService.getUserAllItemScore(userSeasonId));
//    }

    @ApiOperation(value = "评分")
    @PostMapping(value = "/makeScore")
    public CommonResult makeScore(@RequestBody @Valid MakeScoreVM vm) {
        log.debug("REST request to make score: {}", vm);
        try {
            return assessTaskService.makeScore(vm);
        } catch (ServiceErrorException e) {
            log.error("ServiceErrorException[评分]", e);
            return CommonResult.error(e.getMessage());
        }
    }

    @ApiOperation(value = "下属评分任务列表")
    @GetMapping("/subordinateAssessTaskList")
    public CommonResult subordinateAssessTaskList(@RequestParam Long seasonId, String assessorRealName, String userRealName, Pageable pageable) {
        log.debug("REST request to subordinateAssessTaskList: {}", pageable);
        try {
            SubordinateAssessTaskDTO dto = new SubordinateAssessTaskDTO();
            dto.setSeasonId(seasonId);
            dto.setAssessorRealName(StringUtils.trim(assessorRealName));
            dto.setUserRealName(StringUtils.trim(userRealName));
            return CommonResult.success(assessTaskService.subordinateAssessTaskList(dto, pageable));
        } catch (ServiceErrorException e) {
            log.error("ServiceErrorException[下属评分任务列表]", e);
            return CommonResult.error(e.getMessage());
        }

    }
}
