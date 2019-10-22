package com.resto.msgc.backend.card.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * title
 * company resto+
 * author jimmy 2018/4/25 下午4:46
 * version 1.0
 */
@Component
public class PayConf {
    @Value("${alipay.trade.app.id}")
    public String ALIPAY_TRADE_APP_ID;
    @Value("${alipay.trade.private.key}")
    public String ALIPAY_TRADE_PRIVATE_KEY;
    @Value("${alipay.trade.public.key}")
    public String ALIPAY_TRADE_PUBLIC_KEY;
    @Value("${wxpay.mch_id}")
    public String WXPAY_MCH_ID;
    @Value("${wxpay.sub_mch_id}")
    public String WXPAY_SUB_MCH_ID;
    @Value("${wxpay.mchkey}")
    public String WXPAY_MCHKEY;
    @Value("${wxpay.appid}")
    public String WXPAY_APPID;
    @Value("${server.url}")
    public String serverUrl;
    @Value("${wx.url}")
    public String wxUrl;
}
