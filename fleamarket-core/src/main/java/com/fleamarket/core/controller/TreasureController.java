package com.fleamarket.core.controller;

import com.fleamarket.core.model.Treasure;
import com.fleamarket.core.service.TreasureService;
import com.fleamarket.core.shiro.Identity;
import com.fleamarket.core.shiro.token.CustomToken;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@Log4j2
public class TreasureController {
    private final TreasureService treasureService;

    @Autowired
    public TreasureController(TreasureService treasureService) {
        this.treasureService = treasureService;
    }

    @GetMapping("treasure/{treasureId}")
    public String treasure(@PathVariable Integer treasureId, HttpServletRequest request) {
        Treasure treasure = treasureService.selectByPrimaryKey(treasureId);
        String[] tradingMethods = treasure.getTradingMethod().split("");
        request.setAttribute("treasure", treasure);
        request.setAttribute("pickUp", "1".equals(tradingMethods[0]));
        request.setAttribute("faceGay", "1".equals(tradingMethods[1]));
        request.setAttribute("postMan", "1".equals(tradingMethods[2]));
        request.setAttribute("pictures", treasureService.selectAllTreasurePicture(treasureId));
        return "treasure";
    }
}
