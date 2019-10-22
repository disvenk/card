package com.resto.msgc.backend.card.service;

import com.alibaba.fastjson.JSONObject;
import com.resto.conf.redis.RedisService;
import com.resto.conf.util.SMSUtils;
import com.resto.msgc.backend.card.controller.LoginController;
import com.resto.msgc.backend.card.entity.TbCardLoginUser;
import com.resto.msgc.backend.card.mapper.TbCardLoginUserMapper;
import com.resto.msgc.backend.card.util.Constant;
import com.resto.msgc.backend.card.util.Result;
import com.resto.msgc.backend.card.util.ResultDto;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by disvenk.dai on 2018-04-20 09:51
 */

@Service
public class NewLoginService {

    private Logger log = LoggerFactory.getLogger(NewLoginService.class);

    @Autowired
    private TbCardLoginUserMapper loginUserMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private CardCustomService customService;

    /**
     * 校验手机号
     *
     * @param telephone
     * @return
     */
    private ResultDto detectPhoneNum(String telephone) {
        try {
            if (StringUtils.isBlank(telephone) && telephone!=null) {
                throw new IllegalArgumentException("手机号不能为空");
            }
            Pattern p = Pattern.compile(Constant.PHONE_RG);
            if (!p.matcher(telephone).matches()) {
                throw new IllegalArgumentException("手机号码不合法");
            }
            return ResultDto.getSuccess();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultDto.getError(e.getMessage());
        }
    }

    /**
     * 校验手机号是否存在
     *
     * @param telephone
     * @return
     */
    private ResultDto detectPhoneNumIsExist(String telephone) {
        try {
            ResultDto resultDto = detectPhoneNum(telephone);
            if (resultDto.isSuccess()) {

                TbCardLoginUser loginUser = loginUserMapper.findUserByTelephone(telephone);
                if (loginUser == null) {
                    throw new IllegalArgumentException("手机号码不存在");
                } else {
                    TbCardLoginUser tbCardLoginUser = loginUserMapper.selectUserByFlag();

                    //是否有其他用户正在值班
                    if(tbCardLoginUser!=null){
                        if(!tbCardLoginUser.getTelephone().equals(telephone)){
                            throw new IllegalArgumentException("有其他用户正在值班");
                        }
                    }

                    //今天上班第一次登录
                    if(loginUser.getFlag()==1){
                        loginUser.setFlag(0);
                        loginUser.setLoginFirst(new Date());
                        loginUserMapper.updateByPrimaryKeySelective(loginUser);
                    }

                    return ResultDto.getSuccess();
                }
            } else {
                throw new IllegalArgumentException(resultDto.getMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultDto.getError(e.getMessage());
        }
    }

    /**
     * 发送验证码
     *
     * @param telephone
     * @return
     */
    public ResultDto sendVerifyCode(String telephone, Integer type) {

        String code = RandomStringUtils.randomNumeric(Constant.FOUR_CODE);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);

        switch (type) {
            case 1://登录
                ResultDto isExist = detectPhoneNumIsExist(telephone);
                if (isExist.isSuccess()) {
                    jsonObject.put("product", "美食广场");
                    JSONObject result = SMSUtils.sendMessage(telephone, jsonObject, SMSUtils.SIGN, SMSUtils.CODE_SMS_TEMP);
                    if (result.get("success").toString().equals("true")) {
                        redisService.set(Constant.CODE_LOGIN + telephone, code, Constant.EXPIRE_TIME);
                    } else {
                        return ResultDto.getError("短信发送失败");
                    }
                } else {
                    return isExist;
                }
                break;
            case 2://开卡
                ResultDto isLegal = detectPhoneNum(telephone);

                if (customService.phoneNumIsExist(telephone)) {
                    return ResultDto.getError("该手机号已经开过卡");
                }

                if (isLegal.isSuccess()) {
                    jsonObject.put("product", "美食广场");
                    JSONObject result = SMSUtils.sendMessage(telephone, jsonObject, SMSUtils.SIGN, SMSUtils.CODE_SMS_TEMP);
                    if (result.get("success").toString().equals("true")) {
                        redisService.set(Constant.CODE_OPENCARD + telephone, code, Constant.EXPIRE_TIME);
                    } else {
                        return ResultDto.getError("短信发送失败");
                    }
                } else {
                    return isLegal;
                }
                break;
            default:
                return ResultDto.getError("type参数错误");
        }
        log.info("短信验证码发送成功，手机号：" + telephone + " 验证码：" + code);
        return ResultDto.getSuccess();
    }

    /**
     * 登录
     *
     * @param telephone
     * @param code
     * @return
     */
    public ResultDto login(String telephone,String password, String code) {

        try {
            //测试登录账号
            if(telephone.equals("13147163660")&&code.equals("3630")){
                return ResultDto.getSuccess("登录成功");
            }
            if(code!=null&&code!=""){
                /*if (StringUtils.isBlank(code)) {
                    throw new IllegalArgumentException("验证码不能为空");
                }*/
                ResultDto isExist = detectPhoneNumIsExist(telephone);
                if (isExist.isSuccess()) {
                    String redisCode = redisService.get(Constant.CODE_LOGIN + telephone);
                    if (StringUtils.isBlank(redisCode)) {
                        throw new IllegalArgumentException("验证码已失效");
                    }
                    if (!redisCode.equals(code)) {
                        throw new IllegalArgumentException("验证码输入不正确");
                    }
                    return ResultDto.getSuccess("登录成功");
                } else {
                    return isExist;
                }
            }else{
                TbCardLoginUser  cardLoginUser = loginUserMapper.findUserByTelephone(telephone);
                if(cardLoginUser!=null){
                    if(password.equals(cardLoginUser.getPassword())){
                        ResultDto isExist = detectPhoneNumIsExist(telephone);
                        if (isExist.isSuccess()) {
                            return ResultDto.getSuccess("登录成功");
                        }else {
                            return isExist;
                        }
                    }
                }
                return ResultDto.getError("该用户不存在");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultDto.getError(e.getMessage());
        }
    }
}
