package com.resto.msgc.backend.card.entity;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "tb_order_refund_remark")
public class TbOrderRefundRemark  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty("退菜菜品Id")
    @Column(name = "article_id")
    private String articleId;

    @Column(name = "order_id")
    private String orderId;

    @ApiModelProperty("退菜原因Id")
    @Column(name = "refund_remark_id")
    private Integer refundRemarkId;

    @ApiModelProperty("退菜原因")
    @Column(name = "refund_remark")
    private String refundRemark;

    @ApiModelProperty("退菜原因补充")
    @Column(name = "remark_supply")
    private String remarkSupply;

    @ApiModelProperty("退菜数量")
    @Column(name = "refund_count")
    private Integer refundCount;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "shop_id")
    private String shopId;

    @Column(name = "brand_id")
    private String brandId;

    @ApiModelProperty("此字段为唯一字段，可以此来判断数据是否同步正常。")
    @Column(name = "data_sync_id")
    private String dataSyncId;
}