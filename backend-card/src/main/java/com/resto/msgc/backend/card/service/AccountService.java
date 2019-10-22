package com.resto.msgc.backend.card.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.resto.conf.mybatis.base.BaseService;
import com.resto.msgc.backend.card.constants.PayMode;
import com.resto.msgc.backend.card.constants.RedChoose;
import com.resto.msgc.backend.card.entity.*;
import com.resto.msgc.backend.card.form.RefundArticle;
import com.resto.msgc.backend.card.form.RefundOrderForm;
import com.resto.msgc.backend.card.mapper.*;
import com.resto.msgc.backend.card.responseEntity.ResponseEntityException;
import com.resto.msgc.backend.card.util.ApplicationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bruce on 2017-12-11 18:00
 */
@Service
public class AccountService extends BaseService<TbAccount, TbAccountMapper> {

    @Autowired
    private TbAccountMapper tbAccountMapper;

    @Autowired
    private TbRedPacketMapper tbRedPacketMapper;

    @Autowired
    private TbOrderPaymentItemMapper tbOrderPaymentItemMapper;

    @Autowired
    private TbCardRechangeMapper tbCardRechangeMapper;

    @Autowired
    private TbAccountLogMapper tbAccountLogMapper;

    @Autowired
    private TbCouponMapper tbCouponMapper;

    @Autowired
    private TbCardCustomerMapper tbCardCustomerMapper;

    @Autowired
    private TbOrderMapper tbOrderMapper;

    @Autowired
    private TbCardDiscountMapper tbCardDiscountMapper;

    @Autowired
    private TbOrderRefundRemarkMapper tbOrderRefundRemarkMapper;

    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;

    @Autowired
    private TbCardDiscountDetailMapper tbCardDiscountDetailMapper;


    /**
    *@Description:通过id查找
    *@Author:disvenk.dai
    *@Date:17:06 2018/4/23 0023
    */
    public TbAccount selectById(String id){
        return tbAccountMapper.selectById(id);
    }

    /**
    *@Description:判断折扣卡无法退菜
    *@Author:disvenk.dai
    *@Date:10:44 2018/5/4 0004
    */
    public Boolean isEnableRefund(String orderId){
        TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(orderId);
        TbCardCustomer tbCardCustomer = tbCardCustomerMapper.selectByPrimaryKey(Long.parseLong(tbOrder.getCustomerId()));
        BigDecimal discount = whetherTheDiscount(tbCardCustomer);
        if (discount.compareTo(BigDecimal.valueOf(100))!=0 && discount.compareTo(BigDecimal.ZERO)!=0){
            return true;
        }else {
            return false;
        }
    }

