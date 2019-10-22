package com.resto.msgc.backend.card.mapper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.resto.conf.mybatis.util.MyMapper;
import com.resto.msgc.backend.card.dto.BusinessSheet1;
import com.resto.msgc.backend.card.entity.TbOrder;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TbOrderMapper extends MyMapper<TbOrder> {

   JSONObject selectBrandByDateFromStartAndEnd(Map<String,Object> map);

    List<JSONObject> selectShopByDateFromStartAndEnd(Map<String,Object> map);

    List<JSONObject> selectShopByDateFromSomeMoth(Map<String,Object> map);

    List<JSONObject> selectShopBySomeMoth(Map<String,Object> map);

    List<String> selectShopName();

    List<JSONObject> selectShopNameAndCount(@Param("beginDate")String beginDate,@Param("endDate")String endDate);

    JSONObject selectOtherByShopName(Map<String,Object> map);

    //moth
    List<JSONObject> selectShopNameAndCountMoth(@Param("mothDate")String mothDate);

 JSONObject selectOtherByShopNameMoth(Map<String,Object> map);

 //sheet
 List<BusinessSheet1> selectSheet1(@Param("mothDate")String mothDate,@Param("stallName")String stallName);
 JSONObject selectSheet2(Map<String,Object> map);
    BigDecimal selectDis(@Param("date")String date,@Param("stallName")String stallName);

}