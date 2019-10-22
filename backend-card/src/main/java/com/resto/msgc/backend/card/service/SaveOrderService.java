package com.resto.msgc.backend.card.service;

import com.resto.msgc.backend.card.Application;
import com.resto.msgc.backend.card.entity.TbOrder;
import com.resto.msgc.backend.card.entity.TbOrderItem;
import com.resto.msgc.backend.card.mapper.TbOrderItemMapper;
import com.resto.msgc.backend.card.mapper.TbOrderMapper;
import com.resto.msgc.backend.card.util.ApplicationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by disvenk.dai on 2018-04-23 18:31
 */
@Service
public class SaveOrderService {

    @Autowired
    private TbOrderMapper tbOrderMapper;
    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;

        public void insert(TbOrder order){
            tbOrderMapper.insert(order);
        }

        @Transactional(rollbackFor = Exception.class)
        public void insert(List<TbOrderItem> items){
            for(TbOrderItem orderItem : items){
                orderItem.setSort(0);
                orderItem.setStatus(1);
                orderItem.setCreateTime(new Date());
                tbOrderItemMapper.insert(orderItem);
            }
        }
}
