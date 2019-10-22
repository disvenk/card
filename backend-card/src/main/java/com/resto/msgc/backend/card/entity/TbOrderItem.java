package com.resto.msgc.backend.card.entity;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "tb_order_item")
public class TbOrderItem  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ApiModelProperty("菜品名称")
    @Column(name = "article_name")
    private String articleName;

    @ApiModelProperty("商品描述")
    @Column(name = "article_designation")
    private String articleDesignation;

    @ApiModelProperty("订单项数量")
    private Integer count;

    @ApiModelProperty("原价")
    @Column(name = "original_price")
    private BigDecimal originalPrice;

    @ApiModelProperty("单价（和数量参与计算的价格）")
    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @ApiModelProperty("餐品原始单价（和数量参与计算的价格）  ")
    @Column(name = "base_unit_price")
    private BigDecimal baseUnitPrice;

    @ApiModelProperty("最终计算价格 （数量x单价）")
    @Column(name = "final_price")
    private BigDecimal finalPrice;

    @ApiModelProperty("订单项备注,目前用于保存订单的折扣")
    private String remark;

    @ApiModelProperty("保存订单的pos折扣")
    @Column(name = "pos_discount")
    private String posDiscount;

    @ApiModelProperty("订单项顺序")
    private Integer sort;

    @ApiModelProperty("订单项状态 1 正常 其它。。")
    private Integer status;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "article_id")
    private String articleId;

    @ApiModelProperty("订单项类型 1. 餐品 2. 餐品某个单位")
    private Integer type;

    @ApiModelProperty("父订单id")
    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "create_time")
    private Date createTime;

    @ApiModelProperty("套餐子项id")
    @Column(name = "meal_item_id")
    private Long mealItemId;

    @ApiModelProperty("出餐厨房id")
    @Column(name = "kitchen_id")
    private String kitchenId;

    @Column(name = "recommend_id")
    private String recommendId;

    @ApiModelProperty("原始数量")
    @Column(name = "orgin_count")
    private Integer orginCount;

    @ApiModelProperty("原始数量")
    @Column(name = "refund_count")
    private Integer refundCount;

    @ApiModelProperty("餐盒数量")
    @Column(name = "meal_fee_number")
    private Integer mealFeeNumber;

    @ApiModelProperty("pos端修改未完成订单菜品数量的值")
    @Column(name = "change_count")
    private Integer changeCount;

    @ApiModelProperty("是否漏厨打 0-没 1-漏")
    @Column(name = "print_fail_flag")
    private Boolean printFailFlag;

    @ApiModelProperty("用户id")
    @Column(name = "customer_id")
    private String customerId;

    @ApiModelProperty("重量")
    private BigDecimal weight;

    @ApiModelProperty("需要提醒")
    @Column(name = "need_remind")
    private Boolean needRemind;
}