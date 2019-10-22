package com.resto.msgc.backend.card.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.resto.msgc.backend.card.dto.*;
import com.resto.msgc.backend.card.entity.TbCardCompany;
import com.resto.msgc.backend.card.mapper.TbCardCustomerMapper;
import com.resto.msgc.backend.card.mapper.TbCardRechangeMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by disvenk.dai on 2018-04-17 11:46
 */

@Service
public class RechargeDataService {

    @Resource
    TbCardRechangeMapper tbCardRechangeMapper;
    @Resource
    TbCardCustomerMapper tbCardCustomerMapper;

    /**
    *@Description:查询foodMember指定起始时间开卡数据
    *@Author:disvenk.dai
    *@Date:13:37 2018/4/17 0017
    */
    public JSONObject selectEntityChargeByStartAndEnd(String beginDate,String endDate){
        BrandEntityCardChargeDto brandEntityCardChargeDto = tbCardRechangeMapper.selectEntityChargeByStartAndEnd(beginDate, endDate);
        BrandNormalChargeDto brandNormalChargeDto = tbCardRechangeMapper.selectNormalChargeByStartAndEnd(beginDate, endDate);
        BrandNormalChargeDto brandNormalChargeDto1 = tbCardRechangeMapper.selectTemporaryChargeByStartAndEnd(beginDate, endDate);
        List<BrandWorkerChargeAllDto> brandWorkerChargeAllDtos = tbCardRechangeMapper.selelctWorkerChargeAll(beginDate, endDate);

        JSONObject json = new JSONObject();
        json.put("json1",brandEntityCardChargeDto);
        json.put("json2",brandNormalChargeDto);
        json.put("json3",brandNormalChargeDto1);
        json.put("jsonList",brandWorkerChargeAllDtos);

        return json;
    }

    public BrandEntityCardChargeDto selectJson1(String beginDate,String endDate){
        BrandEntityCardChargeDto brandEntityCardChargeDto = tbCardRechangeMapper.selectEntityChargeByStartAndEnd(beginDate, endDate);
        return brandEntityCardChargeDto;
    }

    public BrandNormalChargeDto selectJson2(String beginDate,String endDate){
        BrandNormalChargeDto brandNormalChargeDto = tbCardRechangeMapper.selectNormalChargeByStartAndEnd(beginDate, endDate);
        return brandNormalChargeDto;
    }

    public List<BrandWorkerChargeAllDto> selectJsonList1(String beginDate,String endDate){
        List<BrandWorkerChargeAllDto> brandWorkerChargeAllDtos = tbCardRechangeMapper.selelctWorkerChargeAll(beginDate, endDate);
        return brandWorkerChargeAllDtos;
    }

    //moth

    public BrandEntityCardChargeDto selectJson11(String mothDate){
        BrandEntityCardChargeDto brandEntityCardChargeDto = tbCardRechangeMapper.selectEntityChargeByMoth(mothDate);
        return brandEntityCardChargeDto;
    }

    public BrandNormalChargeDto selectJson22(String mothDate){
        BrandNormalChargeDto brandNormalChargeDto = tbCardRechangeMapper.selectNormalChargeByMoth(mothDate);
        return brandNormalChargeDto;
    }

    public List<BrandWorkerChargeAllDto> selectJsonList2(String mothDate){
        List<BrandWorkerChargeAllDto> brandWorkerChargeAllDtos = tbCardRechangeMapper.selelctWorkerChargeAllByMoth(mothDate );
        return brandWorkerChargeAllDtos;
    }

    public List<BrandChargeCardCompanyAllDto> selectJsonList3(String mothDate){
        List<BrandChargeCardCompanyAllDto> brandChargeCardCompanyAllDtos = tbCardRechangeMapper.selectChargeCardNormalAll(mothDate );
        return brandChargeCardCompanyAllDtos;
    }

    //all
    public JSONObject selectChargeCardCompanyAll(String mothDate){
        List<TbCardCompany> companys = tbCardCustomerMapper.selectAllCompany();
        List<List<BrandChargeCardCompanyAllDto>> lists = new ArrayList<>();
        for (TbCardCompany tbCardCompany : companys){
            List<BrandChargeCardCompanyAllDto> brandChargeCardCompanyAllDtos = tbCardRechangeMapper.selectChargeCardCompanyAll(mothDate, tbCardCompany.getId());
            lists.add(brandChargeCardCompanyAllDtos);
        }

        JSONObject json = new JSONObject();
        json.put("companyName",companys);
        json.put("companyEntity",lists);
        return json;
    }

}
