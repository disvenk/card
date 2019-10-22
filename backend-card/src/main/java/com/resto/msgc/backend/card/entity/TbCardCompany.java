package com.resto.msgc.backend.card.entity;

import com.resto.conf.db.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "tb_card_company")
public class TbCardCompany extends BaseEntity {
    @ApiModelProperty("公司名称")
    @Column(name = "company_name")
    private String companyName;

    @ApiModelProperty("联系人")
    @Column(name = "contact_name")
    private String contactName;

    @ApiModelProperty("联系人电话")
    @Column(name = "contact_mobile")
    private String contactMobile;
}