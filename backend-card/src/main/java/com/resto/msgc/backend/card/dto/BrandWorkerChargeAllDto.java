package com.resto.msgc.backend.card.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by disvenk.dai on 2018-04-18 18:16
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandWorkerChargeAllDto implements Serializable{
    private static final long serialVersionUID = 3617233291855001539L;

    private String companyName;
    private Long chargeNum;
    private BigDecimal chargeCount;
    private BigDecimal cashChargeCount;
    private BigDecimal chequeCardChargeCount;
    private BigDecimal wechatCardChargeCount;
    private BigDecimal alipayCardChargeCount;
    private BigDecimal starCardChargeCount;
}
