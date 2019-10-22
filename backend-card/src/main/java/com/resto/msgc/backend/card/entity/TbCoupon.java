package com.resto.msgc.backend.card.entity;


import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "tb_coupon")
public class TbCoupon  {
    @ApiModelProperty("优惠卷id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ApiModelProperty("优惠券名称")
    private String name;

    @ApiModelProperty("优惠卷价值")
    private BigDecimal value;

    @ApiModelProperty("最低消费额")
    @Column(name = "min_amount")
    private BigDecimal minAmount;

    @ApiModelProperty("有效期开始时间")
    @Column(name = "begin_date")
    private Date beginDate;

    @ApiModelProperty("有效期结束时间")
    @Column(name = "end_date")
    private Date endDate;

    @ApiModelProperty("开始使用时间")
    @Column(name = "begin_time")
    private Date beginTime;

    @ApiModelProperty("结束使用时间")
    @Column(name = "end_time")
    private Date endTime;

    @ApiModelProperty("是否已被使用 0否 1是")
    @Column(name = "is_used")
    private Boolean isUsed;

    @ApiModelProperty("优惠卷使用时间")
    @Column(name = "using_time")
    private Date usingTime;

    @ApiModelProperty("优惠卷来源")
    @Column(name = "coupon_source")
    private String couponSource;

    @ApiModelProperty("是否可以和余额一起使用")
    @Column(name = "use_with_account")
    private Boolean useWithAccount;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("配送模式Id")
    @Column(name = "distribution_mode_id")
    private Integer distributionModeId;

    @ApiModelProperty("优惠卷所属人")
    @Column(name = "customer_id")
    private String customerId;

    @ApiModelProperty("得到优惠券时间")
    @Column(name = "add_time")
    private Date addTime;

    @ApiModelProperty("店铺Id")
    @Column(name = "shop_detail_id")
    private String shopDetailId;

    @ApiModelProperty("品牌Id")
    @Column(name = "brand_id")
    private String brandId;

    @ApiModelProperty("优惠券类型(-1:通用,0:新用户注册,1:邀请注册,2:生日,3:分享,4:实时))")
    @Column(name = "coupon_type")
    private Boolean couponType;

    @ApiModelProperty("推送天数")
    @Column(name = "push_day")
    private Integer pushDay;

    @ApiModelProperty("分享优惠券延迟使用时间")
    @Column(name = "recommend_delay_time")
    private Integer recommendDelayTime;

    @ApiModelProperty("优惠券设置的Id")
    @Column(name = "new_custom_coupon_id")
    private Long newCustomCouponId;
}