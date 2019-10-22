package com.resto.msgc.backend.card.controller;

import com.alibaba.fastjson.JSONObject;
import com.resto.msgc.backend.card.entity.TbOrderPaymentItem;
import com.resto.msgc.backend.card.service.OrderPaymentService;
import com.resto.msgc.backend.card.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by disvenk.dai on 2018-08-28 16:06
 */

@Controller
@RequestMapping("tongbu")
public class TongbuController {

    @Autowired
    OrderPaymentService orderPaymentService;

    @RequestMapping("tongbu")
    @ResponseBody
    public List<TbOrderPaymentItem> tongbu(){
        List<TbOrderPaymentItem> orderPaymentItems = orderPaymentService.get();
        String s = HttpRequest.sendPost("http://test0001.restoplus.cn:8380/shop/foodMember/orderPayMentItem", JSONObject.toJSONString(orderPaymentItems));
        System.out.println(s);
        return orderPaymentItems;
    }
}
