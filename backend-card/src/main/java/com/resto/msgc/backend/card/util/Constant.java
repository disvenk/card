package com.resto.msgc.backend.card.util;

/**
 * title
 * company resto+
 * author jimmy 2017/12/6 下午3:09
 * version 1.0
 */
public class Constant {
    public static final String API = "/msgc-backend-card/";

    //正确code码
    public static final int SUCCESS_CODE = 200;
    //错误code码
    public static final int ERROR_CODE = 500;
    //返回正确的success
    public static final boolean TRUE = true;
    //返回错误的success
    public static final boolean FALSE = false;
    //返回正确的message
    public static final String MESSAGE_OK = "ok";

    //手机号校验正则表达式
    public static final String PHONE_RG = "^((13[\\d])|(15[0-35-9])|(18[\\d])|(145)|(147)|(17[0135678]))\\d{8}$";

    //常量 4位数验证码
    public static int FOUR_CODE = 4;
    //常量 验证码失效时间1小时
    public static long EXPIRE_TIME = 2 * 60 * 60;
    //美食广场登录验证码key
    public static String CODE_LOGIN = "foodmember:code:login:";
    //美食广场开卡验证码key
    public static String CODE_OPENCARD = "foodmember:code:openCard:";

    public static final String COOKIE_BRANDID = "brandId";
    public static final String SESSION_USER = "user";

}
