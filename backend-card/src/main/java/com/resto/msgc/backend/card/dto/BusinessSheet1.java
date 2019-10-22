package com.resto.msgc.backend.card.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by disvenk.dai on 2018-05-19 10:39
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessSheet1 implements Serializable{
    private static final long serialVersionUID = 6409423329819830663L;

    private String shopName;
    private String date;
    private Long orderCount;
    private BigDecimal originalAmount;//订单总额
    private BigDecimal paymentAmount;//实收金额
    private BigDecimal discountMoney;//订单折扣
    private BigDecimal refundMoney;//退款金额
    private BigDecimal reductionAmount;//总折扣

}
