package com.resto.msgc.backend.card.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "tb_order")
public class TbOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "table_number")
    private String tableNumber;

    @ApiModelProperty("档口")
    @Column(name = "stall_name")
    private String stallName;

    @ApiModelProperty("客户总数")
    @Column(name = "customer_count")
    private Integer customerCount;

    @ApiModelProperty("财务结算时间，可以算出来的")
    @Column(name = "accounting_time")
    private Date accountingTime;

    @ApiModelProperty("订单状态")
    @Column(name = "order_state")
    private Integer orderState;

    @ApiModelProperty("生产状态")
    @Column(name = "production_status")
    private Integer productionStatus;

    @ApiModelProperty("原价")
    @Column(name = "original_amount")
    private BigDecimal originalAmount;

    @ApiModelProperty("减免金额")
    @Column(name = "reduction_amount")
    private BigDecimal reductionAmount;

    @ApiModelProperty(" 实际需要支付金额  优惠卷，余额等")
    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;

    @ApiModelProperty("订单实际支付金额 ( 微信，现金等）")
    @Column(name = "order_money")
    private BigDecimal orderMoney;

    @ApiModelProperty("支付折扣金额")
    @Column(name = "ali_pay_discount_money")
    private BigDecimal aliPayDiscountMoney;

    @ApiModelProperty("商品总数")
    @Column(name = "article_count")
    private Integer articleCount;

    @ApiModelProperty("订单流水号")
    @Column(name = "serial_number")
    private String serialNumber;

    @ApiModelProperty("确认收货时间")
    @Column(name = "confirm_time")
    private Date confirmTime;

    @ApiModelProperty(" 打印次数")
    @Column(name = "print_times")
    private Integer printTimes;

    @ApiModelProperty("是否允许取消订单（是否允许退款，微信端操作）")
    @Column(name = "allow_cancel")
    private Boolean allowCancel;

    @ApiModelProperty("是否允许评论默认否")
    @Column(name = "allow_appraise")
    private Boolean allowAppraise;

    @ApiModelProperty("订单是否取消")
    private Boolean closed;

    @ApiModelProperty("备注")
    private String remark;

    @Column(name = "create_time")
    private Date createTime;

    @ApiModelProperty("订单修改人Id")
    @Column(name = "operator_id")
    private String operatorId;

    @ApiModelProperty("客户id")
    @Column(name = "customer_id")
    private String customerId;

    @ApiModelProperty("外卖日期")
    @Column(name = "distribution_date")
    private Date distributionDate;

    @ApiModelProperty("外卖时间段")
    @Column(name = "distribution_time_id")
    private Integer distributionTimeId;

    @ApiModelProperty("配送点")
    @Column(name = "delivery_point_id")
    private Integer deliveryPointId;

    @ApiModelProperty("店铺id")
    @Column(name = "shop_detail_id")
    private String shopDetailId;

    @ApiModelProperty("订单就餐模式：（1：堂吃，3：外带）")
    @Column(name = "distribution_mode_id")
    private Integer distributionModeId;

    @Column(name = "ver_code")
    private String verCode;

    @ApiModelProperty("下单时间")
    @Column(name = "push_order_time")
    private Date pushOrderTime;

    @ApiModelProperty("打印时间")
    @Column(name = "print_order_time")
    private Date printOrderTime;

    @ApiModelProperty("叫号时间")
    @Column(name = "call_number_time")
    private Date callNumberTime;

    @ApiModelProperty("店铺模式")
    @Column(name = "order_mode")
    private Integer orderMode;

    @ApiModelProperty("品牌Id")
    @Column(name = "brand_id")
    private String brandId;

    @ApiModelProperty("订单消费总和，包括子订单的金额")
    @Column(name = "amount_with_children")
    private BigDecimal amountWithChildren;

    @ApiModelProperty("父订单id，如果不为空，代表是子订单")
    @Column(name = "parent_order_id")
    private String parentOrderId;

    @ApiModelProperty("允许继续点餐")
    @Column(name = "allow_continue_order")
    private Boolean allowContinueOrder;

    @ApiModelProperty("餐品总数")
    @Column(name = "count_with_child")
    private Integer countWithChild;

    @ApiModelProperty("最后一个子订单的时间")
    @Column(name = "last_order_time")
    private Date lastOrderTime;

    @ApiModelProperty("人数")
    @Column(name = "person_count")
    private Integer personCount;

    @ApiModelProperty("桌号")
    @Column(name = "table_no")
    private String tableNo;

    @ApiModelProperty("员工id")
    @Column(name = "employee_id")
    private String employeeId;

    @ApiModelProperty("付款方式(1微信,2支付宝,3银联4现金,5美团闪惠)")
    @Column(name = "pay_mode")
    private Integer payMode;

    @ApiModelProperty("服务费")
    @Column(name = "service_price")
    private BigDecimal servicePrice;

    @ApiModelProperty("饿了么订单Id")
    @Column(name = "eleme_order_id")
    private String elemeOrderId;

    @ApiModelProperty("餐盒总价")
    @Column(name = "meal_fee_price")
    private BigDecimal mealFeePrice;

    @ApiModelProperty("订单餐盒总数")
    @Column(name = "meal_all_number")
    private Integer mealAllNumber;

    @ApiModelProperty("订单原始金额（退菜前）")
    @Column(name = "base_money")
    private BigDecimal baseMoney;

    @ApiModelProperty("订单原始支付金额（编辑菜 退菜  会改变次数据）")
    @Column(name = "base_order_money")
    private BigDecimal baseOrderMoney;

    @ApiModelProperty("原始人数（退菜前）")
    @Column(name = "base_customer_count")
    private Integer baseCustomerCount;

    @ApiModelProperty("是否需要扫码 0不需要 1需要")
    @Column(name = "need_scan")
    private Boolean needScan;

    @ApiModelProperty("原始餐盒总数")
    @Column(name = "base_meal_all_count")
    private Integer baseMealAllCount;

    @ApiModelProperty("支付方式 0：立马支付  1：稍后支付")
    @Column(name = "pay_type")
    private Boolean payType;

    @ApiModelProperty("是否已支付 0-未支付 1-付款中 2-已付款")
    @Column(name = "is_pay")
    private Integer isPay;

    @ApiModelProperty("是否确认 0-未确认 1-已确认")
    @Column(name = "is_confirm")
    private Boolean isConfirm;

    @ApiModelProperty("是否退菜光 包括自订单   默认否")
    @Column(name = "is_refund_order")
    private Boolean isRefundOrder;

    @ApiModelProperty("找零")
    @Column(name = "give_change")
    private BigDecimal giveChange;

    @ApiModelProperty("该订单是否领取过分享优惠卷(0:未领取,1:已领取)")
    @Column(name = "is_get_share_coupon")
    private Boolean isGetShareCoupon;

    @ApiModelProperty("是不是pos端结算的订单(0:不是  1:是)")
    @Column(name = "is_pos_pay")
    private Boolean isPosPay;

    @ApiModelProperty("订单备注Ids")
    @Column(name = "order_remark_ids")
    private String orderRemarkIds;

    @ApiModelProperty("0-未打印 1-打印异常 2-异常修正 3打印正常")
    @Column(name = "print_fail_flag")
    private Boolean printFailFlag;

    @ApiModelProperty("0-未打印 1-打印异常 2-异常修正 3打印正常")
    @Column(name = "print_kitchen_flag")
    private Boolean printKitchenFlag;

    @Column(name = "customer_address_id")
    private String customerAddressId;

    @ApiModelProperty("pos折扣率")
    @Column(name = "pos_discount")
    private BigDecimal posDiscount;

    @ApiModelProperty("pos折扣中抹去的金额")
    @Column(name = "erase_money")
    private BigDecimal eraseMoney;

    @ApiModelProperty("pos折扣中不参与折扣的金额")
    @Column(name = "no_discount_money")
    private BigDecimal noDiscountMoney;

    @ApiModelProperty("组号")
    @Column(name = "group_id")
    private String groupId;

    @ApiModelProperty("该订单是否参与1:1返利活动")
    @Column(name = "is_consumption_rebate")
    private Boolean isConsumptionRebate;

    @ApiModelProperty("返利时间")
    @Column(name = "rebate_time")
    private Date rebateTime;

    @Column(name = "order_before")
    private Boolean orderBefore;

    @Column(name = "before_id")
    private String beforeId;

    @ApiModelProperty("餐具费数量")
    @Column(name = "sauce_fee_count")
    private Integer sauceFeeCount;

    @ApiModelProperty("酱料费")
    @Column(name = "sauce_fee_price")
    private BigDecimal sauceFeePrice;

    @ApiModelProperty("纸巾费数量")
    @Column(name = "towel_fee_count")
    private Integer towelFeeCount;

    @ApiModelProperty("纸巾费价格")
    @Column(name = "towel_fee_price")
    private BigDecimal towelFeePrice;

    @ApiModelProperty("酱料费数量")
    @Column(name = "tableware_fee_count")
    private Integer tablewareFeeCount;

    @ApiModelProperty("餐具费价格")
    @Column(name = "tableware_fee_price")
    private BigDecimal tablewareFeePrice;

    @ApiModelProperty("该笔订单是否使用的新版服务费  0：未使用   1：使用")
    @Column(name = "is_use_new_service")
    private Boolean isUseNewService;

    @ApiModelProperty("0：pos2.0  1：服务器")
    @Column(name = "data_origin")
    private Integer dataOrigin;

    @ApiModelProperty("pos端折扣金额")
    @Column(name = "order_pos_discount_money")
    private BigDecimal orderPosDiscountMoney;

    @ApiModelProperty("会员折扣金额")
    @Column(name = "member_discount_money")
    private BigDecimal memberDiscountMoney;

    @ApiModelProperty("会员折扣")
    @Column(name = "member_discount")
    private BigDecimal memberDiscount;

    @ApiModelProperty("存在需要确认的菜品")
    @Column(name = "need_confirm_order_item")
    private Boolean needConfirmOrderItem;

    @ApiModelProperty("lmx_Pos联网之后会以Pos本地的订单数据为基准，覆盖服务器中的订单信息，此字段用于备份，服务器订单被覆盖之前的订单信息(json格式)")
    @Column(name = "pos_back_ups")
    private String posBackUps;
}