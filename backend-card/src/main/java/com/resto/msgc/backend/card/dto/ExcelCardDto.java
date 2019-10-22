package com.resto.msgc.backend.card.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by xielc on 2018/5/8.
 */
@Data
public class ExcelCardDto implements Serializable {

    @ApiModelProperty("卡号")
    private String cardId;

    @ApiModelProperty("顾客姓名")
    private String customerName;

    @ApiModelProperty("联系电话")
    private String telephone;

    @ApiModelProperty("(默认 普通卡)  可能还有 折扣卡   0为普通卡  1为员工卡 2为临时卡")
    private String cardType;

    @ApiModelProperty("折扣0无折扣 1有折扣")
    private String discount;

    @ApiModelProperty("公司id")
    private String companyId;

    @ApiModelProperty("充值金额")
    private String chargeMoney;

    @ApiModelProperty("赠送金额")
    private String rewardMoney;
}
