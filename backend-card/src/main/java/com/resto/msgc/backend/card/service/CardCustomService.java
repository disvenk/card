package com.resto.msgc.backend.card.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.resto.conf.enums.DeleteFlagEnum;
import com.resto.conf.mybatis.base.BaseService;
import com.resto.conf.util.ApplicationUtils;
import com.resto.core.util.DateUtil;
import com.resto.msgc.backend.card.conf.PayConf;
import com.resto.msgc.backend.card.dto.*;
import com.resto.msgc.backend.card.entity.*;
import com.resto.msgc.backend.card.mapper.*;
import com.resto.msgc.backend.card.util.AliPayUtils;
import com.resto.msgc.backend.card.util.Constant;
import com.resto.msgc.backend.card.util.ResultDto;
import com.resto.msgc.backend.card.util.WeChatPayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bruce on 2017/12/8.
 */
@Service
public class CardCustomService extends BaseService<TbCardCustomer, TbCardCustomerMapper> {

    private Logger log = LoggerFactory.getLogger(CardCustomService.class);

    @Autowired
    private TbCardCustomerMapper customerMapper;
    @Autowired
    private TbAccountMapper accountMapper;
    @Autowired
    private TbChargeSettingMapper chargeSettingMapper;
    @Autowired
    private TbCardRechangeMapper tbCardRechangeMapper;
    @Autowired
    private TbCardRechargeLogMapper rechargeLogMapper;
    @Autowired
    private TbCustomerMapper tbCustomerMapper;
    @Autowired
    private TbCardCompanyMapper tbCardCompanyMapper;
    @Autowired
    private TbCardCustomerLogMapper cardCustomerLogMapper;
    @Autowired
    private TbAccountLogMapper accountLogMapper;
    @Autowired
    private TbCardRefundMapper cardRefundMapper;
    @Autowired
    private TbCardCustomerMapper tbCardCustomerMapper;
    @Autowired
    private PayConf payConf;


    public boolean cardIdIsExist(String cardId) {
        List<TbCardCustomer> list = customerMapper.findByCardId(cardId);
        return !list.isEmpty();
    }

    public boolean phoneNumIsExist(String phoneNum) {
        TbCardCustomer tbCardCustomer = new TbCardCustomer();
        tbCardCustomer.setTelephone(phoneNum);
        tbCardCustomer.setDeleteFlag(DeleteFlagEnum.NORMAL);
        return !customerMapper.select(tbCardCustomer).isEmpty();
    }

