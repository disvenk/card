package com.resto.msgc.backend.card.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by disvenk.dai on 2018-04-17 19:00
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandNormalDateDto implements Serializable{
    private static final long serialVersionUID = -7528743399258444449L;

    private String cardCount;
    private String chequeCardMoney;
    private String cashCardMoney;
    private String wechatCardMoney;
    private String aliPayCardMoney;
    private String starPayCardMoney;
}
