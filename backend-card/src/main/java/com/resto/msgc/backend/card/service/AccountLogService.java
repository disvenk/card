package com.resto.msgc.backend.card.service;

import com.resto.conf.enums.DeleteFlagEnum;
import com.resto.conf.mybatis.base.BaseService;
import com.resto.msgc.backend.card.dto.PurchaseHistoryDto;
import com.resto.msgc.backend.card.entity.TbAccountLog;
import com.resto.msgc.backend.card.entity.TbCardCustomer;
import com.resto.msgc.backend.card.mapper.TbAccountLogMapper;
import com.resto.msgc.backend.card.mapper.TbCardCustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xielc on 2017/12/19.
 */
@Service
public class AccountLogService extends BaseService<TbAccountLog, TbAccountLogMapper> {

    @Autowired
    private TbAccountLogMapper accountLogMapper;

    @Autowired
    private TbCardCustomerMapper cardCustomerMapper;

    public List<TbAccountLog> selectaccountId(String accountId){
        return accountLogMapper.selectaccountId(accountId);
    }

    public List<PurchaseHistoryDto> selectCardId(String cardId){
        TbCardCustomer c=new TbCardCustomer();
        c.setCardId(cardId);
        c.setDeleteFlag(DeleteFlagEnum.NORMAL);
        TbCardCustomer cardCustomer=cardCustomerMapper.selectOne(c);
        return accountLogMapper.selectCardId(String.valueOf(cardCustomer.getId()));
    }
}