    @Transactional
    public boolean pinCard(PinCardDto pinCardDto, String loginTelephone,HttpServletRequest request) {
        if (pinCardDto.getCardId() == null) {
            log.error("cardId为空，不能进行销卡操作");
            return false;
        }



        TbCardCustomer findCustomer = new TbCardCustomer();
        findCustomer.setDeleteFlag(DeleteFlagEnum.NORMAL);
        findCustomer.setCardId(pinCardDto.getCardId());
        TbCardCustomer customer = customerMapper.selectOne(findCustomer);
        BigDecimal deleteMoney = BigDecimal.ZERO;
        if (customer != null) {
            TbCardRechange tbCardRechange = new TbCardRechange();
            tbCardRechange.setCardCustomerId(customer.getId());
            List<TbCardRechange> list = tbCardRechangeMapper.select(tbCardRechange);
            for(TbCardRechange cardRechange:list){
                deleteMoney = deleteMoney.add(cardRechange.getChargeBalance());
            }
            TbAccount remainAccount = accountMapper.selectByPrimaryKey(customer.getAccountId());
            TbAccount account = new TbAccount();
            account.setId(customer.getAccountId());
            account.setRemain(remainAccount.getRemain().subtract(deleteMoney));
            accountMapper.updateByPrimaryKeySelective(account);
            //删除充值记录
           // tbCardRechangeMapper.delete(tbCardRechange);
            //更新用户为删除状态
            customer.setDeleteFlag(DeleteFlagEnum.DELETED);
            int i = customerMapper.updateByPrimaryKeySelective(customer);
            log.info("受影响的行数："+i);
            //记录日志
            TbCardCustomerLog cardCustomerLog = new TbCardCustomerLog();
            cardCustomerLog.setCardCustomerId(customer.getId());
            cardCustomerLog.setCardType(customer.getType());
            cardCustomerLog.setCustomerName(customer.getCustomerName());
            cardCustomerLog.setLoginTelephone(loginTelephone);
            cardCustomerLog.setOperateType(true);
            cardCustomerLog.setTelephone(customer.getTelephone());
            cardCustomerLog.setIdCard(customer.getIdCard());
            cardCustomerLog.setCreatedAt(DateUtil.getCurrentDateString());
            cardCustomerLog.setUpdatedAt(DateUtil.getCurrentDateString());
            cardCustomerLogMapper.insertSelective(cardCustomerLog);
            TbCardRefund cardRefund=new TbCardRefund();
            cardRefund.setCardCustomerId(customer.getId());
            cardRefund.setCompanyId(customer.getCompanyId());
            cardRefund.setWechatMoney(pinCardDto.getWechatMoney());
            cardRefund.setAliMoney(pinCardDto.getAliMoney());
            cardRefund.setCashMoney(pinCardDto.getCashMoney());
            cardRefund.setChequeMoney(pinCardDto.getChequeMoney());
            cardRefund.setStarMoney(pinCardDto.getStarMoney());
            cardRefund.setCardType(pinCardDto.getCardType());
            cardRefundMapper.insertSelective(cardRefund);
            for(TbCardRechange cardRechange:list){
                if(cardRechange.getPayType()==1){//微信支付退款
                    TbCardRechargeLog l=new TbCardRechargeLog();
                    l.setChargeOrderId(cardRechange.getId().toString());
                    TbCardRechargeLog cardRechargeLog=rechargeLogMapper.selectOne(l);
                    //微信退款
                    JSONObject myJson = JSON.parseObject(cardRechargeLog.getPayData());
                    //得到本地服务器的退款证书文件
                    String payCertPath = request.getSession().getServletContext().getRealPath("payCert/FoodMember.p12");
                    //int total_fee= cardRechange.getChargeMoney().multiply(new BigDecimal(100)).intValue();
                    int refund_fee=(cardRechange.getChargeBalance().add(cardRechange.getCost())).multiply(new BigDecimal(100)).intValue();
                    WeChatPayUtils.refundNew(myJson.get("out_trade_no").toString(),myJson.get("transaction_id").toString(),Integer.parseInt(myJson.get("total_fee").toString()),refund_fee,payConf.WXPAY_APPID,myJson.get("mch_id").toString(),myJson.get("sub_mch_id").toString(),payConf.WXPAY_MCHKEY,payCertPath);
                }else if(cardRechange.getPayType()==2){//支付宝支付
                    TbCardRechargeLog l=new TbCardRechargeLog();
                    l.setChargeOrderId(cardRechange.getId().toString());
                    TbCardRechargeLog cardRechargeLog=rechargeLogMapper.selectOne(l);
                    //支付宝退款
                    JSONObject myJson = JSON.parseObject(cardRechargeLog.getPayData());
                    //todo 由于是本地服务, 所以此处的支付宝支付相关配置信息写死
                    AliPayUtils.connection(payConf.ALIPAY_TRADE_APP_ID,payConf.ALIPAY_TRADE_PRIVATE_KEY,payConf.ALIPAY_TRADE_PUBLIC_KEY,AliPayUtils.ALI_ENCRYPT);
                    Map map = new HashMap();
                    map.put("out_trade_no", myJson.get("out_trade_no"));
                    map.put("trade_no", myJson.get("trade_no"));
                    map.put("refund_amount",cardRechange.getChargeBalance().add(cardRechange.getCost()));
                    map.put("refund_reason", "销卡退款");
                    AliPayUtils.refundPay(map);
                }
            }
            return true;
        }
        return false;
    }

    public  boolean pinCard(HttpServletRequest request) {
        String payCertPath = request.getSession().getServletContext().getRealPath("payCert/FoodMember.p12");
        WeChatPayUtils.refundNew("520e71581e7d4eacaea73a1f8824a144",
               "4200000175201809042566658883",
                Integer.parseInt("1100"),
                1100,"wx36bd5b9b7d264a8c",
                "1391463202",
                "1420007502",
                "E62ADDBD1A096932FA77DBAA75AF1731",
                payCertPath);
        return true;
    }

    public List<TbCardCustomer> selectByPageNumSize(TbCardCustomer tbCardCustomer, Integer pageNum, Integer pageSize) {
        List<TbCardCustomer> list=customerMapper.selectByPageNumSize(tbCardCustomer, pageNum, pageSize);
        for(TbCardCustomer cardCustomer:list){
            if(cardCustomer.getCompanyId()!=null){
                TbCardCompany tbCardCompany = tbCardCompanyMapper.selectByPrimaryKey(cardCustomer.getCompanyId());
                cardCustomer.setCompanyName(tbCardCompany.getCompanyName());
            }else{
                cardCustomer.setCompanyName("---");
            }
        }
        return list;
    }

