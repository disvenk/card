package com.resto.msgc.backend.card.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by bruce on 2017-12-12 16:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargeCardDto implements Serializable {

    private static final long serialVersionUID = 797101631350741493L;

    @ApiModelProperty(value = "卡号")
    private String cardId;

    @ApiModelProperty(value = "活动id")
    private String activityId;

    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    private String loginTelephone;

}
