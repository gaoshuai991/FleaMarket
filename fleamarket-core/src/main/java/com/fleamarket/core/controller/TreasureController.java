package com.fleamarket.core.controller;

import com.fleamarket.core.model.Treasure;
import com.fleamarket.core.model.TreasureView;
import com.fleamarket.core.model.User;
import com.fleamarket.core.service.*;
import com.fleamarket.core.util.Utils;
import com.github.pagehelper.PageHelper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Controller
@Log4j2
public class TreasureController {
    private final TreasureService treasureService;
    private final TreasurePictureService treasurePictureService;
    private final CategoryService categoryService;
    private final TreasureStarService treasureStarService;
    private final TreasureViewService treasureViewService;

    @Autowired
    public TreasureController(TreasureService treasureService, TreasurePictureService treasurePictureService, CategoryService categoryService, TreasureStarService treasureStarService, TreasureViewService treasureViewService) {
        this.treasureService = treasureService;
        this.treasurePictureService = treasurePictureService;
        this.categoryService = categoryService;
        this.treasureStarService = treasureStarService;
        this.treasureViewService = treasureViewService;
    }
    @GetMapping("shop/{sub_category.id}")
    public String shop(@PathVariable("sub_category.id") Integer subCategoryId, HttpServletRequest request){
        request.setAttribute("treasures",treasureService.selectByCategory(subCategoryId));
        return "shop";
    }
    @GetMapping("treasure/{treasureId}")
    @Transactional(rollbackFor = RuntimeException.class)
    public String treasure(@PathVariable Integer treasureId, HttpServletRequest request) {
        Treasure treasure = treasureService.selectByPrimaryKey(treasureId);
        char[] tradingMethods = treasure.getTradingMethod().toCharArray();
        request.setAttribute("treasure", treasure);
        request.setAttribute("categoryName", categoryService.selectByPrimaryKey(treasure.getCategory()).getName());
        request.setAttribute("pickUp", tradingMethods[0] == '1');
        request.setAttribute("faceGay", tradingMethods[1] == '1');
        request.setAttribute("postMan", tradingMethods[2] == '1');
        request.setAttribute("pictures", treasurePictureService.selectAllTreasurePicture(treasureId));
        User user = Utils.getUserSession(request.getSession());
        if (user != null) {
            treasureViewService.treasureView(user.getId(), treasureId);
            request.setAttribute("viewList",treasureService.selectViewList(user.getId()));
            request.setAttribute("isStar", treasureStarService.isStar(user.getId(), treasure.getId()));
            request.setAttribute("isMine", treasure.getUid().equals(user.getId()));
        }
        return "treasure";
    }
}
