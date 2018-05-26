package com.fleamarket.core.service;

import com.fleamarket.core.model.Order;

import java.util.List;

/**
 * Created by jackiegao on 2018/4/10.
 */
public interface OrderService extends IService<Order> {
    Boolean addOrder(Order order);
    List<Order> selectByUserId(Integer userId);

    List<Order> selectByKeyword(String column, String keyword);

    List<Order> selectSales(Integer userId);
}
