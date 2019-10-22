package com.resto.msgc.backend.card.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "tb_order_payment_item")
public class TbOrderPaymentItem implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ApiModelProperty("支付时间")
    @Column(name = "pay_time")
    private Date payTime;

    @ApiModelProperty("支付金额")
    @Column(name = "pay_value")
    private BigDecimal payValue;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("支付模式 ")
    @Column(name = "payment_mode_id")
    private Integer paymentModeId;

    @Column(name = "order_id")
    private String orderId;

    @ApiModelProperty("是否用于分红")
    @Column(name = "is_use_bonus")
    private Boolean isUseBonus;

    @ApiModelProperty("用来存放余额优惠券支付是时Id")
    @Column(name = "to_pay_id")
    private String toPayId;

    @Column(name = "result_data")
    private String resultData;
}