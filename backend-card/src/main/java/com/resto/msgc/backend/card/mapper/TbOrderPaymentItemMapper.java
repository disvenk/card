package com.resto.msgc.backend.card.mapper;

import com.resto.conf.mybatis.util.MyMapper;
import com.resto.msgc.backend.card.entity.TbOrderPaymentItem;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TbOrderPaymentItemMapper extends MyMapper<TbOrderPaymentItem> {

    List<TbOrderPaymentItem> selectByOrderId(@Param("orderId")String orderId);

    int updateOrderIdByCode(@Param("orderId")String orderId,@Param("id")String id);

    List<TbOrderPaymentItem> selectChargeBalancePayItem(@Param("orderId")String orderId);

    BigDecimal selectChargeBalancePay(@Param("orderId")String orderId);

    List<TbOrderPaymentItem> selectByCarNumber(@Param("orderId")String orderId);
}