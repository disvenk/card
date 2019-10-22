package com.resto.msgc.backend.card.entity;

import com.resto.conf.db.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Table(name = "tb_card_customer")
public class TbCardCustomer extends BaseEntity {
    @ApiModelProperty("卡号")
    @Column(name = "card_id")
    private String cardId;

    @ApiModelProperty("天子星卡号")
    @Column(name = "star_card_id")
    private String starCardId;

    @ApiModelProperty("身份证")
    @Column(name = "id_card")
    private String idCard;

    @ApiModelProperty("用户姓名")
    @Column(name = "customer_name")
    private String customerName;

    @ApiModelProperty("用户手机号")
    private String telephone;

    @ApiModelProperty("关联用户id")
    @Column(name = "account_id")
    private String accountId;

    @ApiModelProperty("(默认 普通卡)  可能还有 折扣卡   0为普通卡  1为员工卡  ")
    private Integer type;

    @ApiModelProperty("折扣id")
    @Column(name = "discount_id")
    private Long discountId;

    @ApiModelProperty("活动id")
    @Column(name = "charge_setting_id")
    private String chargeSettingId;

    @Column(name = "company_id")
    private Long companyId;

    @ApiModelProperty("卡状态1正常2已挂失")
    @Column(name = "card_state")
    private Integer cardState;

    @ApiModelProperty("创建人id")
    @Column(name = "created_id")
    private String createdId;

    @ApiModelProperty("更新人id")
    @Column(name = "update_id")
    private String updateId;

    @Transient
    private BigDecimal remain;

    @Transient
    private String companyName;

    @Transient
    private String discountName;
    
}