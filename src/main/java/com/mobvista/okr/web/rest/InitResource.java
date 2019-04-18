//package com.mobvista.okr.web.rest;
//
//import com.mobvista.okr.common.CommonResult;
//import com.mobvista.okr.config.ApplicationProperties;
//import com.mobvista.okr.service.InitService;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//
//@RestController
//@RequestMapping("/api/init")
//public class InitResource {
//
//    @Resource
//    private ApplicationProperties applicationProperties;
//
//    @Resource
//    private InitService initService;
//
//    @GetMapping
//
//    public CommonResult systemDataInit(@RequestParam String secret) {
//        if (applicationProperties.getInitSecret().equals(secret)) {
//            initService.initData();
//            return CommonResult.success("success");
//        } else {
//            return CommonResult.success("secret error");
//        }
//
//    }
//}
