package com.resto.msgc.backend.card.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "tb_charge_order")
public class TbChargeOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ApiModelProperty("充值金额")
    @Column(name = "charge_money")
    private BigDecimal chargeMoney;

    @ApiModelProperty("返还金额")
    @Column(name = "reward_money")
    private BigDecimal rewardMoney;

    @ApiModelProperty("订单状态1完成0未完成")
    @Column(name = "order_state")
    private Byte orderState;

    @ApiModelProperty("创建时间")
    @Column(name = "create_time")
    private Date createTime;

    @ApiModelProperty("完成时间")
    @Column(name = "finish_time")
    private Date finishTime;

    @ApiModelProperty("微信用户Id")
    @Column(name = "customer_id")
    private String customerId;

    @ApiModelProperty("品牌Id")
    @Column(name = "brand_id")
    private String brandId;

    @ApiModelProperty("店铺Id")
    @Column(name = "shop_detail_id")
    private String shopDetailId;

    @ApiModelProperty("充值剩余余额")
    @Column(name = "charge_balance")
    private BigDecimal chargeBalance;

    @ApiModelProperty("返还剩余余额")
    @Column(name = "reward_balance")
    private BigDecimal rewardBalance;

    @ApiModelProperty("总余额")
    @Column(name = "total_balance")
    private BigDecimal totalBalance;

    @Column(name = "number_day_now")
    private Integer numberDayNow;

    @ApiModelProperty("每笔赠送金额")
    @Column(name = "arrival_amount")
    private BigDecimal arrivalAmount;

    @Column(name = "end_amount")
    private BigDecimal endAmount;

    @ApiModelProperty("0pos充值1微信充值2普通充值3活动充值")
    private Integer type;

    @ApiModelProperty("充值活动Id")
    @Column(name = "charge_setting_id")
    private String chargeSettingId;
}