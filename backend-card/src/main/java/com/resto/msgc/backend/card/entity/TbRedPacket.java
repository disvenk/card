package com.resto.msgc.backend.card.entity;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "tb_red_packet")
public class TbRedPacket{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ApiModelProperty("红包金额")
    @Column(name = "red_money")
    private BigDecimal redMoney;

    @ApiModelProperty("发放时间")
    @Column(name = "create_time")
    private Date createTime;

    @ApiModelProperty("使用完成时间")
    @Column(name = "finish_time")
    private Date finishTime;

    @ApiModelProperty("用户id")
    @Column(name = "customer_id")
    private String customerId;

    @ApiModelProperty("品牌id")
    @Column(name = "brand_id")
    private String brandId;

    @ApiModelProperty("店铺id")
    @Column(name = "shop_detail_id")
    private String shopDetailId;

    @ApiModelProperty("红包剩余金额")
    @Column(name = "red_remainder_money")
    private BigDecimal redRemainderMoney;

    @ApiModelProperty("红包类型 0:评论红包 1:分享返利红包 2:退菜红包")
    @Column(name = "red_type")
    private Integer redType;

    @ApiModelProperty("订单Id")
    @Column(name = "order_id")
    private String orderId;

    @ApiModelProperty("默认1 已到账  0未到账")
    private Boolean state;
}