package com.fleamarket.core.controller;

import com.fleamarket.core.service.CategoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jackiegao on 2018/4/10.
 */
@Controller
@Log4j2
public class IndexController {
    private final CategoryService categoryService;

    @Autowired
    public IndexController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("index")
    public String index(HttpServletRequest request){
        request.setAttribute("categories", categoryService.getAllCategoryGraded());
        return "index";
    }

    @GetMapping("user/treasure")
    public String publishTreasure(){
        return "user/treasure_publish";
    }
}