    public int searchSelectCount(TbCardCustomer tbCardCustomer) {
        return customerMapper.searchSelectCount(tbCardCustomer);
    }

    @Transactional
    public boolean openCard(OpenCardDto openCardDto) {
        String accountId;
        //判断手机号是否已经开过卡
        TbCardCustomer tbCardCustomer=new TbCardCustomer();
        tbCardCustomer.setDeleteFlag(DeleteFlagEnum.NORMAL);
        tbCardCustomer.setTelephone(openCardDto.getTelephone());
        List<TbCardCustomer> list=customerMapper.select(tbCardCustomer);
        if(list!=null && !list.isEmpty()){
            return false;
        }
        //判断该手机号在微信有没有注册
        TbCustomer tbCustomer = new TbCustomer();
        tbCustomer.setTelephone(openCardDto.getTelephone());
        List<TbCustomer> customerList = tbCustomerMapper.select(tbCustomer);
        if (!customerList.isEmpty()) {
            accountId = customerList.get(0).getAccountId();
            if(accountId!=null){
                TbAccount remainAccount = accountMapper.selectByPrimaryKey(accountId);
                if(remainAccount==null){
                    TbAccount tbAccount = new TbAccount();
                    tbAccount.setId(accountId);
                    tbAccount.setStatus(Byte.valueOf("1"));
                    tbAccount.setFrozenRemain(new BigDecimal(0));
                    tbAccount.setRemain(new BigDecimal(0));
                    accountMapper.insertSelective(tbAccount);
                }
            }
        } else {
            accountId = ApplicationUtils.randomUUID();
            TbAccount tbAccount = new TbAccount();
            tbAccount.setId(accountId);
            tbAccount.setStatus(Byte.valueOf("1"));
            tbAccount.setFrozenRemain(new BigDecimal(0));
            tbAccount.setRemain(new BigDecimal(0));
            accountMapper.insertSelective(tbAccount);
        }
        TbCardCustomer customer = new TbCardCustomer();
        customer.setAccountId(accountId);
        customer.setCardId(openCardDto.getCardId());
        customer.setCompanyId(openCardDto.getCompanyId());
        customer.setCustomerName(openCardDto.getCustomerName());
        customer.setDiscountId(openCardDto.getDiscountId());
        customer.setTelephone(openCardDto.getTelephone());
        customer.setType(openCardDto.getCardType());
        customer.setIdCard(openCardDto.getIdCard());
        customer.setCreatedAt(DateUtil.getCurrentDateString());
        customer.setUpdatedAt(DateUtil.getCurrentDateString());
        customer.setCardState(1);
        customerMapper.insertSelective(customer);
        return true;
    }

    public CardDto getCardInfo(String cardId) {
        return customerMapper.findCardInfoByCardId(cardId);
    }

    public CardDto pinCardInfo(String cardId) {
        //BigDecimal returnMoney = BigDecimal.ZERO;
        BigDecimal notMoney = BigDecimal.ZERO;
        CardDto cardDto = customerMapper.findCardInfoByCardId(cardId);
        if (cardDto != null) {
            TbCardRechange t=new TbCardRechange();
            t.setCardCustomerId(cardDto.getCardCustomerId());
            List<TbCardRechange> list=tbCardRechangeMapper.select(t);
            for(TbCardRechange item:list){
               /* if(item.getChargeSettingId()==null){//普通
                    //returnMoney = returnMoney.add(item.getChargeBalance());
                }else{//活动*/
                    notMoney = notMoney.add(item.getRewardBalance());
                /*}*/
            }
            cardDto.setNotReturnAmount(notMoney);
            cardDto.setReturnAmount(cardDto.getRemain().subtract(notMoney));
        }
        return cardDto;
    }

    public List<TbChargeSetting> findActivityList() {
        TbChargeSetting setting = new TbChargeSetting();
        setting.setState(Byte.valueOf("1"));
        List<TbChargeSetting> list = chargeSettingMapper.select(setting);
        TbChargeSetting chargeSetting = new TbChargeSetting();
        chargeSetting.setId(null);
        chargeSetting.setLabelText("无");
        list.add(0, chargeSetting);
        return list;
    }

