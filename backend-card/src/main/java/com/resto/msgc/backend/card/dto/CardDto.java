package com.resto.msgc.backend.card.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by bruce on 2017-12-12 16:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDto implements Serializable {

    private static final long serialVersionUID = 4002233905072660695L;

    private String cardId;

    private String customerName;

    private String telephone;

    private String cardType;

    private String companyName;

    private String idCard;

    private BigDecimal remain;

    private String discountName;

    private Long cardCustomerId;

    private Long companyId;

    private BigDecimal returnAmount;

    private BigDecimal notReturnAmount;

}
