package com.resto.msgc.backend.card.service;

import com.alibaba.fastjson.JSONObject;
import com.resto.msgc.backend.card.dto.BrandOneCompanyAllDto;
import com.resto.msgc.backend.card.dto.StartAndEndDto;
import com.resto.msgc.backend.card.entity.TbCardCompany;
import com.resto.msgc.backend.card.mapper.TbCardCustomerMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by disvenk.dai on 2018-04-18 18:23
 */

@Service
public class ActivateDataService {

    @Resource
    TbCardCustomerMapper tbCardCustomerMapper;

    /**
     *@Description:查询foodMember指定起始时间开卡数据
     *@Author:disvenk.dai
     *@Date:13:37 2018/4/17 0017
     */
    public JSONObject selectBrandRechargeDateByBeginAndEnd(String beginDate, String endDate){
        JSONObject json1 = tbCardCustomerMapper.selectBrandRechargeDateByBeginAndEnd(beginDate, endDate);
        JSONObject json2 = tbCardCustomerMapper.selectNormalRechargeByBeginAndEnd(beginDate, endDate);
        StartAndEndDto json3 = tbCardCustomerMapper.selectTemporaryCardByStartAndEnd(beginDate, endDate);
        List<JSONObject> jsonObjects = tbCardCustomerMapper.selectCompanyCardCount(beginDate,endDate);
        List<JSONObject> workerJson = new ArrayList<>();
        for(JSONObject jsonObject : jsonObjects){
            Integer id = Integer.parseInt(jsonObject.get("id").toString());
            Long cheque = tbCardCustomerMapper.selectCompanyCardMoney(beginDate,endDate,id, 4);
            Long cash = tbCardCustomerMapper.selectCompanyCardMoney(beginDate,endDate,id,3);
            Long aplipay = tbCardCustomerMapper.selectCompanyCardMoney(beginDate,endDate,id,2);
            Long wechat = tbCardCustomerMapper.selectCompanyCardMoney(beginDate,endDate,id,1);
            Long star = tbCardCustomerMapper.selectCompanyCardMoney(beginDate,endDate,id,5);
            jsonObject.put("chequeCardMoney",cheque);
            jsonObject.put("cashCardMoney",cash);
            jsonObject.put("aliPayCardMoney",aplipay);
            jsonObject.put("wechatCardMoney",wechat);
            jsonObject.put("starCardMoney",star);
        }
        if((Long)json1.get("cardCount")==0){
            json1=null;
        }
        if((Long)json2.get("cardCount")==0){
            json2=null;
        }
        /*if((Long)json3.get("cardCount")==0){
            json3=null;
        }*/
        if(json3.getCardCount()==0){
            json3=null;
        }
        JSONObject json = new JSONObject();
        json.put("json1",json1);
        json.put("json2",json2);
        json.put("json3",json3);
        json.put("jsonList",jsonObjects);
        return json;
    }

    public JSONObject selectJson1(String beginDate,String endDate){
        JSONObject json1 = tbCardCustomerMapper.selectBrandRechargeDateByBeginAndEnd(beginDate, endDate);
        return json1;
    }

    public JSONObject selectJson2(String beginDate,String endDate){
        JSONObject json2 = tbCardCustomerMapper.selectNormalRechargeByBeginAndEnd(beginDate, endDate);
        return json2;
    }

    public List<JSONObject> selectJsonList(String beginDate,String endDate){
        List<JSONObject> jsonList = tbCardCustomerMapper.selectCompanyCardCount(beginDate,endDate);
        return jsonList;
    }

    //moth

    public JSONObject selectJson11(String mothDate){
        JSONObject json1 = tbCardCustomerMapper.selectBrandRechargeDateByMoth(mothDate);
        return json1;
    }

    public JSONObject selectJson22(String mothDate){
        JSONObject json2 = tbCardCustomerMapper.selectNormalRechargeByMoth(mothDate);
        return json2;
    }

    public List<JSONObject> selectJsonList2(String mothDate){
        List<JSONObject> jsonObjects = tbCardCustomerMapper.selectCompanyCardCountMoth(mothDate);
        //List<JSONObject> workerJson = new ArrayList<>();
        for(JSONObject jsonObject : jsonObjects){
            Integer id = Integer.parseInt(jsonObject.get("id").toString());
            Long cheque = tbCardCustomerMapper.selectCompanyCardMoneyMoth(mothDate,id, 4);
            Long cash = tbCardCustomerMapper.selectCompanyCardMoneyMoth(mothDate,id,3);
            Long aplipay = tbCardCustomerMapper.selectCompanyCardMoneyMoth(mothDate,id,2);
            Long wechat = tbCardCustomerMapper.selectCompanyCardMoneyMoth(mothDate,id,1);
            Long star = tbCardCustomerMapper.selectCompanyCardMoneyMoth(mothDate,id,5);
            jsonObject.put("chequeCardMoney",cheque);
            jsonObject.put("cashCardMoney",cash);
            jsonObject.put("aliPayCardMoney",aplipay);
            jsonObject.put("wechatCardMoney",wechat);
            jsonObject.put("starCardMoney",star);
        }
        return jsonObjects;
    }

    public List<BrandOneCompanyAllDto> selectJsonList3(String mothDate){
        List<BrandOneCompanyAllDto> list = tbCardCustomerMapper.selectOneCompanyNormalAll(mothDate);
        //List<JSONObject> workerJson = new ArrayList<>();

        return list;
    }

    public JSONObject selectOneCompanyNormalAll(String mothDate){
        List<TbCardCompany> companys = tbCardCustomerMapper.selectAllCompany();
        List<List<BrandOneCompanyAllDto>> lists = new ArrayList<>();
        for (TbCardCompany tbCardCompany : companys){
            List<BrandOneCompanyAllDto> brandOneCompanyAllDtos = tbCardCustomerMapper.selectOneCompanyAll(mothDate, tbCardCompany.getId());
            lists.add(brandOneCompanyAllDtos);
        }

        JSONObject json = new JSONObject();
        json.put("companyName",companys);
        json.put("companyEntity",lists);
        return json;
    }

    public JSONObject selectOneCompanyAll(String mothDate){
        List<TbCardCompany> companys = tbCardCustomerMapper.selectAllCompany();
        List<List<BrandOneCompanyAllDto>> lists = new ArrayList<>();
        for (TbCardCompany tbCardCompany : companys){
            List<BrandOneCompanyAllDto> brandOneCompanyAllDtos = tbCardCustomerMapper.selectOneCompanyAll(mothDate, tbCardCompany.getId());
            lists.add(brandOneCompanyAllDtos);
        }

        JSONObject json = new JSONObject();
        json.put("companyName",companys);
        json.put("companyEntity",lists);
        return json;
    }


}
