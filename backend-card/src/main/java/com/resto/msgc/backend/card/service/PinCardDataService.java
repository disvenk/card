package com.resto.msgc.backend.card.service;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.domain.AlipayDataDataserviceChinaremodelQueryModel;
import com.resto.msgc.backend.card.dto.*;
import com.resto.msgc.backend.card.entity.TbCardCompany;
import com.resto.msgc.backend.card.entity.TbCardRefund;
import com.resto.msgc.backend.card.mapper.TbCardCustomerMapper;
import com.resto.msgc.backend.card.mapper.TbCardRefundMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by disvenk.dai on 2018-04-19 10:51
 */

@Service
public class PinCardDataService {

    @Resource
    TbCardRefundMapper tbCardRefundMapper;
    @Resource
    TbCardCustomerMapper tbCardCustomerMapper;

    /**
    *@Description:查询所有数据
    *@Author:disvenk.dai
    *@Date:10:51 2018/4/19 0019
    */
    public JSONObject selectCardRefundByStartAndEnd(String beginDate,String endDate){
        BrandEntityPinCardDataDto brandEntityPinCardDataDto1 = tbCardRefundMapper.selectEntityCardRefundByStartAndEnd(beginDate, endDate);
        BrandEntityPinCardDataDto brandEntityPinCardDataDto2 = tbCardRefundMapper.selectNormalCardRefundByStartAndEnd(beginDate, endDate);
        BrandEntityPinCardDataDto brandEntityPinCardDataDto3 = tbCardRefundMapper.selectTemporaryCardRefundByStartAndEnd(beginDate, endDate);

        List<BrandEntityPinCardDataDto> brandEntityPinCardDataDtos = tbCardRefundMapper.selectWorkerCardRefundByStartAndEnd(beginDate, endDate);
        JSONObject json = new JSONObject();
        json.put("json1",brandEntityPinCardDataDto1);
        json.put("json2",brandEntityPinCardDataDto2);
        json.put("json3",brandEntityPinCardDataDto3);
        json.put("jsonList",brandEntityPinCardDataDtos);
        return json;
    }

    public BrandEntityPinCardDataDto selectJson1(String beginDate, String endDate){
        BrandEntityPinCardDataDto brandEntityPinCardDataDto1 = tbCardRefundMapper.selectEntityCardRefundByStartAndEnd(beginDate, endDate);
        return brandEntityPinCardDataDto1;
    }

    public BrandEntityPinCardDataDto selectJson2(String beginDate, String endDate){
        BrandEntityPinCardDataDto brandEntityPinCardDataDto2 = tbCardRefundMapper.selectNormalCardRefundByStartAndEnd(beginDate, endDate);
        return brandEntityPinCardDataDto2;
    }

    public List<BrandEntityPinCardDataDto> selectJsonList1(String beginDate, String endDate){
        List<BrandEntityPinCardDataDto> brandEntityPinCardDataDtos = tbCardRefundMapper.selectWorkerCardRefundByStartAndEnd(beginDate, endDate);
        return brandEntityPinCardDataDtos;
    }

    //moth

    public BrandEntityPinCardDataDto selectJson11(String mothDate){
        BrandEntityPinCardDataDto brandEntityPinCardDataDto1 = tbCardRefundMapper.selectEntityCardRefundByMoth(mothDate);
        return brandEntityPinCardDataDto1;
    }

    public BrandEntityPinCardDataDto selectJson22(String mothDate){
        BrandEntityPinCardDataDto brandEntityPinCardDataDto2 = tbCardRefundMapper.selectNormalCardRefundByMoth(mothDate );
        return brandEntityPinCardDataDto2;
    }

    public List<BrandEntityPinCardDataDto> selectJsonList2(String mothDate){
        List<BrandEntityPinCardDataDto> brandEntityPinCardDataDtos = tbCardRefundMapper.selectWorkerCardRefundByMoth(mothDate );
        return brandEntityPinCardDataDtos;
    }

    public List<BrandRefundCardWorkerAllDto> selectJsonList3(String mothDate){
        List<BrandRefundCardWorkerAllDto> brandEntityPinCardDataDtos = tbCardRefundMapper.selectRefundCardNormalAllDto(mothDate );
        return brandEntityPinCardDataDtos;
    }

    //all
    public JSONObject selectRefundCardWorkerAllDto(String mothDate){
        List<TbCardCompany> companys = tbCardCustomerMapper.selectAllCompany();
        List<List<BrandRefundCardWorkerAllDto>> lists = new ArrayList<>();
        for (TbCardCompany tbCardCompany : companys){
            List<BrandRefundCardWorkerAllDto> brandRefundCardWorkerAllDtos = tbCardRefundMapper.selectRefundCardWorkerAllDto(tbCardCompany.getId(), mothDate);
            lists.add(brandRefundCardWorkerAllDtos);
        }

        JSONObject json = new JSONObject();
        json.put("companyName",companys);
        json.put("companyEntity",lists);
        return json;
    }
}
