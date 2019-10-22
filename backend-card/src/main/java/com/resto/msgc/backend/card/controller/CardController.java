package com.resto.msgc.backend.card.controller;

import com.resto.conf.redis.RedisService;
import com.resto.msgc.backend.card.dto.*;
import com.resto.msgc.backend.card.service.CardChargeService;
import com.resto.msgc.backend.card.service.CardCustomService;
import com.resto.msgc.backend.card.util.Constant;
import com.resto.msgc.backend.card.util.ResultDto;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by bruce on 2017/12/8.
 */
@Api
@Controller
@RequestMapping("card")
public class CardController extends CommonController {

    @Autowired
    private CardCustomService cardCustomService;
    @Autowired
    private CardChargeService chargeService;
    @Autowired
    private RedisService redisService;

    @RequestMapping("view")
    public String index() {
        return "card/list";
    }

    @RequestMapping("create")
    public String create() {
        return "card/add";
    }

    @RequestMapping("pin")
    public String pin() {
        return "card/pinCard";
    }

    @RequestMapping("recharge")
    public String recharge() {
        return "card/recharge";
    }

    @RequestMapping("batch")
    public String batch() {
        return "card/batchCharge";
    }

    @RequestMapping("loss")
    public String loss() {
        return "card/lossCard";
    }

    @RequestMapping("fill")
    public String fill() {
        return "card/fillCard";
    }

    @RequestMapping("activated")
    public String activated() {
        return "card/activatedCard";
    }

    /**
     * 开卡
     * @param openCardDto
     * @return
     */
    @PostMapping("addCard")
    @ResponseBody
    public ResultDto addCard(@RequestBody OpenCardDto openCardDto) {
        if (cardCustomService.cardIdIsExist(openCardDto.getCardId())) {
            return ResultDto.getError("该卡号已经存在");
        }
        if (StringUtils.isNotBlank(openCardDto.getTelephone()) && StringUtils.isNotBlank(openCardDto.getCode())) {
            if(openCardDto.getCardType().equals(0)){
                String code = redisService.get(Constant.CODE_OPENCARD + openCardDto.getTelephone());
                if (!openCardDto.getCode().equals(code)) {
                    return ResultDto.getError("验证码输入不正确");
                }
            }
        }
        openCardDto.setLoginTelephone(this.getSessionCurrentUser());
        if (!cardCustomService.openCard(openCardDto)){
            return ResultDto.getError("开卡失败");
        }
        return ResultDto.getSuccess();
    }

    /**
     * 单卡充值
     * @param cardRechangeDto
     * @return
     */
    @PostMapping("addCardRecharge")
    @ResponseBody
    public ResultDto addCardRecharge(@RequestBody CardRechangeDto cardRechangeDto) {
        if (!cardCustomService.addCardRecharge(cardRechangeDto,getSessionCurrentUser())){
            return ResultDto.getError("充值失败");
        }
        return ResultDto.getSuccess();
    }

    /**
     * 销卡
     * @param pinCardDto
     * @param request
     * @return
     */
    @RequestMapping("pinCard")
    @ResponseBody
    public ResultDto pinCard(PinCardDto pinCardDto,HttpServletRequest request) {
        if (cardCustomService.pinCard(pinCardDto, getSessionCurrentUser(),request)) {
            return ResultDto.getSuccess();
        }
        return ResultDto.getError("销卡失败");
    }

    /**
     * 销卡,对应退的钱的各项
     * @param cardId
     * @return
     */
    @RequestMapping("pinCardMoney")
    @ResponseBody
    public ResultDto pinCardMoney(String cardId) {
        return ResultDto.getSuccess(chargeService.pinCardMoney(cardId));
    }

    @PostMapping("chargeCard")
    @ResponseBody
    public ResultDto chargeCard(@RequestBody ChargeCardDto chargeCardDto) {
        chargeCardDto.setLoginTelephone(this.getSessionCurrentUser());
        if (!chargeService.chargeCard(chargeCardDto)){
            return ResultDto.getError("充值失败");
        }
        return ResultDto.getSuccess() ;
    }

    /**
     * 批量充值
     * @param companyId
     * @param chargeMoney
     * @param payType
     * @return
     */
    @PostMapping("allChargeCard")
    @ResponseBody
    public ResultDto allChargeCard(String companyId,String chargeMoney,String payType) {
        if (!chargeService.allChargeCard(companyId,chargeMoney,payType,getSessionCurrentUser())){
            return ResultDto.getError("充值失败");
        }
        return ResultDto.getSuccess() ;
    }

    /**
     * 勾选充值
     * @param oneChargeCardDtos
     * @return
     */
    @PostMapping("allOneChargeCard")
    @ResponseBody
    public ResultDto allOneChargeCard(@RequestBody List<OneChargeCardDto> oneChargeCardDtos) {
        if (!chargeService.allOneChargeCard(oneChargeCardDtos,getSessionCurrentUser())){
            return ResultDto.getError("充值失败");
        }
        return ResultDto.getSuccess() ;
    }

    /**
     * 获取卡信息
     * @param cardId
     * @return
     */
    @GetMapping("{cardId}")
    @ResponseBody
    public ResultDto getCardInfo(@PathVariable("cardId") String cardId) {
        return ResultDto.getSuccess(cardCustomService.getCardInfo(cardId));
    }

    /**
     * 通过卡号查询销卡信息
     * @param cardId
     * @return
     */
    @GetMapping("pinCardInfo/{cardId}")
    @ResponseBody
    public ResultDto pinCardInfo(@PathVariable("cardId") String cardId) {
        return ResultDto.getSuccess(cardCustomService.pinCardInfo(cardId));
    }

    /**
     * 通过卡号查询需要激活卡信息
     * @param cardId
     * @return
     */
    @GetMapping("activatedCardInfo/{cardId}")
    @ResponseBody
    public ResultDto activatedCardInfo(@PathVariable("cardId") String cardId) {
        return cardCustomService.activatedCardInfo(cardId);
    }

    /**
     * 激活天子星卡
     * @param activatedCardDto
     * @return
     */
    @PostMapping("activatedCard")
    @ResponseBody
    public ResultDto activatedCard(@RequestBody ActivatedCardDto activatedCardDto) {
        if(activatedCardDto.getCardId()==""){
            return ResultDto.getError("请读取新卡号");
        }
        if (cardCustomService.cardIdIsExist(activatedCardDto.getCardId())) {
            return ResultDto.getError("该卡号已经存在");
        }
        return cardCustomService.activatedCard(activatedCardDto);
    }

    /**
     * 查询充值活动
     * @return
     */
    @GetMapping("activity/list")
    @ResponseBody
    public ResultDto activityInfo() {
        return ResultDto.getSuccess(cardCustomService.findActivityList());
    }

}