    /**
    *@Description:退菜退款
    *@Author:disvenk.dai
    *@Date:14:48 2018/4/27 0027
    */
    @Transactional(rollbackFor = Exception.class)
    public JSONArray refundOrderOrAticle(RefundOrderForm form) throws ResponseEntityException {
        TbOrder order = tbOrderMapper.selectByPrimaryKey(form.orderId);

        /**********退菜更改订单数据start***************/
        //计算退菜总数
        int reduce = form.articles.stream().mapToInt(RefundArticle::getRefundCount).sum();

        //判断退菜数量是否大于剩余菜品数量
        if(reduce>order.getArticleCount()){
            throw new ResponseEntityException(500,"退菜数量有误");
        }

        //如果退菜数量与剩余数量相等，则更改为全部退完状态
        if(reduce==order.getArticleCount()){
            order.setArticleCount(0);
            order.setProductionStatus(6);
        }
        //如果小于则更改为剩余状态
        if(reduce<order.getArticleCount()){
            order.setArticleCount(order.getArticleCount()-reduce);
        }
        //退餐盒费
        if(form.restoBoxFee!=null){
            if(order.getMealFeePrice()==null || order.getMealFeePrice().compareTo(BigDecimal.ZERO)==0){
                throw new ResponseEntityException(500,"该订单没有餐盒费，请重新退菜");
            }
            if(form.restoBoxFee.compareTo(order.getMealFeePrice())>0){
                throw new ResponseEntityException(500,"餐盒费超出订单应退餐盒费的金额");
            }else{
                order.setMealFeePrice(order.getMealFeePrice().subtract(form.restoBoxFee));
            }
        }
        //退服务费
        if(form.serviceFee!=null){
            if(order.getServicePrice()==null || order.getServicePrice().compareTo(BigDecimal.ZERO)==0){
                throw new ResponseEntityException(500,"该订单没有服务费，请重新退菜");
            }
            if(form.serviceFee.compareTo(order.getServicePrice())>0){
                throw new ResponseEntityException(500,"服务费超出订单应退服务费的金额");
            }else {
                order.setServicePrice(order.getServicePrice().subtract(form.serviceFee));
            }
        }

        tbOrderMapper.updateByPrimaryKeySelective(order);
        /**********退菜更改订单数据end***************/

        /************更改订单菜品记录中的钱和退菜数量start*********************/
        List<RefundArticle> articles = form.articles;
        BigDecimal finalMoney = BigDecimal.ZERO;
        for(RefundArticle refundArticle : articles){
            BigDecimal refundAmount = refundArticle.refundMoney.multiply(BigDecimal.valueOf(refundArticle.refundCount));
            TbOrderItem orderItem = new TbOrderItem();
            orderItem.setArticleId(refundArticle.articleId);
            orderItem.setOrderId(form.orderId);
            TbOrderItem orderItem1 = tbOrderItemMapper.selectOne(orderItem);

            finalMoney = orderItem1.getFinalPrice();
            orderItem1.setRefundCount((orderItem1.getRefundCount()==null?0:orderItem1.getRefundCount())+refundArticle.refundCount);
            orderItem1.setFinalPrice(finalMoney.subtract(refundArticle.refundMoney));
            tbOrderItemMapper.updateByPrimaryKeySelective(orderItem1);
        }
        /************更改订单菜品记录中的钱end*********************/

        //保存每个菜品退菜的原因数据
        for(TbOrderRefundRemark orderRefundRemark : form.orderRefundRemarks){
            orderRefundRemark.setRefundRemarkId(0);
            tbOrderRefundRemarkMapper.insert(orderRefundRemark);
        }

        /********将金额退换到用户充值的账户start*************/
        List<TbOrderPaymentItem> tbOrderPaymentItems = tbOrderPaymentItemMapper.selectChargeBalancePayItem(form.orderId);
        TbOrderPaymentItem tbOrderPaymentItem = tbOrderPaymentItems.get(0);
        Long chargeId = Long.parseLong(tbOrderPaymentItem.getResultData());
        TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(form.orderId);
        TbCardCustomer tbCardCustomer = tbCardCustomerMapper.selectByPrimaryKey(Long.parseLong(tbOrder.getCustomerId()));
        TbAccount tbAccount = tbAccountMapper.selectById(tbCardCustomer.getAccountId());
        BigDecimal remain = tbAccount.getRemain();
        BigDecimal refundMoney = BigDecimal.ZERO;
        JSONArray data = new JSONArray();
     /*   if(form.aticleIds!=null && !form.aticleIds.isEmpty()){


            tbCardRechangeMapper.updateChargeBalanceForRefund1(form.refundMoney,chargeId);

            //修改账户余额
            TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(form.orderId);
            TbCardCustomer tbCardCustomer = tbCardCustomerMapper.selectByPrimaryKey(tbOrder.getCustomerId());
            TbAccount tbAccount = tbAccountMapper.selectById(tbCardCustomer.getAccountId());
            tbAccount.setRemain(tbAccount.getRemain().add(form.refundMoney));
            tbAccountMapper.updateByPrimaryKeySelective(tbAccount);

            TbAccountLog accountLog = new TbAccountLog();
            accountLog.setId(ApplicationUtils.randomUUID());
            accountLog.setMoney(form.refundMoney);
            accountLog.setCreateTime(new Date());
            accountLog.setPaymentType(1);
            accountLog.setRemain(tbAccount.getRemain().add(form.refundMoney));
            accountLog.setRemark("退菜返还到充值赠送金额");
            accountLog.setAccountId(tbAccount.getId());
            accountLog.setShopDetailId(tbOrder.getBrandId());
            accountLog.setOrderId(form.orderId);
            accountLog.setSource(TbAccountLog.CHARGE_PAY_REFUND);
            tbAccountLogMapper.insert(accountLog);*/

       // }else {

            BigDecimal rewardPayMoney = tbOrderPaymentItemMapper.selectChargeBalancePay(form.orderId);


            //如果退换金额小于等于了所有赠送余额支付的支付项
            if(form.refundAmountMoney.compareTo(rewardPayMoney==null?BigDecimal.ZERO:rewardPayMoney)<=0){
                tbCardRechangeMapper.updateChargeBalanceForRefund1(form.refundAmountMoney,chargeId);
                //修改账户余额

                tbAccount.setRemain(tbAccount.getRemain().add(form.refundAmountMoney));
                tbAccountMapper.updateByPrimaryKeySelective(tbAccount);

                TbAccountLog accountLog = new TbAccountLog();
                accountLog.setId(ApplicationUtils.randomUUID());
                accountLog.setMoney(form.refundAmountMoney);
                accountLog.setCreateTime(new Date());
                accountLog.setPaymentType(1);
                remain = remain.add(form.refundAmountMoney);
                accountLog.setRemain(remain);
                accountLog.setRemark("退菜返还到卡充值赠送剩余金额");
                accountLog.setAccountId(tbAccount.getId());
                accountLog.setShopDetailId(tbOrder.getBrandId());
                accountLog.setOrderId(form.orderId);
                accountLog.setSource(TbAccountLog.CARD_CHARGE_PAY_REFUND);
                tbAccountLogMapper.insert(accountLog);

                TbOrderPaymentItem item = new TbOrderPaymentItem();
                item.setId(ApplicationUtils.randomUUID());
                item.setOrderId(form.orderId);
                item.setPaymentModeId(PayMode.CARD_REFUND_PAY);
                item.setPayTime(new Date());
                item.setPayValue(form.refundAmountMoney.multiply(BigDecimal.valueOf(-1)));
                item.setRemark("退菜退款到充值卡:" + item.getPayValue());
                item.setResultData(chargeId.toString());
                tbOrderPaymentItemMapper.insert(item);

                JSONObject json = new JSONObject();
                json.put("id",ApplicationUtils.randomUUID());
                json.put("orderId",form.orderId);
                json.put("payTime", System.currentTimeMillis());
                json.put("payValue",form.refundAmountMoney.multiply(BigDecimal.valueOf(-1)));
                json.put("paymentModeId",PayMode.CARD_REFUND_PAY);
                json.put("remark","退菜返还到卡充值赠送剩余金额:"+form.refundAmountMoney.multiply(BigDecimal.valueOf(-1)));
                data.add(json);
            }else {
                    refundMoney = rewardPayMoney==null?BigDecimal.ZERO:rewardPayMoney;
                tbCardRechangeMapper.updateChargeBalanceForRefund1(refundMoney,chargeId);//退到赠送
                //修改账户余额
                remain  = remain.add(refundMoney);
                tbAccount.setRemain(remain);
                tbAccountMapper.updateByPrimaryKeySelective(tbAccount);
                TbAccountLog accountLog = new TbAccountLog();
                accountLog.setId(ApplicationUtils.randomUUID());
                accountLog.setMoney(refundMoney);
                accountLog.setCreateTime(new Date());
                accountLog.setPaymentType(1);
                accountLog.setRemain(remain);
                accountLog.setRemark("退菜返还到卡充值赠送剩余金额");
                accountLog.setAccountId(tbAccount.getId());
                accountLog.setShopDetailId(tbOrder.getBrandId());
                accountLog.setOrderId(form.orderId);
                accountLog.setSource(TbAccountLog.CARD_CHARGE_PAY_REFUND);
                tbAccountLogMapper.insert(accountLog);

                TbOrderPaymentItem item = new TbOrderPaymentItem();
                item.setId(ApplicationUtils.randomUUID());
                item.setOrderId(form.orderId);
                item.setPaymentModeId(PayMode.CARD_REFUND_PAY);
                item.setPayTime(new Date());
                item.setPayValue(refundMoney.multiply(BigDecimal.valueOf(-1)));
                item.setRemark("退菜退款到充值卡:" + refundMoney);
                item.setResultData(chargeId.toString());
                tbOrderPaymentItemMapper.insert(item);

                JSONObject json1 = new JSONObject();
                json1.put("id",ApplicationUtils.randomUUID());
                json1.put("orderId",form.orderId);
                json1.put("payTime", System.currentTimeMillis());
                json1.put("payValue",refundMoney.multiply(BigDecimal.valueOf(-1)));
                json1.put("paymentModeId",PayMode.CARD_REWARD_PAY);
                json1.put("remark","退菜返还到卡充值赠送剩余金额:"+refundMoney.multiply(BigDecimal.valueOf(-1)));

                data.add(json1);

                tbCardRechangeMapper.updateChargeBalanceForRefund2(form.refundAmountMoney.subtract(refundMoney),chargeId);//退到充值
                //修改账户余额
                remain  = remain.add(form.refundAmountMoney.subtract(refundMoney));
                tbAccount.setRemain(remain);
                tbAccountMapper.updateByPrimaryKeySelective(tbAccount);

                TbAccountLog accountLog1 = new TbAccountLog();
                accountLog1.setId(ApplicationUtils.randomUUID());
                accountLog1.setMoney(form.refundAmountMoney.subtract(refundMoney));
                accountLog1.setCreateTime(new Date());
                accountLog1.setPaymentType(1);
                accountLog1.setRemain(remain);
                accountLog1.setRemark("退菜返还到卡充值剩余金额");
                accountLog1.setAccountId(tbAccount.getId());
                accountLog1.setShopDetailId(tbOrder.getBrandId());
                accountLog1.setOrderId(form.orderId);
                accountLog1.setSource(TbAccountLog.CARD_CHARGE_PAY_REFUND);
                tbAccountLogMapper.insert(accountLog1);

                TbOrderPaymentItem item1 = new TbOrderPaymentItem();
                item1.setId(ApplicationUtils.randomUUID());
                item1.setOrderId(form.orderId);
                item1.setPaymentModeId(PayMode.CARD_REFUND_PAY);
                item1.setPayTime(new Date());
                item1.setPayValue(form.refundAmountMoney.subtract(refundMoney).multiply(BigDecimal.valueOf(-1)));
                item1.setRemark("退菜退款到充值卡:" + form.refundAmountMoney.subtract(refundMoney));
                item1.setResultData(chargeId.toString());
                tbOrderPaymentItemMapper.insert(item1);

                JSONObject json2 = new JSONObject();
                json2.put("id",ApplicationUtils.randomUUID());
                json2.put("orderId",form.orderId);
                json2.put("payTime", System.currentTimeMillis());
                json2.put("payValue",form.refundAmountMoney.subtract(refundMoney).multiply(BigDecimal.valueOf(-1)));
                json2.put("paymentModeId",PayMode.CARD_REFUND_PAY);
                json2.put("remark","退菜返还到卡充值剩余金额:"+form.refundAmountMoney.subtract(refundMoney).multiply(BigDecimal.valueOf(-1)));
                data.add(json2);
            }
        /********将金额退换到用户充值的账户end*************/

       // }
return data;
    }

