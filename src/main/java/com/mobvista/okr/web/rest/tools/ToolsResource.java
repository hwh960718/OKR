package com.mobvista.okr.web.rest.tools;

import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.job.ProductShelfJob;
import com.mobvista.okr.service.tools.ToolsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 描述：系统工具处理
 *
 * @author Weier Gu (顾炜)
 * @date 2018/9/21 10:19
 */
@RestController
@RequestMapping("/api/tools")
public class ToolsResource {

    @Resource
    private ToolsService toolsService;
    @Resource
    private ProductShelfJob productShelfJob;


    /**
     * 同步七牛云图片
     */
    @GetMapping("synQiNiuPicture2Aws")
    public CommonResult synQiNiuPicture2Aws() {
        toolsService.synQiNiuPicture2Aws();
        return CommonResult.success();
    }


    /**
     * 同步默认图片
     */
    @GetMapping("synDefaultPicture")
    public CommonResult synDefaultPicture() {
        toolsService.synDefaultPicture();
        return CommonResult.success();
    }

    /**
     * 上架结束处理
     *
     * @return
     */
    @GetMapping("productShelfDownOrder")
    public CommonResult productShelfDownOrder() {
        productShelfJob.productShelfDownOrder();
        return CommonResult.success();
    }


    /**
     * 商品自动上架触发
     *
     * @return
     */
    @GetMapping("autoUpProductShelf")
    public CommonResult autoUpProductShelf() {
        productShelfJob.autoUpProductShelf();
        return CommonResult.success();
    }
}
