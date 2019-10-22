package com.resto.msgc.backend.card.service;

import com.resto.msgc.backend.card.entity.TbCustomer;
import com.resto.msgc.backend.card.mapper.TbCustomerMapper;
import net.sf.jsqlparser.util.AddAliasesVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by disvenk.dai on 2018-04-23 16:56
 */

@Service
public class WechatCustomerService {

    @Autowired
    private TbCustomerMapper tbCustomerMapper;

    /**
    *@Description:通过手机号查找用户
    *@Author:disvenk.dai
    *@Date:16:57 2018/4/23 0023
    */
    public TbCustomer selectByTelephone(String tel){
        tbCustomerMapper.selectByTelephone(tel);
        return null;
    }

    /**
    *@Description:通过账户id
    *@Author:disvenk.dai
    *@Date:17:29 2018/4/23 0023
    */
    public TbCustomer selectByAccountId(String accountId){
        return tbCustomerMapper.selectByAccountId(accountId);
    }

    public TbCustomer selectOne(TbCustomer t){
       return tbCustomerMapper.selectOne(t);
    }
}
