package com.fleamarket.core.service.impl;

import com.fleamarket.core.IMapper;
import com.fleamarket.core.mapper.CategoryMapper;
import com.fleamarket.core.model.Category;
import com.fleamarket.core.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by jackiegao on 2018/4/10.
 */
@Service
public class CategoryServiceImpl extends BaseService<Category> implements CategoryService {
    @Autowired
    private CategoryMapper mapper;
    @Override
    protected IMapper<Category> getMapper() {
        return mapper;
    }

    @Override
    public Map<Category, List<Category>> getAllCategoryGraded() {
        Map<Category, List<Category>> result = new LinkedHashMap<>();
        List<Category> categories = selectAll();
        categories.forEach(category -> {
            if(category.getPid() == null)
                result.put(category, new ArrayList<>());
        });
        categories.forEach(category -> {
            if(category.getPid() != null){
                result.forEach((key, value) -> {
                    if (category.getPid().equals(key.getId()))
                        value.add(category);
                });
            }
        });
        return result;
    }
}
