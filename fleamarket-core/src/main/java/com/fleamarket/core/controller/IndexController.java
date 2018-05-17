package com.fleamarket.core.controller;

import com.fleamarket.core.model.Treasure;
import com.fleamarket.core.service.CategoryService;
import com.fleamarket.core.service.TreasureService;
import com.fleamarket.core.service.UserService;
import com.fleamarket.core.shiro.Identity;
import com.fleamarket.core.shiro.token.CustomToken;
import com.fleamarket.core.util.Constant;
import com.fleamarket.core.util.Utils;
import com.github.pagehelper.PageHelper;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.fleamarket.core.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jackiegao on 2018/4/10.
 */
@Controller
@Log4j2
public class IndexController {
    private final CategoryService categoryService;
    private final TreasureService treasureService;
    private final UserService userService;

    @Autowired
    public IndexController(CategoryService categoryService, TreasureService treasureService, UserService userService) {
        this.categoryService = categoryService;
        this.treasureService = treasureService;
        this.userService = userService;
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
        user.setPhoto(Constant.MALE_PHOTO_DEFAULT);
        userService.insertSelective(user);
        return "redirect:login";
    }

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("logout")
    public String logout(HttpSession session) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout(); // session 会销毁，在SessionListener监听session销毁，清理权限缓存
            String username = Utils.getUserSession(session).getUsername();
            log.debug("用户" + username + "退出登录");
            session.invalidate();
        }
        return "login";
    }

    @PostMapping("login")
    public String login(String to, String principal, String password, HttpSession session, RedirectAttributes redirectAttributes) {
        log.debug("登录后重定向地址：" + to);
        try {
            CustomToken customToken = new CustomToken(principal, password, Identity.USER);
            SecurityUtils.getSubject().login(customToken);
            if (SecurityUtils.getSubject().isAuthenticated()) {
                session.setAttribute("user", userService.selectByPrincipal(principal));
                if (to == null || "null".equals(to)) {
                    return "redirect:index";
                } else {
                    return "redirect:" + to;
                }
            }
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

    @ResponseBody
    @PostMapping("exist")
    public Object exist(String principal) {
        return new HashMap<String, Object>() {{
            put("status", userService.selectByPrincipal(principal) != null ? 1 : 2);
        }};
    }
    @PostMapping("changepassword")
    public String changePassword(Integer id,String newpwd){
        User user = userService.selectByPrimaryKey(id);
        user.setPassword(new Md5Hash(newpwd, user.getUsername(), 2).toString());
        userService.updateByPrimaryKey(user);
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "login";
    }
    @GetMapping({"", "index"})
    public String index(HttpServletRequest request) {
        PageHelper.startPage(0, 6, "create_time desc");
        List<Treasure> treasures = treasureService.selectAll();

        request.setAttribute("lista", treasures.subList(0, 2));
        request.setAttribute("listb", treasures.subList(2, 4));
        request.setAttribute("listc", treasures.subList(4, 6));

        PageHelper.startPage(0, 6, "view_count desc");
        request.setAttribute("hotList", treasureService.selectAll());
        request.setAttribute("categories", categoryService.getAllCategoryGraded());
        return "index";
    }

}