    /**
    *@Description:优先使用优惠券
    *@Author:disvenk.dai
    *@Date:15:16 2018/4/26 0026
    */
    public JSONObject restoPayConpun(String orderId,String authCode, String accountId, BigDecimal totalAmount, String customerId, String brandId, String shopId){
        BigDecimal resultMoney=totalAmount;
        BigDecimal discount = BigDecimal.ZERO;

        List<TbCoupon> tbCoupons = tbCouponMapper.selectEnableCoupon(customerId);
        if(tbCoupons!=null && !tbCoupons.isEmpty()){
            for(TbCoupon tbCoupon : tbCoupons){
               if(tbCoupon.getShopDetailId()!=null){
                   if(tbCoupon.getShopDetailId().equals(shopId)){
                       if(totalAmount.compareTo(tbCoupon.getMinAmount())>=0){
                           BigDecimal result = tbCoupon.getValue().subtract(totalAmount);
                           if(result.compareTo(BigDecimal.ZERO)<0){
                               resultMoney=result;
                               totalAmount = result.abs();
                               discount = discount.add(tbCoupon.getValue());

                               //更改优惠券使用状态并且插入优惠券支付记录
                               tbCouponMapper.updateIsUsed(tbCoupon.getId());

                               continue;
                           }else if(result.compareTo(BigDecimal.ZERO)==0){
                               resultMoney=result;
                               discount = discount.add(tbCoupon.getValue());

                               //更改优惠券使用状态并且插入优惠券支付记录
                               tbCouponMapper.updateIsUsed(tbCoupon.getId());


                               JSONObject json = new JSONObject();
                               json.put("resultMoney",resultMoney);
                               json.put("discount",discount);
                               return json;
                           }else {
                               resultMoney=result;
                               discount = discount.add(totalAmount);

                               //更改优惠券使用状态并且插入优惠券支付记录
                               tbCouponMapper.updateIsUsed(tbCoupon.getId());

                               JSONObject json = new JSONObject();
                               json.put("resultMoney",resultMoney);
                               json.put("discount",discount);
                               return json;
                           }
                       }
                   }

               } else {
                   if(totalAmount.compareTo(tbCoupon.getMinAmount())>=0){
                       BigDecimal result = tbCoupon.getValue().subtract(totalAmount);
                       if(result.compareTo(BigDecimal.ZERO)<0){
                           resultMoney=result;
                           totalAmount = result.abs();
                           discount = discount.add(tbCoupon.getValue());

                           //更改优惠券使用状态并且插入优惠券支付记录
                           tbCouponMapper.updateIsUsed(tbCoupon.getId());

                           continue;
                       }else if(result.compareTo(BigDecimal.ZERO)==0){
                           resultMoney=result;
                           discount = discount.add(tbCoupon.getValue());

                           //更改优惠券使用状态并且插入优惠券支付记录
                           tbCouponMapper.updateIsUsed(tbCoupon.getId());

                           JSONObject json = new JSONObject();
                           json.put("resultMoney",resultMoney);
                           json.put("discount",discount);
                           return json;
                       }else {
                           resultMoney=result;
                           discount = discount.add(totalAmount);

                           //更改优惠券使用状态并且插入优惠券支付记录
                           tbCouponMapper.updateIsUsed(tbCoupon.getId());

                           JSONObject json = new JSONObject();
                           json.put("resultMoney",resultMoney);
                           json.put("discount",discount);
                           return json;
                       }
                   }
               }

            }
        }

        JSONObject json = new JSONObject();
        json.put("resultMoney",resultMoney);
        json.put("discount",discount);
        return json;
    }

    /**
     *@Description:同时拥有微信账户的实体卡支付先扣红包
     *@Author:disvenk.dai
     *@Date:14:13 2018/4/22 0022
     */
    @Transactional
    public JSONObject restoPayHasWechat(String orderId, String authCode, String accountId, BigDecimal totalAmount, String customerId, String brandId, String shopId){
        BigDecimal resultMoney=totalAmount;
        BigDecimal discount = BigDecimal.ZERO;

        //查询出所有类型的红包进行比较
        for(int i=0;i<5;i++){
            List<TbRedPacket> redPackets = tbRedPacketMapper.selectEableUseAll(customerId, i);
            if(redPackets!=null && !redPackets.isEmpty()){
                disvenk:for(TbRedPacket redPacket:redPackets){
                    BigDecimal result = redPacket.getRedRemainderMoney().subtract(totalAmount);
                    if(result.compareTo(BigDecimal.ZERO)<0){
                        resultMoney=result;
                        totalAmount = result.abs();
                        discount = discount.add(redPacket.getRedRemainderMoney());
                        //插入账户日志记录
                        TbAccount tbAccount = tbAccountMapper.selectById(accountId);
                        TbAccountLog accountLog = new TbAccountLog();
                        accountLog.setId(ApplicationUtils.randomUUID());
                        accountLog.setMoney(redPacket.getRedRemainderMoney().multiply(BigDecimal.valueOf(-1)));
                        accountLog.setCreateTime(new Date());
                        accountLog.setPaymentType(2);
                        accountLog.setRemain(tbAccount.getRemain().subtract(redPacket.getRedRemainderMoney()));
                        accountLog.setRemark(RedChoose.redChoose(redPacket.getRedType())+"支出");
                        accountLog.setAccountId(accountId);
                        accountLog.setShopDetailId(shopId);
                        accountLog.setOrderId(orderId);
                        accountLog.setSource(TbAccountLog.FREEZE_RED_MONEY);
                        //accountLog.setFreezeReturnDate(shopDetail.getRebateTime());

                        tbAccountLogMapper.insert(accountLog);

                        //更改红包剩余金额并且插入红包支付记录
                        tbRedPacketMapper.updateredRemainderMoneyById(BigDecimal.ZERO,redPacket.getId());

                        TbOrderPaymentItem item = new TbOrderPaymentItem();
                        item.setId(ApplicationUtils.randomUUID());
                        item.setOrderId(authCode);
                        item.setPaymentModeId(PayMode.ACCOUNT_PAY);
                        item.setPayTime(new Date());
                        item.setPayValue(redPacket.getRedRemainderMoney());
                        item.setRemark(RedChoose.redChoose(redPacket.getRedType())+"支付:" + item.getPayValue());
                        item.setResultData(accountId);
                        tbOrderPaymentItemMapper.insert(item);
                        continue disvenk;
                    }else if(result.compareTo(BigDecimal.ZERO)==0){
                        resultMoney=result;
                        discount = discount.add(redPacket.getRedRemainderMoney());
                        //插入账户日志记录
                        TbAccount tbAccount = tbAccountMapper.selectById(accountId);
                        TbAccountLog accountLog = new TbAccountLog();
                        accountLog.setId(ApplicationUtils.randomUUID());
                        accountLog.setMoney(redPacket.getRedRemainderMoney().multiply(BigDecimal.valueOf(-1)));
                        accountLog.setCreateTime(new Date());
                        accountLog.setPaymentType(2);
                        accountLog.setRemain(tbAccount.getRemain().subtract(redPacket.getRedRemainderMoney()));
                        accountLog.setRemark(RedChoose.redChoose(redPacket.getRedType())+"支出");
                        accountLog.setAccountId(accountId);
                        accountLog.setShopDetailId(shopId);
                        accountLog.setOrderId(orderId);
                        accountLog.setSource(TbAccountLog.FREEZE_RED_MONEY);
                        tbAccountLogMapper.insert(accountLog);

                        tbRedPacketMapper.updateredRemainderMoneyById(BigDecimal.ZERO,redPacket.getId());
                        TbOrderPaymentItem item = new TbOrderPaymentItem();
                        item.setId(ApplicationUtils.randomUUID());
                        item.setOrderId(authCode);
                        item.setPaymentModeId(PayMode.ACCOUNT_PAY);
                        item.setPayTime(new Date());
                        item.setPayValue(redPacket.getRedRemainderMoney());
                        item.setRemark(RedChoose.redChoose(redPacket.getRedType())+"支付:"+item.getPayValue());
                        item.setResultData(accountId);
                        tbOrderPaymentItemMapper.insert(item);

                        JSONObject json = new JSONObject();
                        json.put("resultMoney",resultMoney);
                        json.put("discount",discount);
                        return json;
                    }else {
                        resultMoney=result;
                        discount = discount.add(totalAmount);
                        //插入账户日志记录
                        TbAccount tbAccount = tbAccountMapper.selectById(accountId);
                        TbAccountLog accountLog = new TbAccountLog();
                        accountLog.setId(ApplicationUtils.randomUUID());
                        accountLog.setMoney(totalAmount.multiply(BigDecimal.valueOf(-1)));
                        accountLog.setCreateTime(new Date());
                        accountLog.setPaymentType(2);
                        accountLog.setRemain(tbAccount.getRemain().subtract(totalAmount));
                        accountLog.setRemark(RedChoose.redChoose(redPacket.getRedType())+"支出");
                        accountLog.setAccountId(accountId);
                        accountLog.setShopDetailId(shopId);
                        accountLog.setOrderId(orderId);
                        accountLog.setSource(TbAccountLog.FREEZE_RED_MONEY);
                        tbAccountLogMapper.insert(accountLog);

                        tbRedPacketMapper.updateredRemainderMoneyById(result,redPacket.getId());
                        TbOrderPaymentItem item = new TbOrderPaymentItem();
                        item.setId(ApplicationUtils.randomUUID());
                        item.setOrderId(authCode);
                        item.setPaymentModeId(PayMode.ACCOUNT_PAY);
                        item.setPayTime(new Date());
                        item.setPayValue(totalAmount);
                        item.setRemark(RedChoose.redChoose(redPacket.getRedType())+"支付:" + item.getPayValue());
                        item.setResultData(redPacket.getId());
                        tbOrderPaymentItemMapper.insert(item);
                        JSONObject json = new JSONObject();
                        json.put("resultMoney",resultMoney);
                        json.put("discount",discount);
                        return json;
                    }

                }
            }

        }
        JSONObject json = new JSONObject();
        json.put("resultMoney",resultMoney);
        json.put("discount",discount);
        return json;
    }

