package com.resto.msgc.backend.card.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xielc on 2018/4/19.
 */
@Data
public class PinCardDto implements Serializable{

    private static final long serialVersionUID = 741672040542737142L;

    @ApiModelProperty("卡号")
    private String cardId;

    @ApiModelProperty("公司id")
    private Long companyId;

    @ApiModelProperty("卡类型0:普通卡1：员工卡2:临时卡")
    private Integer cardType;

    @ApiModelProperty("微信退款金额")
    private BigDecimal wechatMoney;

    @ApiModelProperty("支付宝退款金额")
    private BigDecimal aliMoney;

    @ApiModelProperty("现金退款金额")
    private BigDecimal cashMoney;

    @ApiModelProperty("支票退款金额")
    private BigDecimal chequeMoney;

    @ApiModelProperty("天子星退款金额")
    private BigDecimal starMoney;
}
