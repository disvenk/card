package com.resto.msgc.backend.card.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xielc on 2018/5/9.
 */
@Data
public class ActivatedCardDto implements Serializable {

    private static final long serialVersionUID = -784844587518926383L;

    @ApiModelProperty("卡号")
    private String cardId;

    @ApiModelProperty("账户余额")
    private BigDecimal remain;

    @ApiModelProperty("充值余额")
    private BigDecimal chargeMoney;

    @ApiModelProperty("充值赠送余额")
    private BigDecimal rewardMoney;

    @ApiModelProperty("会员卡id")
    private Long cardCustomerId;

}
