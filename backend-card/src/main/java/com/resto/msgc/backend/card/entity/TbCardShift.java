package com.resto.msgc.backend.card.entity;

import com.resto.conf.db.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "tb_card_shift")
public class TbCardShift extends BaseEntity {
    @ApiModelProperty("现金")
    @Column(name = "cash_money")
    private BigDecimal cashMoney;

    @ApiModelProperty("微信")
    @Column(name = "wechat_money")
    private BigDecimal wechatMoney;

    @ApiModelProperty("支付宝")
    @Column(name = "ali_money")
    private BigDecimal aliMoney;

    @ApiModelProperty("支票")
    @Column(name = "cheque_money")
    private BigDecimal chequeMoney;

    @ApiModelProperty("天子星")
    @Column(name = "star_money")
    private BigDecimal starMoney;

    @ApiModelProperty("实收")
    @Column(name = "actual_cash")
    private BigDecimal actualCash;

    @ApiModelProperty("创建id")
    @Column(name = "created_id")
    private String createdId;

    @ApiModelProperty("更新id")
    @Column(name = "update_id")
    private String updateId;
}