package com.resto.msgc.backend.card.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by disvenk.dai on 2018-04-19 10:45
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandEntityPinCardDataDto implements Serializable{
    private static final long serialVersionUID = -5542968288855991299L;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("销卡总数")
    private Long pinCardCount;

    @ApiModelProperty("退款总额")
    private BigDecimal count;

    @ApiModelProperty("现金退款总额")
    private BigDecimal cashMoney;

    @ApiModelProperty("支票退款总额")
    private BigDecimal chequeMoney;

    @ApiModelProperty("微信退款总额")
    private BigDecimal wechatMoney;

    @ApiModelProperty("支付宝退款总额")
    private BigDecimal aliMoney;

    private BigDecimal starMoney;

}
