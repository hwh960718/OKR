package com.mobvista.okr.web.rest;

import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.config.EmailProperties;
import com.mobvista.okr.dto.UserInfoDTO;
import com.mobvista.okr.exception.ServiceErrorException;
import com.mobvista.okr.service.UserSeasonMailService;
import com.mobvista.okr.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.UUID;

/**
 * @author 顾炜(GUWEI) 时间：2018/4/11 17:18
 */
@RestController
@RequestMapping("/api/email")
public class EmailResource {


    private final Logger log = LoggerFactory.getLogger(EmailResource.class);

    @Resource
    private UserService userService;

    @Resource
    private UserSeasonMailService userSeasonMailService;

    @Resource
    private EmailProperties emailProperties;

    @ApiOperation(value = "通知系统管理员")
    @PostMapping(value = "/feedBack")
    public CommonResult feedBack(MultipartFile file, @RequestParam String message) {
        log.debug("REST request to notifyOrkManager : {}", message);
        try {
            //文件转换
            File jpgFile = null;
            if (null != file) {
                jpgFile = new File(emailProperties.getTempFilePath(), UUID.randomUUID() + ".jpg");
                file.transferTo(jpgFile);
            }
            final UserInfoDTO userInfo = userService.getUserInfo();
            userSeasonMailService.sendFeedBackEmail(message, jpgFile, userInfo);
        } catch (ServiceErrorException e) {
            log.error("ServiceErrorException[通知系统管理员]", e);
            return CommonResult.error(e.getMessage());
        } catch (Exception e) {
            log.error("REST request to notifyOrkManager : error ", e);
            return CommonResult.error("反馈邮件发送失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "提醒未填写待评任务人员")
    @PostMapping(value = "/remindUnderwayAssessTaskUser")
    public CommonResult remindUnderwayAssessTaskUser(Long seasonId) {
        try {
            userSeasonMailService.remindUnderwayAssessTaskUser(seasonId);
            return CommonResult.success();
        } catch (ServiceErrorException e) {
            return CommonResult.error(e.getMessage());
        }
    }


    @ApiOperation(value = "未填写OKR目标人员提醒")
    @PostMapping(value = "/remindUnFinishFilledOKRUser")
    public CommonResult remindUnFinishFilledOKRUser(Long seasonId) {
        try {
            userSeasonMailService.remindUnFinishFilledOKRUser(seasonId);
            return CommonResult.success();
        } catch (ServiceErrorException e) {
            return CommonResult.error(e.getMessage());
        }
    }


}
