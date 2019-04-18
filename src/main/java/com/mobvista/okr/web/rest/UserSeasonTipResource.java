package com.mobvista.okr.web.rest;

import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.exception.ServiceErrorException;
import com.mobvista.okr.service.UserSeasonTipService;
import com.mobvista.okr.vm.UserSeasonTipVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/u/userSeasonTip")
public class UserSeasonTipResource {

    private final Logger log = LoggerFactory.getLogger(UserSeasonTipResource.class);

    @Resource
    private UserSeasonTipService userSeasonTipService;

    /**
     * 创建用户标签
     *
     * @param vm
     * @return
     * @version v3.0
     */
    @PostMapping("/create")
    public CommonResult create(@RequestBody @Valid UserSeasonTipVM vm) {
        log.debug("REST request to create userSeasonTip: {}", vm);
        return userSeasonTipService.create(vm);
    }

    /**
     * 获取用户的标签信息
     *
     * @param userId
     * @return
     * @version v3.0
     */
    @GetMapping("/getListByUserId")
    public CommonResult getListByUserSeasonId(@RequestParam Long userId) {
        log.debug("REST request to getListByUserId userId: {}", userId);
        try {
            return CommonResult.success(userSeasonTipService.getListByUserId(userId));
        } catch (ServiceErrorException e) {
            log.error("ServiceErrorException[获取季度标签]", e);
            return CommonResult.error(e.getMessage());
        }
    }


    /**
     * 删除标签
     *
     * @param title
     * @return
     * @version v3.0
     */
    @GetMapping("/deleteTitle")
    public CommonResult deleteTitle(Long userId, String title) {
        return userSeasonTipService.deleteTitle(userId, title);
    }
}
