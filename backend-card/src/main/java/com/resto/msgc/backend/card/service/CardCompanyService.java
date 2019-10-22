package com.resto.msgc.backend.card.service;

import com.resto.conf.mybatis.base.BaseService;
import com.resto.core.util.DateUtil;
import com.resto.msgc.backend.card.controller.LoginController;
import com.resto.msgc.backend.card.dto.CompanyCustomerDto;
import com.resto.msgc.backend.card.dto.TbCardCompanyDto;
import com.resto.msgc.backend.card.entity.TbCardCompany;
import com.resto.msgc.backend.card.entity.TbCardCustomer;
import com.resto.msgc.backend.card.mapper.TbCardCompanyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xielc on 2017/12/12.
 */
@Service
public class CardCompanyService extends BaseService<TbCardCompany, TbCardCompanyMapper> {
    private Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private TbCardCompanyMapper tbCardCompanyMapper;

    public List<TbCardCompany> selectByPageNumSize(TbCardCompany tbCardCompany, Integer pageNum, Integer pageSize) {
        return tbCardCompanyMapper.selectByPageNumSize(tbCardCompany, pageNum, pageSize);
    }

    public void create(TbCardCompany tbCardCompany){
        tbCardCompany.setCreatedAt(DateUtil.getCurrentDateString());
        tbCardCompany.setUpdatedAt(DateUtil.getCurrentDateString());
        tbCardCompanyMapper.insertSelective(tbCardCompany);
    }

    public void modify(TbCardCompany tbCardCompany){
        tbCardCompany.setUpdatedAt(DateUtil.getCurrentDateString());
        tbCardCompanyMapper.updateByPrimaryKeySelective(tbCardCompany);
    }

    public void delete(Long id){
        TbCardCompany tbCardCompany = new TbCardCompany();
        tbCardCompany.setId(id);
        this.dbDelete(tbCardCompany);
    }

    public List<TbCardCompanyDto> listCompanyDatas(String companyId, int start, int length) {
       return tbCardCompanyMapper.listCompanyDatas(companyId,start,length);
    }

    public List<CompanyCustomerDto> listCompanyDetailDatas(String companyId, String telephone, int start, int length) {
        return tbCardCompanyMapper.listCompanyDetailDatas(companyId,telephone,start,length);
    }
}