    public void updateByCardPP(TbCardCustomer tbCardCustomer){
        tbCardCustomer.setUpdatedAt(DateUtil.getCurrentDateString());
        customerMapper.updateByCardPP(tbCardCustomer);
    }

    @Transactional
    public boolean addCardRecharge(CardRechangeDto cardRechangeDto,String phone) {
        BigDecimal chargeMoney=new BigDecimal(0);
        if(cardRechangeDto.getChargeMoney()!=null){
            chargeMoney=cardRechangeDto.getChargeMoney();
        }
        BigDecimal rewardMoney=new BigDecimal(0);
        if(cardRechangeDto.getRewardMoney()!=null){
            rewardMoney=cardRechangeDto.getRewardMoney();
        }
        TbCardCustomer cardCustomer=new TbCardCustomer();
        cardCustomer.setCardId(cardRechangeDto.getCardId());
        cardCustomer.setDeleteFlag(DeleteFlagEnum.NORMAL);
        TbCardCustomer t=customerMapper.selectOne(cardCustomer);
        //更新账户余额
        TbAccount remainAccount = accountMapper.selectByPrimaryKey(t.getAccountId());
        TbAccount account = new TbAccount();
        account.setId(t.getAccountId());
        account.setRemain(remainAccount.getRemain().add(chargeMoney.add(rewardMoney)));
        accountMapper.updateByPrimaryKeySelective(account);
        //插入充值记录
        TbCardRechange cardRechange=new TbCardRechange();
        cardRechange.setCardCustomerId(t.getId());
        cardRechange.setChargeMoney(cardRechangeDto.getChargeMoney());
        cardRechange.setChargeBalance(cardRechangeDto.getChargeBalance());
        cardRechange.setRewardMoney(cardRechangeDto.getRewardMoney());
        cardRechange.setRewardBalance(cardRechangeDto.getRewardBalance());
        cardRechange.setChargeSettingId(cardRechangeDto.getChargeSettingId());
        cardRechange.setCompanyId(cardRechangeDto.getCompanyId());
        cardRechange.setPayType(cardRechangeDto.getPayType());
        cardRechange.setCardType(cardRechangeDto.getCardType());
        cardRechange.setCost(cardRechangeDto.getCost());
        tbCardRechangeMapper.insertSelective(cardRechange);
        //记录账户余额日志
        TbAccountLog tbAccountLog = new TbAccountLog();
        tbAccountLog.setId(ApplicationUtils.randomUUID());
        tbAccountLog.setCreateTime(new Date());
        tbAccountLog.setMoney(chargeMoney.add(rewardMoney));
        tbAccountLog.setPaymentType(1);
        tbAccountLog.setSource(TbAccountLog.ENTITY_CARD_CHARGE);
        tbAccountLog.setAccountId(t.getAccountId());
        tbAccountLog.setRemain(remainAccount.getRemain().add(chargeMoney.add(rewardMoney)));
        tbAccountLog.setRemark("充值金额");
        accountLogMapper.insertSelective(tbAccountLog);
        //记录充值记录日志
        TbCardRechargeLog rechargeLog = new TbCardRechargeLog();
        rechargeLog.setChargeOrderId(String.valueOf(cardRechange.getId()));
        rechargeLog.setCardCustomerId(t.getId());
        rechargeLog.setTradeNo(DateUtil.formatDate(new Date(), DateUtil.FORMAT_YMDHMS) + RandomStringUtils.randomNumeric(Constant.FOUR_CODE));
        rechargeLog.setTelephone(t.getTelephone());
        rechargeLog.setCustomerName(t.getCustomerName());
        rechargeLog.setLoginTelephone(phone);
        rechargeLog.setPayData(cardRechangeDto.getPayData());
        rechargeLog.setRemain(remainAccount.getRemain().add(chargeMoney.add(rewardMoney)));
        if(cardRechangeDto.getChargeSettingId()!=null){
            rechargeLog.setType(1);
        }else{
            rechargeLog.setType(0);
        }
        rechargeLog.setCreatedAt(DateUtil.getCurrentDateString());
        rechargeLog.setUpdatedAt(DateUtil.getCurrentDateString());
        rechargeLogMapper.insertSelective(rechargeLog);
        return true;
    }

