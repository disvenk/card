package com.resto.msgc.backend.card.service;

import com.alibaba.fastjson.JSON;
import com.resto.conf.mybatis.base.BaseService;
import com.resto.core.util.DateUtil;
import com.resto.msgc.backend.card.controller.LoginController;
import com.resto.msgc.backend.card.entity.*;
import com.resto.msgc.backend.card.mapper.TbCardCustomerMapper;
import com.resto.msgc.backend.card.mapper.TbCardDiscountDetailMapper;
import com.resto.msgc.backend.card.mapper.TbCardDiscountMapper;
import com.resto.msgc.backend.card.util.CardDateUtil;
import com.resto.msgc.backend.card.util.ResultDto;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by xielc on 2017/12/12.
 */
@Service
public class CardDiscountService extends BaseService<TbCardDiscount, TbCardDiscountMapper> {

    private Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private TbCardDiscountMapper tbCardDiscountMapper;

    @Autowired
    private TbCardCustomerMapper tbCardCustomerMapper;

    @Autowired
    private TbCardDiscountDetailMapper tbCardDiscountDetailMapper;

    public List<TbCardDiscount> selectByPageNumSize(TbCardDiscount tbCardDiscount, Integer pageNum, Integer pageSize) {
        List<TbCardDiscount> list=tbCardDiscountMapper.selectByPageNumSize(tbCardDiscount, pageNum, pageSize);
        for(TbCardDiscount cardDiscount:list){
            List<TbCardDiscountDetail> plist=tbCardDiscountDetailMapper.selectdiscountId(cardDiscount.getId());
            cardDiscount.setTbCardDiscountDetailList(plist);
        }
        return list;
    }

    public ResultDto create(TbCardDiscount tbCardDiscount,HttpServletRequest request){
        Map<String,String[]> map = request.getParameterMap();
        log.info(JSON.toJSONString(map));
        List<TbCardDiscountDetail> details = new ArrayList<>();
        TbCardDiscountDetail discountDetail;
        Set<String> strings = new HashSet<>();
        int requestLength = 0;
        List<String> weekIndex = tbCardDiscount.getWeekIndex();
        if(weekIndex == null){
            return ResultDto.getError("请至少添加一个时间段");
        }
        for(int i=1;i<=weekIndex.size();i++){
            discountDetail = new TbCardDiscountDetail();
            String[] nap= map.get("discountWeeks"+i);
            if(nap == null){
                return ResultDto.getError("请选择折扣日期");
            }
            requestLength += nap.length;
            Collections.addAll(strings, nap);
            discountDetail.setDiscountWeek(StringUtils.join(nap,","));
            String[] sap= map.get("startDiscountTimes"+i);
            String[] eap= map.get("endDiscountTimes"+i);
            if(StringUtils.join(sap,",").equals(StringUtils.join(eap,","))){
                return ResultDto.getError("开始时间和结束时间不能相同");
            }
            List<String> list = new ArrayList<>();
            String time=StringUtils.join(sap,",")+","+StringUtils.join(eap,",");
            list.add(time);
            boolean state=CardDateUtil.checkOverlap(list);
            if(state){
                return ResultDto.getError("开始时间小于结束时间");
            }
            discountDetail.setDiscountTime(StringUtils.join(sap,",")+","+StringUtils.join(eap,","));
            details.add(discountDetail);
        }
        if (requestLength != strings.size()&&tbCardDiscount.getWeekIndex().size()>1){
            List<WeekAndTimeDto> list = new ArrayList<WeekAndTimeDto>();
            for(int j=1;j<=tbCardDiscount.getWeekIndex().size();j++){
                String[] nap= map.get("discountWeeks"+j);
                String[] sap= map.get("startDiscountTimes"+j);
                String[] eap= map.get("endDiscountTimes"+j);
                WeekAndTimeDto weekAndTimeDto=new WeekAndTimeDto();
                weekAndTimeDto.setWeekArray(nap);
                weekAndTimeDto.setTimeArray(StringUtils.join(sap,",")+","+StringUtils.join(eap,","));
                list.add(weekAndTimeDto);
            }
            boolean state= CardDateUtil.checkWeekAndTime(list);
            if(state){
                return  ResultDto.getError("折扣时间段不能有重复");
            }
        }
        tbCardDiscount.setCreatedAt(DateUtil.getCurrentDateString());
        tbCardDiscount.setUpdatedAt(DateUtil.getCurrentDateString());
        tbCardDiscountMapper.insertSelective(tbCardDiscount);
        Long id = tbCardDiscount.getId();
        List<String> discounts=tbCardDiscount.getDiscounts();
        if(discounts!=null){
            for(int i = 0; i <discounts.size(); i++){
                TbCardDiscountDetail tbCardDiscountDetail = new TbCardDiscountDetail();
                tbCardDiscountDetail.setDiscountId(id);
                tbCardDiscountDetail.setCreatedAt(DateUtil.getCurrentDateString());
                tbCardDiscountDetail.setUpdatedAt(DateUtil.getCurrentDateString());
                tbCardDiscountDetail.setDiscountWeek(details.get(i).getDiscountWeek());
                tbCardDiscountDetail.setDiscountTime(details.get(i).getDiscountTime());
                tbCardDiscountDetail.setDiscount(discounts.get(i));
                if("0".equals(discounts.get(i))){
                    ResultDto.getError("折扣不能为0");
                }
                tbCardDiscountDetailMapper.insertSelective(tbCardDiscountDetail);
            }
        }
        return ResultDto.getSuccess();
    }

