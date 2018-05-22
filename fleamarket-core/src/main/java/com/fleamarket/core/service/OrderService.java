package com.fleamarket.core.service;

import com.fleamarket.core.model.Order;

import java.util.List;
import java.util.Map;

/**
 * Created by jackiegao on 2018/4/10.
 */
public interface OrderService extends IService<Order> {
    Boolean addOrder(Order order);
    Map<String,Order> selectByUserId(Integer userId);

    List<Order> selectByKeyword(String column, String keyword);
}
