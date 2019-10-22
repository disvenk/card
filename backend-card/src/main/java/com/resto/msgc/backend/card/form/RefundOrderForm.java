package com.resto.msgc.backend.card.form;

import com.resto.msgc.backend.card.entity.TbOrderRefundRemark;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by disvenk.dai on 2018-05-11 17:40
 */
public class RefundOrderForm {
    public String orderId;
    public BigDecimal restoBoxFee;
    public BigDecimal serviceFee;
    public List<RefundArticle> articles = new ArrayList<>();
    public List<TbOrderRefundRemark> orderRefundRemarks = new ArrayList<>();
    public BigDecimal refundAmountMoney;
}
