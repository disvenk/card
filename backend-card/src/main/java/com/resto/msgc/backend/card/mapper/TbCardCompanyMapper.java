package com.resto.msgc.backend.card.mapper;

import com.resto.conf.mybatis.util.MyMapper;
import com.resto.msgc.backend.card.dto.CompanyCustomerDto;
import com.resto.msgc.backend.card.dto.TbCardCompanyDto;
import com.resto.msgc.backend.card.entity.TbCardCompany;
import com.resto.msgc.backend.card.entity.TbCardCustomer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbCardCompanyMapper extends MyMapper<TbCardCompany> {

    List<TbCardCompany> selectByPageNumSize(@Param("tbCardCompany") TbCardCompany tbCardCompany, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

    List<TbCardCompanyDto> listCompanyDatas(@Param("companyId")String companyId,@Param("pageNum")int pageNum,@Param("pageSize")int pageSize);

    List<CompanyCustomerDto> listCompanyDetailDatas(@Param("companyId")String companyId, @Param("telephone")String telephone, @Param("pageNum")int pageNum,@Param("pageSize")int pageSize);
}