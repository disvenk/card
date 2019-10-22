package com.resto.msgc.backend.card.mapper;

import com.resto.conf.mybatis.util.MyMapper;
import com.resto.msgc.backend.card.entity.TbRedPacket;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TbRedPacketMapper extends MyMapper<TbRedPacket> {

    //查询出所有可使用的红包
    List<TbRedPacket> selectEableUseAll(@Param("customerId") String customerId, @Param("redType")Integer redType);
    //更新红包余额
    int updateredRemainderMoneyById(@Param("redRemainderMoney") BigDecimal redRemainderMoney, @Param("id")String id);
}