package com.resto.msgc.backend.card.mapper;

import com.resto.conf.mybatis.util.MyMapper;
import com.resto.msgc.backend.card.entity.TbCardRechargeLog;
import com.resto.msgc.backend.card.entity.TbChargeOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbCardRechargeLogMapper extends MyMapper<TbCardRechargeLog> {

    List<TbChargeOrder> findRemain(@Param("cardId") String cardId);

    int deleteByPinCard(String cardId);

}