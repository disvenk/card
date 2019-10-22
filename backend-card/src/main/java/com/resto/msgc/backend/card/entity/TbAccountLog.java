package com.resto.msgc.backend.card.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import static com.netflix.appinfo.AmazonInfo.MetaDataKey.accountId;

@Data
@Table(name = "tb_account_log")
public class TbAccountLog implements Serializable {
    private static final long serialVersionUID = -6136302459796702867L;

    public static final Integer SOURCE_PAYMENT = -1;  //支付订 单
    public static final Integer APPRAISE_RED_PACKAGE=1; //评论红包奖励余额
    public static final Integer SOURCE_CHARGE=2;	  //充值余额
    public static final Integer SOURCE_CHARGE_REWARD=3; //充值赠送余额
    public static final Integer SOURCE_SHARE_REWARD=4;  //分享返利余额
    public static final Integer SOURCE_CANCEL_ORDER = 5; //取消订单返还余额
    public static final Integer CHARGE_PAY_REFUND = 6;  //取消订单返回充值的金额
    public static final Integer REWARD_PAY_REFUND = 7;  //取消订单返回充值赠送的金额
    public static final Integer REFUND_ARTICLE_RED_PACKAGE = 7;  //退菜红包返回余额
    public static final Integer THIRD_MONEY = 8;  //第三方账户储值余额
    public static final Integer FREEZE_RED_MONEY = 9;  //消费1:1返利 红包余额   有冻结期
    public static final Integer WITHDRAWALS = 9;  //提现
    public static final Integer CARD_CHARGE = 10;  //储蓄卡的充值金额
    public static final Integer CARD_CHARGE_REWARD = 11;  //储蓄卡的充值赠送金额
    public static final Integer CARD_SOURCE_CHARGE=12;	  //卡充值余额
    public static final Integer CARD_SOURCE_CHARGE_REWARD=13; //卡充值赠送余额
    public static final Integer CARD_CHARGE_PAY_REFUND = 14;  //取消订单返回卡充值的金额
    public static final Integer CARD_REWARD_PAY_REFUND = 15;  //取消订单返回卡充值赠送的金额
    public static final Integer ENTITY_CARD_CHARGE= 16;  //实体卡充值和赠送金额
    public static final Integer START_CARD_CHARGE = 17;  //天子星充值金额

    @ApiModelProperty("账户日志ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ApiModelProperty("操作金额")
    private BigDecimal money;

    @ApiModelProperty("记录创建时间")
    @Column(name = "create_time")
    private Date createTime;

    @ApiModelProperty("支付类型 - 付款/扣款")
    @Column(name = "payment_type")
    private Integer paymentType;

    @ApiModelProperty("操作后的余额")
    private BigDecimal remain;

    @ApiModelProperty("操作备注")
    private String remark;

    @ApiModelProperty("账户余额ID")
    @Column(name = "account_id")
    private String accountId;

    @ApiModelProperty("1. 来自红包余额2. 来自充值余额3. 来自充值赠送余额")
    private Integer source;

    @ApiModelProperty("店铺id")
    @Column(name = "shop_detail_id")
    private String shopDetailId;

    @ApiModelProperty("订单id  ")
    @Column(name = "order_id")
    private String orderId;

    @ApiModelProperty("冻结余额的归还日期")
    @Column(name = "freeze_return_date")
    private Date freezeReturnDate;
}