    @Transactional
    public ResultDto modifyCard(TbCardCustomer tbCardCustomer) {
        TbCardCustomer t=customerMapper.selectByPrimaryKey(tbCardCustomer.getId());
        if(t!=null){
            if(t.getCardId().equals(tbCardCustomer.getCardId())){
                customerMapper.updateByPrimaryKeySelective(tbCardCustomer);
            }else{
                TbCardCustomer cardCustomer = new TbCardCustomer();
                cardCustomer.setCardId(tbCardCustomer.getCardId());
                cardCustomer.setDeleteFlag(DeleteFlagEnum.NORMAL);
                List<TbCardCustomer> list=customerMapper.select(cardCustomer);
                if(list!=null && !list.isEmpty()){
                    return ResultDto.getError("该卡号已存在");
                }else{
                    customerMapper.updateByPrimaryKeySelective(tbCardCustomer);
                }
            }
        }
        return ResultDto.getSuccess();
    }

    /**
    *@Description:通过卡号查找用户
    *@Author:disvenk.dai
    *@Date:17:00 2018/4/23 0023
    */
    public TbCardCustomer selectByCardId(String cardId){
        return tbCardCustomerMapper.selectByCardId(cardId);
    }

    /**
    *@Description:通过账户查找
    *@Author:disvenk.dai
    *@Date:17:17 2018/4/23 0023
    */
    public TbCardCustomer selectByAccountId(String accountId){
          return   tbCardCustomerMapper.selectByAccountId(accountId);
    }

    /**
     *导入会员卡，开卡并充值
     * @param list
     */
    @Transactional(rollbackFor = Exception.class)
    public void excelOpenCard(List<ExcelCardDto> list,String phone) {
        for(ExcelCardDto excelCardDto:list){
            BigDecimal chargeMoney=new BigDecimal(0);
            if(excelCardDto.getChargeMoney()!=null){
                chargeMoney=new BigDecimal(excelCardDto.getChargeMoney());
            }
            BigDecimal rewardMoney=new BigDecimal(0);
            if(excelCardDto.getRewardMoney()!=null){
                rewardMoney=new BigDecimal(excelCardDto.getRewardMoney());
            }
            //创建账户
            TbAccount tbAccount = new TbAccount();
            tbAccount.setId(ApplicationUtils.randomUUID());
            tbAccount.setStatus(Byte.valueOf("1"));
            tbAccount.setFrozenRemain(new BigDecimal(0));
            tbAccount.setRemain(chargeMoney.add(rewardMoney));
            accountMapper.insertSelective(tbAccount);
            //创建会员卡信息
            TbCardCustomer customer = new TbCardCustomer();
            customer.setAccountId(tbAccount.getId());
            customer.setStarCardId(excelCardDto.getCardId());
            if(excelCardDto.getCompanyId()!=null){
                customer.setCompanyId(Long.valueOf(excelCardDto.getCompanyId()));
            }
            customer.setCustomerName(excelCardDto.getCustomerName());
            if(excelCardDto.getDiscount().equals("1")){
                customer.setDiscountId(Long.valueOf(2));
            }
            customer.setTelephone(excelCardDto.getTelephone());
            customer.setType(Integer.parseInt(excelCardDto.getCardType()));
            customer.setIdCard("");
            customer.setCreatedAt(DateUtil.getCurrentDateString());
            customer.setUpdatedAt(DateUtil.getCurrentDateString());
            customer.setCardState(2);
            customerMapper.insertSelective(customer);

            //插入充值记录
            TbCardRechange cardRechange=new TbCardRechange();
            cardRechange.setCardCustomerId(customer.getId());
            cardRechange.setChargeMoney(chargeMoney);
            cardRechange.setChargeBalance(chargeMoney);
            cardRechange.setRewardMoney(rewardMoney);
            cardRechange.setRewardBalance(rewardMoney);
            if(excelCardDto.getCompanyId()!=null){
                cardRechange.setCompanyId(Long.valueOf(excelCardDto.getCompanyId()));
            }
            cardRechange.setPayType(5);
            cardRechange.setCardType(Integer.parseInt(excelCardDto.getCardType()));
            cardRechange.setCost(new BigDecimal(10));
            tbCardRechangeMapper.insertSelective(cardRechange);
            //记录账户余额日志
            TbAccountLog tbAccountLog = new TbAccountLog();
            tbAccountLog.setId(ApplicationUtils.randomUUID());
            tbAccountLog.setCreateTime(new Date());
            tbAccountLog.setMoney(chargeMoney.add(rewardMoney));
            tbAccountLog.setPaymentType(1);
            tbAccountLog.setSource(TbAccountLog.START_CARD_CHARGE);
            tbAccountLog.setAccountId(tbAccount.getId());
            tbAccountLog.setRemain(chargeMoney.add(rewardMoney));
            tbAccountLog.setRemark("充值金额");
            accountLogMapper.insertSelective(tbAccountLog);
            //记录充值记录日志
            TbCardRechargeLog rechargeLog = new TbCardRechargeLog();
            rechargeLog.setChargeOrderId(String.valueOf(cardRechange.getId()));
            rechargeLog.setCardCustomerId(customer.getId());
            rechargeLog.setTradeNo(DateUtil.formatDate(new Date(), DateUtil.FORMAT_YMDHMS) + RandomStringUtils.randomNumeric(Constant.FOUR_CODE));
            rechargeLog.setTelephone(excelCardDto.getTelephone());
            rechargeLog.setCustomerName(excelCardDto.getCustomerName());
            rechargeLog.setLoginTelephone(phone);
            rechargeLog.setRemain(chargeMoney.add(rewardMoney));
            rechargeLog.setType(0);
            rechargeLog.setCreatedAt(DateUtil.getCurrentDateString());
            rechargeLog.setUpdatedAt(DateUtil.getCurrentDateString());
            rechargeLogMapper.insertSelective(rechargeLog);
        }
    }

