package com.resto.msgc.backend.card.form;


import com.resto.msgc.backend.card.entity.TbOrder;
import com.resto.msgc.backend.card.entity.TbOrderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by disvenk.dai on 2018-05-11 16:10
 */
public class SaveOrderForm {
    public TbOrder order = new TbOrder();
    public List<TbOrderItem> orderItems = new ArrayList<TbOrderItem>();
}
