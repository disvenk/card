package com.resto.msgc.backend.card.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by disvenk.dai on 2018-04-16 16:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandBusinessDataDto implements Serializable{
    private static final long serialVersionUID = 1374681131399063200L;

    private String shopName;
    private String orderCount;
    private String originalAmount;
    private String paymentAmount;
    private String reductionAmount;
    private BigDecimal refundMoney;


}
