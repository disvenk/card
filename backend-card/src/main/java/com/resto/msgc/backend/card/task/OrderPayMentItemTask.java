package com.resto.msgc.backend.card.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resto.msgc.backend.card.conf.PayConf;
import com.resto.msgc.backend.card.entity.TbRoute;
import com.resto.msgc.backend.card.http.HttpRequest;
import com.resto.msgc.backend.card.service.RouteService;

import com.resto.msgc.backend.card.util.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;

@Component
public class OrderPayMentItemTask {

    private Logger log = LoggerFactory.getLogger(OrderPayMentItemTask.class);

    @Autowired
    PayConf payConf;

    @Autowired
    RouteService routeService;


    //@Scheduled(fixedRate = 3000)//这个只支持单一表达式
    @Scheduled(cron = "*/30 * * * * ?")//这种表达式可以写每日、周、月，位数的6位，不支持年
    //表达式的生成地址http://cron.qqe2.com/可以任意操作
    public void reportDo(){

        List<TbRoute> notSuccess = routeService.getNotSuccess();
        if(notSuccess.size()!=0){
            notSuccess.forEach(n->{
                try {
                    Thread.sleep(500);
//                    String s = HttpClient.doPostJson(payConf.serverUrl + "orderPayMentItem?baseUrl="+payConf.wxUrl, n.getContext());
//                    //String s = HttpRequest.sendPost(payConf.serverUrl+"orderPayMentItem", n.getContext());
//                    if("success".equals(s)){
//                        routeService.update(n.getOrderId());
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });

        }
        log.info(new Date()+"执行了一次同步");
    }

}
