package com.resto.msgc.backend.card.controller;

import com.alibaba.fastjson.JSON;
import com.resto.conf.enums.DeleteFlagEnum;
import com.resto.msgc.backend.card.entity.TbCardCompany;
import com.resto.msgc.backend.card.entity.TbCardDiscount;
import com.resto.msgc.backend.card.entity.TbCardDiscountDetail;
import com.resto.msgc.backend.card.service.CardDiscountService;
import com.resto.msgc.backend.card.util.*;
import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by xielc on 2017/12/12.
 */
@Api
@Controller
@RequestMapping("cardDiscount")
public class CardDiscountController {

    protected Logger log = LoggerFactory.getLogger(getClass());

    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private CardDiscountService cardDiscountService;

    @RequestMapping("view")
    public String index() {
        return "cardDiscount/list";
    }

    @RequestMapping("/datas")
    @ResponseBody
    public DataTablesOutput<TbCardDiscount> selectMoneyAndNumByDate(){
        HttpServletRequest hreq = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        int draw = Integer.parseInt(hreq.getParameter("draw"));
        int start = Integer.parseInt(hreq.getParameter("start"));
        int length = Integer.parseInt(hreq.getParameter("length"));
        List<TbCardDiscount> tbCardDiscountList=null;
        tbCardDiscountList = cardDiscountService.selectByPageNumSize(null,start,length);
        DataTablesOutput<TbCardDiscount> dtable=new DataTablesOutput<TbCardDiscount>();
        dtable.setDraw(draw);
        dtable.setRecordsTotal(Long.valueOf(cardDiscountService.dbSelectCount(null)));
        dtable.setRecordsFiltered(Long.valueOf(cardDiscountService.dbSelectCount(null)));
        dtable.setData(tbCardDiscountList);
        return dtable;
    }

    @RequestMapping("create")
    @ResponseBody
    public ResultDto create(@Valid TbCardDiscount tbCardDiscount,HttpServletRequest request) {
        return cardDiscountService.create(tbCardDiscount,request);
    }

    @RequestMapping("modify")
    @ResponseBody
    public ResultDto modify(@Valid TbCardDiscount tbCardDiscount,HttpServletRequest request) {
        return cardDiscountService.modify(tbCardDiscount,request);
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultDto delete(Long id) {
        return cardDiscountService.delete(id);
    }

    public Result getSuccessResult(Object data){
        if(data==null){
            return new Result(true);
        }
        JSONResult<Object> result = new JSONResult<Object>(data);
        return result;
    }

    @GetMapping("/list")
    @ResponseBody
    public Object findList(){
        TbCardDiscount findDiscount = new TbCardDiscount();
        findDiscount.setDeleteFlag(DeleteFlagEnum.NORMAL);
        return ResultDto.getSuccess(cardDiscountService.dbSelect(findDiscount));
    }

}
