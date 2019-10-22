package com.resto.msgc.backend.card.service;

import com.resto.conf.util.ApplicationUtils;
import com.resto.core.util.DateUtil;
import com.resto.msgc.backend.card.dto.ChargeCardDto;
import com.resto.msgc.backend.card.dto.OneChargeCardDto;
import com.resto.msgc.backend.card.dto.PinCardMoneyDto;
import com.resto.msgc.backend.card.entity.*;
import com.resto.msgc.backend.card.mapper.*;
import com.resto.msgc.backend.card.util.Constant;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by bruce on 2017-12-18 16:35
 */
@Service
public class CardChargeService {

    private Logger log = LoggerFactory.getLogger(CardChargeService.class);

    @Autowired
    private TbCardCustomerMapper customerMapper;
    @Autowired
    private TbAccountMapper accountMapper;
    @Autowired
    private TbChargeSettingMapper chargeSettingMapper;
    @Autowired
    private TbCardRechargeLogMapper rechargeLogMapper;
    @Autowired
    private TbChargeOrderMapper orderMapper;
    @Autowired
    private TbAccountLogMapper accountLogMapper;
    @Autowired
    private TbCustomerMapper tbCustomerMapper;
    @Autowired
    private TbCardRechangeMapper tbCardRechangeMapper;

    @Transactional
    public boolean chargeCard(ChargeCardDto chargeCardDto) {

        BigDecimal total;
        TbCardRechargeLog rechargeLog = new TbCardRechargeLog();
        TbAccount tbAccount = new TbAccount();
        TbChargeOrder tbChargeOrder = new TbChargeOrder();
        TbAccountLog tbAccountLog = new TbAccountLog();
        String chargeOrderId = ApplicationUtils.randomUUID();

        List<TbCardCustomer> customer = customerMapper.findByCardId(chargeCardDto.getCardId());
        if (customer.size() > 1) {
            log.error("通过该cardId：" + chargeCardDto.getCardId() + "找到两条");
            return false;
        }
        String accountId = customer.get(0).getAccountId();

        TbAccount findAccount = accountMapper.selectByPrimaryKey(accountId);
        if (findAccount == null) {
            log.error("找不到该账户");
            return false;
        }
        BigDecimal currentRemain = findAccount.getRemain();

        String customerId = "";
        TbCustomer findCustomer = new TbCustomer();
        findCustomer.setAccountId(accountId);
        TbCustomer tbCustomer = tbCustomerMapper.selectOne(findCustomer);
        if (tbCustomer != null) {
            customerId = tbCustomer.getId();
        }

        if (StringUtils.isBlank(chargeCardDto.getActivityId())
                || chargeCardDto.getActivityId().equals("on")) {
            tbChargeOrder.setType(2);
            tbChargeOrder.setChargeMoney(chargeCardDto.getAmount());
            tbChargeOrder.setRewardMoney(BigDecimal.ZERO);
            tbChargeOrder.setChargeBalance(chargeCardDto.getAmount());
            tbChargeOrder.setRewardBalance(BigDecimal.ZERO);
            tbChargeOrder.setTotalBalance(chargeCardDto.getAmount());
            total = chargeCardDto.getAmount();

            tbAccountLog.setId(ApplicationUtils.randomUUID());
            tbAccountLog.setCreateTime(new Date());
            tbAccountLog.setMoney(chargeCardDto.getAmount());
            tbAccountLog.setPaymentType(1);
            tbAccountLog.setSource(10);
            tbAccountLog.setAccountId(accountId);
            tbAccountLog.setRemain(currentRemain.add(chargeCardDto.getAmount()));
            tbAccountLog.setRemark("充值金额");
            accountLogMapper.insertSelective(tbAccountLog);

            rechargeLog.setType(1);
            rechargeLog.setRemain(tbAccountLog.getRemain());
        } else {
            TbChargeSetting chargeSetting = chargeSettingMapper.selectByPrimaryKey(chargeCardDto.getActivityId());
            if (chargeSetting != null) {
                tbChargeOrder.setType(3);
                tbChargeOrder.setChargeMoney(chargeSetting.getChargeMoney());
                tbChargeOrder.setRewardMoney(chargeSetting.getRewardMoney());
                tbChargeOrder.setChargeBalance(chargeSetting.getChargeMoney());
                tbChargeOrder.setRewardBalance(chargeSetting.getRewardMoney());
                tbChargeOrder.setTotalBalance(chargeSetting.getChargeMoney().add(chargeSetting.getRewardMoney()));
                tbChargeOrder.setId(chargeSetting.getId());
                total = tbChargeOrder.getTotalBalance();

                tbAccountLog.setId(ApplicationUtils.randomUUID());
                tbAccountLog.setCreateTime(new Date());
                tbAccountLog.setMoney(chargeSetting.getChargeMoney());
                tbAccountLog.setPaymentType(1);
                tbAccountLog.setSource(10);
                tbAccountLog.setAccountId(accountId);
                tbAccountLog.setRemain(currentRemain.add(chargeSetting.getChargeMoney()));
                tbAccountLog.setRemark("充值金额");
                accountLogMapper.insertSelective(tbAccountLog);

                TbAccountLog tbAccountLog1 = new TbAccountLog();
                tbAccountLog1.setId(ApplicationUtils.randomUUID());
                tbAccountLog1.setCreateTime(new Date());
                tbAccountLog1.setMoney(chargeSetting.getRewardMoney());
                tbAccountLog1.setPaymentType(1);
                tbAccountLog1.setSource(11);
                tbAccountLog1.setAccountId(accountId);
                tbAccountLog1.setRemain(currentRemain.add(chargeSetting.getChargeMoney()).add(chargeSetting.getRewardMoney()));
                tbAccountLog1.setRemark("充值赠送");
                accountLogMapper.insertSelective(tbAccountLog1);

                rechargeLog.setType(0);
                rechargeLog.setRemain(tbAccountLog.getRemain());
            } else {
                log.error("找不到充值活动");
                return false;
            }
        }
        //操作tb_charge_order
        tbChargeOrder.setId(chargeOrderId);
        tbChargeOrder.setCustomerId(customerId);
        tbChargeOrder.setOrderState(Byte.valueOf("1"));
        tbChargeOrder.setCreateTime(new Date());
        tbChargeOrder.setFinishTime(new Date());
        orderMapper.insertSelective(tbChargeOrder);

        //操作tb_account表
        tbAccount.setId(accountId);
        tbAccount.setRemain(currentRemain.add(total));
        accountMapper.updateByPrimaryKeySelective(tbAccount);

        //操作tb_card_recharge_log表
        rechargeLog.setChargeOrderId(chargeOrderId);
        rechargeLog.setCardCustomerId(customer.get(0).getId());
        rechargeLog.setTradeNo(DateUtil.formatDate(new Date(), DateUtil.FORMAT_YMDHMS) + RandomStringUtils.randomNumeric(Constant.FOUR_CODE));
        rechargeLog.setTelephone(customer.get(0).getTelephone());
        rechargeLog.setCustomerName(customer.get(0).getCustomerName());
        rechargeLog.setLoginTelephone(chargeCardDto.getLoginTelephone());
        rechargeLog.setRemain(currentRemain.add(total));
        if(chargeCardDto.getActivityId()!=null){
            rechargeLog.setType(0);
        }else{
            rechargeLog.setType(1);
        }
        rechargeLog.setCreatedAt(DateUtil.getCurrentDateString());
        rechargeLog.setUpdatedAt(DateUtil.getCurrentDateString());
        rechargeLogMapper.insertSelective(rechargeLog);
        return true;
    }

