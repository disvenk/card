package com.resto.msgc.backend.card.mapper;

import com.resto.conf.mybatis.util.MyMapper;
import com.resto.msgc.backend.card.entity.TbCardCompany;
import com.resto.msgc.backend.card.entity.TbCardDiscount;
import com.resto.msgc.backend.card.entity.TbCardDiscountDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbCardDiscountMapper extends MyMapper<TbCardDiscount> {

    List<TbCardDiscount> selectByPageNumSize(@Param("tbCardDiscount") TbCardDiscount tbCardDiscount, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

    List<TbCardDiscountDetail> cardDiscountOne(@Param("cardId") String cardId);

    TbCardDiscount selectById(@Param("id")Long id);

    TbCardDiscount selectDetailById(@Param("id") Long id);
}