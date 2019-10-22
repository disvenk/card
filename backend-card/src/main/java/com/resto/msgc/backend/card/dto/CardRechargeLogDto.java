package com.resto.msgc.backend.card.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xielc on 2017/12/19.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardRechargeLogDto implements Serializable {

    private static final long serialVersionUID = -5746621355063963972L;

    private String accountId;

    private String createTime;

    private BigDecimal remain;

    private String tradeNo;

    private Integer type;

    private BigDecimal chargeMoney;

    private BigDecimal rewardMoney;

    private String loginTelephone;
}
