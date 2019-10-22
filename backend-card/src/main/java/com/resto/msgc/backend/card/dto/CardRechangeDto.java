package com.resto.msgc.backend.card.dto;

import com.resto.conf.db.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CardRechangeDto extends BaseEntity {

    @ApiModelProperty("充值金额")
    private BigDecimal chargeMoney;

    @ApiModelProperty("赠送金额")
    private BigDecimal rewardMoney;

    @ApiModelProperty("充值剩余余额")
    private BigDecimal chargeBalance;

    @ApiModelProperty("返还剩余余额")
    private BigDecimal rewardBalance;

    @ApiModelProperty("工本费")
    private BigDecimal cost;

    @ApiModelProperty("支付类型1微信支付2支付宝支付3现金支付4支票支付")
    private Integer payType;

    @ApiModelProperty("公司id")
    private Long companyId;

    @ApiModelProperty("充值活动id")
    private String chargeSettingId;

    @ApiModelProperty("卡类型0:普通卡1：员工卡2:临时卡")
    private Integer cardType;

    @ApiModelProperty("卡号")
    private String cardId;

    @ApiModelProperty("支付返回json")
    private String payData;

}