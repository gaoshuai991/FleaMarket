package com.fleamarket.core.controller;

import com.fleamarket.core.shiro.Identity;
import com.fleamarket.core.shiro.token.CustomToken;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Log4j2
public class UserController {
    @GetMapping("login")
    public String login(){
        return "login";
    }

    @PostMapping("login")
    public String login(String username, String password, Boolean rememberMe) {
        log.debug("Username: "+username+",Password: "+password);
        SecurityUtils.getSubject().login(new CustomToken(username,password,Identity.USER));
        log.debug("登录结果: {}",SecurityUtils.getSubject().isAuthenticated());
        return null;
    }
}
