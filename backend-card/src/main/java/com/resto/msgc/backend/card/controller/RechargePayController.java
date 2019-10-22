package com.resto.msgc.backend.card.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.resto.msgc.backend.card.conf.PayConf;
import com.resto.msgc.backend.card.util.AliPayUtils;
import com.resto.msgc.backend.card.util.ApplicationUtils;
import com.resto.msgc.backend.card.util.WeChatPayUtils;
import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

@Api
@Controller
@RequestMapping("/rechargePay")
public class RechargePayController {

    Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private PayConf payConf;
    /**
     * 卡充值发起微信、支付宝支付接口
     * @param authCode 扫码结果
     * @param payType 支付方式
     * @param paymentAmount 支付金额
     * @return
     */
    @PostMapping("/scanCodePayment")
    @ResponseBody
    public JSONObject scanCodePayment(String authCode, Integer payType, BigDecimal paymentAmount) {
        log.info("开始构建支付请求");
        //用作商户系统内部订单号，只用用来查询订单在第三方平台的支付状态
        String outTradeNo  = ApplicationUtils.randomUUID();
        //返回的信息
        JSONObject returnParam = new JSONObject();
        returnParam.put("success", true);
        returnParam.put("isPolling", true);
        Map<String, String> map = new HashMap<>();
        try {
            if (payType == 1){
                //微信支付
                //终端IP String(16)
                String terminalIp = InetAddress.getLocalHost().getHostAddress();
                //微信支付的金额已分为单位
                int total = paymentAmount.multiply(new BigDecimal(100)).intValue();
                //todo 由于是本地服务, 所以此处的微信支付相关配置信息写死(并且走的是服务商模式)
                map = WeChatPayUtils.crashPay(payConf.WXPAY_APPID, payConf.WXPAY_MCH_ID, payConf.WXPAY_SUB_MCH_ID, outTradeNo , total, authCode,
                        "美食广场模式卡充值消费", terminalIp, payConf.WXPAY_MCHKEY);
                if (Boolean.valueOf(map.get("success"))){
                    //构建微信支付请求成功
                    returnParam.put("outTradeNo", outTradeNo);
                }else{
                    //如果构建微信请求失败时的错误原因是系统级别导致的，调用查询API查询订单状态
                    if (!"SYSTEMERROR".equalsIgnoreCase(map.get("errCode")) &&
                            !"BANKERROR".equalsIgnoreCase(map.get("errCode")) &&
                            !"USERPAYING".equalsIgnoreCase(map.get("errCode"))){
                        returnParam.put("isPolling", false);
                        returnParam.put("message", map.get("msg"));
                    }else{
                        returnParam.put("outTradeNo", outTradeNo);
                    }
                    returnParam.put("success", false);
                }
            }else if (payType == 2){
                //支付宝支付
                //todo 由于是本地服务, 所以此处的支付宝支付相关配置信息写死
                AliPayUtils.connection(payConf.ALIPAY_TRADE_APP_ID,payConf.ALIPAY_TRADE_PRIVATE_KEY,payConf.ALIPAY_TRADE_PUBLIC_KEY,AliPayUtils.ALI_ENCRYPT);
                com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
                json.put("out_trade_no", outTradeNo);
                json.put("subject", "美食广场模式卡充值消费");
                json.put("total_amount", paymentAmount + "");
                json.put("auth_code", authCode);
                json.put("scene", "bar_code");
                Map<String, Object> resultMap = AliPayUtils.tradePay(json);
                if (Boolean.valueOf(resultMap.get("success").toString())){
                    //构建支付宝支付请求成功
                    returnParam.put("outTradeNo", outTradeNo);
                }else{
                    if ((resultMap.get("sub_code") != null
                            && !resultMap.get("sub_code").toString().equalsIgnoreCase("ACQ.SYSTEM_ERROR"))
                            || resultMap.get("sub_code") == null){
                        returnParam.put("isPolling", false);
                        returnParam.put("message", resultMap.get("msg"));
                    }
                    returnParam.put("success", false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            //如果在构建支付请求时报错，将不进行下一步查询订单的操作
            returnParam.put("success", false);
            returnParam.put("isPolling", false);
            returnParam.put("message", "构建支付请求失败，请更换支付方式");
        }
        return returnParam;
    }


    /**
     * 充值充值时查询支付进度接口
     * @param outTradeNo 商户订单号
     * @param payType 支付方式
     * @return
     */
    @PostMapping("/confirmPayment")
    @ResponseBody
    public JSONObject confirmPayment(String outTradeNo, Integer payType) {
        log.info("开始构查询订单支付信息");
        //返回的信息
        JSONObject returnParam = new JSONObject();
        returnParam.put("success", true);
        returnParam.put("isPolling", true);
        try{
            if (payType == 1){
                //微信支付 todo 由于是本地服务, 所以此处的微信支付相关配置信息写死(并且走的是服务商模式)
                Map<String, String> map = WeChatPayUtils.queryPay(payConf.WXPAY_APPID, payConf.WXPAY_MCH_ID,
                        payConf.WXPAY_SUB_MCH_ID, outTradeNo, payConf.WXPAY_MCHKEY);
                if (!Boolean.valueOf(map.get("success"))){
                    //如果正在支付中，则轮询继续去查。 反之则支付失败退出轮询
                    if ((map.containsKey("trade_state") && !"USERPAYING".equalsIgnoreCase(map.get("trade_state")))
                            || (map.containsKey("errCode") && !"SYSTEMERROR".equalsIgnoreCase(map.get("errCode")))){
                        returnParam.put("isPolling", false);
                        returnParam.put("message", map.get("msg"));
                    }
                    returnParam.put("success", false);
                }else {
                    //支付成功，退出轮询插入回调信息
                    returnParam.put("isPolling", false);
                    returnParam.put("payData", map.get("data"));
                }
            }else{
                //支付宝支付
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("out_trade_no", outTradeNo);
                //查询支付宝订单的支付进度
                Map<String, Object> returnMap = AliPayUtils.tradeQuery(jsonObject);
                //查询订单成功
                if (Boolean.valueOf(returnMap.get("success").toString())){
                    String trade_status = returnMap.get("trade_status").toString();
                    if (!"TRADE_SUCCESS".equalsIgnoreCase(trade_status) && !"TRADE_FINISHED".equalsIgnoreCase(trade_status)){
                        if ("TRADE_CLOSED".equalsIgnoreCase(trade_status)) {
                            //未付款交易超时关闭，或支付完成后全额退款
                            returnParam.put("isPolling", false);
                            returnParam.put("message", "未付款交易超时关闭");
                        }
                        returnParam.put("success", false);
                    }else{
                        JSONObject resultInfo = JSON.parseObject(returnMap.get("msg").toString());
                        //支付成功，退出轮询插入回调信息
                        returnParam.put("isPolling", false);
                        returnParam.put("payData", resultInfo.get("alipay_trade_query_response").toString());
                    }
                }else{
                    if ((returnMap.get("sub_code") != null
                            && !returnMap.get("sub_code").toString().equalsIgnoreCase("ACQ.SYSTEM_ERROR"))
                            || returnMap.get("sub_code") == null){
                        returnParam.put("isPolling", false);
                        returnParam.put("message", returnMap.get("msg"));
                    }
                    returnParam.put("success", false);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            //如果在构建查询支付进度请求时报错，将不进行下一步查询订单的操作
            returnParam.put("success", false);
        }
        return returnParam;
    }


    /**
     * 充值时撤销超时的订单
     * @param outTradeNo 商户订单号
     * @param payType 支付方式
     * @param request
     * @return
     */
    @PostMapping("/revocationOfOrder")
    @ResponseBody
    public JSONObject revocationOfOrder(String outTradeNo, Integer payType, HttpServletRequest request) {
        log.info("开始撤销订单");
        //定义返回参数
        JSONObject returnObject = new JSONObject();
        returnObject.put("success", true);
        try{
            if (payType == 1){
                //得到本地服务器的退款证书文件
                String payCertPath = request.getSession().getServletContext().getRealPath("payCert/FoodMember.p12");
                //撤销微信订单 todo 由于是本地服务, 所以此处的微信支付相关配置信息写死(并且走的是服务商模式)
                Map<String, String> map = WeChatPayUtils.reverseOrder(payConf.WXPAY_APPID,payConf.WXPAY_MCH_ID,payConf.WXPAY_SUB_MCH_ID, payConf.WXPAY_MCHKEY
                        ,outTradeNo, payCertPath);
                if (!Boolean.valueOf(map.get("success"))){
                    //撤销失败
                    String message = map.get("msg");
                    if (StringUtils.isNotBlank(message)){
                        returnObject.put("message", message);
                    }else{
                        returnObject.put("message", "撤销支付订单失败，请线下处理");
                    }
                    returnObject.put("success", false);
                }
            }else if (payType == 2){
                //撤销支付宝订单
                com.alibaba.fastjson.JSONObject object = new com.alibaba.fastjson.JSONObject();
                object.put("out_trade_no", outTradeNo);
                Map<String, Object>  returnMap = AliPayUtils.tradeCancel(object);
                //撤销失败
                if (!Boolean.valueOf(returnMap.get("success").toString())){
                    //撤销失败
                    String message = returnMap.get("msg").toString();
                    if (StringUtils.isNotBlank(message)){
                        returnObject.put("message", message);
                    }else{
                        returnObject.put("message", "撤销支付订单失败，请线下处理");
                    }
                    returnObject.put("success", false);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.put("success", false);
            returnObject.put("message","撤销支付订单出错，请线下处理");
        }
        return returnObject;
    }
}
