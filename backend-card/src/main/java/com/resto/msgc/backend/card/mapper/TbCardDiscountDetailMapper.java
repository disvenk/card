package com.resto.msgc.backend.card.mapper;

import com.resto.conf.mybatis.util.MyMapper;
import com.resto.msgc.backend.card.entity.TbCardDiscountDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbCardDiscountDetailMapper extends MyMapper<TbCardDiscountDetail> {

    int deleteDiscountId(@Param("discountId") Long discountId);

    List<TbCardDiscountDetail> selectdiscountId(@Param("discountId") Long discountId);
}