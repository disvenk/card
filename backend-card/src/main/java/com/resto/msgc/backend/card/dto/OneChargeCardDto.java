package com.resto.msgc.backend.card.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xielc on 2018/4/16.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OneChargeCardDto implements Serializable{

    private static final long serialVersionUID = 997643525947770404L;

    @ApiModelProperty("充值金额")
    private BigDecimal chargeMoney;

    @ApiModelProperty("公司id")
    private Long companyId;

    @ApiModelProperty("会员卡id")
    private Long cardCustomerId;

    @ApiModelProperty("充值类型")
    private Integer payType;
}
