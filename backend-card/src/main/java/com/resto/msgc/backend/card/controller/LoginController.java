package com.resto.msgc.backend.card.controller;

import com.resto.msgc.backend.card.dto.UserLoginDto;
import com.resto.msgc.backend.card.service.LoginUserService;
import com.resto.msgc.backend.card.service.NewLoginService;
import com.resto.msgc.backend.card.util.Constant;
import com.resto.msgc.backend.card.util.CookieUtil;
import com.resto.msgc.backend.card.util.ResultDto;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by bruce on 2017/12/6.
 */
@Api
@Controller
@RequestMapping("")
public class LoginController extends CommonController {

    private Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginUserService loginUserService;

    @Autowired
    private NewLoginService newLoginService;

    @RequestMapping("index")
    public String index() {
        log.info("LoginController.index");
        return "index";
    }

    @GetMapping("login")
    public String login() {
        return "login";
    }


    @PostMapping("login")
    @ResponseBody
    public ResultDto login(@RequestBody UserLoginDto userLoginDto) {
        log.info("LoginController.login");
        ResultDto resultDto = newLoginService.login(userLoginDto.getTelephone(),userLoginDto.getPassword(),userLoginDto.getCode());
        if (resultDto.isSuccess()) {
            setSessionCurrentUser(userLoginDto.getTelephone());
            ResultDto.getError("登录失败");
        }
        return resultDto;
    }

    @RequestMapping("logout")
    public String logout() {
        log.info("LoginController.logout");
        this.removeSessionCurrentUser();
        return "login";
    }

    @GetMapping("sendCode/{type}/{telephone}")
    @ResponseBody
    public ResultDto sendVerifyCode(@PathVariable("type") Integer type, @PathVariable("telephone") String telephone) {
        log.info("LoginController.sendVerifyCode");
        return loginUserService.sendVerifyCode(telephone, type);
    }

}
