package com.fleamarket.core.controller;

import com.fleamarket.core.model.Order;
import com.fleamarket.core.model.Treasure;
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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    public TreasureController(OrderService orderService, TreasureService treasureService, TreasurePictureService treasurePictureService, CategoryService categoryService, TreasureStarService treasureStarService, TreasureViewService treasureViewService) {
        this.treasureService = treasureService;
        this.treasurePictureService = treasurePictureService;
        this.categoryService = categoryService;
        this.treasureStarService = treasureStarService;
        this.treasureViewService = treasureViewService;
        this.orderService = orderService;
    }

    @GetMapping("search")
    public String search(String keyword, String orderBy,HttpServletRequest request) {
        if (orderBy == null) {
            orderBy = "create_time";
        }
        PageHelper.startPage(0, 15, orderBy + " desc");
        request.setAttribute("orderBy", "create_time".equals(orderBy) ? "时间" : "热度");
        List<Treasure> treasures = treasureService.selectByStatusAndKeyword(1,null,"title","%" + keyword + "%");
        request.setAttribute("treasures",treasures);
        request.setAttribute("searchKeyword",keyword);
        return "search";
    }

    @GetMapping("user/order/{tid}")
    public String order(@PathVariable("tid") Integer tid, HttpServletRequest request) {
        request.setAttribute("treasure", treasureService.selectByPrimaryKey(tid));
        return "user/checkout";
    }

    @PostMapping("user/order/logistics")
    @ResponseBody
    public boolean logistics(Order order) {
        order.setStatus(2); // 2代表已发货
        return orderService.updateByPrimaryKeySelective(order);
    }
    @PostMapping("user/order/confirm")
    @ResponseBody
    public boolean confirm(Order order) {
        order.setStatus(3); // 2代表交易成功
        return orderService.updateByPrimaryKeySelective(order);
    }

    @PostMapping("placeorder")
    public String placeOrder(Order order, String work_province, String work_city, String address, HttpServletRequest request) {
        String str = work_province + "-" + work_city + " " + address;
        order.setAddress(str);
        User user = Utils.getUserSession(request.getSession());
        order.setUserId(user.getId());
        order.setStatus(1);
        request.setAttribute("categories", categoryService.getAllCategoryGraded());
        orderService.addOrder(order);
        Treasure treasure = new Treasure();
        treasure.setId(order.getTreasureId());
        treasure.setStatus(2);
        treasureService.updateByPrimaryKeySelective(treasure);
        return "index";
    }

    @GetMapping("shop/{sub_category.id}")
    public String shop(@PathVariable("sub_category.id") Integer subCategoryId, HttpServletRequest request, String orderBy) {
        if (orderBy == null) {
            orderBy = "create_time";
        }
        PageHelper.startPage(0, 30, orderBy + " desc");
        request.setAttribute("orderBy", "create_time".equals(orderBy) ? "时间" : "热度");
        request.setAttribute("treasures", subCategoryId == 0 ? treasureService.selectAll() : treasureService.selectByCategoryId(subCategoryId));
        //在商品页面排序时使用
        request.setAttribute("category", categoryService.selectByPrimaryKey(subCategoryId));
        return "shop";
    }

    @GetMapping("treasure/{treasureId}")
    @Transactional(rollbackFor = RuntimeException.class)
    public String treasure(@PathVariable Integer treasureId, HttpServletRequest request) {
        Treasure treasure = treasureService.selectByPrimaryKey(treasureId);
        char[] tradingMethods = treasure.getTradingMethod().toCharArray();
        request.setAttribute("treasure", treasure);
        request.setAttribute("category", categoryService.selectByPrimaryKey(treasure.getCategory()));
        request.setAttribute("pickUp", tradingMethods[0] == '1');
        request.setAttribute("faceGay", tradingMethods[1] == '1');
        request.setAttribute("postMan", tradingMethods[2] == '1');
        request.setAttribute("pictures", treasurePictureService.selectAllTreasurePicture(treasureId));
        User user = Utils.getUserSession(request.getSession());
        if (user != null) {
            treasureViewService.treasureView(user.getId(), treasureId);
            request.setAttribute("viewList", treasureService.selectViewList(user.getId()));
            request.setAttribute("isStar", treasureStarService.isStar(user.getId(), treasure.getId()));
            request.setAttribute("isMine", treasure.getUid().equals(user.getId()));
        }
        return "treasure";
    }
}
