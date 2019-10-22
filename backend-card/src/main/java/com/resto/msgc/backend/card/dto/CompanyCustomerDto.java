package com.resto.msgc.backend.card.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xielc on 2018/4/16.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyCustomerDto implements Serializable {

    private static final long serialVersionUID = -3239028009407760062L;

    @ApiModelProperty("会员卡id")
    private String cardCustomerId;

    @ApiModelProperty("卡号")
    private String cardId;

    @ApiModelProperty("折扣名称")
    private String discountName;

    @ApiModelProperty("用户姓名")
    private String customerName;

    @ApiModelProperty("用户手机号")
    private String telephone;

    @ApiModelProperty("身份证")
    private String idCard;

    @ApiModelProperty("卡状态")
    private Integer cardState;

    @ApiModelProperty("账户余额")
    private BigDecimal remain;
}
