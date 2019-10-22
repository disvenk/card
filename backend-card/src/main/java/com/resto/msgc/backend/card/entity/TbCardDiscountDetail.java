package com.resto.msgc.backend.card.entity;

import com.resto.conf.db.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "tb_card_discount_detail")
public class TbCardDiscountDetail extends BaseEntity {

    @ApiModelProperty("主表id")
    @Column(name = "discount_id")
    private Long discountId;

    @ApiModelProperty("周折扣时间段")
    @Column(name = "discount_week")
    private String discountWeek;

    @ApiModelProperty("时间折扣时间段")
    @Column(name = "discount_time")
    private String discountTime;

    @ApiModelProperty("折扣")
    private String discount;

}