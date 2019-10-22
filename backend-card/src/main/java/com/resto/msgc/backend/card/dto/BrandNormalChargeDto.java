package com.resto.msgc.backend.card.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by disvenk.dai on 2018-04-18 17:32
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandNormalChargeDto implements Serializable{
    private static final long serialVersionUID = 3958746913035960332L;

    private BigDecimal chareCount;
    private Long chargeNum;
    private BigDecimal cashChargeCount;
    private BigDecimal chequeCardChargeCount;
    private BigDecimal wechatCardChargeCount;
    private BigDecimal alipayCardChargeCount;
    private BigDecimal starCardChargeCount;


}