    public ResultDto activatedCardInfo(String cardId) {
        if(cardId==null){
           return ResultDto.getError("卡号不能为空");
        }
        ActivatedDto activatedDto =  customerMapper.activatedCardInfo(cardId);
        if(activatedDto==null){
            return ResultDto.getError("该卡信息不存在");
        }
        return ResultDto.getSuccess(activatedDto);
    }

    @Transactional
    public ResultDto activatedCard(ActivatedCardDto activatedCardDto) {
        if(activatedCardDto!=null){
            ResultDto.getError("激活失败");
        }
        if(activatedCardDto.getCardCustomerId()==null){
            ResultDto.getError("会员卡id不能为空");
        }
        if(activatedCardDto.getCardId()==null){
            ResultDto.getError("卡号不能为空");
        }
        TbCardCustomer tbCardCustomer = tbCardCustomerMapper.selectByPrimaryKey(activatedCardDto.getCardCustomerId());
        //更新卡号
        tbCardCustomer.setCardId(activatedCardDto.getCardId());
        tbCardCustomer.setCardState(1);
        tbCardCustomerMapper.updateByPrimaryKeySelective(tbCardCustomer);
        //更新用户账户余额
        TbAccount account=new TbAccount();
        account.setId(tbCardCustomer.getAccountId());
        account.setRemain(activatedCardDto.getRemain());
        accountMapper.updateByPrimaryKeySelective(account);
        //更新用户账户日志
        TbAccountLog accountLog=new TbAccountLog();
        accountLog.setAccountId(tbCardCustomer.getAccountId());
        accountLog.setSource(17);
        TbAccountLog a=accountLogMapper.selectOne(accountLog);
        if(a!=null){
            a.setMoney(activatedCardDto.getRemain());
            a.setRemain(activatedCardDto.getRemain());
            accountLogMapper.updateByPrimaryKeySelective(a);
        }
        //更新充值记录
        TbCardRechange cardRechange = new TbCardRechange();
        cardRechange.setCardCustomerId(activatedCardDto.getCardCustomerId());
        cardRechange.setPayType(5);
        TbCardRechange c=tbCardRechangeMapper.selectOne(cardRechange);
        if(c!=null){
            c.setChargeMoney(activatedCardDto.getChargeMoney());
            c.setChargeBalance(activatedCardDto.getChargeMoney());
            c.setRewardMoney(activatedCardDto.getRewardMoney());
            c.setRewardBalance(activatedCardDto.getRewardMoney());
            tbCardRechangeMapper.updateByPrimaryKeySelective(c);
        }
        //更新充值日志
        TbCardRechargeLog cardRechargeLog=new TbCardRechargeLog();
        cardRechargeLog.setChargeOrderId(String.valueOf(c.getId()));
        TbCardRechargeLog r=rechargeLogMapper.selectOne(cardRechargeLog);
        if(r!=null){
            r.setRemain(activatedCardDto.getRemain());
            rechargeLogMapper.updateByPrimaryKeySelective(r);
        }
        return ResultDto.getSuccess();
    }
}