    /**
     *@Description:红包不够扣时扣充值账户赠送余额和充值剩余余额
     *@Author:disvenk.dai
     *@Date:9:40 2018/4/23 0023
     */
    @Transactional
    public JSONObject GoOnPayCharge1(String orderId,String authCode,String accountId,BigDecimal totalAmount,Long customerId,String brandId,String shopId){
        BigDecimal resultMoney = totalAmount;
        BigDecimal discount = BigDecimal.ZERO;
        TbAccount tbAccount = tbAccountMapper.selectById(accountId);
        BigDecimal remain = tbAccount.getRemain();

        //得到所有的充值项
        List<TbCardRechange> cardRechanges = tbCardRechangeMapper.selectEnableChargeRecord(customerId);

        for (TbCardRechange cardRechange :cardRechanges){
            //赠送余额不为0
            if(!(cardRechange.getRewardBalance().compareTo(BigDecimal.ZERO)<=0)){
                BigDecimal result = cardRechange.getRewardBalance().subtract(totalAmount);
                //如果赠送余额不够扣，扣充值剩余
                if(result.compareTo(BigDecimal.ZERO)<0){
                    resultMoney=result;
                    totalAmount = result.abs();
                    discount = discount.add(cardRechange.getRewardBalance());

                    //插入账户日志记录
                    TbAccountLog accountLog = new TbAccountLog();
                    accountLog.setId(ApplicationUtils.randomUUID());
                    accountLog.setMoney(cardRechange.getRewardBalance().multiply(BigDecimal.valueOf(-1)));
                    accountLog.setCreateTime(new Date());
                    accountLog.setPaymentType(2);
                    remain=remain.subtract(cardRechange.getRewardBalance());
                    accountLog.setRemain(remain);
                    accountLog.setRemark("充值赠送余额支出");
                    accountLog.setAccountId(accountId);
                    accountLog.setShopDetailId(shopId);
                    accountLog.setOrderId(orderId);
                    accountLog.setSource(TbAccountLog.CARD_SOURCE_CHARGE_REWARD);
                    tbAccountLogMapper.insert(accountLog);

                    //更改赠送剩余金额和插入支付记录
                    tbCardRechangeMapper.updateRewardBalance(cardRechange.getId(),BigDecimal.ZERO);
                    //更改account账户余额
                    tbAccountMapper.updateRemain(cardRechange.getRewardBalance(),accountId);

                    TbOrderPaymentItem item1 = new TbOrderPaymentItem();
                    item1.setId(ApplicationUtils.randomUUID());
                    item1.setOrderId(authCode);
                    item1.setPaymentModeId(PayMode.CARD_REWARD_PAY);
                    item1.setPayTime(new Date());
                    item1.setPayValue(cardRechange.getRewardBalance());
                    item1.setRemark(PayMode.getPayModeName(24)+ item1.getPayValue());
                    item1.setResultData(cardRechange.getId().toString());
                    tbOrderPaymentItemMapper.insert(item1);

                    //如果充值剩余不为0再扣充值剩余金额
                    if(!(cardRechange.getChargeBalance().compareTo(BigDecimal.ZERO)<=0)){
                        BigDecimal result2 = cardRechange.getChargeBalance().subtract(totalAmount);
                        //如果还不够扣则继续寻找下一条记录
                        if(result2.compareTo(BigDecimal.ZERO)<0) {
                            resultMoney=result2;
                            totalAmount = result2.abs();

                            //插入账户日志记录
                            TbAccountLog accountLog1 = new TbAccountLog();
                            accountLog1.setId(ApplicationUtils.randomUUID());
                            accountLog1.setMoney(cardRechange.getChargeBalance().multiply(BigDecimal.valueOf(-1)));
                            accountLog1.setCreateTime(new Date());
                            accountLog1.setPaymentType(2);
                            remain=remain.subtract(cardRechange.getChargeBalance());
                            accountLog1.setRemain(remain);
                            accountLog1.setRemark("充值剩余余额支出");
                            accountLog1.setAccountId(accountId);
                            accountLog1.setShopDetailId(shopId);
                            accountLog1.setOrderId(orderId);
                            accountLog1.setSource(TbAccountLog.CARD_SOURCE_CHARGE);
                            tbAccountLogMapper.insert(accountLog1);

                            //更新充值剩余金额和插入支付记录
                            tbCardRechangeMapper.updateChargeBalance(cardRechange.getId(),BigDecimal.ZERO);
                            //更改account账户余额
                            tbAccountMapper.updateRemain(cardRechange.getChargeBalance(),accountId);

                            TbOrderPaymentItem item2 = new TbOrderPaymentItem();
                            item2.setId(ApplicationUtils.randomUUID());
                            item2.setOrderId(authCode);
                            item2.setPaymentModeId(PayMode.CARD_CHARGE_PAY);
                            item2.setPayTime(new Date());
                            item2.setPayValue(cardRechange.getChargeBalance());
                            item2.setRemark(PayMode.getPayModeName(23)+":" + item2.getPayValue());
                            item2.setResultData(cardRechange.getId().toString());
                            tbOrderPaymentItemMapper.insert(item2);

                            continue;
                        }else if (result2.compareTo(BigDecimal.ZERO)==0){
                            resultMoney=result2;
                            //够扣了
                            //插入账户日志记录
                            TbAccountLog accountLog1 = new TbAccountLog();
                            accountLog1.setId(ApplicationUtils.randomUUID());
                            accountLog1.setMoney(cardRechange.getChargeBalance().multiply(BigDecimal.valueOf(-1)));
                            accountLog1.setCreateTime(new Date());
                            accountLog1.setPaymentType(2);
                            remain=remain.subtract(cardRechange.getChargeBalance());
                            accountLog1.setRemain(remain);
                            accountLog1.setRemark("充值剩余余额支出");
                            accountLog1.setAccountId(accountId);
                            accountLog1.setShopDetailId(shopId);
                            accountLog1.setOrderId(orderId);
                            accountLog1.setSource(TbAccountLog.CARD_SOURCE_CHARGE);
                            tbAccountLogMapper.insert(accountLog1);

                            //插入支付记录
                            tbCardRechangeMapper.updateChargeBalance(cardRechange.getId(),BigDecimal.ZERO);
                            //更改account账户余额
                            tbAccountMapper.updateRemain(cardRechange.getChargeBalance(),accountId);

                            TbOrderPaymentItem item22 = new TbOrderPaymentItem();
                            item22.setId(ApplicationUtils.randomUUID());
                            item22.setOrderId(authCode);
                            item22.setPaymentModeId(PayMode.CARD_CHARGE_PAY);
                            item22.setPayTime(new Date());
                            item22.setPayValue(cardRechange.getChargeBalance());
                            item22.setRemark(PayMode.getPayModeName(23)+":" + item22.getPayValue());
                            item22.setResultData(cardRechange.getId().toString());
                            tbOrderPaymentItemMapper.insert(item22);
                            //插入账户记录

                            JSONObject json = new JSONObject();
                            json.put("resultMoney",resultMoney);
                            json.put("discount",discount);
                            return json;
                        }else {
                            resultMoney=result2;
                            //够扣了
                            //插入账户日志记录
                            TbAccountLog accountLog1 = new TbAccountLog();
                            accountLog1.setId(ApplicationUtils.randomUUID());
                            accountLog1.setMoney(totalAmount.multiply(BigDecimal.valueOf(-1)));
                            accountLog1.setCreateTime(new Date());
                            accountLog1.setPaymentType(2);
                            remain=remain.subtract(totalAmount);
                            accountLog1.setRemain(remain);
                            accountLog1.setRemark("充值剩余余额支出");
                            accountLog1.setAccountId(accountId);
                            accountLog1.setShopDetailId(shopId);
                            accountLog1.setOrderId(orderId);
                            accountLog1.setSource(TbAccountLog.CARD_SOURCE_CHARGE);
                            tbAccountLogMapper.insert(accountLog1);

                            //插入支付记录
                            tbCardRechangeMapper.updateChargeBalance(cardRechange.getId(),result2);
                            //更改account账户余额
                            tbAccountMapper.updateRemain(totalAmount,accountId);

                            TbOrderPaymentItem item222 = new TbOrderPaymentItem();
                            item222.setId(ApplicationUtils.randomUUID());
                            item222.setOrderId(authCode);
                            item222.setPaymentModeId(PayMode.CARD_CHARGE_PAY);
                            item222.setPayTime(new Date());
                            item222.setPayValue(totalAmount);
                            item222.setRemark(PayMode.getPayModeName(23)+":" + item222.getPayValue());
                            item222.setResultData(cardRechange.getId().toString());
                            tbOrderPaymentItemMapper.insert(item222);
                            //插入账户记录

                            JSONObject json = new JSONObject();
                            json.put("resultMoney",resultMoney);
                            json.put("discount",discount);
                            return json;
                        }

                    }else {
                        //充值剩余金额为0,
                        continue;
                    }

                }else if(result.compareTo(BigDecimal.ZERO)==0){
                    resultMoney=result;
                    discount = discount.add(cardRechange.getRewardBalance());
                    //赠余够扣了
                    //插入账户日志记录
                    TbAccountLog accountLog = new TbAccountLog();
                    accountLog.setId(ApplicationUtils.randomUUID());
                    accountLog.setMoney(cardRechange.getRewardBalance().multiply(BigDecimal.valueOf(-1)));
                    accountLog.setCreateTime(new Date());
                    accountLog.setPaymentType(2);
                    remain=remain.subtract(cardRechange.getRewardBalance());
                    accountLog.setRemain(remain);
                    accountLog.setRemark("充值赠送余额支出");
                    accountLog.setAccountId(accountId);
                    accountLog.setShopDetailId(shopId);
                    accountLog.setOrderId(orderId);
                    accountLog.setSource(TbAccountLog.CARD_SOURCE_CHARGE_REWARD);
                    tbAccountLogMapper.insertSelective(accountLog);

                    //插入支付记录
                    tbCardRechangeMapper.updateRewardBalance(cardRechange.getId(),BigDecimal.ZERO);
                    //更改account账户余额
                    tbAccountMapper.updateRemain(cardRechange.getRewardBalance(),accountId);

                    TbOrderPaymentItem item3 = new TbOrderPaymentItem();
                    item3.setId(ApplicationUtils.randomUUID());
                    item3.setOrderId(authCode);
                    item3.setPaymentModeId(PayMode.CARD_REWARD_PAY);
                    item3.setPayTime(new Date());
                    item3.setPayValue(cardRechange.getRewardBalance());
                    item3.setRemark(PayMode.getPayModeName(24)+":" + item3.getPayValue());
                    item3.setResultData(cardRechange.getId().toString());
                    tbOrderPaymentItemMapper.insert(item3);
                    //插入账户记录
                    JSONObject json = new JSONObject();
                    json.put("resultMoney",resultMoney);
                    json.put("discount",discount);
                    return json;
                }else {
                    resultMoney=result;
                    discount = discount.add(totalAmount);
                    //赠余够扣了并且还有多余
                    //插入账户日志记录
                    TbAccountLog accountLog = new TbAccountLog();
                    accountLog.setId(ApplicationUtils.randomUUID());
                    accountLog.setMoney(totalAmount.multiply(BigDecimal.valueOf(-1)));
                    accountLog.setCreateTime(new Date());
                    accountLog.setPaymentType(2);
                    remain=remain.subtract(totalAmount);
                    accountLog.setRemain(remain);
                    accountLog.setRemark("充值赠送余额支出");
                    accountLog.setAccountId(accountId);
                    accountLog.setShopDetailId(shopId);
                    accountLog.setOrderId(orderId);
                    accountLog.setSource(TbAccountLog.CARD_SOURCE_CHARGE_REWARD);
                    tbAccountLogMapper.insert(accountLog);

                    //支付记录
                    tbCardRechangeMapper.updateRewardBalance(cardRechange.getId(),result);
                    //更改account账户余额
                    tbAccountMapper.updateRemain(totalAmount,accountId);

                    TbOrderPaymentItem item4 = new TbOrderPaymentItem();
                    item4.setId(ApplicationUtils.randomUUID());
                    item4.setOrderId(authCode);
                    item4.setPaymentModeId(PayMode.CARD_REWARD_PAY);
                    item4.setPayTime(new Date());
                    item4.setPayValue(totalAmount);
                    item4.setRemark(PayMode.getPayModeName(24)+":" + item4.getPayValue());
                    item4.setResultData(cardRechange.getId().toString());
                    tbOrderPaymentItemMapper.insert(item4);
                    //插入账户记录
                    JSONObject json = new JSONObject();
                    json.put("resultMoney",resultMoney);
                    json.put("discount",discount);
                    return json;
                }

            }else {
                //如果赠送余额为0直接扣充值剩余金额
                if(!(cardRechange.getChargeBalance().compareTo(BigDecimal.ZERO)<=0)){
                    BigDecimal result3 = cardRechange.getChargeBalance().subtract(totalAmount);
                    //如果还不够扣则继续寻找下一条记录
                    if(result3.compareTo(BigDecimal.ZERO)<0) {
                        resultMoney=result3;
                        totalAmount = result3.abs();
                        discount = discount.add(cardRechange.getChargeBalance());

                        //插入账户日志记录
                        TbAccountLog accountLog = new TbAccountLog();
                        accountLog.setId(ApplicationUtils.randomUUID());
                        accountLog.setMoney(cardRechange.getChargeBalance().multiply(BigDecimal.valueOf(-1)));
                        accountLog.setCreateTime(new Date());
                        accountLog.setPaymentType(2);
                        remain= remain.subtract(cardRechange.getChargeBalance());
                        accountLog.setRemain(remain);
                        accountLog.setRemark("充值剩余余额支出");
                        accountLog.setAccountId(accountId);
                        accountLog.setShopDetailId(shopId);
                        accountLog.setOrderId(orderId);
                        accountLog.setSource(TbAccountLog.CARD_SOURCE_CHARGE);
                        tbAccountLogMapper.insert(accountLog);

                        //更新充值剩余金额和插入支付记录
                        tbCardRechangeMapper.updateChargeBalance(cardRechange.getId(),BigDecimal.ZERO);
                        //更改account账户余额
                        tbAccountMapper.updateRemain(cardRechange.getChargeBalance(),accountId);

                        TbOrderPaymentItem item = new TbOrderPaymentItem();
                        item.setId(ApplicationUtils.randomUUID());
                        item.setOrderId(authCode);
                        item.setPaymentModeId(PayMode.CARD_CHARGE_PAY);
                        item.setPayTime(new Date());
                        item.setPayValue(cardRechange.getChargeBalance());
                        item.setRemark(PayMode.getPayModeName(23)+":" + item.getPayValue());
                        item.setResultData(cardRechange.getId().toString());
                        tbOrderPaymentItemMapper.insert(item);
                        //插入账户记录

                        continue;
                    }else if (result3.compareTo(BigDecimal.ZERO)==0){
                        resultMoney=result3;
                        discount = discount.add(cardRechange.getChargeBalance());
                        //充余够扣了
                        //更改余额
                        //插入支付记录

                        //插入账户日志记录
                        TbAccountLog accountLog = new TbAccountLog();
                        accountLog.setId(ApplicationUtils.randomUUID());
                        accountLog.setMoney(cardRechange.getChargeBalance().multiply(BigDecimal.valueOf(-1)));
                        accountLog.setCreateTime(new Date());
                        accountLog.setPaymentType(2);
                        remain = remain.subtract(cardRechange.getChargeBalance());
                        accountLog.setRemain(remain);
                        accountLog.setRemark("充值剩余余额支出");
                        accountLog.setAccountId(accountId);
                        accountLog.setShopDetailId(shopId);
                        accountLog.setOrderId(orderId);
                        accountLog.setSource(TbAccountLog.CARD_SOURCE_CHARGE);
                        tbAccountLogMapper.insert(accountLog);

                        //更改充值项余额
                        tbCardRechangeMapper.updateChargeBalance(cardRechange.getId(),BigDecimal.ZERO);
                        //更改account账户余额
                        tbAccountMapper.updateRemain(cardRechange.getChargeBalance(),accountId);

                        TbOrderPaymentItem item = new TbOrderPaymentItem();
                        item.setId(ApplicationUtils.randomUUID());
                        item.setOrderId(authCode);
                        item.setPaymentModeId(PayMode.CARD_CHARGE_PAY);
                        item.setPayTime(new Date());
                        item.setPayValue(cardRechange.getChargeBalance());
                        item.setRemark(PayMode.getPayModeName(23)+":" + item.getPayValue());
                        item.setResultData(cardRechange.getId().toString());
                        tbOrderPaymentItemMapper.insert(item);

                        JSONObject json = new JSONObject();
                        json.put("resultMoney",resultMoney);
                        json.put("discount",discount);
                        return json;
                    }else {
                        resultMoney=result3;

                        tbCardRechangeMapper.updateChargeBalance(cardRechange.getId(),result3);

                        //插入账户日志记录
                        TbAccountLog accountLog = new TbAccountLog();
                        accountLog.setId(ApplicationUtils.randomUUID());
                        accountLog.setMoney(totalAmount.multiply(BigDecimal.valueOf(-1)));
                        accountLog.setCreateTime(new Date());
                        accountLog.setPaymentType(2);
                        remain = remain.subtract(totalAmount);
                        accountLog.setRemain(remain);
                        accountLog.setRemark("充值剩余余额支出");
                        accountLog.setAccountId(accountId);
                        accountLog.setShopDetailId(shopId);
                        accountLog.setOrderId(orderId);
                        accountLog.setSource(TbAccountLog.CARD_SOURCE_CHARGE);
                        tbAccountLogMapper.insert(accountLog);

                        //更改account账户余额
                        tbAccountMapper.updateRemain(totalAmount,accountId);

                        TbOrderPaymentItem item = new TbOrderPaymentItem();
                        item.setId(ApplicationUtils.randomUUID());
                        item.setOrderId(authCode);
                        item.setPaymentModeId(PayMode.CARD_CHARGE_PAY);
                        item.setPayTime(new Date());
                        item.setPayValue(totalAmount);
                        item.setRemark(PayMode.getPayModeName(23)+":" + item.getPayValue());
                        item.setResultData(cardRechange.getId().toString());
                        tbOrderPaymentItemMapper.insert(item);

                        JSONObject json = new JSONObject();
                        json.put("resultMoney",resultMoney);
                        json.put("discount",discount);
                        return json;
                    }

                }else {
                    //充值剩余金额为0
                    continue;
                }
            }
        }
        JSONObject json = new JSONObject();
        json.put("resultMoney",resultMoney);
        json.put("discount",discount);
        return json;
    }


