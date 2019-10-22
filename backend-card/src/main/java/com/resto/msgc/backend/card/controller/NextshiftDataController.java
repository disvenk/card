package com.resto.msgc.backend.card.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Supplier;
import com.resto.msgc.backend.card.entity.TbCardLoginUser;
import com.resto.msgc.backend.card.entity.TbCardShift;
import com.resto.msgc.backend.card.responseEntity.RestResponseEntity;
import com.resto.msgc.backend.card.service.NextshiftDateService;
import com.resto.msgc.backend.card.util.Constant;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.zip.Adler32;

/**
 * Created by disvenk.dai on 2018-04-14 11:01
 */

@Api
@Controller
@RequestMapping("nextshiftData")
public class NextshiftDataController extends CommonController{

    @Resource
    NextshiftDateService nextshiftDateService;

    @RequestMapping("nextshiftData")
    public String index() {
        return "nextshiftData/list";
    }

    /**
    *@Description:查询某天所有营业数据
    *@Author:disvenk.dai
    *@Date:19:18 2018/4/19 0019
    */
    @RequestMapping("selectBusinessCountByDate")
    public ResponseEntity selectBusinessCountByDate(HttpServletRequest request,String date){
        JSONObject jsonObject = nextshiftDateService.selectBusinessCountByDate(request,date);
        return new ResponseEntity(new RestResponseEntity(100,"成功",jsonObject,null), HttpStatus.OK);
    }

    /**
    *@Description:交班前查询今日账单
    *@Author:disvenk.dai
    *@Date:16:45 2018/4/24 0024
    */
    @RequestMapping("selectToDayAmount")
    public ResponseEntity selectToDayAmount(){
        JSONObject jsonObject = nextshiftDateService.selectToDayAmount(getSessionCurrentUser());
        return new ResponseEntity(new RestResponseEntity(100,"成功",jsonObject,null), HttpStatus.OK);
    }

    /**
    *@Description:确认交班
    *@Author:disvenk.dai
    *@Date:19:20 2018/4/19 0019
    */
    @RequestMapping("confirmNextShift")
    public ResponseEntity confirmNextShift(HttpServletRequest request,
                                            String actualMoney){

        String  tel = getSessionCurrentUser();
        if(tel!=null){
            TbCardShift t = new TbCardShift();
            t.setActualCash(new BigDecimal(actualMoney));
            t.setCreatedId(tel);
             nextshiftDateService.confirmNextShift(t, tel);
            return new ResponseEntity(new RestResponseEntity(100,"成功",null,null), HttpStatus.OK);
        }
        return new ResponseEntity(new RestResponseEntity(200,"失败",null,null), HttpStatus.OK);
    }
}
