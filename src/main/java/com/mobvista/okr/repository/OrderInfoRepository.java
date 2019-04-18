package com.mobvista.okr.repository;

import com.mobvista.okr.domain.OrderInfo;
import com.mobvista.okr.dto.product.OrderEmailInfoDTO;
import com.mobvista.okr.dto.product.OrderInfoDTO;
import com.mobvista.okr.repository.base.BaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * OrderInfoRepository继承基类
 *
 * @author guwei
 */
@Mapper
public interface OrderInfoRepository extends BaseRepository<OrderInfo> {


    /**
     * 根据dto统计查询
     *
     * @param dto
     * @return
     */
    int countListByDto(@Param("dto") OrderInfoDTO dto);

    /**
     * 分页查询订单信息
     *
     * @param dto
     * @param start
     * @param size
     * @return
     */
    List<OrderInfoDTO> queryListByDto(@Param("dto") OrderInfoDTO dto, @Param("start") int start, @Param("size") int size);


    /**
     * 查询当前有效订单
     *
     * @param orderInfo
     * @return
     */
    List<OrderInfo> queryOrderInfo(OrderInfo orderInfo);


    /**
     * 根据上架id和状态查询邮件发送信息
     *
     * @param shelfId
     * @param status
     * @return
     */
    List<OrderEmailInfoDTO> queryOrderInfoByShelfIdAndStatus(@Param("shelfId") Long shelfId, @Param("status") Byte status);


    /**
     * 根据订单查询邮件发送信息
     *
     * @param orderIdList
     * @return
     */
    List<OrderEmailInfoDTO> queryOrderInfoByOrderIdList(@Param("list") List<Long> orderIdList);

    /**
     * 根据订单id查询用户订单信息
     *
     * @param orderId
     * @return
     */
    OrderEmailInfoDTO queryOrderInfoByOrderId(@Param("orderId") Long orderId);

    /**
     * 查询订单信息
     *
     * @param shelfId
     * @param statusList
     * @return
     */
    List<OrderInfo> queryOrderInfoListByIdAndStatusList(@Param("shelfId") Long shelfId, @Param("statusList") List<Byte> statusList);


    /**
     * 查询订单信息
     *
     * @param shelfIdList
     * @param statusList
     * @return
     */
    List<OrderInfo> queryOrderInfoListByIdListAndStatusList(@Param("shelfIdList") List<Long> shelfIdList, @Param("statusList") List<Byte> statusList);

    /**
     * 批量修改订单状态
     *
     * @param list
     * @param status
     * @return
     */
    int updateOrderStatusByIdList(@Param("list") List<Long> list, @Param("status") Byte status);

    /**
     * 修改用户订单信息
     *
     * @param orderInfo
     * @return
     */
    int updateOrderStatusById(OrderInfo orderInfo);

}