    /**
     *@Description:红包不够扣时扣充值账户赠送余额和充值剩余余额
     *@Author:disvenk.dai
     *@Date:9:40 2018/4/23 0023
     */
    @Transactional
    public JSONObject GoOnPayCharge(String orderId,String authCode,String accountId,BigDecimal totalAmount,Long customerId,String brandId,String shopId){
        BigDecimal resultMoney = totalAmount;
        BigDecimal discount = BigDecimal.ZERO;
        TbAccount tbAccount = tbAccountMapper.selectById(accountId);
        BigDecimal remain = tbAccount.getRemain();
        List<TbOrderPaymentItem> paymentItems = new ArrayList<>();

        //得到所有的充值项
        List<TbCardRechange> cardRechanges = tbCardRechangeMapper.selectEnableChargeRecord(customerId);

        for (TbCardRechange cardRechange :cardRechanges){
            //充值本金不为0
            if(!(cardRechange.getChargeBalance().compareTo(BigDecimal.ZERO)<=0)){
                BigDecimal result = cardRechange.getChargeBalance().subtract(totalAmount);
                //如果本金不够扣
                if(result.compareTo(BigDecimal.ZERO)<0){
                    resultMoney=result;
                    totalAmount = result.abs();

                    //插入账户日志记录

                    TbAccountLog accountLog = new TbAccountLog();
                    accountLog.setId(ApplicationUtils.randomUUID());
                    accountLog.setMoney(cardRechange.getChargeBalance().multiply(BigDecimal.valueOf(-1)));
                    accountLog.setCreateTime(new Date());
                    accountLog.setPaymentType(2);
                    remain=remain.subtract(cardRechange.getChargeBalance());
                    accountLog.setRemain(remain);
                    accountLog.setRemark("充值本金支出");
                    accountLog.setAccountId(accountId);
                    accountLog.setShopDetailId(shopId);
                    accountLog.setOrderId(orderId);
                    accountLog.setSource(TbAccountLog.CARD_SOURCE_CHARGE);
                    tbAccountLogMapper.insert(accountLog);

                    //更改充值本金和插入支付记录
                    tbCardRechangeMapper.updateChargeBalance(cardRechange.getId(),BigDecimal.ZERO);
                    //更改account账户余额
                    tbAccountMapper.updateRemain(cardRechange.getChargeBalance(),accountId);

                    TbOrderPaymentItem item1 = new TbOrderPaymentItem();
                    item1.setId(ApplicationUtils.randomUUID());
                    item1.setOrderId(authCode);
                    item1.setPaymentModeId(PayMode.CARD_CHARGE_PAY);
                    item1.setPayTime(new Date());
                    item1.setPayValue(cardRechange.getChargeBalance());
                    item1.setRemark(PayMode.getPayModeName(23)+ item1.getPayValue());
                    item1.setResultData(cardRechange.getId().toString());
                    paymentItems.add(item1);
                    tbOrderPaymentItemMapper.insert(item1);

                    //如果赠送不为0再扣赠送
                    if(!(cardRechange.getRewardBalance().compareTo(BigDecimal.ZERO)<=0)){
                        BigDecimal result2 = cardRechange.getRewardBalance().subtract(totalAmount);
                        //如果还不够扣则继续寻找下一条记录
                        if(result2.compareTo(BigDecimal.ZERO)<0) {
                            resultMoney=result2;
                            totalAmount = result2.abs();
                            discount = discount.add(cardRechange.getRewardBalance());

                            //插入账户日志记录
                            TbAccountLog accountLog1 = new TbAccountLog();
                            accountLog1.setId(ApplicationUtils.randomUUID());
                            accountLog1.setMoney(cardRechange.getRewardBalance().multiply(BigDecimal.valueOf(-1)));
                            accountLog1.setCreateTime(new Date());
                            accountLog1.setPaymentType(2);
                            remain=remain.subtract(cardRechange.getRewardBalance());
                            accountLog1.setRemain(remain);
                            accountLog1.setRemark("充值赠送支出");
                            accountLog1.setAccountId(accountId);
                            accountLog1.setShopDetailId(shopId);
                            accountLog1.setOrderId(orderId);
                            accountLog1.setSource(TbAccountLog.CARD_SOURCE_CHARGE_REWARD);
                            tbAccountLogMapper.insert(accountLog1);

                            //更新充值剩余金额和插入支付记录
                            tbCardRechangeMapper.updateRewardBalance(cardRechange.getId(),BigDecimal.ZERO);
                            //更改account账户余额
                            tbAccountMapper.updateRemain(cardRechange.getRewardBalance(),accountId);

                            TbOrderPaymentItem item2 = new TbOrderPaymentItem();
                            item2.setId(ApplicationUtils.randomUUID());
                            item2.setOrderId(authCode);
                            item2.setPaymentModeId(PayMode.CARD_REWARD_PAY);
                            item2.setPayTime(new Date());
                            item2.setPayValue(cardRechange.getRewardBalance());
                            item2.setRemark(PayMode.getPayModeName(24)+":" + item2.getPayValue());
                            item2.setResultData(cardRechange.getId().toString());
                            paymentItems.add(item2);
                            tbOrderPaymentItemMapper.insert(item2);

                            continue;
                        }else if (result2.compareTo(BigDecimal.ZERO)==0){
                            resultMoney=result2;
                            discount = discount.add(cardRechange.getRewardBalance());
                            //够扣了
                            //插入账户日志记录
                            TbAccountLog accountLog1 = new TbAccountLog();
                            accountLog1.setId(ApplicationUtils.randomUUID());
                            accountLog1.setMoney(cardRechange.getRewardBalance().multiply(BigDecimal.valueOf(-1)));
                            accountLog1.setCreateTime(new Date());
                            accountLog1.setPaymentType(2);
                            remain=remain.subtract(cardRechange.getRewardBalance());
                            accountLog1.setRemain(remain);
                            accountLog1.setRemark("充值赠送支出");
                            accountLog1.setAccountId(accountId);
                            accountLog1.setShopDetailId(shopId);
                            accountLog1.setOrderId(orderId);
                            accountLog1.setSource(TbAccountLog.CARD_SOURCE_CHARGE_REWARD);
                            tbAccountLogMapper.insert(accountLog1);

                            //插入支付记录
                            tbCardRechangeMapper.updateRewardBalance(cardRechange.getId(),BigDecimal.ZERO);
                            //更改account账户余额
                            tbAccountMapper.updateRemain(cardRechange.getRewardBalance(),accountId);

                            TbOrderPaymentItem item22 = new TbOrderPaymentItem();
                            item22.setId(ApplicationUtils.randomUUID());
                            item22.setOrderId(authCode);
                            item22.setPaymentModeId(PayMode.CARD_REWARD_PAY);
                            item22.setPayTime(new Date());
                            item22.setPayValue(cardRechange.getRewardBalance());
                            item22.setRemark(PayMode.getPayModeName(24)+":" + item22.getPayValue());
                            item22.setResultData(cardRechange.getId().toString());
                            paymentItems.add(item22);
                            tbOrderPaymentItemMapper.insert(item22);
                            //插入账户记录

                            JSONObject json = new JSONObject();
                            json.put("resultMoney",resultMoney);
                            json.put("discount",discount);
                            json.put("paymentItems",paymentItems);
                            return json;
                        }else {
                            resultMoney=result2;
                            discount = discount.add(totalAmount);
                            //够扣了
                            //插入账户日志记录
                            TbAccountLog accountLog1 = new TbAccountLog();
                            accountLog1.setId(ApplicationUtils.randomUUID());
                            accountLog1.setMoney(totalAmount.multiply(BigDecimal.valueOf(-1)));
                            accountLog1.setCreateTime(new Date());
                            accountLog1.setPaymentType(2);
                            remain=remain.subtract(totalAmount);
                            accountLog1.setRemain(remain);
                            accountLog1.setRemark("充值赠送支出");
                            accountLog1.setAccountId(accountId);
                            accountLog1.setShopDetailId(shopId);
                            accountLog1.setOrderId(orderId);
                            accountLog1.setSource(TbAccountLog.CARD_SOURCE_CHARGE_REWARD);
                            tbAccountLogMapper.insert(accountLog1);

                            //插入支付记录
                            tbCardRechangeMapper.updateRewardBalance(cardRechange.getId(),result2);
                            //更改account账户余额
                            tbAccountMapper.updateRemain(totalAmount,accountId);

                            TbOrderPaymentItem item222 = new TbOrderPaymentItem();
                            item222.setId(ApplicationUtils.randomUUID());
                            item222.setOrderId(authCode);
                            item222.setPaymentModeId(PayMode.CARD_REWARD_PAY);
                            item222.setPayTime(new Date());
                            item222.setPayValue(totalAmount);
                            item222.setRemark(PayMode.getPayModeName(24)+":" + item222.getPayValue());
                            item222.setResultData(cardRechange.getId().toString());
                            paymentItems.add(item222);
                            tbOrderPaymentItemMapper.insert(item222);
                            //插入账户记录

                            JSONObject json = new JSONObject();
                            json.put("resultMoney",resultMoney);
                            json.put("discount",discount);
                            json.put("paymentItems",paymentItems);
                            return json;
                        }

                    }else {
                        //赠送金额为0,
                        continue;
                    }

                }else if(result.compareTo(BigDecimal.ZERO)==0){
                    resultMoney=result;

                    //本金够扣了
                    //插入账户日志记录
                    TbAccountLog accountLog = new TbAccountLog();
                    accountLog.setId(ApplicationUtils.randomUUID());
                    accountLog.setMoney(cardRechange.getChargeBalance().multiply(BigDecimal.valueOf(-1)));
                    accountLog.setCreateTime(new Date());
                    accountLog.setPaymentType(2);
                    remain=remain.subtract(cardRechange.getChargeBalance());
                    accountLog.setRemain(remain);
                    accountLog.setRemark("充值本金支出");
                    accountLog.setAccountId(accountId);
                    accountLog.setShopDetailId(shopId);
                    accountLog.setOrderId(orderId);
                    accountLog.setSource(TbAccountLog.CARD_SOURCE_CHARGE);
                    tbAccountLogMapper.insertSelective(accountLog);

                    //插入支付记录
                    tbCardRechangeMapper.updateChargeBalance(cardRechange.getId(),BigDecimal.ZERO);
                    //更改account账户余额
                    tbAccountMapper.updateRemain(cardRechange.getChargeBalance(),accountId);

                    TbOrderPaymentItem item3 = new TbOrderPaymentItem();
                    item3.setId(ApplicationUtils.randomUUID());
                    item3.setOrderId(authCode);
                    item3.setPaymentModeId(PayMode.CARD_CHARGE_PAY);
                    item3.setPayTime(new Date());
                    item3.setPayValue(cardRechange.getChargeBalance());
                    item3.setRemark(PayMode.getPayModeName(23)+":" + item3.getPayValue());
                    item3.setResultData(cardRechange.getId().toString());
                    paymentItems.add(item3);
                    tbOrderPaymentItemMapper.insert(item3);
                    //插入账户记录
                    JSONObject json = new JSONObject();
                    json.put("resultMoney",resultMoney);
                    json.put("discount",discount);
                    json.put("paymentItems",paymentItems);
                    return json;
                }else {
                    resultMoney=result;
                    //本金够扣了并且还有多余
                    //插入账户日志记录
                    TbAccountLog accountLog = new TbAccountLog();
                    accountLog.setId(ApplicationUtils.randomUUID());
                    accountLog.setMoney(totalAmount.multiply(BigDecimal.valueOf(-1)));
                    accountLog.setCreateTime(new Date());
                    accountLog.setPaymentType(2);
                    remain=remain.subtract(totalAmount);
                    accountLog.setRemain(remain);
                    accountLog.setRemark("充值本金支出");
                    accountLog.setAccountId(accountId);
                    accountLog.setShopDetailId(shopId);
                    accountLog.setOrderId(orderId);
                    accountLog.setSource(TbAccountLog.CARD_SOURCE_CHARGE_REWARD);
                    tbAccountLogMapper.insert(accountLog);

                    //支付记录
                    tbCardRechangeMapper.updateChargeBalance(cardRechange.getId(),result);
                    //更改account账户余额
                    tbAccountMapper.updateRemain(totalAmount,accountId);

                    TbOrderPaymentItem item4 = new TbOrderPaymentItem();
                    item4.setId(ApplicationUtils.randomUUID());
                    item4.setOrderId(authCode);
                    item4.setPaymentModeId(PayMode.CARD_CHARGE_PAY);
                    item4.setPayTime(new Date());
                    item4.setPayValue(totalAmount);
                    item4.setRemark(PayMode.getPayModeName(23)+":" + item4.getPayValue());
                    item4.setResultData(cardRechange.getId().toString());
                    paymentItems.add(item4);
                    tbOrderPaymentItemMapper.insert(item4);
                    //插入账户记录
                    JSONObject json = new JSONObject();
                    json.put("resultMoney",resultMoney);
                    json.put("discount",discount);
                    json.put("paymentItems",paymentItems);
                    return json;
                }

            }else {
                //如果本金余额为0直接扣赠送金额
                if(!(cardRechange.getRewardBalance().compareTo(BigDecimal.ZERO)<=0)){
                    BigDecimal result3 = cardRechange.getRewardBalance().subtract(totalAmount);
                    //如果还不够扣则继续寻找下一条记录
                    if(result3.compareTo(BigDecimal.ZERO)<0) {
                        resultMoney=result3;
                        totalAmount = result3.abs();
                        discount = discount.add(cardRechange.getRewardBalance());

                        //插入账户日志记录
                        TbAccountLog accountLog = new TbAccountLog();
                        accountLog.setId(ApplicationUtils.randomUUID());
                        accountLog.setMoney(cardRechange.getRewardBalance().multiply(BigDecimal.valueOf(-1)));
                        accountLog.setCreateTime(new Date());
                        accountLog.setPaymentType(2);
                        remain= remain.subtract(cardRechange.getRewardBalance());
                        accountLog.setRemain(remain);
                        accountLog.setRemark("充值赠送支出");
                        accountLog.setAccountId(accountId);
                        accountLog.setShopDetailId(shopId);
                        accountLog.setOrderId(orderId);
                        accountLog.setSource(TbAccountLog.CARD_SOURCE_CHARGE_REWARD);
                        tbAccountLogMapper.insert(accountLog);

                        //更新充值剩余金额和插入支付记录
                        tbCardRechangeMapper.updateRewardBalance(cardRechange.getId(),BigDecimal.ZERO);
                        //更改account账户余额
                        tbAccountMapper.updateRemain(cardRechange.getRewardBalance(),accountId);

                        TbOrderPaymentItem item = new TbOrderPaymentItem();
                        item.setId(ApplicationUtils.randomUUID());
                        item.setOrderId(authCode);
                        item.setPaymentModeId(PayMode.CARD_REWARD_PAY);
                        item.setPayTime(new Date());
                        item.setPayValue(cardRechange.getRewardBalance());
                        item.setRemark(PayMode.getPayModeName(24)+":" + item.getPayValue());
                        item.setResultData(cardRechange.getId().toString());
                        paymentItems.add(item);
                        tbOrderPaymentItemMapper.insert(item);
                        //插入账户记录

                        continue;
                    }else if (result3.compareTo(BigDecimal.ZERO)==0){
                        resultMoney=result3;
                        discount = discount.add(cardRechange.getRewardBalance());
                        //充余够扣了
                        //更改余额
                        //插入支付记录

                        //插入账户日志记录
                        TbAccountLog accountLog = new TbAccountLog();
                        accountLog.setId(ApplicationUtils.randomUUID());
                        accountLog.setMoney(cardRechange.getRewardBalance().multiply(BigDecimal.valueOf(-1)));
                        accountLog.setCreateTime(new Date());
                        accountLog.setPaymentType(2);
                        remain = remain.subtract(cardRechange.getRewardBalance());
                        accountLog.setRemain(remain);
                        accountLog.setRemark("充值赠送支出");
                        accountLog.setAccountId(accountId);
                        accountLog.setShopDetailId(shopId);
                        accountLog.setOrderId(orderId);
                        accountLog.setSource(TbAccountLog.CARD_SOURCE_CHARGE_REWARD);
                        tbAccountLogMapper.insert(accountLog);

                        //更改充值项余额
                        tbCardRechangeMapper.updateRewardBalance(cardRechange.getId(),BigDecimal.ZERO);
                        //更改account账户余额
                        tbAccountMapper.updateRemain(cardRechange.getRewardBalance(),accountId);

                        TbOrderPaymentItem item = new TbOrderPaymentItem();
                        item.setId(ApplicationUtils.randomUUID());
                        item.setOrderId(authCode);
                        item.setPaymentModeId(PayMode.CARD_REWARD_PAY);
                        item.setPayTime(new Date());
                        item.setPayValue(cardRechange.getRewardBalance());
                        item.setRemark(PayMode.getPayModeName(24)+":" + item.getPayValue());
                        item.setResultData(cardRechange.getId().toString());
                        paymentItems.add(item);
                        tbOrderPaymentItemMapper.insert(item);

                        JSONObject json = new JSONObject();
                        json.put("resultMoney",resultMoney);
                        json.put("discount",discount);
                        json.put("paymentItems",paymentItems);
                        return json;
                    }else {
                        resultMoney=result3;
                        discount = discount.add(totalAmount);

                        tbCardRechangeMapper.updateRewardBalance(cardRechange.getId(),result3);

                        //插入账户日志记录
                        TbAccountLog accountLog = new TbAccountLog();
                        accountLog.setId(ApplicationUtils.randomUUID());
                        accountLog.setMoney(totalAmount.multiply(BigDecimal.valueOf(-1)));
                        accountLog.setCreateTime(new Date());
                        accountLog.setPaymentType(2);
                        remain = remain.subtract(totalAmount);
                        accountLog.setRemain(remain);
                        accountLog.setRemark("充值赠送支出");
                        accountLog.setAccountId(accountId);
                        accountLog.setShopDetailId(shopId);
                        accountLog.setOrderId(orderId);
                        accountLog.setSource(TbAccountLog.CARD_SOURCE_CHARGE_REWARD);
                        tbAccountLogMapper.insert(accountLog);

                        //更改account账户余额
                        tbAccountMapper.updateRemain(totalAmount,accountId);


                        TbOrderPaymentItem item = new TbOrderPaymentItem();
                        item.setId(ApplicationUtils.randomUUID());
                        item.setOrderId(authCode);
                        item.setPaymentModeId(PayMode.CARD_REWARD_PAY);
                        item.setPayTime(new Date());
                        item.setPayValue(totalAmount);
                        item.setRemark(PayMode.getPayModeName(24)+":" + item.getPayValue());
                        item.setResultData(cardRechange.getId().toString());
                        paymentItems.add(item);
                        tbOrderPaymentItemMapper.insert(item);

                        JSONObject json = new JSONObject();
                        json.put("resultMoney",resultMoney);
                        json.put("discount",discount);
                        json.put("paymentItems",paymentItems);
                        return json;
                    }

                }else {
                    //赠送金额为0
                    continue;
                }
            }
        }
        JSONObject json = new JSONObject();
        json.put("resultMoney",resultMoney);
        json.put("discount",discount);
        json.put("paymentItems",paymentItems);
        return json;
    }

