package com.fleamarket.core.controller;

import com.fleamarket.core.service.CategoryService;
import com.fleamarket.core.shiro.Identity;
import com.fleamarket.core.shiro.token.CustomToken;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jackiegao on 2018/4/10.
 */
@Controller
@Log4j2
public class IndexController {
    private final CategoryService categoryService;

    @Autowired
    public IndexController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("index")
    public String index(HttpServletRequest request){
        request.setAttribute("categories", categoryService.getAllCategoryGraded());
        return "index";
    }
    @GetMapping("login")
    public String login(){
        return "login";
    }

    @PostMapping("login")
    public String login(String principal, String password, Boolean rememberMe, RedirectAttributes redirectAttributes) {
        try {
            CustomToken customToken = new CustomToken(principal, password, rememberMe, Identity.USER);
            SecurityUtils.getSubject().login(customToken);
            if(SecurityUtils.getSubject().isAuthenticated())
                return "redirect:index";
        } catch (UnknownAccountException uae) {
            log.debug("对用户[" + principal + "]登录验证未通过,未知账户");
            redirectAttributes.addAttribute("message", "用户名不存在");
        } catch (IncorrectCredentialsException ice) {
            log.debug("对用户[" + principal + "]登录验证未通过,错误的凭证");
            redirectAttributes.addAttribute("message", "密码不Ø正确");
        } catch (LockedAccountException ule) {
            log.debug("对用户[" + principal + "]登录验证未通过,用户被锁定");
            redirectAttributes.addAttribute("message", "用户被锁定");
        }
        return "redirect:login";
    }
}
