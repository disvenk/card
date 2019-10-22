package com.resto.msgc.backend.card.entity;


import com.resto.conf.db.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Table(name = "tb_card_discount")
public class TbCardDiscount extends BaseEntity {
    @ApiModelProperty("折扣名称")
    @Column(name = "discount_name")
    private String discountName;

    @ApiModelProperty("0不开启1开启")
    @Column(name = "is_open")
    private Byte isOpen;

    @Transient
    private List<String> discountIds;//折扣id集合

    @Transient
    private List<String> discounts;//折扣率 集合

    @Transient
    private List<String> weekIndex;//week长度获取

    @Transient
    private List<TbCardDiscountDetail> tbCardDiscountDetailList;
}