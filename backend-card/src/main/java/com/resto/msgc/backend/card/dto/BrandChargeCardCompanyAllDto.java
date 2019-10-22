package com.resto.msgc.backend.card.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by disvenk.dai on 2018-04-18 20:14
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandChargeCardCompanyAllDto implements Serializable{
    private static final long serialVersionUID = 2527100737764947322L;

    private Long id;
    private String date;
    private String time;
    private String companyName;
    private String customerName;
    private String tel;
    private String idCard;
    private String cardPayType;
    private BigDecimal chargeMoney;
    private Date created;

}
