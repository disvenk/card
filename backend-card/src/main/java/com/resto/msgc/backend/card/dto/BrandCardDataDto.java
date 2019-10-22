package com.resto.msgc.backend.card.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by disvenk.dai on 2018-04-17 09:53
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandCardDataDto implements Serializable{
    private static final long serialVersionUID = -5351236478712620890L;
    private String cardCount;
    private String workerCardCount;
    private String normalCardCount;
    private String tempCardCount;
    private String chequeCardMoney;
    private String cashCardMoney;
    private String wechatCardMoney;
    private String aliPayCardMoney;
    private String starPayCardMoney;

}
