package com.resto.msgc.backend.card.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xielc on 2018/5/15.
 */
@Data
public class StartAndEndDto implements Serializable {

    private static final long serialVersionUID = -2888222156317158965L;

    @ApiModelProperty("临时卡开卡数量")
    private Integer cardCount;

    @ApiModelProperty("支票开卡总额")
    private BigDecimal chequeCardMoney;

    @ApiModelProperty("现金开卡金额")
    private BigDecimal cashCardMoney;

    @ApiModelProperty("微信开卡金额")
    private BigDecimal wechatCardMoney;

    @ApiModelProperty("支付宝金额开卡")
    private BigDecimal aliPayCardMoney;

    @ApiModelProperty("支票开卡总额")
    private BigDecimal starCardMoney;
}
