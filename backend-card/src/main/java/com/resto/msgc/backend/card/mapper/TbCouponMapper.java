package com.resto.msgc.backend.card.mapper;

import com.resto.conf.mybatis.util.MyMapper;
import com.resto.msgc.backend.card.entity.TbCoupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbCouponMapper extends MyMapper<TbCoupon> {

    List<TbCoupon> selectEnableCoupon(@Param("customerId")String customerId);

    int updateIsUsed(@Param("id")String id);
}