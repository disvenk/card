package com.resto.msgc.backend.card.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * title
 * company resto+
 * author jimmy 2017/12/13 下午3:26
 * version 1.0
 */
public class CookieUtil {
    /**
     * 获取cookie内容根据名称
     *
     * @param request
     * @param name
     * @return
     */
    public static String getCookieByName(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            String value = cookie.getValue();
            if (name.equals(cookie.getName())) {
                return value;
            }
        }
        return null;
    }

    /**
     * 设置cookie内容根据名称
     * @param response
     * @param name
     * @param value
     */
    public static void setCookieByName(HttpServletResponse response, String name, String value){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
