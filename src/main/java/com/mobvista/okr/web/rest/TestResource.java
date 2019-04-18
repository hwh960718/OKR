//package com.mobvista.okr.web.rest;
//
//import com.mobvista.okr.common.CommonResult;
//import com.mobvista.okr.exception.ServiceErrorException;
//import com.mobvista.okr.service.TestService;
//import io.swagger.annotations.ApiOperation;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//
//
//@RestController
//@RequestMapping("/api/u/test")
//public class TestResource {
//
//    private final Logger log = LoggerFactory.getLogger(TestResource.class);
//
//    @Resource
//    private TestService testService;
//
//    @ApiOperation(value = "测试自动完成季度考核")
//    @GetMapping(value = "/testAutoSeasonFinish")
//
////    @Secured({AuthoritiesConstants.PERSONNEL, AuthoritiesConstants.ADMIN})
//    public CommonResult testAutoSeasonFinish(@RequestParam Long id) {
//        log.debug("REST request to testAutoSeasonFinish id: {}", id);
//        try {
//            testService.testAutoFinishSeason(id);
//            return CommonResult.success();
//        } catch (ServiceErrorException e) {
//            log.error("ServiceErrorException[测试自动完成季度考核]", e);
//            return CommonResult.error(e.getMessage());
//        }
//    }
//}
