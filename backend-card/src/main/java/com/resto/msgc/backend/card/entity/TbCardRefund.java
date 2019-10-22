package com.resto.msgc.backend.card.entity;

import com.resto.conf.db.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "tb_card_refund")
public class TbCardRefund extends BaseEntity {
    @ApiModelProperty("会员卡id")
    @Column(name = "card_customer_id")
    private Long cardCustomerId;

    @ApiModelProperty("公司id")
    @Column(name = "company_id")
    private Long companyId;

    @ApiModelProperty("微信退款金额")
    @Column(name = "wechat_money")
    private BigDecimal wechatMoney;

    @ApiModelProperty("支付宝退款金额")
    @Column(name = "ali_money")
    private BigDecimal aliMoney;

    @ApiModelProperty("现金退款金额")
    @Column(name = "cash_money")
    private BigDecimal cashMoney;

    @ApiModelProperty("支票退款金额")
    @Column(name = "cheque_money")
    private BigDecimal chequeMoney;

    @ApiModelProperty("天子星退款金额")
    @Column(name = "star_money")
    private BigDecimal starMoney;

    @ApiModelProperty("卡类型0:普通卡1：员工卡2:临时卡")
    @Column(name = "card_type")
    private Integer cardType;

}