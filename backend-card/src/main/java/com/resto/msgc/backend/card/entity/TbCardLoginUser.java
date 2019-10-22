package com.resto.msgc.backend.card.entity;

import com.resto.conf.db.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "tb_card_login_user")
public class TbCardLoginUser extends BaseEntity {
    @Column(name = "brand_id")
    private String brandId;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "password")
    private String password;

    @Column(name = "flag")
    private Integer flag;

    @Column(name = "login_first")
    private Date loginFirst;

}