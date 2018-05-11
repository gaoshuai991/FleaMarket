package com.fleamarket.core.controller;

import com.fleamarket.core.model.Treasure;
import com.fleamarket.core.service.TreasureService;
import com.fleamarket.core.service.UserService;
import com.fleamarket.core.shiro.Identity;
import com.fleamarket.core.shiro.token.CustomToken;
import com.fleamarket.core.util.Utils;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@Log4j2
public class UserController {
    private final TreasureService treasureService;
    private final UserService userService;

    @Autowired
    public UserController(TreasureService treasureService, UserService userService) {
        this.treasureService = treasureService;
        this.userService = userService;
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

    @GetMapping("user_center")
    public String userCenter(HttpServletRequest request) {
        request.setAttribute("treasures", treasureService.selectTreasureByUid(Utils.getUserSession(request.getSession()).getId()));
        return "user/user_center";
    }

    @GetMapping("treasure_picture/{treasureId}")
    @ResponseBody
    public Map<String, Object> userCenter(@PathVariable Integer treasureId) {
        return new HashMap<String, Object>(){{
            put("mainPicture", treasureService.selectByPrimaryKey(treasureId).getPicture());
            put("pictures", treasureService.selectAllTreasurePicture(treasureId));
        }};
    }

}
