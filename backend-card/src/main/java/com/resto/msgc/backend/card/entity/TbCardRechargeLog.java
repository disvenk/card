package com.resto.msgc.backend.card.entity;

import com.resto.conf.db.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "tb_card_recharge_log")
public class TbCardRechargeLog extends BaseEntity {
    @Column(name = "charge_order_id")
    private String chargeOrderId;

    @ApiModelProperty("流水号")
    @Column(name = "trade_no")
    private String tradeNo;

    @ApiModelProperty("会员卡id")
    @Column(name = "card_customer_id")
    private Long cardCustomerId;

    @ApiModelProperty("用户姓名")
    @Column(name = "customer_name")
    private String customerName;

    @ApiModelProperty("用户手机号")
    private String telephone;

    @ApiModelProperty("操作人手机号")
    @Column(name = "login_telephone")
    private String loginTelephone;

    @ApiModelProperty("0活动充值，1普通充值")
    private Integer type;

    @ApiModelProperty("充值后余额")
    private BigDecimal remain;

    @ApiModelProperty("支付返回json")
    private String payData;

    @Column(name = "created_id")
    private String createdId;

    @Column(name = "updated_id")
    private String updatedId;
}