package com.fleamarket.core.service;

import com.fleamarket.core.model.Category;

import java.util.List;
import java.util.Map;

/**
 * Created by jackiegao on 2018/4/10.
 */
public interface CategoryService extends IService<Category> {
    /**
     * 获取所有商品分类信息，并分级，Key：分类，Value：子分类
     * @return
     */
    Map<Category,List<Category>> getAllCategoryGraded();
}
