package com.fleamarket.core.controller;
import com.fleamarket.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



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
}
