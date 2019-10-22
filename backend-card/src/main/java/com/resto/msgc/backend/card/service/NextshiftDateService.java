package com.resto.msgc.backend.card.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.resto.msgc.backend.card.dto.BrandEntityCardChargeDto;
import com.resto.msgc.backend.card.dto.BrandOncallBusinessDto;
import com.resto.msgc.backend.card.entity.TbCardLoginUser;
import com.resto.msgc.backend.card.entity.TbCardShift;
import com.resto.msgc.backend.card.entity.TbCustomer;
import com.resto.msgc.backend.card.mapper.TbCardLoginUserMapper;
import com.resto.msgc.backend.card.mapper.TbCardRechangeMapper;
import com.resto.msgc.backend.card.mapper.TbCardShiftMapper;
import com.resto.msgc.backend.card.mapper.TbCustomerMapper;
import com.resto.msgc.backend.card.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by disvenk.dai on 2018-04-19 19:14
 */

@Service
public class NextshiftDateService {

    @Resource
    TbCardShiftMapper tbCardShiftMapper;
    @Resource
    TbCardLoginUserMapper tbCardLoginUserMapper;

    @Resource
    TbCustomerMapper tbCustomerMapper;


    /**
    *@Description:查询当天所有的值班营业数据
    *@Author:disvenk.dai
    *@Date:19:15 2018/4/19 0019
    */
    public JSONObject selectBusinessCountByDate(HttpServletRequest request,String date){
        BrandEntityCardChargeDto brandEntityCardChargeDto = tbCardShiftMapper.selectBusinessCountByDate(date);
        String tel = (String) request.getSession().getAttribute(Constant.SESSION_USER);

        List<BrandOncallBusinessDto> brandOncallBusinessDtos = tbCardShiftMapper.selectOncallBusinessCountByDate(date);
        for(BrandOncallBusinessDto brandOncallBusinessDto :brandOncallBusinessDtos){
            TbCardLoginUser t = new TbCardLoginUser();
            t.setTelephone(brandOncallBusinessDto.getTel());
            TbCardLoginUser user = tbCardLoginUserMapper.selectOne(t);
            SimpleDateFormat stf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat stf2 = new SimpleDateFormat("HH:mm:ss");
            String datetime =  stf1.format(user.getLoginFirst())+"至"+brandOncallBusinessDto.getDate();
            brandOncallBusinessDto.setDate(datetime);
        }

        JSONObject json = new JSONObject();
        json.put("json",brandEntityCardChargeDto);
        json.put("jsonList",brandOncallBusinessDtos);
        return json;
    }

    /**
    *@Description:交班前查询今日账单
    *@Author:disvenk.dai
    *@Date:16:44 2018/4/24 0024
    */
    public JSONObject selectToDayAmount(String tel){
        TbCardLoginUser t = new TbCardLoginUser();
        t.setTelephone(tel);
        TbCardLoginUser user = tbCardLoginUserMapper.selectOne(t);
        SimpleDateFormat stf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BrandEntityCardChargeDto brandEntityCardChargeDto = tbCardShiftMapper.selectStartAndEndCharge(stf.format(user.getLoginFirst()), stf.format(new Date()));
        SimpleDateFormat stf1 = new SimpleDateFormat("HH:mm:ss");
        String noCallDate = stf.format(user.getLoginFirst());
        noCallDate = noCallDate+"--"+stf1.format(new Date());
        brandEntityCardChargeDto.setTel(tel);
        brandEntityCardChargeDto.setOnCallDate(noCallDate);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("json",brandEntityCardChargeDto);
        return jsonObject;
    }

    /**
    *@Description:确认交班
    *@Author:disvenk.dai
    *@Date:19:21 2018/4/19 0019
    */
    @Transactional
    public void confirmNextShift(TbCardShift tbCardShift, String tel){
        TbCardLoginUser t = new TbCardLoginUser();
        t.setTelephone(tel);
        TbCardLoginUser tbCardLoginUser = tbCardLoginUserMapper.selectOne(t);
        SimpleDateFormat stf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BrandEntityCardChargeDto brandEntityCardChargeDto = tbCardShiftMapper.selectStartAndEndCharge(stf.format(tbCardLoginUser.getLoginFirst()), stf.format(new Date()));
        tbCardShift.setCashMoney(brandEntityCardChargeDto.getCashChargeCount());
        tbCardShift.setWechatMoney(brandEntityCardChargeDto.getWechatCardChargeCount());
        tbCardShift.setAliMoney(brandEntityCardChargeDto.getAlipayCardChargeCount());
        tbCardShift.setStarMoney(brandEntityCardChargeDto.getStarCardChargeCount());
        tbCardShift.setChequeMoney(brandEntityCardChargeDto.getChequeCardChargeCount());


        tbCardShiftMapper.insert(tbCardShift);
        TbCardLoginUser user = tbCardLoginUserMapper.findUserByTelephone(tel);
        user.setFlag(1);
        tbCardLoginUserMapper.updateByPrimaryKeySelective(user);
    }

}
