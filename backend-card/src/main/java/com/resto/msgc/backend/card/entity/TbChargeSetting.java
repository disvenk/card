package com.resto.msgc.backend.card.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "tb_charge_setting")
public class TbChargeSetting implements Serializable {
    private static final long serialVersionUID = -2329966731390427597L;
    @ApiModelProperty("活动ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ApiModelProperty("充值金额")
    @Column(name = "charge_money")
    private BigDecimal chargeMoney;

    @ApiModelProperty("赠送金额")
    @Column(name = "reward_money")
    private BigDecimal rewardMoney;

    @ApiModelProperty("是否显示到Cell栏上")
    @Column(name = "show_in")
    private Byte showIn;

    @ApiModelProperty("显示的文本")
    @Column(name = "label_text")
    private String labelText;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("活动状态")
    private Byte state;

    @ApiModelProperty("创建时间")
    @Column(name = "create_time")
    private Date createTime;

    @ApiModelProperty("品牌id")
    @Column(name = "brand_id")
    private String brandId;

    @ApiModelProperty("店铺id")
    @Column(name = "shop_detail_id")
    private String shopDetailId;

    @ApiModelProperty("到账的天数（1为当场到清）")
    @Column(name = "number_day")
    private Integer numberDay;

    @ApiModelProperty("1首冲 2多冲 1,2是多冲+首冲")
    @Column(name = "first_charge")
    private String firstCharge;
}