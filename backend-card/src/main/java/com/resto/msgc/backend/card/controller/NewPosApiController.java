package com.resto.msgc.backend.card.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.resto.conf.util.ApplicationUtils;
import com.resto.msgc.backend.card.conf.PayConf;
import com.resto.msgc.backend.card.constants.OrderPayMode;
import com.resto.msgc.backend.card.constants.OrderState;
import com.resto.msgc.backend.card.constants.PayMode;
import com.resto.msgc.backend.card.constants.ProductionStatus;
import com.resto.msgc.backend.card.entity.*;
import com.resto.msgc.backend.card.form.RefundOrderForm;
import com.resto.msgc.backend.card.form.RestoPayFrom;
import com.resto.msgc.backend.card.form.SaveOrderForm;
import com.resto.msgc.backend.card.form.SaveOrderPayMentForm;
import com.resto.msgc.backend.card.responseEntity.RestResponseEntity;
import com.resto.msgc.backend.card.service.*;
import com.resto.msgc.backend.card.http.HttpRequest;
import com.resto.msgc.backend.card.util.ResultDto;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.aspectj.apache.bcel.classfile.Code;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by xielc on 2018/4/18.
 */
@Api
@Controller
@RequestMapping("newPosApi")
public class NewPosApiController extends CommonController{

    protected Logger log = LoggerFactory.getLogger(getClass());

    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private PayConf payConf;

    @Autowired
    private WechatCustomerService wechatCustomerService;

    @Autowired
    private CardCustomService cardCustomService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private OrderPaymentService orderPaymentService;

    @Autowired
    private SaveOrderService saveOrderService;

    @Autowired
    RouteService routeService;