    /**
     * 判断该储值卡是否享受折扣， 返回的是折扣率
     * @param cardCustomer
     * @return
     */
    public BigDecimal whetherTheDiscount(TbCardCustomer cardCustomer){
        if(cardCustomer.getAccountId()==null){
            return BigDecimal.ZERO;
        }


        TbCardDiscount cardDiscount = tbCardDiscountMapper.selectById(cardCustomer.getDiscountId());
        List<TbCardDiscountDetail> tbCardDiscountDetails = tbCardDiscountDetailMapper.selectdiscountId(cardCustomer.getDiscountId());
        if(cardDiscount!=null){
            cardDiscount.setTbCardDiscountDetailList(tbCardDiscountDetails);
        }
        //得到当前时间
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        //得到当前星期几
        Integer nowWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (nowWeek == 0) {
            nowWeek = 7;
        }
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        //折扣率
        BigDecimal discount = BigDecimal.ZERO;
        //标识该卡是否享受折扣
        boolean isDiscount = false;
        try {
            //当前时间
            Date nowDate = df.parse(df.format(new Date()));
            //得到所有参加折扣的日期
            if(cardDiscount==null){
                return discount;

            }
            for (TbCardDiscountDetail cardDiscountDetail : cardDiscount.getTbCardDiscountDetailList()) {
                String[] weeks = cardDiscountDetail.getDiscountWeek().split(",");
                //开始时间
                Date beginDate = df.parse(cardDiscountDetail.getDiscountTime().split(",")[0]);
                //结束时间
                Date endDate = df.parse(cardDiscountDetail.getDiscountTime().split(",")[1]);
                for (String day : weeks) {
                    //判断是不是在折扣时间范围内
                    if (Integer.valueOf(day).equals(nowWeek) &&
                            (nowDate.getTime() >= beginDate.getTime() && nowDate.getTime() <= endDate.getTime())) {
                        isDiscount = true;
                        discount = new BigDecimal(cardDiscountDetail.getDiscount());
                        break;
                    }
                }
                //已能折扣退出循环
                if (isDiscount) {
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return discount;
    }

}
