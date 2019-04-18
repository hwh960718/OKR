package com.mobvista.okr.web.rest;

import com.google.common.collect.Lists;
import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.dto.product.OrderInfoDTO;
import com.mobvista.okr.enums.YesOrNoEnum;
import com.mobvista.okr.enums.product.OrderStatusEnum;
import com.mobvista.okr.security.SecurityUtils;
import com.mobvista.okr.service.product.OrderService;
import com.mobvista.okr.util.KVUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述：
 *
 * @author Weier Gu (顾炜)
 * @date 2018/9/16 15:30
 */
@RestController
@RequestMapping("/api/order")
public class OrderResource {

    @Resource
    private OrderService orderService;

    /**
     * 分页查询
     *
     * @param dto
     * @param pageable
     * @return
     * @version v3.1
     */
    @GetMapping("list")
    public CommonResult list(OrderInfoDTO dto, Pageable pageable) {
        //判断是否是管理员用户
        if (SecurityUtils.getUserOperatorPerm().getCanSeeAllOrderList().equals(YesOrNoEnum.NO.getCode())) {
            dto.setCreateUserId(SecurityUtils.getCurrentUserId());
        } else {
            //判断是否是管理员权限
            dto.setAdminRole(true);
        }
        if (null != dto.getStatus() && dto.getStatus() > 0) {
            dto.setStatusList(Lists.newArrayList(dto.getStatus()));
        } else {
            dto.setStatusList(Lists.newArrayList(
                    OrderStatusEnum.Enable.getCode(),
                    OrderStatusEnum.Disable.getCode(),
                    OrderStatusEnum.Locked.getCode(),
                    OrderStatusEnum.Processed.getCode()));
        }
        return orderService.list(dto, pageable);
    }


    /**
     * 创建订单
     *
     * @param dto
     * @return
     */
    @PostMapping("saveOrder")
    public CommonResult saveOrder(@RequestBody OrderInfoDTO dto) {
        try {
            return orderService.saveOrderInfo(dto);
        } catch (RuntimeException e) {
            return CommonResult.error(e.getMessage());
        } catch (Exception e) {
            return CommonResult.error("创建订单失败");
        }
    }

    /**
     * 获取上架对应的订单信息
     *
     * @param productShelfId
     * @param pageable
     * @return
     */
    @GetMapping("queryShelfOrderList")
    public CommonResult queryShelfOrderList(Long productShelfId, Pageable pageable) {
        OrderInfoDTO dto = new OrderInfoDTO();
        dto.setShelfId(productShelfId);
        return orderService.list(dto, pageable);
    }


    /**
     * 处理订单状态
     *
     * @param orderId
     * @return
     * @version v3.3
     */
    @PostMapping("processedOrderStatus")
    public CommonResult processedOrderStatus(@RequestParam Long orderId) {
        //判断是否是管理员用户
        if (SecurityUtils.getUserOperatorPerm().getCanSeeAllOrderList().equals(YesOrNoEnum.NO.getCode())) {
            return CommonResult.error("无操作权限");
        }
        return orderService.processedOrderStatus(orderId);
    }


    /**
     * 获取有效的订单状态
     *
     * @return
     * @version v3.3
     */
    @GetMapping("getValidOrderStatus")
    public CommonResult getValidOrderStatus() {
        List<KVUtil> list = Lists.newArrayList();
        KVUtil kv;
        for (OrderStatusEnum en : OrderStatusEnum.values()) {
            if (en.equals(OrderStatusEnum.UnLocked)) {
                continue;
            }
            kv = new KVUtil();
            kv.setId(Long.valueOf(en.getCode()));
            kv.setName(en.getValue());
            list.add(kv);
        }
        return CommonResult.success(list);
    }
}
