package com.fleamarket.core.controller;

import com.fleamarket.core.model.Order;
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
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Controller
@Log4j2
public class TreasureController {
    private final TreasureService treasureService;

    private final OrderService orderService;
    private final TreasurePictureService treasurePictureService;
    private final CategoryService categoryService;
    private final TreasureStarService treasureStarService;
    private final TreasureViewService treasureViewService;

    @Autowired
    public TreasureController(OrderService orderService,TreasureService treasureService, TreasurePictureService treasurePictureService, CategoryService categoryService, TreasureStarService treasureStarService, TreasureViewService treasureViewService) {
        this.treasureService = treasureService;
        this.treasurePictureService = treasurePictureService;
        this.categoryService = categoryService;
        this.treasureStarService = treasureStarService;
        this.treasureViewService = treasureViewService;
        this.orderService=orderService;
    }
    @GetMapping("shop/{tid}")
    public String order(@PathVariable("tid") Integer tid,HttpServletRequest request){
        request.setAttribute("treasure",treasureService.selectByPrimaryKey(tid));
        return "user/checkout";
    }
    @PostMapping("placeorder")
    public String placeOrder(Order order,String work_province,String work_city,String address, HttpServletRequest request){
        String str=work_province+"-"+work_city+" "+address;
        order.setAddress(str);
        User user = Utils.getUserSession(request.getSession());
        order.setUserId(user.getId());
        order.setStatus(1);
        request.setAttribute("categories", categoryService.getAllCategoryGraded());
        orderService.addOrder(order);
        return "index";
    }
    @GetMapping("shop/{sub_category.id}/{orderbycause}")
    public String shop(@PathVariable("sub_category.id") Integer subCategoryId, HttpServletRequest request,@PathVariable("orderbycause") String orderbycause){
        PageHelper.startPage(0,6,orderbycause+" desc");
        Treasure t=new Treasure();
        t.setCategory(subCategoryId);
        t.setStatus(1);
        request.setAttribute("treasures",treasureService.selectList(t));
        //在商品页面排序时使用
        request.setAttribute("category",subCategoryId);
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
