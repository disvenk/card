package com.resto.msgc.backend.card.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xielc on 2017/12/22.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseHistoryDto implements Serializable {

    private static final long serialVersionUID = -2542696296420147291L;

    private String createTime;

    private String serialNumber;

    private BigDecimal payValue= BigDecimal.ZERO;//实收金额

    private BigDecimal originalAmount= BigDecimal.ZERO;//订单金额

    private BigDecimal discountPayValue= BigDecimal.ZERO;//储值卡折扣金额

    private BigDecimal accountPayValue= BigDecimal.ZERO;//充值账户支付

    private BigDecimal rechargeGiftPayValue= BigDecimal.ZERO;//充值赠送支付

    private BigDecimal wechatPayValue= BigDecimal.ZERO;//微信支付

    private BigDecimal aliPayValue= BigDecimal.ZERO;//支付宝支付

    private BigDecimal couponPayValue= BigDecimal.ZERO;//优惠券支付

    private BigDecimal redEnvelopePayValue= BigDecimal.ZERO;//红包支付

    private BigDecimal waitRedEnvelopePayValue= BigDecimal.ZERO;//等位红包支付

    private BigDecimal swingCardPayValue= BigDecimal.ZERO;//刷卡支付

    private BigDecimal cashPayValue= BigDecimal.ZERO;//现金实收

    private BigDecimal shanHuiPayValue= BigDecimal.ZERO;//闪惠支付

    private BigDecimal memberPayValue= BigDecimal.ZERO;//会员支付

    private BigDecimal returnOrderPayValue= BigDecimal.ZERO;//退菜返还红包

    private BigDecimal otherPayValue= BigDecimal.ZERO;//其他支付方式

    private BigDecimal cardAccountPayValue= BigDecimal.ZERO;//实体卡充值本金支付

    private BigDecimal cardRechargeGiftPayValue= BigDecimal.ZERO;//实体卡充值赠送

    private BigDecimal cashRefund= BigDecimal.ZERO;//现金退款

    private BigDecimal cardRefund= BigDecimal.ZERO;//实体卡退款

    private BigDecimal orderPosDiscountMoney= BigDecimal.ZERO;//pos端折扣

    private BigDecimal memberDiscountMoney= BigDecimal.ZERO;//会员折扣

    private BigDecimal eraseMoney= BigDecimal.ZERO;//抹零
}
