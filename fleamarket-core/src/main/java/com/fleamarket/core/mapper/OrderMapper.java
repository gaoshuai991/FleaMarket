package com.fleamarket.core.mapper;

import com.fleamarket.core.IMapper;
import com.fleamarket.core.model.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper extends IMapper<Order> {
    Boolean addOrder(Order order);


    List<Order> selectByUserId(Integer userId);
}