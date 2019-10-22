package com.resto.msgc.backend.card.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by bruce on 2017-12-12 11:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenCardDto implements Serializable {

    private static final long serialVersionUID = -3594367568009642971L;

    @ApiModelProperty("卡号")
    private String cardId;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("姓名")
    private String customerName;

    @ApiModelProperty("手机号")
    private String telephone;

    @ApiModelProperty("验证码")
    private String code;

    @ApiModelProperty("开卡类型0普通卡1员工卡")
    private Integer cardType;

    @ApiModelProperty("公司id")
    private Long companyId;

    @ApiModelProperty("折扣id")
    private Long discountId;

    private String loginTelephone;

    @ApiModelProperty("充值金额")
    private BigDecimal remain;

}
