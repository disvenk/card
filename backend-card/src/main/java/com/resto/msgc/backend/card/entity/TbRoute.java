package com.resto.msgc.backend.card.entity;

import lombok.Data;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by disvenk.dai on 2018-08-31 11:49
 */
@Data
@Table(name = "tb_router")
public class TbRoute {
    public String id;
    public String orderId;
    public Integer type;
    public Integer status;
    public String cardNumber;
    public String context;
    public Date createTime;
    public Date comfirmTime;
}
