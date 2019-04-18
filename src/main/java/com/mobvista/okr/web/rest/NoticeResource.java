package com.mobvista.okr.web.rest;

import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.enums.NoticeStatus;
import com.mobvista.okr.exception.ServiceErrorException;
import com.mobvista.okr.service.NoticeService;
import com.mobvista.okr.util.KVUtil;
import com.mobvista.okr.vm.NoticeVM;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/u/notice")
public class NoticeResource {

    private final Logger log = LoggerFactory.getLogger(NoticeResource.class);

    @Resource
    private NoticeService noticeService;

    @ApiOperation(value = "查询公告列表")
    @GetMapping("/list")
    public CommonResult list(Byte status) {
        log.debug("REST request to get notice: {}");
        return CommonResult.success(noticeService.findNotice(status, null));
    }


    @ApiOperation(value = "查询状态枚举")
    @GetMapping("/getStatusEnums")
    public CommonResult getStatusEnums() {
        List<KVUtil> list = new ArrayList<>();
        KVUtil kvUtil;
        for (NoticeStatus ns : NoticeStatus.values()) {
            kvUtil = new KVUtil();
            kvUtil.setId(Long.valueOf(ns.getCode()));
            kvUtil.setName(ns.getValue());
            list.add(kvUtil);
        }
        return CommonResult.success(list);
    }


    @ApiOperation(value = "创建公告")
    @PostMapping("/create")
    public CommonResult create(@Valid @RequestBody NoticeVM noticeVM) {
        log.debug("REST request to create notice: {}", noticeVM);
        try {
            noticeService.create(noticeVM);
            return CommonResult.success();
        } catch (ServiceErrorException e) {
            return CommonResult.error(e.getMessage());
        }
    }

    @ApiOperation(value = "删除公告")
    @GetMapping("/delete")
    public CommonResult clear(@RequestParam Long id) {
        log.debug("REST request to clear notice");
        noticeService.deleteById(id);
        return CommonResult.success();
    }

    @ApiOperation(value = "获取公告")
    @GetMapping("/detail")
    public CommonResult detail() {
        log.debug("REST request to get notice: {}");
        return CommonResult.success(noticeService.findValidNoticeList());
    }
}
