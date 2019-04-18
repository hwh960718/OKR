package com.mobvista.okr.web.rest;

import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.dto.UserInfoDTO;
import com.mobvista.okr.exception.ServiceErrorException;
import com.mobvista.okr.service.AdjusterService;
import com.mobvista.okr.vm.SelectedUsersVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/u/adjuster")
public class AdjusterResource {

    private final Logger log = LoggerFactory.getLogger(AdjusterResource.class);

    @Resource
    private AdjusterService adjusterService;

    /**
     * 创建考评对象
     *
     * @param selectedUsersVM
     * @return
     */
    @PostMapping("/create")
    public CommonResult create(@Valid @RequestBody SelectedUsersVM selectedUsersVM) {
        log.debug("REST request to create adjuster: {}", selectedUsersVM);
        try {
            return adjusterService.create(selectedUsersVM);
        } catch (ServiceErrorException e) {
            log.error("ServiceErrorException[创建考评对象]", e);
            return CommonResult.error(e.getMessage());
        }
    }

    /**
     * 移除考评对象
     *
     * @param selectedUserId
     * @param userSeasonId
     * @return
     */
    @GetMapping("/delete")
    public CommonResult remove(@RequestParam Long selectedUserId, @RequestParam Long userSeasonId) {
        log.debug("REST request to remove adjuster");
        try {
            adjusterService.remove(selectedUserId, userSeasonId);
            return CommonResult.success();
        } catch (ServiceErrorException e) {
            log.error("ServiceErrorException[移除考评对象]", e);
            return CommonResult.error(e.getMessage());
        }
    }

    /**
     * 获取用户考评统计
     *
     * @param userSeasonId
     * @return
     */
    @GetMapping("/getAdjustCountInfo")
    public CommonResult getAdjustCountInfo(@RequestParam Long userSeasonId) {
        log.info("获取用户考评统计 ... userSeasonId = {}", userSeasonId);
        try {
            return CommonResult.success(adjusterService.getUserAdjustInfoByUserSeasonId(userSeasonId));
        } catch (ServiceErrorException e) {
            return CommonResult.error(e.getMessage());
        }
    }

    /**
     * 获取考评对象列表
     *
     * @param userSeasonId
     * @return
     */
    @GetMapping("/list")
    public CommonResult list(Long userSeasonId) {
        log.debug("REST request to list adjuster");
        List<UserInfoDTO> list = adjusterService.getSelectedUsers(userSeasonId, null);
        return CommonResult.success(list);
    }

    /**
     * 获取考评对象列表
     *
     * @return
     */
    @GetMapping("/getAllSelectedUsers")
    public CommonResult getAllSelectedUsers() {
        log.debug("REST request to list adjuster");
        List<UserInfoDTO> list = adjusterService.getAllSelectedUsers();
        return CommonResult.success(list);
    }


    /**
     * 导入历史考核评价人
     *
     * @param seasonId
     * @return
     */
    @GetMapping("/importHistorySeasonAdjuster")
    public CommonResult importHistorySeasonAdjuster(@RequestParam Long seasonId) {
        try {
            return adjusterService.importHistorySeasonAdjuster(seasonId);
        } catch (ServiceErrorException e) {
            log.error("导入上一次考核评价人异常", e);
            return CommonResult.error(e.getMessage());
        }
    }
}
