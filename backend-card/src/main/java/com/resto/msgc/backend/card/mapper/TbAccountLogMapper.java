package com.resto.msgc.backend.card.mapper;

import com.resto.conf.mybatis.util.MyMapper;
import com.resto.msgc.backend.card.dto.PurchaseHistoryDto;
import com.resto.msgc.backend.card.entity.TbAccountLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbAccountLogMapper extends MyMapper<TbAccountLog> {

    List<TbAccountLog> selectaccountId(@Param("accountId") String accountId);

    List<PurchaseHistoryDto> selectCardId(@Param("cardCustomerId") String cardCustomerId);
}