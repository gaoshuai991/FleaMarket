package com.fleamarket.core.controller;

import com.fleamarket.core.model.Treasure;
import com.fleamarket.core.service.TreasurePictureService;
import com.fleamarket.core.service.TreasureService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
@Log4j2
public class TreasureController {
    private final TreasureService treasureService;
    private final TreasurePictureService treasurePictureService;

    @Autowired
    public TreasureController(TreasureService treasureService, TreasurePictureService treasurePictureService) {
        this.treasureService = treasureService;
        this.treasurePictureService = treasurePictureService;
    }

    @GetMapping("treasure/{treasureId}")
    public String treasure(@PathVariable Integer treasureId, HttpServletRequest request) {
        Treasure treasure = treasureService.selectByPrimaryKey(treasureId);
        char[] tradingMethods = treasure.getTradingMethod().toCharArray();
        request.setAttribute("treasure", treasure);
        request.setAttribute("pickUp", tradingMethods[0] == '1');
        request.setAttribute("faceGay", tradingMethods[1] == '1');
        request.setAttribute("postMan", tradingMethods[2] == '1');
        request.setAttribute("pictures", treasurePictureService.selectAllTreasurePicture(treasureId));
        return "treasure";
    }
}
