package com.resto.msgc.backend.card.service;

import com.resto.msgc.backend.card.entity.TbOrderPaymentItem;
import com.resto.msgc.backend.card.mapper.TbOrderPaymentItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by disvenk.dai on 2018-04-24 11:11
 */

@Service
public class OrderPaymentService {

    @Autowired
    private TbOrderPaymentItemMapper tbOrderPaymentItemMapper;

    /**
    *@Description:根据orderId查找并更新
    *@Author:disvenk.dai
    *@Date:11:15 2018/4/24 0024
    */
    public void selectAndUpdate(String code, String orderId){
        List<TbOrderPaymentItem> tbOrderPaymentItems = tbOrderPaymentItemMapper.selectByOrderId(code);
        for(TbOrderPaymentItem tbOrderPaymentItem :tbOrderPaymentItems){
            tbOrderPaymentItemMapper.updateOrderIdByCode(orderId,tbOrderPaymentItem.getId());
        }
    }

    public List<TbOrderPaymentItem> get(){
       return tbOrderPaymentItemMapper.selectAll();
    }

    public List<TbOrderPaymentItem> selectByCarNumber(String orderId){
        return tbOrderPaymentItemMapper.selectByCarNumber(orderId);
    }
}
