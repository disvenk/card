package com.resto.msgc.backend.card.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by disvenk.dai on 2018-04-19 19:10
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandOncallBusinessDto implements Serializable{
    private static final long serialVersionUID = -196525918521776327L;

    private String date;
    private BigDecimal cashMoney;
    private BigDecimal aliMoney;
    private BigDecimal wechatMoney;
    private BigDecimal chequeMoney;
    private BigDecimal starMoney;
    private BigDecimal actualCash;
    private String tel;
}
