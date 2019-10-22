package com.resto.msgc.backend.card.mapper;

import com.resto.conf.mybatis.util.MyMapper;
import com.resto.msgc.backend.card.entity.TbCustomer;
import org.apache.ibatis.annotations.Param;

public interface TbCustomerMapper extends MyMapper<TbCustomer> {

    TbCustomer selectByTelephone(@Param("tel")String tel);

    TbCustomer selectByAccountId(@Param("accountId")String accountId);
}