    @Transactional
    public boolean allChargeCard(String companyId,String chargeMoney,String payType,String phone) {
        TbCardCustomer tbCardCustomer=new TbCardCustomer();
        tbCardCustomer.setCompanyId(Long.valueOf(companyId));
        List<TbCardCustomer> list = customerMapper.select(tbCardCustomer);
        for(TbCardCustomer cardCustomer:list){
            TbCardRechange tbCardRechange=new TbCardRechange();
            BigDecimal money=new BigDecimal(chargeMoney);
            tbCardRechange.setChargeMoney(money);
            tbCardRechange.setChargeBalance(money);
            tbCardRechange.setCardCustomerId(cardCustomer.getId());
            tbCardRechange.setCompanyId(Long.valueOf(companyId));
            tbCardRechange.setPayType(Integer.parseInt(payType));
            tbCardRechange.setCardType(1);
            //更新账户余额
            TbAccount account = new TbAccount();
            account.setId(cardCustomer.getAccountId());
            TbAccount a=accountMapper.selectOne(account);
            if(a!=null){
                TbAccount tbAccount = new TbAccount();
                tbAccount.setId(cardCustomer.getAccountId());
                tbAccount.setRemain(money.add(a.getRemain()));
                accountMapper.updateByPrimaryKeySelective(tbAccount);
                tbCardRechangeMapper.insertSelective(tbCardRechange);
            }
            //记录账户余额日志
            TbAccountLog tbAccountLog = new TbAccountLog();
            tbAccountLog.setId(ApplicationUtils.randomUUID());
            tbAccountLog.setCreateTime(new Date());
            tbAccountLog.setMoney(money);
            tbAccountLog.setPaymentType(1);
            tbAccountLog.setSource(TbAccountLog.ENTITY_CARD_CHARGE);
            tbAccountLog.setAccountId(a.getId());
            tbAccountLog.setRemain(money.add(a.getRemain()));
            tbAccountLog.setRemark("充值金额");
            accountLogMapper.insertSelective(tbAccountLog);
            //记录充值记录日志
            TbCardRechargeLog rechargeLog = new TbCardRechargeLog();
            rechargeLog.setChargeOrderId(String.valueOf(tbCardRechange.getId()));
            rechargeLog.setCardCustomerId(cardCustomer.getId());
            rechargeLog.setTradeNo(DateUtil.formatDate(new Date(), DateUtil.FORMAT_YMDHMS) + RandomStringUtils.randomNumeric(Constant.FOUR_CODE));
            rechargeLog.setTelephone(tbCardCustomer.getTelephone());
            rechargeLog.setCustomerName(tbCardCustomer.getCustomerName());
            rechargeLog.setLoginTelephone(phone);
            rechargeLog.setRemain(money.add(a.getRemain()));
            rechargeLog.setType(0);
            rechargeLog.setCreatedAt(DateUtil.getCurrentDateString());
            rechargeLog.setUpdatedAt(DateUtil.getCurrentDateString());
            rechargeLogMapper.insertSelective(rechargeLog);
        }
        return true;
    }

