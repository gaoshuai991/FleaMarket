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
import java.util.Date;
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
    public String register(){
        return "register";
    }
    @PostMapping("register")
    public String register(String principal,String password0){
            User user = new User();
            user.setPassword(new Md5Hash(password0, principal, 2).toString());
            user.setUsername(principal);
            user.setCreateTime(new Date());
            userService.addUser(user);
            return "redirect:login";
    }
    @ResponseBody
    @PostMapping("exist")
    public Object exist(String principal){
        Map<String,Object> map=new HashMap<String,Object>();
        if(userService.selectByPrincipal(principal)!=null){
            map.put("status",1);
            return map;
        }else{
            map.put("status",2);
            return map;
        }
    }
    @GetMapping("myAccount")
    public String myAccount(){
        return "page_temp/my_account";
    }
    @GetMapping("index")
    public String index(HttpServletRequest request){
        request.setAttribute("categories", categoryService.getAllCategoryGraded());
        return "index";
    }

}
