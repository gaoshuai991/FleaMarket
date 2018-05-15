package com.fleamarket.core.controller;
import com.fleamarket.core.model.User;
import com.fleamarket.core.service.UserService;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;



@Controller
public class AccountController {
    @Autowired
    private UserService userService;
    @GetMapping("details")
    public String details(){
        return "user/account_details";
    }
    @GetMapping("addresses")
    public String addresses(){
        return "user/addresses";
    }
    @GetMapping("orders")
    public String orders(){
        return "user/orders";
    }
    @GetMapping("checkout")
    public String checkout(){
        return "user/checkout";
    }
    @GetMapping("getAccount")
    public Object getAccount(){
        Subject subject= SecurityUtils.getSubject();
        Session session=subject.getSession();
        String username=(String)session.getAttribute("username");
        User user=userService.selectByPrincipal(username);
        return user;
    }
}
