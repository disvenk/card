package com.resto.msgc.backend.card.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xielc on 2018/4/19.
 */
@Data
public class PinCardMoneyDto implements Serializable {

    private static final long serialVersionUID = 1588936169616252296L;

    @ApiModelProperty("支付类型1微信支付2支付宝支付3现金支付4支票支付")
    private Integer payType;

    @ApiModelProperty("各项支付剩余的钱")
    private BigDecimal balanceMoney;

    @ApiModelProperty("工本费")
    private BigDecimal cost;
}
