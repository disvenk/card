package com.resto.msgc.backend.card.controller;

import com.alibaba.fastjson.JSON;
import com.resto.conf.enums.DeleteFlagEnum;
import com.resto.msgc.backend.card.dto.CardDto;
import com.resto.msgc.backend.card.dto.PurchaseHistoryDto;
import com.resto.msgc.backend.card.entity.*;
import com.resto.msgc.backend.card.mapper.TbCardDiscountMapper;
import com.resto.msgc.backend.card.service.*;
import com.resto.msgc.backend.card.util.DataTablesOutput;
import com.resto.msgc.backend.card.util.JSONResult;
import com.resto.msgc.backend.card.util.Result;
import com.resto.msgc.backend.card.util.ResultDto;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by xielc on 2017/12/8.
 */
@Api
@Controller
@RequestMapping("cardCustomer")
public class CardCustomerController {

    protected Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private CardCustomService cardCustomService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CardCompanyService cardCompanyService;

    @Autowired
    private AccountLogService accountLogService;

    @Autowired
    private CardDiscountService cardDiscountService;

    @RequestMapping("employeeView")
    public String employeeView() {
        return "cardCustomer/employeeList";
    }

    @RequestMapping("normalView")
    public String normalView() {
        return "cardCustomer/normalList";
    }

    @RequestMapping("temporaryView")
    public String temporaryView() {
        return "cardCustomer/temporaryList";
    }

    @RequestMapping("/datas")
    @ResponseBody
    public DataTablesOutput<TbCardCustomer> selectMoneyAndNumByDate(String beginDate, String endDate,String telephone,Integer type){
        HttpServletRequest hreq = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        int draw = Integer.parseInt(hreq.getParameter("draw"));
        int start = Integer.parseInt(hreq.getParameter("start"));
        int length = Integer.parseInt(hreq.getParameter("length"));
        TbCardCustomer tbCardCustomer = new TbCardCustomer();
        tbCardCustomer.setCreatedAt(beginDate);
        tbCardCustomer.setUpdatedAt(endDate);
        tbCardCustomer.setType(type);
        if(type==2){
            if(telephone!=null){
                tbCardCustomer.setCardId(telephone);
            }
        }else{
            if(telephone!=null){
                tbCardCustomer.setTelephone(telephone);
            }
        }
        List<TbCardCustomer> tbCardCustomerList=null;
        tbCardCustomerList = cardCustomService.selectByPageNumSize(tbCardCustomer,start,length);
        DataTablesOutput<TbCardCustomer> dtable=new DataTablesOutput<TbCardCustomer>();
        dtable.setDraw(draw);
        dtable.setRecordsTotal(Long.valueOf(cardCustomService.searchSelectCount(tbCardCustomer)));
        dtable.setRecordsFiltered(Long.valueOf(cardCustomService.searchSelectCount(tbCardCustomer)));
        dtable.setData(tbCardCustomerList);
        return dtable;
    }

    @RequestMapping("cardRechargeLog")
    public String showModal(String cardId,String accountId,Model model){
        CardDto cardDto = cardCustomService.getCardInfo(cardId);
        List<TbAccountLog> tbAccountLogList = accountLogService.selectaccountId(accountId);
        model.addAttribute("cardDto",cardDto);
        model.addAttribute("tbAccountLogList", JSON.toJSONString(tbAccountLogList));
        return "cardRechargeLog/list";
    }


    @RequestMapping("accountLogView")
    public String accountLogViewindex() {
        return "accountLog/list";
    }

    @RequestMapping("accountLog")
    public String showModal(String cardId,Model model){
        CardDto cardDto = cardCustomService.getCardInfo(cardId);
        List<PurchaseHistoryDto> purchaseHistoryDto = accountLogService.selectCardId(cardId);
        model.addAttribute("cardDto",cardDto);
        model.addAttribute("purchaseHistoryDto", JSON.toJSONString(purchaseHistoryDto));
        return "accountLog/detail";
    }

    @RequestMapping("accountLogp")
    public String showModalp(String cardId,Model model){
        CardDto cardDto = cardCustomService.getCardInfo(cardId);
        List<PurchaseHistoryDto> purchaseHistoryDto = accountLogService.selectCardId(cardId);
        model.addAttribute("cardDto",cardDto);
        model.addAttribute("purchaseHistoryDto", JSON.toJSONString(purchaseHistoryDto));
        return "accountLog/detail_p";
    }

    @RequestMapping("modifyDiscount")
    @ResponseBody
    public ResultDto modify(@Valid TbCardCustomer tbCardCustomer) {
        if(tbCardCustomer.getType()==0){
            tbCardCustomer.setCompanyId(null);
            tbCardCustomer.setIdCard(null);
            cardCustomService.updateByCardPP(tbCardCustomer);
            return ResultDto.getSuccess();
        }else{
            if(tbCardCustomer.getIdCard()==null){
                return ResultDto.getError("身份证号不能为空");
            }
            if(tbCardCustomer.getIdCard().length()!=16&&tbCardCustomer.getIdCard().length()!=18){
                return ResultDto.getError("身份证号格式不对");
            }
            cardCustomService.updateByCardPP(tbCardCustomer);
            return ResultDto.getSuccess();
        }
    }

    @RequestMapping("byTelephone")
    @ResponseBody
    public ResultDto selectByTelephone(String telephone) {
        if(telephone==""||telephone==null){
           return ResultDto.getError("手机号不能为空");
        }
        TbCardCustomer tbCardCustomer=new TbCardCustomer();
        tbCardCustomer.setTelephone(telephone);
        tbCardCustomer.setDeleteFlag(DeleteFlagEnum.NORMAL);
        TbCardCustomer t=cardCustomService.dbSelectOne(tbCardCustomer);
        if(t==null){
            return ResultDto.getError("该卡用户不存在");
        }
        //查账户余额
        TbAccount tbAccount=new TbAccount();
        tbAccount.setId(t.getAccountId());
        TbAccount account=accountService.dbSelectOne(tbAccount);
        t.setRemain(account.getRemain());
        //查公司名称
        System.out.println(t.getCompanyId());
        if(t.getCompanyId()!=null){
            TbCardCompany cardCompany=cardCompanyService.dbSelectByPrimaryKey(t.getCompanyId());
            t.setCompanyName(cardCompany.getCompanyName());
        }
        //查折扣名称
        if(t.getDiscountId()!=null){
            TbCardDiscount cardDiscount = cardDiscountService.dbSelectByPrimaryKey(t.getDiscountId());
            t.setDiscountName(cardDiscount.getDiscountName());
        }
        return ResultDto.getSuccess(t);
    }

    @RequestMapping("modifyCard")
    @ResponseBody
    public ResultDto modifyCard(@Valid TbCardCustomer tbCardCustomer) {
        return cardCustomService.modifyCard(tbCardCustomer);
    }

    public Result getSuccessResult(Object data){
        if(data==null){
            return new Result(true);
        }
        JSONResult<Object> result = new JSONResult<Object>(data);
        return result;
    }
}
