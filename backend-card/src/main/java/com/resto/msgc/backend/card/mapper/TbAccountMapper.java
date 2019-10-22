package com.resto.msgc.backend.card.mapper;

import com.resto.conf.mybatis.util.MyMapper;
import com.resto.msgc.backend.card.entity.TbAccount;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface TbAccountMapper extends MyMapper<TbAccount> {

    int insert(TbAccount tbAccount);

    //主键查找
    TbAccount selectById(@Param("id")String id);

    int updateRemain(@Param("payMoney")BigDecimal payMoney,@Param("id")String id);

    int updateRemainForRefund(@Param("refundMoney")BigDecimal refundMoney);

}