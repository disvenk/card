package com.resto.msgc.backend.card.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Table(name = "tb_account")
public class TbAccount {
    @ApiModelProperty("账户ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ApiModelProperty("账户余额")
    private BigDecimal remain;

    @ApiModelProperty("账户状态")
    private Byte status;

    @ApiModelProperty("冻结余额")
    @Column(name = "frozen_remain")
    private BigDecimal frozenRemain;
}