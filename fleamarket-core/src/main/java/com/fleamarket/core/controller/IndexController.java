package com.fleamarket.core.controller;

import com.fleamarket.core.service.CategoryService;
import com.fleamarket.core.service.UserService;
import com.fleamarket.core.shiro.Identity;
import com.fleamarket.core.shiro.token.CustomToken;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.fleamarket.core.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by jackiegao on 2018/4/10.
 */
@Controller
@Log4j2
public class IndexController {
    private final CategoryService categoryService;
    @Autowired
    private UserService userService;

    @Autowired
    public IndexController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("register")
    public String register() {
        return "register";
    }

    @PostMapping("register")
    public String register(String principal, String password0) {
        User user = new User();
        user.setPassword(new Md5Hash(password0, principal, 2).toString());
        user.setUsername(principal);
        user.setCreateTime(new Date(System.currentTimeMillis()));
        userService.addUser(user);
        return "redirect:login";
    }
    @GetMapping("login")
    public String login(){
        return "login";
    }


    @PostMapping("login")
    public String login(String principal, String password, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            CustomToken customToken = new CustomToken(principal, password,Identity.USER);
            SecurityUtils.getSubject().login(customToken);
            if(SecurityUtils.getSubject().isAuthenticated()) {
                session.setAttribute("user", userService.selectByPrincipal(principal));
                return "redirect:index";
            }
        } catch (UnknownAccountException uae){
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

    @ResponseBody
    @PostMapping("exist")
    public Object exist(String principal) {
        return new HashMap<String, Object>(){{
            put("status", userService.selectByPrincipal(principal) != null ? 1 : 2);
        }};
    }

    @GetMapping({"", "index"})
    public String index(HttpServletRequest request) {
        request.setAttribute("categories", categoryService.getAllCategoryGraded());
        return "index";
    }

}