    public ResultDto modify(TbCardDiscount tbCardDiscount,HttpServletRequest request){
        Map<String,String[]> map = request.getParameterMap();
        log.info(JSON.toJSONString(map));
        List<TbCardDiscountDetail> details = new ArrayList<>();
        TbCardDiscountDetail discountDetail;
        Set<String> strings = new HashSet<>();
        int requestLength = 0;
        for(int i=1;i<=tbCardDiscount.getWeekIndex().size();i++){
            discountDetail = new TbCardDiscountDetail();
            String[] nap= map.get("discountWeeks"+i);
            requestLength += nap.length;
            Collections.addAll(strings, nap);
            discountDetail.setDiscountWeek(StringUtils.join(nap,","));
            String[] sap= map.get("startDiscountTimes"+i);
            String[] eap= map.get("endDiscountTimes"+i);
            if(StringUtils.join(sap,",").equals(StringUtils.join(eap,","))){
                return ResultDto.getError("开始时间和结束时间不能相同");
            }
            List<String> list = new ArrayList<>();
            String time=StringUtils.join(sap,",")+","+StringUtils.join(eap,",");
            list.add(time);
            boolean state=CardDateUtil.checkOverlap(list);
            if(state){
                return ResultDto.getError("开始时间小于结束时间");
            }
            discountDetail.setDiscountTime(StringUtils.join(sap,",")+","+StringUtils.join(eap,","));
            details.add(discountDetail);
        }
        if (requestLength != strings.size()&&tbCardDiscount.getWeekIndex().size()>1){
            List<WeekAndTimeDto> list = new ArrayList<WeekAndTimeDto>();
            for(int j=1;j<=tbCardDiscount.getWeekIndex().size();j++){
                String[] nap= map.get("discountWeeks"+j);
                String[] sap= map.get("startDiscountTimes"+j);
                String[] eap= map.get("endDiscountTimes"+j);
                WeekAndTimeDto weekAndTimeDto=new WeekAndTimeDto();
                weekAndTimeDto.setWeekArray(nap);
                weekAndTimeDto.setTimeArray(StringUtils.join(sap,",")+","+StringUtils.join(eap,","));
                list.add(weekAndTimeDto);
            }
            boolean state= CardDateUtil.checkWeekAndTime(list);
            if(state){
                return  ResultDto.getError("折扣时间段不能有重复");
            }
        }
        tbCardDiscount.setUpdatedAt(DateUtil.getCurrentDateString());
        tbCardDiscountMapper.updateByPrimaryKeySelective(tbCardDiscount);
        Long id = tbCardDiscount.getId();
        tbCardDiscountDetailMapper.deleteDiscountId(id);
        List<String> discounts=tbCardDiscount.getDiscounts();
        if(discounts!=null){
            for(int i = 0; i <discounts.size(); i++){
                TbCardDiscountDetail tbCardDiscountDetail = new TbCardDiscountDetail();
                tbCardDiscountDetail.setDiscountId(id);
                tbCardDiscountDetail.setCreatedAt(DateUtil.getCurrentDateString());
                tbCardDiscountDetail.setUpdatedAt(DateUtil.getCurrentDateString());
                tbCardDiscountDetail.setDiscountWeek(details.get(i).getDiscountWeek());
                tbCardDiscountDetail.setDiscountTime(details.get(i).getDiscountTime());
                if("0".equals(discounts.get(i))){
                   return ResultDto.getError("折扣不能为0");
                }
                tbCardDiscountDetail.setDiscount(discounts.get(i));
                tbCardDiscountDetailMapper.insertSelective(tbCardDiscountDetail);
            }
        }
        return ResultDto.getSuccess();
    }

    public ResultDto delete(Long id){
        TbCardCustomer tbCardCustomer=new TbCardCustomer();
        tbCardCustomer.setDiscountId(id);
        tbCardCustomer=tbCardCustomerMapper.selectOne(tbCardCustomer);
        if(tbCardCustomer!=null){
            return ResultDto.getError("该折扣已使用不能删除");
        }
        TbCardDiscount tbCardDiscount = new TbCardDiscount();
        tbCardDiscount.setId(id);
        tbCardDiscountDetailMapper.deleteDiscountId(id);
        this.dbDelete(tbCardDiscount);
        return ResultDto.getSuccess();
    }

    public List<TbCardDiscountDetail> cardDiscountOne(String cardId) {
        return tbCardDiscountMapper.cardDiscountOne(cardId);
    }

    /**
    *@Description:通过id查找
    *@Author:disvenk.dai
    *@Date:17:36 2018/4/23 0023
    */
    public TbCardDiscount selectById(Long id){
        return tbCardDiscountMapper.selectById(id);
    }

    public TbCardDiscount selectDetailById(Long id){
        return tbCardDiscountMapper.selectDetailById(id);
    }
}
