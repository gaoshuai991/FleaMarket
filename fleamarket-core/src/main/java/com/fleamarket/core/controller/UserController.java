package com.fleamarket.core.controller;

import com.fleamarket.core.model.Category;
import com.fleamarket.core.model.Treasure;
import com.fleamarket.core.service.CategoryService;
import com.fleamarket.core.service.TreasureService;
import com.fleamarket.core.service.UploadService;
import com.fleamarket.core.shiro.Identity;
import com.fleamarket.core.shiro.token.CustomToken;
import com.fleamarket.core.util.Constant;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

@Controller
@Log4j2
@RequestMapping("user")
public class UserController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TreasureService treasureService;
    @Autowired
    private UploadService uploadService;
    @GetMapping("treasure")
    public String publishTreasure(HttpServletRequest request){
        request.setAttribute("newDegrees", Constant.NEW_DEGREE_LIST);
        request.setAttribute("categories", categoryService.selectAllChildren());
        return "user/treasure_publish";
    }
    @GetMapping("success")
    public String publishSuccess(){
        return "user/publish_success";
    }
    @PostMapping("treasure")
    public String publishTreasure(Treasure treasure, MultipartFile photo, HttpServletRequest request){
        String pickUp = request.getParameter("pickUp");
        String faceGay = request.getParameter("faceGay");
        String postMan = request.getParameter("postMan");
        String tradingMethod = (pickUp == null ? "0" : pickUp) + (faceGay == null ? "0" : faceGay) + (postMan == null ? "0" : postMan);
        treasure.setTradingMethod(tradingMethod);
        treasure.setTotal(treasure.getPrice() + treasure.getFare());
        treasure.setUid((Integer) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal());
        try {
            String fileName;
            if ((fileName = uploadService.uploadFile(photo)) != null) {
                treasure.setPicture(fileName);
                if(treasureService.treasurePublish(treasure)){
                    return "user/publish_success"; // TODO
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return "user/treasure_publish";
    }

}
