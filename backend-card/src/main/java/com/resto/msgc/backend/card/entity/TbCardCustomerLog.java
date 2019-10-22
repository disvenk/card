package com.resto.msgc.backend.card.entity;


import com.resto.conf.db.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "tb_card_customer_log")
public class TbCardCustomerLog extends BaseEntity {
    @ApiModelProperty("品牌id")
    @Column(name = "brand_id")
    private String brandId;

    @ApiModelProperty("会员卡id")
    @Column(name = "card_customer_id")
    private Long cardCustomerId;

    @ApiModelProperty("身份证")
    @Column(name = "id_card")
    private String idCard;

    @ApiModelProperty("用户姓名")
    @Column(name = "customer_name")
    private String customerName;

    @ApiModelProperty("用户手机号")
    private String telephone;

    @ApiModelProperty("操作人手机号")
    @Column(name = "login_telephone")
    private String loginTelephone;

    @ApiModelProperty("(默认 普通卡)  可能还有 折扣卡   0为普通卡  1为员工卡   2折扣卡")
    @Column(name = "card_type")
    private Integer cardType;

    @ApiModelProperty("操作类型0新增，1修改")
    @Column(name = "operate_type")
    private Boolean operateType;
}