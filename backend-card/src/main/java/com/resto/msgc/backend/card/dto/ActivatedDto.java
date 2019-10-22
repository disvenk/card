package com.resto.msgc.backend.card.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xielc on 2018/5/9.
 */
@Data
public class ActivatedDto implements Serializable {

    private static final long serialVersionUID = 1893290422688182378L;

    private String cardId;

    private String customerName;

    private String telephone;

    private String cardType;

    private String companyName;

    private String idCard;

    private BigDecimal remain;

    private String discountName;

    private BigDecimal chargeMoney;

    private BigDecimal rewardMoney;

    private Long cardCustomerId;

    private Long companyId;
}
