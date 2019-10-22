package com.resto.msgc.backend.card.form;

import com.resto.msgc.backend.card.entity.TbOrderRefundRemark;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by disvenk.dai on 2018-05-11 17:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundArticle {
    public String articleId;
    public BigDecimal refundMoney;
    public Integer refundCount;

}