    public boolean allOneChargeCard(List<OneChargeCardDto> oneChargeCardDtos,String phone) {
        for(OneChargeCardDto oneChargeCardDto:oneChargeCardDtos){
            TbCardRechange tbCardRechange=new TbCardRechange();
            tbCardRechange.setChargeMoney(oneChargeCardDto.getChargeMoney());
            tbCardRechange.setChargeBalance(oneChargeCardDto.getChargeMoney());
            tbCardRechange.setCardCustomerId(oneChargeCardDto.getCardCustomerId());
            tbCardRechange.setCompanyId(oneChargeCardDto.getCompanyId());
            tbCardRechange.setPayType(oneChargeCardDto.getPayType());
            tbCardRechange.setCardType(1);
            //更新账户余额
            TbCardCustomer cardCustomer=customerMapper.selectByPrimaryKey(Long.valueOf(oneChargeCardDto.getCardCustomerId()));
            TbAccount account = new TbAccount();
            account.setId(cardCustomer.getAccountId());
            TbAccount a=accountMapper.selectOne(account);
            if(a!=null){
                TbAccount tbAccount = new TbAccount();
                tbAccount.setId(cardCustomer.getAccountId());
                tbAccount.setStatus(Byte.valueOf("1"));
                tbAccount.setRemain(oneChargeCardDto.getChargeMoney().add(a.getRemain()));
                accountMapper.updateByPrimaryKey(tbAccount);
                tbCardRechangeMapper.insertSelective(tbCardRechange);
            }
            //记录账户余额日志
            TbAccountLog tbAccountLog = new TbAccountLog();
            tbAccountLog.setId(ApplicationUtils.randomUUID());
            tbAccountLog.setCreateTime(new Date());
            tbAccountLog.setMoney(oneChargeCardDto.getChargeMoney());
            tbAccountLog.setPaymentType(1);
            tbAccountLog.setSource(TbAccountLog.ENTITY_CARD_CHARGE);
            tbAccountLog.setAccountId(a.getId());
            tbAccountLog.setRemain(oneChargeCardDto.getChargeMoney().add(a.getRemain()));
            tbAccountLog.setRemark("充值金额");
            accountLogMapper.insertSelective(tbAccountLog);
            //记录充值记录日志
            TbCardRechargeLog rechargeLog = new TbCardRechargeLog();
            rechargeLog.setChargeOrderId(String.valueOf(String.valueOf(tbCardRechange.getId())));
            rechargeLog.setCardCustomerId(cardCustomer.getId());
            rechargeLog.setTradeNo(DateUtil.formatDate(new Date(), DateUtil.FORMAT_YMDHMS) + RandomStringUtils.randomNumeric(Constant.FOUR_CODE));
            rechargeLog.setTelephone(cardCustomer.getTelephone());
            rechargeLog.setCustomerName(cardCustomer.getCustomerName());
            rechargeLog.setLoginTelephone(phone);
            rechargeLog.setRemain(oneChargeCardDto.getChargeMoney().add(a.getRemain()));
            rechargeLog.setType(0);
            rechargeLog.setCreatedAt(DateUtil.getCurrentDateString());
            rechargeLog.setUpdatedAt(DateUtil.getCurrentDateString());
            rechargeLogMapper.insertSelective(rechargeLog);
        }
        return true;
    }

    public List<PinCardMoneyDto> pinCardMoney(String cardId) {
        List<PinCardMoneyDto> list=tbCardRechangeMapper.pinCardMoney(cardId);
        for(PinCardMoneyDto pinCardMoneyDto:list){
            BigDecimal b=new BigDecimal("10.00");
            int a = pinCardMoneyDto.getCost().compareTo(BigDecimal.ZERO);
            if(a>0){
                pinCardMoneyDto.setBalanceMoney(pinCardMoneyDto.getBalanceMoney().add(b));
            }
        }
        return list;
    }
}
