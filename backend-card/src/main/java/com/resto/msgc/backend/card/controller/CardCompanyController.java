package com.resto.msgc.backend.card.controller;


import com.resto.conf.enums.DeleteFlagEnum;
import com.resto.msgc.backend.card.dto.CompanyCustomerDto;
import com.resto.msgc.backend.card.dto.TbCardCompanyDto;
import com.resto.msgc.backend.card.entity.TbCardCompany;
import com.resto.msgc.backend.card.entity.TbCardCustomer;
import com.resto.msgc.backend.card.service.CardCompanyService;
import com.resto.msgc.backend.card.service.CardCustomService;
import com.resto.msgc.backend.card.util.DataTablesOutput;
import com.resto.msgc.backend.card.util.Result;
import com.resto.msgc.backend.card.util.ResultDto;
import io.swagger.annotations.Api;
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
import java.util.List;

/**
 * Created by xielc on 2017/12/12.
 */
@Api
@Controller
@RequestMapping("cardCompany")
public class CardCompanyController {
    protected Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private CardCompanyService cardCompanyService;

    @Autowired
    private CardCustomService cardCustomService;

    @RequestMapping("view")
    public String index() {
        return "cardCompany/list";
    }

    @RequestMapping("/datas")
    @ResponseBody
    public DataTablesOutput<TbCardCompany> selectMoneyAndNumByDate(){
        HttpServletRequest hreq = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        int draw = Integer.parseInt(hreq.getParameter("draw"));
        int start = Integer.parseInt(hreq.getParameter("start"));
        int length = Integer.parseInt(hreq.getParameter("length"));
        List<TbCardCompany> tbCardCompanyList=null;
        tbCardCompanyList = cardCompanyService.selectByPageNumSize(null,start,length);
        DataTablesOutput<TbCardCompany> dtable=new DataTablesOutput<TbCardCompany>();
        dtable.setDraw(draw);
        dtable.setRecordsTotal(Long.valueOf(cardCompanyService.dbSelectCount(null)));
        dtable.setRecordsFiltered(Long.valueOf(cardCompanyService.dbSelectCount(null)));
        dtable.setData(tbCardCompanyList);
        return dtable;
    }

    @RequestMapping("create")
    @ResponseBody
    public Result create(@Valid TbCardCompany tbCardCompany) {
        cardCompanyService.create(tbCardCompany);
        return Result.getSuccess();
    }

    @RequestMapping("modify")
    @ResponseBody
    public Result modify(@Valid TbCardCompany tbCardCompany) {
        cardCompanyService.modify(tbCardCompany);
        return Result.getSuccess();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Result delete(Long id) {
        cardCompanyService.delete(id);
        return Result.getSuccess();
    }

    @GetMapping("/list")
    @ResponseBody
    public Object findList(){
        TbCardCompany findCompany = new TbCardCompany();
        findCompany.setDeleteFlag(DeleteFlagEnum.NORMAL);
        return ResultDto.getSuccess(cardCompanyService.dbSelect(findCompany));
    }

    @RequestMapping("listCompanyDatas")
    @ResponseBody
    public DataTablesOutput<TbCardCompanyDto> listCompanyDatas(String companyId){
        HttpServletRequest hreq = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        int draw = Integer.parseInt(hreq.getParameter("draw"));
        int start = Integer.parseInt(hreq.getParameter("start"));
        int length = Integer.parseInt(hreq.getParameter("length"));
        List<TbCardCompanyDto> tbCardCompanyDtoList = cardCompanyService.listCompanyDatas(companyId,start,length);
        TbCardCompany tbCardCompany=new TbCardCompany();
        if(companyId!=null){
            tbCardCompany.setId(Long.valueOf(companyId));
        }
        DataTablesOutput<TbCardCompanyDto> dtable=new DataTablesOutput<TbCardCompanyDto>();
        dtable.setDraw(draw);
        dtable.setRecordsTotal(Long.valueOf(cardCompanyService.dbSelectCount(tbCardCompany)));
        dtable.setRecordsFiltered(Long.valueOf(cardCompanyService.dbSelectCount(tbCardCompany)));
        dtable.setData(tbCardCompanyDtoList);
        return dtable;
    }

    @RequestMapping("listCompanyDetailDatas")
    @ResponseBody
    public DataTablesOutput<CompanyCustomerDto> listCompanyDetailDatas(String companyId,String telephone){
        HttpServletRequest hreq = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        int draw = Integer.parseInt(hreq.getParameter("draw"));
        int start = Integer.parseInt(hreq.getParameter("start"));
        int length = Integer.parseInt(hreq.getParameter("length"));
        List<CompanyCustomerDto> companyCustomerDtoList = cardCompanyService.listCompanyDetailDatas(companyId,telephone,start,length);
        TbCardCustomer tbCardCustomer=new TbCardCustomer();
        if(companyId!=null){
            tbCardCustomer.setCompanyId(Long.valueOf(companyId));
        }
        if(telephone!=null){
            tbCardCustomer.setTelephone(telephone);
        }
        DataTablesOutput<CompanyCustomerDto> dtable=new DataTablesOutput<CompanyCustomerDto>();
        dtable.setDraw(draw);
        dtable.setRecordsTotal(Long.valueOf(cardCustomService.dbSelectCount(tbCardCustomer)));
        dtable.setRecordsFiltered(Long.valueOf(cardCustomService.dbSelectCount(tbCardCustomer)));
        dtable.setData(companyCustomerDtoList);
        return dtable;
    }

}
