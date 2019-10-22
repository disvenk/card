package com.resto.msgc.backend.card.entity;

import com.resto.conf.db.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Table(name = "tb_card_rechange")
public class TbCardRechange extends BaseEntity {
    @ApiModelProperty("充值金额")
    @Column(name = "charge_money")
    private BigDecimal chargeMoney;

    @ApiModelProperty("赠送金额")
    @Column(name = "reward_money")
    private BigDecimal rewardMoney;

    @ApiModelProperty("充值剩余余额")
    @Column(name = "charge_balance")
    private BigDecimal chargeBalance;

    @ApiModelProperty("返还剩余余额")
    @Column(name = "reward_balance")
    private BigDecimal rewardBalance;

    @ApiModelProperty("工本费")
    @Column(name = "cost")
    private BigDecimal cost;

    @ApiModelProperty("活动名称")
    @Column(name = "label_text")
    private String labelText;

    private Integer sort;

    @ApiModelProperty("支付类型1微信支付2支付宝支付3现金支付4支票支付")
    @Column(name = "pay_type")
    private Integer payType;

    @Column(name = "company_id")
    private Long companyId;

    @ApiModelProperty("卡类型0:普通卡1：员工卡2:临时卡")
    @Column(name = "card_type")
    private Integer cardType;

    @Column(name = "brand_id")
    private String brandId;

    @Column(name = "charge_setting_id")
    private String chargeSettingId;

    @ApiModelProperty("会员卡id")
    @Column(name = "card_customer_id")
    private Long cardCustomerId;
}