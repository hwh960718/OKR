package com.mobvista.okr.web.rest;

import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.dto.SeasonUserDTO;
import com.mobvista.okr.dto.UserInfoDTO;
import com.mobvista.okr.exception.ServiceErrorException;
import com.mobvista.okr.service.UserService;
import com.mobvista.okr.util.AwsPictureProcessUtil;
import com.mobvista.okr.vm.UpdateUserVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/u/account")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    @Resource
    private UserService userService;

    /**
     * 获取登陆人信息
     *
     * @return
     */
    @GetMapping("/getAccount")
    public CommonResult getAccount() {
        log.debug("REST request to get account");
        try {
            return CommonResult.success(userService.getUserInfo());
        } catch (ServiceErrorException e) {
            log.error("ServiceErrorException[获取登陆人信息]", e);
            return CommonResult.error(e.getMessage());
        }
    }

    /**
     * 根据用户id获取用户信息
     *
     * @param userId
     * @return
     * @version v3.0
     */
    @GetMapping("queryUserInfoById")
    public CommonResult queryUserInfoById(Long userId) {
        log.debug("REST request to queryUserInfoById。userId=" + userId);
        try {
            return CommonResult.success(userService.queryUserInfoById(userId));
        } catch (ServiceErrorException e) {
            log.error("ServiceErrorException[获取登陆人信息]", e);
            return CommonResult.error(e.getMessage());
        }
    }


    /**
     * 修改头像
     *
     * @param faceImg
     * @return
     */
    @PostMapping(path = "/updateFace")
    public CommonResult uploadFaceImg(@RequestParam String faceImg) {
        log.debug("REST request to update face");
        userService.updateUserFace(faceImg);
        return CommonResult.success();
    }

    /**
     * 查询用户信息
     *
     * @param name
     * @param seasonId
     * @param pageable
     * @return
     */
    @GetMapping(path = "/search")
    public CommonResult search(@RequestParam(required = false) String name, @RequestParam(required = false) Long seasonId, Pageable pageable) {
        log.debug("REST request to search account");
        Page<UserInfoDTO> page = userService.findAll(name, seasonId, pageable);
        return CommonResult.success(page);
    }

    /**
     * 根据userSeasonId 查询用户信息
     *
     * @param name
     * @param userSeasonId
     * @param pageable
     * @return
     */
    @GetMapping(path = "/searchByUserSeasonId")
    public CommonResult searchByUserSeasonId(@RequestParam(required = false) String name, @RequestParam(required = false) Long userSeasonId, Pageable pageable) {
        log.debug("REST request to search account");
        Page<UserInfoDTO> page = userService.searchByUserSeasonId(name, userSeasonId, pageable);
        return CommonResult.success(page);
    }

    /**
     * 查询所有用户信息
     *
     * @param name
     * @param pageable
     * @return
     */
    @GetMapping(path = "/searchAll")
    public CommonResult searchAll(String name, Pageable pageable) {
        log.debug("REST request to search searchAll");
        Page<UserInfoDTO> page = userService.findAll(name, pageable);
        return CommonResult.success(page);
    }

    /**
     * 修改用户信息
     *
     * @param updateUserVM
     * @return
     */
    @PostMapping(path = "/updateUserInfo")
    public CommonResult updateUserInfo(@Valid @RequestBody UpdateUserVM updateUserVM) {
        log.debug("REST request to update userInfo");
        return userService.updateUserInfo(updateUserVM);
    }


    /**
     * 查询考核对应的用户
     *
     * @param name
     * @param seasonId
     * @param pageable
     * @return
     */
    @GetMapping(path = "/querySeasonUserByNameAndSeasonId")
    public CommonResult querySeasonUserByNameAndSeasonId(String name, Long seasonId, Pageable pageable) {
        log.debug("REST request to search querySeasonUserByNameAndSeasonId. name={},seasonId={}", name, seasonId);
        try {
            Page<SeasonUserDTO> page = userService.findSeasonUserByNameAndSeasonId(name, seasonId, pageable);
            return CommonResult.success(page);
        } catch (ServiceErrorException e) {
            return CommonResult.error(e.getMessage());
        }
    }


    /**
     * 获取所有默认图片
     *
     * @return
     */
    @GetMapping("getAllDefaultPictureList")
    public CommonResult getAllDefaultPictureList() {
        return CommonResult.success(AwsPictureProcessUtil.getAllDefaultPictureList());
    }
}
