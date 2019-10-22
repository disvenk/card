package com.resto.msgc.backend.card.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by disvenk.dai on 2018-04-19 16:22
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandRefundCardWorkerAllDto implements Serializable{
    private static final long serialVersionUID = 3606080657070554477L;

    private Date created;
    private String date;
    private String time;
    private String companyName;
    private String customerName;
    private String tel;
    private String idCard;
    private BigDecimal chequeMoney;
    private BigDecimal cashMoney;
    private BigDecimal wechatMoney;
    private BigDecimal aliMoney;
    private BigDecimal starMoney;
    private BigDecimal count;
}