    @RequestMapping(value = "getCardNum")
    @ResponseBody
    public ResultDto get() throws IOException {
        Process p;
        String cmd = "cmd /c java -jar C:/card/card.jar";
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        result.setMessage("读卡失败");
        InputStream fis = null;
        BufferedReader br = null;
        try
        {
            //执行命令
            p = Runtime.getRuntime().exec(cmd);
            //取得命令结果的输出流
            fis = p.getInputStream();
            //用一个读输出流类去读
            //用缓冲器读行
            br=new BufferedReader( new InputStreamReader(fis,"GB2312"));
            String line=null;
            //直到读完为止
            if((line=br.readLine())!=null) {
                result.setSuccess(true);
                result.setMessage(line);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }finally {
            fis.close();
            br.close();

        }

        return result;
    }


    /**
    *@Description:退菜退款
    *@Author:disvenk.dai
    *@Date:14:29 2018/4/27 0027
    */
    @RequestMapping("refundOrderOrAticle")
    public ResponseEntity refundOrderOrAticle(@RequestBody RefundOrderForm form){
        JSONArray jsonArray = null;
        try {
            Boolean enableRefund = accountService.isEnableRefund(form.orderId);
            if(enableRefund){
                return new ResponseEntity(new RestResponseEntity(500,"折扣卡不支持退菜",null,null), HttpStatus.OK);
            }
            jsonArray = accountService.refundOrderOrAticle(form);

        }catch (Exception e){
            e.printStackTrace();
            StackTraceElement stackTraceElement= e.getStackTrace()[0];
            String message = "文件名:"+stackTraceElement.getFileName()+
                    "行号:"+stackTraceElement.getLineNumber()+"方法:"+
                    stackTraceElement.getMethodName()+"异常信息:"+e.getMessage();// 打印文件名

            return new ResponseEntity(new RestResponseEntity(500,"退菜失败",null,message), HttpStatus.OK);
        }

            return new ResponseEntity(new RestResponseEntity(200,"退菜成功",jsonArray,null), HttpStatus.OK);

    }

    /**
    *@Description:保存订单
    *@Author:disvenk.dai
    *@Date:18:18 2018/4/23 0023
    */
    @RequestMapping("/saveOrder")
    public ResponseEntity saveOrder(@RequestBody SaveOrderForm form){

        form.order.setAccountingTime(new Date());
        form.order.setOrderState(OrderState.PAYMENT);//订单状态
        form.order.setProductionStatus(ProductionStatus.PRINTED);//生产状态
        //原价originalAmount
        //实际需要支付的金额paymentAmount
        //商品总数articleCount
        form.order.setSerialNumber(DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSSS"));//流水号
        form.order.setAllowCancel(true);
        form.order.setAllowAppraise(false);//是否允许评论
        form.order.setClosed(false);
        form.order.setCreateTime(new Date());//创建时间
        //客户id
        //店铺id
        //就餐模式
        //店铺模式
        //品牌id
        form.order.setAmountWithChildren(BigDecimal.ZERO);
        form.order.setAllowContinueOrder(true);
        //交易码

        form.order.setPayMode(23);
        if (form.order.getDistributionModeId() != 3) {
            form.order.setAllowContinueOrder(true);
        }
        form.order.setSauceFeePrice(BigDecimal.ZERO);
        form.order.setTowelFeePrice(BigDecimal.ZERO);
        form.order.setTablewareFeePrice(BigDecimal.ZERO);
        form.order.setIsUseNewService(false);
        form.order.setDataOrigin(0);
        form.order.setMemberDiscountMoney(BigDecimal.ZERO);
        form.order.setBaseMoney(form.order.getOriginalAmount());
        form.order.setIsPay(2);
        form.order.setIsConfirm(true);
        form.order.setConfirmTime(new Date());
        form.order.setIsPosPay(true);
        form.order.setPrintFailFlag(false);
        form.order.setPrintKitchenFlag(false);
        form.order.setPrintKitchenFlag(false);
        saveOrderService.insert(form.order);
        saveOrderService.insert(form.orderItems);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderId",form.order.getId());
        return new ResponseEntity(new RestResponseEntity(200,"成功",null,null), HttpStatus.OK);
    }

    /**
    *@Description:更新账单
    *@Author:disvenk.dai
    *@Date:11:10 2018/4/24 0024
    */
    @RequestMapping("/savePayment")
    public ResponseEntity savePayment(@RequestBody SaveOrderPayMentForm form) {
        orderPaymentService.selectAndUpdate(form.code,form.orderId);
        List<TbOrderPaymentItem> orderPaymentItems = orderPaymentService.selectByCarNumber(form.orderId);
        TbRoute route = new TbRoute();
        String id = ApplicationUtils.randomUUID();
        route.setId(id);
        route.setOrderId(form.orderId);
        route.setCardNumber(form.code);
        route.setStatus(0);
        route.setContext(JSONObject.toJSONString(orderPaymentItems));

        route.setCreateTime(new Date());
        routeService.insert(route);
        return new ResponseEntity(new RestResponseEntity(200,"成功",null,null), HttpStatus.OK);


    }

    /**
    *@Description:刷卡支付
    *@Author:disvenk.dai
    *@Date:18:12 2018/4/23 0023
    */
    @RequestMapping("/restoPay")
    @ResponseBody
    public ResponseEntity customerPay(@RequestBody RestoPayFrom from){
        String orderId = from.orderId;
        String brandId = from.brandId;
        String shopId = from.shopId;
        String authCode = from.authCode;
        BigDecimal totalAmount = from.totalAmount;
        BigDecimal discountMoney=BigDecimal.ZERO;

        //查询微信用户
        //TbCustomer customer = wechatCustomerService.selectByTelephone(authCode);
        //查询储蓄卡用户

        TbCardCustomer cardCustomer = cardCustomService.selectByCardId(authCode);
        //如果扫的是虚拟卡
     if(cardCustomer != null){

            //查询开卡用户的账户
            TbAccount cardAccount = accountService.selectById(cardCustomer.getAccountId());
            JSONObject resultData = new JSONObject();
            resultData.put("type", OrderPayMode.CARD_PAY);
            resultData.put("cardId", authCode);


            resultData.put("customerId", cardCustomer.getId());


            //判断储值卡是否享受折扣，返回折扣率
            BigDecimal discount = accountService.whetherTheDiscount(cardCustomer);
            resultData.put("posDiscount",discount);
            //计算折扣后的实付金额
            BigDecimal orignMoney =totalAmount;
         BigDecimal subtract=BigDecimal.ZERO;
            if (discount.compareTo(BigDecimal.ZERO) > 0){
                totalAmount = totalAmount.multiply(discount.divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_HALF_UP);
                 subtract = orignMoney.subtract(totalAmount);//卡折扣了多少钱
                discountMoney=discountMoney.add(subtract);

            }
            //对比余额跟支付金额
            if(cardAccount.getRemain().doubleValue() < totalAmount.doubleValue()){
                return new ResponseEntity(new RestResponseEntity(500,"用户当前余额不足，余额为"+cardAccount.getRemain()+"元",null,null), HttpStatus.OK);

            }

         //查询是否关联了微信用户
        // customer = wechatCustomerService.selectByAccountId(cardCustomer.getAccountId());
            //如果有
           /* if(customer!=null){
                //使用优惠券支付
                JSONObject result = accountService.restoPayConpun(orderId, authCode, cardAccount.getId(), totalAmount, customer.getId(), brandId, shopId);
                BigDecimal resultMoney = (BigDecimal) result.get("resultMoney");
                discountMoney = discountMoney.add((BigDecimal) result.get("discount"));
                if(resultMoney.compareTo(BigDecimal.ZERO)>=0 && !(resultMoney.compareTo(totalAmount)>=0)){
                    resultData.put("posDiscount",discount);
                    resultData.put("orderMoney",totalAmount);
                    resultData.put("orderPosDiscountMoney",subtract);
                    resultData.put("reductionAmount",discountMoney);//减免金额
                    resultData.put("paymentAmount",orignMoney.subtract(discountMoney));//实际需要支付金额
                    resultData.put("paymentMode",23);//支付类型
                    return new ResponseEntity(new RestResponseEntity(200,"支付成功",resultData,null), HttpStatus.OK);
                }

                //使用红包支付
                JSONObject result1 = accountService.restoPayHasWechat(orderId,authCode, cardAccount.getId(), totalAmount, customer.getId(), brandId, shopId);
                BigDecimal resultMoney1 = (BigDecimal) result1.get("resultMoney");
                discountMoney = discountMoney.add((BigDecimal) result1.get("discount"));
                if(resultMoney1.compareTo(BigDecimal.ZERO)>=0 && !(resultMoney1.compareTo(totalAmount)>=0)){
                    resultData.put("posDiscount",discount);//折扣率
                    resultData.put("orderMoney",totalAmount);
                    resultData.put("orderPosDiscountMoney",subtract);//折扣了多少钱：原价-（原价×折扣率）
                    resultData.put("reductionAmount",discountMoney);//减免金额
                    resultData.put("paymentAmount",orignMoney.subtract(discountMoney));//实际需要支付金额
                    resultData.put("paymentMode",23);//支付类型 23:充值本金 24:充值赠送
                    return new ResponseEntity(new RestResponseEntity(200,"支付成功",resultData,null), HttpStatus.OK);
                }

                //不够支付，继续扣账户余额
                JSONObject result2 = accountService.GoOnPayCharge( orderId,authCode, cardAccount.getId(),resultMoney1.abs(), cardCustomer.getId(), brandId, shopId);
                BigDecimal resultMoney2 = (BigDecimal) result1.get("resultMoney");
                discountMoney = discountMoney.add((BigDecimal) result1.get("discount"));
                resultData.put("posDiscount",discount);
                resultData.put("orderMoney",totalAmount);
                resultData.put("orderPosDiscountMoney",discountMoney);
                resultData.put("reductionAmount",subtract);//减免金额
                resultData.put("paymentAmount",orignMoney.subtract(discountMoney));//实际需要支付金额
                resultData.put("paymentMode",23);//支付类型
                if(resultMoney2.compareTo(BigDecimal.ZERO)>=0 && !(resultMoney2.compareTo(resultMoney1.abs())==0)){
                    return new ResponseEntity(new RestResponseEntity(200,"支付成功",resultData,null), HttpStatus.OK);
                }
            }else {*/
                //没有关联微信账户直接扣余额
                JSONObject result2 = accountService.GoOnPayCharge(orderId, authCode, cardAccount.getId(),totalAmount, cardCustomer.getId(), brandId, shopId);
                BigDecimal resultMoney2 = (BigDecimal) result2.get("resultMoney");
                discountMoney = discountMoney.add((BigDecimal) result2.get("discount"));
                 List<TbOrderPaymentItem> paymentItems = (List<TbOrderPaymentItem>) result2.get("paymentItems");
                 resultData.put("paymentItems",paymentItems);
                resultData.put("posDiscount",discount);
                resultData.put("orderMoney",totalAmount);
                resultData.put("orderPosDiscountMoney",subtract);
                resultData.put("reductionAmount",discountMoney);//折扣金额
                //resultData.put("paymentAmount",orignMoney.subtract(discountMoney));//实际需要支付金额
                resultData.put("paymentAmount",orignMoney.subtract(discountMoney));//实际需要支付金额
                resultData.put("paymentMode", PayMode.CARD_CHARGE_PAY);//支付类型

                if(resultMoney2.compareTo(BigDecimal.ZERO)>=0 && !(resultMoney2.compareTo(totalAmount)>=0)){
                        return new ResponseEntity(new RestResponseEntity(200,"支付成功",resultData,null), HttpStatus.OK);

                }
             return new ResponseEntity(new RestResponseEntity(200,"支付成功",resultData,null), HttpStatus.OK);
        }else{
         return new ResponseEntity(new RestResponseEntity(500,"当前卡号不存在",null,null), HttpStatus.OK);
        }
    }


}
