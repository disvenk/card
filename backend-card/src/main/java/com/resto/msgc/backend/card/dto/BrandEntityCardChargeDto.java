package com.resto.msgc.backend.card.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by disvenk.dai on 2018-04-18 16:51
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandEntityCardChargeDto implements Serializable{
    private static final long serialVersionUID = -690867956416346745L;

    private String created;
    private BigDecimal chareCount;
    private BigDecimal workerCardChargeCount;
    private BigDecimal normalCardChargeCount;
    private BigDecimal tempCardChargeCount;
    private BigDecimal cashChargeCount;
    private BigDecimal chequeCardChargeCount;
    private BigDecimal wechatCardChargeCount;
    private BigDecimal alipayCardChargeCount;
    private BigDecimal starCardChargeCount;
    private BigDecimal foodmemberRecharge;
    private BigDecimal starRecharge;

    private String tel;
    private String onCallDate;

}
