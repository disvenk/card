package com.resto.msgc.backend.card.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by disvenk.dai on 2018-04-18 14:34
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandOneCompanyAllDto implements Serializable{

    private static final long serialVersionUID = -4341165616651752460L;
    private String date;
    private String time;
    private String companyName;
    private String customerName;
    private String tel;
    private String idCard;
    private String cardPayType;
    private BigDecimal cardPayMoney;
}
