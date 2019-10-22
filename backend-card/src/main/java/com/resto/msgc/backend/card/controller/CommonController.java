package com.resto.msgc.backend.card.controller;

import com.resto.msgc.backend.card.util.Constant;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by bruce on 2017/12/9.
 */
public class CommonController {

    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    private HttpServletResponse getResponse() {
        return ((ServletWebRequest) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    public String getSessionCurrentUser(){
        HttpSession session = getRequest().getSession();
        return (String) session.getAttribute(Constant.SESSION_USER);
    }

    public void setSessionCurrentUser(String value) {
        HttpSession session = getRequest().getSession();
        session.setAttribute(Constant.SESSION_USER, value);
    }

    public void removeSessionCurrentUser() {
        HttpSession session = getRequest().getSession();
        session.removeAttribute(Constant.SESSION_USER);
    }

}
