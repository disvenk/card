package com.resto.msgc.backend.card.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.resto.msgc.backend.card.dto.BusinessSheet1;
import com.resto.msgc.backend.card.mapper.TbOrderMapper;
import org.codehaus.janino.util.Benchmark;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by disvenk.dai on 2018-04-16 11:30
 */

@Service
public class BusunessDataService {

    @Resource
    private TbOrderMapper tbOrderMapper;

    /**
    *@Description:查询和导出指定起始时间Foodmember营业报表
    *@Author:disvenk.dai
    *@Date:14:08 2018/4/16 0016
    */
    public JSONObject selectBrandByDateFromStartAndEnd(String beginDate,String endDate){
        Map<String,Object> map = new HashMap<>();
        map.put("beginDate",beginDate);
        map.put("endDate",endDate);
        JSONObject jsonObject = tbOrderMapper.selectBrandByDateFromStartAndEnd(map);
        if(new BigDecimal(jsonObject.get("orderCount").toString()).compareTo(BigDecimal.ZERO)==0){
            jsonObject=null;
        }else {
            jsonObject.put("brandName","Foodmember");
        }
        List<JSONObject> shopNames = tbOrderMapper.selectShopNameAndCount(beginDate,endDate);
        for(JSONObject json : shopNames){
            String stallName = json.get("shopName").toString();
            Map<String,Object> map1 = new HashMap<>();
            map1.put("stallName",stallName);
            map1.put("beginDate", beginDate);
            map1.put("endDate",endDate);

            //查实收金额
            List<Integer> payModeIds1 = Lists.newArrayList(1,5,6,10,12,20,23);
            map1.put("list",payModeIds1);
            JSONObject actualMoney = tbOrderMapper.selectOtherByShopName(map1);
            json.put("paymentAmount",actualMoney.get("paymentAmount"));

            //查折扣金额
            List<Integer> payModeIds2 = Lists.newArrayList(2,3,7,8,13,15,14,17,21,24);
            map1.put("list",payModeIds2);
            JSONObject disMoney = tbOrderMapper.selectOtherByShopName(map1);
            BigDecimal posDis = new BigDecimal(json.get("discountMoney").toString());
            json.put("reductionAmount",posDis.add(new BigDecimal(disMoney.get("paymentAmount").toString())));

            //查询退款金额
            List<Integer> payModeIds3 = Lists.newArrayList(11,19,25);
            map1.put("list",payModeIds3);
            JSONObject refunMoney = tbOrderMapper.selectOtherByShopName(map1);
            json.put("refundMoney",new BigDecimal(refunMoney.get("paymentAmount").toString()));

            BigDecimal ac = new BigDecimal(actualMoney.get("paymentAmount").toString());
            BigDecimal dis = posDis.add(new BigDecimal(disMoney.get("paymentAmount").toString()));
            BigDecimal ref = new BigDecimal(refunMoney.get("paymentAmount").toString());

            json.put("originalAmount",(ac.add(dis).subtract(ref)));

        }

        JSONObject json = new JSONObject();
        json.put("brand",jsonObject);
        json.put("shop",shopNames);
        return json;
    }

    /**
    *@Description:导出某个月Foodmember营业报表
    *@Author:disvenk.dai
    *@Date:17:50 2018/4/16 0016
    */
    public JSONObject selectShopByDateFromSomeMoth(String mothDate){

        List<JSONObject> shopNames = tbOrderMapper.selectShopNameAndCountMoth(mothDate);
        for(JSONObject json : shopNames){
            String stallName = json.get("shopName").toString();
            Map<String,Object> map1 = new HashMap<>();
            map1.put("stallName",stallName);
            map1.put("mothDate", mothDate);

            //查实收金额
            List<Integer> payModeIds1 = Lists.newArrayList(1,5,6,10,12,20,23);
            map1.put("list",payModeIds1);
            JSONObject actualMoney = tbOrderMapper.selectOtherByShopNameMoth(map1);
            json.put("paymentAmount",actualMoney.get("paymentAmount"));

            //查折扣金额
            List<Integer> payModeIds2 = Lists.newArrayList(2,3,7,8,13,15,14,17,21,24);
            map1.put("list",payModeIds2);
            JSONObject disMoney = tbOrderMapper.selectOtherByShopNameMoth(map1);
            BigDecimal posDis = new BigDecimal(json.get("discountMoney").toString());
            json.put("reductionAmount",posDis.add(new BigDecimal(disMoney.get("paymentAmount").toString())));

            //查询退款金额
            List<Integer> payModeIds3 = Lists.newArrayList(11,19,25);
            map1.put("list",payModeIds3);
            JSONObject refunMoney = tbOrderMapper.selectOtherByShopNameMoth(map1);
            json.put("refundMoney",new BigDecimal(refunMoney.get("paymentAmount").toString()));

            BigDecimal ac = new BigDecimal(actualMoney.get("paymentAmount").toString());
            BigDecimal dis = posDis.add(new BigDecimal(disMoney.get("paymentAmount").toString()));
            BigDecimal ref = new BigDecimal(refunMoney.get("paymentAmount").toString());

            json.put("originalAmount",(ac.add(dis).subtract(ref)));
        }

        JSONObject json = new JSONObject();
        json.put("shop",shopNames);
        return json;
    }

    /**
     *@Description:导出sheet
     *@Author:disvenk.dai
     *@Date:17:50 2018/4/16 0016
     */
    public List<List<BusinessSheet1>> selectShopBySomeMoth(String mothDate){
        List<String> shopNames = this.selectShopName();
        Map<String,Object> map = new HashMap<>();
        map.put("mothDate",mothDate);
        List<List<BusinessSheet1>> lists = new ArrayList<>();


        shopNames.forEach(n->{
            List<BusinessSheet1> businessSheet1s = tbOrderMapper.selectSheet1(mothDate, n);
            businessSheet1s.forEach(m->{
                map.put("stallName",n);
                map.put("date", m.getDate());
                //查每一天实收金额
                List<Integer> payModeIds1 = Lists.newArrayList(1,5,6,10,12,20,23);
                map.put("list",payModeIds1);
                JSONObject actual = tbOrderMapper.selectSheet2(map);
                BigDecimal ac = new BigDecimal(actual.get("money").toString());
                m.setPaymentAmount(ac);

                //查每一天折扣金额
                List<Integer> payModeIds2 = Lists.newArrayList(2,3,7,8,13,15,14,17,21,24);
                map.put("list",payModeIds2);
                JSONObject disMoney = tbOrderMapper.selectSheet2(map);
                BigDecimal dis = m.getDiscountMoney().add(new BigDecimal(disMoney.get("money").toString()));
                m.setReductionAmount(dis);

                //查询每一天退款金额
                List<Integer> payModeIds3 = Lists.newArrayList(11,19,25);
                map.put("list",payModeIds3);
                JSONObject refun = tbOrderMapper.selectSheet2(map);
                m.setRefundMoney(new BigDecimal(refun.get("money").toString()));



                BigDecimal ref = ac.add(dis).subtract(new BigDecimal(refun.get("money").toString()));

                m.setOriginalAmount(ref);
            });

            lists.add(businessSheet1s);
        });

        return lists;
    }

    /**
    *@Description:查询所有的档口名称
    *@Author:disvenk.dai
    *@Date:15:44 2018/4/16 0016
    */
    public List<String> selectShopName(){
        List<String> strings = tbOrderMapper.selectShopName();
        return strings;
    }



}
