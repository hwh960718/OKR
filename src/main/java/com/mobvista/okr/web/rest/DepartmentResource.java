package com.mobvista.okr.web.rest;

import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.service.DepartmentService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/api/u/department")
public class DepartmentResource {

    private final Logger log = LoggerFactory.getLogger(DepartmentResource.class);

    @Resource
    private DepartmentService departmentService;

    @ApiOperation(value = "获取部门树")
    @GetMapping("/getDepartmentTree")
    public CommonResult getDepartmentTree() {
        log.debug("REST request to getDepartmentTree");
        return CommonResult.success(departmentService.getDepartmentTree());
    }

//    @ApiOperation(value = "获取leader的部门树")
//    @GetMapping("/getDepartmentTreeForLeader")
//    public CommonResult getDepartmentTreeForLeader() {
//        log.debug("REST request to getDepartmentTreeForLeader");
//        return CommonResult.success(departmentService.getDepartmentTreeForLeader());
//    }
}
