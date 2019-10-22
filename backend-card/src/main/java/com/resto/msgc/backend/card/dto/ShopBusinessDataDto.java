package com.resto.msgc.backend.card.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by disvenk.dai on 2018-04-16 19:15
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopBusinessDataDto implements Serializable{
    private static final long serialVersionUID = -60442938155005892L;
    private String shopName;
    private String date;
    private String orderCount;
    private String originalAmount;
    private String paymentAmount;
    private String reductionAmount;

}
