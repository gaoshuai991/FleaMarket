package com.fleamarket.core.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fleamarket.core.model.Admin;
import com.fleamarket.core.model.Order;
import com.fleamarket.core.model.Treasure;
import com.fleamarket.core.model.User;
import com.fleamarket.core.service.*;
import com.fleamarket.core.shiro.Identity;
import com.fleamarket.core.shiro.token.CustomToken;
import com.fleamarket.core.util.ShiroHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Log4j2
@Controller
@RequestMapping("admin")
public class AdminController {
    private final AdminService adminService;
    private final UserService userService;
    private final TreasureService treasureService;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @Autowired
    public AdminController(AdminService adminService, UserService userService, TreasureService treasureService, CategoryService categoryService, OrderService orderService) {
        this.adminService = adminService;
        this.userService = userService;
        this.treasureService = treasureService;
        this.categoryService = categoryService;
        this.orderService = orderService;
        objectMapper = new ObjectMapper();
    }

    @GetMapping({"", "index"})
    public String index() {
        return "back/index";
    }

    @GetMapping("login")
    public String login() {
        return "back/login";
    }

    @GetMapping("logout")
    public String logout() {
        SecurityUtils.getSubject().logout();
        SecurityUtils.getSubject().getSession().removeAttribute("admin");
        return "redirect:login";
    }

    @PostMapping("login")
    public String login(String username, String password, HttpSession session, RedirectAttributes redirectAttributes) {
        ShiroHelper.flushSession();
        CustomToken token = new CustomToken(username, password, Identity.ADMIN);
        try {
            SecurityUtils.getSubject().login(token);
            if (SecurityUtils.getSubject().isAuthenticated()) {
                Admin admin = new Admin();
                admin.setUsername(token.getUsername());
                admin = adminService.selectOne(admin);
                admin.setLastLogin(new Date());
                adminService.updateByPrimaryKey(admin);
                session.setAttribute("admin", admin);
                return "redirect:index";
            } else {
                return "redirect:login";
            }
        } catch (UnknownAccountException uae) {
            redirectAttributes.addAttribute("message", "用户名不存在");
        } catch (IncorrectCredentialsException ice) {
            redirectAttributes.addAttribute("message", "密码不正确");
        }
        return "redirect:login";
    }


    @GetMapping("right")
    public String right() {
        return "back/common/right";
    }

    @GetMapping("user/list")
    public String userList() {
        return "back/user/user_list";
    }

    /**
     * 获取用户列表
     *
     * @return
     */
    @GetMapping("user/basic")
    @ResponseBody
    public PageInfo<User> userBasicList(Page<User> page, @RequestParam String column, @RequestParam String keyword) {
        log.debug("接收参数：pageNum=" + page.getPageNum() + ",pageSize=" + page.getPageSize() + ",column=" + column + ",keyword=" + keyword);
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<User> basics = userService.selectByKeyword(column, "%" + keyword + "%");
        PageInfo<User> pageInfo = new PageInfo<>(basics);
        try {
            log.debug(objectMapper.writeValueAsString(pageInfo));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return pageInfo;
    }

    /**
     * 锁定用户
     *
     * @param id
     * @param user
     * @return
     */
    @PutMapping("user/basic/{id}")
    @ResponseBody
    public boolean updateUser(@PathVariable int id, User user) {
        user.setId(id);
        return userService.updateByPrimaryKeySelective(user);
    }


    @GetMapping("treasure/list")
    public String treasureList(HttpServletRequest request) {
        request.setAttribute("categories", categoryService.selectAllChildren());
        return "back/treasure/treasure_list";
    }

    /**
     * 获取商品列表
     *
     * @return
     */
    @GetMapping("treasures")
    @ResponseBody
    public PageInfo<Treasure> treasureList(Page<Treasure> page, @RequestParam Integer status, @RequestParam Integer categoryId, @RequestParam String column, @RequestParam String keyword) {
        if (categoryId == 0) {
            categoryId = null;
        }
        log.debug("接收参数：pageNum=" + page.getPageNum() + ",status=" + status + ",pageSize=" + page.getPageSize() + ",column=" + column + ",keyword=" + keyword);
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<Treasure> treasures = treasureService.selectByStatusAndKeyword(status, categoryId, column, "%" + keyword + "%");
        PageInfo<Treasure> pageInfo = new PageInfo<>(treasures);
        try {
            log.debug(objectMapper.writeValueAsString(pageInfo));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return pageInfo;
    }

    @GetMapping("order/list")
    public String orderList() {
        return "back/order/order_list";
    }

    @GetMapping("orders")
    @ResponseBody
    public PageInfo<Order> orderList(Page<Order> page, @RequestParam String column, @RequestParam String keyword) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<Order> orders = orderService.selectByKeyword(column, "%" + keyword + "%");
        PageInfo<Order> pageInfo = new PageInfo<>(orders);
        try {
            log.debug(objectMapper.writeValueAsString(pageInfo));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return pageInfo;
    }
}
